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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * A geodesic mesh of tiles that mimics a sphere as closely as possible
 * 
 * @author jkunimune
 */
public class Globe implements Surface {
	
	private Tile root;
	
	
	
	public Globe(int order) {
		this.root = new Tile(Math.PI/2, 0, null); //start the tree
		
		List<Tile> vertices = new ArrayList<Tile>(12); //start by constructing an icosohedron
		List<Tile[]> faces = new ArrayList<Tile[]>(20); //all triangles are defined counterclockwise
		vertices.add(root); //north pole
		for (int i = 0; i < 10; i ++) {
			final double lat = i%2>0 ? Math.atan(.5) : -Math.atan(.5);
			final double lon = Math.PI/5 * (i - 4.5);
			vertices.add(new Tile(lat, lon, root)); //26.6&deg; nodes
		}
		vertices.add(new Tile(-Math.PI/2, 0, root)); //south pole
		for (int i = 2; i <= 10; i += 2) {
			faces.add(new Tile[] {
					vertices.get(0), vertices.get(i), vertices.get(i%10+2) }); //north faces
		}
		for (int i = 2; i <= 10; i += 2) {
			faces.add(new Tile[] {
					vertices.get(i), vertices.get(i%10+1), vertices.get(i%10+2) }); //Cancer faces
			faces.add(new Tile[] {
					vertices.get(i), vertices.get(i-1), vertices.get(i%10+1) }); //Capricorn faces
		}
		for (int i = 1; i <= 10; i += 2) {
			faces.add(new Tile[] {
					vertices.get(i), vertices.get(11), vertices.get((i+2)%10) }); // south faces
		}
		
		for (int i = 0; i < order; i ++) { //now, iterate the geodesic process
			List<Tile[]> faces2 = new LinkedList<Tile[]>();
			Map<CoordKey, Tile> newVertices = new HashMap<CoordKey, Tile>();
			
			for (Tile[] tri: faces) { //for each existing triangle
				Tile[] mid = new Tile[3];
				for (int j = 0; j < 3; j ++)
					mid[j] = tileBetween(tri[j], tri[(j+1)%3], newVertices); //generate the midpoints
				for (int j = 0; j < 3; j ++)
					faces2.add(new Tile[] { mid[j], tri[(j+1)%3], mid[(j+1)%3] }); //and generate new triangles
				faces2.add(mid);
			}
			
			faces = faces2; //and replace the old triangles
		}
		
		for (Tile[] face: faces)
			for (int i = 0; i < 3; i ++)
				face[i].getAdjacent().add(face[(i+1)%3]);
	}
	
	
	@Override
	public Iterator<Tile> iterator() {
		return new Iterator<Tile>() {
			
			Queue<Tile> queue = new LinkedList<Tile>(Arrays.asList(root));
			
			public boolean hasNext() {
				return !queue.isEmpty();
			}

			public Tile next() {
				Tile next = queue.poll();
				queue.addAll(next.getChildren());
				return next;
			}
			
		};
	}
	
	
	private Tile tileBetween(Tile t0, Tile t1, Map<CoordKey, Tile> existing) {
		final double x = Math.cos(t0.getLat())*Math.cos(t0.getLon()) + Math.cos(t1.getLat())*Math.cos(t1.getLon());
		final double y = Math.cos(t0.getLat())*Math.sin(t0.getLon()) + Math.cos(t1.getLat())*Math.sin(t1.getLon());
		final double z = Math.sin(t0.getLat()) + Math.sin(t1.getLat());
		final CoordKey key = new CoordKey(Math.atan2(z, Math.hypot(x, y)), Math.atan2(y, x));
		if (existing.containsKey(key)) {
			return existing.get(key);
		}
		else {
			final Tile midpoint = new Tile(key.d1, key.d2, t0); //TODO:If it becomes an issue, I might use whichever of t0 and t1 has fewer children
			existing.put(key, midpoint);
			return midpoint;
		}
	}
}



class CoordKey { //a simple class for keeping track of my things
	public double d1, d2;
	
	public CoordKey(double d1, double d2) {
		this.d1 = d1;
		this.d2 = d2;
	}
	
	@Override
	public int hashCode() {
		return (int)((this.d1/Math.PI+1)*32768) | (int)((this.d2/Math.PI+1)*32768)<<16;
	}
	
	@Override
	public boolean equals(Object that) {
		if (!(that instanceof CoordKey))
			return false;
		return ((CoordKey)that).d1 == this.d1 && ((CoordKey)that).d2 == this.d2;
	}
}
