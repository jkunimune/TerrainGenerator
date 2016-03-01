public final class SimpleSinusoidal extends Map { // a projection that mimics sinusoidal but is simpler and cleaner
  private static final long serialVersionUID = 1L;
  
  
  public SimpleSinusoidal(Surface g, int w, int h) {
    super(g, w, h);
    finishSuper();
  }
  
  
  public final java.awt.Point getCoords(int x, int y) {
    if (Math.abs(x-width()/2.0) < Math.sin(Math.PI*y/height())*width()/2.0) // if it is inside the sin curve
      return sfc.tilByAngles(y*Math.PI/height(), Math.PI * (x-width()/2.0) / (Math.sin(Math.PI*y/height())*width()/2.0));
    
    else if (Math.abs(x-width()/2.0) - 3 < Math.sin(Math.PI*(y+2)/(height()+4))*width()/2.0) // if it is on the edge
      return new java.awt.Point(0, -1);
    
    else // if it is outside the sine curve
      return new java.awt.Point(16777215, -1);
  }
}