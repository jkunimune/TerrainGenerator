public class Tile { // keeps track of a single point on a globe
  public int altitude; // sea level is 0, goes from -512 to 511
  public int temperature;
  public int humidity; // measure of how wet a climate is
  public int biome; // see key below
  public int temp1; // to store various values only necessary during generation
  public int temp2;
// BIOME KEY: 1:Ocean 2:Ice 3:Tundra 4:Plains 6:Desert 7:Jungle 8:Mountain 9:Snowcap 10:Freshwater
  
  
  
  public Tile() {
    altitude = 0;
    temperature = 0;
    humidity = 0;
    biome = 0;
  }
  
  
  public void granitize() {
    altitude = 255;
    biome = 1;
  }
  
  
  public void basaltize() {
    altitude = -256;
    biome = 4;
  }
}