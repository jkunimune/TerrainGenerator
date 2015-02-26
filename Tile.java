import java.awt.Color;



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
// BIOME KEY: 0:Magma 1:Ocean 2:Ice 3:Tundra 4:Plains 5:Desert 6:Jungle 7:Mountain 8:Snowcap 9:Freshwater 10:Space 11:Star
  final Color[] colors = {new Color(255,70,0), new Color(0,0,200), new Color(200,255,255), new Color(255,255,255), new Color(0,255,0),
    new Color(255,200,25), new Color(0,150,25), new Color(200,100,50), new Color(200,100,255), new Color(0,25,255), new Color(0,0,0),
    new Color(255, 255, 200)};
  
  
  
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
  
  
  public Color getColorBy(String type) {
    if (type.equals("biome"))
      return getColorByBiome();
    else if (type.equals("altitude"))
      return getColorByAlt();
    else
      return Color.black;
  }
  
  
  public Color getColorByBiome() {
    return colors[biome];
  }
  
  
  public Color getColorByAlt() {
    if (altitude < -256) // if altitude is below minimum, return the color of lava
      return colors[0];
    else if (altitude >= 256) // if altitude is above maximum, return the color of space
      return colors[10];
    else if (altitude < 0) // if altitude is below sea level, return a blue that gets darker as one goes deeper
      return new Color(0, 0, 256+altitude);
    else // if altitude is above sea level, return a green that gets brighter as one goes higher
      return new Color(altitude, 255, altitude);
  }
//  
//  
//  public Color getColorByTemp() {
//  }
//  
//  
//  public Color getColorByRain() {
//  }
//  
//  
//  public Color getColorByClimate() {
//  }
//  
//  
//  public Color getColorByLevel() {
//  }
}