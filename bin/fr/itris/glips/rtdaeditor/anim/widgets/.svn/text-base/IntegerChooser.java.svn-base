package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtdaeditor.anim.*;

/**
 * the class of the number chooser
 * @author ITRIS, Jordi SUC
 */
public class IntegerChooser extends Widget{

	/**
	 * the spinner
	 */
	private JSpinner spinner;
	
	/**
	 * the spinner model
	 */
	private final SpinnerNumberModel spinnerModel=
		new SpinnerNumberModel(0, 0, 100000000, 1);
	
	/**
	 * the number editor
	 */
	private JTextField numberEditor;
	
	/**
	 * the spinner change listener
	 */
	private ChangeListener changeListener=null;
	
	/**
	 * the spinner caret listener
	 */
	private CaretListener caretListener=null;

	/**
	 * the focus listener of the spinner
	 */
	private FocusAdapter spinnerFocusListener=null;
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected IntegerChooser(boolean isEditor){
		
		super(isEditor);
		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){

		//handling the spinner
		spinner=new JSpinner(spinnerModel);
		spinner.setPreferredSize(new Dimension(100, 18));
		spinner.setBorder(new EmptyBorder(0, 0, 0, 0));
		spinner.setOpaque(false);
		numberEditor=((JSpinner.NumberEditor)spinner.getEditor()).getTextField();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(spinner);
		
		if(isEditor) {
			
			//the spinner listener
			changeListener=new ChangeListener() {
				
				public void stateChanged(ChangeEvent evt) {
				
					getItem().setValue(spinner.getValue().toString());
					getItem().validateChanges();
				}
			};
			
			//the spinner caret listener
			caretListener=new CaretListener(){
				
				public void caretUpdate(CaretEvent e) {
					
					getItem().setValue(numberEditor.getText());
					getItem().validateChanges();
				}
			};

			//the focus listener for the spinner
			spinnerFocusListener=new FocusAdapter(){
		    	
		    	@Override
		    	public void focusLost(FocusEvent evt) {

	            	validateRunnable=null;
		            spinner.removeFocusListener(this);
		    	}
		    };
		}
	}
	
	@Override
	protected void setItem(EditableItem item, Runnable validateRunnable){

		super.setItem(item, validateRunnable);
		
		if(isEditor) {
			
			spinner.removeChangeListener(changeListener);
			numberEditor.removeCaretListener(caretListener);
			spinner.removeFocusListener(spinnerFocusListener);
		}
		
		//getting the default value
		int defaultValue=0;
		
		try{
			defaultValue=Integer.parseInt(item.getDefaultValue());
		}catch (Exception ex){defaultValue=0;}
		
		//getting the default value
		int value=-1;
		
		try{
			value=Integer.parseInt(item.getValue());
		}catch (Exception ex){}
		
		spinner.getModel().setValue(value==-1?defaultValue:value);
			numberEditor.setText(FormatStore.displayFormat.format(
				spinner.getModel().getValue()));
		
		if(isEditor){
			
			spinner.addChangeListener(changeListener);
			numberEditor.addCaretListener(caretListener);
			spinner.addFocusListener(spinnerFocusListener);
		}
	}
}
