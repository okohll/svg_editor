/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.animations;

import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.test.*;
import java.util.*;

/**
 * the listener to the changes of data for the "color on state" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class ColorOnState extends DataChangedListener{

	/**
	 * attribute names
	 */
	private final static String tagAtt="tag", invalidValueFillAtt="invalidValueFill", 
		invalidValueStrokeAtt="invalidValueStroke", defaultValueFillAtt="defaultValueFill", 
		defaultValueStrokeAtt="defaultValueStroke",
		usedAtt="used", valueAtt="value", noneVal="none", fillAtt="fill", strokeAtt="stroke";
    
    /**
     * the default and invalid values
     */
    private String invalidValueFill="", defaultValueFill="", 
    	invalidValueStroke="", defaultValueStroke="";
	
    /**
     * the map of the fill colors associating a possible value of a tag to its corresponding color
     */
    private Map<String, String> fillMap=new HashMap<String, String>();
    
    /**
     * the map of the stroke colors associating a possible value of a tag to its corresponding color
     */
    private Map<String, String> strokeMap=new HashMap<String, String>();

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public ColorOnState(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        //getting the name specified in the "tag" attribute and adding it to the set of the data names
        addData(animationElement.getAttribute(tagAtt));

        //storing the values of the attributes of the animation node and creating the associated blinking color value computer
        invalidValueFill=animationElement.getAttribute(invalidValueFillAtt);
        invalidValueFill=(invalidValueFill.equals("")?noneVal:invalidValueFill);
        addBlinkingColorValueComputer(parentElement, invalidValueFillAtt, invalidValueFill, fillAtt);
        
        defaultValueFill=animationElement.getAttribute(defaultValueFillAtt);
        defaultValueFill=(defaultValueFill.equals("")?noneVal:defaultValueFill);
        addBlinkingColorValueComputer(parentElement, defaultValueFillAtt, defaultValueFill, fillAtt);

        invalidValueStroke=animationElement.getAttribute(invalidValueStrokeAtt);
        invalidValueStroke=(invalidValueStroke.equals("")?noneVal:invalidValueStroke);
        addBlinkingColorValueComputer(parentElement, invalidValueStrokeAtt, invalidValueStroke, strokeAtt);
        
        defaultValueStroke=animationElement.getAttribute(defaultValueStrokeAtt);
        defaultValueStroke=(defaultValueStroke.equals("")?noneVal:defaultValueStroke);
        addBlinkingColorValueComputer(parentElement, defaultValueStrokeAtt, defaultValueStroke, strokeAtt);
        
        //fills the maps with the fill and stroke values
        String value="", fill="", stroke="";
        Element valueEl=null;
        boolean isUsed=false;
        LinkedList<String> realValuesNames=new LinkedList<String>();

        for(Node cur=animationElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
            
            if(cur instanceof Element){
            	
            	valueEl=(Element)cur;
                
                //whether the child should be used or not
        		isUsed=Boolean.parseBoolean(valueEl.getAttribute(usedAtt));

                if(isUsed){
                    
                    //getting one of the possible values of the tag
                    value=valueEl.getAttribute(valueAtt);
                    
                    if(! value.equals("")){
                        
                        //getting the fill and stroke values for the given tag value and creating the associated blinking color value computer
                        fill=((Element)cur).getAttribute(fillAtt);
                        fill=(fill.equals("")?noneVal:fill);
                        addBlinkingColorValueComputer(parentElement, value+"-fill", fill, fillAtt);
                        
                        stroke=((Element)cur).getAttribute(strokeAtt);
                        stroke=(stroke.equals("")?noneVal:stroke);
                        addBlinkingColorValueComputer(parentElement, value+"-stroke", stroke, strokeAtt);
                        
                        realValuesNames.add(value);
                        value=AnimationsToolkit.normalizeEnumeratedValue(value);
                        fillMap.put(value, fill);
                        strokeMap.put(value, stroke);
                    }
                }
            }
        }
        
        //if we are in the test version, we store information on the tag
        if(picture.getMainDisplay().isTestVersion()){
            
            TestTagInformation info=new TestTagInformation(picture, 
            		animationElement.getAttribute(tagAtt), realValuesNames);
            
            dataNamesToInformation.put(animationElement.getAttribute(tagAtt), info);
        }
    }
    
    /**
     * the method called when the data to which the listener is registered, is modified
     * @param evt an event
     * @return the runnable that should be executed to apply the modifications
     */
    public Runnable dataChanged(DataEvent evt) {

        //whether the current state of the listener is invalid or not
        boolean isInvalidState=false;
        
        Runnable runnable=null;

        //deactivates all the blinking value modifiers
        deactivateBlinkingValueModifiers();

        //getting the value of the tag
        String value=(String)getData(
        		animationElement.getAttribute(tagAtt));

        //the new values
        String newFillValue="", newStrokeValue="";

        //whether the fill or stroke attribute can be set, i.e. whether the value of the tag corresponds to a blinking color,
        //if so, the blinking color is activated and no color is set
        boolean canSetFillAttribute=false, canSetStrokeAttribute=false;
        
        if(value==null){
        	
            //the state of the tag is invalid//
        	
        	if(itemNameToBlinkingValueModifier.containsKey(invalidValueFillAtt)){
        		
        		activateBlinkingValueModifier(invalidValueFillAtt);
        		
        	}else{
        		
                newFillValue=invalidValueFill;
                canSetFillAttribute=true;
        	}
        	
        	if(itemNameToBlinkingValueModifier.containsKey(invalidValueStrokeAtt)){
        		
        		activateBlinkingValueModifier(invalidValueStrokeAtt);
        		
        	}else{
        		
                newStrokeValue=invalidValueStroke;
                canSetStrokeAttribute=true;
        	}
        	
        	isInvalidState=true;
            
        }else if(fillMap.containsKey(value)){

            //the ids of the modifiers
            String fillIdModifier=value+"-fill", strokeIdModifier=value+"-stroke";
            
            if(itemNameToBlinkingValueModifier.containsKey(fillIdModifier)){
            	
            	//activates the blinking of the fill attribute for the current value of the tag
            	activateBlinkingValueModifier(fillIdModifier);
            	
            }else{
            	
            	newFillValue=fillMap.get(value);
            	canSetFillAttribute=true;
            }
            
            if(itemNameToBlinkingValueModifier.containsKey(strokeIdModifier)){
            	
            	//activates the blinking of the stroke attribute for the current value of the tag
            	activateBlinkingValueModifier(strokeIdModifier);
            	
            }else{
            	
            	newStrokeValue=strokeMap.get(value);
            	canSetStrokeAttribute=true;
            }
            
        }else{
        	
            //setting the default value//
        	if(itemNameToBlinkingValueModifier.containsKey(defaultValueFillAtt)){
        		
        		activateBlinkingValueModifier(defaultValueFillAtt);
        		
        	}else{
        		
                newFillValue=defaultValueFill;
                canSetFillAttribute=true;
        	}
        	
        	if(itemNameToBlinkingValueModifier.containsKey(defaultValueStrokeAtt)){
        		
        		activateBlinkingValueModifier(defaultValueStrokeAtt);
        		
        	}else{
        		
                newStrokeValue=defaultValueStroke;
                canSetStrokeAttribute=true;
        	}
        }

        if(canSetFillAttribute || canSetStrokeAttribute){
        	
            final String fnewFillValue=newFillValue, fnewStrokeValue=newStrokeValue;
            final boolean fcanSetFillAttribute=canSetFillAttribute;
            final boolean fcanSetStrokeAttribute=canSetStrokeAttribute;

            //the runnable that will be returned
            runnable=new Runnable(){

	            public void run() {

	                //sets the attributes
	                if(fcanSetFillAttribute && 
	                		! fnewFillValue.equals(parentElement.getAttribute("fill"))){
	                    
	                	parentElement.setAttribute("fill", fnewFillValue);
	                }
	                
	                if(fcanSetStrokeAttribute &&
	                		! fnewStrokeValue.equals(parentElement.getAttribute("stroke"))){
	                    
	                	parentElement.setAttribute("stroke", fnewStrokeValue);
	                }
	            } 
            };
        }
        
        //stores that the state of the listener is invalid as some of its tags are invalid
        setInvalidTag(isInvalidState);
        
        return runnable;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#dispose()
     */
    public void dispose() {}

}
