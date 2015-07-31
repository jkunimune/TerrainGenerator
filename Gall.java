public class Gall extends Map { // a compromising projection that resembles Mercator, but shows the poles
  public Gall(Surface g, int w, int h) {
    super(g, w, h);
    finishSuper();
  }
  
  
  public final int getLat(int x, int y) {
    if ((x>>1<<1 == width()/10>>1<<1 || x>>1<<1 == width()*9/10>>1<<1) && y/7%2 == 0)
      return -1;
    else
      return sfc.latIndex(Math.PI/2 + 2*Math.atan((y-height()/2.0) / (height()/2.0)));
  }
  
  
  public final int getLon(int x, int y) {
    if (lats[y][x] == -1) // draw dotted lines on sides
      return 0;
    else
      return sfc.lonIndex(lats[y][x], (x*5/4)%width() * 2*Math.PI / width());
  }
}