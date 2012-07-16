package fr.itris.glips.rtdaeditor.dbchooser;

import fr.itris.glips.rtda.toolkit.*;
import java.util.*;

/**
 * the class allowing to retrieve information from a node in the data base
 * @author ITRIS, Jordi SUC
 */
public class DataBaseNodeInformation {

	/**
	 * the type of the node
	 */
	private int tagType=TagToolkit.NOT_A_TAG;
	
	/**
	 * the xml path of the node
	 */
	private String xmlPath="";
	
	/**
	 * the list of the enumerated values of a tag if this tag is an enumerated one
	 */
	private LinkedList<String> enumeratedValues=null;
	
	/**
	 * the location of a view if this node corresponds to a view
	 */
	private String viewLocation="";
	
	/**
	 * the constructor of the class
	 * @param tagType the type of the node
	 * @param xmlPath the xml path of the node
	 * @param enumeratedValues the list of the enumerated values of a tag if this tag is an enumerated one
	 * @param viewLocation the location of a view if this node corresponds to a view
	 */
	public DataBaseNodeInformation(int tagType, String xmlPath, LinkedList<String> enumeratedValues, String viewLocation){
		
		this.tagType=tagType;
		this.xmlPath=xmlPath;
		this.enumeratedValues=enumeratedValues;
		this.viewLocation=viewLocation;
	}

	/**
	 * @return Returns the enumeratedValues.
	 */
	public LinkedList<String> getEnumeratedValues() {
		return this.enumeratedValues;
	}

	/**
	 * @return Returns the tagType.
	 */
	public int getTagType() {
		return this.tagType;
	}

	/**
	 * @return Returns the viewLocation.
	 */
	public String getViewLocation() {
		return this.viewLocation;
	}

	/**
	 * @return Returns the xmlPath.
	 */
	public String getXmlPath() {
		return this.xmlPath;
	}
}
