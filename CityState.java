import java.util.*;
import java.awt.*;



public class CityState extends Civi { // a Civi that expands to only a few tiles and will not wage war
  private int year;
  
  
  
  public CityState(Tile start, ArrayList<Civi> existing, World wholeNewWorld) {
    super(start, existing, wholeNewWorld);
    year = 0;
  }
  
  
  public CityState(Tile start, ArrayList<Civi> existing, World wholeNewWorld, Civi motherland) {
    super(start, existing, wholeNewWorld, motherland);
    year = 0;
  }
  
  
  
  @Override
  public void advance() {
    super.advance();
    year ++;
  }
  
  
  @Override
  public boolean wants(Tile til) { // human rebellions are the only random fator for robots
    /*if (til.altitude < 0 && sciLevel() < iron) // civis may not claim ocean prior to the iron age
      return false;
    
    if (til.radioactive && (sciLevel() < space || randChance((sciLevel()>>11) - 70))) // radioactive tiles must be cleared by an advanced Civi
      return false;
    til.radioactive = false;
    
    int chance = explorabilityOf[til.biome] + (spdRate()>>3);
    
    if (home() == til.biome) // civis spread fastest in their home biome
      chance += 12;
    
    chance -= year;*/
    
    return super.wants(til) && randChance(-year);
  }
  
  
  @Override
  public boolean wantsWar() {
    return false;
  }
  
  
  @Override
  public String newName() {
    String output = "";
    
    output = ""; // otherwise create a custom captial name
    
    for (int i = (int)(Math.random()*2)*4+2; i >= 0; i --)
      output += alphabet[i%4][(int)(Math.random()*alphabet[i%4].length)]; // strings together a bunch of random syllables
    for (int j = 0; j < output.length()-1; j ++) // capitalizes all words
      if (j == 0 || output.charAt(j-1) == ' ' || output.charAt(j-1) == '-' || output.charAt(j-1) == '\'')
      output = output.substring(0,j) + output.substring(j, j+1).toUpperCase() + output.substring(j+1);
    
    switch ((int)(Math.pow(Math.random(),2.2)*6)) { // puts an ending onto it
      case 1:
        output += " City";
        break;
      case 2:
        output += alphabet[2][(int)(Math.random()*alphabet[2].length)];
        break;
      case 3:
        output += alphabet[3][(int)(Math.random()*alphabet[3].length)] + "ia";
        break;
      case 4:
        output += "town";
        break;
      case 5:
        output += "ville";
        break;
      default:
        break;
    }
    
    return output;
  }
  
  
  @Override
  public String newCapName() {
    return name();
  }
  
  
  public String toString() { // names empire based on characteristics
    return "The Sovereign City-State of "+name();
  }
}