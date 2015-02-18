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
        map[lat][lon] = new Tile();
      }
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
  
  
//  public void spawnContinents() {
//  }
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
  public Tile getTile(double lat, double lon) {
    int x = (int)(lat*map.length/Math.PI);
    int y = (int)(lon*map[x].length/(2*Math.PI));
    return map[x][y];
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
//  public Tile[] adjacentTo(int lat, int lon) {
//  }
}