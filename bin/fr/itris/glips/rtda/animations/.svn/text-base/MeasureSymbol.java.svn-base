/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.animations;

import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.test.*;
import java.util.*;

/**
 * the listener to the changes of data for the "symbol on measure" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class MeasureSymbol extends DataChangedListener{

    /**
     * the set of the children of the parent element
     */
    private Set<Element> childrenSet=new HashSet<Element>();
    
    /**
     * the map associating the id of a child to this child
     */
    private Map<String, Node> idToChild=new HashMap<String, Node>();
    
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
     * the symbols for the invalid an out of range symbols
     */
    private String invalidSymbol="", outOfRangeSymbol="";
    
    /**
     * the last text value
     */
    private String lastSymbol=null;
    
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
    public MeasureSymbol(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        //getting the name specified in the "tag" attribute and adding it to the set of the data names
        tagAttributeValue=animationElement.getAttribute("tag");

        if(AnimationsToolkit.isFunction(tagAttributeValue)){
 
            tagAttributeValue=getNewId(tagAttributeValue);
            isTagFunction=true;
        }

        addData(tagAttributeValue);
        
        //storing the children of the parent element that will be modified
        String theId="";
        
        for(Node child=parentElement.getFirstChild(); child!=null; child=child.getNextSibling()){
            
            if(child instanceof Element && ! child.getNodeName().startsWith("rtda:")){

                childrenSet.add((Element)child);
                
                //getting the id of the child
                theId=Toolkit.toUnURLValue(((Element)child).getAttribute("id"));
                
                if(theId!=null && ! theId.equals("")){
                    
                    idToChild.put(theId, child);
                }
            }
        }
        
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
        
        //storing the values of the attributes of the animation node
        invalidSymbol=Toolkit.toUnURLValue(animationElement.getAttribute("invalidSymbol"));
        outOfRangeSymbol=Toolkit.toUnURLValue(
        		animationElement.getAttribute("outOfRangeSymbol"));
        
        //getting children information
        int i=0;
        AnimationChildValues animationChildValues=null;
        
        for(Node cur=animationElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
            
            if(cur instanceof Element){
                
                //creating the animations child values
                animationChildValues=new AnimationChildValues(
                		cur.getNodeName()+i, (Element)cur);
                
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
        
        //hiding all the children of the parent element
        for(Element child : childrenSet){

            if(child!=null){
                
            	child.setAttribute("opacity", "0");
            }
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
        Map<String, Object> dataNameToValue=evt.getDataNameToValue();

        if(dataNameToValue!=null){
            
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
        }

        //the new symbol
        String newSymbol="";
        
        //whether the tag value is in one of the intervals or not
        boolean isInInterval=false;

        if(! Double.isNaN(tagValue) && ! Double.isNaN(tagMinValue) 
            && ! Double.isNaN(tagMaxValue) && tagMaxValue>=tagMinValue){
        	
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

                    newSymbol=child.getSymbol();
                    isInInterval=true;
                    break;
                }
            }
            
            if(! isInInterval){
                
                //the tag value is out of range
                newSymbol=outOfRangeSymbol;
            }
            
        }else{

            //the state is invalid//
            newSymbol=invalidSymbol;
            isInvalidState=true;
        }

        //displaying the child corresponding to the new symbol
        if(newSymbol!=null && ! newSymbol.equals("") && 
        		! newSymbol.equals(lastSymbol)){
            
            final String fnewSymbol=newSymbol;

            runnable=new Runnable(){

	            public void run() {

	                Element lastChild=null, child=null;
	                
	                //getting the last and new children corresponding to the ids
	                try{
	                    lastChild=(Element)idToChild.get(lastSymbol);
	                }catch (Exception ex){}
	                
	                try{
	                    child=(Element)idToChild.get(fnewSymbol);
	                }catch (Exception ex){}
	                
	                //hiding the last displayed child
	                if(lastChild!=null){
	                    
	                	lastChild.setAttribute("opacity", "0");
	                }
	                
	                if(child!=null){
	                    
	                	child.setAttribute("opacity", "1");
	                }
	                
	                lastSymbol=fnewSymbol;
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
        private DataLimit min=null, max=null;
        
        /**
         * the value of the min and the max
         */
        private double minValue=Double.NaN, maxValue=Double.NaN;
        
        /**
         * the symbol linked with this child
         */
        private String symbol="";
        
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
                
                //the symbol attribute
                symbol=Toolkit.toUnURLValue(animationChildElement.getAttribute("symbol"));

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
                    
                	setMaxValue(Toolkit.getNumber(getData(max.getTag())));
                }
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
        }

        /**
         * whether the given value is in this interval
         * @param dataNameToValue the map containing the new values
         * @param theTagValue the value of the main tag of the animation node
         * @return whether the given value is in this interval
         */
        public boolean isInInterval(Map<String, Object> dataNameToValue, double theTagValue){
            
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
         * @return Returns the symbol
         */
        public String getSymbol() {
            return symbol;
        }
    }
}