public class Sinusoidal extends Map { // an equal-area projection that is shaped like a sine curve
  public Sinusoidal(Globe g, int w, int h) {
    super(g, w, h);
    
    for (int x = 0; x < w; x ++) {
      for (int y = 0; y < h; y ++) {
        if (Math.abs(x-w/2.0) < Math.sin(Math.PI*y/h)*w/2.0) { // if it is inside the sin curve
          lats[y][x] = g.latIndex(Math.PI*y/h);
          lons[y][x] = g.lonIndex(lats[y][x], Math.PI * (x-w/2.0) / (Math.sin(Math.PI*y/h)*w/2.0));
        }
        
        else if (Math.abs(x-w/2.0) - 3 < Math.sin(Math.PI*(y+2)/(h+4))*w/2.0) { // if it is on the edge
          lats[y][x] = -1;
          lons[y][x] = 4144959;
        }
        
        else { // if it is outside the sine curve
          lats[y][x] = g.latIndex(Math.PI*y/h);
          lons[y][x] = g.lonIndex(lats[y][x], Math.PI * (x-w/2.0) / (Math.sin(Math.PI*y/h)*w/2.0));
        }
      }
    }
  }
}