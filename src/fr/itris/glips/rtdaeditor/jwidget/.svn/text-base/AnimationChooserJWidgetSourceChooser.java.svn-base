package fr.itris.glips.rtdaeditor.jwidget;

import java.lang.ref.WeakReference;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.anim.components.*;

/**
 * the class used for setting a jwidget source in an animation or action chooser
 * @author ITRIS, Jordi SUC
 */
public abstract class AnimationChooserJWidgetSourceChooser extends JComponent{

	/**
	 * the animations or actions chooser
	 */
	protected AnimationsChooser animationsChooser=null;
	
	/**
	 * the jwidget element
	 */
	protected WeakReference<Element> jwidgetElementRef=null;
	
	/**
	 * sets the source name for an inner element of a jwidget
	 * @param sourceName the name of a source
	 */
	public void setSource(String sourceName) {
		
		if(animationsChooser!=null) {
			
			animationsChooser.setNewSource(sourceName);
		}
	}
	
	/**
	 * sets the initial source of this chooser
	 * @param animationsChooser the animationsChooser to set
	 * @param jwidgetElement a jwidget element
	 */
	public void setInitialSource(AnimationsChooser animationsChooser, Element jwidgetElement) {
		
		this.animationsChooser=animationsChooser;
		this.jwidgetElementRef=new WeakReference<Element>(jwidgetElement);
		updateWidgets();
	}
	
	/**
	 * @return the jwidget element
	 */
	protected Element getJwidgetElement() {
		
		if(jwidgetElementRef!=null) {
			
			return jwidgetElementRef.get();
		}
		
		return null;       
	}
	
	/**
	 * updates the widgets with new data
	 */
	protected abstract void updateWidgets();
	
	/**
	 * builds this source chooser
	 */
	protected abstract void buildWidget();
	
	/**
	 * cleans this object from its unused listeners
	 */
	public void clean() {
		
		jwidgetElementRef=null;
	}

}
