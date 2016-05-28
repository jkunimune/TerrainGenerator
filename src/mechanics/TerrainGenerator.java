package mechanics;

import mapprojections.*;



/* Generate ultra-realistic spherical terrain */
/* by Justin Kunimune*/
public final class TerrainGenerator{ // a class to generate and display terrain onto a spherical surface
  static long startTime = 0; // the time it was when we last checked
  static java.applet.AudioClip music; // the song that plays
  
  
  
  public static final void main(String args[]) {
    startMusic();
    
    while (true) {
      AdvancedPlanet earth = new AdvancedPlanet(100);
      Map theMap = new Mollweide(earth.getSurface(), 800, 400);
      
      theMap.display(ColS.altitude);
      
      earth.generate(theMap);
      
      //System.out.println(Arrays.deepToString(createHistogram(earth.getSurface())));
      
      //theMap.exhibit(ColS.biome, 30000);
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
  
  
  public static final void delay(int mSec) { // waits a number of milliseconds
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() < start+mSec) {}
  }
  
  
  public static final void setTimer(long mSec) { // sets the timer to a certain time
    startTime = System.currentTimeMillis() - mSec;
  }
  
  
  public static final void waitFor(long mSec) { // waits for the timer to reach a certain time
    while (System.currentTimeMillis() - startTime < mSec) {}
  }
  
  
  public static final int[][] createHistogram(Surface sfc) { // makes a histogram of rainfall and temperature levels
    int[][] output = new int[256][];
    for (int y = 0; y < output.length; y ++) {
      output[y] = new int[256];
      for (int x = 0; x < output[y].length; x ++)
        output[y][x] = 0;
    }
    
    for (Tile til: sfc.list()) {
      if (til.altitude < 0)
        continue;
      int r = til.rainfall;
      int t = til.temperature;
      if (r >= 256)
        r = 255;
      if (t < 0)
        t = 0;
      output[r][t] ++;
    }
    return output;
  }
}