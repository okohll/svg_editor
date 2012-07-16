package fr.itris.glips.svgeditor.display.selection;

import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.selection.*;
import fr.itris.glips.svgeditor.shape.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;

/**
 * the class recording information on the selection on the canvas
 * @author ITRIS, Jordi SUC
 */
public class Selection {

	/**
	 * the constant specifying the first level of the selection
	 */
	public static final int SELECTION_LEVEL_1=0;
	
	/**
	 * the constant specifying the second level of the selection
	 */
	public static final int SELECTION_LEVEL_2=1;
	
	/**
	 * the constant specifying the third level of the selection
	 */
	public static final int SELECTION_LEVEL_3=2;
	
	/**
	 * the constant specifying the third level of the selection
	 */
	public static final int SELECTION_LEVEL_4=3;
	
	/**
	 * the constant specifying the level of the selection 
	 * while in a drawing action mode
	 */
	public static final int SELECTION_LEVEL_DRAWING=10;
	
	/**
	 * the constant specifying the level of the selection 
	 * while in selection items action mode
	 */
	public static final int SELECTION_LEVEL_ITEMS_ACTION=11;
	
	/**
	 * the regular sub mode
	 */
	public static final int REGULAR_SUB_MODE=0;
	
	/**
	 * the translation sub mode
	 */
	public static final int TRANSLATION_SUB_MODE=1;
	
	/**
	 * the action sub mode
	 */
	public static final int ACTION_SUB_MODE=2;
	
	/**
	 * the drawing mouse pressed action
	 */
	public static final int SELECTION_ZONE_MOUSE_PRESSED=0;
	
	/**
	 * the drawing mouse released action
	 */
	public static final int SELECTION_ZONE_MOUSE_RELEASED=1;
	
	/**
	 * the drawing mouse dragged action
	 */
	public static final int SELECTION_ZONE_MOUSE_DRAGGED=2;
	
	/**
	 * the global selection manager
	 */
	private SelectionInfoManager selectionManager=
			Editor.getEditor().getSelectionManager();
	
	/**
	 * the svg handle this object is linked to
	 */
	private SVGHandle svgHandle;
	
	/**
	 * the listener to the mouse actions on the canvas
	 */
	private SelectionListener selectionListener;
	
	/**
	 * the listener to the keyactions on the canvas
	 */
	private KeySelectionListener keySelectionListener;
	
	/**
	 * the selection managers
	 */
	private SelectionManager singleSelectionManager, multiSelectionManager;
	
	/**
	 * the parent element of the edited nodes
	 */
	private Element parentElement;
	
	/**
	 * the set of the currently selected elements
	 */
	private Set<Element> selectedElements=new HashSet<Element>();
	
	/**
	 * the ordered list of the currently selected elements
	 */
	private LinkedList<Element> orderedSelectedElements=
		new LinkedList<Element>();
	
	/**
	 * the set of the currently selected elements that are not locked
	 */
	private Set<Element> unLockedSelectedElements=new HashSet<Element>();
	
	/**
	 * the set of the locked elements
	 */
	private Set<Element> lockedElements=new HashSet<Element>();
	
	/**
	 * the set of the current selection items 
	 */
	private Set<SelectionItem> selectionItems=new HashSet<SelectionItem>();

	/**
	 * the selection mode
	 */
	private int selectionSubMode=REGULAR_SUB_MODE;
	
	/**
	 * the selection level
	 */
	private int selectionLevel=SELECTION_LEVEL_1;
	
	/**
	 * the action selection manager
	 */
	private SelectionManager actionSelectionManager;
	
	/**
	 * the painter of the selection zone rectangle
	 */
	private GhostSelectionZoneCanvasPainter ghostSelectionZoneCanvasPainter1, 
		ghostSelectionZoneCanvasPainter2, ghostSelectionZoneCanvasPainter3, 
		ghostSelectionZoneCanvasPainter4;
	
	/**
	 * the set of the listeners that listen to the selection changes
	 */
	private Set<SelectionChangedListener> selectionChangedListeners=
		new HashSet<SelectionChangedListener>();
	
	/**
	 * the painter of the selection items
	 */
	private SelectionItemsCanvasPainter selectionItemsPainter;
	
	/**
	 * whether the action and translation management should be disabled
	 */
	private boolean actionDisabled=false;

	/**
	 * the first point of an action
	 */
	private Point2D firstPoint;
	
	/**
	 * the selection item of the current action
	 */
	private SelectionItem actionItem;
	
	/**
	 * the set of the elements that are put here when they are translated
	 */
	private Set<Element> translationElements=new HashSet<Element>();
	
	/**
	 * the constructor of the class
	 * @param handle the svg handle that is associated to the selection object
	 */
	public Selection(final SVGHandle handle) {
		
		this.svgHandle=handle;
		
		//creating the selection managers
		singleSelectionManager=new SingleSelectionManager(this);
		multiSelectionManager=new MultiSelectionManager(this);
		
		//getting the parent element of the nodes that will be selected
		setParentElement(handle.getCanvas().getDocument().getDocumentElement(), false);
		
		//creating the painter of the selection items
		selectionItemsPainter=new SelectionItemsCanvasPainter();
		
		//creating the painters of the selection zone rectangle
		ghostSelectionZoneCanvasPainter1=new GhostSelectionZoneCanvasPainter();
		ghostSelectionZoneCanvasPainter2=new GhostSelectionZoneCanvasPainter();
		ghostSelectionZoneCanvasPainter3=new GhostSelectionZoneCanvasPainter();
		ghostSelectionZoneCanvasPainter4=new GhostSelectionZoneCanvasPainter();
		
		//creating the selection listeners
		selectionListener=new SelectionListener(this);
		keySelectionListener=new KeySelectionListener(this);
		
		SVGCanvas canvas=handle.getCanvas();
		JViewport viewport=canvas.getScrollPane().getViewport();
		
		canvas.addMouseListener(selectionListener);
		canvas.addMouseMotionListener(selectionListener);
		viewport.addMouseListener(selectionListener);
		viewport.addMouseMotionListener(selectionListener);		
	}
	
	/**
	 * disposes the selection resources
	 */
	public void dispose() {

		selectionChangedListeners.clear();
		
		SVGCanvas canvas=svgHandle.getCanvas();
		JViewport viewport=canvas.getScrollPane().getViewport();
		
		canvas.removeMouseListener(selectionListener);
		canvas.removeMouseMotionListener(selectionListener);		
		keySelectionListener.dispose();
		viewport.removeMouseListener(selectionListener);
		viewport.removeMouseMotionListener(selectionListener);

		selectionManager=null;
		svgHandle=null;
		selectionListener=null;
		keySelectionListener=null;
		singleSelectionManager=null;
		multiSelectionManager=null;
		parentElement=null;
		selectedElements=null;
		orderedSelectedElements=null;
		unLockedSelectedElements=null;
		lockedElements=null;
		selectionItems=null;
		actionSelectionManager=null;
		ghostSelectionZoneCanvasPainter1=null;
		ghostSelectionZoneCanvasPainter2=null;
		ghostSelectionZoneCanvasPainter3=null;
		ghostSelectionZoneCanvasPainter4=null;
		selectionChangedListeners=null;
		selectionItemsPainter=null;
		firstPoint=null;
		actionItem=null;
		translationElements=null;
	}
	
	/**
	 * @return the parent element of the edited nodes
	 */
	public Element getParentElement() {
		
		return parentElement;
	}

	/** sets the new parent element
	 * @param parentElement the new parent element
	 * @param notify whether the parent modification event should be propagated
	 */
	public void setParentElement(Element parentElement, boolean notify) {
		
		this.parentElement=parentElement;

		if(notify){
			
			svgHandle.getCanvas().notifyParentElementChanged();
			clearSelection();
		}
	}

	/**
	 * @return the selection submode
	 */
	public int getSelectionSubMode() {
		
		return selectionSubMode;
	}
	
	/**
	 * @return the selection manager corresponding to the current selection
	 */
	public SelectionManager getSelectionManager() {
		
		if(selectedElements.size()==1) {
			
			return singleSelectionManager;
			
		}else if(selectedElements.size()>1){
			
			return multiSelectionManager;
		}
		
		return null;
	}

	/**
	 * sets the new selection mode
	 * @param selectionSubMode the new selection sub mode
	 */
	public void setSelectionSubMode(int selectionSubMode) {

		this.selectionSubMode=selectionSubMode;
		
		if(Editor.getEditor().getSelectionManager().getSelectionMode()==
			SelectionInfoManager.ITEMS_ACTION_MODE){
			
			//refreshing the selection
			selectionLevel=SELECTION_LEVEL_ITEMS_ACTION;
			refreshSelection(true);
			
		}else if(Editor.getEditor().getSelectionManager().getSelectionMode()==
			SelectionInfoManager.DRAWING_MODE){
			
			//refreshing the selection
			selectionLevel=SELECTION_LEVEL_DRAWING;
			refreshSelection(true);
			
		}else if(selectionLevel==SELECTION_LEVEL_ITEMS_ACTION || 
				selectionLevel==SELECTION_LEVEL_DRAWING){
			
			//refreshing the selection
			selectionLevel=SELECTION_LEVEL_1;
			refreshSelection(true);
		}
	}
	
	/**
	 * notifies that the selection mode has changed
	 */
	public void selectionModeChanged(){
		
		//getting the set of all the shape modules
		Set<AbstractShape> modules=Editor.getEditor().getShapeModules();
		
		//notifying all the shape modules that the selection mode has changed
		for(AbstractShape shapeModule : modules){
			
			if(shapeModule!=null) {

				shapeModule.notifyDrawingAction(
					svgHandle, null, 0, AbstractShape.DRAWING_END);
			}
		}
	}
	
	/**
	 * sets whether the selection painters should be blocked
	 * @param blockSelectionItemsPaint whether the selection painters should be blocked
	 */
	public void setBlockSelectionItemsPaint(boolean blockSelectionItemsPaint) {
		
		if(blockSelectionItemsPaint){
			
			selectionItemsPainter.clean();
			
		}else{
			
			selectionItemsPainter.reinitialize();
		}
	}

	/**
	 * handles the selection of the element that can be found at the given point
	 * @param currentPoint a mouse point
	 * @param isMultiSelection whether the multi selection has been activated or not
	 */
	public void setSelection(Point2D currentPoint, boolean isMultiSelection) {
		
    	//converting the given point into the user space units
    	Point2D scaledPoint=svgHandle.getTransformsManager().
    		getScaledPoint(currentPoint, true);
		
		//getting the element that can be found at the given location
		Element element=svgHandle.getSvgElementsManager().
			getNodeAt(parentElement, scaledPoint);

		//handling the selection
		handleSelection(element, isMultiSelection, false);
		
		//enabling the actions and translations support
		actionDisabled=false;
	}
	
	/**
	 * handles the selection of the given element
	 * @param element an element
	 * @param isMultiSelection whether the multi selection has been activated or not
	 * @param keepSelectionLevel whether the selection level should be kept or not
	 */
	public void handleSelection(
			Element element, boolean isMultiSelection, boolean keepSelectionLevel) {

		if(element==null) {
			
			//clearing the selection
			selectedElements.clear();
			orderedSelectedElements.clear();
			
		}else {
			
			if(isMultiSelection) {
				
				//if the multi selection mode is enabled//
				if(selectedElements.contains(element)) {
					
					//as the element was already selected, it is removed from the selection
					selectedElements.remove(element);
					orderedSelectedElements.remove(element);
					
				}else {
					
					//the element is added to the selection
					selectedElements.add(element);
					orderedSelectedElements.add(element);
				}
				
			}else {
				
				//checking if the given element is already selected, if then the selection level is incremented
				if(selectedElements.contains(element)){
					
					//getting the selection manager corresponding to the current selection
					SelectionManager selManager=getSelectionManager();
					
					//getting the next selection level for the current selection 
					if(! keepSelectionLevel){
						
						selectionLevel=selManager.getNextSelectionLevel(selectedElements);
					}

				}else{
					
					//clearing the selection
					selectedElements.clear();
					orderedSelectedElements.clear();
					
					//adding the new element to the selection
					selectedElements.add(element);
					orderedSelectedElements.add(element);

					//resetting the selection level
					if(! keepSelectionLevel){
						
						selectionLevel=0;
					}
				}
			}
		}

		refreshSelection(true);
	}
	
	/**
	 * handles the selection of the given elements
	 * @param elements a set of elements
	 * @param isMultiSelection whether the multi selection has been activated or not
	 * @param propagate whether to propagate the event 
	 * of the refresh selection action or not
	 */
	public void handleSelection(
			Set<Element> elements, boolean isMultiSelection, boolean propagate) {

		if(elements==null || elements.size()==0) {
			
			//clearing the selection
			selectedElements.clear();
			orderedSelectedElements.clear();
			
		}else {
			
			if(! isMultiSelection){
				
				//clearing the selection
				selectedElements.clear();
				orderedSelectedElements.clear();
			}

			for(Element element : elements) {
				
				if(isMultiSelection && selectedElements.contains(element)) {
					
					//as when are in the multiselection mode and the element was 
					//already selected, it is removed from the selection
					selectedElements.remove(element);
					orderedSelectedElements.remove(element);
					
				}else {
					
					//the element is added to the selection
					selectedElements.add(element);
					orderedSelectedElements.add(element);
				}
			}
		}
		
		refreshSelection(propagate);
	}
	
	/**
	 * selects all the elements that can be found in the given rectangle
	 * @param rectangle an area on the canvas
	 * @param isMultiSelectionEnabled whether the multi selection is enabled
	 */
	public void handleSelection(
			Rectangle2D rectangle, boolean isMultiSelectionEnabled) {
		
		//reinitializing the selection level
		selectionLevel=SELECTION_LEVEL_1;
		
		//getting the corresponding rectangle in user space
		Rectangle2D scaledRectangle=svgHandle.
			getTransformsManager().getScaledRectangle(rectangle, true);
		
		//getting all the elements that can be found in the given rectangle in the canvas
		 Rectangle2D r2=null;
		 
		 //the set of all the elements that can be found in the given area
		 Set<Element> elements=new HashSet<Element>();
		 Element element=null;

         for(Node current=parentElement.getFirstChild(); 
         		current!=null; 
         			current=current.getNextSibling()){
             
             if(current instanceof Element) {
            	 
            	 element=(Element)current;
            	 
            	 if(EditorToolkit.isElementAShape(element)) {
            		 
            		 r2=svgHandle.getSvgElementsManager().
            		 	getNodeGeometryBounds(element);

                     if(r2!=null){

                         Rectangle2D r3=new Rectangle2D.Double(
                        		 r2.getX(), r2.getY(), r2.getWidth()+1, r2.getHeight()+1);

                         //if the node is contained in the rectangle, it is selected
                         if(scaledRectangle.contains(r3)){

                        	 elements.add(element);
                         }
                     }
            	 }
             }
         }

         handleSelection(elements, isMultiSelectionEnabled, true);

         if(! Editor.getEditor().getRemanentModeManager().isRemanentMode()){

        	 Editor.getEditor().getSelectionManager().setToRegularMode();
         }
	}
	
	/**
	 * selects all the elements that could be selected
	 *
	 */
	public void selectAllElements(){
		
		//clearing the selected elements
		selectedElements.clear();
		orderedSelectedElements.clear();
		
		Set<Element> elementsToSelect=new HashSet<Element>();
		
		//computing the set of the elements to be selected
		for(Node node=parentElement.getFirstChild(); 
			node!=null; node=node.getNextSibling()){
			
			if(node instanceof Element && 
					EditorToolkit.isElementAShape((Element)node)){
				
				elementsToSelect.add((Element)node);
			}
		}
		
		handleSelection(elementsToSelect, true, true);
	}
	
	/**
	 * removes all the selected items from the selection
	 */
	public void clearSelection(){
		
		selectedElements.clear();
		orderedSelectedElements.clear();
		refreshSelection(true);
	}
	
	/**
	 * refreshes the selection items
	 * @param propagate whether to propagate the event 
	 * of the refresh selection action or not
	 */
	public void refreshSelection(boolean propagate) {
		
		selectionItemsPainter.clear();
		selectionItems.clear();
		unLockedSelectedElements.clear();
		
		//checking if all the elements whose selected items 
		//should be handle, have not be deleted
		for(Element element : new HashSet<Element>(selectedElements)){

			if(element.getParentNode()==null){
				
				selectedElements.remove(element);
				orderedSelectedElements.remove(element);
			}
		}
		
		//checking if all the locked elements have not been deleted
		for(Element element : new HashSet<Element>(lockedElements)){

			if(element.getParentNode()==null){
				
				lockedElements.remove(element);
			}
		}
		
		if(selectedElements.size()>0) {
			
			//creating the set of the unlocked elements
			for(Element element : selectedElements){
				
				if(! lockedElements.contains(element)){
					
					unLockedSelectedElements.add(element);
				}
			}

			Set<SelectionItem> items=
						getSelectionManager().getSelectionItems(
								svgHandle, new HashSet<Element>(
										selectedElements), selectionLevel);
			
			if(items!=null) {
				
				selectionItems.addAll(items);
			}
		}

		//reinitializing the selection items painter
		selectionItemsPainter.reinitialize();

		if(propagate){

			//notifies all the selection changed listeners that the selection has changed
			notifySelectionChanged();
		}
	}
	
	/**
	 * adds a new selection changed listener
	 * @param listener a selection changed listener
	 */
	public void addSelectionChangedListener(SelectionChangedListener listener){
		
		if(selectionChangedListeners!=null){
			
			selectionChangedListeners.add(listener);
		}
	}
	
	/**
	 * removes a selection changed listener
	 * @param listener a selection changed listener
	 */
	public void removeSelectionChangedListener(SelectionChangedListener listener){
		
		if(selectionChangedListeners!=null){
			
			selectionChangedListeners.remove(listener);
		}
	}
	
	/**
	 * notifies all the selection changed listeners that the selection has changed
	 */
	public void notifySelectionChanged(){

		for(SelectionChangedListener listener : selectionChangedListeners){
			
			listener.selectionChanged(new HashSet<Element>(selectedElements));
		}
	}
	
	/**
	 * notifies that the mouse button has been pressed while in the drawing mode
	 * @param point a mouse point
	 * @param modifier a key modifier
	 * @param type the type of the drawing action
	 */
	public void drawingAction(Point2D point, int modifier, int type) {
		
		//getting the current drawing shape
		fr.itris.glips.svgeditor.shape.AbstractShape drawingShape=selectionManager.getDrawingShape();

		if(drawingShape!=null) {

			drawingShape.notifyDrawingAction(svgHandle, point, modifier, type);
		}
	}
	
	/**
	 * notifies that a selection items action has occured
	 * @param point a mouse point
	 */
	public void itemsAction(Point2D point){
		
		//getting the selection item corresponding to this point
		SelectionItem item=getSelectionItem(point);
		
		if(item!=null){
			
			//notifying the shape that handles the element 
			//corresponding to the item that an items action has occured//
			
			//getting the element of the selection item
			Element element=item.getElements().iterator().next();
			
			//getting the shape module corresponding to the element
			AbstractShape shape=ShapeToolkit.getShapeModule(element);
			
			if(shape!=null){
				
				//notifying the shape module that an item action occured
				shape.notifyItemsAction(svgHandle, item);
			}

			//refreshing the selection
			refreshSelection(true);
		}
	}
	
	/**
	 * draws a selection zone
	 * @param currentPoint the current point
	 * @param type the type of the action
	 */
	public void handleZoomZone(Point2D currentPoint, int type) {

		//getting the canvas
		SVGCanvas canvas=svgHandle.getScrollPane().getSVGCanvas();
		
		switch (type) {
		
		case SELECTION_ZONE_MOUSE_PRESSED :
			
			//initializing the action
			firstPoint=currentPoint;
			break;
			
		case SELECTION_ZONE_MOUSE_RELEASED :
			
			//computing the rectangle corresponding the first and current clicked points
			Rectangle2D rect=EditorToolkit.getComputedRectangle(firstPoint, currentPoint);
			rect=svgHandle.getTransformsManager().getScaledRectangle(rect, true);

			//setting the current scale for the canvas
			canvas.getZoomManager().scaleTo(rect);
			
			//reinitializing the data
			firstPoint=null;
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, true);
			ghostSelectionZoneCanvasPainter1.reinitialize();
			ghostSelectionZoneCanvasPainter2.reinitialize();
			ghostSelectionZoneCanvasPainter3.reinitialize();
			ghostSelectionZoneCanvasPainter4.reinitialize();
			
	         if(! Editor.getEditor().getRemanentModeManager().isRemanentMode()){

	        	 Editor.getEditor().getSelectionManager().setToRegularMode();
	         }
	         
			break;
			
		case SELECTION_ZONE_MOUSE_DRAGGED :
			
			//computing the current rectangle
			rect=EditorToolkit.getComputedRectangle(firstPoint, currentPoint);
			
			//drawing the new zone ghost rectangle//
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, false);

			//computing the rectangle corresponding the first and current clicked points
			rect=EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

			//setting the new rectangle to the ghost painter
			ghostSelectionZoneCanvasPainter1.setPoints(
					new Point2D.Double(rect.getX(), rect.getY()), 
					new Point2D.Double((rect.getX()+rect.getWidth()), rect.getY()));
			ghostSelectionZoneCanvasPainter2.setPoints(
					new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY()), 
					new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY()+rect.getHeight()));
			ghostSelectionZoneCanvasPainter3.setPoints(
					new Point2D.Double(rect.getX(), rect.getY()+rect.getHeight()), 
					new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY()+rect.getHeight()));
			ghostSelectionZoneCanvasPainter4.setPoints(
					new Point2D.Double(rect.getX(), rect.getY()), 
					new Point2D.Double(rect.getX(), rect.getY()+rect.getHeight()));
			
			canvas.addLayerPaintListener(
					SVGCanvas.DRAW_LAYER, ghostSelectionZoneCanvasPainter1, true);
			canvas.addLayerPaintListener(
					SVGCanvas.DRAW_LAYER, ghostSelectionZoneCanvasPainter2, true);
			canvas.addLayerPaintListener(
					SVGCanvas.DRAW_LAYER, ghostSelectionZoneCanvasPainter3, true);
			canvas.addLayerPaintListener(
					SVGCanvas.DRAW_LAYER, ghostSelectionZoneCanvasPainter4, true);
			break;
		}
	}
	
	/**
	 * draws a selection zone
	 * @param currentPoint the current point
	 * @param type the type of the action
	 * @param isMultiSelectionEnabled whether the multi selection is enabled
	 */
	public void handleSelectionZone(
			Point2D currentPoint, int type, boolean isMultiSelectionEnabled) {
		
		//getting the canvas
		SVGCanvas canvas=svgHandle.getScrollPane().getSVGCanvas();
		
		switch (type) {
		
		case SELECTION_ZONE_MOUSE_PRESSED :
			
			//initializing the action
			firstPoint=currentPoint;
			break;
			
		case SELECTION_ZONE_MOUSE_RELEASED :
			
			//computing the rectangle corresponding the first and current clicked points
			Rectangle2D rect=EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

			//creating the svg rectangle shape
			handleSelection(rect, isMultiSelectionEnabled);
			
			//reinitializing the data
			firstPoint=null;
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, true);
			ghostSelectionZoneCanvasPainter1.reinitialize();
			ghostSelectionZoneCanvasPainter2.reinitialize();
			ghostSelectionZoneCanvasPainter3.reinitialize();
			ghostSelectionZoneCanvasPainter4.reinitialize();
			break;
			
		case SELECTION_ZONE_MOUSE_DRAGGED :
			
			//computing the current rectangle
			rect=EditorToolkit.getComputedRectangle(firstPoint, currentPoint);
			
			//drawing the new zone ghost rectangle//
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, false);

			//computing the rectangle corresponding the first and current clicked points
			rect=EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

			//setting the new rectangle to the ghost painter
			ghostSelectionZoneCanvasPainter1.setPoints(
					new Point2D.Double(rect.getX(), rect.getY()), 
					new Point2D.Double((rect.getX()+rect.getWidth()), rect.getY()));
			ghostSelectionZoneCanvasPainter2.setPoints(
					new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY()), 
					new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY()+rect.getHeight()));
			ghostSelectionZoneCanvasPainter3.setPoints(
					new Point2D.Double(rect.getX(), rect.getY()+rect.getHeight()), 
					new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY()+rect.getHeight()));
			ghostSelectionZoneCanvasPainter4.setPoints(
					new Point2D.Double(rect.getX(), rect.getY()), 
					new Point2D.Double(rect.getX(), rect.getY()+rect.getHeight()));
			
			canvas.addLayerPaintListener(
					SVGCanvas.DRAW_LAYER, ghostSelectionZoneCanvasPainter1, true);
			canvas.addLayerPaintListener(
					SVGCanvas.DRAW_LAYER, ghostSelectionZoneCanvasPainter2, true);
			canvas.addLayerPaintListener(
					SVGCanvas.DRAW_LAYER, ghostSelectionZoneCanvasPainter3, true);
			canvas.addLayerPaintListener(
					SVGCanvas.DRAW_LAYER, ghostSelectionZoneCanvasPainter4, true);
			break;
		}
	}
	
	/**
	 * executes an action according to the item that was selected by the user and 
	 * the point where the mouse can be found
	 * @param currentPoint the current position of the mouse
	 */
	public void doAction(Point2D currentPoint) {

		if(! actionDisabled && ! isSelectionLocked()){
			
		   	//converting the given point into the user space units
	    	Point2D scaledPoint=
	    		svgHandle.getTransformsManager().getScaledPoint(currentPoint, true);
			
			if(firstPoint==null) {
				
				//initializing the action//
				
				//storing the first point
				firstPoint=scaledPoint;
				
				//checking if the given point matches a selection item
				actionItem=getSelectionItem(currentPoint);
				
				//checking if the given point is included in a shape//
				Element translationElement=null;

				if(actionItem==null) {

					//getting the element that can be found at the given location
					translationElement=svgHandle.
						getSvgElementsManager().getNodeAt(parentElement, scaledPoint);
					
					if(translationElement!=null && 
							! lockedElements.contains(translationElement)){
						
						//checking if this element is already selected, 
						//then all the selected elements will be translated
						if(selectedElements.contains(translationElement)) {
							
							translationElements.addAll(unLockedSelectedElements);
							
						}else {
							
							//otherwise, all the selected elements will be deselected, 
							//and the found element will be translated
							selectedElements.clear();
							orderedSelectedElements.clear();
							refreshSelection(true);
							translationElements.add(translationElement);
						}
					}
				}

				//checking if a selection item or an element matches the point, if not, exits the method
				if(actionItem==null && translationElements.size()==0){
					
					//reinitializes the data and returns
					firstPoint=null;
					setSelectionSubMode(REGULAR_SUB_MODE);
					actionDisabled=true;
					return;
				}
				
				if(selectionSubMode==REGULAR_SUB_MODE) {
					
					//computing the new type of the action
					if(actionItem!=null) {
						
						setSelectionSubMode(ACTION_SUB_MODE);	

					}else{
						
						setSelectionSubMode(TRANSLATION_SUB_MODE);
					}
				}

				//getting the selection manager
				switch (selectionSubMode) {
				
					case TRANSLATION_SUB_MODE :

						actionSelectionManager=
							(translationElements.size()>1?multiSelectionManager:singleSelectionManager);
						break;
					
					case ACTION_SUB_MODE:

						actionSelectionManager=getSelectionManager();
						break;
				}
			}
			
			//checking if the selection items should be still painted or not
			setBlockSelectionItemsPaint(! (actionItem!=null && actionItem.isPersistent()));

			//handling the action for the current point of the drag action
			switch (selectionSubMode) {
			
				case TRANSLATION_SUB_MODE :

					//translating the elements
					actionSelectionManager.doTranslateAction(translationElements, firstPoint, scaledPoint);
					break;
					
				case ACTION_SUB_MODE:

					//doing the action
					actionSelectionManager.doAction(
							new HashSet<Element>(selectedElements), firstPoint, scaledPoint, actionItem);
					break;
			}
		}
	}
	
	/**
	 * executes an action according to the item that was selected by the user and 
	 * the point where the mouse can be found
	 * @param currentPoint the current position of the mouse
	 */
	public void validateAction(Point2D currentPoint) {
		
		if(! isSelectionLocked()){
			
		   	//converting the given point into the user space units
	    	Point2D scaledPoint=svgHandle.getTransformsManager().
	    		getScaledPoint(currentPoint, true);
	    	
	    	if(actionSelectionManager!=null){
	    		
	    		//validating the action		
	    		switch (selectionSubMode) {
	    		
	    			case TRANSLATION_SUB_MODE :

	    				//translating the elements
	    				actionSelectionManager.validateTranslateAction(
	    						translationElements, firstPoint, scaledPoint);
	    				break;
	    				
	    			case ACTION_SUB_MODE:

	    				//doing the action
	    				actionSelectionManager.validateAction(
	    						new HashSet<Element>(selectedElements), firstPoint, scaledPoint, actionItem);
	    				break;
	    		}
	    	}

			//reinitializing the data
	    	firstPoint=null;
			actionItem=null;
			actionSelectionManager=null;
			translationElements.clear();
			setSelectionSubMode(REGULAR_SUB_MODE);
		}
	}
	
	/**
	 * translates the currently selected points by the provided factors
	 * @param delta the translation factors
	 */
	public void translate(Point2D delta){
		
		if(! isSelectionLocked()){
			
			//getting the scaled translation factors
	    	Point2D scaledDelta=svgHandle.getTransformsManager().
	    		getScaledPoint(delta, true);
	    	
	    	//getting the current action manager
	    	actionSelectionManager=getSelectionManager();

	    	if(actionSelectionManager!=null){
	    		
	    		//translating the elements
	    		actionSelectionManager.validateTranslateAction(
	    				new HashSet<Element>(selectedElements), new Point(0, 0), scaledDelta);
	    	}
		}
	}
	
	/**
	 * refreshes the labels displaying the current position of the mouse on the canvas
	 * @param currentMousePoint the current mouse point
	 */
	public void refreshSVGFrame(Point currentMousePoint){
		
    	//converting the given point into the user space units
    	Point2D scaledPoint=svgHandle.getTransformsManager().
    		getScaledPoint(currentMousePoint, true);
		
		//refreshing the labels displaying the current position of the mouse on the canvas
		svgHandle.getSVGFrame().getStateBar().setMousePosition(scaledPoint);
	}

	/**
	 * handles the cursor for the given point
	 * @param currentMousePoint
	 */
	public void handleCursor(Point currentMousePoint) {
		
    	//converting the given point into the user space units
    	Point2D scaledPoint=svgHandle.getTransformsManager().
    		getScaledPoint(currentMousePoint, true);
		
        //getting the selection item corresponding to this point, if one exists
		SelectionItem currentItem=null;
		
		for(SelectionItem item : new HashSet<SelectionItem>(selectionItems)) {

			if(item.intersectsItemForMouseMove(currentMousePoint)) {
				
				currentItem=item;
				break;
			}
		}
		
		//computing the point alignedwith rulers point
		Point2D point=svgHandle.getTransformsManager().
			getAlignedWithRulersPoint(currentMousePoint, true);
		
		//refreshing the labels displaying the current position of the mouse on the canvas
		svgHandle.getSVGFrame().getStateBar().setMousePosition(point);

        if(currentItem!=null){

        	//setting the cursor associated with the item for the canvas
        	svgHandle.getScrollPane().getSVGCanvas().setSVGCursor(currentItem.getCursor());
        	svgHandle.getScrollPane().getViewport().setCursor(currentItem.getCursor());

        }else{
        	
            int cursorType=Cursor.DEFAULT_CURSOR;
        	int selectionMode=Editor.getEditor().getSelectionManager().getSelectionMode();
            
        	if(selectionMode==SelectionInfoManager.DRAWING_MODE ||
        		selectionMode==SelectionInfoManager.ZONE_MODE ||
        			selectionMode==SelectionInfoManager.ZOOM_MODE){
        		
            	cursorType=Cursor.CROSSHAIR_CURSOR;
        		
        	}else if(! isSelectionLocked()){
        		
            	//getting the svg element corresponding to this point
                Element element=svgHandle.getSvgElementsManager().
                	getNodeAt(parentElement, scaledPoint);

                if(element!=null && ! isLocked(element)){

                	cursorType=Cursor.MOVE_CURSOR;
                }
        	}

        	Cursor newCursor=Cursor.getPredefinedCursor(cursorType);
        	
            svgHandle.getScrollPane().getSVGCanvas().setSVGCursor(newCursor);
            svgHandle.getScrollPane().getViewport().setCursor(newCursor);
        }
	}
	
	/**
	 * returns the selection item (if it exists) that can be found at the given point
	 * @param point a mouse point
	 * @return the selection item (if it exists) that can be found at the given point
	 */
	public SelectionItem getSelectionItem(Point2D point) {
		
		SelectionItem currentItem=null;
		
		for(SelectionItem item : new HashSet<SelectionItem>(selectionItems)) {

			if(item.intersectsItem(point)) {
				
				currentItem=item;
				break;
			}
		}
		
		return currentItem;
	}

	/**
	 * @return the svg handle that is associated to this object
	 */
	public SVGHandle getSVGHandle() {
		return svgHandle;
	}

	/**
	 * @return the set of the selected elements
	 */
	public Set<Element> getSelectedElements() {
		return selectedElements;
	}
	
	/**
	 * @return the linkedlist of the selected elements,
	 * the order is the one used by the user to select the elements
	 */
	public LinkedList<Element> getOrderedSelectedElements() {
		return orderedSelectedElements;
	}

	/**
	 * @return selectionLevel.
	 */
	public int getSelectionLevel() {
		return selectionLevel;
	}
	
	/**
	 * returns whether the provided element can be a parent element or not
	 * @param element an element
	 * @return whether the provided element can be a parent element or not
	 */
	public static boolean isParentElementEligible(Element element){
		
		return element!=null && element.getNodeName().equals("g");
	}
	
	/**
	 * @return whether one or more locked elements can be 
	 * found in the selected elements
	 */
	public boolean isSelectionLocked(){
		
		return ! unLockedSelectedElements.containsAll(selectedElements);
	}
	
	/**
	 * @return whether all the selected elements are locked
	 */
	public boolean isAllSelectionLocked(){
		
		return unLockedSelectedElements.isEmpty() && 
			selectedElements.size()>0;
	}
	
	/**
	 * returns whether the given element is locked or not
	 * @param element an element
	 * @return whether the given element is locked or not
	 */
	public boolean isLocked(Element element) {
		
		return lockedElements.contains(element);
	}
	
	/**
	 * locks the currently selected elements
	 */
	public void lock(){
		
		lockedElements.addAll(selectedElements);
		refreshSelection(true);
	}
	
	/**
	 * unlocks the currently selected elements
	 */
	public void unLock(){
		
		lockedElements.removeAll(selectedElements);
		refreshSelection(true);
	}
	
	/**
	 * @return the locked elements
	 */
	public Set<Element> getLockedElements() {
		
		return lockedElements;
	}
	
	/**
	 * the class used to paint the selection items on the canvas
	 * @author ITRIS, Jordi SUC
	 */
	protected class SelectionItemsCanvasPainter extends CanvasPainter {
		
		/**
		 * the current clip of this canvas painter
		 */
		private Set<Rectangle2D> clips=new HashSet<Rectangle2D>();
		
		@Override
		public void paintToBeDone(Graphics2D g) {
			
			//painting the selection items
			Set<SelectionItem> items=new HashSet<SelectionItem>(selectionItems);
			
			for(SelectionItem item : items) {
				
				item.paint(g);
			}
		}
		
		/**
		 * clears this painter
		 */
		protected void clear(){
			
			//recomputing the clip
			clips.clear();
			
			if(selectionItems.size()>0) {
				
				//adding all the new clips
				for(SelectionItem item : selectionItems) {
					
					clips.add(new Rectangle(item.getShapeBounds()));
				}
			}
		}
		
		/**
		 * stops displaying the selection items and cleans the canvas
		 */
		protected void clean(){
			
			SVGCanvas canvas=svgHandle.getScrollPane().getSVGCanvas();
			
			if(clips.size()>0) {
				
				canvas.removePaintListener(this, true);
			}
		}
		
		/**
		 * reinitializes all the selection items
		 */
		protected void reinitialize() {
			
			SVGCanvas canvas=svgHandle.getScrollPane().getSVGCanvas();
			
			if(clips.size()>0) {
				
				canvas.removePaintListener(this, true);
				clips.clear();
			}

			if(selectionItems.size()>0) {
				
				//adding all the new clips
				for(SelectionItem item : selectionItems) {
					
					clips.add(new Rectangle(item.getShapeBounds()));
				}

				canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER, this, true);
			}
		}
		
		@Override
		public Set<Rectangle2D> getClip() {

			return clips;
		}
	}
	
	/**
	 * the painter used to draw ghost selection zones on a canvas
	 * @author ITRIS, Jordi SUC
	 */
	protected static class GhostSelectionZoneCanvasPainter extends CanvasPainter{
		
		/**
		 * the stroke for the ghost
		 */
		private static Stroke stroke=new BasicStroke(	1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 
																				1, new float[] {5, 5}, 0);
		
		/**
		 * the points of the line to draw
		 */
		private Point2D point1, point2;
		
		/**
		 * the set of the clip rectangles
		 */
		private Set<Rectangle2D> clips=new HashSet<Rectangle2D>();
		
		@Override
		public void paintToBeDone(Graphics2D g) {
			
			if(point1!=null && point2!=null) {

				g=(Graphics2D)g.create();
				g.setColor(Color.black);
				g.setStroke(stroke);
				g.setXORMode(Color.white);
				g.drawLine((int)point1.getX(), (int)point1.getY(), 
						(int)point2.getX(), (int)point2.getY());
				g.dispose();
			}
		}

		@Override
		public Set<Rectangle2D> getClip() {

			return clips;
		}
		
		/**
		 * sets the points for the line
		 * @param point1 the first point of the line
		 * @param point2 the second point of the line
		 */
		public void setPoints(Point2D point1, Point2D point2) {
			
			this.point1=point1;
			this.point2=point2;
			clips.clear();
			
			if(point1!=null && point2!=null) {
				
				clips.add(new Rectangle2D.Double(point1.getX(), point1.getY(), 
						point2.getX()-point1.getX()+1, point2.getY()-point1.getY()+1));
			}
		}
		
		/**
		 * reinitializing the painter
		 */
		public void reinitialize() {
			
			point1=null;
			point2=null;
		}
	}
}
