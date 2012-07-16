package fr.itris.glips.library.geom.path;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import org.apache.batik.ext.awt.geom.*;
import fr.itris.glips.library.geom.path.segment.*;
import fr.itris.glips.library.geom.path.segment.Segment;

/**
 * the class that handles a path shape
 * @author ITRIS, Jordi SUC
 */
public class Path implements Shape{
	
	/**
	 * the constant corresponding to the case 
	 * when no arc can be found in the path
	 */
	public static final int REGULAR_PATH=0;
	
	/**
	 * the constant corresponding to the case 
	 * when one or more arcs can be found in the path
	 */
	public static final int PATH_WITH_ARC=1;
	
	/**
	 * the constant corresponding to the case 
	 * when only a moveTo and an arcTo instructions
	 * can be found in the path
	 */
	public static final int ARC_PATH=2;
	
	/**
	 * the comma separator that could be used to 
	 * separate the two coordinates of a point
	 */
	public static String commaSeparator=",";

	/**
	 * the extended general path used to draw this path
	 */
	private ExtendedGeneralPath pathShape=new ExtendedGeneralPath();
	
	/**
	 * the start segment for this path
	 */
	private Segment rootSegment;
	
	/**
	 * the path type
	 */
	private int pathType=REGULAR_PATH;
	
	/**
	 * the number of segments in the path
	 */
	private int segmentsNumber=0;
	
	/**
	 * a constructor of the class
	 * @param pointsString the points defining the path
	 */
	public Path(String pointsString){
		
		Segment segment=createSegments(pointsString);
		
		//filling the path
		Segment currentSeg=segment;
		
		while(currentSeg!=null){
			
			currentSeg.fillPath(pathShape);
			currentSeg=currentSeg.getNextSegment();
		}
		
		//computing the normalized segment
		rootSegment=getSegments(pathShape);
	}
	
	/**
	 * a constructor of the class
	 * @param aPath a path from which the 
	 * new path will be initialized
	 */
	public Path(Path aPath){
		
		this.pathShape=aPath.getPathShape();
		
		//computing the normalized segment
		rootSegment=getSegments(pathShape);
	}
	
	/**
	 * a constructor of the class
	 * @param pathShape a path shape from which the 
	 * new path will be initialized
	 */
	public Path(ExtendedGeneralPath pathShape){
		
		this.pathShape=pathShape;
		
		//computing the normalized segment
		rootSegment=getSegments(pathShape);
	}
	
	/**
	 * @return the path that paints the object
	 */
	public ExtendedGeneralPath getPathShape() {
		return pathShape;
	}
	
	/**
	 * @return one of the following constants : 
	 * REGULAR_PATH, PATH_WITH_ARC, ARC_PATH
	 */
	public int getPathType() {
		return pathType;
	}

	/**
	 * @return the number of segments in the path
	 */
	public int getSegmentsNumber() {
		return segmentsNumber;
	}

	/**
	 * refreshes the path shape
	 */
	public void refresh(){
		
		//resetting the path shape
		pathShape.reset();
		
		//filling the path
		Segment currentSeg=rootSegment;
		
		while(currentSeg!=null){
			
			currentSeg.fillPath(pathShape);
			currentSeg=currentSeg.getNextSegment();
		}
	}

	/**
	 * creates and returns the segments that define the path 
	 * represented by the provided string
	 * @param pointsString the string representation of a path
	 * @return the segments that define the path 
	 * represented by the provided string
	 */
	protected Segment createSegments(String pointsString){
		
		Segment startSegment=null;
		
		if(pointsString!=null && ! pointsString.equals("")){
			
			//splitting the string
			char[] chars=pointsString.toCharArray();
			ArrayList<String> splitStrings=new ArrayList<String>();
			StringBuffer buffer=new StringBuffer();
			
			for(int i=0; i<chars.length; i++){
				
				if(Character.isLetter(chars[i])){
					
					//adding the new split string to the list
					splitStrings.add(buffer.toString());
					
					//creating a new buffer string
					buffer=new StringBuffer();
				}
				
				buffer.append(chars[i]);
			}
			
			//adding the last buffer string to the list
			splitStrings.add(buffer.toString());
			
			//creating the segments
			String str="";
			Segment segment;
			Segment previousSegment=null;
			
			for(int i=0; i<splitStrings.size(); i++){
				
				str=splitStrings.get(i);
				
				//cleaning the string
				str=str.replaceAll("["+commaSeparator+"]", " ");
				str=str.replaceAll("\\s+", " ");
				str=str.trim();
				
				if(! str.equals("")){
					
					//creating the segment corresponding to the string
					segment=Segment.createSegment(previousSegment, str);
					
					if(startSegment==null){
						
						//storing the start segment
						startSegment=segment;
					}
					
					if(previousSegment!=null){
						
						//setting the next segment for the previous segment
						previousSegment.setNextSegment(segment);
					}
					
					previousSegment=segment;
				}
			}
		}

		return startSegment;
	}
	
	/**
	 * returns the root segment of the list of the segments
	 * corresponding to the provided path
	 * @param thePath a path
	 * @return the root segment of the list of the segments
	 * corresponding to the provided path
	 */
	protected Segment getSegments(ExtendedGeneralPath thePath){
		
		//creating the list of segments corresponding to the path and
		//getting the type of the path
		ExtendedPathIterator pathIt=thePath.getExtendedPathIterator();
		double[] valuesArray=new double[7];
		int type;
		Segment firstSegment=null;
		Segment segment;
		Segment previousSegment=null;
		int tmpType=REGULAR_PATH;
		
		while(! pathIt.isDone()){
			
			//getting the values for the current segment
			type=pathIt.currentSegment(valuesArray);
			
			//getting the segment corresponding to these values
			segment=getSegment(type, valuesArray, previousSegment);
			
			if(segment instanceof ArcToSegment){
				
				//as the path contains an arc segment, the type changes
				tmpType=PATH_WITH_ARC;
			}
			
			if(firstSegment==null){
				
				firstSegment=segment;
			}
			
			if(previousSegment!=null){
				
				//setting the next segment to the previous segment
				previousSegment.setNextSegment(segment);
			}
			
			previousSegment=segment;
			pathIt.next();
			segmentsNumber++;
		}
		
		//checking if the path is only an arc path
		if(firstSegment instanceof MoveToSegment){
			
			//getting the second segment
			Segment secondSegment=firstSegment.getNextSegment();
			
			if(secondSegment!=null && 
					secondSegment instanceof ArcToSegment && 
						secondSegment.getNextSegment()!=null){
				
				tmpType=ARC_PATH;
			}
		}
		
		//setting the new type for the path
		pathType=tmpType;
		
		return firstSegment;
	}
	
	/**
	 * replaces the point at the provided index by the provided point 
	 * @param point a point
	 * @param index the index of the point to replace
	 */
	public void modifyPoint(Point2D point, int index){
		
		//getting the segment corresponding to the provided index
		Segment segment=getSegment(index);

		if(segment!=null){
			
			//getting the index of the point in the segment
			int pointIndex=index-segment.getSegmentIndex()*10;

			//modifying the point at the found index in the segment
			segment.modifyPoint(point, pointIndex);
			
			//refreshing the path
			refresh();
		}
	}
	
	/**
	 * adds a new segment after the point defined by the provided index
	 * @param index the index of the point of the segment after 
	 * which a segment should be added
	 */
	public void addSegment(int index){
		
		//getting the segment corresponding to the provided index
		Segment segment=getSegment(index);
		
		if(segment!=null){
			
			//getting the middle point of the segment//
			Point2D newPoint=null;
			
			//creating the path corresponding to the segment
			ExtendedGeneralPath thePath=new ExtendedGeneralPath();
			Point2D startPoint=segment.getEndPoint();
			
			thePath.moveTo((int)startPoint.getX(), (int)startPoint.getY());
			
			if(segment.getNextSegment()!=null && 
					! (segment.getNextSegment() instanceof ClosePathSegment)){
				
				segment.getNextSegment().fillPath(thePath);
				
				//getting the length of the path
				PathLength pathLength=new PathLength(thePath);
				float length=pathLength.lengthOfPath();
				
				//computing the middle point 
				newPoint=pathLength.pointAtLength(length/2);
				
			}else{
				
				Point2D endPoint=segment.getEndPoint();
				newPoint=new Point2D.Double(endPoint.getX()+10, endPoint.getY());
			}

			//creating a new segment after this one
			Segment newSegment=null;
			
			if(segment instanceof QuadraticToSegment){
				
				//creating a quadTo segment//
				
				//getting the symetric point of the control point of the previous segment
				//Point2D newControlPoint=Segment.computeSymetric(
				//		segment.getControlPoint(), segment.getEndPoint());
				Point2D newControlPoint=segment.getControlPoint();
				
				//creating the array of the double values
				double[] values=new double[4];
				values[0]=newControlPoint.getX();
				values[1]=newControlPoint.getY();
				values[2]=newPoint.getX();
				values[3]=newPoint.getY();
				
				//creating the segment
				newSegment=new QuadraticToSegment(segment, values);
				
			}else if(segment instanceof CubicToSegment){
				
				//creating a cubicTo segment//
				
				//getting the symetric point of the control point of the previous segment
				Point2D controlPoint1=Segment.computeSymetric(
						segment.getControlPoint(), segment.getEndPoint());
				Point2D controlPoint2=Segment.computeSymetricRelCenter(
						controlPoint1, segment.getEndPoint(), newPoint);
				
				//creating the array of the double values
				double[] values=new double[6];
				values[0]=controlPoint1.getX();
				values[1]=controlPoint1.getY();
				values[2]=controlPoint2.getX();
				values[3]=controlPoint2.getY();
				values[4]=newPoint.getX();
				values[5]=newPoint.getY();
				
				//creating the segment
				newSegment=new CubicToSegment(segment, values);
				
			}else{
				
				//creating a lineTo segment//
				
				//creating the array of the double values
				double[] values=new double[2];
				values[0]=newPoint.getX();
				values[1]=newPoint.getY();
				
				//creating the segment
				newSegment=new LineToSegment(segment, values);
			}
			
			if(segment.getNextSegment()!=null){
				
				//setting the next segment to the new segment
				newSegment.setNextSegment(segment.getNextSegment());
			}
			
			//setting the next segment to the previous segment
			segment.setNextSegment(newSegment);
		}
	}
	
	/**
	 * removes the segment defined by the provided index
	 * @param index the index of a point of the segment to be removed
	 */
	public void removeSegment(int index){
		
		//getting the segment corresponding to the provided index
		Segment segment=getSegment(index);
		
		if(segment!=null && segment.getPreviousSegment()!=null && 
				segment.getNextSegment()!=null){

			Segment previousSegment=segment.getPreviousSegment();
			Segment nextSegment=segment.getNextSegment();
			
			previousSegment.setNextSegment(nextSegment);
			nextSegment.setPreviousSegment(previousSegment);
		}
	}
	
	/**
	 * inserts the provided path before this one
	 * @param insertedPathShape a new path
	 */
	public void insertBefore(ExtendedGeneralPath insertedPathShape){
		
		//reverting the provided path shape
		ExtendedGeneralPath reversedPath=revert(insertedPathShape);
		
		//creating the concatenation of this path shape with the provided one
		ExtendedGeneralPath newPathShape=new ExtendedGeneralPath();
		newPathShape.append(reversedPath.getExtendedPathIterator(), false);
		
		//adding all the segments of the current path shape but the first moveTo segment
		insertAllButFirst(pathShape, newPathShape);
		
		//setting the new path
		this.pathShape=newPathShape;
		
		//computing the normalized segments
		rootSegment=getSegments(pathShape);
	}
	
	/**
	 * closes this path if it is not already closed
	 */
	public void closePath(){
		
		//checking if the path end with a close segment
		pathShape.closePath();
		
		//computing the normalized segments
		rootSegment=getSegments(pathShape);
	}
	
	/**
	 * inserts all the segments of the pathToAdd into the receiverPath but the first segment
	 * @param pathToAdd the path whose segments will be inserted into the receiver path
	 * @param receiverPath the path that will receive new segments from the pathToAdd
	 */
	protected void insertAllButFirst(
			ExtendedGeneralPath pathToAdd, ExtendedGeneralPath receiverPath){
		
		ExtendedPathIterator pathIt=pathToAdd.getExtendedPathIterator();
		float[] values=new float[7];
		int type;
		
		pathIt.next();
		
		while(! pathIt.isDone()){
			
			type=pathIt.currentSegment(values);
			
			switch (type){
				
				case ExtendedPathIterator.SEG_MOVETO :
					
					receiverPath.moveTo(values[0], values[1]);
					break;
					
				case ExtendedPathIterator.SEG_LINETO :
					
					receiverPath.lineTo(values[0], values[1]);
					break;
					
				case ExtendedPathIterator.SEG_QUADTO :
					
					receiverPath.quadTo(
							values[0], values[1], values[2], values[3]);
					break;
					
				case ExtendedPathIterator.SEG_CUBICTO :
					
					receiverPath.curveTo(
							values[0], values[1], values[2], values[3], values[4], values[5]);
					break;
					
				case ExtendedPathIterator.SEG_ARCTO :
					
					receiverPath.arcTo(
							values[0], values[1], values[2], values[3]==1, 
								values[4]==1, values[5], values[6]);
					break;
					
				case ExtendedPathIterator.SEG_CLOSE :
					
					receiverPath.closePath();
					break;
			}
			
			pathIt.next();
		}
	}
	
	/**
	 * returns the reversed general path of the provided general path
	 * @param aPathShape a general path
	 * @return the reversed general path of the provided general path
	 */
	protected ExtendedGeneralPath revert(ExtendedGeneralPath aPathShape){
		
		//the reversed path that will be returned
		ExtendedGeneralPath reversedPath=new ExtendedGeneralPath();
		
		//creating a path object
		Path basePath=new Path(aPathShape);
		
		//getting the last segment of the path
		Segment segment=basePath.getSegment();
		Segment startSegment=null;
		
		while(segment!=null){
			
			startSegment=segment;
			segment=segment.getNextSegment();
		}
		
		//reverting the path//
		segment=startSegment;
		Segment previousSegment;
		int i=0;
		
		while(segment!=null){
			
			previousSegment=segment.getPreviousSegment();
			
			if(i==0){
				
				//adding a moveTo instruction to the path
				reversedPath.moveTo((int)segment.getEndPoint().getX(), 
						(int)segment.getEndPoint().getY());
			}
			
			//getting the values of the current segment
			if(segment instanceof LineToSegment){
				
				reversedPath.lineTo((int)previousSegment.getEndPoint().getX(), 
						(int)previousSegment.getEndPoint().getY());
				
			}else if(segment instanceof QuadraticToSegment){
				
				reversedPath.quadTo((int)segment.getControlPoint().getX(), 
					(int)segment.getControlPoint().getY(), 
						(int)previousSegment.getEndPoint().getX(), 
							(int)previousSegment.getEndPoint().getY());
				
			}else if(segment instanceof CubicToSegment){
				
				reversedPath.curveTo((int)segment.getControlPoint().getX(), 
						(int)segment.getControlPoint().getY(), 
							(int)segment.getOtherControlPoint().getX(), 
								(int)segment.getOtherControlPoint().getY(),
									(int)previousSegment.getEndPoint().getX(), 
										(int)previousSegment.getEndPoint().getY());
			}
			
			i++;
			segment=segment.getPreviousSegment();
		}
		
		return reversedPath;
	}
	
	/**
	 * inserts the provided path after this one
	 * @param insertedPathShape a new path
	 */
	public void insertAfter(ExtendedGeneralPath insertedPathShape){
		
		//creating the concatenation of this path shape with the provided one
		ExtendedGeneralPath newPathShape=new ExtendedGeneralPath();
		newPathShape.append(pathShape.getExtendedPathIterator(), false);
		
		//removing the first segment of the provided path
		insertAllButFirst(insertedPathShape, newPathShape);
		
		//setting the new path
		this.pathShape=newPathShape;
		
		//computing the normalized segments
		rootSegment=getSegments(pathShape);
	}
	
	/**
	 * getting the segment corresponding to the provided index
	 * @param index the index of a point
	 * @return the segment corresponding to the provided index
	 */
	protected Segment getSegment(int index){
		
		Segment foundSegment=null;
		
		//getting teh segment corresponding to the provided index
		Segment currentSeg=rootSegment;
		
		while(currentSeg!=null){
			
			if(currentSeg.indexMatchSegment(index)){
				
				foundSegment=currentSeg;
				break;
			}
			
			currentSeg=currentSeg.getNextSegment();
		}
		
		return foundSegment;
	}
	
	/**
	 * @return returns whether this path could apply a transform to 
	 * its points without using the "transform" attribute
	 */
	public boolean canBeAppliedTransform(){
		
		return pathType==REGULAR_PATH;
	}
	
	/**
	 * @return whether this path defines an arc
	 */
	public boolean isArc(){
		
		return pathType==ARC_PATH;
	}
	
	/**
	 * applies the provided transform to the path if and only if 
	 * the type of the path is REGULAR_PATH
	 * @param transform the new transform to apply to the path
	 */
	public void applyTransform(AffineTransform transform){
		
		if(pathType==REGULAR_PATH && transform!=null && 
				! transform.isIdentity()){
			
			//transforming each segment
			Segment currentSeg=rootSegment;
			
			while(currentSeg!=null){
				
				currentSeg.applyTransform(transform);
				currentSeg=currentSeg.getNextSegment();
			}
		}
	}
	
	/**
	 * computes the current segment type
	 */
	/*protected void computeSegmentType(){
		
		if(rootSegment!=null){
			
			Segment currentSeg=rootSegment;
			int foundType=REGULAR_PATH;
			
			//checking if the type is ARC_PATH
			if(rootSegment instanceof MoveToSegment){
				
				//getting the second segment
				Segment secondSegment=currentSeg.getNextSegment();
				
				if(secondSegment!=null && 
						secondSegment instanceof ArcToSegment && 
							secondSegment.getNextSegment()!=null){
					
					foundType=ARC_PATH;
				}
			}
			
			if(foundType!=ARC_PATH){
				
				//checking if one arc segment exists among all the segments
				while(currentSeg!=null){
					
					if(currentSeg instanceof ArcToSegment){
						
						foundType=PATH_WITH_ARC;
						break;
					}
					
					currentSeg=currentSeg.getNextSegment();
				}
			}
		}
	}*/
	
	@Override
	public String toString() {

		//creating the string representation of the list of the segments
		StringBuffer strRepr=new StringBuffer();
		Segment currentSeg=rootSegment;
		
		while(currentSeg!=null){
			
			strRepr.append(currentSeg.toString());
			currentSeg=currentSeg.getNextSegment();
		}
		
		return strRepr.toString();
	}
	
	/*@Override
	public String toString() {
		
		//creating the list of segments corresponding to the path
		ExtendedPathIterator pathIt=path.getExtendedPathIterator();
		double[] valuesArray=new double[7];
		int type;
		Segment firstSegment=null;
		Segment segment;
		Segment previousSegment=null;
		
		while(! pathIt.isDone()){
			
			//getting the values for the current segment
			type=pathIt.currentSegment(valuesArray);
			
			//getting the segment corresponding to these values
			segment=getSegment(type, valuesArray, previousSegment);
			
			if(firstSegment==null){
				
				firstSegment=segment;
			}
			
			if(previousSegment!=null){
				
				//setting the next segment to the previous segment
				previousSegment.setNextSegment(segment);
			}
			
			previousSegment=segment;
			pathIt.next();
		}
		
		//creating the string representation of the list of the segments
		StringBuffer strRepr=new StringBuffer();
		Segment currentSeg=firstSegment;
		
		while(currentSeg!=null){
			
			strRepr.append(currentSeg.toString());
			currentSeg=currentSeg.getNextSegment();
		}

		return strRepr.toString();
	}*/
	
	/**
	 * returns the segment corresponding to the provided type 
	 * and having the provided values
	 * @param type the type of a segment
	 * @param valuesArray the array values for the segment
	 * @param previousSegment the segment before the segment that will be returned
	 * @return the segment corresponding to the provided type
	 * and having the provided values
	 */
	protected Segment getSegment(int type, double[] valuesArray, 
			Segment previousSegment){
		
		Segment segment=null;
		
		switch(type){
			
			case ExtendedPathIterator.SEG_ARCTO :
				
				segment=new ArcToSegment(previousSegment, valuesArray);
				break;
				
			case ExtendedPathIterator.SEG_CLOSE :
				
				segment=new ClosePathSegment(previousSegment);
				break;
				
			case ExtendedPathIterator.SEG_CUBICTO :
				
				segment=new CubicToSegment(previousSegment, valuesArray);
				break;
				
			case ExtendedPathIterator.SEG_LINETO :
				
				segment=new LineToSegment(previousSegment, valuesArray);
				break;
				
			case ExtendedPathIterator.SEG_MOVETO :
				
				segment=new MoveToSegment(previousSegment, valuesArray);
				break;
				
			case ExtendedPathIterator.SEG_QUADTO :
				
				segment=new QuadraticToSegment(previousSegment, valuesArray);
				break;
		}
		
		return segment;
	}
	
	/**
	 * @return the current root segment for this path
	 */
	public Segment getSegment(){
		
		return rootSegment;
	}

	/**
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(Point2D point) {

		return pathShape.contains(point);
	}

	/**
	 * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
	 */
	public boolean contains(Rectangle2D rect) {

		return pathShape.contains(rect);
	}

	/**
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {

		return pathShape.contains(x, y);
	}

	/**
	 * @see java.awt.Shape#contains(double, double, double, double)
	 */
	public boolean contains(double x, double y, double w, double h) {

		return pathShape.contains(x, y, w, h);
	}

	/**
	 * @see java.awt.Shape#getBounds()
	 */
	public Rectangle getBounds() {

		return pathShape.getBounds();
	}

	/**
	 * @see java.awt.Shape#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {

		return pathShape.getBounds2D();
	}

	/**
	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform)
	 */
	public PathIterator getPathIterator(AffineTransform af) {

		return pathShape.getPathIterator(af);
	}

	/**
	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform, double)
	 */
	public PathIterator getPathIterator(AffineTransform af, double flatness) {

		return pathShape.getPathIterator(af, flatness);
	}

	/**
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D rect) {

		return pathShape.intersects(rect);
	}

	/**
	 * @see java.awt.Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {

		return pathShape.intersects(x, y, w, h);
	}
}
