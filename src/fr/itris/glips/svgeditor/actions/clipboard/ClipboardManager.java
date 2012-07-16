package fr.itris.glips.svgeditor.actions.clipboard;

import java.awt.geom.*;
import java.util.*;
import java.util.concurrent.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the clipboard manager
 * @author Jordi SUC
 */
public class ClipboardManager {

	/**
	 * the map associating a an element to its bounds
	 */
	private final Map<Element, Rectangle2D> clipboardContentMap=
		new ConcurrentHashMap<Element, Rectangle2D>();
	
	/**
	 * the nodes that are added to the clipboard
	 */
	private final List<Element> clipboardContent=
		new CopyOnWriteArrayList<Element>();
	
	/**
	 * the handle from which the elements are copied
	 */
	private SVGHandle sourceHandle;
	
	/**
	 * the constructor of the class
	 */
	public ClipboardManager(){}
	
	/**
	 * adds the provided elements to the clipboard
	 * @param theSourceHandle the handle 
	 * @param elementToBoundsMap the map associating an element to its bounds
	 */
	public synchronized void addToClipboard(SVGHandle theSourceHandle, 
			LinkedHashMap<Element, Rectangle2D> elementToBoundsMap){
		
		this.sourceHandle=theSourceHandle;
		clipboardContentMap.clear();
		clipboardContent.clear();
		clipboardContentMap.putAll(elementToBoundsMap);
		clipboardContent.addAll(elementToBoundsMap.keySet());
	}
	
	/**
	 * clears the clipboard
	 */
	public void clearClipboard(){
		
		clipboardContentMap.clear();
		clipboardContent.clear();
		sourceHandle=null;
	}
	
	/**
	 * @return the clip board content
	 */
	public List<Element> getClipboardContent() {
		return clipboardContent;
	}
	
	/**
	 * @return the clip board content map
	 */
	public Map<Element, Rectangle2D> getClipboardContentMap() {

		return clipboardContentMap;
	}
	
	/**
	 * @return the handle from which the elements are copied
	 */
	public SVGHandle getSourceHandle() {
		return sourceHandle;
	}
	
	/**
	 * clears the source handle
	 */
	public void clearSourceHandle(){
		
		sourceHandle=null;
	}
}
