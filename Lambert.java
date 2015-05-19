public class Lambert extends Map { // an equal area globe projection that distorts shape severely near poles, which are lines
  public Lambert(Globe g, int w, int h) {
    super(g, w, h);
    finishSuper();
  }
  
  
  public final int getLat(int x, int y) {
    if ((x>>1<<1 == width()/10>>1<<1 || x>>1<<1 == width()*9/10>>1<<1) && y/7%2 == 0)
      return -1;
    else
      return glb.latIndex(Math.PI/2 + Math.asin((y-height()/2.0) / (height()/2.0)));
  }
  
  
  public final int getLon(int x, int y) {
    if (lats[y][x] == -1)
      return 0;
    else
      return glb.lonIndex(lats[y][x], (x*5/4)%width() * 2*Math.PI / width());
  }
}