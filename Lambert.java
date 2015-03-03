public class Lambert extends Map { // an equal area globe projection that distorts shape severely near poles, which are lines
  public Lambert(Globe g, int x, int y) {
    super(g, x, y);
  }
  
  
  
  public void display(String colorScheme) {
    for (int x = 0; x < width; x ++)
      for (int y = 0; y < height; y ++)
        drawPx(x, y, getColorBy(colorScheme, glb.getTile(Math.PI/2 + Math.asin((y-height/2.0) / (height/2.0)), (x*5/4)%width * 2*Math.PI / width)));
    show();
  }
}