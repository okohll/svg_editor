package fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools;

import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.TrendsConfiguration;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import java.awt.event.*;

/**
 * the class of the tool used to pick dates for displaying values in the history mode
 * @author ITRIS, Jordi SUC
 */
public class DatePickersDialogTool extends JButton{
	
	/**
	 * the id
	 */
	private String id="DatePickersDialog";
	
	/**
	 * the components handler
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the listener to the button
	 */
	private ActionListener listener;
	
	/**
	 * the constructor of the class
	 * @param toolBar the tool bar component
	 */
	public DatePickersDialogTool(ToolBarComponent toolBar){

		this.componentsHandler=toolBar.getComponentsHandler();
		initialize();
	}
	
	/**
	 * initializes the button
	 */
	public void initialize(){

		//setting the properties of the button
		setToolTipText(TrendsBundle.bundle.getString(id));
		
		//setting the icon
		setIcon(TrendsIcons.getIcon(id, false));
		
		//adding a listener to the button
		listener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				componentsHandler.setCurrentAction(id, true);
				componentsHandler.getDatePickersDialog().
					showDialog(DatePickersDialogTool.this);
			}
		};
		
		modeOrSubModeChanged();
	}
	
	/**
	 * called when the mode or the submode has changed
	 */
	public void modeOrSubModeChanged(){
		
		removeActionListener(listener);
		TrendsConfiguration configuration=
			componentsHandler.getView().getController().getConfiguration();

		if(configuration.getCurrentMode()==TrendsConfiguration.HISTORY_MODE){
			
			setEnabled(true);
			
		}else{
			
			setEnabled(false);
		}

		addActionListener(listener);
	}
	
	/**
	 * disposes the object
	 */
	public void dispose(){//TODO
		
		removeActionListener(listener);
		
		
		listener=null;
		componentsHandler=null;
	}
}
