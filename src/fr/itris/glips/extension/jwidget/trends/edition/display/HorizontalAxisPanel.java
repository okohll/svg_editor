package fr.itris.glips.extension.jwidget.trends.edition.display;

import org.w3c.dom.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class used to configure the horizontal axis on the trend box
 * @author ITRIS, Jordi SUC
 */
public class HorizontalAxisPanel extends JPanel{
	
	/**
	 * the jwidget edition
	 */
	private JWidgetEdition jwidgetEdition;
	
	/**
	 * the currently edited element
	 */
	private Element currentElement;
	
	/**
	 * the time chooser widget used for specifying the whole time that will 
	 * be displayed in the window
	 */
	private TimeChooserWidget displayedTimeChooser;
	
	/**
	 * the listener to the actions on the display time chooser
	 */
	private ActionListener displayedTimeChooserListener;
	
	/**
	 * the display check boxes
	 */
	private JCheckBox displayTimeGraduationCheckBox, displayHorodateCheckBox;
	
	/**
	 * the action listener
	 */
	private ActionListener checkBoxesActionListener;
	
	/**
	 * the text field used for specifying the horodate format
	 */
	private JTextField horodateFormatTextField;
	
	/**
	 * the listener to the textField
	 */
	private CaretListener caretListener;
	
	/**
	 * the jlabels whose state should be handled
	 */
	private JLabel horodateFormatLbl;

	/**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
	public HorizontalAxisPanel(JWidgetEdition jwidgetEdition) {
		
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
		displayedTimeChooser.removeListener(displayedTimeChooserListener);
		displayedTimeChooser.init(jwidgetEdition.getProperty(
								element, jwidgetEdition.getPropertiesList().get(0)));
		displayedTimeChooser.addListener(displayedTimeChooserListener);
		
		displayTimeGraduationCheckBox.removeActionListener(checkBoxesActionListener);
		displayHorodateCheckBox.removeActionListener(checkBoxesActionListener);
		
		try {
			displayTimeGraduationCheckBox.setSelected(
					Boolean.parseBoolean(
							jwidgetEdition.getProperty(
									element, jwidgetEdition.getPropertiesList().get(1))));
		}catch (Exception ex) {ex.printStackTrace();}
		
		try {
			displayHorodateCheckBox.setSelected(
					Boolean.parseBoolean(
							jwidgetEdition.getProperty(
									element, jwidgetEdition.getPropertiesList().get(2))));
		}catch (Exception ex) {ex.printStackTrace();}
		
		displayTimeGraduationCheckBox.addActionListener(checkBoxesActionListener);
		displayHorodateCheckBox.addActionListener(checkBoxesActionListener);
		
		horodateFormatTextField.removeCaretListener(caretListener);
		horodateFormatTextField.setText(jwidgetEdition.getProperty(
				element, jwidgetEdition.getPropertiesList().get(3)));
		horodateFormatTextField.addCaretListener(caretListener);
		
		//handles the state of the format text field
		handleHorodateCheckBoxState();
	}
	
	/**
	 * building the panel
	 */
	protected void build() {
		
		//getting the labels
		String 	horizontalAxisLabel="", displayedTimeLabel="", displayGraduationLabel="",
					horodateLabel="", displayHorizontalHorodatesLabel="",
					horizontalHorodatesFormatLabel="";

		try {
			horizontalAxisLabel=jwidgetEdition.getBundle().getString("horizontalAxis");
			displayedTimeLabel=jwidgetEdition.getBundle().getString("displayedTime");
			displayGraduationLabel=jwidgetEdition.getBundle().getString("displayGraduation");
			
			horodateLabel=jwidgetEdition.getBundle().getString("horodate");
			displayHorizontalHorodatesLabel=jwidgetEdition.getBundle().getString("displayHorizontalHorodates");
			horizontalHorodatesFormatLabel=jwidgetEdition.getBundle().getString("horizontalHorodatesFormat");
		}catch (Exception ex) {ex.printStackTrace();}
		
		//creating the time chooser
		displayedTimeChooser=new TimeChooserWidget();
		
		//creating the jlabel for the time chooser
		JLabel displayedTimeChooserLbl=new JLabel(displayedTimeLabel+" : ");
		displayedTimeChooserLbl.setHorizontalAlignment(JLabel.RIGHT);
		
		//creating the listener to the time chooser
		displayedTimeChooserListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				jwidgetEdition.setProperty(	currentElement, jwidgetEdition.getPropertiesList().get(0), 
															displayedTimeChooser.getValue(), true);
			}
		};
		
		//creating the display time graduation check box
		displayTimeGraduationCheckBox=new JCheckBox(displayGraduationLabel);
		
		//creating the display horodate check box
		displayHorodateCheckBox=new JCheckBox(displayHorizontalHorodatesLabel);

		//creating the listener to the checkboxes
		checkBoxesActionListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if(e.getSource().equals(displayTimeGraduationCheckBox)) {
					
					jwidgetEdition.setProperty(
							currentElement, jwidgetEdition.getPropertiesList().get(1), 
							Boolean.toString(displayTimeGraduationCheckBox.isSelected()), true);
					
				}else if (e.getSource().equals(displayHorodateCheckBox)) {
					
					jwidgetEdition.setProperty(
							currentElement, jwidgetEdition.getPropertiesList().get(2), 
							Boolean.toString(displayHorodateCheckBox.isSelected()), true);
					
					handleHorodateCheckBoxState();
				}
			}
		};

		//creating the horodate format text field
		horodateFormatTextField=new JTextField();
		
		//creating the horodate format jlabel
		horodateFormatLbl=new JLabel(horizontalHorodatesFormatLabel+" : ");
		horodateFormatLbl.setHorizontalAlignment(JLabel.RIGHT);
		
		//creating the listener to the text field
		caretListener=new CaretListener(){
			
			public void caretUpdate(CaretEvent evt) {
				
				jwidgetEdition.setProperty(
						currentElement, jwidgetEdition.getPropertiesList().get(3), 
						horodateFormatTextField.getText(), true);
			}
		};
		
		//creating the panel used for handling the horodates properties
		JPanel horodatePanel=new JPanel();
		TitledBorder horodatePanelBorder=new TitledBorder(horodateLabel);
		horodatePanel.setBorder(horodatePanelBorder);
		
		GridBagLayout gridBag1=new GridBagLayout();
		horodatePanel.setLayout(gridBag1);
		GridBagConstraints c1=new GridBagConstraints();
		c1.insets=new Insets(1, 1, 1, 1);
		c1.fill=GridBagConstraints.BOTH;
		
		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(displayHorodateCheckBox, c1);
		horodatePanel.add(displayHorodateCheckBox);

		c1.gridwidth=1;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(horodateFormatLbl, c1);
		horodatePanel.add(horodateFormatLbl);
		
		c1.gridwidth=2;
		c1.weightx=50;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(horodateFormatTextField, c1);
		horodatePanel.add(horodateFormatTextField);
		
		//handling the properties of this widget
		TitledBorder border=new TitledBorder(horizontalAxisLabel);
		setBorder(border);
		
		//the top panel
		JPanel topPanel=new JPanel();
		gridBag1=new GridBagLayout();
		topPanel.setLayout(gridBag1);
		c1=new GridBagConstraints();
		c1.insets=new Insets(1, 1, 1, 1);
		c1.fill=GridBagConstraints.BOTH;
		
		c1.gridwidth=1;
		c1.anchor=GridBagConstraints.EAST;
		gridBag1.setConstraints(displayedTimeChooserLbl, c1);
		topPanel.add(displayedTimeChooserLbl);

		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(displayedTimeChooser, c1);
		topPanel.add(displayedTimeChooser);

		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(displayTimeGraduationCheckBox, c1);
		topPanel.add(displayTimeGraduationCheckBox);

		//building this widget
		GridBagLayout gridBag0=new GridBagLayout();
		setLayout(gridBag0);
		GridBagConstraints c0=new GridBagConstraints();
		c0.insets=new Insets(1, 1, 1, 1);
		c0.fill=GridBagConstraints.HORIZONTAL;

		c0.gridwidth=GridBagConstraints.REMAINDER;
		c0.anchor=GridBagConstraints.WEST;
		gridBag0.setConstraints(topPanel, c0);
		add(topPanel);
		
		c0.gridwidth=GridBagConstraints.REMAINDER;
		c0.anchor=GridBagConstraints.WEST;
		gridBag0.setConstraints(horodatePanel, c0);
		add(horodatePanel);
	}
	
	/**
	 * handles the state of the horodate check box, ie, 
	 * enabling or disabling the format textfield according to its state
	 */
	protected void handleHorodateCheckBoxState(){
		
		horodateFormatLbl.setEnabled(displayHorodateCheckBox.isSelected());
		horodateFormatTextField.setEnabled(displayHorodateCheckBox.isSelected());
	}
}
