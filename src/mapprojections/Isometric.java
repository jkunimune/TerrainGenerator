package mapprojections;

import java.awt.Color;
import mechanics.ColS;
import mechanics.Map;
import mechanics.Surface;
import mechanics.Tile;

public class Isometric extends Map {	// a class that makes the terrain appear 3D
	private static final long serialVersionUID = 1L;
	
	private double vertScale;
	
	
	public Isometric(Surface newWorld, int w, int h) {
		super(newWorld, w, h);
		finishSuper();
		vertScale = (double)height()/sfc.list()[sfc.list().length-1].lat;
	}

	@Override
	public Tile getCoords(int x, int y) {
		return sfc.getTile(0, x*2*Math.PI/width());
	}
	
	
	@Override
	public Color getColorBy(ColS c, int x, int y) {
		double angle = Math.PI/4;
		for (int lat = 0; true; lat ++) {
			try {
				Tile til = sfc.getTileByIndex(lat, lons[y][x]);
				int altitude = Math.max(til.altitude, 0);
				if (altitude*Math.sin(angle)/8 + til.lat*Math.cos(angle) >= (height()-y)/vertScale) {
					lats[y][x] = lat;
					return super.getColorBy(c, x, y);
				}
			} catch (IndexOutOfBoundsException e) {
				return new Color(200,200,255);
			}
		}
	}
}
