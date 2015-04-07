public class OneDim extends Map { // a one dimensional map that only sees the prime meridian
  int radius;
  
  
  
  public OneDim(Globe g, int w, int h) {
    super(g, w, h);
    
    update();
  }
  
  
  public void display(int colorScheme) {
    update();
    super.display(colorScheme);
  }
  
  
  public void update() { // updates the matrices
    for (int x = 0; x < width(); x ++) {
      for (int y = 0; y < height(); y ++) {
        final Tile til = glb.getTile(x*Math.PI/width(), 0);
        
        if (til.altitude < 0) { // for ocean tiles
          if (y < height()/2) { // the upper half
            lats[y][x] = -1; // is black
            lons[y][x] = 0;
          }
          else if (y-height()/2 < -til.altitude*height()/256) { // the middle section
            lats[y][x] = glb.latIndex(x*Math.PI/width()); // references the tile color itself
            lons[y][x] = 0;
          }
          else { // the bottom
            lats[y][x] = -1; // is a greyish green color
            lons[y][x] = 1081096;
          }
        }
        
        else { // for land tiles
          if (y-height()/2 >= -til.altitude*height()/256) { // the middle section and lower half
            lats[y][x] = glb.latIndex(x*Math.PI/width()); // references the tile color itself
            lons[y][x] = 0;
          }
          else { // the top
            lats[y][x] = -1; // is black
            lons[y][x] = 0;
          }
        }
      }
    }
  }
}