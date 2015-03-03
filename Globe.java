import java.util.*;



public class Globe { // a class to create a spherical surface and generate terrain onto it
  private Tile[][] map;
  private int radius;
  
  
  
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
  
  
  /* PRECONDITION: each Map's Globe is this */
  public void generate(ArrayList<Map> maps) { // randomly generates a map and simultaneously displays it on several projections
    for (Map m: maps)
      m.display("altitude");
      
    this.spawnContinent();
    
    while (any(-257)) {
      delay(10);
      this.spawnContinents();
      for (Map m: maps)
        m.display("altitude");
    }
    
    this.plateTechtonics();
    for (Map m: maps)
      m.display("altitude");
  }
  
  
  /* PRECONDITION: map's Globe is this */
  public void generate(Map map) { // randomly generates a map and simultaneously displays it
    map.display("altitude");
      
    this.spawnContinent();
    
    while (any(-257)) {
      delay(10);
      this.spawnContinents();
      map.display("altitude");
    }
    
    this.plateTechtonics();
    map.display("altitude");
  }
  
  
  public void generate() { // randomly generates a map
    this.spawnContinent();
    
    while (any(-257))
      this.spawnContinents();
    
    this.plateTechtonics();
  }
  
  
  public void test() { // only used for testing purposes
    for (int lat = 0; lat < map.length; lat ++)
      for (int lon = 0; lon < map[lat].length/2; lon ++) {
        map[lat][lon].altitude = 63;
        map[lat][lon].temp1 = 63;
        map[lat][lon].temp2 = 314;
        map[lat][lon].temp3 = 0;
      }
    
    for (int lat = 0; lat < map.length; lat ++)
      for (int lon = map[lat].length/2; lon < map[lat].length; lon ++) {
        map[lat][lon].altitude = -64;
        map[lat][lon].temp1 = -64;
        map[lat][lon].temp2 = 0;
        map[lat][lon].temp3 = 0;
      }
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
  
  
  public void spawnContinent() { // initializes a single techtonic plate
    this.getTile(Math.random()*Math.PI, Math.random()*2*Math.PI).startPlate(Math.random()<.5);
  }
  
  
  public void spawnContinents() { // sets some tiles to a continent or an ocean
    for (Tile[] row: map) {
      for (Tile tile: row) {
        if (tile.altitude == -257) {
          ArrayList<Tile> adjacent = adjacentTo(tile);
          for (Tile ref: adjacent) // reads all adjacent tiles to look for land or sea
            if (ref.altitude >= -256 && randChance(-40 + (int)(Math.pow(ref.altitude,2)/128))) // deeper/higher continents spread faster
            tile.spreadFrom(ref);
          
          if (tile.altitude == -257 && randChance(-150)) // I realize I check that the biome is 0 kind of a lot, but I just want to avoid any excess computations
            tile.startPlate(false); // seeds new plates occasionally
          else if (tile.altitude == -257 && randChance(-143))
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
    for (Tile[] thisRow: map) {
      for (Tile thisTil: thisRow) {
        double totalChange = 0; // keeps track of how much to change the altitude
        for (Tile[] thatRow: map) {
          for (Tile thatTil: thatRow) {
            if (!thatTil.equals(thisTil)) { // assuming they are not the same tile
              if (thisTil.temp2 == thatTil.temp2 && thisTil.temp3 == thatTil.temp3) // ignore them if they are on the same plate
                break;
              
              Vector r1 = new Vector(1, thisTil.lat*Math.PI/map.length, thisTil.lon*2*Math.PI/map[thisTil.lat].length);
              Vector r2 = new Vector(1, thatTil.lat*Math.PI/map.length, thatTil.lon*2*Math.PI/map[thatTil.lat].length);
              Vector delTheta = r1.cross(r2);
              delTheta.setR(r1.angleTo(r2)*radius); // the distance between them
              
              if (delTheta.getR() > 10) // if they are farther than 10 tiles apart, ignore them
                break;
              
              Vector omega1 = new Vector(1, thisTil.temp2/100.0, thisTil.temp3/100.0);
              Vector omega2 = new Vector(1, thatTil.temp2/100.0, thatTil.temp3/100.0);
              Vector delOmega = omega1.minus(omega2); // how fast they are moving toward each other
              
              double rise = 4.0*delOmega.dot(delTheta)/Math.pow(delTheta.getR(),2);
              
              if (thisTil.altitude < 0) { // if this is ocean
                if (rise < 0) { // if they are going towards each other
                  if (thisTil.altitude < thatTil.altitude) { // if this is lower than that one
                    totalChange += rise/1; // it forms a sea trench
                  }
                  else { // if this is above that one
                    totalChange -= rise/1; // it forms an island chain
                  }
                }
                else { // if they are going away from each other
                  totalChange -= rise/2; // it forms an ocean rift
                }
              }
              else { // if this is land
                if (rise < 0) { // if they are going towards each other
                  totalChange -= rise/1; // it forms a mountain range
                }
                else { // if they are going away from each other
                  totalChange -= rise/1; // it forms a valley
                }
              }
            }
          }
        }
        thisTil.temp1 += totalChange;
      }
    }
    
    for (Tile[] thisRow: map)
      for (Tile thisTil: thisRow)
        thisTil.altitude = thisTil.temp1;
  }
//  
//  
//  public void randomize() {
//  }
//  
//  
//  public void smooth() {
//  }
//  
//  
//  public void orographicEffect() {
//  }
//  
//  
//  public void biomeAssign() {
//  }
//  
//  
//  public void rainFall() {
//  }
//  
//  
//  public void generate() {
//  }
//  
//  
  public Tile getTile(double lat, double lon) { // returns a tile at a given coordinate
    if (lat < 0 || lat > Math.PI) {
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
//  
//  
//  public Tile[][] getTileMatrix() {
//  }
//  
//  
//  public int distance(int lat1, int lon1, int lat2, int lon2) {
//  }
//  
//  
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
  
  
  public static void delay(int mSec) {
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() < start+mSec) {}
  }
}