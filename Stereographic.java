public class Stereographic extends Map { // a shape-preserving azimuthal map showing both hemispheres and empasizing the tropics
  int radius;
  
  
  
  public Stereographic(Surface g, int w, int h) {
    super(g, w, h);
    if (w/2 < h)  radius = w/4;
    else          radius = h/2;
    finishSuper();
  }
  
  
  public final java.awt.Point getCoords(int x, int y) {
    if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) // if inside the left circle
      return sfc.tilByAngles(Math.PI - 2*Math.atan(Math.hypot(x-radius, y-radius)/radius),
                             Math.atan2(y-radius, x-radius));
    
    else if ((x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < radius*radius) // if inside the right circle
      return sfc.tilByAngles(2*Math.atan(Math.hypot(x-3*radius, y-radius)/radius),
                             Math.atan2(y-radius, 3*radius-x) + Math.PI);
    
    else if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius) || 
             (x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < (3+radius)*(3+radius)) // if it is on the edge of the circle
      return new java.awt.Point(0, -1);
    
    else
      return new java.awt.Point(16777215, -1);
  }
}