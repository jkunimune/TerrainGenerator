import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



public class GodInterface implements MouseListener {
  Map display;
  Globe world;
  
  
  
  public GodInterface() {
  }
  
  
  
  public void mouseClicked(MouseEvent e) {
    
  }
  
  
  public void mouseExited(MouseEvent e) {
  }
  
  
  public void mouseEntered(MouseEvent e) {
  }
  
  
  public void mouseReleased(MouseEvent e) {
    if (e.getButton() == e.BUTTON1)
      System.out.println("I see you have released left click");
  }
  
  
  public void mousePressed(MouseEvent e) {
    if (e.getButton() == e.BUTTON1)
      System.out.println("I see you are left clicking on "+e.getX()+", "+(e.getY()-23));
  }
}