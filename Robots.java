import java.util.*;
import java.awt.*;
import org.apache.commons.lang3.RandomStringUtils;



public class Robots extends Civi {
  private int year; // robots need to know what year it is
  
  
  
  public Robots(Tile start, ArrayList<Civi> existing, World wholeNewWorld) {
    super(start, existing, wholeNewWorld);
    year = 0;
  }
  
  
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
  public boolean hasDiscontent(boolean urban) { // human rebellions are the only random fator for robots
    if (sciLevel() < classical || dthTimer() < 0) // the ancient age is too early to start a revolution, and the apocalypse is too late
      return false;
    if (urban)  return super.randChance((warChance()>>5) - (milLevel()>>6) + (land.size()>>14) - (dthTimer()>>14) - 130); // big old weak warmongers are more likely to have revolutions
    else        return super.randChance((warChance()>>5) - (milLevel()>>6) + (land.size()>>14) - (dthTimer()>>14) - 160); // urban areas are more likely to seed revolutions
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