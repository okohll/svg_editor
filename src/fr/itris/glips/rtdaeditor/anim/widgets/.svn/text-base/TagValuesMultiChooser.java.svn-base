package fr.itris.glips.rtdaeditor.anim.widgets;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
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
public class TagValuesMultiChooser extends Widget{

	/**
	 * the separator
	 */
	private final static String separator="|", separatorRegex="[|]";
	
	/**
	 * the text field
	 */
	private JTextField textField=null;
	
	/**
	 * the button used to launch the dialog
	 */
	private JButton moreButton;
	
	/**
	 * the chooser dialog
	 */
	private ChooserDialog chooserDialog=null;

	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected TagValuesMultiChooser(boolean isEditor){
		
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

		String currentValue=item.getValue();
		
		if(currentValue==null){
			
			currentValue="";
		}
				
		if(item.getPossibleValues().size()==0){
			
			textField.setText("");
			item.setValue("");
			
		}else{
			
			//setting the new text for the text field
			currentValue=currentValue.replaceAll(separatorRegex, ", ");
			
			if(currentValue.endsWith(", ")) {
				
				currentValue=currentValue.substring(0, currentValue.length()-2);
			}
			
			textField.setText(currentValue);
		}
		
		moreButton.setEnabled(item.getPossibleValues().size()!=0);
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
		 * the list
		 */
		private JList list;
		
		/**
		 * the default list model
		 */
		private DefaultListModel listModel;
		
		/**
		 * the list selection model
		 */
		private DefaultListSelectionModel listSelectionModel;
		
		/**
		 * the list listener
		 */
		private ListSelectionListener listSelectionListener;
		
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
			
			//the content panel
			JPanel thePanel=new JPanel();
			thePanel.setBorder(new EmptyBorder(2, 2, 2, 2));
			
			//getting the labels
			ResourceBundle bundle=ResourcesManager.bundle;
			String titleMessage=bundle.getString("rtdaanim_selecttagvaluestitle");
			String message=bundle.getString("rtdaanim_selecttagvaluesmessage");
			
			//setting the title and the message
			setTitleMessage(titleMessage);
			setMessage(message, INFORMATION_TYPE);
			
			//setting the properties for the list
			list=new JList();
			listModel=new DefaultListModel();
			listSelectionModel=new DefaultListSelectionModel();
			list.setModel(listModel);
			list.setSelectionModel(listSelectionModel);
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			
			JScrollPane listScrollpane=new JScrollPane(list);
			listScrollpane.setPreferredSize(new Dimension(200, 150));
			
			//setting the layout
			thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.Y_AXIS));
			thePanel.add(listScrollpane);
			
			//adding the listener to the list
			listSelectionListener=new ListSelectionListener(){
				
				/**
				 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
				 */
				public void valueChanged(ListSelectionEvent evt) {

					if(getItem()!=null && list.getSelectedValues()!=null){
						
						Object[] values=list.getSelectedValues();
						ComboListItem item=null;
						String val="";
						
						for(Object obj : values) {
							
							if(obj!=null) {
								
								item=(ComboListItem)obj;
								val+=item.getValue()+separator;
							}
						}
						
						value=val;
						
					}else {
						
						value="";
					}
				}
			};
			
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

			this.value=startValue;
			
			list.removeListSelectionListener(listSelectionListener);
			
			//clearing the list
			listModel.removeAllElements();
			listSelectionModel.clearSelection();
			
			//getting the current value
			String currentValue=startValue;
			
			if(currentValue==null) {
				
				currentValue="";
			}
			
			//getting the list of the selected values
			String[] splitValues=currentValue.split(separatorRegex);
			Set<String> selectedValues=new HashSet<String>();
			
			for(String val : splitValues) {
				
				if(val!=null && ! val.equals("")) {
					
					selectedValues.add(val);
				}
			}

			//getting the map of the possible values
			Map<String, String> possibleValues=getItem().getPossibleValues();
			
			if(possibleValues!=null){
				
				//filling the list
				String label="", val="";
				ComboListItem listItem=null;
				int i=0;
				
				for(String name : possibleValues.keySet()){
					
					try{
						label=currentItemReference.get().getAnimationObject().getLabel(name);
					}catch (Exception ex){label=name;}
					
					val=possibleValues.get(name);
					listItem=new ComboListItem(val, label);
					listModel.addElement(listItem);
					
					if(selectedValues.contains(val)){

						listSelectionModel.addSelectionInterval(i, i);
					}
					
					i++;
				}
			}
			
			list.addListSelectionListener(listSelectionListener);
			
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
