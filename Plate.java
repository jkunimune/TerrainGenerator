import java.util.ArrayList;



public class Plate extends ArrayList<Tile> { // a tectonic plate
  public Vector w; // the angular velocity of the plate
  
  
  
  public Plate(int i, Tile til) {
    w = new Vector(Math.random(), Math.asin(Math.random()*2-1) + Math.PI/2, Math.random()*2*Math.PI);
    til.temp2 = i;
    
    til.temp1 = (int)(Math.random()*128+191); // randomizes alt from 128 to 128*3
    til.temp3 = (int)(Math.random()*8); // randomizes spread speed
  }
}
    