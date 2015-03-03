public class FromSpace extends Map { // a globe projection designed to mimic a photograph from space
  public FromSpace(Globe g, int x, int y) {
    super(g, x, y);
  }
  
  
  
  public void display(String colorScheme) {
    for (int x = 0; x < width; x ++)
      for (int y = 0; y < height; y ++)
        drawPx(x, y, getColorBy(colorScheme, glb.getTile(y*Math.PI/height, x*2*Math.PI/width)));
    show();
  }
}