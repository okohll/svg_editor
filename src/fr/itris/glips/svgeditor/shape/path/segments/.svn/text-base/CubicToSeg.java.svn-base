package fr.itris.glips.svgeditor.shape.path.segments;

import java.awt.geom.*;

/**
 * the class describing a cubicTo segment
 * @author Jordi SUC
 */
public class CubicToSeg extends ActionSeg {

	/**
	 * the first control point of the segment
	 */
	private Point2D firstControlPoint;
	
	/**
	 * the second control point of the segment
	 */
	private Point2D secondControlPoint;
	
	/**
	 * the end point of the segment
	 */
	private Point2D endPoint;
	
	/**
	 * the constructor of the class
	 * @param firstControlPoint the first control point
	 * @param secondControlPoint the second control point
	 * @param endPoint the end point
	 */
	public CubicToSeg(Point2D firstControlPoint, 
			Point2D secondControlPoint, Point2D endPoint){
		
		this.firstControlPoint=firstControlPoint;
		this.secondControlPoint=secondControlPoint;
		this.endPoint=endPoint;
	}

	/**
	 * @return the first control point of the segment
	 */
	public Point2D getFirstControlPoint() {
		return firstControlPoint;
	}
	
	@Override
	public Point2D getControlPoint() {
		return secondControlPoint;
	}

	@Override
	public Point2D getEndPoint() {
		return endPoint;
	}
}
