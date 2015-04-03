import java.util.*;



public final class World extends Globe { // a subclass of Globe to handle all political elements
  public ArrayList<Civi> civis;
  
  
  
  public World(int r) {
    super(r);
    civis = new ArrayList<Civi>();
    
    while (civis.size() == 0) // skips ahead to the founding of the first civi
      for (Tile[] row: map)
        for (Tile til: row)
          spread(til);
  }
  
  
  public World(Globe g) {
    super(g);
    civis = new ArrayList<Civi>();
    
    while (civis.size() == 0) // skips ahead to the founding of the first civi
      for (Tile[] row: map)
        for (Tile til: row)
          spread(til);
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
        
        if (til.temp3 > 0)
          til.development ++;
        
        else if (til.temp3 < 0)
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
      final Civi c = civis.get(i);
      if (c.capital.development == 0) { // if it has lost its capital
        for (Tile t: c.land) {
          t.owners.remove(c);
          if (t.owners.size() == 0) // tells all owned tiles that their nation is dead
            t.development = 0;
        }
        c.capital.isCapital = false;
        System.out.println(c+" has fallen."); // announce the fall of the empire
        civis.remove(c);
      }
    }
  }
  
  
  public final void spread(Tile til) { // causes civis to spawn and claim territory
    til.temp1 = -1; // temp1 is the index of til's new owner
    
    if (til.development > 0) // only run for unclaimed tiles
      return;
    
    final ArrayList<Tile> adjacent = adjacentTo(til);
    
    for (Tile adj: adjacent) { // for all adjacent tiles
      if (adj.owners.size() == 1) { // if that one is settled and this is not
        if (adj.owners.get(0).wants(til)) { // if they want this tile
          til.temp1 = civis.indexOf(adj.owners.get(0));
        }
      }
    }
    
    if (til.development == 0 && settlersLike(til)) // spawns new civis
      civis.add(new Civi(til, civis, this));
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
    if (til.development == 0 || til.owners.get(0).atWarWith.size() == 0) // only run for tiles in civis at war
      return;
    
    final ArrayList<Tile> adjacentList = adjacentTo(til);
    
    if (til.owners.size() == 1) { // if it is not disputed
      if (til.temp1 > -2) { // -2 means it has already resolved another tile
        for (Tile adj: adjacentList) {
          if (adj.owners.size() > 1 && adj.owners.contains(til.owners.get(0))) { // if it is adjacent to a disputed tile
            for (Civi civ: adj.owners) {
              if (!civ.equals(til.owners.get(0))) {
                if (civ.canInvade(til)) {
                  til.temp1 = civis.indexOf(civ);
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
                  til.temp2 = civis.indexOf(civ);
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
  
  
  public final void startWar(Tile til) { // causes Civis to wage war on others
    if (til.owners.size() == 1 && til.owners.get(0).wantsWar()) { // Tiles occasionally decide to wage war on neighbors
      final Tile adj = landBorderAt(til);
      if (adj.lat != -1 && adj.altitude >= 0) // if there is a border
        wageWar(til.owners.get(0), adj.owners.get(0), adj);
    }
  }
  
  
  public final void startRevolution(Tile til) { // causes Civis to simultaneously spawn inside an existing one and wage war on the parent Civi
  }
  
  
  public final void endWar() { // causes peace treaties
  }
  
  
  public final void naturallyDisast(Tile til) { // causes natural disasters
  }
  
  
  public final void wageWar(Civi agg, Civi vic, Tile start) { // starts a war between two civis at a Tile
    System.out.println(agg+" has invaded "+vic+"!");
    agg.atWarWith.add(vic); // civis know with whom they are at war
    vic.atWarWith.add(agg);
    
    agg.takes(start);
    
    for (int i = 0; i < 512; i ++) { // calls a random region between the empires into dispute
      for (int j = 0; j < agg.land.size(); j ++) {
        final Tile til = agg.land.get(j);
        if (til.owners.size() >= 2 && til.owners.contains(agg) && til.owners.contains(vic)) // if til is disputed
          for (Tile adj: adjacentTo(til))
            if (adj.owners.size() == 1 && til.owners.get(0).equals(vic))
              if (agg.canInvade(til))
                agg.takes(adj);
      }
    }
  }
  
  
  public final Tile landBorderAt(Tile til) { // decides if there is a land international border here
    final ArrayList<Tile> adjacentList = adjacentTo(til);
    for (Tile adj: adjacentList)
      if (adj.altitude >= 0 && adj.owners.size() == 1 && !adj.owners.equals(til.owners))
        return adj;
    return new Tile(-1, -1);
  }
  
  
  public final void test() {
    civis.add(new Civi(map[0][0], civis, this));
    civis.add(new Civi(map[313][0], civis, this));
    wageWar(civis.get(0), civis.get(1), map[159][0]);
    
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
    if (til.altitude < 0 || til.biome == Tile.freshwater) // civis may not start on ocean or river
      return false;
    
    ArrayList<Tile> adjacent = adjacentTo(til);
    for (Tile adj: adjacent)
      if (adj.altitude < 0 || adj.biome == Tile.freshwater) // civis spawn a lot near rivers and oceans
        return randChance(-115);
    
    return randChance(-145);
  }
}