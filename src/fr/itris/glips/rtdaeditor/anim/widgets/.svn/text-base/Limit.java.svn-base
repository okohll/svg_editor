package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.anim.widgets.tageventchooser.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the widget used to selected a limit
 * @author ITRIS, Jordi SUC
 */
public class Limit extends Widget{
	
	/**
	 * whether this widget is designed for the child mode
	 */
	private boolean isChildMode;

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
	 * the labels for the values
	 */
	private String autoValueLabel="", infinityValueLabel="";
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 * @param isChildMode whether this widget is designed for the child mode
	 */
	protected Limit(boolean isEditor, boolean isChildMode){
		
		super(isEditor);
		this.isChildMode=isChildMode;
		
		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){
		
		//getting the labels for the possible values
		autoValueLabel=ResourcesManager.bundle.getString("rtdaanim_automatic");
		infinityValueLabel=ResourcesManager.bundle.getString("rtdaanim_infiniteValue");
		
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
					chooserDialog.clear();
					int result=chooserDialog.showDialog(getItem().getValue());

					if(result==ChooserDialog.OK){
						
						String newValue=chooserDialog.getValue();
						chooserDialog.clear();
						
						if(newValue==null){
							
							newValue="";
						}

						getItem().setValue(newValue);
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

		String value=item.getValue();
		
		if(value==null){
			
			value="";
		}
		
		if(value.equals(DataLimit.AUTO_LIMIT)){
			
			value=autoValueLabel;
			
		}else if(value.equals(DataLimit.INFINITY_LIMIT)){
			
			value=infinityValueLabel;
		}
		
		textField.setText(value);
		textField.setCaretPosition(0);
	}

	/**
	 * the class of the dialog used to choose the method 
	 * for returning to the initial value
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
		 * the radio buttons
		 */
		private JRadioButton autoRadioButton, absRadioButton, 
			perCentRadioButton, infiniteRadioButton, tagRadioButton;
		
		/**
		 * the button group for the checkboxes
		 */
		private ButtonGroup buttonGroup;
		
		/**
		 * the listener to the radio buttons
		 */
		private ActionListener radioButtonsListener;
		
		/**
		 * the spinners
		 */
		private DoubleSpinnerWidget absSpinner, perCentSpinner;
		
		/**
		 * the tag chooser
		 */
		private TagChooser tagChooser;
		
		/**
		 * the result
		 */
		private int currentResult=OK;
		
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
			String titleMessage=bundle.getString("rtdaanim_selectLimitTitle");
			String message=bundle.getString(isChildMode?
				"rtdaanim_selectLimitMessageIsChild":"rtdaanim_selectLimitMessageNotChild");
			String autoLabel=bundle.getString("rtdaanim_automatic");
			String absValueLabel=bundle.getString("rtdaanim_absValue");
			String perCentValueLabel=bundle.getString("rtdaanim_perCentValue");
			String infiniteValueLabel=bundle.getString("rtdaanim_infiniteValue");
			String tagNameLabel=bundle.getString("rtdaanim_tag");
			
			//setting the title and the message
			setTitleMessage(titleMessage);
			setMessage(message, INFORMATION_TYPE);
			
			//creating the widgets//
			
			//creating the button group handler
			buttonGroup=new ButtonGroup();
			
			//creating the radio buttons
			absRadioButton=new JRadioButton(absValueLabel+" : ");
			buttonGroup.add(absRadioButton);
			tagRadioButton=new JRadioButton(tagNameLabel+" : ");
			buttonGroup.add(tagRadioButton);
			
			if(isChildMode){
				
				perCentRadioButton=new JRadioButton(perCentValueLabel+" : ");
				buttonGroup.add(perCentRadioButton);
				infiniteRadioButton=new JRadioButton(infiniteValueLabel);
				buttonGroup.add(infiniteRadioButton);
				
			}else{
				
				autoRadioButton=new JRadioButton(autoLabel);
				buttonGroup.add(autoRadioButton);
			}
			
			//creating the spinners
			absSpinner=new DoubleSpinnerWidget(
					0, -10000000000D, 1000000000D, 1, false);
			
			if(isChildMode){
				
				perCentSpinner=new DoubleSpinnerWidget(0, 0, 100, 1, false);
			}
			
			//creating the tag chooser
			tagChooser=new TagChooser(TagToolkit.ANALOGIC);
			
			//creating the listeners//
			
			//creating the listener to the radio buttons
			radioButtonsListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					
					selectRadioButton((JRadioButton)e.getSource(), "");
				}
			};

			//filling the panel with the widgets//
			
			//setting the layout for the panel
			GridBagLayout layout=new GridBagLayout();
			thePanel.setLayout(layout);
			GridBagConstraints c=new GridBagConstraints();
			c.fill=GridBagConstraints.HORIZONTAL;
			c.insets=new Insets(2, 2, 2, 2);
			int y=0;
			
			if(! isChildMode){
				
				c.gridx=0;
				c.gridy=y++;
				c.gridwidth=3;
				layout.setConstraints(autoRadioButton, c);
				thePanel.add(autoRadioButton);
				c.gridwidth=1;
			}

			c.gridy=y++;
			layout.setConstraints(absRadioButton, c);
			thePanel.add(absRadioButton);
			
			c.gridx=1;
			c.weightx=50;
			c.gridwidth=2;
			layout.setConstraints(absSpinner, c);
			thePanel.add(absSpinner);
			c.weightx=0;
			c.gridwidth=1;
			
			if(isChildMode){
				
				c.gridx=0;
				c.gridy=y++;
				c.gridwidth=1;
				layout.setConstraints(perCentRadioButton, c);
				thePanel.add(perCentRadioButton);
				
				c.gridx=1;
				c.weightx=50;
				layout.setConstraints(perCentSpinner, c);
				thePanel.add(perCentSpinner);
				
				JLabel perCentLabel=new JLabel("%");
				c.gridx=2;
				c.weightx=0;
				layout.setConstraints(perCentLabel, c);
				thePanel.add(perCentLabel);
				
				c.gridx=0;
				c.gridy=y++;
				c.gridwidth=3;
				layout.setConstraints(infiniteRadioButton, c);
				thePanel.add(infiniteRadioButton);
				c.gridwidth=1;
			}
			
			c.gridx=0;
			c.gridy=y++;
			layout.setConstraints(tagRadioButton, c);
			thePanel.add(tagRadioButton);
			
			c.gridx=1;
			c.weightx=50;
			c.gridwidth=2;
			layout.setConstraints(tagChooser, c);
			thePanel.add(tagChooser);
			
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
			
			//removing the listener the radio buttons
			absRadioButton.removeActionListener(radioButtonsListener);
			tagRadioButton.removeActionListener(radioButtonsListener);
			
			if(isChildMode){
				
				perCentRadioButton.removeActionListener(radioButtonsListener);
				infiniteRadioButton.removeActionListener(radioButtonsListener);
				
			}else{
				
				autoRadioButton.removeActionListener(radioButtonsListener);
			}
			
			if(autoRadioButton!=null && startValue.equals(DataLimit.AUTO_LIMIT)){
				
				selectRadioButton(autoRadioButton, startValue);
				
			}else if(infiniteRadioButton!=null && 
					startValue.equals(DataLimit.INFINITY_LIMIT)){
				
				infiniteRadioButton.setSelected(true);
				
			}else if(perCentRadioButton!=null && startValue.indexOf("%")!=-1){

				selectRadioButton(perCentRadioButton, startValue);
				
			}else{
				
				double value=Double.NaN;
				
				try{
					value=Double.parseDouble(startValue);
				}catch (Exception ex){}
				
				selectRadioButton(Double.isNaN(value)?
						tagRadioButton:absRadioButton, startValue);
			}

			//adding the listener the radio buttons
			absRadioButton.addActionListener(radioButtonsListener);
			tagRadioButton.addActionListener(radioButtonsListener);
			
			if(isChildMode){
				
				perCentRadioButton.addActionListener(radioButtonsListener);
				infiniteRadioButton.addActionListener(radioButtonsListener);
				
			}else{
				
				autoRadioButton.addActionListener(radioButtonsListener);
			}
		}
		
		/**
		 * selects the provided button and handles the related widgets
		 * @param button a radio button
		 * @param startValue the new value
		 */
		protected void selectRadioButton(JRadioButton button, String startValue){
			
			button.setSelected(true);
			
			//reinitializing all the components
			absSpinner.setEnabled(false);
			tagChooser.setEnabled(false);
			
			if(perCentSpinner!=null){
				
				perCentSpinner.setEnabled(false);
			}
			
			if(button.equals(absRadioButton)){
				
				absSpinner.setEnabled(true);
				absSpinner.init(startValue);
				
			}else if(button.equals(tagRadioButton)){
				
				tagChooser.setEnabled(true);
				tagChooser.init(startValue);
				
			}else if(perCentRadioButton!=null &&
					button.equals(perCentRadioButton)){
				
				perCentSpinner.setEnabled(true);
				
				//getting the percentage value
				int pos=startValue.indexOf("%");
				
				if(pos!=-1){
					
					startValue=startValue.substring(0, pos);
				}
				
				perCentSpinner.init(startValue);
			}
		}

		/**
		 * @return the chosen value
		 */
		public String getValue() {
			
			String value="";
			
			if(autoRadioButton!=null && autoRadioButton.isSelected()){
				
				value=DataLimit.AUTO_LIMIT;
				
			}else if(absRadioButton.isSelected()){
				
				value=FormatStore.format(absSpinner.getWidgetValue());
				
			}else if(perCentRadioButton!=null && perCentRadioButton.isSelected()){
				
				value=FormatStore.format(perCentSpinner.getWidgetValue())+"%";
				
			}else if(infiniteRadioButton!=null && infiniteRadioButton.isSelected()){
				
				value=DataLimit.INFINITY_LIMIT;
				
			}else if(tagRadioButton.isSelected()){
				
				value=tagChooser.getCurrentValue();
			}
			
			return value;
		}
		
		/**
		 * clears all the items in the chooser
		 */
		public void clear(){
				
			absSpinner.init("");
			tagChooser.init("");
			
			if(perCentSpinner!=null){
				
				perCentSpinner.init("");
			}
		}		
	}
}
