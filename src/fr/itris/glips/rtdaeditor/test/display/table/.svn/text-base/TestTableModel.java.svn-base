/*
 * Created on 19 avr. 2005
 */
package fr.itris.glips.rtdaeditor.test.display.table;

import java.util.*;
import javax.swing.table.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.test.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the model of the table
 * 
 * @author ITRIS, Jordi SUC
 */
public class TestTableModel extends AbstractTableModel{
    
    /**
     * the list of the simulation values objects
     */
    private java.util.List<TestSimulationTagValues> testSimulationValues=
    	new LinkedList<TestSimulationTagValues>();
    
    /**
     * the array of the names of the columns
     */
    public static String[] columnIds={"name", "value", "min", "max", "type", "period", "initialValue"};
    
    /**
     * the labels for the names of the columns
     */
    private String nameLabel="", valueLabel="", minLabel="", maxLabel="", periodLabel="", initialValueLabel="", typeLabel="";
    
    /**
     * the constructor of the class
     * @param testSimulationValues the list of the simulation values objects
     */
    protected TestTableModel(LinkedList<TestSimulationTagValues> testSimulationValues){
        
        if(testSimulationValues!=null){
            
            this.testSimulationValues.addAll(testSimulationValues);
        }
        
        //getting the labels
        ResourceBundle bundle=ResourcesManager.bundle;
        
        if(bundle!=null){
            
            nameLabel=bundle.getString("rtdaanim_test_name");
            valueLabel=bundle.getString("rtdaanim_test_value");
            minLabel=bundle.getString("rtdaanim_test_min");
            maxLabel=bundle.getString("rtdaanim_test_max");
            typeLabel=bundle.getString("rtdaanim_test_type");
            periodLabel=bundle.getString("rtdaanim_test_period");
            initialValueLabel=bundle.getString("rtdaanim_test_initialValue");
        }
    }
    
    @Override
    public String getColumnName(int col) {
        
        if(columnIds[col].equals("name")){
            
            return nameLabel;
            
        }else if(columnIds[col].equals("value")){
            
            return valueLabel;
            
        }else if(columnIds[col].equals("min")){
            
            return minLabel;
            
        }else if(columnIds[col].equals("max")){
            
            return maxLabel;
            
        }else if(columnIds[col].equals("type")){
            
            return typeLabel;
            
        }else if(columnIds[col].equals("period")){
            
            return periodLabel;
            
        }else if(columnIds[col].equals("initialValue")){
            
            return initialValueLabel;
        }
        
        return "";
    }
    
    /**
     * returns the name of a column given its index
     * @param col the index of a column
     * @return the name of a column given its index
     */
    public String getColumnId(int col) {
        
        if(col>=0 && col<columnIds.length){
            
            return columnIds[col];
        }
        
        return "";
    }
    
    /**
     * @return the number of rows
     */
    public int getRowCount() { 
        return testSimulationValues.size();
    }
    
    /**
     * @return the number of columns
     */
    public int getColumnCount() { 
        return columnIds.length; 
    }
    
    /**
     * returns the value at the cell specified with its coordinates in the table
     * @param row 
     * @param col
     * @return the value at the cell specified with its coordinates in the table
     */
    public Object getValueAt(int row, int col) {
        
        if(row<getRowCount() && col<getColumnCount()){
            
            //getting the accurate test value object
            TestSimulationTagValues testValues=testSimulationValues.get(row);
            
            if(testValues!=null){
                
                //returning the accurate value
                if(columnIds[col].equals("name")){
                    
                    return testValues.getTagName();
                    
                }else if(columnIds[col].equals("value")){
                	
                    Object value=testValues.getValue();
                    
                    if((testValues.getTagType()==TagToolkit.ANALOGIC_FLOAT || 
                    		testValues.getTagType()==TagToolkit.ANALOGIC_INTEGER)){

                        return Toolkit.getNumber(value);
                    }
                    
                    if(value==null){
                    	
                    	return TestSimulationTagValues.getInvalidLabel();
                    }
                    
                    if(value.equals("")){
                    	
                    	return TestSimulationTagValues.getDefaultLabel();
                    }
                    
                    if(testValues.getTagType()==TagToolkit.STRING){
                    	
                    	return value;
                    }

                    return testValues.getEnumeratedValuesMap().get(value);

                }else if(columnIds[col].equals("min")){
                    
                    if(testValues.getTagType()==TagToolkit.ANALOGIC_FLOAT || 
                    		testValues.getTagType()==TagToolkit.ANALOGIC_INTEGER){
                        
                        return testValues.getMin();
                    }
                    
                    return null;
                    
                }else if(columnIds[col].equals("max")){
                    
                    if(testValues.getTagType()==TagToolkit.ANALOGIC_FLOAT || 
                    		testValues.getTagType()==TagToolkit.ANALOGIC_INTEGER){
                        
                        return testValues.getMax();
                    }
                    
                    return null;
                    
                }else if(columnIds[col].equals("type")){
                    
                    return testValues.getType();
                    
                }else if(columnIds[col].equals("period")){
                    
                    return new Double(testValues.getPeriod());
                    
                }else if(columnIds[col].equals("initialValue")){
                    
                    return new Double(testValues.getInitialValue());
                }
            }
        }

        return null;
    }
    
    @Override
    public boolean isCellEditable(int row, int col){
        
        if(row<getRowCount() && col<getColumnCount() && col>0){
            
            //getting the accurate test value object
            TestSimulationTagValues testValues=testSimulationValues.get(row);
            
            if(testValues!=null){
                
                if(columnIds[col].equals("value")){
                    
                    return testValues.canModifyValue();
                    
                }else if(columnIds[col].equals("min")){
                    
                    return testValues.canModifyMin();
                    
                }else if(columnIds[col].equals("max")){
                    
                    return testValues.canModifyMax();
                    
                }else if(columnIds[col].equals("type")){
                    
                    return testValues.canModifyType();
                    
                }else if(columnIds[col].equals("period")){
                    
                    return testValues.canModifyPeriod();
                    
                }else if(columnIds[col].equals("initialValue")){
                    
                    return testValues.canModifyInitialValue();
                }
            }
        }
        
        return false; 
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        
        if(row<getRowCount() && col<getColumnCount()){
            
            //getting the accurate test value object
            TestSimulationTagValues testValues=testSimulationValues.get(row);
            
            if(testValues!=null && value!=null){
                
                //setting the value of the accurate field
                if(columnIds[col].equals("value")){

                    testValues.setValue(value);
                    
                }else if(columnIds[col].equals("min")){
                    
                    double db=0;
                    
                    try{
                        db=Double.parseDouble(value.toString());
                    }catch (Exception ex){db=0;}
                    
                    testValues.setMin(db);
                    
                }else if(columnIds[col].equals("max")){
                    
                    double db=0;
                    
                    try{
                        db=Double.parseDouble(value.toString());
                    }catch (Exception ex){db=0;}
                    
                    testValues.setMax(db);
                    
                }else if(columnIds[col].equals("type")){
                    
                    testValues.setType(value.toString());
                    
                }else if(columnIds[col].equals("period") && value instanceof Double){
                    
                    testValues.setPeriod((Double)value);
                    
                }else if(columnIds[col].equals("initialValue") && value instanceof Double){
                    
                    testValues.setInitialValue((Double)value);
                }
            }
        }

        fireTableCellUpdated(row, col);
    }
    
    /**
     * clears this model
     */
    public void clear() {
    	
    	testSimulationValues.clear();
    }
}
