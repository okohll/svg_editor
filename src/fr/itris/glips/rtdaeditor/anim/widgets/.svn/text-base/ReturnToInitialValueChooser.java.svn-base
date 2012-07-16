package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;
import javax.swing.border.*;

/**
 * the class of the chooser of the method for returning to the initial value
 * @author ITRIS, Jordi SUC
 */
public class ReturnToInitialValueChooser extends Widget{

	/**
	 * the possible values for this widget
	 */
	private final String autoValue="auto", mouseUpEventValue="mouseUpEvent";
	
	/**
	 * the label
	 */
	private final JLabel jlabel=new JLabel();
	
	/**
	 * the button used to launch the dialog
	 */
	private final JButton moreButton=new JButton();
	
	/**
	 * the chooser dialog
	 */
	private ChooserDialog chooserDialog;
	
	/**
	 * the labels
	 */
	private String autoLabel="", mouseUpEventLabel="", 
							secondsLabel="", beforeTimerLabel="";
	
	/**
	 * whether the chooser should be simple, ie, 
	 * not using the mouseup event feature
	 */
	protected boolean isSimple;
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 * @param isSimple whether the chooser should be simple, ie, 
	 * not using the mouseup event feature
	 */
	protected ReturnToInitialValueChooser(boolean isEditor, boolean isSimple){
		
		super(isEditor);
		this.isSimple=isSimple;
		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){
		
		//getting the labels
		String chooserLabel="";
		ResourceBundle bundle=ResourcesManager.bundle;
		
		try{
			chooserLabel=bundle.getString("rtdaanim_tagchooserbutton");
			autoLabel=bundle.getString("rtdaanim_returntoinitialvalueauto");
			mouseUpEventLabel=bundle.getString("rtdaanim_mouseUpEvent");
			secondsLabel=bundle.getString("rtdaanim_seconds");
			beforeTimerLabel=bundle.getString("rtdaanim_beforetimer");
		}catch (Exception ex){}
			
		moreButton.setText(chooserLabel);
		moreButton.setMargin(new Insets(1, 1, 1, 1));
		jlabel.setOpaque(false);
		
		setLayout(new BorderLayout(1, 0));
		add(jlabel, BorderLayout.CENTER);
		add(moreButton, BorderLayout.EAST);
		
		if(isEditor) {
			
			if(Editor.getParent() instanceof Frame){
				
				chooserDialog=new ChooserDialog((Frame)Editor.getParent());
				
			}else{
				
				chooserDialog=new ChooserDialog((JDialog)Editor.getParent());
			}
			
			//adding the listener to the button
			moreButton.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {

					//showing the dialog
					int result=chooserDialog.showDialog(getItem().getValue());

					if(result==ChooserDialog.OK){
						
						String newValue=chooserDialog.getValue();
						
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
		
		String label="";
		String value=item.getValue();
		double timer=Double.NaN;
		
		//getting the timer value if it is a number
		try {
			timer=Double.parseDouble(value);
		}catch (Exception ex) {}
		
		if(value.equals("")){
			
			value=autoValue;
		}
		
		//getting the label corresponding to the value
		if(! Double.isNaN(timer)) {
			
			//the value is a number
			label=beforeTimerLabel+FormatStore.format(timer)+secondsLabel;
			
		}else if(value.equals(autoValue)) {
			
			label=autoLabel;
			
		}else if(! isSimple && value.equals(mouseUpEventValue)) {
			
			label=mouseUpEventLabel;
		}
		
		jlabel.setText(label);
	}
	
	/**
	 * the class of the dialog used to choose the method for returning to the initial value
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
		private JRadioButton autoRadioButton, 
			mouseUpEventRadioButton, timerRadioButton;
		
		/**
		 * the timer spinner
		 */
		private JSpinner timerSpinner;
		
		/**
		 * the value
		 */
		private String value="";
		
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
			
			JPanel thePanel=new JPanel();
			thePanel.setBorder(new EmptyBorder(5, 5, 10, 5));
			
			//getting the labels
			ResourceBundle bundle=ResourcesManager.bundle;
			String titleMessage=bundle.getString("rtdaanim_ReturnToInitialValueChooserTitle");
			String message="";
			
			if(isSimple){
				
				message=bundle.getString("rtdaanim_ReturnToInitialValueChooserSimpleMessage");
				
			}else{
				
				message=bundle.getString("rtdaanim_ReturnToInitialValueChooserMessage");
			}
			
			//setting the messages
			setTitleMessage(titleMessage);
			setMessage(message, INFORMATION_TYPE);
			
			//creating the radio buttons
			autoRadioButton=new JRadioButton();
			timerRadioButton=new JRadioButton();
			
			if(! isSimple){
				
				mouseUpEventRadioButton=new JRadioButton();
			}
			
			//handling the radio buttons
			autoRadioButton.setText(autoLabel);
			timerRadioButton.setText(beforeTimerLabel);
			
			if(! isSimple){
				
				mouseUpEventRadioButton.setText(mouseUpEventLabel);
			}

			ButtonGroup group=new ButtonGroup();
			group.add(autoRadioButton);
			group.add(timerRadioButton);
			
			if(! isSimple){
				
				group.add(mouseUpEventRadioButton);
			}

			//the timer spinner
			SpinnerNumberModel spinnerModel=new SpinnerNumberModel(
					0, 0, Double.POSITIVE_INFINITY-1, 1);
			timerSpinner=new JSpinner(spinnerModel);
			
			//the jlabels
			JLabel secondsLbl=new JLabel(secondsLabel);
			
			//setting the layout
			GridBagLayout gridBag=new GridBagLayout();
			GridBagConstraints c=new GridBagConstraints();
			thePanel.setLayout(gridBag);
			c.fill=GridBagConstraints.HORIZONTAL;

			c.gridwidth=GridBagConstraints.REMAINDER;
			c.insets=new Insets(0, 0, 8, 0);
			
			c.gridwidth=GridBagConstraints.REMAINDER;
			c.insets=new Insets(0, 15, 0, 0);
			gridBag.setConstraints(autoRadioButton, c);
			thePanel.add(autoRadioButton);
			
			if(! isSimple){
				
				gridBag.setConstraints(mouseUpEventRadioButton, c);
				thePanel.add(mouseUpEventRadioButton);
			}

			c.gridwidth=1;
			gridBag.setConstraints(timerRadioButton, c);
			thePanel.add(timerRadioButton);

			c.weightx=50;
			c.insets=new Insets(0, 0, 0, 0);
			gridBag.setConstraints(timerSpinner, c);
			thePanel.add(timerSpinner);
			c.weightx=0;
			c.insets=new Insets(0, 3, 0, 0);
			gridBag.setConstraints(secondsLbl, c);
			thePanel.add(secondsLbl);
			
			//adding the listener to the radio buttons
			ActionListener radioButtonListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {
					
					timerSpinner.setEnabled(false);
					
					if(evt.getSource().equals(autoRadioButton)){
						
						value=autoValue;
						
					}else if(! isSimple && 
							evt.getSource().equals(mouseUpEventRadioButton)){

						value=mouseUpEventValue;
						
					}else {

						timerSpinner.setEnabled(true);
						value=timerSpinner.getValue()+"";
					}
				}
			};
			
			autoRadioButton.addActionListener(radioButtonListener);
			timerRadioButton.addActionListener(radioButtonListener);
			
			if(! isSimple){
				
				mouseUpEventRadioButton.addActionListener(radioButtonListener);
			}

			//adding the listener to the spinner
			timerSpinner.addChangeListener(new ChangeListener() {

				/**
				 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
				 */
				public void stateChanged(ChangeEvent evt) {

					value=timerSpinner.getValue()+"";
				}
			});
			
			//adding the listeners to the buttons
			ActionListener buttonListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {

					if(evt.getSource().equals(okButton)){
						
						currentResult=OK;
						
					}else{
						
						currentResult=CANCEL;
					}
					
					setVisible(false);
				}
			};
			
			okButtonListener=buttonListener;
			cancelButtonListener=buttonListener;
			okButton.addActionListener(okButtonListener);
			cancelButton.addActionListener(cancelButtonListener);
			
			return thePanel;
		}
		
		/**
		 * shows the dialog 
		 * @param startValue the initial value
		 * @return whether the user has clicked 
		 */
		public int showDialog(String startValue){

			this.value=startValue;
			
			double timer=Double.NaN;
			
			//getting the timer value if it is a number
			try {
				timer=Double.parseDouble(value);
			}catch (Exception ex) {}
			
			if(value.equals("")){
				
				value=autoValue;
			}
			
			timerSpinner.setEnabled(false);
			
			//getting the label corresponding to the value
			if(! Double.isNaN(timer)) {
				
				//the value is a number
				timerRadioButton.setSelected(true);
				timerSpinner.setEnabled(true);
				timerSpinner.setValue(timer);

			}else if(value.equals(autoValue)) {
				
				autoRadioButton.setSelected(true);
				
			}else if(value.equals(mouseUpEventValue)) {
				
				mouseUpEventRadioButton.setSelected(true);
			}
			
			pack();
			showDialog(moreButton);

			return currentResult;
		}

		/**
		 * @return the chosen value
		 */
		public String getValue() {
			return value;
		}
	}
}
