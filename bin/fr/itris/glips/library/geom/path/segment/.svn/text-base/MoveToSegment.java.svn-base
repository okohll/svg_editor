package fr.itris.glips.library.geom.path.segment;

import java.awt.geom.*;
import org.apache.batik.ext.awt.geom.*;
import fr.itris.glips.library.*;

/**
 * the class that handles moveTo instructions
 * @author ITRIS, Jordi SUC
 */
public class MoveToSegment extends Segment{
	
	/**
	 * the point to which the path should be moved
	 */
	private Point2D point;
	
	/**
	 * a constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 * @param values the array of the values defining the segment
	 */
	public MoveToSegment(Segment previousSegment, double[] values){
		
		super(previousSegment);
		initialize();
		
		//setting the point for the segment
		if(values!=null && values.length>=2){
			
			point=new Point2D.Double(values[0], values[1]);
		}
		
		storeValues();
	}

	/**
	 * a constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 * @param segmentString the string defining the segment
	 */
	public MoveToSegment(Segment previousSegment, String segmentString){
		
		super(previousSegment);
		initialize();
		parseString(segmentString);
		storeValues();
	}
	
	@Override
	public void initialize() {
		
		this.absoluteCmdName="M";
		this.relativeCmdName="m";
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
		
		//getting the list of the points corresponding to the segment string
		java.util.List<Point2D> pointsList=getPoints(
				segmentString.substring(1, segmentString.length()));
		
		if(pointsList.size()>0){
			
			point=pointsList.get(0);
			
			//if the segment is relative, it is transformed into an absolute one
			if(isRelative && previousSegment!=null){
				
				point=computeAbsolute(point, previousSegment.getEndPoint());
			}
		}
	}
	
	@Override
	public void fillPath(ExtendedGeneralPath path) {
		
		path.moveTo((float)point.getX(), (float)point.getY());
		super.fillPath(path);
	}
	
	@Override
	public void applyTransform(AffineTransform transform) {

		//transforming the point
		point=transform.transform(point, null);
	}
	
	@Override
	public void modifyPoint(Point2D thePoint, int index) {
		
		point=thePoint;
		endPoint=thePoint;
	}
	
	/**
	 * @return the point to which the path should be moved
	 */
	public Point2D getPoint() {
		return point;
	}
	
	@Override
	public String toString() {

		return absoluteCmdName+
			FormatStore.format(point.getX())+" "+
				FormatStore.format(point.getY());
	}
}
