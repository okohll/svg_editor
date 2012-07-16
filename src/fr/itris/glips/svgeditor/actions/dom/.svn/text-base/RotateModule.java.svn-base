package fr.itris.glips.svgeditor.actions.dom;

import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to execute rotate actions on svg elements
 * @author Jordi SUC
 */
public class RotateModule extends DomActionsModule {
	
	/**
	 * the constants
	 */
	protected static final int ROTATE_90=0, ROTATE_M_90=1, ROTATE_180=2;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public RotateModule(Editor editor) {
		
		//setting the id
		moduleId="Rotate";

		//filling the arrays of the types
		int[] types={ROTATE_90, ROTATE_M_90, ROTATE_180};
		actionsTypes=types;
		
		//filling the arrays of the ids
		String[] ids={"Rotate90", "RotateM90", "Rotate180"};
		actionsIds=ids;
		
		createItems();
	}
	
	@Override
	protected void doAction(
		SVGHandle handle, Set<Element> elements, 
			int index, ActionEvent evt) {
		
 		//getting the type of the action
		int type=actionsTypes[index];
		
		//getting the rotation parameters for each element//
		Map<Element, Point2D> centerPointsMap=
			new HashMap<Element, Point2D>();
		Map<Element, Double> anglesMap=
			new HashMap<Element, Double>();
		
		//getting the map of the bounds of the elements
		Map<Element, Rectangle2D> boundsMap=getBounds(handle, elements);
		Rectangle2D bounds=null;
		Point2D centerPoint=null;
		double angle=0;
		
		for(Element element : elements){
			
			//getting the bounds of the element
			bounds=boundsMap.get(element);
			
			//getting the center point of the element
			centerPoint=new Point2D.Double(
					bounds.getCenterX(), bounds.getCenterY());
			
			//putting the center point into the center points map
			centerPointsMap.put(element, centerPoint);

			//getting the angle
			switch (type){
			
				case ROTATE_90 :
					
					angle=-Math.PI/2;
					break;
					
				case ROTATE_M_90 :
					
					angle=Math.PI/2;
					break;
					
				case ROTATE_180 :
					
					angle=Math.PI;
					break;
			}
			
			anglesMap.put(element, angle);
		}

		applyRotateTransform(handle, index, centerPointsMap, anglesMap);
	}

	@Override
	protected boolean selectionCorrect(int index, Set<Element> elements) {

		return selectionCorrectFirstType(elements);
	}
}

