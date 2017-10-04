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
package model;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A small region on the Earth's surface
 * 
 * @author jkunimune
 */
public class Tile {
	
	private double latitude; //northness from equator in radians
	private double longitude; //eastness from prime meridian in radians
	private double altitude; //height from sea level
	private double temperature; //hotness from 0 to 1
	private double humidity; //wetness from 0 to 1
	private Biome biome; //the biosphere category
	
	private Tile parent; //the parent node in tree form
	private Collection<Tile> children; //the children nodes in tree form
	private Collection<Tile> adjacent; //the nodes with which this one shares an edge
	//TODO: account for adjacent tiles having different distances
	
	public Tile(double latitude, double longitude, Tile parent) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = 0;
		this.temperature = 0;
		this.humidity = 0;
		this.biome = null;
		
		this.parent = parent;
		this.children = new LinkedList<Tile>();
		this.adjacent = new LinkedList<Tile>();
		if (parent != null)
			this.parent.children.add(this);
	}
	
	public Collection<Tile> getChildren() {
		return this.children;
	}
	
	public Collection<Tile> getAdjacent() {
		return this.adjacent;
	}
	
	public double getLat() { //return the latitude in radians
		return this.latitude;
	}
	
	public double getLon() {
		return this.longitude;
	}
	
	public static enum Biome {
		OCEAN, ICE, SNOW, GRASSLAND, SAVANNA, DESERT, JUNGLE;
	}
	
}
