import java.util.*;
import java.awt.*;


public interface Surface {
  public abstract ArrayList<Tile> adjacentTo(Tile t);
  
  public abstract int count(int i);
  
  public abstract double distance(Tile t1, Tile t2);
  
  public abstract Tile getTile(double lat, double lon);
  
  public abstract Tile getTileByIndex(int lat, int lon);
  
  public abstract Point tilByAngles(double lat, double lon);
  
  public abstract double latByTil(Tile til);
  
  public abstract double lonByTil(Tile til);
  
  public abstract Tile[] list();
  
  public abstract Tile incomingMeteor();
  
  public abstract void meteor(Tile til);
} 