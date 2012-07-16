package fr.itris.glips.rtda.action;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.animaction.Action;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.test.*;

/**
 * the superclass of the simple send command actions
 * @author Jordi SUC
 */
public abstract class AbstractSimpleSendCommand extends Action {

	/**
	 * the string names
	 */
	protected static String tagToWriteName="tagToWrite", 
		returnToInitialValueMethodName="returnToInitialValueMethod",
				autoName="auto", mouseUpEventName="mouseUpEvent", 
						valueName="value";
	
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
	public AbstractSimpleSendCommand(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
		
		computeRightsForTagsModif();
	}
	
	/**
	 * initializes this action object
	 */
	protected void initializeAction() {
		
		//getting the tag names to handle
        dataNames.add(actionElement.getAttribute(tagToWriteName));

        //if we are in the test version, we store information on the tags
        if(picture.getMainDisplay().isTestVersion()){

        	LinkedList<String> tagToWriteValues=new LinkedList<String>();
        	tagToWriteValues.add(actionElement.getAttribute(valueName));

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
		checkIsAuthorized();
		return null;
	}
	
	@Override
	public void performAction(Object evt) {

		if(isEntitled() && isAuthorized && showConfirmationDialog()){

			//getting the current tag to write value
			Object oldObj=getData(actionElement.getAttribute(tagToWriteName));
			String currentTagToWriteValue=null;
			
			if(oldObj!=null) {
				
				currentTagToWriteValue=oldObj.toString();
			}
			
			putTagValue(actionElement.getAttribute(tagToWriteName), 
					AnimationsToolkit.normalizeEnumeratedValue(
							actionElement.getAttribute(valueName)));
			
			handleReturnToInitialValue(currentTagToWriteValue);
		}
	}
	
	/**
	 * handles the method for returning to the initial value
	 * @param oldValue an old value
	 */
	protected void handleReturnToInitialValue(final String oldValue){

		//handling the return to initial value method
		String returnToInitialValue=
			actionElement.getAttribute(returnToInitialValueMethodName);
		
		if(returnToInitialValue.equals(mouseUpEventName)) {
			
			component.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent evt) {

					((JComponent)evt.getSource()).removeMouseListener(this);
					putTagValue(actionElement.getAttribute(tagToWriteName), oldValue);
				}
			});
			
		}else if(! returnToInitialValue.equals(autoName)) {
			
			//getting the value of the timer
			double timer=0;
			
        	try{
        		timer=Double.parseDouble(returnToInitialValue);
        	}catch (Exception ex){timer=Double.NaN;}
        	
        	if(! Double.isNaN(timer)) {
        		
            	final String tagToWrite=actionElement.getAttribute(tagToWriteName);
            	
            	TimerTask timerTask=new TimerTask() {

            		@Override
            		public void run() {
            			
            			putTagValue(tagToWrite, oldValue);
            		}
            	};
            	
            	AnimationsHandler.getTimer().schedule(timerTask, (long)(timer*1000));
        	}
		}
	}
}
