public class Polar extends Map { // an equal-distant map centered on the north pole much like the projection the UN flag uses
  int radius;
  
  
  
  public Polar(Surface g, int w, int h) {
    super(g, w, h);
    if (w < h)  radius = w/2;
    else        radius = h/2;
    finishSuper();
  }
  
  
  public final int getLat(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius)
      return sfc.latIndex(Math.sqrt((x-radius)*(x-radius) + (y-radius)*(y-radius))*Math.PI/radius);
    else
      return -1;
  }
  
  
  public final int getLon(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) { // if inside the circle
      return sfc.lonIndex(lats[y][x], Math.atan2(y-radius, x-radius));
    }
    else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) { // if it is on the edge of the circle
      return 0;
    }
    else {
      return 16777215;
    }
  }
}