package fr.itris.glips.extension.jwidget.trends.edition.display;

import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class used to configure the cursor on the trend box
 * @author ITRIS, Jordi SUC
 */
public class GridPanel extends JPanel{
	
	/**
	 * the jwidget edition
	 */
	private JWidgetEdition jwidgetEdition;
	
	/**
	 * the currently edited element
	 */
	private Element currentElement;
	
	/**
	 * the display grid check box
	 */
	private JCheckBox gridCheckBox;
	
	/**
	 * the action listener
	 */
	private ActionListener checkBoxActionListener;
	
	/**
	 * the panel used to configure the vertical grid lines
	 */
	private VerticalGridLinesPanel verticalGridLinesPanel;

	/**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
	public GridPanel(JWidgetEdition jwidgetEdition) {
		
		this.jwidgetEdition=jwidgetEdition;
		build();
	}
	
	/**
	 * initializes the panel
	 * @param element an element
	 */
	public void initialize(Element element) {
		
		this.currentElement=element;
		
		//initializing the value for each widget
		gridCheckBox.removeActionListener(checkBoxActionListener);
		
		try {
			gridCheckBox.setSelected(
					Boolean.parseBoolean(
							jwidgetEdition.getProperty(
									element, jwidgetEdition.getPropertiesList().get(12))));
		}catch (Exception ex) {ex.printStackTrace();}

		gridCheckBox.addActionListener(checkBoxActionListener);
		
		//intializing the vertical grid lines panel
		verticalGridLinesPanel.initialize(element);
		
		//handles the the vertical grid lines panel state
		handleCheckBoxState();
	}
	
	/**
	 * building the panel
	 */
	protected void build() {
		
		//getting the labels
		String gridLabel="", displayGridLabel="";

		try {
			gridLabel=jwidgetEdition.getBundle().getString("grid");
			displayGridLabel=jwidgetEdition.getBundle().getString("displayGrid");
		}catch (Exception ex) {ex.printStackTrace();}

		//creating the checkbox
		gridCheckBox=new JCheckBox(displayGridLabel);
		
		//creating the listener to the checkbox
		checkBoxActionListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				jwidgetEdition.setProperty(
						currentElement, jwidgetEdition.getPropertiesList().get(12), 
						Boolean.toString(gridCheckBox.isSelected()), true);
				
				//handles the the vertical grid lines panel state
				handleCheckBoxState();
			}
		};
		
		//creating the vertical grid lines panel
		verticalGridLinesPanel=new VerticalGridLinesPanel(jwidgetEdition);
		
		//creating the panel used for handling the properties
		TitledBorder border=new TitledBorder(gridLabel);
		setBorder(border);
		
		GridBagLayout gridBag1=new GridBagLayout();
		setLayout(gridBag1);
		GridBagConstraints c1=new GridBagConstraints();
		c1.insets=new Insets(1, 1, 1, 1);
		c1.fill=GridBagConstraints.HORIZONTAL;
		
		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(gridCheckBox, c1);
		add(gridCheckBox);
		
		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(verticalGridLinesPanel, c1);
		add(verticalGridLinesPanel);
	}
	
	/**
	 * handles the modifications that should be done when the checkbox state changes
	 */
	protected void handleCheckBoxState(){
		
		verticalGridLinesPanel.setEnabled(gridCheckBox.isSelected());
	}
}
