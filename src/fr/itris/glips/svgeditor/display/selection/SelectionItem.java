package fr.itris.glips.svgeditor.display.selection;

import org.w3c.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;


/**
 * the class of the items that are drawn when a svg shape is selected
 * @author ITRIS, Jordi SUC
 */
public class SelectionItem implements Comparable<SelectionItem>{
	
	/**
	 * the NORTH constant
	 */
	public static final int NORTH=0;
	
	/**
	 * the EAST constant
	 */
	public static final int EAST=1;
	
	/**
	 * the SOUTH constant
	 */
	public static final int SOUTH=2;
	
	/**
	 * the WEST constant
	 */
	public static final int WEST=3;
	
	/**
	 * the NORTH_EAST constant
	 */
	public static final int NORTH_EAST=4;
	
	/**
	 * the SOUTH_EAST constant
	 */
	public static final int SOUTH_EAST=5;
	
	/**
	 * the SOUTH_WEST constant
	 */
	public static final int SOUTH_WEST=6;
	
	/**
	 * the NORTH_WEST constant
	 */
	public static final int NORTH_WEST=7;
	
	/**
	 * the CENTER constant
	 */
	public static final int CENTER=8;
	
	/**
	 * the POINT constant
	 */
	public static final int POINT=9;
	
	/**
	 * the CONTROL_POINT constant
	 */
	public static final int CONTROL_POINT=10;
	
	/**
	 * the LOCKED_POINT constant
	 */
	public static final int LOCKED_POINT=11;
	
	/**
	 * the ARROW_STYLE constant
	 */
	public static final int ARROW_STYLE=0;
	
	/**
	 * the CURVED_ARROW_STYLE constant
	 */
	public static final int CURVED_ARROW_STYLE=1;
	
	/**
	 * the POINT_STYLE constant
	 */
	public static final int POINT_STYLE=2;
	
	/**
	 * the CENTER_POINT_STYLE constant
	 */
	public static final int CENTER_POINT_STYLE=3;
	
	/**
	 * the LOSANGE_STYLE constant
	 */
	public static final int LOSANGE_STYLE=4;
	
	/**
	 * the INVERSE_ARROW_STYLE constant
	 */
	public static final int INVERSE_ARROW_STYLE=5;
	
	/**
	 * the OBLIQUE_ARROW_STYLE constant
	 */
	public static final int OBLIQUE_ARROW_STYLE=6;
	
	/**
	 * predefined affine transforms
	 */
	private static AffineTransform rotate90=AffineTransform.getRotateInstance(Math.PI/2),
														rotateMinus90=AffineTransform.getRotateInstance(-Math.PI/2),
														rotate180=AffineTransform.getRotateInstance(Math.PI);
	
	/**
	 * the available shapes for the items
	 */
	protected static final Map<Integer, Shape> shapes=new HashMap<Integer, Shape>();
	
	static {
		
		//creating the items shapes and filling the map of them//
		
		//the arrow shape
		GeneralPath path=new GeneralPath();
		path.moveTo(0, 6);
		path.lineTo(3, 3);
		path.lineTo(2, 3);
		path.lineTo(2, -3);
		path.lineTo(3, -3);
		path.lineTo(0, -6);
		path.lineTo(-3, -3);
		path.lineTo(-2, -3);
		path.lineTo(-2, 3);
		path.lineTo(-3, 3);
		path.closePath();
		shapes.put(ARROW_STYLE, path);
		
		//the inverse arrow shape
		path=new GeneralPath();
		path.moveTo(6, 0);
		path.lineTo(3, -3);
		path.lineTo(3, -2);
		path.lineTo(-3, -2);
		path.lineTo(-3, -3);
		path.lineTo(-6, 0);
		path.lineTo(-3, 3);
		path.lineTo(-3, 2);
		path.lineTo(3, 2);
		path.lineTo(3, 3);
		path.closePath();
		
		shapes.put(INVERSE_ARROW_STYLE, path);
		
		//the oblique arrow shape
		path=new GeneralPath();
		path.moveTo(-5, 5);
		path.lineTo(-1, 5);
		path.lineTo(-2, 4);
		path.lineTo(4, -2);
		path.lineTo(5, -1);
		path.lineTo(5, -5);
		path.lineTo(1, -5);
		path.lineTo(2, -4);
		path.lineTo(-4, 2);
		path.lineTo(-5, 1);
		path.closePath();
		shapes.put(OBLIQUE_ARROW_STYLE, path);

		int aoShW=12;
		int aoShH=12;
		
		//the curved arrow shape
		path=new GeneralPath();
		path.moveTo(-aoShW/2+4, aoShH/2-2);
		path.lineTo(aoShW/2-3, aoShH/2-2);
		path.lineTo(aoShW/2-3, aoShH/2);
		path.lineTo(aoShW/2, aoShH/2-3);
		path.lineTo(aoShW/2-3, aoShH/2-6);
		path.lineTo(aoShW/2-3, aoShH/2-4);
		path.lineTo(-aoShW/2+6, aoShH/2-4);
		path.lineTo(-aoShW/2+4, aoShH/2-6);
		
		path.lineTo(-aoShW/2+4, -aoShH/2+3);
		path.lineTo(-aoShW/2+6, -aoShH/2+3);
		path.lineTo(-aoShW/2+3, -aoShH/2);
		path.lineTo(-aoShW/2, -aoShH/2+3);
		path.lineTo(-aoShW/2+2, -aoShH/2+3);
		path.lineTo(-aoShW/2+2, aoShH/2-4);
		path.closePath();
		
		shapes.put(CURVED_ARROW_STYLE, path);
		
		//the point shape
		shapes.put(POINT_STYLE, new RoundRectangle2D.Double(-3, -3, 6, 6, 2, 2));
		
		//the center point shape
		shapes.put(CENTER_POINT_STYLE, new Ellipse2D.Double(-3, -3, 6, 6));
		
		//the losange shape
		path=new GeneralPath();
		path.moveTo(-2, 0);
		path.lineTo(0, -4);
		path.lineTo(2, 0);
		path.lineTo(0, 4);
		path.closePath();
		shapes.put(LOSANGE_STYLE, path);
	}
	
	/**
	 * the color used to paint the item when it is selected or not
	 */
	protected final static Color	
			singleSelectionItemStrokeColor=new Color(5, 53, 174),
			singleSelectionItemFillColor=singleSelectionItemStrokeColor.brighter().brighter().brighter(),
			singleSelectionSelectedItemStrokeColor=new Color(255, 108, 0),
			singleSelectionSelectedItemFillColor=singleSelectionSelectedItemStrokeColor.brighter().brighter(),
			multiSelectionItemStrokeColor=new Color(78, 191, 23), 
			multiSelectionItemFillColor=multiSelectionItemStrokeColor.brighter().brighter(),
			multiSelectionSelectedItemStrokeColor=new Color(205, 41, 141),
			multiSelectionSelectedItemFillColor=multiSelectionSelectedItemStrokeColor.brighter().brighter(),
			lockedSelectionItemStrokeColor=new Color(125, 125, 125), 
			lockedSelectionItemFillColor=lockedSelectionItemStrokeColor.brighter().brighter(),
			lockedSelectionSelectedItemStrokeColor=new Color(175, 175, 175),
			lockedSelectionSelectedItemFillColor=lockedSelectionSelectedItemStrokeColor.brighter().brighter();
	
	/**
	 * the svg handle the item is associated to
	 */
	private SVGHandle handle;
	
	/**
	 * the elements the item is associated to
	 */
	private Set<Element> elements=new HashSet<Element>();
	
	/**
	 * the point at which the element should be drawn on the canvas
	 */
	private Point2D point;
	
	/**
	 * the type of the item
	 */
	private int type;
	
	/**
	 * the style that should be used to draw the item
	 */
	private int style;
	
	/**
	 * the index of the item, it is only useful when the type of the item is POINT or CONTROL_POINT
	 * and is used to get the position of the item in a path
	 */
	private int index=0;
	
	/**
	 * the shape to be drawn
	 */
	private Shape shape;
	
	/**
	 * the bounds of the shape
	 */
	private Rectangle shapeBounds;
	
	/**
	 * whether several elements are selected at the same time
	 */
	private boolean isMultiSelection=false;
	
	/**
	 * the current color used to fill the item
	 */
	private Color currentStrokeColor, currentFillColor;
	
	/**
	 * the colors that will be used for this item
	 */
	private Color itemStrokeColor, itemFillColor, 
		selectedItemStrokeColor, selectedItemFillColor;
	
	/**
	 * the current cursor
	 */
	private Cursor currentCursor=null;
	
	/**
	 * whether the selection item should be always painted when an action occurs 
	 * when it is selected
	 */
	private boolean persistent=false;
	
	/**
	 * the related point, i.e. the point to which a line should be drawn
	 */
	private Point2D relatedPoint;
	
	/**
	 * the constructor of the class
	 * @param handle the svg handle the item is associated to
	 * @param elements the elements the item is associated to
	 * @param point the point at which the element should be drawn on the canvas
	 * @param type the type of the item
	 * @param style the style that should be used to draw the item
	 * @param index the index of the item, it is only useful when the 
	 * type of the item is POINT or CONTROL_POINT and is used to 
	 * get the position of the item in a path
	 * @param persistent whether the selection item should be always painted when an action occurs 
	 * when it is selected
	 * @param relatedPoint the related point, i.e. the point to which a line should be drawn
	 */
	public SelectionItem(SVGHandle handle, Set<Element> elements, Point2D point, 
			int type, int style, int index, boolean persistent, Point2D relatedPoint) {
		
		this.handle=handle;
		this.elements.addAll(elements);
		this.point=point;
		this.type=type;
		this.style=style;
		this.index=index;
		this.shape=computeShape();
		this.isMultiSelection=elements.size()>1;
		this.currentCursor=computeCursor();
		this.persistent=persistent;
		this.relatedPoint=relatedPoint;
		
		if(type!=LOCKED_POINT){
			
			itemStrokeColor=isMultiSelection?multiSelectionItemStrokeColor:singleSelectionItemStrokeColor;
			itemFillColor=isMultiSelection?multiSelectionItemFillColor:singleSelectionItemFillColor;
			selectedItemStrokeColor=
				isMultiSelection?multiSelectionSelectedItemStrokeColor:singleSelectionSelectedItemStrokeColor;
			selectedItemFillColor=
							isMultiSelection?multiSelectionSelectedItemFillColor:singleSelectionSelectedItemFillColor;
			
		}else{
			
			itemStrokeColor=lockedSelectionItemStrokeColor;
			itemFillColor=lockedSelectionItemFillColor;
			selectedItemStrokeColor=lockedSelectionSelectedItemStrokeColor;
			selectedItemFillColor=lockedSelectionSelectedItemFillColor;
		}

		currentStrokeColor=itemStrokeColor;
		currentFillColor=itemFillColor;
	}

	/**
	 * @return the element the item is associated to
	 */
	public Set<Element> getElements() {
		return elements;
	}
	
	/**
	 * @return the svg handle the item is associated to
	 */
	public SVGHandle getSVGHandle() {
		return handle;
	}

	/**
	 * @return the index of the item, it is only useful when the type of the item is POINT or CONTROL_POINT
	 * 				 and is used to get the position of the item in a path
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return the style that should be used to draw the item
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * @return the type of the item
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the point at which the item will be painted
	 */
	public Point2D getPoint() {
		return point;
	}

	/**
	 * @return whether several elements are selected at the same time
	 */
	public boolean isMultiSelection() {
		return isMultiSelection;
	}
	
	/**
	 * @return whether the selection item should be always painted when an action occurs 
	 * when it is selected
	 */
	public boolean isPersistent() {
		return persistent;
	}
	
	/**
	 * @return the type of the cursor that should be used for this item
	 */
	protected Cursor computeCursor(){
		
		Cursor cursor=null;
		int cursorType=Cursor.DEFAULT_CURSOR;
		
		switch (type){
		
			case NORTH :
				
				cursorType=(style==ARROW_STYLE)?
						Cursor.N_RESIZE_CURSOR:Cursor.HAND_CURSOR;
				break;
				
			case SOUTH :
				
				cursorType=(style==ARROW_STYLE)?
						Cursor.S_RESIZE_CURSOR:Cursor.HAND_CURSOR;
				break;
				
			case EAST :
				
				cursorType=(style==ARROW_STYLE)?
						Cursor.E_RESIZE_CURSOR:Cursor.HAND_CURSOR;
				break;
				
			case WEST :
				
				cursorType=(style==ARROW_STYLE)?
						Cursor.W_RESIZE_CURSOR:Cursor.HAND_CURSOR;
				break;
				
			case NORTH_EAST :
				
				cursorType=(style==OBLIQUE_ARROW_STYLE)?
						Cursor.NE_RESIZE_CURSOR:Cursor.HAND_CURSOR;
				break;
				
			case SOUTH_EAST :
				
				cursorType=(style==OBLIQUE_ARROW_STYLE)?
						Cursor.SE_RESIZE_CURSOR:Cursor.HAND_CURSOR;
				break;
				
			case SOUTH_WEST :
				
				cursorType=(style==OBLIQUE_ARROW_STYLE)?
						Cursor.SW_RESIZE_CURSOR:Cursor.HAND_CURSOR;
				break;
				
			case NORTH_WEST :
				
				cursorType=(style==OBLIQUE_ARROW_STYLE)?
						Cursor.NW_RESIZE_CURSOR:Cursor.HAND_CURSOR;
				break;
				
			case CENTER :
			case POINT :
			case CONTROL_POINT :
				
				cursorType=Cursor.HAND_CURSOR;
				break;
				
			default :
				
				cursorType=Cursor.DEFAULT_CURSOR;
				break;
		}

		//getting the cursor
		cursor=Cursor.getPredefinedCursor(cursorType);
		
		return cursor;	
	}

	/**
	 * @return the mouse cursor associated with this item
	 */
	public Cursor getCursor() {
		
		return currentCursor;
	}
	
	/**
	 * setting the new point for the item
	 * @param point the new point
	 */
	public void setPoint(final Point2D point) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				
				//storing the olds bounds
				Rectangle oldBounds=shapeBounds;
				
				//computing the scaled point so that it fits the current zoom
				Point2D scaledPoint=handle.getTransformsManager().
					getScaledPoint(point, false);
				
				//setting the new location and bounds
				SelectionItem.this.point=scaledPoint;
				SelectionItem.this.shape=computeShape();
				
				//repainting the item
				Rectangle repaintBounds=new Rectangle(oldBounds);
				repaintBounds.add(shapeBounds);
				handle.getScrollPane().getSVGCanvas().doRepaint(repaintBounds);
			}
		});
	}
	
	/**
	 * @return whether this item is editable
	 */
	public boolean isEditable() {
		
		return type==CENTER;
	}
	
	/**
	 * @return whether this selection item is locked
	 */
	public boolean isLocked(){
		
		return type==LOCKED_POINT;
	}
	
	/**
	 * checks whether the given point is in the area of this item, 
	 * if it is true, this item is filled with the color used to paint selected items
	 * @param mousePoint a point on the canvas
	 * @return  whether the given point is in the area of this item
	 */
	public boolean intersectsItemForMouseMove(Point mousePoint) {
		
		final boolean intersects=shapeBounds.contains(mousePoint);

		//getting the new colors corresponding to the state of the selection
		final Color newStrokeColor=intersects?selectedItemStrokeColor:itemStrokeColor;
		final Color newFillColor=intersects?selectedItemFillColor:itemFillColor;
		
		if(currentFillColor!=newFillColor) {
			
			//setting the new colors
			currentStrokeColor=newStrokeColor;
			currentFillColor=newFillColor;
			handle.getScrollPane().getSVGCanvas().doRepaint(shapeBounds);
		}

		return intersects;
	}
	
	/**
	 * checks whether the given point is in the area of this item, 
	 * @param mousePoint a point on the canvas
	 * @return  whether the given point is in the area of this item
	 */
	public boolean intersectsItem(Point2D mousePoint) {
		
		return shapeBounds.contains(mousePoint);
	}
	
	/**
	 * paints the item
	 * @param g a graphics object
	 */
	public void paint(Graphics2D g) {

		boolean paint=(g.getClipBounds()!=null && 
				g.getClipBounds().intersects(shapeBounds)) || g.getClipBounds()==null;

		if(paint){
			
			g=(Graphics2D)g.create();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			
			if(relatedPoint!=null){

				g.setColor(Color.gray);
				g.drawLine((int)point.getX(), (int)point.getY(), 
						(int)relatedPoint.getX(), (int)relatedPoint.getY());
			}
			
			g.setColor(currentFillColor);
			g.fill(shape);
			
			g.setColor(currentStrokeColor);
			g.draw(shape);
				
			g.dispose();
		}
	}
	
	/**
	 * @return the shape of the bounds
	 */
	public Rectangle getShapeBounds() {
		return shapeBounds;
	}
	
	/**
	 * @return the shape that this item will paint
	 */
	protected Shape computeShape() {
		
		//getting the shape associating with the style of the item
		Shape theShape=shapes.get(style);
		
		//getting the affine transform corresponding to the type of the item
		AffineTransform af=new AffineTransform(getTransformForShape());

		//concatenating the translate transform corresponding to the place of the 
		//item on the canvas to the founds affine transform
		af.preConcatenate(AffineTransform.getTranslateInstance(point.getX(), point.getY()));
		theShape=af.createTransformedShape(theShape);

		//computing the bounds of the shape
		shapeBounds=theShape.getBounds();
		int offset=2;
		shapeBounds.x-=offset;
		shapeBounds.y-=offset;
		shapeBounds.width+=2*offset;
		shapeBounds.height+=2*offset;
		
		return theShape;
	}
	
	/**
	 * @return the affine transform for the current shape style and types
	 */
	protected AffineTransform getTransformForShape(){
		
		AffineTransform af=null;
		
		//the size of some shapes
		int aShW=12;
		int aShH=12;
		int acShW=12;
		int acShH=12;
		int aoShW=10;
		int aoShH=10;
		
		switch(style){
		
			case ARROW_STYLE :
			case INVERSE_ARROW_STYLE :
				
				switch(type){
				
					case NORTH :
					
						af=AffineTransform.getTranslateInstance(0, -aShH/2);
						break;
						
					case SOUTH :
						
						af=AffineTransform.getTranslateInstance(0, aShH/2);
						break;
						
					case EAST :
						
						af=new AffineTransform(rotate90);
						af.preConcatenate(AffineTransform.getTranslateInstance(aShW/2, 0));
						break;
						
					case WEST :
						
						af=new AffineTransform(rotate90);
						af.preConcatenate(AffineTransform.getTranslateInstance(-aShW/2, 0));
						break;
				}
				
				break;
				
			case CURVED_ARROW_STYLE :
				
					switch(type){
							
						case NORTH_WEST :
							
							af=new AffineTransform(rotate90);
							af.preConcatenate(
									AffineTransform.getTranslateInstance(-acShW/2, -acShH/2));
							break;
							
						case NORTH_EAST :
			
							af=new AffineTransform(rotate180);
							af.preConcatenate(
									AffineTransform.getTranslateInstance(acShW/2, -acShH/2));
							break;
							
						case SOUTH_EAST :
						
							af=new AffineTransform(rotateMinus90);
							af.preConcatenate(
									AffineTransform.getTranslateInstance(acShW/2, acShH/2));
							break;
							
						case SOUTH_WEST :

							af=new AffineTransform();
							af.preConcatenate(
									AffineTransform.getTranslateInstance(-acShW/2, acShH/2));
							break;
				}		
				
				break;
				
			case OBLIQUE_ARROW_STYLE :
				
				switch(type){

					case NORTH_WEST :
						
						af=new AffineTransform(rotate90);
						af.preConcatenate(AffineTransform.getTranslateInstance(-aoShW/2, -aoShH/2));
						break;
						
					case NORTH_EAST :
	
						af=new AffineTransform();
						af.preConcatenate(AffineTransform.getTranslateInstance(aoShW/2, -aoShH/2));
						break;
						
					case SOUTH_EAST :
					
						af=new AffineTransform(rotate90);
						af.preConcatenate(AffineTransform.getTranslateInstance(aoShW/2, aoShH/2));
						break;
						
					case SOUTH_WEST :
						
						af=new AffineTransform();
						af.preConcatenate(AffineTransform.getTranslateInstance(-aoShW/2, aoShH/2));
						break;
				}
				break;
				
			case POINT_STYLE :	
			case CENTER_POINT_STYLE :
			case LOSANGE_STYLE :
				
				af=new AffineTransform();
				break;
		}
		
		return af;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SelectionItem sel) {
		
		return getIndex()-sel.getIndex();
	}
}

