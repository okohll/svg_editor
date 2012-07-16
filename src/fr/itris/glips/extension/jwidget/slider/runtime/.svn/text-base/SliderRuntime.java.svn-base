package fr.itris.glips.extension.jwidget.slider.runtime;

import java.awt.event.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.slider.edition.*;
import fr.itris.glips.extension.jwidget.slider.runtime.actions.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.animaction.Action;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class of the view browser runtime jwidget
 * @author ITRIS, Jordi SUC
 */
public class SliderRuntime extends JWidgetRuntime{

	/**
	 * the jwidget id
	 */
	public static String jwidgetId="SliderWidget";
	
	/**
	 * the slider listener
	 */
	private MouseListener sliderListener;
	
    /**
     * the constructor of the class
     * @param picture the svg picture
     * @param element the svg element defining the jwidget
     */
    public SliderRuntime(SVGPicture picture, Element element){
		
		super(picture, element);
	}
	
    @Override
    public void initialize() {

    	//getting the parameters value
    	int minValue=0, maxValue=0, initialValue=0, minorTickSpacing=0, majorTickSpacing=0;
    	
    	try{
    		minValue=Integer.parseInt(element.getAttribute("minValue"));
    		maxValue=Integer.parseInt(element.getAttribute("maxValue"));
    		initialValue=Integer.parseInt(element.getAttribute("initialValue"));
    		minorTickSpacing=Integer.parseInt(element.getAttribute("minorTickSpacing"));
    		majorTickSpacing=Integer.parseInt(element.getAttribute("majorTickSpacing"));
    	}catch (Exception ex){}
    	
    	String orientationStr=element.getAttribute("orientation");
    	int orientation=orientationStr.equals("vertical")?SwingConstants.VERTICAL:SwingConstants.HORIZONTAL;

    	boolean inverted=false, paintTicks=false, paintLabels=false;
    	inverted=element.getAttribute("inverted").equals(Boolean.toString(true))?true:false;
    	paintTicks=element.getAttribute("paintTicks").equals(Boolean.toString(true))?true:false;
    	paintLabels=element.getAttribute("paintLabels").equals(Boolean.toString(true))?true:false;
    	
    	JSlider slider=new JSlider(orientation, minValue, maxValue, initialValue);
    	slider.setOpaque(false);
    	slider.setInverted(inverted);
    	slider.setPaintTicks(paintTicks);
    	slider.setPaintLabels(paintLabels);
    	
    	if(paintTicks){
    		
    		slider.setMinorTickSpacing(minorTickSpacing);
    		slider.setMajorTickSpacing(majorTickSpacing);
    	}

    	component=slider;
    	
        //creating the listener to the slider
        sliderListener=new MouseAdapter(){
        	
        	@Override
        	public void mouseReleased(MouseEvent evt) {

    			for(Action action : getActions()){
    				
    				action.performAction(evt);
    			}
        	}
        };
        
        slider.addMouseListener(sliderListener);
    }

    @Override
    public Action createAction(Element actionElement) {
    	
    	Action action=null;
    	
    	if(actionElement!=null) {
    		
    		String tagName=actionElement.getTagName();
    		
    		if(tagName.equals("rtda:sendMeasure")) {
    			
    			action=new SliderSendMeasure(picture, projectName, 
    				picture.getCanvas(), component, null, actionElement, this);
        		
    		}else {
    			
    			action=super.createAction(actionElement);
    		}
    	}
    	
    	return action;
    }
    
    /**
     * @return the jwidget edition class linked to this jwidget runtime class
     */
    public static Class<?> getEditionClass(){
    	
    	return SliderEdition.class;
    }
    
    @Override
    public void registerListeners() {

    	if(sliderListener!=null){
    		
        	((JSlider)component).removeMouseListener(sliderListener);
        	((JSlider)component).addMouseListener(sliderListener);
    	}
    }
    
    @Override
    public void unregisterListeners() {

    	if(sliderListener!=null){
    		
        	((JSlider)component).removeMouseListener(sliderListener);
    	}
    }
    
    @Override
    public void dispose() {

    	unregisterListeners();
    	super.dispose();
    }
}
