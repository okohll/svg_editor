package fr.itris.glips.rtda;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import fr.itris.glips.rtda.resources.RtdaResources;
import fr.itris.glips.rtda.action.svg.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import org.w3c.dom.*;

/**
 * the class handling the actions that can be found over canvases 
 * or other components
 * @author ITRIS, Jordi SUC
 */
public class ActionsHandler {

	/**
	 * the cursor for the active zones and the default cursor
	 */
	private static final Cursor activeZoneCursor=Cursor.getPredefinedCursor(Cursor.HAND_CURSOR), 
			defaultCursor=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    /**
     * the color of the messages that will be displayed
     */
    public static Color messageColor=new Color(255, 230, 230);
    
	/**
	 * the font
	 */
	public static final Font theFont=new Font("theFont", Font.ROMAN_BASELINE, 12);
	
    /**
     * the handler of the animations
     */
    private AnimationsHandler animationsHandler;

	/**
	 * the map associating a svg picture to a map associating an element to its related actions
	 */
	private Map<SVGPicture, Map<Element, LinkedList<Action>>> pictureToElementToActions=
			new HashMap<SVGPicture, Map<Element, LinkedList<Action>>>();
	
	/**
	 * the map associating a canvas to a set of key actions
	 */
	private Map<SVGPicture, LinkedList<Action>> pictureToKeyActions=
		new HashMap<SVGPicture, LinkedList<Action>>();
	
	/**
	 * the events listener
	 */
	private final EventsListener eventsListener=new EventsListener();

    /**
     * the constructor of the class
     * @param animationsHandler the handler of the animations
     */
    public ActionsHandler(AnimationsHandler animationsHandler){
        
        this.animationsHandler=animationsHandler;
    }
    
    /**
     * adds a new action to handle
     * @param action an action
     */
    public void addAction(Action action) {
    	
    	if(action!=null && action.getActionComponent() instanceof SVGCanvas && 
				action.getEventsManager() instanceof SVGEventsManager) {

			SVGPicture picture=action.getPicture();
			SVGEventsManager eventsManager=(SVGEventsManager)action.getEventsManager();
			
			if(eventsManager.isKeyAction()){
				
    			//if the action is a key action, it is added to the map of the key actions
				LinkedList<Action> actions=pictureToKeyActions.get(picture);
    			
        		if(actions==null) {
        			
        			actions=new LinkedList<Action>();
        			pictureToKeyActions.put(picture, actions);
        			picture.getCanvas().addKeyListener(eventsListener);
        		}
        		
        		if(! actions.contains(action)){
        			
            		actions.add(action);
        		}

			}else{
				
	 			Map<Element, LinkedList<Action>> elementToActions=
	 				pictureToElementToActions.get(picture);
    			
    			if(elementToActions==null){
    				
    				//creating the map
    				elementToActions=new HashMap<Element, LinkedList<Action>>();
    				pictureToElementToActions.put(picture, elementToActions);
    				
    				//adding the listeners
    				picture.getCanvas().addMouseListener(eventsListener);
    				picture.getCanvas().addMouseMotionListener(eventsListener);
    			}
    			
    			LinkedList<Action> actions=elementToActions.get(action.getActionObject());
    			
        		if(actions==null) {
        		
        			//creating the set of actions
        			actions=new LinkedList<Action>();
        			elementToActions.put((Element)action.getActionObject(), actions);
        		}
        		
        		if(! actions.contains(action)){
        			
            		actions.add(action);
        		}
			}
    	}
    }
    
    /**
     * removes the given action from the handler
     * @param action an action
     */
    public void removeAction(Action action) {
    	
    	if(action!=null && action.getActionComponent() instanceof SVGCanvas && 
				action.getEventsManager() instanceof SVGEventsManager) {

			SVGPicture picture=action.getPicture();
			SVGEventsManager eventsManager=(SVGEventsManager)action.getEventsManager();

    		if(eventsManager.isKeyAction()) {
    			
    			//if the action is a key action, it is removed from the map of the key actions
    			LinkedList<Action> actions=pictureToKeyActions.get(picture);
    			
        		if(actions!=null) {

        			actions.remove(action);

        			if(actions.size()==0) {
        				
        				pictureToKeyActions.remove(picture);
        				picture.getCanvas().removeKeyListener(eventsListener);
        			}
        		}
        		
    		}else{
    			
    			Map<Element, LinkedList<Action>> elementToActions=
    				pictureToElementToActions.get(picture);
    			
    			if(elementToActions!=null){

    				//getting the set of the actions
    				LinkedList<Action> actions=elementToActions.get(action.getActionObject());
        			
        			if(actions!=null) {
        				
        				actions.remove(action);
        				
        				if(actions.size()==0) {
        					
        					//removing the canvas entry from the map
        					pictureToElementToActions.remove(picture);
        					
            				//removing the listeners
                    		action.getActionComponent().removeMouseListener(eventsListener);
                    		action.getActionComponent().removeMouseMotionListener(eventsListener);
        				}
        			}
    			}
    		}
    	}
    }
    
    /**
     * the class of the listener of the events that can be made on registered components
     * @author ITRIS, Jordi SUC
     */
    protected final class EventsListener extends MouseMotionAdapter implements MouseListener, KeyListener{
    	
    	/**
    	 * the map associating a modifier to an other one
    	 */
    	protected final Map<Integer, Integer> modifiersMap=new HashMap<Integer, Integer>();
    
    	/**
    	 * the label defining that the action is not entitled and/or not authorized
    	 */
    	protected String notAuthorizedLabel="", notEntitledLabel="", notEntitledAndnotAuthorizedLabel="";
    	
    	/**
    	 * the constructor of the class
    	 */
    	protected EventsListener() {

    		modifiersMap.put(InputEvent.ALT_GRAPH_MASK, InputEvent.ALT_GRAPH_DOWN_MASK);
    		modifiersMap.put(InputEvent.ALT_MASK, InputEvent.ALT_DOWN_MASK);
    		modifiersMap.put(InputEvent.BUTTON1_MASK, InputEvent.BUTTON1_DOWN_MASK);
    		modifiersMap.put(InputEvent.BUTTON2_MASK, InputEvent.BUTTON2_DOWN_MASK);
    		modifiersMap.put(InputEvent.BUTTON3_MASK, InputEvent.BUTTON3_DOWN_MASK);
    		modifiersMap.put(InputEvent.CTRL_MASK, InputEvent.CTRL_DOWN_MASK);
    		modifiersMap.put(InputEvent.META_MASK, InputEvent.META_DOWN_MASK);
    		modifiersMap.put(InputEvent.SHIFT_MASK, InputEvent.SHIFT_DOWN_MASK);
    		
    		//getting the labels
    		notEntitledLabel=RtdaResources.bundle.getString("tooltip_notEntitled");
    		notAuthorizedLabel=RtdaResources.bundle.getString("tooltip_notAuthorized");
    		notEntitledAndnotAuthorizedLabel=RtdaResources.bundle.getString("tooltip_notEntitledAndnotAuthorized");
    	}
    	
    	/**
    	 * performs the action
    	 * @param component the source component
    	 * @param mousePosition the mouse position while the event occured
    	 * @param eventType the type of the event
    	 * @param modifiers the modifiers
    	 */
    	protected void performMouseAction(JComponent component, 
    			Point mousePosition, int eventType, int modifiers) {
    		
    		if(canListen()) {

        		//normalizing the modifiers
        		modifiers=checkModifiers(modifiers);

        		LinkedList<Action> allActions=new LinkedList<Action>();
				SVGCanvas canvas=(SVGCanvas)component;
				SVGPicture picture=canvas.getPicture();
				
				//getting the element corresponding to the event mouse position
				Element element=(Element)AnimationsToolkit.getNodeAt(
						picture, mousePosition, true);
				
				if(element!=null) {
					
					//getting the root element of the document
					Element rootElement=
						element.getOwnerDocument().getDocumentElement();
					
					Map<Element, LinkedList<Action>> elementToActions=
						pictureToElementToActions.get(picture);
					
					if(elementToActions!=null) {

						//getting one the parent elements of this element or 
						//this element that has an action registered on it
						LinkedList<Action> currentActions=new LinkedList<Action>();
						
						while(element!=null && 
								element!=rootElement){
							
							currentActions=elementToActions.get(element);
							
							if(currentActions!=null){
								
								allActions.addAll(currentActions);
							}
							
							element=(Element)element.getParentNode();
						}
					}

					if(allActions.size()>0){
						
		    			canvas.setCursor(activeZoneCursor);
		    			
					}else{
						
		    			canvas.setCursor(defaultCursor);
					}
					
				}else {
					
	    			canvas.setCursor(defaultCursor);
				}
    			
    			if(allActions.size()>0) {
    				
    				if(eventType==SVGEventsManager.MOUSE_MOVE) {
    					
            			String tooltipText="";
        				
    					//for each action, checks if the mouse event 
            			//corresponds to it, and then performs this action
    					int i=0;
    					SVGEventsManager eventsManager=null;
    					boolean cannotExecuteAction=false;//TODO
    					String inactiveLabel="";
    					
    					for(Action action : allActions) {
    						
    						eventsManager=(SVGEventsManager)action.getEventsManager();
    						
    						if(eventsManager.isEvent(eventType, modifiers, -1)) {
    							
    							eventsManager.performAction(eventType, modifiers, -1);
    						}
    						
    						i++;
    						cannotExecuteAction=! action.isAuthorized() || ! action.isEntitled();
    						
    						//getting the inactive label
    						inactiveLabel="";
    						
    						if(action.isEntitled() && ! action.isAuthorized()){
    							
    							inactiveLabel=notAuthorizedLabel;
    							
    						}else if(! action.isEntitled() && action.isAuthorized()){
    							
    							inactiveLabel=notEntitledLabel;
    							
    						}else if(! action.isEntitled() && ! action.isAuthorized()){
    							
    							inactiveLabel=notEntitledAndnotAuthorizedLabel;
    						}
    						
    						tooltipText+=(cannotExecuteAction?"<span style=\"text-decoration:line-through;\">"
    							+action.getTooltipText()+"</span><small>"
    								+inactiveLabel+"</small>":action.getTooltipText())+
    									(i<allActions.size()?"<br>":"");
    					}
    					
    					//displaying the tool tip
    					if(! tooltipText.equals("")) {
    						
    						tooltipText="<html><body>"+tooltipText+"</body></html>";
    						
    						component.setToolTipText(tooltipText);
    		            	JToolTip toolTip=component.createToolTip();
    		            	toolTip.setLocation(mousePosition);
    		            	toolTip.setTipText(tooltipText);
    		            	
    					}else{
    						
    						component.setToolTipText(null);
    					}
    					
    				}else {
    					
    					SVGEventsManager eventsManager=null;
    					
    					for(Action action : allActions) {
    						
    						eventsManager=(SVGEventsManager)action.getEventsManager();
    						
    						if(eventsManager.isEvent(eventType, modifiers, -1)) {
    							
    							eventsManager.performAction(eventType, modifiers, -1);
    						}
    					}
    				}
    				
    			}else{
    				
    				component.setToolTipText(null);
    			}
    		}
    	}
    	
    	/**
    	 * performs a key action
    	 * @param component the source component
    	 * @param eventType the type of the event
    	 * @param modifiers the modifiers
    	 * @param eventKey the key of the event
    	 */
    	protected void performKeyAction(JComponent component, int eventType, int modifiers, int eventKey) {
    		
    		if(canListen()) {
    			
        		//normalizing the modifiers
        		modifiers=checkModifiers(modifiers);
        		
        		//getting the action set corresponding to the given component
        		LinkedList<Action> actions=pictureToKeyActions.get(
        				((SVGCanvas)component).getPicture());
        		SVGEventsManager eventsManager=null;
        		
        		for(Action action : actions) {
        			
        			eventsManager=(SVGEventsManager)action.getEventsManager();
        			
        			if(eventsManager.isEvent(eventType, modifiers, eventKey)) {
        				
        				eventsManager.performAction(eventType, modifiers, eventKey);
        			}
        		}
    		}
    	}
    	
    	/**
    	 * normalizes the modifiers
    	 * @param modifiers modifiers
    	 * @return the normalized modifiers
    	 */
    	protected int checkModifiers(int modifiers) {

    		int mod=modifiers;

    		for(int md : modifiersMap.keySet()) {

    			if((mod & md)==md) {

    				mod=mod-md+modifiersMap.get(md);
    			}
    		}

    		return mod;
    	}
    	
    	@Override
    	public void mouseMoved(MouseEvent evt) {

    		if(canListen()) {
    			
        		performMouseAction((JComponent)evt.getSource(), 
        			evt.getPoint(), SVGEventsManager.MOUSE_MOVE, evt.getModifiers());
    		}    		
    	}
    	
    	/**
    	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
    	 */
    	public void mouseClicked(MouseEvent evt) {
    		
    		performMouseAction((JComponent)evt.getSource(), evt.getPoint(), 
    				SVGEventsManager.CLICK, evt.getModifiers());
    	}
    	
    	/**
    	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
    	 */
    	public void mouseEntered(MouseEvent evt) {
    
    		performMouseAction((JComponent)evt.getSource(), evt.getPoint(), 
    				SVGEventsManager.MOUSE_OVER, evt.getModifiers());
    	}
    	
    	/**
    	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
    	 */
    	public void mouseExited(MouseEvent evt) {

    		performMouseAction((JComponent)evt.getSource(), evt.getPoint(), 
    				SVGEventsManager.MOUSE_OVER, evt.getModifiers());
    	}
    	
    	/**
    	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
    	 */
    	public void mousePressed(MouseEvent evt) {

    		performMouseAction((JComponent)evt.getSource(), evt.getPoint(), 
    				SVGEventsManager.MOUSE_DOWN, evt.getModifiers());
    	}
    	
    	/**
    	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
    	 */
    	public void mouseReleased(MouseEvent evt) {

    		performMouseAction((JComponent)evt.getSource(), evt.getPoint(), 
    				SVGEventsManager.MOUSE_UP, evt.getModifiers());
    	}
    	
    	/**
    	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
    	 */
    	public void keyPressed(KeyEvent evt) {

    		performKeyAction((JComponent)evt.getSource(), 
    				SVGEventsManager.KEY_DOWN, evt.getModifiers(), evt.getKeyCode());
    	}
    	
    	/**
    	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
    	 */
    	public void keyReleased(KeyEvent evt) {

    		performKeyAction((JComponent)evt.getSource(), 
    				SVGEventsManager.KEY_UP, evt.getModifiers(), evt.getKeyCode());
    	}
    	
    	/**
    	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
    	 */
    	public void keyTyped(KeyEvent evt) {

    		performKeyAction((JComponent)evt.getSource(), 
    				SVGEventsManager.KEY_TYPED, evt.getModifiers(), evt.getKeyCode());
    	}
    	
    	/**
    	 * @return whether the listener can listen to events
    	 */
    	protected boolean canListen() {
    		
			return ! (animationsHandler.isStopped() || animationsHandler.isPaused());
    	}
    }
}
