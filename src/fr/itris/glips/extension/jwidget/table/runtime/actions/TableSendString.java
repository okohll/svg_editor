package fr.itris.glips.extension.jwidget.table.runtime.actions;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.table.runtime.*;
import fr.itris.glips.rtda.action.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the class of the send string action
 * @author ITRIS, Jordi SUC
 */
public class TableSendString extends AbstractSendString{
	
	/**
	 * the row and col indices corresponding to the cell that is handled by this action
	 */
	private int rowIndex=0, colIndex=0;
	
	/**
	 * the constructor of the class
	 * @param picture the svg picture this action is linked to
	 * @param projectName the name of the project this action is linked to
	 * @param parent the top level container
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
	 * @param jwidgetRuntime the jwidget runtime object, if this action is linked to a jwidget
	 */
	public TableSendString(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
		
		initializeAction();
	}
	
	@Override
	protected void initializeAction() {

		//getting the cell coordinates in the table
		rowIndex=TableRuntime.getRow(sourceId);
		colIndex=TableRuntime.getCol(sourceId);
		
		super.initializeAction();
		
        //setting whether the cell linked to this action can be edited or not
        ((TableWidget)component).setCellEditable(rowIndex, colIndex, isAuthorized);
	}
	
	@Override
	public Runnable dataChanged(DataEvent evt) {
		
		super.dataChanged(evt);
		
		if(isEntitled()){
			
			final Map<String, Object> newMap=evt.getDataNameToValue();
			
			if(isAuthorized && newMap.containsKey(actionElement.getAttribute(tagToWriteName))){
				
				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {

				        String newValue="";
				        Object obj=getData(actionElement.getAttribute(tagToWriteName));
				        
				        if(obj!=null){
				        	
				        	newValue=obj.toString();
				        }
						
				        jwidgetRuntime.unregisterListeners();
				        ((TableWidget)component).getModel().setValueAt(newValue, rowIndex, colIndex);
				        jwidgetRuntime.registerListeners();
					}
				});
			}
			
			jwidgetRuntime.refreshComponentState();
		}

		return null;
	}
	
	@Override
	public void performAction(Object evt){
		
		if(isEntitled() && isAuthorized && showConfirmationDialog()) {
			
			//getting the current value of the tag
			Object obj=getData(actionElement.getAttribute(tagToWriteName));
			String currentTagToWriteValue="";
			
			if(obj!=null){
				
				currentTagToWriteValue=obj.toString();
			}
			
			//getting the new value
			Object newObj=((TableWidget)component).getModel().getValueAt(rowIndex, colIndex);
			String newValue="";
			
			if(newObj!=null){
				
				newValue=newObj.toString();
			}
			
			putTagValue(actionElement.getAttribute(tagToWriteName), newValue);
			
			handleReturnToInitialValue(currentTagToWriteValue);
		}
	}
}
