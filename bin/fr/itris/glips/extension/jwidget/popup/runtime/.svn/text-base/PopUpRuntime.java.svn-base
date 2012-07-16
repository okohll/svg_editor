package fr.itris.glips.extension.jwidget.popup.runtime;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.base.runtime.*;
import fr.itris.glips.extension.jwidget.base.runtime.actions.*;
import fr.itris.glips.extension.jwidget.popup.edition.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.components.picture.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fr.itris.glips.rtda.animaction.Action;

/**
 * the class of the view browser runtime jwidget
 * @author ITRIS, Jordi SUC
 */
public class PopUpRuntime extends JWidgetRuntime{

	/**
	 * the jwidget id
	 */
	public static String jwidgetId="PopUpMenuWidget";
	
	/**
	 * the container of the menu bar
	 */
	private MenuItemsContainerWidget menuItemsContainerWidget;
	
	/**
	 * the mouse listener
	 */
	private MouseListener mouseListener=null;
	
    /**
     * the constructor of the class
     * @param picture the svg picture
     * @param element the svg element defining the jwidget
     */
    public PopUpRuntime(SVGPicture picture, Element element){
		
		super(picture, element);
	}
	
    @Override
    public void initialize() {

    	menuItemsContainerWidget=new MenuItemsContainerWidget(this, element);
    	
    	//adding a listener to the canvas so that the pop up menu is shown 
    	//when clicking on a canvas zone defined by the previously computed bounds
    	mouseListener=new MouseAdapter(){
    		
    		@Override
    		public void mousePressed(MouseEvent evt) {
    			
    	    	//whether this event is a popup event
    	    	boolean isPopupEvent=(evt.isPopupTrigger() || SwingUtilities.isRightMouseButton(evt));

    			if(isPopupEvent && componentBounds.contains(evt.getPoint())){
    				
    				((JPopupMenu)menuItemsContainerWidget.getComponent()).show(
    						picture.getPictureComponent(), evt.getPoint().x, evt.getPoint().y);
    			}
    		}
    	};
    	
    	picture.getCanvas().addMouseListener(mouseListener);
    }
    
    @Override
    public void handleBounds() {
    	
    	computeBounds();
    }

    @Override
    public Action createAction(Element actionElement) {
    	
    	Action action=null;
    	
    	if(actionElement!=null) {
    		
    		String tagName=actionElement.getTagName();
    		
    		//getting the source id of the action
            String sourceId=actionElement.getAttribute(
            		jwidgetSourceAttributeName);
            
            //getting the component corresponding to this sourceId
            JComponent cmp=menuItemsContainerWidget.getItem(sourceId);

            if(cmp!=null){
    			
    	   		if(tagName.equals("rtda:simpleSendCommand")) {
        			
        			action=new MenuItemSimpleSendCommand(
        				picture, projectName, picture.getCanvas(), 
        				cmp, null, actionElement, this);
    			
    	   		}else if(tagName.equals("rtda:sendCommand")) {
        			
        			action=new MenuItemSendCommand(
        				picture, projectName, picture.getCanvas(), 
        				cmp, null, actionElement, this);
        			
        		}else if(tagName.equals("rtda:sendMeasure")) {
        			
        			action=new MenuItemSendMeasure(picture, 
        				projectName, picture.getCanvas(), cmp, null, actionElement, this);
           			
        		}else if(tagName.equals("rtda:loadView")) {
        			
        			action=new MenuItemLoadView(picture, 
        				projectName, picture.getCanvas(), cmp, null, actionElement, this);
        			
        		}else if(tagName.equals("rtda:runApplication")) {
        			
        			action=new MenuItemRunApplication(picture, 
        				projectName, picture.getCanvas(), cmp, null, actionElement, this);
        		
        		}else {
        			
        			action=super.createAction(actionElement);
        		}
    		}
    	}
    	
    	return action;
    }
    
    @Override
    public void registerListeners() {

    	menuItemsContainerWidget.registerListeners();
    }
    
    @Override
    public void unregisterListeners() {

    	menuItemsContainerWidget.unregisterListeners();
    }
    
    @Override
    public void dispose() {

    	menuItemsContainerWidget.dispose();
    	picture.getCanvas().removeMouseListener(mouseListener);
    	super.dispose();
    }
    
    /**
     * @return the jwidget edition class linked to this jwidget runtime class
     */
    public static Class<?> getEditionClass(){
    	
    	return PopUpEdition.class;
    }
    
    @Override
    public void refreshComponentState() {
    	
    	//getting the map associating each sub component id to its actions
    	Map<String, Set<Action>> subCmpIdToActions=
    		getSubComponentIdToActionsMap();
    	Map<String, JMenuItem> sourceIdToMenuItem=
    		menuItemsContainerWidget.getMenuItems();
    	
    	if(sourceIdToMenuItem.size()>0){
    		
    		Set<Action> actionsSet=null;
    		Component cmp=null;
    		
    		for(String sourceId : sourceIdToMenuItem.keySet()){
    			
    			actionsSet=subCmpIdToActions.get(sourceId);
    			
    			//getting the component associated to the source id
    			cmp=sourceIdToMenuItem.get(sourceId);

    			cmp.setEnabled((cmp instanceof JMenu) ||
    					(actionsSet!=null && ! actionsInactive(actionsSet)));
    		}
    	}
    }
}
