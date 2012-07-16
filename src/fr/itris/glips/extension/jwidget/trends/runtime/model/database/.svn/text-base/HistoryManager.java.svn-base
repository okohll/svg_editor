package fr.itris.glips.extension.jwidget.trends.runtime.model.database;

import java.sql.*;
import java.util.*;

/**
 * the class providing information on a database
 * @author ITRIS, Jordi SUC
 */
public class HistoryManager {

	/**
	 * the id of the history manager
	 */
	private String id="";
	
	/**
	 * the subprotocol
	 */
	private String subProtocol;
	
	/**
	 * the data base name
	 */
	private String dbName;
	
	/**
	 * the host
	 */
	private String host;
	
	/**
	 * the user
	 */
	private String user;
	
	/**
	 * the password
	 */
	private String password;
	
	/**
	 * the set of the history tables
	 */
	private Set<HistoryTable> historyTablesSet=new HashSet<HistoryTable>();
	
	/**
	 * the constructor of the class
	 * @param id the id of the history manager
	 * @param driverName the driver class name
	 * @param subProtocol the subprotocol
	 * @param host the host for the data base url
	 * @param dbName the data base name
	 * @param user the user
	 * @param password the password
	 */
	public HistoryManager(String id, String driverName, String subProtocol, String host, 
										String dbName, String user, String password){

		this.id=id;
		this.subProtocol=subProtocol;
		this.host=host;
		this.dbName=dbName;
		this.user=user;
		this.password=password;
		
		//normalizing the values
		if(! host.endsWith("/")){
			
			host+="/";
		}
		
		//registering the data base driver
		try{
			Class.forName(driverName).newInstance();
		}catch (Exception ex){ex.printStackTrace();}
	}

	/**
	 * @return the id of the history manager
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return the data base name
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * returns the history table corresponding to the given id
	 * @param tableId the id of a history table
	 * @return the history table corresponding to the given id
	 */
	public HistoryTable getHistoryTable(String tableId){

		for(HistoryTable historyTable : historyTablesSet){
			
			if(historyTable.getId().equals(tableId)){
				
				return historyTable;
			}
		}
		
		return null;
	}
	
	/**
	 * @return the set of the history tables
	 */
	public Set<HistoryTable> getHistoryTables() {
		return historyTablesSet;
	}
	
	/**
	 * records a history table
	 * @param table a history table
	 */
	public void recordHistoryTable(HistoryTable table){
		
		if(table!=null){
			
			historyTablesSet.add(table);
		}
	}
	
	/**
	 * @return opens and returns a new connection
	 */
	public Connection openConnection(){
		
    	Connection connection=null;
    	
    	try{
    		String url="jdbc:"+subProtocol+"://"+host+"/"+dbName;
        	connection=DriverManager.getConnection(url, user, password);
    	}catch (Exception ex){ex.printStackTrace();}

		return connection;
	}
}
