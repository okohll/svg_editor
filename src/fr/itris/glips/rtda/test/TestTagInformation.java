/*
 * Created on 1 févr. 2005
 */
package fr.itris.glips.rtda.test;

import java.util.*;
import java.util.concurrent.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class allowing to store information on a tag
 * 
 * @author ITRIS, Jordi SUC
 */
public class TestTagInformation implements Comparable<TestTagInformation>{

	/**
	 * the svg picture linked to this object
	 */
	private SVGPicture picture;

    /**
     * the name of the data tag
     */
    private String tagName="";
    
    /**
     * the possible values if the tag is an enumerated tag
     */
    private List<String> enumeratedValues=new CopyOnWriteArrayList<String>();

    /**
     * the constructor of the class
     * @param picture the svg picture linked to this object
     * @param tagName the name of a data tag
     * @param enumeratedValues the list of the values if the tag is an enumerated tag
     */
    public TestTagInformation(SVGPicture picture, String tagName, List<String> enumeratedValues){
        
    	this.picture=picture;
        this.tagName=tagName;

        if(enumeratedValues!=null){
            
            this.enumeratedValues.addAll(enumeratedValues);
        }
    }

    /**
     * @return Returns the tagName.
     */
    public String getTagName() {
        return tagName;
    }
    
    /**
     * @return Returns the enumeratedValues.
     */
    public List<String> getEnumeratedValues() {
    	
        return enumeratedValues;
    }
    
    /**
     * adds an enumerated value to the list of the enumerated values
     * @param value an enumerated value
     */
    public void addEnumeratedValue(String value){
        
        if(value!=null && ! value.equals("")){
            
            enumeratedValues.add(value);
        }
    }
    
    /**
     *@return whether this object can be used
     */
    public boolean canBeUsed(){
    	
    	if(picture!=null && picture.isDisplayed()){
    		
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * @return the svg picture linked to this object
     */
    public SVGPicture getPicture() {
		return picture;
	}
    
    public int compareTo(TestTagInformation o) {

    	return 0;
    }
}
