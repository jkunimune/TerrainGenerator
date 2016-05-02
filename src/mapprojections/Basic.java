package mapprojections;

import java.awt.Point;

import mechanics.Map;
import mechanics.Surface;

public class Basic extends Map {
	private static final long serialVersionUID = 1L;
	
	
	public Basic(Surface g, int w, int h) {
		super(g, w, h);
		finishSuper();
	}
	@Override
	public Point getCoords(int x, int y) {
		return sfc.tilByAngles(y*Math.PI/height(), x*2*Math.PI/width());
	}

}