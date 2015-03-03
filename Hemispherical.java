public class Hemispherical extends Map { // a realistic globe projection that shows each hemisphere seperately and resembles FromSpace
  int radius;
  
  
  
  public Hemispherical(Globe g, int x, int y) {
    super(g, x, y);
    if (x/2 < y)  radius = x/4;
    else          radius = y/2;
  }
  
  
  
  public void display(String colorScheme) {
    for (int x = 0; x < 2*radius; x ++) { // displays western hemisphere
      for (int y = 0; y < 2*radius; y ++) {
        if ((x-radius)*(x-radius) + (y-radius)*(y-radius) < radius*radius) {
          double longitude;
          if (x > radius) // if x is on left of circle
            longitude = Math.atan((double)(y-radius)/(x-radius));
          else if (x < radius) // if point is on right of circle
            longitude = Math.atan((double)(y-radius)/(x-radius)) + Math.PI;
          else { // if point is on vertical line of symetry
            if (y > radius)  longitude = Math.PI/2;
            else        longitude = 3*Math.PI/2;
          }
          double lattitude = Math.sqrt((x-radius)*(x-radius) + (y-radius)*(y-radius))*Math.PI/radius;
          
          drawPx(x, y, getColorBy(colorScheme, glb.getTile(lattitude, longitude)));
        }
      }
    }
    
    for (int x = 2*radius; x < 4*radius; x ++) { // displays eastern hemisphere
      for (int y = 0; y < 2*radius; y ++) {
        if ((x-3*radius)*(x-3*radius) + (y-radius)*(y-radius) < radius*radius) {
          double longitude;
          if (x > radius) // if x is on left of circle
            longitude = Math.atan((double)(y-radius)/(x-radius));
          else if (x < radius) // if point is on right of circle
            longitude = Math.atan((double)(y-radius)/(x-radius)) + Math.PI;
          else { // if point is on vertical line of symetry
            if (y > radius)  longitude = Math.PI/2;
            else        longitude = 3*Math.PI/2;
          }
          double lattitude = Math.sqrt((x-radius)*(x-radius) + (y-radius)*(y-radius))*Math.PI/radius;
          
          drawPx(x, y, getColorBy(colorScheme, glb.getTile(lattitude, longitude)));
        }
      }
    }
    show();
  }
}