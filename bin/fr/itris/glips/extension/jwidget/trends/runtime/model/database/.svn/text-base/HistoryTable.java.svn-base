package fr.itris.glips.extension.jwidget.trends.runtime.model.database;

import java.util.*;

/**
 * the class providing information on a table in a data base
 * @author ITRIS, Jordi SUC
 */
public class HistoryTable {

	/**
	 * the history manager that contains this table
	 */
	private HistoryManager historyManager;
	
	/**
	 * the id of the table
	 */
	private String id="";
	
	/**
	 * the history fields set
	 */
	private Set<HistoryField> historyFields=new HashSet<HistoryField>();
	
	/**
	 * the constructor of the class
	 * @param historyManager the history manager
	 * @param id an id
	 */
	public HistoryTable(HistoryManager historyManager, String id){
		
		this.historyManager=historyManager;
		this.id=id;
		
		historyManager.recordHistoryTable(this);
	}

	/**
	 * @return the history manager
	 */
	public HistoryManager getHistoryManager() {
		return historyManager;
	}

	/**
	 * @return the id of the table
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * records a new history field in this table
	 * @param historyField a new history field
	 */
	public void recordField(HistoryField historyField){
		
		if(historyField!=null){
			
			historyFields.add(historyField);
		}
	}
	
	/**
	 * @return the history fields set
	 */
	public Set<HistoryField> getHistoryFields() {
		return historyFields;
	}
}
