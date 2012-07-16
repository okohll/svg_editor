package fr.itris.glips.extension.jwidget.trends.runtime.view.component.painters;

import java.awt.*;
import java.util.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;

/**
 * the manager used to handle curve painters
 * @author ITRIS, Jordi SUC
 */
public class CurvePaintersManager {

	/**
	 * the curves component
	 */
	private CurvesComponent curvesComponent;
	
	/**
	 * the map associating a tag name to a curve painter
	 */
	private HashMap<String, CurvePainter> painters=new HashMap<String, CurvePainter>();
	
	/**
	 * the list of the painters
	 */
	private LinkedList<CurvePainter> paintersList=new LinkedList<CurvePainter>();
	
	/**
	 * the constructor of the class
	 * @param curvesComponent the curves component
	 */
	public CurvePaintersManager(CurvesComponent curvesComponent){
		
		this.curvesComponent=curvesComponent;
		initialize();
	}
	
	/**
	 * initializes the object
	 */
	protected void initialize(){
		
		//getting the list of the configuration curve objects
		LinkedList<TrendsCurveConfiguration> configs=
			curvesComponent.getComponentsHandler().getView().getController().
					getConfiguration().getCurvesConfigurationList();
		
		//creating all the painters
		CurvePainter painter=null;
		
		for(TrendsCurveConfiguration curveConfig : configs){

			painter=new CurvePainter(this, curveConfig);
			painters.put(curveConfig.getTagName(), painter);
			paintersList.add(painter);
		}
	}
	
	/**
	 * returns the curve painter corresponding to the given tag name
	 * @param tagName a tag name
	 * @return the curve painter corresponding to the given tag name
	 */
	public CurvePainter getCurvePainter(String tagName){
		
		return painters.get(tagName);
	}
	
	/**
	 * update the tag values for the give tag name
	 * @param tagName the name of a tag
	 * @param tagValues the map associating a time value to a tag value
	 * @param startDate the start date of the tag values
	 * @param endDate the end date of the tag values
	 * @param leftOffset the left offset	
	 * @param rightOffset the right offset
	 * @param topOffset the top offset of the component of the curves
	 * @param availableWidth the available width to draw
	 * @param availableHeight the available height to draw
	 */
	public void updateValues(String tagName, TreeMap<Long, Object> tagValues, 
												long startDate, long endDate, int leftOffset, int rightOffset,
												int topOffset, int availableWidth, int availableHeight){
		
		//getting the curve painter corresponding to the given tag name
		CurvePainter painter=painters.get(tagName);
		
		if(painter!=null){

			//refreshing the display
			painter.preparePoints(tagValues, leftOffset, rightOffset, topOffset, availableWidth, 
												availableHeight, startDate, endDate);
		}
	}
	
	/**
	 * paints the points of the curves with the given zoom factors
	 * @param g a graphics object
	 * @param bounds the bounds for painting the curves
	 */
	public void validateUpdates(Graphics2D g, Rectangle bounds){
		
		for(CurvePainter painter : painters.values()){

			painter.paintCurve(g, bounds);
		}
	}
	
	/**
	 * @return the curve painters
	 */
	public LinkedList<CurvePainter> getCurvePainters(){
		
		return paintersList;
	}

	/**
	 * @return curvesComponent
	 */
	public CurvesComponent getCurvesComponent() {
		return curvesComponent;
	}
}
