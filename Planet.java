import java.util.*;



public class Planet extends Globe { // a subclass of Globe that handles all geological elements
  public Planet(int r) {
    super(r);
  }
  
  
  public Planet(Globe g) {
    super(g);
  }
  
  
  
  public void generate() {
    System.out.println("Generating landmasses...");
    this.spawnFirstContinent();
    while (this.any(-257))
      this.spawnContinents();
    
    System.out.println("Shifting continents...");
    this.plateTechtonics();
    
    System.out.println("Roughing up and smoothing down terrain...");
    for (int i = 64; i >= 1; i >>= 2) { // gradually randomizes and smooths out terrain
      for (int j = 0; j < i; j ++)
        this.smooth(.4);
      this.rough(i*1.2);
    }
    System.out.println("Generating climate...");
    this.acclimate(.1);
    this.climateEnhance();
    
    System.out.println("Raining...");
    this.rain();
    this.runoff();
    
    System.out.println("Setting up biomes...");
    this.biomeAssign();
  }
  
  
  public void test() { // only used for testing purposes
    getTile(.1, Math.PI).biome = 2;
    for (Tile t: adjacentTo(getTile(.1, Math.PI)))
      t.biome = 3;
  }
  
  
  public void randomize() { // randomizes each tile for testing purposes
    for (Tile[] row: map)
    for (Tile t: row)
      t.randomize();
  }
  
  
  public void setAllAlt(int alt) {
    for (Tile[] row: map)
    for (Tile t: row)
      t.altitude = alt;
  }
  
  
  public void spawnFirstContinent() { // initializes a single techtonic plate
    this.getTile(Math.asin(Math.random()*2-1) + Math.PI/2, Math.random()*2*Math.PI).startPlate(Math.random()<.6);
  }
  
  
  public void spawnContinents() { // sets some tiles to a continent or an ocean
    for (Tile[] row: map) {
      for (Tile tile: row) {
        if (tile.altitude == -257) {
          ArrayList<Tile> adjacent = adjacentTo(tile);
          for (Tile ref: adjacent) // reads all adjacent tiles to look for land or sea
            if (ref.altitude >= -256 && randChance(-43 + ((int)Math.pow(ref.altitude,2)>>7))) // deeper/higher continents spread faster
            tile.spreadFrom(ref);
          
          if (tile.altitude == -257 && randChance(-148)) // I realize I check that the biome is 0 kind of a lot, but I just want to avoid any excess computations
            tile.startPlate(false); // seeds new plates occasionally
          else if (tile.altitude == -257 && randChance(-141))
            tile.startPlate(true);
        }
      }
    }
    
    for (Tile[] row: map) // copies the temporary variables to altitude
    for (Tile tile: row)
      if (tile.temp1 < -56 || tile.temp1 >= 56) // only copies those that have been set to something
      tile.altitude = tile.temp1;
  }
  
  
  public void plateTechtonics() { // creates mountain ranges, island chains, ocean trenches, and rifts along fault lines
    for (Tile[] row: map) {
      for (Tile thisTil: row) {
        thisTil.temp1 = thisTil.altitude;
        double totalChange = 0; // keeps track of how much to change the altitude
        ArrayList<Tile> adj = adjacentTo(thisTil);
        for (Tile thatTil: adj) {
          if (thisTil.temp2 == thatTil.temp2 && thisTil.temp3 == thatTil.temp3) // if they are on the same plate
            continue; // skip this pair
          
          final Vector r1 = new Vector(1, thisTil.lat*Math.PI/map.length, thisTil.lon*2*Math.PI/map[thisTil.lat].length);
          final Vector r2 = new Vector(1, thatTil.lat*Math.PI/map.length, thatTil.lon*2*Math.PI/map[thatTil.lat].length);
          Vector delTheta = r1.cross(r2);
          delTheta.setR(r1.angleTo(r2)*getRadius()); // the distance between them
          
          Vector omega1 = new Vector(1, thisTil.temp2/128.0, thisTil.temp3/128.0);
          Vector omega2 = new Vector(1, thatTil.temp2/128.0, thatTil.temp3/128.0);
          Vector delOmega = omega1.minus(omega2); // how fast they are moving toward each other
          
          double rise = 2000.0*delOmega.dot(delTheta)/Math.pow(delTheta.getR(),3);
          
          if (thisTil.altitude < 0) { // if this is ocean
            if (rise < 0) { // if they are going towards each other
              if (thisTil.altitude < thatTil.altitude) { // if this is lower than that one
                totalChange += rise; // it forms a sea trench
              }
              else if (thisTil.altitude > thatTil.altitude) { // if this is above that one
                totalChange -= rise*.77; // it forms an island chain
              }
              else { // if they are going at the same speed
                if (Math.random() < .5)  totalChange += rise/2.0; // it forms a random type thing
                else                     totalChange -= rise/2.0;
                
              }
            }
            else { // if they are going away from each other
              totalChange += rise/32; // it forms an ocean rift
            }
          }
          else { // if this is land
            if (rise < 0) { // if they are going towards each other
              totalChange -= rise; // it forms a mountain range
            }
            else { // if they are going away from each other
              totalChange -= rise/4; // it forms a valley
            }
          }
        }
        thisTil.temp1 += totalChange;
      }
    }
    
    for (Tile[] thisRow: map) {
      for (Tile thisTil: thisRow) {
        thisTil.altitude = thisTil.temp1;
      }
    }
  }
  
  
  public void acclimate(double amount) { // defines and randomizes the climate a bit
    for (int lat = 0; lat < map.length; lat ++) {
      for (int lon = 0; lon < map[lat].length; lon ++) {
        map[lat][lon].temperature = (int)(255*Math.sin(lat*Math.PI/map.length)); // things are colder near poles
        map[lat][lon].rainfall = (int)(255*Math.pow(Math.sin(lat*Math.PI/map.length),2)); // things get wetter around equator
        map[lat][lon].temperature += (int)(Math.random()*255*amount-map[lat][lon].temperature*amount);
        map[lat][lon].rainfall += (int)(Math.random()*255*amount-map[lat][lon].rainfall*amount);
      }
    }
  }
  
  
  public void rough(double amount) { // randomizes the terrain a bit
    for (Tile[] row: map) {
      for (Tile t: row) {
        t.altitude += (int)((Math.random()-.5)*amount);
      }
    }
  }
  
  
  public void smooth(double amount) { // makes terrain more smooth-looking
    for (Tile[] row: map) {
      for (Tile til: row) {
        ArrayList<Tile> adjacent = adjacentTo(til);
        ArrayList<Tile> nearby = new ArrayList<Tile>(); // declares and initializes arraylists for adjacent tiles and tiles
        for (Tile adj: adjacent)                        // that are adjacent to adjacent tiles
        for (Tile nby: adjacentTo(adj))
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
    }
    
    for (Tile[] row: map) {
      for (Tile til: row) {
        til.altitude = til.temp1;
      }
    }
  }
  
  
  public int moisture(Tile til, int dir, int dist) { // determines how much moisture blows in from a given direction
    if (dist <= 0)
      return 0;
    
    int here;
    if (til.altitude < 0)  here = (int)Math.sqrt(dist)>>1; // if this is an ocean, draw moisture from it
    else if (til.altitude < 64) here = (int)Math.sqrt(dist)>>3; // if not, draw a little bit of moisture from it
    else here = 0; // if it is a mountain, no moisture
    
    final Tile next = map[til.lat][(til.lon+dir+map[til.lat].length)%map[til.lat].length];
    
    if (next.altitude >= 64) // if there is a mountain range coming up
      return here;
    else
      return here + moisture(next, dir, dist-1);
  }
  
  
  public void rain() { // forms, rivers, lakes, valleys, and deltas
    for (Tile[] row: map) { // initializes temps to -1
      for (Tile til: row) {
        til.temp1 = -1;
        til.temp2 = -1;
      }
    }
    
    for (int r = 0; r < map.length; r ++) { // fills all lakes and routes all water
      for (int c = 0; c < map[r].length; c ++) {
        Tile til = map[r][c];
        if (til.altitude >= 0) { // if land
          ArrayList<Tile> destinations = adjacentTo(til);
          Tile lowest = destinations.get(0); // lowest tile to which water can flow
          
          for (Tile adj: destinations) {
            if (adj.altitude < 0 || adj.altitude+adj.water < lowest.altitude+lowest.water) { // if this is the new lowest
              lowest = adj;
            }
//            else if (adj.altitude+adj.water == lowest.altitude+lowest.water && randChance(0)) { // if it is equal to the lowest
//              lowest = adj; // throw in a random element
//            }
          }
          
          if (lowest.waterLevel() < til.waterLevel()) { // if water can flow
            til.temp1 = lowest.lat; // set the water path from here to there
            til.temp2 = lowest.lon;
          }
          else if (lowest.waterLevel() == til.waterLevel()) { // if it is flat
            if (til.temp1 == -1) { // if temp 1 has not been set
              fillLake(til, lowest); // set it
              r = 0; // start over
              c = 0;
            }
          }
          else { // if water can't flow
            til.water = lowest.altitude+lowest.water - til.altitude;
            fillLake(til, lowest); // fill in the area so it can
            r = 0; // start over
            c = 0;
          }
        }
      }
    }
  }
  
  
  public void runoff() {
    for (Tile[] row: map)
    for (Tile til: row)
      til.temp3 = 0;
    
    for (Tile[] row: map) {
      for (Tile til: row) {
        runoffFrom(til, til.rainfall*.0045); // fills rivers
      }
    }
  }
  
  
  public void fillLake(Tile seed, Tile adj) {
    ArrayList<Tile> lake = new ArrayList<Tile>();
    lake.add(seed);
    lake.add(adj);
    fillLake(lake);
  }
  
  
  /* PRECONDITION: all Tiles in lake are at the same height (altitude+water) */
  public void fillLake(ArrayList<Tile> lake) {
    //System.out.println("\nFilling a lake which starts at "+lake);
    int height = lake.get(0).waterLevel();
    //System.out.println("The height is "+height);
    //System.out.println("To be sure, the height of the next one is "+lake.get((int)(Math.random()*lake.size())).waterLevel());
    ArrayList<Tile> shore = new ArrayList<Tile>();
    for (Tile til: lake) // defines the shore as all tiles adjacent to and not in the lake
    for (Tile adj: adjacentTo(til))
      if (!lake.contains(adj) && !shore.contains(adj))
      shore.add(adj);
    //System.out.println("The shore is "+shore);
    
    Tile low = shore.get(0);
    for (Tile til: shore)
      if (til.altitude+til.water < low.altitude+low.water) // cycles through shore to find lowest point
      low = til;
    
    //System.out.println("The lowest point on the shore is at "+low.waterLevel());
    if (low.waterLevel() < height || low.altitude < 0) { // if it is an outlet or the ocean
      for (Tile til: lake) {
        til.temp1 = low.lat; // sets path from lake to outlet
        til.temp2 = low.lon;
        map[til.lat][til.lon].temp1 = low.lat; // accounts for some bug in my OO skills
        map[til.lat][til.lon].temp2 = low.lon;
      }
      return;
    }
    if (low.altitude+low.water > height) { // if the lowest is above this lake
      //System.out.println("Raising the lake level to "+low.waterLevel()+" to match "+low);
      for (Tile til: lake) { // fill the lake some more and try again
        til.water = low.waterLevel() - til.altitude;
        map[til.lat][til.lon].water = low.waterLevel() - til.altitude;
      }
      lake.add(low);
      fillLake(lake);
    }
    else { // if the lowest is on the same level
      //System.out.println("Incorporating "+low);
      lake.add(low); // join the club and try again
      fillLake(lake);
    }
  }
  
  
  public void runoffFrom(Tile start, double amount) {
    if (start.altitude < 0) // do not bother with ocean
      return;
    
    start.water += amount;
    start.temp3 = 1; // indicate start is checked
    
    if (start.temp1 < 0) // if temp1 is not set
      System.out.println("Warning: a river flowed into the void"); // throw error message
    else if (map[start.temp1][start.temp2].temp3 == 0) // if the next one is not checked
      runoffFrom(map[start.temp1][start.temp2], amount);
    else  System.out.println("JOOWEE! JOOWEE! Rivers are flowing into themselves! FillLake() is not doing its job!");
    
    start.temp3 = 0;
  }
  
  
  public void climateEnhance() {
    for (int i = 0; i < 16; i ++) { // smooths out the climate
      for (Tile[] row: map) {
        for (Tile til: row) {
          ArrayList<Tile> set = adjacentTo(til);
          int rain = 0;
          int temp = 0;
          for (Tile adj: set) {
            rain += adj.rainfall;
            temp += adj.temperature;
          }
          til.rainfall = (int)(.9*til.rainfall + .1*rain/set.size());
          til.temperature = (int)(.9*til.temperature + .1*temp/set.size());
        }
      }
    }
    
    for (Tile[] row: map) {
      for (Tile til: row) {
        if (til.altitude < 0) // applies orographic effect (tiles draw moisture from sea winds)
          til.rainfall = 255;
        else {
          til.rainfall += moisture(til, -1, 16);
          til.rainfall += moisture(til, 1, 16);
        }
        til.temperature -= (int)Math.abs(til.altitude) >> 3; // cools down extreme altitudes
      }
    }
  }
  
  
  public void biomeAssign() { // assigns each tile a biome based on rainfall, altitude, and temperature
    for (Tile[] row: map) {
      for (Tile til: row) {
        if (til.altitude < 0) { // if below sea level
          if (til.temperature + 8*Math.sin(til.rainfall) < 120) { // if cold
            til.biome = Tile.ice;
          }
          else if (til.altitude < -64) { // if super deep
            til.biome = Tile.trench;
          }
          else if (til.temperature < 234) { // if warm
            til.biome = Tile.ocean;
          }
          else { // if hot
            til.biome = Tile.reef;
          }
        }
        else if (til.water > 16) { // if has freshwater on it
          til.biome = Tile.freshwater;
        }
        else if (til.altitude < 64) { // if low altitude
          if (til.temperature + 4*(Math.sin(til.rainfall)) < 140) { // if cold
            til.biome = Tile.tundra;
          }
          else if (til.temperature >= 150 && til.rainfall >= 229) { // if hot and wet
            til.biome = Tile.jungle;
          }
          else if ((255-til.temperature)*(255-til.temperature) + (til.rainfall-180)*(til.rainfall-180) < 2600) { // if hot and dry
            til.biome = Tile.desert;
          }
          else { // if neutral
            til.biome = Tile.plains;
          }
        }
        else { // if mountainous
          if (til.temperature + 4*(Math.sin(til.rainfall)) < 140) { // if cold
            til.biome = Tile.snowcap;
          }
          else { // if warm
            til.biome = Tile.mountain;
          }
        }
      }
    }
  }
}