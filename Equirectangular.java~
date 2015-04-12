public class Equirectangular extends Map { // a simple globe projection that is easy to calculate
  public Equirectangular(Globe g, int w, int h) {
    super(g, w, h);
    
    for (int x = 0; x < w; x ++) {
      for (int y = 0; y < h; y ++) {
        if ((x>>1<<1 == w/10>>1<<1 || x<<1>>1 == w*9/10>>1<<1) && y/7%2 == 0) { // draw dotted lines on sides
          lats[y][x] = -1;
          lons[y][x] = 0;
        }
        else {
          lats[y][x] = g.latIndex(x*h/Math.PI);
          lons[y][x] = g.lonIndex(lats[y][x], (x*5/4)%w*2*Math.PI/w);
        }
      }
    }
    
    initialPaint();
  }
}