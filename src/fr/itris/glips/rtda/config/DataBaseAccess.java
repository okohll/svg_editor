package fr.itris.glips.rtda.config;

import java.sql.*;
import java.util.*;
import java.util.Timer;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class used to access the database
 * @author Jordi SUC
 */
public class DataBaseAccess {
	
	/**
	 * constants
	 */
	protected static final String idFieldName="id";
	
	/**
	 * the timer
	 */
	protected static final Timer timer=new Timer();
	
	/**
	 * the svg picture
	 */
	protected SVGPicture picture;

	/**
	 * the subprotocol
	 */
	protected String subProtocol;
	
	/**
	 * the data base name
	 */
	protected String dbName;
	
	/**
	 * the table name
	 */
	protected String tableName;
	
	/**
	 * the host
	 */
	protected String host;
	
	/**
	 * the user
	 */
	protected String user;
	
	/**
	 * the password
	 */
	protected String password;
	
	/**
	 * the constructor of the class
	 * @param picture a svg picture
	 * @param dbName the data base name
	 * @param tableName the table name
	 */
	public DataBaseAccess(SVGPicture picture, 
			String dbName, String tableName){
		
		this.picture=picture;
		this.dbName=dbName;
		this.tableName=tableName;

		//getting the xml data base root element corresponding to this element
		ConfigurationDocument confDoc=
			picture.getMainDisplay().getPictureManager().
				getConfigurationDocument(picture);

		if(confDoc!=null && confDoc.getRootElement()!=null){
			
			//getting the history manager elements in the xml data base
			NodeList historyManagerElements=
				confDoc.getRootElement().getElementsByTagName(
						Toolkit.historyManagerAttName);
			
			if(historyManagerElements!=null){
				
				//getting the history manager element corresponding to the provided db name
				Element dbElement=null;
				Element el=null;
				
				for(int i=0; i<historyManagerElements.getLength(); i++){
					
					el=(Element)historyManagerElements.item(i);

					if(el.getAttribute(Toolkit.dbNameAttName).equals(dbName)){

						dbElement=el;
						break;
					}
				}
				
				if(dbElement!=null){
					
					//getting the parameters for the db connection
					subProtocol=dbElement.getAttribute(Toolkit.subProtocolAttName);
					host=dbElement.getAttribute(Toolkit.hostAttName);
					user=dbElement.getAttribute(Toolkit.userAttName);
					password=dbElement.getAttribute(Toolkit.passwordAttName);

					//normalizing the values
					if(host.endsWith("/")){
						
						host=host.substring(0, host.length()-1);
					}
					
					//loading the driver
					try{
						Class.forName(dbElement.getAttribute(
								Toolkit.driverNameAttName)).newInstance();
					}catch (Exception ex){ex.printStackTrace();}
				}
			}
		}
	}
	
	/**
	 * @return the table name
	 */
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * @return opens and returns a new connection
	 */
	protected Connection openConnection(){
		
    	Connection connection=null;
    	
    	try{
    		String url="jdbc:"+subProtocol+"://"+host+"/"+dbName;
        	connection=DriverManager.getConnection(url, user, password);
    	}catch (Exception ex){ex.printStackTrace();}

		return connection;
	}
	
	/** 
	 * method used for receiving data from the db
	 * @param data the data found in the db
	 */
	protected void receiveData(final LinkedList<Object[]> data){}
	
	/**
	 * retrieves all the data of the table of the db
	 * @param request the sql request
	 */
	public void retrieveData(final String request){

		TimerTask task=new TimerTask(){
			
			@Override
			public void run() {

				final LinkedList<Object[]> data=new LinkedList<Object[]>();
				
				//opening the connection
				Connection connection=openConnection();
				Statement statement=null;
				
				try{
					
					ResultSet resultSet=null;

					if(request.toLowerCase().indexOf("{call")!=-1){

						statement=configureCallableRequest(connection, request);
						resultSet=((CallableStatement)statement).executeQuery();			
						
					}else{
						
						statement=connection.createStatement();
						resultSet=statement.executeQuery(request);
					}

					//creating the array of the column names
					ResultSetMetaData metaData=resultSet.getMetaData();
					int columnNb=metaData.getColumnCount();
					Object[] colArray=new Object[columnNb];
					
					for(int i=0; i<columnNb; i++){

						colArray[i]=metaData.getColumnName(i+1);
					}

					data.add(colArray);
					boolean keepWorking=true;
					Object[] rowArray=null;
					
					//getting the data
					while(true){
						
						keepWorking=resultSet.next();
						
						if(! keepWorking){
							
							break;
						}
						
						rowArray=new Object[columnNb];
						
						for(int i=0; i<columnNb; i++){
							
							rowArray[i]=resultSet.getObject(i+1);
						}

						data.add(rowArray);
					}
					
					SwingUtilities.invokeLater(new Runnable(){
						
						public void run() {

							receiveData(data);
						}
					});
					
				}catch (Exception ex){ex.printStackTrace();}
				
				try{
					if(statement!=null){
						
						statement.close();
					}
				}catch (Exception ex){ex.printStackTrace();}
				
				try{
					connection.close();
				}catch (Exception ex){ex.printStackTrace();}
			}
		};
		
		timer.schedule(task, 0);
	}
	
	/**
	 * records the data provided in the map
	 * @param fieldNameToValue the map associating a field name 
	 * in the table to its value for the row
	 */
	public void recordData(final Map<String, Object> fieldNameToValue){

		if(fieldNameToValue!=null && fieldNameToValue.size()>0){
			
			//checks if the table of the request contains an ID field
			final boolean containsIdField=fieldNameToValue.containsKey(idFieldName);
			
			//getting the id field value
			final Object idFieldValue=fieldNameToValue.get(idFieldName);

			if(idFieldValue!=null || ! containsIdField){
				
				TimerTask task=new TimerTask(){
					
					@Override
					public void run() {

						//opening the connection
						Connection connection=openConnection();
						
						try{
							connection.setAutoCommit(false);
						}catch (Exception ex){}
						
						Statement statement=null;
						
						boolean doUpdate=containsIdField;
						
						if(doUpdate && idFieldValue!=null){
							
							//getting the number of rows in the table that have whose id value is the current one
							String countRequest="SELECT "+idFieldName+" FROM "+tableName+
								" WHERE "+idFieldName+"=\'"+idFieldValue.toString()+"\'";
							
							try{
								//executing the request
								statement=connection.createStatement();
								ResultSet resultSet=statement.executeQuery(countRequest);
								connection.commit();
								doUpdate=resultSet!=null && resultSet.next();
							}catch (Exception ex){
								
								doUpdate=false;
							}
						}
						
						try{

							if(doUpdate && idFieldValue!=null){
								
								//removing the id field name
								fieldNameToValue.remove(idFieldName);
								
								//an update query will be done
								//creating the request
								String request="UPDATE "+tableName+" SET";
								String value="";
								Object objVal=null;
								
								for(String fieldName : fieldNameToValue.keySet()){
									
									objVal=fieldNameToValue.get(fieldName);
									
									if(objVal!=null){
										
										value=objVal.toString();
										request+=" "+fieldName+" = "+"\'"+value+"\',";
									}
								}
								
								if(request.endsWith(",")){
									
									request=request.substring(0, request.length()-1);
								}
								
								request+=" WHERE "+idFieldName+" = \'"+idFieldValue.toString()+"\'";
								
								//executing the request
								statement=connection.createStatement();
								statement.executeUpdate(request);
								
							}else{
								
								//an insert query will be done
								String request="INSERT INTO "+tableName+" (";
								String value="";
								Object objVal=null;
								
								for(String fieldName : fieldNameToValue.keySet()){
									
									request+=" "+fieldName+",";
								}
								
								if(request.endsWith(",")){
									
									request=request.substring(0, request.length()-1);
								}
								
								request+=") VALUES ( ";
								
								for(String fieldName : fieldNameToValue.keySet()){
									
									objVal=fieldNameToValue.get(fieldName);
									
									if(objVal!=null){
										
										value=objVal.toString();
										request+=" \'"+value+"\',";
									}
								}
								
								if(request.endsWith(",")){
									
									request=request.substring(0, request.length()-1);
								}
								
								request+=")";
								
								//executing the request
								statement=connection.createStatement();
								statement.executeUpdate(request);
							}
							
						}catch (Exception ex){ex.printStackTrace();}
						
						try{
							if(statement!=null){
								
								statement.close();
							}
						}catch (Exception ex){ex.printStackTrace();}
						
						try{
							connection.close();
						}catch (Exception ex){ex.printStackTrace();}
					}
				};
				
				timer.schedule(task, 0);
			}
		}
	}
	
	/**
	 * configures the provided callable request and creates 
	 * the associated callable statement
	 * @param request a request
	 * @param connection a connection
	 * @return the created callable statement
	 */
	protected CallableStatement configureCallableRequest(
			Connection connection, String request){
		
		//the statement to return
		CallableStatement statement=null;
		
		//creating the params and types list
		LinkedList<String> params=new LinkedList<String>();
		LinkedList<String> types=new LinkedList<String>();
		
		//getting the start and end parts of the request
		String startPart="", endPart="", newParams="";
		
		//getting the list of the parameters in the request
		int pos=request.indexOf("(");
		
		if(pos!=-1 && pos+1<request.length()){
			
			String paramStr=request.substring(pos+1, request.length());
			startPart=request.substring(0, pos+1);
			
			int pos2=paramStr.indexOf(")");
			
			if(pos2!=-1){
				
				endPart=paramStr.substring(pos2, paramStr.length());
				paramStr=paramStr.substring(0, pos2).trim();
				
				if(! paramStr.equals("")){
					
					//parsing the string of the parameters
					String[] splitParams=paramStr.split(",");
					String value="", type="";
					
					for(int i=0; i+1<splitParams.length; i+=2){
						
						value=splitParams[i].trim();
						value=value.replaceAll("'", "");
						type=splitParams[i+1].trim();
						params.add(value);
						types.add(type);
						newParams+="?, ";
					}
					
					if(newParams.endsWith("?, ")){
						
						newParams=newParams.substring(0, newParams.length()-2);
					}
				}
			}
		}
		
		try{
			if(params.size()>0 && types.size()>0){
				
				//rebuilding the request
				request=startPart+newParams+endPart;

				//creating the statement
				statement=connection.prepareCall(request);
				
				//setting the in parameters
				String param="", type="";
				
				for(int i=0; i<params.size(); i++){

					param=params.get(i);
					type=types.get(i);
					setParameter(statement, param, type, i+1);
				}
			}
		}catch (Exception ex){ex.printStackTrace();}
		
		return statement;
	}
	
	/**
	 * sets the parameter given by its value and its type
	 * @param statement  the callable statement to configure
	 * @param value the value of the parameter
	 * @param type the type of the parameter
	 * @param index the index of a parameter
	 */
	protected void setParameter(CallableStatement statement, String value, String type, int index){
		
		try{
			
			type=type.toUpperCase();
			
			if(type.equals("BOOLEAN") || type.equals("BIT")){
				
				boolean bVal=value.equals("1");
				statement.setBoolean(index, bVal);
				
			}else if(type.equals("SMALLINT")){

				short sVal=Short.parseShort(value);
				statement.setShort(index, sVal);
				
			}else if(type.equals("INTEGER")){
				
				int iVal=Integer.parseInt(value);
				statement.setInt(index, iVal);

			}else if(type.equals("BIGINT")){
				
				long lVal=Long.parseLong(value);
				statement.setLong(index, lVal);
				
			}else if(type.equals("REAL")){
				
				float fVal=Float.parseFloat(value);
				statement.setFloat(index, fVal);
				
			}else if(type.equals("DOUBLE")){
				
				double dVal=Double.parseDouble(value);
				statement.setDouble(index, dVal);
				
			}else if(type.equals("VARCHAR") || type.equals("LONGVARCHAR")){
				
				statement.setString(index, value);
			}
		}catch (Exception ex){}                    
	}
	
}
