package fr.itris.glips.extension.jwidget.progressbar.edition;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.event.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtdaeditor.anim.widgets.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.svgeditor.widgets.*;
import org.w3c.dom.*;

/**
 * the class of the spinner widget edition
 * @author ITRIS, Jordi SUC
 */
public class ProgressBarEdition extends JWidgetEdition{

	/**
	 * the spinners format
	 */
	protected static String spinnersFormat="##########";
	
	 /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
    public ProgressBarEdition(JWidgetManager jwidgetManager, Frame mainFrame) {

    	super(jwidgetManager, mainFrame, "ProgressBarWidget", 8);

    	//filling the list of the property names
    	propertiesList.add("minValue");
    	propertiesList.add("maxValue");
    	propertiesList.add("orientation");
    	propertiesList.add("foregroundColor");
    	
    	defaultValues.add("0");
    	defaultValues.add("100");
    	defaultValues.add(""+SwingConstants.HORIZONTAL);
    	defaultValues.add("#0000ff");
    	
		//building the configuration panel
		buildConfigurationPanel();
    }

    @Override
    protected BufferedImage createImage(Element jwidgetElement, Dimension size) {

    	//creating the image
    	BufferedImage image=new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    	JProgressBar progressBar=new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
    	progressBar.setValue(33);
    	
    	//handling the look of the progress bar
    	JWidgetToolkit.handleLook(jwidgetElement, progressBar);
    	
    	progressBar.setSize(size);
    	progressBar.setPreferredSize(size);
    	
    	progressBar.setOrientation(Integer.parseInt(getProperty(jwidgetElement, propertiesList.get(2))));
    	progressBar.setBounds(0, 0, size.width, size.height);  	
    	progressBar.print(image.getGraphics());
		
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
			protected JSpinner minValueSpinner, maxValueSpinner;
			
			/**
			 * the spinner models
			 */
			protected SpinnerNumberModel minValueSpinnerModel, maxValueSpinnerModel;
			
			/**
			 * the textfields of the spinners
			 */
			protected JTextField minValueTextField, maxValueTextField;
			
			/**
			 * the combo
			 */
			protected JComboBox orientationCombo;
			
			/**
			 * the change listener
			 */
			protected ChangeListener changeListener;
			
			/**
			 * the textfields listener
			 */
			protected CaretListener caretListener;
			
			/**
			 * the combo listener
			 */
			protected ActionListener comboListener;
			
			/**
			 * the color chooser
			 */
			protected ColorChooserWidget colorChooser;
			
			/**
			 * the listener to the color chooser
			 */
			protected ActionListener colorChooserListener;
			
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
					minValueTextField.removeCaretListener(caretListener);
					maxValueTextField.removeCaretListener(caretListener);
					orientationCombo.removeActionListener(comboListener);
					
					//setting the new value for the spinners
					try{
						minValueSpinner.setValue(Double.parseDouble(getProperty(getElement(), propertiesList.get(0))));
					}catch(Exception ex) {minValueSpinner.setValue(0);}
					try{
						maxValueSpinner.setValue(Double.parseDouble(getProperty(getElement(), propertiesList.get(1))));
					}catch(Exception ex) {maxValueSpinner.setValue(0);}
					
					int orientation=SwingConstants.HORIZONTAL;
					
					try {
						orientation=Integer.parseInt(getProperty(getElement(), propertiesList.get(2)));
					}catch (Exception ex) {}
					
					//getting the item corresponding to the current property value
					ComboListItem item=null;
					int value=SwingConstants.HORIZONTAL;
					
					for(int i=0; i<orientationCombo.getItemCount(); i++) {
						
						item=(ComboListItem)orientationCombo.getItemAt(i);
						
						//getting the value of the item
						try {
							value=Integer.parseInt(item.getValue().toString());
							
							if(value==orientation) {
								
								orientationCombo.setSelectedIndex(i);
								break;
							}
						}catch (Exception ex) {value=SwingConstants.HORIZONTAL;}
					}

					orientationCombo.addActionListener(comboListener);
					minValueTextField.addCaretListener(caretListener);
					maxValueTextField.addCaretListener(caretListener);
					minValueSpinner.addChangeListener(changeListener);
					maxValueSpinner.addChangeListener(changeListener);
					
					colorChooser.removeListener(colorChooserListener);
					colorChooser.init(getProperty(getElement(), propertiesList.get(3)));
					colorChooser.addListener(colorChooserListener);
				}
			}
			
			@Override
			public void buildPanel() {

				String 	minValueLabel="", maxValueLabel="", orientationLabel="", horizontalLabel="", verticalLabel="",
							colorLabel="", lookLabel="", dataLabel="";
				try {
					minValueLabel=bundle.getString("minValue");
					maxValueLabel=bundle.getString("maxValue");
					orientationLabel=bundle.getString("orientation");
					horizontalLabel=bundle.getString("horizontal");
					verticalLabel=bundle.getString("vertical");
					dataLabel=bundle.getString("dataLabel");
					lookLabel=bundle.getString("lookLabel");
					colorLabel=bundle.getString("colorLabel");
				}catch (Exception ex) {ex.printStackTrace();}

				JLabel minValueLbl=new JLabel(minValueLabel+" : ");
				minValueLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				JLabel maxValueLbl=new JLabel(maxValueLabel+" : ");
				maxValueLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				JLabel orientationLbl=new JLabel(orientationLabel+" : ");
				orientationLbl.setHorizontalAlignment(SwingConstants.RIGHT);
				
				//creating the spinners
				minValueSpinnerModel=new SpinnerNumberModel(0, Double.NEGATIVE_INFINITY+1, Double.POSITIVE_INFINITY-1, 1);
				maxValueSpinnerModel=new SpinnerNumberModel(0, Double.NEGATIVE_INFINITY+1, Double.POSITIVE_INFINITY-1, 1);

				minValueSpinner=new JSpinner(minValueSpinnerModel); 
				maxValueSpinner=new JSpinner(maxValueSpinnerModel);
				minValueSpinner.setEditor(new JSpinner.NumberEditor(minValueSpinner, spinnersFormat));
				maxValueSpinner.setEditor(new JSpinner.NumberEditor(maxValueSpinner, spinnersFormat));
				minValueTextField=((JSpinner.NumberEditor)minValueSpinner.getEditor()).getTextField();
				maxValueTextField=((JSpinner.NumberEditor)maxValueSpinner.getEditor()).getTextField();

				//setting the listener to the changes of the properties
				changeListener=new ChangeListener() {

					public void stateChanged(ChangeEvent evt) {

						if(evt.getSource().equals(minValueSpinner)) {
							
							setProperty(getElement(), propertiesList.get(0), minValueSpinner.getValue().toString(), false);
							
						}else if(evt.getSource().equals(maxValueSpinner)) {
							
							setProperty(getElement(), propertiesList.get(1), maxValueSpinner.getValue().toString(), false);
						}
					}
				};
				
				caretListener=new CaretListener() {
					
					public void caretUpdate(CaretEvent evt) {
						
						if(evt.getSource().equals(minValueTextField)) {
							
							setProperty(getElement(), propertiesList.get(0), minValueTextField.getText(), false);
							
						}else if(evt.getSource().equals(maxValueTextField)) {
							
							setProperty(getElement(), propertiesList.get(1), maxValueTextField.getText(), false);
						}
					}
				};

				//creating the panel used to select the values for the progress bar
				JPanel dataPanel=new JPanel();
				TitledBorder dataBorder=new TitledBorder(dataLabel);
				dataPanel.setBorder(dataBorder);

				GridBagLayout gridBag=new GridBagLayout();
				dataPanel.setLayout(gridBag);
				GridBagConstraints c=new GridBagConstraints();
				c.fill=GridBagConstraints.HORIZONTAL;
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
				
				//creating the look panel
				JPanel lookPanel=new JPanel();
				TitledBorder lookBorder=new TitledBorder(lookLabel);
				lookPanel.setBorder(lookBorder);

				//creating the color chooser and its label
				JLabel colorChooserLbl=new JLabel(colorLabel+" :");
				colorChooser=new ColorChooserWidget();
				
				colorChooserListener=new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
					
						setProperty(getElement(), propertiesList.get(3), colorChooser.getValue(), true);
					}
				};
				
				//the combo used to select the orientation of the progress bar
				orientationCombo=new JComboBox();
				
				//creating the combo items
				orientationCombo.addItem(new ComboListItem(SwingConstants.HORIZONTAL, horizontalLabel));
				orientationCombo.addItem(new ComboListItem(SwingConstants.VERTICAL, verticalLabel));
				
				//adding the listener to the combo box
				comboListener=new ActionListener() {

					public void actionPerformed(ActionEvent evt) {
						
						ComboListItem item=(ComboListItem)orientationCombo.getSelectedItem();
						
						if(item!=null) {
							
							setProperty(getElement(), propertiesList.get(2), item.getValue().toString(), true);
						}
					}
				};
				
				//filling the panel
				GridBagLayout gridBag0=new GridBagLayout();
				lookPanel.setLayout(gridBag0);
				GridBagConstraints c0=new GridBagConstraints();
				c0.fill=GridBagConstraints.HORIZONTAL;
				
				c0.gridwidth=1;
				c0.anchor=GridBagConstraints.EAST;
				c0.weightx=1;
				c0.insets=new Insets(1, 0, 1, 5);
				gridBag0.setConstraints(orientationLbl, c0);
				lookPanel.add(orientationLbl);
				
				c0.gridwidth=GridBagConstraints.REMAINDER;
				c0.anchor=GridBagConstraints.WEST;
				c0.weightx=50;
				c0.insets=new Insets(1, 0, 1, 0);
				gridBag0.setConstraints(orientationCombo, c0);
				lookPanel.add(orientationCombo);
				
				c0.gridwidth=1;
				c0.anchor=GridBagConstraints.EAST;
				c0.weightx=1;
				c0.insets=new Insets(1, 0, 1, 5);
				gridBag0.setConstraints(colorChooserLbl, c0);
				lookPanel.add(colorChooserLbl);
				
				c0.fill=GridBagConstraints.NONE;
				c0.gridwidth=GridBagConstraints.REMAINDER;
				c0.anchor=GridBagConstraints.WEST;
				c0.weightx=1;
				c0.insets=new Insets(1, 0, 1, 0);
				gridBag0.setConstraints(colorChooser, c0);
				lookPanel.add(colorChooser);
				
				//creating and filling the panel containing all the widgets
				JPanel allPanel=new JPanel();
				allPanel.setBorder(new EmptyBorder(7, 7, 20, 7));
				allPanel.setLayout(new GridLayout(1, 2));
				allPanel.add(dataPanel);
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
