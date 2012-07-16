package fr.itris.glips.rtdaeditor.anim.components;

import javax.swing.table.*;
import fr.itris.glips.rtdaeditor.anim.*;

/**
 * the class of the model of the table used for modifying the properties of an animation
 * @author ITRIS, Jordi SUC
 */
public abstract class AnimationTableRenderer implements TableCellRenderer{

	/**
	 * the current animation object
	 */
	protected AnimationObject animationObject=null;
	
	/**
	 * sets the animation object
	 * @param animationObject the animation object
	 */
	public void setAnimationObject(AnimationObject animationObject){
		
		this.animationObject=animationObject;
	}
	
	/**
	 * @return Returns the animationObject.
	 */
	public AnimationObject getAnimationObject() {
		
		return animationObject;
	}
	
	/**
	 * cleans this renderer
	 */
	public void clean() {
		
		animationObject=null;
	}

}
