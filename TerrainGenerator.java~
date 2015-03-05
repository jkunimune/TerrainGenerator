public class TerrainGenerator{ // a class to generate and display terrain onto a spherical surface
  public static void main(String args[])
  {
    /*Globe world = new Globe(20);
    Map theMap = new Lambert(world, 1200, 600);
    
    world.test();
    theMap.display("altitude");
    world.plateTechtonics();
    delay(2000);
    theMap.display("altitude");
    System.out.println("end");*/
    
    while (true) {
      Globe world = new Globe(100);
      Map theMap = new Hemispherical(world, 1200, 600);
      
      world.generate(theMap);
      
      System.out.println("end");
      delay(20000);
    }
  }
  
  
  public static void delay(int mSec) {
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() < start+mSec) {}
  }
}