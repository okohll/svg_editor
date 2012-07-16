package fr.itris.glips.rtdaeditor.anim.widgets.tageventchooser;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.rtda.action.tagevent.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the selector for the string tag and it value
 * @author Jordi SUC
 */
public class StringTagAndValuesSelector extends JPanel{
	
	/**
	 * the tag event chooser component
	 */
	protected TagEventChooser tagEventChooser;
	
	/**
	 * the radio button
	 */
	protected JRadioButton radioButton;
	
	/**
	 * the tag chooser widget
	 */
	protected TagChooser tagChooserWidget;
	
	/**
	 * the text field use to enter the value for the tag for the condition
	 */
	protected JTextField valueTextField;
	
	/**
	 * the jlabels
	 */
	private JLabel tagValuesLbl;
	
	/**
	 * the condition panel
	 */
	private JPanel conditionPanel;

	/**
	 * the constructor of the class
	 * @param tagEventChooser the tag event chooser
	 * @param radioButton the radio button
	 */
	public StringTagAndValuesSelector(
			TagEventChooser tagEventChooser, JRadioButton radioButton) {
		
		this.tagEventChooser=tagEventChooser;
		this.radioButton=radioButton;
		build();
	}
	
	/**
	 * builds the new component
	 */
	protected void build(){
		
		//getting the labels
		String conditionLabel=ResourcesManager.bundle.getString("rtdaanim_condition");
		String tagValuesLabel=ResourcesManager.bundle.getString("rtdaanim_tagStringValueCondition");
		
		//creating the jlabels
		tagValuesLbl=new JLabel(tagValuesLabel+" : ");
		tagValuesLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//creating the tag chooser
		tagChooserWidget=new TagChooser(TagToolkit.STRING);

		//creating the text field
		valueTextField=new JTextField(10);
		
		//creating the condition panel
		conditionPanel=new JPanel();
		conditionPanel.setBorder(new CompoundBorder(
				new EmptyBorder(0, 25, 0, 0), new TitledBorder(conditionLabel)));
		
		//filling the condition panel
		conditionPanel.setLayout(new BorderLayout(2, 0));
		conditionPanel.add(tagValuesLbl, BorderLayout.WEST);
		conditionPanel.add(valueTextField, BorderLayout.CENTER);
		
		//building the component
		GridBagLayout layout=new GridBagLayout();
		setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();
		c.anchor=GridBagConstraints.WEST;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(1, 1, 1, 1);
		
		c.gridx=0;
		c.gridy=0;
		layout.setConstraints(radioButton, c);
		add(radioButton);
		
		c.gridx=1;
		c.gridy=0;
		c.weightx=50;
		layout.setConstraints(tagChooserWidget, c);
		add(tagChooserWidget);
		
		c.gridx=0;
		c.gridy=1;
		c.gridwidth=2;
		layout.setConstraints(conditionPanel, c);
		add(conditionPanel);
	}
	
	/**
	 * initializes the widgets with the new provided value
	 * @param newValue the new value
	 */
	protected void init(String newValue){
		
		//getting the tag name
		newValue=newValue.trim();
		String[] splitValues=newValue.split(TagEventsManager.separatorRegex);
		
		if(splitValues!=null && splitValues.length==3){

			//getting the tag name
			String tagName=splitValues[1];

			//getting the string value
			String tagValue=splitValues[2];
			
			tagChooserWidget.init(tagName);
			valueTextField.setText(tagValue);
			
		}else{
			
			tagChooserWidget.init("");
			valueTextField.setText("");
		}
	}

	/**
	 * @return the current value 
	 */
	protected String getValue(){
		
		String value="";
		String tagValue=tagChooserWidget.getCurrentValue();
		
		if(tagValue==null){
			
			tagValue="";
		}
		
		value=TagEventsManager.stringKeyword+TagEventsManager.separator+tagValue+
			TagEventsManager.separator+valueTextField.getText();
		
		return value;
	}
	
	@Override
	public void setEnabled(boolean enable){
		
		tagChooserWidget.setEnabled(enable);
		valueTextField.setEnabled(enable);
		conditionPanel.setEnabled(enable);
		tagValuesLbl.setEnabled(enable);
		
		super.setEnabled(enable);
	}
}
