package fr.itris.glips.extension.jwidget.trends.runtime.model;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.controller.*;
import fr.itris.glips.extension.jwidget.trends.runtime.model.buffer.*;
import fr.itris.glips.extension.jwidget.trends.runtime.model.database.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.widgets.WaitDialog;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.config.*;

/**
 * the class of the model of the trend widget
 * @author ITRIS, Jordi SUC
 */
public class TrendsRuntimeModel {
	
	/**
	 * some labels
	 */
	protected static String receivingValuesLabel, normalizingValuesLabel, refreshingDisplayLabel;
	
	static{
		
		receivingValuesLabel=TrendsBundle.bundle.getString("receivingData");
		normalizingValuesLabel=TrendsBundle.bundle.getString("normalizingValues");
		refreshingDisplayLabel=TrendsBundle.bundle.getString("refreshingDisplay");
	}
	
	/**
	 * the controller of the trends widget
	 */
	private TrendsRuntimeController controller;
	
	/**
	 * the map associating a tag name to a real time curve buffer
	 */
	private Map<String, CurveBuffer> rtBuffers=new HashMap<String, CurveBuffer>();
	
	/**
	 * the map associating a tag name to a history curve buffer
	 */
	private Map<String, CurveBuffer> historyBuffers=new HashMap<String, CurveBuffer>();
	
	/**
	 * the set of the history managers
	 */
	private Set<HistoryManager> historyManagersSet=new HashSet<HistoryManager>(); 

	/**
	 * the date before which no tag values can be displayed for the real time mode
	 */
	private long firstRTDate=0;
	
	/**
	 * the date after which no tag values can be displayed for the real time mode
	 */
	private long lastRTDate=0;
	
	/**
	 * the date before which no tag values can be displayed for the history mode
	 */
	private long firstHistoryDate=0;
	
	/**
	 * the date after which no tag values can be displayed for the history mode
	 */
	private long lastHistoryDate=0;
	
	/**
	 * whether the db request has been cancelled
	 */
	private boolean dbRequestCancelled=false;
	
	/**
	 * the constructor of the class
	 * @param controller the controller of the trends widget
	 */
	public TrendsRuntimeModel(TrendsRuntimeController controller){
		
		this.controller=controller;
	}
	
	/**
	 * initializing the model
	 */
	public void initialize(){
		
		//handling the history data base
		createDataBaseConfig();
		
		//setting the first current date
		firstRTDate=System.currentTimeMillis();
		lastRTDate=firstRTDate+1;
		
		//creating the buffers
		CurveBuffer rtBuffer=null, historyBuffer=null;
		
		for(TrendsCurveConfiguration curveConfiguration : 
				controller.getConfiguration().getCurvesConfigurationList()){
			
			if(curveConfiguration.isEnumeratedTag()){
				
				rtBuffer=new StringCurveBuffer(curveConfiguration.getTagName(),
					curveConfiguration.isEnumeratedTag(),
					controller.getConfiguration().getBufferPointsNumber());
				
				historyBuffer=new StringCurveBuffer(curveConfiguration.getTagName(),
					curveConfiguration.isEnumeratedTag(),
					controller.getConfiguration().getBufferPointsNumber());
				
			}else{
				
				rtBuffer=new DoubleCurveBuffer(curveConfiguration.getTagName(),
					curveConfiguration.isEnumeratedTag(),
					controller.getConfiguration().getBufferPointsNumber());
				
				historyBuffer=new DoubleCurveBuffer(curveConfiguration.getTagName(),
					curveConfiguration.isEnumeratedTag(),
					controller.getConfiguration().getBufferPointsNumber());
			}
			
			//putting the new buffers into the maps
			rtBuffers.put(rtBuffer.getTagName(), rtBuffer);
			historyBuffers.put(historyBuffer.getTagName(), historyBuffer);
		}
		
		//adding a listener to the modifications of the mode
		controller.getConfiguration().addListener(new ConfigurationChangeListener(){
			
			@Override
			public void modeOrSubModeChanged() {
				
				if(controller.getConfiguration().getCurrentMode()==
						TrendsConfiguration.REAL_TIME_MODE){
					
					//clearing the buffers of the history mode
					for(CurveBuffer buffer : historyBuffers.values()){
						
						buffer.reset();
					}
				}
			}
		});
	}
	
	/**
	 * disposes the model
	 */
	public void dispose(){//TODO

		//clearing the buffers of the history mode
		for(CurveBuffer buffer : historyBuffers.values()){
			
			buffer.reset();
		}
		
		//clearing the buffers of the real-time mode
		for(CurveBuffer buffer : rtBuffers.values()){
			
			buffer.reset();
		}
		
		controller=null;
		rtBuffers=null;
		historyBuffers=null;
		historyManagersSet=null;
	}

	/**
	 * @return the date before which no tag values can be displayed
	 */
	public long getFirstDate() {
		
		long currentFirstDate=0;
		
		//getting the first date
		if(controller.getConfiguration().getCurrentMode()==
			TrendsConfiguration.REAL_TIME_MODE){
			
			currentFirstDate=firstRTDate;

		}else{
			
			currentFirstDate=firstHistoryDate;
		}
		
		return currentFirstDate;
	}

	/**
	 * @return the date after which no tag values can be displayed
	 */
	public long getLastDate() {

		long currentLastDate=0;
		
		//getting the first date
		if(controller.getConfiguration().getCurrentMode()==
			TrendsConfiguration.REAL_TIME_MODE){
			
			currentLastDate=lastRTDate;

		}else{
			
			currentLastDate=lastHistoryDate;
		}
		
		return currentLastDate;
	}
	
	/**
	 * sets the date before which no tag values can be displayed
	 * @param firstDate the date before which no tag values can be displayed
	 */
	public synchronized void setFirstDate(long firstDate) {

		if(controller.getConfiguration().getCurrentMode()==
			TrendsConfiguration.HISTORY_MODE){
			
			this.firstHistoryDate=firstDate;
		}
	}

	/**
	 * sets the date after which no tag values can be displayed
	 * @param lastDate the date after which no tag values can be displayed
	 */
	public synchronized void setLastDate(long lastDate) {

		if(controller.getConfiguration().getCurrentMode()==
			TrendsConfiguration.REAL_TIME_MODE){
			
			if(lastRTDate<lastDate){
				
				lastRTDate=lastDate;
			}
			
			//checking the first date
			if(lastRTDate-firstRTDate>controller.getConfiguration().getMaxDisplayableTime()){
				
				firstRTDate=lastRTDate-controller.getConfiguration().getMaxDisplayableTime();
			}
			
		}else{
			
			this.lastHistoryDate=lastDate;
		}
	}

	/**
	 * updates the value of each tag 
	 * @param dataMap the map associating a tag name to its new value
	 */
	public void updateTags(Map<String, Object> dataMap){

		AnimationsHandler handler=controller.getJwidgetRuntime().
			getPicture().getMainDisplay().getAnimationsHandler();
		
		//checking if the new values should be stored
		if(controller.getConfiguration().getCurrentMode()==
			TrendsConfiguration.HISTORY_MODE){

			String startDateTag=controller.getConfiguration().getStartDateTag();
			String endDateTag=controller.getConfiguration().getEndDateTag();
			
			if(dataMap.containsKey(startDateTag) || 
					dataMap.containsKey(endDateTag)){
				
				//getting the start and end dates
				long startDate=-1, endDate=-1;
				
				try{
					startDate=Long.parseLong(
							handler.getData(startDateTag).toString());
				}catch (Exception ex){}
				
				try{
					endDate=Long.parseLong(
							handler.getData(endDateTag).toString());
				}catch (Exception ex){}
				
				if(startDate!=-1 && endDate!=-1 && 
					(startDate!=firstHistoryDate || endDate!=lastHistoryDate)){

					requestFromDataBase(startDate, endDate);
				}
			}
		}
		
		if((controller.getConfiguration().getCurrentMode()==
			TrendsConfiguration.HISTORY_MODE && 
				controller.getConfiguration().keepTagRecordInHistory()) || 
					controller.getConfiguration().getCurrentMode()==
						TrendsConfiguration.REAL_TIME_MODE){
			
			//updating each tag value provided by the given data map
			CurveBuffer buffer=null;
			
			for(String tagName : dataMap.keySet()){
				
				buffer=rtBuffers.get(tagName);
				
				if(buffer!=null){
					
					buffer.addTagValue(handler.getData(tagName));
				}
			}
		}
	}
	
	/**
	 * returns all the values that have been recorded by the model from the given start date
	 * to the given end date.Tthe value corresponding to the date should be in the buffer, 
	 * no values will be gotten from the history
	 * @param tagName the name of a tag
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return all the values that have been recorded by the model from the given start date
	 * to the given end date
	 */
	public TreeMap<Long, Object> getValues(String tagName, long startDate, long endDate){
		
		TreeMap<Long, Object> valuesMap=null;
		
		//getting the buffer corresponding to the given tag name
		CurveBuffer buffer=null;
		
		if(controller.getConfiguration().getCurrentMode()==
			TrendsConfiguration.REAL_TIME_MODE){
			
			buffer=rtBuffers.get(tagName);
			
		}else{
			
			buffer=historyBuffers.get(tagName);
		}

		if(buffer!=null){
			
			if(startDate==-1){
				
				valuesMap=buffer.getBufferPart(buffer.getFirstRecordedDate());
				
			}else{
				
				valuesMap=buffer.getBufferPart(startDate, endDate);
			}
		}

		return valuesMap;
	}
	
	/**
	 * requests the tag value corresponding to the tag name and that 
	 * is the closest value corresponding to the horodate and that occured before this horodate
	 * @param tagName a tag name
	 * @param horodate a horodate
	 */
	public void requestTagValue(final String tagName, final long horodate){
		
		Thread thread=new Thread(){
			
			@Override
			public void run() {
				
				Object value=null;
				
				//getting the buffer corresponding to the given tag name
				CurveBuffer buffer=null;
				
				if(controller.getConfiguration().getCurrentMode()==
					TrendsConfiguration.REAL_TIME_MODE){
					
					buffer=rtBuffers.get(tagName);
					
				}else{
					
					buffer=historyBuffers.get(tagName);
				}

				if(buffer!=null){
					
					value=buffer.getTagValue(horodate);
				}
				
				if(value!=null){
					
					final Object fvalue=value;

					SwingUtilities.invokeLater(new Runnable(){
						
						public void run() {
							
							controller.getView().setTagValue(tagName, fvalue);
						}
					});
				}
			}
		};
		
		thread.start();
	}

	/**
	 * @return dbRequestCancelled
	 */
	public boolean isDbRequestCancelled() {
		return dbRequestCancelled;
	}

	/**
	 * sets whether the data base request has been cancelled
	 * @param dbRequestCancelled whether the data base request has been cancelled
	 */
	public synchronized void setDbRequestCancelled(boolean dbRequestCancelled) {
		
		this.dbRequestCancelled=dbRequestCancelled;
	}
	
	/**
	 * notifies that the data base request has failed
	 */
	public void notifyDateBaseRequestFailed() {
		
		//provides the user an error info
		try {
			SwingUtilities.invokeAndWait(new Runnable(){
				
				public void run() {

					controller.getView().getComponentsHandler().getWaitDialog().setInError();
				}
			});
		}catch (Exception ex){ex.printStackTrace();}
		
		//cancels any left request
		setDbRequestCancelled(true);
	}

	/**
	 * stores all the values of the tags that were recorded in the data base 
	 * from the <code>startDate</code> to the <code>endDate</code>
	 * @param startDate the start date
	 * @param endDate the end date
	 */
	public void requestFromDataBase(final long startDate, final long endDate){
		
		//reinitializing the cancel action boolean
		setDbRequestCancelled(false);
		
		//setting the first and last dates
		synchronized(this){
			
			firstHistoryDate=startDate;
			lastHistoryDate=endDate;
		}

		Thread thread=new Thread(){
			
			/**
			 * the set of the request threads
			 */
			private Set<DataBaseRequest> dataBaseRequestsSet=
							new HashSet<DataBaseRequest>();
			
			@Override
			public void run() {
				
				//getting the wait dialog
				WaitDialog waitDialog=
					controller.getView().getComponentsHandler().getWaitDialog();
				waitDialog.setProgressMessageInAwtThread(receivingValuesLabel, 0, 0, 100);
				
				DataBaseRequest dataBaseRequest;

				//creating the data base request thread for each table of each history manager
				for(HistoryManager historyManager : historyManagersSet){
					
					if(historyManager!=null && historyManager.getHistoryTables().size()>0){
						
						for(HistoryTable historyTable : historyManager.getHistoryTables()){

							//creating the data base request thread
							dataBaseRequest=new DataBaseRequest(
									historyTable.getHistoryFields(), startDate, endDate);
							dataBaseRequestsSet.add(dataBaseRequest);
							
							//starting the thread
							dataBaseRequest.start();
						}
					}
				}
				
				//the number of the requests
				int requestsNumber=historyManagersSet.size();
				int nb=0;

				//waiting for all the requests to be done
				while(! requestsDone()){

					if(dbRequestCancelled){
						
						//closing each db connection
						for(DataBaseRequest request : dataBaseRequestsSet){
							
							request.closeConnection();
						}

						return;
						
					}else if(! valuesReceived()){
						
						//getting the number of threads that have received their values
						nb=0;
						
						for(DataBaseRequest request : dataBaseRequestsSet){
							
							if(request.isValuesReturned()){
								
								nb++;
							}
						}
						
						waitDialog.setProgressMessageInAwtThread(
							receivingValuesLabel, 50*nb/requestsNumber, 0, 100);
						
					}else{
						
						//getting the number of threads that are done
						nb=0;
						
						for(DataBaseRequest request : dataBaseRequestsSet){
							
							if(request.isDone()){
								
								nb++;
							}
						}
						
						waitDialog.setProgressMessageInAwtThread(
							normalizingValuesLabel, 50+30*nb/requestsNumber, 0, 100);
					}
					
					try{sleep(250);}catch (Exception ex){}
				}
				
				waitDialog.setProgressMessageInAwtThread(
						normalizingValuesLabel, 80, 0, 100);
				
				//clearing the history buffers
				for(CurveBuffer buffer :  historyBuffers.values()){
					
					buffer.reset();
				}
				
				//filling the buffer with the found values
				Map<String, TreeMap<Long, Object>> tagNameToValues;
				TreeMap<Long, Object> values;
				CurveBuffer buffer;
				
				for(DataBaseRequest dbRequest : dataBaseRequestsSet){
					
					if(dbRequestCancelled){return;}
					
					//getting the map associating a tag name to a values map
					tagNameToValues=dbRequest.getValues();
					
					if(tagNameToValues!=null && tagNameToValues.size()>0){
						
						for(String tagName : tagNameToValues.keySet()){
							
							//getting the corresponding values map 
							values=tagNameToValues.get(tagName);
							
							//getting the buffer corresponding to the tag name
							buffer=historyBuffers.get(tagName);

							if(buffer!=null && values!=null && values.size()>0){
								
								//filling the buffer with the new values
								buffer.addTagValues(values);
							}
						}
					}
				}
				
				if(dbRequestCancelled){return;}
				
				waitDialog.setProgressMessageInAwtThread(refreshingDisplayLabel, 90, 0, 100);
				
				//notifying the view that new history values are available for display
				controller.getView().refreshDisplay(startDate, 
						startDate+controller.getConfiguration().getHorizontalAxisDuration());

				//hiding the wait dialog
				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {
	
						controller.getView().getComponentsHandler().
							getWaitDialog().setVisible(false);
					}
				});
			}
			
			/**
			 * checks whether all the requests are done
			 */
			protected boolean requestsDone(){

				for(DataBaseRequest request : dataBaseRequestsSet){
					
					if(request!=null && ! request.isDone()){
						
						return false;
					}
				}

				return true;
			}
			
			/**
			 * checks whether all the values have been received
			 */
			protected boolean valuesReceived(){

				for(DataBaseRequest request : dataBaseRequestsSet){
					
					if(request!=null && ! request.isValuesReturned()){
						
						return false;
					}
				}

				return true;
			}
		};
		
		thread.start();
	}
	
	/**
	 * creates the data base configuration
	 */
	protected void createDataBaseConfig(){
		
		ConfigurationDocument confDoc=
			controller.getConfiguration().getConfigurationDocument();
		
		if(confDoc!=null && confDoc.getRootElement()!=null){
			
			//getting the history manager elements in the xml data base
			NodeList historyManagerElements=
				confDoc.getRootElement().getElementsByTagName(Toolkit.historyManagerAttName);
			
			if(historyManagerElements!=null){
				
				//creating a history manager for each node and its contained tables
				Node node;
				NodeList tableNodes;
				Element histEl, tableEl;
				String tableName="";
				HistoryManager historyManager;
				
				for(int i=0; i<historyManagerElements.getLength(); i++){
					
					node=historyManagerElements.item(i);
					
					if(node!=null && node instanceof Element){
						
						histEl=(Element)node;
						
						//creating the history manager object
						historyManager=new HistoryManager(histEl.getAttribute(Toolkit.idAttName),
								histEl.getAttribute(Toolkit.driverNameAttName), histEl.getAttribute(Toolkit.subProtocolAttName),
								histEl.getAttribute(Toolkit.hostAttName), histEl.getAttribute(Toolkit.dbNameAttName), 
								histEl.getAttribute(Toolkit.userAttName), histEl.getAttribute(Toolkit.passwordAttName));
						historyManagersSet.add(historyManager);
						
						//getting all the table child nodes of this history manager element
						tableNodes=histEl.getElementsByTagName(Toolkit.historyTableAttName);
						
						if(tableNodes!=null){
							
							//creating the history table objects corresponding to each node
							//and registering them
							for(int j=0; j<tableNodes.getLength(); j++){
								
								node=tableNodes.item(j);
								
								if(node!=null && node instanceof Element){
									
									tableEl=(Element)node;
									tableName=tableEl.getAttribute(Toolkit.idAttName);
									
									if(! tableName.equals("")){
										
										new HistoryTable(historyManager, tableName);
									}
								}
							}
						}
					}
				}
			}
			
			//handling the data base values for each trends curve
			LinkedList<TrendsCurveConfiguration> configs=
				controller.getConfiguration().getCurvesConfigurationList();
			
			for(TrendsCurveConfiguration config : configs){
				
				config.computeDatabaseValues();
			}
		}
	}
	
	/**
	 * returns the history manager corresponding to the given id
	 * @param id the id of a history manager
	 * @return the history manager corresponding to the given id
	 */
	public HistoryManager getHistoryManager(String id){

		for(HistoryManager historyManager : historyManagersSet){
			
			if(historyManager.getId().equals(id)){
				
				return historyManager;
			}
		}
		
		return null;
	}
	
	/**
	 * the class of the data base request
	 * @author ITRIS, Jordi SUC
	 */
	protected class DataBaseRequest extends Thread{
		
		/**
		 * the fields set
		 */
		private Set<HistoryField> fields;
		
		/**
		 * the start and end dates
		 */
		private long startDate=0, endDate=0;
		
		/**
		 * the map into which the returned values should be added
		 */
		private Map<String, TreeMap<Long, Object>> values;
		
		/**
		 * whether the values have been received from the data base
		 */
		private boolean valuesReturned=false;
		
		/**
		 * whether the thread has done
		 */
		private boolean done=false;
		
		/**
		 * the connection that will be used for connecting to the data base
		 */
		private Connection connection=null;
		
		/**
		 * the constructor of the class
		 * @param fields the fields whose values should be requested
		 * @param startDate the start date
		 * @param endDate the end date
		 */
		public DataBaseRequest(Set<HistoryField> fields, long startDate, long endDate){

			this.fields=fields;
			this.startDate=startDate/1000;
			this.endDate=endDate/1000;//TODO converting in seconds
		}
		
		@Override
		public void run() {

			if(fields!=null && fields.size()>0 && ! dbRequestCancelled){
				
				try{
					//getting the first field
					HistoryField firstField=fields.iterator().next();
					connection=firstField.getHistoryTable().getHistoryManager().openConnection();

					if(connection==null){
						
						notifyDateBaseRequestFailed();
						return;
					}

					if(dbRequestCancelled){return;}
					
					Statement statement=connection.createStatement();
					
					//creating the request field
					String request="SELECT TimeStamp, ";
					int i=0;
					
					for(HistoryField field : fields){
						
						request+=field.getFieldName();
						
						if(i!=fields.size()-1){

							request+=",";
						}
						
						request+=" ";
						
						i++;
					}
					
					request+="FROM "+firstField.getHistoryTable().getId()+
						" WHERE TimeStamp >= "+startDate+
							" AND TimeStamp <= "+endDate+" ORDER BY TimeStamp";
					
					if(dbRequestCancelled){return;}

					//executing the query
					ResultSet resultSet=statement.executeQuery(request);
					
					//notifies that the values have been returned
					synchronized (TrendsRuntimeModel.this) {
						valuesReturned=true;
					}

					if(resultSet!=null){
						
						if(dbRequestCancelled){return;}
						
						//getting the number of rows
						boolean state=resultSet.last();
						
						if(state){
							
							//creating the map associating a tag name to the map of the values//
							values=new HashMap<String, TreeMap<Long, Object>>();
							
							//creating the maps corresponding to each field name
							for(HistoryField field : fields){
								
								values.put(field.getTagName(), new TreeMap<Long, Object>());
							}
							
							//getting the number of rows
							int rowsNumber=resultSet.getRow();
								
							//moving to the first row
							state=resultSet.first();
							
							//creating the set of all the row index that should not be handled so that 
							//the number of values in the buffer is the buffer capacity
							Set<Integer> removeIndices=new HashSet<Integer>();
							
							if(rowsNumber>controller.getConfiguration().getBufferPointsNumber()){
								
								//the number of rows to remove
								int nbToRemove=
									rowsNumber-controller.getConfiguration().getBufferPointsNumber();

								int rInt=0;
								Random random=new Random(System.currentTimeMillis());
								Random random2=new Random(System.currentTimeMillis()+500);
								
								for(int j=0; j<nbToRemove; j++){
									
									rInt=random.nextInt(rowsNumber);
									rInt+=random2.nextInt(rowsNumber);
									rInt=rInt/2;
									
									if(! removeIndices.contains(rInt)){
										
										removeIndices.add(rInt);
									}
								}
							}
							
							if(dbRequestCancelled){return;}
							
							//filling the maps of the values
							TreeMap<Long, Object> map=null;
							long horodate=0;
							
							while(state){
								
								if(dbRequestCancelled){return;}
								
								//getting the horodate
								horodate=resultSet.getLong(1);
								horodate=horodate*1000;//converting in ms
								
								//checking if the row should not be handled
								if(removeIndices.size()==0 || (removeIndices.size()!=0 && 
										! removeIndices.contains(resultSet.getRow()))){
									
									//getting the value
									for(HistoryField field : fields){
										
										if(dbRequestCancelled){return;}
										
										map=values.get(field.getTagName());
										
										if(map!=null){
											
											map.put(horodate, resultSet.getObject(field.getFieldName()));
										}
									}
								}

								//next row
								state=resultSet.next();
							}
						}

						resultSet.close();
					}
					
					statement.close();
					connection.close();
				}catch (Exception ex){
					
					notifyDateBaseRequestFailed();
					ex.printStackTrace();
				}
			}

			//notifies that the action is done
			synchronized (TrendsRuntimeModel.this) {
				done=true;
			}
		}
		
		/**
		 * closes the connection
		 */
		public void closeConnection(){
			
			try{
				connection.close();
			}catch (Exception ex){ex.printStackTrace();}
		}
		
		/**
		 * @return whether the values have been received from the data base
		 */
		public boolean isValuesReturned() {
			return valuesReturned;
		}
		
		/**
		 * @return whether the request is done
		 */
		public boolean isDone(){
			
			return done;
		}

		/**
		 * @return the map associating tag name to the 
		 * map associating a horodate to a tag value
		 */
		public Map<String, TreeMap<Long, Object>> getValues() {
			
			return values;
		}
	}
	
}
