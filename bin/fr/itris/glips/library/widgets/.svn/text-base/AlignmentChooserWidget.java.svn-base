package fr.itris.glips.library.widgets;

import javax.swing.*;
import fr.itris.glips.library.*;
import java.awt.event.*;

/**
 * the class of the widget used to choose a line dash by means of a combo box
 * @author ITRIS, Jordi SUC
 */
public class AlignmentChooserWidget extends Widget {

	/**
	 * the array of the possible alignment types
	 */
	public static String[] alignments={"left", "center", "right"};
	
	/**
	 * the combo box
	 */
	private JComboBox combo;
	
	/**
	 * the listener to the combo box
	 */
	private ActionListener comboListener;
	
	/**
	 * the constructor of the class
	 */
	public AlignmentChooserWidget() {
		
		build();
	}
	
	@Override
	public void dispose(){
		
		super.dispose();
		combo.removeActionListener(comboListener);
	}
	
	/**
	 * initializes the widget with the given value
	 * @param newValue a new value
	 */
	public void init(String newValue){
		
		//reinitializing the value of the widget
		currentValue="";
		
		if(newValue==null){
			
			newValue="";
		}
		
		//removing the listeners from the widget
		combo.removeActionListener(comboListener);
		
		//selecting the combo item corresponding to the given value
		int i=0;
		
		for(String val : alignments){
			
			if(val.equals(newValue)){
				
				//selecting the corresponding combo item
				combo.setSelectedIndex(i);
				currentValue=alignments[i];
				break;
			}
			
			i++;
		}
	
		//adding the listener to the widget
		combo.addActionListener(comboListener);
	}
	
	@Override
	protected void build(){
		
		//creating the combo box items
		ComboItem[] items=new ComboItem[alignments.length];
		int i=0;
		
		for(String value : alignments){
			
			items[i++]=new ComboItem(value);
		}
		
		//creating the combo box
		combo=new JComboBox(items);
		
		//creating the listener to the combo box
		comboListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				currentValue="";
				
				if(combo.getSelectedItem()!=null){
					
					currentValue=((ComboItem)combo.getSelectedItem()).getValue();
				}
				
				notifyListeners();
			}
		};
		
		//adding the widgets to the time chooser widget
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(combo);
	}
	
	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
		combo.setEnabled(enable);
	}
	
	/**
	 * the class of the combo items
	 * @author ITRIS, Jordi SUC
	 */
	protected class ComboItem{
		
		/**
		 * the value and the label of the combo item
		 */
		private String value="";
		
		/**
		 * the label for the combo item
		 */
		private String label="";
		
		/**
		 * the constructor of the class
		 * @param value the value of the combo item
		 */
		private ComboItem(String value){
			
			this.value=value;
			this.label=Bundle.bundle.getString(value);
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
