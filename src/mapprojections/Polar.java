package mapprojections;

import mechanics.Map;
import mechanics.Surface;

public class Polar extends Map { // an equal-distant map centered on the north pole much like the projection the UN flag uses
  private static final long serialVersionUID = 1L;
  
  int radius;
  
  
  public Polar(Surface g, int w, int h) {
    super(g, w, h);
    if (w < h)  radius = w/2;
    else        radius = h/2;
    finishSuper();
  }
  
  
  public final java.awt.Point getCoords(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) // if inside the circle
      return sfc.tilByAngles(Math.sqrt((x-radius)*(x-radius) + (y-radius)*(y-radius))*Math.PI/radius, Math.atan2(y-radius, x-radius));
      
    else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) // if it is on the edge of the circle
      return new java.awt.Point(0, -1);
    
    else
      return new java.awt.Point(16777215, -1);
  }
}