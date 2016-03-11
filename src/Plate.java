import java.util.ArrayList;



public class Plate extends ArrayList<Tile> { // a tectonic plate
  private static final long serialVersionUID = 1L;
  
  public Vector w; // the angular velocity of the plate
  public Vector a; // the angular acceleration
  
  
  
  public Plate(int i, Tile til) {
    w = new Vector(Math.random(), Math.acos(Math.random()*2-1) + Math.PI/2, Math.random()*2*Math.PI);
    a = new Vector(Math.random(), Math.acos(Math.random()*2-1) + Math.PI/2, Math.random()*2*Math.PI);
    til.temp2 = i;
    
    til.temp1 = (int)(Math.random()*256-128); // randomizes alt from -128 to 127
    til.temp3 = (int)(Math.random()*8); // randomizes spread speed
  }
  
  
  
  public void changeCourse(double delT, double r) { // r is the amount of randomness
    w = w.plus(a.times(delT));
    a = a.plus(new Vector(Math.random(), Math.asin(Math.random()*2-1) + Math.PI/2, Math.random()*2*Math.PI).times(r)).times(1/(1+r));
  }
}
    