package mapprojections;

import mechanics.Map;
import mechanics.Surface;
import mechanics.Tile;

public class Mercator extends Map { // a sphere projection designed to preserve shapes but distorts size near poles, which are not visible
  private static final long serialVersionUID = 1L;
	
  
  public Mercator(Surface g, int w, int h) {
    super(g, w, h);
    finishSuper();
  }
  
  
  public final Tile getCoords(int x, int y) {
    if ((x>>1<<1 == width()/10>>1<<1 || x>>1<<1 == width()*9/10>>1<<1) && y/7%2 == 0) // draw dotted lines on sides
      return new Tile(0, -1);
    else
      return sfc.getTile(Math.PI/2 + Math.atan(Math.sinh(((double)y-height()/2.0) / (width()/(2*Math.PI)))),
                             (x*5/4)%width() * 2*Math.PI / width());
  }
}