package fr.itris.glips.library.geom.path.segment;

import java.awt.geom.*;

import org.apache.batik.ext.awt.geom.*;
import fr.itris.glips.library.*;

/**
 * the class that handles horizontal lineTo instructions
 * @author ITRIS, Jordi SUC
 */
public class VerticalLineToSegment extends Segment{
	
	/**
	 * the point to which the path should be lined
	 */
	private Point2D point;

	/**
	 * the constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 * @param segmentString the string defining the segment
	 */
	public VerticalLineToSegment(Segment previousSegment, String segmentString){
		
		super(previousSegment);
		parseString(segmentString);
		storeValues();
	}
	
	@Override
	public void initialize() {
		
		this.absoluteCmdName="V";
		this.relativeCmdName="v";
	}
	
	@Override
	public void storeValues() {

		//setting the end point
		endPoint=point;
	}
	
	/**
	 * parses the segment string and initializes this segment properties
	 * @param segmentString the string representation of a segment
	 */
	protected void parseString(String segmentString){
		
		segmentString=segmentString.trim();
		
		//whether the command is a relative one
		boolean isRelative=isRelativeCommand(segmentString);
		
		//getting the coordinates array corresponding to the string
		double[] coordinatesArray=getNumbers(segmentString);
		double x=0;
		double y=0;
		
		if(coordinatesArray!=null){
			
			y=coordinatesArray[coordinatesArray.length-1];
		}
		
		if(isRelative && previousSegment!=null && 
				previousSegment.getEndPoint()!=null){
			
			x=previousSegment.getEndPoint().getX();
			y+=previousSegment.getEndPoint().getY();
		}
		
		//creating the new point
		point=new Point2D.Double(x, y);
		
		//setting the end point
		endPoint=point;
	}
	
	@Override
	public void fillPath(ExtendedGeneralPath path) {
		
		path.lineTo((float)point.getX(), (float)point.getY());
		super.fillPath(path);
	}
	
	@Override
	public void applyTransform(AffineTransform transform) {

		//transforming the point
		point=transform.transform(point, null);
	}
	
	@Override
	public void modifyPoint(Point2D thePoint, int index) {}
	
	/**
	 * @return the point to which the path should be lined
	 */
	public Point2D getPoint() {
		return point;
	}
	
	@Override
	public String toString() {

		return absoluteCmdName+
			FormatStore.format(point.getY());
	}
}
