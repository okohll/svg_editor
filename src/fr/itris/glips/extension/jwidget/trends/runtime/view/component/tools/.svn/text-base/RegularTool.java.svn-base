package fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools;

import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import java.awt.event.*;

/**
 * the class of the tool used to select an horodate and a value on the trends component
 * @author ITRIS, Jordi SUC
 */
public class RegularTool extends JToggleButton{

	/**
	 * the  id
	 */
	public static String id="Regular";
	
	/**
	 * the listener to the button
	 */
	private ActionListener listener;
	
	/**
	 * the components handler
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the constructor of the class
	 * @param toolBar the tool bar component
	 */
	public RegularTool(ToolBarComponent toolBar){

		this.componentsHandler=toolBar.getComponentsHandler();
		
		build();
	}
	
	/**
	 * builds the button
	 */
	protected void build(){
		
		//getting the label
		String label=TrendsBundle.bundle.getString(id);
		
		//setting the properties of the button
		setToolTipText(label);
		
		//getting the icon
		setIcon(TrendsIcons.getIcon(id, false));
		
		//adding a listener to the button
		listener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				if(isSelected()){
					
					componentsHandler.setCurrentExclusiveAction(id);
				}
			}
		};
		
		addActionListener(listener);
	}
	
	/**
	 * selects this tool
	 */
	public void select(){
		
		setSelected(true);
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
