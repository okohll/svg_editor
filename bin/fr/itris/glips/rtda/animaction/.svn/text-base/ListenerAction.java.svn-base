package fr.itris.glips.rtda.animaction;

import java.util.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.test.*;

/**
 * the interface that each data listener or action should implement
 * @author ITRIS, Jordi SUC
 */
public interface ListenerAction {
	
	/**
	 * called once the canvas is displayed and before calls to the 
	 * <code>dataChanged(DataEvent evt)</code> method
	 */
	public void initializeWhenCanvasDisplayed();

	/**
     * the method called when the data to which the listener or action is registered, is modified.
     * A runnable should only be returned if a svg element has to be modified
     * @param evt an event
     * @return the runnable that should be executed to apply the modifications
     */
    public Runnable dataChanged(DataEvent evt);
    
    /**
     * @return whether this listener or action allowed the display or invalid icons when a tag is invalid
     */
    public boolean invalidMarkersAllowed();
    
    /**
     * @return the set of the data names
     */
    public Set<String> getDataNames();
    
    /**
     * @return the map associating a tag name to its pieces of information
     */
    public Map<String, TestTagInformation> getDataNamesToInformation();
    
    /**
     * @return the svg picture to which the listener or action is registered
     */
    public SVGPicture getPicture();
    
    /**
     * @return the tool tip text
     */
    public String getTooltipText();
    
    /**
     * disposes the listener or the action
     */
    public void dispose();
    
}
