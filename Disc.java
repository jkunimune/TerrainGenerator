import java.util.*;
import java.awt.*;



public class Disc implements Surface { // a thin three-dimensional disc
  public Tile[][] map; // the irregular matrix of tiles representing the surface
  private int radius; // the radius of the disc
  
  
  
  public Disc(int r) {
    radius = r;
    map = new Tile[radius<<1][radius<<1]; // the map is a plane of tiles one-eight as deep as it is wide
    
    for (int lat = 0; lat < map.length; lat ++)
      for (int lon = 0; lon < map[lat].length; lon ++)
        map[lat][lon] = new Tile(lat, lon);
  }
  
  
  public Disc(Disc source) { // copies a preexisting globe
    radius = source.getRadius();
    map = new Tile[radius<<1][radius<<1]; // the map is a thin box of tiles one-eight as deep as it is wide
    
    for (int lat = 0; lat < map.length; lat ++)
      for (int lon = 0; lon < map[lat].length; lon ++)
        map[lat][lon] = new Tile(source.getTileByIndex(lat,lon));
  }
  
  
  
  @Override
  public final Tile getTile(double lat, double lon) { // returns a tile at a given coordinate
    if (lat < 0) {
      System.out.println("Error accessing "+lat+","+lon);
      return new Tile(-1, -1);
    }
    
    int y = (int)(lat*radius/Math.PI*Math.sin(lon));
    int x = (int)(lat*radius/Math.PI*Math.cos(lon));
    
    return map[y][x];
  }
  
  
  @Override
  public final Tile getTileByIndex(int lat, int lon) {
    return map[lat][lon];
  }
  
  
  @Override
  public final Point tilByAngles(double lattitude, double longitude) { // converts a lattitude and longitude to indicies
    if (lattitude < 0) {
      System.out.println("Error accessing "+lattitude+","+longitude);
      return new Point(-1, -1);
    }
    
    return new Point((int)(lattitude*radius/Math.PI*Math.sin(longitude)),
                     (int)(lattitude*radius/Math.PI*Math.cos(longitude)));
  }
  
  
  public final Tile[][] getTileMatrix() {
    return map;
  }
  
  
  public final int getRadius() {
    return radius;
  }
  
  
  @Override
  public final ArrayList<Tile> adjacentTo(Tile tile) { // returns an arrayList of tiles adjacent to a given tile
    int x = tile.lon;
    int y = tile.lat;
    ArrayList<Tile> output = new ArrayList<Tile>(6); // initializes the output
    
    try {
      output.add(map[y][x-1]);
    } catch (Error e) {}
    try {
      output.add(map[y][x+1]);
    } catch (Error e) {}
    try {
      output.add(map[y-1][x]);
    } catch (Error e) {}
    try {
      output.add(map[y+1][x]);
    } catch (Error e) {}
    
    return output;
  }
  
  
  @Override
  public final boolean any(int testAlt) { // checks if any of a given altitude exists on the map
    for (Tile[] row: map)
      for (Tile til: row)
        if (til.altitude == testAlt)
          return true;
    return false;
  }
  
  
  @Override
  public final double distance(Tile from, Tile to) { // calculates the distance between two tiles
    return Math.sqrt(Math.pow(from.lat-to.lat,2) + Math.pow(from.lon-to.lon,2));
  }
  
  
  @Override
  public final boolean randChance(int p) { // scales an int to a probability and returns true that probability of the time
    return Math.random() < 1 / (1+Math.pow(Math.E, -.1*p));
  }
}