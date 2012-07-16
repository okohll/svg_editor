package fr.itris.glips.rtda.animaction;

import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class describing a limit attribute
 * @author ITRIS, Jordi SUC
 */
public class DataLimit {
	
	/**
	 * the constant for the auto limit
	 */
	public static final String AUTO_LIMIT="auto";
	
	/**
	 * the constant for the infinity limit
	 */
	public static final String INFINITY_LIMIT="infinity";
	
    /**
     * whether this limit is auto
     */
    private boolean isAuto=false;
    
    /**
     * whether this limit is infinite
     */
    private boolean isInfinite=false;
    
    /**
     * the percentage value
     */
    private double percentValue=Double.NaN;
    
    /**
     * the absolute value
     */
    private double absoluteValue=Double.NaN;
      
    /**
     * the name of a tag
     */
    private String tag="";
    
    /**
     * the constructor of the class
     * @param picture the picture to which animation or 
     * action using this limit object belongs
     * @param refTagName the name of the tag whose min or max is computed
     * @param limitValue the value of the limit
     * @param isMin whether the min or the max is handled in this object
     */
    public DataLimit(SVGPicture picture, String refTagName, 
    		String limitValue, boolean isMin){
        
        if(limitValue!=null && ! limitValue.equals("")){
            
            //gets the value of the limit according to the content of "limitValue"
            if(limitValue.equals(AUTO_LIMIT)){
            	
            	isAuto=true;
                
                //getting the absolute value corresponding to this "auto" value
            	Element tagElement=null;
            	
            	try{
            		tagElement=picture.getMainDisplay().getPictureManager().
            		getConfigurationDocument(picture).getElement(refTagName);
            	}catch (Exception ex){}

                if(tagElement!=null){
                	
                	String valStr=tagElement.getAttribute(isMin?"min":"max");
                	absoluteValue=Toolkit.getNumber(valStr);
                }
                
            }else if(limitValue.equals(INFINITY_LIMIT)){
                
                isInfinite=true;
                
            }else if(limitValue.indexOf("%")!=-1){

                limitValue=limitValue.replaceAll("\\s+", "");
                limitValue=limitValue.replaceAll("%", "");
                
                try{
                    percentValue=Double.parseDouble(limitValue);
                }catch (Exception ex){percentValue=Double.NaN;}
                
                if(Double.isNaN(percentValue)){
                    
                    tag=limitValue;
                }
                
            }else{
                
                try{
                    absoluteValue=Double.parseDouble(limitValue);
                }catch (Exception ex){absoluteValue=Double.NaN;}
                
                if(Double.isNaN(absoluteValue)){
                    
                    tag=limitValue;
                }
            }
        }
    }
    
    /**
     * @return Returns the isInfinite.
     */
    public boolean isInfinite() {
        return isInfinite;
    }

    /**
     * @return whether the value is a percentage value
     */
    public boolean isPercentValue() {
        
        return (! (Double.isNaN(percentValue)));
    }
    
    /**
     * @return Returns the percentValue.
     */
    public double getPercentValue() {
        
        return percentValue;
    }
    
    /**
     * @return whether the value is an absolute value
     */
    public boolean isAbsoluteValue() {
        
        return isAuto || (! (Double.isNaN(absoluteValue)));
    }
    
    /**
     * @return Returns the absoluteValue.
     */
    public double getAbsoluteValue() {
        return absoluteValue;
    }
    
    /**
     * @return whether the value is a tag
     */
    public boolean isTag(){
        
        return (! tag.equals(""));
    }
    
    /**
     * @return Returns the tag.
     */
    public String getTag() {
        return tag;
    }
}
