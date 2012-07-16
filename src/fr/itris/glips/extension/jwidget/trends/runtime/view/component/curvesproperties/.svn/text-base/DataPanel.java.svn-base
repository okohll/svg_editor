package fr.itris.glips.extension.jwidget.trends.runtime.view.component.curvesproperties;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;

/**
 * the class of the panel displaying the widgets used to display 
 * and modify the curves data properties
 * @author ITRIS, Jordi SUC
 */
public class DataPanel extends JPanel{
	
	/**
	 * the components handler
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the textfields
	 */
	private JTextField labelTxt, tagNameTxt, currentValueTxt, lowerLimitTxt, higherLimitTxt;
	
	/**
	 * the limits panel
	 */
	private JPanel limitsPanel;
	
	/**
	 * the empty panel
	 */
	private JPanel emptyPanel;
	
	/**
	 * the constructor of the class
	 * @param curvesPropertiesComponent the curves properties component
	 */
	public DataPanel(CurvesPropertiesComponent curvesPropertiesComponent) {

		this.componentsHandler=curvesPropertiesComponent.getComponentsHandler();
		build();
	}
	
	/**
	 * builds the panel
	 */
	protected void build(){
		
		//getting the labels
		String tagLabelLabel=TrendsBundle.bundle.getString("TagLabel");
		String tagNameLabel=TrendsBundle.bundle.getString("TagName");
		String tagCurrentValueLabel=TrendsBundle.bundle.getString("TagCurrentValue");
		String limitsLabel=TrendsBundle.bundle.getString("Limits");
		String lowerLimitLabel=TrendsBundle.bundle.getString("LowerLimit");
		String higherLimitLabel=TrendsBundle.bundle.getString("HigherLimit");

		//creating the jlabels
		JLabel tagLabelJLabel=new JLabel(tagLabelLabel+" : ");
		tagLabelJLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel tagNameJLabel=new JLabel(tagNameLabel+" : ");
		tagNameJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel tagCurrentValueJLabel=new JLabel(tagCurrentValueLabel+" : ");
		tagCurrentValueJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lowerLimitJLabel=new JLabel(lowerLimitLabel+" : ");
		lowerLimitJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel higherLimitJLabel=new JLabel(higherLimitLabel+" : ");
		higherLimitJLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		//creating the text fields
		labelTxt=new JTextField();
		labelTxt.setEditable(false);
		
		tagNameTxt=new JTextField();
		tagNameTxt.setEditable(false);
		
		currentValueTxt=new JTextField();
		currentValueTxt.setEditable(false);
		
		lowerLimitTxt=new JTextField();
		lowerLimitTxt.setEditable(false);
		
		higherLimitTxt=new JTextField();
		higherLimitTxt.setEditable(false);

		//creating the panels for the limits
		limitsPanel=new JPanel();
		
		//filling the limits panel
		GridBagLayout gridBagLayout=new GridBagLayout();
		limitsPanel.setLayout(gridBagLayout);
		
		TitledBorder border=new TitledBorder(limitsLabel);
		limitsPanel.setBorder(border);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(1, 1, 1, 1);
		
		c.gridx=0;
		c.gridy=0;
		c.anchor=GridBagConstraints.EAST;
		c.weightx=1;
		gridBagLayout.setConstraints(lowerLimitJLabel, c);
		limitsPanel.add(lowerLimitJLabel);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.WEST;
		c.weightx=50;
		gridBagLayout.setConstraints(lowerLimitTxt, c);
		limitsPanel.add(lowerLimitTxt);
		
		c.gridx=0;
		c.gridy=1;
		c.anchor=GridBagConstraints.EAST;
		c.weightx=1;
		gridBagLayout.setConstraints(higherLimitJLabel, c);
		limitsPanel.add(higherLimitJLabel);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.WEST;
		c.weightx=50;
		gridBagLayout.setConstraints(higherLimitTxt, c);
		limitsPanel.add(higherLimitTxt);
	
		//filling the data panel
		gridBagLayout=new GridBagLayout();
		setLayout(gridBagLayout);
		c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(1, 1, 1, 1);
		
		c.gridx=0;
		c.gridy=0;
		c.anchor=GridBagConstraints.EAST;
		c.weightx=1;
		gridBagLayout.setConstraints(tagLabelJLabel, c);
		add(tagLabelJLabel);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.WEST;
		c.weightx=50;
		gridBagLayout.setConstraints(labelTxt, c);
		add(labelTxt);
		
		c.gridx=0;
		c.gridy=1;
		c.anchor=GridBagConstraints.EAST;
		c.weightx=1;
		gridBagLayout.setConstraints(tagNameJLabel, c);
		add(tagNameJLabel);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.WEST;
		c.weightx=50;
		gridBagLayout.setConstraints(tagNameTxt, c);
		add(tagNameTxt);
		
		c.gridx=0;
		c.gridy=2;
		c.anchor=GridBagConstraints.EAST;
		c.weightx=1;
		gridBagLayout.setConstraints(tagCurrentValueJLabel, c);
		add(tagCurrentValueJLabel);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.WEST;
		c.weightx=50;
		gridBagLayout.setConstraints(currentValueTxt, c);
		add(currentValueTxt);
		
		c.gridx=0;
		c.gridy=3;
		c.weightx=1;
		c.gridwidth=2;
		gridBagLayout.setConstraints(limitsPanel, c);
		add(limitsPanel);
		
		//the empty panel
		emptyPanel=new JPanel();
		c.gridx=0;
		c.gridy=4;
		c.weightx=1;
		c.weighty=50;
		c.gridwidth=2;
		gridBagLayout.setConstraints(emptyPanel, c);
		add(emptyPanel);
	}
	
	/**
	 * changes the curve for the curves properties
	 * @param trendsCurveConfiguration the trends curve configuration
	 */
	public void changeCurve(TrendsCurveConfiguration trendsCurveConfiguration){
		
		if(trendsCurveConfiguration!=null){
			
			labelTxt.setText(trendsCurveConfiguration.getLabel());
			tagNameTxt.setText(trendsCurveConfiguration.getTagName());
			currentValueTxt.setText("");
			
			if(trendsCurveConfiguration.isEnumeratedTag()){
				
				limitsPanel.setVisible(false);
				emptyPanel.setVisible(true);
				
			}else{
				
				lowerLimitTxt.setText(
						componentsHandler.getStringRepresentation(
								trendsCurveConfiguration.getMinValue()));
				higherLimitTxt.setText(
						componentsHandler.getStringRepresentation(
								trendsCurveConfiguration.getMaxValue()));
				emptyPanel.setVisible(false);
				limitsPanel.setVisible(true);
			}
		}
	}
	
	/**
	 * sets the current value for the tag
	 * @param currentValue the current value
	 */
	public void setCurrentValue(Object currentValue){
		
		if(currentValue instanceof String){
			
			currentValueTxt.setText((String)currentValue);
			
		}else if(currentValue instanceof Double){
			
			currentValueTxt.setText(componentsHandler.getStringRepresentation((Double)currentValue));
		}
	}
}
