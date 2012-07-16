package fr.itris.glips.extension.jwidget.trends.edition.general;

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
public class StartPanel extends JPanel{
	
	/**
	 * the prefix string of the labels
	 */
	protected static String labelPrefix="startPanel_";
	
	/**
	 * the possible values for the combo box
	 */
	protected static String[] valueItems={"realTime", "history"};
	
	/**
	 * the jwidget edition
	 */
	private JWidgetEdition jwidgetEdition;
	
	/**
	 * the currently edited element
	 */
	private Element currentElement;
	
	/**
	 * the display check box
	 */
	private JComboBox combo;
	
	/**
	 * the action listener
	 */
	private ActionListener actionListener;

	/**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
	public StartPanel(JWidgetEdition jwidgetEdition) {
		
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
		combo.removeActionListener(actionListener);
		
		//getting the value of the property
		String value=jwidgetEdition.getProperty(
									element, jwidgetEdition.getPropertiesList().get(9));
		
		//getting the index of the combo item corresponding to the property value
		int i=0;
		
		for(String val : valueItems){
			
			if(val.equals(value)){
				
				combo.setSelectedIndex(i);
				break;
			}
			
			i++;
		}

		combo.addActionListener(actionListener);
	}
	
	/**
	 * building the panel
	 */
	protected void build() {
		
		//getting the labels
		String startLabel="", startModeLabel="";

		try {
			startLabel=jwidgetEdition.getBundle().getString("start");
			startModeLabel=jwidgetEdition.getBundle().getString("startMode");
		}catch (Exception ex) {ex.printStackTrace();}

		//creating the jlabel for the combo
		JLabel comboLbl=new JLabel(startModeLabel+" : ");
		comboLbl.setHorizontalAlignment(JLabel.RIGHT);
		
		//creating the combo items
		ComboItem[] comboItems=new ComboItem[valueItems.length];
		int i=0;
		
		for(String value : valueItems){
			
			comboItems[i++]=new ComboItem(value);
		}

		//creating the combo box
		combo=new JComboBox(comboItems);

		//creating the listener to the combo box
		actionListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if(combo.getSelectedItem()!=null){
					
					jwidgetEdition.setProperty(
							currentElement, jwidgetEdition.getPropertiesList().get(9), 
								((ComboItem)combo.getSelectedItem()).getValue(), true);
				}
			}
		};
		
		//creating the panel used for handling the properties
		TitledBorder border=new TitledBorder(startLabel);
		setBorder(border);
		
		GridBagLayout gridBag1=new GridBagLayout();
		setLayout(gridBag1);
		GridBagConstraints c1=new GridBagConstraints();
		c1.insets=new Insets(1, 1, 1, 1);
		c1.fill=GridBagConstraints.HORIZONTAL;
		
		c1.gridwidth=1;
		c1.anchor=GridBagConstraints.EAST;
		gridBag1.setConstraints(comboLbl, c1);
		add(comboLbl);
		
		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(combo, c1);
		add(combo);
	}
	
	/**
	 * the class of the combo items
	 * @author ITRIS, Jordi SUC
	 */
	protected class ComboItem{
		
		/**
		 * the value and the label of the combo item
		 */
		private String value="", label="";
		
		/**
		 * the constructor of the class
		 * @param value the value of the combo item
		 */
		private ComboItem(String value){
			
			this.value=value;
			
			try{
				label=jwidgetEdition.getBundle().getString(labelPrefix+value);
			}catch (Exception ex){}
		}
		
		/**
		 * @return the value of the combo item
		 */
		public String getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return label;
		}
	}
}
