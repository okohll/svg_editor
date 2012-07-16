package fr.itris.glips.extension.jwidget.externaldatatable.runtime.anim;

import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.externaldatatable.runtime.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the animation used to load data in a table
 * @author Jordi SUC
 */
public class ExternalDataTableLoadData extends JWidgetAnimation{
	
	/**
	 * the string names
	 */
	private static String dbAttributeName="dbName", 
		requestAttributeName="request", separator="|", 
			separatorRegex="[|]", childrenTagName="rtda:fieldLabel", 
				dataNameAtt="dataName", labelAtt="dataNameLabel";
	
    /**
     * the constructor of the class
     * @param jwidgetRuntime the associated jwidget runtime object
     * @param component the component to animate
     * @param animationElement the animation element
     */
    public ExternalDataTableLoadData(JWidgetRuntime jwidgetRuntime, 
    		JComponent component, Element animationElement) {
    	
    	super(jwidgetRuntime, component, animationElement);

    	//getting the parameters
    	String dbName=animationElement.getAttribute(dbAttributeName);
    	String request=animationElement.getAttribute(requestAttributeName);
    	request=request.replaceAll(Toolkit.separatorBrRegex, " ");
    	
    	//creating the db access objects
    	ExternalDataTableDataBaseAccess dbAccess=new ExternalDataTableDataBaseAccess(
    		(ExternalDataTableRuntime)jwidgetRuntime, dbName, "", request);
    	
    	((ExternalDataTableRuntime)jwidgetRuntime).setDataBaseAccess(dbAccess);
    	
    	//creating the map associating a field name to its label
    	Map<String, String> fieldNameToLabelMap=
    		new HashMap<String, String>();
    	
    	//getting all the children of the animation element and filling the map of the labels
    	NodeList labelChildren=
    		animationElement.getElementsByTagName(childrenTagName);
    	Element element=null;
    	String fieldName="", label="";
    	
    	for(int i=0; i<labelChildren.getLength(); i++){
    		
    		element=(Element)labelChildren.item(i);
    		fieldName=element.getAttribute(dataNameAtt);
    		label=element.getAttribute(labelAtt);
    		
    		fieldNameToLabelMap.put(fieldName, label);
    	}
    	
    	((ExternalDataTableRuntime)jwidgetRuntime).
    		setFieldNameToLabelMap(fieldNameToLabelMap);
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
}
