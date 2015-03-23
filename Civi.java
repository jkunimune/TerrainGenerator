import java.util.*;
import java.awt.*;



public class Civi {
  public final String[] vowels = {"A", "E", "I", "O", "U", "A", "E", "I", "O", "U", "Y", "Ae", "Ai", "Aw", "Ar", "Ay", "Ea", "Ee", "Er", "Ia", "Ie", "Io", "Ir", "Oa", "Oe",
    "Oi", "Ou", "Oo", "Oo", "Or"};
  public final String[] consonants = {"Qu", "W", "R", "RR", "Ry", "T", "T", "Th", "Tr", "Y", "P", "P", "Ph", "Pl", "Pr", "Pp", "Py", "S", "Sh", "Shr", "St",
    "Str", "D", "D", "Dr", "F", "F", "Fl", "Fr", "G", "Gh", "Gr", "H", "J", "K", "K", "Kh", "Kl", "Ky", "L", "Ll", "Z", "Zh", "X", "C", "Ch", "Cl", "Ck", "Cr",
    "Cz", "V", "Vr", "B", "B", "Br", "Bl", "N", "Nr", "M", "Mr", "'"};
  public final int classical = 4096;
  public final int ironAge = 8192;
  
  public ArrayList<Tile> land; // all of the tiles it owns
  private Tile captial; // its capital city
  private String name; // its name
  private String capName; // its captial's name
  private Color emblem; // its special color
  private int homeBiome; // the biome it started in
  private int spreadRate; // how fast it spreads
  private int scienceRate; // how fast it advances
  private int scienceLevel; // how much science it has
  private int militaryLevel; // how much military it has
  private int warChance; // how likely it is to wage war
  private int deathTimer; // how soon it will die (negative means apocalype is in progress)
  
  
  
  public Civi(Tile start, ArrayList<Civi> existing) {
    captial = start; // set capital and territory
    start.owners.add(this);
    start.ownership = 2;
    land = new ArrayList<Tile>(1);
    land.add(start);
    homeBiome = start.biome;
    
    int hueNo = chooseColor(256, existing); // pick color
    emblem = intToColor(hueNo);
    
    spreadRate = (int)(Math.random()*255); // randomize stats
    scienceRate = (int)(Math.random()*255);
    scienceLevel = 0;
    militaryLevel = (int)(Math.random()*255);
    warChance = (int)(Math.random()*255);
    deathTimer = 1024 + (int)(Math.random()*1024);
    
    name = newName(); // picks a custom name
    capName = newCapName();
    System.out.println(this+" has been founded in "+capName+"!"); // announces the civi's arrival
  }
  
  
  
  public void advance() { // naturally alters stats
    deathTimer -= (int)(Math.random()*2);
    warChance += (int)(Math.random()*7-3.5);
    militaryLevel += (int)(Math.random()*7-3.5);
    scienceLevel += scienceRate;
    scienceRate += (int)(Math.random()*7-3.5);
    spreadRate += (int)(Math.random()*7-3.5);
  }
  
  
  public boolean wants(Tile til) { // decides whether civi can claim a tile
    int chance = -30;
    switch (til.biome) {
      case Tile.magma:
        return false;
      case Tile.ocean:
        if (scienceLevel < ironAge)  return false; // civis spread slow in ocean (after iron age)
        else                         chance -= 12;
        break;
      case Tile.ice:
        if (scienceLevel < ironAge)  return false; // slower in ice (after iron age)
        else                         chance -= 16;
        break;
      case Tile.reef:
        if (scienceLevel < ironAge)  return false; // not as slow in reefs (after iron age)
        else                         chance -= 8;
        break;
      case Tile.trench:
        if (scienceLevel < ironAge)  return false; // slow in trenches (after iron age)
        else                         chance -= 8;
        break;
      case Tile.tundra:
        chance -= 4; // kind of slow in tundra
        break;
      case Tile.plains:
        break; // normal speed in plains
      case Tile.desert:
        chance += 16; // really fast in desert
        break;
      case Tile.jungle:
        chance -= 4; // kind of slow in jungle
        break;
      case Tile.mountain:
        chance -= 8; // slow over mountains
        break;
      case Tile.snowcap:
        chance -= 12; // even slower over mountains
        break;
      case Tile.freshwater:
        chance -= 16; // super slow over rivers
        break;
      case Tile.space:
        return false;
    }
    if (homeBiome == til.biome) // civis spread fastest in their home biome
      chance += 8;
    
    return randChance(chance);
  }
  
  
  public String newName() {
    String output = ""; // create a name
    
    int t = (int)(Math.random()*1.5);
    if (t%2 == 1)  output += vowels[(int)(Math.random()*vowels.length)];
    else           output += consonants[(int)(Math.random()*consonants.length)];
    
    do {
      if (t%2 == 0)  output += vowels[(int)(Math.random()*vowels.length)].toLowerCase();
      else           output += consonants[(int)(Math.random()*consonants.length)].toLowerCase();
      t++;
    } while (t%2 != 0 || Math.random() < .4);
    
    switch ((int)(Math.pow(Math.random(),2)*5)) { // puts an ending onto it
      case 1:
        output += "an";
        break;
      case 2:
        output += vowels[(int)(Math.random()*vowels.length)].toLowerCase();
        break;
      case 3:
        break;
      case 4:
        output += "land";
        break;
      default:
        output += "ia";
        break;
    }
    
    return output;
  }
  
  
  public String newCapName() {
    String output = "";
    
    if (Math.random() < .2)
      output = name+" City"; // lots of civis use their own name for their capital
    else {
      output = ""; // otherwise create a custom captial name
      int t = (int)(Math.random()*1.5);
      if (t%2 == 1)  output += vowels[(int)(Math.random()*vowels.length)];
      else           output += consonants[(int)(Math.random()*consonants.length)];
      
      do {
        if (t%2 == 0)  output += vowels[(int)(Math.random()*vowels.length)].toLowerCase();
        else           output += consonants[(int)(Math.random()*consonants.length)].toLowerCase();
        t++;
      } while (t%2 != 0 || Math.random() < .3);
      
      switch ((int)(Math.pow(Math.random(),1.2)*5)) { // puts an ending onto it
        case 1:
          output += "town";
          break;
        case 2:
          output = "The City of " + output + vowels[(int)(Math.random()*vowels.length)].toLowerCase();
          break;
        case 3:
          output = "The City of " + output;
          break;
        case 4:
          output += "ville";
          break;
        default:
          output += " City";
          break;
      }
    }
    
    return output;
  }
  
  
  public int chooseColor(int tolerance, ArrayList<Civi> existing) {
    int hue = (int)(Math.random()*1530);
    
    for (Civi c: existing)
      if (Math.abs(hue-c.hueNumber()) < tolerance)
        hue = chooseColor(tolerance>>1, existing); // makes sure the color is not too close to any existing ones
    
    return hue;
  }
  
  
  public Color intToColor(int hue) {
    if (hue < 256)
      return new Color(255, hue, 0); // converts hue to color
    else if (hue < 512)
      return new Color(511-hue, 255, 0);
    else if (hue < 767)
      return new Color(0, 255, hue-511);
    else if (hue < 1023)
      return new Color(0, 1022-hue, 255);
    else if (hue < 1278)
      return new Color(hue-1022, 0, 255);
    else
      return new Color(255, 0, 1533-hue);
  }
  
  
  public int hueNumber() {
    if (emblem.getRed() == 255 && emblem.getGreen() < 255) // orange
      return emblem.getGreen();
    else if (emblem.getRed() < 255 && emblem.getGreen() == 255) // chartreuse
      return 511-emblem.getRed();
    else if (emblem.getGreen() == 255 && emblem.getBlue() < 255) // teal
      return 511+emblem.getBlue();
    else if (emblem.getGreen() < 255 && emblem.getBlue() == 255) // aquamarine
      return 1022-emblem.getGreen();
    else if (emblem.getBlue() == 255 && emblem.getRed() < 255) // purple
      return 1022+emblem.getRed();
    else if (emblem.getBlue() < 255 && emblem.getRed() == 255) // maroon
      return 1533-emblem.getBlue();
    else
      return -1;
  }
  
  
  public Color emblem() {
    return emblem;
  }
  
  
  public final boolean randChance(int p) { // scales an int to a probability and returns true that probability of the time
    return Math.random() < 1 / (1+Math.pow(Math.E, -.1*p));
  }
  
  
  public String toString() { // names empire based on characteristics
    if (spreadRate > scienceRate && spreadRate > militaryLevel && spreadRate > warChance) // if this Civi is known for is massive territory
      return "The Great Empire of "+name;
    else if (scienceRate > militaryLevel && scienceRate > warChance) // if this Civi is known for its technological prowess
      return "The Glorious Empire of "+name;
    else if (militaryLevel > warChance) // if this Civi is known for its powerful military
      return "The Powerful Empire of "+name;
    else // if this Civi is a warmonger
      return "The Mighty Empire of "+name;
  }
}