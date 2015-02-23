import java.util.*;



public class Globe { // a class to create a spherical surface and generate terrain onto it
  private Tile[][] map;
  private int radius;
  
  
  
  public Globe(int r) {
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
  
  
  public void test() { // only used for testing purposes
    for (Tile t: adjacentTo(getTile(Math.PI/2, 3)))
      t.biome = 0;
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
  
  
  public void spawnContinents() { // sets everything to a continent or an ocean
    //while (any(0)) {
      for (Tile[] row: map) {
        for (Tile tile: row) {
          if (tile.altitude == -257) {
            ArrayList<Tile> adjacent = adjacentTo(tile);
            for (Tile ref: adjacent) // reads all adjacent tiles to look for land or sea
              if (ref.altitude >= -256 && randChance(-8))
                tile.spreadFrom(ref);
            
            if (tile.altitude == -257 && randChance(-146)) // I realize I check that the biome is 0 kind of a lot, but I just want to avoid any excess computations
              tile.startPlate(false); // seeds new plates occasionally
            else if (tile.altitude == -257 && randChance(-139))
              tile.startPlate(true);
          }
        }
      }
      
      for (Tile[] row: map) // copies the temporary variables to altitude
        for (Tile tile: row)
          if (tile.temp1 < -56 || tile.temp1 >= 56) // only copies those that have been set to something
            tile.altitude = tile.temp1;
    }
  //}
//  
//  
//  public void plateTechtonics() {
//  }
//  
//  
//  public void randomize() {
//  }
//  
//  
//  public void orographicEffect() {
//  }
//  
//  
//  public void smooth() {
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
    if (lat < 0 || lat > Math.PI || lon < 0 || lon > 2*Math.PI) {
      System.out.println("Error accessing "+lat+","+lon);
      return new Tile(-1, -1);
    }
    
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
}