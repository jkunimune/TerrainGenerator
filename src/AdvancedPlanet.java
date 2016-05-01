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
          map.display(ColS.altitude);
      t ++;
    }
    if (t%30 != 1)
      for (Map map: maps)
        map.display(ColS.altitude);
    
    System.out.println("Shifting continents...");
    for (int i = 0; i < 8; i += 1) {
      shiftPlates(0.125);
      for (Plate p: crust)
        p.changeCourse(.125);
      erode(0.5);
      for (Map map: maps)
        map.display(ColS.altitude);
    }
    
    System.out.println("Filling in oceans...");
    fillOceans();
    for (Map map: maps)
      map.display(ColS.altitude);
    
    System.out.println("Evaporating oceans...");
    evaporateSeas();
    for (Map map: maps)
      map.display(ColS.water);
    
    System.out.println("Raining...");
    fillLakes();
    runoff();
    for (Map map: maps)
      map.display(ColS.water);
    
    System.out.println("Generating climate...");
    acclimate();
    setBiomes();
    for (Map map: maps)
      map.display(ColS.biome);
    
    System.out.println("Done!\n");
  }
  
  
  public final void setAllAlt(int alt) {
    for (Tile t: map.list())
      t.altitude = alt;
  }
  
  
  public final void spawnFirstContinent() { // initializes a single tectonic plate
    for (Tile til: map.list())
      til.temp1 = 9001; // initializes temp1
    
    crust = new ArrayList<Plate>(1);
    crust.add(new Plate(0,
    					map.getTile(Math.acos(Math.random()*2-1), Math.random()*2*Math.PI),
    					50.0/map.getRadius())); // spawns a plate at a random tile
  }
  
  
  public final void spawnContinents() { // sets all tiles to a random altitude arranged in plates
    for (Tile tile: map.list()) {
      if (tile.altitude < -256) {
        ArrayList<Tile> adjacent = map.adjacentTo(tile);
        for (Tile ref: adjacent) { // reads all adjacent tiles to look for land or sea
          if (ref.altitude > -256 && randChance(ref.temp3-20)) { // hotter continents spread faster
            tile.join(ref);
            crust.get(ref.temp2).add(tile);
            break; // no need to look for a plate anymore
          }
        }
        
        if (tile.altitude < -256 && randChance(-133)) // seeds new plates occasionally
          crust.add(new Plate(crust.size(),tile,30.0/map.getRadius()));
      }
    }
    
    for (Tile til: map.list()) { // copies the temporary variables to altitude
      if (til.temp1 != 9001) { // only copies those that have been set to something
        til.altitude = til.temp1;
      }
    }
  }
  
  
  public final void shiftPlates(double delT) { // creates mountain ranges, island chains, ocean trenches, and rifts along fault lines
    for (Tile til: map.list()) {
      til.temp1 = 0;         // temp1 is the new altitude
      til.temp3 = til.temp2; // temp3 is the new plate index (temp2 is the old one)
    }
    for (Tile til: map.list()) { // takes each tile and pushes things onto it
      for (int i = crust.size()-1; i >= 0; i --) { // iterates through the crust
        Plate plt = crust.get(i);
        Vector w = plt.w;
        Vector r = new Vector (map.getRadius(), map.latByTil(til), map.lonByTil(til));
        Vector v = w.cross(r);
        Vector r0 = r.minus(v.times(delT));
        Tile origin = map.getTile(r0.getA(),r0.getB()); // the tile that would have landed here
        if (origin.temp2 == i) { // if that tile was actually on that plate
          til.temp1 += origin.altitude; // bring that altitude over here
          til.temp3 = origin.temp2; // whichever plate was generated first takes dominance
        }
      }
    }
    for (Tile til: map.list()) {
      if (til.temp1 == 0) // if no plate came here
        til.altitude >>= 1;
      else
        til.altitude = til.temp1;
      til.temp2 = til.temp3;
    }
  }
  
  
  public final void fillOceans() { // fills in the oceans to about 70% of the surface
    final int target = (int)(4*Math.PI*map.getRadius()*map.getRadius()*.71); // the approximate number of tiles we want underwater
    int seaLevel = 0;
    int min = -256;
    int max = 255;
    
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
  
  
  public void erode(double a) { // erodes extreme altitudes
    for (Tile til: map.list()) {
      double x = Math.abs(til.altitude);
      if (x >= 128) {
        double y = (Math.log(x/256+0.5)+0.5)*256;
        til.altitude = (int)(Math.ceil(y)*Math.signum(til.altitude));
      }
    }
  }
  
  
  public void smooth(double amount) { // makes terrain more smooth-looking
    for (Tile til: map.list()) {
      ArrayList<Tile> adjacent = map.adjacentTo(til);
      ArrayList<Tile> nearby = new ArrayList<Tile>(); // declares and initializes arraylists for adjacent tiles and tiles
      for (Tile adj: adjacent)                        // that are adjacent to adjacent tiles
      for (Tile nby: map.adjacentTo(adj))
        if (!nby.equals(til))
        nearby.add(nby);
      for (Tile adj: adjacent)
        nearby.remove(adj);
      
      double calcAlt = 0; // the altitude the program calculates the point should be at based on adjacent and nearby tiles
      for (Tile adj: adjacent)
        calcAlt += 1.5*adj.altitude/adjacent.size();
      for (Tile nby: nearby)
        calcAlt -= .5*nby.altitude/nearby.size();
      til.temp1 = (int)(amount*calcAlt+(1-amount)*til.altitude); // averages calculated altitude with current altitude
    }
    
    for (Tile til: map.list()) {
      til.altitude = til.temp1;
      til.temp1 = 0;
    }
  }
  
  
  public void acclimate() { // defines and randomizes the climate a bit
    for (Tile til: map.list()) {
      til.temperature = (int)(255*Math.sin(map.latByTil(til)) - ((int)Math.abs(til.altitude)>>3)); // things are colder near poles and at extreme altitudes
      til.rainfall = (int)(255*Math.pow(Math.sin(map.latByTil(til)),2)); // things get wetter around equator
      til.rainfall += moistureFrom(til,-1,0) + moistureFrom(til,1,0); // the orographic effect
    }
  }
  
  
  public int moistureFrom(Tile til, int dir, int dist) { // collects moisture from oceans and rivers and blows it
    if (dist > 16 || til.altitude > 64)
      return 0;
    Tile nxt = map.getTileByIndex(til.lat, til.lon+dir);
    int moistureHere = 0;
    if (nxt.water > 0)
      moistureHere += 16-dist>>3;
    else if (nxt.altitude < 0)
      moistureHere += 16-dist>>2;
    return moistureFrom(nxt, dir, dist+1) + moistureHere;
  }
  
  
  public void evaporateSeas() { // replaces small oceans with lakes
    for (Tile til: map.list())
      til.temp1 = 0; // temp1 is a flag for whether it has been checked yet
    
    for (Tile til: map.list()) {
      if (til.temp1 == 0) {
        if (til.waterLevel() >= 0) // land is ignored
          til.temp1 = 1;
        else { // oceans must either be salt-water or fresh-water
          ArrayList<Tile> sea = searchOcean(til); // flags this sea
          if (sea.size() < 100) // if it is too small
            for (Tile wtr: sea)
              wtr.water = -wtr.altitude; // put some fresh-water in it
        }
      }
    }
  }
  
  
  public ArrayList<Tile> searchOcean(Tile start) { // flags all sub-sea level tiles connected to this and adds to this arraylist
    ArrayList<Tile> sea = new ArrayList<Tile>();
    ArrayList<Tile> que = new ArrayList<Tile>();
    que.add(start);
    start.temp1 = 1;
    
    while (!que.isEmpty()) { // BFSs all connected tiles (I would totally DFS here, but the stack overflow limit is too low
      Tile til = que.remove(0);
      final ArrayList<Tile> adjacentList = map.adjacentTo(til);
      for (Tile adj: adjacentList) {
        if (adj.temp1 == 0 && adj.waterLevel() < 0) {
          adj.temp1 = 1;
          sea.add(til);
          que.add(adj);
        }
      }
    }
    
    return sea;
  }
  
  
  public void fillLakes() { // places freshwater to eliminate sinks
    
  }
  
  
  public void runoff() { // produces rivers
    
  }
  
  
  public void setBiomes() {
    for (Tile til: map.list()) {
      if (til.altitude < 0) { // if below sea level
        if (til.temperature < 128) { // if cold
          if (randChance(-60))
            til.biome = Tile.ice;
        }
        else if (til.altitude < -192) { // if super deep
          til.biome = Tile.trench;
        }
        else if (til.temperature < 252) { // if cool
          if (randChance(-60))
            til.biome = Tile.ocean;
        }
        else if (randChance(-50)) { // if hot
          til.biome = Tile.reef;
        }
      }
      else if (til.water >= 10) { // if has freshwater on it
        til.biome = Tile.freshwater;
      }
      else if (til.altitude >= 120) { // if mountainous
        if (til.temperature < 150) { // if cold
          til.biome = Tile.snowcap;
        }
        else { // if warm
          til.biome = Tile.mountain;
        }
      }
      else if (randChance(-64)) { // if low altitude, it only has a chance to seed
        if (til.temperature < 150) { // if cold
          til.biome = Tile.tundra;
        }
        else if (til.temperature >= 196 && til.rainfall < 215) { // if hot and dry
          til.biome = Tile.desert;
        }
        else if (til.temperature >= 245 && til.rainfall >= 245) { // if hot and wet
          til.biome = Tile.jungle;
        }
        else if (til.rainfall-256 < 2.0*(til.temperature-245)) { // if hot-ish and dry-ish
        	til.biome = Tile.plains;
        }
        else { // if cold-ish and wet-ish
          til.biome = Tile.forest;
        }
      }
      til.temp1 = til.biome;
    }
    
    for (int i = 0; i < 128 && weNeedMoreBiomes(); i ++) {
      for (Tile til: map.list()) {
        if (til.temp1 == 0) {
          final ArrayList<Tile> adjacentList = map.adjacentTo(til);
          for (Tile adj: adjacentList)
            if (til.isSuitableFor(adj.biome))
              til.temp1 = adj.biome;
        }
      }
      for (Tile til: map.list())
        til.biome = til.temp1;
    }
    for (Tile til: map.list()) {
      if (til.biome == 0) { // if it still does not have a biome, it will just pick a biome
        if (til.altitude < 0) {
          if (til.temperature < 128) { // if freezing
            til.biome = Tile.ice;
          }
          else {
            til.biome = Tile.ocean;
          }
        }
        else {
          if (til.temperature < 150) { // if cold
            til.biome = Tile.tundra;
          }
          else if (til.temperature >= 196 && til.rainfall < 215) { // if hot and dry
            til.biome = Tile.desert;
          }
          else if (til.temperature >= 245 && til.rainfall >= 245) { // if hot and wet
            til.biome = Tile.jungle;
          }
          else if (til.rainfall-256 < 2.0*(til.temperature-245)) { // if hot-ish and dry-ish
            til.biome = Tile.plains;
          }
          else { // if cold-ish and wet-ish
            til.biome = Tile.forest;
          }
        }
      }
    }
  }
  
  
  public boolean weNeedMoreBiomes() {
    for (Tile til: map.list())
      if (til.biome == 0)
        return true;
    return false;
  }
  
  
  public Surface getSurface() {
    return map; // returns the map of the surface for display purposes
  }
  
  
  public final boolean randChance(int p) { // scales an int to a probability and returns true that probability of the time
    return Math.random() < 1 / (1+Math.pow(Math.E, -.1*p));
  }
}