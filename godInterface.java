import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



public final class GodInterface implements MouseListener {
  Map display;
  Globe world;
  
  
  
  public GodInterface(Map newMap, Globe newGlobe) {
    display = newMap;
    world = newGlobe;
  }
  
  
  
  public final void mouseClicked(MouseEvent e) {
  }
  
  
  public final void mouseExited(MouseEvent e) {
  }
  
  
  public final void mouseEntered(MouseEvent e) {
  }
  
  
  public final void mouseReleased(MouseEvent e) {
    display.hideTileTip();
  }
  
  
  public final void mousePressed(MouseEvent e) {
    display.showTileTip(e.getX(), e.getY()-23); // clicking shows the a TileTip
  }
}