public class Lambert extends Map { // an equal area globe projection that distorts shape severely near poles, which are lines
  public Lambert(Globe g, int w, int h) {
    super(g, w, h);
    
    for (int x = 0; x < w; x ++) {
      for (int y = 0; y < h; y ++) {
        if ((x>>1<<1 == w/10>>1<<1 || x>>1<<1 == w*9/10>>1<<1) && y/7%2 == 0) { // draw dotted lines on sides
          lats[y][x] = -1;
          lons[y][x] = 0;
        }
        else {
          lats[y][x] = g.latIndex(Math.PI/2 + Math.asin((y-h/2.0) / (h/2.0)));
          lons[y][x] = g.lonIndex(lats[y][x], (x*5/4)%w * 2*Math.PI / w);
        }
      }
    }
    
    initialPaint();
  }
  
  
  public final void replaceLat(int x, int y) {
    if (!((x>>1<<1 == width()/10>>1<<1 || x>>1<<1 == width()*9/10>>1<<1) && y/7%2 == 0))
      lats[y][x] = glb.latIndex(Math.PI/2 + Math.asin((y-height()/2.0) / (height()/2.0)));
  }
}