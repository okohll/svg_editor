package fr.itris.glips.extension.jwidget.viewbrowser.edition;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.components.viewbrowser.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import javax.swing.border.*;
import org.w3c.dom.*;

/**
 * the class of the textfield widget
 * @author ITRIS, Jordi SUC
 */
public class ViewBrowserEdition extends JWidgetEdition{

	/**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
    public ViewBrowserEdition(JWidgetManager jwidgetManager, Frame mainFrame) {

    	super(jwidgetManager, mainFrame, "ViewBrowserWidget", 14);
    	propertiesList.add("showHeader");
    	defaultValues.add(Boolean.toString(true));
    	propertiesList.add("initialView");
    	defaultValues.add("");
    	
		//building the configuration panel
		buildConfigurationPanel();
    }
    
    @Override
    protected BufferedImage createImage(Element jwidgetElement, Dimension size) {

    	//creating the image
    	BufferedImage image=new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    	
    	ViewBrowser viewBrowser=new ViewBrowser(
    		null, null, Boolean.toString(true).equals(getProperty(jwidgetElement, propertiesList.get(0))));
    	viewBrowser.setPreferredSize(size);
    	
    	JFrame frame=new JFrame();
    	frame.getContentPane().add(viewBrowser);
    	frame.pack();  	
    	viewBrowser.setSize(size);
    	viewBrowser.print(image.getGraphics());
		
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
			 * the check box
			 */
			protected JCheckBox checkBox;
			
			/**
			 * the check box listener
			 */
			protected ActionListener checkBoxListener=null;
			
			/**
			 * the label text field
			 */
			protected JTextField labelTextField;
			
			/**
			 * the listener to the label text field
			 */
			protected CaretListener labelTextFieldListener;
			
			/**
			 * the view text field
			 */
			protected JTextField viewTextField;
			
			/**
			 * the view chooser button
			 */
			protected JButton viewChooserButton;
			
			/**
			 * the view chooser button listener
			 */
			protected ActionListener viewChooserButtonListener;
			
			/**
			 * the constructor of the class
			 */
			protected ExtendedJWidgetConfigurationPanel() {
				
				super();
				buildPanel();
			}

			@Override
			public void initializePanel() {
				
				//initializing the label text field
				labelTextField.removeCaretListener(labelTextFieldListener);
				
				if(getElement()!=null) {
					
					labelTextField.setText(getProperty(getElement(), Toolkit.labelAttribute));
				}

				labelTextField.addCaretListener(labelTextFieldListener);
				
				//initializing the check box
				checkBox.removeActionListener(checkBoxListener);
				try {
					checkBox.setSelected(Boolean.parseBoolean(getProperty(getElement(), propertiesList.get(0))));
				}catch (Exception ex) {}
				checkBox.addActionListener(checkBoxListener);
				
				if(getElement()!=null) {
					
					//initializing the view text field
					viewTextField.setText(getProperty(getElement(), propertiesList.get(1)));
				}
				
				if(getElement()!=null) {
					
					//initializing the view chooser button
					viewChooserButton.removeActionListener(viewChooserButtonListener);
					viewChooserButton.addActionListener(viewChooserButtonListener);
				}
			}
			
			@Override
			public void buildPanel() {
				
				JPanel allPanel=new JPanel();
				allPanel.setBorder(new EmptyBorder(7, 7, 20, 7));
				
				String 	label="", dataLabel="", initialViewLabel="", viewChooserLabel="", 
							lookLabel="", labelShowHeader="";

				try {
					label=bundle.getString("viewBrowserLabel");
					dataLabel=bundle.getString("dataLabel");
					initialViewLabel=bundle.getString("initialViewLabel");
					viewChooserLabel=bundle.getString("viewChooserLabel");
					lookLabel=bundle.getString("lookLabel");
					labelShowHeader=bundle.getString("showHeaderLabel");
				}catch (Exception ex) {}
				
				//the data panel
				JPanel dataPanel=new JPanel();
				TitledBorder dataBorder=new TitledBorder(dataLabel);
				dataPanel.setBorder(dataBorder);
				
				//the textfield jlabel
				JLabel textFieldjLabel=new JLabel(label+" : ");
				textFieldjLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				
				//the text field
				labelTextField=new JTextField(12);
				
				//adding the listener to the text field
				labelTextFieldListener=new CaretListener() {

					public void caretUpdate(CaretEvent evt) {
						
						if(getElement()!=null) {
							
							setProperty(getElement(), 
								Toolkit.labelAttribute, labelTextField.getText(), false);
						}
					}
				};
				
				//the view textfield jLabel
				JLabel viewJLabel=new JLabel(initialViewLabel+" : ");
				viewJLabel.setFont(FontStore.theFont);
				viewJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				
				//the view text field
				viewTextField=new JTextField(12);
				viewTextField.setEditable(false);
				
				//the view chooser button
				viewChooserButton=new JButton(viewChooserLabel);
				viewChooserButton.setMargin(new Insets(0, 0, 0, 0));
				
				//the listener to the view chooser button
				viewChooserButtonListener=new ActionListener() {

					public void actionPerformed(ActionEvent evt) {
						
						String excludedViewPath="";
						
						//getting the current handle
						SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
						
						if(handle!=null) {
							
							fr.itris.glips.svgeditor.display.canvas.SVGCanvas canvas=
								handle.getScrollPane().getSVGCanvas();
							
							//getting the view path of the canvas
							excludedViewPath=Toolkit.getViewPath(canvas.getDocument());
						}

						//creating the filter for choosing a tag
						DataBaseNodeFilter filter=new DataBaseNodeFilter(	
								viewTextField.getText(), 0, TagToolkit.VIEW, 
										true, false, excludedViewPath);
						
						//getting the information object about the selected tag
						DataBaseNodeChooser nodeChooser=DataBaseNodeChooser.getDataBaseNodeChooser(
								Editor.getParent(), getElement().getOwnerDocument(), filter, true, false);
						nodeChooser.showDialog(((JComponent)evt.getSource()));
						nodeChooser.disposeDialog();
						DataBaseNodeInformation info=nodeChooser.getInfo();

						if(info!=null){
							
							String newValue=info.getXmlPath();
							
							if(newValue==null){
								
								newValue="";
							}
							
							setProperty(getElement(), propertiesList.get(1), newValue, false);
							viewTextField.setText(newValue);
						}
					}
				};
				
				//filling the data panel
				GridBagLayout gridBag=new GridBagLayout();
				dataPanel.setLayout(gridBag);
				GridBagConstraints c=new GridBagConstraints();
				
				c.fill=GridBagConstraints.HORIZONTAL;
				c.insets=new Insets(2, 0, 0, 0);
				
				c.gridwidth=1;
				c.weightx=1;
				c.anchor=GridBagConstraints.EAST;
				gridBag.setConstraints(textFieldjLabel, c);
				dataPanel.add(textFieldjLabel);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.WEST;
				c.weightx=50;
				gridBag.setConstraints(labelTextField, c);
				dataPanel.add(labelTextField);
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.EAST;
				gridBag.setConstraints(viewJLabel, c);
				dataPanel.add(viewJLabel);
				
				c.gridwidth=1;
				c.weightx=50;
				c.anchor=GridBagConstraints.WEST;
				gridBag.setConstraints(viewTextField, c);
				dataPanel.add(viewTextField);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.weightx=1;
				c.anchor=GridBagConstraints.SOUTHEAST;
				c.insets=new Insets(0, 2, 0, 0);
				gridBag.setConstraints(viewChooserButton, c);
				dataPanel.add(viewChooserButton);
				
				//the check box
				checkBox=new JCheckBox(labelShowHeader);
				checkBox.setFont(FontStore.theFont);
				
				//the listener to the check box
				checkBoxListener=new ActionListener() {

					public void actionPerformed(ActionEvent evt) {

						setProperty(getElement(), propertiesList.get(0), Boolean.toString(checkBox.isSelected()), true);
					}
				};
				
				//creating the look panel
				JPanel lookPanel=new JPanel();
				TitledBorder lookBorder=new TitledBorder(lookLabel);
				lookPanel.setBorder(lookBorder);
				
				lookPanel.setLayout(new BoxLayout(lookPanel, BoxLayout.X_AXIS));
				lookPanel.add(checkBox);

				//building the panel
				allPanel.setLayout(new GridLayout(1, 2));
				allPanel.add(dataPanel);
				allPanel.add(lookPanel);

				setLayout(new FlowLayout(FlowLayout.LEFT));
				add(allPanel);
				
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
