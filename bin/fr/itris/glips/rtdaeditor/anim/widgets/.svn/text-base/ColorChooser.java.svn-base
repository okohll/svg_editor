package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class of the color chooser
 * @author ITRIS, Jordi SUC
 */
public class ColorChooser extends Widget{

	/**
	 * the check box
	 */
	private JCheckBox checkBox=new JCheckBox();
	
	/**
	 * the textfield listener
	 */
	private ActionListener checkBoxListener=null;
	
	/**
	 * the button listener
	 */
	private ActionListener colorButtonListener=null;
	
	/**
	 * the color button
	 */
	private JButton colorButton=null;
	
	/**
	 * the default enabled color
	 */
	private Color defaultEnabledColor=Color.blue;
	
	/**
	 * the default disabled color
	 */
	private Color defaultDisabledColor=Color.lightGray;
	
	/**
	 * the current color
	 */
	private Color currentColor=null;

	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected ColorChooser(boolean isEditor){
		
		super(isEditor);
		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){

		setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
		
		//creating the checkbox
		checkBox=new JCheckBox();
		checkBox.setOpaque(false);

		colorButton=new JButton(){
			
			@Override
			protected void paintComponent(Graphics g) {

				super.paintComponent(g);
				
				if(currentColor!=null){
					
					g.setColor(currentColor);
					g.fillRect(3, 3, 17, 9);
				}
			}
		};
		
		Insets buttonInsets=new Insets(0, 0, 0, 0);
		colorButton.setMargin(buttonInsets);
		colorButton.setPreferredSize(new Dimension(24, 16));
		
		//adding the items
		add(checkBox);
		add(colorButton);
		
		if(isEditor) {
			
			//adding the checkbox listener
			checkBoxListener=new ActionListener() {

				public void actionPerformed(ActionEvent evt) {

					if(checkBox.isSelected()) {

						//getting the current handle
						SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
						
						if(handle!=null) {
							
							getItem().setValue(Editor.getColorChooser().getColorString(defaultEnabledColor));
						}

					}else {

						getItem().setValue("none");
					}
					
					if(validateRunnable!=null) {
						
						validateRunnable.run();
					}
				}
			};
			
			//adding the button listener
			colorButtonListener=new ActionListener() {

				public void actionPerformed(ActionEvent evt) {

					//showing the color chooser
                	Color color=Editor.getColorChooser().showColorChooserDialog(currentColor);

					if(color!=null){
						
						//getting the current handle
						SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
						
						if(handle!=null) {

							String scl=Editor.getColorChooser().getColorString(color);
							getItem().setValue(scl);
							Editor.getSVGColorManager().setCurrentColor(color);
						}
					}
					
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
			
			//removing the listeners
			checkBox.removeActionListener(checkBoxListener);
			colorButton.removeActionListener(colorButtonListener);
		}

		//getting the value of the item
		String currentValue=item.getValue();

		if(currentValue.equals("") || currentValue.equals("none")) {
			
			checkBox.setSelected(false);
			colorButton.setEnabled(false);
			currentColor=defaultDisabledColor;
			
		}else {
			
			//getting the current handle
			SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
			
			if(handle!=null) {

				//getting the current color
				Color cColor=Editor.getColorChooser().getColor(handle, currentValue);
				checkBox.setSelected(true);
				colorButton.setEnabled(true);
				currentColor=cColor;
			}
		}
		
		if(currentColor==null) {
			
			currentColor=Color.black;
		}

		if(isEditor) {
			
			//adding the listeners
			checkBox.addActionListener(checkBoxListener);
			colorButton.addActionListener(colorButtonListener);
		}
	}	
}
