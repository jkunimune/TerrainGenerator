public class Vector {
  private double x; // magnitude of the x component
  private double y; // magnitude of the y component
  private double z; // magnitude of the z component
  
  
  
  public Vector(double x, double y, double z) { // constructs a new vector given horizontal, vertical, and depthual lengths
  }
  
  
  public Vector(int r, double alpha, double beta) { // constructs a new vector given magnitude, altitude, and bearing
  }
  
  
  
  public double getX() { // magnitude of the x component
  }
  
  
  public double getY() { // magnitude of the y component
  }
  
  
  public double getX() { // magnitude of the z component
  }
  
  
  public int getR() { // magnitude
  }
  
  
  public double getA() { // altitude
  }
  
  
  public double getB() { // bearing
  }
  
  
  public Vector plus(Vector that) { // computes sum with that
  }
  
  
  public Vector minus(Vector that) { // computes difference with that
  }
  
  
  public Vector times(double c) { // computes product with c
  }
  
  
  public double dot(Vector that) { // computes dot product with that
  }
  
  
  public Vector cross(Vector that) { // computes cross product with that
  }
  
  
  public double to(Vector that) { // computes angle to that
  }
  
  
  public void plusEquals(Vector that) { // adds that
  }
  
  
  public void minusEquals(Vector that) { // subtracts that
  }
  
  
  public void timesEquals(double c) { // multiplies by c
  }
  
  
  public void crossEquals(Vector that) { // becomes cross product with that
  }
}