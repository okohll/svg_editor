package fr.itris.glips.extension.jwidget.base.runtime;

import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.animaction.Action;
import fr.itris.glips.rtda.jwidget.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class of the button group widget
 * @author ITRIS, Jordi SUC
 */
public class ButtonGroupWidget extends JToolBar{
	
	/**
	 * the button group runtime object
	 */
	private ButtonGroupRuntime buttonGroupRuntime;
	
	/**
	 * the map associating a button to its listener
	 */
	private LinkedHashMap<AbstractButton, ActionListener> buttonsToListeners=
		new LinkedHashMap<AbstractButton, ActionListener>();
	
	/**
	 * the map associating the source id of a button to 
	 */
	private Map<String, AbstractButton> sourceNameToButtonMap=
		new HashMap<String, AbstractButton>();
	
	/**
	 * the button group manager
	 */
	private ButtonGroup buttonGroup=new ButtonGroup();
	
	/**
	 * the constructor of the class
	 * @param buttonGroupRuntime the button group runtime object
	 * @param jwidgetElement the jwidget element
	 */
	public ButtonGroupWidget(ButtonGroupRuntime buttonGroupRuntime, 
			Element jwidgetElement){
		
		this.buttonGroupRuntime=buttonGroupRuntime;
		initialize(jwidgetElement);
	}
	
	/**
	 * initializes the component
	 * @param jwidgetElement the jwidget element
	 */
	protected void initialize(Element jwidgetElement){
		
		setFloatable(false);
		
		//whether the component is a tool bar or a radio button group
		boolean isToolBar=jwidgetElement.getAttribute(
			JWidgetRuntime.jwidgetNameAttributeName).equals("ToggleButtonBarWidget");

    	//creating the buttons
    	NodeList jwidgetElements=jwidgetElement.getElementsByTagName(
    			JWidgetRuntime.jwidgetTagName);
    	Element el=null;
    	String sourceName="", label="";
    	AbstractButton button=null;
    	ActionListener listener=null;
    	
    	for(int i=0; i<jwidgetElements.getLength(); i++){
    		
    		el=(Element)jwidgetElements.item(i);
    		
    		if(el!=null){
    			
    			label=el.getAttribute(JWidgetRuntime.jwidgetLabelAttributeName);
    			sourceName=el.getAttribute(JWidgetRuntime.jwidgetIdAttributeName);
    			
    			if(isToolBar){
    				
    				button=new JToggleButton(label);
    				
    			}else{
    				
    				button=new JRadioButton(label);
    	    		button.setOpaque(false);
    			}
    			
    			final AbstractButton fbutton=button;
    			
    			listener=new ActionListener(){
    				
    				public void actionPerformed(ActionEvent e) {

    	    			for(Action action : buttonGroupRuntime.getActions()){
    	    				
    	    				if(action.getActionComponent()!=null && 
    	    						action.getActionComponent().equals(fbutton)){

        	    				action.performAction(e);
    	    				}
    	    			}
    				}
    			};

    			button.addActionListener(listener);
    			
    			buttonGroup.add(button);
    			buttonsToListeners.put(button, listener);
    			sourceNameToButtonMap.put(sourceName, button);
    			add(button);
    		}
    	}
    	
		//getting the orientation of the button group component
    	String orientationStr=jwidgetElement.getAttribute("orientation");
    	int orientation=orientationStr.equals("vertical")?
    			SwingConstants.VERTICAL:SwingConstants.HORIZONTAL;
    	
    	/*if(isToolBar){
    		
    		//checking whether all the buttons should have the same size
    		boolean sameSizeForButtons=false;
    		
        	try{
        		sameSizeForButtons=Boolean.parseBoolean(
        				jwidgetElement.getAttribute(JWidgetToolkit.sameSizeForButtonsName));
        	}catch (Exception ex){}
        	
        	if(sameSizeForButtons){
        		
        		//setting the new layout manager for the tool bar
        		switch(orientation){
        		
        			case SwingConstants.HORIZONTAL :
        				
        				setLayout(new GridLayout(buttonsToListeners.size(), 1));
        				break;
        				
        			case SwingConstants.VERTICAL :
        				
        				setLayout(new GridLayout(1, buttonsToListeners.size()));
        				break;
        		}
        		
        	}else{
        		
        		//setting the properties of the component
        		setLayout(new BoxLayout(this, (orientation==SwingConstants.HORIZONTAL)?
        				BoxLayout.X_AXIS:BoxLayout.Y_AXIS));
        	}
    		
    	}else{*/
    		
    		//setting the properties of the component
    		setLayout(new BoxLayout(this, (orientation==SwingConstants.HORIZONTAL)?
    				BoxLayout.X_AXIS:BoxLayout.Y_AXIS));
    	//}
    	
    	//handling the look of the buttons
    	JWidgetToolkit.handleLook(jwidgetElement, 
    		new HashSet<Component>(buttonsToListeners.keySet()));
    	
    	//handling the isOpaque property
    	if(! isToolBar){
    		
    		setBorder(new EmptyBorder(0, 0, 0, 0));
    		setOpaque(false);
    		
    	}else{
    		
        	//getting whether the component should be opaque
        	boolean isOpaque=true;
        	
        	try{
        		isOpaque=Boolean.parseBoolean(
        				jwidgetElement.getAttribute(JWidgetToolkit.isOpaqueName));
        	}catch (Exception ex){}
        	
        	if(! isOpaque){
        		
            	setOpaque(false);
            	setBorder(new EmptyBorder(0, 0, 0, 0));
        	}
    	}
	}
	
	/**
	 * returns the button corresponding to the given source id
	 * @param sourceId a source id
	 * @return the button corresponding to the given source id
	 */
	public AbstractButton getButton(String sourceId){

		return sourceNameToButtonMap.get(sourceId);
	}
	
	/**
	 * @return the map associating the source id of a button to 
	 */
	public Map<String, AbstractButton> getSourceNameToButtonMap() {
		return sourceNameToButtonMap;
	}
	
	/**
	 * @return all the buttons handled by this button group
	 */
	public LinkedList<AbstractButton> getButtons() {
		
		return new LinkedList<AbstractButton>(buttonsToListeners.keySet());
	}
	
	/**
	 * returns whether the given source id represents a valid button
	 * @param sourceId a source id 
	 * @return whether the given source id represents a valid button
	 */
	public boolean containsSourceId(String sourceId){
		
		return sourceNameToButtonMap.containsKey(sourceId);
	}
	
	/**
	 * sets whether the button denoted by the given id should be enabled or not
	 * @param sourceId the id of a menu item
	 * @param enable whether the button denoted by the given id should be enabled or not
	 */
	public void setEnabled(String sourceId, boolean enable){
		
		AbstractButton button=sourceNameToButtonMap.get(sourceId);
		
		if(button!=null){
			
			button.setEnabled(enable);
		}
	}
	
    /**
     * registers all the listeners
     */
    public void registerListeners() {

		ActionListener listener;
		
		for(AbstractButton button : buttonsToListeners.keySet()){
			
			listener=buttonsToListeners.get(button);
			button.removeActionListener(listener);
			button.addActionListener(listener);
		}   	
    }
    
    /**
     * unregisters all the listeners
     */
    public void unregisterListeners() {

		ActionListener listener;
		
		for(AbstractButton button : buttonsToListeners.keySet()){
			
			listener=buttonsToListeners.get(button);
			button.removeActionListener(listener);
		}   	
    }
	
	/**
	 * disposes the component
	 */
	public void dispose(){
		
		unregisterListeners();
	}
}
