public class Equirectangular extends Map { // a simple globe projection that is easy to calculate
  public Equirectangular(Globe g, int x, int y) {
    super(g, x, y);
  }
  
  
  
  public void display(String colorScheme) {
    for (int x = 0; x < width; x ++)
      for (int y = 0; y < height; y ++)
        drawPx(x, y, getColorBy(colorScheme, glb.getTile(y*Math.PI/height, (x*5/4)%width*2*Math.PI/width)));
    show();
  }
}