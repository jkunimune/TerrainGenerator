public class Miller extends Map { // a cyllindrical projection based on Mercator that displays more of the equatorial regions
  private static final long serialVersionUID = 1L;
	
  
  public Miller(Surface g, int w, int h) {
    super(g, w, h);
    finishSuper();
  }
  
  
  public final java.awt.Point getCoords(int x, int y) {
    if ((x>>1<<1 == width()/10>>1<<1 || x>>1<<1 == width()*9/10>>1<<1) && y/7%2 == 0) // draw dotted lines on sides
      return new java.awt.Point(0, -1);
    else
      return sfc.tilByAngles(Math.PI/2 + 4.0/5.0*Math.atan(Math.sinh(5.0/4.0*(((double)y-height()/2.0) / (width()/(2*Math.PI))))),
                             (x*5/4)%width() * 2*Math.PI / width());
  }
}