package fr.itris.glips.svgeditor.actions.dom;

import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to execute align actions on svg elements
 * @author Jordi SUC
 */
public class AlignModule extends DomActionsModule {
	
	/**
	 * the constants
	 */
	protected static final int ALIGN_LEFT=0, ALIGN_RIGHT=1, 
		ALIGN_TOP=2, ALIGN_BOTTOM=3, ALIGN_CENTER=4, 
			ALIGN_H_CENTER=5, ALIGN_V_CENTER=6;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public AlignModule(Editor editor) {
		
		//setting the id
		moduleId="Align";

		//filling the arrays of the types
		int[] types={ALIGN_LEFT, ALIGN_RIGHT, ALIGN_TOP, 
				ALIGN_BOTTOM, ALIGN_CENTER, ALIGN_H_CENTER, 
				ALIGN_V_CENTER};
		actionsTypes=types;
		
		//filling the arrays of the ids
		String[] ids={"AlignLeft", "AlignRight", "AlignTop", "AlignBottom", 
				"AlignCenter", "AlignHCenter", "AlignVCenter"};
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
		
		//getting the translation factors for each element//
		Map<Element, Point2D> translationFactors=
			new HashMap<Element, Point2D>();
		
		//getting the union of all the bounds
		Rectangle2D unionRect=union(
				new HashSet<Rectangle2D>(elementsToBounds.values()));

		switch(type){
			
			case ALIGN_LEFT : 

				Rectangle2D bounds=null;
				Point2D factors=null;
				
				for(Element element : elementsToBounds.keySet()){
					
					bounds=elementsToBounds.get(element);
					factors=new Point2D.Double(unionRect.getX()-bounds.getX(), 0);
					
					translationFactors.put(element, factors);
				}
				
				break;
				
			case ALIGN_RIGHT : 
				
				for(Element element : elementsToBounds.keySet()){
					
					bounds=elementsToBounds.get(element);
					factors=new Point2D.Double(
						(unionRect.getX()+unionRect.getWidth())-
							(bounds.getX()+bounds.getWidth()), 0);
					
					translationFactors.put(element, factors);
				}
				
				break;
				
			case ALIGN_TOP :
		
				for(Element element : elementsToBounds.keySet()){
					
					bounds=elementsToBounds.get(element);
					factors=new Point2D.Double(0, unionRect.getY()-bounds.getY());
					
					translationFactors.put(element, factors);
				}
				
				break;
				
			case ALIGN_BOTTOM : 
			
				for(Element element : elementsToBounds.keySet()){
					
					bounds=elementsToBounds.get(element);
					factors=new Point2D.Double(0, 
						(unionRect.getY()+unionRect.getHeight())-
							(bounds.getY()+bounds.getHeight()));
					
					translationFactors.put(element, factors);
				}
				
				break;
				
			case ALIGN_CENTER : 
				
				for(Element element : elementsToBounds.keySet()){
					
					bounds=elementsToBounds.get(element);
					factors=new Point2D.Double(
						unionRect.getCenterX()-bounds.getCenterX(), 
							unionRect.getCenterY()-bounds.getCenterY());
					
					translationFactors.put(element, factors);
				}
				
				break;
				
			case ALIGN_H_CENTER :
				
				for(Element element : elementsToBounds.keySet()){
					
					bounds=elementsToBounds.get(element);
					factors=new Point2D.Double(0, 
						unionRect.getCenterY()-bounds.getCenterY());
					
					translationFactors.put(element, factors);
				}
				
				break;
				
			case ALIGN_V_CENTER :
			
				for(Element element : elementsToBounds.keySet()){
					
					bounds=elementsToBounds.get(element);
					factors=new Point2D.Double(
							unionRect.getCenterX()-bounds.getCenterX(), 0);
					
					translationFactors.put(element, factors);
				}
				
				break;
		}
		
		applyTranslateTransform(handle, index, translationFactors);
	}
	
	@Override
	protected boolean selectionCorrect(int index, Set<Element> elements) {

		return selectionCorrectSecondType(elements);
	}
}
