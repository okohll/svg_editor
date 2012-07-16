/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.animaction;

import java.util.*;

/**
 * the event notifying the modification of the value of a data
 * 
 * @author ITRIS, Jordi SUC
 */
public class DataEvent {

    /**
     * the map associating the name of a data to its value
     */
    private Map<String, Object> dataNameToValue=null;
    
    /**
     * the source that has modified the data
     */
    private Object source=null;
    
    /**
     * the constructor of the class
     * @param dataNameToValue the map associating the name of a data to its value
     * @param source the source that has modified the data
     */
    public DataEvent(Map<String, Object> dataNameToValue, Object source){
        
        this.dataNameToValue=dataNameToValue;
        this.source=source;
    }

    /**
     * @return Returns the dataNameToValue.
     */
    public Map<String, Object> getDataNameToValue() {
        return dataNameToValue;
    }
    
    /**
     * @return the source that has modified the data
     */
    public Object getSource() {
        return source;
    }
}
