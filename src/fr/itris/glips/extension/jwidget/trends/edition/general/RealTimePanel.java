package fr.itris.glips.extension.jwidget.trends.edition.general;

import org.w3c.dom.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class used to configure the cursor on the trend box
 * @author ITRIS, Jordi SUC
 */
public class RealTimePanel extends JPanel{
	
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
	private TimeChooserWidget refreshPeriodTimeChooser;
	
	/**
	 * the listener to the actions on the display time chooser
	 */
	private ActionListener refreshPeriodChooserListener;
	
	/**
	 * the spinner used to choose the number of points to display
	 */
	private IntegerSpinnerWidget pointsNumberSpinner;
	
	/**
	 * the listener to the spinner
	 */
	private ActionListener spinnerListener;

	/**
	 * the time chooser widget used for specifying the max displayable time
	 */
	private TimeChooserWidget maxDisplayableTimeChooser;
	
	/**
	 * the listener to the actions on the max displayable time chooser
	 */
	private ActionListener maxDisplayableTimeChooserListener;
	
	/**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
	public RealTimePanel(JWidgetEdition jwidgetEdition) {
		
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
		refreshPeriodTimeChooser.removeListener(refreshPeriodChooserListener);
		refreshPeriodTimeChooser.init(jwidgetEdition.getProperty(
								element, jwidgetEdition.getPropertiesList().get(6)));
		refreshPeriodTimeChooser.addListener(refreshPeriodChooserListener);
		
		pointsNumberSpinner.removeListener(spinnerListener);
		pointsNumberSpinner.init(jwidgetEdition.getProperty(
								element, jwidgetEdition.getPropertiesList().get(7)));
		pointsNumberSpinner.addListener(spinnerListener);
		
		maxDisplayableTimeChooser.removeListener(maxDisplayableTimeChooserListener);
		maxDisplayableTimeChooser.init(jwidgetEdition.getProperty(
								element, jwidgetEdition.getPropertiesList().get(22)));
		maxDisplayableTimeChooser.addListener(maxDisplayableTimeChooserListener);
	}
	
	/**
	 * building the panel
	 */
	protected void build() {
		
		//getting the labels
		String 	realTimeLabel="", refreshPeriodLabel="",
					pointsNumberLabel="", maxDisplayableTimeLabel="";

		try {
			realTimeLabel=jwidgetEdition.getBundle().getString("realTime");
			refreshPeriodLabel=jwidgetEdition.getBundle().getString("refreshPeriod");
			pointsNumberLabel=jwidgetEdition.getBundle().getString("pointsNumber");
			maxDisplayableTimeLabel=jwidgetEdition.getBundle().getString("maxDisplayableTime");
		}catch (Exception ex) {ex.printStackTrace();}

		//creating the time chooser
		refreshPeriodTimeChooser=new TimeChooserWidget();
		
		//creating the jlabel for the time chooser
		JLabel refreshPeriodTimeChooserLbl=new JLabel(refreshPeriodLabel+" : ");
		refreshPeriodTimeChooserLbl.setHorizontalAlignment(JLabel.RIGHT);
		
		//creating the listener to the time chooser
		refreshPeriodChooserListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				jwidgetEdition.setProperty(	currentElement, jwidgetEdition.getPropertiesList().get(6), 
															refreshPeriodTimeChooser.getValue(), false);
			}
		};
		
		//creating the points number spinner
		pointsNumberSpinner=new IntegerSpinnerWidget(1000, 0, 10000000, 1);
		
		//creating the jlabel for the spinner
		JLabel pointsNumberSpinnerLbl=new JLabel(pointsNumberLabel+" : ");
		pointsNumberSpinnerLbl.setHorizontalAlignment(JLabel.RIGHT);
		
		//creating the listener to the spinner
		spinnerListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				jwidgetEdition.setProperty(	currentElement, jwidgetEdition.getPropertiesList().get(7), 
															pointsNumberSpinner.getWidgetValue()+"", false);
			}
		};
		
		//creating the max displayable time chooser
		maxDisplayableTimeChooser=new TimeChooserWidget();
		
		//creating the jlabel for the time chooser
		JLabel maxDisplayableTimeChooserLbl=new JLabel(maxDisplayableTimeLabel+" : ");
		maxDisplayableTimeChooserLbl.setHorizontalAlignment(JLabel.RIGHT);
		
		//creating the listener to the time chooser
		maxDisplayableTimeChooserListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				jwidgetEdition.setProperty(	currentElement, jwidgetEdition.getPropertiesList().get(22), 
						maxDisplayableTimeChooser.getValue(), false);
			}
		};
		
		//creating the panel used for handling the properties
		TitledBorder border=new TitledBorder(realTimeLabel);
		setBorder(border);
		
		GridBagLayout gridBag1=new GridBagLayout();
		setLayout(gridBag1);
		GridBagConstraints c1=new GridBagConstraints();
		c1.weightx=50;
		c1.insets=new Insets(1, 1, 1, 1);
		c1.fill=GridBagConstraints.HORIZONTAL;

		c1.gridwidth=GridBagConstraints.RELATIVE;
		c1.anchor=GridBagConstraints.EAST;
		gridBag1.setConstraints(refreshPeriodTimeChooserLbl, c1);
		add(refreshPeriodTimeChooserLbl);
		
		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(refreshPeriodTimeChooser, c1);
		add(refreshPeriodTimeChooser);
		
		c1.gridwidth=GridBagConstraints.RELATIVE;
		c1.anchor=GridBagConstraints.EAST;
		gridBag1.setConstraints(pointsNumberSpinnerLbl, c1);
		add(pointsNumberSpinnerLbl);
		
		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(pointsNumberSpinner, c1);
		add(pointsNumberSpinner);
		
		c1.gridwidth=GridBagConstraints.RELATIVE;
		c1.anchor=GridBagConstraints.EAST;
		gridBag1.setConstraints(maxDisplayableTimeChooserLbl, c1);
		add(maxDisplayableTimeChooserLbl);
		
		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(maxDisplayableTimeChooser, c1);
		add(maxDisplayableTimeChooser);
	}
}
