package fr.itris.glips.rtda.test;

import fr.itris.glips.library.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.toolkit.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * the class storing information given by the user to simulate the changes of the value of a tag
 * @author ITRIS, Jordi SUC
 */
public class TestSimulationTagValues {
	
    /**
     * the default and invalid labels
     */
    private static String defaultLabel="", invalidLabel="";
	
	static {
		
        //getting the resource bundle
		ResourceBundle bundle=null;
		
		try{
			bundle=ResourceBundle.getBundle("fr.itris.glips.rtda.resources.properties.strings");
		}catch (Exception ex){}
		
		if(bundle!=null){
			
	        try{
	            defaultLabel=bundle.getString("defaultLabel");
	            invalidLabel=bundle.getString("invalidLabel");
	        }catch (Exception ex){}
		}
	}
	
    /**
     * the name of a data
     */
    private String tagName="";
    
    /**
     * the type of the tag
     */
    private int tagType=-1;
    
    /**
     * the value of the tag that will be returned to the rtdp
     */
    private Object tagValue=new Object(){

		private String str="/**/";
		
		@Override
		public String toString() {
			
			return str;
		}
    };
    
    /**
     * the value of the data
     */
    private Object value="";
    
    /**
     * the min value
     */
    private double min=0;
    
    /**
     * the max value
     */
    private double max=0;
    
    /**
     * the initial value
     */
    private double initialValue=0;
    
    /**
     * the period
     */
    private double period=1;
    
    /**
     * the function used to describe the variation of the value of the tag
     */
    private String type=AnimationsToolkit.CONSTANT_FUNCTION;
    
    /**
     * whether the item has changed or not
     */
    private boolean hasChanged=true;
    
    /**
     * whether the min has changed
     */
    private boolean minHasChanged=true;
    
    /**
     * whether the max has changed
     */
    private boolean maxHasChanged=true;
    
    /**
     * the list of the enumerated values
     */
    private CopyOnWriteArrayList<String> enumeratedValues=
    	new CopyOnWriteArrayList<String>();
    
    /**
     * the map of the possible values if the tag is an enumerated tag,
     * the map associating an enumerated value to its label
     */
    private Map<String, String> enumeratedValuesMap=
    	new ConcurrentHashMap<String, String>();
    
    /**
     * the map of the possible values if the tag is an enumerated tag,
     * the map associating the label of an enumerated value to its value
     */
    private Map<String, String> inverseEnumeratedValues=
    	new ConcurrentHashMap<String, String>();

    /**
     * the constructor of the class 
     * @param tagName the name of a data
     * @param currentValue the current value
     * @param tagType the type of the tag
     * @param enumeratedValues the possible values if the tag is an enumerated tag
     * @param min the initial value for the minimum
     * @param max the initial value for the maximum
     */
    public TestSimulationTagValues(
    	String tagName, Object currentValue, int tagType, 
    		List<String> enumeratedValues, double min, double max){
        
        this.tagName=tagName;
        this.value=currentValue;
        this.tagType=tagType;
        this.min=min;
        this.max=max;

        if(tagType==TagToolkit.ENUMERATED){
            
        	setEnumeratedValues(enumeratedValues);
        }
        
        if(value==null){
        	
            value=getStartValue();
        }
    }

    /**
     * adds new enumerated values to the list of the enumerated values of the tag
     * @param values a list of enumerated values of the tag
     */
    public void setEnumeratedValues(List<String> values){
    	
		enumeratedValues.clear();
		enumeratedValuesMap.clear();
		inverseEnumeratedValues.clear();
    	
    	if(values!=null){
    		
    		String val="";

        	for(String label : values){
        		
        		val=AnimationsToolkit.normalizeEnumeratedValue(label);
        		
        		this.enumeratedValues.add(val);
                enumeratedValuesMap.put(val, label);
                inverseEnumeratedValues.put(label, val);
        	}
    	}
    	
		this.enumeratedValues.add(defaultLabel);
		this.enumeratedValues.add(invalidLabel);
		
        this.enumeratedValuesMap.put(defaultLabel, defaultLabel);
        this.enumeratedValuesMap.put(invalidLabel, invalidLabel);
        
        this.inverseEnumeratedValues.put(defaultLabel, defaultLabel);
        this.inverseEnumeratedValues.put(invalidLabel, invalidLabel);
    }

    /**
     * @return the default label.
     */
    public static String getDefaultLabel() {
        return defaultLabel;
    }
    
    /**
     * @return the invalid label.
     */
    public static String getInvalidLabel() {
        return invalidLabel;
    }
    
    /**
     * sets the new value for the tag whose name is given
     * @param tagName the name of a tag
     * @param value a value
     */
    public void setNewValueFromRtdp(String tagName, Object value){

    	if(tagName!=null && ! tagName.equals("")){

			//sets the new value for the tag
            this.value=value;
            this.type=new String(AnimationsToolkit.CONSTANT_FUNCTION);
            hasChanged=true;
    	}
    }

    /**
     * @return the list of the enumerated values 
     */
    public List<String> getEnumeratedValues() {
    	
        return enumeratedValues;
    }

    /**
     * @return the map of the enumerated values 
     * associating a tag value to its label
     */
    public Map<String, String> getEnumeratedValuesMap() {
    	
    	//creating the linked hash map of the enumerated values
    	LinkedHashMap<String, String> linkedHashMap=
    		new LinkedHashMap<String, String>();
    	
    	String label="";
    	
    	for(String val : enumeratedValues){
    		
    		label=enumeratedValuesMap.get(val);
    		linkedHashMap.put(val, label);
    	}
    	
		return linkedHashMap;
	}
    
    /**
     * @return the initial value
     */
    public double getInitialValue() {
        return initialValue;
    }
    
    /**
     * @return the type of the tag handled by this object
     */
    public int getTagType() {
		return tagType;
	}

    /**
     * @return the value.
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * @return the maximum
     */
    public double getMax() {

        return max;
    }
    
    /**
     * @return the minimum
     */
    public double getMin() {

        return min;
    }
    
    /**
     * @return the period.
     */
    public double getPeriod() {
        return period;
    }
    
    /**
     * @return the tagName.
     */
    public String getTagName() {
        return tagName;
    }
    
    /**
     * @return the type.
     */
    public String getType() {
        return type;
    }
    
    /**
     * @return whether the initial value can be modified or not
     */
    public boolean canModifyInitialValue(){
        
        return (! type.equals(AnimationsToolkit.CONSTANT_FUNCTION) && 
        	enumeratedValues.size()==0);
    }
    
    /**
     * sets the initial value
     * @param initialValue the initial value
     */
    public synchronized void setInitialValue(double initialValue) {
        
        if(canModifyInitialValue()){
            
            this.initialValue=initialValue;
            hasChanged=true;
        }
    }
    
    /**
     * @return whether the value can be modified or not
     */
    public boolean canModifyValue(){
        
        return type.equals(AnimationsToolkit.CONSTANT_FUNCTION);
    }

    /**
     * sets the new value
     * @param value the new value to set
     */
    public void setValue(Object value) {
        
        if(canModifyValue()){

        	if(tagType==TagToolkit.ENUMERATED){
        		
        		//getting the value corresponding to the provided value label
        		if(value.equals(invalidLabel)){
        			
        			this.value=null;
        			
        		}else if(value.equals(defaultLabel)){
        			
        			this.value="";
        			
        		}else{
        			
        			this.value=inverseEnumeratedValues.get(value);
        		}
        		
        	}else if(tagType==TagToolkit.STRING){
        		
        		if(value.equals(invalidLabel)){
        			
        			this.value=null;
        			
        		}else{
        			
                	this.value=value;
        		}

        	}else if(tagType==TagToolkit.ANALOGIC_FLOAT || 
        			tagType==TagToolkit.ANALOGIC_INTEGER) {
        		
        		this.value=Toolkit.getNumberObject(value);
        	}

            hasChanged=true;
        }
    }
    
    /**
     * @return whether the max can be modified or not
     */
    public boolean canModifyMax(){
        
        return (tagType==TagToolkit.ANALOGIC_FLOAT || 
        		tagType==TagToolkit.ANALOGIC_INTEGER);
    }
    
    /**
     * @param max The max to set.
     */
    public synchronized void setMax(double max) {
        
        if(canModifyMax()){
            
            this.max=max;
            hasChanged=true;
            maxHasChanged=true;
        }
    }

    /**
     * @return Returns the maxHasChanged.
     */
    public boolean maxHasChanged() {
        return maxHasChanged;
    }
    
    /**
     * @return Returns the minHasChanged.
     */
    public boolean minHasChanged() {
        return minHasChanged;
    }
    
    /**
     * @return whether the min can be modified or not
     */
    public boolean canModifyMin(){
        
        return (tagType==TagToolkit.ANALOGIC_FLOAT || 
        		tagType==TagToolkit.ANALOGIC_INTEGER);
    }
    
    /**
     * @param min The min to set.
     */
    public synchronized void setMin(double min) {
        
        if(canModifyMin()){
            
            this.min=min;
            hasChanged=true;
            minHasChanged=true;
        }
    }
    
    /**
     * @return whether the period can be modified or not
     */
    public boolean canModifyPeriod(){
        
        return (! type.equals(AnimationsToolkit.CONSTANT_FUNCTION));
    }
    
    /**
     * @param period The period to set.
     */
    public synchronized void setPeriod(double period) {
        
        if(canModifyPeriod()){
            
            this.period=period;
            hasChanged=true;
        }
    }
    
    /**
     * @return whether the type can be modified or not
     */
    public boolean canModifyType(){
    	
    	boolean isValue=(tagType==TagToolkit.ANALOGIC_FLOAT || 
    			tagType==TagToolkit.ANALOGIC_INTEGER);
        
        return (isValue || ( ! isValue && enumeratedValues.size()>1));
    }
    
    /**
     * @param type The type to set.
     */
    public synchronized void setType(String type) {
        
        if(canModifyType()){
            
            this.type=new String(type);
            hasChanged=true;
        }
    }
    
    /**
     * @return the value of the maximum for this data
     */
    public Object getMaxValue(){
        
        synchronized(this){maxHasChanged=false;}
        
        if(tagType==TagToolkit.ANALOGIC_FLOAT || 
    			tagType==TagToolkit.ANALOGIC_INTEGER){
            
            return max;
        }
            
        return null;
    }
    
    /**
     * @return the value of the minimum for this data
     */
    public Object getMinValue(){
        
        synchronized(this){minHasChanged=false;}
        
        if(tagType==TagToolkit.ANALOGIC_FLOAT || 
    			tagType==TagToolkit.ANALOGIC_INTEGER){
            
            return min;
        }
            
        return null;
    }
    
    /**
     * sets the new value for the tag
     * @param tagValue the new value for the tag
     */
    private synchronized void setTagValue(Object tagValue){

        this.tagValue=tagValue;
    }
    
    /**
     * computes the value of the tag that will be used
     * @param time the current time
     *@return whether the tag value has changed
     */
    public boolean computeTagValue(double time){
        
    	boolean isDifferentValue=false;
        Object lastTagValue=tagValue;

        if(type.equals(AnimationsToolkit.CONSTANT_FUNCTION)){
            
            if(hasChanged){
                
                //if the function is "constant", the new value is computed
                setTagValue(value);
            }

        }else{

            double val=Double.NaN;
            
            if(! Double.isNaN(period) && period!=0){
                
                if(tagType!=TagToolkit.ANALOGIC_FLOAT && 
                		tagType!=TagToolkit.ANALOGIC_INTEGER){
                    
                    synchronized(this){initialValue=0;}
                }
                
                //computing the value of the functions (a number between -1 and  1)
                if(type.equals(AnimationsToolkit.SIN_FUNCTION)){

                    val=AnimationsToolkit.sin(time, initialValue, period);

                }else if(type.equals(AnimationsToolkit.TRIANGLE_FUNCTION)){

                    val=AnimationsToolkit.triangle(time, initialValue, period);

                }else if(type.equals(AnimationsToolkit.RAMP_FUNCTION)){

                    val=AnimationsToolkit.ramp(time, initialValue, period);
                }   
            }

            if((tagType==TagToolkit.ANALOGIC_FLOAT || 
            		tagType==TagToolkit.ANALOGIC_INTEGER) && ! Double.isNaN(min) && 
            			! Double.isNaN(max) && ! Double.isNaN(initialValue)){

            	if(min<max){
            		
                    //computing the value that will be returned
                    val=((val+1)/2)*Math.abs(max-min)+min;
                    Object oVal=val;
                    
                    if(tagType==TagToolkit.ANALOGIC_INTEGER){
                    	
                    	oVal=(int)val;
                    }

                    setTagValue(oVal);
            		
            	}else if(min==max){
            		
            		val=min;
            		
                    Object oVal=val;
                    
                    if(tagType==TagToolkit.ANALOGIC_INTEGER){
                    	
                    	oVal=(int)val;
                    }
                    
                    setTagValue(oVal);
            	}

            }else if(tagType==TagToolkit.ENUMERATED && 
            		enumeratedValues.size()>1){
                
                double base=1/((double)enumeratedValues.size());
                val=(val+1)/2;
                
                //retrieving the index of an item in the list
                int index=(int)Math.floor(val/base);
                
                if(index==enumeratedValues.size()){
                    
                    index=enumeratedValues.size()-1;
                }

                //getting the value corresponding to the index in the list
                String strValue=enumeratedValues.get(index);//TODO
                
                if(strValue==null || strValue.equals(invalidLabel)){
                    
                    setTagValue(null);
                    
                }else{
                    
                    setTagValue(strValue);
                }
                
            }else if(tagType==TagToolkit.STRING){
                
                setTagValue(tagValue);
            	
            }else{
                
                setTagValue(null);
            }
        }
        
        //if the computed value is not equal to the newly computed value, a refresh of the nodes will be done
        if(lastTagValue==null || tagValue==null){
            
            if(lastTagValue!=null || tagValue!=null){

            	isDifferentValue=true;
            }
            
        }else if(! lastTagValue.equals(tagValue)){
        	
        	isDifferentValue=true;
        }
        
        lastTagValue=tagValue;

        return isDifferentValue;
    }
    
    /**
     * @return the value of a tag
     */
    public Object getTagValue(){

        return tagValue;
    }
    
    /**
     * @return the start value for the tag
     */
    public Object getStartValue(){
        
        if(tagType==TagToolkit.ANALOGIC_FLOAT){
            
            return 0D;
            
        }else if(tagType==TagToolkit.ANALOGIC_INTEGER){
        	
        	return 0;
            
        }else{
            
            if(enumeratedValuesMap!=null){
                
                return null;
            }
            
            return "";
        }
    }
	
    @Override
    public String toString() {
       
        return "tagName="+getTagName()+" value="+
        		value+" min="+min+" max="+max;
    }
    
}
