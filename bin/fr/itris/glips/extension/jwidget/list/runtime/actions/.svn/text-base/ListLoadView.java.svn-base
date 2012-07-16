package fr.itris.glips.extension.jwidget.list.runtime.actions;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import org.w3c.dom.*;
import fr.itris.glips.rtda.action.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.widget.*;

/**
 * the class of the load view action
 * @author ITRIS, Jordi SUC
 */
public class ListLoadView extends AbstractLoadView{
	
	/**
	 * the string names
	 */
	protected static final String usedAttName="used";
	
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
	public ListLoadView(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
		
		initializeAction();
	}
	
	@Override
	protected void initializeAction() {
        
       initializeAuthorizationTag();
		
    	//getting the values of the tag to write
    	LinkedList<String> viewPaths=new LinkedList<String>();
    	
    	if(actionElement.hasChildNodes()) {
    		
    		Element childElement=null;
    		
    		for(Node child=actionElement.getFirstChild(); child!=null; child=child.getNextSibling()) {
    			
    			if(child instanceof Element) {
    				
    				childElement=(Element)child;
    				
    				if(childElement.getAttribute(usedAttName).equals(Boolean.toString(true))){
    					
        				viewPaths.add(childElement.getAttribute(viewAttributeName));
    				}
    			}
    		}
    	}

        //filling the list with the items corresponding to each view path
    	final JList list=(JList)component;
        jwidgetRuntime.unregisterListeners();
    	final DefaultListModel model=(DefaultListModel)list.getModel();
        ComboListItem item=null;
        
        //adding an empty item
        model.addElement(new ComboListItem("", ""));
        
        for(String value : viewPaths){
        	
        	item=new ComboListItem(value, value);
        	model.addElement(item);
        }
        
        jwidgetRuntime.registerListeners();
        
        viewListener=new ViewLoadedListener(){
        	
        	/**
        	 * @param xmlViewPath the xml path of a view
        	 */
        	public void viewLoaded(final String xmlViewPath) {

    			SwingUtilities.invokeLater(new Runnable(){
    				
    				public void run() {

    			        int selectedIndex=0;
    			        ComboListItem comboItem;
    			        
    			        for(int i=0; i<model.size(); i++){
    			        	
    			        	comboItem=(ComboListItem)model.elementAt(i);
    			        	
    			        	if(xmlViewPath.equals(comboItem.getValue())){
    			        		
    			        		selectedIndex=i;
    			        		break;
    			        	}
    			        }
    			        
    			        jwidgetRuntime.unregisterListeners();
    			        list.setSelectedIndex(selectedIndex);
    			        jwidgetRuntime.registerListeners();
    				}
    			});
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
	public void performAction(Object evt){
		
		if(isEntitled() && isAuthorized && showConfirmationDialog()) {
			
			//getting the id of the target view browser
			String targetId=actionElement.getAttribute(targetAttributeName);
			
			if(targetId.equals("") && picture.getViewBrowser()!=null){
				
				//getting the id of the viewbrowser into 
				//which the current picture is displayed
				targetId=picture.getViewBrowser().getViewBrowserId();
			}
			
			ComboListItem item=(ComboListItem)((JList)component).getSelectedValue();
			
			if(item!=null){
				
				//getting the path of the view to be loaded
				String viewPath=item.getValue().toString();

				if(viewPath.equals(quitValue)) {
					
					picture.getMainDisplay().getAnimationsHandler().fireQuitProgramRequest();
					
				}else if(viewPath.equals(returnToPreviousValue)) {
					
					//displaying the previously displayed picture
					picture.getMainDisplay().getAnimationsHandler().fireReturnToLastViewRequest(targetId);
					
				}else {
					
					//getting the uri of the view to be loaded
					String uri=picture.getMainDisplay().getPictureManager().getViewUri(projectName, viewPath);
					
					if(uri!=null && ! uri.equals("")) {

						picture.getMainDisplay().getAnimationsHandler().fireViewDisplayRequest(targetId, uri);
					}
				}
			}
		}
	}

	@Override
	public void dispose() {
		
		picture.getMainDisplay().getViewBrowsersManager().
			removeViewLoadedListener(viewListener);
	}
}
