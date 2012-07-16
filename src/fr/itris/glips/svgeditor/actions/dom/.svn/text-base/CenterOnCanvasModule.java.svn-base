package fr.itris.glips.svgeditor.actions.dom;

import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to execute center on canvas actions on svg elements
 * @author Jordi SUC
 */
public class CenterOnCanvasModule extends DomActionsModule {
	
	/**
	 * the constants
	 */
	protected static final int H_CENTER_ON_CANVAS=0, 
		V_CENTER_ON_CANVAS=1, BOTH_CENTER_ON_CANVAS=2;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public CenterOnCanvasModule(Editor editor) {
		
		//setting the id
		moduleId="CenterOnCanvas";

		//filling the arrays of the types
		int[] types={H_CENTER_ON_CANVAS, V_CENTER_ON_CANVAS, 
				BOTH_CENTER_ON_CANVAS};
		actionsTypes=types;
		
		//filling the arrays of the ids
		String[] ids={"HCenterOnCanvas", "VCenterOnCanvas", 
			"BothCenterOnCanvas"};
		actionsIds=ids;
		
		createItems();
	}
	
	@Override
	protected void doAction(
			SVGHandle handle, Set<Element> elements, 
				int index, ActionEvent evt) {
		
		//getting the type of the action
		int type=actionsTypes[index];
		
		//getting the bounds of each element
		Map<Element, Rectangle2D> elementsToBounds=
			getBounds(handle, elements);
		
		//getting the canvas size
		Point2D canvasSize=handle.getCanvas().getGeometryCanvasSize();
		
		//getting the translation factors for each element//
		Map<Element, Point2D> translationFactors=
			new HashMap<Element, Point2D>();
		
		//getting the union of all the bounds
		Rectangle2D unionRect=union(
				new HashSet<Rectangle2D>(elementsToBounds.values()));
		Point2D factors=null;
		
		switch(type){
			
			case H_CENTER_ON_CANVAS : 

				for(Element element : elementsToBounds.keySet()){
					
					factors=new Point2D.Double(
						-unionRect.getX()+canvasSize.getX()/2-unionRect.getWidth()/2, 0);
					
					translationFactors.put(element, factors);
				}
				
				break;
				
			case V_CENTER_ON_CANVAS : 
				
				for(Element element : elementsToBounds.keySet()){
					
					factors=new Point2D.Double(0,
						-unionRect.getY()+canvasSize.getY()/2-unionRect.getHeight()/2);
					
					translationFactors.put(element, factors);
				}
				
				break;
				
			case BOTH_CENTER_ON_CANVAS :
		
				for(Element element : elementsToBounds.keySet()){
					
					factors=new Point2D.Double(
						-unionRect.getX()+canvasSize.getX()/2-unionRect.getWidth()/2,
							-unionRect.getY()+canvasSize.getY()/2-unionRect.getHeight()/2);
					
					translationFactors.put(element, factors);
				}
				
				break;
		}
		
		applyTranslateTransform(handle, index, translationFactors);
	}
	
	@Override
	protected boolean selectionCorrect(int index, Set<Element> elements) {

		return selectionCorrectFirstType(elements);
	}
}
