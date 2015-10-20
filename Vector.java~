public final class Vector {
  private double r; // magnitude
  private double a; // altitude
  private double b; // bearing
  
  
  
  public Vector(double newX, double newY, double newZ, boolean cartesian) { // constructs a new vector given horizontal, vertical, and depthual lengths
    r = Math.sqrt(newX*newX + newY*newY + newZ*newZ);
    a = Math.acos(newY/r);
    if (newX != 0) { // accounts for various cases for beta
      if (newX > 0)  b = Math.atan(newZ/newX);
      else           b = Math.atan(newZ/newX)+Math.PI;
    }
    else {
      if (newZ > 0)  b = Math.PI/2;
      else           b = 3*Math.PI/2;
    }
  }
  
  
  public Vector(double newR, double newAlpha, double newBeta) { // constructs a new vector given magnitude, altitude, and bearing
    r = newR;
    a = newAlpha;
    b = newBeta;
  }
  
  
  
  public final void setR(double newR) {
    r = newR;
  }
  
  
  public final void setA(double newAlpha) {
    a = newAlpha;
  }
  
  
  public final void setB(double newBeta) {
    b = newBeta;
  }
  
  
  public final double getX() { // magnitude of the x component
    //System.out.println("Calculating X: r is "+r+", alpha is "+a+", and beta is "+b);
    return r*Math.sin(a)*Math.cos(b);
  }
  
  
  public final double getY() { // magnitude of the y component
    return -r*Math.cos(a);
  }
  
  
  public final double getZ() { // magnitude of the z component
    return r*Math.sin(a)*Math.sin(b);
  }
  
  
  public final double getR() { // magnitude
    return r;
  }
  
  
  public final double getA() { // altitude
    return a;
  }
  
  
  public final double getB() { // bearing
    return b;
  }
  
  
  public final Vector negative() { // computes the opposite
    return new Vector(r, Math.PI-a, (Math.PI+b)%Math.PI);
  }
  
  
  public final Vector plus(Vector that) { // computes sum with that
    return new Vector(this.getX()+that.getX(), this.getY()+that.getY(), this.getZ()+that.getZ(), true);
  }
  
  
  public final Vector minus(Vector that) { // computes difference with that
    return new Vector(this.getX()-that.getX(), this.getY()-that.getY(), this.getZ()-that.getZ(), true);
  }
  
  
  public final Vector times(double c) { // computes product with c
    return new Vector(c*r, a, b);
  }
  
  
  public final double dot(Vector that) { // computes dot product with that
    return this.getX()*that.getX() + this.getY()*that.getY() + this.getZ()*that.getZ();
  }
  
  
  public final Vector cross(Vector that) { // computes cross product with that
    return new Vector(this.getY()*that.getZ() - this.getZ()*that.getY(),
                      this.getZ()*that.getX() - this.getX()*that.getZ(),
                      this.getX()*that.getY() - this.getY()*that.getX(), true);
  }
  
  
  public final double angleTo(Vector that) { // computes angle to that
    return Math.acos(1 - (
                          Math.pow(Math.sin(this.getA())*Math.cos(this.getB()) - Math.sin(that.getA())*Math.cos(that.getB()) ,2) + 
                          Math.pow(Math.cos(this.getA()) - Math.cos(that.getA()) ,2) + 
                          Math.pow(Math.sin(this.getA())*Math.sin(this.getB()) - Math.sin(that.getA())*Math.sin(that.getB()) ,2)
                         )/2);
  }
  
  
  public final void negate() { // negates
    a = Math.PI-a;
    b = (Math.PI+b)%Math.PI;
  }
  
  
  public final void plusEquals(Vector that) { // adds that
    Vector sum = this.plus(that);
    r = sum.getR();
    a = sum.getA();
    b = sum.getB();
  }
  
  
  public final void minusEquals(Vector that) { // subtracts that
    Vector dif = this.minus(that);
    r = dif.getR();
    a = dif.getA();
    b = dif.getB();
  }
  
  
  public final void timesEquals(double c) { // multiplies by c
    r *= c;
  }
  
  
  public final void crossEquals(Vector that) { // becomes cross product with that
    Vector txt = this.cross(that);
    r = txt.getR();
    a = txt.getA();
    b = txt.getB();
  }
  
  
  public final String toString() {
    return "<"+getX()+", "+getY()+", "+getZ()+">";
  }
  
  
  public final String toStringPolar() {
    return "("+getR()+", "+getA()+", "+getB()+")";
  }
}