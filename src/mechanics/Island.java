package mechanics;

public class Island {
	private FinitePlane sfc;
	
	
	
	public Island(int w, int h) {
		sfc = new FinitePlane(w,h);
	}
	
	
	
	public final void generate() {
		generate(new Map[0]);
	}
	
	
	public final void generate(Map map) {
		Map[] sheath = new Map[1];
		sheath[0] = map;
		generate(sheath);
	}
	
	
	public final void generate(Map[] maps) {
		for (Map map: maps)
			map.display(ColS.altitude);
		
		System.out.println("Forming Island...");
		formIsland();
		for (Map map: maps)
			map.display(ColS.altitude);
		
		System.out.println("Eroding...");
		rainAndErode();
		for (Map map: maps)
			map.display(ColS.water);
		
		System.out.println("Growing Plants...");
		generateClimate();
		selectBiomes();
		for (Map map: maps)
			map.display(ColS.biome);
		
		System.out.println("Done!");
	}



	private void formIsland() {
		// TODO Auto-generated method stub
		
	}



	private void rainAndErode() {
		// TODO Auto-generated method stub
		
	}



	private void generateClimate() {
		// TODO Auto-generated method stub
		
	}



	private void selectBiomes() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public Surface getSurface() {
		return sfc;
	}
}
