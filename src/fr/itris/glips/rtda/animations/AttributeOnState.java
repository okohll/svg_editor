/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.animations;

import org.w3c.dom.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.colorsblinkings.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.test.*;
import java.util.*;

/**
 * the listener to the changes of data for the "attribute on state" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class AttributeOnState extends DataChangedListener{
	
	/**
	 * attribute names
	 */
	protected final static String tagAtt="tag", attributeAtt="attribute", 
		invalidValue1Att="invalidValue1", invalidValue2Att="invalidValue2", 
		defaultValue1Att="defaultValue1", defaultValue2Att="defaultValue2",
		invalidValueBlinkingAtt="invalidValueBlinking", 
		defaultValueBlinkingAtt="defaultValueBlinking", usedAtt="used", valueAtt="value", 
		blinkingAtt="blinking", attributeValue1Att="attributeValue1", 
		attributeValue2Att="attributeValue2";

    /**
     * the blinkings of this animation
     */
    private Blinking invalidValueBlinking, defaultValueBlinking;
    
    /**
     * the id of the modifiers for the invalid value and the default value
     */
    private final String invalidValueBVModifierId="InvalidValue", defaultValueBVModifierId="default";
    
    /**
     * the map associating a the value of a tag to its corresponding child
     */
    private Map<String, ChildrenAttributeOnState> childrenMap=
    		new HashMap<String, ChildrenAttributeOnState>();

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public AttributeOnState(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        //adding the tag to the set of the data names
        addData(animationElement.getAttribute(tagAtt));
        
        //getting the invalid blinking id and its corresponding blinking
        invalidValueBlinking=picture.getMainDisplay().getColorsAndBlinkingsToolkit().
        	getBlinking(picture.getAnimActionsHandler().getProjectFile(), 
        			animationElement.getAttribute(invalidValueBlinkingAtt));

        if(invalidValueBlinking!=null){
            
            //creating the blinking value modifier for the invalid value
            addBlinkingValue(invalidValueBVModifierId, invalidValueBlinking.getId(), 
            		animationElement.getAttribute(attributeAtt), 
            			animationElement.getAttribute(invalidValue1Att), 
            				animationElement.getAttribute(invalidValue2Att));
        }
        
        //getting the default blinking
        defaultValueBlinking=picture.getMainDisplay().getColorsAndBlinkingsToolkit().
        	getBlinking(picture.getAnimActionsHandler().getProjectFile(), 
        			animationElement.getAttribute(defaultValueBlinkingAtt));
        
        if(defaultValueBlinking!=null){
            
            //creating the blinking value modifier for the default value
            addBlinkingValue(defaultValueBVModifierId, defaultValueBlinking.getId(), 
            	animationElement.getAttribute(attributeAtt), 
        			animationElement.getAttribute(defaultValue1Att), 
    					animationElement.getAttribute(defaultValue2Att));
        }
        
        //creating the children
        ChildrenAttributeOnState child=null;
        Element childElement;
        boolean isUsed=false;
        LinkedList<String> realValuesNames=new LinkedList<String>();

        for(Node cur=animationElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
            
            if(cur instanceof Element){
            	
            	childElement=(Element)cur;
            	
                //whether the child should be used or not
            	isUsed=Boolean.parseBoolean(childElement.getAttribute(usedAtt));

                if(isUsed){

                    //creating the new child
                    child=new ChildrenAttributeOnState(childElement);

                    //registering the child
                    childrenMap.put(AnimationsToolkit.normalizeEnumeratedValue(
                    		childElement.getAttribute(valueAtt)), child);
                    realValuesNames.add(childElement.getAttribute(valueAtt));
                }
            }
        }
        
        //if we are in the test version, we store information on the tag
        if(picture.getMainDisplay().isTestVersion()){
            
            TestTagInformation info=new TestTagInformation(
            		picture, animationElement.getAttribute(tagAtt), realValuesNames);
            
            dataNamesToInformation.put(animationElement.getAttribute(tagAtt), info);
        }
    }
    
    /**
     * the method called when the data to which the listener is registered, is modified
     * @param evt an event
     * @return the runnable that should be executed to apply the modifications
     */
    public Runnable dataChanged(DataEvent evt) {
    	
        Runnable runnable=null;
        
    	if(! animationElement.getAttribute(attributeAtt).equals("")){
    		
    	       boolean isTagInvalid=false;
    	        
    	        //deactivates all the blinking value modifiers
    	        deactivateBlinkingValueModifiers();

    	        //getting the value of the tag
    	        String value=(String)getData(
    	        		animationElement.getAttribute(tagAtt));

    	        //getting the child corresponding to this value
    	        ChildrenAttributeOnState child=childrenMap.get(value);
    	        
    	        //the new value
    	        String newValue="";

    	        //whether the attribute can be set, i.e. whether the value 
    	        //of the tag corresponds to a blinking color,
    	        //if so, the blinking color is activated and no value is set
    	        boolean canSetAttribute=false;
    	        
    	        if(value==null){
    	        	
    	            //the state of the tag is invalid//
    	        	if(invalidValueBlinking!=null){
    	        		
    	        		activateBlinkingValueModifier(invalidValueBVModifierId);
    	        		newValue="";
    	        		
    	        	}else{
    	        		
    	        	    newValue=animationElement.getAttribute(invalidValue1Att);
    	                canSetAttribute=true;
    	        	}

    	        	isTagInvalid=true;

    	        }else if(child!=null){

    	            if(child.getBlinking()!=null){
    	            	
    	            	//activates the blinking of the fill attribute for the current value of the tag
    	            	activateBlinkingValueModifier(child.getBlinkingModifierId());
    	            	
    	            }else{
    	            	
    	            	newValue=child.getAttributeValue1();
    	            	canSetAttribute=true;
    	            }
    	            
    	        }else{
    	        	
    	            //setting the default value//
    	        	if(defaultValueBlinking!=null){
    	        		
    	        		activateBlinkingValueModifier(defaultValueBVModifierId);
    	        		
    	        	}else{
    	        		
    	        	    newValue=animationElement.getAttribute(defaultValue1Att);
    	                canSetAttribute=true;
    	        	}
    	        }

    	        if(canSetAttribute && newValue!=null && 
    	        		! newValue.equals(parentElement.getAttribute(
    	        				animationElement.getAttribute(attributeAtt)))){
    	        	
    	            final String fnewValue=newValue;

    	            //the runnable that will be returned
    	            runnable=new Runnable(){

    		            public void run() {

    		                //sets the attribute
    		                parentElement.setAttribute(
    		                		animationElement.getAttribute(attributeAtt), fnewValue);
    		            } 
    	            };
    	        }
    	        
    	        setInvalidTag(isTagInvalid);
    	}

        return runnable;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#dispose()
     */
    public void dispose() {}

    /**
     * the class of the children
     * 
     * @author ITRIS, Jordi SUC
     */
    protected class ChildrenAttributeOnState{

    	/**
    	 * the value element
    	 */
        private Element valueElement;
        
        /**
         * the corresponding blinking
         */
        private Blinking blinking;
        
        /**
         * the id of the related blinking modifier
         */
        private String blinkingModifierId="";
        
        /**
         * the constructor of the class
         * @param valueElement the value element
         */
        protected ChildrenAttributeOnState(Element valueElement){
            
        	this.valueElement=valueElement;
            
            blinkingModifierId=valueElement.getAttribute(valueAtt)+"-blinking";
            blinking=picture.getMainDisplay().getColorsAndBlinkingsToolkit().
            	getBlinking(picture.getAnimActionsHandler().getProjectFile(), 
            			valueElement.getAttribute(blinkingAtt));
            
            if(blinking!=null){
                
                //registering the blinking
                addBlinkingValue(blinkingModifierId, 
                		valueElement.getAttribute(blinkingAtt), 
                			animationElement.getAttribute(attributeAtt), 
                				valueElement.getAttribute(attributeValue1Att), 
                					valueElement.getAttribute(attributeValue2Att));
            }
        }

        /**
         * @return the first value for the attribute
         */
        protected String getAttributeValue1() {
            return valueElement.getAttribute(attributeValue1Att);
        }
        
        /**
         * @return the second value for the attribute
         */
        protected String getAttributeValue2() {
            return valueElement.getAttribute(attributeValue2Att);
        }
        
        /**
         * @return Returns the blinking.
         */
        protected Blinking getBlinking() {
            return blinking;
        }
        
        /**
         * @return the id of the blinking
         */
        protected String getBlinkingId() {
            return valueElement.getAttribute(blinkingAtt);
        }
        
        /**
         * @return the id of the related blinking modifier
         */
        protected String getBlinkingModifierId() {
            return blinkingModifierId;
        }
    }
}
