package fr.itris.glips.extension.jwidget.trends.runtime.view;

import java.text.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.curvesproperties.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.dialog.*;
import fr.itris.glips.library.widgets.*;

/**
 * the class used to handle all the updates, actions and modifications 
 * that can occur on one of the components
 * @author ITRIS, Jordi SUC
 */
public class ComponentsHandler {
	
	/**
	 * the offset from which the curve update will be displayed
	 */
	public static final int rightOffset=20;

	/**
	 * the view part
	 */
	private TrendsRuntimeView view;
	
	/**
	 * the component that will be displayed on a svg canvas
	 */
	private JPanel trendsComponent;
	
	/**
	 * the component displaying the curves
	 */
	private CurvesComponent curvesComponent;
	
	/**
	 * the component displaying the scrollbar
	 */
	private HorizontalBarComponent horizontalBarComponent;
	
	/**
	 * the component displaying a tool bar
	 */
	private ToolBarComponent toolBarComponent;
	
	/**
	 * the curves properties component
	 */
	private CurvesPropertiesComponent curvesPropertiesComponent;
	
	/**
	 * the current action
	 */
	private String currentAction="";
	
	/**
	 * the current exclusive action
	 */
	private String currentExclusiveAction="";
	
    /**
     * the decimal formats
     */
	private DecimalFormat format, exponentFormat;
	
	/**
	 * the date pickers dialog
	 */
	private DatePickersDialog datePickersDialog;
	
	/**
	 * the dialog that is displayed when history values are retrieved
	 */
	private WaitDialog waitDialog;
	
	/**
	 * the constructor of the class
	 * @param view the view part
	 */
	public ComponentsHandler(TrendsRuntimeView view){
		
		this.view=view;
		
		//getting the configuration object
		TrendsConfiguration configuration=view.getController().getConfiguration();
		
		//getting the number of decimal digits for displaying the values
		int decimalDigitNumber=configuration.getValuesDecimalDigitsNumber();
		
		//computing the pattern for the decimal part of the format
		String decimalPattern="";
		
		for(int i=0; i<decimalDigitNumber; i++){
			
			decimalPattern+="#";
		}
		
		//creating the decimal format
		DecimalFormatSymbols symbols=new DecimalFormatSymbols();
		format=new DecimalFormat("#############."+decimalPattern, symbols);
		format.setDecimalSeparatorAlwaysShown(false);
		exponentFormat=new DecimalFormat("0."+decimalPattern+"E0####", symbols);
		exponentFormat.setDecimalSeparatorAlwaysShown(false);

		//creating the components
		trendsComponent=new JPanel();
		trendsComponent.setLayout(new BorderLayout(0, 0));
		
		JPanel centerComponent=new JPanel();
		centerComponent.setLayout(new BorderLayout(0, 0));
		curvesComponent=new CurvesComponent(this);
		centerComponent.add(curvesComponent, BorderLayout.CENTER);
		
		if(configuration.displayToolBar()){
			
			toolBarComponent=new ToolBarComponent(this);
			centerComponent.add(toolBarComponent, BorderLayout.NORTH);
		}

		if(configuration.displayScrollBar()){

			horizontalBarComponent=new HorizontalBarComponent(this);
			centerComponent.add(horizontalBarComponent, BorderLayout.SOUTH);
		}

		trendsComponent.add(centerComponent, BorderLayout.CENTER);
		
		curvesPropertiesComponent=new CurvesPropertiesComponent(this);
	}
	
	/**
	 * initializes this component
	 */
	protected void initialize(){
		
		curvesComponent.initialize();
		
		if(toolBarComponent!=null){
			
			toolBarComponent.initialize();
		}
		
		if(horizontalBarComponent!=null){
			
			horizontalBarComponent.initialize();
		}
		
		curvesPropertiesComponent.initialize();
		
		Point location=new Point();
		
		if(toolBarComponent!=null){
			
			location=new Point(
					toolBarComponent.getDisplayCurvesPropertiesTool().getWidth()/2,
					toolBarComponent.getDisplayCurvesPropertiesTool().getHeight()/2);
			SwingUtilities.convertPointToScreen(location, 
					toolBarComponent.getDisplayCurvesPropertiesTool());
			SwingUtilities.convertPointFromScreen(location, 
					getView().getController().getJwidgetRuntime().getPicture().getCanvas());
		}

		curvesPropertiesComponent.setVisible(
				view.getController().getConfiguration().displayCurvesLegend(), location);
		
		//creating the date pickers manager
		Component parent=trendsComponent.getTopLevelAncestor();
		
		if(parent instanceof Frame){
			
			datePickersDialog=new DatePickersDialog(view, (Frame)parent);
			
		}else if(parent instanceof JApplet){
			
			datePickersDialog=new DatePickersDialog(view, 
				getView().getController().getJwidgetRuntime().getPicture().
					getMainDisplay().getTopLevelFrame());
			
		}else if(parent instanceof JDialog){
			
			datePickersDialog=new DatePickersDialog(view, (JDialog)parent);
		}
		
		//creating the wait dialog//
		
		//creating the cancel button
		ActionListener actionListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				Thread thread=new Thread(){
					
					@Override
					public void run() {

						view.getController().getModel().setDbRequestCancelled(true);
					}
				};

				thread.start();
				
				waitDialog.setVisible(false);
			}
		};
		
		if(parent instanceof Frame){
			
			waitDialog=new WaitDialog((Frame)parent, actionListener);
			
		}else if(parent instanceof JDialog){
			
			waitDialog=new WaitDialog((JDialog)parent, actionListener);
		}
		
		//setting the messages
		waitDialog.setTitleMessage(TrendsBundle.bundle.getString("historyDataLoading"));
		waitDialog.setMessage(TrendsBundle.bundle.getString("historyDataLoadingInfo"));
		waitDialog.setErrorMessage(TrendsBundle.bundle.getString("historyDataErrorLoading"));
	}
	
	/**
	 * @return the curves properties component
	 */
	public CurvesPropertiesComponent getCurvesPropertiesComponent() {
		
		return curvesPropertiesComponent;
	}
	
	/**
	 * disposes the handler
	 */
	public void dispose(){
		
		datePickersDialog.disposeDialog();
		waitDialog.disposeDialog();
		curvesComponent.dispose();
		
		if(horizontalBarComponent!=null){
			
			horizontalBarComponent.dispose();
		}
		
		if(toolBarComponent!=null){
			
			toolBarComponent.dispose();
		}

		curvesPropertiesComponent.dispose();
		
		view=null;
		trendsComponent=null;
		curvesComponent=null;
		horizontalBarComponent=null;
		toolBarComponent=null;
		curvesPropertiesComponent=null;
		datePickersDialog=null;
		waitDialog=null;
	}
	
	/**
	 * sets the current action
	 * @param id the id of the action
	 * @param deselectAll whether to deselect all the mouse action
	 */
	public void setCurrentAction(String id, boolean deselectAll){
		
		this.currentAction=id;
		clearTools(true);
	}
	
	/**
	 * sets the current exclusive action 
	 * @param id the id of the action
	 */
	public void setCurrentExclusiveAction(String id){
		
		this.currentExclusiveAction=id;
	}
	
	/**
	 * clearing some tools
	 * @param deselect whether to deselect the tools
	 */
	public void clearTools(boolean deselect){
		
		if(toolBarComponent!=null){
			
			toolBarComponent.getCursorLineTool().clear();
			toolBarComponent.getCursorCrossTool().clear();
			
			if(deselect){
				
				toolBarComponent.deselectMouseTools();
			}
		}
	}
	
	/**
	 * @return the current mouse action
	 */
	public String getCurrentAction() {
		
		return currentAction;
	}
	
	/**
	 * @return the current exclusive action
	 */
	public String getCurrentExclusiveAction() {
		
		return currentExclusiveAction;
	}

	/**
	 * initiates the update
	 */
	public void initiateUpdate(){
		
		curvesComponent.initiateUpdate();
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
		
		curvesComponent.updateValues(tagName, tagValues, startDate, endDate);
	}
	
	/**
	 * validates all the updates that have been done, 
	 * so that the curves panel can be refreshed
	 * @param originDate the date before which no tag value should be displayed
	 * @param lastDate the date after which no tag value should be displayed
	 * @param startDate the start date of the tag values
	 * @param endDate the end date of the tag values
	 */
	public void validateUpdates(long originDate, long lastDate, long startDate, long endDate){
		
		curvesComponent.validateUpdates(startDate, endDate);
		
		if(horizontalBarComponent!=null){
			
			horizontalBarComponent.validateUpdates(originDate, lastDate, startDate, endDate);
		}
	}
	
	/**
	 * updates the scroll bar given the dates when in scroll sub mode
	 * @param firstDate the date before which no tag value should be displayed
	 * @param lastDate the date after which no tag value should be displayed
	 * @param startDate the start date of the tag values
	 * @param endDate the end date of the tag values
	 */
	public void updateScrollBar(long firstDate, long lastDate, long startDate, long endDate){
		
		if(horizontalBarComponent!=null){
			
			horizontalBarComponent.updateScrollBar(firstDate, lastDate, startDate, endDate);
		}
	}

	/**
	 * @return the view part
	 */
	public TrendsRuntimeView getView() {
		return view;
	}

	/**
	 * @return the trends component
	 */
	public JPanel getTrendsComponent() {
		return trendsComponent;
	}
	
	/**
	 * @return the curves component
	 */
	public CurvesComponent getCurvesComponent() {
		return curvesComponent;
	}
	
	/**
	 * @return the horizontal bar component
	 */
	public HorizontalBarComponent getHorizontalBarComponent() {//TODO
		return horizontalBarComponent;
	}
	
	/**
	 * notifies that the mode or the sub mode changed
	 */
	public void modeOrSubModeChanged() {

		if(toolBarComponent!=null){
			
			toolBarComponent.modeOrSubModeChanged();
		}
	}

	/**
	 * @return the tool bar component
	 */
	public ToolBarComponent getToolBarComponent() {//TODO
		return toolBarComponent;
	}
	
	/**
	 * @return the date pickers dialog
	 */
	public DatePickersDialog getDatePickersDialog() {
		return datePickersDialog;
	}
	
	/**
	 * @return the wait dialog
	 */
	public WaitDialog getWaitDialog() {
		return waitDialog;
	}

	/**
	 * returns the string representation of the given value
	 * @param value a double value
	 * @return the string representation of the given value
	 */
	public String getStringRepresentation(double value){
		
		String str="";
		
		double absValue=Math.abs(value);
		
		if(absValue>0 && (absValue<=view.getController().getConfiguration().getLowerLimit() || 
				absValue>=view.getController().getConfiguration().getHigherLimit())){
			
			str=exponentFormat.format(value);
			
		}else{
			
			str=format.format(value);
		}
		
		return str;
	}
}
