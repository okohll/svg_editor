/*
 * Created on 19 avr. 2005
 */
package fr.itris.glips.rtdaeditor.test.display.table;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.test.*;
import fr.itris.glips.rtda.toolkit.*;

/**
 * a cell renderer
 * @author ITRIS, Jordi SUC
 */
public class TestTableRenderer implements TableCellRenderer{
    
    /**
     * the color for the empty panels
     */
    public final static Color emptyPanelColor=new Color(230, 230, 230);
    
    /**
     * the list of the simulation values objects
     */
    private java.util.List<TestSimulationTagValues> testSimulationValues=
    	new LinkedList<TestSimulationTagValues>();
    
    /**
     * the map associating the name of a function to its label
     */
    private Map<String, String> functions;
    
    /**
     * the constructor of the class
     * @param testSimulationValues the list of the simulation values objects
     * @param functions the map associating
     */
    public TestTableRenderer(LinkedList<TestSimulationTagValues> testSimulationValues, 
    		Map<String, String> functions){

        this.functions=functions;
        
        if(testSimulationValues!=null){
            
            this.testSimulationValues.addAll(testSimulationValues);
        }
    }

    /**
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value, 
    		boolean hasFocus, boolean isSelected, int row, int col) {
        
        Component cmp=null;

        if(row<testSimulationValues.size() && value!=null){
            
            //getting the accurate test value object
            TestSimulationTagValues testValues=testSimulationValues.get(row);
            
            if(testValues!=null){

            	TestTableModel model=(TestTableModel)table.getModel();
            	
                //disables the widgets that should not be used
                if((model.getColumnId(col).equals("value") && ! testValues.canModifyValue()) || 
                	(model.getColumnId(col).equals("min") && ! testValues.canModifyMin()) || 
                	(model.getColumnId(col).equals("max") && ! testValues.canModifyMax()) ||
                	(model.getColumnId(col).equals("type") && ! testValues.canModifyType()) ||
                	(model.getColumnId(col).equals("period") && ! testValues.canModifyPeriod()) || 
                	(model.getColumnId(col).equals("initialValue") && ! testValues.canModifyInitialValue())){
                    
                    cmp=new JPanel();
                    cmp.setBackground(emptyPanelColor);

                }else{
                    
                    if(value instanceof Number){

                    	double dVal=Toolkit.getNumber(value);
                        JTextField textField=new JTextField(FormatStore.format(dVal));
                        textField.setFont(table.getFont());
                        textField.setHorizontalAlignment(SwingConstants.RIGHT);

                        cmp=textField;
                        
                    }else if(((TestTableModel)table.getModel()).getColumnId(col).equals("value") && 
                    		value instanceof String && testValues.getTagType()==TagToolkit.ENUMERATED){

                        //returns a combo when the tag is an enumerated one
                        java.util.List<String> list=new LinkedList<String>(
                        		testValues.getEnumeratedValuesMap().values());
                        
                        JComboBox combo=new JComboBox(list.toArray());
                        combo.setFont(table.getFont());
                        
                        if(testValues.getTagType()==TagToolkit.STRING){
                        	
                        	combo.setEditable(true);
                            combo.addItem(testValues.getTagValue());
                        }
                        
                        if(testValues.getEnumeratedValues().size()==1){
                            
                            combo.addItem(value);
                        }

                        //selecting the item
                        combo.setSelectedItem(value);

                        cmp=combo;

                    }else if(((TestTableModel)table.getModel()).getColumnId(col).equals("type") && 
                    		value instanceof String){

                        JComboBox combo=new JComboBox();
                        combo.setFont(table.getFont());
                        
                        //getting the value to be selected
                        String val=value.toString();
                        
                        if(val.equals("")){
                            
                            val="constant";
                        }
                        
                        //getting the label of the selected value
                        String label=functions.get(val);
                        
                        if(label!=null && ! label.equals("")){
                        	
                        	combo.addItem(label);
                        	
                        }else{
                        	
                        	combo.addItem(val);
                        }
                        
                        //selecting the item
                        combo.setSelectedIndex(0);

                        return combo;
                        
                    }else if(! ((TestTableModel)table.getModel()).getColumnId(col).equals("name")){
                        
                        JTextField textField=new JTextField(value.toString());
                        textField.setFont(table.getFont());
                        textField.setHorizontalAlignment(SwingConstants.LEFT);

                        cmp=textField;
                        
                    }else{
                        
                        JLabel label=new JLabel(value.toString());
                        label.setFont(table.getFont());
                        label.setHorizontalAlignment(SwingConstants.LEFT);

                        cmp=label;
                    }
                }
            }
        }
            
        return cmp;
    }
}
