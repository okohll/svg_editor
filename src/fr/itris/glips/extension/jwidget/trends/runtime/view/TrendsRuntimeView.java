package fr.itris.glips.extension.jwidget.trends.runtime.view;

import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.controller.*;
import fr.itris.glips.extension.jwidget.trends.runtime.model.*;
import java.util.*;
import javax.swing.*;

/**
 * the class of the view part of the trends jwidget runtime object
 * @author ITRIS, Jordi SUC
 */
public class TrendsRuntimeView {

	/**
	 * the controller of the trends widget
	 */
	private TrendsRuntimeController controller;
	
	/**
	 * the runnable used to update the real time values while in real time mode
	 */
	private UpdateRunnable updaterRunnable=new UpdateRunnable();
	
	/**
	 * the runnable used to scroll the real time values while in real time mode
	 */
	private ScrollRunnable scrollerRunnable=new ScrollRunnable();
	
	/**
	 * the object used to handle all the updates, actions and modifications
     * that can occur on one of the components
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the constructor of the class
	 * @param controller the controller of the trends widget
	 */
	public TrendsRuntimeView(TrendsRuntimeController controller){
		
		this.controller=controller;
		
		//creating the manager of the trends components
		componentsHandler=new ComponentsHandler(this);
	}
	
	/**
	 * initializes the view part
	 */
	public void initialize(){
		
		//creating the listener to the changes of the modes and of the sub modes
		controller.getConfiguration().addListener(new ConfigurationChangeListener(){
			
			@Override
			public void modeOrSubModeChanged() {

				componentsHandler.modeOrSubModeChanged();
				
				if(controller.getConfiguration().getCurrentMode()==TrendsConfiguration.HISTORY_MODE){

					refreshDisplay(System.currentTimeMillis()-1, System.currentTimeMillis());
				}
			}
			
			@Override
			public void durationChanged() {

				int mode=controller.getConfiguration().getCurrentMode();
				int submode=controller.getConfiguration().getCurrentSubMode();
				
				if((mode==TrendsConfiguration.REAL_TIME_MODE &&
						submode!=TrendsConfiguration.UPDATE) || 
							mode==TrendsConfiguration.HISTORY_MODE){

					scrollerRunnable.requestDisplayWhenZoomChanged();
				}
			}
		});
		
		//adding the listeners for each curve
		for(TrendsCurveConfiguration trendsCurveConfiguration : 
					controller.getConfiguration().getCurvesConfigurationList()){
			
			trendsCurveConfiguration.addCurvesConfigurationChangeListener(
					new CurvesConfigurationChangeListener(){
						
				@Override
				public void curveScaleChanged() {

					int mode=controller.getConfiguration().getCurrentMode();
					int submode=controller.getConfiguration().getCurrentSubMode();
					
					if((mode==TrendsConfiguration.REAL_TIME_MODE &&
							submode!=TrendsConfiguration.UPDATE) || 
								mode==TrendsConfiguration.HISTORY_MODE){

						scrollerRunnable.requestSimpleRefresh();
					}
				}
				
				@Override
				public void curveStyleChanged() {

					int mode=controller.getConfiguration().getCurrentMode();
					int submode=controller.getConfiguration().getCurrentSubMode();
					
					if((mode==TrendsConfiguration.REAL_TIME_MODE &&
							submode!=TrendsConfiguration.UPDATE) || 
								mode==TrendsConfiguration.HISTORY_MODE){

						scrollerRunnable.requestSimpleRefresh();
					}
				}
			});
		}
		
		componentsHandler.initialize();
		
		//starting the updater
		Thread thread=new Thread(updaterRunnable);
		thread.start();
		
		thread=new Thread(scrollerRunnable);
		thread.start();
	}
	
	/**
	 * @return the controller part
	 */
	public TrendsRuntimeController getController() {
		return controller;
	}
	
	/**
	 * @return the trends component
	 */
	public JComponent getTrendsComponent(){
		
		return componentsHandler.getTrendsComponent();
	}
	
	/**
	 * sets the value of the given tag
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	public void setTagValue(String tagName, Object value){
		
		componentsHandler.getCurvesPropertiesComponent().setTagValue(tagName, value);
		
		if(componentsHandler.getToolBarComponent()!=null){
			
			componentsHandler.getToolBarComponent().getCursorLineTool().setTagValue(tagName, value);
			componentsHandler.getToolBarComponent().getCursorCrossTool().setTagValue(tagName, value);
		}
	}
	
	/**
	 * refreshes the values of the tags for the given start date and to the given end date
	 * @param startDate the start date
	 * @param endDate the end date
	 */
	public void refreshDisplay(long startDate, long endDate){
		
		scrollerRunnable.requestDisplay(startDate, endDate);
	}
	
	/**
	 * refreshes the values of the tags for the given start date and to the given end date
	 * @param startDate the start date
	 * @param endDate the end date
	 */
	protected void refreshValues(final long startDate, final long endDate){
		
		//getting the list of the tag names of the curves
		final Set<String> tagNames=controller.getConfiguration().getTagNames();
		
		//initiating the update
		componentsHandler.initiateUpdate();
		
		TreeMap<Long, Object> tagValues=null;
		
		for(String tagName : tagNames){
			
			//getting the map of the values of the tag
			tagValues=controller.getModel().getValues(tagName, startDate, endDate);

			//updating the tag values
			componentsHandler.updateValues(tagName, tagValues, startDate, endDate);
		}
		
		//getting the first date and the last date
		final long firstDate=controller.getModel().getFirstDate();
		final long lastDate=controller.getModel().getLastDate();

		//validating the updates
		try{
			SwingUtilities.invokeAndWait(new Runnable(){
			
				public void run() {
					
					componentsHandler.validateUpdates(firstDate, lastDate, startDate, endDate);
				}
			});
		}catch (Exception ex){ex.printStackTrace();}
	}
	
	/**
	 * updates the representation of the time values
	 * @param startDate the start date
	 * @param endDate the end date
	 */
	protected void updateTime(long startDate, long endDate){
		
		componentsHandler.updateScrollBar(controller.getModel().getFirstDate(), 
			controller.getModel().getLastDate(), startDate, endDate);
	}
	
	/**
	 * the class used to update the tag values 
	 * @author ITRIS, Jordi SUC
	 */
	protected class UpdateRunnable implements Runnable{
		
		/**
		 * the current end date
		 */
		private long currentEndDate=0;
		
		public void run() {
			
			final TrendsConfiguration configuration=controller.getConfiguration();
			
			//getting the list of the tag names of the curves
			final Set<String> tagNames=controller.getConfiguration().getTagNames();
			
			//the period for refreshing the value on the curves component
			long refreshPeriod=controller.getConfiguration().getRefreshPeriod();

			//the time of the update
			long currentTime=-1;
			
			if(tagNames.size()>0){
				
				while(controller!=null && ! controller.isJWidgetDisposed()){
					
					if(controller.getConfiguration().getCurrentMode()==TrendsConfiguration.REAL_TIME_MODE){
						
						currentTime=System.currentTimeMillis();
						controller.getModel().setLastDate(currentTime);
						
						if(controller.getConfiguration().getCurrentSubMode()==TrendsConfiguration.UPDATE){
							
							//refreshing the display
							refreshValues(currentTime-configuration.getHorizontalAxisDuration(), currentTime);
							synchronized(this){currentEndDate=currentTime;}
							
						}else{
							
							final long fcurrentTime=currentTime;
							
							try{
								SwingUtilities.invokeAndWait(new Runnable(){
									
									public void run() {

										//modifying the time values 
										updateTime(fcurrentTime-configuration.getHorizontalAxisDuration(), 
												fcurrentTime);
									}
								});
							}catch (Exception ex){}
						}
						
						//sleeping
						try{
							Thread.sleep(refreshPeriod);
						}catch (Exception ex){}
						
					}else{
						
						//sleeping
						try{
							Thread.sleep(500);
						}catch (Exception ex){}
					}
				}
			}	
		}

		/**
		 * @return the current end date
		 */
		public long getCurrentEndDate() {
			return currentEndDate;
		}
	}
	
	/**
	 * the class used to scroll among the tag values 
	 * @author ITRIS, Jordi SUC
	 */
	protected class ScrollRunnable implements Runnable{
		
		/**
		 * the start date used for the refresh action when in SCROLL mode
		 */
		private long refreshStartDate=-1;
		
		/**
		 * the end date used for the refresh action when in SCROLL mode
		 */
		private long refreshEndDate=-1;
		
		/**
		 * whether the end date has been set by the user
		 */
		private boolean endDateSet=false;
		
		/**
		 * whether or not to refresh the curves
		 */
		private boolean refresh=false;
		
		public void run() {
			
			long currentStartDate=-1;
			long currentEndDate=-1;
			
			while(controller!=null && ! controller.isJWidgetDisposed()){
				
				if(refresh && refreshStartDate!=-1){
					
					synchronized (this) {refresh=false;}
					
					//the tag values should be refreshed
					currentStartDate=refreshStartDate;
					currentEndDate=refreshEndDate;

					//refreshing the values
					refreshValues(currentStartDate, currentEndDate);
					
					//sleeping
					try{
						Thread.sleep(50);
					}catch (Exception ex){}
					
				}else{
					
					//sleeping
					try{
						Thread.sleep(200);
					}catch (Exception ex){}
				}
			}
		}
		
		/**
		 * requests to display the tag values from the start date to the end date
		 * @param startDate the start date
		 * @param endDate the end date
		 */
		public synchronized void requestDisplay(long startDate, long endDate){
			
			this.refreshStartDate=startDate;
			this.refreshEndDate=endDate;
			this.refresh=true;
		}
		
		/**
		 * requests the scroller runnable to refresh
		 */
		public synchronized void requestSimpleRefresh(){
			
			//getting the model and the horizontal duration
			TrendsRuntimeModel model=controller.getModel();
			long duration=controller.getConfiguration().getHorizontalAxisDuration();
			
			if(refreshEndDate==-1){
				
				this.refreshStartDate=model.getLastDate()-duration;
				this.refreshEndDate=model.getLastDate();
				
			}else{
				
				this.refreshStartDate=refreshEndDate-duration;
			}

			this.refresh=true;
		}
		
		/**
		 * requests to refresh the tag values when the zoom has changed
		 */
		public synchronized void requestDisplayWhenZoomChanged(){
			
			//getting the model and the horizontal duration
			TrendsRuntimeModel model=controller.getModel();
			long duration=controller.getConfiguration().getHorizontalAxisDuration();
			
			if(endDateSet){
				
				this.refreshStartDate=refreshEndDate-duration;
				this.endDateSet=false;
				
			}else if(duration>(model.getLastDate()-model.getFirstDate())){
				
				this.refreshStartDate=model.getFirstDate();
				this.refreshEndDate=model.getLastDate();
				
			}else if(refreshEndDate==-1){
				
				this.refreshStartDate=model.getLastDate()-duration;
				this.refreshEndDate=model.getLastDate();
				
			}else{
				
				this.refreshStartDate=refreshEndDate-duration;
				
				if(this.refreshStartDate<controller.getModel().getFirstDate()){
					
					this.refreshStartDate=controller.getModel().getFirstDate();
					this.refreshEndDate=refreshStartDate+duration;
				}
			}

			this.refresh=true;
		}
		
		/**
		 * sets the new current end date
		 * @param currentEndDate the current end date
		 */
		public synchronized void setCurrentEndDate(long currentEndDate){
			
			this.refreshEndDate=currentEndDate;
			this.endDateSet=true;
		}
		
		/**
		 * @return the current end date for the scrolling action
		 */
		public long getCurrentEndDate(){
			
			return refreshEndDate;
		}
	}
	
	/**
	 * disposes the view
	 */
	public void dispose(){//TODO
		
		componentsHandler.dispose();
		
		controller=null;
		updaterRunnable=null;
		scrollerRunnable=null;
		componentsHandler=null;
	}
	
	/**
	 * @return the current end date for the scrolling action
	 */
	public long getCurrentEndDate(){
		
		long date=scrollerRunnable.getCurrentEndDate();
		
		if(date<=0){
			
			date=updaterRunnable.getCurrentEndDate();
		}
		
		return date;
	}
	
	/**
	 * sets the new current end date
	 * @param currentEndDate the current end date
	 */
	public void setCurrentEndDate(long currentEndDate){
		
		scrollerRunnable.setCurrentEndDate(currentEndDate);
	}

	/**
	 * @return the components handler
	 */
	public ComponentsHandler getComponentsHandler() {
		return componentsHandler;
	}
}
