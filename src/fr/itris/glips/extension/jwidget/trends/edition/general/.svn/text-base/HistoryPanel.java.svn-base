package fr.itris.glips.extension.jwidget.trends.edition.general;

import org.w3c.dom.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.anim.widgets.tageventchooser.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class used to configure the history
 * @author ITRIS, Jordi SUC
 */
public class HistoryPanel extends JPanel{
	
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
	 * the tag choosers
	 */
	private TagChooser tagChooser1, tagChooser2;
	
	/**
	 * the listeners to the tag choosers
	 */
	private ActionListener tagListener1, tagListener2;

	/**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
	public HistoryPanel(JWidgetEdition jwidgetEdition) {
		
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
									element, jwidgetEdition.getPropertiesList().get(20))));
		}catch (Exception ex) {ex.printStackTrace();}

		checkBox.addActionListener(checkBoxActionListener);
		
		//initializing the tag choosers
		tagChooser1.init(element.getAttribute(jwidgetEdition.getPropertiesList().get(28)));
		tagChooser2.init(element.getAttribute(jwidgetEdition.getPropertiesList().get(29)));
	}
	
	/**
	 * building the panel
	 */
	protected void build() {
		
		//getting the labels
		String historicLabel="", keepAliveLabel="", historicPeriodFromTagsLabel="", 
			tag1Label="", tag2Label="";

		try {
			historicLabel=jwidgetEdition.getBundle().getString("historic");
			keepAliveLabel=jwidgetEdition.getBundle().getString("keepAlive");
			historicPeriodFromTagsLabel=jwidgetEdition.getBundle().getString("historicPeriodFromTags");
			tag1Label=jwidgetEdition.getBundle().getString("tag1");
			tag2Label=jwidgetEdition.getBundle().getString("tag2");
		}catch (Exception ex) {ex.printStackTrace();}

		//creating the checkbox
		checkBox=new JCheckBox(keepAliveLabel);
		checkBox.setHorizontalAlignment(SwingConstants.LEFT);
		
		//creating the listener to the checkbox
		checkBoxActionListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				jwidgetEdition.setProperty(
					currentElement, jwidgetEdition.getPropertiesList().get(20), 
						Boolean.toString(checkBox.isSelected()), true);
			}
		};
		
		//creating the jlabels
		JLabel label1=new JLabel(tag1Label+" : ");
		label1.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel label2=new JLabel(tag2Label+" : ");
		label2.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//creating the tag choosers
		tagChooser1=new TagChooser(TagToolkit.STRING);
		tagChooser2=new TagChooser(TagToolkit.STRING);
		
		//creating the listeners for the tag chooser
		tagListener1=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {

				jwidgetEdition.setProperty(
					currentElement, jwidgetEdition.getPropertiesList().get(28), 
						tagChooser1.getCurrentValue(), false);
			}
		};
		
		tagChooser1.addActionListener(tagListener1);
		
		tagListener2=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {

				jwidgetEdition.setProperty(
					currentElement, jwidgetEdition.getPropertiesList().get(29), 
						tagChooser2.getCurrentValue(), false);
			}
		};
		
		tagChooser2.addActionListener(tagListener2);
	
		//creating the tags panel
		JPanel tagsPanel=new JPanel();
		tagsPanel.setBorder(new TitledBorder(historicPeriodFromTagsLabel));
		GridBagLayout gridBag1=new GridBagLayout();
		tagsPanel.setLayout(gridBag1);
		GridBagConstraints c1=new GridBagConstraints();
		c1.insets=new Insets(1, 1, 1, 1);
		c1.fill=GridBagConstraints.HORIZONTAL;
		
		c1.gridwidth=1;
		c1.gridx=0;
		c1.gridy=0;
		gridBag1.setConstraints(label1, c1);
		tagsPanel.add(label1);
		
		c1.gridx=1;
		c1.weightx=50;
		gridBag1.setConstraints(tagChooser1, c1);
		tagsPanel.add(tagChooser1);
		
		c1.gridx=0;
		c1.gridy=1;
		c1.weightx=0;
		gridBag1.setConstraints(label2, c1);
		tagsPanel.add(label2);
		
		c1.gridx=1;
		c1.weightx=50;
		gridBag1.setConstraints(tagChooser2, c1);
		tagsPanel.add(tagChooser2);
		
		//creating the panel used for handling the properties
		setBorder(new TitledBorder(historicLabel));
		
		gridBag1=new GridBagLayout();
		setLayout(gridBag1);
		c1=new GridBagConstraints();
		c1.insets=new Insets(1, 1, 1, 1);
		c1.fill=GridBagConstraints.NONE;
		c1.anchor=GridBagConstraints.WEST;
		
		c1.gridx=0;
		c1.gridy=0;
		gridBag1.setConstraints(checkBox, c1);
		add(checkBox);

		c1.gridy=1;
		c1.weightx=50;
		gridBag1.setConstraints(tagsPanel, c1);
		add(tagsPanel);
	}
}
