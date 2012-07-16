package fr.itris.glips.library.widgets;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.*;

/**
 * the superclass of widgets
 * @author ITRIS, Jordi SUC
 */
public abstract class Widget extends JComponent{

	/**
	 * the spinners format
	 */
	protected static String spinnersFormat="##########.#################";
	
	/**
	 * the set of the action listeners listening to the widget
	 */
	protected Set<ActionListener> actionListeners=new HashSet<ActionListener>();
	
	/**
	 * the current value
	 */
	protected String currentValue="";
	
	/**
	 * the constructor of the class
	 */
	public Widget() {
		
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}
	
	/**
	 * builds the widget
	 */
	protected void build(){
		
	}
	
	/**
	 * @return the value the user selected in this widget
	 */
	public String getValue(){
		
		return currentValue;
	}
	
	/**
	 * disposing this widget
	 */
	public void dispose(){
		
		actionListeners.clear();
	}
	
	/**
	 * registers a new listener on this widget
	 * @param listener a listener
	 */
	public void addListener(ActionListener listener){
		
		if(listener!=null){
			
			actionListeners.add(listener);
		}
	}
	
	/**
	 * unregisters the given listener on this widget
	 * @param listener a listener
	 */
	public void removeListener(ActionListener listener){
		
		if(listener!=null){
			
			actionListeners.remove(listener);
		}
	}
	
	/**
	 * notifies all the listeners that the value of the widget has changed
	 */
	protected void notifyListeners(){
		
		for(ActionListener listener : actionListeners){
			
			listener.actionPerformed(new ActionEvent(this, 1, ""));
		}
	}
	
	/**
	 * grabs the focus for a widget in this component
	 */
	public void takeFocus(){}
}
