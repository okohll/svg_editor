package fr.itris.glips.rtdaeditor.anim.widgets;

import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * the class of the event chooser
 * @author ITRIS, Jordi SUC
 */
public class RequestChooser extends Widget{
	
	/**
	 * the separator
	 */
	private final static String separator="<br>", separatorRegex="<br>";
	
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
	protected RequestChooser(boolean isEditor){
		
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

		String value=item.getValue();
		
		if(value==null){
			
			value="";
			
		}else{
			
			value=value.replaceAll(separatorRegex, " ");
			value=value.replaceAll("\\s+", " ");
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
		 * the text area
		 */
		private JTextArea textArea;
		
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
			String titleMessage=bundle.getString("rtdaanim_selectrequesttitle");
			String message=bundle.getString("rtdaanim_selectrequestmessage");
			
			//setting the title and the message
			setTitleMessage(titleMessage);
			setMessage(message, INFORMATION_TYPE);
			
			//setting the properties for the text area
			textArea=new JTextArea();
			
			JScrollPane textAreaScrollpane=new JScrollPane(textArea);
			textAreaScrollpane.setPreferredSize(new Dimension(500, 250));
			
			//setting the layout
			thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.Y_AXIS));
			thePanel.add(textAreaScrollpane);
			
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

			//normalizing the start value
			startValue=startValue.replaceAll(separatorRegex+"+", "\n");
			textArea.setText(startValue);
			pack();
			showDialog(moreButton);

			return currentResult;
		}

		/**
		 * @return the chosen value
		 */
		public String getValue() {
			
			//adding separators
			String value=textArea.getText();
			value=value.replaceAll(" +", "&nbsp;");
			value=value.replaceAll("\\s+", separator);
			value=value.replaceAll("&nbsp;", " ");
			value=value.replaceAll("\\s+", " ");
			
			return value;
		}
	}
}
