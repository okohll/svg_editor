package fr.itris.glips.rtdaeditor.jwidget;

import java.util.*;
import java.util.concurrent.*;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to refresh the representation of jwidgets
 * @author ITRIS, Jordi SUC
 */
public class JWidgetRepresentationRefreshThread extends Thread{
	
	/**
	 * the jwidget manager
	 */
	private JWidgetManager jwidgetManager;
	
	/**
	 * the set of the objects to refresh
	 */
	private Set<RefreshObject> refreshObjects=
		new CopyOnWriteArraySet<RefreshObject>();

	/**
	 * the constructor of the class
	 * @param jwidgetManager the jwidget manager
	 */
	public JWidgetRepresentationRefreshThread(JWidgetManager jwidgetManager) {
		
		this.jwidgetManager=jwidgetManager;
		start();
	}
	
	/**
	 * adds a new jwidget elements to refresh
	 * @param element
	 */
	protected void addElement(Element element) {

		//getting the current handle
		SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		if(element!=null && handle!=null) {
	
			refreshObjects.add(new RefreshObject(handle, element));
		}
	}
	
	@Override
	public void run() {
		
		Set<RefreshObject> clonedRefreshObjects=null;
		
		while(true) {
			
			if(refreshObjects.size()>0) {
				
				//cloning the set of the elements
				clonedRefreshObjects=refreshObjects;
				Set<RefreshObject> set=new CopyOnWriteArraySet<RefreshObject>();
				
				synchronized(JWidgetRepresentationRefreshThread.this) {
					
					refreshObjects=set;
				}
				
				//refreshing each element for each refresh object				
				for(RefreshObject refreshObject : clonedRefreshObjects){

					final SVGHandle handle=refreshObject.getHandle();
					final Element element=refreshObject.getElement();

					//getting its related jwidget edition object
					final JWidgetEdition jwidgetEdition=jwidgetManager.getJWidgetEdition(
							element.getAttribute(Toolkit.nameAttribute));

					if(jwidgetEdition!=null) {

						try{
							SwingUtilities.invokeAndWait(new Runnable(){

								public void run() {

									//refreshing the representation of this element
									jwidgetEdition.handleRepresentationImage(
											handle, element, (Element)element.getParentNode(), null);
								}
							});
						}catch (Exception ex){ex.printStackTrace();}
					}
				}
				
				clonedRefreshObjects.clear();
			}
			
			try {sleep(500);}catch (Exception ex) {}
		}
	}
	
	/**
	 * the class used to store information 
	 * @author Jordi SUC
	 */
	protected class RefreshObject implements Comparable<RefreshObject>{
		
		/**
		 * the handle to which the element to refresh belongs
		 */
		private SVGHandle handle;
		
		/**
		 * the element to refresh
		 */
		private Element element;
		
		/**
		 * the constructor of the class
		 * @param handle the handle to which the element to refresh belongs
		 * @param element the element to refresh
		 */
		public RefreshObject(SVGHandle handle, Element element){
			
			this.handle=handle;
			this.element=element;
		}
		
		/**
		 * @return the handle to which the element to refresh belongs
		 */
		public SVGHandle getHandle() {
			
			return handle;
		}
		
		/**
		 * @return the element to refresh
		 */
		public Element getElement(){
			
			return element;
		}
		
		public int compareTo(RefreshObject o) {

			return 0;
		}
	}
}
