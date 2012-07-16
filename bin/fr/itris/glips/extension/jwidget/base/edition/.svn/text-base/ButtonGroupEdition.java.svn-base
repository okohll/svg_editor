package fr.itris.glips.extension.jwidget.base.edition;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.w3c.dom.*;

import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtdaeditor.anim.components.*;
import fr.itris.glips.rtdaeditor.anim.widgets.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.rtdaeditor.widget.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;
import javax.swing.border.*;

/**
 * the abtract class of the jwidget of button container
 * @author ITRIS, Jordi SUC
 */
public abstract class ButtonGroupEdition extends JWidgetEdition{ 
	
	/**
	 * the TOGGLE_BUTTON_BAR type
	 */
	public static final int TOGGLE_BUTTON_BAR=0;
	
	/**
	 * the RADIO_BUTTON_BAR type
	 */
	public static final int RADIO_BUTTON_BAR=1;
	
	/**
	 * the main type
	 */
	protected int mainType=TOGGLE_BUTTON_BAR;
	
	/**
	 * the horizontal and vertical values
	 */
	protected static String horizontalValue="horizontal", verticalValue="vertical";
	
	/**
	 * the constructor of the class
	 * @param jwidgetManager the jwidget manager
	 * @param mainFrame the main frame
	 * @param mainType the type of the jwidget
	 * @param id the id of the jwidget type
	 * @param position the position of the toggle button of the jwidget
	 */
    public ButtonGroupEdition(JWidgetManager jwidgetManager, 
    		Frame mainFrame, int mainType, String id, int position) {

    	super(jwidgetManager, mainFrame, id, position);
    	this.mainType=mainType;
    	this.containsInnerComponents=true;

    	//filling the list of the property names and default values
    	propertiesList.add("orientation");
    	propertiesList.add("foregroundColor");
    	propertiesList.add("fontFamily");
    	propertiesList.add("fontSize");
    	propertiesList.add("bold");
    	propertiesList.add("italic");
    	propertiesList.add("isOpaque");
    	propertiesList.add("sameSizeForButtons");
    	
    	defaultValues.add(horizontalValue);
    	defaultValues.add("#000000");
    	defaultValues.add(FontFamilyChooserWidget.SANS_SERIF);
    	defaultValues.add("12");
    	defaultValues.add("false");
    	defaultValues.add("false");
    	defaultValues.add("true");
    	defaultValues.add("false");
    	
		//building the configuration panel
		buildConfigurationPanel();
		
		//building the source choosers
    	buildSourceChoosers();
    }
 
	@Override
	protected void buildConfigurationPanel() {
		
		configurationPanel=new ExtendedJWidgetConfigurationPanel();
	}
	
	/**
	 * the source chooser
	 */
	protected void buildSourceChoosers() {
		
		animationsSourceChooser=new ButtonGroupSourceChooser();
		actionsSourceChooser=new ButtonGroupSourceChooser();
	}
	
	@Override
	public Element createJWidgetElement(Element parentElement) {
		
		Element jwidgetElement=super.createJWidgetElement(parentElement);
		
		//creating a sub widget
		createNewChild(jwidgetElement);
		
		return jwidgetElement;
	}
	
	/**
	 * creates a new child
	 * @param element an element
	 */
	protected void createNewChild(Element element) {
		
		//creating the label
		String label="";
		
		try {
			label=bundle.getString("idLabel");
		}catch (Exception ex) {}

		String newLabel=getLabel(label, element);
		
		//creating the new button
		createSubWidgetElement(element, "ButtonWidget", newLabel, null, true);
	}
	
	/**
	 * creates and returns a unique label in the children given a base string
	 * @param baseString the base string for the id
	 * @param element an element
	 * @return a unique id in the children given a base string
	 */
	protected String getLabel(String baseString, Element element){
		
		if(baseString!=null){

			//creating the list of the label of the siblings
			HashSet<String> labels=new HashSet<String>();
			
			for(Node subWidget=element.getFirstChild(); subWidget!=null; subWidget=subWidget.getNextSibling()){
				
				if(subWidget instanceof Element) {
					
					labels.add(getProperty((Element)subWidget, fr.itris.glips.library.Toolkit.labelAttribute));
				}
			}
			
			//getting the new label
			int i=0;
			
			while(i<=labels.size()) {
				
				if(! labels.contains(baseString+i)) {
					
					return baseString+i;
				}
				
				i++;
			}
		}
		
		return baseString+"0";
	}
	
	/**
	 * the class of the configuration panel
	 * @author ITRIS, Jordi SUC
	 */
	protected class ExtendedJWidgetConfigurationPanel extends JWidgetConfigurationPanel {
		
		/**
		 * the text field
		 */
		protected JComboBox orientationCombo;
		
		/**
		 * the combo box listener
		 */
		protected ActionListener comboBoxListener;
		
		/**
		 * the table used to choose the buttons
		 */
		protected JTable buttonsChooserTable;
		
		/**
		 * the table model
		 */
		protected ButtonsChooserTableModel tableModel;
		
		/**
		 * the listener to the table selections
		 */
		protected ListSelectionListener listSelectionListener;
		
		/**
		 * the new, delete, up and down buttons
		 */
		protected JButton newButton, deleteButton, upButton, downButton;
		
		/**
		 * the buttons listener
		 */
		protected ActionListener buttonsListener;

		/**
		 * the font style chooser
		 */
		protected FontStyleChooser fontStyleChooser;
		
		/**
		 * the font style listener
		 */
		protected ActionListener fontStyleListener;
		
		/**
		 * the isOpaque chooser
		 */
		protected JCheckBox isOpaqueChooser;
		
		/**
		 * the listener to the isOpaque chooser 
		 */
		protected ActionListener isOpaqueChooserListener;
		
		/**
		 * the sameSizeForButtons chooser
		 */
		/*protected JCheckBox sameSizeForButtonsChooser;*/
		
		/**
		 * the listener to the sameSizeForButtons chooser 
		 */
		/*protected ActionListener sameSizeForButtonsChooserListener;*/
		
		/**
		 * the icons for the buttons
		 */
		private ImageIcon	newIcon=ResourcesManager.getIcon("New", false),
									deleteIcon=ResourcesManager.getIcon("Delete", false),
									ddeleteIcon=ResourcesManager.getIcon("Delete", true),
									upButtonIcon=ResourcesManager.getIcon("ArrowUp", false),
									dupButtonIcon=ResourcesManager.getIcon("ArrowUp", true),
									downButtonIcon=ResourcesManager.getIcon("ArrowDown", false),
									ddownButtonIcon=ResourcesManager.getIcon("ArrowDown", true);
		
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
				
				orientationCombo.removeActionListener(comboBoxListener);
				
				//getting the value of the orientation property
				String orientationValue=getProperty(getElement(), propertiesList.get(0));
				
				if(orientationValue.equals(verticalValue)) {
					
					orientationCombo.setSelectedIndex(1);
					
				}else {
					
					orientationCombo.setSelectedIndex(0);
				}
				
				orientationCombo.addActionListener(comboBoxListener);
				
				//removing the buttons listener
				newButton.removeActionListener(buttonsListener);
				deleteButton.removeActionListener(buttonsListener);
				upButton.removeActionListener(buttonsListener);
				downButton.removeActionListener(buttonsListener);
				
				//handling the table
				tableModel.setCurrentJWidgetElement(getElement());
				
				if(buttonsChooserTable.getRowCount()>0) {
					
					buttonsChooserTable.getSelectionModel().setSelectionInterval(0, 0);
				}
				
				handleButtonsState();
				
				//adding the buttons listeners
				newButton.addActionListener(buttonsListener);
				deleteButton.addActionListener(buttonsListener);
				upButton.addActionListener(buttonsListener);
				downButton.addActionListener(buttonsListener);

				//getting the array of the values for the font style chooser
				String[] values=new String[5];
				
				for(int i=0; i<values.length; i++){
					
					values[i]=getProperty(getElement(), propertiesList.get(i+1));
				}

				fontStyleChooser.removeListener(fontStyleListener);
				fontStyleChooser.init(values);
				fontStyleChooser.addListener(fontStyleListener);
				
				if(mainType==TOGGLE_BUTTON_BAR){
					
					isOpaqueChooser.removeActionListener(isOpaqueChooserListener);
					isOpaqueChooser.setSelected(Boolean.parseBoolean(
							getProperty(getElement(), propertiesList.get(6))));
					isOpaqueChooser.addActionListener(isOpaqueChooserListener);
					
					//TODO add sameSizeForButtons checkbox
				}
				
			}else {
				
				tableModel.setCurrentJWidgetElement(null);
			}
		}
		
		@Override
		public void buildPanel() {

			//getting the labels
			String orientationLabel="", horizontalLabel="", verticalLabel="", titleBorderLabel="", 
						lookLabel="";

			try {
				orientationLabel=bundle.getString("orientationLabel");
				horizontalLabel=bundle.getString("horizontalLabel");
				verticalLabel=bundle.getString("verticalLabel");
				titleBorderLabel=bundle.getString("titleBorderLabel");
				lookLabel=bundle.getString("lookLabel");
			}catch (Exception ex) {}
			
			//building the buttons chooser panel
			JPanel buttonsChooserPanel=new JPanel();
			TitledBorder border=new TitledBorder(titleBorderLabel);
			buttonsChooserPanel.setBorder(border);

			//creating the table
			buttonsChooserTable=new JTable(){
				
				@Override
			    protected void configureEnclosingScrollPane() {
					
			        Container p=getParent();
			        
			        if(p instanceof JViewport) {
			        	
			            Container gp=p.getParent();
			            
			            if(gp instanceof JScrollPane) {
			            	
			                JScrollPane scrollPane=(JScrollPane)gp;
			                JViewport viewport=scrollPane.getViewport();
			                
			                if (viewport==null || viewport.getView()!=this) {
			                	
			                    return;
			                }

			                scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
			            }
			        }
			    }
			};
			
			//creating the model
			tableModel=new ButtonsChooserTableModel(buttonsChooserTable);
			buttonsChooserTable.setModel(tableModel);
			buttonsChooserTable.setAutoCreateColumnsFromModel(true);
			
			//creating the table cell editor
			ButtonChooserTableCellEditor tableEditor=new ButtonChooserTableCellEditor();
			buttonsChooserTable.setCellEditor(tableEditor);
			
			//setting the properties of the table
			buttonsChooserTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			buttonsChooserTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			buttonsChooserTable.setIntercellSpacing(new Dimension(1, 1));

			buttonsChooserTable.setShowGrid(false);
			buttonsChooserTable.setOpaque(true);

			JScrollPane scrollpane=new JScrollPane(buttonsChooserTable);
			scrollpane.getViewport().setBackground(Color.white);
			scrollpane.setPreferredSize(new Dimension(150, 220));
			
			//creating the widget to create, delete, move the columns//
			//the buttons panel
			newButton=new JButton(newIcon); 
			deleteButton=new JButton(deleteIcon);
			deleteButton.setDisabledIcon(ddeleteIcon);
			upButton=new JButton(upButtonIcon); 
			upButton.setDisabledIcon(dupButtonIcon);
			downButton=new JButton(downButtonIcon);
			downButton.setDisabledIcon(ddownButtonIcon);
			
			//getting the labels for the tool tip
			String newLabel="", deleteLabel="";
			
			try {
				newLabel=ResourcesManager.bundle.getString("labelnew");
				deleteLabel=ResourcesManager.bundle.getString("labeldelete");
			}catch (Exception ex) {}
			
			Insets insets=new Insets(1, 1, 1, 1);
			
			//setting the properties of the buttons
			newButton.setMargin(insets);
			newButton.setToolTipText(newLabel);
			deleteButton.setMargin(insets);
			deleteButton.setToolTipText(deleteLabel);
			upButton.setMargin(insets);
			downButton.setMargin(insets);
			
			//building the buttons panel
			JPanel buttons=new JPanel();
			buttons.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 0));
			buttons.add(newButton);
			buttons.add(deleteButton);
			buttons.add(upButton);
			buttons.add(downButton);
			
			deleteButton.setEnabled(false);
			upButton.setEnabled(false);
			downButton.setEnabled(false);
			
			buttonsListener=new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					
					if(evt.getSource().equals(newButton)) {
						
						tableModel.addButton();
						
					}else if(evt.getSource().equals(deleteButton)) {
						
						tableModel.removeButton(buttonsChooserTable.getSelectedRow());
						
					}else if(evt.getSource().equals(upButton)) {
						
						tableModel.putUp();
						
					}else if(evt.getSource().equals(downButton)) {
						
						tableModel.putDown();
					}
					
					refreshSourceChoosers();
				}
			};
			
			newButton.addActionListener(buttonsListener);
			deleteButton.addActionListener(buttonsListener);
			upButton.addActionListener(buttonsListener);
			downButton.addActionListener(buttonsListener);
			
			//adding a listener to the table selections
			buttonsChooserTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent evt) {
					
					handleButtonsState();
				}
			});
			
			//building the component
			buttonsChooserPanel.setLayout(new BorderLayout(0, 2));
			buttonsChooserPanel.add(scrollpane, BorderLayout.CENTER);
			buttonsChooserPanel.add(buttons, BorderLayout.SOUTH);
			
			//creating and filling the display panel
			JPanel lookPanel=new JPanel();
			TitledBorder lookBorder=new TitledBorder(lookLabel);
			lookPanel.setBorder(lookBorder);
			
			//handling the orientation combo box
			JLabel orientationLbl=new JLabel(orientationLabel+" : ");
			orientationCombo=new JComboBox();
			
			//filling the orientation combo
			orientationCombo.addItem(new ComboListItem(horizontalValue, horizontalLabel));
			orientationCombo.addItem(new ComboListItem(verticalValue, verticalLabel));
			
			//adding the listener to the combo box
			comboBoxListener=new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					
					//getting the selected item for the combo
					ComboListItem item=(ComboListItem)orientationCombo.getSelectedItem();
					
					if(item!=null) {
						
						setProperty(getElement(), propertiesList.get(0), item.getValue().toString(), true);
					}
				}
			};

			if(mainType==TOGGLE_BUTTON_BAR){

				//creating the isOpaque checkbox
				isOpaqueChooser=new JCheckBox(bundle.getString("isOpaqueLabel"));
				
				isOpaqueChooserListener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {

						setProperty(getElement(), propertiesList.get(6), 
								Boolean.toString(isOpaqueChooser.isSelected()), true);
					}
				};
				
				//creating the sameSizeForButtons checkbox
				/*sameSizeForButtonsChooser=new JCheckBox(bundle.getString("sameSizeForButtonsLabel"));
				
				sameSizeForButtonsChooserListener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {

						setProperty(getElement(), propertiesList.get(7), 
								Boolean.toString(sameSizeForButtonsChooser.isSelected()), true);
					}
				};*/
			}
			
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
			
			//filling the panel
			GridBagLayout gridBag1=new GridBagLayout();
			lookPanel.setLayout(gridBag1);
			GridBagConstraints c1=new GridBagConstraints();
			c1.fill=GridBagConstraints.HORIZONTAL;
			c1.insets=new Insets(1, 1, 1, 1);
			c1.anchor=GridBagConstraints.WEST;
			
			c1.gridwidth=1;
			gridBag1.setConstraints(orientationLbl, c1);
			lookPanel.add(orientationLbl);
			
			c1.gridwidth=GridBagConstraints.REMAINDER;
			c1.weightx=50;
			gridBag1.setConstraints(orientationCombo, c1);
			lookPanel.add(orientationCombo);
			
			if(mainType==TOGGLE_BUTTON_BAR){

				c1.weightx=0;
				gridBag1.setConstraints(isOpaqueChooser, c1);
				lookPanel.add(isOpaqueChooser);
				
				/*gridBag1.setConstraints(sameSizeForButtonsChooser, c1);
				lookPanel.add(sameSizeForButtonsChooser);*/
			}
			
			c1.weightx=50;
			gridBag1.setConstraints(fontStyleChooser, c1);
			lookPanel.add(fontStyleChooser);
			
			//creating and filling the panel containing all the widgets
			JPanel allPanel=new JPanel();
			allPanel.setBorder(new EmptyBorder(7, 7, 20, 7));
			
			GridBagLayout gridBag=new GridBagLayout();
			allPanel.setLayout(gridBag);
			GridBagConstraints c=new GridBagConstraints();
			c.fill=GridBagConstraints.BOTH;
			c.insets=new Insets(0, 0, 0, 0);
			
			c.gridx=0;
			c.gridy=0;
			c.weightx=2;
			c.gridheight=3;
			gridBag.setConstraints(buttonsChooserPanel, c);
			allPanel.add(buttonsChooserPanel);
			
			c.gridx=1;
			c.gridheight=1;
			c.weightx=0;
			gridBag.setConstraints(lookPanel, c);
			allPanel.add(lookPanel);
			
			JPanel emptyPanel=new JPanel();
			c.gridy=1;
			c.gridheight=2;
			c.weighty=50;
			gridBag.setConstraints(emptyPanel, c);
			allPanel.add(emptyPanel);
			
			emptyPanel=new JPanel();
			c.gridx=2;
			c.gridy=0;
			c.gridheight=1;
			c.weighty=0;
			c.weightx=50;
			gridBag.setConstraints(emptyPanel, c);
			allPanel.add(emptyPanel);

			SpringLayout layout=new SpringLayout();
			setLayout(layout);
			layout.putConstraint(SpringLayout.NORTH, this, 0, SpringLayout.NORTH, allPanel);
			layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, allPanel);
			add(allPanel);
		}
		
		/**
		 * handles the buttons' state
		 */
		protected void handleButtonsState() {
			
			//disabling the buttons
			 deleteButton.setEnabled(false);
			 upButton.setEnabled(false);
			 downButton.setEnabled(false);

			 //getting the selected row index
			 int row=buttonsChooserTable.getSelectedRow();
			 
			 if(row>=0 && row<tableModel.getRowCount()) {
				 
				 //handling the state of the buttons
				 if(tableModel.getRowCount()>1){
					 
					 deleteButton.setEnabled(true);
				 }
				 
				 if(row>0) {
					 
					 upButton.setEnabled(true);
				 }
				 
				 if(row<tableModel.getRowCount()-1) {
					 
					 downButton.setEnabled(true);
				 }
			 }
		}
		
		/**
		 * sets the new label for the given sub widget element
		 * @param subJWidgetElement a sub widget element
		 * @param newLabel the new label to set
		 */
		protected void setButtonLabel(Element subJWidgetElement, String newLabel) {
			
			if(subJWidgetElement!=null) {
				
				if(newLabel==null || newLabel.equals("")) {
					
					showMalformedIdDialog();
					
				}else {
					
					setProperty(subJWidgetElement, 
						fr.itris.glips.library.Toolkit.labelAttribute, newLabel, true);
					refreshSourceChoosers();
				}
			}
		}
	    
	    /**
	     * shows a dialog notifying that the id is malformed
	     */
	    protected void showMalformedIdDialog(){
	    	
	        //getting the labels
	        ResourceBundle editorBundle=ResourcesManager.bundle;
        	String malformedIdLabel="", errorLabel="";
	        
	        if(editorBundle!=null){
	           
	            try{
	                errorLabel=editorBundle.getString("labelerror");
	            	malformedIdLabel=editorBundle.getString("rtdaanim_malformedId");
	            }catch (Exception ex){}
	        }
	        
	        JOptionPane.showMessageDialog(Editor.getParent(), 
																malformedIdLabel,
																errorLabel,
																JOptionPane.ERROR_MESSAGE);
	    }
	    
		/**
		 * the class of the model of the table of the buttons chooser
		 * @author ITRIS, Jordi SUC
		 */
		protected class ButtonsChooserTableModel extends AbstractTableModel{
			
			/**
			 * the table
			 */
			protected JTable table=null;
			
			/**
			 * the current jwidget element
			 */
			protected Element tableJwidgetElement=null;
			
			/**
			 * the list of the elements of the button 
			 */
			protected LinkedList<Element> buttonJwidgetElements=new LinkedList<Element>();
			
			/**
			 * the constructor of the class
			 * @param table the table
			 */
			protected ButtonsChooserTableModel(JTable table) {
				
				this.table=table;
			}
			
			/**
			 * setting the current jwidget element
			 * @param element an element
			 */
			protected void setCurrentJWidgetElement(Element element) {
				
				buttonJwidgetElements.clear();
				
				if(element!=null) {
					
					this.tableJwidgetElement=element;
					
					//creating the list of the jwidget button elements
					for(Node node=element.getFirstChild(); node!=null; node=node.getNextSibling()) {
						
						if(node instanceof Element) {
							
							buttonJwidgetElements.add((Element)node);
						}
					}
					
					fireTableStructureChanged();
					table.getSelectionModel().setSelectionInterval(0, 0);
					
				}else {
					
					tableJwidgetElement=null;
					fireTableStructureChanged();
				}
			}
			
			/**
			 * adds a new button at the end of the list
			 */
			protected void addButton() {
				
				//create a sub widget
				createNewChild(tableJwidgetElement);
				
				//adding the element to the list of the jwigdet buttons
				setCurrentJWidgetElement(tableJwidgetElement);
				table.getSelectionModel().setSelectionInterval(buttonJwidgetElements.size()-1, buttonJwidgetElements.size()-1);
			}
			
			/**
			 * removes a button at the given position
			 * @param pos a position
			 */
			protected void removeButton(int pos) {
				
				if(pos>=0 && pos<=buttonJwidgetElements.size()) {
					
					removeSubWidgetElement(tableJwidgetElement, buttonJwidgetElements.get(pos), true);
					buttonJwidgetElements.remove(pos);
					fireTableStructureChanged();
					
					int selectionPos=pos;
					
					//computing the new index to be selected
					if(pos>=buttonJwidgetElements.size()) {
						
						selectionPos=buttonJwidgetElements.size()-1;
					}
					
					setCurrentJWidgetElement(tableJwidgetElement);
					table.getSelectionModel().setSelectionInterval(selectionPos, selectionPos);
				}
			}
			
			/**
			 * puts this tree node at a upper place
			 */
			protected void putUp() {
				
				//getting the currently selected node
				int row=table.getSelectedRow();
				
				if(row>0 && row<buttonJwidgetElements.size()) {
					
					ButtonGroupEdition.this.putUp(buttonJwidgetElements.get(row), true);
					setCurrentJWidgetElement(tableJwidgetElement);
					table.getSelectionModel().setSelectionInterval(row-1, row-1);
				}
			}
			
			/**
			 * puts this tree node at a lower place
			 */
			protected void putDown() {
				
				//getting the currently selected node
				int row=table.getSelectedRow();
				
				if(row>=0 && row<buttonJwidgetElements.size()) {
					
					ButtonGroupEdition.this.putDown(buttonJwidgetElements.get(row), true);
					setCurrentJWidgetElement(tableJwidgetElement);
					table.getSelectionModel().setSelectionInterval(row+1, row+1);
				}
			}

			/**
			 * @see javax.swing.table.TableModel#getColumnCount()
			 */
			public int getColumnCount() {
				
				return 1;
			}
			
			/**
			 * @see javax.swing.table.TableModel#getRowCount()
			 */
			public int getRowCount() {

				return buttonJwidgetElements.size();
			}
			
			/**
			 * @see javax.swing.table.TableModel#getValueAt(int, int)
			 */
			public Object getValueAt(int row, int col) {
				
				if(col==0 && tableJwidgetElement!=null && row>=0 && 
					row<buttonJwidgetElements.size()) {

					return buttonJwidgetElements.get(row).getAttribute(
							fr.itris.glips.library.Toolkit.labelAttribute);
				}
				
				return null;
			}

			@Override
			public String getColumnName(int col) {
				
				return "";
			}

			@Override
			public void setValueAt(Object value, int row, int col) {

				if(col==0 && tableJwidgetElement!=null && row>=0 && row<buttonJwidgetElements.size()) {

					setButtonLabel(buttonJwidgetElements.get(row), value.toString());
				}
			}

			@Override
			public boolean isCellEditable(int row, int col) {
			
				return true;
			}
		}
		
		/**
		 * the class of the table cell editor
		 * @author ITRIS, Jordi SUC
		 */
		protected class ButtonChooserTableCellEditor extends AbstractCellEditor implements TableCellEditor{
			
			/**
			 * the editor value
			 */
			protected Object editorValue=null;
			
			/**
			 * the text field
			 */
			protected JTextField textField=new JTextField();
			
			/**
			 * the text field listener
			 */
			protected CaretListener textFieldListener;
			
			/**
			 * the focus listener
			 */
			protected FocusAdapter focusAdapter;
			
			/**
			 * the constructor of the class
			 */
			public ButtonChooserTableCellEditor() {

				textFieldListener=new CaretListener() {

					public void caretUpdate(CaretEvent evt) {

						editorValue=textField.getText();
					}
				};
				
				textField.addCaretListener(textFieldListener);
				
				focusAdapter=new FocusAdapter() {

					@Override
					public void focusLost(FocusEvent evt) {

						fireEditingStopped();
					}
				};
				
				textField.addFocusListener(focusAdapter);
			}
			
			/**
			 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
			 */
			public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row, int col) {

				if(value==null) {
					
					value="";
				}
				
				textField.setText(value.toString());
				
				return textField;
			}
			
			/**
			 * @see javax.swing.CellEditor#getCellEditorValue()
			 */
			public Object getCellEditorValue() {

				return editorValue;
			}
		}
	}
	
	/**
	 * the class of the source chooser for the button group jwidget
	 * @author ITRIS, Jordi SUC
	 */
	protected class ButtonGroupSourceChooser extends AnimationChooserJWidgetSourceChooser{
		
		/**
		 * the combo box allowing to choose a child id
		 */
		protected JComboBox combo;
		
		/**
		 * the combo listener
		 */
		protected ActionListener comboListener;
		
		/**
		 * the constructor of the class
		 */
		protected ButtonGroupSourceChooser() {
			
			buildWidget();
		}

		@Override
		protected void buildWidget() {

			//the label for the source chooser
			String label="";
			
			try {
				label=bundle.getString("sourceLabel");
			}catch (Exception ex) {}
			
			JLabel sourceLbl=new JLabel(label);
			
			//handling the combo box
			combo=new JComboBox();
			setBorder(new EmptyBorder(2, 2, 2, 2));
			JPanel comboPanel=new JPanel();
			comboPanel.setBorder(new EmptyBorder(3, 10, 0, 0));
			comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
			comboPanel.add(combo);
			
			//building the component
			setLayout(new BorderLayout());
			add(sourceLbl, BorderLayout.NORTH);
			add(comboPanel, BorderLayout.CENTER);
			
			//adding the listener to the combo
			comboListener=new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					
					//getting the currently selected combo item
					ComboListItem item=(ComboListItem)combo.getSelectedItem();
					
					if(item!=null) {
						
						//setting the new source to edit
						setSource(item.getValue().toString());
					}
				}
			};
		}

		@Override
		protected void updateWidgets() {
			
			if(getJwidgetElement()!=null) {
				
				combo.removeActionListener(comboListener);
				
				//removing all the combo items
				combo.removeAllItems();
				
				//creating the new combo items
				String value="", label="";
				ComboListItem item=null, selectedItem=null;
				String currentSource=RtdaAnimationsAndActionsModule.getStateRecord().getSourceName();

				for(Node node=getJwidgetElement().getFirstChild(); node!=null; node=node.getNextSibling()) {
					
					if(node instanceof Element) {
						
						value=((Element)node).getAttribute(fr.itris.glips.library.Toolkit.idAttribute);

						if(value!=null && ! value.equals("")) {
							
							label=((Element)node).getAttribute(
								fr.itris.glips.library.Toolkit.labelAttribute);
							
							//creating the combo item and adding it to the combo box
							item=new ComboListItem(value, label);
							combo.addItem(item);
							
							if(item.getValue().equals(currentSource)) {
								
								selectedItem=item;
							}
						}
					}
				}
				
				//setting the new source for the chooser
				if(currentSource.equals("")) {
					
					if(combo.getItemCount()>0) {
						
						setSource(((ComboListItem)combo.getItemAt(0)).getValue().toString());
						
						//selecting the default item
						combo.setSelectedIndex(0);
					}

				}else{

					//setting the former source and selected item
					setSource(currentSource);

					if(selectedItem!=null) {
						
						combo.setSelectedItem(selectedItem);
					}
				}

				combo.addActionListener(comboListener);
			}
		}
		
		@Override
		public void clean() {
			
			super.clean();
			combo.removeAllItems();
		}
	}
}
