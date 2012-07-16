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
import java.awt.geom.*;

/**
 * the listener to the changes of data for the "scale" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class Scale extends DataChangedListener{
    
    /**
     * the value of the tag attribute of the animation node
     */
    private String tagAttributeValue="";
    
    /**
     * the direction of the scale
     */
    private String direction="";
    
    /**
     * whether the ratio should be preserved when scaling
     */
    private boolean preserveAspectRatio=false;
    
    /**
     * the tag min and tag max
     */
    private DataLimit tagMin, tagMax;
    
    /**
     * the computed value of the tag, tagMin and tagMax attributes
     */
    private double tagMinValue=Double.NaN, tagMaxValue=Double.NaN;
    
    /**
     * the absolute center point, center, reference, min and max points
     */
    private Point2D.Double	absoluteCenterPoint, centerPoint, 
    	refPoint, minPoint, maxPoint;
    
    /**
     * whether the parameters are correct
     */
    private boolean parametersCorrect=false;
    
    /**
     * whether the main tag attribute is described by a function or not
     */
    private boolean isTagFunction=false;

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public Scale(SVGPicture picture, Element animationElement){

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
        
        //getting the preserveAspectRatio attribute
        preserveAspectRatio=Boolean.parseBoolean(
        		animationElement.getAttribute("preserveAspectRatio"));
        
        //getting the point values
        double x=0, y=0;
        
        try{
            x=Double.parseDouble(animationElement.getAttribute("xCenter"));
            y=Double.parseDouble(animationElement.getAttribute("yCenter"));
        }catch (Exception ex){x=0; y=0;}
        
        centerPoint=new Point2D.Double(x, y);
        
        try{
            x=Double.parseDouble(animationElement.getAttribute("xRef"))-centerPoint.x;
            y=Double.parseDouble(animationElement.getAttribute("yRef"))-centerPoint.y;
        }catch (Exception ex){x=0; y=0;}
        
        refPoint=new Point2D.Double(x, y);
        
        try{
            x=Double.parseDouble(animationElement.getAttribute("xMin"))-centerPoint.x;
            y=Double.parseDouble(animationElement.getAttribute("yMin"))-centerPoint.y;
        }catch (Exception ex){x=0; y=0;}
        
        minPoint=new Point2D.Double(x, y);
        
        try{
            x=Double.parseDouble(animationElement.getAttribute("xMax"))-centerPoint.x;
            y=Double.parseDouble(animationElement.getAttribute("yMax"))-centerPoint.y;
        }catch (Exception ex){x=0; y=0;}
        
        maxPoint=new Point2D.Double(x, y);

        //checking if the point values are correct
        if(direction.equals("x") || direction.equals("xy")){

        	if(minPoint.getX()<maxPoint.getX()){
        		
        		if(refPoint.getX()>=minPoint.getX() && 
        				refPoint.getX()<=maxPoint.getX()){
        			
        			parametersCorrect=true;
        		}
        		
        	}else{
        		
        		if(refPoint.getX()>=maxPoint.getX() && 
        				refPoint.getX()<=minPoint.getX()){
        			
        			parametersCorrect=true;
        		}
        	}
        }
        
        if(direction.equals("y") || 
        		(direction.equals("xy") && parametersCorrect)){

        	if(minPoint.getY()<maxPoint.getY()){
        		
        		if(refPoint.getY()>=minPoint.getY() && 
        				refPoint.getY()<=maxPoint.getY()){
        			
        			parametersCorrect=true;
        		}
        		
        	}else{
        		
        		if(refPoint.getY()>=maxPoint.getY() && 
        				refPoint.getY()<=minPoint.getY()){
        			
        			parametersCorrect=true;
        		}
        	}
        }
        
        if(parametersCorrect && refPoint.getX()!=0 && refPoint.getY()!=0){

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
            
        }else{
        	
        	parametersCorrect=false;
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
        	centerPoint.x+bounds.getX(), centerPoint.y+bounds.getY());
    }

    /**
     * the method called when the data to which the listener is registered, is modified
     * @param evt an event
     * @return the runnable that should be executed to apply the modifications
     */
    public Runnable dataChanged(DataEvent evt) {
    	
    	//the runnable that will be returned
    	Runnable runnable=null;
    	
    	if(parametersCorrect){
    		
    		double tagValue=0;
    		
    	       //deactivates all the blinking value modifiers
            deactivateBlinkingValueModifiers();
            
            //whether the current state of the listener is invalid or not
            boolean isStateInvalid=false;

            //refreshing the min and max values//
            Map<String, Object> dataNameToValue=evt.getDataNameToValue();

            if(dataNameToValue!=null){

                //getting the value of the tag//
                if(dataNameToValue.containsKey(tagAttributeValue)){
                    
                	tagValue=Toolkit.getNumber(getData(tagAttributeValue));
                }
                
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

                double amplitude=Math.abs(tagMaxValue-tagMinValue);
                
                if(amplitude>0){
                	
                	//whether the min and max values are reversed
                	boolean reversedX=minPoint.getX()>maxPoint.x;
                	boolean reversedY=minPoint.getY()>maxPoint.y;
                	
                	//computing the position of the new point 
                	Point2D.Double newPoint=new Point2D.Double();
                	
                	if(reversedX){
                		
                		newPoint.x=minPoint.x-tagValue*Math.abs(
                			minPoint.getX()-maxPoint.getX())/amplitude;
                		
                	}else{
                		
                		newPoint.x=tagValue*Math.abs(
                			minPoint.getX()-maxPoint.getX())/amplitude+minPoint.x;
                	}
                	
                	if(reversedY){
                		
                		newPoint.y=minPoint.y-tagValue*Math.abs(
                    		minPoint.getY()-maxPoint.getY())/amplitude;
                		
                	}else{
                		
                		newPoint.y=tagValue*Math.abs(
                			minPoint.getY()-maxPoint.getY())/amplitude+minPoint.y;
                	}
    		        
    		        //computing the scales
    		        Point2D.Double scaleFactors=new Point2D.Double(1, 1);
    		        
    		        if(direction.equals("x")){
    		            
    		        	scaleFactors.x=newPoint.x/refPoint.x;
    		        	
    		        	if(preserveAspectRatio){
    		        		
    		        		scaleFactors.y=scaleFactors.x;
    		        	}
    		
    		        }else if(direction.equals("y")){
    		            
    		        	scaleFactors.y=newPoint.y/refPoint.y;
    		        	
    		        	if(preserveAspectRatio){
    		        		
    		        		scaleFactors.x=scaleFactors.y;
    		        	}

    		        }else if(direction.equals("xy")){
    		        	
    		        	scaleFactors.x=newPoint.x/refPoint.x;
    		        	
    		        	if(preserveAspectRatio){
    		        		
    		        		scaleFactors.y=scaleFactors.x;
    		        		
    		        	}else{
    		        		
        		        	scaleFactors.y=newPoint.y/refPoint.y;
    		        	}
    		        }

    		        affineTransform=AffineTransform.getTranslateInstance(
    		        		-absoluteCenterPoint.x, -absoluteCenterPoint.y);
    		        affineTransform.preConcatenate(AffineTransform.getScaleInstance(scaleFactors.x, scaleFactors.y));
    		        affineTransform.preConcatenate(AffineTransform.getTranslateInstance(
    		        		absoluteCenterPoint.x, absoluteCenterPoint.y));
    		    	
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
                
                isStateInvalid=true;
            }
            
            setInvalidTag(isStateInvalid);
    	}

        return runnable;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#dispose()
     */
    public void dispose() {}
}