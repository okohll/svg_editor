package fr.itris.glips.extension.jwidget.base.edition;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import org.w3c.dom.*;

import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.rtdaeditor.widget.*;
import javax.swing.border.*;
import java.awt.event.*;

/**
 * the class of the widget of a button 
 * @author ITRIS, Jordi SUC
 */
public abstract class AbstractButtonEdition extends JWidgetEdition{ 
	
	/**
	 * the BUTTON type
	 */
	public static final int BUTTON=0;
	
	/**
	 * the TOGGLE_BUTTON type
	 */
	public static final int TOGGLE_BUTTON=1;
	
	/**
	 * the CHECK_BOX type
	 */
	public static final int CHECK_BOX=2;
	
	/**
	 * the main type
	 */
	protected int mainType=BUTTON;
	
	/**
	 * the constructor of the class
	 * @param jwidgetManager the jwidget manager
	 * @param mainFrame the main frame
	 * @param mainType the main type
	 * @param id the id of the jwidget type
	 * @param position the position of the toggle button of the jwidget
	 */
    public AbstractButtonEdition(JWidgetManager jwidgetManager, Frame mainFrame, 
    		int mainType, String id, int position) {

    	super(jwidgetManager, mainFrame, id, position);
    	this.mainType=mainType;
    	
    	//filling the list of the property names and default values
    	propertiesList.add("buttonLabel");
    	propertiesList.add("foregroundColor");
    	propertiesList.add("fontFamily");
    	propertiesList.add("fontSize");
    	propertiesList.add("bold");
    	propertiesList.add("italic");
    	
    	defaultValues.add("");
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
		AbstractButton button=null;

		switch (mainType) {
		
			case BUTTON :

		    	button=new JButton();
				break;
				
			case TOGGLE_BUTTON :

				button=new JToggleButton();
				break;
				
			case CHECK_BOX :

		    	button=new JCheckBox();
		    	button.setOpaque(false);
				break;
		}
		
		if(button!=null){
			
	    	button.setText(getProperty(jwidgetElement, propertiesList.get(0)));
	    	
	    	//handling the look of the button
	    	JWidgetToolkit.handleLook(jwidgetElement, button);
	    	
			button.setSize(size);
			button.setPreferredSize(size);
			button.setMaximumSize(size);
			button.print(image.getGraphics());
		}

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
			 * the text field
			 */
			protected JTextField textField=new JTextField(20);
			
			/**
			 * the caret listener
			 */
			protected CaretListener caretListener;
			
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
					
					//getting the array of the values for the font style chooser
					String[] values=new String[5];
					
					for(int i=0; i<values.length; i++){
						
						values[i]=getProperty(getElement(), propertiesList.get(i+1));
					}

					fontStyleChooser.removeListener(fontStyleListener);
					fontStyleChooser.init(values);
					fontStyleChooser.addListener(fontStyleListener);
					
					textField.removeCaretListener(caretListener);
					textField.setText(getProperty(getElement(), propertiesList.get(0)));
					textField.addCaretListener(caretListener);
				}
			}
			
			@Override
			public void buildPanel() {

				JPanel allPanel=new JPanel();
				allPanel.setBorder(new EmptyBorder(7, 7, 20, 7));
				
				//getting the labels
				String dataLabel="", buttonLabel="";

				try {
					dataLabel=bundle.getString("dataLabel");
					buttonLabel=bundle.getString("buttonLabel");
				}catch (Exception ex) {ex.printStackTrace();}

				//creating the data panel
				JPanel dataPanel=new JPanel();
				TitledBorder dataBorder=new TitledBorder(dataLabel);
				dataPanel.setBorder(dataBorder);
				
				JLabel buttonLbl=new JLabel(buttonLabel+" : ");

				//setting the listener to the changes of the properties
				caretListener=new CaretListener() {

					public void caretUpdate(CaretEvent evt) {
						
						if(! textField.getText().equals(getProperty(getElement(), propertiesList.get(0)))) {
							
							setProperty(getElement(), propertiesList.get(0), textField.getText(), true);
						}
					}
				};
				
				textField.addCaretListener(caretListener);
				
				GridBagLayout gridBag=new GridBagLayout();
				dataPanel.setLayout(gridBag);
				GridBagConstraints c=new GridBagConstraints();
				c.fill=GridBagConstraints.HORIZONTAL;
				
				c.gridwidth=1;
				c.anchor=GridBagConstraints.NORTHEAST;
				c.insets=new Insets(0, 0, 0, 5);
				gridBag.setConstraints(buttonLbl, c);
				dataPanel.add(buttonLbl);
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				c.anchor=GridBagConstraints.NORTHWEST;
				c.weightx=50;
				gridBag.setConstraints(textField, c);
				dataPanel.add(textField);
				
				//creating the font style chooser
				fontStyleChooser=new FontStyleChooser();
				
				//creating the font style listener
				fontStyleListener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {
						
						//getting the array of the values
						String[] newValues=fontStyleChooser.getValues();
						
						//setting all the properties
						for(int i=0; i<newValues.length; i++){
							
							setProperty(getElement(), propertiesList.get(i+1), newValues[i], true);
						}
					}
				};

				//filling the all panel
				gridBag=new GridBagLayout();
				allPanel.setLayout(gridBag);
				c=new GridBagConstraints();
				c.fill=GridBagConstraints.HORIZONTAL;
				c.anchor=GridBagConstraints.WEST;
				
				c.gridx=0;
				c.gridy=0;
				c.gridwidth=1;

				gridBag.setConstraints(dataPanel, c);
				allPanel.add(dataPanel);
				
				c.gridx=1;
				c.weightx=50;
				JPanel emptyPanel=new JPanel();
				gridBag.setConstraints(emptyPanel, c);
				allPanel.add(emptyPanel);
				
				c.gridx=0;
				c.gridy=1;
				c.weightx=0;
				gridBag.setConstraints(fontStyleChooser, c);
				allPanel.add(fontStyleChooser);

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
