package fr.itris.glips.extension.jwidget.textfield.runtime.actions;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.action.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the class of the send string action
 * @author ITRIS, Jordi SUC
 */
public class TextFieldSendString extends AbstractSendString{
	
	/**
	 * the constructor of the class
	 * @param picture the svg picture this action is linked to
	 * @param projectName the name of the project this action is linked to
	 * @param parent the top level container
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
	 * @param jwidgetRuntime the jwidget runtime object, if this action is linked to a jwidget
	 */
	public TextFieldSendString(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
		
		initializeAction();
	}
	
	@Override
	public Runnable dataChanged(DataEvent evt) {
		
		super.dataChanged(evt);
		
		if(isEntitled()){

			final Map<String, Object> newMap=evt.getDataNameToValue();
			
			if(isAuthorized && newMap.containsKey(
				actionElement.getAttribute(tagToWriteName))){
				
				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {

				        JTextField textField=(JTextField)component;
				        String newValue="";
				        Object obj=getData(actionElement.getAttribute(tagToWriteName));
				        
				        if(obj!=null){
				        	
				        	newValue=obj.toString();
				        }
						
				        jwidgetRuntime.unregisterListeners();
				        textField.setText(newValue);
				        jwidgetRuntime.registerListeners();
					}
				});
			}
			
			jwidgetRuntime.refreshComponentState();
		}

		return null;
	}
	
	@Override
	public void performAction(Object evt){

		if(isEntitled() && isAuthorized && showConfirmationDialog()) {
			
			//getting the current value of the tag
			Object obj=getData(actionElement.getAttribute(tagToWriteName));
			String currentTagToWriteValue="";
			
			if(obj!=null){
				
				currentTagToWriteValue=obj.toString();
			}

			//the tag to write value to set
			putTagValue(actionElement.getAttribute(tagToWriteName), 
					((JTextField)component).getText());
			handleReturnToInitialValue(currentTagToWriteValue);
		}
	}
}
