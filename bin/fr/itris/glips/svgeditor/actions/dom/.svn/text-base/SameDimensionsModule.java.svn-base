package fr.itris.glips.svgeditor.actions.dom;

import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.shape.*;

/**
 * the class used to execute resize actions on svg elements
 * @author Jordi SUC
 */
public class SameDimensionsModule extends DomActionsModule {
	
	/**
	 * the constants
	 */
	protected static final int SAME_SIZE=0, SAME_WIDTH=1, SAME_HEIGHT=2, 
		SAME_SIZE_EACH=3, SAME_WIDTH_EACH=4, SAME_HEIGHT_EACH=5;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public SameDimensionsModule(Editor editor) {
		
		//setting the id
		moduleId="SameDimensions";

		//filling the arrays of the types
		int[] types={SAME_SIZE, SAME_WIDTH, SAME_HEIGHT, 
			SAME_SIZE_EACH, SAME_WIDTH_EACH, SAME_HEIGHT_EACH};
		actionsTypes=types;
		
		//filling the arrays of the ids
		String[] ids={"SameSize", "SameWidth", "SameHeight", "SameSizeEach", 
				"SameWidthEach", "SameHeightEach"};
		actionsIds=ids;
		createItems();
	}
	
	@Override
	protected void doAction(
		SVGHandle handle, Set<Element> elements, 
			int index, ActionEvent evt) {
		
 		//getting the type of the action
		int type=actionsTypes[index];
		
		//getting the clipboard elements
		Map<Element, Rectangle2D> clipboardContents=
			new HashMap<Element, Rectangle2D>(
					Editor.getEditor().getClipboardManager().getClipboardContentMap());

		//getting the union of the bounds of the elements in the clipboard
		Rectangle2D clipboardBounds=union(
				new HashSet<Rectangle2D>(clipboardContents.values()));

		//getting the resize transforms for each element//
		Map<Element, AffineTransform> transformsMap=
			new HashMap<Element, AffineTransform>();
		
		//getting the map of the bounds of the elements
		Map<Element, Rectangle2D> boundsMap=getBounds(handle, elements);
		
		if(type==SAME_SIZE || type==SAME_WIDTH || type==SAME_HEIGHT){
			
			//getting the whole bounds of the selected elements
			Rectangle2D wholeBounds=
				MultiAbstractShape.getElementsBounds(handle, elements);
			
			//computing the scale factors
			double sx=1, sy=1;
			
			if(type==SAME_WIDTH || type==SAME_SIZE){
				
				sx=clipboardBounds.getWidth()/wholeBounds.getWidth();
			}
			
			if(type==SAME_HEIGHT || type==SAME_SIZE){
				
				sy=clipboardBounds.getHeight()/wholeBounds.getHeight();
			}

			//computing the new transform for these bounds
			AffineTransform transform=AffineTransform.getTranslateInstance(
					-wholeBounds.getX(), -wholeBounds.getY());
			
			transform.preConcatenate(AffineTransform.getScaleInstance(sx, sy));
			
			transform.preConcatenate(AffineTransform.getTranslateInstance(
					wholeBounds.getX(), wholeBounds.getY()));

			for(Element element : elements){
				
				transformsMap.put(element, transform);
			}
			
		}else{
			
			Rectangle2D bounds=null;
			AffineTransform transform=null;
			double sx=1, sy=1;

			//getting the scale factors
			for(Element element : elements){
				
				//getting the bounds of the element
				bounds=boundsMap.get(element);
				sx=1;
				sy=1;
				
				if(type==SAME_WIDTH_EACH || type==SAME_SIZE_EACH){
					
					sx=clipboardBounds.getWidth()/bounds.getWidth();
				}
				
				if(type==SAME_HEIGHT_EACH || type==SAME_SIZE_EACH){
					
					sy=clipboardBounds.getHeight()/bounds.getHeight();
				}

				//computing the new transform
				transform=AffineTransform.getTranslateInstance(
						-bounds.getX(), -bounds.getY());
				
				transform.preConcatenate(AffineTransform.getScaleInstance(sx, sy));
				
				transform.preConcatenate(AffineTransform.getTranslateInstance(
						bounds.getX(), bounds.getY()));
				
				//putting the new transform into the map of the transforms
				transformsMap.put(element, transform);
			}
		}

		applyResizeTransform(handle, index, transformsMap);
	}

	@Override
	protected boolean selectionCorrect(int index, Set<Element> elements) {

		boolean isCorrect=false;
		
		//getting the current handle
		SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		if(handle!=null && selectionCorrectFirstType(elements) && 
				Editor.getEditor().getClipboardManager().getClipboardContent().size()>0){
			
			//getting the elements in the clipboard
			Set<Element> clipboardElements=new HashSet<Element>(
					Editor.getEditor().getClipboardManager().getClipboardContent());
			
			//getting an item of the set
			Element el=clipboardElements.iterator().next();
			
			if(el.getOwnerDocument().equals(handle.getCanvas().getDocument())){
				
				isCorrect=true;
			}
		}
		
		return isCorrect;
	}
}