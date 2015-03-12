import java.awt.*;



public class Mercator extends Map { // a sphere projection designed to preserve shapes but distorts size near poles, which are not visible
  public Mercator(Globe g, int x, int y) {
    super(g, x, y);
  }
  
  
  
  public void display(String colorScheme) {
    for (int x = 0; x < width(); x ++) {
      for (int y = 0; y < height(); y ++) {
        if ((x/2*2 == width()/10/2*2 || x/2*2 == width()*9/10/2*2) && y/7%2 == 0)
          drawPx(x, y, Color.black);
        else
          drawPx(x, y, getColorBy(colorScheme, x, y));
      }
    }
    show();
  }
  
  
  public double getLat(int x, int y) {
    return Math.PI/2 + Math.atan(((double)y-height()*5/8) / (width()/2/Math.PI));
  }
  
  
  public double getLon(int x, int y) {
    return (x*5/4)%width() * 2*Math.PI / width();
  }
}