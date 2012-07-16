package fr.itris.glips.svgeditor.actions.dom;

import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to execute distribute actions on svg elements
 * @author Jordi SUC
 */
public class DistributeModule extends DomActionsModule {
	
	/**
	 * the constants
	 */
	protected static final int H_DISTRIBUTE=0, V_DISTRIBUTE=1;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public DistributeModule(Editor editor) {
		
		//setting the id
		moduleId="Distribute";

		//filling the arrays of the types
		int[] types={H_DISTRIBUTE, V_DISTRIBUTE};
		actionsTypes=types;
		
		//filling the arrays of the ids
		String[] ids={"HDistribute", "VDistribute"};
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
		
		//computing the list of the elements sorted according to their bounds
		LinkedList<Element> sortedElements=new LinkedList<Element>();
		sortedElements.addAll(elementsToBounds.keySet());
		
		//creating the comparator
		ElementComparator comparator=
			new ElementComparator(elementsToBounds, index==0);
		
		//sorting the list
		Collections.sort(sortedElements, comparator);

		switch(type){
				
			case H_DISTRIBUTE :

				//computing the left space between the rectangles
				double leftSpace=
					getLeftSpace(elementsToBounds.values(), unionRect, true);
				
				//dividing the left space between all the elements
				leftSpace=leftSpace/(elements.size()-1);
				
				if(leftSpace>0){
					
					int i=0;
					
					//the offset from the top of the union area
					int currentOffset=0;
					Rectangle2D bounds;

					for(Element element : sortedElements){

						if(i!=sortedElements.size()-1){
							
							//getting the bounds for the element
							bounds=elementsToBounds.get(element);
							
							if(i!=0){

								//computing the translation factors
								translationFactors.put(element, 
									new Point2D.Double(
											currentOffset-(bounds.getX()-unionRect.getX()), 0));
							}
							
							currentOffset+=bounds.getWidth()+leftSpace;
						}
						
						i++;
					}
				}

				break;
				
			case V_DISTRIBUTE :
		
				//computing the left space between the rectangles
				leftSpace=
					getLeftSpace(elementsToBounds.values(), unionRect, false);

				if(leftSpace>0){
					
					//dividing the left space between all the elements
					leftSpace=leftSpace/(elements.size()-1);
					int i=0;
					int currentOffset=0;
					Rectangle2D bounds=null;

					for(Element element : sortedElements){

						if(i!=sortedElements.size()-1){
							
							//getting the bounds for the element
							bounds=elementsToBounds.get(element);
							
							if(i!=0){

								//computing the translation factors
								translationFactors.put(element, 
									new Point2D.Double(0,
											currentOffset-(bounds.getY()-unionRect.getY())));
							}
							
							currentOffset+=bounds.getHeight()+leftSpace;
						}
						
						i++;
					}
				}
				
				break;
		}
		
		applyTranslateTransform(handle, index, translationFactors);
	}

	@Override
	protected boolean selectionCorrect(int index, Set<Element> elements) {

		return selectionCorrectThirdType(elements);
	}
	
	/**
	 * returns the space that is unused between the elements 
	 * denoted by the provided bounds
	 * @param elementsBounds the collection of the bounds of elements
	 * @param unionRect the union of all the provided bounds
	 * @param isHorizontal whether the computed left space 
	 * should be the one along the horizontal axis
	 * @return the space that is unused between the elements 
	 * denoted by the provided bounds
	 */
	protected double getLeftSpace(Collection<Rectangle2D> elementsBounds, 
			Rectangle2D unionRect, boolean isHorizontal){
		
		double leftSpace=0;
		
		//creating the set of the union of the rectangles that intersect one another
		Set<Rectangle2D> zones=new HashSet<Rectangle2D>();
		Set<Rectangle2D> startSet=new HashSet<Rectangle2D>(elementsBounds);
		Set<Rectangle2D> modifiedSet;
		Rectangle2D rect=null;
		
		start :
		
		for(Rectangle2D bounds : startSet){

			modifiedSet=new HashSet<Rectangle2D>();
			
			for(Rectangle2D theBounds : startSet){
				
				//if a rectangle intersects the current one
				if(bounds!=theBounds && intersects(bounds, theBounds, isHorizontal)){
					
					//creating the union bounds of the two rectangles
					rect=new Rectangle2D.Double(bounds.getX(), bounds.getY(), 
							bounds.getWidth(), bounds.getHeight());
					rect.add(theBounds);
					
					//filling the set of the new rectangles
					modifiedSet.addAll(startSet);
					modifiedSet.remove(bounds);
					modifiedSet.remove(theBounds);
					modifiedSet.removeAll(zones);
					modifiedSet.add(rect);
					startSet=modifiedSet;

					break start;
				}
			}
			
			//as no other rectangle intersects the current one, it is stored
			zones.add(bounds);
		}
		
		if(zones.size()>1){
			
			if(isHorizontal){
				
				//getting the total width of the computed zone bounds
				double width=0;
				
				for(Rectangle2D theRect : zones){
					
					width+=theRect.getWidth();
				}
				
				leftSpace=unionRect.getWidth()-width;
				
			}else{
				
				//getting the total height of the computed zone bounds
				double height=0;
				
				for(Rectangle2D theRect : zones){
					
					height+=theRect.getHeight();
				}
				
				leftSpace=unionRect.getHeight()-height;
			}
		}
		
		return leftSpace;
	}
	
	/**
	 * whether the two bounds intersect horizontally or vertically
	 * @param rect1 a rectangle
	 * @param rect2 a rectangle
	 * @param isHorizontal whether the intersection should 
	 * be computed horizontally or vertically
	 * @return whether the two bounds intersect horizontally or vertically
	 */
	protected boolean intersects(
			Rectangle2D rect1, Rectangle2D rect2, boolean isHorizontal){
		
		boolean intersects=false;
		
		if(isHorizontal){
			
			if(rect1.getX()<rect2.getX()){
				
				intersects=rect2.getX()<=(rect1.getX()+rect1.getWidth());
				
			}else{
				
				intersects=rect1.getX()<=(rect2.getX()+rect2.getWidth());
			}
			
		}else{
			
			if(rect1.getY()<rect2.getY()){
				
				intersects=rect2.getY()<=(rect1.getY()+rect1.getHeight());
				
			}else{
				
				intersects=rect1.getY()<=(rect2.getY()+rect2.getHeight());
			}
		}
		
		return intersects;
	}
}
