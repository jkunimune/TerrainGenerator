public class Mercator extends Map { // a sphere projection designed to preserve shapes but distorts size near poles, which are not visible
  public Mercator(Globe g, int w, int h) {
    super(g, w, h);
    
    for (int x = 0; x < w; x ++) {
      for (int y = 0; y < h; y ++) {
        if ((x>>1<<1 == w/10>>1<<1 || x>>1<<1 == w*9/10>>1<<1) && y/7%2 == 0) { // draw dotted lines on sides
          lats[y][x] = -1;
          lons[y][x] = 0;
        }
        else {
          lats[y][x] = g.latIndex(Math.PI/2 + Math.atan(((double)y-h*5/8) / (w/2/Math.PI)));
          lons[y][x] = g.lonIndex(lats[y][x], (x*5/4)%w * 2*Math.PI / w);
        }
      }
    }
    
    initialPaint();
  }
  
  
  public final void replaceLat(int x, int y) {
    if (!((x>>1<<1 == width()/10>>1<<1 || x>>1<<1 == width()*9/10>>1<<1) && y/7%2 == 0))
      lats[y][x] = glb.latIndex(Math.PI/2 + Math.atan(((double)y-height()*5/8) / (width()/2/Math.PI)));
  }
}