import java.awt.*;
import java.awt.image.*;
import javax.swing.*;



public class Map extends JPanel { // a class to manage the graphic elements of terrain generation
  private static int width;
  private static int height;
  BufferedImage img;
  
  
  
  public Map(int w, int h) {
    width = w;
    height = h;
    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
    setPreferredSize(new Dimension(width, height));
    JFrame frame = new JFrame();
    frame.add(this);
    frame.pack();
    frame.setVisible(true);
  }
  
  
  public void drawPx(int x, int y, Color z) { // draws a pixel to the buffered image
    Graphics g = img.getGraphics();
    g.setColor(z);
    g.drawLine(x, y, x, y);
    g.dispose();
    //repaint();
  }
  
  @Override
  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }
  
  
  public void show() {
    repaint();
  }
  
  
  public void equirectangular(Globe world, String colorScheme) {
    for (int x = 0; x < width; x ++)
      for (int y = 0; y < height; y ++)
        drawPx(x, y, world.getTile(y*Math.PI/height, x*2*Math.PI/width).getColorBy(colorScheme));
    show();
  }
  
  
  public void mercator(Globe world, String colorScheme) {
    for (int x = 0; x < width; x ++)
      for (int y = 0; y < height; y ++)
        drawPx(x, y, world.getTile(Math.PI/2 + Math.atan(((double)y-height/2) / (width/2/Math.PI)), x * 2*Math.PI / width).getColorBy(colorScheme));
    show();
  }
  
  
  public void lambert(Globe world, String colorScheme) {
    for (int x = 0; x < width; x ++)
      for (int y = 0; y < height; y ++)
        drawPx(x, y, world.getTile(Math.PI/2 + Math.asin((y-height/2.0) / (height/2.0)), x * 2*Math.PI / width).getColorBy(colorScheme));
    show();
  }
//  
//  
//  public ??? sinusoidal(Globe world) {
//  }
//  
//  
//  public ??? robinson(Globe world) {
//  }
//  
//  
//  public ??? equidistantConic(Globe world) {
//  }
//  
//  
//  public ??? polar(Globe world) {
//  }
//
//
//  public ??? fromSpace(Globe world) {
//  }
//  
//  
//  public ??? fromSpace3D(Globe world) {
//  }
}