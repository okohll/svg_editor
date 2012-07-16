package fr.itris.glips.extension.jwidget.combo.runtime.actions;

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
import java.awt.*;

/**
 * the class of the simple send command action
 * @author ITRIS, Jordi SUC
 */
public class ComboSimpleSendCommand extends AbstractSimpleSendCommand{
	
	/**
	 * the string names
	 */
	private static String usedAttName="used";
	
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
	public ComboSimpleSendCommand(SVGPicture picture, String projectName, Container parent, 
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
			toolTipText=bundle.getString("tooltip_simpleSendCommand");
		}catch (Exception ex){}
		
		//getting the tag names to handle
        dataNames.add(actionElement.getAttribute(tagToWriteName));

    	//getting the values of the tag to write
    	LinkedList<String> tagToWriteValues=new LinkedList<String>();
    	LinkedList<String> realTagToWriteValues=new LinkedList<String>();
    	
    	if(actionElement.hasChildNodes()) {
    		
    		Element childElement=null;
    		
    		for(Node child=actionElement.getFirstChild(); child!=null; child=child.getNextSibling()) {
    			
    			if(child instanceof Element) {
    				
    				childElement=(Element)child;
    				
    				if(childElement.getAttribute(usedAttName).equals(Boolean.toString(true))){
    					
    					realTagToWriteValues.add(childElement.getAttribute(valueName));
        				tagToWriteValues.add(AnimationsToolkit.normalizeEnumeratedValue(
        						childElement.getAttribute(valueName)));
    				}
    			}
    		}
    	}

        //if we are in the test version, we store information on the tags
        if(picture.getMainDisplay().getAnimationsHandler().isTestVersion()){

        	//adding the information object for the test for the tag to write
            TestTagInformation info=new TestTagInformation(
            		picture, actionElement.getAttribute(tagToWriteName), realTagToWriteValues);
            dataNamesToTestTagInfo.put(actionElement.getAttribute(tagToWriteName), info);
        }
        
        initializeAuthorizationTag();
        
        //filling the combo box with the combo items corresponding to each tag to write value
        final JComboBox combo=(JComboBox)component;
        jwidgetRuntime.unregisterListeners();
        ComboListItem item=null;
        
        //adding an empty item
        combo.addItem(new ComboListItem("", ""));
        int i=0;
        
        for(String value : tagToWriteValues){
        	
        	item=new ComboListItem(value, realTagToWriteValues.get(i));
        	combo.addItem(item);
        	i++;
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

				        JComboBox combo=(JComboBox)component;
				        jwidgetRuntime.unregisterListeners();
						
						String newTagToWriteValue="";
						Object obj=newMap.get(actionElement.getAttribute(tagToWriteName));
						
						if(obj!=null){
							
							newTagToWriteValue=obj.toString();
						}

				        int selectedIndex=0;
				        ComboListItem item;
				        
				        for(int i=0; i<combo.getItemCount(); i++){
				        	
				        	item=(ComboListItem)combo.getItemAt(i);
				        	
				        	if(newTagToWriteValue.equals(item.getValue())){
				        		
				        		selectedIndex=i;
				        		break;
				        	}
				        }
				        
				        combo.setSelectedIndex(selectedIndex);
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
			String tagValue="";
			ComboListItem item=(ComboListItem)((JComboBox)component).getSelectedItem();
			
			if(item!=null && item.getValue()!=null){
				
				tagValue=item.getValue().toString();
			}
	
			putTagValue(actionElement.getAttribute(tagToWriteName), tagValue);
			
			handleReturnToInitialValue(currentTagToWriteValue);
		}
	}
}
