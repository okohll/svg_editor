/*
 * Created on 19 avr. 2005
 */
package fr.itris.glips.rtdaeditor.test.display.table;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.test.*;
import fr.itris.glips.rtda.toolkit.*;

/**
 * the class of the renderer of the cells in the table
 * 
 * @author ITRIS, Jordi SUC
 */
public class TestTableStringEditor extends AbstractCellEditor implements TableCellEditor{
    
    /**
     * the list of the simulation values objects
     */
    private java.util.List<TestSimulationTagValues> testSimulationValues=
    	new LinkedList<TestSimulationTagValues>();
    
    /**
     * the current value for the editor
     */
    private Object currentValue;
    
    /**
     * the map associating the name of a function to its label
     */
    private Map<String, String> functions;
    
    /**
     * the table to which this cell editor is associated
     */
    private JTable theTable;
    
    /**
     * the combo box
     */
    private JComboBox combo=new JComboBox();
    
    /**
     * the editable combo box
     */
    private JComboBox editableCombo=new JComboBox();
    
    /**
     * the listeners
     */
    private ActionListener comboListener;
    
    /**
     * the text field listeners
     */
    private CaretListener textFieldListener, textFieldStringListener;
    
    /**
     * the last editing component
     */
    private JComponent lastComponent;

    /**
     * the constructor of the class
     * @param table a table
     * @param testSimulationValues the list of the simulation values objects
     * @param functions the map associating
     */
    protected TestTableStringEditor(JTable table, LinkedList<TestSimulationTagValues> testSimulationValues, 
    													Map<String, String> functions){

        this.theTable=table;
        this.functions=functions;
        
        if(testSimulationValues!=null){
            
            this.testSimulationValues.addAll(testSimulationValues);
        }
        
        //setting the properties of the combo
        editableCombo.setEditable(true);
        
        //the listener to the combo changes
        comboListener=new ActionListener(){

            public void actionPerformed(ActionEvent evt) {

            	JComboBox theCombo=(JComboBox)evt.getSource();
            	
                if(theCombo.getSelectedItem()!=null){
                    
                    Object item=theCombo.getSelectedItem();
                    
                    if(item instanceof TestTableSVGComboItem){
                        
                        currentValue=((TestTableSVGComboItem)item).getValue();
                        
                    }else{
                        
                        currentValue=item.toString();
                    }

                    fireEditingStopped();
                    
                    theCombo.removeActionListener(this);
                    TestTableStringEditor.this.theTable.repaint();
                }
            }
        };

        //the listener to the textfield changes
	    textFieldListener=new CaretListener(){

	    	public void caretUpdate(CaretEvent evt) {

                double db=0;
                
                try{
                    db=Double.parseDouble(((JTextField)evt.getSource()).getText());
                }catch (Exception ex){db=Double.NaN;}
                
                if(db!=Double.NaN){
                    
                    currentValue=new Double(db);
                    
                }else{
                    
                    currentValue=null;
                }
	    	}
	    };
	    
        //the listener to the string textfield changes
	    textFieldStringListener=new CaretListener(){

	    	public void caretUpdate(CaretEvent evt) {

                currentValue=((JTextField)evt.getSource()).getText();
	    	}
	    };
    }
    
	/**
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {

	    return currentValue;
	}
	
	@Override
	protected void fireEditingStopped() {
		
		//removing all the listeners
        if(lastComponent!=null && lastComponent instanceof JTextField) {
        	
        	((JTextField)lastComponent).removeCaretListener(textFieldListener);
        	((JTextField)lastComponent).removeCaretListener(textFieldStringListener);
        }
		
		super.fireEditingStopped();
	}
	
	/**
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(
    		JTable table, Object value, boolean hasFocus, int row, int col) {
        
        currentValue=null;
        lastComponent=null;
        
        if(row<testSimulationValues.size() && value!=null){
            
            //getting the accurate test value object
            TestSimulationTagValues testValues=testSimulationValues.get(row);
            
            if(testValues!=null){
                
                if(value instanceof Number){
                	
                	double dVal=Toolkit.getNumber(value);

                    //setting the properties of the textfield
                	JTextField textField=new JTextField();
                    textField.setText(FormatStore.format(dVal));
                    textField.setFont(table.getFont());
                    textField.setHorizontalAlignment(SwingConstants.RIGHT);
            	    textField.addCaretListener(textFieldListener);
            	    
            	    lastComponent=textField;
                    
                }else if(((TestTableModel)table.getModel()).getColumnId(col).equals("value") && 
                        		value instanceof String && testValues.getTagType()==TagToolkit.ENUMERATED){
                	
                	//getting the combo to be used
                	JComboBox currentCombo=null;
                	
                    if(testValues.getTagType()==TagToolkit.STRING){
                        
                    	currentCombo=editableCombo;
                    	
                    }else{
                    	
                    	currentCombo=combo;
                    }

                    currentCombo.removeActionListener(comboListener);
                    currentCombo.removeAllItems();
                    currentCombo.setFont(table.getFont());

                    if(testValues.getTagType()==TagToolkit.STRING){
                    	
                    	//adding the current value to the combo
                    	currentCombo.addItem(testValues.getValue());
                    	
                    	if(testValues.getValue()!=TestSimulationTagValues.getInvalidLabel()){
                    		
                    		TestTableSVGComboItem item=new TestTableSVGComboItem(
                    			TestSimulationTagValues.getInvalidLabel(),
                    				TestSimulationTagValues.getInvalidLabel(), col, testValues);
                    		
                    		currentCombo.addItem(item);
                    	}
                    	
                    }else{
                        
                        //creating the list of the enumerated values
                        java.util.List<String> list=new LinkedList<String>();
                        list.addAll(testValues.getEnumeratedValuesMap().values());
                    	
                        //adding the items
                        TestTableSVGComboItem item=null, selectedItem=null;
                        
                        for(String enumValue : list){

                            if(enumValue!=null){
                                
                                item=new TestTableSVGComboItem(
                                		enumValue, enumValue, col, testValues);
                                
                                if(value.equals(enumValue)){
                                    
                                    selectedItem=item;
                                }
                                
                                currentCombo.addItem(item);
                            }
                        }
                        
                        //selecting an item
                        if(selectedItem!=null){
                            
                        	currentCombo.setSelectedItem(selectedItem);
                        }
                    }

                    currentCombo.addActionListener(comboListener);

                    lastComponent=currentCombo;

                }else if(((TestTableModel)table.getModel()).getColumnId(col).equals("type") && 
                		value instanceof String){

                	combo.removeActionListener(comboListener);
                	combo.removeAllItems();
                    combo.setFont(table.getFont());
                    
                    //filling the combo with the items
                    TestTableSVGComboItem item=null, selectedItem=null;
                    String label="";
                    
                    for(String name : functions.keySet()){

                        if(name!=null && ! name.equals("")){
                            
                        	label=functions.get(name);
                        	
                        	if(label!=null && ! label.equals("")){
                        		
                                item=new TestTableSVGComboItem(name, label, col, testValues);

                        	}else{
                        		
                                item=new TestTableSVGComboItem(name, name, col, testValues);
                        	}
                        	
                            if(value.equals(name)){
                                
                                selectedItem=item;
                            }
                            
                            combo.addItem(item);
                        }
                    }

                    //selecting an item
                    if(selectedItem!=null){
                        
                        combo.setSelectedItem(selectedItem);
                    }
                    
                    combo.addActionListener(comboListener);

                    lastComponent=combo;
                    
                }else{

                	JTextField stringTextField=new JTextField();
            	    
                    //setting the properties of the textfield
                	stringTextField.setText(value.toString());
                	stringTextField.setFont(table.getFont());
                	stringTextField.setHorizontalAlignment(SwingConstants.LEFT);

                	stringTextField.addCaretListener(textFieldStringListener);
            	    
                	lastComponent=stringTextField;
                }
            }
        }
        
        return lastComponent;
    }
 
}
