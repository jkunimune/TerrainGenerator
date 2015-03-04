import java.awt.*;
import java.awt.image.*;
import javax.swing.*;



public class Map extends JPanel { // a class to manage the graphic elements of terrain generation
  final Color[] colors = {new Color(255,70,0), new Color(0,0,200), new Color(200,255,255), new Color(255,255,255), new Color(0,255,0),
    new Color(255,200,25), new Color(0,150,25), new Color(200,100,50), new Color(200,100,255), new Color(0,25,255), new Color(0,0,0),
    new Color(255, 255, 200)}; // colors of the different biomes
  final boolean[][][] key = {{ // contains information for how to draw different biomes
    {false, false, false, false, false},
    {false,  true, false,  true, false},
    {false,  true, false,  true, false},
    {false, false,  true, false, false},
    {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, true, false, true, false},
      {false, true, false, true, false},
      {false, false, true, false, false},
      {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},},
    {
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},
      {false, false, false, false, false},},};
      
  private BufferedImage img;
  public int width;
  public int height;
  public Globe glb;
  
  
  
  public Map(Globe newWorld, int w, int h) {
    glb = newWorld;
    width = w;
    height = h;
    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
    setPreferredSize(new Dimension(width, height));
    JFrame frame = new JFrame();
    frame.add(this);
    frame.pack();
    frame.setVisible(true);
  }
  
  
  public void drawPx(int x, int y, Color z) { // draws a pixel to the buffered image
    Graphics g = img.getGraphics();
    g.setColor(z);
    g.drawLine(x, y, x, y);
    g.dispose();
    //repaint();
  }
  
  
  public void display(String colorScheme) {
    System.out.println("WARNING: Non-functional Map object.\nPlease construct a subclass of Map.");
  }
  
  
  public Color getColorBy(String type, Tile t) {
    if (type.equals("biome"))
      return getColorByBiome(t);
    else if (type.equals("altitude"))
      return getColorByAlt(t);
    else if (type.equals("rainfall"))
      return getColorByRain(t);
    else if (type.equals("temperature"))
      return getColorByTemp(t);
    else
      return Color.black;
  }
  
  
  public Color getColorByBiome(Tile t) {
    return colors[t.biome];
  }
  
  
  public Color getColorByAlt(Tile t) {
    if (t.altitude < -256) // if altitude is below minimum, return the color of lava
      return colors[0];
    else if (t.altitude >= 256) // if altitude is above maximum, return the color of space
      return colors[10];
    else if (t.altitude < 0) // if altitude is below sea level, return a blue that gets darker as one goes deeper
      return new Color(0, 0, 256+t.altitude);
    else // if altitude is above sea level, return a green that gets brighter as one goes higher
      return new Color(t.altitude, 255, t.altitude);
  }
  
  
  public Color getColorByRain(Tile t) {
    return new Color(255-t.rainfall, 255-t.rainfall, 255);
  }
  
  
  public Color getColorByTemp(Tile t) {
    return new Color(255, 255-t.temperature, 255-t.temperature);
  }
  
  
  @Override
  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }
  
  
  public void show() {
    repaint();
  }
}