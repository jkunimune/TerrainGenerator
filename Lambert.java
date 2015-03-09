import java.awt.*;



public class Lambert extends Map { // an equal area globe projection that distorts shape severely near poles, which are lines
  public Lambert(Globe g, int x, int y) {
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
    return Math.PI/2 + Math.asin((y-height()/2.0) / (height()/2.0));
  }
  
  
  public double getLon(int x, int y) {
    return (x*5/4)%width() * 2*Math.PI / width();
  }
}