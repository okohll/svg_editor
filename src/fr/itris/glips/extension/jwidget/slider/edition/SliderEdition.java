package fr.itris.glips.extension.jwidget.slider.edition;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import fr.itris.glips.rtdaeditor.anim.widgets.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import org.w3c.dom.*;

/**
 * the class of the spinner widget edition
 * @author ITRIS, Jordi SUC
 */
public class SliderEdition extends JWidgetEdition{

	/**
	 * the horizontal and vertical values
	 */
	protected static String horizontalValue="horizontal", verticalValue="vertical";
	
	/**
	 * the spinners format
	 */
	protected static String spinnersFormat="##########";
	
	 /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
    public SliderEdition(JWidgetManager jwidgetManager, Frame mainFrame) {

    	super(jwidgetManager, mainFrame, "SliderWidget", 7);
    	
    	//filling the list of the property names
    	propertiesList.add("minValue");
    	propertiesList.add("maxValue");
    	propertiesList.add("initialValue");
    	
    	propertiesList.add("orientation");
    	propertiesList.add("inverted");
    	
    	propertiesList.add("paintTicks");
    	propertiesList.add("majorTickSpacing");
    	propertiesList.add("minorTickSpacing");
    	propertiesList.add("paintLabels");
    	
    	defaultValues.add("0");
    	defaultValues.add("100");
    	defaultValues.add("0");
    	
    	defaultValues.add(horizontalValue);
    	defaultValues.add(Boolean.toString(false));
    	
    	defaultValues.add(Boolean.toString(false));
    	defaultValues.add("25");
    	defaultValues.add("10");
    	defaultValues.add(Boolean.toString(false));
    	
		//building the configuration panel
		buildConfigurationPanel();
    }

    @Override
    protected BufferedImage createImage(Element jwidgetElement, Dimension size) {

    	//creating the image
    	BufferedImage image=new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    	JSlider slider=new JSlider(0, 100, 25);
    	slider.setPreferredSize(size);
    	slider.setOpaque(false);
    	
    	//setting the properties of the slider
    	try {
    		slider.setMinimum(Integer.parseInt(getProperty(jwidgetElement, propertiesList.get(0))));
    	}catch (Exception ex){ex.printStackTrace();slider.setMinimum(Integer.parseInt(defaultValues.get(0)));}
    	try {
    		slider.setMaximum(Integer.parseInt(getProperty(jwidgetElement, propertiesList.get(1))));
    	}catch (Exception ex){ex.printStackTrace();slider.setMaximum(Integer.parseInt(defaultValues.get(1)));}
    	try {
    		slider.setValue(Integer.parseInt(getProperty(jwidgetElement, propertiesList.get(2))));
    	}catch (Exception ex){ex.printStackTrace();slider.setValue(Integer.parseInt(defaultValues.get(2)));}
    	
    	slider.setOrientation(getProperty(jwidgetElement, propertiesList.get(3)).equals(verticalValue)?
    																SwingConstants.VERTICAL:SwingConstants.HORIZONTAL);
    	try {
        	slider.setInverted(Boolean.parseBoolean(getProperty(jwidgetElement, propertiesList.get(4))));
    	}catch (Exception ex) {ex.printStackTrace();slider.setInverted(Boolean.parseBoolean(defaultValues.get(4)));}
    	try {
        	slider.setPaintTicks(Boolean.parseBoolean(getProperty(jwidgetElement, propertiesList.get(5))));
    	}catch (Exception ex) {ex.printStackTrace();slider.setPaintTicks(Boolean.parseBoolean(defaultValues.get(5)));}
    	try {
    		slider.setMajorTickSpacing(Integer.parseInt(getProperty(jwidgetElement, propertiesList.get(6))));
    	}catch (Exception ex){ex.printStackTrace();slider.setMajorTickSpacing(Integer.parseInt(defaultValues.get(6)));}
    	try {
    		slider.setMinorTickSpacing(Integer.parseInt(getProperty(jwidgetElement, propertiesList.get(7))));
    	}catch (Exception ex){ex.printStackTrace();slider.setMinorTickSpacing(Integer.parseInt(defaultValues.get(7)));}
    	try {
        	slider.setPaintLabels(Boolean.parseBoolean(getProperty(jwidgetElement, propertiesList.get(8))));
    	}catch (Exception ex) {ex.printStackTrace();slider.setPaintLabels(Boolean.parseBoolean(defaultValues.get(8)));}
    	
    	slider.setBounds(0, 0, size.width, size.height);
    	slider.print(image.getGraphics());
		
		return image;
    }
 
	@Override
	protected void buildConfigurationPanel() {
		
		/**
		 * the class of the configuration panel
		 * @author ITRIS, Jordi SUC
		 */
		class ExtendedJWidgetConfigurationPanel extends JWidgetConfigurationPanel{
			
			/**
			 * the spinners
			 */
			protected JSpinner minValueSpinner, maxValueSpinner, initialValueSpinner, 
											minorTickSpacingSpinner, majorTickSpacingSpinner;
			
			/**
			 * the spinner models
			 */
			protected SpinnerNumberModel 	minValueSpinnerModel, maxValueSpinnerModel, 
																	initialValueSpinnerModel, 
																	minorTickSpacingSpinnerModel, 
																	majorTickSpacingSpinnerModel;
			
			/**
			 * the textfields in the spinners
			 */
			protected JTextField minValueTextField, maxValueTextField, initialValueTextField, 
											 minorTickSpacingTextField, majorTickSpacingTextField;
			
			/**
			 * the change listener
			 */
			protected ChangeListener changeListener=null;
			
			/**
			 * the caret listener to the text fields
			 */
			protected CaretListener caretListener=null;
			
			/**
			 * the text field
			 */
			protected JComboBox orientationCombo;
			
			/**
			 * the combo box listener
			 */
			protected ActionListener comboBoxListener;
			
			/**
			 * the check boxes
			 */
			protected JCheckBox invertedCheckBox, paintTicksCheckBox, paintLabelsCheckBox;
			
			/**
			 * the listener to the check boxes
			 */
			protected ActionListener checkBoxesListener;
			
			/**
			 * the constructor of the class
			 */
			protected ExtendedJWidgetConfigurationPanel() {
				
				super();
				buildPanel();
			}

			@Override
			public void initializePanel() {
				
				if(getElement()!=null) {
					
					minValueSpinner.removeChangeListener(changeListener);
					maxValueSpinner.removeChangeListener(changeListener);
					initialValueSpinner.removeChangeListener(changeListener);
					minorTickSpacingSpinner.removeChangeListener(changeListener);
					majorTickSpacingSpinner.removeChangeListener(changeListener);
					
					minValueTextField.removeCaretListener(caretListener);
					maxValueTextField.removeCaretListener(caretListener);
					initialValueTextField.removeCaretListener(caretListener);
					minorTickSpacingTextField.removeCaretListener(caretListener);
					majorTickSpacingTextField.removeCaretListener(caretListener);
					
					orientationCombo.removeActionListener(comboBoxListener);
					
					invertedCheckBox.removeActionListener(checkBoxesListener);
					paintTicksCheckBox.removeActionListener(checkBoxesListener);
					paintLabelsCheckBox.removeActionListener(checkBoxesListener);
					
					//setting the new value for the spinners
					try{
						minValueSpinner.setValue(Integer.parseInt(getProperty(getElement(), propertiesList.get(0))));
					}catch(Exception ex) {minValueSpinner.setValue(Integer.parseInt(defaultValues.get(0)));}
					try{
						maxValueSpinner.setValue(Integer.parseInt(getProperty(getElement(), propertiesList.get(1))));
					}catch(Exception ex) {maxValueSpinner.setValue(Integer.parseInt(defaultValues.get(1)));}
					try{
						initialValueSpinner.setValue(Integer.parseInt(getProperty(getElement(), propertiesList.get(2))));
					}catch(Exception ex) {initialValueSpinner.setValue(Integer.parseInt(defaultValues.get(2)));}
					try{
						majorTickSpacingSpinner.setValue(Integer.parseInt(getProperty(getElement(), propertiesList.get(6))));
					}catch(Exception ex) {majorTickSpacingSpinner.setValue(Integer.parseInt(defaultValues.get(6)));}
					try{
						minorTickSpacingSpinner.setValue(Integer.parseInt(getProperty(getElement(), propertiesList.get(7))));
					}catch(Exception ex) {minorTickSpacingSpinner.setValue(Integer.parseInt(defaultValues.get(7)));}

					//setting the new value for the combo box
					String newValue=getProperty(getElement(), propertiesList.get(3));

					if(newValue.equals(verticalValue)) {
						
						orientationCombo.setSelectedIndex(1);
						
					}else {
						
						orientationCombo.setSelectedIndex(0);
					}
					
					//setting the value for the check boxes
					try {
						invertedCheckBox.setSelected(Boolean.parseBoolean(getProperty(getElement(), propertiesList.get(4))));
					}catch (Exception ex) {}
					try {
						paintTicksCheckBox.setSelected(Boolean.parseBoolean(getProperty(getElement(), propertiesList.get(5))));
					}catch (Exception ex) {}
					try {
						paintLabelsCheckBox.setSelected(Boolean.parseBoolean(getProperty(getElement(), propertiesList.get(8))));
					}catch (Exception ex) {}

					invertedCheckBox.addActionListener(checkBoxesListener);
					paintTicksCheckBox.addActionListener(checkBoxesListener);
					paintLabelsCheckBox.addActionListener(checkBoxesListener);
					
					orientationCombo.addActionListener(comboBoxListener);
					
					minValueTextField.addCaretListener(caretListener);
					maxValueTextField.addCaretListener(caretListener);
					initialValueTextField.addCaretListener(caretListener);
					minorTickSpacingTextField.addCaretListener(caretListener);
					majorTickSpacingTextField.addCaretListener(caretListener);
					
					minValueSpinner.addChangeListener(changeListener);
					maxValueSpinner.addChangeListener(changeListener);
					initialValueSpinner.addChangeListener(changeListener);
					minorTickSpacingSpinner.addChangeListener(changeListener);
					majorTickSpacingSpinner.addChangeListener(changeListener);
				}
			}
			
			@Override
			public void buildPanel() {

				//getting the labels
				String minValueLabel="", maxValueLabel="", initialValueLabel="", majorTickSpacingLabel="",
				minorTickSpacingLabel="", orientationLabel="", horizontalLabel="", verticalLabel="", 
				invertedLabel="", paintTickLabel="", paintLabelsLabel="", numericDataLabel="", ticksLabel="",
				displayLabel="";
				
				try {
					minValueLabel=bundle.getString("minValue");
					maxValueLabel=bundle.getString("maxValue");
					initialValueLabel=bundle.getString("initialValue");
					majorTickSpacingLabel=bundle.getString("majorTickSpacing");
					minorTickSpacingLabel=bundle.getString("minorTickSpacing");
					orientationLabel=bundle.getString("orientation");
					horizontalLabel=bundle.getString("horizontal");
					verticalLabel=bundle.getString("vertical");
					invertedLabel=bundle.getString("inverted");
					paintTickLabel=bundle.getString("paintTick");
					paintLabelsLabel=bundle.getString("paintLabels");
					numericDataLabel=bundle.getString("numericData");
					ticksLabel=bundle.getString("ticks");
					displayLabel=bundle.getString("display");
				}catch (Exception ex) {ex.printStackTrace();}

				//building the group used to select the bounds of the slider//
				
				//the jlabels
				JLabel minValueLbl=new JLabel(minValueLabel+" : ");
				minValueLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				JLabel maxValueLbl=new JLabel(maxValueLabel+" : ");
				maxValueLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				JLabel initialValueLbl=new JLabel(initialValueLabel+" : ");
				initialValueLbl.setHorizontalAlignment(SwingConstants.RIGHT);

				//creating the spinners
				minValueSpinnerModel=new SpinnerNumberModel(0, Integer.MIN_VALUE+1, Integer.MAX_VALUE-1, 1);
				maxValueSpinnerModel=new SpinnerNumberModel(0, Integer.MIN_VALUE+1, Integer.MAX_VALUE-1, 1);
				initialValueSpinnerModel=new SpinnerNumberModel(0, Integer.MIN_VALUE+1, Integer.MAX_VALUE-1, 1);

				minValueSpinner=new JSpinner(minValueSpinnerModel); 
				maxValueSpinner=new JSpinner(maxValueSpinnerModel);
				initialValueSpinner=new JSpinner(initialValueSpinnerModel);
				
				minValueSpinner.setEditor(new JSpinner.NumberEditor(minValueSpinner, spinnersFormat));
				maxValueSpinner.setEditor(new JSpinner.NumberEditor(maxValueSpinner, spinnersFormat));
				initialValueSpinner.setEditor(new JSpinner.NumberEditor(initialValueSpinner, spinnersFormat));
				
				minValueTextField=((JSpinner.NumberEditor)minValueSpinner.getEditor()).getTextField();
				maxValueTextField=((JSpinner.NumberEditor)maxValueSpinner.getEditor()).getTextField();
				initialValueTextField=((JSpinner.NumberEditor)initialValueSpinner.getEditor()).getTextField();

				//creating the group panel for choosing the numeric values
				JPanel geomPanel=new JPanel();
				TitledBorder geomBorder=new TitledBorder(numericDataLabel);
				geomPanel.setBorder(geomBorder);

				//building the numeric data panel
				GridBagLayout gridBag=new GridBagLayout();
				geomPanel.setLayout(gridBag);
				GridBagConstraints c=new GridBagConstraints();
				c.fill=GridBagConstraints.HORIZONTAL;
				c.insets=new Insets(1, 0, 1, 0);
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				gridBag.setConstraints(minValueLbl, c);
				geomPanel.add(minValueLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(minValueSpinner, c);
				geomPanel.add(minValueSpinner);
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				c.weightx=1;
				gridBag.setConstraints(maxValueLbl, c);
				geomPanel.add(maxValueLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(maxValueSpinner, c);
				geomPanel.add(maxValueSpinner);
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				c.weightx=1;
				gridBag.setConstraints(initialValueLbl, c);
				geomPanel.add(initialValueLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(initialValueSpinner, c);
				geomPanel.add(initialValueSpinner);
				
				//creating the panel of the display of the slider
				JPanel displayPanel=new JPanel();
				TitledBorder displayBorder=new TitledBorder(displayLabel);
				displayPanel.setBorder(displayBorder);
				
				//creating the orientation combo
				JLabel orientationLbl=new JLabel(orientationLabel+" : ");
				orientationCombo=new JComboBox();

				//filling the combo
				orientationCombo.addItem(new ComboListItem(horizontalValue, horizontalLabel));
				orientationCombo.addItem(new ComboListItem(verticalValue, verticalLabel));
				
				//the check boxes
				invertedCheckBox=new JCheckBox(invertedLabel);
				paintLabelsCheckBox=new JCheckBox(paintLabelsLabel);
				
				//building the display panel
				gridBag=new GridBagLayout();
				displayPanel.setLayout(gridBag);
				c=new GridBagConstraints();
				c.fill=GridBagConstraints.HORIZONTAL;
				c.insets=new Insets(1, 0, 1, 0);
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				gridBag.setConstraints(orientationLbl, c);
				displayPanel.add(orientationLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(orientationCombo, c);
				displayPanel.add(orientationCombo);

				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(invertedCheckBox, c);
				displayPanel.add(invertedCheckBox);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(paintLabelsCheckBox, c);
				displayPanel.add(paintLabelsCheckBox);
				
				//creating the panel for specifying the tick properties
				JPanel tickPanel=new JPanel();
				TitledBorder tickBorder=new TitledBorder(ticksLabel);
				tickPanel.setBorder(tickBorder);
				
				//creating the checkbox
				paintTicksCheckBox=new JCheckBox(paintTickLabel);
				
				//the jlabels
				JLabel majorTickSpacingLbl=new JLabel(majorTickSpacingLabel+" : ");
				majorTickSpacingLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				JLabel minorTickSpacingLbl=new JLabel(minorTickSpacingLabel+" : ");
				minorTickSpacingLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				
				//creating the spinners
				majorTickSpacingSpinnerModel=new SpinnerNumberModel(0, 0, Integer.MAX_VALUE-1, 1);
				minorTickSpacingSpinnerModel=new SpinnerNumberModel(0, 0, Integer.MAX_VALUE-1, 1);
				
				majorTickSpacingSpinner=new JSpinner(majorTickSpacingSpinnerModel); 
				minorTickSpacingSpinner=new JSpinner(minorTickSpacingSpinnerModel);
				majorTickSpacingSpinner.setEditor(new JSpinner.NumberEditor(majorTickSpacingSpinner, spinnersFormat));
				minorTickSpacingSpinner.setEditor(new JSpinner.NumberEditor(minorTickSpacingSpinner, spinnersFormat));

				minorTickSpacingTextField=((JSpinner.NumberEditor)minorTickSpacingSpinner.getEditor()).getTextField();
				majorTickSpacingTextField=((JSpinner.NumberEditor)majorTickSpacingSpinner.getEditor()).getTextField();
				
				//building the tick panel
				gridBag=new GridBagLayout();
				tickPanel.setLayout(gridBag);
				c=new GridBagConstraints();
				c.fill=GridBagConstraints.HORIZONTAL;
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.insets=new Insets(0, 0, 8, 0);
				gridBag.setConstraints(paintTicksCheckBox, c);
				tickPanel.add(paintTicksCheckBox);
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				c.weightx=1;
				c.insets=new Insets(1, 0, 1, 0);
				gridBag.setConstraints(majorTickSpacingLbl, c);
				tickPanel.add(majorTickSpacingLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=100;
				gridBag.setConstraints(majorTickSpacingSpinner, c);
				tickPanel.add(majorTickSpacingSpinner);

				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				c.weightx=1;
				gridBag.setConstraints(minorTickSpacingLbl, c);
				tickPanel.add(minorTickSpacingLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=100;
				gridBag.setConstraints(minorTickSpacingSpinner, c);
				tickPanel.add(minorTickSpacingSpinner);

				//building the all panel
				JPanel allPanel=new JPanel();
				allPanel.setLayout(new GridLayout(2, 2));
				allPanel.add(geomPanel);
				allPanel.add(displayPanel);
				allPanel.add(tickPanel);
				allPanel.add(new JPanel());
				
				SpringLayout layout=new SpringLayout();
				setLayout(layout);
				layout.putConstraint(SpringLayout.NORTH, this, 0, SpringLayout.NORTH, allPanel);
				layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, allPanel);
				add(allPanel);
				
				//creating the listener to the spinners
				changeListener=new ChangeListener() {

					public void stateChanged(ChangeEvent evt) {

						if(evt.getSource().equals(minValueSpinner)) {
							
							setProperty(getElement(), propertiesList.get(0), minValueSpinner.getValue().toString(), true);
							
						}else if(evt.getSource().equals(maxValueSpinner)) {
							
							setProperty(getElement(), propertiesList.get(1), maxValueSpinner.getValue().toString(), true);
							
						}else if(evt.getSource().equals(initialValueSpinner)) {
							
							setProperty(getElement(), propertiesList.get(2), initialValueSpinner.getValue().toString(), true);
							
						}else if(evt.getSource().equals(majorTickSpacingSpinner)) {
							
							setProperty(getElement(), propertiesList.get(6), majorTickSpacingSpinner.getValue().toString(), true);
							
						}else if(evt.getSource().equals(minorTickSpacingSpinner)) {
							
							setProperty(getElement(), propertiesList.get(7), minorTickSpacingSpinner.getValue().toString(), true);
						}
					}
				};
				
				//creating the listener to the spinner text fields
				caretListener=new CaretListener() {

					public void caretUpdate(CaretEvent evt) {
						
						if(evt.getSource().equals(minValueTextField)) {
							
							setProperty(getElement(), propertiesList.get(0), minValueTextField.getText(), true);
							
						}else if(evt.getSource().equals(maxValueTextField)) {
							
							setProperty(getElement(), propertiesList.get(1), maxValueTextField.getText(), true);
							
						}else if(evt.getSource().equals(initialValueTextField)) {
							
							setProperty(getElement(), propertiesList.get(2), initialValueTextField.getText(), true);
							
						}else if(evt.getSource().equals(majorTickSpacingTextField)) {
							
							setProperty(getElement(), propertiesList.get(6), majorTickSpacingTextField.getText(), true);
							
						}else if(evt.getSource().equals(minorTickSpacingTextField)) {
							
							setProperty(getElement(), propertiesList.get(7), minorTickSpacingTextField.getText(), true);
						}
					}
				};
				
				//creating the listener to the combo box
				comboBoxListener=new ActionListener() {

					public void actionPerformed(ActionEvent evt) {
						
						//getting the selected item for the combo
						ComboListItem item=(ComboListItem)orientationCombo.getSelectedItem();

						if(item!=null) {

							setProperty(getElement(), propertiesList.get(3), item.getValue().toString(), true);
						}
					}
				};

				//creating the listener to the check boxes
				checkBoxesListener=new ActionListener() {

					public void actionPerformed(ActionEvent evt) {

						if(evt.getSource().equals(invertedCheckBox)) {
							
							setProperty(getElement(), propertiesList.get(4), Boolean.toString(invertedCheckBox.isSelected()), true);
							
						}else if(evt.getSource().equals(paintTicksCheckBox)) {
							
							setProperty(getElement(), propertiesList.get(5), Boolean.toString(paintTicksCheckBox.isSelected()), true);
							
						}else if(evt.getSource().equals(paintLabelsCheckBox)){
							
							setProperty(getElement(), propertiesList.get(8), Boolean.toString(paintLabelsCheckBox.isSelected()), true);
						}
					}
				};

			}
		}
		
		configurationPanel=new ExtendedJWidgetConfigurationPanel();
	}
}
