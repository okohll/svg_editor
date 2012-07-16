package fr.itris.glips.svgeditor.actions.dom;

import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to execute flip actions on svg elements
 * @author Jordi SUC
 */
public class FlipModule extends DomActionsModule {
	
	/**
	 * the constants
	 */
	protected static final int H_FLIP=0, V_FLIP=1;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public FlipModule(Editor editor) {
		
		//setting the id
		moduleId="Flip";

		//filling the arrays of the types
		int[] types={H_FLIP, V_FLIP};
		actionsTypes=types;
		
		//filling the arrays of the ids
		String[] ids={"HFlip", "VFlip"};
		actionsIds=ids;
		
		createItems();
	}
	
	@Override
	protected void doAction(
		SVGHandle handle, Set<Element> elements, 
			int index, ActionEvent evt) {
		
		//getting the type of the action
		int type=actionsTypes[index];
		
		//getting the resize transform for each element//
		Map<Element, AffineTransform> transformsMap=
			new HashMap<Element, AffineTransform>();
		AffineTransform transform=null;
		
		//getting the map of the bounds of the elements
		Map<Element, Rectangle2D> boundsMap=getBounds(handle, elements);
		Rectangle2D bounds=null;
		
		for(Element element : elements){
			
			//getting the bounds of the element
			bounds=boundsMap.get(element);
			
			transform=AffineTransform.getTranslateInstance(
					-bounds.getCenterX(), -bounds.getCenterY());
			
			//computing the resize transform
			switch(type){

				case H_FLIP :

					transform.preConcatenate(AffineTransform.getScaleInstance(-1, 1));
					break;
					
				case V_FLIP :
			
					transform.preConcatenate(AffineTransform.getScaleInstance(1, -1));
					break;
			}
			
			transform.preConcatenate(AffineTransform.getTranslateInstance(
					bounds.getCenterX(), bounds.getCenterY()));
			transformsMap.put(element, transform);
		}

		applyResizeTransform(handle, index, transformsMap);
	}

	@Override
	protected boolean selectionCorrect(int index, Set<Element> elements) {

		return selectionCorrectFirstType(elements);
	}
}

