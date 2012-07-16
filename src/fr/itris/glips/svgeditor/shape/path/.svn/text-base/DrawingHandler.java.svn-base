package fr.itris.glips.svgeditor.shape.path;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.apache.batik.ext.awt.geom.*;
import org.w3c.dom.*;

import fr.itris.glips.svgeditor.*;




import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;
import fr.itris.glips.svgeditor.shape.*;
import fr.itris.glips.svgeditor.shape.path.segments.*;
import fr.itris.glips.library.geom.path.*;
import fr.itris.glips.library.geom.path.segment.Segment;

/**
 * the class that handles the creation of path elements
 * 
 * Drawing steps :
 * QuadTo : dragged -> released -> moved -> pressed
 * / CubicTo : moved -> pressed -> dragged -> released
 * / LineTo : moved -> pressed -> released
 * / MoveTo : pressed, when no segment has been 
 * 					created yet and no point has been registered yet
 * 
 * @author Jordi SUC
 */
public class DrawingHandler {

	/**
	 * the ghost painter
	 */
	private DefaultGhostShapeCanvasPainter ghostPainter=
		new DefaultGhostShapeCanvasPainter();
	
	/**
	 * the path shape module
	 */
	private PathShape pathShapeModule;
	
	/**
	 * the path that should be used to create the svg element
	 */
	private ExtendedGeneralPath path=new ExtendedGeneralPath();
	
	/**
	 * the path that contains all the segments before the previous action
	 */
	private ExtendedGeneralPath wholePath=new ExtendedGeneralPath();
	
	/**
	 * the path used for the drawing action
	 */
	private ExtendedGeneralPath drawnPath=new ExtendedGeneralPath();
	
	/**
	 * the path used to draw control point lines of the current segment
	 */
	private ExtendedGeneralPath controlPointsPath=new ExtendedGeneralPath();
	
	/**
	 * the list of the points that correspond to an event
	 */
	private LinkedList<ActionPoint> points=new LinkedList<ActionPoint>();
	
	/**
	 * the list of the segments
	 */
	private LinkedList<ActionSeg> segments=new LinkedList<ActionSeg>();
	
	/**
	 * the object storing information on the potential path that could be 
	 * connected to the path created in the drawing mode
	 */
	private PathToConnect drawingPathToConnect=null;
	
	/**
	 * the constructor of the class
	 * @param pathShapeModule the path shape module
	 */
	public DrawingHandler(PathShape pathShapeModule){
		
		this.pathShapeModule=pathShapeModule;
		
		//adding a listener to the keys entered by the user
		AWTEventListener keyListener=new AWTEventListener(){
			
			public void eventDispatched(AWTEvent evt) {
				
				//getting the key event
				KeyEvent event=(KeyEvent)evt;
				
				if(event!=null && event.isControlDown() && 
					event.getKeyCode()==KeyEvent.VK_Z &&
						event.getID()==KeyEvent.KEY_PRESSED && 
						(segments.size()>0 || points.size()>0)){

					event.consume();
					
					//getting the current handle
					SVGHandle handle=
						Editor.getEditor().getHandlesManager().getCurrentHandle();
					
					if(handle!=null){
						
						//whether a modification occured
						boolean modified=false;
						
						if(points.size()>1){
							
							points.clear();
							
							//clearing the drawing paths
							drawnPath.reset();
							controlPointsPath.reset();
							
							modified=true;
							
						}else if(segments.size()>1){
							
							points.clear();
							
							//clearing the drawing paths
							drawnPath.reset();
							controlPointsPath.reset();
							
							//removing the last segment
							segments.removeLast();
							
							//rebuilding the paths
							wholePath.reset();
							path.reset();
							Point2D pt=null, pt1=null, pt2=null;
							
							for(ActionSeg seg : segments){
								
								if(seg instanceof MoveToSeg){
									
									pt=seg.getEndPoint();
									wholePath.moveTo((float)pt.getX(), (float)pt.getY());
									
									pt=handle.getTransformsManager().getScaledPoint(pt, true);
									path.moveTo((float)pt.getX(), (float)pt.getY());

								}else if(seg instanceof LineToSeg){
									
									pt=seg.getEndPoint();
									wholePath.lineTo((float)pt.getX(), (float)pt.getY());
									
									pt=handle.getTransformsManager().getScaledPoint(pt, true);
									path.lineTo((float)pt.getX(), (float)pt.getY());
									
								}else if(seg instanceof QuadToSeg){
									
									pt=seg.getEndPoint();
									pt1=seg.getControlPoint();
									wholePath.quadTo((float)pt1.getX(), (float)pt1.getY(), 
											(float)pt.getX(), (float)pt.getY());
									
									pt=handle.getTransformsManager().getScaledPoint(pt, true);
									pt1=handle.getTransformsManager().getScaledPoint(pt1, true);
									path.quadTo((float)pt1.getX(), (float)pt1.getY(), 
											(float)pt.getX(), (float)pt.getY());
									
								}else if(seg instanceof CubicToSeg){
									
									pt=seg.getEndPoint();
									pt1=((CubicToSeg)seg).getFirstControlPoint();
									pt2=seg.getControlPoint();
									wholePath.curveTo((float)pt1.getX(), (float)pt1.getY(), 
										(float)pt2.getX(), (float)pt2.getY(),
											(float)pt.getX(), (float)pt.getY());
									
									pt=handle.getTransformsManager().getScaledPoint(pt, true);
									pt1=handle.getTransformsManager().getScaledPoint(pt1, true);
									pt2=handle.getTransformsManager().getScaledPoint(pt2, true);
									path.curveTo((float)pt1.getX(), (float)pt1.getY(), 
										(float)pt2.getX(), (float)pt2.getY(),
											(float)pt.getX(), (float)pt.getY());
								}
							}
							
							modified=true;
						}
						
						//repainting the shapes
						if(modified){
							
							paintShape(handle);
						}
					}
				}
			}
		};
		
		Toolkit.getDefaultToolkit().addAWTEventListener(
				keyListener, AWTEvent.KEY_EVENT_MASK);
	}

	/**
	 * @return the path that should be used to create the svg element
	 */
	public ExtendedGeneralPath getPath() {
		return wholePath;
	}

	/**
	 * @return the path that is drawn
	 */
	public ExtendedGeneralPath getDrawnPath() {
		return drawnPath;
	}

	/**
	 * notifies that the mouse has been pressed
	 * @param handle a svg handle
	 * @param point the where action occured
	 */
	public void mousePressed(SVGHandle handle, Point2D point){

		if(segments.size()==0 && points.size()==0){
			
			//handling the path to connect//
			//getting the selection item corresponding to the point
			SelectionItem item=handle.getSelection().getSelectionItem(point);
			
			if(item!=null && item.getElements().size()==1){
				
				//getting the point
				point=item.getPoint();
				
				//getting the element
				Element element=item.getElements().iterator().next();
				
				if(element!=null && 
						pathShapeModule.isElementTypeSupported(element)){
					
					//creating the object handling the path to connect
					drawingPathToConnect=new PathToConnect(element, item.getIndex());
					
					if(! drawingPathToConnect.isCorrectPath()){
						
						//the new path could not be connect to this path
						drawingPathToConnect=null;
					}
				}
			}
			
			//creating a moveTo segment, step 1/1
			segments.add(new MoveToSeg(point));
			wholePath.reset();
			wholePath.moveTo((float)point.getX(), (float)point.getY());
			
			//getting the base scaled point value
			Point2D scaledPoint=handle.getTransformsManager().getScaledPoint(point, true);
			path.reset();
			path.moveTo((float)scaledPoint.getX(), (float)scaledPoint.getY());
			
		}else if(points.size()==1 && 
					points.getLast().getActionType()==
						AbstractShape.DRAWING_MOUSE_MOVED){
			
			if(Editor.getEditor().getConstraintLinesModeManager().constraintLines()){
				
				//computing the point so that the line is constrained
				point=computeLinePointWhenLineConstraintModeActive(
						segments.getLast().getEndPoint(), point);
			}
			
			//the action is "cubicTo" step 2/6 or lineTo step 2/3,
			points.clear();
			points.add(new ActionPoint(
					AbstractShape.DRAWING_MOUSE_PRESSED, point));

		}else if(points.size()==2 && points.getLast().getActionType()==
						AbstractShape.DRAWING_MOUSE_MOVED){
			
			//the action is "quadTo" , step 4/4
			//creating the quadTo segment
			QuadToSeg seg=new QuadToSeg(points.getFirst().getActionPoint(), point);
			segments.add(seg);
			points.clear();
			
			//filling the path
			wholePath.quadTo((float)seg.getControlPoint().getX(), (float)seg.getControlPoint().getY(), 
					(float)seg.getEndPoint().getX(), (float)seg.getEndPoint().getY());
			
			//getting the base scaled points value
			Point2D scControlPoint=handle.getTransformsManager().
				getScaledPoint(seg.getControlPoint(), true);
			Point2D scEndPoint=handle.getTransformsManager().
				getScaledPoint(seg.getEndPoint(), true);
			path.quadTo((float)scControlPoint.getX(), (float)scControlPoint.getY(), 
					(float)scEndPoint.getX(), (float)scEndPoint.getY());
		}
	}
	
	/**
	 * notifies that the mouse has been released
	 * @param handle a svg handle
	 * @param point the where action occured
	 */
	public void mouseReleased(SVGHandle handle, Point2D point){

		if(points.size()==1 && 
				points.getLast().getActionType()==
					AbstractShape.DRAWING_MOUSE_DRAGGED){
			 
			//the action is "quadTo" , step 2/4
			points.clear();
			points.add(new ActionPoint(
					AbstractShape.DRAWING_MOUSE_RELEASED, point));
			
		}else if(points.size()==1 && 
				points.getLast().getActionType()==
					AbstractShape.DRAWING_MOUSE_PRESSED){
			
			//the action is "lineTo" , step 3/3
			LineToSeg seg=new LineToSeg(points.getFirst().getActionPoint());
			segments.add(seg);
			points.clear();
			
			//drawing the line
			wholePath.lineTo((float)seg.getEndPoint().getX(), (float)seg.getEndPoint().getY());
			
			//getting the base scaled points value
			Point2D scEndPoint=handle.getTransformsManager().
				getScaledPoint(seg.getEndPoint(), true);
			path.lineTo((float)scEndPoint.getX(), (float)scEndPoint.getY());
			
		}else if(points.size()==2 && 
				points.getLast().getActionType()==
					AbstractShape.DRAWING_MOUSE_DRAGGED){
			
			//the action is "cubicTo", step 4/4
			points.removeLast();
			
			//storing the current point
			points.add(new ActionPoint(
					AbstractShape.DRAWING_MOUSE_RELEASED, point));
			
			//computing the points of the segment
			Point2D endPoint=getEndPoint();
			Point2D pt1, pt2, pt3;
			
			pt1=points.get(0).getActionPoint();
			
			if(segments.getLast() instanceof CubicToSeg || 
					segments.getLast() instanceof QuadToSeg){
				
				pt2=Segment.computeSymetric(
						segments.getLast().getControlPoint(), 
							segments.getLast().getEndPoint());
				pt3=points.get(1).getActionPoint();
				
			}else{
				
				pt2=points.get(1).getActionPoint();
				pt3=Segment.computeSymetricRelCenter(pt2, endPoint, pt1);
			}
			
			//creating the cubicTo segment
			CubicToSeg seg=new CubicToSeg(pt2, pt3, pt1);
			segments.add(seg);
			points.clear();
			
			//adding the segment to the path
			wholePath.curveTo((float)pt2.getX(), (float)pt2.getY(), 
					(float)pt3.getX(), (float)pt3.getY(), 
					(float)pt1.getX(), (float)pt1.getY());
			
			//getting the base scaled points value
			Point2D scControlPoint1=handle.getTransformsManager().
				getScaledPoint(seg.getFirstControlPoint(), true);
			Point2D scControlPoint2=handle.getTransformsManager().
				getScaledPoint(seg.getControlPoint(), true);
			Point2D scEndPoint=handle.getTransformsManager().
				getScaledPoint(seg.getEndPoint(), true);
			path.curveTo((float)scControlPoint1.getX(), (float)scControlPoint1.getY(),
					(float)scControlPoint2.getX(), (float)scControlPoint2.getY(),
					(float)scEndPoint.getX(), (float)scEndPoint.getY());
		}
	}
	
	/**
	 * notifies that the mouse has been dragged
	 * @param handle a svg handle
	 * @param point the where action occured
	 */
	public void mouseDragged(SVGHandle handle, Point2D point){
		
		//removing the last point of the points list of its a dragged action point
		if(points.size()>0 && points.getLast().getActionType()==
				AbstractShape.DRAWING_MOUSE_DRAGGED){
			
			points.removeLast();
		}

		//initializing the drawing shape
		initializeDrawingPath();
		
		if(points.size()==0 && segments.size()>0){
			
			//recording the point
			points.add(new ActionPoint(
					AbstractShape.DRAWING_MOUSE_DRAGGED, point));
			
			//the action is "quadTo", step 1/4
			drawQuadToSegment();
			
		}else if(points.size()==1 && points.getLast().getActionType()==
					AbstractShape.DRAWING_MOUSE_PRESSED){
			
			//recording the point
			points.add(new ActionPoint(
					AbstractShape.DRAWING_MOUSE_DRAGGED, point));
			
			//the action is "cubicTo", step 3/4
			drawCubicToSegment();
		}
		
		//painting the shape
		paintShape(handle);
	}
	
	/**
	 * notifies that the mouse has been moved
	 * @param handle a svg handle
	 * @param point the where action occured
	 */
	public void mouseMoved(SVGHandle handle, Point2D point){

		//initializing the drawing shape
		initializeDrawingPath();

		//removing the last point of the points list of its a dragged action point
		if(points.size()>0 && points.getLast().getActionType()==
				AbstractShape.DRAWING_MOUSE_MOVED){
			
			points.removeLast();
		}
		
		if(points.size()==0 && segments.size()>0){
			
			if(Editor.getEditor().getConstraintLinesModeManager().constraintLines()){
				
				//computing the point so that the line is constrained
				point=computeLinePointWhenLineConstraintModeActive(
						segments.getLast().getEndPoint(), point);
			}
			
			//the action is "cubicTo" step 1/6 or lineTo step 1/3,
			points.add(new ActionPoint(
					AbstractShape.DRAWING_MOUSE_MOVED, point));
			
			//drawing the line
			drawnPath.lineTo((float)point.getX(), (float)point.getY());
				
		}else if(points.size()==1 && 
				points.getLast().getActionType()==
					AbstractShape.DRAWING_MOUSE_RELEASED){
			
			//the action is "quadTo", step 3/4
			points.add(new ActionPoint(
					AbstractShape.DRAWING_MOUSE_MOVED, point));
			
			drawQuadToSegment();
		}
		
		//painting the shape
		paintShape(handle);
	}
	
	/**
	 * notifies that the mouse has been double clicked
	 * @param handle a svg handle
	 * @param point the where action occured
	 */
	public void mouseDoubleClicked(SVGHandle handle, Point2D point){
		
		if(segments.size()>0){
			
			Element element=null;
			
			if(drawingPathToConnect!=null){
				
				//getting the element that should be modified
				final Element modifiedElement=drawingPathToConnect.getElement();
				element=modifiedElement;
				
				//getting the path to modified
				Path connectedPath=drawingPathToConnect.getPath();
				
				//duplicating the path so that it can be modified
				Path modifiedPath=new Path(connectedPath);
				
				//modifying the path
				if(drawingPathToConnect.isInsertBeforeAction()){
					
					modifiedPath.insertBefore(path);
					
				}else{
					
					modifiedPath.insertAfter(path);
				}
				
				//checking if the path should be closed
				if(Editor.getEditor().getClosePathModeManager().shouldClosePath()){
					
					modifiedPath.closePath();
				}

				//applying the modifications
				//getting the string representation of the both paths
				final String initDValue=connectedPath.toString();
				final String modifiedDValue=modifiedPath.toString();
				
				//setting the new translation factors
				Runnable executeRunnable=new Runnable() {

					public void run() {

						modifiedElement.setAttribute(PathShape.dAtt, modifiedDValue);
					}
				};
				
				//the undo runnable
				Runnable undoRunnable=new Runnable() {

					public void run() {

						modifiedElement.setAttribute(PathShape.dAtt, initDValue);
					}
				};
				
				//creating the undo/redo action, and adding it to the undo/redo stack
				Set<Element> elements=new HashSet<Element>();
				elements.add(modifiedElement);
				ShapeToolkit.addUndoRedoAction(
						handle, pathShapeModule.modifyPointUndoRedoLabel, 
							executeRunnable, undoRunnable, elements);

			}else{
				
				//creating the svg element
				element=pathShapeModule.createElement(handle, path);
			}

			//selecting the element
			handle.getSelection().handleSelection(element, false, true);
		}
		
		reset(handle);
	}
	
	/**
	 * resets the drawing action
	 * @param handle the current svg handle
	 */
	protected void reset(SVGHandle handle){
		
		if(segments.size()>0){
			
			path.reset();
			wholePath.reset();
			drawnPath.reset();
			controlPointsPath.reset();
			points.clear();
			segments.clear();
			drawingPathToConnect=null;
			
			//removing the ghost canvas painter
			if(ghostPainter.getClip()!=null) {
				
				handle.getCanvas().removePaintListener(ghostPainter, true);
			}
		}
	}
	
	/**
	 * initializes the drawing path
	 */
	protected void initializeDrawingPath(){
		
		drawnPath.reset();
		controlPointsPath.reset();
		
		//getting the endPoint of the segment
		Point2D endPoint=getEndPoint();
		
		if(endPoint!=null){
			
			//adding the moveTo command
			drawnPath.moveTo(
					(float)endPoint.getX(), (float)endPoint.getY());
			controlPointsPath.moveTo(
					(float)endPoint.getX(), (float)endPoint.getY());
		}
	}
	
	/**
	 * @return the end point of the last segment
	 */
	protected Point2D getEndPoint(){
		
		if(segments.size()>0){
			
			//getting the end point of the last added segment
			return segments.getLast().getEndPoint();
		}
		
		return null;
	}
	
	/**
	 * draws the current quadTo segment
	 */
	protected void drawQuadToSegment(){
		
		if(points.size()==1){
			
			Point2D point=points.getLast().getActionPoint();
			controlPointsPath.lineTo((float)point.getX(), (float)point.getY());
			drawCircle(controlPointsPath, point);
			
		}else if(points.size()==2){
			
			//drawing the curve
			Point2D pt1=points.get(0).getActionPoint();
			Point2D pt2=points.get(1).getActionPoint();
			
			drawnPath.quadTo((float)pt1.getX(), (float)pt1.getY(), 
					(float)pt2.getX(), (float)pt2.getY());
			controlPointsPath.lineTo((float)pt1.getX(), (float)pt1.getY());
			drawCircle(controlPointsPath, pt1);
		}
	}
	
	/**
	 * draws the current cubicTo segment
	 */
	protected void drawCubicToSegment(){
		
		if(points.size()==2){
			
			//getting the points
			Point2D endPoint=getEndPoint();
			Point2D pt1, pt2, pt3;
			
			pt1=points.get(0).getActionPoint();
			
			if(segments.getLast() instanceof CubicToSeg || 
					segments.getLast() instanceof QuadToSeg){
				
				pt2=Segment.computeSymetric(
						segments.getLast().getControlPoint(), 
							segments.getLast().getEndPoint());
				pt3=points.get(1).getActionPoint();
				
			}else{
				
				pt2=points.get(1).getActionPoint();
				pt3=Segment.computeSymetricRelCenter(pt2, endPoint, pt1);
			}

			drawnPath.curveTo((float)pt2.getX(), (float)pt2.getY(), 
					(float)pt3.getX(), (float)pt3.getY(), 
					(float)pt1.getX(), (float)pt1.getY());
			controlPointsPath.lineTo((float)pt2.getX(), (float)pt2.getY());
			drawCircle(controlPointsPath, pt2);
			
			controlPointsPath.moveTo((float)pt1.getX(), (float)pt1.getY());
			controlPointsPath.lineTo((float)pt3.getX(), (float)pt3.getY());
			drawCircle(controlPointsPath, pt3);
		}
	}
	
	/**
	 * paints the drawing shape
	 * @param handle a svg handle
	 */
	protected void paintShape(SVGHandle handle){
		
		//removing the ghost canvas painter
		if(ghostPainter.getClip()!=null) {
			
			handle.getCanvas().removePaintListener(ghostPainter, false);
		}
		
		//setting the new shapes to the ghost painter
		ghostPainter.reinitialize();
		ghostPainter.setPathShape(wholePath);
		ghostPainter.setCurrentSegmentShape(drawnPath);
		ghostPainter.setControlPointsShape(controlPointsPath);
		handle.getCanvas().addLayerPaintListener(
				SVGCanvas.DRAW_LAYER, ghostPainter, true);
	}
	
	/**
	 * draws a circle in the provided path whose center is the provided point
	 * @param aPath a path
	 * @param point the center of the circle
	 */
	protected void drawCircle(ExtendedGeneralPath aPath, Point2D point){
		
		int rx=2;
		aPath.append(new Ellipse2D.Double(
				point.getX()-rx, point.getY()-rx, 2*rx, 2*rx), false);
	}
	
	/**
	 * computes the last point of a line given the current point 
	 * of the mouse when the line constraint mode is active
	 * @param startPoint the start point for the segment
	 * @param basePoint the current's mouse point
	 * @return the computed point
	 */
	protected Point2D computeLinePointWhenLineConstraintModeActive(
			Point2D startPoint, Point2D basePoint){

	    Point2D.Double pt=new Point2D.Double();
	    
	    if(startPoint!=null && basePoint!=null){
	        
	        pt.x=basePoint.getX();
	        pt.y=basePoint.getY();
	        
	        Point2D.Double pt1=new Point2D.Double(startPoint.getX(), startPoint.getY());
	        Point2D.Double pt2=new Point2D.Double(basePoint.getX(), basePoint.getY());
	        
	        //the norme
	        double n=Math.sqrt(Math.pow((pt2.x-pt1.x), 2)+Math.pow((pt2.y-pt1.y), 2));
	        
	        //the x-distance and the y-distance
	        double xDistance=Math.abs(pt2.x-pt1.x), yDistance=Math.abs(pt2.y-pt1.y);

	        //the angle
	        double cosinus=(pt2.x-pt1.x)/n;

	        //computing the new point
	        if(pt1.x<=pt2.x && pt1.y>=pt2.y){
	            
	            if(cosinus<=1 && cosinus>Math.cos(Math.PI/8)){
	                
	               pt.x=(int)(pt1.x+xDistance);
	               pt.y=pt1.y;

	            }else if(cosinus<=Math.cos(Math.PI/8) && 
	            		cosinus>Math.cos(3*Math.PI/8)){
	                
	               pt.x=(int)(pt1.x+xDistance);
	               pt.y=(int)(pt1.y-xDistance);
	                
	            }else if(cosinus<=Math.cos(3*Math.PI/8) && cosinus>0){
	                
	               pt.x=pt1.x;
	               pt.y=(int)(pt1.y-yDistance);
	            }

	        }else if(pt1.x>pt2.x && pt1.y>=pt2.y){
	            
	            if(cosinus<=0 && cosinus>Math.cos(5*Math.PI/8)){
	                
		               pt.x=pt1.x;
		               pt.y=(int)(pt1.y-yDistance);

	            }else if(cosinus<=Math.cos(5*Math.PI/8) && cosinus>=Math.cos(7*Math.PI/8)){
	                
		               pt.x=(int)(pt1.x-xDistance);
		               pt.y=(int)(pt1.y-xDistance);
	                
	            }else if(cosinus<=Math.cos(7*Math.PI/8) && cosinus>=-1){
		               
		               pt.x=(int)(pt1.x-xDistance);
		               pt.y=pt1.y;
	            }

	        }else if(pt1.x>=pt2.x && pt1.y<pt2.y){
	            
	            if(cosinus>=-1 && cosinus<Math.cos(7*Math.PI/8)){
	                
		               pt.x=(int)(pt1.x-xDistance);
		               pt.y=pt1.y;

	            }else if(cosinus>=Math.cos(7*Math.PI/8) && cosinus<Math.cos(5*Math.PI/8)){
	                
		               pt.x=(int)(pt1.x-xDistance);
		               pt.y=(int)(pt1.y+xDistance);
		               
	            }else if(cosinus>=Math.cos(5*Math.PI/8) && cosinus<0){
	                
		               pt.x=pt1.x;
		               pt.y=(int)(pt1.y+yDistance);
	            }

	        }else if(pt1.x<=pt2.x && pt1.y<=pt2.y){
	            
	            if(cosinus>=0 && cosinus<Math.cos(3*Math.PI/8)){
	                
		               pt.x=pt1.x;
		               pt.y=(int)(pt1.y+yDistance);

	            }else if(cosinus>=Math.cos(3*Math.PI/8) && cosinus<Math.cos(Math.PI/8)){
	                
		               pt.x=(int)(pt1.x+xDistance);
		               pt.y=(int)(pt1.y+xDistance);
	                
	            }else if(cosinus>=Math.cos(Math.PI/8) && cosinus<1){
	                
		               pt.x=(int)(pt1.x+xDistance);
		               pt.y=pt1.y;
	            }
	        }
	    }

	    return pt;
	}
	
	/**
	 * the painter used to draw ghost shapes on a canvas
	 * @author ITRIS, Jordi SUC
	 */
	public class DefaultGhostShapeCanvasPainter extends CanvasPainter{
		
		/**
		 * the path shape
		 */
		private Shape pathShape;
		
		/**
		 * the current segment shape
		 */
		private Shape currentSegmentShape;
		
		/**
		 * the control points of the current segment shape
		 */
		private Shape controlPointsShape;
		
		/**
		 * the set of the clip rectangles
		 */
		private Set<Rectangle2D> clips=new HashSet<Rectangle2D>();
		
		@Override
		public void paintToBeDone(Graphics2D g) {
			
			g=(Graphics2D)g.create();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			
			if(pathShape!=null) {

				g.setColor(Color.black);
				g.draw(pathShape);
			}
			
			if(currentSegmentShape!=null) {

				g.setColor(Color.green);
				g.draw(currentSegmentShape);
			}
			
			if(controlPointsShape!=null) {

				g.setColor(Color.gray);
				g.draw(controlPointsShape);
			}
			
			g.dispose();
		}

		@Override
		public Set<Rectangle2D> getClip() {

			return clips;
		}

		/**
		 * sets the path shape
		 * @param pathShape the path shape
		 */
		public void setPathShape(Shape pathShape) {
			
			this.pathShape=pathShape;
			clips.add(pathShape.getBounds2D());
		}
		
		/**
		 * sets the current segment shape
		 * @param currentSegmentShape the current segment shape
		 */
		public void setCurrentSegmentShape(Shape currentSegmentShape) {
			
			this.currentSegmentShape=currentSegmentShape;
			clips.add(currentSegmentShape.getBounds2D());
		}

		/**
		 * sets the control points shape
		 * @param controlPointsShape the control points shape
		 */
		public void setControlPointsShape(Shape controlPointsShape) {
			
			this.controlPointsShape=controlPointsShape;
			clips.add(controlPointsShape.getBounds2D());
		}
		
		/**
		 * reinitializing the painter
		 */
		public void reinitialize() {
			
			clips.clear();
		}
	}
}
