/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.animaction;

import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.components.picture.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.colorsblinkings.*;
import fr.itris.glips.rtda.test.*;
import java.util.*;
import java.util.concurrent.*;
import java.awt.geom.*;

/**
 * the listener to the changes of a specified data
 * 
 * @author ITRIS, Jordi SUC
 */
public abstract class DataChangedListener implements ListenerAction{
    
    /**
     * the integer allowing to create unique ids
     */
    protected static int id=0;
	
    /**
     * the svg picture to which this animation is registered
     */
    protected SVGPicture picture;

    /**
     * the animation node this listener is linked to
     */
    protected Element animationElement;
    
    /**
     * the parent element of the animation node
     */
    protected Element parentElement;
    
    /**
     * the map associating the name of the datas used by the resource 
     * node to their respective value, the function tags are not included in this map
     */
    protected Set<String> dataNamesWithoutFunctionTags=
    	new CopyOnWriteArraySet<String>();
    
    /**
     * the list of the computers of the function values
     */
    protected Set<FunctionValueComputer> functionValueComputers=
    	new CopyOnWriteArraySet<FunctionValueComputer>();
    
    /**
     * the map associating a tag name to its pieces of information
     */
    protected Map<String, TestTagInformation> dataNamesToInformation=
    	new ConcurrentHashMap<String, TestTagInformation>();
    
    /**
     * the map associating an item to its linked blinking value modifier
     */
    protected Map<String, BlinkingValueModifier> itemNameToBlinkingValueModifier=
    	new ConcurrentHashMap<String, BlinkingValueModifier>();
    
    /**
     * whether one of the tags handled by the listener is invalid or not
     */
    protected boolean isInvalidTag;

	/**
	 * whether this animation is a transform animation
	 */
	protected boolean isTransformAnimation=false;
	
	/**
	 * the transform that will be applied to the parent element
	 */
	protected AffineTransform affineTransform;

	/**
	 * the initial transform of the parent element
	 */
	protected AffineTransform initialAffineTransform;
    
    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public DataChangedListener(SVGPicture picture, Element animationElement){

        this.picture=picture;
        this.animationElement=animationElement;
        
        //storing the initial value of the style attribute of the 
        //parent node of the animation node
        if(animationElement!=null && animationElement.getParentNode()!=null && 
        		animationElement.getParentNode() instanceof Element){
            
            parentElement=(Element)animationElement.getParentNode();
        }
        
        setInvalidTag(true);
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#initializeWhenCanvasDisplayed()
     */
    public void initializeWhenCanvasDisplayed() {}
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#getPicture()
     */
    public SVGPicture getPicture() {

    	return picture;
    }

	/**
	 * @return Returns the parentElement.
	 */
	public Element getParentElement() {
		return parentElement;
	}
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#invalidMarkersAllowed()
     */
    public boolean invalidMarkersAllowed() {
    	
    	return true;
    }
    
    /**
     * @return the collection of the blinking value modifier for this listener
     */
    public Collection<BlinkingValueModifier> getBlinkingValueModifiers(){

        return itemNameToBlinkingValueModifier.values();
    }
    
    /**
     * @return the set of the data names
     */
    public Set<String> getDataNames() {

        return dataNamesWithoutFunctionTags;
    }
    
	/**
	 * returns the value corresponding to the provided tag name
	 * @param tagName a tag name
	 * @return the value corresponding to the provided tag name
	 */
	public Object getData(String tagName){
		
		return picture.getMainDisplay().
			getAnimationsHandler().getData(tagName);
	}
    
    /**
     * puts a data name and its value to the map of datas
     * @param name the name of a tag
     */
    protected void addData(String name){
        
        if(name!=null && ! name.equals("")){
            
            if(! AnimationsToolkit.isFunction(name)){
                
                dataNamesWithoutFunctionTags.add(name);
            }
        }
    }
    
    /**
     * activates the modifier of the given id, and deactivates the others
     * @param modifierId the id of a blinking value modifier
     */
    public void activateBlinkingValueModifier(String modifierId){
        
        if(modifierId!=null && ! modifierId.equals("")){

            //activates the modifier that has the given id
            BlinkingValueModifier modifier=
            	itemNameToBlinkingValueModifier.get(modifierId);
            
            if(modifier!=null){
                
                modifier.setActive(true);
            }
        }
    }
    
    /**
     * activates the modifier of the given id, and deactivates the others
     */
    public void deactivateBlinkingValueModifiers(){
        
        //deactivates each modifier
        for(BlinkingValueModifier modifier : 
        	itemNameToBlinkingValueModifier.values()){

            if(modifier!=null){
                
                modifier.setActive(false);
            }
        }
    }
    
    /**
     * computes the values of the tags described by a function
     * @param time the current time
     * @return the map associating the name of a tag to its newly computed value
     */
    public Map<String, Object> computeFunctionValues(double time){
        
        HashMap<String, Object> map=new HashMap<String, Object>();
        Object value=0;

        //for each computer in the list, computes a new value for the tag 
        //and puts it into the map that will be returned
        for(FunctionValueComputer computer : functionValueComputers){

            if(computer!=null){
                
                value=computer.getFunctionValue(time);
                map.put(computer.getTagName(), value);
            }
        }

        return map;
    }
    
    /**
     * if the property values describes a blinking color, a new blinking value computer is created
     * @param element the element onto which the blinking color should be applied
     * @param modifierId the id of a modifier
     * @param value the value from which the blinking color will be computed
     * @param propertyName the name of a property
     */
    protected void addBlinkingColorValueComputer(Element element, 
    		String modifierId, String value, String propertyName){
        
        if(element!=null && modifierId!=null && ! modifierId.equals("") && 
        	propertyName!=null && ! propertyName.equals("") && value!=null && 
        	! value.equals("")){
        	
        	MainDisplay mainDisplay=picture.getMainDisplay();
            
            //getting the blinking color linked with the value of this property, if it exists
            BlinkingColor blinkingColor=mainDisplay.getColorsAndBlinkingsToolkit().
            	getBlinkingColor(picture.getAnimActionsHandler().getProjectFile(), value);

            if(blinkingColor!=null){
                
                //creates a new blinkg value modifier and adds it to the map of the modifiers
                BlinkingValueModifier modifier=new BlinkingValueModifier(mainDisplay,
					picture, picture.getAnimActionsHandler().getProjectFile(), 
						blinkingColor.getBlinkingId(), element, propertyName, 
							blinkingColor.getColorValue(), blinkingColor.getColorValue2());
                
                modifier.setActive(false);
                
                //adding this blinking value modifier to the map
                itemNameToBlinkingValueModifier.put(modifierId, modifier);
            }
        }
    }
    
    /**
     * creates a blinking modifier
     * @param modifierId the id of a modifier
     * @param blinkingId the id of a blinking
     * @param propertyName the name of a property
     * @param value1 the first value of the blinking
     * @param value2 the second value of the blinking
     */
    protected void addBlinkingValue(String modifierId, String blinkingId, 
    		String propertyName, String value1, String value2){
        
        if(modifierId!=null && ! modifierId.equals("") && propertyName!=null && 
        	! propertyName.equals("") && blinkingId!=null && ! blinkingId.equals("") && 
        	value1!=null && value2!=null){
            
            //creates a new blinkg value modifier and adds it to the map of the modifiers
            BlinkingValueModifier modifier=new BlinkingValueModifier(
            	picture.getMainDisplay(), picture,  picture.getAnimActionsHandler().getProjectFile(), 
            		blinkingId, parentElement, propertyName, value1, value2);
            
            modifier.setActive(false);
            
            //adding this blinking value modifier to the map
            itemNameToBlinkingValueModifier.put(modifierId, modifier);
        }
    }

    /**
     * @return Returns the affineTransform.
     */
    public AffineTransform getAffineTransform() {
        return affineTransform;
    }
    
    /**
     * @return Returns the initialAffineTransform.
     */
    public AffineTransform getInitialAffineTransform() {
        return initialAffineTransform;
    }

    /**
     * @return Returns the dataNamesToInformation.
     */
    public Map<String, TestTagInformation> getDataNamesToInformation() {

        return dataNamesToInformation;
    }

    /**
     * @return whether a tag attribute in the animation is a function or not
     */
    public boolean useFunction() {
        return functionValueComputers.size()>0;
    }

    /**
     * @return Returns the isTransformAnimation.
     */
    public boolean isTransformAnimation() {
        return isTransformAnimation;
    }

    /**
     * @return Returns the isInvalidTag.
     */
    public boolean isInvalidTag() {
        return isInvalidTag;
    }
    
    /**
     * @param isInvalidTag The isInvalidTag to set.
     */
    public void setInvalidTag(boolean isInvalidTag) {
    	
        synchronized (this) {
        	this.isInvalidTag=isInvalidTag;
		}
        
        if(isInvalidTag){
        	
        	picture.getMainDisplay().getAnimationsHandler().
        		getInvalidityNotifier().registerListener(this);
        	
        }else{
        	
        	picture.getMainDisplay().getAnimationsHandler().
    			getInvalidityNotifier().unregisterListener(this);
        }
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#getTooltipText()
     */
    public String getTooltipText() {return null;}
    
    /**
     * returns a new id given a base string
     * @param baseString a base string
     * @return a new unique id
     */
    protected static String getNewId(String baseString){
        
        if(baseString==null){
            
            baseString="";
        }
        
        String idStr=id+":"+baseString;
        
        id++;
        
        return idStr;
    }

}
