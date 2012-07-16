package fr.itris.glips.extension.jwidget.trends.edition.display;

import org.w3c.dom.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class used to configure the scrollbar
 * @author ITRIS, Jordi SUC
 */
public class ValuesPanel extends JPanel{
	
	/**
	 * the jwidget edition
	 */
	private JWidgetEdition jwidgetEdition;
	
	/**
	 * the currently edited element
	 */
	private Element currentElement;
	
	/**
	 * the decimal chooser
	 */
	protected IntegerSpinnerWidget decimalChooser;
	
	/**
	 * the listener to the decimal chooser
	 */
	protected ActionListener decimalChooserListener;
	
	/**
	 * the higher limit chooser
	 */
	protected DoubleSpinnerWidget higherChooser;
	
	/**
	 * the listener to the higher limit chooser
	 */
	protected ActionListener higherChooserListener;
	
	/**
	 * the lower limit chooser
	 */
	protected DoubleSpinnerWidget lowerChooser;
	
	/**
	 * the listener to the lower limit chooser
	 */
	protected ActionListener lowerChooserListener;

	/**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
	public ValuesPanel(JWidgetEdition jwidgetEdition) {
		
		this.jwidgetEdition=jwidgetEdition;
		build();
	}
	
	/**
	 * initializes the panel
	 * @param element an element
	 */
	public void initialize(Element element) {
		
		this.currentElement=element;
		
		decimalChooser.removeListener(decimalChooserListener);
		decimalChooser.init(jwidgetEdition.getProperty(
							element, jwidgetEdition.getPropertiesList().get(24)));
		decimalChooser.addListener(decimalChooserListener);
		
		higherChooser.removeListener(higherChooserListener);
		higherChooser.init(jwidgetEdition.getProperty(
							element, jwidgetEdition.getPropertiesList().get(25)));
		higherChooser.addListener(higherChooserListener);
		
		lowerChooser.removeListener(lowerChooserListener);
		lowerChooser.init(jwidgetEdition.getProperty(
							element, jwidgetEdition.getPropertiesList().get(26)));
		lowerChooser.addListener(lowerChooserListener);
	}
	
	/**
	 * building the panel
	 */
	protected void build() {
		
		//getting the labels
		String valuesLabel="", decimalNumberLabel="", limitLabel="", higherLimitLabel="", lowerLimitLabel="";

		try {
			valuesLabel=jwidgetEdition.getBundle().getString("values");
			decimalNumberLabel=jwidgetEdition.getBundle().getString("decimalNumber");
			limitLabel=jwidgetEdition.getBundle().getString("limit");
			higherLimitLabel=jwidgetEdition.getBundle().getString("higherLimit");
			lowerLimitLabel=jwidgetEdition.getBundle().getString("lowerLimit");
		}catch (Exception ex) {ex.printStackTrace();}

		//creating the jlabel for the decimal number chooser
		JLabel decimalNumberLbl=new JLabel(decimalNumberLabel+" : ");
		decimalNumberLbl.setHorizontalAlignment(JLabel.RIGHT);
		
		//creating the decimal number chooser
		decimalChooser=new IntegerSpinnerWidget(3, 0, 50, 1);
		
		decimalChooserListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			
				jwidgetEdition.setProperty(currentElement, jwidgetEdition.getPropertiesList().get(24), 
						decimalChooser.getWidgetValue()+"", true);
			}
		};
		
		//creating the jlabel for the limit choosers
		JLabel limitLbl=new JLabel(limitLabel);
		limitLbl.setHorizontalAlignment(JLabel.LEFT);
		
		//creating the jlabel for the higher limit chooser
		JLabel higherLimitLbl=new JLabel(higherLimitLabel+" : ");
		higherLimitLbl.setHorizontalAlignment(JLabel.RIGHT);
		
		//creating the higher limit chooser
		higherChooser=new DoubleSpinnerWidget(100000, 0, 1000000000000.0, 1, false);
		
		higherChooserListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			
				jwidgetEdition.setProperty(currentElement, jwidgetEdition.getPropertiesList().get(25), 
						higherChooser.getWidgetValue()+"", true);
			}
		};
		
		//creating the jlabel for the lower limit chooser
		JLabel lowerLimitLbl=new JLabel(lowerLimitLabel+" : ");
		lowerLimitLbl.setHorizontalAlignment(JLabel.RIGHT);
		
		//creating the lower limit chooser
		lowerChooser=new DoubleSpinnerWidget(100000, 0, 1000000000000.0, 1, false);
		
		lowerChooserListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			
				jwidgetEdition.setProperty(currentElement, jwidgetEdition.getPropertiesList().get(26), 
						lowerChooser.getWidgetValue()+"", true);
			}
		};
		
		//creating the panel used for handling the properties
		TitledBorder border=new TitledBorder(valuesLabel);
		setBorder(border);
		
		GridBagLayout gridBag1=new GridBagLayout();
		setLayout(gridBag1);
		GridBagConstraints c1=new GridBagConstraints();
		c1.insets=new Insets(1, 1, 1, 1);
		c1.fill=GridBagConstraints.HORIZONTAL;
		
		c1.gridx=0;
		c1.gridy=0;
		c1.gridwidth=2;
		c1.anchor=GridBagConstraints.EAST;
		gridBag1.setConstraints(decimalNumberLbl, c1);
		add(decimalNumberLbl);
		
		c1.gridx=2;
		c1.gridy=0;
		c1.gridwidth=1;
		c1.anchor=GridBagConstraints.EAST;
		gridBag1.setConstraints(decimalChooser, c1);
		add(decimalChooser);
		
		c1.gridx=0;
		c1.gridy=1;
		c1.gridwidth=3;
		c1.weightx=50;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(limitLbl, c1);
		add(limitLbl);
		
		c1.gridx=0;
		c1.gridy=2;
		c1.gridwidth=1;
		c1.ipadx=30;
		c1.weightx=0;
		c1.anchor=GridBagConstraints.EAST;
		gridBag1.setConstraints(higherLimitLbl, c1);
		add(higherLimitLbl);
		
		c1.gridx=1;
		c1.gridy=2;
		c1.gridwidth=2;
		c1.ipadx=0;
		c1.anchor=GridBagConstraints.EAST;
		gridBag1.setConstraints(higherChooser, c1);
		add(higherChooser);
		
		c1.gridx=0;
		c1.gridy=3;
		c1.gridwidth=1;		
		c1.ipadx=30;
		c1.anchor=GridBagConstraints.EAST;
		//c1.weightx=1;
		gridBag1.setConstraints(lowerLimitLbl, c1);
		add(lowerLimitLbl);
		
		c1.gridx=1;
		c1.gridy=3;
		c1.gridwidth=2;
		c1.ipadx=0;
		//c1.weightx=50;
		c1.anchor=GridBagConstraints.EAST;
		gridBag1.setConstraints(lowerChooser, c1);
		add(lowerChooser);
	}
}
