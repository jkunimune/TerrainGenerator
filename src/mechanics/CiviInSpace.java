package mechanics;

import mapprojections.*;



/* Generate a galaxy and simulate civilizations on it */
/* by Justin Kunimune*/
public final class CiviInSpace { // the driver for the interstellar version of Civi HD (will not compile until all instances of "Globe" in World are replaced with "Disc"
  static java.applet.AudioClip intro; // songs for generation and CiviHD
  static java.applet.AudioClip music;
  
  static long startTime = 0; // the time it was when we last checked
  
  
  
  public static final void main(String args[]) {
    loadSound();
    
    Galaxy protoEarth = new Galaxy(200);
    Map topographical = new Polar(protoEarth.getSurface(), 500, 500);
    
    intro.play();
    protoEarth.generate(topographical);
    delay(3000);
    
    World earth = new World(protoEarth.getSurface());
    Map political = new Polar(earth.getSurface(), 500, 500);
    
    music.loop();
    
    while (true) {
      setTimer(0);
      earth.update();
      political.display(ColS.drawn);
      waitFor(10);
    }
    
    /*World world = new World();
    while (true) {
      world.spawnCivi(new Tile(0,0));
    }*/
  }
  
  
  
  public static final void loadSound() { // loads all sound files
    try {
      intro = java.applet.Applet.newAudioClip(new java.net.URL("file:Sound/babaYetu.wav"));
      music = java.applet.Applet.newAudioClip(new java.net.URL("file:Sound/terraNova.wav"));
    }
    catch (java.net.MalformedURLException error) {
      System.out.println(error);
    }
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