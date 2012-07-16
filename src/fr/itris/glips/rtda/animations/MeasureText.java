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
import java.text.*;

/**
 * the listener to the changes of data for the "text on mesure" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class MeasureText extends DataChangedListener{
    
    /**
     * the value of the tag attribute of the animation node
     */
    private String tagAttributeValue="";
    
    /**
     * the text for the invalid tag value
     */
    private String invalidText="";
    
    /**
     * the pattern for displaying the value
     */
    private String pattern="";
    
    /**
     * the last text value
     */
    private String lastTextValue;
    
    /**
     * the format for writing the received values
     */
    private DecimalFormat patternFormat;
    
    /**
     * whether the listener is enabled or not
     */
    protected boolean isEnabled=true;

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public MeasureText(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        if(animationElement!=null){
        	
            //getting the name specified in the "tag" attribute and adding it to the set of the data names
            tagAttributeValue=animationElement.getAttribute("tag");
            addData(tagAttributeValue);

            //storing the values of the attributes of the animation node
            invalidText=animationElement.getAttribute("invalidText");
            
            //getting the pattern
            pattern=animationElement.getAttribute("pattern");
            
            try{
                //sets the format used to convert numbers into a string
                DecimalFormatSymbols symbols=new DecimalFormatSymbols();
                symbols.setDecimalSeparator('.');
                patternFormat=new DecimalFormat(pattern, symbols);
            }catch (Exception ex){patternFormat=null;}
            
            if(patternFormat==null){
                
                patternFormat=FormatStore.format;
            }
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
        double newValue=Toolkit.getNumber(getData(tagAttributeValue));

        //the new value
        String newTextValue="";
        
        if(Double.isNaN(newValue)){
        	
            //the state of the tag is invalid
            newTextValue=invalidText;
            isInvalidState=true;
            
        }else{

            newTextValue=patternFormat.format(newValue);
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

