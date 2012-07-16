package fr.itris.glips.library.geom.path.segment;

import java.awt.geom.*;
import java.util.*;
import org.apache.batik.ext.awt.geom.*;
import fr.itris.glips.library.*;

/**
 * the class that handles smooth cubicTo instructions
 * @author ITRIS, Jordi SUC
 */
public class SmoothCubicToSegment extends Segment{
	
	/**
	 * the list of the points that define the segment
	 */
	private java.util.List<Point2D> pointsList=new ArrayList<Point2D>();
	
	/**
	 * a constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 * @param segmentString the string defining the segment
	 */
	public SmoothCubicToSegment(Segment previousSegment, String segmentString){
		
		super(previousSegment);
		initialize();
		parseString(segmentString);
		storeValues();
	}
	
	@Override
	public void initialize() {
		
		this.absoluteCmdName="S";
		this.relativeCmdName="s";
	}
	
	@Override
	public void storeValues() {

		//setting the last control point and the end point
		controlPoint=pointsList.get(pointsList.size()-2);
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

			//adding the first control point for each sub segment of the segment//
			
			//creating the new list of points
			ArrayList<Point2D> newPoints=new ArrayList<Point2D>();
			boolean isPreviousCubicSegment=isCubicSegment(previousSegment);
			
			//computing the first control point
			Point2D firstControlPoint;
			Point2D centerPoint;
			
			for(int i=0; i<pointsList.size(); i+=2){
				
				if(i==0){
					
					firstControlPoint=isPreviousCubicSegment?
							previousSegment.getControlPoint():previousSegment.getEndPoint();
					centerPoint=previousSegment.getEndPoint();
					
				}else{
					
					firstControlPoint=pointsList.get(i-2);
					centerPoint=pointsList.get(i-1);
				}
				
				//adding the first control point
				newPoints.add(computeSymetric(firstControlPoint, centerPoint));
				
				//adding the two other points to the list
				newPoints.add(pointsList.get(i));
				newPoints.add(pointsList.get(i+1));
			}
			
			//clearing the points list and adding the new computed points
			pointsList.clear();
			pointsList.addAll(newPoints);
		}
	}
	
	/**
	 * returns whether the provided segment is a cubic segment
	 * @param segment a segment
	 * @return whether the provided segment is a cubic segment
	 */
	protected boolean isCubicSegment(Segment segment){
		
		return (segment!=null && (segment instanceof CubicToSegment || 
				segment instanceof SmoothCubicToSegment));
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
	public void modifyPoint(Point2D point, int index) {}
	
	/**
	 * @return the list of the points that define the segment
	 */
	public java.util.List<Point2D> getPointsList() {
		return pointsList;
	}
	
	@Override
	public String toString() {
		
		String rep="";
		Point2D p2, p3;
		
		for(int i=0; i<pointsList.size(); i+=3){

			p2=pointsList.get(i+1);
			p3=pointsList.get(i+2);
			
			rep+=absoluteCmdName+
				FormatStore.format(p2.getX())+" "+
				FormatStore.format(p2.getY())+" "+
				FormatStore.format(p3.getX())+" "+
				FormatStore.format(p3.getY());
		}

		return rep;
	}
}
