package fr.itris.glips.extension.jwidget.externaldatatable.runtime;

import java.util.*;
import fr.itris.glips.rtda.config.*;

/**
 * the class storing information on a data base and a table to access 
 * @author Jordi SUC
 */
public class ExternalDataTableDataBaseAccess extends DataBaseAccess{
	
	/**
	 * the jwidget runtime object
	 */
	private ExternalDataTableRuntime jwidgetRuntime;
	
	/**
	 * the request that should be executed
	 */
	private String request;

	/**
	 * the constructor of the class
	 * @param jwidgetRuntime the jwidget runtime
	 * @param dbName the data base name
	 * @param tableName the table name
	 * @param request the request
	 */
	public ExternalDataTableDataBaseAccess(ExternalDataTableRuntime jwidgetRuntime, 
			String dbName, String tableName, String request){
		
		super(jwidgetRuntime.getPicture(), dbName, tableName);
		this.jwidgetRuntime=jwidgetRuntime;
		this.request=request;
	}
	
	/**
	 * @return the request
	 */
	public String getRequest(){
		
		String theRequest="";
		
		if(request==null || request.equals("")){
			
			theRequest="SELECT * FROM "+getTableName();
			
		}else{
			
			theRequest=request;
		}
		
		return theRequest;
	}
	
	@Override
	protected void receiveData(LinkedList<Object[]> data) {

		jwidgetRuntime.receiveData(data);
	}
}
