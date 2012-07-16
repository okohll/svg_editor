package fr.itris.glips.extension.jwidget.trends.runtime.view.component;

import java.awt.event.*;
import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.model.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;

/**
 * the panel of the horizontal bar
 * @author ITRIS, Jordi SUC
 */
public class HorizontalBarComponent extends JPanel{
	
	/**
	 * the components handler
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the scroll bar used to navigate among the data according to the time
	 */
	private JScrollBar scrollbar;
	
	/**
	 * the configuration object
	 */
	private TrendsConfiguration configuration;
	
	/**
	 * the listener to the scroll bar
	 */
	private AdjustmentListener listener;
	
	/**
	 * whether the scroll bar is adjsuting
	 */
	private boolean isAdjusting=false;
	
	/**
	 * the base date for the values of the scroll bar
	 */
	private long baseDate=0;
	
	/**
	 * whether the scrollbar is disabled or not
	 */
	private boolean scrollbarDisabled=false;
	
	/**
	 * the constructor of the class
	 * @param componentsHandler  	the object used to handle all the updates, actions and 
	 * 													modifications that can occur on one of the components
	 */
	public HorizontalBarComponent(ComponentsHandler componentsHandler){
		
		this.componentsHandler=componentsHandler;
		this.configuration=componentsHandler.getView().getController().getConfiguration();
	}
	
	/**
	 * initializes the component
	 */
	public void initialize(){
		
		//setting the layout
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		//creating the scrollbar
		scrollbar=new JScrollBar(JScrollBar.HORIZONTAL);
		add(scrollbar);
		
		//getting the model
		
		//creating the listener to the scroll bar
		listener=new AdjustmentListener(){
			
			public void adjustmentValueChanged(AdjustmentEvent evt) {
				
				isAdjusting=evt.getValueIsAdjusting();
				
				//clearing some tools
				componentsHandler.clearTools(true);
				
				if(configuration.getCurrentMode()==TrendsConfiguration.REAL_TIME_MODE){

					//enabling the scrolling mode
					configuration.setCurrentSubMode(TrendsConfiguration.SCROLL);
				}
				
				///getting the start date
				long startDate=baseDate+scrollbar.getValue();
				long endDate=startDate+configuration.getHorizontalAxisDuration();
				
				//updating the curves
				componentsHandler.getView().refreshDisplay(startDate, endDate);
			}
		};
		
		scrollbar.addAdjustmentListener(listener);
	}
	
	/**
	 * disposes the object
	 */
	public void dispose(){//TODO
		
		scrollbar.removeAdjustmentListener(listener);
		
		
		componentsHandler=null;
		scrollbar=null;
		configuration=null;
		listener=null;
	}
	
	/**
	 * validates all the updates that have been done, 
	 * so that the horizontal bar panel can be refreshed
	 * @param firstDate the date before which no tag value should be displayed
	 * @param lastDate the date after which no tag value should be displayed
	 * @param startDate the start date of the tag values
	 * @param endDate the end date of the tag values
	 */
	public void validateUpdates(long firstDate, long lastDate, long startDate, long endDate){
		
		scrollbar.removeAdjustmentListener(listener);
		
		//storing the base date
		baseDate=firstDate;

		//setting the new values
		int unitIncrement=(int)(configuration.getHorizontalAxisDuration()/20);
		
		if(unitIncrement<0){
			
			unitIncrement=1;
		}

		scrollbar.setUnitIncrement(unitIncrement);
		scrollbar.setBlockIncrement(unitIncrement*4);
		scrollbar.setValues((int)(startDate-baseDate), (int)(endDate-startDate), 
										0, (int)(lastDate-baseDate));
		
		scrollbar.addAdjustmentListener(listener);
	}
	
	/**
	 * updates the scroll bar given the dates when in scroll sub mode
	 * @param firstDate the date before which no tag value should be displayed
	 * @param lastDate the date after which no tag value should be displayed
	 * @param startDate the start date of the tag values
	 * @param endDate the end date of the tag values
	 */
	public void updateScrollBar(long firstDate, long lastDate, long startDate, long endDate){
		
		if(! isAdjusting){
			
			scrollbar.removeAdjustmentListener(listener);
			
			//getting the horizontal duration
			long duration=configuration.getHorizontalAxisDuration();
			
			//getting the model
			TrendsRuntimeModel model=componentsHandler.getView().getController().getModel();
			
			if(duration>(model.getLastDate()-model.getFirstDate())){

				scrollbar.setValues(0, 0, 0, 0);
				scrollbarDisabled=true;
				
			}else if (scrollbarDisabled){
				
				scrollbar.setValues((int)(startDate-firstDate), (int)(lastDate-startDate), 
						0, (int)(lastDate-firstDate));

				//setting the new base date
				baseDate=firstDate;
				scrollbarDisabled=false;
				
			}else{
				
				//getting the start and the end dates
				long lastStartDate=baseDate+scrollbar.getValue();
				long lastEndDate=lastStartDate+scrollbar.getVisibleAmount();
				
				//checking the start and the end dates
				if(lastStartDate<firstDate){
					
					lastStartDate=firstDate;
				}
				
				if(lastEndDate<lastStartDate){
					
					lastEndDate=firstDate+configuration.getHorizontalAxisDuration();
				}
				
				if(lastEndDate>lastDate){
					
					lastEndDate=lastDate;
				}
				
				scrollbar.setValues((int)(lastStartDate-firstDate), (int)(lastEndDate-lastStartDate), 
												0, (int)(lastDate-firstDate));
				
				//setting the new base date
				baseDate=firstDate;
			}
			
			//setting the new values
			int unitIncrement=(int)(configuration.getHorizontalAxisDuration()/20);
			
			if(unitIncrement<0){
				
				unitIncrement=1;
			}

			scrollbar.setUnitIncrement(unitIncrement);
			scrollbar.setBlockIncrement(unitIncrement*4);
			scrollbar.addAdjustmentListener(listener);
		}
	}
	
	/**
	 * @return the current end date
	 */
	public long getStartDate(){
		
		return baseDate+scrollbar.getValue();
	}
	
	/**
	 * @return the object used to handle all the updates, actions and 
	 * 				modifications that can occur on one of the components
	 */
	public ComponentsHandler getComponentsHandler() {
		return componentsHandler;
	}
}
