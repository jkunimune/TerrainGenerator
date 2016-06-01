package mechanics;
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
    
    System.out.println("Generating climate...");
    acclimate();
    for (Map map: maps)
      map.display(ColS.climate);
    
    System.out.println("Raining...");
    setUpWater();
    while (rain())
      for (Map map: maps)
        map.display(ColS.water);
    
    System.out.println("Finalizing climate...");
    setBiomes();
    for (Map map: maps)
      map.display(ColS.biome);
    
    System.out.println("Done!\n");
  }
  
  
  private final void spawnFirstContinent() { // initializes a single tectonic plate
    for (Tile til: map.list())
      til.temp1 = 9001; // initializes temp1
    
    crust = new ArrayList<Plate>(1);
    crust.add(new Plate(0,
    					map.getTile(Math.acos(Math.random()*2-1), Math.random()*2*Math.PI),
    					50.0/map.getRadius())); // spawns a plate at a random tile
  }
  
  
  private final void spawnContinents() { // sets all tiles to a random altitude arranged in plates
    for (Tile tile: map.list()) {
      if (tile.altitude < -256) {
        for (Tile ref: tile.adjacent) { // reads all adjacent tiles to look for land or sea
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
  
  
  private final void shiftPlates(double delT) { // creates mountain ranges, island chains, ocean trenches, and rifts along fault lines
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
  
  
  private final void fillOceans() { // fills in the oceans to about 70% of the surface
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
    
    for (Tile til: map.list()) {
      til.altitude -= seaLevel; // fills in oceans
      if (til.altitude < 0)
        til.water = -256*til.altitude;	// prepares for some hydraulic algorithms later on
      else
        til.water = 0;
    }
  }
  
  
  private void erode(double a) { // erodes extreme altitudes
    for (Tile til: map.list()) {
      double x = Math.abs(til.altitude);
      if (x >= 128) {
        double y = (Math.log(x/256+0.5)+0.5)*256;
        til.altitude = (int)(Math.ceil(y)*Math.signum(til.altitude));
      }
    }
  }
  
  
  private void acclimate() { // defines and randomizes the climate a bit
    for (Tile til: map.list()) {
      til.temperature = (int)(255*Math.sin(map.latByTil(til)) - ((int)Math.abs(til.altitude)>>3)); // things are colder near poles and at extreme altitudes
      til.rainfall = (int)(255*Math.pow(Math.sin(map.latByTil(til)),2)); // things get wetter around equator
      til.temp3 = 0;
    }
  }
  
  
  private int moistureFrom(Tile til, int dir, int dist) { // collects moisture from oceans and rivers and blows it
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
  
  
  private void setUpWater() {	// initialize some variables for rain
    for (Tile til: map.list()) {
      til.temp2 = -1;	// temp2, temp3: The indices of the tile into which til will flow
      til.temp3 = -1;
      if (til.waterLevel() < 0)
        til.biome = Tile.ocean;
    }
  }
  
  
  private boolean rain() {	// generates rivers; returns whether it needs to run again or not
    final Tile[] allTiles = map.list();
    
    for (Tile til: allTiles) {	// look at all tiles
      if (!waterCanFlowFrom(til)) {	// if this tile would prevent rivers from flowing
        ArrayList<Tile> lake = new ArrayList<Tile>();
        lake.add(til);
        fillLake(lake);			// pour water on it until it won't
        return true;			// then go back to the beginning
      }
    }
    
    for (Tile til: allTiles) {
      if (til.temp2 == -1) {	// if a tile still hasn't been routed
        int lowestHeight = Integer.MAX_VALUE;	// route it to the lowest adjacent tile
        Tile lowestNeighbor = null;
        for (Tile adj: til.adjacent) {
          if (adj.waterLevel() < lowestHeight) {
            lowestHeight = adj.waterLevel();
            lowestNeighbor = adj;
          }
        }
        assert lowestHeight<til.waterLevel(): "fillLake didn't do it's job; "+til+" doesn't have anywhere to flow";
        til.temp2 = lowestNeighbor.lat;
        til.temp3 = lowestNeighbor.lon;
      }
    }
    
    for (Tile til: allTiles)	// finally, build the rivers
      runoff(til);
    return false;
  }
  
  
  private void fillLake(List<Tile> lake) {
    do {
      if (lake.size() >= 128) {	// if the lake is big enough
        for (Tile til: lake) {	// it becomes a sea
          til.biome = Tile.ocean;	// so all tiles in it just become ocean tiles
          return;					// and we are done with this lake
        }
      }
      final int lakeHeight = lake.get(0).waterLevel();
      int lowestHeight = Integer.MAX_VALUE;	// the altitude that this lake will rise to
      Tile lowestShore = null;				// the lowest adjacent tile that this lake will rise to
      for (Tile til: lake) {
        for (Tile adj: til.adjacent) {
          if (!lake.contains(adj)) {
            if (adj.waterLevel() < lakeHeight || adj.biome == Tile.ocean) {	// if this is a potential outlet
              setDestination(lake, adj);	// route all tiles in the lake to the destination
              return;						// and we're done here
            }
            else if (adj.waterLevel() < lowestHeight) {
              lowestHeight = adj.waterLevel();
              lowestShore = adj;
            }
          }
        }
      }
      
      for (Tile til: lake)	// if you check all adjacent tiles and none of them triggered the return
        til.water = lowestShore.waterLevel()-(til.altitude<<2);
      lake.add(lowestShore);	// then lowestShore should become part of the lake and the lake should rise
    } while (true);	// repeat
  }
  
  
  private void setDestination(List<Tile> lake, Tile out) {	// routes all tiles in a list randomly to one point
    //Collections.shuffle(lake);
    List<Tile> done = new ArrayList<Tile>();
    done.add(out);
    while (!lake.isEmpty()) {
      for (int i = 0; i < done.size(); i ++) {
        final Tile wet = done.get(i);
        for (Tile dry: wet.adjacent) {
          if (lake.contains(dry)) {
            dry.temp2 = wet.lat;
            dry.temp3 = wet.lon;
            lake.remove(dry);
            done.add(dry);
          }
        }
      }
    }
  }
  
  
  public void runoff(Tile t) {	// creates the rivers based on temp2 and temp3
    t.water ++;			// rain on it
    if (t.temp2 >= 0)	// if it has a destination set
      if (t.temp2 != t.lat || t.temp3 != t.lon)	// and that destination is not itself
        runoff(map.getTileByIndex(t.temp2, t.temp3));
  }
  
  
  private boolean waterCanFlowFrom(Tile til) {
    if (til.temp2 >= 0)
      return true;
    if (til.biome == Tile.ocean)
      return true;
    for (Tile adj: til.adjacent)
      if (adj.waterLevel() < til.waterLevel() || adj.biome == Tile.ocean)
        return true;
    return false;
  }
  
  
  private void setBiomes() {
    for (Tile til: map.list())
      til.rainfall += moistureFrom(til,-1,0) + moistureFrom(til,1,0); // apply the orographic effect
    
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
      else if (til.water >= 128) { // if has freshwater on it
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
    
    for (int i = 0; i < 64 && weNeedMoreBiomes(); i ++) {
      for (Tile til: map.list()) {
        if (til.temp1 == 0) {
          for (Tile adj: til.adjacent)
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
  
  
  private boolean weNeedMoreBiomes() {
    for (Tile til: map.list())
      if (til.biome == 0)
        return true;
    return false;
  }
  
  
  public Surface getSurface() {
    return map; // returns the map of the surface for display purposes
  }
  
  
  private final boolean randChance(int p) { // scales an int to a probability and returns true that probability of the time
    return Math.random() < 1 / (1+Math.pow(Math.E, -.1*p));
  }
}