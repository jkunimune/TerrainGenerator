public class Galaxy extends Disc {
  public Galaxy(int r) {
    super(r);
  }
  
  
  public Galaxy(Disc g) {
    super(g);
  }
  
  
  
  public final void generate() {
    generate(new Map[0]);
  }
  
  
  /* PRECONDITION: map's Globe is world */
  public final void generate(Map map) { // randomly generates a map and simultaneously displays it
    Map[] sheath = new Map[1];
    sheath[0] = map;
    generate(sheath);
  }
  
  
  /* PRECONDITION: each map's Globe is world */
  public final void generate(Map[] maps) { // randomly generates a map and simultaneously displays it
    for (Map map: maps)
      map.display(ColS.biome);
    
    buildCore();
    
    for (Map map: maps)
      map.display(ColS.biome);
    
    formArms();
    
    for (Map map: maps)
      map.display(ColS.biome);
    
    for (int i = 0; i < 128; i ++) {
      thickenArms();
      for (Map map: maps)
        map.display(ColS.biome);
    }
  }
  
  
  private final void buildCore() {
    for (Tile[] row: map) {
      for (Tile til: row) {
        if (Math.pow(til.lat-map.length/2,2) + Math.pow(til.lon-map.length/2,2)
              < Math.pow(map.length/32.0,2))
          til.radioactive = true;
        if (Math.pow(til.lat-map.length/2,2) + Math.pow(til.lon-map.length/2,2)
              < Math.pow(map.length/8.0,2))
          til.biome = Tile.jungle;
        else
          til.biome = Tile.ocean;
      }
    }
  }

  
  private final void formArms() {
  }
  
  
  private final void thickenArms() {
  }
}