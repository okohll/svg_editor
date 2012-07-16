/*
 * Created on 24 févr. 2005
 */
package fr.itris.glips.rtda;

import fr.itris.glips.rtda.colorsblinkings.*;
import fr.itris.glips.rtda.components.picture.*;
import java.util.*;
import org.w3c.dom.*;
import java.io.*;

/**
 * the class for modifying the values associated with a blinking
 * 
 * @author ITRIS, Jordi SUC
 */
public class BlinkingValueModifier implements Comparable<BlinkingValueModifier>{
	
	/**
	 * the svg picture this blinking value modifier is linked to
	 */
	protected SVGPicture picture;
    
    /**
     * the associated blinking
     */
    protected Blinking blinking;
    
    /**
     * the element the property belongs to
     */
    protected Element element;
    
    /**
     * the name of a property or an attribute
     */
    protected String propertyName;
    
    /**
     * the first and second values for the blinking
     */
    protected String value1, value2;
    
    /**
     * whether this blinking value modifier is active or not
     */
    protected boolean isActive=false;
    
    /**
     * the current value
     */
    protected Object currentModifierValue;
    
    /**
     * the constructor of the class
     * @param mainDisplay the main display
     * @param picture a svg picture
     * @param projectFile a project file
     * @param blinkingId the id of a blinking
     * @param element the element the property belongs to
     * @param propertyName the name of a property or an attribute
     * @param value1 the first value for the blinking
     * @param value2 the second value for the blinking
     */
    public BlinkingValueModifier(MainDisplay mainDisplay, SVGPicture picture, 
    		File projectFile, String blinkingId, Element element, String propertyName, 
    		String value1, String value2){
       
        if(picture!=null && blinkingId!=null && element!=null && 
        		propertyName!=null && ! propertyName.equals("")){
            
            this.picture=picture;
            //retrieving the blinking object corresponding to the blinking id
            Map<String, Object> blinkings=
            	mainDisplay.getColorsAndBlinkingsToolkit().getBlinkingsMap(projectFile);
            
            if(blinkings!=null){
                
                blinking=(Blinking)blinkings.get(blinkingId);
            }
            
            this.element=element;
            this.propertyName=propertyName;
            this.value1=value1;
            this.value2=value2;
        }
    }
    
    /**
     * @return Returns the blinking.
     */
    public Blinking getBlinking() {
        return blinking;
    }
    
    /**
     * @return the svg picture this blinking value modifier is linked to
     */
    public SVGPicture getPicture() {
		return picture;
	}

    /**
     * @return Returns the isActive.
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * @param isActive The isActive to set.
     */
    public void setActive(boolean isActive) {
        
        if(! this.isActive){
            
            synchronized(this){
                
                currentModifierValue=null;
            }
        }
        
        this.isActive=isActive;
    }
    
    /**
     * applies the changes of the value of the blinking on the attribute or property this object is linked with
     * @return runnable the runnable used to modify the value of an attribute or a property
     */
    public Runnable applyBlinkingChanges(){
        
        Runnable runnable=null;
        
        if(blinking!=null && isActive){
            
            //getting the current value for the blinking
            String currentValue="";
            
            if(blinking.getCurrentValue()==Blinking.VALUE_DOWN){
                
                currentValue=value1;
                
            }else{
                
                currentValue=value2;
            }
            
            //test whether the blinking value has changed since the last time it has been set
            if(currentModifierValue==null || (currentModifierValue!=null && ! currentModifierValue.equals(currentValue))){
                
                final String fcurrentValue=currentValue;
                
                //creating the runnable that will be executed to apply the blinking value
                runnable=new Runnable(){

    	            public void run() {

	                    element.setAttribute(propertyName, fcurrentValue);
    	            } 
                };
                
                //sets the current value for the modifier
                synchronized(this){
                    
                    currentModifierValue=currentValue;
                }
            }
        }

        return runnable;
    }
    
    public int compareTo(BlinkingValueModifier o) {

    	return 0;
    }
}
