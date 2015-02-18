public class TerrainGenerator{ // a class to generate and display terrain onto a spherical surface
  public static void main( String args[] )
  {
    Globe world = new Globe(20);
    Map theMap = new Map();
    
    world.randomize();
    theMap.equirectangular(world);
    theMap.show();
  }
}