import java.util.*;



public class Galaxy {
  Surface map;
  
  
  
  public Galaxy(int r) {
    map = new Disc(r);
  }
  
  
  public Galaxy(Disc g) {
    map = new Disc(g);
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
    
    System.out.println("Building core...");
    buildCore();
    
    for (Map map: maps)
      map.display(ColS.altitude);
    
    System.out.println("Generating arms...");
    formArms();
    
    for (Map map: maps)
      map.display(ColS.altitude);
    
    System.out.println("Enhancing arms...");
    for (int i = 0; i < 128; i ++) {
      thickenArms();
      
      if (i%32 == 0)
        for (Map map: maps)
          map.display(ColS.altitude);
    }
    
    System.out.println("Randomizing...");
    randomize();
    
    for (Map map: maps)
      map.display(ColS.altitude);
    
    System.out.println("Color-coding...");
    biomeAssign();
    
    for (Map map: maps)
      map.display(ColS.biome);
    
    System.out.println("Done!");
  }
  
  
  private final void buildCore() {
    for (Tile til: map.list()) {
      if (map.latByTil(til) < Math.PI/32.0)
        til.radioactive = true;
      if (map.latByTil(til) < Math.PI/8.0)
        til.altitude = 24;
      else
        til.altitude = -8;
    }
  }
  
  
  private final void formArms() {
    buildArm(Math.PI/64.0, 0, 3.0, .8);
    buildArm(Math.PI/64.0, Math.PI, 3.0, .8);
  }
  
  
  private final void thickenArms() {
    for (Tile til: map.list())
      til.temp1 = 0;
    
    for (Tile til: map.list())
      for (Tile adj: map.adjacentTo(til))
        if (adj.altitude > 0)
          if (randChance(adj.altitude/10-33))
            til.temp1 ++;
    
    for (Tile til: map.list())
      til.altitude += til.temp1;
  }
  
  
  private final void randomize() {
    for (Tile til: map.list())
      til.altitude += 10*(Math.random()-.5);
  }
  
  
  private final void biomeAssign() {
    for (Tile til: map.list()) {
        if (til.altitude < 0)
          til.biome = Tile.ocean;
        else if (randChance((til.altitude>>5)-40))
          til.biome = Tile.freshwater;
        else if (til.altitude < 96)
          til.biome = Tile.plains;
        else
          til.biome = Tile.jungle;
      }
    }
  
  
  private final void buildArm(double lat, double lon, double slope, double thickness) { // continues an arm from the given polar coordinates
    if (lat >= Math.PI)
      return;
    
    final double delX = .01;
    
    final Tile til = map.getTile(lat, lon);
    ArrayList<Tile> list = map.adjacentTo(til);
    list.add(til);
    for (Tile adj: list)
      if (Math.random() < thickness*(1-lat/Math.PI))
        adj.altitude += Math.random()*32;
    
    if (Math.random() >= delX*Math.PI)
      buildArm(lat+delX, lon+slope*delX, slope, thickness);
    else { // arms have a chance to split
      double frac = Math.random();
      buildArm(lat+delX, lon+slope*delX, slope+(1-frac)*1.5, thickness*frac);
      buildArm(lat+delX, lon+slope*delX, slope-frac*1.5, thickness*(1-frac));
    }
  }
  
  
  public Surface getSurface() {
    return map; // returns a map of the surface for display purposes
  }
  
  
  public final boolean randChance(int p) { // scales an int to a probability and returns true that probability of the time
    return Math.random() < 1 / (1+Math.pow(Math.E, -.1*p));
  }
}