package fr.itris.glips.library.widgets;

import javax.swing.*;
import javax.swing.event.*;
import fr.itris.glips.library.*;
import java.awt.*;

/**
 * the class of a spinner widget
 * @author ITRIS, Jordi SUC
 */
public class IntegerSpinnerWidget extends Widget{

	/**
	 * the spinner used to choose a number
	 */
	private JSpinner spinner;
	
	/**
	 * the current spinner value
	 */
	private int spinnerValue=0;
	
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
	private int defaultValue=0;
	
	/**
	 * the constructor of the class
	 * @param defaultValue the default value for the spinner
	 * @param min the minimum value of the spinner
	 * @param max the maximum value of the spinner
	 * @param step the step value of the spinner
	 */
	public IntegerSpinnerWidget(int defaultValue, int min, int max, int step){

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
		currentValue=defaultValue+"";
		
		//removing the listeners to the widgets
		spinner.removeChangeListener(changeListener);
		spinnerEditor.removeCaretListener(caretListener);
		
		if(objectValue!=null){
			
			if(objectValue instanceof Integer){
				
				spinnerValue=(Integer)objectValue;
				
			}else{
				
				String value=objectValue.toString();
				currentValue=value;
				
				if(! value.equals("")){
					
					//getting the number corresponding to the given string
					int val=0;
					
					try{
						val=Integer.parseInt(value);
					}catch (Exception ex){val=defaultValue;}
					
					//getting the value for the spinner
					spinnerValue=val;
				}
			}
		}
		
		//setting the new value for the spinner
		try{
			spinner.setValue(spinnerValue);
		}catch(Exception ex){ex.printStackTrace();}
		
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
	protected void build(int min, int max, int step){
		
		//creating the spinner
		SpinnerNumberModel spinnerModel=new SpinnerNumberModel(min, min, max, step);
		spinner=new JSpinner(spinnerModel);
		spinner.setEditor(new JSpinner.NumberEditor(spinner, spinnersFormat));
		spinnerEditor=((JSpinner.NumberEditor)spinner.getEditor()).getTextField();
		spinner.setFont(FontStore.smallFont);
		
		//creating the listener to the spinner
		changeListener=new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				
				currentValue=spinner.getValue().toString();
				
				try{
					spinnerValue=Integer.parseInt(currentValue);
				}catch (Exception ex){}
				
				notifyListeners();
			}
		};
		
		//creating the listeners to the spinner editor
		caretListener=new CaretListener() {
			
			public void caretUpdate(CaretEvent evt) {
				
				currentValue=spinnerEditor.getText();
				
				try{
					spinnerValue=Integer.parseInt(currentValue);
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
	public int getWidgetValue(){
		
		return spinnerValue;
	}
	
	@Override
	public void takeFocus() {

		spinner.grabFocus();
	}
}
