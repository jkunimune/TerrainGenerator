import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



public final class GodInterface implements MouseListener {
  Map display;
  Surface place;
  
  
  
  public GodInterface(Map newMap, Surface newSfc) {
    display = newMap;
    place = newSfc;
  }
  
  
  
  public final void mouseClicked(MouseEvent e) {	// yet to be reimplemented due to changes in program structure
    //if (e.getButton() == MouseEvent.BUTTON3)
      //place.meatyore(display.getTile(e.getX(), e.getY()-23));
  }
  
  
  public final void mouseExited(MouseEvent e) {
  }
  
  
  public final void mouseEntered(MouseEvent e) {
  }
  
  
  public final void mouseReleased(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1)
      display.hideTileTip();
  }
  
  
  public final void mousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1)
      display.showTileTip(e.getX(), e.getY()-23); // left clicking shows the a TileTip
  }
}