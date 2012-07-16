package fr.itris.glips.extension.jwidget.trends.runtime.view.component.painters;

import java.awt.*;
import java.awt.geom.*;

/**
 * a class used to paint items on the curves component
 * @author ITRIS, Jordi SUC
 */
public class AnyPainter {
	
	/**
	 * the shapes to paint
	 */
	protected GeneralPath path=new GeneralPath();
	
	/**
	 * the bounds of the path
	 */
	protected Rectangle bounds=new Rectangle();
	
	/**
	 * the stroke used to paint
	 */
	protected Stroke stroke;
	
	/**
	 * the color used to paint the path
	 */
	private Color mainColor;
	
	/**
	 * whether to use a stroke to draw the path
	 */
	private boolean useStroke;
	
	/**
	 * the constructor of the class
	 * @param mainColor the color used to paint the path
	 * @param useStroke whether to use a stroke to draw the path
	 */
	public AnyPainter(Color mainColor, boolean useStroke){
		
		this.mainColor=mainColor;
		this.useStroke=useStroke;
		stroke=new BasicStroke(
				1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, new float[]{2, 2}, 0f);
	}

	/**
	 * @return the path that will be drawn
	 */
	public GeneralPath getPath() {
		return path;
	}
	
	/**
	 * validates the changes of the path
	 */
	public void validatePath(){
		
		Rectangle pathBounds=path.getBounds();
		
		bounds.x=pathBounds.x-1;
		bounds.y=pathBounds.y-1;
		bounds.width=pathBounds.width+2;
		bounds.height=pathBounds.height+2;
	}

	/**
	 * paints the shape
	 * @param g a graphics object
	 */
	public void paint(Graphics2D g){
		
		if(path!=null){
			
			g=(Graphics2D)g.create();
			
			if(useStroke){
				
				g.setColor(Color.white);
				g.draw(path);
				g.setStroke(stroke);
			}
			
			g.setColor(mainColor);
			g.draw(path);
			
			g.dispose();
		}
	}
	
	/**
	 * @return the bounds to paint
	 */
	public Rectangle getBounds(){
		
		return bounds;
	}
}
