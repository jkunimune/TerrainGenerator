import java.util.*;



public final class World extends Globe { // a subclass of Globe to handle all political elements
  public ArrayList<Civi> civis;
  
  
  
  public World(int r) {
    super(r);
    civis = new ArrayList<Civi>();
    
    while (civis.size() == 0) // skips ahead to the founding of the first civi
      spread();
  }
  
  
  public World(Globe g) {
    super(g);
    civis = new ArrayList<Civi>();
    
    while (civis.size() == 0) // skips ahead to the founding of the first civi
      spread();
  }
  
  
  
  public final void update() { // updates the world a frame
    naturallyDisast();
    endWar();
    startWar();
    startRevolution();
    spread();
    
    for (Civi c: civis)
      c.advance();
  }
  
  
  public final void spread() { // causes Civis to claim terriory
    for (Tile[] row: map) { // for each tile in map
      for (Tile til: row) {
        til.temp1 = -1;
        ArrayList<Tile> adjacent = adjacentTo(til);
        for (Tile adj: adjacent) { // for all adjacent tiles
          if (adj.development > 0 && til.development == 0) { // if that one is settled and this is not
            if (adj.owners.get(0).wants(til)) { // if they want this tile
              til.temp1 = adj.lat;
              til.temp2 = adj.lon;
            }
          }
        }
        
        if (til.development == 0 && settlersLike(til))
          civis.add(new Civi(til, civis, this));
        else if (til.development > 0)
          til.owners.get(0).tryUpgrade(til);
      }
    }
    
    for (Tile[] row: map)
      for (Tile til: row)
        if (til.temp1 != -1)
          til.getsTakenBy(map[til.temp1][til.temp2].owners.get(0));
  }
  
  
  public final void startWar() { // causes Civis to wage war on others
  }
  
  
  public final void startRevolution() { // causes Civis to simultaneously spawn inside an existing one and wage war on the parent Civi
  }
  
  
  public final void endWar() { // causes peace treaties
  }
  
  
  public final void naturallyDisast() { // causes natural disasters
  }
  
  
  public final void test() {
    civis.add(new Civi(map[0][0], civis, this));
    
    for (Tile[] row: map)
      for (Tile til: row)
        til.owners.add(civis.get(0));
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