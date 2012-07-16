package fr.itris.glips.extension.jwidget.list.edition;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.rtdaeditor.widget.*;
import fr.itris.glips.svgeditor.widgets.ColorChooserWidget;
import org.w3c.dom.*;

/**
 * the class of the combo widget
 * @author ITRIS, Jordi SUC
 */
public class ListEdition extends JWidgetEdition{ 

    /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
    public ListEdition(JWidgetManager jwidgetManager, Frame mainFrame) {

    	super(jwidgetManager, mainFrame, "ListWidget", 4);
    	
    	propertiesList.add("backgroundColor");
    	propertiesList.add("isOpaque");
    	propertiesList.add("displayBorder");
    	propertiesList.add("foregroundColor");
    	propertiesList.add("fontFamily");
    	propertiesList.add("fontSize");
    	propertiesList.add("bold");
    	propertiesList.add("italic");
    	
    	defaultValues.add("#ffffff");
    	defaultValues.add("true");
    	defaultValues.add("true");
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

    	//creating the image
    	BufferedImage image=new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    	
    	String itemLabel="";
    	
    	try {
    		itemLabel=bundle.getString("itemLabel");
    	}catch (Exception ex) {}
    	
    	//creating the component
    	Object[] items={(itemLabel+" 1"), (itemLabel+" 2"), (itemLabel+" 3"), 
    			(itemLabel+" 4"), (itemLabel+" 5")};
    	JList list=new JList(items);
    	JScrollPane scrollPane=new JScrollPane(list);
		scrollPane.setPreferredSize(size);
    	
    	//handling the look of the button
    	JWidgetToolkit.handleLook(jwidgetElement, list);
    	JWidgetToolkit.handleBackgroundAndBorderLook(jwidgetElement, list);
    	JWidgetToolkit.handleBackgroundAndBorderLook(jwidgetElement, scrollPane);
    	JWidgetToolkit.handleBackgroundAndBorderLook(jwidgetElement, scrollPane.getViewport());

		JFrame frame=new JFrame();
		frame.getContentPane().setLayout(new BoxLayout(
				frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.getContentPane().add(scrollPane);
		frame.pack();
    	scrollPane.print(image.getGraphics());
		
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
					
					colorChooser.removeListener(colorChooserListener);
					colorChooser.init(getProperty(getElement(), propertiesList.get(0)));
					colorChooser.addListener(colorChooserListener);
					
					isOpaqueChooser.removeActionListener(isOpaqueChooserListener);
					isOpaqueChooser.setSelected(Boolean.parseBoolean(
							getProperty(getElement(), propertiesList.get(1))));
					isOpaqueChooser.addActionListener(isOpaqueChooserListener);
					
					displayBorderChooser.removeActionListener(displayBorderChooserListener);
					displayBorderChooser.setSelected(Boolean.parseBoolean(
							getProperty(getElement(), propertiesList.get(2))));
					displayBorderChooser.addActionListener(displayBorderChooserListener);
					
					//getting the array of the values for the font style chooser
					String[] values=new String[5];
					
					for(int i=0; i<values.length; i++){
						
						values[i]=getProperty(getElement(), propertiesList.get(i+3));
					}

					fontStyleChooser.removeListener(fontStyleListener);
					fontStyleChooser.init(values);
					fontStyleChooser.addListener(fontStyleListener);
				}
			}
			
			@Override
			public void buildPanel() {

				//getting the labels
				String lookLabel="", colorLabel="", isOpaqueLabel="", displayBorderLabel="";

				try {
					lookLabel=bundle.getString("lookLabel");
					colorLabel=bundle.getString("colorLabel");
					isOpaqueLabel=bundle.getString("isOpaqueLabel");
					displayBorderLabel=bundle.getString("displayBorderLabel");
				}catch (Exception ex) {ex.printStackTrace();}
				
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
					
						setProperty(getElement(), propertiesList.get(0), colorChooser.getValue(), true);
					}
				};
				
				//creating the isOpaque checkbox
				isOpaqueChooser=new JCheckBox(isOpaqueLabel);
				
				isOpaqueChooserListener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {

						setProperty(getElement(), propertiesList.get(1), 
								Boolean.toString(isOpaqueChooser.isSelected()), true);
					}
				};
				
				//creating the displayBorder checkbox
				displayBorderChooser=new JCheckBox(displayBorderLabel);
				
				displayBorderChooserListener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {

						setProperty(getElement(), propertiesList.get(2), 
								Boolean.toString(displayBorderChooser.isSelected()), true);
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
				GridBagLayout gridBag0=new GridBagLayout();
				lookPanel.setLayout(gridBag0);
				GridBagConstraints c0=new GridBagConstraints();
				c0.fill=GridBagConstraints.HORIZONTAL;
				
				c0.gridwidth=1;
				gridBag0.setConstraints(colorChooserLbl, c0);
				lookPanel.add(colorChooserLbl);
				
				c0.gridwidth=GridBagConstraints.REMAINDER;
				gridBag0.setConstraints(colorChooser, c0);
				lookPanel.add(colorChooser);
				
				gridBag0.setConstraints(isOpaqueChooser, c0);
				lookPanel.add(isOpaqueChooser);
				
				gridBag0.setConstraints(displayBorderChooser, c0);
				lookPanel.add(displayBorderChooser);
				
				gridBag0.setConstraints(fontStyleChooser, c0);
				lookPanel.add(fontStyleChooser);
				
				//creating and filling the all panel
				JPanel allPanel=new JPanel();
				allPanel.setBorder(new EmptyBorder(7, 7, 20, 7));
				GridBagLayout gridBag=new GridBagLayout();
				allPanel.setLayout(gridBag);
				GridBagConstraints c=new GridBagConstraints();
				c.fill=GridBagConstraints.HORIZONTAL;
				c.anchor=GridBagConstraints.WEST;
				
				c.gridx=0;
				c.gridy=0;
				c.weightx=0;
				gridBag.setConstraints(lookPanel, c);
				allPanel.add(lookPanel);
				
				c.gridx=1;
				c.weightx=50;
				JPanel emptyPanel=new JPanel();
				gridBag.setConstraints(emptyPanel, c);
				allPanel.add(emptyPanel);

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
