package fr.itris.glips.extension.jwidget.trends.runtime.model.buffer;

import java.util.*;
import fr.itris.glips.library.*;

/**
 * the class of a curve buffer containing string values
 * @author ITRIS, Jordi SUC
 */
public class DoubleCurveBuffer extends CurveBuffer {
	
	/**
	 * the constructor of the class
	 * @param tagName the tag name whose values are recorded in this buffer
     * @param isEnumeratedTag whether the tag is an enumerated one
	 * @param bufferSize the size of the buffer
	 */
	public DoubleCurveBuffer(String tagName, boolean isEnumeratedTag, int bufferSize){
		
		super(tagName, isEnumeratedTag, bufferSize);
	}
	
	@Override
	public void addTagValue(Object newValue) {

		//checking if the buffer is full, if so, the first element of the sorted set is removed
		if(buffer.size()==bufferSize){
			
			synchronized (buffer) {
				buffer.remove(buffer.firstKey());
			}
		}
		
		//converting the given string value unto a double value
		double doubleNewValue=Toolkit.getNumber(newValue);
		
		//adding a new value to the buffer
		if(! Double.isNaN(doubleNewValue)){
			
			synchronized (buffer) {
				buffer.put(System.currentTimeMillis(), doubleNewValue);
			}
			
		}else{
			
			synchronized (buffer) {
				buffer.put(System.currentTimeMillis(), null);
			}
		}
	}
	
	@Override
	public void addTagValues(Map<Long, Object> values) {

		Object val;
		
		for(Long horodate : values.keySet()){
			
			val=values.get(horodate);
			
			if(val!=null && val instanceof Integer){
				
				val=new Double((Integer)val);
			}
			
			synchronized (buffer) {
				buffer.put(horodate, val);
			}
		}
	}
}
