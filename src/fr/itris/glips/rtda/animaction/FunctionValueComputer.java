package fr.itris.glips.rtda.animaction;

import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.toolkit.*;

/**
 * the class allowing to compute the value of a tag that is described by a function
 * @author ITRIS, Jordi SUC
 */
public class FunctionValueComputer implements 
	Comparable<FunctionValueComputer>{
    
    /**
     * the name of the data that contains the function
     */
    private String tagName;
    
    /**
     * the tag type
     */
    private int tagType=-1;
    
    /**
     * the name of the function
     */
    private String functionName="";
    
    /**
     * the period
     */
    private double period=1;
    
    /**
     * the initial value
     */
    private double initialValue=0;
    
    /**
     * the minimum value
     */
    private double minValue;
    
    /**
     * the maximum value
     */
    private double maxValue;
    
    /**
     * the constructor of the class
     * @param picture a svg picture
     * @param tagName the name of the data that contains the function
     * @param minValue the min value
     * @param maxValue the max value
     */
    public FunctionValueComputer(SVGPicture picture, String tagName, 
    		double minValue, double maxValue){
        
        this.tagName=tagName;
        this.minValue=minValue;
        this.maxValue=maxValue;
        
        //getting the type of the tag
    	tagType=picture.getMainDisplay().getPictureManager().
    		getConfigurationDocument(picture).getTagType(tagName);
        
        //analysing the function string and retrieving the name of the function, the period and the initial value
        String function=new String(tagName);
        function=function.substring(function.indexOf("function(")+9, function.length());
        
        try{
            function=function.replaceAll("\\s+", "");
            function=function.replaceAll("[)]", "");
            function=function.replaceAll(",", " ");
        }catch (Exception ex){}
        
        String[] functionValues=function.split("\\s");
        
        //setting the name of the function
        if(functionValues.length>0){
            
            functionName=functionValues[0];
        }
        
        //setting the period of the function
        if(functionValues.length>1){
            
            try{
                period=Double.parseDouble(functionValues[1]);
            }catch(Exception ex){period=1;}
        }
        
        //setting the initial value for the function
        if(functionValues.length>2){
            
            try{
                initialValue=Double.parseDouble(functionValues[2]);
            }catch(Exception ex){initialValue=0;}
        }
    }

    /**
     * @return the tag name
     */
    public String getTagName() {
        return tagName;
    }
    
    /**
     * returns the current value of the function of the tag
     * @param time the current time
     * @return the current value of the function of the tag
     */
    public Object getFunctionValue(double time){
        
        //the value that will be returned
        Object value=0;
        
        //getting the min and max
        double minVal=minValue, maxVal=maxValue;

        if(Double.isNaN(minVal)){
            
            minVal=-1;
        }
        
        if(Double.isNaN(maxVal)){
            
            maxVal=1;
        }
        
        if(functionName!=null && ! functionName.equals("")){

        	if(minVal<maxVal){
        		
        		double dVal=0;
        		
                //computing the value of the functions (a number between -1 and  1)
                if(functionName.equals(AnimationsToolkit.SIN_FUNCTION)){

                	dVal=AnimationsToolkit.sin(time, initialValue, period);

                }else if(functionName.equals(AnimationsToolkit.TRIANGLE_FUNCTION)){

                	dVal=AnimationsToolkit.triangle(time, initialValue, period);

                }else if(functionName.equals(AnimationsToolkit.RAMP_FUNCTION)){

                	dVal=AnimationsToolkit.ramp(time, initialValue, period);
                }   

                //computing the value that will be returned
                dVal=(maxVal-minVal)/2*dVal+(maxVal+minVal)/2;
                
                if(tagType==TagToolkit.ANALOGIC_INTEGER){
                	
                	value=(int)dVal;
                	
                }else{
                	
                	value=dVal;
                }
        		
        	}else if(minVal==maxVal){
        		
                if(tagType==TagToolkit.ANALOGIC_INTEGER){
                	
                	value=(int)minVal;
                	
                }else{
                	
                	value=minVal;
                }
        	}
        }
        
        return value;
    }
    
    public int compareTo(FunctionValueComputer o) {

    	return 0;
    }
}
