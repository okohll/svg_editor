package fr.itris.glips.rtda.action.svg;

import java.util.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.animaction.eventsmanager.*;

/**
 * the events manager for the actions on svg shapes
 * @author Jordi SUC
 */
public class SVGEventsManager extends EventsManager{
	
	/**
	 * the separators
	 */
	protected static final String elementSeparator=":", itemsSeparator=";";

	/**
	 * the mouseUp event constant
	 */
	public static final int MOUSE_UP=0;
	
	/**
	 * the mouseDown event constant
	 */
	public static final int MOUSE_DOWN=1;
	
	/**
	 * the click event constant
	 */
	public static final int CLICK=2;
	
	/**
	 * the mouseOver event constant
	 */
	public static final int MOUSE_OVER=3;
	
	/**
	 * the mouseOut event constant
	 */
	public static final int MOUSE_OUT=4;
	
	/**
	 * the mouseMove event constant
	 */
	public static final int MOUSE_MOVE=5;
	
	/**
	 * the load event constant
	 */
	public static final int LOAD=6;
	
	/**
	 * the keyUp event constant
	 */
	public static final int KEY_UP=7;
	
	/**
	 * the keyDown event constant
	 */
	public static final int KEY_DOWN=8;
	
	/**
	 * the keyTyped event constant
	 */
	public static final int KEY_TYPED=9;
	
	/**
	 * the map associating an event type to a set of the events by which this action is triggered
	 */
	protected HashMap<Integer, Event> events=new HashMap<Integer, Event>();
	
	/**
	 * whether this action is a key action
	 */
	protected boolean isKeyAction;
	
	/**
	 * the constructor of the class
	 * @param action the action this events manager monitors
	 */
	public SVGEventsManager(Action action){
		
		this.action=action;
		computeEvents(action.getActionElement().getAttribute("event"));
	}
	
    /**
     * performs this action given the type of the event (this method should only be used with actions on svg shapes; 
     * it will not be called for jwidgets)
     * @param eventType the type of the event
     * @param modifier the modifier mask (@see java.aw.event.InputEvent for the modifiers codes)
     * @param key the tapped key if the event is a key one (@see java.awt.event.KeyEvent for the key codes)
     */
    public void performAction(int eventType, int modifier, int key){
    	
    	action.performAction(null);
    }
    
    /**
     * returns whether the given event corresponds to one of the events defined in this action
     * @param eventType the type of an event
     * @param eventModifier the modifier
     * @param eventKey the key if the event is a key event
     * @return returns the given event corresponds to one of the events defined in this action
     */
    public boolean isEvent(int eventType, int eventModifier, int eventKey) {

    	boolean isEvent=false;
    	boolean isKey=(eventType==KEY_DOWN || 
    			eventType==KEY_UP || eventType==KEY_TYPED);
    	Event eventObject=events.get(eventType);
    	
    	if(eventObject!=null && eventObject.getModifiers()==eventModifier &&
    			((isKey && eventKey==eventObject.getKey()) || ! isKey)) {
    		
    		isEvent=true;
    	}

    	return isEvent;
    }
    
    /**
     * @return whether this action is a key action
     */
    public boolean isKeyAction() {
    	
    	return isKeyAction;
    }
    
	/**
	 * creates the set of the event objects for which this action should be triggered
	 * @param value the events value
	 */
	protected void computeEvents(String value) {
		
		if(value==null) {
			
			value="";
		}
		
		if(! value.equals("")) {
			
			String[] splitValue=value.split(itemsSeparator);
			int event=-1, modifier=0, key=-1;
			String[] innerItemSplitValue=null;
			LinkedList<String> elements=new LinkedList<String>();
			Event eventObject=null;
			
			for(String itemValue : splitValue) {
				
				if(itemValue!=null) {
					
					//splitting the item's elements
					elements.clear();
					innerItemSplitValue=itemValue.split(elementSeparator);
					
					for(String val : innerItemSplitValue) {
						
						if(! val.equals("")) {
							
							elements.add(val);
						}
					}
					
					if(elements.size()>=3) {
						
						//getting the values for the event item 
						//corresponding to the current element value
						event=-1;
						modifier=0;
						key=-1;
						
						try {
							event=Integer.parseInt(elements.getFirst());
							modifier=Integer.parseInt(elements.get(1));
							key=Integer.parseInt(elements.get(2));
						}catch (Exception ex) {}
						
						//setting the new values for the event item
						if(event!=-1) {
							
							eventObject=new Event(event, modifier, key);
							
							if(eventObject.isKeyAction()) {
								
								isKeyAction=true;
							}
							
							events.put(event, eventObject);
						}
					}
				}
			}
		}
	}
	
	/**
	 * the class of an event type
	 * @author ITRIS, Jordi SUC
	 */
	protected class Event{
		
		/**
		 * the event type
		 */
		protected int eventType=-1;
		
		/**
		 * the modifiers
		 */
		protected int modifiers=-1;
		
		/**
		 * the key
		 */
		protected int key=-1;
		
		/**
		 * the constructor of the class
		 * @param eventType the event type
		 * @param modifiers the modifiers
		 * @param key the key
		 */
		protected Event(int eventType, int modifiers, int key) {
			
			this.eventType=eventType;
			this.modifiers=modifiers;
			this.key=key;
		}

		/**
		 * @return the eventType
		 */
		public int getEventType() {
			return eventType;
		}

		/**
		 * @return the key
		 */
		public int getKey() {
			return key;
		}

		/**
		 * @return the modifiers
		 */
		public int getModifiers() {
			return modifiers;
		}
		
		/**
		 * @return whether this event is a key event
		 */
		public boolean isKeyAction() {
			
			return (eventType==KEY_UP || eventType==KEY_DOWN || eventType==KEY_TYPED);
		}
		
		/**
		 * returns whether the event object defines the same event as the one
		 * defined by the given parameters
		 * @param evtType an event type
		 * @param evtMod an event modifier
		 * @param evtKey an event key
		 * @return whether the event object defines the same event as the one
		 */
		public boolean equals(int evtType, int evtMod, int evtKey) {
			
			return (evtType==eventType && evtMod==modifiers && evtKey==key);
		}
	}
}
