import java.util.*;



public class World extends Globe { // a subclass of Globe to handle all political elements
  public ArrayList<Civi> civis;
  
  
  public World(int r) {
    super(r);
    civis = new ArrayList<Civi>();
  }
  
  
  public World(Globe g) {
    super(g);
  }
}