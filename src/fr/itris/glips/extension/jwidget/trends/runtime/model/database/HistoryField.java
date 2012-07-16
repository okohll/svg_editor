package fr.itris.glips.extension.jwidget.trends.runtime.model.database;

/**
 * the class of a history field
 */
public class HistoryField {

	/**
	 * a history table
	 */
	private HistoryTable historyTable;
	
	/**
	 * the tag name for this field
	 */
	private String tagName;
	
	/**
	 * the field name
	 */
	private String fieldName;
	
	/**
	 * the constructor of the class
	 * @param historyTable a history table
	 * @param tagName a tag name
	 * @param fieldName a field name
	 */
	public HistoryField(HistoryTable historyTable, String tagName, String fieldName){
		
		this.historyTable=historyTable;
		this.tagName=tagName;
		this.fieldName=fieldName;
		
		historyTable.recordField(this);
	}
	
	/**
	 * @return the history table
	 */
	public HistoryTable getHistoryTable() {
		return historyTable;
	}

	/**
	 * @return the field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @return the tag name
	 */
	public String getTagName() {
		return tagName;
	}
}
