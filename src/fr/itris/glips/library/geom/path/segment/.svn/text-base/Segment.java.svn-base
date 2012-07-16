package fr.itris.glips.library.geom.path.segment;

import java.awt.geom.*;
import java.util.*;
import org.apache.batik.ext.awt.geom.*;

/**
 * the superclass of all the segment classes that 
 * defines segments in a svg path
 * @author ITRIS, Jordi SUC
 */
public abstract class Segment {
	
	/**
	 * the absolute and the relative segment command names
	 */
	protected String absoluteCmdName="", relativeCmdName="";

	/**
	 * the segment that lies before the instance of this class
	 */
	protected Segment previousSegment;
	
	/**
	 * the segment that lies after the instance of this class
	 */
	protected Segment nextSegment;
	
	/**
	 * the last control point of the segment if the segment 
	 * corresponds to a Bezier curve
	 */
	protected Point2D controlPoint;
	
	/**
	 * the other control point of the segment if the segment 
	 * corresponds to a Bezier curve
	 */
	protected Point2D otherControlPoint;
	
	/**
	 * the end point of the segment
	 */
	protected Point2D endPoint;
	
	/**
	 * the segment index
	 */
	protected int segmentIndex;
	
	/**
	 * the constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 */
	public Segment(Segment previousSegment){
		
		this.previousSegment=previousSegment;
		this.segmentIndex=(previousSegment!=null?
				previousSegment.getSegmentIndex()+1:0);
	}
	
	/**
	 * returns whether the provided index matches a point in this segment
	 * @param index an index
	 * @return whether the provided index matches a point in this segment
	 */
	public boolean indexMatchSegment(int index){
		
		return ((index>=segmentIndex*10) && 
				(index<(segmentIndex+1)*10));
	}

	/**
	 * @return the segment index
	 */
	public int getSegmentIndex() {
		return segmentIndex;
	}

	/**
	 * initializes the segment
	 */
	public abstract void initialize();
	
	/**
	 * stores the values that should be used by other segments
	 */
	public abstract void storeValues();
	
	/**
	 * @return the segment before this one
	 */
	public Segment getPreviousSegment() {
		return previousSegment;
	}
	
	/**
	 * sets the previous segment before this one
	 * @param previousSegment the previous segment before this one
	 */
	public void setPreviousSegment(Segment previousSegment) {
		
		this.previousSegment=previousSegment;
	}

	/**
	 * @return the next segment after this one
	 */
	public Segment getNextSegment() {
		return nextSegment;
	}

	/**
	 * sets the next segment after this one
	 * @param nextSegment the next segment after this one
	 */
	public void setNextSegment(Segment nextSegment) {
		this.nextSegment=nextSegment;
	}
	
	/**
	 * @return the last control point of the segment if the segment 
	 * corresponds to a Bezier curve
	 */
	public Point2D getControlPoint() {
		return controlPoint;
	}

	/**
	 * @return the other control point of the segment if the segment 
	 * corresponds to a Bezier curve
	 */
	public Point2D getOtherControlPoint() {
		return otherControlPoint;
	}

	/**
	 * @return the last point of this segment
	 */
	public Point2D getEndPoint() {
		return endPoint;
	}
	
	/**
	 * @return the absolute segment command name
	 */
	public String getAbsoluteCmdName() {
		return absoluteCmdName;
	}

	/**
	 * @return the relative segment command name
	 */
	public String getRelativeCmdName() {
		return relativeCmdName;
	}

	/**
	 * fills the path with the command and values corresponding to the segment
	 * @param path a path
	 */
	public void fillPath(ExtendedGeneralPath path){}
	
	/**
	 * applies the provided affine transform to the segment
	 * @param transform a transform
	 */
	public abstract void applyTransform(AffineTransform transform);
	
	/**
	 * replaces the point at the provided index by the provided point 
	 * @param point a point
	 * @param index the index of the point to replace
	 */
	public abstract void modifyPoint(Point2D point, int index);

	/**
	 * returns whether the segment defined by the segment string 
	 * representation corresponds to a relative command
	 * @param segmentString the string representation of a segment
	 * @return whether the segment defined by the segment string 
	 * representation corresponds to a relative command
	 */
	protected boolean isRelativeCommand(String segmentString){
		
		return segmentString.startsWith(relativeCmdName);
	}
	
	/**
	 * returns the array of the double defined by the string
	 * @param pointsString a string list of points
	 * @return the array of the double defined by the string
	 */
	protected double[] getNumbers(String pointsString){

		//getting the array of doubles corresponding to each coordinate point
		String[] numberStrings=pointsString.split(" ");
		ArrayList<Double> coordinatesList=new ArrayList<Double>();
		
		if(numberStrings!=null && numberStrings.length>0){
			
			double dVal=0;
			
			for(String str : numberStrings){
				
				//converting the string into a double
				try{
					dVal=Double.parseDouble(str);
				}catch (Exception ex){dVal=Double.NaN;}
				
				if(! Double.isNaN(dVal)){
					
					//adding this double to the coordinates list
					coordinatesList.add(dVal);
				}
			}
		}
		
		//creating the coordinates array
		double[] coordinatesArray=new double[coordinatesList.size()];
		
		for(int i=0; i<coordinatesList.size(); i++){
			
			coordinatesArray[i]=coordinatesList.get(i).doubleValue();
		}
		
		return coordinatesArray;
	}
	
	/**
	 * returns the list of the points defined in the string
	 * @param pointsString a string list of points
	 * @return the list of the points defined in the string
	 */
	protected java.util.List<Point2D> getPoints(String pointsString){
		
		//the list that will be returned
		java.util.List<Point2D> pointsList=new ArrayList<Point2D>();
		
		//getting the coordinates array
		double[] coordinatesArray=getNumbers(pointsString);

		if(coordinatesArray!=null && coordinatesArray.length>0 && 
				(coordinatesArray.length%2)==0){
			
			//filling the list of the points
			for(int i=0; i<coordinatesArray.length; i+=2){
				
				pointsList.add(new Point2D.Double(
						coordinatesArray[i], coordinatesArray[i+1]));
			}
		}
		
		return pointsList;
	}
	
	/**
	 * returns the absolute value of the relative point against the base point
	 * @param relativePoint a relative point
	 * @param basePoint the base point to which the relative point is relative to
	 * @return the absolute value of the relative point against the base point
	 */
	protected Point2D computeAbsolute(Point2D relativePoint, Point2D basePoint){
		
		Point2D absolutePoint=relativePoint;
		
		if(relativePoint!=null && basePoint!=null){
			
			//computing the coordinates of the absolute point
			double x=relativePoint.getX()+basePoint.getX();
			double y=relativePoint.getY()+basePoint.getY();
			
			absolutePoint=new Point2D.Double(x, y);
		}

		return absolutePoint;
	}
	
	/**
	 * computes the symetrical point of the provided original point, 
	 * relatively to the center point
	 * @param originalPoint the original point
	 * @param center the center point
	 * @return computes the symetrical point of the provided original point, 
	 * relatively to the center point
	 */
	public static Point2D computeSymetric(Point2D originalPoint, Point2D center){
		
		return new Point2D.Double(center.getX()-(originalPoint.getX()-center.getX()), 
				center.getY()-(originalPoint.getY()-center.getY()));
	}
	
	/**
	 * computes the symetrical point of the provided original point, 
	 * relatively to the center point of the segment formed by the provided points
	 * @param originalPoint the original point
	 * @param pt1 the first point of the segment
	 * @param pt2 the second point of the segment
	 * @return computes the symetrical point of the provided original point, 
	 * relatively  to the center point of the segment formed by the provided points
	 */
	public static Point2D computeSymetricRelCenter(
			Point2D originalPoint, Point2D pt1, Point2D pt2){
		
		double x1=0, x2=0, y1=0, y2=0;
		
		if(pt1.getX()<=pt2.getX()){
			
			x1=pt1.getX();
			x2=pt2.getX();
			
		}else{
			
			x1=pt2.getX();
			x2=pt1.getX();
		}
		
		if(pt1.getY()<=pt2.getY()){
			
			y1=pt1.getY();
			y2=pt2.getY();
			
		}else{
			
			y1=pt2.getY();
			y2=pt1.getY();
		}
		
		//getting the center point of the segment
		double x=(x2-x1)/2+x1;
		double y=(y2-y1)/2+y1;
		Point2D centerPoint=new Point2D.Double(x, y);

		return computeSymetric(originalPoint, centerPoint);
	}
	
	/**
	 * creates and returns the segment described in the provided string
	 * @param previousSegment the previous segment before the one that will be created
	 * @param segmentString the string describing the segment to create
	 * @return the segment described in the provided string
	 */
	public static Segment createSegment(Segment previousSegment, String segmentString){
		
		Segment segment=null;
		
		//getting the command character
		char command=Character.toLowerCase(segmentString.charAt(0));
		
		switch (command){
		
			case 'm' :
				
				segment=new MoveToSegment(previousSegment, segmentString);
				break;
				
			case 'z' :
				
				segment=new ClosePathSegment(previousSegment);
				break;
				
			case 'l' :
				
				segment=new LineToSegment(previousSegment, segmentString);
				break;
				
			case 'h' :
				
				segment=new HorizontalLineToSegment(previousSegment, segmentString);
				break;
				
			case 'v' :
				
				segment=new VerticalLineToSegment(previousSegment, segmentString);
				break;
				
			case 'c' :
				
				segment=new CubicToSegment(previousSegment, segmentString);
				break;
				
			case 's' :
				
				segment=new SmoothCubicToSegment(previousSegment, segmentString);
				break;
				
			case 'q' :
				
				segment=new QuadraticToSegment(previousSegment, segmentString);
				break;
				
			case 't' :
				
				segment=new SmoothQuadraticToSegment(previousSegment, segmentString);
				break;
				
			case 'a' :
				
				segment=new ArcToSegment(previousSegment, segmentString);
				break;
		}
		
		return segment;
	}
}
