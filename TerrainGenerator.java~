public class TerrainGenerator{ // a class to generate and display terrain onto a spherical surface
  public static void main(String args[])
  {
    /*Globe world = new Globe(20);
    Map lamb = new Map(world, 1200, 600);
    
    world.test();
    lamb.lambert("altitude");
    world.plateTechtonics();
    delay(2000);
    lamb.lambert("altitude");
    System.out.println("end");*/
    
    while (true) {
      Globe world = new Globe(100);
      Map lamb = new Map(world, 1200, 600);
      
      lamb.lambert("altitude");
      
      world.spawnContinent();
      
      while (world.any(-257)) {
        delay(10);
        world.spawnContinents();
        lamb.lambert("altitude");
      }
      
      world.plateTechtonics();
      lamb.lambert("altitude");
      
      System.out.println("end");
      delay(20000);
    }
  }
  
  
  public static void delay(int mSec) {
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() < start+mSec) {}
  }
}