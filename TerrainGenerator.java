public class TerrainGenerator{ // a class to generate and display terrain onto a spherical surface
  public static void main( String args[] )
  {
    Globe world = new Globe(10);
    Map original = new Map(800, 600);
    Map tested = new Map(800, 600);
    
    world.randomize();
    original.equirectangular(world, "biome");
    world.test();
    tested.equirectangular(world, "biome");
  }
}