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
  
  
  
  public final void update() { // updates the world a year or so
    return;
  }
  
  
  public final void test() {
    civis.add(new Civi(map[0][0], civis));
    
    for (Tile[] row: map)
      for (Tile til: row)
        til.owners.add(civis.get(0));
  }
}