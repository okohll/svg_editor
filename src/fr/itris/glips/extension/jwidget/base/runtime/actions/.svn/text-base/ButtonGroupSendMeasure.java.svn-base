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
 *the class used to change a value
 * @author ITRIS, Jordi SUC
 */
public class ButtonGroupSendMeasure extends AbstractSendMeasure{

	/**
	 * the string names
	 */
	private static String tagValueName="tagValue";
	
	/**
	 * the value to set
	 */
	private Object value;

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
	public ButtonGroupSendMeasure(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);

		initializeAction();
	}
	
	@Override
	protected void initializeAction() {
		
		//getting the tool tip
		try{
			toolTipText=bundle.getString("tooltip_sendMeasure");
		}catch (Exception ex){}
		
		//getting the tag names to handle
        dataNames.add(actionElement.getAttribute(tagToWriteName));

        //if we are in the test version, we store information on the tags
        if(picture.getMainDisplay().isTestVersion()){
        	
        	//adding the information object for the test for the tag to write
            TestTagInformation info=new TestTagInformation(
            		picture, actionElement.getAttribute(tagToWriteName), null);
            dataNamesToTestTagInfo.put(actionElement.getAttribute(tagToWriteName), info);
        }
        
        initializeAuthorizationTag();
        
        //getting the type of the tag to set
        int tagType=picture.getMainDisplay().getPictureManager().
        	getConfigurationDocument(picture).getTagType(
        			actionElement.getAttribute(tagToWriteName));
        
        //getting the value to set
        value=AnimationsToolkit.getNumber(
        		actionElement.getAttribute(tagValueName), tagType);
	}
	
    @Override
    public Runnable dataChanged(DataEvent evt) {
    	
    	super.dataChanged(evt);
    	
    	if(isEntitled()){
    		
    		final Map<String, Object> newMap=evt.getDataNameToValue();
    		
    		if(isAuthorized && newMap.containsKey(actionElement.getAttribute(tagToWriteName))){
    			
    			SwingUtilities.invokeLater(new Runnable(){
    				
    				public void run() {
    					
    					//selecting the button if the current tag value corresponds to the specified tag value
    					Object obj=newMap.get(actionElement.getAttribute(tagToWriteName));
    			        
    			        if(obj!=null && value!=null && obj.equals(value)){
    			        	
							jwidgetRuntime.unregisterListeners();
    				        ((AbstractButton)component).setSelected(true);
							jwidgetRuntime.registerListeners();
    			        }
    				}
    			});
    		}
    		
    		jwidgetRuntime.refreshComponentState();
    	}

		return null;
    }

	@Override
	public void performAction(Object evt) {

		if(isEntitled() && isAuthorized && showConfirmationDialog()) {
			
   			//getting the old tag to write value
			Object oldObj=getData(actionElement.getAttribute(tagToWriteName));

    		//setting the new value for the tag to write
    		putTagValue(actionElement.getAttribute(tagToWriteName), value);
    		handleReturnToInitialValue(oldObj);
		}
    }
}
