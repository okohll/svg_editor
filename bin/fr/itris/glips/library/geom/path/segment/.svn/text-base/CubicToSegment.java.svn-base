package fr.itris.glips.library.geom.path.segment;

import java.awt.geom.*;
import java.util.*;
import org.apache.batik.ext.awt.geom.*;
import fr.itris.glips.library.*;

/**
 * the class that handles cubicTo instructions
 * @author ITRIS, Jordi SUC
 */
public class CubicToSegment extends Segment{
	
	/**
	 * the list of the points that define the segment
	 */
	private java.util.List<Point2D> pointsList=new ArrayList<Point2D>();

	/**
	 * a constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 * @param values the array of the values defining the segment
	 */
	public CubicToSegment(Segment previousSegment, double[] values){
		
		super(previousSegment);
		initialize();
		
		if(values!=null && values.length>=6){
			
			pointsList.add(new Point2D.Double(values[0], values[1]));
			pointsList.add(new Point2D.Double(values[2], values[3]));
			pointsList.add(new Point2D.Double(values[4], values[5]));
		}
		
		storeValues();
	}
	
	/**
	 * a constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 * @param segmentString the string defining the segment
	 */
	public CubicToSegment(Segment previousSegment, String segmentString){
		
		super(previousSegment);
		initialize();
		parseString(segmentString);
		storeValues();
	}
	
	@Override
	public void initialize() {
		
		this.absoluteCmdName="C";
		this.relativeCmdName="c";
	}
	
	@Override
	public void storeValues() {

		//setting the last control point and the end point
		controlPoint=pointsList.get(pointsList.size()-2);
		otherControlPoint=pointsList.get(pointsList.size()-3);
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
		
		Point2D p1, p2, p3;
		
		for(int i=0; i<pointsList.size(); i+=3){
			
			p1=pointsList.get(i);
			p2=pointsList.get(i+1);
			p3=pointsList.get(i+2);
			
			path.curveTo((float)p1.getX(), (float)p1.getY(),
					(float)p2.getX(), (float)p2.getY(),
					(float)p3.getX(), (float)p3.getY());
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
		Point2D p1, p2, p3;
		
		for(int i=0; i<pointsList.size(); i+=3){
			
			p1=pointsList.get(i);
			p2=pointsList.get(i+1);
			p3=pointsList.get(i+2);
			
			rep+=absoluteCmdName+
				FormatStore.format(p1.getX())+" "+
				FormatStore.format(p1.getY())+" "+
				FormatStore.format(p2.getX())+" "+
				FormatStore.format(p2.getY())+" "+
				FormatStore.format(p3.getX())+" "+
				FormatStore.format(p3.getY());
		}

		return rep;
	}
}
