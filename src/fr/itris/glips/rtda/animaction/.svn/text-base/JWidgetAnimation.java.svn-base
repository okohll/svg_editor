package fr.itris.glips.rtda.animaction;

import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.test.*;

/**
 * the class of a jwidget animation listener
 * @author Jordi SUC
 */
public class JWidgetAnimation implements ListenerAction{

	/**
	 * the animation element
	 */
	protected Element animationElement;
	
	/**
	 * the associated jwidget runtime object
	 */
	protected JWidgetRuntime jwidgetRuntime;
	
	/**
	 * the component to animate
	 */
	protected JComponent component;
	
    /**
     * the set of the names of the data
     */
    protected Set<String> dataNames=
    	new CopyOnWriteArraySet<String>();
    
    /**
     * the map associating a tag name to its pieces of information
     */
    protected Map<String, TestTagInformation> dataNamesToInformation=
    	new ConcurrentHashMap<String, TestTagInformation>();
	
    /**
     * the constructor of the class
     * @param jwidgetRuntime the associated jwidget runtime object
     * @param component the component to animate
     * @param animationElement the animation element
     */
    public JWidgetAnimation(JWidgetRuntime jwidgetRuntime, 
    		JComponent component, Element animationElement) {
    	
    	this.jwidgetRuntime=jwidgetRuntime;
    	this.component=component;
    	this.animationElement=animationElement;
    	
    	jwidgetRuntime.addAnimation(this);
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#initializeWhenCanvasDisplayed()
     */
    public void initializeWhenCanvasDisplayed() {}
	
	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#dataChanged(fr.itris.glips.rtda.animaction.DataEvent)
	 */
	public Runnable dataChanged(DataEvent evt) {
		return null;
	}
	
	/**
	 * returns the value corresponding to the provided tag name
	 * @param tagName a tag name
	 * @return the value corresponding to the provided tag name
	 */
	public Object getData(String tagName){
		
		return jwidgetRuntime.getPicture().getMainDisplay().
			getAnimationsHandler().getData(tagName);
	}
	
	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#dispose()
	 */
	public void dispose() {
		
    	jwidgetRuntime.removeAnimation(this);
	}

	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#getDataNames()
	 */
	public Set<String> getDataNames() {

		return dataNames;
	}

	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#getDataNamesToInformation()
	 */
	public Map<String, TestTagInformation> getDataNamesToInformation() {
		
		return dataNamesToInformation;
	}

	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#getTooltipText()
	 */
	public String getTooltipText() {

		return "";
	}

	public SVGPicture getPicture() {

		return jwidgetRuntime.getPicture();
	}

	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#invalidMarkersAllowed()
	 */
	public boolean invalidMarkersAllowed() {

		return false;
	}
}
