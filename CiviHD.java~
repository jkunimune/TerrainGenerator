// by Justin Kunimune



import java.util.*;




public final class CiviHD { // the driver for my final project
  static java.applet.AudioClip intro; // songs for generation and CiviHD
  static java.applet.AudioClip music;
  
  static long startTime = 0; // the time it was when we last checked
  
  
  
  public static final void main(String args[]) {
    loadSound();
    
    Planet protoEarth = new Planet(100);
    Map topographical = new Equirectangular(protoEarth, 600, 600);
    
    intro.play();
    protoEarth.generate(topographical);
    delay(3000);
    
    World earth = new World(protoEarth);
    Map political = new Equirectangular(earth, 600, 600);
    
    music.loop();
    
    while (true) {
      setTimer(0);
      earth.update();
      political.display(ColS.territory);
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