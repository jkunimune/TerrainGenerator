public class PartialStereo extends Map { // a stereographic map that utilizes the entire space but only displays the south portion of the globe
  int radius;
  
  
  
  public PartialStereo(Surface g, int w, int h) {
    super(g, w, h);
    radius = (int)(Math.hypot(w, h)/6); // the radius of the map (from pole to equator) is an eighth of the diagonal of the screen
    finishSuper();
  }
  
  
  public final java.awt.Point getCoords(int x, int y) {
    return sfc.tilByAngles(2*Math.atan(Math.hypot(x-width()/2, y-height()/2) / radius),
                           Math.atan2(y-height()/2, x-width()/2));
  }
}