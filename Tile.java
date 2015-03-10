public class Tile { // keeps track of a single point on a globe
  public int lat; // lattitude (actually an array index)
  public int lon; // longitude (actually an array index)
  public int altitude; // sea level is 0, goes from -256 to 255
  public int temperature; // temperature from 0 to 255
  public int rainfall; // measure of how wet a climate is from 0 (parched) to 255 (Kauai)
  public int water; // the freshwater level from 0 to 255
  public int biome; // see key below
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
  
// BIOME KEY: 0:Magma 1:Ocean 2:Ice 3:Reef 4:Tundra 5:Plains 6:Desert 7:Jungle 8:Mountain 9:Snowcap 10:Freshwater 11:Space
  
  
  
  public Tile(int newLat, int newLon) {
    lat = newLat;
    lon = newLon;
    altitude = -257;
    temperature = 256;
    rainfall = -1;
    biome = 0;
  }
  
  
  
  public void randomize() { // randomizes the tile's biome for testing purposes
    biome = (int)(Math.random()*9+1);
    altitude = (int)(Math.random()*512-256);
  }
  
  
  public void spreadFrom(Tile source) { // joins the continental plate of another tile (but only temporarily; remember to copy temp1 to biome!)
    temp1 = source.altitude;
    temp2 = source.temp2; // temp2 represents lattitudinal component of new motion
    temp3 = source.temp3; // temp3 represents longitudinal component of new motion
  }
  
  
  public void startPlate(boolean wet) { // becomes the seed for a continental plate
    if (wet)  temp1 = (int)(Math.random()*16-72); // randomizes altitude from -72 to -56
    else      temp1 = (int)(Math.random()*16+56); // randomizes altitude from 56 to 72
    temp2 = (int)(100*Math.random()*Math.PI); // randomizes drift velocity
    temp3 = (int)(100*Math.random()*2*Math.PI); // these numbers represent a vector, so they are coordinates representing a point on the axis of the plate's rotation, which also goes through the center of the sphere
  }
}