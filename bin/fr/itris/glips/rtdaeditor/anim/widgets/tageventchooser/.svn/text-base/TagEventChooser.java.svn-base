package fr.itris.glips.rtdaeditor.anim.widgets.tageventchooser;

import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.action.tagevent.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.anim.widgets.Widget;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * the class of the event chooser
 * @author ITRIS, Jordi SUC
 */
public class TagEventChooser extends Widget{
	
	/**
	 * the text field
	 */
	private JTextField textField;
	
	/**
	 * the button used to launch the dialog
	 */
	private JButton moreButton;
	
	/**
	 * the chooser dialog
	 */
	private ChooserDialog chooserDialog;

	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	public TagEventChooser(boolean isEditor){
		
		super(isEditor);
		buildWidget();
	}
	
	/**
	 * builds the widget
	 */
	protected void buildWidget(){
		
		//setting the properties for the text field
		textField=new JTextField();
		textField.setEditable(false);
		textField.setOpaque(false);
		textField.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		//setting the properties of the button
		moreButton=new JButton();
		String moreChooserLabel="";
		ResourceBundle bundle=ResourcesManager.bundle;
		
		try{
			moreChooserLabel=bundle.getString("rtdaanim_tagchooserbutton");
		}catch (Exception ex){}
			
		moreButton.setText(moreChooserLabel);
		moreButton.setMargin(new Insets(1, 1, 1, 1));

		setLayout(new BorderLayout(1, 0));
		add(textField, BorderLayout.CENTER);
		add(moreButton, BorderLayout.EAST);

		if(isEditor) {
			
			//the chooser dialog
			if(Editor.getParent() instanceof Frame){
				
				chooserDialog=new ChooserDialog((Frame)Editor.getParent());
				
			}else if(Editor.getParent() instanceof JDialog){
				
				chooserDialog=new ChooserDialog((JDialog)Editor.getParent());
			}
			
			//adding the listener to the button
			moreButton.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {

					//showing the dialog
					int result=chooserDialog.showDialog(getItem().getValue());

					if(result==ChooserDialog.OK){

						getItem().setValue(chooserDialog.getValue());
						chooserDialog.clear();
					}

					if(validateRunnable!=null) {
						
						validateRunnable.run();
					}
				}
			});
		}
	}
	
	@Override
	protected void setItem(EditableItem item, Runnable validateRunnable){

		super.setItem(item, validateRunnable);
		
		//computing the label to display in the text field
		String label="";
		String value=item.getValue().trim();
		
		//splitting the value
		String[] splitValues=value.split(TagEventsManager.separatorRegex);
		
		if(splitValues!=null){
			
			if(splitValues.length==4 && 
					splitValues[0].equals(TagEventsManager.analogicKeyword)){
				
				//getting the tag name
				String tagName=splitValues[1];

				//getting the equality mode 
				String equalityMode=splitValues[2];
				
				//getting the tag value
				String tagValue=splitValues[3];
				
				label=tagName+" "+equalityMode+" "+tagValue;
				
			}else if(splitValues.length==3 && 
					splitValues[0].equals(TagEventsManager.stringKeyword)){
				
				//getting the tag name
				String tagName=splitValues[1];

				//getting the string value
				String tagValue=splitValues[2];
				
				label=tagName+" = "+tagValue;
				
			}else if(splitValues.length>1 && ! splitValues[1].equals("")){
				
				//getting the tag name
				String tagName=splitValues[1];
				label=tagName+" = {";

				if(splitValues.length>1){
					
					for(int i=2; i<splitValues.length; i++){
						
						label+=splitValues[i]+(i<(splitValues.length-1)?", ":"");
					}
				}
				
				label+="}";
			}
		}

		//setting the textfield text
		textField.setText(label);
	}
	
	/**
	 * the class of the dialog used to choose the tag and 
	 * the conditions that will defines the tag event
	 * @author ITRIS, Jordi SUC
	 */
	protected class ChooserDialog extends TitledDialog{
		
		/**
		 * the ok constant
		 */
		public static final int OK=0;
		
		/**
		 * the cancel constant
		 */
		public static final int CANCEL=1;
		
		/**
		 * the message labels
		 */
		protected String infoLabel;
		
		/**
		 * the radio buttons 
		 */
		protected JRadioButton enumRadioButton, 
			analogRadioButton, stringRadioButton;
		
		/**
		 * the selector for the enumerated tag and its condition
		 */
		protected EnumeratedTagAndValuesSelector enumTagValuesSelector;
		
		/**
		 * the selector for the analogic tag and its condition
		 */
		private AnalogicTagAndValuesSelector analogicTagAndValuesSelector;
		
		/**
		 * the selector for the string tag and its condition
		 */
		private StringTagAndValuesSelector stringTagAndValuesSelector;
		
		/**
		 * the result
		 */
		protected int currentResult=OK;
		
		/**
		 * the listener to the radio buttons
		 */
		protected ActionListener radioButtonListener;
		
		/**
		 * the constructor of the class
		 * @param parent the parent container
		 */
		protected ChooserDialog(Frame parent){
			
			super(parent, true, true);
		}
		
		/**
		 * the constructor of the class
		 * @param parent the parent container
		 */
		protected ChooserDialog(JDialog parent){
			
			super(parent, true);
		}
		
		@Override
		protected JPanel buildContentPanel() {
			
			//the content panel
			JPanel thePanel=new JPanel();
			thePanel.setBorder(new EmptyBorder(2, 2, 2, 2));
			
			//getting the labels
			ResourceBundle bundle=ResourcesManager.bundle;
			String titleMessage=bundle.getString("rtdaanim_tageventchoosertitle");
			infoLabel=bundle.getString("rtdaanim_tageventchoosermessage");
			String enumeratedTagLabel=TagToolkit.enumeratedTagLabel;
			String analogicTagLabel=TagToolkit.analogicTagLabel;
			String stringTagLabel=TagToolkit.stringTagLabel;

			//setting the title and the message
			setTitleMessage(titleMessage);
			setMessage(infoLabel, INFORMATION_TYPE);
			
			//creating the radio buttons
			enumRadioButton=new JRadioButton(enumeratedTagLabel+" : ");
			analogRadioButton=new JRadioButton(analogicTagLabel+" : ");
			stringRadioButton=new JRadioButton(stringTagLabel+" : ");
			
			//creating the listener to the radio buttons
			radioButtonListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					
					selectRadioButton((JRadioButton)e.getSource(), "");
				}
			};
			
			//creating the button group for the radio buttons
			ButtonGroup buttonGroup=new ButtonGroup();
			buttonGroup.add(enumRadioButton);
			buttonGroup.add(analogRadioButton);
			buttonGroup.add(stringRadioButton);
			
			//the enumerated condition selector
			enumTagValuesSelector=new EnumeratedTagAndValuesSelector(
					TagEventChooser.this, enumRadioButton);
			
			//the analogic condition selector
			analogicTagAndValuesSelector=new AnalogicTagAndValuesSelector(
					TagEventChooser.this, analogRadioButton);
			
			//the string condition selector
			stringTagAndValuesSelector=new StringTagAndValuesSelector(
					TagEventChooser.this, stringRadioButton);
			
			//building the component
			GridBagLayout layout=new GridBagLayout();
			thePanel.setLayout(layout);
			GridBagConstraints c=new GridBagConstraints();
			c.insets=new Insets(1, 1, 1, 1);
			c.anchor=GridBagConstraints.WEST;
			c.fill=GridBagConstraints.HORIZONTAL;
			c.weightx=50;
			
			c.gridx=0;
			c.gridy=0;
			layout.setConstraints(enumTagValuesSelector, c);
			thePanel.add(enumTagValuesSelector);
			
			c.gridy=1;
			layout.setConstraints(analogicTagAndValuesSelector, c);
			thePanel.add(analogicTagAndValuesSelector);
			
			c.gridy=2;
			layout.setConstraints(stringTagAndValuesSelector, c);
			thePanel.add(stringTagAndValuesSelector);
			
			//adding the listeners to the buttons
			ActionListener buttonsListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {

					if(evt.getSource().equals(okButton)){
						
						currentResult=OK;
						
					}else{
						
						currentResult=CANCEL;
					}
					
					setVisible(false);
				}
			};
			
			okButtonListener=buttonsListener;
			cancelButtonListener=buttonsListener;
			
			okButton.addActionListener(buttonsListener);
			cancelButton.addActionListener(buttonsListener);
			
			return thePanel;
		}
		
		/**
		 * shows the dialog 
		 * @param startValue the initial value
		 * @return whether the user has clicked 
		 */
		public int showDialog(String startValue){
			
			handleComponentsState(startValue);
			
			pack();
			showDialog(moreButton);

			return currentResult;
		}
		
		/**
		 * handles the initial value and state of the components
		 * @param startValue the start value
		 */
		protected void handleComponentsState(String startValue){
			
			enumRadioButton.removeActionListener(radioButtonListener);
			analogRadioButton.removeActionListener(radioButtonListener);
			stringRadioButton.removeActionListener(radioButtonListener);
			
			if(startValue.indexOf(TagEventsManager.analogicKeyword)!=-1){
				
				selectRadioButton(analogRadioButton, startValue);
				
			}else if(startValue.indexOf(TagEventsManager.stringKeyword)!=-1){
				
				selectRadioButton(stringRadioButton, startValue);
				
			}else{
				
				selectRadioButton(enumRadioButton, startValue);
			}
			
			enumRadioButton.addActionListener(radioButtonListener);
			analogRadioButton.addActionListener(radioButtonListener);
			stringRadioButton.addActionListener(radioButtonListener);
		}
		
		/**
		 * selects the provided button and handles the related widgets
		 * @param button a radio button
		 * @param startValue the new value
		 */
		protected void selectRadioButton(JRadioButton button, String startValue){
			
			button.setSelected(true);
			
			//reinitializing all the components
			enumTagValuesSelector.setEnabled(false);
			enumTagValuesSelector.init("");
			analogicTagAndValuesSelector.setEnabled(false);
			analogicTagAndValuesSelector.init("");
			stringTagAndValuesSelector.setEnabled(false);
			stringTagAndValuesSelector.init("");
			
			if(button.equals(enumRadioButton)){
				
				enumTagValuesSelector.setEnabled(true);
				enumTagValuesSelector.init(startValue);
				
			}else if(button.equals(analogRadioButton)){
				
				analogicTagAndValuesSelector.setEnabled(true);
				analogicTagAndValuesSelector.init(startValue);
				
			}else if(button.equals(stringRadioButton)){

				stringTagAndValuesSelector.setEnabled(true);
				stringTagAndValuesSelector.init(startValue);
			}
		}

		/**
		 * @return the chosen value
		 */
		public String getValue() {
			
			String value="";
			
			if(enumRadioButton.isSelected()){
				
				value=enumTagValuesSelector.getValue();
				
			}else if(analogRadioButton.isSelected()){
				
				value=analogicTagAndValuesSelector.getValue();
				
			}else if(stringRadioButton.isSelected()){
				
				value=stringTagAndValuesSelector.getValue();
			}

			return value;
		}
		
		/**
		 * clears all the items in the chooser
		 */
		public void clear(){
			
			enumTagValuesSelector.init("");
			analogicTagAndValuesSelector.init("");
			stringTagAndValuesSelector.init("");
		}		
	}
}
