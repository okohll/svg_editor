package fr.itris.glips.extension.jwidget.slider.runtime.actions;

import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.action.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.test.*;
import fr.itris.glips.rtda.toolkit.*;
import java.awt.*;

/**
 * the class of the send measure action
 * @author ITRIS, Jordi SUC
 */
public class SliderSendMeasure extends AbstractSendMeasure{
	
	/**
	 * the tag type
	 */
	private int tagType;
	
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
	public SliderSendMeasure(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
		
		initializeAction();
	}
	
	@Override
	protected void initializeAction() {
		
        //getting the type of the tag to set
        tagType=picture.getMainDisplay().getPictureManager().
        	getConfigurationDocument(picture).getTagType(
        			actionElement.getAttribute(tagToWriteName));
		
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
    }
	
	@Override
	public Runnable dataChanged(DataEvent evt) {
		
		super.dataChanged(evt);
		
		if(isEntitled()){
		
			final Map<String, Object> newMap=evt.getDataNameToValue();
			
			if(isAuthorized && newMap.containsKey(actionElement.getAttribute(tagToWriteName))){
				
				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {

						JSlider slider=(JSlider)component;
						int newTagToWriteValue=0;
						Object obj=newMap.get(actionElement.getAttribute(tagToWriteName));
						
						newTagToWriteValue=(int)Toolkit.getNumber(obj);
				        
    			        jwidgetRuntime.unregisterListeners();
						slider.setValue(newTagToWriteValue);
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
			
			//getting the current value of the slider
			int currentValue=((JSlider)component).getValue();
			Object value=null;
			
			if(tagType==TagToolkit.ANALOGIC_INTEGER){
				
				value=currentValue;
				
			}else{
				
				value=(double)currentValue;
			}

			putTagValue(actionElement.getAttribute(tagToWriteName), value);
			handleReturnToInitialValue(obj);
		}
	}
}
