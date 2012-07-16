package fr.itris.glips.extension.jwidget.base.runtime.actions;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.action.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.test.*;

/**
 * the class of the simple send command action
 * @author ITRIS, Jordi SUC
 */
public class StateButtonSimpleSendCommand extends AbstractSimpleSendCommand{

	/**
	 * the string names
	 */
	protected static String valueDownName="valueDown", valueUpName="valueUp";
	
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
	public StateButtonSimpleSendCommand(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);

		initializeAction();
	}
	
	@Override
	protected void initializeAction() {

		//getting the tag names to handle
        dataNames.add(actionElement.getAttribute(tagToWriteName));

        //if we are in the test version, we store information on the tags
        if(picture.getMainDisplay().isTestVersion()){

        	LinkedList<String> tagToWriteValues=new LinkedList<String>();
        	tagToWriteValues.add(actionElement.getAttribute(valueDownName));
        	tagToWriteValues.add(actionElement.getAttribute(valueUpName));

        	//adding the information object for the test for the tag to write
            TestTagInformation info=new TestTagInformation(
            		picture, actionElement.getAttribute(tagToWriteName), tagToWriteValues);
            dataNamesToTestTagInfo.put(actionElement.getAttribute(tagToWriteName), info);
        }
        
        initializeAuthorizationTag();
    }
	
	@Override
	public Runnable dataChanged(DataEvent evt) {
		
		super.dataChanged(evt);
		
		if(isEntitled()){
			
			//whether the button should be selected or not;
			String tagToWrite=actionElement.getAttribute(tagToWriteName);
			
			if(isAuthorized && evt.getDataNameToValue().containsKey(tagToWrite)){
				
				//getting the current value of the tag to write
				Object obj=getData(tagToWrite);
				
				if(obj==null){
					
					obj="";
				}
				
				String tagToWriteValue=obj.toString();
				String tagDownValue=AnimationsToolkit.normalizeEnumeratedValue(
						actionElement.getAttribute(valueDownName));
				final boolean isSelected=tagDownValue.equals(tagToWriteValue);

				if(((JToggleButton)component).isSelected()!=isSelected){

					SwingUtilities.invokeLater(new Runnable(){
						
						public void run() {

							//handling the state of the button
							jwidgetRuntime.unregisterListeners();
							((JToggleButton)component).setSelected(isSelected);
							jwidgetRuntime.registerListeners();
						}
					});
				}
			}
			
			jwidgetRuntime.refreshComponentState();
		}

		return null;
	}

	@Override
	public void performAction(Object evt) {

		if(isEntitled() && isAuthorized && showConfirmationDialog()) {

			//getting the current tag to write value
			Object oldObj=getData(actionElement.getAttribute(tagToWriteName));
			String currentTagToWriteValue=null;
			
			if(oldObj!=null) {
				
				currentTagToWriteValue=oldObj.toString();
			}

			String theDownValue=AnimationsToolkit.normalizeEnumeratedValue(
					actionElement.getAttribute(valueDownName));
			String theUpValue=AnimationsToolkit.normalizeEnumeratedValue(
					actionElement.getAttribute(valueUpName));
			
			if(theDownValue.equals(oldObj)){
				
				putTagValue(actionElement.getAttribute(tagToWriteName), theUpValue);
				
			}else{

				putTagValue(actionElement.getAttribute(tagToWriteName), theDownValue);
			}

			handleReturnToInitialValue(currentTagToWriteValue);
		}
	}
}
