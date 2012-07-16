package fr.itris.glips.rtdaeditor.anim.widgets;

import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * the class of the event chooser
 * @author ITRIS, Jordi SUC
 */
public class TableDBChooser extends Widget{
	
	/**
	 * the separator
	 */
	private final static String separator="|", separatorRegex="[|]";
	
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
	 * the label used for linking the db name to the table name
	 */
	private String inLabel="";

	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected TableDBChooser(boolean isEditor){
		
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
			inLabel=bundle.getString("rtdaanim_in");
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
		
		//reinitializing the textfield text
		textField.setText("");
		
		//getting the value of the DB name and the table name
		String value=item.getValue();
		String[] values=getDbAndTable(value);
		String dbName=values[0], tableName=values[1];
		
		if(! dbName.equals("") && ! tableName.equals("")){
			
			textField.setText("\""+tableName+"\" "+inLabel+" \""+dbName+"\"");
		}
	}
	
	/**
	 * returns an array of two values one for the name of a db, 
	 * the other for a table name
	 * @param value a value
	 * @return an array of two values one for the name of a db, 
	 * the other for a table name
	 */
	protected String[] getDbAndTable(String value){
		
		String[] valArray=new String[2];
		valArray[0]="";
		valArray[1]="";
		
		if(value.indexOf(separator)!=-1){
			
			String[] splitValues=value.split(separatorRegex);
			
			if(splitValues!=null && splitValues.length==2){
				
				valArray[0]=splitValues[0];
				valArray[1]=splitValues[1];
			}
		}
		
		return valArray;
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
		 * the message labels
		 */
		protected String infoLabel, missingDBLabel, missingTableLabel;
		
		/**
		 * the combo boxes
		 */
		protected JComboBox dbCombo, tableCombo;
		
		/**
		 * the combo listener
		 */
		protected ActionListener comboListener;
		
		/**
		 * the result
		 */
		protected int currentResult=OK;
		
		/**
		 * the current map of the db and table names
		 */
		protected LinkedHashMap<String, Set<String>> currentDBAndTableNames=
			new LinkedHashMap<String, Set<String>>();
		
		/**
		 * the current root element
		 */
		protected Element currentRootElement;
		
		/**
		 * the current db and table names
		 */
		protected String currentDBName="", currentTableName="";
		
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
			String titleMessage=bundle.getString("rtdaanim_selectdbtabletitle");
			infoLabel=bundle.getString("rtdaanim_selectdbtablemessage");
			missingDBLabel=bundle.getString("rtdaanim_missingdbmessage");
			missingTableLabel=bundle.getString("rtdaanim_missingtablemessage");
			String dbLabel=bundle.getString("rtdaanim_db");
			String tableLabel=bundle.getString("rtdaanim_dbtable");

			//setting the title and the message
			setTitleMessage(titleMessage);
			
			//creating the jlabels
			JLabel dbJLbl=new JLabel(dbLabel+" : ");
			dbJLbl.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel tableJLbl=new JLabel(tableLabel+" : ");
			tableJLbl.setHorizontalAlignment(SwingConstants.RIGHT);
			
			//creating the combo boxes
			dbCombo=new JComboBox();
			tableCombo=new JComboBox();
			
			//setting the layout
			GridBagLayout gridBag=new GridBagLayout();
			thePanel.setLayout(gridBag);
			GridBagConstraints c=new GridBagConstraints();
			c.fill=GridBagConstraints.BOTH;
			c.insets=new Insets(1, 1, 0, 1);
			c.anchor=GridBagConstraints.EAST;
			
			c.gridx=0;
			c.gridy=0;
			gridBag.setConstraints(dbJLbl, c);
			thePanel.add(dbJLbl);
			
			c.gridx=1;
			c.weightx=50;
			gridBag.setConstraints(dbCombo, c);
			thePanel.add(dbCombo);
			
			c.gridx=0;
			c.gridy=1;
			c.weightx=1;
			gridBag.setConstraints(tableJLbl, c);
			thePanel.add(tableJLbl);
			
			c.gridx=1;
			c.weightx=50;
			gridBag.setConstraints(tableCombo, c);
			thePanel.add(tableCombo);
			
			//creating the listener to the combo boxes
			comboListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					
					okButton.setEnabled(true);
					
					if(e.getSource().equals(dbCombo)){
						
						currentDBName="";
						
						if(dbCombo.getSelectedItem()!=null){
							
							currentDBName=((ComboListItem)dbCombo.getSelectedItem()).
								getValue().toString();
						}
						
						if(currentDBName.equals("")){
							
							setMessage(missingDBLabel, ERROR_TYPE);
							okButton.setEnabled(false);
						}
						
						refreshTableCombo();
						
					}else if(e.getSource().equals(tableCombo)){
						
						if(tableCombo.getSelectedItem()!=null){
							
							currentTableName=((ComboListItem)tableCombo.getSelectedItem()).
								getValue().toString();
						}
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
			
			//removing the listeners
			dbCombo.removeActionListener(comboListener);
			tableCombo.removeActionListener(comboListener);
			
			//clearing the map
			currentDBAndTableNames.clear();
			
			//getting the current map of the dbs and tables
			currentRootElement=DataBaseNodeToolkit.getRootDataBaseElement(
					getItem().getDocument());

			if(currentRootElement!=null){
				
				LinkedHashMap<String, Set<String>> dbsAndTablesMap=
					AnimationsToolkit.getDBsAndTables(currentRootElement);

				if(dbsAndTablesMap!=null){
					
					currentDBAndTableNames.putAll(dbsAndTablesMap);
				}
			}

			//getting the current value
			String currentValue=startValue;
			
			if(currentValue==null) {
				
				currentValue="";
			}
			
			//getting the db and table names
			String[] values=getDbAndTable(startValue);
			currentDBName=values[0];
			currentTableName=values[1];
			
			//clearing the combo boxes
			dbCombo.removeAllItems();
			tableCombo.removeAllItems();

			//creating the db combo items
			ComboListItem dbComboItem=null;
			ComboListItem selectedDBComboItem=null;
			
			if(currentDBAndTableNames.size()==0){
				
				dbCombo.setEnabled(false);
				currentDBName="";
				
			}else{
				
				dbCombo.setEnabled(true);
				
				for(String theDbName : currentDBAndTableNames.keySet()){
					
					dbComboItem=new ComboListItem(theDbName, theDbName);
					dbCombo.addItem(dbComboItem);
					
					if(theDbName.equals(currentDBName)){
						
						selectedDBComboItem=dbComboItem;
					}
				}
				
				if(selectedDBComboItem!=null){
					
					dbCombo.setSelectedItem(selectedDBComboItem);
					
				}else{
					
					dbCombo.setSelectedIndex(0);
					currentDBName=((ComboListItem)dbCombo.getItemAt(0)).getValue().toString();
				}
			}
			
			refreshTableCombo();
			
			//adding the listeners
			dbCombo.addActionListener(comboListener);
			tableCombo.addActionListener(comboListener);
			
			pack();
			showDialog(moreButton);

			return currentResult;
		}
		
		/**
		 * refreshes the table combo
		 */
		protected void refreshTableCombo(){
			
			Set<String> tableNames=currentDBAndTableNames.get(currentDBName);
			tableCombo.removeAllItems();
			
			if(tableNames==null || tableNames.size()==0){
				
				tableCombo.setEnabled(false);

				if(! currentDBAndTableNames.containsKey(currentDBName)){
					
					setMessage(missingDBLabel, ERROR_TYPE);
					
				}else{
					
					setMessage(missingTableLabel, ERROR_TYPE);
				}

				okButton.setEnabled(false);
				currentTableName="";
				
			}else{
				
				tableCombo.setEnabled(true);
				ComboListItem tableComboItem=null;
				ComboListItem selectedTableComboItem=null;
				
				for(String theTableName : tableNames){
					
					tableComboItem=new ComboListItem(theTableName, theTableName);
					tableCombo.addItem(tableComboItem);
					
					if(theTableName.equals(currentTableName)){
						
						selectedTableComboItem=tableComboItem;
					}
				}
				
				if(selectedTableComboItem!=null){
					
					tableCombo.setSelectedItem(selectedTableComboItem);
					
				}else{
					
					tableCombo.setSelectedIndex(0);
					currentTableName=((ComboListItem)tableCombo.getItemAt(0)).getValue().toString();
				}
				
				okButton.setEnabled(true);
				setMessage(infoLabel, INFORMATION_TYPE);
			}
		}

		/**
		 * @return the chosen value
		 */
		public String getValue() {
			
			String value="";
			
			if(currentDBName==null || currentDBName.equals("") || 
					currentTableName==null || currentDBName.equals("")){
				
				value="";
				
			}else{
				
				value=currentDBName+separator+currentTableName;
			}

			return value;
		}
		
		/**
		 * clears all the items in the chooser
		 *
		 */
		public void clear(){
			
			currentDBAndTableNames.clear();
			currentRootElement=null;
			dbCombo.removeAllItems();
			tableCombo.removeAllItems();
		}
		
		@Override
		public void disposeDialog() {
			
			//removing the listeners
			dbCombo.removeActionListener(comboListener);
			tableCombo.removeActionListener(comboListener);
			
			super.disposeDialog();
		}
		
	}

}
