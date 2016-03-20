// by Justin Kunimune



public final class CiviHD { // the driver for my independent project
  static java.applet.AudioClip intro; // songs for generation and CiviHD
  static java.applet.AudioClip music;
  
  static long startTime = 0; // the time it was when we last checked
  static final int invFrameRate = 2; // the number of simulations to run before showing a frame
  static final int waitTime = 5; // the amount of time each simulation takes in ms
  
  
  
  public static final void main(String args[]) {
    loadSound();
    
    Planet protoEarth = new Planet(100);
    Map topographical = new PierceQuincuncial(protoEarth.getSurface(), 500, 500);
    
    intro.play();
    protoEarth.generate(topographical);
    delay(3000);
    
    World earth = new World(protoEarth.getSurface());
    Map political = new Mollweide(earth.getSurface(), 700, 350);
    
    music.loop();
    
    while (true) {
      setTimer(0);
      for (int i = 0; i < invFrameRate; i ++)
        earth.update();
      political.display(ColS.drawn);
      waitFor(invFrameRate*waitTime);
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