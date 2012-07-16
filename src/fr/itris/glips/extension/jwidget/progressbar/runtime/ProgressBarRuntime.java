package fr.itris.glips.extension.jwidget.progressbar.runtime;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.progressbar.edition.*;
import fr.itris.glips.extension.jwidget.progressbar.runtime.anim.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class of the view browser runtime jwidget
 * @author ITRIS, Jordi SUC
 */
public class ProgressBarRuntime extends JWidgetRuntime{

	/**
	 * the jwidget id
	 */
	public static String jwidgetId="ProgressBarWidget";
	
    /**
     * the constructor of the class
     * @param picture the svg picture
     * @param element the svg element defining the jwidget
     */
    public ProgressBarRuntime(SVGPicture picture, Element element){
		
		super(picture, element);
	}
	
    @Override
    public void initialize() {

    	//getting the parameters value
    	int minValue=0, maxValue=0;
    	
    	try{
    		minValue=Integer.parseInt(element.getAttribute("minValue"));
    		maxValue=Integer.parseInt(element.getAttribute("maxValue"));
    	}catch (Exception ex){}
    	
    	String orientationStr=element.getAttribute("orientation");
    	int orientation=orientationStr.equals(SwingConstants.VERTICAL+"")?
    			SwingConstants.VERTICAL:SwingConstants.HORIZONTAL;

    	JProgressBar progressBar=new JProgressBar(orientation, minValue, maxValue);
    	progressBar.setValue(0);
    	JWidgetToolkit.handleLook(element, progressBar);
    	component=progressBar;
    }
    
    @Override
    public JWidgetAnimation createAnimation(Element animationElement) {

    	JWidgetAnimation animation=null;
    	
    	if(animationElement!=null) {
    		
    		String tagName=animationElement.getTagName();
    		
    		if(tagName.equals("rtda:bargraph")) {
    			
    			animation=new ProgressBarBarGraph(
    					this, component, animationElement);
    		}
    	}
    	
    	return animation;
    }
    
    @Override
    public void registerListeners() {}
    
    @Override
    public void unregisterListeners() {}
    
    /**
     * @return the jwidget edition class linked to this jwidget runtime class
     */
    public static Class<?> getEditionClass(){
    	
    	return ProgressBarEdition.class;
    }
    
    @Override
    public void refreshComponentState() {}
}
