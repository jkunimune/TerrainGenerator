public class Mercator extends Map { // a sphere projection designed to preserve shapes but distorts size near poles, which are not visible
  public Mercator(Globe g, int w, int h) {
    super(g, w, h);
    
    for (int x = 0; x < w; x ++) {
      for (int y = 0; y < h; y ++) {
        if ((x/2*2 == w/10/2*2 || x/2*2 == w*9/10/2*2) && y/7%2 == 0) { // draw dotted lines on sides
          lats[y][x] = -1;
          lons[y][x] = 0;
        }
        else {
          lats[y][x] = g.latIndex(Math.PI/2 + Math.atan(((double)y-h*5/8) / (w/2/Math.PI)));
          lons[y][x] = g.lonIndex(lats[y][x], (x*5/4)%w * 2*Math.PI / w);
        }
      }
    }
  }
}