package fr.itris.glips.svgeditor.shape;

import java.awt.*;
import java.awt.geom.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the superclass of all the module that handle ellipse shapes
 * @author ITRIS, Jordi SUC
 */
public class EllipseShape extends RectangularShape {
	
	/**
	 * the element attributes names
	 */
	protected static String cxAtt="cx", cyAtt="cy";
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public EllipseShape(Editor editor) {
		
		super(editor);
		
		shapeModuleId="EllipseShape";
		handledElementTagName="ellipse";
		retrieveLabels();
		createMenuAndToolItems();
	}
	
	@Override
	public void notifyDrawingAction(
			SVGHandle handle, Point2D point, int modifier, int type) {

		//getting the canvas
		SVGCanvas canvas=handle.getScrollPane().getSVGCanvas();
		
		// according to type of the event for the drawing action
		switch (type) {
			
			case DRAWING_MOUSE_PRESSED :
				
				if(firstDrawingPoint==null) {
					
					firstDrawingPoint=point;
				}

				break;
				
			case DRAWING_MOUSE_RELEASED :
				
				//computing the rectangle corresponding the first and current clicked points
				Rectangle2D rect=null;
				
				if(Editor.getEditor().getSquareModeManager().isSquareMode()){
					
					rect=EditorToolkit.getComputedSquare(firstDrawingPoint, point);
					
				}else{
					
					rect=EditorToolkit.getComputedRectangle(firstDrawingPoint, point);
				}
				
				//computing the base scaled rectangle 
				Rectangle2D rect2D=handle.getTransformsManager().
					getScaledRectangle(rect, true);
				
				//creating the svg shape
				createElement(handle, rect2D);

				//reinitializing the data
				firstDrawingPoint=null;
				canvas.removePaintListener(ghostCanvasPainter, true);
				ghostCanvasPainter.reinitialize();
				resetDrawing();
				break;
				
			case DRAWING_MOUSE_DRAGGED :
				
				//removing the ghost canvas painter
				if(ghostCanvasPainter.getClip()!=null) {
					
					canvas.removePaintListener(ghostCanvasPainter, false);
				}

				//computing the rectangle corresponding the first and current clicked points
				if(Editor.getEditor().getSquareModeManager().isSquareMode()){
					
					rect=EditorToolkit.getComputedSquare(firstDrawingPoint, point);
					
				}else{
					
					rect=EditorToolkit.getComputedRectangle(firstDrawingPoint, point);
				}
				
				//getting the ellipse to draw
				Ellipse2D.Double shape=new Ellipse2D.Double(
						rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

				//setting the new shape to the ghost painter
				ghostCanvasPainter.setShape(shape);
				canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER, ghostCanvasPainter, true);
				break;
		}
	}
	
	@Override
	public Element createElement(SVGHandle handle, Rectangle2D bounds){
		
		//the edited document
		Document doc=handle.getScrollPane().getSVGCanvas().getDocument();
		
		//normalizing the bounds of the element
		if(bounds.getWidth()<1){
		    
		    bounds.setRect(bounds.getX(), bounds.getY(), 1, bounds.getHeight());
		}
		
		if(bounds.getHeight()<1){
		    
			bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(), 1);
		}
		
		// creating the shape
		final Element element=doc.createElementNS
			(doc.getDocumentElement().getNamespaceURI(), handledElementTagName);
		
		EditorToolkit.setAttributeValue(element, cxAtt, bounds.getX()+bounds.getWidth()/2);
		EditorToolkit.setAttributeValue(element, cyAtt, bounds.getY()+bounds.getHeight()/2);
		EditorToolkit.setAttributeValue(element, rxAtt, bounds.getWidth()/2);
		EditorToolkit.setAttributeValue(element, ryAtt, bounds.getHeight()/2);
		
		//getting the last color that has been used by the user
		String colorString=Editor.getColorChooser().getColorString(ColorManager.getCurrentColor());
		element.setAttributeNS(null, "style", "fill:"+colorString+";stroke:#000000;");
		
		//inserting the element in the document and handling the undo/redo support
		insertShapeElement(handle, element);
		
		return element;
	}

	@Override
	public Shape getShape(SVGHandle handle, Element element, boolean isOutline){
		
		Ellipse2D.Double shape=null;
		
		//getting the bounds of the initial shape
		double initCX=EditorToolkit.getAttributeValue(element, cxAtt);
		double initCY=EditorToolkit.getAttributeValue(element, cyAtt);
		double initRX=EditorToolkit.getAttributeValue(element, rxAtt);
		double initRY=EditorToolkit.getAttributeValue(element, ryAtt);
		
		if(! Double.isNaN(initCX) && ! Double.isNaN(initCY) &&
			! Double.isNaN(initRX) && ! Double.isNaN(initRY)){
			
			//creating the shape
			shape=new Ellipse2D.Double(initCX-initRX, initCY-initRY, 2*initRX, 2*initRY);
		}
		
		return shape;
	}
	
	@Override
	public void setShape(SVGHandle handle, Element element, Shape shape) {

		if(shape!=null && shape instanceof java.awt.geom.RectangularShape){
			
			java.awt.geom.RectangularShape theShape=(java.awt.geom.RectangularShape)shape;
			
			EditorToolkit.setAttributeValue(element, cxAtt, theShape.getX()+theShape.getWidth()/2);
			EditorToolkit.setAttributeValue(element, cyAtt, theShape.getY()+theShape.getHeight()/2);
			EditorToolkit.setAttributeValue(element, rxAtt, theShape.getWidth()/2);
			EditorToolkit.setAttributeValue(element, ryAtt, theShape.getHeight()/2);
		}
	}
}
