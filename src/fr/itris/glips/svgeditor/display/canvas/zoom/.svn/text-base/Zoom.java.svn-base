package fr.itris.glips.svgeditor.display.canvas.zoom;

import java.awt.geom.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.display.canvas.*;

/**
 * the class of the zoom
 * @author ITRIS, Jordi SUC
 */
public class Zoom {

	/**
	 * the canvas
	 */
	private SVGCanvas canvas;
	
	/**
	 * the min zoom factor
	 */
	public static final double minZoomFactor=0.001;
	
	/**
	 * the max zoom factor
	 */
	public static final double maxZoomFactor=29;
	
	/**
	 * the default zoom factors
	 */
	public static final double[] defaultZoomFactors={
		0.05, 0.1, 0.2, 0.5, 0.75, 1, 2, 5, 7.5, 10, 20};
	
	/**
	 * the zoom increment
	 */
	public static final double zoomIncrement=0.1;

	/**
	 * the current canvas scale
	 */
	private double currentScale=1;
	
	/**
	 * the constructor of the class
	 * @param canvas the canvas to which this object is linked
	 */
	public Zoom(SVGCanvas canvas){
		
		this.canvas=canvas;
	}
	
	/**
	 * @return the current canvas scale
	 */
	public double getCurrentScale() {
		return currentScale;
	}
	
	/**
	 * sets the current scale to the next zoom in scale relatively 
	 * to the current scale
	 */
	public void setToNextZoomInScale(){
		
		double newScale=currentScale+zoomIncrement;
		
		if(newScale>maxZoomFactor) {
			
			newScale=maxZoomFactor;
		}
		
		scaleTo(newScale);
	}
	
	/**
	 * sets the current scale to the next zoom out scale relatively 
	 * to the current scale
	 */
	public void setToNextZoomOutScale(){
		
		double newScale=currentScale-zoomIncrement;
		
		if(newScale<minZoomFactor) {
			
			newScale=minZoomFactor;
		}
		
		scaleTo(newScale);
	}

	/**
	 * sets the new s factor
	 * @param scale a positive number
	 */
	public void scaleTo(double scale){

		scale=checkScale(scale);

		//getting the current scroll values of the scrollpane and 
		//computing the center point
		Dimension scrollValues=canvas.getScrollPane().getScrollValues();
		final Rectangle viewPortBounds=
			canvas.getScrollPane().getViewport().getViewRect();
		final Point2D centerPoint=new Point2D.Double(
				(scrollValues.getWidth()+viewPortBounds.getWidth()/2)/currentScale,
				(scrollValues.getHeight()+viewPortBounds.getHeight()/2)/currentScale);

		//setting the new scale
		this.currentScale=scale;

		canvas.getScrollPane().setListenersEnabled(false);
		canvas.setCanvasPreferredSize(canvas.getScaledCanvasSize());
		canvas.getScrollPane().getInnerScrollpane().validate();
		
		//computing the new center point
		Point2D newCenterPoint=new Point2D.Double(
				centerPoint.getX()*currentScale, centerPoint.getY()*currentScale);
		Dimension newScrollValues=new Dimension(
				(int)Math.round(newCenterPoint.getX()-viewPortBounds.getWidth()/2),
				(int)Math.round(newCenterPoint.getY()-viewPortBounds.getHeight()/2));

		//setting the new scroll values
		canvas.getScrollPane().setScrollValues(newScrollValues);
		
		//refreshing the svg content
		Rectangle newViewPortBounds=new Rectangle(
				canvas.getScrollPane().getViewport().getViewRect());
		canvas.setRenderedRectangle(
				newViewPortBounds, false, true);

		canvas.getScrollPane().refreshRulers();
		canvas.getSVGHandle().getSelection().refreshSelection(false);
		
		SwingUtilities.invokeLater(new Runnable(){
			
			public void run() {
				
				canvas.getScrollPane().setListenersEnabled(true);
			}
		});
		
		//displaying the new zoom factor
		canvas.getSVGHandle().getSVGFrame().getStateBar().
			setZoomFactor(currentScale);
	}
	
	/**
	 * setting the scale so that the provided rectangle is 
	 * @param zoomZone the rectangle defining a selected area on the canvas
	 */
	public void scaleTo(Rectangle2D zoomZone){
		
		if(zoomZone!=null){
			
			//getting the view port bounds
			Rectangle2D viewportBounds=canvas.getScrollPane().getViewPortBounds();
			
			//getting the center point of the rectangle in the canvas coordinates
			final Point2D centerPoint=new Point2D.Double(
				zoomZone.getX()+zoomZone.getWidth()/2,
					zoomZone.getY()+zoomZone.getHeight()/2);

			//computing the new scale and the scroll bar values for the scrollpane
			double newScale=1.0;
			double vScale=viewportBounds.getWidth()/zoomZone.getWidth();
			double hScale=viewportBounds.getHeight()/zoomZone.getHeight();
			
			if(vScale>=hScale){
				
				newScale=hScale;
				
			}else{
				
				newScale=vScale;
			}

			//checking if the scale is correct
			newScale=checkScale(newScale);
			this.currentScale=newScale;
			
			canvas.getScrollPane().setListenersEnabled(false);
			canvas.setCanvasPreferredSize(canvas.getScaledCanvasSize());
			canvas.getScrollPane().getInnerScrollpane().validate();
			
			Rectangle newViewPortBounds=new Rectangle(
					canvas.getScrollPane().getViewport().getViewRect());

			//computing the new center point
			Point2D newCenterPoint=new Point2D.Double(
					centerPoint.getX()*currentScale, centerPoint.getY()*currentScale);

			//computing the new scroll values
			Dimension newScrollValues=new Dimension(
					(int)Math.round(newCenterPoint.getX()-newViewPortBounds.getWidth()/2),
					(int)Math.round(newCenterPoint.getY()-newViewPortBounds.getHeight()/2));

			//setting the new scroll values
			canvas.getScrollPane().setScrollValues(newScrollValues);

			//rendering the picture
			Rectangle newViewPortBounds2=new Rectangle(
					canvas.getScrollPane().getViewport().getViewRect());

			canvas.setRenderedRectangle(
					newViewPortBounds2, false, true);

			canvas.getScrollPane().refreshRulers();
			canvas.getSVGHandle().getSelection().refreshSelection(false);

			SwingUtilities.invokeLater(new Runnable(){

				public void run() {

					canvas.getScrollPane().setListenersEnabled(true);
				}
			});
			
			//displaying the new zoom factor
			canvas.getSVGHandle().getSVGFrame().getStateBar().
				setZoomFactor(currentScale);
		}
	}
	
	/**
	 * checks if the provided scale checks the boundaries
	 * @param scale a scale
	 * @return the corrected scale
	 */
	protected double checkScale(double scale){
		
		if(scale<minZoomFactor){
			
			scale=minZoomFactor;
			
		}else if(scale>maxZoomFactor){
			
			scale=maxZoomFactor;
		}
		
		return scale;
	}
	
	/**
	 * scales the canvas so that the image takes the whole viewport
	 */
	public void fitImageToViewport(){
		
		//getting the canvas bounds
		Point2D canvasSize=canvas.getGeometryCanvasSize();

		if(canvasSize!=null){
			
			Rectangle2D imageBounds=new Rectangle2D.Double(
					0, 0, canvasSize.getX(), canvasSize.getY());
			
			scaleTo(imageBounds);
		}
	}
	
	/**
	 * scales the canvas so that the content nodes take the whole viewport
	 */
	public void fitContentToViewport(){
		
		Rectangle2D drawingBounds=
			canvas.getGraphicsNode().getSensitiveBounds();
		
		if(drawingBounds!=null){
			
			drawingBounds=new Rectangle2D.Double(
					drawingBounds.getX(), drawingBounds.getY(), 
						drawingBounds.getWidth(), drawingBounds.getHeight());
			
			scaleTo(drawingBounds);
		}
	}
	
	/**
	 * scales the canvas so that the selected nodes take the whole viewport
	 */
	public void fitSelectedNodesToViewport(){
		
		Collection<Element> selectedNodes=
			new HashSet<Element>(canvas.getSVGHandle().
					getSelection().getSelectedElements());

		Rectangle2D bounds=null;
		Rectangle2D wholeBounds=null;
		
		for(Element element : selectedNodes){
			
			if(element!=null){
				
				bounds=canvas.getSVGHandle().
					getSvgElementsManager().getSensitiveBounds(element);
				
				if(wholeBounds==null){
					
					wholeBounds=new Rectangle2D.Double(
						bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
					
				}else{
					
					wholeBounds.add(bounds);
				}
			}
		}
		
		if(wholeBounds!=null){
			
			scaleTo(wholeBounds);
		}
	}
}
