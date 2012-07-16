package fr.itris.glips.rtda.action;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.config.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.toolkit.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * the abstract class of the load data action
 * @author ITRIS, Jordi SUC
 */
public abstract class AbstractLoadData extends fr.itris.glips.rtda.animaction.Action{
	
	/**
	 * the string names
	 */
	private static final String dbAttributeName="dbName", 
		requestAttributeName="request", dataNameAttributeName="dataName", 
			tagAttributeName="tag", childNodeName="rtda:match";
	
	/**
	 * the object used to access db
	 */
	private LoadDataDataBaseAccess dbAccess;
	
	/**
	 * the map associating a field name to a tag name
	 */
	private Map<String, String> fieldToTagMap=new HashMap<String, String>();
	
	/**
	 * whether the request contains tags
	 */
	private boolean requestWithTags=false;
	
	/**
	 * the list of the the names
	 */
	private LinkedList<String> requestTagNames=new LinkedList<String>();
	
	/**
	 * the parts of the request between tag names
	 */
	private LinkedList<String> requestParts=new LinkedList<String>();
	
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
	public AbstractLoadData(SVGPicture picture, String projectName, Container parent, 
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
			toolTipText=bundle.getString("tooltip_loadData");
		}catch (Exception ex){}
		
		String dbName=actionElement.getAttribute(dbAttributeName);
		
		//creating the db access object
		dbAccess=new LoadDataDataBaseAccess(picture, dbName, "");
		
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
		
		//handles the request
		handleRequest();
		initializeAuthorizationTag();
	}
	
	/**
	 * handles the request to check if it contains tag names
	 */
	protected void handleRequest(){
		
		//getting the value of the request attribute
		String cValue=actionElement.getAttribute(requestAttributeName);
		
		int pos=cValue.indexOf(Toolkit.startTagInRequest);
		
		if(pos!=-1){
			
			requestWithTags=true;

    		int pos2=0;
    		String tagName="";
    		String beforeTagName="";
    		
    		while(pos!=-1){
    			
    			//getting the part before the tag name
    			beforeTagName=cValue.substring(0, pos+Toolkit.startTagInRequest.length()-1);
    			cValue=cValue.substring(pos+Toolkit.startTagInRequest.length(), cValue.length());
    			
    			//getting the tag name
    			pos2=cValue.indexOf(Toolkit.endTagInRequest);
    			
    			if(pos2!=-1){
    				
    				tagName=cValue.substring(0, pos2);
        			cValue=cValue.substring(pos2+Toolkit.endTagInRequest.length(), cValue.length());

    			}else{
    				
    				break;
    			}
    			
    			requestParts.add(beforeTagName);
    			requestTagNames.add(tagName);
    			
    			pos=cValue.indexOf(Toolkit.startTagInRequest);
    		}
    		
    		requestParts.add(cValue);
			dataNames.addAll(requestTagNames);
			//TODO test tag info
		}
	}
	
	@Override
	public void performAction(Object evt) {
		
		if(isEntitled() && isAuthorized && showConfirmationDialog()){
			
			String request=dbAccess.getRequest();
			
			if(request!=null){
				
				dbAccess.retrieveData(request);
			}
		}
	}
	
	@Override
	public Runnable dataChanged(DataEvent evt) {
		
		super.dataChanged(evt);
		checkIsAuthorized();
		return null;
	}
	
	/** 
	 * method used for receiving data form the db
	 * @param data the data found in the db, 
	 * the first array is the array of the fields of the table, 
	 * the second array is the array of the labels of the fields of the table, 
	 */
	protected void receiveData(final LinkedList<Object[]> data){

		if(data.size()>0){

			//creating the array of the column names
			Object[] colNames=data.getFirst();
			final String[] columnNames=new String[colNames.length];

			for(int i=0; i<colNames.length; i++){

				columnNames[i]=(String)colNames[i];
			}

			data.removeFirst();

			//getting the array of the data
			if(data.size()>0){

				Object[] dataArray=data.getFirst();

				//getting the configuration document of the picture of this action
				ConfigurationDocument confDoc=picture.getMainDisplay().
					getPictureManager().getConfigurationDocument(picture);
				
				//setting each tag with the value from the db
				String tagName;
				int tagType=0;
				int i=0;
				Object value=null;

				for(String fieldName : columnNames){

					tagName=fieldToTagMap.get(fieldName.toLowerCase());

					if(tagName!=null && ! tagName.equals("")){
						
						//getting the value
						value=dataArray[i];

						//getting the type of the tag
						tagType=confDoc.getTagType(tagName);
						
						//getting the value
    					if(value instanceof Timestamp || value instanceof Date){
    						
    						switch(tagType){
    							
    							/*case TagToolkit.ANALOGIC_INTEGER :

    	        					//getting the date format string
    	        					value=(int)((Date)value).getTime();
    								break;*/
    								
    							case TagToolkit.ENUMERATED_CHILD :
    							case TagToolkit.STRING :
    								
		        					value=((Date)value).getTime()+"";
    								break;
    						}
    					}

						putTagValue(tagName, value);
					}

					i++;
				}
			}
		}
	}

	/**
	 * the class used to load data from a db
	 * @author Jordi SUC
	 */
	protected class LoadDataDataBaseAccess extends DataBaseAccess{

		/**
		 * the constructor of the class
		 * @param picture a svg picture
		 * @param dbName the data base name
		 * @param tableName the table name
		 */
		public LoadDataDataBaseAccess(SVGPicture picture, 
				String dbName, String tableName){

			super(picture, dbName, tableName);
		}

		/**
		 * @return the sql request
		 */
		public String getRequest(){
			
			String request="";

    		if(requestWithTags){
        		
        		//rebuilding the attribute value
        		int i=0;
        		String tagName, tagValue;
        		Object obj;
        		double dVal=0;
        		
        		for(String part : requestParts){
        			
        			request+=part;
        			
        			if(i<requestTagNames.size()){
        				
        				tagName=requestTagNames.get(i);
        				obj=picture.getMainDisplay().
        					getAnimationsHandler().getData(tagName);
        				
        				if(obj==null){
        					
        					obj="";
        				}
        				
        				tagValue=obj.toString();
        				
        				//checking if the value is of double or int type
        				try{
        					dVal=Double.parseDouble(tagValue);
        				}catch (Exception ex){dVal=Double.NaN;}
        				
        				if(Math.floor(dVal)==dVal){
        					
        					tagValue=((int)dVal)+"";
        				}
        				
        				//adding the tag value to the request
        				request+=tagValue+"'";
        			}
        			
        			i++;
        		}
    			
    		}else{
    			
    			request=actionElement.getAttribute(requestAttributeName);
    		}
    		
        	request=request.replaceAll(Toolkit.separatorBrRegex, " ");
    		
    		return request;
		}

		@Override
		protected void receiveData(LinkedList<Object[]> data) {

			AbstractLoadData.this.receiveData(data);
		}
	}
}