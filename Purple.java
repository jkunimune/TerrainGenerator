import java.util.*;
import java.awt.*;
import org.apache.commons.lang3.RandomStringUtils;



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
    return "Purple"; // this Civ's name is "Purple" names are random strings
  }
  
  
  @Override
  public final boolean randChance(int p) { // like randChance, but less random
    return true;
  }
  
  
  public String toString() { // says "Purple"
    return "Purple";
  }
}