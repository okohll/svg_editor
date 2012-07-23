package fr.itris.glips.library.widgets;

import javax.swing.*;
import fr.itris.glips.library.*;
import java.awt.event.*;
import java.awt.*;

/**
 * the widget used to chose a duration
 * @author ITRIS, Jordi SUC
 */
public class TimeChooserWidget extends Widget{

	/**
	 * the array of the possible values for the combo
	 */
	public static String[] timeUnits={"year", "month", "day", "hour", "min", "sec", "ms"};/***TODO***/
	
	/**
	 * the prefix string for all the labels
	 */
	protected static String labelPrefix="JWidgetTimeChooser_";
	
	/**
	 * the spinner used to choose a number
	 */
	private IntegerSpinnerWidget spinner;
	
	/**
	 * the current spinner value
	 */
	private String spinnerValue="";
	
	/**
	 * the listener to the spinner
	 */
	private ActionListener spinnerListener;
	
	/**
	 * the combo used to choose the unit of the duration
	 */
	private JComboBox combo;
	
	/**
	 * the current combo value
	 */
	private String comboValue="";
	
	/**
	 * the listener to the combo box
	 */
	private ActionListener comboListener;
	
	/**
	 * the constructor of the class
	 */
	public TimeChooserWidget(){
		
		build();
	}

	/**
	 * initializes the widget with the given value
	 * @param value a new value
	 */
	public void init(String value){
		
		//removing the listeners to the widgets
		spinner.removeListener(spinnerListener);
		combo.removeActionListener(comboListener);
		
		if(value!=null && ! value.equals("")){
			
			//getting the index of the space character
			value=value.trim();
			int index=value.indexOf(" ");
			
			//getting the value for the spinner
			spinnerValue=value.substring(0, index);
			spinnerValue=spinnerValue.trim();
			
			//setting the new value for the spinner
			try{
				spinner.init(Integer.parseInt(spinnerValue));
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
			
			//getting the value for the combo
			comboValue=value.substring(index+1, value.length());
			comboValue=comboValue.trim();
			
			//computing the index in the combo corresponding to the given value
			int i=0;
			
			for(String val : timeUnits){
				
				if(val.equals(comboValue)){
					
					//selecting the corresponding combo item
					combo.setSelectedIndex(i);
					break;
				}
				
				i++;
			}
		}
		
		//adding the listeners to the widgets
		spinner.addListener(spinnerListener);
		combo.addActionListener(comboListener);
	}
	
	@Override
	public void dispose(){
		
		super.dispose();
		spinner.removeListener(spinnerListener);
		combo.removeActionListener(comboListener);
	}
	
	@Override
	protected void build(){
		
		//creating the spinner
		spinner=new IntegerSpinnerWidget(1, 0, 1000000, 1);
		spinner.setPreferredSize(new Dimension(45,16));
		
		//creating the listener to the spinner editor
		spinnerListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
	
				spinnerValue=spinner.getWidgetValue()+"";
				notifyListeners();
			}
		};
		
		//creating the combo box items
		ComboItem[] items=new ComboItem[timeUnits.length];
		int i=0;
		
		for(String value : timeUnits){
			
			items[i++]=new ComboItem(value);
		}
		
		//creating the combo box
		combo=new JComboBox(items);
		combo.setFont(FontStore.smallFont);
		
		//creating the listener to the combo box
		comboListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				comboValue="";
				
				if(combo.getSelectedItem()!=null){
					
					comboValue=((ComboItem)combo.getSelectedItem()).getValue();
				}
				
				notifyListeners();
			}
		};
		
		//adding the widgets to the time chooser widget
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(spinner);
		add(Box.createHorizontalStrut(2));
		add(combo);
	}
	
	@Override
	public void takeFocus() {
		
		spinner.takeFocus();
	}
	
	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
		
		spinner.setEnabled(enable);
		combo.setEnabled(enable);
	}
	
	@Override
	public String getValue(){
		
		return spinnerValue+" "+comboValue;
	}
	
	/**
	 * the class of the combo items
	 * @author ITRIS, Jordi SUC
	 */
	protected class ComboItem{
		
		/**
		 * the value and the label of the combo item
		 */
		private String value="", label="";
		
		/**
		 * the constructor of the class
		 * @param value the value of the combo item
		 */
		private ComboItem(String value){
			
			this.value=value;
			
				label=Bundle.bundle.getString(labelPrefix+value);
		}
		
		/**
		 * @return the value of the combo item
		 */
		public String getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return label;
		}
	}
}
