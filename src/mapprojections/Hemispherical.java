package mapprojections;

import mechanics.Map;
import mechanics.Surface;
import mechanics.Tile;

public class Hemispherical extends Map { // a realistic globe projection that mimics what the planet would look like if viewed from an infinite distance
  private static final long serialVersionUID = 1L;
	
  int radius;
  
  
  
  public Hemispherical(Surface g, int w, int h) {
    super(g, w, h);
    if (w/2 < h)  radius = w/4;
    else          radius = h/2;
    finishSuper();
  }
  
  
  public final Tile getCoords(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) // if in the left circle
      return sfc.getTile(Math.acos(1-(double)y/radius),
                             Math.asin((x-radius) / Math.sqrt(radius*radius - (y-radius)*(y-radius))) + Math.PI/2);
    
    else if ((x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < radius*radius) // if in the right
      return sfc.getTile(Math.acos(1-(double)y/radius),
                             Math.asin((x-3*radius) / Math.sqrt(radius*radius - (y-radius)*(y-radius))) + 3*Math.PI/2);
                             
    else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius) ||
             (x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) // if it is on the edge of a circle
      return new Tile(8355839, -1);
    
    else
      return new Tile(0, -1);
  }
}