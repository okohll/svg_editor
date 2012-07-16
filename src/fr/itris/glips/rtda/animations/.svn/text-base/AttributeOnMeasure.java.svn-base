/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.animations;

import fr.itris.glips.library.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.test.*;
import java.util.*;

/**
 * the listener to the changes of data for the "attribute on measure" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class AttributeOnMeasure extends DataChangedListener{
    
    /**
     * the value of the tag attribute of the animation node
     */
    private String tagAttributeValue="";
    
    /**
     * the tag min and tag max
     */
    private DataLimit tagMin, tagMax;
    
    /**
     * the computed value of the tag, tagMin and tagMax attributes
     */
    private double tagValue=Double.NaN, tagMinValue=Double.NaN, tagMaxValue=Double.NaN;
    
    /**
     * the name of the attribute that should be modified
     */
    private String attributeName="";
    
    /**
     * the invalid, min attribute and max attribute values
     */
    private String invalidValue="", attributeMin="", attributeMax="";
    
    /**
     * the value of the min and max attributes
     */
    private double attributeMinValue=0, attributeMaxValue=0;
    
    /**
     * whether the main tag attribute is described by a function or not
     */
    private boolean isTagFunction=false;

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public AttributeOnMeasure(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        //getting the name specified in the "tag" attribute and adding it to the set of the data names
        tagAttributeValue=animationElement.getAttribute("tag");

        if(AnimationsToolkit.isFunction(tagAttributeValue)){
 
            tagAttributeValue=getNewId(tagAttributeValue);
            isTagFunction=true;
        }

        addData(tagAttributeValue);

        //getting the name of the attribute that should be modified
        attributeName=animationElement.getAttribute("attribute");
        
        //getting the tagMin and tagMax
        tagMin=new DataLimit(picture, tagAttributeValue, 
        		animationElement.getAttribute("tagMin"), true);
        tagMax=new DataLimit(picture, tagAttributeValue, 
        		animationElement.getAttribute("tagMax"), false);
        
        //getting the tag min and tag max values
        if(tagMin.isAbsoluteValue()){
            
            tagMinValue=tagMin.getAbsoluteValue();
        }
        
        if(tagMax.isAbsoluteValue()){
            
            tagMaxValue=tagMax.getAbsoluteValue();
        }
        
        if(tagMin.isTag()){
            
            addData(tagMin.getTag());
        }
        
        if(tagMax.isTag()){
            
            addData(tagMax.getTag());
        }
        
        //getting the invalid values, and the min and max attribute values
        invalidValue=animationElement.getAttribute("invalidValue");
        attributeMin=animationElement.getAttribute("attributeMin");
        attributeMax=animationElement.getAttribute("attributeMax");
        
        //computing the values of the min and max attributes
        try{
        	attributeMinValue=Double.parseDouble(attributeMin);
        }catch (Exception ex){attributeMinValue=0;}
        
        try{
        	attributeMaxValue=Double.parseDouble(attributeMax);
        }catch (Exception ex){attributeMaxValue=0;}

        if(isTagFunction){
            
            //creates the function value computer if the tag is described by a function
            FunctionValueComputer computer=new FunctionValueComputer(
            		picture, tagAttributeValue, tagMinValue, tagMaxValue);
            
            //adds the computer to the list of the function values computer
            functionValueComputers.add(computer);
        }

        //if we are in the test version, we store information on the tag, the minTag and the maxTag
        if(picture.getMainDisplay().isTestVersion()){
            
            TestTagInformation info=null;
            
            if(! isTagFunction){
                
                info=new TestTagInformation(picture, tagAttributeValue, null);
                dataNamesToInformation.put(tagAttributeValue, info);
            }
            
            if(tagMin.isTag()){
                
                info=new TestTagInformation(picture, tagMin.getTag(), null);
                dataNamesToInformation.put(tagMin.getTag(), info);
            }
            
            if(tagMax.isTag()){
                
                info=new TestTagInformation(picture, tagMax.getTag(), null);
                dataNamesToInformation.put(tagMax.getTag(), info);
            }
        }
    }
    
    /**
     * the method called when the data to which the listener is registered, is modified
     * @param evt an event
     * @return the runnable that should be executed to apply the modifications
     */
    public Runnable dataChanged(DataEvent evt) {

        Runnable runnable=null;
        
    	if(! attributeName.equals("")){
    		
            Map<String, Object> dataNameToValue=evt.getDataNameToValue();
            
            if(dataNameToValue!=null) {
            	
                //deactivates all the blinking value modifiers
                deactivateBlinkingValueModifiers();
                
                //getting the value of the tag//
                if(dataNameToValue.containsKey(tagAttributeValue)){
                    
                	tagValue=Toolkit.getNumber(getData(tagAttributeValue));
                }
                
                //refreshing the min and max values//
                if(tagMin.isTag() && dataNameToValue.containsKey(tagMin.getTag())){
                    
                	tagMinValue=Toolkit.getNumber(getData(tagMin.getTag()));
                }
                
                if(tagMax.isTag() && dataNameToValue.containsKey(tagMax.getTag())){
                    
                	tagMaxValue=Toolkit.getNumber(getData(tagMax.getTag()));
                }
                
                //the new value
                String newValue="";

                //whether the tag value is invalid or not
                boolean isStateInvalid=true;

                if(! Double.isNaN(tagValue) && ! Double.isNaN(tagMinValue) 
                        && ! Double.isNaN(tagMaxValue) && tagMaxValue>=tagMinValue 
                        && tagValue>=tagMinValue && tagValue<=tagMaxValue 
                        && attributeMinValue<=attributeMaxValue){

                    double ratio=0, amplitude=Math.abs(tagMaxValue-tagMinValue);
                    
                    if(amplitude>0){
                        
                        isStateInvalid=false;
                        ratio=Math.abs(tagValue/amplitude);
                        
                        double newAttributeValue=ratio*(
                        	Math.abs(attributeMaxValue-attributeMinValue))+attributeMinValue;
                        
                        newValue=FormatStore.format(newAttributeValue);
                    }
                }
                
                if(isStateInvalid){
                    
                    //the state is invalid//
                    newValue=invalidValue;
                }
                
                //setting the new value for the attribute
                if(newValue!=null && ! newValue.equals("") && 
                		! newValue.equals(parentElement.getAttribute(attributeName))){
                    
                    final String fnewValue=newValue;

                    runnable=new Runnable(){

        	            public void run() {

        	                parentElement.setAttribute(attributeName, fnewValue);
        	            } 
                    };
                }
                
                setInvalidTag(isStateInvalid);
            }
    	}

        return runnable;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#dispose()
     */
    public void dispose() {}
}