package fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools;

import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.ComponentsHandler;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import java.awt.event.*;

/**
 * the class of the tool used to decrease the displayed time on the horizontal axis
 * @author ITRIS, Jordi SUC
 */
public class TimeZoomInitialTool extends JButton{
	
	/**
	 * the id
	 */
	private String id="TimeZoomInitial";
	
	/**
	 * the components handler
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the listener to the button
	 */
	private ActionListener listener;
	
	/**
	 * the configuration object
	 */
	private TrendsConfiguration configuration;
	
	/**
	 * the constructor of the class
	 * @param toolBar the tool bar component
	 */
	public TimeZoomInitialTool(ToolBarComponent toolBar){

		this.componentsHandler=toolBar.getComponentsHandler();
		this.configuration=componentsHandler.getView().getController().getConfiguration();
		build();
	}
	
	/**
	 * builds the button
	 */
	protected void build(){

		//setting the properties of the button
		setToolTipText(TrendsBundle.bundle.getString(id));
		
		//setting the icon
		setIcon(TrendsIcons.getIcon(id, false));
		
		//adding a listener to the button
		listener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				componentsHandler.setCurrentAction(id, true);
				
				//setting the horizontal axis duration
				configuration.setHorizontalAxisDuration(configuration.getInitialHorizontalAxisDuration());
			}
		};
		
		addActionListener(listener);
	}
	
	/**
	 * disposes the object
	 */
	public void dispose(){
		
		removeActionListener(listener);
		
		
		listener=null;
		componentsHandler=null;
		configuration=null;
	}
}