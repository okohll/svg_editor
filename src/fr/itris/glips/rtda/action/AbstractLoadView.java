package fr.itris.glips.rtda.action;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.components.viewbrowser.*;
import fr.itris.glips.rtda.jwidget.*;
import java.awt.*;

/**
 * the abstract class of the load view action
 * @author ITRIS, Jordi SUC
 */
public abstract class AbstractLoadView extends fr.itris.glips.rtda.animaction.Action{
	
	/**
	 * the string names
	 */
	protected static final String targetAttributeName="target", viewAttributeName="view", 
												closePopupDialogValue="/**closePopupDialog**/",
												quitValue="/**quit**/", returnToPreviousValue="/**returnToPrevious**/",
												popupTargetOption="popupTarget";
	
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
	public AbstractLoadView(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
		computeRightsForViewLoading();
	}
	
	/**
	 * initializes the action
	 */
	protected void initializeAction(){
		
		//getting the tool tip
		String viewPath=actionElement.getAttribute(viewAttributeName);
		
		try{
			if(viewPath.equals(quitValue)){
				
				toolTipText=bundle.getString("tooltip_quit");
				
			}else if(viewPath.equals(closePopupDialogValue)){
				
				toolTipText=bundle.getString("tooltip_closePopupDialog");
				
			}else if(viewPath.equals(returnToPreviousValue)){
				
				toolTipText=bundle.getString("tooltip_returnToLastView");
				
			}else{
				
				toolTipText=bundle.getString("tooltip_loadView")+" "+viewPath;
			}
		}catch (Exception ex){ex.printStackTrace();}

		initializeAuthorizationTag();
	}
	
	@Override
	public void performAction(Object evt) {

		if(isEntitled() && isAuthorized && showConfirmationDialog()){
			
			//getting the id of the target view browser
			String targetId=actionElement.getAttribute(targetAttributeName);
			
			//getting the path of the view to be loaded
			String viewPath=actionElement.getAttribute(viewAttributeName);

			if(viewPath.equals(quitValue)) {
				
				picture.getMainDisplay().getAnimationsHandler().
					fireQuitProgramRequest();
				
			}else if(viewPath.equals(closePopupDialogValue)) {
				
				//getting the top most component of the picture
				//that is linked to the popup dialog
				Component topMostComponent=picture.getCanvas().getTopLevelAncestor();

				if(topMostComponent!=null && 
						topMostComponent instanceof ViewBrowserPopupDialog){
					
					ViewBrowserPopupDialog dialog=
						(ViewBrowserPopupDialog)topMostComponent;
					dialog.dispose();
				}
				
			}else if(viewPath.equals(returnToPreviousValue)) {
				
				//displaying the previously displayed picture
				picture.getMainDisplay().getAnimationsHandler().
					fireReturnToLastViewRequest(targetId);
				
			}else {
				
				if(targetId.equals(popupTargetOption)){
					
					//launching the dialog
					Container parentContainer=picture.getCanvas().getTopLevelAncestor();
					
					if(parentContainer instanceof Frame){
						
						new ViewBrowserPopupDialog(
								picture.getMainDisplay(), (Frame)parentContainer, viewPath, 
										picture.getCanvas().getProjectName(), component);
						
					}else if(parentContainer instanceof JApplet){
						
						new ViewBrowserPopupDialog(
							picture.getMainDisplay(), picture.getMainDisplay().getTopLevelFrame(), 
								viewPath, picture.getCanvas().getProjectName(), component);
						
					}else{
						
						new ViewBrowserPopupDialog(
								picture.getMainDisplay(), (JDialog)parentContainer, viewPath, 
										picture.getCanvas().getProjectName(), component);
					}

				}else{
					
					//getting the uri of the view to be loaded
					String uri=picture.getMainDisplay().getPictureManager().
						getViewUri(projectName, viewPath);

					if(uri!=null && ! uri.equals("")) {
						
						if(targetId.equals("") && picture.getViewBrowser()!=null){
							
							//getting the id of the viewbrowser into 
							//which the current picture is displayed
							targetId=picture.getViewBrowser().getViewBrowserId();
						}

						picture.getMainDisplay().getAnimationsHandler().
							fireViewDisplayRequest(targetId, uri);
					}
				}
			}
		}
	}
	
	@Override
	public Runnable dataChanged(DataEvent evt) {
		
		super.dataChanged(evt);
		checkIsAuthorized();
		return null;
	}
}
