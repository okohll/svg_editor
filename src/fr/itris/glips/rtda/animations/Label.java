package fr.itris.glips.rtda.animations;

import org.w3c.dom.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.test.*;
import java.util.*;

/**
 * the listener to the changes of data for the "label" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class Label extends DataChangedListener{
    
	/**
	 * attribute names
	 */
	private final static String tagAtt="tag", invalidTextAtt="invalidText", 
		defaultTextAtt="defaultText", textAtt="text", usedAtt="used", valueAtt="value";
    
    /**
     * the map of the text labels associating a possible value of a tag to its corresponding label
     */
    private Map<String, String> labelsMap=new HashMap<String, String>();

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public Label(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        //getting the name specified in the "tag" attribute 
    	//and adding it to the set of the data names
        addData(animationElement.getAttribute(tagAtt));
        
        //fills the maps with the text values
        String value="", text="";
        Element childElement;
        boolean isUsed=false;
        LinkedList<String> realValuesNames=new LinkedList<String>();

        for(Node cur=animationElement.getFirstChild(); 
        	cur!=null; cur=cur.getNextSibling()){
            
            if(cur instanceof Element){
            	
            	childElement=(Element)cur;
            	
                //whether the child should be used or not
        		isUsed=Boolean.parseBoolean(childElement.getAttribute(usedAtt));

                if(isUsed){
                    
                    //getting one of the possible values of the tag
                    value=childElement.getAttribute(valueAtt);
                    
                    if(! value.equals("")){
                        
                        //getting the text value for the given tag value
                        text=childElement.getAttribute(textAtt);
                        realValuesNames.add(value);
                        value=AnimationsToolkit.normalizeEnumeratedValue(value);
                        labelsMap.put(value, text);
                    }
                }
            }
        }
        
        //if we are in the test version, we store information on the tag
        if(picture.getMainDisplay().isTestVersion()){
            
            TestTagInformation info=new TestTagInformation(
            		picture, animationElement.getAttribute(tagAtt), realValuesNames);
            
            dataNamesToInformation.put(
            		animationElement.getAttribute(tagAtt), info);
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
        
        //getting the value of the tag
        String value=(String)getData(
        		animationElement.getAttribute(tagAtt));
        
        //the new text value
        String newTextValue="";
        
        if(value==null){
        	
            //the state of the tag is invalid//
            newTextValue=animationElement.getAttribute(invalidTextAtt);
            isInvalidState=true;
            
        }else if(labelsMap.containsKey(value)){

            newTextValue=labelsMap.get(value);
            
        }else{
            
            newTextValue=animationElement.getAttribute(defaultTextAtt);
        }

        if(newTextValue!=null){
        	
            final String fnewTextValue=newTextValue;

            //the runnable that will be returned
            runnable=new Runnable(){

	            public void run() {

	                AnimationsToolkit.setText(parentElement, fnewTextValue);
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
