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
// BIOME KEY: 1:Ocean 2:Ice 3:Tundra 4:Plains 5:Desert 6:Jungle 7:Mountain 8:Snowcap 9:Freshwater
  final Color[] colors = {new Color(255,70,0), new Color(0,0,200), new Color(200,255,255), new Color(255,255,255), new Color(0,150,0),
    new Color(255,200,25), new Color(0,150,25), new Color(200,100,50), new Color(200,100,255), new Color(0,25,255)};
  
  
  
  public Tile(int newLat, int newLon) {
    lat = newLat;
    lon = newLon;
    altitude = 0;
    temperature = 0;
    rainfall = 0;
    biome = 0;
  }
  
  
  
  public void randomize() { // randomizes the tile's biome for testing purposes
    biome = (int)(Math.random()*9+1);
    altitude = (int)(Math.random()*512-256);
  }
  
  
  public void spreadFrom(Tile source) { // joins the continental plate of another tile (but only temporarily; remember to copy temp1 to biome!)
    temp1 = source.biome;
    temp2 = source.temp2; // temp2 represents lattitudinal component of new motion
    temp3 = source.temp3; // temp3 represents longitudinal component of new motion
  }
  
  
  public void startPlate(boolean wet) {
    if (wet)  temp1 = 1;
    else      temp1 = 4;
    temp2 = (int)(Math.random()*Math.PI);
    temp3 = (int)(Math.random()*2*Math.PI);
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