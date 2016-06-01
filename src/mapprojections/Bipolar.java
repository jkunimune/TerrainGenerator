package mapprojections;

import mechanics.Map;
import mechanics.Surface;
import mechanics.Tile;

public class Bipolar extends Map { // an equal-distant map showing polar projections of both hemispheres
  private static final long serialVersionUID = 1L;
	
  int radius;
  
  
  
  public Bipolar(Surface g, int w, int h) {
    super(g, w, h);
    if (w/2 < h)  radius = w/4;
    else          radius = h/2;
    finishSuper();
  }
  
  
  public final Tile getCoords(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) // if inside the left circle
      return sfc.getTile(Math.PI - Math.sqrt((x-radius)*(x-radius) + (y-radius)*(y-radius))*Math.PI/2/radius,
                            Math.atan2(y-radius, x-radius));
    
    else if ((x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < radius*radius) // if inside the right circle
      return sfc.getTile(Math.sqrt((x-3*radius)*(x-3*radius) + (y-radius)*(y-radius))*Math.PI/2/radius,
                            Math.atan2(y-radius, 3*radius-x));
    
    else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius) || 
             (x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) // if on the edge of a circle
      return new Tile(0, -1);
    
    else
      return new Tile(16777215, -1);
  }
}