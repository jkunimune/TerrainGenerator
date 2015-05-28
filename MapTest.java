public final class MapTest{ // a class to generate and display terrain onto a spherical surface
  static long startTime = 0; // the time it was when we last checked
  static java.applet.AudioClip music; // the song that plays
  
  
  
  public static final void main(String args[]) {
    startMusic();
    
    while (true) {
      Planet earth = new Planet(100);
      Map[] maps = new Map[3];
      maps[0] = new Hemispherical(earth, 400, 200);
      maps[1] = new Bipolar(earth, 400, 200);
      maps[2] = new Stereographic(earth, 400, 200);
      
      for (Map theMap: maps)
        theMap.display(ColS.altitude);
      
      earth.generate(maps);
      
      System.out.println("end");
      delay(20000);
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