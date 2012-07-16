package fr.itris.glips.svgeditor.display.canvas;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 * The interface of the class used to paint on the canvas
 * @author ITRIS, Jordi SUC
 */
public abstract class CanvasPainter {

	/**
	 * the action that has to be done
	 * @param g the graphics object
	 */
	public abstract void paintToBeDone(Graphics2D g);
	
	/**
	 * @return the set of the clip zones that should be painted
	 */
	public Set<Rectangle2D> getClip(){
		
		return null;
	}
}
