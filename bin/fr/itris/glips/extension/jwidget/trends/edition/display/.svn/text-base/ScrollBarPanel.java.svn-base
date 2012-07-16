package fr.itris.glips.extension.jwidget.trends.edition.display;

import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class used to configure the scrollbar
 * @author ITRIS, Jordi SUC
 */
public class ScrollBarPanel extends JPanel{
	
	/**
	 * the jwidget edition
	 */
	private JWidgetEdition jwidgetEdition;
	
	/**
	 * the currently edited element
	 */
	private Element currentElement;
	
	/**
	 * the check box
	 */
	private JCheckBox checkBox;
	
	/**
	 * the action listener
	 */
	private ActionListener checkBoxActionListener;

	/**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
	public ScrollBarPanel(JWidgetEdition jwidgetEdition) {
		
		this.jwidgetEdition=jwidgetEdition;
		build();
	}
	
	/**
	 * initializes the panel
	 * @param element an element
	 */
	public void initialize(Element element) {
		
		this.currentElement=element;
		
		//initializing the value for the widget
		checkBox.removeActionListener(checkBoxActionListener);
		
		try {
			checkBox.setSelected(
					Boolean.parseBoolean(
							jwidgetEdition.getProperty(
									element, jwidgetEdition.getPropertiesList().get(23))));
		}catch (Exception ex) {ex.printStackTrace();}

		checkBox.addActionListener(checkBoxActionListener);
	}
	
	/**
	 * building the panel
	 */
	protected void build() {
		
		//getting the labels
		String scrollBarLabel="", displayScrollBarLabel="";

		try {
			scrollBarLabel=jwidgetEdition.getBundle().getString("scrollBar");
			displayScrollBarLabel=jwidgetEdition.getBundle().getString("displayScrollBar");
		}catch (Exception ex) {ex.printStackTrace();}

		//creating the checkbox
		checkBox=new JCheckBox(displayScrollBarLabel);
		checkBox.setHorizontalAlignment(SwingConstants.LEFT);
		
		//creating the listener to the checkbox
		checkBoxActionListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				jwidgetEdition.setProperty(
						currentElement, jwidgetEdition.getPropertiesList().get(23), 
						Boolean.toString(checkBox.isSelected()), true);
			}
		};
		
		//creating the panel used for handling the properties
		TitledBorder border=new TitledBorder(scrollBarLabel);
		setBorder(border);
		
		GridBagLayout gridBag1=new GridBagLayout();
		setLayout(gridBag1);
		GridBagConstraints c1=new GridBagConstraints();
		c1.insets=new Insets(1, 1, 1, 1);
		c1.fill=GridBagConstraints.NONE;
		
		c1.gridwidth=1;
		c1.anchor=GridBagConstraints.WEST;
		c1.weightx=50;
		gridBag1.setConstraints(checkBox, c1);
		add(checkBox);
	}
}
