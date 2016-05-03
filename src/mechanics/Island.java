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
		
		System.out.println("Growing Plants...");
		generateClimate();
		adjustClimate();
		for (Map map: maps)
			map.display(ColS.climate);
		
		System.out.println("Eroding...");
		rainAndErode();
		for (Map map: maps)
			map.display(ColS.light);
		
		System.out.println("Done!");
	}



	private void formIsland() {	// forms the first iteration of the island
		for (int x = 0; x < sfc.getWidth(); x += sfc.getWidth()-1)
			for (int y = 0; y < sfc.getHeight(); y += sfc.getHeight()-1) {
				sfc.getTileByIndex(x, y).altitude = -256;	// sets some initial values
			}
		sfc.getTileByIndex(sfc.getWidth()/2, sfc.getHeight()/2).altitude = 255;
		
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
						for (double tht = 0; tht < 6; tht += Math.PI/2) {
							try {
								sum += sfc.getTileByIndex(x+d*(int)Math.cos(tht),y+d*(int)Math.sin(tht)).altitude;
							} catch (IndexOutOfBoundsException e) {
								sum -= 256;
							}
						}
						int avg = sum/4;
						int sqs = 0;
						for (double tht = 0; tht < 6; tht += Math.PI/2) {
							try {
								sqs += Math.pow(sfc.getTileByIndex(x+d*(int)Math.cos(tht),y+d*(int)Math.sin(tht)).altitude-avg, 2);
							} catch (IndexOutOfBoundsException e) {
								sqs += Math.pow(-256-avg, 2);
							}
						}
						double dev = Math.sqrt(sqs/4);
						sfc.getTileByIndex(x,y).altitude = random(avg, dev);
					}
				}
			}
		}
	}


	private void rainAndErode() {
		// TODO Auto-generated method stub
		
	}



	private void generateClimate() {	// picks rainfalls and temepratures for each tile
		for (Tile til: sfc.list()) {
			til.rainfall = 0;
			til.temperature = 0;
		}
		
		for (int gridSize = 32; gridSize > 1; gridSize >>= 1) {
			double[][][][] nodes = buildPerlinArrays(gridSize);
			for (int x = 0; x < sfc.getWidth(); x ++) {	// now calculate the values
				for (int y = 0; y < sfc.getHeight(); y ++) {
					sfc.getTileByIndex(y, x).rainfall += (int)(2*gridSize*calcPerlin(x,y,gridSize,nodes[0]));
					sfc.getTileByIndex(y, x).temperature += (int)(2*gridSize*calcPerlin(x,y,gridSize,nodes[1]));
				}	// these values range from 0 to 127
			}
		}
	}
	
	
	public void adjustClimate() {
		for (Tile til: sfc.list())	// high altitudes are colder
			if (til.altitude >= 0)
				til.temperature = 128 + til.temperature - til.altitude/2;
		
		for (int y = 0; y < sfc.getHeight(); y ++) {	// the orographic effect
			int moisture = 2048;
			for (int x = 0; x < sfc.getWidth(); x ++) {
				Tile til = sfc.getTileByIndex(y, x);
				if (til.altitude >= 0) {
					int rain = (int)((til.altitude+64)*(1-Math.exp(-moisture/256.0)));
					moisture -= rain;
					til.rainfall += rain;
				}
				til.rainfall = (int)(255 - (255-til.rainfall)*0.4);	// normalizes for the tropical climate
			}
		}
	}
	
	
	
	public Surface getSurface() {
		return sfc;
	}
	
	
	
	public static double calcPerlin(int x, int y, int gridSize, double[][][] nodes) {	// does the perlin thing
		int ny0 = y/gridSize;	// chooses nodes
		int nx0 = x/gridSize;
		double dy = (double)y/gridSize - ny0;
		double dx = (double)x/gridSize - nx0;
		int ny1, nx1;
		if (dy==0)	ny1 = ny0;
		else		ny1 = ny0+1;
		if (dx==0)	nx1 = nx0;
		else		nx1 = nx0+1;
		
		final double dtl = (dx-0)*nodes[ny0][nx0][0] + (dy-0)*nodes[ny0][nx0][1];	// computes dot products
		final double dtr = (dx-1)*nodes[ny0][nx1][0] + (dy-0)*nodes[ny0][nx1][1];
		final double dbl = (dx-0)*nodes[ny1][nx0][0] + (dy-1)*nodes[ny1][nx0][1];
		final double dbr = (dx-1)*nodes[ny1][nx1][0] + (dy-1)*nodes[ny1][nx1][1];
		
		final double wy = Math.pow(Math.sin(dy*Math.PI/2), 2);	// determines weights
		final double wx = Math.pow(Math.sin(dx*Math.PI/2), 2);
		
		return 0.5 + dtl*(1-wx)*(1-wy) + dtr*wx*(1-wy) + dbl*(1-wx)*wy + dbr*wx*wy;	// interpolates
	}
	
	
public double[][][][] buildPerlinArrays(int gridSize) {
	double[][][][] nodes = new double[2][][][];	// the base climate is made from perlin noise
	
	for (int i = 0; i < nodes.length; i ++) {	// the first layer is rainfall/temperature
		nodes[i] = new double[sfc.getHeight()/gridSize+1][][];
		for (int j = 0; j < nodes[i].length; j ++) {	// the second layer is y
			nodes[i][j] = new double[sfc.getWidth()/gridSize+1][];
			for (int k = 0; k < nodes[i][j].length; k ++) {	// the third layer is x
				nodes[i][j][k] = new double[2];
				double theta = Math.random()*2*Math.PI;	// the final layer is the x and y components of the vector
				nodes[i][j][k][0] = Math.cos(theta);
				nodes[i][j][k][1] = Math.sin(theta);
			}
		}
	}
	return nodes;
}
	
	
	
	public static final int random(double mu, double sigma) { // generates a random number given mean and standard deviation
		return (int)(mu + sigma/4*Math.log(1/Math.random()-1));
	}
}
