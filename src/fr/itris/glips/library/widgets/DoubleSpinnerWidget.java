package fr.itris.glips.library.widgets;

import javax.swing.*;
import javax.swing.event.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.Toolkit;

import java.awt.*;

/**
 * the class of a spinner widget
 * @author ITRIS, Jordi SUC
 */
public class DoubleSpinnerWidget extends Widget{

	/**
	 * the spinner used to choose a number
	 */
	private JSpinner spinner;
	
	/**
	 * the number editor
	 */
	private JSpinner.NumberEditor numberEditor;
	
	/**
	 * the current spinner value
	 */
	private double spinnerValue=0;
	
	/**
	 * the spinner editor
	 */
	private JTextField spinnerEditor;
	
	/**
	 * the listener to the spinner
	 */
	private ChangeListener changeListener;
	
	/**
	 * the listener to the spinner editor
	 */
	private CaretListener caretListener;
	
	/**
	 * the default value for the spinner
	 */
	private double defaultValue=0;
	
	/**
	 * the constructor of the class
	 * @param defaultValue the default value for the spinner
	 * @param min the minimum value of the spinner
	 * @param max the maximum value of the spinner
	 * @param step the step value of the spinner
	 * @param borderless whether the spinner should have borders
	 */
	public DoubleSpinnerWidget(double defaultValue, double min, 
													double max, double step,
													boolean borderless){
		
		this.defaultValue=defaultValue;
		build(min, max, step);
	}
	
	/**
	 * initializes the widget with the given value
	 * @param objectValue a new value
	 */
	public void init(Object objectValue){
		
		//reinitializing the value of the spinner
		spinnerValue=defaultValue;
		
		//removing the listeners to the widgets
		spinner.removeChangeListener(changeListener);
		spinnerEditor.removeCaretListener(caretListener);
		
		if(objectValue!=null){
			
			spinnerValue=Toolkit.getNumber(objectValue);
			
			if(Double.isNaN(spinnerValue)){
				
				spinnerValue=defaultValue;
			}
		}
		
		//setting the new value for the spinner
		spinner.setValue(new Double(spinnerValue));
		
		//adding the listeners to the widgets
		spinner.addChangeListener(changeListener);
		spinnerEditor.addCaretListener(caretListener);
	}
	
	@Override
	public void dispose(){
		
		super.dispose();
		spinner.removeChangeListener(changeListener);
		spinnerEditor.removeCaretListener(caretListener);
	}
	
	/**
	 * builds the widget
	 * @param min the minimum value of the spinner
	 * @param max the maximum value of the spinner
	 * @param step the step value of the spinner
	 */
	protected void build(double min, double max, double step){
		
		//creating the spinner
		SpinnerNumberModel spinnerModel=new SpinnerNumberModel(min, min, max, step);
		spinner=new JSpinner(spinnerModel);
		numberEditor=new JSpinner.NumberEditor(spinner, spinnersFormat);
		spinner.setEditor(numberEditor);
		spinnerEditor=((JSpinner.NumberEditor)spinner.getEditor()).getTextField();
		spinner.setFont(FontStore.smallFont);
		
		//creating the listener to the spinner
		changeListener=new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				
				try{
					spinnerValue=Double.parseDouble(spinner.getValue().toString());
				}catch (Exception ex){}
				
				notifyListeners();
			}
		};
		
		//creating the listener to the spinner editor
		caretListener=new CaretListener() {
			
			public void caretUpdate(CaretEvent evt) {
				
				try{
					spinnerValue=Double.parseDouble(spinnerEditor.getText());
				}catch (Exception ex){}

				notifyListeners();
			}
		};
		
		//adding the widgets to the time chooser widget
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(spinner);
	}
	
	@Override
	public void setPreferredSize(Dimension newSize){
		
		super.setPreferredSize(newSize);
		
		if(newSize!=null){
			
			spinner.setPreferredSize(newSize);
		}
	}
	
	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
		
		spinner.setEnabled(enable);
	}
	
	/**
	 * @return the value the user selected in this spinner
	 */
	public double getWidgetValue(){
		
		return spinnerValue;
	}
	
	@Override
	public void takeFocus() {

		spinner.grabFocus();
	}
}
