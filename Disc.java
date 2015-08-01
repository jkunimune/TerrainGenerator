import java.util.*;



public class Disc implements Surface{ // a thin three-dimensional disc
  public Tile[][][] map; // the irregular matrix of tiles representing the surface
  private int radius; // the radius of the disc
  
  
  
  public Disc(int r) {
    radius = r;
    map = new Tile[radius>>2][radius<<1][radius<<1]; // the map is a thin box of tiles one-eight as deep as it is wide
    
    for (int alt = 0; alt < map.length; alt ++) // initializes all elements
      for (int lat = 0; lat < map[alt].length; lat ++)
        for (int lon = 0; alt < map[alt][lat].length; lon ++)
          map[alt][lat][lon] = new Tile(alt, lat, lon);
  }
  
  
  public Disc(Disc source) { // copies a preexisting globe
    radius = source.getRadius();
    map = new Tile[radius>>2][radius<<1][radius<<1]; // the map is a thin box of tiles one-eight as deep as it is wide
    
    for (int alt = 0; alt < map.length; alt ++) // initializes all elements
      for (int lat = 0; lat < map[alt].length; lat ++)
        for (int lon = 0; alt < map[alt][lat].length; lon ++)
          map[alt][lat][lon] = new Tile(source.getTileByIndex(alt, lat, lon));
  }
  
  
  
  @Override
  public final Tile getTile(double lat, double lon) { // returns a tile at a given coordinate
    if (lat < 0) {
      System.out.println("Error accessing "+lat+","+lon);
      return new Tile(-1, -1);
    }
    
    int z = map.length>>1;
    int y = (int)(lat*radius/Math.PI*Math.sin(lon));
    int x = (int)(lat*radius/Math.PI*Math.cos(lon));
    
    return map[z][y][x];
  }
  
  
  @Override
  public final Tile getTileByIndex(int lat, int lon) {
    return map[radius>>1][lat][lon];
  }
  
  
  public final Tile getTileByIndex(int z, int y, int x) {
    return map[z][y][x];
  }
  
  
  @Override
  public final int latIndex(double lattitude) { // converts a lattitude to an index
    if (lattitude == Math.PI)
      return map.length-1;
    return (int)(lattitude*map.length/Math.PI);
  }
  
  
  @Override
  public final int lonIndex(int lat, double longitude) { // converts an index and a longitude to a secondary index.
    if (longitude < 0) // makes longitude valid
      longitude = 2*Math.PI - ((-longitude)%(2*Math.PI));
    if (longitude >= 2*Math.PI)
      longitude = longitude%(2*Math.PI);
    
    return (int)(longitude*map[lat].length/(2*Math.PI));
  }
  
  
  public final Tile[][] getTileMatrix() {
    return map[radius>>1];
  }
  
  
  public final int getRadius() {
    return radius;
  }
  
  
  @Override
  public final ArrayList<Tile> adjacentTo(Tile tile) { // returns an arrayList of tiles adjacent to a given tile
    int x = tile.lon;
    int y = tile.lat;
    int z = tile.alt;
    ArrayList<Tile> output = new ArrayList<Tile>(6); // initializes the output
    
    try {
      output.add(map[z][y][x-1]);
    } catch (Error e) {}
    try {
      output.add(map[z][y][x+1]);
    } catch (Error e) {}
    try {
      output.add(map[z][y-1][x]);
    } catch (Error e) {}
    try {
      output.add(map[z][y+1][x]);
    } catch (Error e) {}
    try {
      output.add(map[z-1][y][x]);
    } catch (Error e) {}
    try {
      output.add(map[z+1][y][x]);
    } catch (Error e) {}
    
    return output;
  }
  
  
  @Override
  public final boolean any(int testAlt) { // checks if any of a given altitude exists on the map
    for (Tile[][] lvl: map)
      for (Tile[] row: lvl)
        for (Tile til: row)
          if (til.altitude == testAlt)
            return true;
    return false;
  }
  
  
  @Override
  public final double distance(Tile from, Tile to) { // calculates the distance between two tiles
    return Math.sqrt(Math.pow(from.alt-to.alt,2) + Math.pow(from.lat-to.lat,2) + Math.pow(from.lon-to.lon,2));
  }
  
  
  @Override
  public final boolean randChance(int p) { // scales an int to a probability and returns true that probability of the time
    return Math.random() < 1 / (1+Math.pow(Math.E, -.1*p));
  }
}