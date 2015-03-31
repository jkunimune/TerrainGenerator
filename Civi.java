import java.util.*;
import java.awt.*;
import java.util.ArrayList;



public class Civi {
  public final String[] vowels = {"A", "A", "A", "E", "A", "E", "I", "A", "E", "I", "O", "U", "A", "E", "I", "O", "U", "A", "E", "I", "O", "U", "Y",
    "Ae", "Ai", "Ao", "Aw", "Ar", "Ay", "Ea", "Ee", "Er", "Ey", "Ia", "Ie", "Io", "Ir", "Oa", "Oe", "Oi", "Ou", "Oo", "Oo", "Or", "Ol", "Ow"}; // vowels and consonants for name generation
  public final String[] consonants = {"Qu", "W", "R", "R", "RR", "Ry", "T", "T", "T", "T", "T", "Th", "Th", "Tr", "Y", "P", "P", "P", "P", "Ph", "Pl",
    "Pr", "Pp", "Py", "Ps", "S", "S", "S", "S", "S", "Sh", "Sh", "Sh", "Shr", "St", "Str", "D", "D", "D", "D", "Dr", "F", "F", "F", "F", "Fl", "Fr", "G",
    "G", "G", "G", "Gh", "Gr", "H", "H", "H", "J", "J", "J", "K", "K", "K", "K", "K", "Kh", "Kl", "Ky", "Ks", "L", "L", "L", "L", "Ll", "Lh", "Z", "Z",
    "Z", "Z", "Zh", "X", "C", "Ch", "Cl", "Cr","Cz", "V", "V", "Vr", "B", "B", "B", "B", "Br", "Bl", "Bs", "N", "N", "N", "N", "N", "Nr", "Nt", "Nst",
    "Nstr", "Ng", "Nj", "Nh", "M", "M", "M", "M", "M", "Mr", "'"};
  public final int classical = 8192; // science values of the different ages
  public final int iron = 16384;
  public final int imperialist = 24576;
  public final int industrial = 32768;
  public final int modern = 40960;
  public final int space = 49152;
  public final int prosperity = 57344;
  public final int apocalypse = 65536;
  public final int[] explorabilityOf = {0, -46, -50, -42, -46, -34, -30, -14, -34, -46, -54, -46, 0}; // how quickly civis spread over biomes
  public final int[] fertilityOf =     {0, -36, -44, -32, -36, -18, -10, -26, -06, -14, -15, -06, 0}; // how quickly civis develop them
  
  public World world; // the world it belongs to
  public ArrayList<Tile> land; // all of the tiles it owns
  public Tile capital; // its capital city
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
  public ArrayList<Civi> atWarWith;
  
  
  
  public Civi(Tile start, ArrayList<Civi> existing, World wholeNewWorld) {
    world = wholeNewWorld;
    
    capital = start; // set capital and territory
    start.owners.add(this);
    start.development = 2;
    start.isCapital = true;
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
    deathTimer = 32768 + (int)(Math.random()*32768);
    
    atWarWith = new ArrayList<Civi>(0);
    
    name = newName(); // picks a custom name
    capName = newCapName();
    System.out.println(this+" ("+colorName()+") has been founded in "+capName+"!"); // announces the civi's arrival
  }
  
  
  
  public void advance() { // naturally alters stats
    final boolean apocalypseBefore = deathTimer <= 0;
    deathTimer --;
    warChance += (int)(Math.random()*7-3.5);
    militaryLevel += (int)(Math.random()*7-3.5);
    scienceRate += (int)(Math.random()*7-3.5);
    spreadRate += (int)(Math.random()*7-3.5);
    
    scienceLevel += scienceRate;
    if (scienceLevel >= apocalypse && scienceLevel < apocalypse+scienceRate) // starts apocalypse when entering apocalypse age
      deathTimer = 0;
    else if (scienceLevel >= prosperity && scienceLevel < prosperity+scienceRate) // automatically urbanizes or utopianizes capital when possible and starts apocalypse when necessary
      capital.development = 4;
    else if (scienceLevel >= industrial && scienceLevel < industrial+scienceRate)
      capital.development = 3;
    
    if (!apocalypseBefore && deathTimer <= 0) // announces when the apocalypse starts
      System.out.println(this+" has begun to crumble!");
  }
  
  
  public boolean wants(Tile til) { // decides whether civi can claim a tile
    if (til.altitude < 0 && scienceLevel < iron) // civis may not claim ocean prior to the iron age
      return false;
    
    int chance = explorabilityOf[til.biome] + (spreadRate>>3);
    
    if (homeBiome == til.biome) // civis spread fastest in their home biome
      chance += 12;
    
    return randChance(chance);
  }
  
  
  public boolean canUpgrade(Tile til) { // decides if a tile is ready to be upgraded
    if (deathTimer >= 0) {
      switch (til.development) {
        case 1: // til is territory applying for settlement
          if ((til.altitude < 0 || til.biome == Tile.freshwater) && scienceLevel < space) // water biomes may not be settled prior to the space era
            return false;
          
          ArrayList<Tile> adjacent = world.adjacentTo(til); // counts all adjacent tiles
          int waterAdjacency = 0; // if it is adjacent to water
          int settledAdjacency = 0; // how much settlement it is adjacent to
          for (Tile adj: adjacent) {
            if (adj.development > 1 && adj.owners.equals(til.owners))
              settledAdjacency ++;
            if (adj.altitude < 0 || adj.biome == Tile.freshwater)
              waterAdjacency = 30;
          }
        
          for (int i = 0; i < settledAdjacency; i ++)
            if (randChance(fertilityOf[til.biome] + waterAdjacency))
              return true;
        
          if (til.development == 1 && scienceLevel >= imperialist && randChance(fertilityOf[til.biome]+waterAdjacency-100)) // if still unsettled and civi is in imperialist age
            return true; // it might get settled
          return false;
        
        case 2: // til is settlement applying for urbanization
          if (scienceLevel < industrial) // urbanization may not happen prior to industrial era
            return false;
          
          adjacent = world.adjacentTo(til); // counts all adjacent tiles
          waterAdjacency = -20; // if it is adjacent to water
          int urbanAdjacency = 0; // how much urbanization it is adjacent to
          
          for (Tile adj: adjacent) {
            if (adj.development > 2) // cities can spread from civi to civi
              urbanAdjacency ++;
            if (adj.altitude < 0 || adj.biome == Tile.freshwater)
              waterAdjacency = 5;
          }
        
          for (int i = 0; i < urbanAdjacency; i ++) // urbanization is like settlement but adjacency does not matter as much
            if (randChance(-40) || scienceLevel > space) // after the space age cities grow like crazy
              return true;
        
          if (til.development == 2 && randChance(waterAdjacency-90)) // if still unurbanized
            return true; // it might get urbanized
          return false;
        
        case 3: // til is urban applying for utopia
          if (scienceLevel < prosperity) // utopianization is not possible before prosperity age
            return false;
          
          ArrayList<Tile> adjacento = world.adjacentTo(til); // counts all adjacent tiles
          int utopiaAdjacency = 0; // how much utopia it is adjacent to
          
          for (Tile adj: adjacento)
            if (adj.development > 3 && adj.owners.equals(til.owners))
              utopiaAdjacency ++;
        
          if (utopiaAdjacency > 0) { // if it is adjacent to utopia
            if (randChance(8-8*utopiaAdjacency)) // utopia spreads not in circles, but in fractally spires that spread best in big urban areas
              return true;
          }
          else { // it it needs to seed
            if (randChance(-110))
              return true;
          }
          return false;
        default:
          return false;
      }
    }
    else { // if this is the apocalypse, don't upgrade
      ArrayList<Tile> adjacent = world.adjacentTo(til); // counts all adjacent tiles
      for (Tile adj: adjacent) {
        if (!adj.owners.equals(til.owners) && randChance(-(deathTimer>>7) - 40)) { // causes lands to be undeveloped during apocalypse
          if (!til.isCapital || randChance(-20)) // captials are less likely to be lost
            loseGraspOn(til);
        }
      }
      if (!til.isCapital && randChance(-(deathTimer>>7) - 100))
        loseGraspOn(til);
      return false; // nothing may be upgraded during the apocalypse
    }
  }
  
  
  public boolean canInvade(Tile til) {
    if (til.biome == homeBiome)
      return randChance((militaryLevel>>3) + explorabilityOf[til.biome] + 10);
    else
      return randChance((militaryLevel>>3) + explorabilityOf[til.biome] - 10);
  }
  
  
  public boolean cannotDefend(Tile til) {
    if (til.biome == homeBiome)
      return randChance(-(militaryLevel>>3) + explorabilityOf[til.biome] + 10);
    else
      return randChance(-(militaryLevel>>3) + explorabilityOf[til.biome] - 10);
  }
  
  
  public void takes(Tile t) { // add a tile to this empire
    t.owners.add(this);
    if (t.development == 0)
      t.development = 1;
    land.add(t);
    deathTimer -= 3;
  }
  
  
  public void loseGraspOn(Tile t) { // undevelop or remove a tile from this empire
    if (t.development == 2 || t.development == 3)
      t.development = 1;
    else {
      t.owners.remove(this);
      if (t.owners.size() == 0)
        t.development = 0;
      land.remove(t);
    }
  }
  
  
  public void failsToDefend(Tile t) { // lose a tile to another empire
    t.owners.remove(this);
    if (t.owners.size() == 0)
      t.development = 0;
    land.remove(t);
  }
  
  
  public boolean wantsWar() { // if the Civi feels like starting a war
    return randChance((warChance>>3) - 120);
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
    } while (t%2 != 0 || Math.random() < .3);
    
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
      } while (Math.random() < .2);
      
      switch ((int)(Math.pow(Math.random(),1.4)*4)) { // puts an ending onto it
        case 1:
          output += " City";
          break;
        case 2:
          output += "town";
          break;
        case 3:
          output += "ville";
          break;
        default:
          output = "The City of " + output;
          break;
      }
    }
    
    return output;
  }
  
  
  public int chooseColor(int intolerance, ArrayList<Civi> existing) {
    int hue = (int)(Math.random()*1530+1);
    
    for (Civi c: existing) {
      if (Math.abs(hue-c.hueNumber()) < intolerance) {
        hue = chooseColor(intolerance-1, existing); // makes sure the color is not too close to any existing ones
        break;
      }
    }
    
    return hue;
  }
  
  
  public Color intToColor(int hue) { // converts hue to color
    if (hue <= 255 && hue > 0)
      return new Color(255, hue, 0); // orange
    else if (hue <= 510)
      return new Color(510-hue, 255, 0); // chartreuse
    else if (hue <= 765)
      return new Color(0, 255, hue-510); // teal
    else if (hue <= 1020)
      return new Color(0, 1020-hue, 255); // aquamarine
    else if (hue <= 1275)
      return new Color(hue-1020, 0, 255); // purple
    else
      return new Color(255, 0, 1530-hue); // maroon
  }
  
  
  public int hueNumber() {
    if (emblem.getRed() == 255 && emblem.getGreen() <= 255 && emblem.getGreen() > 0) // orange
      return emblem.getGreen();
    
    else if (emblem.getRed() < 255 && emblem.getGreen() == 255) // chartreuse
      return 510-emblem.getRed();
    
    else if (emblem.getGreen() == 255 && emblem.getBlue() <= 255) // teal
      return 510+emblem.getBlue();
    
    else if (emblem.getGreen() < 255 && emblem.getBlue() == 255) // aquamarine
      return 1020-emblem.getGreen();
    
    else if (emblem.getBlue() == 255 && emblem.getRed() <= 255) // purple
      return 1020+emblem.getRed();
    
    else if (emblem.getBlue() < 255 && emblem.getRed() == 255) // maroon
      return 1530-emblem.getBlue();
    
    else
      return -1;
  }
  
  
  public final String colorName() {
    if (emblem.getRed() >= 128 && emblem.getBlue() < 128 && emblem.getGreen() < 64)
      return "red";
    else if (emblem.getRed() == 255 && emblem.getBlue() >= 128 && emblem.getGreen() < 128)
      return "magenta";
    else if (emblem.getRed() >= 128 && emblem.getBlue() >= 128 && emblem.getGreen() < 128)
      return "violet";
    else if (emblem.getRed() < 128 && emblem.getBlue() >= 128 && emblem.getGreen() < 196)
      return "blue";
    else if (emblem.getRed() < 128 && emblem.getBlue() >= 128 && emblem.getGreen() >= 196)
      return "cyan";
    else if (emblem.getRed() < 128 && emblem.getBlue() < 128 && emblem.getGreen() >= 128)
      return "green";
    else if (emblem.getRed() >= 128 && emblem.getBlue() < 128 && emblem.getGreen() >= 196)
      return "yellow";
    else if (emblem.getRed() >= 128 && emblem.getBlue() < 128 && emblem.getGreen() >= 64)
      return "orange";
    else
      return "unaccounted color";
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