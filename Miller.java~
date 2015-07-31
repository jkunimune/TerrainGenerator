public class Miller extends Map { // a cyllindrical projection based on Mercator that displays more of the equatorial regions
  public Miller(Globe g, int w, int h) {
    super(g, w, h);
    finishSuper();
  }
  
  
  public final int getLat(int x, int y) {
    if ((x>>1<<1 == width()/10>>1<<1 || x>>1<<1 == width()*9/10>>1<<1) && y/7%2 == 0) // draw dotted lines on sides
      return -1;
    else
      return glb.latIndex(Math.PI/2 + 4.0/5.0*Math.atan(Math.sinh(5.0/4.0*(((double)y-height()/2.0) / (width()/(2*Math.PI))))));
  }
  
  
  public final int getLon(int x, int y) {
    if (lats[y][x] == -1) // draw dotted lines on sides
      return 0;
    else
      return glb.lonIndex(lats[y][x], (x*5/4)%width() * 2*Math.PI / width());
  }
}