import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class DrawingTest extends JPanel {
  
  public static void main(String[] args) {
    new DrawingTest();
  }
  
  final static int width = 1000;
  final static int height = 1000;
  float i = 1;
  BufferedImage img;
  
  DrawingTest() {
    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
    setPreferredSize(new Dimension(width, height));
    JFrame frame = new JFrame();
    frame.add(this);
    frame.pack();
    frame.setVisible(true);
    
    //START GENERATING TERRAIN//
    for (int row = 0; row < height; row++) {
      i+=.05;
      for (int col = 400; col < 600; col++) {
        DrawPixel(row, col);
      }
      long startTime = System.currentTimeMillis();
      while (System.currentTimeMillis() - startTime < (int)(i)) {
      }
    }
    repaint();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // good call!
  }
  
  public void DrawPixel(int x, int y) {
    Graphics g = img.getGraphics();
    g.setColor(Color.red);
    g.drawLine(x, y, x, y);
    g.dispose();
    //repaint();
  }
  
  @Override
  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }
}