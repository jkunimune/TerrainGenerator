public class TerrainGenerator{ // a class to generate and display terrain onto a spherical surface
  static long startTime = 0; // the time it was when we last checked
  
  
  
  public static void main(String args[]) {
    while (true) {
      Globe earth = new Globe(100);
      Map theMap = new Hemispherical(earth, 800, 400);
      
      theMap.display("altitude");
      
      generate(earth, theMap);
      
      System.out.println("end");
      delay(20000);
    }
  }
  
  
  /* PRECONDITION: map's Globe is world */
  public static void generate(Globe world, Map map) { // randomly generates a map and simultaneously displays it
    map.display("altitude");
    System.out.println("Generating landmasses...");
      
    world.spawnFirstContinent();
    
    int t = 0;
    while (world.any(-257)) {
      world.spawnContinents();
      if (t%10 == 0)
        map.display("altitude");
      t ++;
    }
    if (t%10 != 1)
      map.display("altitude");
    
    System.out.println("Shifting continents...");
    world.plateTechtonics();
    map.display("altitude");
    
    System.out.println("Roughing up and smoothing down terrain...");
    for (int i = 64; i >= 1; i >>= 2) { // gradually randomizes and smooths out terrain
      for (int j = 0; j < i; j ++)
        world.smooth(.4);
      map.display("altitude");
      world.rough(i/1.0);
      map.display("altitude");
    }
    
    System.out.println("Generating climate...");
    world.acclimate(.1);
    world.climateEnhance();
    map.display("temperature");
    map.display("rainfall");
    
    System.out.println("Raining...");
    map.display("water level");
    delay(5000);
    world.rain();
    System.out.println("Lakes are filled.");
    map.display("water level");
    delay(5000);
    world.runoff();
    map.display("water level");
    
    System.out.println("Setting up biomes...");
    world.biomeAssign();
    map.display("biome");
  }
  
  
  public static void delay(int mSec) { // waits a number of miliseconds
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() < start+mSec) {}
  }
  
  
  public static void setTimer(long mSec) { // sets the timer to a certain time
    startTime = System.currentTimeMillis() - mSec;
  }
  
  
  public static void waitFor(long mSec) { // waits for the timer to reach a certain time
    while (System.currentTimeMillis() - startTime < mSec) {}
  }
}