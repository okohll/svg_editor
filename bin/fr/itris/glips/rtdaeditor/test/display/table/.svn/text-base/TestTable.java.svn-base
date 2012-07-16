/*
 * Created on 19 avr. 2005
 */
package fr.itris.glips.rtdaeditor.test.display.table;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.rtda.test.*;

/**
 * the class of a test table
 * 
 * @author ITRIS, Jordi SUC
 */
public class TestTable extends JTable{

    /**
     * the font
     */
    public final Font theFont=new Font("theFont", Font.ROMAN_BASELINE, 12);
    
    /**
     * the map associating the name of a function to its label
     */
    private LinkedHashMap<String, String> functions=new LinkedHashMap<String, String>();
    
    /**
     * the cell renderer
     */
    private TestTableRenderer renderer;
    
    /**
     * the cell editor of the table
     */
    private TestTableStringEditor editor;
    
    /**
     * the constructor of the class
     * @param model the model of the table
     * @param testSimulationValues the list of the simulation values objects
     */
    public TestTable(TestTableModel model, LinkedList<TestSimulationTagValues> testSimulationValues){
        
        super(model);

        //the labels for the functions
        String constantLabel="", sinusLabel="", triangleLabel="", rampLabel="";
        
        //getting the labels
        ResourceBundle bundle=ResourcesManager.bundle;
        
        if(bundle!=null){
            
            constantLabel=bundle.getString("rtdaanim_test_constant");
            sinusLabel=bundle.getString("rtdaanim_test_sinus");
			triangleLabel=bundle.getString("rtdaanim_test_triangle");
			rampLabel=bundle.getString("rtdaanim_test_ramp");
        }
        
        //fills the map of the name and label of the function
        functions.put("constant", constantLabel);
        functions.put("sin", sinusLabel);
        functions.put("triangle", triangleLabel);
        functions.put("ramp", rampLabel);
        
        setFont(theFont);
        
        //creating the cell renderer
        renderer=new TestTableRenderer(testSimulationValues, functions);
        
        //creating the cell editor
        editor=new TestTableStringEditor(this, testSimulationValues, functions);
        
        //setting the editor for the columns
        TableColumn col=null; 

        for(int i=0; i<getModel().getColumnCount(); i++){
            
            col=this.getColumnModel().getColumn(i);
            col.setCellEditor(editor);
        }
        
        //setting the properties of the table
        setCellSelectionEnabled(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setDefaultEditor(Object.class, null);
        setIntercellSpacing(new Dimension(2, 2));
        setAutoCreateColumnsFromModel(true);
        setRowHeight(20);
        getTableHeader().setReorderingAllowed(false);
    }

	@Override
    public TableCellRenderer getCellRenderer(int row, int col) {
        
        if(row<getRowCount() && col<getColumnCount()){
            
            return renderer;
        }

        return super.getCellRenderer(row, col);
    }
}
