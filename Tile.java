import java.util.*;



public final class Tile { // keeps track of a single point on a globe
  public int lat; // lattitude (actually an array index)
  public int lon; // longitude (actually an array index)
  public int altitude; // sea level is 0, goes from -256 to 255
  public int temperature; // temperature from 0 to 255
  public int rainfall; // measure of how wet a climate is from 0 (parched) to 255 (Kauai)
  public int water; // the freshwater level from 0 to 255
  public int biome; // see key below
  public int development; // how settled it is
  public ArrayList<Civi> owners;
  public boolean isCapital; // if it is a capital city
  public boolean radioactive;
  public int temp1; // to store various values only necessary during generation
  public int temp2;
  public int temp3;
  
  public static final int magma = 0; // biome values
  public static final int ocean = 1;
  public static final int ice = 2;
  public static final int reef = 3;
  public static final int trench = 4;
  public static final int tundra = 5;
  public static final int plains = 6;
  public static final int desert = 7;
  public static final int jungle = 8;
  public static final int mountain = 9;
  public static final int snowcap = 10;
  public static final int freshwater = 11;
  public static final int space = 12;
  public static final String[] biomeNames = {"magma","ocean","ice","coral reef","trench","tundra","plains","desert","jungle","mountains","snowy mountains",
    "freshwater","space"};
  public static final String[] developmentNames = {"Some unclaimed ", "Some frontier", "Some settlement", "A city", "A utopia", "An error"};
  
  
  
  public Tile(int newLat, int newLon) { // initializes with default values
    lat = newLat;
    lon = newLon;
    altitude = -257;
    temperature = 256;
    rainfall = -1;
    water = 0;
    biome = 0;
    development = 0;
    owners = new ArrayList<Civi>(1);
  }
  
  
  public Tile(int newLat, int newLon, int newAlt, int newTemp, int newRain, int newWater, int newBiome) { // initializes with given values
    lat = newLat;
    lon = newLon;
    altitude = newAlt;
    temperature = newTemp;
    rainfall = newRain;
    water = newWater;
    biome = newBiome;
    development = 0;
    owners = new ArrayList<Civi>(1);
  }
  
  
  public Tile(Tile source) { // copies another tile
    lat = source.lat;
    lon = source.lon;
    altitude = source.altitude;
    temperature = source.temperature;
    rainfall = source.rainfall;
    water = source.water;
    biome = source.biome;
    temp1 = source.temp1;
    temp2 = source.temp2;
    temp3 = source.temp3;
    development = source.development;
    owners = source.owners;
  }
  
  
  
  public final void randomize() { // randomizes the tile's biome for testing purposes
    biome = (int)(Math.random()*9+1);
    altitude = (int)(Math.random()*512-256);
  }
  
  
  public final void spreadFrom(Tile source) { // joins the continental plate of another tile (but only temporarily; remember to copy temp1 to biome!)
    temp1 = source.altitude;
    temp2 = source.temp2; // temp2 represents lattitudinal component of new motion
    temp3 = source.temp3; // temp3 represents longitudinal component of new motion
  }
  
  
  public final void startPlate(boolean wet) { // becomes the seed for a continental plate
    if (wet)  temp1 = (int)(Math.random()*16-72); // randomizes altitude from -72 to -56
    else      temp1 = (int)(Math.random()*16+56); // randomizes altitude from 56 to 72
    temp2 = (int)(Math.random()*Math.PI*128); // randomizes drift velocity
    temp3 = (int)(Math.random()*2*Math.PI*128); // these numbers represent a vector, so they are coordinates representing a point on the axis of the plate's rotation, which also goes through the center of the sphere
  }
  
  
  public final int waterLevel() {
    return altitude+water;
  }
  
  
  public final void getsNuked() { // causes this tile to be radiated and killed
    radioactive = true;
    development = 0;
    for (Civi civ: owners)
      civ.land.remove(this);
    owners.clear();
  }
  
  
  public final void getsHitByMeteor() { // causes this tile to be partially destroyed
    if (development < 4) {
      development = 0;
      owners.clear();
      for (Civi civ: owners)
        civ.land.remove(this);
    }
  }
  
  
  public final String[] getTip() { // returns the TileTip
    String[] output = new String[owners.size() + 1];
    output[0] = developmentNames[development]; // starts with the development level
    
    if (development == 0)
      output[0] += biomeNames[biome]; // unclaimed land prints out the biome
    
    else if (owners.size() == 1) { // owned land prints out the owner on a new line
      output[0] += " owned by";
      output[1] = owners.get(0).toString();
    }
    
    else { // disputed land prints out all owners
      output[0] += " disputed by";
      for (int i = 0; i < owners.size(); i ++)
        output[i+1] = owners.get(i).toString();
    }
    
    int maxLen = 0;
    for (String line: output)
      if (line.length() > maxLen) // figures out how long the longest line is
        maxLen = line.length();
    
    while (output[0].length() < maxLen) // stacks spaces on the end of the first line to make it the longest
      output[0] += " ";
    
    return output;
  }
  
  
  public final String toString() {
    return "the "+biomeNames[biome]+" tile at "+lat+", "+lon;
  }
}