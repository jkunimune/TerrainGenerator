public final class CustomTrig extends Map { // a compromising projection based on Sinusoidal that I came up with myself
  public CustomTrig(Globe g, int w, int h) {
    super(g, w, h);
    finishSuper();
  }
  
  
  public final int getLat(int x, int y) {
    if (Math.abs(x-width()/2.0) < Math.sin(Math.PI*y/height())*width()/2.0 ||
        Math.abs(x-width()/2.0) - 3 >= Math.sin(Math.PI*(y+2)/(height()+4))*width()/2.0) // if it is inside the sin curve or outside the sine curve
      return glb.latIndex(Math.acos(Math.exp(-Math.pow(2.0*x/width()-1,2)/2) * Math.cos(y*Math.PI/height())));
    
    else // if it is on the edge
      return -1;
  }
  
  
  public final int getLon(int x, int y) {
    if (Math.abs(x-width()/2.0) < Math.sin(Math.PI*y/height())*width()/2.0) { // if it is inside the sin curve
      return glb.lonIndex(lats[y][x], Math.PI * (x-width()/2.0) / (Math.sin(Math.PI*y/height())*width()/2.0));
    }
    
    else if (Math.abs(x-width()/2.0) - 3 < Math.sin(Math.PI*(y+2)/(height()+4))*width()/2.0) { // if it is on the edge
      return 4144959;
    }
    
    else { // if it is outside the sine curve
      return glb.lonIndex(lats[y][x], Math.PI * (x-width()/2.0) / (Math.sin(Math.PI*y/height())*width()/2.0));
    }
  }
}