package mapprojections;

import mechanics.Map;
import mechanics.Surface;

public final class Sinusoidal extends Map { // an equal-area projection that takes the shape of a sine curve
  private static final long serialVersionUID = 1L;
  
  
  public Sinusoidal(Surface g, int w, int h) {
    super(g, w, h);
    finishSuper();
  }
  
  
  public final java.awt.Point getCoords(int x, int y) {
    if (Math.abs(x-width()/2.0) < Math.sin(Math.PI*y/height())*width()/2.0 || // if it is inside
        Math.abs(x-width()/2.0) - 3 >= Math.sin(Math.PI*(y+2)/(height()+4))*width()/2.0) // or outside the sine curve
      return sfc.tilByAngles(y*Math.PI/height(), Math.PI * (x-width()/2.0) / (Math.sin(Math.PI*y/height())*width()/2.0));
    
    else // if it is on the edge
      return new java.awt.Point(4144959, -1);
  }
}