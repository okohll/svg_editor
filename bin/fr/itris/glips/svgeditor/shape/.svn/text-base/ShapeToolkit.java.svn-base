package fr.itris.glips.svgeditor.shape;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;

/**
 * the shape toolkit
 * @author ITRIS, Jordi SUC
 */
public class ShapeToolkit {

	/**
	 * returns the shape module that can handle the given element
	 * @param element an element
	 * @return the shape module that can handle the given element
	 */
	public static AbstractShape getShapeModule(Element element) {
		
		AbstractShape shape=null;
		
		if(element!=null) {
			
			//the set of all the current modules
			Set<AbstractShape> modules=
				Editor.getEditor().getSVGModuleLoader().getShapeModules();
			
			//the set of all the modules that match the provided element
			Set<AbstractShape> possibleModules=
				new HashSet<AbstractShape>();

			for(AbstractShape shapeModule : modules) {

				if(shapeModule.isElementTypeSupported(element)) {
					
					possibleModules.add(shapeModule);
				}
			}
			
			if(possibleModules.size()==1){
				
				shape=possibleModules.iterator().next();
				
			}else if(possibleModules.size()>1){
				
				int i=0;
				
				for(AbstractShape shapeModule : possibleModules) {
					
					if(shapeModule.isPrioritary() || i==possibleModules.size()-1){
						
						shape=shapeModule;
						break;
					}
					
					i++;
				}
			}
		}
		
		return shape;
	}
	
	/**
	 * applies the element's transformation to the given shape and
	 * returns the resulting shape
	 * @param svgHandle a svg handle
	 * @param element an element
	 * @param shape a shape
	 * @return the transformed shape
	 */
	public static Shape getTransformedShape(
			SVGHandle svgHandle, Element element, Shape shape){
		
		Shape transformedShape=shape;
		
		//getting the element's affine transform
		AffineTransform af=
			svgHandle.getSvgElementsManager().getTransform(element);
		
		if(! af.isIdentity()){
			
			transformedShape=af.createTransformedShape(shape);
		}
		
		return transformedShape;
	}
	
	/**
	 * returns the translation coefficients taking into account the element's transform
	 * @param handle the svg handle of the canvas into which the translation has been done
	 * @param element the element to be handled
	 * @param deltaX the absciss factor of the translation
	 * @param deltaY the ordinate factor of the translation
	 * @return the translation coefficients taking into account the element's transform
	 */
	public static Point2D getTranslationFactors(
			SVGHandle handle, Element element, double deltaX, double deltaY){
		
		//getting the element's transform
		AffineTransform af=handle.getSvgElementsManager().
			getTransform(element);

		//computing the new location for the element
		double e0=deltaX;
		double f0=deltaY;
		double a=af.getScaleX();
		double b=af.getShearY();
		double c=af.getShearX();
		double d=af.getScaleY();
		
		double e1=0;
		double f1=0;
		
		if(d!=0){
			
			e1=(e0*d-c*f0)/(a*d-b*c);
			f1=(f0-b*e1)/d;
			
		}else if(c!=0 && b!=0){
			
			e1=f0/b;
			f1=e0/c;
		}

		return new Point2D.Double(e1, f1);
	}
	
	/**
	 * returns the angle between the two segment defined by the three points 
	 * @param centerPoint the center point
	 * @param firstPoint the point defining the first segment whose origin is the center point
	 * @param secondPoint the point  the point defining the second segment whose origin is the center point
	 * @return an angle the point defining the second segment with the center point
	 */
	public static double getRotationAngle(Point2D centerPoint,
							Point2D firstPoint, Point2D secondPoint){
		
		//getting the first angle
		double angle1=getAngle(centerPoint, firstPoint);
		
		//getting the second angle
		double angle2=getAngle(centerPoint, secondPoint);
		
		//computing the rotation angle
		return angle2-angle1;
	}

	/**
	 * returns the angle in the trigonometric circle of the segment whose center is the center 
	 * point and the second point is the given point 
	 * @param centerPoint the center point
	 * @param point the other point og the segment
	 * @return an angle
	 */
	public static double getAngle(Point2D centerPoint, Point2D point){
		
		double angle=0;
		
		//translating the points into the trigonometric circle
		Point2D signedPoint=
			new Point2D.Double(point.getX()-centerPoint.getX(), point.getY()-centerPoint.getY());
		
		//putting the point into the upper right part of the trigonometric circle
		Point2D normalizedPoint=
			new Point2D.Double(Math.abs(signedPoint.getX()), Math.abs(signedPoint.getY()));

		//computing the angle
		angle=Math.abs(Math.atan2(normalizedPoint.getY(), normalizedPoint.getX()));

		//getting the angle for the point with signed coordinates
		if(signedPoint.getX()<0 && signedPoint.getY()>=0){
			
			angle=Math.PI-angle;
			
		}else if(signedPoint.getX()<0 && signedPoint.getY()<0){
			
			angle=Math.PI+angle;
			
		}else if(signedPoint.getX()>=0 && signedPoint.getY()<0){
			
			angle=2*Math.PI-angle;
		}
		
		return angle;
	}
	
	/**
	 * inserts a new undo/redo action in the undo/redo stack
	 * @param handle a svg handle
	 * @param actionLabel the label of the action
	 * @param executeRunnable the runnable used to execute the action
	 * @param undoRunnable the runnable used to undo the action
	 * @param elements the set of the elements to modify
	 */
	public static void addUndoRedoAction(
			SVGHandle handle, String actionLabel, Runnable executeRunnable, 
			Runnable undoRunnable, Set<Element> elements){
		
		//creating the undo/redo action and inserting it into the undo/redo stack
		UndoRedoAction action=getUndoRedoAction(
				actionLabel, executeRunnable, undoRunnable, elements);

		UndoRedoActionList actionlist=
			new UndoRedoActionList(actionLabel, false);
		actionlist.add(action);
		handle.getUndoRedo().addActionList(actionlist, true);
	}
	
	/**
	 * creates and returns to a undo/Redo action
	 * @param actionLabel the label of the action
	 * @param executeRunnable the runnable used to execute the action
	 * @param undoRunnable the runnable used to undo the action
	 * @param elements the set of the elements to modify
	 * @return a undo/Redo action
	 */
	public static UndoRedoAction getUndoRedoAction(
			String actionLabel, Runnable executeRunnable, 
				Runnable undoRunnable, Set<Element> elements) {
		
		UndoRedoAction action=new UndoRedoAction(actionLabel, 
				executeRunnable, undoRunnable, executeRunnable, elements);
		
		return action;
	}
	
	/**
	 * returns the skew affine transform corresponding to the given elements
	 * @param svgHandle a svg handle
	 * @param element a svg element
	 * @param delta the difference between the first and the last point of the relative coordinate
	 * @param centerPoint the center point
	 * @param isHorizontal whether the skew action is horizontal or vertical
	 * @return  the skew affine transform corresponding to the given elements
	 */
	public static AffineTransform getSkewAffineTransform(
			SVGHandle svgHandle, Element element, Point2D centerPoint, 
				double delta, boolean isHorizontal){
		
		//getting the bounds of the element
		Rectangle2D bounds=
			svgHandle.getSvgElementsManager().getNodeGeometryBounds(element);
		
		if(centerPoint==null){

			//computing the center point
			centerPoint=new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
		}
		
		//getting the horizontal and vertical skew factors
		double skewX=0, skewY=0;
		
		if(bounds.getWidth()>0 && bounds.getHeight()>0){

			if(isHorizontal){
				
				skewX=(delta)/bounds.getHeight();
				
			}else{
				
				skewY=(delta)/bounds.getWidth();
			}
		}

		//creating the affine transform
		AffineTransform af=
			AffineTransform.getTranslateInstance(-centerPoint.getX(), -centerPoint.getY());
		af.preConcatenate(AffineTransform.getShearInstance(skewX, skewY));
		af.preConcatenate(
				AffineTransform.getTranslateInstance(centerPoint.getX(), centerPoint.getY()));

		return af;
	}
}
