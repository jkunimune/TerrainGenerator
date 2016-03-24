import ellipticFunctions.Jacobi;
import mfc.field.Complex;

public class PierceQuincuncial extends Map { // a super-awesome conformal projection
  private static final long serialVersionUID = 1L;
	
	
  public PierceQuincuncial(Surface g, int w, int h) {
    super(g, w, h);
    finishSuper();
  }
  
  
  public final java.awt.Point getCoords(int x, int y) {
    if (((x>>1 == width()/12>>1 || x>>1 == width()*11/12>>1) && y/7%2 == 0) ||
    	((y>>1 == height()/12>>1 || y>>1 == height()*11/12>>1) && x/7%2 == 0))
      return new java.awt.Point(0, -1);
    
    Complex u = new Complex(1.2*(3.7116*x/width())-.37116, 1.2*(3.7116*y/height()-1.8558)); // don't ask me where 3.7116 come from because I have no idea
    Complex k = new Complex(Math.sqrt(0.5)); // the rest comes from some fancy complex calculus stuff
    Complex ans = Jacobi.cn(u, k);
    double p = 2*Math.atan(ans.abs());
    double lon1 = Math.atan2(ans.getRe(), ans.getIm());
    double lat1 = p-Math.PI/2;
    
    double latitude = Math.asin(Math.cos(lon1)*Math.cos(lat1)); // this bit makes it transverse
    double longitude = Math.acos(Math.sin(lat1)/Math.cos(latitude));
    if (Math.sin(lon1) < 0)
      longitude *= -1;
    
    return sfc.tilByAngles(Math.PI/2-latitude, longitude);
  }
}