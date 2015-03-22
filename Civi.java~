import java.util.*;



public class Civi {
  public final String[] vowels = {"A", "E", "I", "O", "U", "Y", "Ae", "Ai", "Aw", "Ar", "Ay", "Ea", "Ee", "Er", "Ia", "Ie", "Io", "Ir", "Oa", "Oe",
    "Oi", "Ou", "Oo", "Or"};
  public final String[] consonants = {"Qu", "W", "R", "RR", "Ry", "T", "Th", "Tr", "Y", "P", "Ph", "Pl", "Pr", "Pp", "Py", "S", "Sh", "Shr", "St",
    "Str", "D", "Dr", "F", "Fl", "Fr", "G", "Gh", "Gr", "H", "J", "K", "Kh", "Kl", "Ky", "L", "Ll", "Z", "Zh", "X", "C", "Ch", "Cl", "Ck", "Cr",
    "Cz", "V", "Vr", "B", "Br", "Bl", "N", "Nr", "M", "Mr"};
  
  public ArrayList<Tile> land; // all of the tiles it owns
  private Tile captial; // its capital city
  private String name; // its name
  private String capName; // its captial's name
  private int homeBiome; // the biome it started in
  private int spreadRate; // how fast it spreads
  private int scienceRate; // how fast it advances
  private int scienceLevel; // how much science it has
  private int militaryLevel; // how much military it has
  private int warChance; // how likely it is to wage war
  
  
  
  public Civi(Tile start) {
    captial = start; // set capital and territory
    land = new ArrayList<Tile>(1);
    land.add(start);
    homeBiome = start.biome;
    
    spreadRate = (int)(Math.random()*255); // randomize stats
    scienceRate = (int)(Math.random()*255);
    scienceLevel = 0;
    militaryLevel = (int)(Math.random()*255);
    warChance = (int)(Math.random()*255);
    
    name = ""; // create a name
    int t = (int)(Math.random()*1.5);
    if (t%2 == 1)  name += vowels[(int)(Math.random()*vowels.length)];
    else           name += consonants[(int)(Math.random()*consonants.length)];
    
    do {
      if (t%2 == 0)  name += vowels[(int)(Math.random()*vowels.length)].toLowerCase();
      else           name += consonants[(int)(Math.random()*consonants.length)].toLowerCase();
      t++;
    } while (t%2 != 0 || Math.random() < .4);
    
    switch ((int)(Math.pow(Math.random(),2)*5)) { // puts an ending onto it
      case 1:
        name += "an";
        break;
      case 2:
        name += vowels[(int)(Math.random()*vowels.length)].toLowerCase();
        break;
      case 3:
        break;
      case 4:
        name += "land";
        break;
      default:
        name += "ia";
        break;
    }
    
    if (Math.random() < .3)
      capName = name+" City"; // lots of civis use their own name for their capital
    else {
      capName = ""; // otherwise create a custom captial name
      t = (int)(Math.random()*1.5);
      if (t%2 == 1)  capName += vowels[(int)(Math.random()*vowels.length)];
      else           capName += consonants[(int)(Math.random()*consonants.length)];
      
      do {
        if (t%2 == 0)  capName += vowels[(int)(Math.random()*vowels.length)].toLowerCase();
        else           capName += consonants[(int)(Math.random()*consonants.length)].toLowerCase();
        t++;
      } while (t%2 != 0 || Math.random() < .3);
      
      switch ((int)(Math.pow(Math.random(),1.3)*5)) { // puts an ending onto it
        case 1:
          capName += "town";
          break;
        case 2:
          capName += vowels[(int)(Math.random()*vowels.length)].toLowerCase();
          break;
        case 3:
          break;
        case 4:
          capName += "ville";
          break;
        default:
          capName += " City";
          break;
      }
    }
    
    System.out.println(this+" has been founded in "+capName+"!"); // announces the civi's arrival
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