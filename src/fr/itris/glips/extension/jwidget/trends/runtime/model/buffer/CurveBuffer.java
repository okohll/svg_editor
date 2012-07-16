package fr.itris.glips.extension.jwidget.trends.runtime.model.buffer;

import java.util.*;

/**
 * the class of a curve buffer
 * @author ITRIS, Jordi SUC
 */
public abstract class CurveBuffer {
	
	/**
	 * the buffer
	 */
	protected SortedMap<Long, Object> buffer=new TreeMap<Long, Object>();
	
	/**
	 * the tag name whose values are recorded in this buffer
	 */
	protected String tagName;
	
	/**
	 * whether the tag is an enumerated one
	 */
	protected boolean isEnumeratedTag;
	
	/**
	 * the size of the buffer
	 */
	protected int bufferSize=0;
	
	/**
	 * the constructor of the class
	 * @param tagName the tag name whose values are recorded in this buffer
     * @param isEnumeratedTag whether the tag is an enumerated one
	 * @param bufferSize the size of the buffer
	 */
	public CurveBuffer(String tagName, boolean isEnumeratedTag, int bufferSize){
		
		this.tagName=tagName;
		this.isEnumeratedTag=isEnumeratedTag;
		this.bufferSize=bufferSize;
	}

	/**
	 * returns the sub map of the buffer associating a time value (long) to a tag value
	 * @param startDate the date from which the elements in the returned map are added
	 * @param endDate the date before which the elements in the returned map are added
	 * @return the sub map of the buffer associating a time value (long) to a tag value
	 */
	public TreeMap<Long, Object> getBufferPart(long startDate, long endDate) {

		TreeMap<Long, Object> map=null;
		
		try{
			
			synchronized (buffer) {
				map=new TreeMap<Long, Object>(buffer.subMap(startDate, endDate+1));
			}
			
			//getting the key in the buffer that lies just before the first key of the computed map
			//and putting the key and its associated value to the computed map
			SortedMap<Long, Object> headMap=null;
			
			synchronized (buffer) {
				headMap=buffer.headMap(map.size()>0?map.firstKey():startDate);
			}

			if(headMap.size()>0){
				
				long lastKey=headMap.lastKey();
				map.put(lastKey, headMap.get(lastKey));
			}
			
			//getting the key in the buffer that lies just after the last key of the computed map
			//and putting the key and its associated value to the computed map
			SortedMap<Long, Object> tailMap=null;
			
			synchronized (buffer) {
				tailMap=buffer.tailMap(map.size()>0?map.lastKey()+1:endDate);
			}
			
			if(tailMap.size()>0){
				
				long firstKey=tailMap.firstKey();
				map.put(firstKey, tailMap.get(firstKey));
			}
			
		}catch (Exception ex){ex.printStackTrace();}
		
		return map;
	}
	
	/**
	 * returns the sub map of the buffer associating a time value (long) to a tag value
	 * @param date the date from which the elements in the returned map are added
	 * @return the sub map of the buffer associating a time value (long) to a tag value
	 */
	public TreeMap<Long, Object> getBufferPart(long date) {

		TreeMap<Long, Object> map=null;
		
		if(buffer.size()>0){
			
			if(date>=buffer.firstKey() && date<=buffer.lastKey()){
				
				//if the date is included in the map dates, a part of the buffer is returned
				try{
					
					synchronized (buffer) {
						map=new TreeMap<Long, Object>(buffer.tailMap(date));
					}

					//getting the key in the buffer that lies just before the first key of the computed map
					//and putting the key and its associated value to the computed map
					SortedMap<Long, Object> headMap=null;
					
					synchronized (buffer) {
						
						headMap=buffer.headMap(map.size()>0?map.firstKey():date);
					}

					if(headMap.size()>0){
						
						long lastKey=headMap.lastKey();
						map.put(lastKey, headMap.get(lastKey));
					}
				}catch (Exception ex){}
				
			}else if(date<=buffer.firstKey()){
				
				//if the date is not included in the map dates, and is lower than the first date, 
				//the whole buffer is returned
				try{
					synchronized (buffer) {
						map=new TreeMap<Long, Object>(buffer);
					}
				}catch (Exception ex){}
			}
		}

		return map;
	}
	
	/**
	 * returns the value corresponding to the horodate, i.e., 
	 * the closest value that occured before the horodate
	 * @param horodate the horodate
	 * @return the value corresponding to the horodate, i.e., 
	 * the closest value that occured before the horodate
	 */
	public Object getTagValue(long horodate){
		
		Object value=null;
		
		if(horodate>0){
			
			SortedMap<Long, Object> map=null;
			
			synchronized (buffer) {
				map=buffer.headMap(horodate);
			}
			
			if(map.size()>0){
				
				value=map.get(map.lastKey());
			}
		}
		
		return value;
	}

	/**
	 * adds a new value to the buffer
	 * @param newValue the new value
	 */
	public abstract void addTagValue(Object newValue);
	
	/**
	 * adds all the values of the map to the buffer
	 * @param values a map associating a horodate to a tag value
	 */
	public abstract void addTagValues(Map<Long, Object> values);
	
	/**
	 * @return the date of the first recorded tag value  or <code>-1</code> 
	 * 				if the buffer contains no value
	 */
	public long getFirstRecordedDate() {
		
		long firstDate=-1;
		
		if(buffer.size()>0){
			
			firstDate=buffer.firstKey();
		}
		
		return firstDate;
	}
	
	/**
	 * @return the date of the last recorded tag value  or <code>-1</code> 
	 * 				if the buffer contains no value
	 */
	public long getLastRecordedDate() {
		
		long lastDate=-1;
		
		if(buffer.size()>0){
			
			lastDate=buffer.lastKey();
		}
		
		return lastDate;
	}
	
	/**
	 * @return the size of the buffer
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * @return the tag name whose values are recorded in this buffer
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @return whether the tag is an enumerated one
	 */
	public boolean isEnumeratedTag() {
		return isEnumeratedTag;
	}
	
	/**
	 * resets the buffer
	 */
	public synchronized void reset(){
		
		buffer.clear();
	}
}
