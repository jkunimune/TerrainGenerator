public class Globe { // a class to create a spherical surface and generate terrain onto it
  private Tile[][] map;
  private int radius;
  
  
  
  public Globe(int r) {
    map = new Tile[(int)(r*Math.PI)][]; // the map is a matrix of tiles with varying width
    map[0] = new Tile[1]; // the top and bottom are of width 1 (the poles)
    map[map.length-1] = new Tile[1];
    
    for (int lat = 1; lat < map.length/2; lat ++) { // the length of each row is determined with trig
      map[lat] = new Tile[(int)(2*Math.PI*Math.sin(lat))/map[lat-1].length*map[lat-1].length];
      map[map.length-lat] = map[lat]; // the top and bottom are symmetrical
    }
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
//  public Tile getTile(int lat, int lon) {
//  }
//  
//  
//  public Tile[][] getTileMatrix() {
//  }
//  
//  
//  public Color getColor(int lat, int lon) {
//  }
//  
//  
//  public Color[][] getColorMatrix() {
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