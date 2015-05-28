import java.util.*;



public final class World extends Globe { // a subclass of Globe to handle all political elements
  public ArrayList<Civi> civis;
  public java.applet.AudioClip boom;
  public java.applet.AudioClip blast;
  
  
  
  public World(int r) {
    super(r);
    loadSound();
    civis = new ArrayList<Civi>();
    
    while (civis.size() == 0) // skips ahead to the founding of the first civi
      for (Tile[] row: map)
        for (Tile til: row)
          spread(til);
  }
  
  
  public World(Globe g) {
    super(g);
    loadSound();
    civis = new ArrayList<Civi>();
    
    while (civis.size() == 0) // skips ahead to the founding of the first civi
      for (Tile[] row: map)
        for (Tile til: row)
          spread(til);
  }
  
  
  public World() { // FOR TESTING PURPOSES ONLY: DO NOT CALL
    super(1);
  }
  
  
  
  public final void update() { // updates the world a frame
    for (Tile[] row: map) { // for each tile in map
      for (Tile til: row) {
        
        naturallyDisast(til);
        
        collapse(til);
        
        develop(til);
        
        spread(til);
        
        fightWar(til);
        
        startWar(til);
        
        startRevolution(til);
      }
    }
    
    for (Tile[] row: map) { // asssigns all new values
      for (Tile til: row) {
        if (til.temp1 >= 0)
          civis.get(til.temp1).takes(til);
        
        if (til.temp2 >= 0)
          civis.get(til.temp2).failsToDefend(til);
        
        if (til.temp3 > 0 && til.owners.size() == 1)
          til.development ++;
        
        else if (til.temp3 < 0 && til.owners.size() == 1)
          til.owners.get(0).loseGraspOn(til);
      }
    }
    
    endWar();
    
    cleanse();
    
    for (Civi c: civis)
      c.advance();
  }
  
  
  public final void cleanse() { // removes dead civis from the game
    for (int i = civis.size()-1; i >= 0; i --) {
      final Civi civ = civis.get(i);
      
      if (civ.capital.development == 0) { // if is capital has been destroyed
        System.out.println(civ+" has fallen."); // announce the fall of the empire
        delete(civ);
      }
      
      else if (!civ.capital.owners.contains(civ)) { // if its capital has been captured
        final Civi winner = civ.capital.owners.get(0);
        if (civ.adversaries.size() > 0) // if it has no adversaries, it was a coup d'etat
          System.out.println(winner+" has captured "+civ.capitalName()+". "+civ+" has fallen.");
        give(civ, winner);
        delete(civ);
      }
    }
  }
  
  
  public final void spread(Tile til) { // causes civis to spawn and claim territory
    til.temp1 = -1; // temp1 is the index of til's new owner
    
    if (til.development > 0) // only run for unclaimed tiles
      return;
    
    final ArrayList<Tile> adjacent = adjacentTo(til);
    
    for (Tile adj: adjacent) { // for all adjacent tiles
      if (adj.owners.size() == 1 && adj.temp3 != -1) { // if that one is settled and this is not and that one is not succumbing to the apocalypse
        if (adj.owners.get(0).wants(til)) { // if they want this tile
          til.temp1 = civis.indexOf(adj.owners.get(0));
        }
      }
    }
    
    if (til.development == 0 && settlersLike(til)) // spawns new civis
      spawnCivi(til);
  }
  
  
  public final void develop(Tile til) { // causes tiles to be developed from territory to utopias
    if (til.temp3 != -1) // tiles that have succumbed to the apocalypse shall not be upgraded
      if (til.owners.size() == 1 && til.owners.get(0).canUpgrade(til)) // upgrades tiles
        til.temp3 = 1;
  }
  
  
  public final void collapse(Tile til) { // causes apocalypses
    til.temp3 = 0; // temp3 is whether it gets upgraded this turn
    if (til.owners.size() == 1 && til.owners.get(0).cannotSupport(til))
      til.temp3 = -1; // a temp3 of -1 means that tile decays this turn
  }
  
  
  public final void fightWar(Tile til) { // causes wars to work out
    til.temp2 = -1;
    if (til.development == 0 || til.owners.get(0).adversaries.size() == 0) // only run for tiles in civis at war
      return;
    
    final ArrayList<Tile> adjacentList = adjacentTo(til);
    
    if (til.owners.size() == 1) { // if it is not disputed
      for (Civi enemy: til.owners.get(0).adversaries)
        if (enemy.canNuke(til)) { // it might get nuked
          nuke(til);
          return;
        }
      
      if (til.temp1 > -2) { // -2 means it has already resolved another tile
        for (Tile adj: adjacentList) {
          if (adj.owners.size() > 1 && adj.owners.contains(til.owners.get(0))) { // if it is adjacent to a disputed tile
            for (Civi civ: adj.owners) {
              if (!civ.equals(til.owners.get(0))) {
                if (civ.canInvade(til)) {
                  til.temp1 = civis.indexOf(civ); // tell it to get taken by the civi
                  adj.temp2 = -2; // Tiles that have caused a Tile to be disputed may not be undisputed
                  break;
                }
              }
            }
          }
        }
      }
    }
    else { // if it is disputed
      if (til.temp2 > -2) {
        for (Tile adj: adjacentList) {
          if (adj.owners.size() == 1 && til.owners.contains(adj.owners.get(0))) {
            for (Civi civ: til.owners) {
              if (!civ.equals(adj.owners.get(0))) {
                if (civ.cannotDefend(til)) {
                  til.temp2 = civis.indexOf(civ); // tell it to get undisputed from the civi
                  adj.temp1 = -2; // Tiles that have caused a Tile to be resolved may not be resolved
                  break;
                }
              }
            }
          }
        }
      }
    }
  }
  
  
  public final void startWar(Tile til) { // causes Civis to wage war on others from tiles occasionally
    if (til.owners.size() == 1 && til.owners.get(0).wantsWar()) { // Tiles occasionally decide to wage war on neighbors
      final Tile adj = borderAt(til);
      if (adj.lat != -1) // if there is a border and these civis are not already at war
        wageWar(til.owners.get(0), adj.owners.get(0));
    }
  }
  
  
  public final void startRevolution(Tile start) { // causes Civis to simultaneously spawn inside an existing one and wage war on the parent Civi
    if (start.owners.size() != 1 || start.isCapital || start.altitude < 0) // revolutions cannot happen in unclaimed, disputed, ocean, or capital territory
      return;
    
    if (start.owners.get(0).hasDiscontent(start)) { // if a rebellion is going to happen
      final Civi empire = start.owners.get(0);
      Civi rebels;
      
      if (empire.sciLevel() >= Civi.space && !empire.getClass().getName().equals("Robots") && randChance(-10)) // advanced human Civilizations sometimes have robot rebellions
        rebels = new Robots(start, civis, this, empire);
      else
        rebels = new Civi(start, civis, this, empire); // but revolutions are usually human
      
      civis.add(rebels);
      
      for (int i = 0; i < 64; i ++) { // gives rebels a head start
        for (int j = 0; j < rebels.land.size(); j ++) {
          final Tile til = rebels.land.get(j);
          for (Tile adj: adjacentTo(til))
            if (adj.owners.size() == 1 && adj.owners.get(0).equals(empire)) // imperial land can be invaded by rebel land
              if (rebels.canInvade(adj))
                rebels.takes(adj);
        }
      }
      for (Tile t: rebels.land) { // some land is completely given to the rebels
        if (t.owners.contains(empire))
        empire.failsToDefend(t);
      }
      if (rebels.land.contains(empire.capital)) { // if the rebels have already taken the capital
        System.out.println(rebels+" have toppled "+empire+" in a coup d'Žtat!");
      }
      else {
        for (int i = 0; i < 128; i ++) { // give rebels more disputed territory if they have not won yet
          for (int j = 0; j < rebels.land.size(); j ++) {
            final Tile til = rebels.land.get(j);
            for (Tile adj: adjacentTo(til))
              if (adj.owners.size() == 1 && adj.owners.get(0).equals(empire)) // imperial land can be invaded by rebel land
                if (rebels.canInvade(adj))
                  rebels.takes(adj);
          }
        }
      }
      rebels.adversaries.add(empire); // civis know with whom they are at war
      empire.adversaries.add(rebels);
    }
  }
  
  
  public final void endWar() { // causes wars to end
    for (Civi civ: civis) {
      for (int i = civ.adversaries.size()-1; i >= 0; i --) { // for each war a civ is fighting
        final Civi adv = civ.adversaries.get(i);
        if (civ.isAtPeaceWith(adv)) // if no disputed territory is left
          civ.makePeaceWith(adv); // end all wars
        else if (civ.wantsSurrender())
          signPeaceTreaty(adv, civ);
      }
    }
  }
  
  
  public final void naturallyDisast(Tile til) { // causes natural disasters
  }
  
  
  public final void spawnCivi(Tile til) {
    if (randChance(-40))  civis.add(new CityState(til, civis, this)); // occasionally City-States spawn
    else                  civis.add(new Civi(til, civis, this)); // but usually, they are empires
  }
  
  
  public final void wageWar(Civi agg, Civi vic) { // starts a war between two civis at a Tile
    if (agg.adversaries.contains(vic)) // if they are already at war
      return;
    
    for (int i = 0; i < 64; i ++) { // calls a random region between the empires into dispute
      for (int j = 0; j < agg.land.size(); j ++) {
        final Tile til = agg.land.get(j);
        for (Tile adj: adjacentTo(til))
          if (adj.owners.size() == 1 && adj.owners.get(0).equals(vic)) // land owned only by the victim can be disputed by the aggressor
            if (adj.isWet() || agg.canInvade(adj)) // naval territory is disputed immediately
              agg.takes(adj);
      }
    }
    
    if (agg.isAtPeaceWith(vic)) // if no land was disputed, nothing happens
      return;
    
    System.out.println(agg+" has invaded "+vic+"!");
    agg.adversaries.add(vic); // civis know with whom they are at war
    vic.adversaries.add(agg);
  }
  
  
  public final void delete(Civi civ) { // removes a civi from the game
    for (Tile t: civ.land) {
      t.owners.remove(civ);
      if (t.owners.size() == 0) // tells all owned tiles that their nation is dead
        t.development = 0;
    }
    civ.capital.isCapital = false;
    for (Civi c: civis)
      c.adversaries.remove(civ);
    civis.remove(civ);
  }
  
  
  public final void signPeaceTreaty(Civi victor, Civi loser) { // causes all disputed territory to got to the victor
    for (int i = 0; i < victor.land.size(); i ++)
      if (victor.land.get(i).owners.contains(loser))
        loser.failsToDefend(victor.land.get(i));
    
    victor.adversaries.remove(loser);
    loser.adversaries.remove(victor);
    //System.out.println(loser+" has surrendered to "+victor+"!");
  }
  
  
  public final Tile borderAt(Tile til) { // decides if there is a international border here
    final ArrayList<Tile> adjacentList = adjacentTo(til);
    for (Tile adj: adjacentList)
      if (adj.owners.size() == 1 && !adj.owners.equals(til.owners))
        return adj;
    return new Tile(-1, -1);
  }
  
  
  public final void test() {
    civis.add(new Civi(map[0][0], civis, this));
    civis.add(new Civi(map[313][0], civis, this));
    wageWar(civis.get(0), civis.get(1));
    
    for (Tile[] row: map) {
      for (Tile til: row) {
        if (til.lat > 100)
          civis.get(0).takes(til);
        if (til.lat < 200)
          civis.get(1).takes(til);
      }
    }
  }
  
  
  public final boolean settlersLike(Tile til) { // determines whether to found a civi
    if (til.altitude < 0 || til.biome == Tile.freshwater || til.biome == Tile.tundra) // civis may not start on ocean or river or in tundra
      return false;
    
    ArrayList<Tile> adjacent = adjacentTo(til);
    for (Tile adj: adjacent)
      if (adj.altitude < 0 || adj.biome == Tile.freshwater) // civis spawn a lot near rivers and oceans
        return randChance(-115);
    
    return randChance(-145);
  }
  
  
  public final void give(Civi civ, Civi winner) {
    for (Tile t: civ.land) { // give all of its land to the victor
      t.owners.remove(civ);
      if (!t.owners.contains(winner)) {
        winner.land.add(t);
        t.owners.add(winner);
      }
    }
  }
  
  
  public final void nuke(Tile t) { // shoots a nuclear warhead at the specified Tile
    boom.play(); // BOOM
    t.getsNuked();
    final ArrayList<Tile> adjacentList = adjacentTo(t);
    for (Tile adj: adjacentList) // spews radiation onto this and all surrounding tiles
      adj.getsNuked();
  }
  
  
  public final void meteor(Tile t) { // shoots a meteor of random size at the specified Tile
    blast.play();
    System.out.println("A meteor has struck!");
    
    int size = 50 + (int)(Math.random()*50);
    for (Tile[] row: map)
      for (Tile til: row)
        if (distance(t, til) < size)
          til.getsHitByMeteor();
  }
  
  
  public void loadSound() {
    try {
      boom = java.applet.Applet.newAudioClip(new java.net.URL("file:Sound/explosion.wav"));
      blast = java.applet.Applet.newAudioClip(new java.net.URL("file:Sound/impact.wav"));
    }
    catch (java.net.MalformedURLException error) {
      System.out.println(error);
    }
  }
}