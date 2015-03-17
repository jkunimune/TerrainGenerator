import java.awt.*;
import java.awt.image.*;
import javax.swing.*;



public class Map extends JPanel { // a class to manage the graphic elements of terrain generation
  final Color[] colors = {new Color(255,63,0), new Color(0,0,200), new Color(200,200,255), new Color(20, 70, 200), new Color(0,0,150),
    new Color(255,255,255), new Color(0,255,0), new Color(200,255,25), new Color(0,150,25), new Color(200,100,50), new Color(200,100,255),
    new Color(0,25,255), new Color(0,0,0)}; // colors of the biomes
  final boolean[][][] key = { // contains information for how to draw different biomes
    {
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
  public int[][] lats; // remembers which tile goes to which pixels (remembr to initialize in subclass constructor)
  public int[][] lons; // if a lat is -1, the lon is the rgb value of the color to put there
  public Globe glb;
  
  
  
  public Map(Globe newWorld, int w, int h) {
    glb = newWorld;
    lats = new int[h][w];
    lons = new int[h][w];
    img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
    setPreferredSize(new Dimension(w, h));
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
  
  
  public void display(String colorScheme) { // displays the map
    for (int x = 0; x < width(); x ++)
      for (int y = 0; y < height(); y ++)
        drawPx(x, y, getColorBy(colorScheme, x, y));
    show();
  }
  
  
  public Color getColorBy(String type, int x, int y) { // gets the color at a point on the screen
    if (lats[y][x] < 0) // if it is a color, not a tile
      return new Color(lons[y][x]>>16, lons[y][x]>>8, lons[y][x]); // return a color with the rgb value found
    
    if (type.equals("biome")) // otherwise calculate the color from that tile
      return getColorByBiome(x, y);
    else if (type.equals("altitude"))
      return getColorByAlt(x, y);
    else if (type.equals("rainfall"))
      return getColorByRain(x, y);
    else if (type.equals("temperature"))
      return getColorByTemp(x, y);
    else if (type.equals("climate"))
      return getColorByClimate(x, y);
    else if (type.equals("water"))
      return getColorByWater(x, y);
    else
      return new Color(255, 0, 150);
  }
  
  
  public Color getColorByBiome(int x, int y) {
    return colors[glb.getTileByIndex(lats[y][x], lons[y][x]).biome];
  }
  
  
  public Color getColorByAlt(int x, int y) {
    int alt = glb.getTileByIndex(lats[y][x], lons[y][x]).altitude;
    if (alt < -256) // if altitude is below minimum, return black
      return Color.black;
    else if (alt >= 256) // if altitude is above maximum, return white
      return Color.white;
    else if (alt < 0) // if altitude is below sea level, return a blue that gets darker as one goes deeper
      return new Color(0, 0, 256+alt);
    else // if altitude is above sea level, return a green that gets brighter as one goes higher
      return new Color(alt, 255, alt);
  }
  
  
  public Color getColorByRain(int x, int y) {
    int dryness = 255 - glb.getTileByIndex(lats[y][x], lons[y][x]).rainfall;
    if (dryness < 0)
      return new Color(0, 0, 255);
    if (dryness >= 256)
      return new Color(255, 255, 255);
    return new Color(dryness, dryness, 255); // return a blue that gets darker with rainfall
  }
  
  
  public Color getColorByTemp(int x, int y) {
    int coldness = 255 - glb.getTileByIndex(lats[y][x], lons[y][x]).temperature;
    if (coldness >= 256)
      return new Color(255, 0, 0);
    if (coldness < 0)
      return new Color(255, 255, 255);
    return new Color(255, coldness, coldness); // return a red that gets darker with temperature
  }
  
  
  public Color getColorByClimate(int x, int y) {
    int temp = 255-glb.getTileByIndex(lats[y][x], lons[y][x]).temperature;
    int rain = 255-glb.getTileByIndex(lats[y][x], lons[y][x]).rainfall;
    if (temp < 0)
      return new Color(255, 0, 0);
    if (temp >= 256)
      return new Color(255, 255, 255);
    if (rain < 0)
      return new Color(0, 0, 255);
    if (rain >= 256)
      return new Color(255, 255, 255);
    return new Color(rain, (temp+rain)/2, rain);
  }
  
  
  public Color getColorByWater(int x, int y) {
    if (glb.getTileByIndex(lats[y][x], lons[y][x]).altitude < 0)
      return Color.red;
    int dryness = 255 - glb.getTileByIndex(lats[y][x], lons[y][x]).water;
    if (dryness >= 256)
      return new Color(255, 255, 255);
    if (dryness < 0)
      return new Color(0, 0, 255);
    return new Color(dryness, dryness, 255); // return a blue that gets darker with temperature
  }
  
  
  public int width() {
    return lats[0].length;
  }
  
  
  public int height() {
    return lats.length;
  }
  
  
  @Override
  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }
  
  
  public void show() {
    repaint();
  }
}