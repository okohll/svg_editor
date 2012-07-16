package fr.itris.glips.extension.jwidget.combo.edition;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;

import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.rtdaeditor.widget.*;
import org.w3c.dom.*;

/**
 * the class of the combo widget
 * @author ITRIS, Jordi SUC
 */
public class ComboEdition extends JWidgetEdition{

    /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
    public ComboEdition(JWidgetManager jwidgetManager, Frame mainFrame) {

    	super(jwidgetManager, mainFrame, "ComboWidget", 3);
    	
    	propertiesList.add("foregroundColor");
    	propertiesList.add("fontFamily");
    	propertiesList.add("fontSize");
    	propertiesList.add("bold");
    	propertiesList.add("italic");
    	
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
    	JComboBox combo=new JComboBox();
    	combo.setPreferredSize(size);
    	
    	//adding the first item
    	String itemLabel="";
    	
    	try {
    		itemLabel=bundle.getString("itemLabel");
    	}catch (Exception ex) {}
    	
    	combo.addItem(itemLabel);
    	
    	//handling the look of the combo
    	JWidgetToolkit.handleLook(jwidgetElement, combo);
    	
    	JFrame frame=new JFrame();
    	frame.getContentPane().add(combo);
    	frame.pack();  	
    	combo.print(image.getGraphics());
		
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
			 * the font style chooser
			 */
			protected FontStyleChooser fontStyleChooser;
			
			/**
			 * the listener to the font style chooser
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
					
					//getting the array of the values for the font style chooser
					String[] values=new String[5];
					
					for(int i=0; i<values.length; i++){
						
						values[i]=getProperty(getElement(), propertiesList.get(i));
					}
					
					fontStyleChooser.removeListener(fontStyleListener);
					fontStyleChooser.init(values);
					fontStyleChooser.addListener(fontStyleListener);
				}
			}
			
			@Override
			public void buildPanel() {
				
				//creating the font style chooser
				fontStyleChooser=new FontStyleChooser();
				
				//creating the font style listener
				fontStyleListener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {
						
						//getting the array of the values
						String[] newValues=fontStyleChooser.getValues();
						
						//setting all the properties
						for(int i=0; i<newValues.length; i++){
							
							setProperty(getElement(), propertiesList.get(i), newValues[i], true);
						}
					}
				};
				
				//creating the all panel
				JPanel allPanel=new JPanel();
				allPanel.setBorder(new EmptyBorder(7, 7, 20, 7));

				//filling the all panel
				GridBagLayout gridBag=new GridBagLayout();
				allPanel.setLayout(gridBag);
				GridBagConstraints c=new GridBagConstraints();
				c.fill=GridBagConstraints.HORIZONTAL;
				c.anchor=GridBagConstraints.WEST;
				
				c.gridx=0;
				c.gridy=0;
				c.weightx=3;
				gridBag.setConstraints(fontStyleChooser, c);
				allPanel.add(fontStyleChooser);
				
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
