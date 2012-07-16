package fr.itris.glips.extension.jwidget.table.edition;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtdaeditor.anim.components.*;
import fr.itris.glips.rtdaeditor.anim.widgets.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.rtdaeditor.widget.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.widgets.ColorChooserWidget;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;
import org.w3c.dom.*;

/**
 * the class of the widget of a table 
 * @author ITRIS, Jordi SUC
 */
public class TableEdition extends JWidgetEdition{
	
	/**
	 * the column separator
	 */
	public static String columnNamesSeparator="/**/";
	
	/**
	 * the regex column separators
	 */
	public static String regexColumnNamesSeparator="/[*][*]/";
	
	/**
	 * the initial source name
	 */
	public static String initialSourceName="0 0";
	
	/**
	 * the constructor of the class
	 * @param jwidgetManager the jwidget manager
	 * @param mainFrame the main frame
	 */
	public TableEdition(JWidgetManager jwidgetManager, Frame mainFrame) {
		
		super(jwidgetManager, mainFrame, "TableWidget", 9);
		this.containsInnerComponents=true;
		
		//filling the list of the property names and default values
		propertiesList.add("rowCount");
		propertiesList.add("colNames");
		propertiesList.add("showHorizontalLines");
		propertiesList.add("showVerticalLines");
    	propertiesList.add("backgroundColor");
    	propertiesList.add("foregroundColor");//5
    	propertiesList.add("fontFamily");
    	propertiesList.add("fontSize");
    	propertiesList.add("bold");
    	propertiesList.add("italic");
    	propertiesList.add("rowHeight");
    	
		defaultValues.add("1");
		defaultValues.add(" "+columnNamesSeparator);
		defaultValues.add(Boolean.toString(true));
		defaultValues.add(Boolean.toString(true));
		defaultValues.add("#ffffff");
    	defaultValues.add("#000000");
    	defaultValues.add(FontFamilyChooserWidget.SANS_SERIF);
    	defaultValues.add("12");
    	defaultValues.add("false");
    	defaultValues.add("false");
    	defaultValues.add("16");
		
		//building the configuration panel
		buildConfigurationPanel();
		
		//building the source choosers
    	buildSourceChoosers();
	}
	
	@Override
    protected BufferedImage createImage(Element jwidgetElement, Dimension size) {
		
		//getting each property of the table
		int rowCount=Integer.parseInt(defaultValues.get(0));
		
		try {
			rowCount=Integer.parseInt(getProperty(jwidgetElement, propertiesList.get(0)));
		}catch (Exception ex) {}
		
		final int frowCount=rowCount;
		
		//getting the name of the columns
		String[] columnNames=null;
		String[] splitColNames=jwidgetElement.getAttribute("colNames").split(regexColumnNamesSeparator);
		
		if(splitColNames!=null && splitColNames.length!=0){
			
			columnNames=new String[splitColNames.length];
			
			for(int i=0; i<splitColNames.length; i++){
				
				columnNames[i]=splitColNames[i];
			}
			
		}else{
			
			columnNames=new String[0];
		}
		
		final String[] fcolumnNames=columnNames;
		
		//whether the horizontal or vertical lines should be displayed
		boolean showHLines=Boolean.parseBoolean(jwidgetElement.getAttribute("showHorizontalLines"));
		boolean showVLines=Boolean.parseBoolean(jwidgetElement.getAttribute("showVerticalLines"));
		
		//creating the image
		final BufferedImage image=new BufferedImage(
				size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		
		//creating the table that will be represented
		JTable table=new JTable();
		
		//setting the properties of the table
		table.setShowGrid(false);
		table.setShowHorizontalLines(showHLines);
		table.setShowVerticalLines(showVLines);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		//handles the look of the table
		JWidgetToolkit.handleLook(jwidgetElement, table);
		JWidgetToolkit.handleLook(jwidgetElement, table.getTableHeader());
    	JWidgetToolkit.handleBackgroundAndBorderLook(jwidgetElement, table);
    	JWidgetToolkit.handleRowHeight(jwidgetElement, table);
		
		//the table model
		DefaultTableModel model=new DefaultTableModel() {
			
			@Override
			public String getColumnName(int col) {
				
				String columnName=" ";
				
				if(col<fcolumnNames.length) {
					
					columnName=fcolumnNames[col];
				}
				
				return columnName;
			}
			
			@Override
			public int getColumnCount() {
				
				int colCount=fcolumnNames.length;
				
				return colCount;
			}
			
			@Override
			public int getRowCount() {
				
				return frowCount;
			}
			
			@Override
			public Object getValueAt(int row, int col) {
				return " ";
			}
		};
		
		table.setAutoCreateColumnsFromModel(true);
		table.setModel(model);
		
		final JScrollPane scrollpane=new JScrollPane(table);
		scrollpane.setPreferredSize(size);
		
		JFrame frame=new JFrame();
		frame.getContentPane().setLayout(
				new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.getContentPane().add(scrollpane);
		frame.pack();
		JWidgetToolkit.resizeColumns(table);
		scrollpane.validate();
		scrollpane.print(image.getGraphics());

		return image;
	}
	
	@Override
	protected void buildConfigurationPanel() {
		
		configurationPanel=new ExtendedJWidgetConfigurationPanel();
	}
	
	/**
	 * the source chooser
	 */
	protected void buildSourceChoosers() {
		
		animationsSourceChooser=new TableAnimationChooserJWidgetSourceChooser();
		actionsSourceChooser=new TableAnimationChooserJWidgetSourceChooser();
	}
	
	/**
	 * the class of the configuration panel
	 * @author ITRIS, Jordi SUC
	 */
	protected class ExtendedJWidgetConfigurationPanel extends JWidgetConfigurationPanel{
		
		/**
		 * the spinners
		 */
		protected JSpinner rowsSpinner;
		
		/**
		 * the spinner models
		 */
		protected SpinnerNumberModel rowsSpinnerModel;
		
		/**
		 * the change listener
		 */
		protected ChangeListener changeListener;
		
		/**
		 * the column table widget
		 */
		protected ColumnTable columnTable;
		
		/**
		 * the checkboxes
		 */
		protected JCheckBox horizontalLines, verticalLines;
		
		/**
		 * the listener to the check boxes
		 */
		protected ActionListener checkBoxListener;
		
		/**
		 * the background color chooser
		 */
		protected ColorChooserWidget backgroundColorChooser;
		
		/**
		 * the listener to the background color chooser
		 */
		protected ActionListener backgroundColorChooserListener;
		
		/**
		 * the row height chooser
		 */
		protected IntegerSpinnerWidget rowHeightChooser;
		
		/**
		 * the row height chooser listener
		 */
		protected ActionListener rowHeightChooserListener;
		
		/**
		 * the font style chooser
		 */
		protected FontStyleChooser fontStyleChooser;
		
		/**
		 * the font style listener
		 */
		protected ActionListener fontStyleListener;

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
				
				rowsSpinner.removeChangeListener(changeListener);
				horizontalLines.removeActionListener(checkBoxListener);
				verticalLines.removeActionListener(checkBoxListener);
				
				//setting the new value for the spinner
				try{
					rowsSpinner.setValue(Integer.parseInt(getProperty(getElement(), propertiesList.get(0))));
				}catch(Exception ex) {rowsSpinner.setValue(1);}
				
				//setting the new value for the table
				columnTable.initializeValue(getProperty(getElement(), propertiesList.get(1)));
				
				//setting the new value for the check boxes
				try{
					horizontalLines.setSelected(Boolean.parseBoolean(getProperty(getElement(), propertiesList.get(2))));
				}catch(Exception ex) {}
				
				try{
					verticalLines.setSelected(Boolean.parseBoolean(getProperty(getElement(), propertiesList.get(3))));
				}catch(Exception ex) {}

				rowsSpinner.addChangeListener(changeListener);
				horizontalLines.addActionListener(checkBoxListener);
				verticalLines.addActionListener(checkBoxListener);
				
				backgroundColorChooser.removeListener(backgroundColorChooserListener);
				backgroundColorChooser.init(getProperty(getElement(), propertiesList.get(4)));
				backgroundColorChooser.addListener(backgroundColorChooserListener);
				
				rowHeightChooser.removeListener(rowHeightChooserListener);
				rowHeightChooser.init(getProperty(getElement(), propertiesList.get(10)));
				rowHeightChooser.addListener(rowHeightChooserListener);
				
				//getting the array of the values for the font style chooser
				String[] values=new String[5];
				
				for(int i=0; i<values.length; i++){
					
					values[i]=getProperty(getElement(), propertiesList.get(i+5));
				}

				fontStyleChooser.removeListener(fontStyleListener);
				fontStyleChooser.init(values);
				fontStyleChooser.addListener(fontStyleListener);
			}
		}
		
		@Override
		public void buildPanel() {
			
			String tableSizeLabel=bundle.getString("tableSizeLabel");
			String rowsLabel=bundle.getString("rowCountLabel");
			String colsLabel=bundle.getString("colCountLabel");
			String gridLabel=bundle.getString("gridLabel");
			String horizontalLinesLabel=bundle.getString("showHorizontalLinesLabel");
			String verticalLinesLabel=bundle.getString("showVerticalLinesLabel");
			String lookLabel=bundle.getString("lookLabel");
			String colorLabel=bundle.getString("colorLabel");
			String rowHeightLabel=bundle.getString("rowHeightLabel");
			
			JLabel rowsLbl=new JLabel(rowsLabel+" : ");
			rowsLbl.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel colsLbl=new JLabel(colsLabel+" : ");
			colsLbl.setHorizontalAlignment(SwingConstants.RIGHT);
			
			//creating the spinner
			rowsSpinnerModel=new SpinnerNumberModel(1, 1, Integer.MAX_VALUE-1, 1);
			rowsSpinner=new JSpinner(rowsSpinnerModel); 
			
			//setting the listener to the changes of the properties
			changeListener=new ChangeListener() {
				
				public void stateChanged(ChangeEvent evt) {
					
					if(evt.getSource().equals(rowsSpinner)) {
						
						setProperty(getElement(), propertiesList.get(0), rowsSpinner.getValue().toString(), true);
						
						//refreshes the source choosers
						refreshSourceChoosers();
					}
				}
			};
			
			rowsSpinner.addChangeListener(changeListener);
			
			//creating the column table widget
			columnTable=new ColumnTable();
			
			//creating the check boxes
			horizontalLines=new JCheckBox(horizontalLinesLabel);
			verticalLines=new JCheckBox(verticalLinesLabel);
			
			//creating the listener to the check boxes
			checkBoxListener=new ActionListener() {
				
				public void actionPerformed(ActionEvent evt) {
					
					if(evt.getSource().equals(horizontalLines)) {
						
						setProperty(getElement(), propertiesList.get(2), 
								Boolean.toString(horizontalLines.isSelected()), true);
						
					}else if(evt.getSource().equals(verticalLines)) {
						
						setProperty(getElement(), propertiesList.get(3), 
								Boolean.toString(verticalLines.isSelected()), true);
					}
				}
			};
			
			//the size table panel
			JPanel tableSizePanel=new JPanel();
			TitledBorder border=new TitledBorder(tableSizeLabel);
			tableSizePanel.setBorder(border);
			
			GridBagLayout gridBag0=new GridBagLayout();
			tableSizePanel.setLayout(gridBag0);
			GridBagConstraints c0=new GridBagConstraints();
			c0.fill=GridBagConstraints.HORIZONTAL;
			c0.insets=new Insets(1, 0, 1, 0);
			
			c0.gridwidth=1;
			c0.anchor=GridBagConstraints.EAST;
			gridBag0.setConstraints(rowsLbl, c0);
			tableSizePanel.add(rowsLbl);
			
			c0.gridwidth=GridBagConstraints.REMAINDER;
			c0.anchor=GridBagConstraints.WEST;
			c0.weightx=50;
			gridBag0.setConstraints(rowsSpinner, c0);
			tableSizePanel.add(rowsSpinner);
			
			c0.gridwidth=1;
			c0.anchor=GridBagConstraints.NORTHEAST;
			c0.weightx=1;
			gridBag0.setConstraints(colsLbl, c0);
			tableSizePanel.add(colsLbl);
			
			JPanel emptyPanel=new JPanel();
			c0.insets=new Insets(2, 0, 2, 0);
			c0.gridwidth=GridBagConstraints.REMAINDER;
			c0.anchor=GridBagConstraints.WEST;
			c0.weightx=50;
			gridBag0.setConstraints(emptyPanel, c0);
			tableSizePanel.add(emptyPanel);
			
			c0.gridwidth=GridBagConstraints.REMAINDER;
			c0.anchor=GridBagConstraints.WEST;
			c0.weightx=50;
			gridBag0.setConstraints(columnTable, c0);
			tableSizePanel.add(columnTable);
			
			//creating and filling the grid panel
			JPanel gridPanel=new JPanel();
			border=new TitledBorder(gridLabel);
			gridPanel.setBorder(border);
			gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
			gridPanel.add(horizontalLines);
			horizontalLines.setBorder(new EmptyBorder(1, 15, 1, 0));
			gridPanel.add(verticalLines);
			verticalLines.setBorder(new EmptyBorder(1, 15, 1, 0));
			
			//creating and filling the display panel
			JPanel lookPanel=new JPanel();
			TitledBorder lookBorder=new TitledBorder(lookLabel);
			lookPanel.setBorder(lookBorder);

			//creating the color chooser and its label
			JLabel colorChooserLbl=new JLabel(colorLabel+" : ");
			backgroundColorChooser=new ColorChooserWidget();
			
			backgroundColorChooserListener=new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
				
					setProperty(getElement(), propertiesList.get(4), backgroundColorChooser.getValue(), true);
				}
			};
			
			//creating the row height chooser
			JLabel rowHeightLbl=new JLabel(rowHeightLabel);
			rowHeightChooser=new IntegerSpinnerWidget(16, 1, 1000, 1);
			
			rowHeightChooserListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					
					setProperty(getElement(), propertiesList.get(10), 
							rowHeightChooser.getWidgetValue()+"", true);
				}
			};

			//creating the font style chooser
			fontStyleChooser=new FontStyleChooser();
			
			//creating the font style listener
			fontStyleListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					
					//getting the array of the values
					String[] newValues=fontStyleChooser.getValues();
					
					//setting all the properties
					for(int i=0; i<newValues.length; i++){
						
						setProperty(getElement(), propertiesList.get(i+5), newValues[i], true);
					}
				}
			};
			
			//filling the panel
			GridBagLayout gridBag1=new GridBagLayout();
			lookPanel.setLayout(gridBag1);
			GridBagConstraints c1=new GridBagConstraints();
			c1.insets=new Insets(1, 0, 1, 0);
			c1.fill=GridBagConstraints.HORIZONTAL;
			
			c1.gridx=0;
			c1.gridy=0;
			gridBag1.setConstraints(colorChooserLbl, c1);
			lookPanel.add(colorChooserLbl);
			
			c1.gridx=1;
			gridBag1.setConstraints(backgroundColorChooser, c1);
			lookPanel.add(backgroundColorChooser);
			
			//TODO
			c1.gridx=0;
			c1.gridy=1;
			gridBag1.setConstraints(rowHeightLbl, c1);
			lookPanel.add(rowHeightLbl);
			
			c1.gridx=1;
			gridBag1.setConstraints(rowHeightChooser, c1);
			lookPanel.add(rowHeightChooser);
			
			c1.gridx=0;
			c1.gridy=2;
			c1.gridwidth=2;
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
			
			c.gridx=0;
			c.gridy=0;
			c.gridheight=4;
			c.weightx=50;
			gridBag.setConstraints(tableSizePanel, c);
			allPanel.add(tableSizePanel);
			
			c.gridx=1;
			c.gridheight=2;
			c.weightx=0;
			gridBag.setConstraints(lookPanel, c);
			allPanel.add(lookPanel);
			
			c.gridx=1;
			c.gridy=2;
			c.gridheight=1;
			gridBag.setConstraints(gridPanel, c);
			allPanel.add(gridPanel);

			SpringLayout layout=new SpringLayout();
			setLayout(layout);
			layout.putConstraint(SpringLayout.NORTH, this, 0, SpringLayout.NORTH, allPanel);
			layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, allPanel);
			add(allPanel);
		}
		
		/**
		 * the class of the table used to selected the name of each column
		 * @author ITRIS, Jordi SUC
		 */
		protected class ColumnTable extends JComponent{
			
			/**
			 * the table used to select the name of each column
			 */
			protected JTable table=null;
			
			/**
			 * the table model
			 */
			protected JWidgetTableModel tableModel=null;
			
			/**
			 * the new, delete, up and down buttons
			 */
			protected JButton 	newButton, deleteButton, upButton, downButton;
			
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
			protected ColumnTable() {
				
				buildWidget();
			}
			
			/**
			 * builds the widget
			 */
			protected void buildWidget() {
				
				//creating the table
				table=new JTable();
				
				//creating the model
				tableModel=new JWidgetTableModel();
				table.setModel(tableModel);
				table.setAutoCreateColumnsFromModel(true);
				
				//setting the properties of the table
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				table.setIntercellSpacing(new Dimension(1, 1));
				table.getTableHeader().setReorderingAllowed(false);
				table.setRowHeight(18);
				table.setAlignmentX(SwingConstants.CENTER);
				
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
				buttons.setBorder(new EmptyBorder(1, 0, 1, 0));
				
				deleteButton.setEnabled(false);
				upButton.setEnabled(false);
				downButton.setEnabled(false);
				
				//adding the listeners to the buttons
				newButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent evt) {
						
						tableModel.addColumn();
					}
				});
				
				deleteButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent evt) {
						
						int selectedRow=table.getSelectedRow();
						tableModel.removeColumn(selectedRow);
						table.getSelectionModel().setSelectionInterval(selectedRow-1, selectedRow-1);
					}
				});
				
				upButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent evt) {
						
						int selectedRow=table.getSelectedRow();
						tableModel.putColumn(selectedRow, selectedRow-1);
						table.getSelectionModel().setSelectionInterval(selectedRow-1, selectedRow-1);
					}
				});
				
				downButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent evt) {
						
						int selectedRow=table.getSelectedRow();
						tableModel.putColumn(selectedRow, selectedRow+1);
					}
				});
				
				//adding a listener to the table selections
				table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
					
					public void valueChanged(ListSelectionEvent evt) {
						
						handleButtonsState();
					}
				});
				
				//building the component
				setLayout(new BorderLayout());
				JScrollPane scrollpane=new JScrollPane(table);
				scrollpane.setPreferredSize(new Dimension(100, 175));
				add(scrollpane, BorderLayout.CENTER);
				add(buttons, BorderLayout.SOUTH);
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
				int index=table.getSelectionModel().getMinSelectionIndex();
				
				//handling the state of the buttons
				if(index>=0 && index<tableModel.getRowCount()) {
					
					if(tableModel.getRowCount()>1) {
						
						deleteButton.setEnabled(true);
					}
					
					if(index>0) {
						
						upButton.setEnabled(true);
					}
					
					if(index<tableModel.getRowCount()-1) {
						
						downButton.setEnabled(true);
					}
				}
			}
			
			/**
			 * initializing the new string containing the list of the column names
			 * @param newValue a string containing a list of column names
			 */
			protected void initializeValue(String newValue) {
				
				tableModel.putNewValue(newValue, false);
			}
			
			/**
			 * setting the new string containing the list of the column names
			 * @param newValue a string containing a list of column names
			 */
			protected void setNewValue(String newValue) {
				
				tableModel.putNewValue(newValue, true);
			}
			
			/**
			 * resize all the columns
			 */
			public void resizeColumns(){
				
				Graphics g=getGraphics();

				if(g!=null) {
					
					int parentWidth=table.getParent().getSize().width-1;
					String columnName0=table.getColumnName(0);
					
					//computing the width in px of the column name
					int width=0;
					char[] chars=columnName0.toCharArray();
					
					for(int j=0; j<chars.length; j++) {
						
						width+=g.getFontMetrics().charWidth(chars[j])+2;
					}
					
					table.getColumnModel().getColumn(0).setPreferredWidth(width);
					table.getColumnModel().getColumn(1).setPreferredWidth(parentWidth-width);
					table.revalidate();
				}
			}
			
			/**
			 * the class of the table model
			 * @author ITRIS, Jordi SUC
			 */
			protected class JWidgetTableModel extends AbstractTableModel{
				
				/**
				 * the list of the specified names of the column
				 */
				protected LinkedList<String> columnNames=new LinkedList<String>();
				
				/**
				 * the names of the columns
				 */
				protected String[] colNames=new String[2];
				
				/**
				 * the new item to select
				 */
				private int newSelection=0;
				
				/**
				 * the constructor of the class
				 */
				protected JWidgetTableModel() {
					
					//getting the label of the column names
					try {
						colNames[0]=bundle.getString("colNumberLabel");
						colNames[1]=bundle.getString("nameColumnLabel");
					}catch (Exception ex) {}
				}
				
				/**
				 * notifies that the table data have changed
				 */
				public void tableChanged() {
					
					storeNewValue();
				}
				
				@Override
				public void fireTableStructureChanged() {
					
					SwingUtilities.invokeLater(new Runnable() {
						
						public void run() {
						
							JWidgetTableModel.super.fireTableStructureChanged();
							resizeColumns();
							table.getSelectionModel().setSelectionInterval(newSelection, newSelection);
							handleButtonsState();
						}
					});
				}
				
				/**
				 * putting the new string containing the list of the column names
				 * @param newValue a string containing a list of column names
				 * @param recordNewValue whether the new value should be recorded in the SVG document or not
				 */
				protected void putNewValue(String newValue, boolean recordNewValue) {
					
					if(newValue==null) {
						
						newValue=" "+TableEdition.columnNamesSeparator;
					}
					
					//clearing the column names list
					columnNames.clear();
					
					//filling the list of the column names
					Scanner scanner=new Scanner(newValue);
					scanner.useDelimiter(regexColumnNamesSeparator);
					
					while(scanner.hasNext()) {
						
						columnNames.add(scanner.next());
					}
					
					if(recordNewValue) {
						
						tableChanged();
					}

					fireTableStructureChanged();
				}
				
				/**
				 * adds a new column at the given position
				 */
				protected void addColumn() {
					
					columnNames.add("");
					tableChanged();
					newSelection=columnNames.size()-1;
					fireTableStructureChanged();
				}
				
				/**
				 * removes a column at the given position
				 * @param pos a position
				 */
				protected void removeColumn(int pos) {
					
					if(pos>=0 && pos<=columnNames.size()) {
						
						columnNames.remove(pos);
						tableChanged();
						fireTableStructureChanged();
						
						int selectionPos=pos;
						
						//computing the new index to be selected
						if(pos>=columnNames.size()) {
							
							selectionPos=columnNames.size()-1;
						}
						
						newSelection=selectionPos;
					}
				}
				
				/**
				 * puts the column given by its index to the new given place
				 * @param initialPlace the initial place of the column
				 * @param newPlace the new place
				 */
				protected void putColumn(int initialPlace, final int newPlace) {
					
					if(	initialPlace!=newPlace && initialPlace>=0 && initialPlace<columnNames.size() && 
						newPlace>=0 && newPlace<columnNames.size()) {
						
						String name1=columnNames.get(initialPlace), name2=columnNames.get(newPlace);
						columnNames.set(newPlace, name1);
						columnNames.set(initialPlace, name2);

						newSelection=newPlace;
						tableChanged();
						fireTableStructureChanged();
					}
				}
				
				/**
				 * @see javax.swing.table.TableModel#getColumnCount()
				 */
				public int getColumnCount() {
					
					return colNames.length;
				}
				
				/**
				 * @see javax.swing.table.TableModel#getRowCount()
				 */
				public int getRowCount() {
					
					return columnNames.size();
				}
				
				@Override
				public String getColumnName(int col) {
					
					return colNames[col];
				}
				
				@Override
				public boolean isCellEditable(int row, int col) {
					
					return col==1;
				}
				
				/**
				 * @see javax.swing.table.TableModel#getValueAt(int, int)
				 */
				public Object getValueAt(int row, int col) {
					
					if(col==1 && row>=0 && row<columnNames.size()) {
						
						return columnNames.get(row);
						
					}else if(col==0) {
						
						return row;
					}
					
					return null;
				}
				
				@Override
				public void setValueAt(Object value, int row, int col) {
					
					if(value==null) {
						
						value="";
					}
					
					if(col==1 && row>=0 && row<columnNames.size()) {
						
						columnNames.set(row, value.toString());
					}
					
					storeNewValue();
				}
				
				/**
				 * sets the value buit from the model to the jwidget element
				 */
				protected void storeNewValue() {
					
					//building the value of the attribute to set
					String newValue="";
					
					for(String name : columnNames) {
						
						newValue+=name+columnNamesSeparator;
					}
					
					//setting the new value for the attribute
					setProperty(getElement(), propertiesList.get(1), newValue, true);
					
					//refreshes the source choosers
					refreshSourceChoosers();
				}
			}
		}
	}
	
	/**
	 * the class of the source chooser
	 * @author ITRIS, Jordi SUC
	 */
	protected class TableAnimationChooserJWidgetSourceChooser 
	extends AnimationChooserJWidgetSourceChooser{
		
		/**
		 * the combo for selecting the table cell
		 */
		protected JComboBox rowCombo=new JComboBox(),
											colCombo=new JComboBox();
		
		/**
		 * the listener to the combo boxes
		 */
		protected ActionListener comboListener=null;
		
		/**
		 * the constructor of the class
		 */
		protected TableAnimationChooserJWidgetSourceChooser() {
			
			buildWidget();
		}
		
		@Override
		protected void buildWidget() {
			
			String sourceLabel="", rowLabel="", colLabel="";
			
			try {
				sourceLabel=bundle.getString("cellChooserLabel");
				rowLabel=bundle.getString("rowLabel");
				colLabel=bundle.getString("colLabel");
			}catch (Exception ex) {}
			
			//the source label
			JLabel sourceLbl=new JLabel(sourceLabel);
			
			//the labels for the combos
			JLabel rowLbl=new JLabel(rowLabel+" : ");
			JLabel colLbl=new JLabel(colLabel+" : ");
			
			//building the component
			GridBagLayout layout=new GridBagLayout();
			GridBagConstraints c=new GridBagConstraints();
			c.anchor=GridBagConstraints.WEST;
			setLayout(layout);
			
			c.fill=GridBagConstraints.HORIZONTAL;
			c.insets=new Insets(0, 0, 2, 0);
			c.gridwidth=GridBagConstraints.REMAINDER;
			c.weightx=50;
			layout.setConstraints(sourceLbl, c);
			add(sourceLbl);
			
			c.fill=GridBagConstraints.NONE;
			c.insets=new Insets(0, 0, 1, 0);
			c.gridwidth=1;
			c.weightx=1;
			c.anchor=GridBagConstraints.EAST;
			layout.setConstraints(rowLbl, c);
			add(rowLbl);
			
			c.fill=GridBagConstraints.HORIZONTAL;
			c.gridwidth=GridBagConstraints.REMAINDER;
			c.weightx=50;
			c.anchor=GridBagConstraints.WEST;
			layout.setConstraints(rowCombo, c);
			add(rowCombo);
			
			c.fill=GridBagConstraints.NONE;
			c.gridwidth=1;
			c.weightx=1;
			c.anchor=GridBagConstraints.EAST;
			layout.setConstraints(colLbl, c);
			add(colLbl);
			
			c.fill=GridBagConstraints.HORIZONTAL;
			c.gridwidth=GridBagConstraints.REMAINDER;
			c.weightx=50;
			c.anchor=GridBagConstraints.WEST;
			layout.setConstraints(colCombo, c);
			add(colCombo);
			
			//adding the listener to the combos
			comboListener=new ActionListener() {
				
				public void actionPerformed(ActionEvent evt) {
					
					//getting the selected row and column index
					int row=0, col=0;
					ComboListItem item=(ComboListItem)rowCombo.getSelectedItem();
					
					if(item!=null) {
						
						try {
							row=Integer.parseInt(item.getValue().toString());
						}catch (Exception ex) {}
					}
					
					item=(ComboListItem)colCombo.getSelectedItem();
					
					if(item!=null) {
						
						try {
							col=Integer.parseInt(item.getValue().toString());
						}catch (Exception ex) {}
					}
					
					//setting the new source to edit
					setSource(row+" "+col);
				}
			};
		}
		
		@Override
		protected void updateWidgets() {
			
			rowCombo.removeActionListener(comboListener);
			colCombo.removeActionListener(comboListener);
			
			//initializing the component
			if(getJwidgetElement()!=null) {
				
				//getting the number of rows
				int rowCount=1;
				
				try {
					rowCount=Integer.parseInt(getProperty(getJwidgetElement(), propertiesList.get(0)));
				}catch (Exception ex) {}
				
				//getting the list of the column names
				String colNamesString=getProperty(getJwidgetElement(), propertiesList.get(1));
				LinkedList<String> columnNames=new LinkedList<String>();
				Scanner scanner=new Scanner(colNamesString);
				scanner.useDelimiter(regexColumnNamesSeparator);
				
				while(scanner.hasNext()) {
					
					columnNames.add(scanner.next());
				}
				
				//clearing the combo boxes
				rowCombo.removeAllItems();
				colCombo.removeAllItems();
				
				//filling the combo boxes
				ComboListItem item=null;
				
				for(int i=0; i<rowCount; i++) {
					
					item=new ComboListItem(i, i+"");
					rowCombo.addItem(item);
				}
				
				String columnName="", str="";
				
				for(int i=0; i<columnNames.size(); i++) {
					
					columnName=columnNames.get(i);
					
					if(columnName!=null) {
						
						columnName=columnName.trim();
					}
					
					if(columnName!=null && ! columnName.equals("")) {
						
						str=i+" ("+columnNames.get(i)+")";
						
					}else {
						
						str=i+"";
					}

					item=new ComboListItem(i, str);
					colCombo.addItem(item);
				}
				
				//setting the new source for the chooser
				if(RtdaAnimationsAndActionsModule.getStateRecord().getSourceName().equals("")) {
					
					setSource(initialSourceName);
					
					//selecting the default items
					rowCombo.setSelectedIndex(0);
					colCombo.setSelectedIndex(0);
					
				}else{
					
					String source=RtdaAnimationsAndActionsModule.getStateRecord().getSourceName();
					setSource(source);
					
					//getting the index that have to be selected
					int row=0, col=0;
					
					scanner=new Scanner(source);
					scanner.useDelimiter("\\s+");
					
					try {
						row=Integer.parseInt(scanner.next());
					}catch (Exception ex) {}
					
					try {
						col=Integer.parseInt(scanner.next());
					}catch (Exception ex) {}
					
					//setting the selected combo items
					rowCombo.setSelectedIndex(row);
					colCombo.setSelectedIndex(col);
				}
			}
			
			rowCombo.addActionListener(comboListener);
			colCombo.addActionListener(comboListener);
		}
	}
}
