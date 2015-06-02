public class Hemispherical extends Map { // a realistic globe projection that mimics what the planet would look like if viewed from an infinite distance
  int radius;
  
  
  
  public Hemispherical(Globe g, int w, int h) {
    super(g, w, h);
    if (w/2 < h)  radius = w/4;
    else          radius = h/2;
    finishSuper();
  }
  
  
  public final int getLat(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius)
      return glb.latIndex(Math.acos(1-(double)y/radius));
    
    else if ((x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < radius*radius)
      return glb.latIndex(Math.acos(1-(double)y/radius));
    
    else
      return -1;
  }
  
  
  public final int getLon(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) // if it is in the left circle
      return glb.lonIndex(lats[y][x], Math.asin((x-radius) / Math.sqrt(radius*radius - (y-radius)*(y-radius))) + Math.PI/2);
    
    else if ((x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < radius*radius) // if it is in the right circle
      return glb.lonIndex(lats[y][x], Math.asin((x-3*radius) / Math.sqrt(radius*radius - (y-radius)*(y-radius))) + 3*Math.PI/2);
    
    else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius) ||
             (x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) // if it is on the edge of a circle
      return 8355839;
    
    else
      return 0;
  }
}