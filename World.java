import java.util.*;



public final class World extends Globe { // a subclass of Globe to handle all political elements
  public ArrayList<Civi> civis;
  
  
  
  public World(int r) {
    super(r);
    civis = new ArrayList<Civi>();
  }
  
  
  public World(Globe g) {
    super(g);
    civis = new ArrayList<Civi>();
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
        ArrayList<Tile> adjacent = adjacentTo(til);
        for (Tile adj: adjacent) { // for all adjacent tiles
          if (adj.ownership > 0 && til.ownership == 0) { // if that one is settled and this is not
            if (adj.owners.get(0).wants(til)) // if they want this tile
              til.getsTakenBy(adj.owners.get(0));
          }
        }
        
        if (til.ownership == 0 && settlersLike(til)) {
          civis.add(new Civi(til, civis));
        }
      }
    }
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
    civis.add(new Civi(map[0][0], civis));
    
    for (Tile[] row: map)
      for (Tile til: row)
        til.owners.add(civis.get(0));
  }
  
  
  public final boolean settlersLike(Tile til) { // determines whether to found a civi
    int chance = -9001;
    
    ArrayList<Tile> adjacent = adjacentTo(til);
    for (Tile adj: adjacent)
      if (adj.altitude < 0 || adj.biome == Tile.freshwater) // civis spawn a lot near rivers and oceans
        chance = -80;
    
    switch (til.biome) {
      case Tile.magma:
        return false;
      case Tile.ocean:
        return false; // sea biomes are not viable start locations
      case Tile.ice:
        return false;
      case Tile.reef:
        return false;
      case Tile.trench:
        return false;
      case Tile.tundra:
        break; // tundra has normal fertility
      case Tile.plains:
        chance += 4; // plains have normal fertility
        break;
      case Tile.desert:
        chance -= 4; // deserts are not very fertile
        break;
      case Tile.jungle:
        chance += 4; // jungles have extra fertility
        break;
      case Tile.mountain:
        break; // mountains have normal fertility
      case Tile.snowcap:
        chance -= 4; // snowcaps are extra unfertile
        break;
      case Tile.freshwater:
        return false; // while rivers are great places to settle next to, they may not be settled on
      case Tile.space:
        return false;
    }
    
    return randChance(chance);
  }
}