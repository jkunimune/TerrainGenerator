public class Robinson extends Map { // a compromise globe projection that was used by National Geographic for some time
  private static int width;
  private static int height;
  
  
  
  public Robinson(Globe g, int x, int y) {
    super(g, x, y);
  }
  
  
  
  public void display(String colorScheme) {
    for (int x = 0; x < width; x ++)
      for (int y = 0; y < height; y ++)
        drawPx(x, y, getColorBy(colorScheme, glb.getTile(y*Math.PI/height, x*2*Math.PI/width)));
    show();
  }
}