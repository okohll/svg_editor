package fr.itris.glips.extension.jwidget.trends.runtime.view.component.painters;

import java.awt.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import java.util.*;

/**
 * the painter of the scales of the curves
 * @author ITRIS, Jordi SUC
 */
public class ScalesPainter {

	/**
	 * the max string value width
	 */
	private int maxStringValueWidth=100;
	
	/**
	 * the curves component
	 */
	private CurvesComponent curvesComponent;
	
	/**
	 * the size of the smallest graduation
	 */
	private int graduationSize=2;
	
	/**
	 * the array of the scale width of each curve
	 */
	private int[] curvesScaleWidth;
	
	/**
	 * the curves graduations map
	 */
	private Map<TrendsCurveConfiguration, double[]> curvesGraduations=
										new HashMap<TrendsCurveConfiguration, double[]>();
	
	/**
	 * the whole scale width
	 */
	private int wholeScalesWidth=0;
	
	/**
	 * the constructor of the class
	 * @param curvesComponent the curves component
	 */
	public ScalesPainter(CurvesComponent curvesComponent){
		
		this.curvesComponent=curvesComponent;
		curvesScaleWidth=new int[curvesComponent.getComponentsHandler().
		                             getView().getController().getConfiguration().
		                             		getCurvesConfigurationList().size()];
	}
	
	/**
	 * initiates the update
	 * @param g a graphics object
	 * @param availableHeight the available height for drawing the curve
	 */
	public void initiateUpdate(Graphics2D g, int availableHeight){

		wholeScalesWidth=0;
		
		if(g!=null){
			
			int currentWholeScalesWidth=0;
			
			//clearing the curves graduations map
			curvesGraduations.clear();
			
			//updating the array of the scale width//
			ComponentsHandler componentsHandler=curvesComponent.getComponentsHandler();
			LinkedList<TrendsCurveConfiguration> configs=curvesComponent.getComponentsHandler().
											getView().getController().getConfiguration().getCurvesConfigurationList();
			int i=0, maxStringWidth, scaleWidth;
			double minValue, maxValue, delta;
			String valueStr;
			double verticalZoomFactor;
			double[] graduations;
			int width=0;
			
			for(TrendsCurveConfiguration config : configs){
				
				if(config.displayScale()){
					
					//getting the min and the max values for the curve
					minValue=config.getMinValue();
					maxValue=config.getMaxValue();
					
					//getting the zoom factor
					verticalZoomFactor=config.getConfiguration().getVerticalZoomFactor();
					
					if(config.isEnumeratedTag()){

						//getting the array of the graduations
						graduations=getGraduations(minValue, maxValue, availableHeight, 
														(int)(availableHeight*verticalZoomFactor), true);

						if(graduations.length>0){
							
							//adding the graduations array to the map
							curvesGraduations.put(config, graduations);
							
							maxStringWidth=0;
							
							//getting the maximum size of the strings that will be displayed
							for(String val : config.getEnumeratedTagValues()){

								width=(int)g.getFontMetrics().getStringBounds(val, g).getWidth();
								
								if(maxStringWidth<width){
									
									maxStringWidth=width;
								}
							}
							
							if(maxStringWidth>maxStringValueWidth){
								
								maxStringWidth=maxStringValueWidth;
							}
							
							//computing the width of this scale
							scaleWidth=maxStringWidth+graduations.length*graduationSize+2+5;
							curvesScaleWidth[i]=scaleWidth;
							currentWholeScalesWidth+=scaleWidth;
						}
						
					}else{

						//getting the array of the graduations
						graduations=getGraduations(minValue, maxValue, availableHeight, 
														(int)(availableHeight*verticalZoomFactor), false);

						if(graduations.length>0){
							
							//adding the graduations array to the map
							curvesGraduations.put(config, graduations);
							
							//setting the properties for the graphics object
							Font font=config.getConfiguration().getFont();

							g.setColor(config.getConfiguration().getBackgroundColor());
							g.setFont(font);
							
							//getting the delta between the min and the max
							delta=maxValue-minValue;
							
							//getting the width of the string representation of the value 
							//corresponding to the first graduations item
							maxStringWidth=0;
							double[] values={(int)(minValue+delta/2)+graduations[graduations.length-1],
															maxValue, minValue, (int)(minValue+delta/2)};
							valueStr=componentsHandler.getStringRepresentation(maxValue);
							
							//getting the maximum size of the strings that will be displayed
							maxStringWidth=g.getFontMetrics().stringWidth(valueStr);
							
							for(double val : values){
								
								valueStr=componentsHandler.getStringRepresentation(val);
								width=(int)g.getFontMetrics().getStringBounds(valueStr, g).getWidth();
								
								if(maxStringWidth<width){
									
									maxStringWidth=width;
								}
							}
							
							if(maxStringWidth>maxStringValueWidth){
								
								maxStringWidth=maxStringValueWidth;
							}

							//computing the width of this scale
							scaleWidth=maxStringWidth+graduations.length*graduationSize+2;
							curvesScaleWidth[i]=scaleWidth;
							currentWholeScalesWidth+=scaleWidth;
						}
					}
				}
				
				i++;
			}
			
			wholeScalesWidth=currentWholeScalesWidth;
		}
	}

	/**
	 * @return the whole scales width
	 */
	public int getWholeScalesWidth() {
		return wholeScalesWidth;
	}

	/**
	 * paints the scale of each curve
	 * @param g a graphics object
	 * @param topOffset the top offset of the component of the curves
	 * @param availableHeight the available height for drawing the curve
	 * @param gridAvailableWidth the available width for drawing the grid
	 * @param gridLeftOffset the left offset for drawing the grid
	 */
	public void validateUpdates(Graphics2D g, int topOffset, int availableHeight, 
													int gridAvailableWidth, int gridLeftOffset){
		
		//painting the scale of each curve//
		LinkedList<TrendsCurveConfiguration> configs=curvesComponent.getComponentsHandler().
		getView().getController().getConfiguration().getCurvesConfigurationList();

		//getting the number of curves that display a scale
		int curveScaleNb=0;
		
		for(TrendsCurveConfiguration config : configs){
			
			if(config.displayScale()){
				
				curveScaleNb++;
			}
		}
		
		//painting the scale and the horizontal grid lines for the last curve
		int currentXPosition=0;
		int i=0;
		int paintedScaleNumber=0;
		
		for(TrendsCurveConfiguration config : configs){
			
			if(config.displayScale()){
				
				//painting the scale of the curve
				paintScale(config, g, topOffset, availableHeight, currentXPosition, 
									curvesScaleWidth[i], curvesGraduations.get(config), 
									((paintedScaleNumber==(curveScaleNb-1)) && 
									config.getConfiguration().displayGrid()), 
									gridAvailableWidth, gridLeftOffset);
				
				currentXPosition+=curvesScaleWidth[i];
				paintedScaleNumber++;
			}
			
			i++;
		}
	}
	
	/**
	 * paints the scale of the curve denoted by the given curve
	 * @param config a curve configuration object
	 * @param g a graphics object
	 * @param topOffset the top offset of the component of the curves
	 * @param availableHeight the available height for drawing the curve
	 * @param xPosition the position from which the scale should be painted
	 * @param scaleWidth the width of this scale
	 * @param graduations the array of the graduations
	 * @param paintHorizontalGridLines whether or not to paint the horizontal grid lines
	 * @param gridAvailableWidth the available width for drawing the grid
	 * @param gridLeftOffset the left offset for drawing the grid
	 */
	protected void paintScale(TrendsCurveConfiguration config, Graphics2D g, 
												 int topOffset, int availableHeight, int xPosition, 
												 int scaleWidth, double[] graduations, 
												 boolean paintHorizontalGridLines, 
												 int gridAvailableWidth, int gridLeftOffset){

		if(graduations!=null && graduations.length>0){
			
			//getting the higher graduation item
			double lastGraduationItemValue=graduations[graduations.length-1];

			//getting the multiplier factor for the graduation items
			int factor=1;
			
			if(lastGraduationItemValue<1){
				
				factor=(int)Math.pow(10, Math.floor(-Math.log10(lastGraduationItemValue)+1));
			}

			//copying the graduations array
			double[] normalizedGraduations=new double[graduations.length];
			
			for(int i=0; i<normalizedGraduations.length; i++){
				
				normalizedGraduations[i]=graduations[i]*factor;
			}
			
			lastGraduationItemValue=normalizedGraduations[normalizedGraduations.length-1];
			
			//getting the min and the max values for the curve
			double minValue=config.getMinValue()*factor;
			double maxValue=config.getMaxValue()*factor;
			
			//computing the max string width of this scale
			int maxStringWidth=scaleWidth-normalizedGraduations.length*graduationSize-2;
			
			//getting the components handler
			ComponentsHandler componentsHandler=curvesComponent.getComponentsHandler();
			
			//getting the delta between the min and the max
			double delta=maxValue-minValue;
			
			//getting the zoom factor
			double verticalZoomFactor=config.getConfiguration().getVerticalZoomFactor();
			
			//getting the index in the graduations panel after which no grid line should be drawn
			int gridGraduationsIndex=0;
			int itemHeight=0;
			
			for(int i=0; i<normalizedGraduations.length; i++){
				
				itemHeight=(int)Math.floor((availableHeight*verticalZoomFactor)*normalizedGraduations[i]/delta);
				
				if(itemHeight>=availableHeight || itemHeight>=20){
					
					gridGraduationsIndex++;
					
				}else{
					
					break;
				}
			}

			//setting the properties for the graphics object
			g.setFont(config.getConfiguration().getFont());
			g.setColor(config.getConfiguration().getBackgroundColor());

			//painting the scale//
			
			//filling the background
			g.fillRect(xPosition, 0, scaleWidth, availableHeight+2*topOffset);
			
			//setting the color for drawing the scales
			g.setColor(config.getColor());
			
			//drawing the righter line
			g.drawLine(xPosition+scaleWidth-1, topOffset, xPosition+scaleWidth-1, availableHeight+topOffset);
			
			//drawing the graduations//

			//getting the height of a string
			int stringHeight=g.getFontMetrics().getHeight();
			
			//getting the transparent color for painting the grid line
			Color gridColor=null;
			
			if(paintHorizontalGridLines){
				
				Color color=config.getColor();
				gridColor=new Color(color.getRed(), color.getGreen(), color.getBlue(), 150);
			}
			
			//getting the higher graduation item value
			double higherGraduationItemValue=
				((int)(maxValue/lastGraduationItemValue))*lastGraduationItemValue;
			
			//getting the zoom origin
			double verticalZoomOrigin=config.getConfiguration().getVerticalZoomOrigin();

			int x=0, y=0, x2=0;
			int itemIndex=0;
			String strValue="";
			int width=0;
			double value=0;
			int count=0;
			String next="...";
			int nextStringWidth=g.getFontMetrics().stringWidth(next);

			for(int index=0; value>=minValue; index--){
				
				value=higherGraduationItemValue+index*lastGraduationItemValue;
				
				if(value<minValue){
					
					break;
				}

				//getting the y position of the item corresponding to this value
				y=(int)Math.round(((higherGraduationItemValue-value)*
							availableHeight/delta-verticalZoomOrigin)*verticalZoomFactor)+topOffset;

				if(y>=topOffset && y<=topOffset+availableHeight+1){
					
					if(config.isEnumeratedTag()){
						
						strValue=config.getEnumeratedTagValues().get(config.getEnumeratedTagValues().size()-count-1);

						//getting the width of this string
						width=g.getFontMetrics().stringWidth(strValue);

						//drawing the string representation of the value
						g.setColor(config.getColor());

						if(width>=maxStringWidth){
							
							Graphics g2=g.create();
							g2.setClip(xPosition, 0, maxStringWidth-nextStringWidth, 2*topOffset+availableHeight);
							g2.drawString(strValue, xPosition, y+stringHeight/2);
							g2.dispose();
							g.drawString(next, xPosition+maxStringWidth-nextStringWidth, y+stringHeight/2);
							
						}else{

							g.drawString(strValue, xPosition+maxStringWidth-width, y+stringHeight/2);
						}

						if(paintHorizontalGridLines){
							
							//drawing the horizontal grid line
							g.setColor(gridColor);
							g.drawLine(gridLeftOffset, y, 
									gridLeftOffset+gridAvailableWidth+ComponentsHandler.rightOffset, y);
						}
						
					}else{
						
						//getting the index of the type of the graduation item that will be painted
						for(int i=0; i<normalizedGraduations.length; i++){

							if(value%normalizedGraduations[i]==0){
								
								if(i<gridGraduationsIndex){
									
									//getting the string representation of the value
									strValue=componentsHandler.getStringRepresentation(value/factor);

									//getting the width of this string
									width=g.getFontMetrics().stringWidth(strValue);

									//drawing the string representation of the value
									Graphics g2=g.create();
									g2.setClip(xPosition, 0, maxStringWidth, availableHeight+2*topOffset);
									g2.setColor(config.getColor());
									g2.drawString(strValue, 
											xPosition+maxStringWidth-(width>maxStringWidth?maxStringWidth:width), y+stringHeight/2);
									g2.dispose();
									
									if(paintHorizontalGridLines){
										
										//drawing the horizontal grid line
										g.setColor(gridColor);
										g.drawLine(gridLeftOffset, y, 
												gridLeftOffset+gridAvailableWidth+ComponentsHandler.rightOffset, y);
									}
								}

								itemIndex=i;
								break;
							}
						}
					}

					g.setColor(config.getColor());

					//getting the x position of the item
					x=xPosition+maxStringWidth+itemIndex*graduationSize;
					x2=xPosition+maxStringWidth+normalizedGraduations.length*graduationSize;
					
					//painting this item
					g.drawLine(x, y, x2, y);
				}
				
				count++;
			}
		}
	}
	
	/**
	 * returns the graduations array, ie, the array of the values type that 
	 * should be graduated on the scale
	 * @param minValue the min value for the graduation
	 * @param maxValue the max value for the graduation
	 * @param baseHeight the unscaled height
	 * @param availableHeight the available height
	 * @param useOnlyInteger whether the only graduation items that 
	 * 				should be kept are integers
	 * @return the graduations array, ie, the array of the values type that 
	 * 				should be graduated on the scale
	 */
	protected double[] getGraduations(double minValue, double maxValue, 
														int baseHeight, int availableHeight, boolean useOnlyInteger){
		
		double[] dividers={2, 4, 8, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 
				20000, 50000, 100000, 200000, 500000, 1000000};
		
		//getting the delta between the min and the max
		double delta=maxValue-minValue;
		
		//getting the lowest number whose log10 integer part is 
		//the same than the log10 integer part of the delta 
		double baseNumber=Math.pow(10, Math.floor(Math.log10(delta)));

		//getting the height value corresponding to each type of graduation
		int itemHeight=0;
		LinkedList<Double> graduationsList=new LinkedList<Double>();
		double currentNumber=baseNumber;
		int dividerIndex=0;
		
		do{
			
			if(currentNumber==Math.floor(currentNumber) || (! useOnlyInteger && currentNumber<1)){
				
				itemHeight=(int)Math.floor(availableHeight*currentNumber/delta);
				
				if(itemHeight>=4){
					
					if(itemHeight<baseHeight){
						
						graduationsList.add(currentNumber);
					}
					
				}else{
					
					break;
				}
			}

			currentNumber=baseNumber/dividers[dividerIndex];
			dividerIndex++;
			
		}while(graduationsList.size()<6 && dividerIndex<dividers.length);
		
		//computing the array that will be returned
		double[] graduations=new double[graduationsList.size()];

		for(int i=0; i<graduationsList.size(); i++){
			
			graduations[i]=graduationsList.get(i).doubleValue();
		}

		return graduations;
	}
}