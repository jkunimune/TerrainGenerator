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
        
        develop(til);
        
        spread(til);
        
        startWar(til);
        startRevolution(til);
      }
    }
    
    for (Tile[] row: map) { // asssigns all new values
      for (Tile til: row) {
        if (til.temp1 != -1 && map[til.temp1][til.temp2].owners.size() > 0)
          map[til.temp1][til.temp2].owners.get(0).takes(til);
        
        else if (til.temp3 > 0 && til.owners.size() > 0)
          til.development ++;
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
    til.temp1 = -1; // temp1 and temp2 are the indices of til's new owner
    
    final ArrayList<Tile> adjacent = adjacentTo(til);
    
    for (Tile adj: adjacent) { // for all adjacent tiles
      if (adj.development > 0 && til.development == 0) { // if that one is settled and this is not
        if (adj.owners.get(0).wants(til)) { // if they want this tile
          til.temp1 = adj.lat;
          til.temp2 = adj.lon;
          til.temp3 = 1;
        }
      }
    }
    
    if (til.development == 0 && settlersLike(til)) // spawns new civis
      civis.add(new Civi(til, civis, this));
  }
  
  
  public final void develop(Tile til) { // causes tiles to be developed from territory to utopias
    til.temp3 = 0; // temp3 is whether it gets upgraded this turn
    
    if (til.development > 0 && til.owners.get(0).canUpgrade(til)) // upgrades tiles
      til.temp3 = 1;
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
    
    for (int i = 0; i < 10; i ++) // calls a random region between the empires into dispute
      for (Tile[] row: map)
        for (Tile til: row)
          if (til.owners.size() >= 2 && til.owners.contains(agg) && til.owners.contains(vic)) // if til is disputed
            for (Tile adj: adjacentTo(til))
              if (adj.owners.size() == 1 && til.owners.get(0).equals(vic))
                if (agg.canInvade(til))
                  agg.takes(adj);
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
    civis.add(new Civi(map[0][0], civis, this));
    
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
        return randChance(-120);
    
    return randChance(-150);
  }
}