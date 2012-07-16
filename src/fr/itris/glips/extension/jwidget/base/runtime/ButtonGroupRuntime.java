package fr.itris.glips.extension.jwidget.base.runtime;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.base.runtime.actions.*;
import fr.itris.glips.rtda.animaction.Action;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the class of the view browser runtime jwidget
 * @author ITRIS, Jordi SUC
 */
public abstract class ButtonGroupRuntime extends JWidgetRuntime{
	
    /**
     * the constructor of the class
     * @param picture the svg picture
     * @param element the svg element defining the jwidget
     */
    public ButtonGroupRuntime(SVGPicture picture, Element element){
		
		super(picture, element);
	}
	
    @Override
    public void initialize() {

    	ButtonGroupWidget buttonGroupWidget=new ButtonGroupWidget(this, element);
    	component=buttonGroupWidget;
    }

    @Override
    public Action createAction(Element actionElement) {
    	
    	Action action=null;
    	
    	if(actionElement!=null) {
    		
    		//getting the tag name of this action element
    		String tagName=actionElement.getTagName();
    		
    		//getting the source id of the action
            String sourceId=actionElement.getAttribute(
            		jwidgetSourceAttributeName);
            
            //getting the component corresponding to this sourceId
            JComponent cmp=((ButtonGroupWidget)component).getButton(sourceId);
            
            if(component!=null){
            	
           		if(tagName.equals("rtda:simpleSendCommand")) {
        			
        			action=new ButtonGroupSimpleSendCommand(picture, 
        					projectName, picture.getCanvas(), cmp, null, actionElement, this);
        			
        		}else if(tagName.equals("rtda:sendMeasure")) {
        			
        			action=new ButtonGroupSendMeasure(picture, projectName, 
        					picture.getCanvas(), cmp, null, actionElement, this);
           			
        		}else if(tagName.equals("rtda:loadView")) {
        			
        			action=new ButtonGroupLoadView(picture, projectName, 
        					picture.getCanvas(), cmp, null, actionElement, this);
        			
        		}else if(tagName.equals("rtda:loadView")) {
        			
        			action=new ButtonGroupLoadView(picture, projectName, 
        					picture.getCanvas(), cmp, null, actionElement, this);
           			
        		}else {
        			
        			action=super.createAction(actionElement);
        		}
            }
    	}
    	
    	return action;
    }
    
    @Override
    public void refreshComponentState() {
    	
    	//getting the map associating each sub component id to its actions
    	Map<String, Set<Action>> subCmpIdToActions=
    		getSubComponentIdToActionsMap();

    	Map<String, AbstractButton> sourceIdToButtonMap=
			((ButtonGroupWidget)component).getSourceNameToButtonMap();

    	if(sourceIdToButtonMap.size()>0){
    		
    		Set<Action> actionsSet=null;
    		Component cmp=null;
    		
    		for(String sourceId : sourceIdToButtonMap.keySet()){
    			
    			actionsSet=subCmpIdToActions.get(sourceId);
    			
    			//getting the component associated to the source id
    			cmp=sourceIdToButtonMap.get(sourceId);
    			
    			cmp.setEnabled(actionsSet!=null && 
    					! actionsInactive(actionsSet));
    		}
    	}
    }
    
    @Override
    public void registerListeners() {

    	((ButtonGroupWidget)component).registerListeners();
    }
    
    @Override
    public void unregisterListeners() {

    	((ButtonGroupWidget)component).unregisterListeners();
    }
    
    @Override
    public void dispose() {

    	((ButtonGroupWidget)component).dispose();
    	super.dispose();
    }
}
