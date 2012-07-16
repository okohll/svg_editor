package fr.itris.glips.svgeditor.shape;

import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.selection.*;
import fr.itris.glips.svgeditor.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.w3c.dom.*;

import javax.swing.*;

/**
 * the superclass of all the shape modules
 * @author ITRIS, Jordi SUC
 */
public abstract class AbstractShape extends ModuleAdapter {

	/**
	 * the drawing mouse pressed action
	 */
	public static final int DRAWING_MOUSE_PRESSED=0;
	
	/**
	 * the drawing mouse released action
	 */
	public static final int DRAWING_MOUSE_RELEASED=1;
	
	/**
	 * the drawing mouse dragged action
	 */
	public static final int DRAWING_MOUSE_DRAGGED=2;
	
	/**
	 * the drawing mouse move action
	 */
	public static final int DRAWING_MOUSE_MOVED=3;
	
	/**
	 * the drawing mouse double click action
	 */
	public static final int DRAWING_MOUSE_DOUBLE_CLICK=4;
	
	/**
	 * the drawing mouse end action event
	 */
	public static final int DRAWING_END=5;
	
	/**
	 * the color used for drawing outline of shapes
	 */
	public static final Color strokeColor=Color.blue;
	
	/**
	 * the id of the shape module
	 */
	protected String shapeModuleId="";
	
	/**
	 * the tag name of the element handled by this module
	 */
	protected String handledElementTagName="";
	
	/**
	 * the icons for the shape creator menu item
	 */
	protected Icon shapeCreatorIcon, shapeCreatorDisabledIcon;
	
	/**
	 * the menu item that is displayed in the menu bar to create shape elements
	 */
	protected JMenuItem shapeCreatorMenuItem;
	
	/**
	 * the tool item that is displayed in the toolbar to create shape elements
	 */
	protected AbstractButton shapeCreatorToolItem;
	
	/**
	 * the label for the menu and tool item
	 */
	protected String itemLabel="", itemToolTip="", undoRedoLabel="";
	
	/**
	 * the undo/redo labels
	 */
	protected String 	translateUndoRedoLabel="", resizeUndoRedoLabel="", 
									rotateUndoRedoLabel="", skewUndoRedoLabel="";
	
	/**
	 * the center point of the rotate or the skew action for the selection item
	 */
	protected Point2D rotationSkewSelectionItemCenterPoint;
	
	/**
	 * the center point of the rotate or the skew action
	 */
	protected Point2D rotationSkewCenterPoint;
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public AbstractShape(Editor editor) {}
	
	/**
	 * @return the id of this shape module
	 */
	public String getId(){
		
		return shapeModuleId;
	}
	
	/**
	 * retrieves all the labels required in this module
	 * and stores them
	 */
	protected void retrieveLabels(){
		
		//getting the labels
		itemLabel=
			ResourcesManager.bundle.getString(shapeModuleId+"ItemLabel");
		itemToolTip=
			ResourcesManager.bundle.getString(shapeModuleId+"ItemToolTip");
		undoRedoLabel=
			ResourcesManager.bundle.getString(shapeModuleId+"UndoRedoLabel");
		translateUndoRedoLabel=
			ResourcesManager.bundle.getString(shapeModuleId+"TranslateUndoRedoLabel");
		resizeUndoRedoLabel=
			ResourcesManager.bundle.getString(shapeModuleId+"ResizeUndoRedoLabel");
		rotateUndoRedoLabel=
			ResourcesManager.bundle.getString(shapeModuleId+"RotateUndoRedoLabel");
		skewUndoRedoLabel=
			ResourcesManager.bundle.getString(shapeModuleId+"SkewUndoRedoLabel");
	}
	
	/**
	 * creates the menu and tool items
	 */
	protected void createMenuAndToolItems() {

		//creating the listener to the menu and tool items
		ActionListener listener=new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				notifyDrawingMode();
				
				if(e.getSource().equals(shapeCreatorMenuItem)){
					
					shapeCreatorToolItem.removeActionListener(this);
					shapeCreatorToolItem.setSelected(true);
					shapeCreatorToolItem.addActionListener(this);
				}
			}
		};
		
		//getting the icons for the items
		shapeCreatorIcon=ResourcesManager.getIcon(shapeModuleId, false);
		shapeCreatorDisabledIcon=ResourcesManager.getIcon(shapeModuleId, true);
		
		//creating the menu item
		shapeCreatorMenuItem=new JMenuItem(itemLabel, shapeCreatorIcon);
		shapeCreatorMenuItem.setDisabledIcon(shapeCreatorDisabledIcon);
		shapeCreatorMenuItem.addActionListener(listener);
		shapeCreatorMenuItem.setEnabled(false);
		
		//creating the tool item
		shapeCreatorToolItem=new JToggleButton(shapeCreatorIcon);
		shapeCreatorToolItem.setDisabledIcon(shapeCreatorDisabledIcon);
		shapeCreatorToolItem.setToolTipText(itemToolTip);
		shapeCreatorToolItem.addActionListener(listener);
		shapeCreatorToolItem.setEnabled(false);
		
		//adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager=
			Editor.getEditor().getHandlesManager();
		
		svgHandleManager.addHandlesListener(new HandlesListener(){
					
			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {

				boolean isDrawingEnabled=isDrawingEnabled(currentHandle);
				
				shapeCreatorMenuItem.setEnabled(isDrawingEnabled);
				shapeCreatorToolItem.setEnabled(isDrawingEnabled);
				SelectionInfoManager selectionManager=Editor.getEditor().getSelectionManager();
				
				if(selectionManager.getDrawingShape()!=null && 
						selectionManager.getDrawingShape().equals(AbstractShape.this)){

					selectionManager.setToRegularMode();
				}
			}			
		});
	}
	
	/**
	 * returns whether shapes of that module can be drawn 
	 * in the canvas of the provided handle 
	 * @param handle a svg handle
	 * @return whether shapes of that module can be drawn 
	 * in the canvas of the provided handle 
	 */
	protected boolean isDrawingEnabled(SVGHandle handle){
		
		return handle!=null;
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		
		HashMap<String, JMenuItem> menuItems=new HashMap<String, JMenuItem>();
		
		if(shapeCreatorMenuItem!=null){
			
			menuItems.put(shapeModuleId, shapeCreatorMenuItem);
		}
		
		return menuItems;
	}
	
	@Override
	public HashMap<String, AbstractButton> getToolItems() {

		HashMap<String, AbstractButton> toolItems=new HashMap<String, AbstractButton>();
		
		if(shapeCreatorToolItem!=null){
			
			toolItems.put(shapeModuleId, shapeCreatorToolItem);
		}
		
		return toolItems;
	}
	
	/**
	 * a convenience method for refreshing anything when an action is validated
	 * @param element a svg element
	 */
	public void refresh(Element element){}
	
	/**
	 * inserts the given shape element in a svg document
	 * @param handle the current svg handle
	 * @param shapeElement the shape element to be inserted in the document
	 */
	protected void insertShapeElement(final SVGHandle handle, final Element shapeElement){
		
		if(shapeElement!=null){
			
			//getting the current parent element of all the edited nodes
			final Element parentElement=handle.getSelection().getParentElement();

			//the execute runnable
			Runnable executeRunnable=new Runnable(){
				
				public void run() {

					parentElement.appendChild(shapeElement);
					handle.getSelection().clearSelection();
					handle.getSelection().handleSelection(shapeElement, false, false);
				}
			};
			
			//the undo runnable
			Runnable undoRunnable=new Runnable(){
				
				public void run() {

					parentElement.removeChild(shapeElement);
				}
			};
			
			//executing the action and creating the undo/redo action
			HashSet<Element> elements=new HashSet<Element>();
			elements.add(shapeElement);
			UndoRedoAction undoRedoAction=ShapeToolkit.getUndoRedoAction(
					undoRedoLabel, executeRunnable, undoRunnable, elements);
			
			UndoRedoActionList actionlist=
				new UndoRedoActionList(undoRedoLabel, false);
			actionlist.add(undoRedoAction);
			handle.getUndoRedo().addActionList(actionlist, false);
		}
	}

	/**
	 * returns whether this module handles the elements that have the 
	 * same tag name as the one of the given element
	 * @param element an element
	 * @return whether this module handles the elements that have the 
	 * same tag name as the one of the given element
	 */
	public boolean isElementTypeSupported(Element element) {
		
		return (element!=null &&
				handledElementTagName.equals(element.getNodeName()));
	}
	
	/**
	 * @return whether this shape module should be taken for an
	 * element if several modules support this element
	 */
	public boolean isPrioritary(){
		
		return false;
	}
	
	/**
	 * @return the number of the selection levels this module can handle
	 */
	public abstract int getLevelCount();
	
	/**
	 * notifies the shape manager of a mouse event at the given 
	 * point for the drawing action
	 * @param handle the svg handle
	 * @param point the location of the mouse when the event occured
	 * @param modifier a key modifier
	 * @param type the type of the drawing action
	 */
	public abstract void notifyDrawingAction(
			SVGHandle handle, Point2D point, int modifier, int type);
	
	/**
	 * resets the drawing tool after the drawing action
	 * is the mode is not the remanent mode
	 */
	public void resetDrawing(){
		
		if(! Editor.getEditor().getRemanentModeManager().isRemanentMode()){
			
			Editor.getEditor().getSelectionManager().setToRegularMode();
		}
	}
	
	/**
	 * notifies that an action occured on the provided selection item
	 * @param handle a svg handle
	 * @param item a selection item 
	 */
	public void notifyItemsAction(SVGHandle handle, SelectionItem item){}
	
	/**
	 * notifies all the selection managers that the drawing mode has been enabled
	 */
	public void notifyDrawingMode() {
		
		Editor.getEditor().getSelectionManager().setSelectionMode(
				SelectionInfoManager.DRAWING_MODE, this);
	}
	
	/**
	 * notifies all the selection managers that the items action mode has been enabled
	 */
	public void notifyItemsActionMode() {
		
		Editor.getEditor().getSelectionManager().setSelectionMode(
				SelectionInfoManager.ITEMS_ACTION_MODE, this);
	}
	
	/**
	 * returns the set of the selection items associated with the given 
	 * elements for the current selection
	 * @param handle the svg handle to which the elements belong
	 * @param elements a set of elements that can be handled by the shape module
	 * @param level the current selection level
	 * @return the set of the selection items associated with the given 
	 * element for the current selection
	 */
	public abstract Set<SelectionItem> getSelectionItems(
			SVGHandle handle, Set<Element> elements, int level);
	
	/**
	 * displays a Java2D outline showing what the translation should be after validating it
	 * @param handle the svg handle of the canvas 
	 * @param elementSet the elements to be handled
	 * @param firstPoint the first point the user clicked when initiating the action
	 * @param currentPoint the currrent point of the action
	 * @return the painter of the outline of the shape
	 */
	public abstract CanvasPainter showTranslateAction(	
			SVGHandle handle, Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint);
	
	/**
	 * validates the translation
	 * @param handle the svg handle of the canvas into which the translation has been done
	 * @param elementSet the elements to be handled
	 * @param firstPoint the first point the user clicked when initiating the action
	 * @param currentPoint the currrent point of the action
	 * @return the undo/redo action used for this translation
	 */
	public abstract UndoRedoAction validateTranslateAction(	
			SVGHandle handle, Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint);
	
	/**
	 * displays a Java2D outline showing what the action should be after validating it
	 * @param handle the svg handle of the canvas into which the action has been done
	 * @param level the selection level
	 * @param elementSet the elements to be handled
	 * @param item the selection item the mouse is moving
	 * @param firstPoint the first point the user clicked when initiating the action
	 * @param currentPoint the currrent point of the action
	 * @return the painter of the outline of the shape
	 */
	public abstract CanvasPainter showAction(
			SVGHandle handle, int level, Set<Element> elementSet, SelectionItem item, 
				Point2D firstPoint, Point2D currentPoint);
	
	/**
	 * validates the action
	 * @param handle the svg handle of the canvas into which the action has been done
	 * @param level the selection level
	 * @param elementSet the elements to be handled
	 * @param item the selection item the mouse is moving
	 * @param firstPoint the first point the user clicked when initiating the action
	 * @param lastPoint the first point the user clicked when finishing the action
	 * @return the undo/redo action used for this action
	 */
	public abstract UndoRedoAction validateAction(
			SVGHandle handle,  int level, Set<Element> elementSet,
				SelectionItem item, Point2D firstPoint, Point2D lastPoint);
	
	
	/**
	 * translates the given element with the given translation factors
	 * @param handle the svg handle of the canvas into which the action has been done
	 * @param elementSet the set  of the elements to be handled
	 * @param translationFactors the translation factors
	 * @return the undo/redo action for this action
	 */
	public abstract UndoRedoAction translate(
			SVGHandle handle, Set<Element> elementSet, Point2D translationFactors);
	
	/**
	 * translates the given element with the given translation factors
	 * @param handle the svg handle of the canvas into which the action has been done
	 * @param elementSet the set  of the elements to be handled
	 * @param transform the resize transform
	 * @return the undo/redo action for this action
	 */
	public abstract UndoRedoAction resize(
			SVGHandle handle, Set<Element> elementSet, AffineTransform transform);
	
	/**
	 * translates the given element with the given translation factors
	 * @param handle the svg handle of the canvas into which the action has been done
	 * @param elementSet the set  of the elements to be handled
	 * @param centerPoint the center point of the rotation
	 * @param angle the angle of the rotation
	 * @return the undo/redo action for this action
	 */
	public abstract UndoRedoAction rotate(
			SVGHandle handle, Set<Element> elementSet, Point2D centerPoint, double angle);
	
	/**
	 * translates the given element with the given translation factors
	 * @param handle the svg handle of the canvas into which the action has been done
	 * @param elementSet the set  of the elements to be handled
	 * @param centerPoint the center point of the rotation
	 * @param skewFactor the skew factor
	 * @param isHorizontal whether the skex action is horizontal or not
	 * @return the undo/redo action for this action
	 */
	public abstract UndoRedoAction skew(
			SVGHandle handle, Set<Element> elementSet, Point2D centerPoint, 
				double skewFactor, boolean isHorizontal);

	/**
	 * returns the set of the selection items corresponding to a resize selection level and 
	 * for the elements and the covered area
	 * @param handle a svg handle
	 * @param elements a set of elements
	 * @param area the union of the bounds of the elements
	 * @return the set of the selection items corresponding to a resize selection level and 
	 * for the elements and the covered area
	 */
	public Set<SelectionItem> getResizeSelectionItems(
			SVGHandle handle, Set<Element> elements, Rectangle2D area){
		
		//the set of the selection items that will be returned
		Set<SelectionItem> items=new HashSet<SelectionItem>();

		//creating the items
		SelectionItem item=new SelectionItem(handle, elements, 
				new Point((int)area.getCenterX(), (int)area.getY()),
				SelectionItem.NORTH, SelectionItem.ARROW_STYLE, 0, false, null);
		items.add(item);
		
		items.add(new SelectionItem(handle, elements, 
				new Point((int)area.getCenterX(), (int)area.getMaxY()),
				SelectionItem.SOUTH, SelectionItem.ARROW_STYLE, 0, false, null));
		
		items.add(new SelectionItem(handle, elements, 
				new Point((int)area.getMaxX(), (int)area.getCenterY()),
				SelectionItem.EAST, SelectionItem.ARROW_STYLE, 0, false, null));
		
		items.add(new SelectionItem(handle, elements, 
				new Point((int)area.getX(), (int)area.getCenterY()),
				SelectionItem.WEST, SelectionItem.ARROW_STYLE, 0, false, null));
		
		if(area.getWidth()>item.getShapeBounds().width && 
				area.getHeight()>item.getShapeBounds().height){
			
			items.add(new SelectionItem(handle, elements, 
					new Point((int)area.getX(), (int)area.getY()),
					SelectionItem.NORTH_WEST, SelectionItem.OBLIQUE_ARROW_STYLE, 0, false, null));
			
			items.add(new SelectionItem(handle, elements, 
					new Point((int)area.getMaxX(), (int)area.getMaxY()),
					SelectionItem.SOUTH_EAST, SelectionItem.OBLIQUE_ARROW_STYLE, 0, false, null));
		
			items.add(new SelectionItem(handle, elements, 
					new Point((int)area.getMaxX(), (int)area.getY()),
					SelectionItem.NORTH_EAST, SelectionItem.OBLIQUE_ARROW_STYLE, 0, false, null));
			
			items.add(new SelectionItem(handle, elements, 
					new Point((int)area.getX(), (int)area.getMaxY()),
					SelectionItem.SOUTH_WEST, SelectionItem.OBLIQUE_ARROW_STYLE, 0, false, null));
		}

		return items;
	}
	
	/**
	 * returns the shape (without its transform applied) corresponding to this element
	 * @param handle a svg handle
	 * @param element an element
	 * @param isOutline whether the returned shape should only be used for showing the action
	 * @return the shape (without its transform applied) corresponding to this element
	 */
	public abstract Shape getShape(SVGHandle handle, Element element, boolean isOutline);
	
	/**
	 * sets the new shape for this element
	 * @param handle a svg handle
	 * @param element an element
	 * @param shape the new shape
	 */
	public abstract void setShape(SVGHandle handle, Element element, Shape shape);
	
	/**
	 * returns the transformed shape corresponding to this element
	 * @param handle a svg handle
	 * @param element an element
	 * @param isOutline whether the returned shape should only be used for showing the action
	 * @return the transformed shape corresponding to this element
	 */
	public Shape getTransformedShape(SVGHandle handle, Element element, boolean isOutline) {

		Shape shape=getShape(handle, element, isOutline);

		if(shape!=null){
			
			//getting the transformation of this element
			AffineTransform af=
				handle.getSvgElementsManager().getTransform(element);
			
			//transforming this shape
			if(! af.isIdentity()){
				
				shape=af.createTransformedShape(shape);
			}
		}

		return shape;
	}
	
	/**
	 * returns the resize transform corresponding to the given element and the points
	 * @param handle a svg handle
	 * @param element a svg element
	 * @param item the selection item
	 * @param firstPoint the first point
	 * @param secondPoint the second point
	 * @return the resize transform corresponding to the given element and the points
	 */
	protected AffineTransform getResizeTransform(
			SVGHandle handle, Element element, SelectionItem item, 
				Point2D firstPoint, Point2D secondPoint){

		//computes the scale and translate values taking the type of the selection square into account
		Rectangle2D bounds=getTransformedShape(handle, element, false).getBounds2D();

		return getResizeTransform(handle, bounds, item, firstPoint, secondPoint);
	}
	
	/**
	 * returns the resize transform corresponding to the parameters
	 * @param handle a svg handle
	 * @param bounds the bounds of the area to be resized
	 * @param item the selection item
	 * @param firstPoint the first point
	 * @param secondPoint the second point
	 * @return the resize transform corresponding to the parameters
	 */
	protected AffineTransform getResizeTransform(
			SVGHandle handle, Rectangle2D bounds, SelectionItem item, 
				Point2D firstPoint, Point2D secondPoint){
		
		//getting the diff point
		Point2D diff=new Point2D.Double(secondPoint.getX()-firstPoint.getX(), 
				secondPoint.getY()-firstPoint.getY());
	
		//getting the scale and the translation factors
		double sx=1.0, sy=1.0, tx=0, ty=0;
		int type=item.getType();
		
		if(Editor.getEditor().getSquareModeManager().isSquareMode()){
			
			switch (type){
			
			case SelectionItem.NORTH :
				
				diff=new Point2D.Double(diff.getY(), diff.getY());
				sy=1-diff.getY()/bounds.getHeight();
				sx=sy;
				tx=(bounds.getX()+bounds.getWidth()/2)*(1-sx);
				ty=(bounds.getY()+bounds.getHeight())*(1-sy);
				break;
				
			case SelectionItem.SOUTH :
				
				diff=new Point2D.Double(diff.getY(), diff.getY());
				sy=1+diff.getY()/bounds.getHeight();
				sx=sy;
				tx=(bounds.getX()+bounds.getWidth()/2)*(1-sx);
				ty=bounds.getY()*(1-sy);
				break;
				
			case SelectionItem.EAST :
				
				diff=new Point2D.Double(diff.getX(), diff.getX());
				sx=1+diff.getX()/bounds.getWidth();
				sy=sx;
				tx=bounds.getX()*(1-sx);
				ty=(bounds.getY()+bounds.getHeight()/2)*(1-sy);
				break;
				
			case SelectionItem.WEST :
				
				diff=new Point2D.Double(diff.getX(), -diff.getX());
				sx=1-diff.getX()/bounds.getWidth();
				sy=sx;
				tx=(bounds.getX()+bounds.getWidth())*(1-sx);
				ty=(bounds.getY()+bounds.getHeight()/2)*(1-sy);		
				break;
	
			case SelectionItem.NORTH_EAST :
				
				diff=new Point2D.Double(diff.getX(), -diff.getX());
				
				sx=1+diff.getX()/bounds.getWidth();
				sy=sx;
				tx=(bounds.getX())*(1-sx);
				ty=(bounds.getY()+bounds.getHeight())*(1-sy);
				break;
				
			case SelectionItem.NORTH_WEST :
				
				diff=new Point2D.Double(diff.getY(), diff.getY());
				sy=1-diff.getY()/bounds.getHeight();
				sx=sy;
				tx=(bounds.getX()+bounds.getWidth())*(1-sx);
				ty=(bounds.getY()+bounds.getHeight())*(1-sy);
				break;
				
			case SelectionItem.SOUTH_EAST :
				
				diff=new Point2D.Double(diff.getY(), diff.getY());
				sy=1+diff.getY()/bounds.getHeight();
				sx=sy;
				tx=bounds.getX()*(1-sx);
				ty=bounds.getY()*(1-sy);
				break;
	
			case SelectionItem.SOUTH_WEST :
				
				diff=new Point2D.Double(diff.getX(), -diff.getX());
				sx=1-diff.getX()/bounds.getWidth();
				sy=sx;
				tx=(bounds.getX()+bounds.getWidth())*(1-sx);
				ty=(bounds.getY())*(1-sy);		
				break;
			}
			
		}else{
			
			switch (type){
			
			case SelectionItem.NORTH :
				
				sy=1-diff.getY()/bounds.getHeight();
				ty=(bounds.getY()+bounds.getHeight())*(1-sy);
				break;
				
			case SelectionItem.SOUTH :
				
				sy=1+diff.getY()/bounds.getHeight();
				ty=bounds.getY()*(1-sy);
				break;
				
			case SelectionItem.EAST :
				
				sx=1+diff.getX()/bounds.getWidth();
				tx=bounds.getX()*(1-sx);	
				break;
				
			case SelectionItem.WEST :
				
				sx=1-diff.getX()/bounds.getWidth();
				tx=(bounds.getX()+bounds.getWidth())*(1-sx);
				break;
				
			case SelectionItem.NORTH_EAST :
				
				sx=1+diff.getX()/bounds.getWidth();
				sy=1-diff.getY()/bounds.getHeight();
				tx=(bounds.getX())*(1-sx);
				ty=(bounds.getY()+bounds.getHeight())*(1-sy);	
				break;
				
			case SelectionItem.NORTH_WEST :
				
				sx=1-diff.getX()/bounds.getWidth();
				sy=1-diff.getY()/bounds.getHeight();
				tx=(bounds.getX()+bounds.getWidth())*(1-sx);
				ty=(bounds.getY()+bounds.getHeight())*(1-sy);
				break;
				
			case SelectionItem.SOUTH_EAST :
				
				sx=1+diff.getX()/bounds.getWidth();
				sy=1+diff.getY()/bounds.getHeight();
				tx=bounds.getX()*(1-sx);
				ty=bounds.getY()*(1-sy);
				break;
				
			case SelectionItem.SOUTH_WEST :
				
				sx=1-diff.getX()/bounds.getWidth();
				sy=1+diff.getY()/bounds.getHeight();
				tx=(bounds.getX()+bounds.getWidth())*(1-sx);
				ty=(bounds.getY())*(1-sy);		
				break;
			}
		}
	
		//creating the transform
		AffineTransform af=new AffineTransform();
		af.preConcatenate(AffineTransform.getScaleInstance(sx, sy));
		af.preConcatenate(AffineTransform.getTranslateInstance(tx, ty));
	
		return af;
	}
	
	/**
	 * returns the rotation transform
	 * @param svgHandle a svg handle
	 * @param element a svg element
	 * @param firstPoint the first clicked point by the user
	 * @param currentPoint the current point of the drag action by the user
	 * @return the rotation transform
	 */
	protected AffineTransform getRotationTransform(
			SVGHandle svgHandle, Element element, 
				Point2D firstPoint, Point2D currentPoint){

		//getting the bounds of the element
		Rectangle2D bounds=getTransformedShape(svgHandle, element, false).getBounds2D();
		
		return getRotationTransform(svgHandle, bounds, firstPoint, currentPoint);
	}
	
	/**
	 * returns the rotation transform
	 * @param svgHandle a svg handle
	 * @param bounds the bounds of the area to be rotated
	 * @param firstPoint the first clicked point by the user
	 * @param currentPoint the current point of the drag action by the user
	 * @return the rotation transform
	 */
	protected AffineTransform getRotationTransform(
			SVGHandle svgHandle, Rectangle2D bounds, 
				Point2D firstPoint, Point2D currentPoint){

		//getting the center point
		Point2D centerPoint=getRotationSkewCenterPoint(svgHandle, bounds);
		
		//computing the angle for the rotation
		double angle=ShapeToolkit.getRotationAngle(centerPoint, firstPoint, currentPoint);
		
		//returning the rotation affine transform
		return AffineTransform.getRotateInstance(angle, centerPoint.getX(), centerPoint.getY());
	}
	
	/**
	 * returns the skew transform
	 * @param svgHandle a svg handle
	 * @param element an element
	 * @param firstPoint the first clicked point by the user
	 * @param currentPoint the current point of the drag action by the user
	 * @param item the selection item
	 * @return the skew transform
	 */
	protected  AffineTransform getSkewTransform(
			SVGHandle svgHandle, Element element, Point2D firstPoint, 
				Point2D currentPoint, SelectionItem item){

		//getting the bounds of the element
		Rectangle2D bounds=getTransformedShape(svgHandle, element, false).getBounds2D();
		
		return getSkewTransform(svgHandle, bounds, firstPoint, currentPoint, item);
	}
	
	/**
	 * returns the skew transform
	 * @param svgHandle a svg handle
	 * @param bounds the bounds of the area to transform
	 * @param firstPoint the first clicked point by the user
	 * @param currentPoint the current point of the drag action by the user
	 * @param item the selection item
	 * @return the skew transform
	 */
	protected  AffineTransform getSkewTransform(
			SVGHandle svgHandle, Rectangle2D bounds, Point2D firstPoint, 
				Point2D currentPoint, SelectionItem item){

		//getting the skew factor
		double skewX=0, skewY=0;
		boolean isHorizontal=(item.getType()==SelectionItem.NORTH || 
				item.getType()==SelectionItem.SOUTH);
		
		if(bounds.getWidth()>0 && bounds.getHeight()>0){

			if(isHorizontal){
				
				skewX=(currentPoint.getX()-firstPoint.getX())/bounds.getHeight();
				
			}else{
				
				skewY=(currentPoint.getY()-firstPoint.getY())/bounds.getWidth();
			}
		}
		
		//getting the center point
		Point2D centerPoint=getRotationSkewCenterPoint(svgHandle, bounds);
		
		//creating the affine transform
		AffineTransform af=
			AffineTransform.getTranslateInstance(-centerPoint.getX(), -centerPoint.getY());
		af.preConcatenate(AffineTransform.getShearInstance(skewX, skewY));
		af.preConcatenate(
				AffineTransform.getTranslateInstance(centerPoint.getX(), centerPoint.getY()));
		
		return af;
	}
	
	/**
	 * returns the center point for the rotate and skew actions
	 * @param handle a handle
	 * @param element the element to be transformed
	 * @return the center point for the rotate and skew actions
	 */
	public Point2D getRotationSkewCenterPoint(SVGHandle handle, Element element){

		//getting the bounds of the shape to be transformed
		Rectangle2D bounds=getTransformedShape(handle, element, false).getBounds2D();
		
		return getRotationSkewCenterPoint(handle, bounds);
	}
	
	/**
	 * returns the center point for the rotate and skew actions
	 * @param handle a handle
	 * @param bounds the bounds of an area
	 * @return the center point for the rotate and skew actions
	 */
	public Point2D getRotationSkewCenterPoint(
			SVGHandle handle, Rectangle2D bounds){
		
		Point2D centerPoint=null;

		if(rotationSkewCenterPoint==null){
			
			centerPoint=new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
			
		}else{
			
			centerPoint=rotationSkewCenterPoint;
		}
		
		return centerPoint;
	}
	
	/**
	 * returns the set of the selection items corresponding to a rotate selection level and 
	 * for the elements and the covered area
	 * @param handle a svg handle
	 * @param elements a set of elements
	 * @param area the union of the bounds of the elements
	 * @return the set of the selection items corresponding to a rotate selection level and 
	 * for the elements and the covered area
	 */
	public Set<SelectionItem> getRotateSelectionItems(
			SVGHandle handle, Set<Element> elements, Rectangle2D area){
		
		//the set of the selection items that will be returned
		Set<SelectionItem> items=new HashSet<SelectionItem>();

		//creating the items		
		items.add(new SelectionItem
				(handle, elements, new Point((int)area.getX(), (int)area.getY()),
					SelectionItem.NORTH_WEST, SelectionItem.CURVED_ARROW_STYLE, 0, false, null));
		
		items.add(new SelectionItem(
				handle, elements, new Point((int)area.getCenterX(), (int)area.getY()),
					SelectionItem.NORTH, SelectionItem.INVERSE_ARROW_STYLE, 0, false, null));
		
		items.add(new SelectionItem(
				handle, elements, new Point((int)area.getMaxX(), (int)area.getY()),
					SelectionItem.NORTH_EAST, SelectionItem.CURVED_ARROW_STYLE, 0, false, null));
		
		items.add(new SelectionItem(
				handle, elements, new Point((int)area.getMaxX(), (int)area.getCenterY()),
					SelectionItem.EAST, SelectionItem.INVERSE_ARROW_STYLE, 0, false, null));
		
		items.add(new SelectionItem(
				handle, elements, new Point((int)area.getMaxX(), (int)area.getMaxY()),
					SelectionItem.SOUTH_EAST, SelectionItem.CURVED_ARROW_STYLE, 0, false, null));
		
		items.add(new SelectionItem(
				handle, elements, new Point((int)area.getCenterX(), (int)area.getMaxY()),
					SelectionItem.SOUTH, SelectionItem.INVERSE_ARROW_STYLE, 0, false, null));
		
		items.add(new SelectionItem(
				handle, elements, new Point((int)area.getX(), (int)area.getMaxY()),
					SelectionItem.SOUTH_WEST, SelectionItem.CURVED_ARROW_STYLE, 0, false, null));
		
		items.add(new SelectionItem(
				handle, elements, new Point((int)area.getX(), (int)area.getCenterY()),
					SelectionItem.WEST, SelectionItem.INVERSE_ARROW_STYLE, 0, false, null));
		
		//getting the point for the center item
		Point2D centerPoint=null;
		
		if(rotationSkewSelectionItemCenterPoint!=null){
			
			centerPoint=rotationSkewCenterPoint;

		}else{
			
			centerPoint=new Point((int)area.getCenterX(), (int)area.getCenterY());
		}
			
		items.add(new SelectionItem(handle, elements, centerPoint,
				SelectionItem.CENTER, SelectionItem.CENTER_POINT_STYLE, 0, true, null));
		
		return items;
	}
	
	/**
	 * the painter used to draw ghost shapes on a canvas
	 * @author ITRIS, Jordi SUC
	 */
	public class DefaultGhostShapeCanvasPainter extends CanvasPainter{
		
		/**
		 * the shape to draw
		 */
		private Shape shape;
		
		/**
		 * the set of the clip rectangles
		 */
		private Set<Rectangle2D> clips=new HashSet<Rectangle2D>();
		
		@Override
		public void paintToBeDone(Graphics2D g) {
			
			if(shape!=null) {

				g=(Graphics2D)g.create();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
						RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(Color.black);
				//g.setXORMode(Color.white);
				g.draw(shape);
				g.dispose();
			}
		}

		@Override
		public Set<Rectangle2D> getClip() {

			return clips;
		}
		
		/**
		 * sets the shape to paint
		 * @param shape a shape
		 */
		public void setShape(Shape shape) {
			
			this.shape=shape;
			clips.clear();
			
			if(shape!=null) {
				
				clips.add(shape.getBounds2D());
			}
		}
		
		/**
		 * reinitializing the painter
		 */
		public void reinitialize() {
			
			shape=null;
		}
	}
}
