package fr.itris.glips.extension.jwidget.list.runtime.actions;

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
import fr.itris.glips.rtda.widget.*;

/**
 * the class of the send measure action
 * @author ITRIS, Jordi SUC
 */
public class ListSendMeasure extends AbstractSendMeasure{
	
	/**
	 * the string names
	 */
	private static String valueName="value", usedAttName="used";
	
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
	public ListSendMeasure(SVGPicture picture, String projectName, Container parent, 
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
        
    	//getting the values of the tag to write
    	LinkedList<Object> tagToWriteValues=new LinkedList<Object>();
    	LinkedList<String> tagToWriteValuesLabel=new LinkedList<String>();
    	
    	if(actionElement.hasChildNodes()) {
    		
            //getting the type of the tag to set
            int tagType=picture.getMainDisplay().getPictureManager().
            	getConfigurationDocument(picture).getTagType(
            			actionElement.getAttribute(tagToWriteName));
    		
    		Element childElement=null;
    		String label="";
    		
    		for(Node child=actionElement.getFirstChild(); child!=null; child=child.getNextSibling()) {
    			
    			if(child instanceof Element) {
    				
    				childElement=(Element)child;
    				
    				if(childElement.getAttribute(usedAttName).equals(Boolean.toString(true))){
    					
    					label=childElement.getAttribute(valueName);
    					tagToWriteValuesLabel.add(label);
        				tagToWriteValues.add(AnimationsToolkit.getNumber(label, tagType));
    				}
    			}
    		}
    	}

        //if we are in the test version, we store information on the tags
        if(picture.getMainDisplay().getAnimationsHandler().isTestVersion()){

        	//adding the information object for the test for the tag to write
            TestTagInformation info=new TestTagInformation(
            		picture, actionElement.getAttribute(tagToWriteName), null);
            dataNamesToTestTagInfo.put(actionElement.getAttribute(tagToWriteName), info);
        }
        
        initializeAuthorizationTag();
        
        //filling the list with the list items corresponding to each tag to write value
        final JList list=(JList)component;
        jwidgetRuntime.unregisterListeners();
        DefaultListModel model=(DefaultListModel)list.getModel();
        ComboListItem item=null;
        String label="";
        Object value=null;
        
        //adding an empty item
        model.addElement(new ComboListItem(null, ""));
        
        for(int i=0; i<tagToWriteValues.size(); i++){
        	
        	value=tagToWriteValues.get(i);
        	label=tagToWriteValuesLabel.get(i);
        	item=new ComboListItem(value, label);
        	model.addElement(item);
        }
        
        jwidgetRuntime.registerListeners();
    }
	
	@Override
	public Runnable dataChanged(DataEvent evt) {

		super.dataChanged(evt);
		
		if(isEntitled()){

			final Map<String, Object> newMap=evt.getDataNameToValue();
			
			if(isAuthorized && newMap.containsKey(actionElement.getAttribute(tagToWriteName))){
				
				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {

					    jwidgetRuntime.unregisterListeners();
					    
						final JList list=(JList)component;
						DefaultListModel model=(DefaultListModel)list.getModel();
						Object obj=newMap.get(actionElement.getAttribute(tagToWriteName));
				        int selectedIndex=0;
				        ComboListItem item;
				        Object itemValue=null;
				        
				        if(obj!=null){
				        	
				        	for(int i=0; i<model.size(); i++){
					        	
					        	item=(ComboListItem)model.elementAt(i);
					        	itemValue=item.getValue();
					        	
					        	if(itemValue!=null && obj.equals(itemValue)){
					        		
					        		selectedIndex=i;
					        		break;
					        	}
					        }
				        }

				        list.setSelectedIndex(selectedIndex);
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
			Object currentValue=getData(actionElement.getAttribute(tagToWriteName));
			
			//the tag to write value to set
			Object tagValue=null;
			ComboListItem item=(ComboListItem)((JList)component).getSelectedValue();
			
			if(item!=null && item.getValue()!=null){
				
				tagValue=item.getValue();
			}
	
			putTagValue(actionElement.getAttribute(tagToWriteName), tagValue);
			handleReturnToInitialValue(currentValue);
		}
	}
}
