package fr.itris.glips.svgeditor.widgets;

import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import java.awt.event.*;
import java.awt.*;

/**
 * the class of the component used to choose a color
 * @author ITRIS, Jordi SUC
 */
public class ColorChooserWidget extends Widget{

	/**
	 * the button
	 */
	private JButton button;
	
	/**
	 * the listener to the button
	 */
	private ActionListener colorButtonListener;
	
	/**
	 * the current color
	 */
	private Color currentColor=Color.black;

	/**
	 * the preference size of the button
	 */
	private Dimension prefSize=new Dimension(24, 16);
	
	/**
	 * the constructor of the class
	 */
	public ColorChooserWidget() {

		build();
	}
	
	/**
	 * initializes the widget with the given value
	 * @param objectValue a new value
	 */
	public void init(Object objectValue){
		
		button.removeActionListener(colorButtonListener);
		
		if(objectValue!=null) {
			
			if(objectValue instanceof Color){
				
				currentColor=(Color)objectValue;
				currentValue=Editor.getColorChooser().getColorString(currentColor);
				
			}else{
				
				String colorString=objectValue.toString();
				
				if(colorString.equals("")){
					
					currentColor=Color.black;
					currentValue="#000000";
					
				}else{
					
					SVGHandle handle=Editor.getEditor().
						getHandlesManager().getCurrentHandle();
					
					if(handle!=null) {
						
						Color color=Editor.getColorChooser().getColor(handle, colorString);
						
						if(color!=null) {
							
							currentColor=color;
							currentValue=colorString;
						}
					}
				}
			}
		}
		
		button.repaint();
		button.addActionListener(colorButtonListener);
	}
	
	@Override
	protected void build() {
		
		//creating the button
		button=new JButton(){
			
			@Override
			protected void paintComponent(Graphics g) {

				super.paintComponent(g);
				
				if(currentColor!=null){
					
					int diff=3;
					
					g.setColor(currentColor);
					g.fillRect(diff, diff, prefSize.width-2*diff, prefSize.height-2*diff);
				}
			}
		};
		
		//setting the properties of the button
		Insets buttonInsets=new Insets(0, 0, 0, 0);
		button.setMargin(buttonInsets);
		button.setPreferredSize(prefSize);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setSize(prefSize);
		setBackground(Color.red);
		
		//creating the listener to the button
		colorButtonListener=new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				//showing the color chooser
            	Color color=Editor.getColorChooser().showColorChooserDialog(currentColor);

				if(color!=null){
					
					currentColor=color;
					Editor.getSVGColorManager().setCurrentColor(color);
					currentValue=Editor.getColorChooser().getColorString(color);
					notifyListeners();
				}
			}
		};
		
		//adding the button to the widget
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		add(button);
	}

	/**
	 * @return the current color
	 */
	public Color getCurrentColor() {
		return currentColor;
	}
	
	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
		button.setEnabled(enable);
	}
	
	@Override
	public void dispose() {
		
		super.dispose();
		button.removeActionListener(colorButtonListener);
	}
}
