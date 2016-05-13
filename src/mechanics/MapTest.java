package mechanics;

import mapprojections.*;



/* Compare and contrast different map projections using random Terrain Generation */
/* by Justin Kunimune*/
public final class MapTest{ // a class to experiment with and compare different map projections
  static long startTime = 0; // the time it was when we last checked
  static java.applet.AudioClip music; // the song that plays
  
  
  
  public static final void main(String args[]) {
    startMusic();
    
    while (true) {
      Planet earth = new Planet(100);
      Map[] maps = new Map[4];
      maps[0] = new Equirectangular(earth.getSurface(), 600, 300);
      maps[1] = new PierceQuincuncial(earth.getSurface(), 400, 400);
      maps[2] = new Mollweide(earth.getSurface(), 600, 300);
      maps[3] = new Hemispherical(earth.getSurface(), 600, 300);
      
      for (Map theMap: maps)
        theMap.display(ColS.altitude);
      
      earth.generate(maps);
      
      System.out.println("end");
      delay(30000);
    }
  }
  
  
  public static final void startMusic() {
    try {
      music = java.applet.Applet.newAudioClip(new java.net.URL("file:Sound/terraNova.wav"));
    }
    catch (java.net.MalformedURLException error) {
      System.out.println(error);
    }
    music.loop();
  }
  
  
  public static final void delay(int mSec) { // waits a number of miliseconds
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() < start+mSec) {}
  }
  
  
  public static final void setTimer(long mSec) { // sets the timer to a certain time
    startTime = System.currentTimeMillis() - mSec;
  }
  
  
  public static final void waitFor(long mSec) { // waits for the timer to reach a certain time
    while (System.currentTimeMillis() - startTime < mSec) {}
  }
}