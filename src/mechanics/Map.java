package mechanics;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;



public abstract class Map extends JPanel { // a class to manage the graphic elements of terrain generation
  private static final long serialVersionUID = 1L;
  
  public Font monaco;
  public static final Color[] colors = {new Color(255,0,0), new Color(0,0,200), new Color(200,200,255), new Color(0,100,200), new Color(0,0,100),
		  new Color(255,255,255), new Color(100,255,0), new Color(255,220,0), new Color(0,100,0), new Color(150,100,0), new Color(190,180,165),
		  new Color(0,50,255), new Color(0,0,0), new Color(0,200,100)}; // colors of the biomes
  
  public static final boolean[][][] key = {
    {
      {  true } // magma
    }, {
      { false } // ocean
    }, {
      {  true, false }, // ice
      { false, false }
    }, {
      { false } // reef
    }, {
      { false } // trench
    }, {
      { false,  true,  true,  true }, // tundra
      {  true, false,  true,  true },
      {  true,  true, false,  true },
      {  true,  true,  true, false }
    }, {
      {  true,  true, false, false, false,  true }, // plains
      {  true,  true,  true,  true,  true,  true },
      {  true,  true,  true,  true,  true,  true },
      { false, false,  true,  true,  true, false },
      {  true,  true,  true,  true,  true,  true },
      {  true,  true,  true,  true,  true,  true }
    }, {
      {  true,  true,  true,  true }, // desert
      {  true, false,  true,  true },
      {  true,  true,  true,  true },
      {  true,  true,  true,  true }
    }, {
      {  true,  true,  true,  true }, // jungle
      {  true, false,  true, false },
      {  true,  true, false,  true },
      {  true,  true, false,  true }
    }, {
      {  true,  true,  true,  true,  true,  true,  true }, // mountains
      {  true,  true,  true,  true,  true,  true, false },
      { false,  true,  true,  true,  true, false,  true },
      {  true, false,  true,  true, false,  true,  true },
      {  true,  true, false, false,  true,  true,  true },
      {  true,  true, false,  true,  true,  true,  true },
      {  true, false,  true,  true,  true,  true,  true },
      {  true,  true,  true,  true,  true,  true,  true }
    }, {
      {  true,  true,  true,  true,  true,  true,  true }, // snowy mountains
      {  true,  true,  true,  true,  true,  true, false },
      { false,  true,  true,  true,  true, false, false },
      { false, false,  true,  true, false, false, false },
      {  true,  true, false, false,  true,  true,  true },
      {  true,  true, false,  true,  true,  true,  true },
      {  true, false,  true,  true,  true,  true,  true },
      {  true,  true,  true,  true,  true,  true,  true }
    }, {
      { false, false, false }, // freshwater
      { false,  true, false },
      { false, false, false }
    }, {
      { false } // space
    }, {
      { true,  true,  true,  true, false,  true }, // forest
      { true, false,  true,  true, false,  true },
      { true, false,  true,  true,  true,  true },
      { true, false,  true,  true,  true,  true },
      { true, false,  true,  true, false,  true },
      { true,  true,  true,  true, false,  true }
    }
  };
  
  private BufferedImage whittaker1, whittaker2;
      
  private int tipX, tipY, tipW, tipH; // TileTip coordinates and dimensions
  private BufferedImage img;
  public int[][] lats, lons; // remembers which tile goes to which pixels (remember to initialize in subclass constructor)
  public Surface sfc;        // if a lon is -1, the lat is the rgb value of the color to put there
  
  
  
  public Map(Surface newWorld, int w, int h) {
    monaco = new Font("Monaco", 0, 12);
    tipX = -1;
    tipY = -1;
    tipW = -1;
    tipH = -1;
    sfc = newWorld;
    
    try {
      whittaker1 = ImageIO.read(new File("Assets/whittaker1.png"));
      whittaker2 = ImageIO.read(new File("Assets/whittaker2.png"));
    } catch (IOException e) {
      System.out.println("Assets/whittaker1.png or Assets/whittaker2.png not found; some display features may be affected as a result.");
      whittaker1 = null;
      whittaker2 = null;
    }
    
    lats = new int[h][w];
    lons = new int[h][w];
    
    JFrame frame = new JFrame();
    frame.setTitle("Civi-HD");
    img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
    img.getGraphics().setFont(monaco);
    setPreferredSize(new Dimension(w, h));
    frame.add(this);
    frame.addMouseListener(new GodInterface(this, newWorld));
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
  
  
  
  public abstract Tile getCoords(int x, int y); // the tile for any given pixel
  
  
  public final void finishSuper() { // a method stupid Java forces me to put in because super must be the first thing in the constructor because the stupid constructor says so and I can't just put this dang thing in the constructor.
    for (int x = 0; x < lats[0].length; x ++) { // and i cant use w or h or g because, again, stupid Java
      for (int y = 0; y < lats.length; y ++) {
        final Tile tl = getCoords(x,y);
        lats[y][x] = tl.lat;
        lons[y][x] = tl.lon;
      }
    }
    
    initialPaint();
  }
    
  
  public final void drawPx(int x, int y, Color z) { // draws a pixel to the buffered image
    final Graphics g = img.getGraphics();
    g.setColor(z);
    g.drawLine(x, y, x, y);
    g.dispose();
  }
  
  
  public void showTileTip(int x, int y) { // displays the "TileTip" for a point on the screen
    if (lons[y][x] == -1) // if no tile here
      return; // do nothing
    
    final String[] tip = sfc.getTileByIndex(lats[y][x], lons[y][x]).getTip();
    
    if (x < width()/2)  tipX = x; // left is the left side of the tip, which changes based on what side of the screen you are on
    else                   tipX = x - tip[0].length()*7;
    tipY = y;
    tipW = tip[0].length()*7;
    tipH = tip.length*12;
    
    for (int i = -3; i < tipW + 3 && tipX+i < width(); i ++) { // draw a black rectangle around the words
      for (int j = -3; j < tipH + 3 && tipY-j >= 0; j ++) {
        drawPx(tipX+i, tipY-j, Color.black);
        lons[tipY-j][tipX+i] = -1;
      }
    }
    
    final Graphics g = img.getGraphics(); // draws the words in white 12 pt. Monaco
    g.setColor(Color.white);
    g.setFont(monaco);
    for (int i = 0; i < tip.length; i ++) // writes each line
      g.drawString(tip[i], tipX, tipY+12*(i-tip.length+1));
  }
  
  
  public final void hideTileTip() {
    if (tipX == -1) // if there is no tile tip
      return;
    
    for (int i = -3; i < tipW + 3 && tipX+i < width(); i ++) { // draw a colorful rectangle around the words
      for (int j = -3; j < tipH + 3 && tipY-j >= 0; j ++) {
        final int lon = getCoords(tipX+i, tipY-j).lon;
        if (lon != -1)
          lons[tipY-j][tipX+i] = lon;
        else
          drawPx(tipX+i, tipY-j, new Color(lons[tipY-j][tipX+i]>>16, lons[tipY-j][tipX+i]%65536>>8, lons[tipY-j][tipX+i]%256));
      }
    }
    
    tipX = -1;
  }
  
  
  public final void initialPaint() {
    for (int x = 0; x < width(); x ++) // draws background colors
      for (int y = 0; y < height(); y ++)
        if (lons[y][x] == -1)	// a lon of -1 means solid color
          drawPx(x, y, new Color(lats[y][x]>>16, lats[y][x]%65536>>8, lats[y][x]%256)); // draw the color found in the rgb value
    show();
  }
  
  
  public void display(ColS theme) { // displays the map
    for (int x = 0; x < width(); x ++)
      for (int y = 0; y < height(); y ++)
        if (lons[y][x] != -1)
          drawPx(x, y, getColorBy(theme, x, y));
    show();
  }
  
  
  public final Tile getTile(int x, int y) {
    return sfc.getTileByIndex(lats[y][x], lons[y][x]);
  }
  
  
  public Color getColorBy(ColS c, int x, int y) { // gets the color at a point on the screen
    switch (c) {
      case biome: // otherwise calculate the color from that tile
        return getColorByBiome(x, y);
      case altitude:
        return getColorByAlt(x, y);
      case altitude2:
        return getColorByAlt2(x, y);
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
      case hybrid:
        return getColorByHybrid(x, y);
      case drawn:
        return getColorByDrawn(x, y);
      case light:
        return getColorByLight(x, y);
      case satellite:
        return getColorBySatellite(x, y);
      default:
        System.err.println("...? I think you created a new color scheme constant and forgot to assign it a method.");
        return new Color(200,0,150);
    }
  }
  
  
  public Color getColorByBiome(int x, int y) {
    return colors[sfc.getTileByIndex(lats[y][x], lons[y][x]).biome];
  }
  
  
  public Color getColorByAlt(int x, int y) {
    int alt = sfc.getTileByIndex(lats[y][x], lons[y][x]).altitude;
    if (alt < -256) // if altitude is below minimum, return black
      return Color.black;
    else if (alt >= 256) // if altitude is above maximum, return white
      return Color.white;
    else if (alt < 0) // if altitude is deep, return a blue that gets blacker as oneg oes deeper
      return new Color(0, 0, alt+256);
    else if (alt == 0) // if altitude is sea level
      return new Color(0,127,255);
    else if (alt < 128) // if altitude is above sea level, return a green that gets brighter as one goes higher
      return new Color(0, alt+128, 0);
    else // if altitude is high, return a green that gets whiter as one goes higher
      return new Color(alt*2-256, 255, alt*2-256);
  }
  
  
  public Color getColorByAlt2(int x, int y) { // approximates altitude by shifting it down 255
    int alt = sfc.getTileByIndex(lats[y][x], lons[y][x]).altitude-255;
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
    int dryness = 255 - sfc.getTileByIndex(lats[y][x], lons[y][x]).rainfall;
    if (dryness < 0)
      return new Color(100, 0, 255);
    if (dryness >= 256)
      return new Color(255, 255, 100);
    return new Color(dryness, dryness, 255); // return a blue that gets darker with rainfall
  }
  
  
  public Color getColorByTemp(int x, int y) {
    int coldness = 255 - sfc.getTileByIndex(lats[y][x], lons[y][x]).temperature;
    if (coldness >= 256)
      return new Color(0, 0, 100);
    if (coldness < 0)
      return new Color(255, 100, 0);
    return new Color(255, coldness, coldness); // return a red that gets darker with temperature
  }
  
  
  public Color getColorByClimate(int x, int y) { // returns a color from a whittaker diagram
    Tile til = sfc.getTileByIndex(lats[y][x], lons[y][x]);
    
    final int temper = Math.min(Math.max(til.temperature, 0), 255);
    final int rainfl = Math.min(Math.max(til.rainfall, 0), 255);
    final int waterl = Math.min(Math.max(til.water, 0), 255);
    if (til.altitude < 0 || til.water >= 1)
      return new Color(whittaker2.getRGB(temper, waterl));
    else
      return new Color(whittaker1.getRGB(temper, rainfl));
  }
  
  
  public Color getColorByWater(int x, int y) { // a blue that gets darker with more freshwater
    int wtr = sfc.getTileByIndex(lats[y][x], lons[y][x]).water;
    if (wtr < 0)
      return Color.red;
    else if (wtr == 0)
      return Color.black;
    else if (wtr <= 255)
      return new Color(255-wtr,255-wtr,255);
    else if (wtr <= 1020)
      return new Color(0,0,319-wtr/4);
    else
      return new Color(100,0,255);
  }
  
  
  public Color getColorByWaterLevel(int x, int y) {
    int height = sfc.getTileByIndex(lats[y][x], lons[y][x]).waterLevel()/4;
    if (height >= 256)
      return Color.white;
    if (height < 0)
      return Color.black;
    return new Color(height, height, height); // return a grey that gets brighter with water level
  }
  
  
  public Color getColorByLight(int x, int y) {	// return a color that mimics sunlight hitting the terrain
    Tile t0 = sfc.getTileByIndex(lats[y][x], lons[y][x]);
    Tile t1,t2;
    try {
      t1 = sfc.getTileByIndex(lats[y][x]-1, lons[y][x]);
    } catch (IndexOutOfBoundsException e) {
      t1 = t0;
    }
    try {
      t2 = sfc.getTileByIndex(lats[y][x], lons[y][x]-1);
    } catch (IndexOutOfBoundsException e) {
      t2 = t0;
    }
    final int intensity = Math.max(Math.min((2*t0.altitude-t1.altitude-t2.altitude+32)<<2, 255), 0);
    if (t0.altitude >= 0)	return new Color(intensity>>1,intensity,intensity>>1);
    else					return new Color(intensity>>2,intensity>>2,(intensity>>2)+128);
  }
  
  
  public Color getColorBySatellite(int x, int y) { // a combination of climate and light
    Tile t0 = sfc.getTileByIndex(lats[y][x], lons[y][x]);
    Tile t1,t2;
    try {
      t1 = sfc.getTileByIndex(lats[y][x]-1, lons[y][x]);
    } catch (IndexOutOfBoundsException e) {
      t1 = t0;
    }
    try {
      t2 = sfc.getTileByIndex(lats[y][x], lons[y][x]-1);
    } catch (IndexOutOfBoundsException e) {
      t2 = t0;
    }
    final double s = Math.max(Math.min(t0.altitude/16.0-t1.altitude/32.0-t2.altitude/32.0+1/2.0, 1), 0.2);
    // first calculate the sunlight intensity
    final int temper = Math.min(Math.max(t0.temperature, 0), 255);
    final int rainfl = Math.min(Math.max(t0.rainfall, 0), 255); // then calculate the color
    final int waterl = Math.min(Math.max(t0.water, 0), 255);
    
    Color c;
    if (t0.altitude >= 0 && t0.water < 1)	// land takes from whittaker1
      c = new Color(whittaker1.getRGB(temper, rainfl));
    else									// water takes from whittaker2
      c = new Color(whittaker2.getRGB(temper, waterl));
      
    return new Color((int)(s*c.getRed()), (int)(s*c.getGreen()), (int)(s*c.getBlue()));
  }
  
  
  public Color getColorByTerritory(int x, int y) {
    final Tile til = sfc.getTileByIndex(lats[y][x], lons[y][x]);
    
    if (til.development == 0) {
      if (til.isWet()) // unsettled water is black
        return Color.black;
      else
        return Color.white; // unclaimed land is white
    }
    else {
      final Civi civ = til.owners.get((x+y >>2) % til.owners.size());
      
      switch (til.development) {
        case 1: // territory
          if (til.isWet())
          return Color.black;
          
          for (int i = -3; i <= 3; i ++)
            if (y+i >= 0 && y+i < lats.length) // if in bounds
              for (int j = -3; j <= 3; j ++)
                if (i*i + j*j <= 9) // if in circle
                  if (x+j >=0 && x+j < lats[0].length && lons[y+i][x+j] != -1) // if in bounds
                    if (!til.owners.equals(sfc.getTileByIndex(lats[y+i][x+j], lons[y+i][x+j]).owners) ||
                        sfc.getTileByIndex(lats[y+i][x+j], lons[y+i][x+j]).altitude < 0) // if it is near a tile owned by someone else or near ocean
                      return civ.emblem();
          
          return Color.white;
          
        case 2: // settlement
          return civ.emblem();
          
        case 3: // urban area
          return civ.emblem().darker(); // return darkened tile
          
        case 4: // utopia
          return new Color(255- ((255-civ.emblem().getRed()) >>1), 
                           255- ((255-civ.emblem().getGreen()) >>1), 
                           255- ((255-civ.emblem().getBlue()) >>1)); // return lightened tile
        default:
          return new Color(255, 127, 0);
      }
    }
  }
  
  
  public Color getColorByHybrid(int x, int y) {
    final Tile til = sfc.getTileByIndex(lats[y][x], lons[y][x]);
    
    if (til.development == 0) {
      return getColorByBiome(x, y);
    }
    else {
      final Civi civ = til.owners.get((x+y >>2) % til.owners.size());
      
      switch (til.development) {
        case 1: // territory
          for (int i = -3; i <= 3; i ++)
            if (y+i >= 0 && y+i < lats.length) // if in bounds
              for (int j = -3; j <= 3; j ++)
                if (i*i + j*j <= 9) // if in circle
                  if (x+j >=0 && x+j < lats[0].length && lons[y+i][x+j] != -1) // if in bounds
                    if (!til.owners.equals(sfc.getTileByIndex(lats[y+i][x+j], lons[y+i][x+j]).owners)) // if it is near a tile owned by someone else
                      return civ.emblem();
          
          return getColorByBiome(x,y);
          
        case 2: // settlement
          return civ.emblem();
          
        case 3: // urban area
          return new Color(civ.emblem().getRed()>>1,
                           civ.emblem().getGreen()>>1,
                           civ.emblem().getBlue()>>1); // return darkened tile
          
        case 4: // utopia
          return new Color(255- ((255-civ.emblem().getRed()) >>1), 
                           255- ((255-civ.emblem().getGreen()) >>1), 
                           255- ((255-civ.emblem().getBlue()) >>1)); // return lightened tile
        default:
          return new Color(255, 127, 0);
      }
    }
  }
  
  
  public Color getColorByDrawn(int x, int y) {
    final Tile til = sfc.getTileByIndex(lats[y][x], lons[y][x]);
    
    if (!key[til.biome][y%key[til.biome].length][x%key[til.biome][0].length])
      return Color.black; // draws biomes in black patterns
    
    if (til.development == 0) {
      return Color.white; // unclaimed land is white
    }
    else {
      final Civi civ = til.owners.get((x+y >>2) % til.owners.size());
      
      switch (til.development) {
        case 1: // territory
          
          for (int i = -3; i <= 3; i ++)
            if (y+i >= 0 && y+i < lats.length) // if in bounds
              for (int j = -3; j <= 3; j ++)
                if (i*i + j*j <= 9) // if in circle
                  if (x+j >=0 && x+j < lats[0].length && lons[y+i][x+j] != -1) // if in bounds and there is a tile
                    if (!til.owners.equals(sfc.getTileByIndex(lats[y+i][x+j], lons[y+i][x+j]).owners) || // if it is near a tile owned by someone else
                        ((sfc.getTileByIndex(lats[y+i][x+j], lons[y+i][x+j]).altitude < 0) && til.altitude >= 0)) // or a coast
                      return civ.emblem();
          
          return Color.white;
          
        case 2: // settlement
          return civ.emblem();
          
        case 3: // urban area
          return new Color(civ.emblem().getRed()>>1,
                           civ.emblem().getGreen()>>1,
                           civ.emblem().getBlue()>>1); // return darkened tile
          
        case 4: // utopia
          return new Color(255- ((255-civ.emblem().getRed()) >>1), 
                           255- ((255-civ.emblem().getGreen()) >>1), 
                           255- ((255-civ.emblem().getBlue()) >>1)); // return lightened tile
        default:
          return new Color(255, 127, 0);
      }
    }
  }
  
  
  public void setSurface(Surface newSfc) {
    sfc = newSfc;
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
  
  
  @Override
  public void show() {
    repaint();
  }
}