package fr.itris.glips.extension.jwidget.trends.runtime.view.component.painters;

import java.awt.*;
import java.text.*;
import java.util.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;

/**
 * the class used to compute the division and subdivision 
 * values for the graduations and the grid
 * @author ITRIS, Jordi SUC
 */
public class DivisionPainter {
	
	/**
	 * the division periods that could be possibly used
	 */
	public static long[] possibleDivisionPeriods={	1L, 5L, 10L, 100L, 250L, 500L, //ms
																					1000L, 5000L, 10000L, //sec
																					60000L, 300000L, 600000L, //min
																					3600000L, 7200000L, 21600000L, //hour
																					86400000L, 1296000000L, //day
																					2592000000L, 7776000000L, 15552000000L, //month
																					31536000000L, 157680000000L, 315360000000L,
																					1576800000000L, 3153600000000L //year
																				};
	
	/**
	 * the date formatter
	 */
	private SimpleDateFormat dateFormat;
	
	/**
	 * a date
	 */
	private Date date=new Date();

	/**
	 * the components handler
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the configuration object
	 */
	private TrendsConfiguration configuration;
	
	/**
	 * the graduation lines height
	 */
	private int graduationDivisionMinHeight=2;
	
	/**
	 * the distance between each vertical grid division lines
	 */
	private int verticalGridDivisionLinesMinDistance=50;
	
	/**
	 * the distance between each vertical grid subdivision lines
	 */
	private int verticalGridSubDivisionLinesMinDistance=10;
	
	/**
	 * the horodates string width
	 */
	private int horodatesWidth=0;
	
	/**
	 * the vertical position of the horodates
	 */
	private int horodateVerticalPosition=graduationDivisionMinHeight+18;
	
	/**
	 * the array of the divisions
	 */
	private long[] divisions;
	
	/**
	 * the distance between two vertical grid division lines in milliseconds
	 */
	private long verticalGridLinesDivisionDuration=0;
	
	/**
	 * the stroke for the vertical grid division lines
	 */
	private Stroke verticalGridLinesDivisionStroke;
	
	/**
	 * the distance between two vertical grid subdivision lines in milliseconds
	 */
	private long verticalGridLinesSubDivisionDuration=0;
	
	/**
	 * the stroke for the vertical grid subdivision lines
	 */
	private Stroke verticalGridLinesSubDivisionStroke;

	/**
	 * the constructor of the class
	 * @param curvesComponent the curves component
	 */
	public DivisionPainter(CurvesComponent curvesComponent){
		
		this.componentsHandler=curvesComponent.getComponentsHandler();
	}
	
	/**
	 * initializes the painter
	 */
	public void initialize(){
		
		//getting the configuration object
		configuration=componentsHandler.getView().
										getController().getConfiguration();
		
		//handling the graduations
		if(configuration.displayHorizontalAxisTimeGraduations() &&
				configuration.displayHorizontalAxisHorodates()){
			
			//creating the date format
			dateFormat=new SimpleDateFormat(configuration.getHorizontalAxisHorodatesFormat());
			
			//computing the average width for drawing the horodates//
			
			//creating a date
			Date currentDate=new Date(System.currentTimeMillis());
			
			//getting the string representation of this date
			String dateString=dateFormat.format(currentDate);
			
			if(dateString!=null){
				
				//computing the string width with the current font
				horodatesWidth=componentsHandler.getTrendsComponent().getGraphics().
					getFontMetrics(configuration.getFont()).stringWidth(dateString);
			}
		}
		
		//handling the vertical grid lines
		if(configuration.displayGrid()){
			
			//getting the stroke of the vertical grid division lines
			float[] dashArray=TrendsCurveConfiguration.getDashArray(
										configuration.getVerticalLinesDivisionDash());
			
			if(dashArray!=null){
				
				verticalGridLinesDivisionStroke=
						new BasicStroke(1, BasicStroke.CAP_BUTT, 
								BasicStroke.JOIN_BEVEL, 1.0f, dashArray, 0);
			}
			
			//getting the stroke of the vertical grid subdivision lines
			dashArray=TrendsCurveConfiguration.getDashArray(
									configuration.getVerticalLinesSubDivisionDash());
			
			if(dashArray!=null){
				
				verticalGridLinesSubDivisionStroke=
						new BasicStroke(1, BasicStroke.CAP_BUTT, 
								BasicStroke.JOIN_BEVEL, 1.0f, dashArray, 0);
			}
			
			//if the grid division and subdivision lines should not be handled automatically,
			//the value of their periods will be retrieved
			if(! configuration.isVerticalLinesDivisionAutomaticPeriod()){
				
				verticalGridLinesDivisionDuration=configuration.getVerticalLinesDivisionDuration();
			}
			
			if(! configuration.isVerticalLinesSubDivisionAutomaticPeriod()){
				
				verticalGridLinesSubDivisionDuration=configuration.getVerticalLinesSubDivisionDuration();
			}
		}
	}
	
	/**
	 * validates all the updates that have been done, 
	 * so that the divisions can be refreshed
	 * @param endDate the date for the right part
	 * @param availableWidth the available width for painting the graduations 
	 * 										and the grid vertical lines
	 */
	public void validateUpdates(long endDate, int availableWidth){
		
		if(configuration.displayHorizontalAxisTimeGraduations() || 
				configuration.displayGrid() && 
					(configuration.isVerticalLinesDivisionAutomaticPeriod() || 
							configuration.isVerticalLinesSubDivisionAutomaticPeriod())){
			
			//getting the array of the division periods
			divisions=getDivisionPeriods(availableWidth, configuration.getHorizontalAxisDuration());
			
			if(divisions!=null && divisions.length>0){

				if(configuration.isVerticalLinesDivisionAutomaticPeriod()){
					
					//getting the period of the grid vertical division lines
					long divisionTimeLength=verticalGridDivisionLinesMinDistance*
									configuration.getHorizontalAxisDuration()/availableWidth;

					//getting the corresponding horodate division
					for(int i=0; i<divisions.length; i++){
				
						if(divisions[i]>=divisionTimeLength){
							
							verticalGridLinesDivisionDuration=divisions[i];
							break;
						}
					}
				}

				if(configuration.isVerticalLinesSubDivisionAutomaticPeriod()){
					
					//getting the period of the grid vertical subdivision lines
					long subdivisionTimeLength=verticalGridSubDivisionLinesMinDistance*
									configuration.getHorizontalAxisDuration()/availableWidth;

					//getting the corresponding horodate division
					for(int i=0; i<divisions.length; i++){
						
						if(divisions[i]>=subdivisionTimeLength){
							
							verticalGridLinesSubDivisionDuration=divisions[i];
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * paints the graduations
	 * @param g a graphics object
	 * @param startDate the last time the update happened
	 * @param endDate the date for the right part
	 * @param topOffset the top offset
	 * @param leftOffset the left offset
	 * @param availableWidth the available width for painting the graduations
	 * @param availableHeight the available height
	 */
	public void paintGraduations(Graphics2D g, long startDate, long endDate, int topOffset, 
													int leftOffset, int availableWidth, int availableHeight){
		
		if(divisions!=null){

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g.setFont(configuration.getFont());
			g.setColor(Color.white);
			
			//clearing the zone
			g.fillRect(leftOffset, topOffset, availableWidth+ComponentsHandler.rightOffset, availableHeight);
			
			//getting the period division that includes the horodates length//
			long horodateDivision=-1;
			
			if(horodatesWidth>0){
				
				//computing the time on the curves component that the horodate string should take
				long horodatesTimeLength=horodatesWidth*
								configuration.getHorizontalAxisDuration()/availableWidth;

				//getting the corresponding horodate division
				for(int i=0; i<divisions.length; i++){
					
					if(divisions[i]>(horodatesTimeLength+divisions[i]/4)){
						
						horodateDivision=divisions[i];
						break;
					}
				}
			}

			//getting the closest higher date of the current date corresponding to a division
			long closestHigherDate=(long)Math.floor(endDate/divisions[0]+1)*divisions[0];
			
			//painting each item
			int position=0;
			String dateString="";
			int stringWidth=0;
			long currentDivision=0;
			
			for(long time=closestHigherDate; ; time-=divisions[0]){

				//computing the position of the item
				position=(int)Math.floor(availableWidth-(endDate-time)*availableWidth/
									configuration.getHorizontalAxisDuration())+leftOffset;
				
				if(position>=leftOffset && position<=(availableWidth+leftOffset)){
					
					//drawing the line
					for(int i=divisions.length-1; i>=0; i--){
						
						if((time%divisions[i])==0){
							
							currentDivision=divisions[i];
							g.setColor((divisions[i]%horodateDivision==0)?Color.black:Color.darkGray);
							g.drawLine(position, topOffset, position, topOffset+graduationDivisionMinHeight+i*2);
							break;
						}
					}
					
					//drawing the horodate 
					if(configuration.displayHorizontalAxisHorodates() && horodateDivision>0 && 
							(currentDivision%horodateDivision==0)){

						//computing the time string representation
						date.setTime(time);
						dateString=dateFormat.format(date);
						
						if(dateString!=null && ! dateString.equals("")){
							
							//computing the string width
							stringWidth=g.getFontMetrics().stringWidth(dateString);
							
							if(stringWidth>0){
								
								//drawing the string
								g.drawString(dateString, position-stringWidth/2, topOffset+horodateVerticalPosition);
							}
						}
					}
					
				}else if(position<leftOffset){
					
					break;
				}
			}
		}
	}
	
	/**
	 * paints the grid vertical lines
	 * @param g a graphics object
	 * @param startDate the date from which the lines should be painted
	 * @param endDate the date for the right part
	 * @param leftOffset the left offset
	 * @param availableWidth the available width for painting the vertical grid lines
	 * @param availableHeight the available height for painting the vertical grid lines
	 */
	public void paintGridVerticalLines(Graphics2D g, long startDate, long endDate, int leftOffset,
															int availableWidth, int availableHeight){

		if(divisions!=null){
		
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			Graphics2D g2=(Graphics2D)g.create();
			
			//painting the divisions//
			
			//setting the properties of the graphics object
			g2.setColor(configuration.getVerticalLinesDivisionColor());
			
			if(verticalGridLinesDivisionStroke!=null){
				
				g2.setStroke(verticalGridLinesDivisionStroke);
			}
			
			//getting the closest higher date of the current date corresponding to a division
			long closestHigherDate=
				(long)Math.floor(endDate/verticalGridLinesDivisionDuration)*
							verticalGridLinesDivisionDuration;
			
			//painting each vertical division line
			int position=0;
	
			for(long time=closestHigherDate; ; time-=verticalGridLinesDivisionDuration){

				//computing the position of the item
				position=(int)Math.floor(availableWidth-(endDate-time)*availableWidth/
						configuration.getHorizontalAxisDuration())+leftOffset;
				
				if(position>=leftOffset && 
						position<=(availableWidth+leftOffset)){
					
					//drawing the line
					g2.drawLine(position, 0, position, availableHeight);
					
				}else if(position<leftOffset){
					
					break;
				}
			}
			
			g2.dispose();
			
			//painting the sub divisions//
			g2=(Graphics2D)g.create();
			
			//setting the properties of the graphics object
			g2.setColor(configuration.getVerticalLinesSubDivisionColor());
			
			if(verticalGridLinesSubDivisionStroke!=null){
				
				g2.setStroke(verticalGridLinesSubDivisionStroke);
			}
			
			//getting the closest higher date of the current date corresponding to a subdivision
			closestHigherDate=
				(long)Math.floor(endDate/verticalGridLinesSubDivisionDuration)*
							verticalGridLinesSubDivisionDuration;
			
			//painting each vertical sub division line
			for(long time=closestHigherDate; ; 
					time-=verticalGridLinesSubDivisionDuration){
				
				//computing the position of the item
				position=(int)Math.floor(availableWidth-(endDate-time)*availableWidth/
						configuration.getHorizontalAxisDuration())+leftOffset;

				if(position>=leftOffset && position<=(availableWidth+leftOffset)){
					
					if((time%verticalGridLinesDivisionDuration)!=0){
						
						//drawing the line
						g2.drawLine(position, 0, position, availableHeight);
					}

				}else if(position<leftOffset){
					
					break;
				}
			}
			
			g2.dispose();
		}
	}
	
	/**
	 * returns the array of the period for each kind of divisions
	 * @param availableWidth the available width for painting the graduations and the grid vertical lines
	 * @param duration a time
	 * @return the array of the period for each kind of divisions
	 */
	protected long[] getDivisionPeriods(int availableWidth, long duration){
		
		//the array of the division periods
		long[] currentDivisions=null;
		
		if(duration>0){
			
			//computing the width/time ratio
			double ratio=((double)availableWidth)/(double)duration;
			
			//getting the first period of the table that could fit the given duration
			int periodWidth=0;
			int firstPeriodIndex=-1;
			
			for(int index=0; index<possibleDivisionPeriods.length; index++){
				
				periodWidth=(int)Math.floor(possibleDivisionPeriods[index]*ratio);
				
				if(periodWidth>=4){
					
					//storing the first index of the periods array 
					//matching the accurate period
					firstPeriodIndex=index;
					break;
				}
			}
			
			if(firstPeriodIndex!=-1 && firstPeriodIndex<possibleDivisionPeriods.length){
				
				//filling the array of the division periods that will be used
				currentDivisions=new long[5];
				
				for(	int i=firstPeriodIndex; 
						i<possibleDivisionPeriods.length 
							&& (i-firstPeriodIndex)<currentDivisions.length; 
						i++){
					
					currentDivisions[i-firstPeriodIndex]=possibleDivisionPeriods[i];
				}
			}
		}
		
		return currentDivisions;
	}
}