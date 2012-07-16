package fr.itris.glips.rtdaeditor.jwidget;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the panel used to configure a JWidget
 * @author ITRIS, Jordi SUC
 */
public abstract class JWidgetConfigurationPanel extends JPanel{

	/**
	 * the widget element
	 */
	protected Element widgetElement=null;
	
	/**
	 * the dom listener to an element
	 */
	private SVGDOMListener domListener=null;
	
	/**
	 * sets the new widget element for this panel
	 * @param element a jwidget element
	 */
	public void setElement(Element element) {

		this.widgetElement=null;
		
		//removing the previous dom listener
		if(domListener!=null) {
			
			domListener.removeListener();
		}
		
		if(element!=null) {
			
			this.widgetElement=element;
			
			//setting the new dom listener
			domListener=new SVGDOMListener(element){
				
				@Override
				public void nodeChanged() {

					initializePanel();
				}

				@Override
				public void nodeInserted(Node insertedNode) {}

				@Override
				public void nodeRemoved(Node removedNode) {}

				@Override
				public void structureChanged(Node lastModifiedNode) {}
			};
			
			//getting the current handle
			SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
			
			if(handle!=null) {

				handle.getSvgDOMListenerManager().
					addDOMListener(domListener);
			}
		}
		
		initializePanel();
	}
	
	/**
	 * @return the JWidget element
	 */
	public Element getElement() {
		
		return widgetElement;
	}
	
	/**
	 * initializes the panel with the new element
	 */
	public abstract void initializePanel();
	
	/**
	 * builds the configuration panel
	 */
	public abstract void buildPanel();
	
	/**
	 * cleans this object from its listeners
	 */
	public void clean() {
		
		widgetElement=null;
		
		if(domListener!=null) {
			
			domListener.removeListener();
		}
	}
}
