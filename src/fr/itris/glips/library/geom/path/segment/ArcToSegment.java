package fr.itris.glips.library.geom.path.segment;

import java.awt.geom.*;
import java.util.*;
import org.apache.batik.ext.awt.geom.*;
import fr.itris.glips.library.*;

/**
 * the class that handles horizontal arcTo instructions
 * @author ITRIS, Jordi SUC
 */
public class ArcToSegment extends Segment{
	
	/**
	 * the list of the arc data objects 
	 */
	private java.util.List<ArcData> data=new ArrayList<ArcData>();
	
	/**
	 * a constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 * @param values the array of the values defining the segment
	 */
	public ArcToSegment(Segment previousSegment, double[] values){
		
		super(previousSegment);
		initialize();
		
		//getting the end point of the line
		if(values!=null && values.length>=7){
			
			ArcData arcData=new ArcData(
					new Point2D.Double(values[0], values[1]), 
						values[2], values[3]==1, values[4]==1, 
							new Point2D.Double(values[5], values[6]));
			
			data.add(arcData);
		}
		
		storeValues();
	}

	/**
	 * the constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 * @param segmentString the string defining the segment
	 */
	public ArcToSegment(Segment previousSegment, String segmentString){
		
		super(previousSegment);
		parseString(segmentString);
		storeValues();
	}
	
	@Override
	public void initialize() {
		
		this.absoluteCmdName="A";
		this.relativeCmdName="a";
	}
	
	@Override
	public void storeValues() {

		//setting the end point
		endPoint=data.get(data.size()-1).getEndPoint();
	}
	
	/**
	 * parses the segment string and initializes this segment properties
	 * @param segmentString the string representation of a segment
	 */
	protected void parseString(String segmentString){
		
		segmentString=segmentString.trim();
		
		//whether the command is a relative one
		boolean isRelative=isRelativeCommand(segmentString);
		
		//getting the values array corresponding to the string
		double[] valuesArray=getNumbers(segmentString);
		
		if(valuesArray!=null && valuesArray.length>0){
			
			//creating the arc data
			Point2D radii;
			double xAxisRotation;
			boolean largeArcFlag;
			boolean sweepFlag;
			Point2D theEndPoint;
			ArcData arcData;
			
			for(int i=0; i<valuesArray.length; i+=7){
				
				radii=new Point2D.Double(valuesArray[i], valuesArray[i+1]);
				xAxisRotation=valuesArray[i+2];
				largeArcFlag=(valuesArray[i+3]==1);
				sweepFlag=(valuesArray[i+4]==1);
				theEndPoint=new Point2D.Double(valuesArray[i+5], valuesArray[i+6]);
				
				if(isRelative && previousSegment!=null){
					
					//converting the relative end point into an absolute one
					theEndPoint=computeAbsolute(
							theEndPoint, previousSegment.getEndPoint());
				}
				
				//creating the arc data object
				arcData=new ArcData(
						radii, xAxisRotation, largeArcFlag, sweepFlag, theEndPoint);
					
				//storing the arc data object
				data.add(arcData);
			}
		}
	}
	
	@Override
	public void fillPath(ExtendedGeneralPath path) {
		
		for(ArcData arcData : data){
			
			path.arcTo((float)arcData.getRadii().getX(), (float)arcData.getRadii().getY(), 
					(float)arcData.getXAxisRotation(), arcData.getLargeArcFlag(), 
						arcData.getSweepFlag(), (float)arcData.getEndPoint().getX(), 
						(float)arcData.getEndPoint().getY());
		}
		
		super.fillPath(path);
	}
	
	/**
	 * @return the list of the data object describing an arc
	 */
	public java.util.List<ArcData> getData() {
		return data;
	}
	
	@Override
	public void applyTransform(AffineTransform transform) {}
	
	@Override
	public void modifyPoint(Point2D point, int index) {

		//getting the arc data corresponding to the provided index
		ArcData arcData=data.get(index);
		
		//setting the new value for the point
		arcData.theEndPoint=point;
		
		//setting the new segment end point
		if(index==data.size()-1){
			
			endPoint=point;
		}
	}
	
	@Override
	public String toString() {
		
		String repr="";
		
		for(ArcData arcData : data){
			
			repr+=arcData.toString();
		}
		
		return repr;
	}
	
	/**
	 * the class used to store data 
	 * @author ITRIS, Jordi SUC
	 */
	public class ArcData{
		
		/**
		 * the radii point
		 */
		protected Point2D radii;
		
		/**
		 * the x-axis rotation angle
		 */
		protected double xAxisRotation=0;
		
		/**
		 * whether to use a large arc flag
		 */
		protected boolean largeArcFlag;
		
		/**
		 * whether to sweep the arc
		 */
		protected boolean sweepFlag;
		
		/**
		 * the end point
		 */
		protected Point2D theEndPoint;
		
		/**
		 * the constructor of the class
		 * @param radii the radii point
		 * @param xAxisRotation the x-axis rotation angle
		 * @param largeArcFlag whether to use a large arc flag
		 * @param sweepFlag whether to sweep the arc
		 * @param theEndPoint the end point
		 */
		protected ArcData(Point2D radii, double xAxisRotation, 
				boolean largeArcFlag, boolean sweepFlag, Point2D theEndPoint){
			
			this.radii=radii;
			this.xAxisRotation=xAxisRotation;
			this.largeArcFlag=largeArcFlag;
			this.sweepFlag=sweepFlag;
			this.theEndPoint=theEndPoint;
		}

		/**
		 * @return the end point
		 */
		public Point2D getEndPoint() {
			return theEndPoint;
		}

		/**
		 * @return whether to use a large arc flag
		 */
		public boolean getLargeArcFlag() {
			return largeArcFlag;
		}

		/**
		 * @return the radii point
		 */
		public Point2D getRadii() {
			return radii;
		}

		/**
		 * @return whether to sweep the arc
		 */
		public boolean getSweepFlag() {
			return sweepFlag;
		}

		/**
		 * @return the x-axis rotation angle
		 */
		public double getXAxisRotation() {
			return xAxisRotation;
		}
		
		@Override
		public String toString() {
			
			String rep=absoluteCmdName+
				FormatStore.format(radii.getX())+" "+
				FormatStore.format(radii.getY())+" "+
				FormatStore.format(xAxisRotation)+" "+
				(largeArcFlag?"1":"0")+" "+(sweepFlag?"1":"0")+" "+
				FormatStore.format(theEndPoint.getX())+" "+
				FormatStore.format(theEndPoint.getY());

			return rep;
		}
	}
}
