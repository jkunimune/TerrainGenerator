public class TerrainGenerator{ // a class to generate and display terrain onto a spherical surface
  public static void main(String args[])
  {
    Globe world = new Globe(100);
    Map theMap = new Map(800, 600);
    
    theMap.lambert(world, "biome");
    
    while (world.any(0)) {
      delay(10);
      world.spawnContinents();
      theMap.lambert(world, "biome");
    }
  }
  
  
  public static void delay(int mSec) {
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() < start+mSec) {}
  }
}