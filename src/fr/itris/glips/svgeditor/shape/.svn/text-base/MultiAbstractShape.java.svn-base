package fr.itris.glips.svgeditor.shape;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;
import fr.itris.glips.svgeditor.display.undoredo.*;






/**
 * the abstract class of all the modules that handle several shapes 
 * @author Jordi SUC
 */
public abstract class MultiAbstractShape extends AbstractShape {

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public MultiAbstractShape(Editor editor) {
		
		super(editor);
	}
	
	@Override
	public int getLevelCount() {

		return 1;
	}
	
	/**
	 * returns the set of the elements that should be handled
	 * according to the provided set
	 * @param elements a set of elements
	 * @return the set of the elements that should be handled
	 * according to the provided set
	 */
	public abstract Set<Element> getElements(Set<Element> elements);

	@Override
	public Set<SelectionItem> getSelectionItems(
			SVGHandle handle, Set<Element> elements, int level) {

		//the set of the items that will be returned
		Set<SelectionItem> items=new HashSet<SelectionItem>();
		
		//getting the area that is the union of the bounds of each element
		Rectangle2D wholeBounds=getBounds(handle, elements);
		
		//scaling the bounds in the canvas space
		Rectangle2D scaledWholeBounds=handle.getTransformsManager().
			getScaledRectangle(wholeBounds, false);
		
		//getting the selection items according to the level type
		switch (level) {
			
			case Selection.SELECTION_LEVEL_DRAWING :
			case Selection.SELECTION_LEVEL_1 :
				
				items.addAll(getResizeSelectionItems(handle, elements, scaledWholeBounds));
				break;
				
			case Selection.SELECTION_LEVEL_2 :
				
				items.addAll(getRotateSelectionItems(handle, elements, scaledWholeBounds));
				break;
		}
		
		return items;
	}
	
	/**
	 * returns the bounds corresponding to the union of the bounds of each element
	 * @param handle the svg handle to which the elements belong
	 * @param elements a set of elements
	 * @return the bounds corresponding to the union of the bounds of each element
	 */
	public Rectangle2D getBounds(SVGHandle handle, Set<Element> elements){
		
		return getElementsBounds(handle, elements);
	}
	
	/**
	 * returns the bounds corresponding to the union of the bounds of each element
	 * @param handle the svg handle to which the elements belong
	 * @param elements a set of elements
	 * @return the bounds corresponding to the union of the bounds of each element
	 */
	public static Rectangle2D getElementsBounds(SVGHandle handle, Set<Element> elements){
		
		Rectangle2D wholeBounds=null;
		Rectangle2D elementBounds=null;
		
		//getting the bounds of each element and merging it to the already computed area
		for(Element element : elements) {
			
			elementBounds=handle.getSvgElementsManager().
				getNodeGeometryBounds(element);
			
			if(elementBounds!=null) {
				
				if(wholeBounds==null) {
					
					wholeBounds=elementBounds;
					
				}else {
					
					wholeBounds.add(elementBounds);
				}
			}
		}

		return wholeBounds;
	}
	
	@Override
	public CanvasPainter showTranslateAction(
			SVGHandle handle, Set<Element> elementSet,
				Point2D firstPoint, Point2D currentPoint) {
		
		//getting the set of the elements that should be handled
		elementSet=getElements(elementSet);
		
		//translating the computed shape
		AffineTransform translationTransform=
			AffineTransform.getTranslateInstance(
				currentPoint.getX()-firstPoint.getX(), currentPoint.getY()-firstPoint.getY());
		
		//computing the screen scaled shape
		Shape shape=handle.getTransformsManager().getScaledShape(
				getTransformedShape(handle, elementSet), false, translationTransform);

		//creating the set of the clips
		final Set<Rectangle2D> fclips=new HashSet<Rectangle2D>();
		fclips.add(shape.getBounds());
		
		final Shape fshape=shape;

		CanvasPainter canvasPainter=new CanvasPainter(){

			@Override
			public void paintToBeDone(Graphics2D g) {
				
				g=(Graphics2D)g.create();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
						RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(strokeColor);
				g.draw(fshape);
				g.dispose();
			}
			
			@Override
			public Set<Rectangle2D> getClip() {

				return fclips;
			}
		};
		
		return canvasPainter;
	}
	
	@Override
	public UndoRedoAction validateTranslateAction(SVGHandle handle, Set<Element> elementSet, 
			Point2D firstPoint, Point2D currentPoint) {
		
		//getting the translation factors
		Point2D translationFactors=new Point2D.Double(
				currentPoint.getX()-firstPoint.getX(), currentPoint.getY()-firstPoint.getY());

		return translate(handle, elementSet, translationFactors);
	}
	
	@Override
	public CanvasPainter showAction(SVGHandle handle, int level, Set<Element> elementSet, 
			SelectionItem item, Point2D firstPoint, Point2D currentPoint) {
		
		//getting the set of the elements that should be handled
		Set<Element> newElementSet=getElements(elementSet);
		
		CanvasPainter canvasPainter=null;
		
		//creating the union of all the shapes corresponding to the set of elements
		Shape unionShape=getTransformedShape(handle, newElementSet);
		
		if(unionShape!=null){
			
			//whether the shape should be painted
			boolean canPaintShape=true;
			
			//getting the bounds of the area formed by the set of the elements
			Rectangle2D bounds=getBounds(handle, elementSet);
			
			//getting the action transform//
			AffineTransform actionTransform=new AffineTransform();
			
			//getting the action transform
			switch (level){
			
				case 0 :
				
					//getting the resize transform
					actionTransform=getResizeTransform(
							handle, bounds, item, firstPoint, currentPoint);
					break;
					
				case 1 :
					
					if(item.getType()==SelectionItem.CENTER){

						item.setPoint(currentPoint);
						
						//storing the center point for the rotate action
						rotationSkewSelectionItemCenterPoint=currentPoint;
						canPaintShape=false;
						
					}else if(item.getType()==SelectionItem.NORTH_WEST || 
							item.getType()==SelectionItem.NORTH_EAST ||
							item.getType()==SelectionItem.SOUTH_EAST ||
							item.getType()==SelectionItem.SOUTH_WEST){
				
						//getting the rotation transform
						actionTransform=getRotationTransform(handle, bounds, firstPoint, currentPoint);
						
					}else{
						
						//getting the skew transform
						actionTransform=getSkewTransform(handle, bounds, firstPoint, currentPoint, item);
					}
						
					break;
			}
			
			if(canPaintShape && actionTransform!=null){

				//computing the screen scaled shape
				Shape shape=handle.getTransformsManager().
					getScaledShape(unionShape, false, actionTransform);

				//creating the set of the clips
				final Set<Rectangle2D> fclips=new HashSet<Rectangle2D>();
				fclips.add(shape.getBounds());
				
				final Shape fshape=shape;

				canvasPainter=new CanvasPainter(){

					@Override
					public void paintToBeDone(Graphics2D g) {
						
						g=(Graphics2D)g.create();
						g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
								RenderingHints.VALUE_ANTIALIAS_ON);
						g.setColor(strokeColor);
						g.draw(fshape);
						g.dispose();
					}
					
					@Override
					public Set<Rectangle2D> getClip() {

						return fclips;
					}
				};
			}
		}

		return canvasPainter;
	}
	
	@Override
	public UndoRedoAction validateAction(SVGHandle handle, int level, Set<Element> elementsSet, 
			SelectionItem item, Point2D firstPoint, Point2D lastPoint) {
		
		//the undo/redo action that will be returned
		UndoRedoAction undoRedoAction=null;

		//getting the bounds of the area formed by the set of the elements
		Rectangle2D bounds=getBounds(handle, elementsSet);
		
		//executing the accurate action
		switch (level){
		
			case 0 :
				
				//getting the resize transform for this action
				AffineTransform resizeTransform=
					getResizeTransform(handle, bounds, item, firstPoint, lastPoint);

				undoRedoAction=resize(handle, elementsSet, resizeTransform);
				break;
				
			case 1 :
				
				if(item.getType()!=SelectionItem.CENTER){

					//getting the center point
					Point2D centerPoint=getRotationSkewCenterPoint(handle, bounds);

					if(item.getType()==SelectionItem.NORTH_WEST || 
						item.getType()==SelectionItem.NORTH_EAST ||
						item.getType()==SelectionItem.SOUTH_EAST ||
						item.getType()==SelectionItem.SOUTH_WEST){
						
						//getting the angle for the rotation
						double angle=ShapeToolkit.getRotationAngle(
								centerPoint, firstPoint, lastPoint);
						
						undoRedoAction=rotate(handle, elementsSet, centerPoint, angle);

					}else{

						//getting the skew factor and whether it's horizontal or not
						boolean isHorizontal=(item.getType()==SelectionItem.NORTH || 
								item.getType()==SelectionItem.SOUTH);
						double skewFactor=0;
						
						if(isHorizontal){
							
							skewFactor=lastPoint.getX()-firstPoint.getX();
							
						}else{
							
							skewFactor=lastPoint.getY()-firstPoint.getY();
						}
						
						undoRedoAction=skew(handle, elementsSet, 
								centerPoint, skewFactor, isHorizontal);
					}

					rotationSkewSelectionItemCenterPoint=null;
					
				}else{
					
					rotationSkewCenterPoint=rotationSkewSelectionItemCenterPoint;
					rotationSkewSelectionItemCenterPoint=null;
				}

				break;
		}

		return undoRedoAction;
	}

	/**
	 * returns the shape union of the shapes corresponding to each element in the set
	 * @param handle a svg handle
	 * @param elementsSet a set of elements
	 * @return the shape union of the shapes corresponding to each element in the set
	 */
	protected Shape getTransformedShape(SVGHandle handle, Set<Element> elementsSet){

		//creating the union of all the shapes corresponding to the set of elements
		GeneralPath path=new GeneralPath();
		Shape shp=null;
		
		for(Element element : elementsSet){
			
			//getting the shape corresponding to the element
			shp=handle.getSvgElementsManager().getOutline(element);
			
			if(shp!=null){

				path.append(shp, false);
			}
		}
		
		return path;
	}
	
	/*protected Shape getTransformedShape(SVGHandle handle, Set<Element> elementsSet){
		
		//getting the set of the elements that should be handled
		elementsSet=getElements(elementsSet);

		//creating the union of all the shapes corresponding to the set of elements
		GeneralPath path=new GeneralPath();
		Area area=null;
		LinkedList<Shape> lineShapes=new LinkedList<Shape>();
		AbstractShape shapeModule=null;
		Shape shp=null;

		for(Element element : elementsSet){
			
			//getting the shape module corresponding to the element
			shapeModule=ShapeToolkit.getShapeModule(element);
			
			if(shapeModule!=null){
				
				//getting the shape corresponding to the element
				shp=shapeModule.getTransformedShape(handle, element, true);
				
				if(shp!=null){
					
					if(shp instanceof Line2D){
						
						lineShapes.add(shp);
						
					}else{
						
						if(area==null){
							
							area=new Area(shp);
							
						}else{
							
							area.add(new Area(shp));
						}
					}
				}
			}
		}

		if(area!=null){
			
			//filling the path
			path.append(area, false);
		}

		//adding the line shapes to the path
		for(Shape lineShape : lineShapes){
			
			path.append(lineShape, false);
		}
		
		return path;
	}*/
	
	@Override
	public UndoRedoAction translate(
			SVGHandle handle, Set<Element> elementSet, Point2D translationFactors) {
		
		elementSet=getElements(elementSet);

		//the set of the undo/redo actions for each element
		final Set<UndoRedoAction> undoRedoActionSet=new HashSet<UndoRedoAction>();
		
		//translating each element and registering the returned undo/redo action
		UndoRedoAction action=null;
		AbstractShape shapeModule=null;
		Set<Element> elementsToTransform=null;
		
		for(Element element : elementSet){
			
			//getting the shape module corresponding to the element
			shapeModule=ShapeToolkit.getShapeModule(element);
			
			if(shapeModule!=null){
				
				elementsToTransform=new HashSet<Element>();
				elementsToTransform.add(element);
				action=shapeModule.translate(
						handle, elementsToTransform, translationFactors);
				
				if(action!=null){
					
					undoRedoActionSet.add(action);
				}
			}
		}
		
		return getUndoRedoAction(
				undoRedoActionSet, elementSet, translateUndoRedoLabel);
	}
	
	@Override
	public UndoRedoAction resize(
			SVGHandle handle, Set<Element> elementSet, AffineTransform transform) {
		
		elementSet=getElements(elementSet);

		//the set of the undo/redo actions for each element
		final Set<UndoRedoAction> undoRedoActionSet=new HashSet<UndoRedoAction>();
		
		//executing the resize actions
		AbstractShape shapeModule=null;
		UndoRedoAction undoRedoAction=null;
		Set<Element> elementsToTransform=null;
		
		for(Element element : elementSet){
			
			//getting the shape module for this element
			shapeModule=ShapeToolkit.getShapeModule(element);
			
			if(shapeModule!=null){
				
				elementsToTransform=new HashSet<Element>();
				elementsToTransform.add(element);
				undoRedoAction=shapeModule.resize(
						handle, elementsToTransform, transform);
				
				if(undoRedoAction!=null){
					
					undoRedoActionSet.add(undoRedoAction);
				}
			}
		}
		
		return getUndoRedoAction(
				undoRedoActionSet, elementSet, resizeUndoRedoLabel);
	}
	
	@Override
	public UndoRedoAction rotate(SVGHandle handle, 
			Set<Element> elementSet, Point2D centerPoint, double angle) {
		
		elementSet=getElements(elementSet);

		//the set of the undo/redo actions for each element
		final Set<UndoRedoAction> undoRedoActionSet=
			new HashSet<UndoRedoAction>();
		
		//executing the rotate actions
		AbstractShape shapeModule=null;
		UndoRedoAction undoRedoAction=null;
		Set<Element> elementsToTransform=null;
		
		//executing the rotation actions
		for(Element element : elementSet){

			//getting the shape module for this element
			shapeModule=ShapeToolkit.getShapeModule(element);
			
			if(shapeModule!=null){
				
				//executing the rotation action
				elementsToTransform=new HashSet<Element>();
				elementsToTransform.add(element);
				undoRedoAction=shapeModule.rotate(
						handle, elementsToTransform, centerPoint, angle);
				
				if(undoRedoAction!=null){
					
					undoRedoActionSet.add(undoRedoAction);
				}
			}
		}

		return getUndoRedoAction(
				undoRedoActionSet, elementSet, rotateUndoRedoLabel);
	}
	
	@Override
	public UndoRedoAction skew(SVGHandle handle, Set<Element> elementSet, 
			Point2D centerPoint, double skewFactor, boolean isHorizontal) {
		
		elementSet=getElements(elementSet);
		
		//the set of the undo/redo actions for each element
		final Set<UndoRedoAction> undoRedoActionSet=
			new HashSet<UndoRedoAction>();
		
		//executing the skew actions
		AbstractShape shapeModule=null;
		UndoRedoAction undoRedoAction=null;
		Set<Element> elementsToTransform=null;
		
		//executing the skew actions
		for(Element element : elementSet){

			//getting the shape module for this element
			shapeModule=ShapeToolkit.getShapeModule(element);
			
			if(shapeModule!=null){
				
				//executing the skew action
				elementsToTransform=new HashSet<Element>();
				elementsToTransform.add(element);
				undoRedoAction=shapeModule.skew(
						handle, elementsToTransform, centerPoint, skewFactor, isHorizontal);
				
				if(undoRedoAction!=null){
					
					undoRedoActionSet.add(undoRedoAction);
				}
			}
		}

		return getUndoRedoAction(
			undoRedoActionSet, elementSet, skewUndoRedoLabel);
	}

	/**
	 * returns the undo/redo action used for merging the provided undo/redo action set
	 * @param undoRedoActionSet the undo/redo action set
	 * @param label the label for the undo/redo action
	 * @param elementsSet the set of the elements that will be modified
	 * @return the undo/redo action used for merging the provided undo/redo action set
	 */
	protected UndoRedoAction getUndoRedoAction(
			final Set<UndoRedoAction> undoRedoActionSet, 
				Set<Element> elementsSet, String label){
		
		//the execute runnable
		Runnable executeRunnable=new Runnable(){
			
			public void run() {
				
				for(UndoRedoAction theAction : undoRedoActionSet){
					
					theAction.execute();
				}
			}
		};
		
		//the undo runnable
		Runnable undoRunnable=new Runnable(){
			
			public void run() {
				
				for(UndoRedoAction theAction : undoRedoActionSet){
					
					theAction.undo();
				}
			}
		};
		
		//executing the action and creating the undo/redo action
		return ShapeToolkit.getUndoRedoAction(
				label, executeRunnable, undoRunnable, elementsSet);
	}

	@Override
	public Shape getShape(SVGHandle handle, Element element, boolean isOutline) {

		return null;
	}
	
	@Override
	public void setShape(SVGHandle handle, Element element, Shape shape) {}

	@Override
	public void notifyDrawingAction(
			SVGHandle handle, Point2D point, int modifier, int type) {}

}
