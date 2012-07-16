package fr.itris.glips.rtdaeditor.widget;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.widgets.ColorChooserWidget;

/**
 * the font style chooser
 * @author Jordi SUC
 */
public class FontStyleChooser extends Widget {

	/**
	 * the color chooser
	 */
	protected ColorChooserWidget colorChooser;
	
	/**
	 * the listener to the color chooser
	 */
	protected ActionListener colorChooserListener;
	
	/**
	 * the font family chooser
	 */
	protected FontFamilyChooserWidget fontFamilyChooser;
	
	/**
	 * the font family chooser listener
	 */
	protected ActionListener fontFamilyChooserListener;

	/**
	 * the font size chooser
	 */
	protected IntegerSpinnerWidget fontSizeChooser;
	
	/**
	 * the listener to the font size chooser
	 */
	protected ActionListener fontSizeChooserListener;
	
	/**
	 * the checkbox for the bold option
	 */
	protected JCheckBox boldCheckBox;
	
	/**
	 * the bold checkbox listener
	 */
	protected ActionListener boldCheckBoxListener;
	
	/**
	 * the checkbox for the italic option
	 */
	protected JCheckBox italicCheckBox;
	
	/**
	 * the italic checkbox listener
	 */
	protected ActionListener italicCheckBoxListener;
	
	/**
	 * the array of the values
	 */
	protected String[] values=new String[5];
	
	/**
	 * the constructor of the class
	 */
	public FontStyleChooser(){
		
		build();
	}
	
	/**
	 * initializes the widget with the provided array of values
	 * @param initValues an array of five strings containing 
	 * the values for the following fields: 
	 * {font color, font family, font size, bold, italic};
	 */
	public void init(String[] initValues){
		
		for(int i=0; i<initValues.length; i++){
			
			values[i]=initValues[i];
		}
		
		//initializing all the widgets
		colorChooser.removeListener(colorChooserListener);
		colorChooser.init(initValues[0]);
		colorChooser.addListener(colorChooserListener);
		
		fontFamilyChooser.removeListener(fontFamilyChooserListener);
		fontFamilyChooser.init(initValues[1]);
		fontFamilyChooser.addListener(fontFamilyChooserListener);
		
		fontSizeChooser.removeListener(fontSizeChooserListener);
		fontSizeChooser.init(initValues[2]);
		fontSizeChooser.addListener(fontSizeChooserListener);
		
		boldCheckBox.removeActionListener(boldCheckBoxListener);
		try{
			boldCheckBox.setSelected(Boolean.parseBoolean(initValues[3]));
		}catch (Exception ex){boldCheckBox.setSelected(false);}
		boldCheckBox.addActionListener(boldCheckBoxListener);
		
		italicCheckBox.removeActionListener(italicCheckBoxListener);
		try{
			italicCheckBox.setSelected(Boolean.parseBoolean(initValues[4]));
		}catch (Exception ex){italicCheckBox.setSelected(false);}
		italicCheckBox.addActionListener(italicCheckBoxListener);
	}
	
	@Override
	protected void build() {
		
		//getting the labels
		String fontBorderLabel=ResourcesManager.bundle.getString("FontSizeChooserBorderLabel");
		String colorLabel=ResourcesManager.bundle.getString("FontSizeChooserColorLabel");
		String familyLabel=ResourcesManager.bundle.getString("FontSizeChooserFamilyLabel");
		String sizeLabel=ResourcesManager.bundle.getString("FontSizeChooserSizeLabel");
		String boldLabel=ResourcesManager.bundle.getString("FontSizeChooserBoldLabel");
		String italicLabel=ResourcesManager.bundle.getString("FontSizeChooserItalicLabel");
		
		//creating the jlabels
		JLabel colorJLabel=new JLabel(colorLabel+" : ");
		colorJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel familyJLabel=new JLabel(familyLabel+" : ");
		familyJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel sizeJLabel=new JLabel(sizeLabel+" : ");
		sizeJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//creating the color chooser
		colorChooser=new ColorChooserWidget();
		
		colorChooserListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {

				values[0]=colorChooser.getValue();
				notifyListeners();
			}
		};
		
		//creating the font family chooser
		fontFamilyChooser=new FontFamilyChooserWidget();
		
		fontFamilyChooserListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {

				values[1]=fontFamilyChooser.getValue();
				notifyListeners();
			}
		};

		//creating the font size chooser
		fontSizeChooser=new IntegerSpinnerWidget(12, 0, 1000, 1);
		
		fontSizeChooserListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
		
				values[2]=fontSizeChooser.getValue();
				notifyListeners();
			}
		};
		
		//creating the bold chooser
		boldCheckBox=new JCheckBox(boldLabel);
		
		boldCheckBoxListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
		
				values[3]=Boolean.toString(boldCheckBox.isSelected());
				notifyListeners();
			}
		};
		
		//creating the italic chooser
		italicCheckBox=new JCheckBox(italicLabel);
		
		italicCheckBoxListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {

				values[4]=Boolean.toString(italicCheckBox.isSelected());
				notifyListeners();
			}
		};
		
		//setting the properties of the panel
		setBorder(new TitledBorder(fontBorderLabel));
		
		//building the panel
		GridBagLayout layout=new GridBagLayout();
		setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(1, 1, 1, 1);
		
		c.gridx=0;
		c.gridy=0;
		layout.setConstraints(colorJLabel, c);
		add(colorJLabel);
		
		c.gridx=1;
		layout.setConstraints(colorChooser, c);
		add(colorChooser);
		
		c.gridx=0;
		c.gridy=1;
		layout.setConstraints(familyJLabel, c);
		add(familyJLabel);
		
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(fontFamilyChooser, c);
		add(fontFamilyChooser);

		c.gridx=0;
		c.gridy=2;
		c.weightx=0;
		layout.setConstraints(sizeJLabel, c);
		add(sizeJLabel);
		
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(fontSizeChooser, c);
		add(fontSizeChooser);
		
		c.gridx=0;
		c.gridy=3;
		c.weightx=0;
		layout.setConstraints(boldCheckBox, c);
		add(boldCheckBox);
		
		c.gridx=1;
		layout.setConstraints(italicCheckBox, c);
		add(italicCheckBox);	
	}
	
	/**
	 * @return the array of the values 
	 */
	public String[] getValues() {
		return values;
	}
	
	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
		colorChooser.setEnabled(enable);
		fontFamilyChooser.setEnabled(enable);
		fontSizeChooser.setEnabled(enable);
		boldCheckBox.setEnabled(enable);
		italicCheckBox.setEnabled(enable);
	}
	
	@Override
	public void dispose() {
		
		super.dispose();
		colorChooser.dispose();
		fontFamilyChooser.dispose();
		fontSizeChooser.dispose();
		boldCheckBox.removeActionListener(boldCheckBoxListener);
		italicCheckBox.removeActionListener(italicCheckBoxListener);
	}
}
