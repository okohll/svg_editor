package fr.itris.glips.svgeditor.shape.path.segments;

import java.awt.geom.*;

/**
 * the class describing a quadTo segment
 * @author Jordi SUC
 */
public class QuadToSeg extends ActionSeg {

	/**
	 * the control point of the segment
	 */
	private Point2D controlPoint;
	
	/**
	 * the end point of the segment
	 */
	private Point2D endPoint;
	
	/**
	 * the constructor of the class
	 * @param controlPoint the control point
	 * @param endPoint the end point
	 */
	public QuadToSeg(Point2D controlPoint, Point2D endPoint){
		
		this.controlPoint=controlPoint;
		this.endPoint=endPoint;
	}

	@Override
	public Point2D getControlPoint() {
		return controlPoint;
	}

	@Override
	public Point2D getEndPoint() {
		return endPoint;
	}
}
