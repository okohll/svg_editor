package fr.itris.glips.rtda.action;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.config.*;
import fr.itris.glips.rtda.jwidget.*;
import java.awt.*;
import java.util.*;

/**
 * the abstract class of the record data action
 * @author ITRIS, Jordi SUC
 */
public abstract class AbstractRecordData extends fr.itris.glips.rtda.animaction.Action{
	
	/**
	 * the string names
	 */
	protected static final String /*dbTableAttributeName="dbTable",*/ dbAttributeName="dbName", 
		tableAttributeName="tableName", dataNameAttributeName="dataName", 
			tagAttributeName="tag", childNodeName="rtda:match";
	
	/**
	 * the object used to access db
	 */
	protected RecordDataDataBaseAccess dbAccess;
	
	/**
	 * the map associating a field name to a tag name
	 */
	protected Map<String, String> fieldToTagMap=new HashMap<String, String>();
	
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
	public AbstractRecordData(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);

		computeRightsForRecipeModification();
	}
	
	/**
	 * initializes the action
	 */
	protected void initializeAction(){
		
		//getting the tool tip
		try{
			toolTipText=bundle.getString("tooltip_recordData");
		}catch (Exception ex){}
		
		//getting the data base and table names
		/*String[] dbTableNames=DataBaseAccess.getDbAndTable(
				actionElement.getAttribute(dbTableAttributeName));*/
		String dbName=actionElement.getAttribute(dbAttributeName);
		String tableName=actionElement.getAttribute(tableAttributeName);
		
		//creating the db access object
		dbAccess=new RecordDataDataBaseAccess(picture, dbName, tableName);
		
		//filling the map associating a db field name to a tag name
		NodeList childNodes=actionElement.getElementsByTagName(childNodeName);
		Element el;
		String fieldName="", tagName="";
		
		for(int i=0; i<childNodes.getLength(); i++){
			
			el=(Element)childNodes.item(i);
			fieldName=el.getAttribute(dataNameAttributeName).toLowerCase();
			tagName=el.getAttribute(tagAttributeName);
			fieldToTagMap.put(fieldName, tagName);
			dataNames.add(tagName);
		}

		initializeAuthorizationTag();
	}
	
	@Override
	public void performAction(Object evt) {

		if(isEntitled() && isAuthorized && showConfirmationDialog()){
			
			//computing the map associating a table field to its corresponding tag value
			Map<String, Object> fieldNameToValueMap=
				new HashMap<String, Object>();
			
			String tagName="";
			Object tagValue=null;
			
			for(String fieldName : fieldToTagMap.keySet()){
				
				tagName=fieldToTagMap.get(fieldName);

				if(tagName!=null){
					
					tagValue=getData(tagName);
					
					if(tagValue!=null){
						
						fieldNameToValueMap.put(fieldName, tagValue);
					}
				}
			}
			
			dbAccess.recordData(fieldNameToValueMap);
		}
	}
	
	@Override
	public Runnable dataChanged(DataEvent evt) {
		
		super.dataChanged(evt);
		checkIsAuthorized();
		return null;
	}
	
	/**
	 * the class used to load data from a db
	 * @author Jordi SUC
	 */
	protected class RecordDataDataBaseAccess extends DataBaseAccess{
		
		/**
		 * the constructor of the class
		 * @param picture a svg picture
		 * @param dbName the data base name
		 * @param tableName the table name
		 */
		public RecordDataDataBaseAccess(SVGPicture picture, 
				String dbName, String tableName){

			super(picture, dbName, tableName);
		}
	}
}
