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
import java.awt.geom.*;

/**
 * the listener to the changes of data for the "translation" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class Translation extends DataChangedListener{
    
    /**
     * the value of the tag attribute of the animation node
     */
    private String tagAttributeValue="";
    
    /**
     * the direction of the scale
     */
    private String direction="";
    
    /**
     * the tag min and tag max
     */
    private DataLimit tagMin, tagMax;
    
    /**
     * the computed value of the tag, tagMin and tagMax attributes
     */
    private double tagMinValue=Double.NaN, tagMaxValue=Double.NaN;
    
    /**
     * the reference, min and max points
     */
    private Point2D.Double refPoint=new Point2D.Double(), 
    	minPoint=new Point2D.Double(), maxPoint=new Point2D.Double();
    
    /**
     * the translation values
     */
    private double minTranslationX=0, minTranslationY=0, maxTranslationX=0, maxTranslationY=0;
    
    /**
     * whether the main tag attribute is described by a function or not
     */
    private boolean isTagFunction=false;

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public Translation(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        isTransformAnimation=true;
        affineTransform=new AffineTransform();
        
        //getting the name specified in the "tag" attribute and adding it to the set of the data names
        tagAttributeValue=animationElement.getAttribute("tag");

        if(AnimationsToolkit.isFunction(tagAttributeValue)){
 
            tagAttributeValue=getNewId(tagAttributeValue);
            isTagFunction=true;
        }

        addData(tagAttributeValue);
        
        //getting the direction attribute
        direction=animationElement.getAttribute("direction");

        //getting the point values
        String 	xRef=animationElement.getAttribute("xRef"),
        			yRef=animationElement.getAttribute("yRef"),
        			xMin=animationElement.getAttribute("xMin"),
        			yMin=animationElement.getAttribute("yMin"),
        			xMax=animationElement.getAttribute("xMax"),
        			yMax=animationElement.getAttribute("yMax");
        
        double x=0, y=0;
        
        try{
            x=Double.parseDouble(xRef);
            y=Double.parseDouble(yRef);
        }catch (Exception ex){x=0; y=0;}
        
        refPoint.x=(int)x;
        refPoint.y=(int)y;
        
        try{
            x=Double.parseDouble(xMin);
            y=Double.parseDouble(yMin);
        }catch (Exception ex){x=0; y=0;}
        
        minPoint.x=(int)x;
        minPoint.y=(int)y;
        
        try{
            x=Double.parseDouble(xMax);
            y=Double.parseDouble(yMax);
        }catch (Exception ex){x=0; y=0;}
        
        maxPoint.x=(int)x;
        maxPoint.y=(int)y;

        //computing the translation values
        minTranslationX=minPoint.x-refPoint.x;
        minTranslationY=minPoint.y-refPoint.y;
        
        maxTranslationX=maxPoint.x-refPoint.x;
        maxTranslationY=maxPoint.y-refPoint.y;

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
    
    @Override
    public void initializeWhenCanvasDisplayed() {

        //getting the initial value of the transform attribute
        initialAffineTransform=AnimationsToolkit.getTransform(picture, parentElement);
    }
    
    /**
     * the method called when the data to which the listener is registered, is modified
     * @param evt an event
     * @return the runnable that should be executed to apply the modifications
     */
    public Runnable dataChanged(DataEvent evt) {

    	//the runnable that will be returned
    	Runnable runnable=null;
    	
        //whether the current state of the listener is invalid or not
        boolean isInvalidState=false;
        
        //getting the tag value
        double tagValue=0;
        
        //deactivates all the blinking value modifiers
        deactivateBlinkingValueModifiers();

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

        if(! Double.isNaN(tagValue) && ! Double.isNaN(tagMinValue) 
               && ! Double.isNaN(tagMaxValue) && tagMaxValue>=tagMinValue 
               		&& tagValue>=tagMinValue && tagValue<=tagMaxValue){

            double ratio=0, amplitude=Math.abs(tagMaxValue-tagMinValue);
            
            if(amplitude>0){

                //computing the new translation values
                double newTranslationX=0;
                double newTranslationY=0;
                
                ratio=Math.abs(tagValue/amplitude);
                newTranslationX=(maxTranslationX-minTranslationX)*ratio+minTranslationX;
                newTranslationY=(maxTranslationY-minTranslationY)*ratio+minTranslationY;
                
                //setting the current scale
		        if(direction.equals("x")){

		            newTranslationY=0;
		
		        }else if(direction.equals("y")){
		            
		            newTranslationX=0;
		        }

		        //creates the new transform
		        affineTransform=AffineTransform.getTranslateInstance(
		        	newTranslationX, newTranslationY);

		        //getting the new affine transform for the element
		        final AffineTransform newAffineTransform=picture.getMainDisplay().getAnimationsHandler().
		        	getTransformHandler().computeTransform(parentElement);

		        if(! newAffineTransform.equals(AnimationsToolkit.getTransform(
		        		picture, parentElement))){
		        	
			        runnable=new Runnable(){
			        	
			        	public void run() {

	                        AnimationsToolkit.setTransform(
	                        		picture, parentElement, newAffineTransform);
			        	}
			        };
		        }
            }
            
        }else{
            
            isInvalidState=true;
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