package fr.itris.glips.rtdaeditor.jwidget;

import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

import java.awt.event.*;
import java.util.*;
import java.awt.*;

/**
 * the class of the component used to choose a color
 * @author ITRIS, Jordi SUC
 */
public class JWidgetColorChooser extends JButton{
	
	/**
	 * the set of the action listeners
	 */
	private Set<ActionListener> actionListeners=new HashSet<ActionListener>();
	
	/**
	 * the current color
	 */
	private Color currentColor=Color.black;
	
	/**
	 * the string representation of the current color
	 */
	private String colorStr="#000000";
	
	/**
	 * the listener to the button
	 */
	private ActionListener colorButtonListener;
	
	/**
	 * the preference size of the button
	 */
	private Dimension prefSize=new Dimension(30, 20);
	
	/**
	 * the constructor of the class
	 */
	public JWidgetColorChooser() {

		buildWidget();
	}
	
	/**
	 * builds the widget
	 */
	protected void buildWidget() {
		
		Insets buttonInsets=new Insets(0, 0, 0, 0);
		setMargin(buttonInsets);
		setPreferredSize(prefSize);
		
		//creating the listener to the button
		colorButtonListener=new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				//showing the color chooser
            	Color color=Editor.getColorChooser().showColorChooserDialog(currentColor);

				if(color!=null){
					
					currentColor=color;
					Editor.getSVGColorManager().setCurrentColor(color);
					colorStr=Editor.getColorChooser().getColorString(color);
					notifyChanges();
				}
			}
		};
		
		addActionListener(colorButtonListener);
	}
	
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		if(currentColor!=null){
			
			int diff=3;
			
			g.setColor(currentColor);
			g.fillRect(diff, diff, prefSize.width-2*diff, prefSize.height-2*diff);
		}
	}
	
	/**
	 * @return the string representation of the current color
	 */
	public String getColorStringRepresentation() {
		return colorStr;
	}

	/**
	 * @return the current color
	 */
	public Color getCurrentColor() {
		return currentColor;
	}
	
	/**
	 * sets the new color value
	 * @param colorString the string representation of a color
	 */
	public void setColorValue(String colorString) {
		
		if(colorString!=null && ! colorString.equals("")) {
			
			SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
			
			if(handle!=null) {
				
				Color color=Editor.getColorChooser().getColor(handle, colorString);
				
				if(color!=null) {
					
					currentColor=color;
					colorStr=colorString;
				}
			}
		}
		
		repaint();
	}

	/**
	 * adds a new action listener to the component
	 * @param listener a new action listener to the component
	 */
	public void addButtonActionListener(ActionListener listener) {
		
		if(listener!=null) {
			
			actionListeners.add(listener);
		}
	}
	
	/**
	 * removes an action listener to the component
	 * @param listener an action listener to the component
	 */
	public void removeButtonActionListener(ActionListener listener) {
		
		if(listener!=null) {
			
			actionListeners.remove(listener);
		}
	}
	
	/**
	 * notifies changes on the component
	 */
	protected void notifyChanges() {
		
		ActionEvent evt=new ActionEvent(this, 0, "");
		
		for(ActionListener listener : new HashSet<ActionListener>(actionListeners)) {
			
			listener.actionPerformed(evt);
		}
	}
	
	/**
	 * disables the listeners to the components
	 */
	public void disableListeners() {
		
		removeActionListener(colorButtonListener);
	}
	
	/**
	 * enables the listeners to the components
	 */
	public void enableListeners() {
		
		addActionListener(colorButtonListener);
	}
	
	/**
	 * disposes this component
	 */
	public void dispose() {
		
		actionListeners.clear();
		removeActionListener(colorButtonListener);
	}
}
