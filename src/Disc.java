import java.util.*;
import java.awt.*;
import org.apache.commons.lang3.ArrayUtils;



public class Disc implements Surface { // a thin three-dimensional disc
  public Tile[][] map; // the irregular matrix of tiles representing the surface
  private int radius; // the radius of the disc
  private Tile meteorTarget; // the next place to put a meteor (usually null)
  
  
  
  public Disc(int r) {
    radius = r;
    map = new Tile[radius<<1][radius<<1]; // the map is a plane of tiles one-eight as deep as it is wide
    
    for (int lat = 0; lat < map.length; lat ++)
      for (int lon = 0; lon < map[lat].length; lon ++)
        map[lat][lon] = new Tile(lat, lon);
    meteorTarget = null;
  }
  
  
  public Disc(Disc source) { // copies a preexisting globe
    radius = source.getRadius();
    map = new Tile[radius<<1][radius<<1]; // the map is a thin box of tiles one-eight as deep as it is wide
    
    for (int lat = 0; lat < map.length; lat ++)
      for (int lon = 0; lon < map[lat].length; lon ++)
        map[lat][lon] = new Tile(source.getTileByIndex(lat,lon));
    meteorTarget = null;
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
      System.out.println("Error accessing "+lat+","+lon);
      return new Tile(-1, -1);
    }
    
    int y = (int)(lat*radius/Math.PI*Math.sin(lon)+radius);
    int x = (int)(lat*radius/Math.PI*Math.cos(lon)+radius);
    
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
    
    return new Point((int)(lattitude*radius/Math.PI*Math.sin(longitude)+radius),
                     (int)(lattitude*radius/Math.PI*Math.cos(longitude)+radius));
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
    
    for (int dx = -2; dx <= 2; dx ++)
      for (int dy = -2; dy <= 2; dy ++)
        if (dx*dx+dy*dy < 7 && (dx!=0 || dy!= 0)) // adjacent tiles can actually have some distance between them in Discs, to simulate 3 dimensions
          if (x+dx>=0 && x+dx<map[0].length && y+dy>=0 && y+dy<map.length)
            output.add(map[y+dy][x+dx]);
    
    return output;
  }
  
  
  @Override
  public final int count(int testAlt) { // counts tiles under a given altitude existing on the map
    int count = 0;
    for (Tile[] row: map)
      for (Tile til: row)
        if (til.altitude < testAlt)
          count ++;
    return count;
  }
  
  
  @Override
  public final double distance(Tile from, Tile to) { // calculates the distance between two tiles
    return Math.sqrt(Math.pow(from.lat-to.lat,2) + Math.pow(from.lon-to.lon,2));
  }
  
  
  public final boolean randChance(int p) { // scales an int to a probability and returns true that probability of the time
    return Math.random() < 1 / (1+Math.pow(Math.E, -.1*p));
  }
  
  
  @Override
  public double latByTil(Tile til) { // lattitude angle of til
    return Math.hypot(til.lat-radius,til.lon-radius)*Math.PI/radius;
  }
  
  
  @Override
  public double lonByTil(Tile til) { // longitude angle of til
    return Math.atan2(til.lat-radius,til.lon-radius);
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