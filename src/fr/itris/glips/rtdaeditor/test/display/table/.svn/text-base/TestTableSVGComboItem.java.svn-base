/*
 * Created on 19 avr. 2005
 */
package fr.itris.glips.rtdaeditor.test.display.table;

import fr.itris.glips.rtda.test.*;

/**
 * the class of the items in the combo boxes
 * @author ITRIS, Jordi SUC
 */
public class TestTableSVGComboItem{
    
	/**
	 * the value and label strings
	 */
    private String value, label;
    
    /**
     * the index of a column
     */
    private int columnIndex=0;
    
    /**
     * the object describing the variables that should be modified to simulate the animations
     */
    private TestSimulationTagValues testValues=null;
    
    /**
     * the constructor of the class
     * @param value the value of the item
     * @param label the label of the item
     * @param columnIndex the index of a column
     * @param testValues the object describing the variables that should be modified to simulate the animations
     */
    public TestTableSVGComboItem(String value, String label, int columnIndex, TestSimulationTagValues testValues){
        
        this.value=value;
        this.label=label;
        this.columnIndex=columnIndex;
        this.testValues=testValues;
    }
    
    /**
     * @return the value of the item
     */
    public String getValue(){
        return value;
    }
    
    @Override
    public String toString(){
        return label;
    }
    
    /**
     * sets the new values for the combo item
     * @param value the value of the item
     * @param label the label of the item
     */
    public void setValues(String value, String label){
        
        this.value=value;
        this.label=label;
    }

    /**
     * @return Returns the testValues.
     */
    public TestSimulationTagValues getTestValues() {
        return testValues;
    }

    /**
     * @return Returns the columnIndex.
     */
    public int getColumnIndex() {
        return columnIndex;
    }
}
