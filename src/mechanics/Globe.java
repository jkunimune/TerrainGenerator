package mechanics;
import java.util.*;
import org.apache.commons.lang3.ArrayUtils;



public class Globe implements Surface{ // a spherical surface
  public Tile[][] map; // the irregular matrix of tiles representing the surface
  private int radius; // the radius of the sphere
  private Tile meteorTarget; // the tile to be hit by a meteor
  
  
  
  public Globe(int r) {
    radius = r;
    map = new Tile[(int)(radius * Math.PI * Math.sqrt(4/3.0))][]; // the map is a matrix of tiles with varying width
    
    map[0] = new Tile[1]; // the top and bottom are of width 1 (the poles)
    map[map.length-1] = new Tile[1];
    
    for (int lat = 1; lat <= map.length/2; lat ++) {
      int width = (int)(2*Math.PI * r * Math.sin(lat*Math.PI/map.length)); // the length of each row is determined with trig
      
      map[lat] = new Tile[width];
      map[map.length-lat-1] = new Tile[width]; // the top and bottom are symmetrical
    }
    
    for (int lat = 0; lat < map.length; lat ++) { // initializes all elements
      for (int lon = 0; lon < map[lat].length; lon ++) {
        map[lat][lon] = new Tile(lat, lon);
      }
    }
    
    for (Tile[] row: map)
      for (Tile til: row)
        til.adjacent = adjacentTo(til);	// tells each tile its neighbors
// FOR TESTING
//    for (Tile[] row: map) {
//      for (Tile til: row)
//        System.out.print(".");
//      System.out.println(row.length);
//    }
  }
  
  
  public Globe(Globe source) { // copies a preexisting globe
    radius = source.getRadius();
    map = new Tile[(int)(radius * Math.PI * Math.sqrt(4/3.0))][]; // the map is a matrix of tiles with varying width
    
    map[0] = new Tile[1]; // the top and bottom are of width 1 (the poles)
    map[map.length-1] = new Tile[1];
    
    for (int lat = 1; lat <= map.length/2; lat ++) {
      int width = (int)(2*Math.PI * radius * Math.sin(lat*Math.PI/map.length)); // the length of each row is determined with trig
      
      map[lat] = new Tile[width];
      map[map.length-lat-1] = new Tile[width]; // the top and bottom are symmetrical
    }
    
    for (int lat = 0; lat < map.length; lat ++) { // initializes all elements
      for (int lon = 0; lon < map[lat].length; lon ++) {
        map[lat][lon] = new Tile(source.getTileByIndex(lat, lon));
      }
    }
  }
  
  
  
  @Override
  public final Tile[] list() {
    Tile[] output = new Tile[0];
    for (Tile[] row: map)
      output = ArrayUtils.addAll(output, row);
    return output;
  }
  
  
  @Override
  public final Tile getTile(double lat, double lon) { // returns a tile at a given coordinate
    if (lat < 0 || lat > Math.PI) {
      throw new IndexOutOfBoundsException("Error accessing "+lat+","+lon);
    }
    if (lat == Math.PI)
      return map[map.length-1][0];
    
    lon %= 2*Math.PI;
    if (lon < 0)
      lon += 2*Math.PI;
    if (lon == 2*Math.PI)
      lon = 0;
    
    int y = (int)(lat*map.length/Math.PI);
    int x = (int)(lon*map[y].length/(2*Math.PI));
    try {
      return map[y][x];
    } catch (ArrayIndexOutOfBoundsException e) {
      System.err.println("Error in Globe.getTile: tried to access map["+y+"]["+x+"] due to coordinates ("+lat+","+lon+")!");
      throw(e);
    }
  }
  
  
  @Override
  public final Tile getTileByIndex(int lat, int lon) {
    return map[lat][(lon+map[lat].length)%map[lat].length];
  }
  
  
  @Override
  public final double latByTil(Tile til) {	// gets coordinates from tiles
    return (til.lat + .5)*Math.PI/map.length;
  }
  
  
  @Override
  public final double lonByTil(Tile til) {	// gets coordinates from tiles
    return (til.lon + .5 + (til.lat%2)/2.0)/map[til.lat].length*(2*Math.PI);
  }
  
  
  public final Tile[][] getTileMatrix() {
    return map;
  }
  
  
  public final int getRadius() {
    return radius;
  }
  
  
  @Override
  public final Tile[] adjacentTo(Tile tile) { // returns an array of tiles adjacent to a given tile
    /*WARNING: It is recommended that the adjacent instance variable of Tile be used rather than this method in loops*/
    if (tile.lat == 0)
      return map[1]; // returns a whole row if it is a pole
    if (tile.lat == map.length-1)
      return map[map.length-2];
    
    ArrayList<Tile> output = new ArrayList<Tile>(); // initializes the output
    output.add(map [tile.lat] [(tile.lon+1)%map[tile.lat].length]); // adds the tiles laterally adjacent
    output.add(map [tile.lat] [(tile.lon-1+map[tile.lat].length)%map[tile.lat].length]); // does a bunch of complex addition and moduli to keep it in bounds
    final double westBound = (double)(tile.lon /*+ tile.lat%2/2.0*/)/map[tile.lat].length + 1;	// determines the bounds of the tiles
    final double eastBound = (double)(tile.lon+1 /*+ tile.lat%2/2.0*/)/map[tile.lat].length + 1;
    for (int i = (int)Math.floor(Math.round(westBound*map[tile.lat-1].length*100000)/100000.0 /*- (tile.lat-1)%2/2.0*/);
    		i <= (int)(Math.ceil(Math.round(eastBound*map[tile.lat-1].length*100000)/100000.0)-1); i ++)		// very complicated for loop to find adjacent tiles in other rows
      output.add(map[tile.lat-1][i%map[tile.lat-1].length]);
    for (int i = (int)Math.floor(Math.round(westBound*map[tile.lat+1].length*100000)/100000.0 /*- (tile.lat+1)%2/2.0*/);
    		i <= (int)(Math.ceil(Math.round(eastBound*map[tile.lat+1].length*100000)/100000.0)-1); i ++)
      output.add(map[tile.lat+1][i%map[tile.lat+1].length]);
    
    return output.toArray(new Tile[output.size()]);
  }
  
  
  @Override
  public final int count(int testAlt) { // checks if any of a given altitude exists on the map
    int count = 0;
    for (Tile[] row: map)
      for (Tile t: row)
        if (t.altitude < testAlt)
          count ++;
    return count;
  }
  
  
  @Override
  public final double distance(Tile from, Tile to) { // calculates the distance between two tiles
    return radius *
      (new Vector(1.0, from.lat*Math.PI/map.length, from.lon*2*Math.PI/map[from.lat].length)).angleTo
      (new Vector(1.0, to.lat*Math.PI/map.length, to.lon*2*Math.PI/map[to.lat].length));
  }
  
  
  public final boolean randChance(int p) { // scales an int to a probability and returns true that probability of the time
    return Math.random() < 1 / (1+Math.pow(Math.E, -.1*p));
  }
  
  
  @Override
  public Tile incomingMeteor() { // returns where to put a meteor, if any
    if (meteorTarget != null) {		// if there is an incoming meteor
      Tile temp = meteorTarget;		// return its target and reset meteorTarget
      meteorTarget = null;
      return temp;
    }
    else
      return null;	// otherwise, just ignore it.
  }
  
  
  @Override
  public void meteor(Tile t) {
	  meteorTarget = t;
  }
  
  
  public void verify() {	// checks to make sure adjacency is always a two-way street
    for (Tile til: this.list()) {
      for (Tile adj: adjacentTo(til)) {
        if (!Arrays.asList(adj.adjacent).contains(til)) {
          System.err.println("BAAAAAH! Why aren't "+til+" (out of "+map[til.lat].length+") and "+adj+" (out of "+map[adj.lat].length+") adjacent?!");
        }
      }
    }
    System.out.println("Verification Complete!");
  }
  
  
  public static void main(String[] args) {	// just checks a few things
    Globe g = new Globe(100);
    g.verify();
  }
}