import java.util.*;



public final class AdvancedPlanet { // a subclass of Globe that handles all geological elements  
  private Globe map;
  private List<Plate> crust;
  
  
  
  public AdvancedPlanet(int r) {
    map = new Globe(r);
  }
  
  
  public AdvancedPlanet(Globe s) {
    map = s;
  }
  
  
  
  public final void generate() {
    generate(new Map[0]);
  }
  
  
  /* PRECONDITION: map's Globe is world */
  public final void generate(Map map) { // randomly generates a map and simultaneously displays it
    Map[] sheath = new Map[1];
    sheath[0] = map;
    generate(sheath);
  }
  
  
  /* PRECONDITION: each map's Globe is world */
  public final void generate(Map[] maps) { // randomly generates a map and simultaneously displays it
    for (Map map: maps)
      map.display(ColS.altitude);
    
    System.out.println("Generating landmasses...");
    spawnFirstContinent();
    
    int t = 0;
    while (map.count(-256) > 0) {
      spawnContinents();
      if (t%30 == 0)
        for (Map map: maps)
          map.display(ColS.altitude2);
      t ++;
    }
    if (t%30 != 1)
      for (Map map: maps)
        map.display(ColS.altitude2);
    
    System.out.println("Shifting continents...");
    for (int i = 0; i < 5; i += 1)
      shiftPlates(.1);
    
    System.out.println("Filling in oceans...");
    fillOceans();
    for (Map map: maps)
      map.display(ColS.altitude);
    
    System.out.println("Done!\n");
  }
  
  
  public final void setAllAlt(int alt) {
    for (Tile t: map.list())
      t.altitude = alt;
  }
  
  
  public final void spawnFirstContinent() { // initializes a single techtonic plate
    for (Tile til: map.list())
      til.temp1 = 9001; // initializes temp1
    
    crust = new ArrayList<Plate>(1);
    crust.add(new Plate(0, map.getTile(Math.asin(Math.random()*2-1) + Math.PI/2, Math.random()*2*Math.PI))); // spawns a plate at a random tile
  }
  
  
  public final void spawnContinents() { // sets all tiles to a random altitude aranged in plates
    for (Tile tile: map.list()) {
      if (tile.altitude < 0) {
        ArrayList<Tile> adjacent = map.adjacentTo(tile);
        for (Tile ref: adjacent) { // reads all adjacent tiles to look for land or sea
          if (ref.altitude > 0 && randChance(ref.temp3-20)) { // hotter continents spread faster
            tile.join(ref);
            crust.get(ref.temp2).add(tile);
          }
        }
        
        if (tile.altitude < 0 && randChance(-135)) // seeds new plates occasionally
          crust.add(new Plate(crust.size(),tile));
      }
    }
    
    for (Tile til: map.list()) // copies the temporary variables to altitude
      if (til.temp1 != 9001) // only copies those that have been set to something
        til.altitude = til.temp1;
  }
  
  
  public final void shiftPlates(double delT) { // creates mountain ranges, island chains, ocean trenches, and rifts along fault lines
    for (Tile til: map.list()) { // delT is the number of radians each plate moves
      
    }
  }
  
  
  public final void fillOceans() { // fills in the oceans to about 70% of the surface
    final int target = (int)(4*Math.PI*map.getRadius()*map.getRadius()*.7); // the approximate number of tiles we want underwater
    int seaLevel = 255;
    int min = 0;
    int max = 511;
    
    for (int i = 0; i < 5; i ++) {
      if (map.count(seaLevel) < target) { // if too many are abovewater
        min = seaLevel; // raise the sea level
        seaLevel = (seaLevel+max)>>1;
      }
      else { // if too many are underwater
        max = seaLevel; // lower the sea level
        seaLevel = (seaLevel+min)>>1; // assume it will never be exactly equal
      }
    }
    
    for (Tile til: map.list())
      til.altitude -= seaLevel; // fills in oceans
  }
  
  
  public Surface getSurface() {
    return map; // returns the map of the surface for display purposes
  }
  
  
  public final boolean randChance(int p) { // scales an int to a probability and returns true that probability of the time
    return Math.random() < 1 / (1+Math.pow(Math.E, -.1*p));
  }
}