package fr.itris.glips.rtda.action;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.config.*;
import fr.itris.glips.rtda.jwidget.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

/**
 * the abstract class of the write data to file action
 * @author ITRIS, Jordi SUC
 */
public abstract class AbstractWriteDataToFile extends fr.itris.glips.rtda.animaction.Action{
	
	/**
	 * the string names
	 */
	private static final String dbAttributeName="dbName",
		requestAttributeName="request", directoryAttributeName="directory", 
		fileNamePatternAttributeName="fileNamePattern",
		writeOnSameFileAttributeName="writeOnSameFile", sepBetweenFieldsAttributeName="sepBetweenFields",
		displayColumnNamesAttributeName="displayColumnNames", 
		descriptionTypeSuffix=")", regularTypePrefix="string(", dateTypePrefix="date(";
	
	/**
	 * the date format
	 */
	protected static DateFormat dateFormat=
		DateFormat.getDateInstance(DateFormat.SHORT);
	
	/**
	 * the date and time format
	 */
	protected static DateFormat dateTimeFormat=
		DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
	
	/**
	 * the simple date format
	 */
	private SimpleDateFormat customDateFormat;
	
	/**
	 * the object used to access the db
	 */
	private LoadDataToWriteDataBaseAccess dbAccess;
	
	/**
	 * whether to always write on the same file
	 */
	private boolean writeOnSameFile;
	
	/**
	 * whether to display the column names in the header
	 */
	private boolean displayColumnNames;
	
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
	public AbstractWriteDataToFile(SVGPicture picture, String projectName, Container parent, 
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
			toolTipText=bundle.getString("tooltip_writeDataToFile");
		}catch (Exception ex){}

		String dbName=actionElement.getAttribute(dbAttributeName);
		
		//creating the db access object
		dbAccess=new LoadDataToWriteDataBaseAccess(picture, dbName);

		//getting the properties for the writing process
		try{
			writeOnSameFile=Boolean.parseBoolean(
					actionElement.getAttribute(writeOnSameFileAttributeName));
		}catch (Exception ex){}
		
		try{
			displayColumnNames=Boolean.parseBoolean(
					actionElement.getAttribute(displayColumnNamesAttributeName));
		}catch (Exception ex){}
		
		initializeAuthorizationTag();
	}
	
	@Override
	public void performAction(Object evt) {
		
		if(isEntitled() && isAuthorized && showConfirmationDialog()){
			
			dbAccess.retrieveData(dbAccess.getRequest());
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
		
		try{
			//opening the file//
			
			//getting the parent directory of the file to write
			File parentDirectory=new File(new URI(
					actionElement.getAttribute(directoryAttributeName)));
			
			if(! parentDirectory.exists()){
				
				parentDirectory.mkdirs();
			}
			
			//creating the file to write
			File file=new File(parentDirectory, getFileName());
			file.createNewFile();

			//getting the output stream for this file
			FileOutputStream out=new FileOutputStream(file, writeOnSameFile);
			
			//getting the channel
			FileChannel channel=out.getChannel();

			//filling the buffer//
			StringBuilder buffer=new StringBuilder();
			ByteBuffer byteBuffer=null;
			
			//getting the separator between the fields
			String sep=actionElement.getAttribute(sepBetweenFieldsAttributeName);
			
			//getting the line separator
			String lineSep=System.getProperty("line.separator");

			if(displayColumnNames){
				
				//creating the line of the column names
				Object[] colNames=data.getFirst();

				for(int i=0; i<colNames.length; i++){

					buffer.append((String)colNames[i]+sep);
				}
				
				buffer.append(lineSep);
				byteBuffer=ByteBuffer.wrap(buffer.toString().getBytes("UTF-8"));
				channel.write(byteBuffer);
				byteBuffer.clear();
			}
			
			data.removeFirst();
			
			//adding the content
			if(data.size()>0){

				for(Object[] array : data){
					
					buffer=new StringBuilder();
					
					for(Object obj : array){
						
						if(obj==null){
							
							obj="";
						}

    					if(obj instanceof Timestamp){
    						
        					//getting the date format string
    						obj=dateTimeFormat.format((Date)obj);
    						
    					}else if(obj instanceof Date){
    						
        					//getting the date format string
    						obj=dateFormat.format((Date)obj);
    					}
    					
						buffer.append(obj+sep);
					}
					
					buffer.append(lineSep);
					byteBuffer=ByteBuffer.wrap(buffer.toString().getBytes("UTF-8"));
					channel.write(byteBuffer);
					byteBuffer.clear();
				}
			}
			
			out.flush();
			out.close();
			channel.close();
			
		}catch (Exception ex){ex.printStackTrace();}
	}
	
	/**
	 * @return the file name for the current action
	 */
	protected String getFileName(){
		
		String fileName=actionElement.getAttribute(fileNamePatternAttributeName).trim();
		
		if(! writeOnSameFile && fileName.length()>0){
			
			try{
				fileName=fileName.substring(0, fileName.length()-descriptionTypeSuffix.length());
			}catch (Exception ex){}
			
			if(fileName.startsWith(regularTypePrefix)){
				
				fileName=fileName.substring(regularTypePrefix.length(), fileName.length());
				
			}else if(fileName.startsWith(dateTypePrefix)){
				
				fileName=fileName.substring(dateTypePrefix.length(), fileName.length());
				
				if(customDateFormat==null){
					
					customDateFormat=new SimpleDateFormat(fileName);
				}
				
				//getting the current name
				Date currentDate=new Date(System.currentTimeMillis());
				fileName=customDateFormat.format(currentDate);
			}
		}
		
		return fileName;
	}

	/**
	 * the class used to load data from a db
	 * @author Jordi SUC
	 */
	protected class LoadDataToWriteDataBaseAccess extends DataBaseAccess{

		/**
		 * the constructor of the class
		 * @param picture a svg picture
		 * @param dbName the data base name
		 */
		public LoadDataToWriteDataBaseAccess(
				SVGPicture picture, String dbName){

			super(picture, dbName, "");
		}

		/**
		 * @return the sql request
		 */
		public String getRequest(){
			
			String request=actionElement.getAttribute(requestAttributeName);
	    	request=request.replaceAll(Toolkit.separatorBrRegex, " ");
			return request;
		}

		@Override
		protected void receiveData(LinkedList<Object[]> data) {

			AbstractWriteDataToFile.this.receiveData(data);
		}
	}
}
