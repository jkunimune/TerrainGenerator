import java.awt.*;
import java.awt.image.*;
import javax.swing.*;



public final class TileTip extends JPanel { // just writes words to the screen
  private BufferedImage img;
  
  
  
  public TileTip() {
    JFrame frame = new JFrame();
    img = new BufferedImage(300, 200, BufferedImage.TYPE_INT_ARGB_PRE);
    img.getGraphics().setColor(Color.black);
    setPreferredSize(new Dimension(300, 200));
    frame.add(this);
    frame.setResizable(false);
    frame.pack();
    frame.setVisible(true);
  }
  
  
  
  public final void drawTileTip(String tip, int x, int y) {
    final Graphics g = img.getGraphics();
    g.drawString(tip, x, y);
    g.dispose();
  }
  
  
  @Override
  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }
  
  
  public void show() {
    repaint();
  }
}