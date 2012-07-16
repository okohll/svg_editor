package fr.itris.glips.rtdaeditor.jwidget;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * the component used to choose a font size
 * @author ITRIS, Jordi SUC
 */
public class JWidgetFontSizeChooser extends JSpinner{

	/**
	 * the unit and default value strings
	 */
	protected static String defaultValue=12+"";
	
	/**
	 * the spinner format
	 */
	protected static String spinnerFormat="##########";
	
	/**
	 * the set of the action listeners
	 */
	private Set<ActionListener> actionListeners=new HashSet<ActionListener>();
	
	/**
	 * the current value
	 */
	private String currentValue=defaultValue;
	
	/**
	 * the spinner change listener
	 */
	private ChangeListener spinnerListener;
	
	/**
	 * the caret listener to the spinner textfield
	 */
	private CaretListener caretListener;
	
	/**
	 * the constructor of the class
	 */
	public JWidgetFontSizeChooser() {
		
		super(new SpinnerNumberModel(12, 2, 1000, 1));
		setEditor(new JSpinner.NumberEditor(this, spinnerFormat));
		
		buildWidget();
	}
	
	/**
	 * builds the widget
	 */
	protected void buildWidget() {

		//adding the listener to the spinner changes
		spinnerListener=new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {

				handleEvent();
			}
		};
		
		addChangeListener(spinnerListener);
		
		//adding the listener to the changes in the spinner textfield
		caretListener=new CaretListener() {
			
			public void caretUpdate(CaretEvent e) {
				
				handleEvent();
			}
		};
		
		((JSpinner.DefaultEditor)getEditor()).getTextField().addCaretListener(caretListener);
	}
	
	/**
	 * handles what must be done in the listeners
	 */
	protected void handleEvent() {
		
		String value=((JSpinner.DefaultEditor)getEditor()).getTextField().getText();
		
		if(currentValue==null || (value!=null && ! value.equals("") && ! value.equals(currentValue))) {
			
			currentValue=value;
			notifyChanges();
		}
	}

	/**
	 * @return Returns the currentValue.
	 */
	public String getCurrentValue() {
		return currentValue;
	}
	
	/**
	 * sets the new value
	 * @param newValue the new value
	 */
	public void setCurrentValue(String newValue) {
		
		if(newValue!=null && ! newValue.equals("")) {
			
			currentValue=newValue;
			
			//computing the integer value of the string
			int value=12;
			
			try {
				value=Integer.parseInt(newValue);
			}catch (Exception ex) {}
			
			setValue(value);
		}
	}

	/**
	 * adds a new action listener to the component
	 * @param listener a new action listener to the component
	 */
	public void addActionListener(ActionListener listener) {
		
		if(listener!=null) {
			
			actionListeners.add(listener);
		}
	}
	
	/**
	 * removes an action listener to the component
	 * @param listener an action listener to the component
	 */
	public void removeActionListener(ActionListener listener) {
		
		if(listener!=null) {
			
			actionListeners.remove(listener);
		}
	}
	
	/**
	 * notifies changes on the component
	 */
	protected void notifyChanges() {
		
		ActionEvent evt=new ActionEvent(this, 0, "");
		
		for(ActionListener listener : new HashSet<ActionListener>(actionListeners)) {
			
			listener.actionPerformed(evt);
		}
	}
	
	/**
	 * disables the listeners to the components
	 */
	public void disableListeners() {
		
		((JSpinner.DefaultEditor)getEditor()).getTextField().removeCaretListener(caretListener);
		removeChangeListener(spinnerListener);
	}
	
	/**
	 * enables the listeners to the components
	 */
	public void enableListeners() {
		
		((JSpinner.DefaultEditor)getEditor()).getTextField().addCaretListener(caretListener);
		addChangeListener(spinnerListener);
	}
	
	/**
	 * disposes this component
	 */
	public void dispose() {
		
		actionListeners.clear();
		((JSpinner.DefaultEditor)getEditor()).getTextField().removeCaretListener(caretListener);
		removeChangeListener(spinnerListener);
	}
}
