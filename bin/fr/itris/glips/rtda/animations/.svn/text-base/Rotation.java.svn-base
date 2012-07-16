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
 * the listener to the changes of data for the "rotation" animation node
 * @author ITRIS, Jordi SUC
 */
public class Rotation extends DataChangedListener{
    
    /**
     * the value of the tag attribute of the animation node
     */
    private String tagAttributeValue="";
    
    /**
     * whether the rotation is clockwise or not
     */
    private boolean clockwise=false;
    
    /**
     * the tag min and tag max
     */
    private DataLimit tagMin, tagMax;
    
    /**
     * the computed value of the tag, tagMin and tagMax attributes
     */
    private double tagMinValue=Double.NaN, tagMaxValue=Double.NaN;
    
    /**
     * the center, reference, min and max points
     */
    private Point2D centerPoint, absoluteCenterPoint, refPoint, minPoint, maxPoint;
    
    /**
     * the scales 
     */
    private double minAngle=0, maxAngle=0, refAngle=0;
    
    /**
     * whether the main tag attribute is described by a function or not
     */
    private boolean isTagFunction=false;

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public Rotation(SVGPicture picture, Element animationElement){

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
        
        //getting the clockwise attribute
        clockwise=animationElement.getAttribute("clockwise").equals("true");
        
        //getting the point values
        String 	xCenter=animationElement.getAttribute("xCenter"),
        			yCenter=animationElement.getAttribute("yCenter"),
        			xRef=animationElement.getAttribute("xRef"),
        			yRef=animationElement.getAttribute("yRef"),
        			xMin=animationElement.getAttribute("xMin"),
        			yMin=animationElement.getAttribute("yMin"),
        			xMax=animationElement.getAttribute("xMax"),
        			yMax=animationElement.getAttribute("yMax");
        
        double x=0, y=0;
        
        try{
            x=Double.parseDouble(xCenter);
            y=Double.parseDouble(yCenter);
        }catch (Exception ex){x=0; y=0;}
        
        centerPoint=new Point2D.Double(x, y);
        
        try{
            x=Double.parseDouble(xRef);
            y=Double.parseDouble(yRef);
        }catch (Exception ex){x=0; y=0;}
        
        refPoint=new Point2D.Double(x, y);
        
        try{
            x=Double.parseDouble(xMin);
            y=Double.parseDouble(yMin);
        }catch (Exception ex){x=0; y=0;}
        
        minPoint=new Point2D.Double(x, y);
        
        try{
            x=Double.parseDouble(xMax);
            y=Double.parseDouble(yMax);
        }catch (Exception ex){x=0; y=0;}
        
        maxPoint=new Point2D.Double(x, y);

        //computing the angles 
        refAngle=Math.acos((refPoint.getX()-centerPoint.getX())/
        		Math.sqrt(Math.pow(refPoint.getX()-centerPoint.getX(), 2)+
        				Math.pow(refPoint.getY()-centerPoint.getY(), 2)));
        
        if(refPoint.getY()>centerPoint.getY()){
            
            refAngle=2*Math.PI-refAngle;
        }
        
        minAngle=Math.acos((minPoint.getX()-centerPoint.getX())/
        		Math.sqrt(Math.pow(minPoint.getX()-centerPoint.getX(), 2)+
        				Math.pow(minPoint.getY()-centerPoint.getY(), 2)));
        
        if(minPoint.getY()>centerPoint.getY()){

            minAngle=2*Math.PI-minAngle;
        }
        
        minAngle-=refAngle;
        
        if(minAngle<0){
            
            minAngle=2*Math.PI+minAngle;
        }
        
        maxAngle=Math.acos((maxPoint.getX()-centerPoint.getX())/
        		Math.sqrt(Math.pow(maxPoint.getX()-centerPoint.getX(), 2)+
        				Math.pow(maxPoint.getY()-centerPoint.getY(), 2)));
        
        if(maxPoint.getY()>centerPoint.getY()){
            
            maxAngle=2*Math.PI-maxAngle;
        }
        
        maxAngle-=refAngle;
        
        if(maxAngle<0){
            
            maxAngle=2*Math.PI+maxAngle;
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
        
        //getting the bounds of the parent element
        Rectangle2D bounds=AnimationsToolkit.getGeometryNodeBounds(
        		picture, parentElement);
        
        //computing the absolute coordinates of the center point
        absoluteCenterPoint=new Point2D.Double(
        		centerPoint.getX()+bounds.getX(), centerPoint.getY()+bounds.getY());
    }
    
    /**
     * the method called when the data to which the listener is registered, is modified
     * @param evt an event
     * @return the runnable that should be executed to apply the modifications
     */
    public Runnable dataChanged(DataEvent evt) {
        
    	//the runnable that will be returned
    	Runnable runnable=null;
    	
        //deactivates all the blinking value modifiers
        deactivateBlinkingValueModifiers();
    	
        //whether the tag value is invalid or not
        boolean isInvalidState=false;

        Map<String, Object> dataNameToValue=evt.getDataNameToValue();

        //getting the tag value
        double tagValue=0;
        
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

                //computing the new angle value//
                double currentAngle=0;
                ratio=Math.abs(tagValue/amplitude);

                if(clockwise){
                    
                    if(minAngle<maxAngle){
                        
                        currentAngle=-(minAngle-ratio*(minAngle-(maxAngle-2*Math.PI)));

                    }else if(minAngle>maxAngle){
                        
                        currentAngle=-(minAngle-ratio*(minAngle-maxAngle));
                        
                    }else{
                        
                        currentAngle=-(-2*Math.PI*ratio+minAngle);
                    }
                    
                }else{
                    
                    if(minAngle<maxAngle){

                        currentAngle=-(ratio*(maxAngle-minAngle)+minAngle);
                        
                    }else if(minAngle>maxAngle){
                        
                        currentAngle=-(ratio*(maxAngle-(minAngle-2*Math.PI))+(minAngle-2*Math.PI));
                        
                    }else{
                        
                        currentAngle=-(2*Math.PI*ratio+minAngle);
                    }
                }

		        //computes the accurate transform
		        affineTransform=new AffineTransform();

		        affineTransform.preConcatenate(AffineTransform.getTranslateInstance(
		        		-absoluteCenterPoint.getX(), -absoluteCenterPoint.getY()));
		        affineTransform.preConcatenate(AffineTransform.getRotateInstance(currentAngle));
		        affineTransform.preConcatenate(AffineTransform.getTranslateInstance(
		        		absoluteCenterPoint.getX(), absoluteCenterPoint.getY()));
		        
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
        
        setInvalidTag(isInvalidState);
        
        return runnable;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#dispose()
     */
    public void dispose() {}

}