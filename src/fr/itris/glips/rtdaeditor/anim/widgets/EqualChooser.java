package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the bounds chooser
 * @author ITRIS, Jordi SUC
 */
public class EqualChooser extends Widget{
	
	/**
	 * the icons
	 */
	private static ImageIcon infIcon, infEqIcon, supIcon, supEqIcon;
	
	static{
		
		//loading the icons
		infIcon=ResourcesManager.getIcon("Inf", false);
		infEqIcon=ResourcesManager.getIcon("InfEq", false);
		supIcon=ResourcesManager.getIcon("Sup", false);
		supEqIcon=ResourcesManager.getIcon("SupEq", false);
	}
	
	/**
	 * the choosers
	 */
	private JButton infChooser, supChooser;
	
	/**
	 * the listener to the choosers
	 */
	private ActionListener actionListener=null;
	
	/**
	 * the state of the labels
	 */
	protected boolean isInfEq=false, isSupEq=false;
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected EqualChooser(boolean isEditor){
		
		super(isEditor);
		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){

		setBorder(new EmptyBorder(0, 2, 0, 2));
		
		//getting the labels
		String valueLabel="";
		
		try{
			valueLabel=ResourcesManager.bundle.getString("rtdaanim_valueLabel");
		}catch (Exception ex){}
		
		//creating the choosers
		infChooser=new JButton();
		infChooser.setPreferredSize(new Dimension(13, 17));
		supChooser=new JButton();
		supChooser.setPreferredSize(new Dimension(13, 17));
		infChooser.setOpaque(false);
		supChooser.setOpaque(false);
		infChooser.setMargin(new Insets(0, 0, 0, 0));
		supChooser.setMargin(new Insets(0, 0, 0, 0));
		
		//the jlabel displaying the "value" string
		JLabel valueLbl=new JLabel(valueLabel);
		valueLbl.setBorder(new EmptyBorder(0, 3, 0, 3));
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		add(infChooser);
		add(valueLbl);
		add(supChooser);

		if(isEditor) {
			
			actionListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {
					
					if(evt.getSource().equals(infChooser)){
						
						isInfEq=(! isInfEq);
						
					}else if (evt.getSource().equals(supChooser)){
						
						isSupEq=(! isSupEq);
					}
					
					String value=	Boolean.toString(isInfEq)+EditableItem.separator+
											Boolean.toString(isSupEq)+EditableItem.separator;
					
					//setting the new value
					currentItemReference.get().setValue(value);
					
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

		if(isEditor){
			
			infChooser.removeActionListener(actionListener);
			supChooser.removeActionListener(actionListener);
		}
		
		//getting the two values of the group item
		String value=item.getValue(), defaultValue=item.getDefaultValue();
		String[] defaultSplitValue=defaultValue.split(EditableItem.separatorRegex);
		String[] splitValue=value.split(EditableItem.separatorRegex);
		
		String value1=defaultSplitValue[0];
		String value2=defaultSplitValue[1];
		
		if(splitValue!=null && splitValue.length>1){
			
			value1=splitValue[0];
			value2=splitValue[1];
		}

		isInfEq=value1.equals(Boolean.toString(true));
		isSupEq=value2.equals(Boolean.toString(true));

		infChooser.setIcon(isInfEq?infEqIcon:infIcon);
		supChooser.setIcon(isSupEq?supEqIcon:supIcon);
		
		if(isEditor){
			
			infChooser.addActionListener(actionListener);
			supChooser.addActionListener(actionListener);
		}
	}
}
