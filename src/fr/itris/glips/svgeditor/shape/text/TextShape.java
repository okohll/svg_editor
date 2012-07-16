package fr.itris.glips.svgeditor.shape.text;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.shape.*;

/**
 * the class of the module that handle text shapes
 * @author ITRIS, Jordi SUC
 */
public class TextShape extends AbstractShape {
	
	/**
	 * the element attributes names
	 */
	protected static String xAtt="x", yAtt="y";
	
	/**
	 * the dialog used to select a text
	 */
	private TextDialog textDialog;
	
	/**
	 * the point selected for the drawing action
	 */
	private Point2D drawingPoint;
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public TextShape(Editor editor) {
		
		super(editor);
		
		shapeModuleId="TextShape";
		handledElementTagName="text";
		retrieveLabels();
		createMenuAndToolItems();
		
		//creating the text dialog
		if(Editor.getParent() instanceof JDialog){
			
			textDialog=new TextDialog(this, (JDialog)Editor.getParent());
			
		}else if(Editor.getParent() instanceof Frame){
			
			textDialog=new TextDialog(this, (Frame)Editor.getParent());
		}
	}
	
	@Override
	public int getLevelCount() {
		
		return 1;
	}
	
	@Override
	public void refresh(Element element){}

	@Override
	public void notifyDrawingAction(
			SVGHandle handle, Point2D point, int modifier, int type) {

		//according to the type of the event for the drawing action
		switch (type) {
				
			case DRAWING_MOUSE_RELEASED :
				
				//scaling the two points to fit a 1.1 zoom factor
				Point2D scaledPoint=handle.getTransformsManager().
					getScaledPoint(point, true);
				this.drawingPoint=scaledPoint;
				textDialog.showDialog(handle.getSVGFrame(), handle);
				resetDrawing();
				break;
		}
	}
	
	/**
	 * creates a svg element by specifiying its parameters
	 * @param handle the current svg handle
	 * @param text the text for the new element
	 * @return the created svg element
	 */
	public Element createElement(SVGHandle handle, String text){
		
		//the edited document
		Document doc=handle.getScrollPane().getSVGCanvas().getDocument();
		
		//creating the text element
		final Element element=doc.createElementNS(
				doc.getDocumentElement().getNamespaceURI(), 
					handledElementTagName);
		
		//getting the last color that has been used by the user
		String colorString=Editor.getColorChooser().getColorString(ColorManager.getCurrentColor());
		element.setAttributeNS(null, "style", "fill:"+colorString+";stroke:none;");
		element.setAttributeNS(null, "style", "font-size:12pt;fill:"+colorString+";");
		
		EditorToolkit.setAttributeValue(element, xAtt, drawingPoint.getX());
		EditorToolkit.setAttributeValue(element, yAtt, drawingPoint.getY());
		
		//creating the text node
		Text textValue=doc.createTextNode(text);
		element.appendChild(textValue);
		
		//inserting the element in the document and handling the undo/redo support
		insertShapeElement(handle, element);
		handle.getSelection().handleSelection(element, false, false);
		
		return element;
	}

	@Override
	public Set<SelectionItem> getSelectionItems(
			SVGHandle handle, Set<Element> elements, int level) {

		//clearing the stored values
		rotationSkewCenterPoint=null;
		
		// getting the first element of this set
		Element element=elements.iterator().next();
		
		// the set of the items that will be returned
		Set<SelectionItem> items=new HashSet<SelectionItem>();
		
		// getting the bounds of the element
		Rectangle2D bounds=getTransformedShape(handle, element, false).getBounds2D();
		
		// scaling the bounds in the canvas space
		Rectangle2D scaledWholeBounds=
			handle.getTransformsManager().getScaledRectangle(bounds, false);
		
		//getting the selection items according to the level type
		switch (level){
			
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
	
	@Override
	public UndoRedoAction translate(
		final SVGHandle handle, Set<Element> elementsSet, Point2D translationFactors) {

		//getting the translation transform
		AffineTransform transform=AffineTransform.getTranslateInstance(
				translationFactors.getX(), translationFactors.getY());

		return applyTransform(handle, elementsSet.iterator().next(), 
				transform, translateUndoRedoLabel);
	}
	
	@Override
	public UndoRedoAction resize(
			SVGHandle handle, Set<Element> elementsSet, AffineTransform transform) {

		return applyTransform(handle, elementsSet.iterator().next(), 
				transform, resizeUndoRedoLabel);
	}
	
	@Override
	public UndoRedoAction rotate(
			SVGHandle handle, Set<Element> elementsSet,
				Point2D centerPoint, double angle) {

		//getting the rotation affine transform
		AffineTransform actionTransform=
			AffineTransform.getRotateInstance(angle, centerPoint.getX(), centerPoint.getY());
		
		return applyTransform(handle, elementsSet.iterator().next(), 
				actionTransform, rotateUndoRedoLabel);
	}
	
	@Override
	public UndoRedoAction skew(
			SVGHandle handle, Set<Element> elementsSet, Point2D centerPoint, 
				double skewFactor, boolean isHorizontal) {

		Element element=elementsSet.iterator().next();
		
		//getting the skew affine transform
		AffineTransform actionTransform=ShapeToolkit.getSkewAffineTransform(
				handle, element, centerPoint, skewFactor, isHorizontal);

		return applyTransform(handle, element, actionTransform, skewUndoRedoLabel);
	}
	
	/**
	 * computes the new coordinates of the element according to the transform
	 * a returns an undo/redo action
	 * @param handle a svg handle
	 * @param element the element that will be transformed
	 * @param transform the transform to apply
	 * @param actionUndoRedoLabel the action undo/redo label
	 * @return an undo/redo action
	 */
	protected UndoRedoAction applyTransform(
		final SVGHandle handle, final Element element, 
			AffineTransform transform, String actionUndoRedoLabel){
		
		//getting the initial transform 
		final AffineTransform initialTransform=
			handle.getSvgElementsManager().getTransform(element);

		//getting the new transform
		final AffineTransform newTransform=new AffineTransform(initialTransform);
		newTransform.preConcatenate(transform);

		//setting the new x and y attributes for the elements
		Runnable executeRunnable=new Runnable() {

			public void run() {
				
				handle.getSvgElementsManager().
					setTransform(element, newTransform);
			}
		};
		
		//the undo runnable
		Runnable undoRunnable=new Runnable() {

			public void run() {

				handle.getSvgElementsManager().
					setTransform(element, initialTransform);
			}
		};

		//executing the action and creating the undo/redo action
		HashSet<Element> elements=new HashSet<Element>();
		elements.add(element);
		UndoRedoAction undoRedoAction=ShapeToolkit.getUndoRedoAction(
				actionUndoRedoLabel, executeRunnable, undoRunnable, elements);

		return undoRedoAction;
	}

	@Override
	public CanvasPainter showTranslateAction(SVGHandle handle, 
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {

		CanvasPainter canvasPainter=null;
		
		//getting the element that will undergo the action
		Element element=elementSet.iterator().next();
		
		//getting the initial line
		Shape shape=getTransformedShape(handle, element, true);

		if(shape!=null){
			
			Point2D translationCoefficients=new Point2D.Double(
					currentPoint.getX()-firstPoint.getX(), currentPoint.getY()-firstPoint.getY());

			AffineTransform transform=AffineTransform.getTranslateInstance(
					translationCoefficients.getX(), translationCoefficients.getY());
			
			//computing the screen scaled shape
			final Shape fshape=handle.getTransformsManager().
				getScaledShape(shape, false, transform);
			
			//creating the set of the clips
			final Set<Rectangle2D> fclips=new HashSet<Rectangle2D>();
			fclips.add(fshape.getBounds2D());

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
		
		return canvasPainter;
	}
	
	@Override
	public UndoRedoAction validateTranslateAction(SVGHandle handle, Set<Element> elementSet, 
			Point2D firstPoint, Point2D currentPoint) {

		return translate(handle, elementSet, new Point2D.Double(
				currentPoint.getX()-firstPoint.getX(), currentPoint.getY()-firstPoint.getY()));
	}
	
	@Override
	public CanvasPainter showAction(
			SVGHandle handle, int level, Set<Element> elementSet, SelectionItem item, 
			Point2D firstPoint, Point2D currentPoint) {
		
		//getting the element that will undergo the action
		Element element=elementSet.iterator().next();

		//the canvas painter that should be returned
		CanvasPainter painter=null;
		
		//whether the shape should be painted
		boolean canPaintShape=true;
		
		//the shape that will be painted
		Shape shape=null;
		
		//getting the action transform
		AffineTransform actionTransform=null;
		
		switch (level){

			case 0 :
				
				//getting the resize transform
				actionTransform=getResizeTransform(
						handle, element, item, firstPoint, currentPoint);
				break;
				
			case 1 :
				
				if(item.getType()==SelectionItem.CENTER){

					//storing the center point for the rotate action
					rotationSkewSelectionItemCenterPoint=currentPoint;
					item.setPoint(currentPoint);
					canPaintShape=false;
					
				}else if(item.getType()==SelectionItem.NORTH_WEST || 
								item.getType()==SelectionItem.NORTH_EAST ||
								item.getType()==SelectionItem.SOUTH_EAST ||
								item.getType()==SelectionItem.SOUTH_WEST){
					
					//getting the rotation transform
					actionTransform=getRotationTransform(handle, element, firstPoint, currentPoint);
					
				}else{
					
					//getting the skew transform
					actionTransform=getSkewTransform(handle, element, firstPoint, currentPoint, item);
				}
				
				break;
		}
		
		if(actionTransform!=null){
			
			//getting the initial shape
			shape=getShape(handle, element, true);
			
			//getting the element's transform
			AffineTransform transform=
				handle.getSvgElementsManager().getTransform(element);
			
			//concatenating the action transform to the element's transform
			transform.preConcatenate(actionTransform);
			
			//computing the screen scaled shape
			shape=handle.getTransformsManager().
				getScaledShape(shape, false, transform);
		}

		if(canPaintShape && shape!=null){
			
			final Shape fshape=shape;

			//creating the set of the clips
			final HashSet<Rectangle2D> fclips=new HashSet<Rectangle2D>();
			fclips.add(fshape.getBounds2D());
			
			painter=new CanvasPainter(){

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
		
		return painter;
	}
	
	@Override
	public UndoRedoAction validateAction(
			SVGHandle handle,  int level, Set<Element> elementSet,
			SelectionItem item, Point2D firstPoint, Point2D lastPoint) {
		
		//the undo/redo action that will be returned
		UndoRedoAction undoRedoAction=null;

		//getting the element that will undergo the action
		Element element=elementSet.iterator().next();

		//executing the accurate action
		switch (level){
		
			case 0 :
				
				//getting the resize transform
				AffineTransform resizeTransform=getResizeTransform(
						handle, element, item, firstPoint, lastPoint);
				
				//executing the resize action
				undoRedoAction=resize(handle, elementSet, resizeTransform);
				break;
				
			case 1 :
				
				if(item.getType()!=SelectionItem.CENTER){

					//getting the center point
					Point2D centerPoint=getRotationSkewCenterPoint(handle, element);

					if(item.getType()==SelectionItem.NORTH_WEST || 
						item.getType()==SelectionItem.NORTH_EAST ||
						item.getType()==SelectionItem.SOUTH_EAST ||
						item.getType()==SelectionItem.SOUTH_WEST){
						
						//getting the angle for the rotation
						double angle=ShapeToolkit.getRotationAngle(
								centerPoint, firstPoint, lastPoint);
						
						//executing the rotation action
						undoRedoAction=rotate(handle, elementSet, centerPoint, angle);

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
						
						//executing the skew action
						undoRedoAction=skew(handle, elementSet, 
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
	
	@Override
	public Shape getShape(SVGHandle handle, Element element, boolean isOutline) {

		Shape shape=handle.getSvgElementsManager().
			getGeometryOutline(element);
		
		if(shape==null || shape.getBounds().getWidth()==0 || 
				shape.getBounds().getHeight()==0){
			
			//getting the location of the text
			double x=EditorToolkit.getAttributeValue(element, xAtt);
			double y=EditorToolkit.getAttributeValue(element, yAtt);
			
			shape=new Rectangle2D.Double(x, y, 1, 1);
		}
		
		return shape;
	}
	
	@Override
	public void setShape(SVGHandle handle, Element element, Shape shape) {}
	
	@Override
	public Shape getTransformedShape(
			SVGHandle handle, Element element, boolean isOutline) {

		Shape shape=getShape(handle, element, isOutline);
		
		if(shape!=null){
			
			//getting the transformed points
			AffineTransform af=
				handle.getSvgElementsManager().getTransform(element);
			
			if(af!=null && ! af.isIdentity()){
				
				shape=af.createTransformedShape(shape);
			}
		}
		 
		return shape;
	}
}
