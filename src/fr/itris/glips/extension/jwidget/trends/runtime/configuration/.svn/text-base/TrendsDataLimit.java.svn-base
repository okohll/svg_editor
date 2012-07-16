package fr.itris.glips.extension.jwidget.trends.runtime.configuration;

import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.trends.runtime.controller.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class computing a limit value
 * @author ITRIS, Jordi SUC
 */
public class TrendsDataLimit {

	/**
	 * the controller of the trends widget
	 */
	private TrendsRuntimeController controller;
	
    /**
     * whether the value of the limit is auto
     */
    private boolean isAuto=false;
    
    /**
     * the absolute value
     */
    private double absoluteValue=Double.NaN;
      
    /**
     * the name of a tag
     */
    private String tagName="";
    
    /**
     * the constructor of the class
     * @param controller the controller of the trends widget
     * @param referenceTagName the tag name whose limit is computed
     * @param limitValue the string value of the limit
     * @param isMinValue whether this limit stands for a min value
     */
    public TrendsDataLimit(TrendsRuntimeController controller, String referenceTagName, 
    										String limitValue, boolean isMinValue){
        
    	this.controller=controller;
    	
        if(limitValue!=null && ! limitValue.equals("")){
            
            //gets the value of the limit according to the content of "limitValue"
            if(limitValue.equals(DataLimit.AUTO_LIMIT)){
                
                isAuto=true;
                
                //getting the absolute value corresponding to this "auto" value
            	Element tagElement=null;
            	
            	try{
            		SVGPicture picture=controller.getJwidgetRuntime().getPicture();
            		tagElement=picture.getMainDisplay().getPictureManager().
            		getConfigurationDocument(picture).getElement(referenceTagName);
            	}catch (Exception ex){}

                if(tagElement!=null){
                	
                	String valStr=tagElement.getAttribute(isMinValue?"min":"max");
                	absoluteValue=Toolkit.getNumber(valStr);
                }

            }else{
                
                try{
                    absoluteValue=Double.parseDouble(limitValue);
                }catch (Exception ex){absoluteValue=Double.NaN;}
                
                if(Double.isNaN(absoluteValue)){
                    
                	tagName=limitValue;
                    
                    //registering this tag
                    controller.registerTag(tagName, false);
                }
            }
        }
    }
    
    /**
     * @return the current value of the limit
     */
    public double getValue(){
    	
    	double value=0;

    	if(isAuto || ! Double.isNaN(absoluteValue)){
    		
    		value=absoluteValue;
    		
    	}else{
    		
    		//getting the current value of the specified tag
    		try{
    			value=Toolkit.getNumber(controller.getTagValue(tagName));
    		}catch (Exception ex){value=Double.NaN;}
    	}
    	
    	return value;
    }
}
