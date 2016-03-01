import java.awt.*;



public class FromSpace extends Map { // like hemispherical, but it spins!
  private static final long serialVersionUID = 1L;
	
  public static final double camPeriod = 3000.0; // the rate at which the globe seems to turn on screen in msec/rad
  public static final double sunPeriod = -800.0; // the rate at which the sun seems to go around the globe in msec/rad
  private double camAngle;
  private double sunAngle;
  private double[][] longitudes; // like lons but carries actual angles, not indicies, and remains static
  private int radius;
  
  
  
  public FromSpace(Surface g, int w, int h) {
    super(g, w, h);
    
    if (w/2 < h)  radius = w/4;
    else          radius = h/2;
    
    updateAngle();
    
    longitudes = new double[h][w];
    for (int x = 0; x < w; x ++)
      for (int y = 0; y < h; y ++)
        longitudes[y][x] = getLongitude(x,y);
    
    finishSuper();
  }
  
  
  public void exhibit(ColS theme, long time) { // continuously displays a rotating globe for a given time interval
    long startTime = System.currentTimeMillis();
    while(System.currentTimeMillis() < startTime + time)
      display(theme);
  }
  
  
  private void updateAngle() { // calculates the angle at which we view the planet and the angle at which the sun strikes the planet in radians
    camAngle = System.currentTimeMillis() / camPeriod;
    sunAngle = System.currentTimeMillis() / sunPeriod;
  }
  
  
  @Override
  public void display(ColS theme) {
    updateAngle();
    
    for (int x = 0; x < width(); x ++) {
      for (int y = 0; y < height(); y ++) {
        if (lats[y][x] != -1) {
          lons[y][x] = getCoords(x,y).x;
          drawPx(x, y, getColorBy(theme, x, y));
        }
      }
    }
    
    show();
  }
  
  
  @Override
  public Color getColorBy(ColS c, int x, int y) { // gets the color at a point on the screen
    final Color daytime = super.getColorBy(c,x,y);
    final Tile til = sfc.getTileByIndex(lats[y][x], lons[y][x]);
    final double scaleFactor = Math.sin(longitudes[y][x] + sunAngle) * .5 + .5;
    if (til.development == 3 && scaleFactor < .4)
      return Color.yellow;
    if (til.development == 4 && scaleFactor < .5)
      return Color.cyan;
    if (scaleFactor < 0)
      return Color.black;
    else
      return new Color((int)(daytime.getRed()*scaleFactor),
                       (int)(daytime.getGreen()*scaleFactor),
                       (int)(daytime.getBlue()*scaleFactor));
  }


  public final double getLongitude(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) // if it is in the left circle
      return Math.asin((x-radius) / Math.sqrt(radius*radius - (y-radius)*(y-radius))) + Math.PI/2;
    
    else if (lats[y][x] != -1) // if it is in the right circle
      return Math.asin((x-3*radius) / Math.sqrt(radius*radius - (y-radius)*(y-radius))) + 3*Math.PI/2;
    
    return 0;
  }
  
  
  public final java.awt.Point getCoords(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius)
      return sfc.tilByAngles(Math.acos(1-(double)y/radius),
                             longitudes[y][x] + camAngle);
    
    else if ((x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < radius*radius)
      return sfc.tilByAngles(Math.acos(1-(double)y/radius),
                          longitudes[y][x] + camAngle);
    
    else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius) ||
             (x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) // if it is on the edge of a circle
      return new java.awt.Point(8355839, -1);
    
    else
      return new java.awt.Point(0, -1);
  }
}