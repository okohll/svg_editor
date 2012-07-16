package fr.itris.glips.extension.jwidget.externaldatatable.edition;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.table.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.rtdaeditor.widget.*;
import fr.itris.glips.svgeditor.widgets.ColorChooserWidget;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.w3c.dom.*;

/**
 * the class of the widget of a table 
 * @author ITRIS, Jordi SUC
 */
public class ExternalDataTableEdition extends JWidgetEdition{
	
	/**
	 * the constructor of the class
	 * @param jwidgetManager the jwidget manager
	 * @param mainFrame the main frame
	 */
	public ExternalDataTableEdition(JWidgetManager jwidgetManager, Frame mainFrame) {
		
		super(jwidgetManager, mainFrame, "ExternalDataTableWidget", 18);
		this.containsInnerComponents=false;
		
		//filling the list of the property names and default values
		propertiesList.add("showHorizontalLines");
		propertiesList.add("showVerticalLines");
    	propertiesList.add("backgroundColor");
    	propertiesList.add("foregroundColor");//3
    	propertiesList.add("fontFamily");
    	propertiesList.add("fontSize");
    	propertiesList.add("bold");
    	propertiesList.add("italic");
    	propertiesList.add("showReloadButton");//8
    	propertiesList.add("reloadButtonLabel");
    	propertiesList.add("reloadButtonForegroundColor");//10
    	propertiesList.add("reloadButtonFontFamily");
    	propertiesList.add("reloadButtonFontSize");
    	propertiesList.add("reloadButtonBold");
    	propertiesList.add("reloadButtonItalic");
    	propertiesList.add("rowHeight");//15

		defaultValues.add(Boolean.toString(true));
		defaultValues.add(Boolean.toString(true));
    	defaultValues.add("#ffffff");
    	defaultValues.add("#000000");
    	defaultValues.add(FontFamilyChooserWidget.SANS_SERIF);
    	defaultValues.add("12");
    	defaultValues.add("false");
    	defaultValues.add("false");    	
    	defaultValues.add("false");
    	defaultValues.add("");
    	defaultValues.add("#000000");
    	defaultValues.add(FontFamilyChooserWidget.SANS_SERIF);
    	defaultValues.add("12");
    	defaultValues.add("false");
    	defaultValues.add("false");
    	defaultValues.add("16");

		//building the configuration panel
		buildConfigurationPanel();
	}
	
	@Override
    protected BufferedImage createImage(Element jwidgetElement, Dimension size) {

		//whether the horizontal or vertical lines should be displayed
		boolean showHLines=jwidgetElement.getAttribute(
				propertiesList.get(0)).equals(Boolean.toString(true));
		boolean showVLines=jwidgetElement.getAttribute(
				propertiesList.get(1)).equals(Boolean.toString(true));
				
		//creating the image
		BufferedImage image=new BufferedImage(
				size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		
		//creating the table that will be represented
		JTable table=new JTable();
		
		//checking whether the refresh button should be shown
		boolean showRefreshButton=jwidgetElement.getAttribute(
				propertiesList.get(8)).equals(Boolean.toString(true));

		//the panel containing the table and the button
		JButton refreshButton=null;
		
		if(showRefreshButton){
			
			refreshButton=new JButton(
					jwidgetElement.getAttribute(propertiesList.get(9)));
			Set<Component> cmpSet=new HashSet<Component>();
			cmpSet.add(refreshButton);

			JWidgetToolkit.handleLook(jwidgetElement, 
				new String[]{propertiesList.get(10), propertiesList.get(11), 
					propertiesList.get(12), propertiesList.get(13), 
						propertiesList.get(14)}, refreshButton);
		}

		//handles the look of the table
		JWidgetToolkit.handleLook(jwidgetElement, table);
		JWidgetToolkit.handleLook(jwidgetElement, table.getTableHeader());
    	JWidgetToolkit.handleBackgroundAndBorderLook(jwidgetElement, table);
    	JWidgetToolkit.handleRowHeight(jwidgetElement, table);
		
		//the table model
		DefaultTableModel model=new DefaultTableModel() {
			
			@Override
			public String getColumnName(int col) {

				return col+"";
			}
			
			@Override
			public int getColumnCount() {

				return 2;
			}
			
			@Override
			public int getRowCount() {
				
				return 3;
			}
			
			@Override
			public Object getValueAt(int row, int col) {
				return " ";
			}
		};
		
		table.setAutoCreateColumnsFromModel(true);
		table.setModel(model);
		
		//handles the grid of the table
		table.setShowHorizontalLines(showHLines);
		table.setShowVerticalLines(showVLines);
		
		//creating the scrollpane for the table
		JScrollPane scrollpane=new JScrollPane(table);

		//creating the panel containing all the components
		JPanel allPanel=new JPanel();
		allPanel.setOpaque(false);
		allPanel.setLayout(new BorderLayout(0, 2));
		
		if(showRefreshButton){
			
			//creating the panel that will contain the button
			JPanel reloadButtonPanel=new JPanel();
			reloadButtonPanel.setOpaque(false);
			reloadButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
			reloadButtonPanel.add(refreshButton);
			allPanel.add(reloadButtonPanel, BorderLayout.NORTH);
		}
		
		allPanel.add(scrollpane, BorderLayout.CENTER);
		allPanel.setPreferredSize(size);
		
		JFrame frame=new JFrame();
		frame.getContentPane().setLayout(new BoxLayout(
				frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.getContentPane().add(allPanel);
		allPanel.validate();
		frame.pack();
		allPanel.print(image.getGraphics());
		
		return image;
	}
	
	@Override
	protected void buildConfigurationPanel() {
		
		configurationPanel=new ExtendedJWidgetConfigurationPanel();
	}
	
	/**
	 * the class of the configuration panel
	 * @author ITRIS, Jordi SUC
	 */
	protected class ExtendedJWidgetConfigurationPanel 
		extends JWidgetConfigurationPanel{
		
		/**
		 * the checkboxes
		 */
		protected JCheckBox horizontalLinesCheckbox, 
			verticalLinesCheckbox, showReloadCheckBox;
		
		/**
		 * the listener to the check boxes
		 */
		protected ActionListener checkBoxListener;
		
		/**
		 * the background color chooser
		 */
		protected ColorChooserWidget backgroundColorChooser;
		
		/**
		 * the listener to the background color chooser
		 */
		protected ActionListener backgroundColorChooserListener;
		
		/**
		 * the row height chooser
		 */
		protected IntegerSpinnerWidget rowHeightChooser;
		
		/**
		 * the row height chooser listener
		 */
		protected ActionListener rowHeightChooserListener;
		
		/**
		 * the font style chooser
		 */
		protected FontStyleChooser fontStyleChooser;
		
		/**
		 * the font style listener
		 */
		protected ActionListener fontStyleListener;
		
		/**
		 * the reload button text field
		 */
		protected JTextField reloadButtonLabelTextField;
		
		/**
		 * the listener to the reload button text field
		 */
		protected CaretListener reloadButtonLabelTextFieldListener;
		
		/**
		 * the font style chooser for the reload button
		 */
		protected FontStyleChooser reloadButtonFontStyleChooser;
		
		/**
		 * the font style listener for the reload button
		 */
		protected ActionListener reloadButtonFontStyleListener;

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
				
				//setting the new value for the check boxes
				horizontalLinesCheckbox.removeActionListener(checkBoxListener);
				
				try{
					horizontalLinesCheckbox.setSelected(Boolean.parseBoolean(
							getProperty(getElement(), propertiesList.get(0))));
				}catch(Exception ex) {}
				
				horizontalLinesCheckbox.addActionListener(checkBoxListener);
				verticalLinesCheckbox.removeActionListener(checkBoxListener);
				
				try{
					verticalLinesCheckbox.setSelected(Boolean.parseBoolean(
							getProperty(getElement(), propertiesList.get(1))));
				}catch(Exception ex) {}
				
				verticalLinesCheckbox.addActionListener(checkBoxListener);
				
				backgroundColorChooser.removeListener(backgroundColorChooserListener);
				backgroundColorChooser.init(getProperty(getElement(), propertiesList.get(2)));
				backgroundColorChooser.addListener(backgroundColorChooserListener);
				
				//getting the array of the values for the font style chooser
				String[] values=new String[5];
				
				for(int i=0; i<values.length; i++){
					
					values[i]=getProperty(getElement(), propertiesList.get(i+3));
				}

				fontStyleChooser.removeListener(fontStyleListener);
				fontStyleChooser.init(values);
				fontStyleChooser.addListener(fontStyleListener);

				showReloadCheckBox.removeActionListener(checkBoxListener);
				
				try{
					showReloadCheckBox.setSelected(Boolean.parseBoolean(
							getProperty(getElement(), propertiesList.get(8))));
				}catch(Exception ex) {}
				
				showReloadCheckBox.addActionListener(checkBoxListener);
				
				reloadButtonLabelTextField.removeCaretListener(reloadButtonLabelTextFieldListener);
				reloadButtonLabelTextField.setText(getProperty(getElement(), propertiesList.get(9)));
				reloadButtonLabelTextField.addCaretListener(reloadButtonLabelTextFieldListener);
				
				//getting the array of the values for the font style chooser
				values=new String[5];
				
				for(int i=0; i<values.length; i++){
					
					values[i]=getProperty(getElement(), propertiesList.get(i+10));
				}

				reloadButtonFontStyleChooser.removeListener(reloadButtonFontStyleListener);
				reloadButtonFontStyleChooser.init(values);
				reloadButtonFontStyleChooser.addListener(reloadButtonFontStyleListener);
				
				rowHeightChooser.removeListener(rowHeightChooserListener);
				rowHeightChooser.init(getProperty(getElement(), propertiesList.get(15)));
				rowHeightChooser.addListener(rowHeightChooserListener);
			}
		}
		
		@Override
		public void buildPanel() {

			String gridLabel=bundle.getString("gridLabel");
			String horizontalLinesLabel=bundle.getString("showHorizontalLinesLabel");
			String verticalLinesLabel=bundle.getString("showVerticalLinesLabel");
			String lookLabel=bundle.getString("lookLabel");
			String colorLabel=bundle.getString("colorLabel");
			String refreshButtonLabel=bundle.getString("refreshButtonLabel");
			String showReloadButtonLabel=bundle.getString("showReloadButtonLabel");
			String refreshButtonLabelLabel=bundle.getString("refreshButtonLabelLabel");
			String rowHeightLabel=bundle.getString("rowHeightLabel");
			
			//creating the check boxes
			horizontalLinesCheckbox=new JCheckBox(horizontalLinesLabel);
			verticalLinesCheckbox=new JCheckBox(verticalLinesLabel);
			showReloadCheckBox=new JCheckBox(showReloadButtonLabel);
			
			//creating the listener to the check boxes
			checkBoxListener=new ActionListener() {
				
				public void actionPerformed(ActionEvent evt) {
					
					if(evt.getSource().equals(horizontalLinesCheckbox)) {
						
						setProperty(getElement(), propertiesList.get(0), 
								Boolean.toString(horizontalLinesCheckbox.isSelected()), true);
						
					}else if(evt.getSource().equals(verticalLinesCheckbox)) {
						
						setProperty(getElement(), propertiesList.get(1), 
								Boolean.toString(verticalLinesCheckbox.isSelected()), true);
						
					}else if(evt.getSource().equals(showReloadCheckBox)) {
						
						setProperty(getElement(), propertiesList.get(8), 
								Boolean.toString(showReloadCheckBox.isSelected()), true);
					}
				}
			};
			
			//creating and filling the grid panel
			JPanel gridPanel=new JPanel();
			TitledBorder border=new TitledBorder(gridLabel);
			gridPanel.setBorder(border);
			gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
			gridPanel.add(horizontalLinesCheckbox);
			gridPanel.add(verticalLinesCheckbox);
			
			
			//creating and filling the look panel
			JPanel lookPanel=new JPanel();
			TitledBorder lookBorder=new TitledBorder(lookLabel);
			lookPanel.setBorder(lookBorder);

			//creating the color chooser and its label
			JLabel colorChooserLbl=new JLabel(colorLabel+" : ");
			backgroundColorChooser=new ColorChooserWidget();
			
			backgroundColorChooserListener=new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
				
					setProperty(getElement(), propertiesList.get(2), backgroundColorChooser.getValue(), true);
				}
			};
			
			//creating the row height chooser
			JLabel rowHeightLbl=new JLabel(rowHeightLabel);
			rowHeightChooser=new IntegerSpinnerWidget(16, 1, 1000, 1);
			
			rowHeightChooserListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					
					setProperty(getElement(), propertiesList.get(15), 
							rowHeightChooser.getWidgetValue()+"", true);
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
						
						setProperty(getElement(), propertiesList.get(i+3), newValues[i], true);
					}
				}
			};
			
			//filling the panel
			GridBagLayout gridBag1=new GridBagLayout();
			lookPanel.setLayout(gridBag1);
			GridBagConstraints c1=new GridBagConstraints();
			c1.insets=new Insets(1, 1, 1, 1);
			
			c1.gridwidth=1;
			gridBag1.setConstraints(colorChooserLbl, c1);
			lookPanel.add(colorChooserLbl);
			
			c1.gridwidth=GridBagConstraints.REMAINDER;
			gridBag1.setConstraints(backgroundColorChooser, c1);
			lookPanel.add(backgroundColorChooser);
			
			c1.gridwidth=1;
			gridBag1.setConstraints(rowHeightLbl, c1);
			lookPanel.add(rowHeightLbl);
			
			c1.gridwidth=GridBagConstraints.REMAINDER;
			gridBag1.setConstraints(rowHeightChooser, c1);
			lookPanel.add(rowHeightChooser);
			
			c1.gridwidth=2;
			gridBag1.setConstraints(fontStyleChooser, c1);
			lookPanel.add(fontStyleChooser);
			
			
			//the jlabels for the reload button panel
			JLabel refreshButtonLabelLbl=new JLabel(refreshButtonLabelLabel+" : ");
			refreshButtonLabelLbl.setHorizontalAlignment(SwingConstants.RIGHT);
			
			//creating and filling the reload button panel
			JPanel reloadButtonPanel=new JPanel();
			TitledBorder theBorder=new TitledBorder(refreshButtonLabel);
			reloadButtonPanel.setBorder(theBorder);
			
			//creating the text field for the reload button
			reloadButtonLabelTextField=new JTextField(15);
			
			reloadButtonLabelTextFieldListener=new CaretListener(){
				
				public void caretUpdate(CaretEvent e) {

					setProperty(getElement(), propertiesList.get(9), 
							reloadButtonLabelTextField.getText(), true);
				}
			};
			
			//creating the reload button font style chooser
			reloadButtonFontStyleChooser=new FontStyleChooser();
			
			//creating the font style listener
			reloadButtonFontStyleListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					
					//getting the array of the values
					String[] newValues=reloadButtonFontStyleChooser.getValues();
					
					//setting all the properties
					for(int i=0; i<newValues.length; i++){
						
						setProperty(getElement(), propertiesList.get(i+10), newValues[i], true);
					}
				}
			};
			
			gridBag1=new GridBagLayout();
			reloadButtonPanel.setLayout(gridBag1);
			c1=new GridBagConstraints();
			c1.insets=new Insets(1, 1, 1, 1);
			c1.fill=GridBagConstraints.HORIZONTAL;
			
			c1.gridx=0;
			c1.gridy=0;
			c1.gridwidth=2;
			gridBag1.setConstraints(showReloadCheckBox, c1);
			reloadButtonPanel.add(showReloadCheckBox);	
			
			c1.gridy=1;
			c1.gridwidth=1;
			gridBag1.setConstraints(refreshButtonLabelLbl, c1);
			reloadButtonPanel.add(refreshButtonLabelLbl);
			
			c1.gridx=1;
			gridBag1.setConstraints(reloadButtonLabelTextField, c1);
			reloadButtonPanel.add(reloadButtonLabelTextField);

			c1.gridx=0;
			c1.gridy=2;
			c1.gridwidth=2;
			gridBag1.setConstraints(reloadButtonFontStyleChooser, c1);
			reloadButtonPanel.add(reloadButtonFontStyleChooser);

			
			//creating and filling the panel containing all the widgets
			JPanel allPanel=new JPanel();
			allPanel.setBorder(new EmptyBorder(7, 7, 20, 7));
			
			GridBagLayout gridBag=new GridBagLayout();
			allPanel.setLayout(gridBag);
			GridBagConstraints c=new GridBagConstraints();
			c.fill=GridBagConstraints.BOTH;
			c.insets=new Insets(0, 0, 0, 0);
			
			c.gridx=0;
			c.gridy=0;
			c.gridheight=2;
			gridBag.setConstraints(lookPanel, c);
			allPanel.add(lookPanel);

			c.gridx=1;
			c.gridheight=3;
			gridBag.setConstraints(reloadButtonPanel, c);
			allPanel.add(reloadButtonPanel);
			
			c.gridx=0;
			c.gridy=2;
			c.gridheight=2;
			gridBag.setConstraints(gridPanel, c);
			allPanel.add(gridPanel);
			
			c.gridx=1;
			c.gridy=2;
			c.gridheight=1;
			JPanel emptyPanel=new JPanel();
			gridBag.setConstraints(emptyPanel, c);
			allPanel.add(emptyPanel);
			
			c.gridx=2;
			c.gridy=0;
			c.gridheight=1;
			c.weightx=50;
			emptyPanel=new JPanel();
			gridBag.setConstraints(emptyPanel, c);
			allPanel.add(emptyPanel);
			
			
			SpringLayout layout=new SpringLayout();
			setLayout(layout);
			layout.putConstraint(SpringLayout.NORTH, this, 0, SpringLayout.NORTH, allPanel);
			layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, allPanel);
			add(allPanel);
		}
	}
	
}
