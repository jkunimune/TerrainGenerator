import java.awt.*;



public class OneDim extends Map { // a one dimensional map that only sees the prime meridian
  public OneDim(Surface g, int w, int h) {
    super(g, w, h);
    finishSuper();
  }
  
  
  public int getLat(int x, int y) { // updates the matrices
    return sfc.latIndex(x*Math.PI/width());
  }
  
  
  public int getLon(int x, int y) {
    return 0;
  }
  
  
  @Override
  public Color getColorBy(ColS c, int x, int y) {
    final Tile til = sfc.getTileByIndex(lats[y][x], lons[y][x]);
    
    if (til.altitude < 0) { // for ocean tiles
      if (y < height()/2) // the upper half
        return Color.black; // is black
      
      else if (y-height()/2 < -til.altitude*height()/256) // the middle section
        return super.getColorBy(c, x, y); // references the tile color itself
      
      else // the bottom
        return new Color(15, 255, 7); // is a greyish green color
    }
    
    else { // for land tiles
      if (y-height()/2 >= -til.altitude*height()/256) // the middle section and lower half
        return super.getColorBy(c, x, y); // references the tile color itself;

      else // the top
        return Color.black; // is black
    }
  }
}