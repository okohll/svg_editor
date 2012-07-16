package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the chooser of the target id
 * @author ITRIS, Jordi SUC
 */
public class TargetChooser extends Widget{

	/**
	 * the value for the popup target option
	 */
	public static final String popupTargetOption="popupTarget";
	
	/**
	 * the label for the popup target
	 */
	protected static String popupTargetLabel="";
	
	static{
		
		popupTargetLabel=
			ResourcesManager.bundle.getString("rtdaanim_popupTarget");
	}
	
	/**
	 * the combo
	 */
	private JComboBox combo=new JComboBox();
	
	/**
	 * the combo listener
	 */
	private ActionListener comboListener;
	
	/**
	 * whether the target chooser should be complex
	 */
	private boolean isComplex;
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 * @param isComplex whether the target chooser should be complex
	 */
	protected TargetChooser(boolean isEditor, boolean isComplex){
		
		super(isEditor);
		this.isComplex=isComplex;
		buildWidget();
	}
	
	/**
	 * builds the widget
	 */
	protected void buildWidget(){
		
		//setting the properties for the combo
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

			//getting all the available view browser jwidget ids
			Map<String, String> idsToLabel=EditableItem.getViewBrowsersIds(item.getSVGElement().
					getOwnerDocument().getDocumentElement());

			//filling the combo
			ComboListItem comboItem=null, selectedComboItem=emptyItem;
		
			for(String id : idsToLabel.keySet()){

				comboItem=new ComboListItem(id, idsToLabel.get(id));
				
				if(currentValue.equals(id)){
					
					selectedComboItem=comboItem;
				}
				
				combo.addItem(comboItem);
			}
			
			if(isComplex){
				
				//adding the popup dialog option
				comboItem=new ComboListItem(
						popupTargetOption, popupTargetLabel);
				combo.addItem(comboItem);
				
				if(currentValue.equals(popupTargetOption)){
					
					selectedComboItem=comboItem;
				}
			}
			
			//setting the new selected item
			combo.setSelectedItem(selectedComboItem);
			combo.addActionListener(comboListener);
			
		}else {
			
			//showing the value of the item in the combo
			combo.removeAllItems();
			
			//getting all the available view browser jwidget ids
			Map<String, String> idsToLabel=EditableItem.getViewBrowsersIds(item.getSVGElement().
					getOwnerDocument().getDocumentElement());
			
			//getting the value of the item
			String id=item.getValue();
			
			if(isComplex && id.equals(popupTargetOption)){
				
				combo.addItem(new ComboListItem(
						popupTargetOption, popupTargetLabel));
				
			}else{
				
				String label=idsToLabel.get(id);
				
				if(label==null || label.equals("")) {
					
					label=id;
				}
				
				combo.addItem(new ComboListItem(id, label));
			}
		}
	}
}
