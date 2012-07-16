package fr.itris.glips.svgeditor.display.selection;

import java.util.*;
import org.w3c.dom.*;

import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.shape.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * the class handling selections, translations and actions
 * @author ITRIS, Jordi SUC
 */
public abstract class SelectionManager {

	/**
	 * the selection object that uses this selection manager
	 */
	protected Selection selection;
	
	/**
	 * computes and returns the next selection level for the given elements set
	 * @param elements a set of elements
	 * @return the next selection level for the given elements set
	 */
	public abstract int getNextSelectionLevel(Set<Element> elements);
	
	/**
	 * computes and returns the selection items for the given elements
	 * @param handle a svg handle
	 * @param elements a set of elements
	 * @param level the level of the selection
	 * @return the selection items for the given elements
	 */
	public abstract Set<SelectionItem> getSelectionItems(
			SVGHandle handle, Set<Element> elements, int level);
	
	/**
	 * executes an action according to the item that was selected by the user 
	 * and the point where the mouse can be found
	 * @param elements the set of the elements that are handled
	 * @param firstPoint the first clicked point
	 * @param currentPoint the current position of the mouse
	 * @param item the selection item for this action
	 */
	public abstract void doAction(
			Set<Element> elements, Point2D firstPoint, Point2D currentPoint, SelectionItem item);
	
	/**
	 * validates an action according to the selection item that was selected by the user and 
	 * the point where the mouse can be found
	 * @param elements the set of the elements that are handled
	 * @param firstPoint the first clicked point
	 * @param currentPoint the current position of the mouse
	 * @param item the selection item for this action
	 */
	public abstract void validateAction(
			Set<Element> elements, Point2D firstPoint, Point2D currentPoint, SelectionItem item);
	
	/**
	 * executes the translate action for the given elements
	 * @param elements the set of the elements that are handled
	 * @param firstPoint the first clicked point
	 * @param currentPoint the current position of the mouse
	 */
	public abstract void doTranslateAction(
			Set<Element> elements, Point2D firstPoint, Point2D currentPoint);
	
	/**
	 * validates the translate action for the given elements
	 * @param elements the set of the elements that are handled
	 * @param firstPoint the first clicked point
	 * @param currentPoint the current position of the mouse
	 */
	public abstract void validateTranslateAction(
			Set<Element> elements, Point2D firstPoint, Point2D currentPoint);
	
	/**
	 * computes and returns the locked selection items for the given elements
	 * @param handle a svg handle
	 * @param elements a set of elements
	 * @return the locked selection items for the given elements
	 */
	public Set<SelectionItem> getLockedSelectionItems(
			SVGHandle handle, Set<Element> elements){
		
		//the set of the items that will be returned
		Set<SelectionItem> items=new HashSet<SelectionItem>();
		
		//getting the area that is the union of the bounds of each element
		Rectangle2D wholeBounds=MultiAbstractShape.getElementsBounds(handle, elements);
		
		//scaling the bounds in the canvas space
		Rectangle2D area=handle.getTransformsManager().
			getScaledRectangle(wholeBounds, false);
		
		//getting the selection items
		//creating the items
		SelectionItem item=new SelectionItem(handle, elements, 
				new Point((int)area.getX(), (int)area.getY()),
				SelectionItem.LOCKED_POINT, SelectionItem.POINT_STYLE, 0, false, null);
		items.add(item);
		
		items.add(new SelectionItem(handle, elements, 
				new Point((int)area.getX(), (int)area.getMaxY()),
				SelectionItem.LOCKED_POINT, SelectionItem.POINT_STYLE, 0, false, null));
		
		items.add(new SelectionItem(handle, elements, 
				new Point((int)area.getMaxX(), (int)area.getMaxY()),
				SelectionItem.LOCKED_POINT, SelectionItem.POINT_STYLE, 0, false, null));
		
		items.add(new SelectionItem(handle, elements, 
				new Point((int)area.getMaxX(), (int)area.getY()),
				SelectionItem.LOCKED_POINT, SelectionItem.POINT_STYLE, 0, false, null));
		
		return items;
	}
}
