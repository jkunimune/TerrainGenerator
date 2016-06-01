package mapprojections;

import mechanics.Map;
import mechanics.Surface;
import mechanics.Tile;

public class Basic extends Map {
	private static final long serialVersionUID = 1L;
	
	
	public Basic(Surface g, int w, int h) {
		super(g, w, h);
		finishSuper();
	}
	@Override
	public Tile getCoords(int x, int y) {
		return sfc.getTile(y*Math.PI/height(), x*2*Math.PI/width());
	}

}