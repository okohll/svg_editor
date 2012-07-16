/*
 * Created on 18 mars 2005
 */
package fr.itris.glips.rtda.animations;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.test.*;

/**
 * the listener to the changes of data for the "color on measure" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class ColorOnMeasure extends DataChangedListener{
    
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
     * the outOfRange and invalid values
     */
    private String invalidValueFill="", outOfRangeFill="", invalidValueStroke="", outOfRangeStroke="";
    
    /**
     * the list of the objects containing all information about the child of an animation node
     */
    private List<AnimationChildValues> childrenAttributeValues=
    	new LinkedList<AnimationChildValues>();
    
    /**
     * whether the main tag attribute is described by a function or not
     */
    private boolean isTagFunction=false;

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public ColorOnMeasure(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        //getting the name specified in the "tag" attribute and adding it to the set of the data names
        tagAttributeValue=animationElement.getAttribute("tag");

        if(AnimationsToolkit.isFunction(tagAttributeValue)){
 
            tagAttributeValue=getNewId(tagAttributeValue);
            isTagFunction=true;
        }

        addData(tagAttributeValue);
        
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

        //storing the values of the attributes of the animation node and creating the associated blinking color value computer
        invalidValueFill=animationElement.getAttribute("invalidValueFill");
        invalidValueFill=(invalidValueFill.equals("")?"none":invalidValueFill);
        addBlinkingColorValueComputer(parentElement, "invalidValueFill", invalidValueFill, "fill");
        
        outOfRangeFill=animationElement.getAttribute("outOfRangeFill");
        outOfRangeFill=(outOfRangeFill.equals("")?"none":outOfRangeFill);
        addBlinkingColorValueComputer(parentElement, "outOfRangeFill", outOfRangeFill, "fill");

        invalidValueStroke=animationElement.getAttribute("invalidValueStroke");
        invalidValueStroke=(invalidValueStroke.equals("")?"none":invalidValueStroke);
        addBlinkingColorValueComputer(parentElement, "invalidValueStroke", invalidValueStroke, "stroke");
        
        outOfRangeStroke=animationElement.getAttribute("outOfRangeStroke");
        outOfRangeStroke=(outOfRangeStroke.equals("")?"none":outOfRangeStroke);
        addBlinkingColorValueComputer(parentElement, "outOfRangeStroke", outOfRangeStroke, "stroke");
        
        //getting children information
        int i=0;
        AnimationChildValues animationChildValues=null;
        
        for(Node cur=animationElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
            
            if(cur instanceof Element){
                
                //creating the animations child values
                animationChildValues=new AnimationChildValues(cur.getNodeName()+i, (Element)cur);
                
                if(animationChildValues.isUsed()){
                    
                    childrenAttributeValues.add(animationChildValues);
                }
                
                i++;
            }
        }

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
        
        System.out.println("Limits="+tagMinValue+" "+tagMaxValue);
    }
    
    /**
     * the method called when the data to which the listener is registered, is modified
     * @param evt an event
     * @return the runnable that should be executed to apply the modifications
     */
    public Runnable dataChanged(DataEvent evt) {
    	
    	System.out.println("data changed");
    	
        Runnable runnable=null;
        
        //whether the current state of the listener is invalid or not
        boolean isInvalidState=false;
        
        Map<String, Object> dataNameToValue=evt.getDataNameToValue();

        if(dataNameToValue!=null){

            //deactivates all the blinking value modifiers
            deactivateBlinkingValueModifiers();
            
            //the new fill and stroke values
            String newFillValue="", newStrokeValue="";
            
            //whether the fill or stroke attribute can be set, i.e. whether the tag value corresponds to a blinking color,
            //if so, the blinking color is activated and no color is set
            boolean canSetFillAttribute=false, canSetStrokeAttribute=false;
            
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

            if(! Double.isNaN(tagValue) && ! Double.isNaN(tagMinValue) 
                    && ! Double.isNaN(tagMaxValue) && tagMaxValue>=tagMinValue){

                //whether the value of the tag is in one interval
                boolean isInInterval=false;
                
                if(tagValue>=tagMinValue && tagValue<=tagMaxValue){
                	
                	//refreshing the min and max value of each interval
                	for(AnimationChildValues child : childrenAttributeValues){
                		
                		child.refreshValues(dataNameToValue);
                	}

                    //for each child of the animation node, checks if the current value is inside a child's interval//
                    for(AnimationChildValues child : childrenAttributeValues){
                    	
                        if(child!=null){
                            
                            isInvalidState=isInvalidState || child.isChildInvalid();
                        }
                        
                        if(child!=null && child.isInInterval(dataNameToValue, tagValue)){
                            
                            isInInterval=true;
                            
                            //sets the new fill and stroke value//
                            
                            //the ids of the modifiers
                            String fillIdModifier=child.getChildId()+"-fill", strokeIdModifier=child.getChildId()+"-stroke";
                            
                            if(itemNameToBlinkingValueModifier.containsKey(fillIdModifier)){
                            	
                            	//activates the blinking of the fill attribute for the current value of the tag
                            	activateBlinkingValueModifier(fillIdModifier);
                            	
                            }else{
                            	
                            	newFillValue=child.getFillValue();

                                if(newFillValue==null){
                                    
                                    newFillValue="";
                                }
                                
                            	canSetFillAttribute=true;
                            }
                            
                            if(itemNameToBlinkingValueModifier.containsKey(strokeIdModifier)){
                            	
                            	//activates the blinking of the stroke attribute for the current value of the tag
                            	activateBlinkingValueModifier(strokeIdModifier);
                            	
                            }else{
                            	
                            	newStrokeValue=child.getStrokeValue();

                                if(newStrokeValue==null){
                                    
                                    newStrokeValue="";
                                }
                                
                            	canSetStrokeAttribute=true;
                            }

                            break;
                        }
                    } 
                }
                
                //the tag value is out of range, as it is not in any interval
                if(! isInInterval || tagValue<tagMinValue || tagValue>tagMaxValue){
                    
                	if(itemNameToBlinkingValueModifier.containsKey("outOfRangeFill")){
                		
                		activateBlinkingValueModifier("outOfRangeFill");
                		
                	}else{
                		
                        newFillValue=outOfRangeFill;
                        canSetFillAttribute=true;
                	}
                	
                	if(itemNameToBlinkingValueModifier.containsKey("outOfRangeStroke")){
                		
                		activateBlinkingValueModifier("outOfRangeStroke");
                		
                	}else{
                		
                        newStrokeValue=outOfRangeStroke;
                        canSetStrokeAttribute=true;
                	}
                }

            }else{

                //the state of the tag is invalid//
            	
            	if(itemNameToBlinkingValueModifier.containsKey("invalidValueFill")){
            		
            		activateBlinkingValueModifier("invalidValueFill");
            		
            	}else{
            		
                    newFillValue=invalidValueFill;
                    canSetFillAttribute=true;
            	}
            	
            	if(itemNameToBlinkingValueModifier.containsKey("invalidValueStroke")){
            		
            		activateBlinkingValueModifier("invalidValueStroke");
            		
            	}else{
            		
                    newStrokeValue=invalidValueStroke;
                    canSetStrokeAttribute=true;
            	}
            	
                isInvalidState=true;
            }

            //setting the new value for the attribute
            if(canSetFillAttribute || canSetStrokeAttribute){
                
                final String fnewFillValue=newFillValue, fnewStrokeValue=newStrokeValue;
                final boolean fcanSetFillAttribute=canSetFillAttribute;
                final boolean fcanSetStrokeAttribute=canSetStrokeAttribute;

                runnable=new Runnable(){

    	            public void run() {

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
        }
        
        return runnable;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#dispose()
     */
    public void dispose() {}
    
    /**
     * the class storing information on a the child of an animation node that describes an interval
     * 
     * @author ITRIS, Jordi SUC
     */
    protected class AnimationChildValues{
        
        /**
         * whether this child should be considered or not
         */
        private boolean isUsed=false;
        
        /**
         * whether the value of the tag of the animation node could be equal to the min
         */
        private boolean equal1=false;
        
        /**
         * whether the value of the tag of the animation node could be equal to the max
         */
        private boolean equal2=false;
        
        /**
         * the min and max values
         */
        private DataLimit min, max;
        
        /**
         * the value of the min and the max
         */
        private double minValue=Double.NaN, maxValue=Double.NaN;
        
        /**
         * the possible fill and stroke values if the value of the tag of the animation node is in this interval
         */
        private String fill="", stroke="";
        
        /**
         * the id of the child
         */
        private String childId="";
        
        /**
         * the constructor of the class
         * @param childId the id of the child
         * @param animationChildElement a child of the animation node
         */
        protected AnimationChildValues(String childId, Element animationChildElement){
            
            this.childId=childId;
            
            if(animationChildElement!=null){
                
                //whether this child should be used
                String used=animationChildElement.getAttribute("used");
                
                if(used!=null && used.equals("true")){
                    
                    isUsed=true;  
                }
                
                //getting the equal1 and equal2 attributes
                String 	eq1=animationChildElement.getAttribute("equal1"),
                			eq2=animationChildElement.getAttribute("equal2");
                
                if(eq1!=null && ! eq1.equals("false")){
                    
                    equal1=true;
                }
                
                if(eq2!=null && ! eq2.equals("false")){
                    
                    equal2=true;
                }
                
                //the fill and stroke attributes
                fill=animationChildElement.getAttribute("fill");
                fill=(fill.equals("")?"none":fill);
                addBlinkingColorValueComputer(parentElement, childId+"-fill", fill, "fill");
                
                stroke=animationChildElement.getAttribute("stroke");
                stroke=(stroke.equals("")?"none":stroke);
                addBlinkingColorValueComputer(parentElement, childId+"-stroke", stroke, "stroke");

                //getting the min and max
                String 	minStr=animationChildElement.getAttribute("min"),
                			maxStr=animationChildElement.getAttribute("max");
                
                min=new DataLimit(picture, "", minStr, true);
                max=new DataLimit(picture, "", maxStr, false);
                
                //computing the values of the min and max
                if(min.isAbsoluteValue()){
                    
                    setMinValue(min.getAbsoluteValue());
                }
                
                if(max.isAbsoluteValue()){
                    
                    setMaxValue(max.getAbsoluteValue());
                }
                
                if(min.isInfinite()){
                    
                    setMinValue(Double.NEGATIVE_INFINITY);
                }
                
                if(max.isInfinite()){
                    
                    setMaxValue(Double.POSITIVE_INFINITY);
                }
                
                if(min.isTag()){
                    
                    addData(min.getTag());
                }
                
                if(max.isTag()){
                    
                    addData(max.getTag());
                }
                
                refreshPercentValues();
                
                //if we are in the test version, we store information on the tag
                if(picture.getMainDisplay().isTestVersion()){
                    
                    TestTagInformation info;
                    
                    if(min.isTag()){
                        
                        info=new TestTagInformation(picture, min.getTag(), null);
                        dataNamesToInformation.put(min.getTag(), info);
                    }
                    
                    if(max.isTag()){
                        
                        info=new TestTagInformation(picture, max.getTag(), null);
                        dataNamesToInformation.put(max.getTag(), info);
                    }
                }
            }
        }

        /**
         * @return the id of this child object
         */
        public String getChildId() {
            return childId;
        }
        
        /**
         * @return whether the child tags are invalid
         */
        public boolean isChildInvalid(){
            
            return (Double.isNaN(minValue) || Double.isNaN(maxValue));
        }
        
        /**
         * @param maxValue The maxValue to set.
         */
        protected synchronized void setMaxValue(double maxValue) {
            this.maxValue = maxValue;
        }
        
        /**
         * @param minValue The minValue to set.
         */
        protected synchronized void setMinValue(double minValue) {
            this.minValue = minValue;
        }
        
        /**
         * refreshes the values
         * @param dataNameToInfo the map containing the new values
         */
        public void refreshValues(Map<String, Object> dataNameToInfo){
            
            //re-computing the values of the min and max//
            
            if(! Double.isNaN(tagMinValue) && ! Double.isNaN(tagMaxValue)){
                
            	refreshPercentValues();
                
                //computing the tag value
                if(min.isTag() && dataNameToInfo.containsKey(min.getTag())){
                    
                	setMinValue(Toolkit.getNumber(getData(min.getTag())));
                }
                
                if(max.isTag() && dataNameToInfo.containsKey(max.getTag())){
                	
                    System.out.println("SETTING MAX="+getData(max.getTag())+" "+Toolkit.getNumber(getData(max.getTag())));
                    
                	setMaxValue(Toolkit.getNumber(getData(max.getTag())));
                }
                
                System.out.println("CHILD="+childId+" "+minValue+" "+maxValue+" "+
                		min.isTag()+" "+max.isTag());
            }
        }
        
        /**
         * refreshes the percent values
         */
        protected void refreshPercentValues(){
        	
            //computing the percentage values
            double range=tagMaxValue-tagMinValue;
            
            if(min.isPercentValue()){
                
                setMinValue(tagMinValue+range*min.getPercentValue()/100);
            }
            
            if(max.isPercentValue()){
                
                setMaxValue(tagMinValue+range*max.getPercentValue()/100);
            }
            
            System.out.println("CHILD="+childId+" "+minValue+" "+maxValue+" "+
            		min.isPercentValue()+" "+max.isPercentValue());
        }

        /**
         * whether the given value is in this interval
         * @param dataNameToValue the map containing the new values
         * @param theTagValue the value of the main tag of the animation node
         * @return whether the given value is in this interval
         */
        public boolean isInInterval(Map<String, Object> dataNameToValue, double theTagValue){
            System.out.println("isInInterval="+dataNameToValue);
            //tests if the given value is in the interval
            boolean isInInterval=true;
            
            if(dataNameToValue!=null){

                if(! Double.isNaN(tagMinValue) && ! Double.isNaN(tagMaxValue) && 
                        ! Double.isNaN(minValue) && ! Double.isNaN(maxValue)){

    	            if(equal1){
    	                
    	                isInInterval=isInInterval && (minValue<=theTagValue);
    	                
    	            }else{
    	                
    	                isInInterval=isInInterval && (minValue<theTagValue);
    	            }
    	            
    	            if(equal2){
    	                
    	                isInInterval=isInInterval && (maxValue>=theTagValue);
    	                
    	            }else{
    	                
    	                isInInterval=isInInterval && (maxValue>theTagValue);
    	            }
                
                }else{
                    
                    isInInterval=false;
                }
            }

            return isInInterval;
        }
        
        /**
         * @return Returns the equal1.
         */
        public boolean isEqual1() {
            return equal1;
        }
        
        /**
         * @return Returns the equal2.
         */
        public boolean isEqual2() {
            return equal2;
        }
        
        /**
         * @return Returns the isUsed.
         */
        public boolean isUsed() {
            return isUsed;
        }
        
        /**
         * @return Returns the max.
         */
        public DataLimit getMax() {
            return max;
        }
        /**
         * @return Returns the min.
         */
        public DataLimit getMin() {
            return min;
        }
        
        /**
         * @return the fill value
         */
        public String getFillValue() {
            return fill;
        }
        
        /**
         * @return the stroke value
         */
        public String getStrokeValue() {
            return stroke;
        }
        
    }
}