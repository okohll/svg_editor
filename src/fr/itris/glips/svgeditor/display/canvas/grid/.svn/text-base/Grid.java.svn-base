package fr.itris.glips.svgeditor.display.canvas.grid;

import java.awt.*;
import java.awt.geom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.*;

/**
 * the class used to display a grid on a canvas
 * @author ITRIS, Jordi SUC
 */
public class Grid {

	/**
	 * the canvas
	 */
	private SVGCanvas canvas;

	/**
	 * the constructor of the class
	 * @param canvas the canvas
	 */
	public Grid(SVGCanvas canvas){
		
		this.canvas=canvas;
		initializeGrid();
	}
	
	/**
	 * initializes the grid
	 */
	protected void initializeGrid(){
		
		//creating the paint listener
		CanvasPainter paintListener=new CanvasPainter(){
			
			@Override
			public void paintToBeDone(Graphics2D g) {
				
				//getting the grid parameters handler
				GridParametersManager handler=Editor.getEditor().
					getHandlesManager().getGridParametersHandler();
				
				if(handler.isGridEnabled()){

					//getting the grid parameters
					double horizontalDistance=handler.getHorizontalDistance();
					double verticalDistance=handler.getVerticalDistance();
					Color gridColor=handler.getGridColor();
					BasicStroke gridStroke=handler.getGridStroke();
					
					//getting the canvas bounds and the viewport
					Rectangle canvasBounds=canvas.getScrollPane().getCanvasBounds();
					Rectangle viewportRectangle=canvas.getScrollPane().getViewPortBounds();

					Rectangle2D scaledCanvasBounds=canvas.getSVGHandle().
						getTransformsManager().getScaledRectangle(
							new Rectangle2D.Double(0, 0, canvasBounds.width, canvasBounds.height), true);
					Rectangle2D innerRectangle=canvas.getSVGHandle().
						getTransformsManager().getScaledRectangle(
							new Rectangle2D.Double(-canvasBounds.x, -canvasBounds.y, 
									viewportRectangle.width, viewportRectangle.height), true);
					
					Point2D point=null;
					Rectangle2D.Double resultRect=new Rectangle2D.Double();

					if(canvasBounds.x>=0){
						
						resultRect.x=scaledCanvasBounds.getX();
						resultRect.width=scaledCanvasBounds.getWidth();
						
					}else{
						
						resultRect.x=innerRectangle.getX();
						resultRect.width=innerRectangle.getWidth();
					}
					
					if(canvasBounds.y>=0){

						resultRect.y=scaledCanvasBounds.getY();
						resultRect.height=scaledCanvasBounds.getHeight();
						
					}else{
						
						resultRect.y=innerRectangle.getY();
						resultRect.height=innerRectangle.getHeight();
					}
					
					Graphics2D g2=(Graphics2D)g.create();
					g2.setColor(gridColor);
					g2.setXORMode(Color.white);
					
					if(gridStroke!=null){
						
						g2.setStroke(gridStroke);
					}

					//getting the clip rectangle
					Rectangle clip=g2.getClip().getBounds();
					
					double startx=(Math.floor(resultRect.x/horizontalDistance)+1)*horizontalDistance;
					double starty=(Math.floor(resultRect.y/verticalDistance)+1)*verticalDistance;
					
					if(clip!=null){

						for(double i=startx; i<resultRect.x+resultRect.width; i+=horizontalDistance){
							
							point=canvas.getSVGHandle().getTransformsManager().
								getScaledPoint(new Point2D.Double(i, 0), false);
							
							if(point.getX()>=clip.x && point.getX()<=(clip.x+clip.width)){
								
								g2.drawLine((int)point.getX(), 0, (int) point.getX(), canvasBounds.height);
							}
						}

						for(double i=starty; i<resultRect.y+resultRect.height; i+=verticalDistance){
							
							point=canvas.getSVGHandle().getTransformsManager().
								getScaledPoint(new Point2D.Double(0, i), false);
							
							if(point.getY()>=clip.y && point.getY()<=(clip.y+clip.height)){
								
								g2.drawLine(0, (int)point.getY(), canvasBounds.width, (int)point.getY());
							}
						}
						
					}else{
						
						for(double i=startx; i<resultRect.x+resultRect.width; i+=horizontalDistance){
							
							point=canvas.getSVGHandle().getTransformsManager().
								getScaledPoint(new Point2D.Double(i, 0), false);
							
							g2.drawLine((int)point.getX(), 0,(int) point.getX(), canvasBounds.height);
						}

						for(double i=starty; i<resultRect.y+resultRect.height; i+=verticalDistance){
							
							point=canvas.getSVGHandle().getTransformsManager().
								getScaledPoint(new Point2D.Double(0, i), false);
							g2.drawLine(0, (int)point.getY(), canvasBounds.width, (int)point.getY());
						}
					}

					g2.dispose();
				}
			}
		};

		canvas.addLayerPaintListener(SVGCanvas.GRID_LAYER, paintListener, false);
	}
	
	/**
	 * refreshes the grid
	 */
	public void refresh(){
		
		canvas.doRepaint(null);
	}
}
