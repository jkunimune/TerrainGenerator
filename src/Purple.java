import java.util.*;
import java.awt.*;



public class Purple extends Civi { // an easter-egg Civi based on a glitch in my original Civi
  public Purple(Tile start, ArrayList<Civi> existing, World wholeNewWorld) {
    super(start, existing, wholeNewWorld);
  }
  
  
  
  @Override
  public Color chooseColor(int intolerance, ArrayList<Civi> existing) {
    return new Color(150, 0, 255);
  }
  
  
  @Override
  public String newName() {
    return "The Mongolian Empire"; // this Civ's name is "The Mongolian Empire"
  }
  
  
  @Override
  public String colorName() {
    return "purple";
  }
  
  
  @Override
  public final boolean randChance(int p) { // like randChance, but less random
    return true;
  }
  
  
  public String toString() { // says "The Mongols"
    return "The Mongols";
  }
}