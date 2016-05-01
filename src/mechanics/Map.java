package mechanics;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;



public abstract class Map extends JPanel { // a class to manage the graphic elements of terrain generation
  private static final long serialVersionUID = 1L;
  
  public Font monaco;
  /*public static final Color[] colors = {new Color(255,63,0), new Color(0,0,200), new Color(200,200,255), new Color(20, 70, 200), new Color(0,0,150),
    new Color(255,255,255), new Color(79,191,39), new Color(200,200,50), new Color(0,100,0), new Color(200,100,50), new Color(200,150,255),
    new Color(10,33,255), new Color(0,0,0), new Color(0,127,75)}; // colors of the biomes*/
  public static final Color[] colors = {new Color(255,0,0), new Color(0,0,200), new Color(200,200,255), new Color(0,100,200), new Color(0,0,100),
		  new Color(255,255,255), new Color(100,255,0), new Color(255,220,0), new Color(0,100,0), new Color(150,100,0), new Color(200,100,255),
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
      
  private int tipX, tipY, tipW, tipH; // TileTip coordinates and dimensions
  private BufferedImage img;
  public int[][] lats, lons; // remembers which tile goes to which pixels (remember to initialize in subclass constructor)
  public Surface sfc;        // if a lat is -1, the lon is the rgb value of the color to put there
  
  
  
  public Map(Surface newWorld, int w, int h) {
    monaco = new Font("Monaco", 0, 12);
    tipX = -1;
    tipY = -1;
    tipW = -1;
    tipH = -1;
    sfc = newWorld;
    
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
  
  
  
  public abstract Point getCoords(int x, int y); // the tile coordinates for any given pixel
  
  
  public final void finishSuper() { // a method stupid Java forces me to put in because super must be the first thing in the constructor because the stupid constructor says so and I can't just put this dang thing in the constructor.
    for (int x = 0; x < lats[0].length; x ++) { // and i cant use w or h or g because, again, stupid Java
      for (int y = 0; y < lats.length; y ++) {
        final Point pt = getCoords(x,y);
        lats[y][x] = pt.y;
        lons[y][x] = pt.x;
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
    if (lats[y][x] == -1) // if no tile here
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
        lats[tipY-j][tipX+i] = -1;
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
        final int lat = getCoords(tipX+i, tipY-j).y;
        if (lat != -1)
          lats[tipY-j][tipX+i] = lat;
        else
          drawPx(tipX+i, tipY-j, new Color(lons[tipY-j][tipX+i]>>16, lons[tipY-j][tipX+i]%65536>>8, lons[tipY-j][tipX+i]%256));
      }
    }
    
    tipX = -1;
  }
  
  
  public final void initialPaint() {
    for (int x = 0; x < width(); x ++) // draws background colors
      for (int y = 0; y < height(); y ++)
        if (lats[y][x] == -1)
          drawPx(x, y, new Color(lons[y][x]>>16, lons[y][x]%65536>>8, lons[y][x]%256)); // draw the color found in the rgb value
    show();
  }
  
  
  public void display(ColS theme) { // displays the map
    for (int x = 0; x < width(); x ++)
      for (int y = 0; y < height(); y ++)
        if (lats[y][x] != -1)
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
      default:
        return new Color(150, 0, 255);
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
      return new Color(0, 0, 255);
    if (dryness >= 256)
      return new Color(255, 255, 255);
    return new Color(dryness, dryness, 255); // return a blue that gets darker with rainfall
  }
  
  
  public Color getColorByTemp(int x, int y) {
    int coldness = 255 - sfc.getTileByIndex(lats[y][x], lons[y][x]).temperature;
    if (coldness >= 256)
      return new Color(255, 0, 0);
    if (coldness < 0)
      return new Color(255, 255, 255);
    return new Color(255, coldness, coldness); // return a red that gets darker with temperature
  }
  
  
  public Color getColorByClimate(int x, int y) { // returns a pastoral combination of cyan and magenta corresponding to humidity and temperature
    int coldness = 255-sfc.getTileByIndex(lats[y][x], lons[y][x]).temperature;
    int dryness = 255-sfc.getTileByIndex(lats[y][x], lons[y][x]).rainfall;
    if (coldness < 0)
      coldness = 0;
    if (coldness >= 256)
      coldness = 255;
    if (dryness < 0)
      dryness = 0;
    if (dryness >= 256)
      dryness = 255;
    return new Color(coldness, dryness, 255);
  }
  
  
  public Color getColorByWater(int x, int y) {
    int dryness = 255 - sfc.getTileByIndex(lats[y][x], lons[y][x]).water;
    if (dryness == 255)
      return Color.black;
    if (dryness >= 256)
      return new Color(255, 255, 255);
    if (dryness < 0)
      return new Color(0, 0, 255);
    return new Color(dryness, dryness, 255); // return a blue that gets darker with freshwater
  }
  
  
  public Color getColorByWaterLevel(int x, int y) {
    int height = sfc.getTileByIndex(lats[y][x], lons[y][x]).waterLevel();
    if (height >= 256)
      return Color.white;
    if (height < 0)
      return Color.black;
    return new Color(height, height, height); // return a grey that gets brighter with water level
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
                  if (x+j >=0 && x+j < lats[0].length && lats[y+i][x+j] != -1) // if in bounds
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
                  if (x+j >=0 && x+j < lats[0].length && lats[y+i][x+j] != -1) // if in bounds
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
                  if (x+j >=0 && x+j < lats[0].length && lats[y+i][x+j] != -1) // if in bounds
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