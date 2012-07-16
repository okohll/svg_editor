package fr.itris.glips.svgeditor.display.canvas.rulers;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.display.canvas.*;
import java.util.*;

/**
 * the class of the rulers for a canvas
 * @author ITRIS, Jordi SUC
 */
public class Ruler extends JPanel{

	/**
	 * the font
	 */
	private final static Font font=new Font("theFont", Font.ROMAN_BASELINE, 9);
	
	/**
	 * the scrollpane
	 */
	private SVGScrollPane scrollpane;
	
	/**
	 * the listener to the actions on the canvas
	 */
	private RulerCanvasListener canvasListener=new RulerCanvasListener();
	
	/**
	 * whether the ruler is horizontal or vertical
	 */
	private boolean isHorizontal=true;
	
	/**
	 * the range object
	 */
	private Range range=null;
	
	/**
	 * the mouse position
	 */
	private Point mousePoint=null;
	
	/**
	 * the current position
	 */
	private int currentPosition=0;
	
	/**
	 * the shape of the cursor 
	 */
	private GeneralPath cursorShape=new GeneralPath();
	
	/**
	 * the colors for drawing the cursors
	 */
	private static final Color fillColorCursor=new Color(255, 0, 0);
	
	/**
	 * the length of the cursor
	 */
	private int cursorLength=12;
	
	/**
	 * the constructor of the class
	 * @param scrollpane a scroll pane
	 * @param isHorizontal whether the ruler is horizontal or vertical
	 */
	public Ruler(SVGScrollPane scrollpane, boolean isHorizontal){
		
		this.scrollpane=scrollpane;
		this.isHorizontal=isHorizontal;
		setDoubleBuffered(false);
		setOpaque(false);
		
		//adding the listener to the canvas actions
		scrollpane.getSVGCanvas().addMouseMotionListener(canvasListener);
		
		//creating the shape of the cursor
		if(isHorizontal){
			
			cursorShape.moveTo(0, 0);
			cursorShape.lineTo(cursorLength/2, 0);
			cursorShape.lineTo(0, cursorLength/2);
			cursorShape.lineTo(-cursorLength/2, 0);
			cursorShape.closePath();
			
		}else{
			
			cursorShape.moveTo(0, 0);
			cursorShape.lineTo(0, -cursorLength/2);
			cursorShape.lineTo(cursorLength/2, 0);
			cursorShape.lineTo(0, cursorLength/2);
			cursorShape.closePath();
		}
	}
	
	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
		
		if(enable){
			
			//adding the listener to the canvas actions
			scrollpane.getSVGCanvas().removeMouseMotionListener(canvasListener);
			scrollpane.getSVGCanvas().addMouseMotionListener(canvasListener);
			
		}else{
			
			//removing the listener to the canvas actions
			scrollpane.getSVGCanvas().removeMouseMotionListener(canvasListener);
		}
	}
	
	@Override
	public void paint(Graphics g) {

		drawRuler((Graphics2D)g);
		drawCursor(g);
	}
	
	/**
	 * @return the number for aligning
	 */
	public double getRangeForAlignment(){
		
		double number=5;
		
		if(range!=null){
			
			try{
				number=range.getRanges().getLast();
			}catch (Exception ex){ex.printStackTrace();}
		}
		
		return number;
	}
	
	/**
	 * refreshes the range of this ruler
	 */
	public void refreshRange(){
		
		double viewPortLength=0;
		double canvasLength=0;
		double scale=scrollpane.getSVGCanvas().
			getZoomManager().getCurrentScale();

		if(isHorizontal){
			
			viewPortLength=scrollpane.getViewPortBounds().width;
			canvasLength=scrollpane.getSVGCanvas().getScaledCanvasSize().width;
			
		}else{
			
			viewPortLength=scrollpane.getViewPortBounds().height;
			canvasLength=scrollpane.getSVGCanvas().getScaledCanvasSize().height;
		}
		
		if(canvasLength>viewPortLength){
			
			range=new Range(viewPortLength, scale);
			
		}else{
			
			range=new Range(canvasLength, scale);
		}
		
		//reinitializing data
		currentPosition=0;
	}
	
	protected int computeCurrentPosition(){
		
		int pos=0;
		
		//computing the position of the cursor
		if(mousePoint!=null){
			
			Rectangle canvasBounds=Ruler.this.scrollpane.getCanvasBounds();
			
			if(Ruler.this.isHorizontal){

				if(canvasBounds.x>0){
					
					pos=mousePoint.x+canvasBounds.x;
					
					if(pos<canvasBounds.x){
						
						pos=canvasBounds.x;
						
					}else if(pos>canvasBounds.x+canvasBounds.width){
						
						pos=canvasBounds.x+canvasBounds.width;
					}
					
				}else{
					
					pos=mousePoint.x+canvasBounds.x;
				}
				
			}else{
				
				if(canvasBounds.y>0){
					
					pos=mousePoint.y+canvasBounds.y;
					
					if(pos<canvasBounds.y){
						
						pos=canvasBounds.y;
						
					}else if(pos>canvasBounds.y+canvasBounds.height){
						
						pos=canvasBounds.y+canvasBounds.height;
					}
					
				}else{
					
					pos=mousePoint.y+canvasBounds.y;
				}
			}
			
			if(pos<0){
				
				pos=0;
			}
		}
		
		return pos;
	}
	
	/**
	 * draws the cursor
	 * @param g a graphics object
	 */
	protected void drawCursor(Graphics g){

		currentPosition=computeCurrentPosition();

		//drawing the cursor
		Shape cursor=null;

		if(isHorizontal){
			
			cursor=cursorShape.createTransformedShape(
				AffineTransform.getTranslateInstance(
					currentPosition, getHeight()-cursorShape.getBounds().height));
			
		}else{
			
			cursor=cursorShape.createTransformedShape(
				AffineTransform.getTranslateInstance(
					getWidth()-cursorShape.getBounds().width, currentPosition));
		}
		
		//paint the cursor
		Graphics2D g2=(Graphics2D)g.create();
		g2.setColor(fillColorCursor);
		g2.fill(cursor);
		
		g.dispose();
	}
	
	/**
	 * draws a ruler
	 * @param g a graphics object
	 */
	protected void drawRuler(Graphics2D g) {
		
		//getting the clip rectangle that should be painted
		Rectangle clip=g.getClip().getBounds();
		
		if(range==null){
			
			refreshRange();
		}

		g.setColor(Color.black);
		
		if(range.getRanges().size()>0){
			
			double smallestRange=range.getRanges().getLast();
			double scale=scrollpane.getSVGCanvas().getZoomManager().getCurrentScale();
			double viewPortOrigin=0, viewPortLength=0, canvasLength=0;
			Rectangle canvasBounds=scrollpane.getCanvasBounds();

			//getting values used to draw the ruler
			double startPoint=0, startRangePoint=0, endPoint=0;
			
			if(canvasLength>viewPortLength){
				
				if(isHorizontal){
					
					viewPortOrigin=-canvasBounds.x;
					viewPortLength=scrollpane.getViewPortBounds().width;
					canvasLength=canvasBounds.width;
					
				}else{
					
					viewPortOrigin=-canvasBounds.y;
					viewPortLength=scrollpane.getViewPortBounds().height;
					canvasLength=canvasBounds.height;
				}

				//the scrolling is enabled
				startPoint=viewPortOrigin/scale;
				endPoint=(viewPortOrigin+viewPortLength)/scale;
				startRangePoint=Math.floor(startPoint/smallestRange)*smallestRange;
				
				//painting the items
				double cur=0;
				double scrollPanePosition=0;
				
				if(isHorizontal){
					
					while(cur<=endPoint){
						
						scrollPanePosition=(startRangePoint+cur)*scale-viewPortOrigin;
						
						//checking if the item that will be painted belongs to the clip rectangle
						if(scrollPanePosition>=clip.x && scrollPanePosition<=(clip.x+clip.width)){
							
							paintItem(g, cur, scrollPanePosition);

						}else if(scrollPanePosition>(clip.x+clip.width)){
							
							break;
						}
						
						cur+=smallestRange;
					}
					
				}else{
					
					while(cur<=endPoint){
						
						scrollPanePosition=(startRangePoint+cur)*scale-viewPortOrigin;
						
						//checking if the item that will be painted belongs to the clip rectangle
						if(scrollPanePosition>=clip.y && scrollPanePosition<=(clip.y+clip.height)){
							
							paintItem(g, cur, scrollPanePosition);//TODO

						}else if(scrollPanePosition>(clip.y+clip.height)){
							
							break;
						}
						
						cur+=smallestRange;
					}
				}

			}else{
				
				if(isHorizontal){
					
					viewPortOrigin=canvasBounds.x;
					viewPortLength=canvasBounds.width;
					canvasLength=canvasBounds.width;
					
				}else{
					
					viewPortOrigin=canvasBounds.y;
					viewPortLength=canvasBounds.height;
					canvasLength=canvasBounds.height;
				}
				
				//the scrolling is disabled
				startPoint=0;
				endPoint=canvasLength/scale;
				startRangePoint=0;

				//painting the items
				double cur=0;
				double scrollPanePosition=0;
				
				if(isHorizontal){
					
					while(cur<=endPoint){
						
						scrollPanePosition=(startRangePoint+cur)*scale+viewPortOrigin;
						
						//checking if the item that will be painted belongs to the clip rectangle
						if(scrollPanePosition>=clip.x && scrollPanePosition<=(clip.x+clip.width)){
							
							paintItem(g, cur, scrollPanePosition);//TODO

						}else if(scrollPanePosition>(clip.x+clip.width)){
							
							break;
						}
						
						cur+=smallestRange;
					}
					
				}else{
					
					while(cur<=endPoint){
						
						scrollPanePosition=(startRangePoint+cur)*scale+viewPortOrigin;
						
						//checking if the item that will be painted belongs to the clip rectangle
						if(scrollPanePosition>=clip.y && scrollPanePosition<=(clip.y+clip.height)){
							
							paintItem(g, cur, scrollPanePosition);//TODO

						}else if(scrollPanePosition>(clip.y+clip.height)){
							
							break;
						}
						
						cur+=smallestRange;
					}
				}
			}
		}
	}
	
	/**
	 * draws a ruler item
	 * @param g the graphics
	 * @param canvasPos the position of the item in the unscaled canvas coordinates
	 * @param scrollpanePos the position of the item in the scrollpane coordinates
	 */
	protected void paintItem(Graphics2D g, double canvasPos, double scrollpanePos){
		
		double scale=scrollpane.getSVGCanvas().
			getZoomManager().getCurrentScale();
		
		//determining which range should be used for this item and then paints the item
		if(range.getRanges().size()==3){

			if(canvasPos%range.getRanges().getFirst()==0){
				
				paintRawItem(g, canvasPos, scrollpanePos, 0, 
						range.getRanges().getFirst()*scale);
				
			}else if(canvasPos%range.getRanges().get(1)==0){
				
				paintRawItem(g, canvasPos, scrollpanePos, 1, 
						range.getRanges().get(1)*scale);
				
			}else{
				
				paintRawItem(g, canvasPos, scrollpanePos, 2, 
						range.getRanges().getLast()*scale);
			}

		}else if(range.getRanges().size()==2){
			
			if(canvasPos%range.getRanges().getFirst()==0){
				
				paintRawItem(g, canvasPos, scrollpanePos, 0,
						range.getRanges().getFirst()*scale);
				
			}else{
				
				paintRawItem(g, canvasPos, scrollpanePos, 1, 
						range.getRanges().get(1)*scale);
			}

		}else if(range.getRanges().size()==1){
			
			paintRawItem(g, canvasPos, scrollpanePos, 0, 
					range.getRanges().getFirst()*scale);
		}
	}
	
	/**
	 * paints an item
	 * @param g the graphics
	 * @param canvasPos the position of the item in the canvas
	 * @param pos the position of the item
	 * @param index the kind of representation for this item
	 * @param availableSize the available size until the next item of the same index
	 */
	protected void paintRawItem(
		Graphics2D g, double canvasPos, double pos, int index, double availableSize){
		
		switch (index){
		
			case 0 :
				
				if(isHorizontal){

					//drawing the string
					String str=(int)(canvasPos)+"";
					Rectangle2D rect=font.getStringBounds(str, 
							new FontRenderContext(new AffineTransform(), true, false));
					
					if(rect.getWidth()<availableSize){
						
						Graphics2D g2=(Graphics2D)g.create();
						g2.setFont(font);
						g2.drawString(str, (int)pos+2, font.getSize()-2);
						g2.dispose();
					}
					
					g.draw(new Line2D.Double(pos, getHeight()-8, pos, getHeight()));
					
				}else{
					
					Graphics2D g2=(Graphics2D)g.create();

					//drawing the strings
					char[] tab=new String((int)(canvasPos)+"").toCharArray();
					
					if(tab.length*font.getSize()<=availableSize){
						
						int vpos=font.getSize()-1;
						
						for(char c : tab){
							
							g2.setFont(font);
							g2.drawString(c+"", 0, (int)pos+vpos);
							vpos+=font.getSize()-1;
						}

						g2.dispose();
					}
					
					g.draw(new Line2D.Double(getWidth()-8, pos, getWidth(), pos));
				}
				
				break;
				
			case 1 :
				
				if(isHorizontal){
					
					g.draw(new Line2D.Double(pos, getHeight()-6, pos, getHeight()));
					
				}else{
					
					g.draw(new Line2D.Double(getWidth()-6, pos, getWidth(), pos));
				}
				
				break;
				
			case 2 :
				
				if(isHorizontal){
					
					g.draw(new Line2D.Double(pos, getHeight()-4, pos, getHeight()));
					
				}else{
					
					g.draw(new Line2D.Double(getWidth()-4, pos, getWidth(), pos));
				}
				
				break;
		}
	}
	
	/**
	 * disposes this ruler
	 */
	public void dispose() {
		
		//removing the listener to the canvas actions
		scrollpane.getSVGCanvas().removeMouseMotionListener(canvasListener);
		
		scrollpane=null;
		canvasListener=null;
		range=null;
		mousePoint=null;
		cursorShape=null;
	}
	
	/**
	 * the class of the range of the ruler
	 * @author ITRIS, Jordi SUC
	 */
	protected class Range{
		
		/**
		 * the constant for large 
		 */
		public static final int LARGE=0;
		
		/**
		 * the constant for medium 
		 */
		public static final int MEDIUM=1;
		
		/**
		 * the constant for small 
		 */
		public static final int SMALL=2;
		
		/**
		 * the list of the computed ranges
		 */
		private LinkedList<Double> ranges=new LinkedList<Double>();
		
		/**
		 * the constructor of the class
		 * @param distance the available scaled distance
		 * @param scale the scale 
		 */
		protected Range(double distance, double scale){
			
			//the geometry distance
			double geometryDistance=distance/scale;
			
			//the maximal range
			double maxRange=Math.floor(Math.log10(geometryDistance));
			
			//computing the list of all the ranges 
			double crange=maxRange, availableSpace=0, scaledSpace=0;

			while(true){
				
				//checking if the available space can be a part of the ranges
				availableSpace=Math.pow(10, crange);
				scaledSpace=availableSpace*scale;
				
				if(scaledSpace<=3){

					break;
				}
				
				ranges.add(availableSpace);
				
				//checking if the middle of the available space can be a part of the ranges
				availableSpace=availableSpace/2;
				scaledSpace=availableSpace*scale;
				
				if(scaledSpace<=3){
					
					break;
				}
				
				ranges.add(availableSpace);
				
				crange--;
			}

			//handling the list of the ranges so that only the last three ranges remain in it
			if(ranges.size()>3){
				
				ranges=new LinkedList<Double>(ranges.subList(ranges.size()-3, ranges.size()));
			}
		}
		
		/**
		 * @return the list of the ranges
		 */
		protected LinkedList<Double> getRanges(){
			
			return ranges;
		}
		
		/**
		 * computes and returns the closest number from the given number that belongs to a range 
		 * @param pos a number
		 * @return the closest number from the given number that belongs to a range 
		 */
		protected double getRound(double pos){
			
			double res=0;
			
			//getting the smallest integer range
			double smallestRange=ranges.getLast();
			double pos1=Math.floor(pos/smallestRange)*smallestRange;
			double pos2=(Math.floor(pos/smallestRange)+1)*smallestRange;

			if(Math.abs(pos1-pos)<Math.abs(pos2-pos)){
				
				res=pos1;
				
			}else{
				
				res=pos2;
			}
			
			return res;
		}
	}
	
	/**
	 * the class of the listener to the mouse move and drag actions on the svg canvas
	 * @author ITRIS, Jordi SUC
	 */
	protected class RulerCanvasListener extends MouseMotionAdapter{
		
		@Override
		public void mouseDragged(MouseEvent evt) {

			handleEvent(evt.getPoint());
		}
		
		@Override
		public void mouseMoved(MouseEvent evt) {

			handleEvent(evt.getPoint());
		}
		
		/**
		 * handles the event
		 * @param point the mouse point for the event
		 */
		protected void handleEvent(Point point) {
			
			//storing the new point
			mousePoint=point;
			
			//getting the old position of the cursor
			int oldPosition=currentPosition;
			
			//computing the new position
			int newPosition=computeCurrentPosition();
			
			//computing the new area to repaint
			Rectangle rect=null;
			
			if(isHorizontal){

				//computing the new bounds of the cursor
				rect=new Rectangle(newPosition-cursorLength/2, 
						getHeight()-cursorLength/2, cursorLength, cursorLength/2);
				
				//computing the old bounds of the cursor and merging them to the previously computed ones
				rect.add(new Rectangle(oldPosition-cursorLength/2, 
						getHeight()-cursorLength/2, cursorLength, cursorLength/2));

			}else{
				
				//computing the new bounds of the cursor
				rect=new Rectangle(getWidth()-cursorLength/2, 
						newPosition-cursorLength/2, cursorLength/2, cursorLength);
				
				//computing the old bounds of the cursor and merging them to the previously computed ones
				rect.add(new Rectangle(getWidth()-cursorLength/2, 
						oldPosition-cursorLength/2, cursorLength/2, cursorLength));
			}

			//repainting the ruler
			repaint(rect);
		}
	}
}
