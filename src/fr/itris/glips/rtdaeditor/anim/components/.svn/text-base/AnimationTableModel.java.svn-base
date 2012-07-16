package fr.itris.glips.rtdaeditor.anim.components;

import javax.swing.table.*;
import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.display.canvas.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;
import java.util.*;

/**
 * the super class of the table models
 * @author ITRIS, Jordi SUC
 */
public abstract class AnimationTableModel extends AbstractTableModel {
	
	/**
	 * the current animation object
	 */
	protected AnimationObject animationObject=null;
	
	/**
	 * the animation table
	 */
	protected AnimationTable animationTable;
	
	/**
	 * the dom listener
	 */
	protected SVGDOMListener domListener=null;
	
	/**
	 * the listeners to the modification of the child elements
	 */
	protected LinkedList<SVGDOMListener> childListeners=new LinkedList<SVGDOMListener>();
	
	/**
	 * the constructor of the class
	 * @param animationTable the table of this model
	 */
	public AnimationTableModel(AnimationTable animationTable){
		
		this.animationTable=animationTable;
	}
	
	/**
	 * sets the animation object
	 * @param animObject the animation object
	 */
	public abstract void setAnimationObject(AnimationObject animObject);
	
	/**
	 * removes all the children listeners
	 * @param handle a svg handle
	 */
	protected void removeChildrenListeners(SVGHandle handle) {

		for(SVGDOMListener listener : childListeners) {
			
			handle.getSvgDOMListenerManager().
				removeDOMListener(listener);
		}
		
		childListeners.clear();
	}
	
	/**
	 * creates all the children listeners
	 */
	protected void createChildrenListeners() {

		if(animationObject!=null) {
			
			Element childElement=null;
			SVGDOMListener listener=null;
			
			for(ChildObject child : animationObject.getChildrenList()) {
				
				if(child!=null) {
					
					childElement=child.getChildAnimationElement();
					
					listener=new SVGDOMListener(childElement) {

						@Override
						public void nodeChanged() {
							
							fireTableDataChanged();
						}

						@Override
						public void nodeInserted(Node insertedNode) {}

						@Override
						public void nodeRemoved(Node removedNode) {}

						@Override
						public void structureChanged(Node lastModifiedNode) {
							
							fireTableDataChanged();
						}
					};
					
					animationObject.getSVGHandle().
						getSvgDOMListenerManager().addDOMListener(listener);
					childListeners.add(listener);
				}
			}
		}
	}
	
	/**
	 * @return Returns the animationObject.
	 */
	public AnimationObject getAnimationObject() {
		return animationObject;
	}
	
	@Override
	public void fireTableStructureChanged() {
		
		super.fireTableStructureChanged();
		animationTable.resizeColumns();
	}
	
	/**
	 * removes the listeners in this object
	 */
	public void clean() {
		
		animationObject=null;
		
		//removing the former dom listener
		if(domListener!=null) {
			
			domListener.removeListener();
			domListener=null;
		}
		
		//removing the former children dom listeners
		for(SVGDOMListener listener : childListeners) {
			
			if(listener!=null) {
				
				listener.removeListener();
			}
		}
	}
}
