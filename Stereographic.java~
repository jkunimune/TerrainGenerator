public class Stereographic extends Map { // a shape-preserving azimuthal map showing both hemispheres and empasizing the tropics
  int radius;
  
  
  
  public Stereographic(Globe g, int w, int h) {
    super(g, w, h);
    if (w/2 < h)  radius = w/4;
    else          radius = h/2;
    finishSuper();
  }
  
  
  public final int getLat(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) // if inside the left circle
      return glb.latIndex(Math.PI - 2*Math.atan(Math.hypot(x-radius, y-radius)/radius));
    
    else if ((x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < radius*radius) // if inside the right circle
      return glb.latIndex(2*Math.atan(Math.hypot(x-3*radius, y-radius)/radius));
      
    else
      return -1;
  }
  
  
  public final int getLon(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) { // if inside the left circle
      if (x > radius) // if x is on right of circle
        return glb.lonIndex(lats[y][x], Math.atan((double)(y-radius)/(x-radius)));
      else if (x < radius) // if point is on left of circle
        return glb.lonIndex(lats[y][x], Math.atan((double)(y-radius)/(x-radius)) + Math.PI);
      else { // if point is on vertical line of symetry
        if (y > radius)  return glb.lonIndex(lats[y][x], Math.PI/2);
        else             return glb.lonIndex(lats[y][x], 3*Math.PI/2);
      }
    }
    
    else if (lats[y][x] != -1) { // if inside the right circle
      if (x > 3*radius) // if x is on right of circle
        return glb.lonIndex(lats[y][x], Math.atan((double)(y-radius)/(3*radius-x)) + Math.PI);
      else if (x < 3*radius) // if point is on left of circle
        return glb.lonIndex(lats[y][x], Math.atan((double)(y-radius)/(3*radius-x)));
      else { // if point is on vertical line of symetry
        if (y > radius)  return glb.lonIndex(lats[y][x], Math.PI/2);
        else             return glb.lonIndex(lats[y][x], 3*Math.PI/2);
      }
    }
    
    else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius) || 
             (x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) { // if it is on the edge of the circle
      return 0;
    }
    
    else {
      return 16777215;
    }
  }
}