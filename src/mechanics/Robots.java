package mechanics;
import java.util.*;
import org.apache.commons.lang3.RandomStringUtils;



public class Robots extends Civi {
  private int year; // robots need to know what year it is
  
  
  
  public Robots(Tile start, ArrayList<Civi> existing, World wholeNewWorld, Civi motherland) {
    super(start, existing, wholeNewWorld, motherland);
    year = 0;
  }
  
  
  
  @Override
  public void advance() {
    super.advance();
    year ++;
  }
  
  
  @Override
  public boolean hasDiscontent(Tile til) { // human rebellions are the only random fator for robots
    if (sciLevel() < classical || dthTimer() < 0) // the ancient age is too early to start a revolution, and the apocalypse is too late
      return false;
    if (super.randChance(120 - (warChance()>>5) + (milLevel()>>6) - (land.size()>>14) + (dthTimer()>>13))) // big old weak warmongers are more likely to have revolutions in cities
      return false;
    
    for (Tile adj: til.adjacent)
      if (!adj.owners.equals(til.owners) || adj.altitude < 0) // revolutions must happen on borders
        return true;
    return false;
  }
  
  
  @Override
  public String newName() {
    return "" + RandomStringUtils.randomAlphanumeric(5); // robot names are random strings
  }
  
  
  @Override
  public String newCapName() {
    return "The City "+ RandomStringUtils.randomAlphanumeric(5);
  }
  
  
  @Override
  public final boolean randChance(int p) { // like randChance, but less random
    return year % (int)(1+Math.pow(Math.E, -.1*p)) == 0;
  }
  
  
  public String toString() { // names empire based on characteristics
    return "The Robotic Empire "+name();
  }
}