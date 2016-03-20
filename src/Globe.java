import java.awt.*;
import java.util.*;
import org.apache.commons.lang3.ArrayUtils;



public class Globe implements Surface{ // a spherical surface
  public Tile[][] map; // the irregular matrix of tiles representing the surface
  private int radius; // the radius of the sphere
  private Tile meteorTarget; // the tile to be hit by a meteor
  
  
  
  public Globe(int r) {
    radius = r;
    map = new Tile[(int)(r * Math.PI)][]; // the map is a matrix of tiles with varying width
    
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
// FOR TESTING
//    for (Tile[] row: map) {
//      for (Tile til: row)
//        System.out.print(".");
//      System.out.println(row.length);
//    }
  }
  
  
  public Globe(Globe source) { // copies a preexisting globe
    radius = source.getRadius();
    map = new Tile[(int)(radius * Math.PI)][]; // the map is a matrix of tiles with varying width
    
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
      System.err.println("Error accessing "+lat+","+lon);
      return new Tile(-1, -1);
    }
    if (lat == Math.PI)
      return map[map.length-1][0];
    
    lon %= 2*Math.PI;
    if (lon < 0)
      lon += 2*Math.PI;
    
    int y = (int)(lat*map.length/Math.PI);
    int x = (int)(lon*map[y].length/(2*Math.PI));
    return map[y][x];
  }
  
  
  @Override
  public final Tile getTileByIndex(int lat, int lon) {
    return map[lat][lon];
  }
  
  
  @Override
  public final Point tilByAngles(double lattitude, double longitude) { // converts a lattitude and longitude to indicies
    if (lattitude < 0 || lattitude > Math.PI) { // extraneous lattitudes will not do
      System.err.println("Error accessing "+lattitude+","+longitude);
      return new Point(-1, -1);
    }
    
    longitude %= 2*Math.PI; // puts longitude into a usable range
    if (longitude < 0)
      longitude += 2*Math.PI;
    
    int y = (int)(lattitude*map.length/Math.PI);
    if (y == map.length) // forces pixels looking for pi to look at the southmost tile
      y --;
    int x = (int)(longitude*map[y].length/(2*Math.PI));
    if (x >= map[y].length)
      x = map[y].length-1; // in the event of unfortunate math screw-ups on java's part, look to the last tile
    
    return new Point(x, y);
  }
  
  
  @Override
  public final double latByTil(Tile til) {
    return (til.lat+.5)/radius;
  }
  
  
  @Override
  public final double lonByTil(Tile til) {
    return (til.lon+.5)/map[til.lat].length*(2*Math.PI);
  }
  
  
  public final Tile[][] getTileMatrix() {
    return map;
  }
  
  
  public final int getRadius() {
    return radius;
  }
  
  
  @Override
  public final ArrayList<Tile> adjacentTo(Tile tile) { // returns an arrayList of tiles adjacent to a given tile
    if (tile.lat == 0)
      return new ArrayList<Tile>(Arrays.asList(map[1])); // returns a whole row if it is a pole
    if (tile.lat == map.length-1)
      return new ArrayList<Tile>(Arrays.asList(map[map.length-2]));
    
    ArrayList<Tile> output = new ArrayList<Tile>(); // initializes the output
    output.add(map [tile.lat] [(tile.lon+1)%map[tile.lat].length]); // adds the tiles laterally adjacent
    output.add(map [tile.lat] [(tile.lon-1+map[tile.lat].length)%map[tile.lat].length]); // does a bunch of complex addition and moduli to keep it in bounds
    if (tile.lat < map.length/2) { // behaves differently from here for the northern and southern hemispheres
      output.add(map [tile.lat-1] [tile.lon*map[tile.lat-1].length/map[tile.lat].length]); // adds the one north of it
      final int min = tile.lon*map[tile.lat+1].length/map[tile.lat].length;
      final int max = (tile.lon+1)*map[tile.lat+1].length/map[tile.lat].length;
      for (int i = min; i < max; i ++) // adds all the ones south of it
        output.add(map [tile.lat+1] [i]);
    }
    else {
      output.add(map[tile.lat+1][tile.lon*map[tile.lat+1].length/map[tile.lat].length]); // adds the one south of it
      final int min = tile.lon*map[tile.lat-1].length/map[tile.lat].length;
      final int max = (tile.lon+1)*map[tile.lat-1].length/map[tile.lat].length;
      for (int i = min; i < max; i ++) // adds all the ones north of it
        output.add(map [tile.lat-1] [i]);
    }
    
    return output;
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
    if (meteorTarget != null) {
      Tile temp = meteorTarget;
      meteorTarget = null;
      return temp;
    }
    else
      return null;
  }
  
  
  @Override
  public void meteor(Tile t) {
	  meteorTarget = t;
  }
}