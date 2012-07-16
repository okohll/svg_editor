package fr.itris.glips.library.geom.path.segment;

import java.awt.geom.*;
import java.util.*;
import org.apache.batik.ext.awt.geom.*;
import fr.itris.glips.library.*;

/**
 * the class that handles lineTo instructions
 * @author ITRIS, Jordi SUC
 */
public class LineToSegment extends Segment{
	
	/**
	 * the list of the points that define the segment
	 */
	private java.util.List<Point2D> pointsList=new ArrayList<Point2D>();

	/**
	 * a constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 * @param values the array of the values defining the segment
	 */
	public LineToSegment(Segment previousSegment, double[] values){
		
		super(previousSegment);
		initialize();
		
		//getting the end point of the line
		if(values!=null && values.length>=2){
			
			pointsList.add(new Point2D.Double(values[0], values[1]));
		}
		
		storeValues();
	}
	
	/**
	 * a constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 * @param segmentString the string defining the segment
	 */
	public LineToSegment(Segment previousSegment, String segmentString){
		
		super(previousSegment);
		initialize();
		parseString(segmentString);
		storeValues();
	}
	
	@Override
	public void initialize() {
		
		this.absoluteCmdName="L";
		this.relativeCmdName="l";
	}
	
	@Override
	public void storeValues() {

		//setting the end point
		endPoint=pointsList.get(pointsList.size()-1);
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
		java.util.List<Point2D> thePointsList=getPoints(
				segmentString.substring(1, segmentString.length()));
		
		if(thePointsList.size()>0){
			
			//filling the list of the points
			if(isRelative && previousSegment!=null){
				
				//converting the relative points into absolute ones
				Point2D prevPoint=previousSegment.getEndPoint();
				Point2D absolutePoint=null;
				
				for(Point2D point : thePointsList){
					
					absolutePoint=computeAbsolute(point, prevPoint);
					pointsList.add(absolutePoint);
				}
				
			}else{
				
				pointsList.addAll(thePointsList);
			}
		}
	}
	
	@Override
	public void fillPath(ExtendedGeneralPath path) {
		
		for(Point2D point : pointsList){
			
			path.lineTo((float)point.getX(), (float)point.getY());
		}
		
		super.fillPath(path);
	}
	
	@Override
	public void applyTransform(AffineTransform transform) {

		//creating the list of the transformed points
		ArrayList<Point2D> newList=new ArrayList<Point2D>();
		Point2D transformedPoint=null;
		
		for(Point2D point : pointsList){
			
			//transforming the point
			transformedPoint=transform.transform(point, null);
			newList.add(transformedPoint);
		}
		
		//clearing the old points and adding the new ones
		pointsList.clear();
		
		//adding the transformed points to the list
		pointsList.addAll(newList);
	}
	
	@Override
	public void modifyPoint(Point2D point, int index) {

		//setting the new point for the index
		pointsList.set(index, point);

		//setting the new segment end point
		if(index==pointsList.size()-1){
			
			endPoint=point;
		}
	}
	
	/**
	 * @return the list of the points that define the segment
	 */
	public java.util.List<Point2D> getPointsList() {
		return pointsList;
	}
	
	@Override
	public String toString() {
		
		String rep="";

		for(Point2D point : pointsList){
			
			rep+=absoluteCmdName+
				FormatStore.format(point.getX())+" "+
				FormatStore.format(point.getY());
		}

		return rep;
	}
}
