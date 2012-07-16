/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.animations;

import org.w3c.dom.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.test.*;

/**
 * the listener to the changes of data for the "text" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class Text extends DataChangedListener{
    
    /**
     * the value of the tag attribute of the animation node
     */
    private String tagAttributeValue="";
    
    /**
     * the text for the invalid tag value
     */
    private String invalidText="";
    
    /**
     * the last text value
     */
    private String lastTextValue=null;

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public Text(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        if(animationElement!=null){
        	
            //getting the name specified in the "tag" attribute and adding it to the set of the data names
            tagAttributeValue=animationElement.getAttribute("tag");
            addData(tagAttributeValue);

            //storing the values of the attributes of the animation node
            invalidText=animationElement.getAttribute("invalidText");
        }
        
        //if we are in the test version, we store information on the tag
        if(picture.getMainDisplay().isTestVersion()){
            
            TestTagInformation info=new TestTagInformation(picture, tagAttributeValue, null);
            
            dataNamesToInformation.put(tagAttributeValue, info);
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
        String value=(String)getData(tagAttributeValue);

        //the new value
        String newTextValue="";
        
        if(value==null){
        	
            //the state of the tag is invalid//
        	
            newTextValue=invalidText;
            isInvalidState=true;
            
        }else{

            newTextValue=value;
        }

        if(newTextValue!=null && ! newTextValue.equals(lastTextValue)){
        	
            final String fnewTextValue=newTextValue;

            //the runnable that will be returned
            runnable=new Runnable(){

	            public void run() {

	                AnimationsToolkit.setText(parentElement, fnewTextValue);
	                lastTextValue=fnewTextValue;
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

