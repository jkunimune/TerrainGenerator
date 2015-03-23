public class Hemispherical extends Map { // a realistic globe projection that shows each hemisphere seperately and resembles FromSpace
  int radius;
  
  
  
  public Hemispherical(Globe g, int w, int h) {
    super(g, w, h);
    if (w/2 < h)  radius = w/4;
    else          radius = h/2;
    
    for (int x = 0; x < w; x ++) {
      for (int y = 0; y < h; y ++) {
        if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) { // if it is in the left circle
          lats[y][x] = g.latIndex(Math.acos(1-(double)y/radius));
          lons[y][x] = g.lonIndex(lats[y][x], Math.asin((x-radius) / Math.sqrt(radius*radius - (y-radius)*(y-radius))) + Math.PI/2);
        }
        else if ((x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < radius*radius) { // if it is in the right circle
          lats[y][x] = g.latIndex(Math.acos(1-(double)y/radius));
          lons[y][x] = g.lonIndex(lats[y][x], Math.asin((x-3*radius) / Math.sqrt(radius*radius - (y-radius)*(y-radius))) + 3*Math.PI/2);
        }
        else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius) ||
                 (x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) { // if it is on the edge of a circle
          lats[y][x] = -1;
          lons[y][x] = 8355839;
        }
        else {
          lats[y][x] = -1;
          lons[y][x] = 0;
        }
      }
    }
  }
}