/**
 * MIT License
 * 
 * Copyright (c) 2017 Justin Kunimune
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package apps;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Globe;
import model.Surface;
import model.Tile;
import view.Cartogram;

/**
 * A script that simply generates some real nice terrain
 * 
 * @author jkunimune
 */
public class TerrainGenerator extends Application {
	
	public static final void main(String[] args) {
		launch(args);
	}
	
	
	public void start(Stage rootStage) throws Exception {
		Surface earth = new Globe(4);
		Cartogram map = new Cartogram();
		
		rootStage.setTitle("Terrain Generator");
		rootStage.setScene(new Scene(map));
		rootStage.show();
		
		map.draw(earth);
	}
}