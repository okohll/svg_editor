package fr.itris.glips.extension.jwidget.base.runtime.actions;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.action.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;
import java.awt.*;

/**
 * the class of the load view action
 * @author ITRIS, Jordi SUC
 */
public class ButtonGroupLoadView extends AbstractLoadView{
	
	/**
	 * the string names
	 */
	private static final String viewAttribute="view";
	
	/**
	 * the listener to the loading of views
	 */
	private ViewLoadedListener viewListener;
	
	/**
	 * the constructor of the class
	 * @param picture the svg picture this action is linked to
	 * @param projectName the name of the project this action is linked to
	 * @param parent the top level container
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
	 * @param jwidgetRuntime the jwidget runtime object, if this action is linked to a jwidget
	 */
	public ButtonGroupLoadView(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
		
		initializeAction();
	}
	
	@Override
	protected void initializeAction(){

        initializeAuthorizationTag();
        
        viewListener=new ViewLoadedListener(){
        	
        	/**
        	 * @param xmlViewPath the xml path of a view
        	 */
        	public void viewLoaded(final String xmlViewPath) {

        		if(isAuthorized){
        			
        			SwingUtilities.invokeLater(new Runnable(){
        				
        				public void run() {

        					String buttonView=actionElement.getAttribute(viewAttribute);
        					
        					if(! buttonView.equals("") && buttonView.equals(xmlViewPath)){
        						
    							jwidgetRuntime.unregisterListeners();
        						((AbstractButton)component).setSelected(true);
    							jwidgetRuntime.registerListeners();
        					}
        				}
        			});
        		}
        	}
        };
        
        picture.getMainDisplay().getViewBrowsersManager().
        	addViewLoadedListener(actionElement.getAttribute(targetAttributeName), viewListener);
	}
	
	@Override
	public Runnable dataChanged(DataEvent evt) {

		super.dataChanged(evt);
		
		if(isEntitled()){
			
    		jwidgetRuntime.refreshComponentState();
		}

		return null;
	}

	@Override
	public void dispose() {

		picture.getMainDisplay().getViewBrowsersManager().removeViewLoadedListener(viewListener);

		super.dispose();
	}
}
