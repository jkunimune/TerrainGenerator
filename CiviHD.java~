// by Justin Kunimune



import java.util.*;




public class CiviHD { // the driver for my final project
  static long startTime = 0; // the time it was when we last checked
  
  
  
  public static final void main(String args[]) {
    Planet protoEarth = new Planet(100);
    Map theMap = new Sinusoidal(protoEarth, 600, 400);
    generate(protoEarth, theMap);
    
    World earth = new World(protoEarth);
    
    while (true) {
      setTimer(0);
      earth.update();
      theMap.display(Map.territory);
      waitFor(100);
    }
  }
  
  
  
  /* PRECONDITION: map's Globe is world */
  public static final void generate(Planet world, Map map) { // randomly generates a map and simultaneously displays it
    map.display(Map.altitude);
    System.out.println("Generating landmasses...");
      
    world.spawnFirstContinent();
    
    int t = 0;
    while (world.any(-257)) {
      world.spawnContinents();
      if (t%10 == 0)
        map.display(Map.altitude);
      t ++;
    }
    if (t%10 != 1)
      map.display(Map.altitude);
    
    System.out.println("Shifting continents...");
    world.plateTechtonics();
    map.display(Map.altitude);
    
    System.out.println("Roughing up and smoothing down terrain...");
    for (int i = 64; i >= 1; i >>= 2) { // gradually randomizes and smooths out terrain
      for (int j = 0; j < i; j ++)
        world.smooth(.4);
      map.display(Map.altitude);
      world.rough(i*1.2);
      map.display(Map.altitude);
    }
    
    System.out.println("Generating climate...");
    world.acclimate(.1);
    world.climateEnhance();
    map.display(Map.temperature);
    map.display(Map.rainfall);
    
    System.out.println("Raining...");
    world.rain();
    world.runoff();
    map.display(Map.water);
    
    System.out.println("Setting up biomes...");
    world.biomeAssign();
    map.display(Map.biome);
    
    System.out.println("Done!\n");
    delay(3000);
  }
  
  
  public static final void delay(int mSec) { // waits a number of miliseconds
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() < start+mSec) {}
  }
  
  
  public static final void setTimer(long mSec) { // sets the timer to a certain time
    startTime = System.currentTimeMillis() - mSec;
  }
  
  
  public static final void waitFor(long mSec) { // waits for the timer to reach a certain time
    while (System.currentTimeMillis() - startTime < mSec) {}
  }
}