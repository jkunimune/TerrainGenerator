public class Bipolar extends Map { // an equal-distant map showing polar projections of both hemispheres
  int radius;
  
  
  
  public Bipolar(Globe g, int w, int h) {
    super(g, w, h);
    if (w < h)  radius = w/2;
    else        radius = h/2;
    
    for (int x = 0; x < 2*radius; x ++) {
      for (int y = 0; y < 2*radius; y ++) {
        if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) { // if inside the circle
          lats[y][x] = g.latIndex(Math.sqrt((x-radius)*(x-radius) + (y-radius)*(y-radius))*Math.PI/radius); // latitud is distance from center
          
          if (x > radius) // if x is on right of circle
            lons[y][x] = g.lonIndex(lats[y][x], Math.atan((double)(y-radius)/(x-radius)));
          else if (x < radius) // if point is on left of circle
            lons[y][x] = g.lonIndex(lats[y][x], Math.atan((double)(y-radius)/(x-radius)) + Math.PI);
          else { // if point is on vertical line of symetry
            if (y > radius)  lons[y][x] = g.lonIndex(lats[y][x], Math.PI/2);
            else             lons[y][x] = g.lonIndex(lats[y][x], 3*Math.PI/2);
          }
        }
        else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) { // if it is on the edge of the circle
          lats[y][x] = -1;
          lons[y][x] = 0;
        }
        else {
          lats[y][x] = -1;
          lons[y][x] = 16777215;
        }
      }
    }
  }
}