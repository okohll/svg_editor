package fr.itris.glips.extension.jwidget.spinner.edition;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.rtdaeditor.widget.*;
import fr.itris.glips.svgeditor.widgets.ColorChooserWidget;
import org.w3c.dom.*;

/**
 * the class of the spinner widget edition
 * @author ITRIS, Jordi SUC
 */
public class SpinnerEdition extends JWidgetEdition {

	/**
	 * the spinners format
	 */
	protected static String spinnersFormat="##########.###############";
	
	   /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
    public SpinnerEdition(JWidgetManager jwidgetManager, Frame mainFrame) {

    	super(jwidgetManager, mainFrame, "SpinnerWidget", 6);

    	//filling the list of the property names
    	propertiesList.add("minValue");
    	propertiesList.add("maxValue");
    	propertiesList.add("initialValue");
    	propertiesList.add("stepSize");
    	propertiesList.add("backgroundColor");//4
    	propertiesList.add("isOpaque");
    	propertiesList.add("displayBorder");
    	propertiesList.add("alignment");
    	propertiesList.add("foregroundColor");//8
    	propertiesList.add("fontFamily");
    	propertiesList.add("fontSize");
    	propertiesList.add("bold");
    	propertiesList.add("italic");

    	defaultValues.add("0");
    	defaultValues.add("100");
    	defaultValues.add("0");
    	defaultValues.add("1");
    	defaultValues.add("#ffffff");
    	defaultValues.add("true");
    	defaultValues.add("true");
    	defaultValues.add(AlignmentChooserWidget.alignments[2]);
    	defaultValues.add("#000000");
    	defaultValues.add(FontFamilyChooserWidget.SANS_SERIF);
    	defaultValues.add("12");
    	defaultValues.add("false");
    	defaultValues.add("false");
    	
		//building the configuration panel
		buildConfigurationPanel();
    }

    @Override
    protected BufferedImage createImage(Element jwidgetElement, Dimension size) {

    	//getting the min and max values
    	double minValue=0, maxValue=0;
    	
    	try{
    		minValue=Double.parseDouble(getProperty(jwidgetElement, propertiesList.get(0)));
    	}catch(Exception ex){ex.printStackTrace();}
    	
    	try{
    		maxValue=Double.parseDouble(getProperty(jwidgetElement, propertiesList.get(1)));
    	}catch(Exception ex){ex.printStackTrace();}
    	
    	//creating the image
    	BufferedImage image=new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    	JSpinner spinner=new JSpinner(new SpinnerNumberModel(0, minValue, maxValue, 1));
    	spinner.setPreferredSize(size);
    	
    	//getting the initial value of the spinner
    	double initialValue=0;
    	
    	try {
    		initialValue=Double.parseDouble(getProperty(jwidgetElement, propertiesList.get(2)));
    	}catch (Exception ex) {initialValue=Double.parseDouble(defaultValues.get(0));}
    	
    	spinner.getModel().setValue(initialValue);
    	
    	//handling the look of the component
    	JTextField textField=((JSpinner.NumberEditor)spinner.getEditor()).getTextField();
    	JWidgetToolkit.handleLook(jwidgetElement, spinner);
    	JWidgetToolkit.handleLook(jwidgetElement, textField);
    	JWidgetToolkit.handleBackgroundAndBorderLook(jwidgetElement, spinner);
    	JWidgetToolkit.handleBackgroundAndBorderLook(jwidgetElement, spinner.getEditor());
    	JWidgetToolkit.handleBackgroundAndBorderLook(jwidgetElement, textField);
    	JWidgetToolkit.handleAlignment(jwidgetElement, textField);

    	JFrame frame=new JFrame();
    	frame.getContentPane().add(spinner);
    	frame.pack();
    	frame.getContentPane().remove(spinner);
    	spinner.print(image.getGraphics());
		
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
			protected JSpinner minValueSpinner, maxValueSpinner, initialValueSpinner, stepSizeSpinner;
			
			/**
			 * the text fields of the spinners
			 */
			protected JTextField minValueTextField, maxValueTextField, initialValueTextField, stepSizeTextField;
			
			/**
			 * the spinner models
			 */
			protected SpinnerNumberModel minValueSpinnerModel, maxValueSpinnerModel, 
					initialValueSpinnerModel, stepSizeSpinnerModel;
			
			/**
			 * the change listener
			 */
			protected ChangeListener changeListener;
			
			/**
			 * the caret listener
			 */
			protected CaretListener caretListener;
			
			/**
			 * the background color chooser
			 */
			protected ColorChooserWidget colorChooser;
			
			/**
			 * the listener to the background color chooser
			 */
			protected ActionListener colorChooserListener;
			
			/**
			 * the isOpaque chooser
			 */
			protected JCheckBox isOpaqueChooser;
			
			/**
			 * the listener to the isOpaque chooser 
			 */
			protected ActionListener isOpaqueChooserListener;
			
			/**
			 * the displayBorder chooser
			 */
			protected JCheckBox displayBorderChooser;
			
			/**
			 * the listener to the displayBorder chooser 
			 */
			protected ActionListener displayBorderChooserListener;
			
			/**
			 * the alignment chooser widget
			 */
			protected AlignmentChooserWidget alignmentChooser;
			
			/**
			 * the alignment chooser widget listener
			 */
			protected ActionListener alignmentChooserListener;
			
			/**
			 * the font style chooser
			 */
			protected FontStyleChooser fontStyleChooser;
			
			/**
			 * the font style listener
			 */
			protected ActionListener fontStyleListener;
			
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
					stepSizeSpinner.removeChangeListener(changeListener);
					minValueTextField.removeCaretListener(caretListener);
					maxValueTextField.removeCaretListener(caretListener);
					initialValueTextField.removeCaretListener(caretListener);
					stepSizeTextField.removeCaretListener(caretListener);
					
					//setting the new value for the spinners
					try{
						minValueSpinner.setValue(Double.parseDouble(getProperty(getElement(), propertiesList.get(0))));
					}catch(Exception ex) {minValueSpinner.setValue(Double.parseDouble(defaultValues.get(0)));}
					try{
						maxValueSpinner.setValue(Double.parseDouble(getProperty(getElement(), propertiesList.get(1))));
					}catch(Exception ex) {maxValueSpinner.setValue(Double.parseDouble(defaultValues.get(1)));}
					try{
						initialValueSpinner.setValue(Double.parseDouble(getProperty(getElement(), propertiesList.get(2))));
					}catch(Exception ex) {initialValueSpinner.setValue(Double.parseDouble(defaultValues.get(2)));}
					try{
						stepSizeSpinner.setValue(Double.parseDouble(getProperty(getElement(), propertiesList.get(3))));
					}catch(Exception ex) {stepSizeSpinner.setValue(Double.parseDouble(defaultValues.get(3)));}

					minValueTextField.addCaretListener(caretListener);
					maxValueTextField.addCaretListener(caretListener);
					initialValueTextField.addCaretListener(caretListener);
					stepSizeTextField.addCaretListener(caretListener);
					minValueSpinner.addChangeListener(changeListener);
					maxValueSpinner.addChangeListener(changeListener);
					initialValueSpinner.addChangeListener(changeListener);
					stepSizeSpinner.addChangeListener(changeListener);
					
					colorChooser.removeListener(colorChooserListener);
					colorChooser.init(getProperty(getElement(), propertiesList.get(4)));
					colorChooser.addListener(colorChooserListener);
					
					isOpaqueChooser.removeActionListener(isOpaqueChooserListener);
					isOpaqueChooser.setSelected(Boolean.parseBoolean(
							getProperty(getElement(), propertiesList.get(5))));
					isOpaqueChooser.addActionListener(isOpaqueChooserListener);
					
					displayBorderChooser.removeActionListener(displayBorderChooserListener);
					displayBorderChooser.setSelected(Boolean.parseBoolean(
							getProperty(getElement(), propertiesList.get(6))));
					displayBorderChooser.addActionListener(displayBorderChooserListener);
					
					alignmentChooser.removeListener(alignmentChooserListener);
					alignmentChooser.init(getProperty(getElement(), propertiesList.get(7)));
					alignmentChooser.addListener(alignmentChooserListener);
					
					//getting the array of the values for the font style chooser
					String[] values=new String[5];
					
					for(int i=0; i<values.length; i++){
						
						values[i]=getProperty(getElement(), propertiesList.get(i+8));
					}

					fontStyleChooser.removeListener(fontStyleListener);
					fontStyleChooser.init(values);
					fontStyleChooser.addListener(fontStyleListener);
				}
			}
			
			@Override
			public void buildPanel() {

				String dataLabel="", minValueLabel="", maxValueLabel="", initialValueLabel="", stepSizeLabel="", 
						  lookLabel="", colorLabel="",  isOpaqueLabel="", 
							displayBorderLabel="", alignmentLabel="";
				try {
					dataLabel=bundle.getString("dataLabel");
					minValueLabel=bundle.getString("minValue");
					maxValueLabel=bundle.getString("maxValue");
					initialValueLabel=bundle.getString("initialValue");
					stepSizeLabel=bundle.getString("stepSize");
					lookLabel=bundle.getString("lookLabel");
					colorLabel=bundle.getString("colorLabel");
					isOpaqueLabel=bundle.getString("isOpaqueLabel");
					displayBorderLabel=bundle.getString("displayBorderLabel");
					alignmentLabel=bundle.getString("alignmentLabel");
				}catch (Exception ex) {ex.printStackTrace();}

				JLabel minValueLbl=new JLabel(minValueLabel+" : ");
				minValueLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				JLabel maxValueLbl=new JLabel(maxValueLabel+" : ");
				maxValueLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				JLabel initialValueLbl=new JLabel(initialValueLabel+" : ");
				initialValueLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				JLabel stepSizeLbl=new JLabel(stepSizeLabel+" : ");
				stepSizeLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				
				//creating and filling the data panel
				JPanel dataPanel=new JPanel();
				TitledBorder dataBorder=new TitledBorder(dataLabel);
				dataPanel.setBorder(dataBorder);
				
				//creating the spinners
				minValueSpinnerModel=new SpinnerNumberModel(
						0, Double.NEGATIVE_INFINITY+1, Double.POSITIVE_INFINITY-1, 1);
				maxValueSpinnerModel=new SpinnerNumberModel(
						0, Double.NEGATIVE_INFINITY+1, Double.POSITIVE_INFINITY-1, 1);
				initialValueSpinnerModel=new SpinnerNumberModel(
						0, Double.NEGATIVE_INFINITY+1, Double.POSITIVE_INFINITY-1, 1);
				stepSizeSpinnerModel=new SpinnerNumberModel(
						0, Double.NEGATIVE_INFINITY+1, Double.POSITIVE_INFINITY-1, 1);

				minValueSpinner=new JSpinner(minValueSpinnerModel); 
				maxValueSpinner=new JSpinner(maxValueSpinnerModel);
				initialValueSpinner=new JSpinner(initialValueSpinnerModel); 
				stepSizeSpinner=new JSpinner(stepSizeSpinnerModel);
				
				minValueSpinner.setEditor(new JSpinner.NumberEditor(minValueSpinner, spinnersFormat));
				maxValueSpinner.setEditor(new JSpinner.NumberEditor(maxValueSpinner, spinnersFormat));
				initialValueSpinner.setEditor(new JSpinner.NumberEditor(initialValueSpinner, spinnersFormat));
				stepSizeSpinner.setEditor(new JSpinner.NumberEditor(stepSizeSpinner, spinnersFormat));
				
				minValueTextField=((JSpinner.NumberEditor)minValueSpinner.getEditor()).getTextField();
				maxValueTextField=((JSpinner.NumberEditor)maxValueSpinner.getEditor()).getTextField();
				initialValueTextField=((JSpinner.NumberEditor)initialValueSpinner.getEditor()).getTextField();
				stepSizeTextField=((JSpinner.NumberEditor)stepSizeSpinner.getEditor()).getTextField();

				//setting the listener to the changes of the properties
				changeListener=new ChangeListener() {

					public void stateChanged(ChangeEvent evt) {

						if(evt.getSource().equals(minValueSpinner)) {
							
							setProperty(getElement(), propertiesList.get(0), minValueSpinner.getValue().toString(), false);
							
						}else if(evt.getSource().equals(maxValueSpinner)) {
							
							setProperty(getElement(), propertiesList.get(1), maxValueSpinner.getValue().toString(), false);
							
						}else if(evt.getSource().equals(initialValueSpinner)) {
							
							setProperty(getElement(), propertiesList.get(2), initialValueSpinner.getValue().toString(), false);
							
						}else if(evt.getSource().equals(stepSizeSpinner)) {
							
							setProperty(getElement(), propertiesList.get(3), stepSizeSpinner.getValue().toString(), false);
						}
					}
				};
				
				//creating the caret listener
				caretListener=new CaretListener() {
					
					public void caretUpdate(CaretEvent evt) {
					
						if(evt.getSource().equals(minValueTextField)) {
							
							setProperty(getElement(), propertiesList.get(0), minValueTextField.getText(), false);
							
						}else if(evt.getSource().equals(maxValueTextField)) {
							
							setProperty(getElement(), propertiesList.get(1), maxValueTextField.getText(), false);
							
						}else if(evt.getSource().equals(initialValueTextField)) {
							
							setProperty(getElement(), propertiesList.get(2), initialValueTextField.getText(), false);
							
						}else if(evt.getSource().equals(stepSizeTextField)) {
							
							setProperty(getElement(), propertiesList.get(3), stepSizeTextField.getText(), false);
						}
					}
				};
				
				//filling the data panel
				GridBagLayout gridBag=new GridBagLayout();
				dataPanel.setLayout(gridBag);
				GridBagConstraints c=new GridBagConstraints();
				c.fill=GridBagConstraints.BOTH;
				c.insets=new Insets(1, 0, 1, 0);
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				gridBag.setConstraints(minValueLbl, c);
				dataPanel.add(minValueLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(minValueSpinner, c);
				dataPanel.add(minValueSpinner);
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				c.weightx=1;
				gridBag.setConstraints(maxValueLbl, c);
				dataPanel.add(maxValueLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(maxValueSpinner, c);
				dataPanel.add(maxValueSpinner);
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				c.weightx=1;
				gridBag.setConstraints(initialValueLbl, c);
				dataPanel.add(initialValueLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(initialValueSpinner, c);
				dataPanel.add(initialValueSpinner);
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				c.weightx=1;
				gridBag.setConstraints(stepSizeLbl, c);
				dataPanel.add(stepSizeLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(stepSizeSpinner, c);
				dataPanel.add(stepSizeSpinner);
				
				//creating the look panel
				JPanel lookPanel=new JPanel();
				TitledBorder lookBorder=new TitledBorder(lookLabel);
				lookPanel.setBorder(lookBorder);

				//creating the color chooser and its label
				JLabel colorChooserLbl=new JLabel(colorLabel+" : ");
				colorChooserLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				colorChooser=new ColorChooserWidget();
				
				colorChooserListener=new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
					
						setProperty(getElement(), propertiesList.get(4), colorChooser.getValue(), true);
					}
				};
				
				//creating the isOpaque checkbox
				isOpaqueChooser=new JCheckBox(isOpaqueLabel);
				
				isOpaqueChooserListener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {

						setProperty(getElement(), propertiesList.get(5), 
								Boolean.toString(isOpaqueChooser.isSelected()), true);
					}
				};
				
				//creating the displayBorder checkbox
				displayBorderChooser=new JCheckBox(displayBorderLabel);
				
				displayBorderChooserListener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {

						setProperty(getElement(), propertiesList.get(6), 
								Boolean.toString(displayBorderChooser.isSelected()), true);
					}
				};
				
				//the alignment chooser
				JLabel alignmentChooserLbl=new JLabel(alignmentLabel+" : ");
				alignmentChooserLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				alignmentChooser=new AlignmentChooserWidget();
				
				alignmentChooserListener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {
						
						setProperty(getElement(), propertiesList.get(7), 
								alignmentChooser.getValue(), true);
					}
				};
				
				//creating the font style chooser
				fontStyleChooser=new FontStyleChooser();
				
				//creating the font style listener
				fontStyleListener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {
						
						//getting the array of the values
						String[] newValues=fontStyleChooser.getValues();
						
						//setting all the properties
						for(int i=0; i<newValues.length; i++){
							
							setProperty(getElement(), propertiesList.get(i+8), newValues[i], true);
						}
					}
				};
				
				//filling the look panel
				GridBagLayout gridBag0=new GridBagLayout();
				lookPanel.setLayout(gridBag0);
				GridBagConstraints c0=new GridBagConstraints();
				c0.fill=GridBagConstraints.HORIZONTAL;
				
				c0.gridx=0;
				c0.gridy=0;
				gridBag0.setConstraints(colorChooserLbl, c0);
				lookPanel.add(colorChooserLbl);
				
				c0.gridx=1;
				gridBag0.setConstraints(colorChooser, c0);
				lookPanel.add(colorChooser);
				
				c0.gridx=0;
				c0.gridy=1;
				c0.gridwidth=2;
				gridBag0.setConstraints(isOpaqueChooser, c0);
				lookPanel.add(isOpaqueChooser);
				
				c0.gridy=2;
				gridBag0.setConstraints(displayBorderChooser, c0);
				lookPanel.add(displayBorderChooser);
				
				c0.gridy=3;
				c0.gridwidth=1;
				gridBag0.setConstraints(alignmentChooserLbl, c0);
				lookPanel.add(alignmentChooserLbl);
				
				c0.gridx=1;
				gridBag0.setConstraints(alignmentChooser, c0);
				lookPanel.add(alignmentChooser);
				
				c0.gridx=2;
				c0.gridy=0;
				c0.gridheight=5;
				c0.insets=new Insets(0, 5, 0, 0);
				gridBag0.setConstraints(fontStyleChooser, c0);
				lookPanel.add(fontStyleChooser);
				
				//filling the main panel
				JPanel allPanel=new JPanel();
				allPanel.setBorder(new EmptyBorder(7, 7, 20, 7));

				gridBag=new GridBagLayout();
				allPanel.setLayout(gridBag);
				c=new GridBagConstraints();
				c.anchor=GridBagConstraints.NORTH;
				c.fill=GridBagConstraints.BOTH;
				c.insets=new Insets(1, 0, 1, 0);

				c.gridx=0;
				c.gridy=0;
				c.weightx=5;
				gridBag.setConstraints(dataPanel, c);
				allPanel.add(dataPanel);
				
				c.gridx=1;
				c.weightx=0;
				gridBag.setConstraints(lookPanel, c);
				allPanel.add(lookPanel);

				SpringLayout layout=new SpringLayout();
				setLayout(layout);
				layout.putConstraint(SpringLayout.NORTH, this, 0, SpringLayout.NORTH, allPanel);
				layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, allPanel);
				add(allPanel);
			}
		}
		
		configurationPanel=new ExtendedJWidgetConfigurationPanel();
	}
}
