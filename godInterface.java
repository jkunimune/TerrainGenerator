import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



public final class GodInterface implements MouseListener {
  Map display;
  World world;
  
  
  
  public GodInterface(Map newMap, World newGlobe) {
    display = newMap;
    world = newGlobe;
  }
  
  
  
  public final void mouseClicked(MouseEvent e) {
    if (e.getButton() == e.BUTTON3)
      world.meatyore(display.getTile(e.getX(), e.getY()-23));
  }
  
  
  public final void mouseExited(MouseEvent e) {
  }
  
  
  public final void mouseEntered(MouseEvent e) {
  }
  
  
  public final void mouseReleased(MouseEvent e) {
    if (e.getButton() == e.BUTTON1)
      display.hideTileTip();
  }
  
  
  public final void mousePressed(MouseEvent e) {
    if (e.getButton() == e.BUTTON1)
      display.showTileTip(e.getX(), e.getY()-23); // left clicking shows the a TileTip
  }
}