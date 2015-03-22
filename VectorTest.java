public final class VectorTest {
  public final static void main(String[] args) {
    Vector u = new Vector(1, Math.PI/2, 7*Math.PI/4);
    Vector v = new Vector(1, Math.PI/2, Math.PI/4);
    System.out.println("u = "+u+" = "+u.toStringPolar());
    System.out.println("v = "+v+" = "+v.toStringPolar());
    
    Vector sum = u.plus(v);
    Vector del = u.minus(v);
    double ang = u.angleTo(v);
    double dot = u.dot(v);
    Vector xxx = u.cross(v);
    System.out.println("u + v = "+sum);
    System.out.println("u - v = "+del);
    System.out.println("(-) = "+ang);
    System.out.println("u ¥ v = "+dot);
    System.out.println("u x v = "+xxx);
  }
}