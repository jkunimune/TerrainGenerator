public class TerrainGenerator{ // a class to generate and display terrain onto a spherical surface
  static long startTime = 0; // the time it was when we last checked
  
  
  
  public static void main(String args[]) {
    while (true) {
      Globe earth = new Globe(100);
      Map theMap = new Lambert(earth, 1200, 600);
      
      theMap.display("altitude");
      
      generate(earth, theMap);
      
      /*System.out.println("Generating landmasses...");
      start.spawnFirstContinent();
      int t = 0;
      while (start.any(-257)) {
        //setTimer(0);
        start.spawnContinents();
        if (t%10 == 0)
          map.display("altitude");
        t ++;
        //waitFor(100);
      }
      if (t%10 != 1)
        map.display("altitude");
      
      Globe chains = new Globe(start);
      Globe trench = new Globe(start);
      Map chainM = new Lambert(chains, 800, 500);
      Map trencM = new Lambert(trench, 800, 500);
      
      chains.plateTechtonicsJustChains();
      trench.plateTechtonicsJustTrench();
      start.plateTechtonics();
      
      chainM.display("altitude");
      trencM.display("altitude");
      map.display("altitude");*/
      
      System.out.println("end");
      delay(20000);
    }
  }
  
  

/* PRECONDITION: each Map's Globe is world */
  public static void generate(Globe world, Map[] maps) { // randomly generates a map and simultaneously displays it on several projections
    for (Map m: maps)
      m.display("altitude");
    System.out.println("Generating landmasses...");
      
    world.spawnFirstContinent();
    
    while (world.any(-257)) {
      delay(10);
      world.spawnContinents();
      for (Map m: maps)
        m.display("altitude");
    }
    
    System.out.println("Shifting continents...");
    //world.plateTechtonics();
    for (Map m: maps)
      m.display("altitude");
    
    System.out.println("Roughing up terrain...");
    world.rough(64);
    for (Map m: maps)
      m.display("altitude");
    
    System.out.println("Smoothing down terrain...");
    world.smooth(.5);
    for (Map m: maps)
      m.display("altitude");
  }
  
  
  /* PRECONDITION: map's Globe is world */
  public static void generate(Globe world, Map map) { // randomly generates a map and simultaneously displays it
    map.display("altitude");
    System.out.println("Generating landmasses...");
      
    world.spawnFirstContinent();
    
    int t = 0;
    while (world.any(-257)) {
      //setTimer(0);
      world.spawnContinents();
      if (t%10 == 0)
        map.display("altitude");
      t ++;
      //waitFor(100);
    }
    if (t%10 != 1)
      map.display("altitude");
    
    System.out.println("Shifting continents...");
    world.plateTechtonics();
    map.display("altitude");
    
    System.out.println("Roughing up and smoothing down terrain...");
    for (int i = 64; i > 1; i >>= 3) { // gradually randomizes and smooths out terrain
      for (int j = 0; j < i; j ++)
        world.smooth(.4);
      map.display("altitude");
      world.rough(i/128.0);
      map.display("altitude");
    }
    
    System.out.println("Generating climate...");
    world.acclimate(.1);
    world.climateEnhance();
    map.display("temperature");
    delay(1000);
    
    System.out.println("Setting up biomes...");
    world.biomeAssign();
    map.display("biome");
  }
  
  
  public static void generate(Globe world) { // randomly generates a map
    world.spawnFirstContinent();
    
    System.out.println("Generating landmasses...");
    while (world.any(-257))
      world.spawnContinents();
    
    System.out.println("Shifting continents...");
    world.plateTechtonics();
    
    System.out.println("Roughing up terrain...");
    world.rough(64);
    
    System.out.println("Smoothing down terrain...");
    world.smooth(.5);
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