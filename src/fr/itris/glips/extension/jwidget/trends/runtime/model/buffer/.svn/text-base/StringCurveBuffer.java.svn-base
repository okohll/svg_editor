package fr.itris.glips.extension.jwidget.trends.runtime.model.buffer;

import java.util.*;

/**
 * the class of a curve buffer containing string values
 * @author ITRIS, Jordi SUC
 */
public class StringCurveBuffer extends CurveBuffer {
	
	/**
	 * the constructor of the class
	 * @param tagName the tag name whose values are recorded in this buffer
     * @param isEnumeratedTag whether the tag is an enumerated one
	 * @param bufferSize the size of the buffer
	 */
	public StringCurveBuffer(String tagName, boolean isEnumeratedTag, int bufferSize){
		
		super(tagName, isEnumeratedTag, bufferSize);
	}
	
	@Override
	public synchronized void addTagValue(Object newValue) {
		
		//checking if the buffer is full, if so, the first element of the sorted set is removed
		if(buffer.size()==bufferSize){
			
			buffer.remove(buffer.firstKey());
		}
		
		buffer.put(System.currentTimeMillis(), newValue);
	}
	
	@Override
	public synchronized void addTagValues(Map<Long, Object> values) {

		String sVal;
		
		for(Long horodate : values.keySet()){
			
			sVal=(String)values.get(horodate);
			
			buffer.put(horodate, sVal);
		}
	}
}
