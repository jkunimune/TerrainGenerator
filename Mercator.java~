public class Mercator extends Map { // a sphere projection designed to preserve shapes but distorts size near poles, which are not visible
  public Mercator(Globe g, int x, int y) {
    super(g, x, y);
  }
  
  
  
  public void display(String colorScheme) {
    for (int x = 0; x < width; x ++)
      for (int y = 0; y < height; y ++)
        drawPx(x, y, getColorBy(colorScheme, glb.getTile(Math.PI/2 + Math.atan(((double)y-height*5/8) / (width/2/Math.PI)), (x*5/4)%width * 2*Math.PI / width)));
    show();
  }
}