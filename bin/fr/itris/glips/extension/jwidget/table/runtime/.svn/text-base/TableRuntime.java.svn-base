package fr.itris.glips.extension.jwidget.table.runtime;

import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.table.edition.*;
import fr.itris.glips.extension.jwidget.table.runtime.actions.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.animaction.Action;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class of the view browser runtime jwidget
 * @author ITRIS, Jordi SUC
 */
public class TableRuntime extends JWidgetRuntime{

	/**
	 * the jwidget id
	 */
	public static String jwidgetId="TableWidget";
	
	/**
	 * the table widget
	 */
	private TableWidget tableWidget;
	
    /**
     * the constructor of the class
     * @param picture the svg picture
     * @param element the svg element defining the jwidget
     */
    public TableRuntime(SVGPicture picture, Element element){
		
		super(picture, element);
	}
	
    @Override
    public void initialize() {

    	tableWidget=new TableWidget(this, element);
    	component=tableWidget;
    	
    	//handling the look of the component
    	JWidgetToolkit.handleLook(element, tableWidget.getTable());
		JWidgetToolkit.handleLook(element, tableWidget.getTable().getTableHeader());
		JWidgetToolkit.handleBackgroundAndBorderLook(element, tableWidget.getTable());
		
		//handling the row height of the component
		JWidgetToolkit.handleRowHeight(element, tableWidget.getTable());
		
		//whether the horizontal or vertical lines should be displayed
		boolean showHLines=element.getAttribute(
				"showHorizontalLines").equals(Boolean.toString(true));
		boolean showVLines=element.getAttribute(
				"showVerticalLines").equals(Boolean.toString(true));
		
		tableWidget.getTable().setShowGrid(false);
		tableWidget.getTable().setShowHorizontalLines(showHLines);
		tableWidget.getTable().setShowVerticalLines(showVLines);
		
		//creating the cell editor for the table
		JTextField textField=new JTextField();
    	JWidgetToolkit.handleLook(element, textField);
		DefaultCellEditor cellEditor=new DefaultCellEditor(textField);
		tableWidget.getTable().setCellEditor(cellEditor);
    }
    
    @Override
    public void initializeWhenCanvasDisplayed() {

    	JWidgetToolkit.resizeColumns(tableWidget.getTable());
    	tableWidget.validate();
    }

    @Override
    public Action createAction(Element actionElement) {
    	
    	Action action=null;
    	
    	if(actionElement!=null) {
    		
    		String tagName=actionElement.getTagName();
    		String sourceId=actionElement.getAttribute("jwidget-source");
    		
    		if(tableWidget.cellExist(sourceId)){
    			
        		if(tagName.equals("rtda:simpleSendCommand")) {
        			
        			action=new TableSimpleSendCommand(picture, projectName, 
        				picture.getCanvas(), component, null, actionElement, this);
        			
        		}else if(tagName.equals("rtda:sendMeasure")) {
        			
        			action=new TableSendMeasure(picture, projectName, 
        				picture.getCanvas(), component, null, actionElement, this);
        			
        		}else if(tagName.equals("rtda:sendString")) {
        			
        			action=new TableSendString(picture, projectName, 
            				picture.getCanvas(), component, null, actionElement, this);
            		
        		}else {
        			
        			action=super.createAction(actionElement);
        		}
    		}
    	}
    	
    	return action;
    }
    
    /**
     * returns the row in a table denoted by the provided source id
     * @param sourceId the id of a source
     * @return the row in a table denoted by the provided source id
     */
    public static int getRow(String sourceId){
    	
    	int row=-1;
		int pos=sourceId.indexOf(" ");
		
		try{
			row=Integer.parseInt(sourceId.substring(0, pos));
		}catch(Exception ex){}
    	
    	return row;
    }
    
    /**
     * returns the col in a table denoted by the provided source id
     * @param sourceId the id of a source
     * @return the col in a table denoted by the provided source id
     */
    public static int getCol(String sourceId){
    	
    	int col=-1;
		int pos=sourceId.indexOf(" ");
		
		try{
			col=Integer.parseInt(sourceId.substring(
				pos+1, sourceId.length()));
		}catch(Exception ex){}
    	
    	return col;
    }
    
    /**
     * @return the jwidget edition class linked to this jwidget runtime class
     */
    public static Class<?> getEditionClass(){
    	
    	return TableEdition.class;
    }
    
    @Override
    public void refreshComponentState() {
    	
    	//getting the map associating each sub component id to its actions
    	Map<String, Set<Action>> subCmpIdToActions=
    		getSubComponentIdToActionsMap();

		Set<Action> actionsSet=null;
		boolean enabled=false;
		int row=0, col=0;
		
		for(String sourceId : subCmpIdToActions.keySet()){
			
			actionsSet=subCmpIdToActions.get(sourceId);
			
			//getting the component associated to the source id
			row=getRow(sourceId);
			col=getCol(sourceId);
			
			if(row>=0 && col>=0){
				
				enabled=(actionsSet!=null && 
						! actionsInactive(actionsSet));
				tableWidget.setCellEditable(row, col, enabled);
			}
		}
    }
    
    @Override
    public void registerListeners() {

    	tableWidget.registerListeners();
    }
    
    @Override
    public void unregisterListeners() {

    	tableWidget.unregisterListeners();
    }
    
    @Override
    public void dispose() {
    	
    	tableWidget.dispose();
    	super.dispose();
    }
}
