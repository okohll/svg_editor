package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.rtdaeditor.anim.*;

/**
 * the class of the check box
 * @author ITRIS, Jordi SUC
 *
 */
public class CheckBox extends Widget{

	/**
	 * the check box 
	 */
	private JCheckBox checkBox=new JCheckBox();
	
	/**
	 * the listener to the check box
	 */
	private ActionListener checkBoxListener=null;

	/**
	 * the two possible values
	 */
	private String value1="", value2="";
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected CheckBox(boolean isEditor){
		
		super(isEditor);

		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){

		checkBox.setBorder(new EmptyBorder(0, 0, 0, 0));
		checkBox.setOpaque(false);
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
		setBorder(new EmptyBorder(3, 0, 0, 0));
		/*GridBagLayout gridBag=new GridBagLayout();
		GridBagConstraints c=new GridBagConstraints();
		setLayout(gridBag);
		
		c.fill=GridBagConstraints.HORIZONTAL;
		c.anchor=GridBagConstraints.WEST;
		c.insets=new Insets(0, 10, 0, 0);
		c.gridwidth=GridBagConstraints.REMAINDER;
		gridBag.setConstraints(checkBox, c);*/
		add(checkBox);
		
		if(isEditor) {
			
			checkBoxListener=new ActionListener() {
				
				public void actionPerformed(ActionEvent evt) {

					if(checkBox.isSelected()){
						
						getItem().setValue(value1);
						
					}else{
						
						getItem().setValue(value2);
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
		
		//getting the two possible values
		LinkedList<String> possibleValues=new LinkedList<String>(item.getPossibleValues().values());
		value1=possibleValues.get(0);
		value2=possibleValues.get(1);
		
		//setting the initial value
		if(isEditor) {
			
			checkBox.removeActionListener(checkBoxListener);
		}
		
		checkBox.setSelected(item.getValue().equals(value1));
		
		if(isEditor) {
			
			checkBox.addActionListener(checkBoxListener);
		}
	}
}
