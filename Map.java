import java.awt.*;
import java.awt.image.*;
import javax.swing.*;



public class Map extends JPanel { // a class to manage the graphic elements of terrain generation
  public static final int biome = 0; // colorscheme codes
  public static final int altitude = 1;
  public static final int temperature = 2;
  public static final int rainfall = 3;
  public static final int climate = 4;
  public static final int water = 5;
  public static final int waterLevel = 6;
  public static final int territory = 7;
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
  
  
  
  public final void drawPx(int x, int y, Color z) { // draws a pixel to the buffered image
    Graphics g = img.getGraphics();
    g.setColor(z);
    g.drawLine(x, y, x, y);
    g.dispose();
    //repaint();
  }
  
  
  public void display(int colorScheme) { // displays the map
    for (int x = 0; x < width(); x ++)
      for (int y = 0; y < height(); y ++)
        drawPx(x, y, getColorBy(colorScheme, x, y));
    show();
  }
  
  
  public Color getColorBy(int type, int x, int y) { // gets the color at a point on the screen
    if (lats[y][x] < 0) // if it is a color, not a tile
      return new Color(lons[y][x]>>16, lons[y][x]%65536>>8, lons[y][x]%256); // return a color with the rgb value found
    
    switch (type) {
      case biome: // otherwise calculate the color from that tile
        return getColorByBiome(x, y);
      case altitude:
        return getColorByAlt(x, y);
      case rainfall:
        return getColorByRain(x, y);
      case temperature:
        return getColorByTemp(x, y);
      case climate:
        return getColorByClimate(x, y);
      case water:
        return getColorByWater(x, y);
      case waterLevel:
        return getColorByWaterLevel(x, y);
      case territory:
        return getColorByTerritory(x, y);
      default:
        return new Color(255, 0, 150);
    }
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
    else if (alt < 0) // if altitude is deep, return a blue that gets blacker as oneg oes deeper
      return new Color(0, 0, alt+256);
    else if (alt < 128) // if altitude is above sea level, return a green that gets brighter as one goes higher
      return new Color(0, alt+128, 0);
    else // if altitude is high, return a green that gets whiter as one goes higher
      return new Color(alt*2-256, 255, alt*2-256);
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
  
  
  public Color getColorByWaterLevel(int x, int y) {
    int height = glb.getTileByIndex(lats[y][x], lons[y][x]).waterLevel();
    if (height >= 256)
      return Color.white;
    if (height < 0)
      return Color.black;
    return new Color(height, height, height); // return a grey that gets brighter with temperature
  }
  
  
  public Color getColorByTerritory(int x, int y) {
    Tile til = glb.getTileByIndex(lats[y][x], lons[y][x]);
//    if (glb.randChance(-100))
//      System.out.println("I see a development of "+til.development+" at "+x+", "+y);
    switch (til.development) {
      case 0: // unclaimed
        if (til.altitude < 0) // unsettled ocean is black
          return Color.black;
        else
          return Color.white; // unclaimed land is white
        
      case 1: // territory
        if (til.altitude < 0)
          return Color.black;
        
        for (int i = -1; i < 2; i ++)
          for (int j = -1; j < 2; j ++)
            if (y+i >= 0 && y+i < lats.length && x+j >=0 && x+j < lats[0].length && lats[y+i][x+j] != -1) // if in bounds
            if (!til.owners.equals(glb.getTileByIndex(lats[y+i][x+j], lons[y+i][x+j]).owners) ||
                glb.getTileByIndex(lats[y+i][x+j], lons[y+i][x+j]).altitude < 0) // if it is near a tile owned by someone else or near ocean
              return til.owners.get(0).emblem();
        
        return Color.white;
        
      case 2: // settlement
        return til.owners.get(0).emblem();
        
      case 3: // urban area
        return new Color(til.owners.get(0).emblem().getRed()>>1,
                         til.owners.get(0).emblem().getGreen()>>1,
                         til.owners.get(0).emblem().getBlue()>>1); // return darkened tile
        
      case 4: // utopia
        return new Color(255- ((255-til.owners.get(0).emblem().getRed()) >>1), 
                         255- ((255-til.owners.get(0).emblem().getGreen()) >>1), 
                         255- ((255-til.owners.get(0).emblem().getBlue()) >>1)); // return lightened tile
      default:
        return new Color(255, 127, 0);
    }
//    if (til.owners.size() > 0) {
//      if (til.development > 0)
//        System.out.println(til.development);
//      return til.owners.get(0).emblem();
//    }
//    else if (til.altitude < 0)
//      return Color.black;
//    else
//      return Color.white;
  }
  
  
  public void setGlobe(Globe newGlb) {
    glb = newGlb;
  }
  
  
  public final int width() {
    return lats[0].length;
  }
  
  
  public final int height() {
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