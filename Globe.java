import java.util.*;



public class Globe { // a class to create a spherical surface and generate terrain onto it
  private Tile[][] map; // the irregular matrix of tiles representing the surface
  private int radius; // the radius of the sphere
  
  
  
  public Globe(int r) {
    radius = r;
    map = new Tile[(int)(r * Math.PI)][]; // the map is a matrix of tiles with varying width
    
    map[0] = new Tile[1]; // the top and bottom are of width 1 (the poles)
    map[map.length-1] = new Tile[1];
    
    for (int lat = 1; lat <= map.length/2; lat ++) {
      int width = (int)(2*Math.PI * r * Math.sin(lat*Math.PI/map.length)); // the length of each row is determined with trig
      width = width / map[lat-1].length * map[lat-1].length; // each row has length divisible with row above it (for convinience)
      
      map[lat] = new Tile[width];
      map[map.length-lat-1] = new Tile[width]; // the top and bottom are symmetrical
    }
    
    for (int lat = 0; lat < map.length; lat ++) { // initializes all elements
      for (int lon = 0; lon < map[lat].length; lon ++) {
        map[lat][lon] = new Tile(lat, lon);
      }
    }
  }
  
  
  public Globe(Globe source) { // copies a preexisting globe
    radius = source.getRadius();
    map = new Tile[(int)(radius * Math.PI)][]; // the map is a matrix of tiles with varying width
    
    map[0] = new Tile[1]; // the top and bottom are of width 1 (the poles)
    map[map.length-1] = new Tile[1];
    
    for (int lat = 1; lat <= map.length/2; lat ++) {
      int width = (int)(2*Math.PI * radius * Math.sin(lat*Math.PI/map.length)); // the length of each row is determined with trig
      width = width / map[lat-1].length * map[lat-1].length; // each row has length divisible with row above it (for convinience)
      
      map[lat] = new Tile[width];
      map[map.length-lat-1] = new Tile[width]; // the top and bottom are symmetrical
    }
    
    for (int lat = 0; lat < map.length; lat ++) { // initializes all elements
      for (int lon = 0; lon < map[lat].length; lon ++) {
        map[lat][lon] = new Tile(source.getTileByIndex(lat, lon));
      }
    }
  }
  
  
  
  /* Generation Methods */
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
          delTheta.setR(r1.angleTo(r2)*radius); // the distance between them
          
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
                totalChange -= rise*.75; // it forms an island chain
              }
              else { // if they are going at the same speed
                totalChange -= rise/16; // it forms a taller rift
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
          
          if (lowest.altitude+lowest.water < til.altitude+til.water) { // if water can flow
            til.temp1 = lowest.lat; // set the water path from here to there
            til.temp2 = lowest.lon;
          }
          else if (lowest.altitude+lowest.water == til.altitude+til.water) { // if there is a lake
            til.temp1 = -1;
            til.temp2 = -1;
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
        runoffFrom(til, (til.rainfall>>6)+1); // fills rivers
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
    System.out.println("\nFilling a lake which starts at "+lake);
    int height = lake.get(0).waterLevel();
    System.out.println("The height is "+height);
    System.out.println("To be sure, the height of the next one is "+lake.get((int)(Math.random()*lake.size())).waterLevel());
    ArrayList<Tile> shore = new ArrayList<Tile>();
    for (Tile til: lake) // defines the shore as all tiles adjacent to and not in the lake
      for (Tile adj: adjacentTo(til))
        if (!lake.contains(adj) && !shore.contains(adj))
          shore.add(adj);
    System.out.println("The shore is "+shore);
    
    Tile low = new Tile(-1, -1, 9001, 9001, 9001, 9001, 9001);
    for (Tile til: shore) {
      if (til.altitude < 0) // if lake is next to ocean, forget it
        return;
      else if (til.altitude+til.water < height) // if there is an outlet, we are done here
        return;
      else if (til.altitude+til.water < low.altitude+low.water) // cycles through shore to find lowest point
        low = til;
    }
    
    System.out.println("The lowest point on the shore is at "+low.waterLevel());
    
    if (low.altitude+low.water > height) { // if the lowest is above this lake
      System.out.println("Raising the lake level to "+low.waterLevel()+" to match "+low);
      for (Tile til: lake) { // fill the lake some more and try again
        til.water = low.waterLevel() - til.altitude;
        map[til.lat][til.lon].water = low.waterLevel() - til.altitude;
      }
      lake.add(low);
      fillLake(lake);
    }
    else if (low.altitude+low.water == height) { // if the lowest is on the same level
      System.out.println("Incorporating "+low);
      lake.add(low); // join the club and try again
      fillLake(lake);
    }
    else
      System.out.println("This should not trigger.  If you are reading this, an outlet was incorporated into the lake.  Gravity must be broken.");
  }
  
  
  public void runoffFrom(Tile start, int amount) {
    if (start.altitude < 0) // do not bother with ocean
      return;
    
    start.water += amount;
    start.altitude -= amount>>16;
    start.temp3 = 1; // indicate start is checked
    
    if (start.temp1 < 0) { // if it flows into a lake
      return; // the river ends here
    }
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
        else if (til.water > 128) { // if has freshwater on it
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
  
  
  /* Utility Methods */
  public Tile getTile(double lat, double lon) { // returns a tile at a given coordinate
    if (lat < 0 || lat >= Math.PI) {
      System.out.println("Error accessing "+lat+","+lon);
      return new Tile(-1, -1);
    }
    
    lon %= 2*Math.PI;
    if (lon < 0)
      lon += 2*Math.PI;
    
    int y = (int)(lat*map.length/Math.PI);
    int x = (int)(lon*map[y].length/(2*Math.PI));
    return map[y][x];
  }
  
  
  public Tile getTileByIndex(int lat, int lon) {
    return map[lat][lon];
  }
  
  
  public int latIndex(double lattitude) { // converts a lattitude to an index
    return (int)(lattitude*map.length/Math.PI);
  }
  
  
  public int lonIndex(int lat, double longitude) { // converts an index and a longitude to a secondary index.
    return (int)(longitude*map[lat].length/(2*Math.PI));
  }
  
  
  public Tile[][] getTileMatrix() {
    return map;
  }
  
  
  public int getRadius() {
    return radius;
  }
  
  
  public ArrayList<Tile> adjacentTo(Tile tile) { // returns an arrayList of tiles adjacent to a given tile
    if (tile.lat == 0)
      return new ArrayList<Tile>(Arrays.asList(map[1])); // returns a whole row if it is a pole
    if (tile.lat == map.length-1)
      return new ArrayList<Tile>(Arrays.asList(map[map.length-2]));
    
    //System.out.println("Running adjacentTo for the tile with index "+tile.lat+", "+tile.lon);
    ArrayList<Tile> output = new ArrayList<Tile>(); // initializes the output
    output.add(map [tile.lat] [(tile.lon+1)%map[tile.lat].length]); // adds the tiles laterally adjacent
    output.add(map [tile.lat] [(tile.lon-1+map[tile.lat].length)%map[tile.lat].length]); // does a bunch of complex addition and moduli to keep it in bounds
    if (tile.lat < map.length/2) { // behaves differently from here for the northern and southern hemispheres
      output.add(map [tile.lat-1] [tile.lon*map[tile.lat-1].length/map[tile.lat].length]); // adds the one north of it
      for (int i = 0; i < map [tile.lat+1].length/map[tile.lat].length; i ++) // adds all the ones south of it
        output.add(map [tile.lat+1] [tile.lon*map[tile.lat+1].length/map[tile.lat].length+i]);
    }
    else {
      output.add(map[tile.lat+1][tile.lon*map[tile.lat+1].length/map[tile.lat].length]); // adds the one north of it
      for (int i = 0; i < map[tile.lat-1].length/map[tile.lat].length; i ++) // adds all the ones south of it
        output.add(map[tile.lat-1][tile.lon*map[tile.lat-1].length/map[tile.lat].length+i]);
    }
    
    return output;
  }
  
  
  public boolean any(int testAlt) { // checks if any of a given altitude exists on the map
    for (Tile[] row: map)
      for (Tile t: row)
        if (t.altitude == testAlt)
          return true;
    return false;
  }
  
  
  private boolean randChance(int p) { // scales an int to a probability and returns true that probability of the time
    return Math.random() < 1 / (1+Math.pow(Math.E, -.1*p));
  }
}