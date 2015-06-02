public final class Lemons extends Map { // a clich "unfolding"-type lemon-y map
  int lemonWidth;
  
  
  
  public Lemons(Globe g, int w, int h) {
    super(g, w, h);
    lemonWidth = w/12;
    finishSuper();
  }
  
  
  public final int getLat(int x, int y) {
    if (x/lemonWidth >= 12) // only show 12 lemons
      return -1;
    
    if (Math.abs(x%lemonWidth-lemonWidth/2.0) < Math.sin(Math.PI*y/height())*lemonWidth/2.0) // if it is inside a sin curve
      return glb.latIndex(y*Math.PI/height());
    
    return -1;
  }
  
  
  public final int getLon(int x, int y) {
    if (lats[y][x] != -1) { // if it is inside the sin curve
      return glb.lonIndex(lats[y][x], Math.PI * (x%lemonWidth-lemonWidth/2.0) / (Math.sin(Math.PI*y/height())*lemonWidth*6.0) + x/lemonWidth*Math.PI/6);
    }
    
    else { // if it is outside a sine curve
      return 0;
    }
  }
}