package fr.itris.glips.extension.jwidget.trends.runtime.view.component;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.painters.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * the panel into which the curves are drawn
 * @author ITRIS, Jordi SUC
 */
public class CurvesComponent extends JPanel{
	
	/**
	 * the top offset in pixels
	 */
	private static int topOffset=10;
	
	/**
	 * the bottom offset
	 */
	private static int bottomOffset=25;
	
	/**
	 * the left offset
	 */
	private int leftOffset=0;
	
	/**
	 * the components handler
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the curve painters manager
	 */
	private CurvePaintersManager curvePaintersManager;
	
	/**
	 * the divisions painter
	 */
	private DivisionPainter divisionsPainter;
	
	/**
	 * the scales painter
	 */
	private ScalesPainter scalesPainter;
	
	/**
	 * the offscreen images
	 */
	private BufferedImage offscreenImage;
	
	/**
	 * the set of the painters used to paint shapes on the component
	 */
	private Set<AnyPainter> anyPainters=new HashSet<AnyPainter>();

	/**
	 * the constructor of the class
	 * @param componentsHandler  	the object used to handle all the updates, actions and 
	 * 													modifications that can occur on one of the components
	 */
	public CurvesComponent(ComponentsHandler componentsHandler){
		
		this.componentsHandler=componentsHandler;
	}
	
	/**
	 * initializes the component
	 */
	public void initialize(){
		
		//getting the configuration object
		TrendsConfiguration configuration=componentsHandler.getView().
																	getController().getConfiguration();
		
		//setting the background
		setBackground(configuration.getBackgroundColor());
		
		//creating the curve painters manager
		curvePaintersManager=new CurvePaintersManager(this);
		
		//creating the scales painter
		scalesPainter=new ScalesPainter(this);
		
		//creating the divisions painter
		if(configuration.displayHorizontalAxisTimeGraduations() || configuration.displayGrid()){
			
			divisionsPainter=new DivisionPainter(this);
			divisionsPainter.initialize();
			
		}else{
			
			//computing the bottom offset
			bottomOffset=0;
		}
	}
	
	/**
	 * disposes the object
	 */
	public void dispose(){//TODO
		
		anyPainters.clear();
		
		
		componentsHandler=null;
		curvePaintersManager=null;
		divisionsPainter=null;
		scalesPainter=null;
		offscreenImage=null;
		anyPainters.clear();
	}
	
	/**
	 * adds a new painter
	 * @param painter a painter
	 */
	public void addAnyPainter(AnyPainter painter){
		
		if(painter!=null){
			
			anyPainters.add(painter);
		}
	}
	
	/**
	 * removes a painter
	 * @param painter a painter
	 */
	public void removeAnyPainter(AnyPainter painter){
		
		if(painter!=null){
			
			anyPainters.remove(painter);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		if(offscreenImage!=null){
			
			//painting the curves
			((Graphics2D)g).drawRenderedImage(offscreenImage, new AffineTransform());
		}
		
		//painting the left items
		if(anyPainters.size()>0){

			//painting
			for(AnyPainter painter : anyPainters){
				
				painter.paint((Graphics2D)g);
			}
		}
	}
	
	/**
	 * initiates the update
	 */
	public void initiateUpdate(){
		
		int availableHeight=getHeight()-2*topOffset-bottomOffset;
		
		if(availableHeight>0){
			
			//getting the whole width of the scales
			scalesPainter.initiateUpdate((Graphics2D)getGraphics(), availableHeight);
			leftOffset=scalesPainter.getWholeScalesWidth();
		}
	}
	
	/**
	 * update the tag values for the give tag name
	 * @param tagName the name of a tag
	 * @param tagValues the map associating a time value to a tag value
	 * @param startDate the start date of the tag values
	 * @param endDate the end date of the tag values
	 */
	public void updateValues(String tagName, TreeMap<Long, Object> tagValues, 
												long startDate, long endDate){

		int availableWidth=getWidth()-leftOffset-ComponentsHandler.rightOffset;
		int availableHeight=getHeight()-2*topOffset-bottomOffset;
		
		if(availableWidth>0 && availableHeight>0){
			
			curvePaintersManager.updateValues(tagName, tagValues, startDate, endDate, leftOffset,
					ComponentsHandler.rightOffset, topOffset, availableWidth, availableHeight);
		}
	}
	
	/**
	 * validates all the updates that have been done, 
	 * so that the curves panel can be refreshed
	 * @param startDate the last time the update happened
	 * @param endDate the date for the right part
	 */
	public void validateUpdates(long startDate, final long endDate){

		int availableWidth=getWidth()-leftOffset-ComponentsHandler.rightOffset;
		int availableHeight=getHeight()-2*topOffset-bottomOffset;

		if(availableWidth>0 && availableHeight>0){
			
			//getting the configuration object
			TrendsConfiguration configuration=componentsHandler.getView().
																		getController().getConfiguration();

			if(offscreenImage==null){
				
				//creating the offscreen image
				offscreenImage=new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			}
			
			//clearing the image
			Graphics2D g=offscreenImage.createGraphics();
			g.setColor(getBackground());
			g.fillRect(0, 0, offscreenImage.getWidth(), offscreenImage.getHeight());

			//validating the divisions painter
			if(divisionsPainter!=null){
				
				divisionsPainter.validateUpdates(endDate, availableWidth);

				//drawing the vertical grid lines portion
				if(configuration.displayGrid()){
					
					divisionsPainter.paintGridVerticalLines(
							g, startDate, endDate, leftOffset, availableWidth, getHeight()-bottomOffset);
				}

				//drawing the new graduations portion
				if(configuration.displayHorizontalAxisHorodates()){

					divisionsPainter.paintGraduations(g, startDate, endDate, getHeight()-bottomOffset, 
																			leftOffset, availableWidth, bottomOffset);
				}
			}

			//drawing the curves
			Rectangle clip=getCurvesZoneBounds();
			clip.width+=ComponentsHandler.rightOffset;
			clip.height+=2*topOffset;
			clip.y=0;
			
			curvePaintersManager.validateUpdates(g, clip);

			//drawing the scales
			scalesPainter.validateUpdates(g, topOffset, availableHeight, availableWidth, leftOffset);
			g.dispose();
			
			//repainting the panel
			repaint();
		}
	}
	
	/**
	 * @return the bounds of the zone into which the curves are painted
	 */
	public Rectangle getCurvesZoneBounds(){
		
		return new Rectangle(
				leftOffset, topOffset, getWidth()-leftOffset-ComponentsHandler.rightOffset, 
				getHeight()-2*topOffset-bottomOffset);
	}
	
	/**
	 * @return the object used to handle all the updates, actions and 
	 * 				modifications that can occur on one of the components
	 */
	public ComponentsHandler getComponentsHandler() {
		return componentsHandler;
	}

	/**
	 * @return the curve painters manager
	 */
	public CurvePaintersManager getCurvePaintersManager() {
		return curvePaintersManager;
	}
}
