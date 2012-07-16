package fr.itris.glips.rtdaeditor.anim.widgets;

import javax.swing.*;
import fr.itris.glips.rtdaeditor.anim.*;
import java.awt.event.*;
import java.util.*;

/**
 * the class of the event chooser
 * @author ITRIS, Jordi SUC
 */
public class Combo extends Widget{

	/**
	 * the combo
	 */
	private JComboBox combo=new JComboBox();
	
	/**
	 * the combo listener
	 */
	private ActionListener comboListener;
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected Combo(boolean isEditor){
		
		super(isEditor);
		buildWidget();
	}
	
	/**
	 * builds the widget
	 */
	protected void buildWidget(){
		
		//setting the properties for the combo
		combo.setOpaque(false);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(combo);

		if(isEditor) {
			
			//adding the listener to the combo
			comboListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {
					
					if(getItem()!=null && combo.getSelectedItem()!=null){
						
						ComboListItem comboItem=(ComboListItem)combo.getSelectedItem();
						
						if(comboItem!=null){
							
							getItem().setValue(comboItem.getValue().toString());
						}
					}
					
					getItem().validateChanges();
					
					//validating
					if(validateRunnable!=null) {
						
						validateRunnable.run();
					}
				}
			};
		}
	}
	
	@Override
	protected void setItem(EditableItem item, Runnable validateRunnable){

		super.setItem(item, validateRunnable);
		
		if(isEditor) {
			
			//removing the listener
			combo.removeActionListener(comboListener);
			
			//clearing the combo box
			combo.removeAllItems();
			
			//getting the current value
			String currentValue=item.getValue();
			
			if(currentValue==null) {
				
				currentValue="";
			}
			
			//getting the value that should be selected
			String valueToSelect=currentValue;
			
			if(valueToSelect.equals("")) {
				
				valueToSelect=item.getDefaultValue();
			}
			
			//if no default value exists the empty item is used
			ComboListItem emptyItem=new ComboListItem("", "");
			combo.addItem(emptyItem);
			
			//getting the map of the possible values
			Map<String, String> possibleValues=item.getPossibleValues();
			
			if(possibleValues!=null){
				
				//filling the combo
				String label="", value="";
				ComboListItem comboItem=null, selectedComboItem=emptyItem;
			
				for(String name : possibleValues.keySet()){
					
					try{
						label=item.getAnimationObject().getLabel(name);
					}catch (Exception ex){label=name;ex.printStackTrace();}
					
					value=possibleValues.get(name);
					comboItem=new ComboListItem(value, label);
					
					if(currentValue.equals(value)){
						
						selectedComboItem=comboItem;
					}
					
					combo.addItem(comboItem);
				}
				
				//setting the new selected item
				combo.setSelectedItem(selectedComboItem);
			}
			
			combo.addActionListener(comboListener);
			
		}else {
			
			//showing the value of the item in the combo
			combo.removeAllItems();
			
			String value=item.getValue();
			String label=value;
			
			if(! value.equals("")){
				
				try{
					label=item.getAnimationObject().getLabel("item_"+value);
				}catch (Exception ex){}
			}

			combo.addItem(new ComboListItem(value, label));
		}
	}

}
