package fr.itris.glips.library.geom;

import java.awt.*;
import java.awt.geom.*;

/**
 * the class defining a polyline with points having double coordinates
 * @author ITRIS, Jordi SUC
 */
public class Polyline2D extends PolyShape2D{

	/**
	 * the constructor of the class
	 */
	public Polyline2D() {}
	
	/**
	 * the constructor of the class
	 * @param coordinates the array of the coordinates of the points defining the polyline,
	 * the array size should be even
	 * @throws Exception an exception raised if the array is null or empty and 
	 * the array size is not even
	 */
	public Polyline2D(double[] coordinates) throws Exception{
		
		super(coordinates);
	}
	
	/**
	 * the constructor of the class
	 * @param polyline the polyline to clone
	 */
	public Polyline2D(PolyShape2D polyline){
		
		super(polyline);
	}
	
	/**
	 * the constructor of the class
	 * @param thePoints the array of the points defining the polyline,
	 * the array size should be even
	 * @throws Exception an exception raised if the array is null or empty and 
	 * the array size is not even
	 */
	public Polyline2D(Point2D[] thePoints) throws Exception{
		
		super(thePoints);
	}
	
	/**
	 * the constructor of the class
	 * @param thePoints the array of the points defining the polyline,
	 * the array size should be even
	 * @throws Exception an exception raised if the array is null or empty and 
	 * the array size is not even
	 */
	public Polyline2D(Point[] thePoints) throws Exception{
		
		super(thePoints);
	}
	
	@Override
	public PolyShape2D cloneShape() {
		
		return new Polyline2D(this);
	}
}
