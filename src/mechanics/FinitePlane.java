package mechanics;

import java.awt.Point;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

public class FinitePlane implements Surface {
	private Tile[][] matrix;
	private int width,height;
	private Tile meteorTarget;
	
	
	public FinitePlane(int w, int h) {
		matrix = new Tile[h][];
		for (int y = 0; y < h; y ++) {
			matrix[y] = new Tile[w];
			for (int x = 0; x < w; x ++)
				matrix[y][x] = new Tile(y,x);
		}
		width = w;
		height = h;
		meteorTarget = null;
	}


	
	public int getWidth() {
		return width;
	}
	
	
	public int getHeight() {
		return height;
	}
	
	@Override
	public ArrayList<Tile> adjacentTo(Tile t) {
		ArrayList<Tile> output = new ArrayList<Tile>(4);
		/*for (int dx = -1; dx <= 1; dx ++) {
			for (int dy = -1; dy <= 1; dy ++) {
				if (dx != 0 || dy != 0) {
					try {
						output.add(matrix[t.lat+dy][t.lon+dx]);
					} catch (IndexOutOfBoundsException e) {}
				}
			}
		}*/
		try {
			output.add(matrix[t.lat+1][t.lon]);
		} catch (IndexOutOfBoundsException e) {}
		try {
			output.add(matrix[t.lat-1][t.lon]);
		} catch (IndexOutOfBoundsException e) {}
		try {
			output.add(matrix[t.lat][t.lon+1]);
		} catch (IndexOutOfBoundsException e) {}
		try {
			output.add(matrix[t.lat][t.lon-1]);
		} catch (IndexOutOfBoundsException e) {}
		return output;
	}

	@Override
	public int count(int i) { // checks if any of a given altitude exists on the map
		int count = 0;
		for (Tile[] row: matrix)
			for (Tile til: row)
				if (til.altitude == i)
					count += 1;
		return count;
	}

	@Override
	public double distance(Tile t1, Tile t2) {
		return Math.hypot(t1.lat-t2.lat, t1.lon-t2.lon);
	}

	@Override
	public Tile getTile(double lat, double lon) {	// gives coordinates and gets a tile
		return matrix[(int)(lat*height/(Math.PI))][(int)(lon*width/(2*Math.PI))];
	}

	@Override
	public Tile getTileByIndex(int lat, int lon) {	// gives indices and gets a tile
		return matrix[lat][lon];
	}

	@Override
	public Point tilByAngles(double lat, double lon) {
		return new Point((int)(lon*width/(2*Math.PI)),(int)(lat*height/(Math.PI)));
	}

	@Override
	public double latByTil(Tile til) {
		return til.lat*Math.PI/height;
	}

	@Override
	public double lonByTil(Tile til) {
		return til.lon*2*Math.PI/width;
	}

	@Override
	public Tile[] list() {
		Tile[] output = new Tile[0];
		for (Tile[] row: matrix)
			output = ArrayUtils.addAll(output, row);
		return output;
	}

	@Override
	public Tile incomingMeteor() { // returns where to put a meteor, if any
		if (meteorTarget != null) {		// if there is an incoming meteor
			Tile temp = meteorTarget;		// return its target and reset meteorTarget
			meteorTarget = null;
			return temp;
		}
		else
			return null;	// otherwise, just ignore it.
	}

	@Override
	public void meteor(Tile til) {
		meteorTarget = til;
	}
}
