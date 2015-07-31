public class PartialStereo extends Map { // a stereographic map that utilizes the entire space but only displays the south portion of the globe
  int radius;
  
  
  
  public PartialStereo(Globe g, int w, int h) {
    super(g, w, h);
    radius = (int)(Math.hypot(w, h)/6); // the radius of the map (from pole to equator) is an eighth of the diagonal of the screen
    finishSuper();
  }
  
  
  public final int getLat(int x, int y) {
    return glb.latIndex(2*Math.atan(Math.hypot(x-width()/2, y-height()/2) / radius));
  }
  
  
  public final int getLon(int x, int y) {
    if (x > width()/2) // if x is on right of circle
      return glb.lonIndex(lats[y][x], Math.atan((double)(y-height()/2)/(x-width()/2)));
    else if (x < width()/2) // if point is on left of circle
      return glb.lonIndex(lats[y][x], Math.atan((double)(y-height()/2)/(x-width()/2)) + Math.PI);
    else { // if point is on vertical line of symetry
      if (y > height()/2)  return glb.lonIndex(lats[y][x], Math.PI/2);
      else                 return glb.lonIndex(lats[y][x], 3*Math.PI/2);
    }
  }
}