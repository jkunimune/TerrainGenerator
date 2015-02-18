import java.awt.Color;



public class Tile { // keeps track of a single point on a globe
  public int altitude; // sea level is 0, goes from -256 to 255
  public int temperature;
  public int rainfall; // measure of how wet a climate is
  public int water; // the freshwater level
  public int biome; // see key below
  public int temp1; // to store various values only necessary during generation
  public int temp2;
// BIOME KEY: 1:Ocean 2:Ice 3:Tundra 4:Plains 5:Desert 6:Jungle 7:Mountain 8:Snowcap 9:Freshwater
  final Color[] colors = {new Color(0,0,0), new Color(0,0,200), new Color(200,255,255), new Color(255,255,255), new Color(0,255,0),
    new Color(255,200,25), new Color(0,150,25), new Color(200,100,50), new Color(200,100,255), new Color(0,25,255)};
  
  
  
  public Tile() {
    altitude = 0;
    temperature = 0;
    rainfall = 0;
    biome = 0;
  }
  
  
  
  public void randomize() { // randomizes the tile's biome for testing purposes
    biome = (int)(Math.random()*9+1);
    altitude = (int)(Math.random()*512-256);
  }
  
  
  public void granitize() { // establishes a tile as a continental plate
    altitude = 255;
    biome = 1;
  }
  
  
  public void basaltize() { // establishes a tile as oceanic plate
    altitude = -256;
    biome = 4;
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
    if (altitude < 0)
      return new Color(0, 0, 256+altitude);
    else
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
//  public Color getColorByLevel() {
//  }
}