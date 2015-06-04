import java.awt.*;



public class FromSpace extends Map { // like hemispherical, but it spins!
  public static final double camPeriod = 3000.0; // the rate at which the globe seems to turn on screen in msec/rad
  public static final double sunPeriod = -150.0; // the rate at which the sun seems to go around the globe in msec/rad
  double camAngle;
  double sunAngle;
  int radius;
  
  
  
  public FromSpace(Globe g, int w, int h) {
    super(g, w, h);
    if (w/2 < h)  radius = w/4;
    else          radius = h/2;
    updateAngle();
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
          lons[y][x] = getLon(x,y);
          drawPx(x, y, getColorBy(theme, x, y));
        }
      }
    }
    
    show();
  }
  
  
  @Override
  public Color getColorBy(ColS c, int x, int y) { // gets the color at a point on the screen
    final Color daytime = super.getColorBy(c,x,y);
    return daytime;
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
      return glb.lonIndex(lats[y][x], Math.asin((x-radius) / Math.sqrt(radius*radius - (y-radius)*(y-radius))) + Math.PI/2 + camAngle);
    
    else if (lats[y][x] != -1) // if it is in the right circle
      return glb.lonIndex(lats[y][x], Math.asin((x-3*radius) / Math.sqrt(radius*radius - (y-radius)*(y-radius))) + 3*Math.PI/2 + camAngle);
    
    else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius) ||
             (x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) // if it is on the edge of a circle
      return 8355839;
    
    else
      return 0;
  }
}