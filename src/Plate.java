import java.util.ArrayList;



public class Plate extends ArrayList<Tile> { // a tectonic plate
  private static final long serialVersionUID = 1L;
  
  public Vector w; // the angular velocity of the plate
  public Vector a; // the angular acceleration
  
  
  
  public Plate(int i, Tile til, double speed) {
    w = Vector.random(speed);
    a = Vector.random(speed).minus(w);
    til.temp2 = i;
    
    til.temp1 = (int)(Math.random()*256-128); // randomizes alt from -128 to 127
    til.temp3 = (int)(Math.random()*8); // randomizes spread speed
  }
  
  
  
  public void changeCourse(double delT) { // r is the amount of randomness
    w = w.plus(a.times(delT));
  }
}
    