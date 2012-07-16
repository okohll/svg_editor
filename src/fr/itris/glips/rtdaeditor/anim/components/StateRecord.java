package fr.itris.glips.rtdaeditor.anim.components;

import java.lang.ref.*;
import org.w3c.dom.*;

/**
 * the class of the object used to store the state of the animations and actions frame
 * @author ITRIS, Jordi SUC
 */
public class StateRecord {
	
	/**
	 * the currently selected element reference
	 */
	protected WeakReference<Element> selectedElementRef=null;
	
	/**
	 * the source name
	 */
	protected String sourceName="";
	
	/**
	 * the index of the animation
	 */
	protected int animationIndex=0;
	
	/**
	 * the index of the action
	 */
	protected int actionIndex=0;

	/**
	 * @return the animation Index
	 */
	public int getAnimationIndex() {
		return animationIndex;
	}

	/**
	 * @param animationIndex the animationIndex to set
	 */
	public void setAnimationIndex(int animationIndex) {
		
		if(animationIndex<0) {
			
			animationIndex=0;
		}
		
		this.animationIndex=animationIndex;
	}
	
	/**
	 * @return the action index
	 */
	public int getActionIndex() {
		return actionIndex;
	}

	/**
	 * @param actionIndex the new action index
	 */
	public void setActionIndex(int actionIndex) {
		
		if(actionIndex<0) {
			
			actionIndex=0;
		}
		
		this.actionIndex=actionIndex;
	}

	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * @param sourceName the sourceName to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName=sourceName;
	}

	/**
	 * @return the selectedElement
	 */
	public Element getSelectedElement() {
		
		if(selectedElementRef!=null) {
			
			return selectedElementRef.get();
		}
		
		return null;
	}

	/**
	 * @param selectedElement the selectedElement to set
	 */
	public void setSelectedElement(Element selectedElement) {
		this.selectedElementRef = new WeakReference<Element>(selectedElement);
	}

	/**
	 * reinitializing the record
	 */
	public void reinitialize() {
		
		selectedElementRef=null;
		sourceName="";
		animationIndex=0;
	}
}
