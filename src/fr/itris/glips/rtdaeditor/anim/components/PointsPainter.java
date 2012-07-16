package fr.itris.glips.rtdaeditor.anim.components;

import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.canvas.dom.*;
import java.util.*;
import java.util.concurrent.*;
import java.awt.*;
import java.awt.geom.*;
import org.w3c.dom.*;

/**
 * the class of the painter used to paint the points 
 * that are obtained from a point chooser
 * @author ITRIS, Jordi SUC
 */
public class PointsPainter {

	/**
	 * the current animation object
	 */
	private AnimationObject animationObject;
	
	/**
	 * the current canvas
	 */
	private SVGCanvas canvas;
	
	/**
	 * the dom listener
	 */
	private SVGDOMListener domListener;
	
	/**
	 * the current canvas paint listener
	 */
	private CanvasPainter canvasPaintListener;
	
	/**
	 * the map of the points
	 */
	private Map<AttributeObject, Point2D> points=
		new ConcurrentHashMap<AttributeObject, Point2D>();
	
	/**
	 * the map of the colors
	 */
	private Map<AttributeObject, Color> colors=new HashMap<AttributeObject, Color>();
	
	/**
	 * whether this painter is enable or not
	 */
	private boolean enable=true;

	/**
	 * the constructor of the class
	 */
	public PointsPainter() {}
	
	/**
	 * sets the current animation object
	 * @param animationObject the current animation object
	 */
	public void setCurrentAnimationObjet(final AnimationObject animationObject) {

		//reinitializing the points painter
		this.animationObject=animationObject;
		
		if(canvas!=null) {

			canvas.removePaintListener(canvasPaintListener, true);
			canvas.getSVGHandle().
				getSvgDOMListenerManager().removeDOMListener(domListener);

			canvas=null;
			canvasPaintListener=null;
			domListener=null;
			points.clear();
			colors.clear();
		}

		if(animationObject!=null) {
			
			//getting the canvas
			try{
				canvas=Editor.getEditor().getHandlesManager().
					getCurrentHandle().getScrollPane().getSVGCanvas();
			}catch (Exception ex) {}
			
			if(canvas!=null) {

				//checking if the new animation object contains attributes that handles points, and then 
				//retrieves the values of the points
				Point2D point2D=null;
				
				for(final AttributeObject attributeObject : animationObject.getAttributesList()) {
					
					if(attributeObject!=null && 
							attributeObject.getType().equals(EditableItem.POINT_CHOOSER)) {

						point2D=getPoint(attributeObject);
						
						if(point2D!=null) {
							
							//getting the color corresponding to this point
							String colorStr=attributeObject.getColor();
							String[] colorSplitStrs=colorStr.split(EditableItem.separatorRegex);

							//getting the colors corresponding to these strings
							Color color=Editor.getColorChooser().getColor(null, colorSplitStrs[0]);

							//filling the colors map
							colors.put(attributeObject, color);
						}
					}
				}

				//adding the painter and the dom listener
				if(points.size()>0) {
					
					canvasPaintListener=new CanvasPainter() {
						
						/**
						 * the size of each line of the representation of the point
						 */
						private int size=4;
						
						@Override
						public void paintToBeDone(Graphics2D g) {

							Point2D pt=null;
							Color color=null;
							
							//painting each point on the canvas
							for(AttributeObject attObj : points.keySet()) {
								
								pt=points.get(attObj);
								
								if(pt!=null) {
									
									//making this point absolute
									pt=setToAbsolute(pt);
									
									if(pt!=null) {
										
										color=colors.get(attObj);
										
										if(color==null) {
											
											color=Color.black;
										}
										
										g.setColor(color.darker().darker());
										g.drawLine((int)(pt.getX()-size), (int)(pt.getY()-size), 
												(int)(pt.getX()+size), (int)(pt.getY()+size));
										g.setColor(color.brighter().brighter().brighter().brighter());
										g.drawLine((int)(pt.getX()-size), (int)(pt.getY()+size), 
												(int)(pt.getX()+size), (int)(pt.getY()-size));
									}
								}
							}
						}
						
						@Override
						public Set<Rectangle2D> getClip() {

							//filling the set of the clip rectangle
							/*HashSet<Rectangle2D> clips=new HashSet<Rectangle2D>();
							Rectangle2D rect=null;
							
							for(Point2D point : new HashSet<Point2D>(points.values())){
								
								rect=new Rectangle2D.Double(point.getX()-size, point.getY()-size, 2*size, 2*size);
								clips.add(rect);
							}
							
							return clips;*/
							
							return null;
						}
					};
					
					if(enable) {
						
						canvas.addLayerPaintListener(SVGCanvas.TOP_LAYER, canvasPaintListener, true);
					}

					//creating the dom listener
					domListener=new SVGDOMListener(animationObject.getAnimationElement()) {
						
						@Override
						public void nodeChanged() {
						
							 handleEvent();
						}
						
						@Override
						public void structureChanged(Node lastModifiedNode) {
						
							 handleEvent();
						}
						
						@Override
						public void nodeInserted(Node insertedNode) {}
						
						@Override
						public void nodeRemoved(Node removedNode) {}
						
						/**
						 * handles the event
						 */
						protected void handleEvent() {
							
							if(canvasPaintListener!=null){
								
								canvas.removePaintListener(canvasPaintListener, true);
							}
							
							Point2D pt=null;
							
							for(final AttributeObject attributeObject : animationObject.getAttributesList()) {
								
								if(attributeObject!=null && 
										attributeObject.getType().equals(EditableItem.POINT_CHOOSER)) {

									//getting the point defined by the attribute, and putting it into the map
									pt=getPoint(attributeObject);
									
									if(pt!=null) {
										
										points.put(attributeObject, pt);
										
									}else {
										
										points.remove(attributeObject);
									}
								}
							}
							
							canvas.removePaintListener(canvasPaintListener, true);
							canvas.addLayerPaintListener(
								SVGCanvas.TOP_LAYER, canvasPaintListener, true);
						}
					};
					
					canvas.getSVGHandle().getSvgDOMListenerManager().
						addDOMListener(domListener);
				}
			}
		}
	}
	
	/**
	 * refreshes the displayed point chooser items
	 */
	public void refresh(){}
	
	/**
	 * computes the point to be displayed on the canvas of the given attribute object
	 * @param attObj an attribute object
	 * @return the point to be displayed on the canvas of the given attribute object
	 */
	protected Point2D getPoint(AttributeObject attObj){
		
		Point2D point=null;
		
		if(attObj!=null) {

			//getting the two values of the group item
			String value=attObj.getValue();
			String defaultValue=attObj.getDefaultValue();
			String[] defaultSplitValue=defaultValue.split(EditableItem.separatorRegex);
			String[] splitValue=value.split(EditableItem.separatorRegex);
			String value1=defaultSplitValue[0];
			String value2=defaultSplitValue[1];
			
			if(splitValue!=null && splitValue.length>1){
				
				value1=splitValue[0];
				value2=splitValue[1];
			}
			
			try {
				double x=Double.parseDouble(value1);
				double y=Double.parseDouble(value2);
				point=new Point2D.Double(x, y);
			}catch (Exception ex) {}
			
			if(point!=null) {
				
				points.put(attObj, point);
			}
		}

		return point;
	}
	
	/**
	 * sets whether this painter is enabled or not
	 * @param enable whether this painter is enabled or not
	 */
	public void setEnabled(boolean enable) {
		
		if(this.enable!=enable) {
			
			this.enable=enable;
			
			if(canvas!=null && canvasPaintListener!=null) {
				
				if(enable) {
					
					canvas.addLayerPaintListener(SVGCanvas.TOP_LAYER, canvasPaintListener, true);
					
				}else {
					
					canvas.removePaintListener(canvasPaintListener, true);
				}
			}
		}
	}
	
	/**
	 * set the given point in absolute coordinates according to the upper left corner of the 
	 * representation of the parent node
	 * @param point a point 
	 * @return the absolute point
	 */
	protected Point2D setToAbsolute(Point2D point){
		
		Point2D resPoint=null;
		Node parent=animationObject.getAnimationElement().getParentNode();
		
		if(parent!=null && parent instanceof Element){
			
			Rectangle2D elementBounds=canvas.getSVGHandle().getSvgElementsManager().
				getNodeBounds((Element)parent);
			
			if(point!=null && elementBounds!=null){
				
				//scaling the point to handle, to the current scale
				point=canvas.getSVGHandle().getTransformsManager().
					getScaledPoint(point, false);
				resPoint=new Point2D.Double(point.getX()+elementBounds.getX(),
						point.getY()+elementBounds.getY());
			}
		}
		
		return resPoint;
	}
}
