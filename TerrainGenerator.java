public class TerrainGenerator{ // a class to generate and display terrain onto a spherical surface
  public static void main( String args[] )
  {
    Globe world = new Globe(20);
    Map theMap = new Map(800, 600);
    
    world.randomize();
    theMap.equirectangular(world, "altitude");
    theMap.show();
  }
}