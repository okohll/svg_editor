package fr.itris.glips.extension.jwidget.trends.runtime.view.component.painters;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.controller.*;

/**
 * the painter used to draw curves
 * @author ITRIS, Jordi SUC
 */
public class CurvePainter {
	
	/**
	 * the size of each point shape
	 */
	private final Dimension pointShapeSize=new Dimension(6, 6);
	
	/**
	 * the ellipse used to draw points
	 */
	private final Ellipse2D.Double ellipsePoint=new Ellipse2D.Double();
	
	/**
	 * the object storing all the configuration property values for the current curve
	 */
	private TrendsCurveConfiguration config;
	
	/**
	 * whether an interpolation shape in drawn between two points
	 */
	private boolean useInterpolation=false;
	
	/**
	 * whether the last drawn point should be drawn at each period when no point is drawn
	 */
	private boolean interpolationWithPeriod=false;
	
	/**
	 * the general paths
	 */
	private GeneralPath[] paths=null;
	
	/**
	 * the constructor of the class
	 * @param painterManager the curve painters manager
	 * @param config 	the object storing all the configuration property values
	 * 							for the current curve
	 */
	public CurvePainter(CurvePaintersManager painterManager, TrendsCurveConfiguration config){
		
		this.config=config;
		
		//getting the controller part of the trends widget
		TrendsRuntimeController controller=painterManager.getCurvesComponent().
																		getComponentsHandler().getView().getController();
		
		//registering the tag of this curve
		controller.registerTag(config.getTagName(), config.isEnumeratedTag());
		
		//computing whether an interpolation shape should be drawn between two points
		String interpolationType=config.getInterpolation();
		useInterpolation=(! interpolationType.equals("") && ! interpolationType.equals("dot"));
		
		//computing whether the last drawn point should 
		//be drawn at each period when no point is drawn
		interpolationWithPeriod=config.getConfiguration().interpolationWithPeriod();
		
		//creating the paths
		paths=new GeneralPath[6];
		
		for(int i=0; i<paths.length; i++){
			
			paths[i]=new GeneralPath();
		}
	}

	/**
	 * @return the configuration object
	 */
	public TrendsCurveConfiguration getConfig() {
		return config;
	}
	
	/**
	 * prepares and returns the general paths that will be drawn on the trends component when the real
	 * time mode is started
	 * @param tagValues the map associating a horodate to a tag value
	 * @param leftOffset the left offset
	 * @param rightOffset the right offset
	 * @param topOffset the top offset of the component of the curves
	 * @param availableWidth the available width to draw
	 * @param availableHeight the available height to draw
	 * @param startDate the start date
	 * @param endDate the end date
	 */
	public void preparePoints(TreeMap<Long, Object> tagValues, int leftOffset, int rightOffset, 
												int topOffset, int availableWidth, int availableHeight, 
												long startDate, long endDate){
		
		if(tagValues!=null){

			//resetting the general paths
			for(int i=0; i<paths.length; i++){
				
				paths[i].reset();
			}
			
			//getting the list of the tag values
			LinkedList<String> enumTagValues=config.getEnumeratedTagValues();
				
			if(((config.isEnumeratedTag() && enumTagValues.size()>0) || 
					(! config.isEnumeratedTag())) && tagValues.size()>0){
				
				//getting the min and max values
				double minValue=config.getMinValue();
				double maxValue=config.getMaxValue();

				//getting the current min and max values
				if(! Double.isNaN(minValue) && ! Double.isNaN(maxValue)){
					
					//getting the delta between the min and the max values
					double delta=maxValue-minValue;

					//getting the refresh period
					long period=config.getConfiguration().getRefreshPeriod();
					
					//getting the vertical zoom data
					double verticalZoomFactor=config.getConfiguration().getVerticalZoomFactor();
					double verticalZoomOrigin=config.getConfiguration().getVerticalZoomOrigin();
					
					//adding the points into the general path and interpolating between the points
					int x=0;
					int y=0;
					int pX=Integer.MAX_VALUE;
					int pY=Integer.MAX_VALUE;
					Object value=null;
					double dVal=0;
					String sVal="";
					int index=0;
					boolean isInvalid=false, isPreviousInvalid=false;
					long previousTime=0;
					int i=0;
					int size=tagValues.size();
					boolean succeeded=false;
					
					for(long time : tagValues.keySet()){

						//computing the abscisse for the item
						x=(int)Math.floor(availableWidth-(endDate-time)*availableWidth/
								config.getConfiguration().getHorizontalAxisDuration())+leftOffset;

						//computing the ordinate for the item
						value=tagValues.get(time);
						succeeded=false;

						if(value!=null){
							
							if(config.isEnumeratedTag()){
								
								//handling the enumerated tag value position
								sVal=(String)value;
								index=enumTagValues.indexOf(sVal);
								
								if(index!=-1){
									
									y=(int)Math.round((((availableHeight-
											(index-minValue)*availableHeight/delta-verticalZoomOrigin)*
												verticalZoomFactor)+topOffset));
									
									succeeded=true;
									isInvalid=false;
								}
								
							}else{
								
								//handling the double tag value position
								if(value instanceof Integer){
									
									dVal=(Integer)value;
									
								}else{
									
									dVal=(Double)value;
								}

								y=(int)Math.round((((availableHeight-
										(dVal-minValue)*availableHeight/delta-verticalZoomOrigin)*
											verticalZoomFactor)+topOffset));
								
								succeeded=true;
								isInvalid=false;
							}
						}
						
						if(! succeeded){
							
							//the curve is in an invalid state
							y=availableHeight/2+topOffset;
							isInvalid=true;
						}

						if(useInterpolation && interpolationWithPeriod){
							
							if(previousTime>0 && time-previousTime>=period){

								//if the time delta between the current point and the previous 
								//point is higher than the refresh period, then an interpolation line is drawn
								addInterpolation(paths, pX, pY, x, pY, isPreviousInvalid);
								pX=x;
							}
							
							if(i==size-1 && endDate-time>period){

								//if the time delta between the current point and the end date
								//point is higher than the refresh period, then an interpolation line is drawn								
								addInterpolation(paths, x, y, availableWidth+leftOffset, y, isInvalid);
								pX=x;
							}
						}
						
						//adding the point to the general paths
						addPoint(paths, x, y, isInvalid);

						if(useInterpolation && pX!=Integer.MAX_VALUE && pY!=Integer.MAX_VALUE){
							
							addInterpolation(paths, pX, pY, x, y, isPreviousInvalid);
						}
						
						pX=x;
						pY=y;
						isPreviousInvalid=isInvalid;
						previousTime=time;
						i++;
					}
				}
			}
		}
	}
	
	/**
	 * paints the points of the curve with the given zoom factors
	 * @param g a graphics object
	 * @param bounds the bounds for painting the curves
	 */
	public void paintCurve(Graphics2D g, Rectangle bounds){

		g=(Graphics2D)g.create();
		g.setClip(bounds);
		
		if(paths!=null){
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_OFF);

			//drawing the points
			g.setColor(config.getColor());
			g.draw(paths[0]);
			g.fill(paths[1]);

			g.setColor(config.getColorForInvalid());
			g.draw(paths[3]);
			g.fill(paths[4]);

			//interpolating
			if(useInterpolation){

				Graphics2D g2=(Graphics2D)g.create();

				if(config.getStroke()!=null){
					
					g2.setStroke(config.getStroke());
				}

				g2.setColor(config.getColor());
				g2.draw(paths[2]);
				g2.dispose();
				g2=(Graphics2D)g.create();

				if(config.getStrokeForInvalid()!=null){
					
					g2.setStroke(config.getStrokeForInvalid());
				}

				g2.setColor(config.getColorForInvalid());
				g2.draw(paths[5]);
				g2.dispose();
			}	
		}

		g.dispose();
	}
	
	/**
	 * draws the interpolation curve between the given two points
	 * @param currentPaths the array of the paths
	 * @param x1 the absciss of the first point to paint
	 * @param y1 the ordinate of the first point to paint
	 * @param x2 the absciss of the second point to paint
	 * @param y2 the ordinate of the second point to paint
	 * @param isInvalid whether the first given point is invalid
	 */
	protected void addInterpolation(GeneralPath[] currentPaths, 
															int x1, int y1, int x2, int y2, boolean isInvalid){

		//getting the path for the interpolation
		GeneralPath path=isInvalid?currentPaths[5]:currentPaths[2];
		
		//the type of the interpolation
		String interpolationType=config.getInterpolation();
		
		if(interpolationType.equals("square")){
			
			path.moveTo(x1, y1);
			path.lineTo(x2, y1);
			path.lineTo(x2, y2);
			
		}else if(interpolationType.equals("triangle")){
			
			path.moveTo(x1, y1);
			path.lineTo(x2, y2);
		}
	}
	
	/**
	 * adds a point to the general path at the given location
	 * @param currentPaths the array of the paths
	 * @param x the absciss of the location point
	 * @param y the ordinate of the location point
	 * @param isInvalid whether the given point is invalid
	 */
	protected void addPoint(GeneralPath[] currentPaths, int x, int y, boolean isInvalid){

		//getting the general paths
		GeneralPath drawnPath=isInvalid?currentPaths[3]:currentPaths[0];
		GeneralPath filledPath=isInvalid?currentPaths[4]:currentPaths[1];
		
		//getting the name of the shape
		String shapeName=config.getPoint();
		
		if(shapeName.equals("plus")){
			
			drawnPath.moveTo(x, y-pointShapeSize.height/2);
			drawnPath.lineTo(x, y+pointShapeSize.height/2);

			drawnPath.moveTo(x-pointShapeSize.width/2, y);
			drawnPath.lineTo(x+pointShapeSize.width/2, y);

		}else if(shapeName.equals("cross")){
			
			drawnPath.moveTo(x-pointShapeSize.width/2, y-pointShapeSize.height/2);
			drawnPath.lineTo(x+pointShapeSize.width/2, y+pointShapeSize.height/2);
			
			drawnPath.moveTo(x+pointShapeSize.width/2, y-pointShapeSize.height/2);
			drawnPath.lineTo(x-pointShapeSize.width/2, y+pointShapeSize.width/2);

		}else if(shapeName.equals("disc")){
			
			ellipsePoint.x=x-pointShapeSize.width/2;
			ellipsePoint.y=y-pointShapeSize.height/2;
			ellipsePoint.width=pointShapeSize.width;
			ellipsePoint.height=pointShapeSize.height;
			
			filledPath.append(ellipsePoint, false);

		}else if(shapeName.equals("circle")){

			ellipsePoint.x=x-pointShapeSize.width/2;
			ellipsePoint.y=y-pointShapeSize.height/2;
			ellipsePoint.width=pointShapeSize.width;
			ellipsePoint.height=pointShapeSize.height;
			
			drawnPath.append(ellipsePoint, false);
			
		}else if(shapeName.equals("square")){
			
			filledPath.moveTo(x-pointShapeSize.width/2, y-pointShapeSize.height/2);
			filledPath.lineTo(x+pointShapeSize.width/2, y-pointShapeSize.height/2);
			filledPath.lineTo(x+pointShapeSize.width/2, y+pointShapeSize.height/2);
			filledPath.lineTo(x-pointShapeSize.width/2, y+pointShapeSize.height/2);
			filledPath.lineTo(x-pointShapeSize.width/2, y-pointShapeSize.height/2);
			
		}else if(shapeName.equals("rect")){
			
			drawnPath.moveTo(x-pointShapeSize.width/2, y-pointShapeSize.height/2);
			drawnPath.lineTo(x+pointShapeSize.width/2, y-pointShapeSize.height/2);
			drawnPath.lineTo(x+pointShapeSize.width/2, y+pointShapeSize.height/2);
			drawnPath.lineTo(x-pointShapeSize.width/2, y+pointShapeSize.height/2);
			drawnPath.lineTo(x-pointShapeSize.width/2, y-pointShapeSize.height/2);
			
		}else if(shapeName.equals("filledTriangle") || shapeName.equals("triangle")){

			int[] xPoints={x, x+pointShapeSize.width/2, x-pointShapeSize.width/2};
			int[] yPoints={y-pointShapeSize.height/2, y+pointShapeSize.height/2, 
									y+pointShapeSize.height/2};
			
			GeneralPath path=shapeName.equals("filledTriangle")?filledPath:drawnPath;
			
			path.moveTo(xPoints[0], yPoints[0]);
			path.lineTo(xPoints[1], yPoints[1]);
			path.lineTo(xPoints[2], yPoints[2]);
			path.lineTo(xPoints[0], yPoints[0]);
		}
	}
}
