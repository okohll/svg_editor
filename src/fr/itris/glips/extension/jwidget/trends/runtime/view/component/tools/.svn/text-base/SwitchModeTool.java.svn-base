package fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools;

import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import java.awt.event.*;

/**
 * the class of the tool used to switch between the RT and the history mode
 * @author ITRIS, Jordi SUC
 */
public class SwitchModeTool extends JToggleButton{
	
	/**
	 * the real time mode id
	 */
	public static final String realTimeModeId="RealTimeMode";
	
	/**
	 * the history mode id
	 */
	public static final String historyModeId="HistoryMode";
	
	/**
	 * the listener to the button
	 */
	private ActionListener listener;
	
	/**
	 * the configuration object
	 */
	private TrendsConfiguration configuration;
	
	/**
	 * the labels
	 */
	private String rtLabel="", historyLabel="";
	
	/**
	 * the icons
	 */
	private ImageIcon rtIcon, historyIcon;
	
	/**
	 * the components handler
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the constructor of the class
	 * @param toolBar the tool bar component
	 */
	public SwitchModeTool(ToolBarComponent toolBar){

		this.componentsHandler=toolBar.getComponentsHandler();
		this.configuration=componentsHandler.getView().getController().getConfiguration();
		
		if(configuration.isWidget()){
			
			setEnabled(false);
		}
		
		build();
	}
	
	/**
	 * builds the button
	 */
	protected void build(){
		
		//getting the labels
		rtLabel=TrendsBundle.bundle.getString(realTimeModeId);
		historyLabel=TrendsBundle.bundle.getString(historyModeId);
		
		//getting the icons
		rtIcon=TrendsIcons.getIcon(realTimeModeId, false);
		historyIcon=TrendsIcons.getIcon(historyModeId, false);
		
		//adding a listener to the button
		listener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {	

				//disabling configuration changes event dispatch
				configuration.setAllowEventDispatch(false);
				
				//reinitializing the horizontal and vertical zooms
				configuration.setVerticalZoomFactor(1.0, false);
				configuration.setVerticalZoomOrigin(0);
				configuration.setHorizontalAxisDuration(configuration.getInitialHorizontalAxisDuration());
				
				if(configuration.getCurrentMode()==TrendsConfiguration.REAL_TIME_MODE){
					
					configuration.setAllowEventDispatch(true);
					componentsHandler.setCurrentAction(historyModeId, true);
					configuration.setCurrentMode(TrendsConfiguration.HISTORY_MODE);
					
					componentsHandler.getDatePickersDialog().
						showDialog(SwitchModeTool.this);

				}else{
					
					componentsHandler.setCurrentAction(realTimeModeId, true);
					configuration.setCurrentSubMode(TrendsConfiguration.UPDATE);
					configuration.setAllowEventDispatch(true);
					configuration.setCurrentMode(TrendsConfiguration.REAL_TIME_MODE);
				}
			}
		};
		
		//initializing the state of the button
		modeOrSubModeChanged();
	}
	
	/**
	 * updates the text and the icon of the button
	 */
	protected void updateTextAndIcon(){
		
		setIcon(configuration.getCurrentMode()==TrendsConfiguration.REAL_TIME_MODE?
					rtIcon:historyIcon);
		setToolTipText(configuration.getCurrentMode()==TrendsConfiguration.REAL_TIME_MODE?
								rtLabel:historyLabel);
		setSelected(configuration.getCurrentMode()==TrendsConfiguration.REAL_TIME_MODE);
	}
	
	/**
	 * disposes the object
	 */
	public void dispose(){//TODO
		
		removeActionListener(listener);
		
		
		listener=null;
		componentsHandler=null;
		configuration=null;
	}
	
	/**
	 * called when the mode or the submode has changed
	 */
	public void modeOrSubModeChanged(){
		
		removeActionListener(listener);
		updateTextAndIcon();
		addActionListener(listener);
	}
}
