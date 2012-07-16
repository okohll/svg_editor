/*
 * Created on 19 mars 2005
 */
package fr.itris.glips.rtda.animations;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.test.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * the listener to the changes of data for the "bar graph" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class BarGraph extends DataChangedListener{
	
	/**
	 * the value of the tag attribute of the animation node
	 */
	private String tagAttributeValue="";
	
	/**
	 * the tag min and tag max
	 */
	private DataLimit tagMin, tagMax;
	
	/**
	 * the computed value of the tag, tagMin and tagMax attributes
	 */
	private double tagValue=Double.NaN, tagMinValue=Double.NaN, tagMaxValue=Double.NaN;
	
	/**
	 * the foreground, background and invalid colors and the direction
	 */
	private String foregroundColor="", backgroundColor="", invalidColor="", direction="";
	
	/**
	 * the last value for the color of the foreground element
	 */
	private String lastForegroundValue;
	
	/**
	 * the foreground, the background, the rectangle that will be used for intersecting with the foreground element 
	 * to create the accurate shape
	 */
	private Element foregroundElement;
	
	/**
	 * whether the background is transparent
	 */
	private boolean isBackgroundTransparent=false;
	
	/**
	 * whether the main tag attribute is described by a function or not
	 */
	private boolean isTagFunction=false;
	
    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public BarGraph(SVGPicture picture, Element animationElement){

        super(picture, animationElement);
		
		//getting the name specified in the "tag" attribute and adding it to the set of the data names
		tagAttributeValue=animationElement.getAttribute("tag");
		
		if(AnimationsToolkit.isFunction(tagAttributeValue)){
			
			tagAttributeValue=getNewId(tagAttributeValue);
			isTagFunction=true;
		}
		
		addData(tagAttributeValue);
		
		//getting the "transparentBackground" attribute
		String transparentBackground=
			animationElement.getAttribute("transparentBackground");
		
		//whether the background is transparent or not
		isBackgroundTransparent=(transparentBackground!=null && 
				transparentBackground.equals("true"));
		
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
		
		//storing the values of the attributes of the animation node 
		//and creating the associated blinking color value computer
		foregroundColor=animationElement.getAttribute("foregroundColor");
		foregroundColor=(foregroundColor.equals("")?"none":foregroundColor);
		addBlinkingColorValueComputer(foregroundElement, "foregroundColor", foregroundColor, "fill");
		
		backgroundColor=animationElement.getAttribute("backgroundColor");
		backgroundColor=(backgroundColor.equals("")?"none":backgroundColor);
		
		if(! isBackgroundTransparent){
			
			addBlinkingColorValueComputer(parentElement, "backgroundColor", backgroundColor, "fill");
		}
		
		invalidColor=animationElement.getAttribute("invalidColor");
		invalidColor=(invalidColor.equals("")?"none":invalidColor);
		addBlinkingColorValueComputer(foregroundElement, "invalidColor", invalidColor, "fill");
		
		//getting the direction
		direction=animationElement.getAttribute("direction");
		
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
		
		//initializing the animation//
		
		//setting the background color
		if(! isBackgroundTransparent){
			
			if(itemNameToBlinkingValueModifier.containsKey("backgroundColor")){
				
				activateBlinkingValueModifier("backgroundColor");
				
			}else{
				
				parentElement.setAttribute("fill", backgroundColor);
			}

		}else{
			
			parentElement.setAttribute("opacity", "0");
		}
		
		//creating the foreground and background elements
		Document doc=parentElement.getOwnerDocument();
		foregroundElement=doc.createElementNS(
				doc.getDocumentElement().getNamespaceURI(), "path");
		
		//setting the initial points of the foreground element
		foregroundElement.setAttribute("d", "M 0 0");
		
		//adding the foreground element to the parent of the parent element
		if(parentElement.getNextSibling()!=null){
			
			parentElement.getParentNode().insertBefore(
					foregroundElement, parentElement.getNextSibling());
			
		}else{
			
			parentElement.getParentNode().appendChild(foregroundElement);
		}
	}
	
	/**
	 * the method called when the data to which the listener is registered, is modified
	 * @param evt an event
	 * @return the runnable that should be executed to apply the modifications
	 */
	public Runnable dataChanged(DataEvent evt) {
		
		Runnable runnable=null;
		Map<String, Object> dataNameToValue=evt.getDataNameToValue();

		if(dataNameToValue!=null){

			//deactivates all the blinking value modifiers
			deactivateBlinkingValueModifiers();
			
			//activates the foreground blinking
			if(itemNameToBlinkingValueModifier.containsKey("backgroundColor")){
				
				activateBlinkingValueModifier("backgroundColor");
			}
			
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
	
			//the runnable into which the new values will be set
			runnable=new Runnable(){

				public void run() {

					boolean isTagInvalid=false;

					//the new foreground value
					String newForegroundValue="";

					//the new bounds for the rectangle 
					Rectangle2D theBounds=
						AnimationsToolkit.getGeometryNodeBounds(picture, parentElement);
					
					if(theBounds!=null){

						Rectangle newBounds=theBounds.getBounds();
						String pathPoints="";

						//whether the foreground can be set, i.e. whether the value of the tag 
						//(invalid or regular) corresponds to a blinking color,
						//if so, the blinking color is activated and no color is set
						boolean canSetForeground=false;

						if(! Double.isNaN(tagValue) && ! Double.isNaN(tagMinValue) 
								&& ! Double.isNaN(tagMaxValue) && tagMaxValue>=tagMinValue 
								&& tagValue>=tagMinValue && tagValue<=tagMaxValue){

							if(itemNameToBlinkingValueModifier.containsKey("foregroundColor")){

								activateBlinkingValueModifier("foregroundColor");
								lastForegroundValue=null;

							}else{

								newForegroundValue=foregroundColor;
								canSetForeground=true;
							}

							//computing the percentage of the foreground element facing the background element
							double ratio=0, amplitude=Math.abs(tagMaxValue-tagMinValue);

							if(amplitude>0){

								ratio=Math.abs((tagValue-tagMinValue)/amplitude);
							}

							//computing the new bounds for the rectangle in the mask
							if(direction.equals("left")){

								newBounds.x=(int)Math.round(newBounds.x+newBounds.width*(1-ratio));

							}else if(direction.equals("right")){

								newBounds.width=(int)Math.round(newBounds.width*ratio);

							}else if(direction.equals("bottom")){

								newBounds.height=(int)Math.round(newBounds.height*ratio);

							}else{

								newBounds.y=(int)Math.round(newBounds.y+newBounds.height*(1-ratio));
							}

							//getting the path element attribute value that is the intersection between 
							//the parent element and the computed bounds for the foreground element
							pathPoints=AnimationsToolkit.intersectPath(picture, parentElement, newBounds);

						}else{

							//the state is invalid//
							if(itemNameToBlinkingValueModifier.containsKey("invalidColor")){

								activateBlinkingValueModifier("invalidColor");
								lastForegroundValue=null;

							}else{

								newForegroundValue=invalidColor;
								canSetForeground=true;
							}

							isTagInvalid=true;
						}

						if(canSetForeground && ! newForegroundValue.equals(lastForegroundValue)){

							foregroundElement.setAttribute("fill", newForegroundValue);
							lastForegroundValue=newForegroundValue;
						}

						//setting the new points for the foreground element path
						foregroundElement.setAttribute("d", pathPoints);
					}

					setInvalidTag(isTagInvalid);
				} 
			};
		}
		
		return runnable;
	}
	
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#dispose()
     */
    public void dispose() {}

}
