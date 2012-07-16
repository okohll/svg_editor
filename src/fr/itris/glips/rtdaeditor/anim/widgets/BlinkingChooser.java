package fr.itris.glips.rtdaeditor.anim.widgets;

import javax.swing.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.colorchooser.RtdaColorChooserModule;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import java.awt.event.*;
import java.util.*;

/**
 * the class of the event chooser
 * @author ITRIS, Jordi SUC
 */
public class BlinkingChooser extends Widget{

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
	protected BlinkingChooser(boolean isEditor){
		
		super(isEditor);
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
			
			//getting the current handle
			SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
			
			//getting the map associating a blinking id to a blinking
			Map<String, Object> blinkings=
				((RtdaColorChooserModule)Editor.getColorChooser()).getColorsAndBlinkingsToolkit().
					getBlinkingsMap(handle.getScrollPane().getSVGCanvas().getProjectFile());
			
			//adding an empty item
			combo.addItem(new ComboListItem("", ""));
			
			//creating the combo items and adding them to the combo
			ComboListItem comboItem=null, selectedItem=null;
			
			for(String blinkingId : blinkings.keySet()){

				if(blinkingId!=null){
					
					comboItem=new ComboListItem(blinkingId, blinkingId);
					
					if(blinkingId.equals(item.getValue())){
						
						selectedItem=comboItem;
					}
	
					combo.addItem(comboItem);
				}
			}
			
			//sets the selected item
			if(selectedItem!=null){
				
				combo.setSelectedItem(selectedItem);
			}
			
			combo.addActionListener(comboListener);
			
		}else {
			
			//showing the value of the item in the combo
			combo.removeAllItems();
			
			String value=item.getValue();
			combo.addItem(new ComboListItem(value, value));
		}
	}
}
