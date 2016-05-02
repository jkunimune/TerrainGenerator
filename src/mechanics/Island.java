package mechanics;

public class Island {
	private FinitePlane sfc;
	
	
	
	public Island(int s) {
		sfc = new FinitePlane(s,s);
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
			map.display(ColS.altitude);
		
		System.out.println("Done!");
	}



	private void formIsland() {	// forms the first iteration of the island
		for (int x = 0; x < sfc.getWidth(); x += sfc.getWidth()/2)
			for (int y = 0; y < sfc.getHeight(); y += sfc.getHeight()/2) {
				double alt = 255 - 256*Math.sqrt(2)*Math.hypot(2.0*x/sfc.getWidth()-1, 2.0*y/sfc.getHeight()-1);
				sfc.getTileByIndex(x, y).altitude = (int)alt;	// sets some initial values
			}
		
		for (int d = sfc.getWidth()/2; d >= 1; d /= 2) {	// generates diamond-square noise
			for (int x = d; x < sfc.getWidth(); x += 2*d) {	// diamond step
				for (int y = d; y < sfc.getHeight(); y += 2*d) {
					if (sfc.getTileByIndex(x,y).altitude < -256) {	// only set tiles that do not already have a value
						int sum = 0;	// the sum of the altitudes
						for (int dx = -d; dx <= d; dx += 2*d)
							for (int dy = -d; dy <= d; dy += 2*d)
								sum += sfc.getTileByIndex(x+dx, y+dy).altitude;
						int avg = sum/4;
						int sqs = 0;	// the sum of the squares of the altitudes
						for (int dx = -d; dx <= d; dx += 2*d)
							for (int dy = -d; dy <= d; dy += 2*d)
								sqs += Math.pow(sfc.getTileByIndex(x+dx, y+dy).altitude-avg, 2);
						double dev = Math.sqrt(sqs/4);
						sfc.getTileByIndex(x,y).altitude = random(avg, dev);
					}
				}
			}
			
			for (int x = 0; x < sfc.getWidth(); x += d) { // square step
				for (int y = 0; y < sfc.getHeight(); y += d) {
					if (sfc.getTileByIndex(x,y).altitude < -256) {
						int sum = 0;
						int num = 0;
						for (double tht = 0; tht < 6; tht += Math.PI/2) {
							try {
								sum += sfc.getTileByIndex(x+d*(int)Math.cos(tht),y+d*(int)Math.sin(tht)).altitude;
								num ++;
							} catch (IndexOutOfBoundsException e) {
								continue;
							}
						}
						int avg = sum/num;
						int sqs = 0;
						for (double tht = 0; tht < 6; tht += Math.PI/2) {
							try {
								sqs += Math.pow(sfc.getTileByIndex(x+d*(int)Math.cos(tht),y+d*(int)Math.sin(tht)).altitude-avg, 2);
							} catch (IndexOutOfBoundsException e) {
								continue;
							}
						}
						double dev = Math.sqrt(sqs/num);
						sfc.getTileByIndex(x,y).altitude = random(avg, dev);
					}
				}
			}
		}
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
	
	
	
	public static final int random(double mu, double sigma) { // generates a random number given mean and standard deviation
		return (int)(mu + sigma/4*Math.log(1/Math.random()-1));
	}
}
