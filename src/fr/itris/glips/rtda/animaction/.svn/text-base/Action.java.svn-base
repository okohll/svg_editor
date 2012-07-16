package fr.itris.glips.rtda.animaction;

import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.eventsmanager.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.config.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.test.*;
import fr.itris.glips.rtda.widget.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;
import org.w3c.dom.*;
import java.awt.*;

/**
 * the abstract class that each custom action should extend
 * @author ITRIS, Jordi SUC
 */
public abstract class Action implements ListenerAction{
	
	/**
	 * the attribute name for the confirmation dialog
	 */
	protected static final String confirmationDialogAtt="confirmationDialog";
	
	/**
	 * the label used when an unauthorized action occurs
	 */
	protected static String unauthorizedLabel="";
	
	/**
	 * the label used when an action occurs triggered by a user that has no right
	 */
	protected static String notEntitledLabel="";
	
	/**
	 * the attribute names for specifying rights
	 */
	protected static final String authorizationLevelAtt="authorizationLevel";
	
	/**
	 * attributes names and separators
	 */
	protected static final String authorizationTagName="authorizationTag", 
		authorizationValueName="authorizationValue", valueSeparatorRegex="[|]";
	
	/**
	 * the resources bundle
	 */
	protected static ResourceBundle bundle;
	
	/**
	 * the number chooser
	 */
	protected static NumberChooser numberChooser;
	
	static {
		
        //getting the resource bundle
		try{
			bundle=ResourceBundle.getBundle("fr.itris.glips.rtda.resources.properties.strings");
		}catch (Exception ex){}
		
		//getting the labels
		unauthorizedLabel=bundle.getString("unauthorizedAction");
		notEntitledLabel=bundle.getString("notEntitledAction");
		
		//creating the number chooser
		numberChooser=new NumberChooser(bundle);
	}
	
	/**
	 * the events manager
	 */
	protected EventsManager eventsManager;
	
	/**
	 * the svg picture this action is linked to
	 */
	protected SVGPicture picture;
	
	/**
	 * the name of the project
	 */
	protected String projectName;
	
	/**
	 * the parent container
	 */
	protected Container parent;
	
	/**
	 * the component on which the action is registered
	 */
	protected JComponent component;
	
	/**
	 * the id of the sub component linked to this action
	 * (if the component contains sub components)
	 */
	protected String sourceId="";

	/**
	 * the object to which the action applies, if it is not the provided component
	 */
	protected Object actionObject;
	
	/**
	 * the dom element defining the action
	 */
	protected Element actionElement;
	
	/**
	 * the jwidget runtime object, if this action is linked to a jwidget
	 */
	protected JWidgetRuntime jwidgetRuntime;
	
	/**
	 * the set of the data names that are handled by this action
	 */
	protected Set<String> dataNames=new HashSet<String>();
	
	/**
	 * the map associating a data name to a test tag information object
	 */
	protected Map<String, TestTagInformation> dataNamesToTestTagInfo=
		new HashMap<String, TestTagInformation>();
	
	/**
	 * the tool tip text
	 */
	protected String toolTipText="";
	
	/**
	 * the right type for this action
	 */
	protected int rightType;
	
	/**
	 * the right level for this action
	 */
	protected int rightLevel=0;
	
	/**
	 * whether the user is authorized to execute the action
	 */
	protected boolean isAuthorized=true;
	
	/**
	 * the authorization tag
	 */
	protected String authorizationTag;
	
	/**
	 * the list of the values of the authorization tag
	 */
	protected LinkedList<String> authorizationTagValues=new LinkedList<String>();
	
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
	public Action(SVGPicture picture, String projectName, Container parent, JComponent component, 
			Object actionObject, Element actionElement, JWidgetRuntime jwidgetRuntime) {
		
		this.picture=picture;
		this.projectName=projectName;
		this.parent=parent;
		this.component=component;
		this.actionObject=actionObject;
		this.actionElement=actionElement;
		this.jwidgetRuntime=jwidgetRuntime;

		if(jwidgetRuntime!=null){
			
			//registering the action to the jwidget runtime object
			jwidgetRuntime.addAction(this);
			sourceId=actionElement.getAttribute("jwidget-source");
		}
	}
	
	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#initializeWhenCanvasDisplayed()
	 */
	public void initializeWhenCanvasDisplayed() {}
	
	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#getPicture()
	 */
	public SVGPicture getPicture() {
		
		return picture;
	}
	
	/**
	 * @return the events manager
	 */
	public EventsManager getEventsManager() {
		return eventsManager;
	}

	/**
	 * computes the rights of the user for the actions that modify tags
	 */
	protected void computeRightsForTagsModif(){
		
		this.rightType=UserRights.RIGHT_FOR_TAG_MODIFICATION;
		try{
			rightLevel=Integer.parseInt(
				actionElement.getAttribute(authorizationLevelAtt));
		}catch (Exception ex){}
	}
	
	/**
	 * computes the rights of the user for the actions that load views
	 */
	protected void computeRightsForViewLoading(){
		
		this.rightType=UserRights.RIGHT_FOR_VIEW_LOADING;
		try{
			rightLevel=Integer.parseInt(
				actionElement.getAttribute(authorizationLevelAtt));
		}catch (Exception ex){}
	}
	
	/**
	 * computes the rights of the user for the actions that load recipes
	 */
	protected void computeRightsForRecipeLoading(){
		
		this.rightType=UserRights.RIGHT_FOR_RECIPE_LOADING;
		try{
			rightLevel=Integer.parseInt(
				actionElement.getAttribute(authorizationLevelAtt));
		}catch (Exception ex){}
	}
	
	/**
	 * computes the rights of the user for the actions that modify recipes
	 */
	protected void computeRightsForRecipeModification(){
		
		this.rightType=UserRights.RIGHT_FOR_RECIPE_MODIFICATION;
		try{
			rightLevel=Integer.parseInt(
				actionElement.getAttribute(authorizationLevelAtt));
		}catch (Exception ex){}
	}
	
	/**
	 * computes the rights of the user for the actions that launch programs
	 */
	protected void computeRightsForProgramLaunching(){
		
		this.rightType=UserRights.RIGHT_FOR_PROGRAM_LAUNCHING;
		try{
			rightLevel=Integer.parseInt(
				actionElement.getAttribute(authorizationLevelAtt));
		}catch (Exception ex){}
	}

	/**
	 * @return whether the user has the rights to execute this action
	 */
	public boolean isEntitled(){

		boolean isEntitled=false;
		UserRights userRights=picture.getMainDisplay().getUserRights();
		
		switch(rightType){
			
			case UserRights.RIGHT_FOR_TAG_MODIFICATION :
				
				isEntitled=
					(userRights.getRightForTagModification()>=rightLevel);
				break;
				
			case UserRights.RIGHT_FOR_VIEW_LOADING :
				
				isEntitled=
					(userRights.getRightForViewLoading()>=rightLevel);
				break;
				
			case UserRights.RIGHT_FOR_RECIPE_LOADING :
				
				isEntitled=
					(userRights.getRightForRecipeLoading()>=rightLevel);
				break;
				
			case UserRights.RIGHT_FOR_RECIPE_MODIFICATION :
				
				isEntitled=
					(userRights.getRightForRecipeModification()>=rightLevel);
				break;
				
			case UserRights.RIGHT_FOR_PROGRAM_LAUNCHING :
				
				isEntitled=
					(userRights.getRightForProgramLaunching()>=rightLevel);
				break;
		}
		
		return isEntitled;
	}
	
	/**
	 * initializes the authorization tag
	 */
	protected void initializeAuthorizationTag(){
		
		authorizationTag=actionElement.getAttribute(authorizationTagName);
		LinkedList<String> realAuthorizationValues=new LinkedList<String>();
		
        //checking if the authorization tag has been specified
        if(authorizationTag!=null && ! authorizationTag.equals("")){

        	isAuthorized=false;
        	dataNames.add(authorizationTag);
        	
        	//getting the values of the authorization tag
        	String[] splitValues=actionElement.getAttribute(
        		authorizationValueName).split(valueSeparatorRegex);
        	
        	if(splitValues!=null){
        		
        		for(int i=0; i<splitValues.length; i++){
        			
        			if(splitValues[i]!=null && ! splitValues[i].equals("")){
        				
        				authorizationTagValues.add(
        					AnimationsToolkit.normalizeEnumeratedValue(splitValues[i]));
        				realAuthorizationValues.add(splitValues[i]);
        			}
        		}
        	}
        	
            //if we are in the test version, we store information on the tags
            if(picture.getMainDisplay().isTestVersion()){

            	//adding the information object for the test for the authorization tag
            	TestTagInformation info=new TestTagInformation(
            			picture, authorizationTag, realAuthorizationValues);
            	dataNamesToTestTagInfo.put(authorizationTag, info);
            }
        }
	}
	
	/**
	 * checks whether the action is authorized
	 */
	protected void checkIsAuthorized(){
		
		//checking if the action is authorized or not
		if(authorizationTag!=null && ! authorizationTag.equals("") &&
				dataNames.contains(authorizationTag)) {
			
			isAuthorized=authorizationTagValues.contains(
					getData(authorizationTag));
		}
	}

    /**
     * notifies that an action should be performed
     * @param event the event for the action
     */
    public abstract void performAction(Object event);
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#dataChanged(fr.itris.glips.rtda.animaction.DataEvent)
     */
    public Runnable dataChanged(DataEvent evt) {

    	if(eventsManager!=null){
    		
    		eventsManager.dataChanged(evt);
    	}
    	
    	return null;
    }

	/**
	 * returns the value corresponding to the provided tag name
	 * @param tagName a tag name
	 * @return the value corresponding to the provided tag name
	 */
	public Object getData(String tagName){
		
		return picture.getMainDisplay().
			getAnimationsHandler().getData(tagName);
	}
	
	/**
	 * registers the provided tag name
	 * @param tagName a tag name
	 */
	public void addData(String tagName){
		
		dataNames.add(tagName);
	}
    
    /**
     * sets the value of the given tag
     * @param tagName the name of a tag
     * @param tagValue the value of a tag
     */
    public final void putTagValue(String tagName, Object tagValue) {
    	
    	if(tagValue!=null && tagValue instanceof String){
    		
    		tagValue=((String)tagValue).toLowerCase();
    	}
    	
    	picture.getMainDisplay().getAnimationsHandler().
    		setDataValue(picture.getAnimActionsHandler().getServerIPAddress(), //TODO
    				tagName, tagValue);
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#getDataNamesToInformation()
     */
    public Map<String, TestTagInformation> getDataNamesToInformation() {

    	return dataNamesToTestTagInfo;
    }
    
	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#getDataNames()
	 */
	public Set<String> getDataNames() {
		
		return dataNames;
	}
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#getTooltipText()
     */
    public String getTooltipText() {

    	return toolTipText;
    }
    
    /**
     * @return the component onto which the action can be done
     */
    public JComponent getActionComponent() {
    	
    	return component;
    }
    
    /**
     * @return the id of the sub component linked to this action
	 * (if the component contains sub components)
     */
    public String getSourceId() {
		return sourceId;
	}
    
    /**
     * @return the object to which the action applies, if it is not the component
     */
    public Object getActionObject() {
    	
    	return actionObject;
    }
    
    /**
     * @return the dom element defining the action
     */
    public Element getActionElement() {
    	
    	return actionElement;
    }
    
    /**
     * @return the rectangle corresponding to the area for which the action should be triggered
     * 					<tt>null<tt> if the action spreads over the whole component 
     */
    public Rectangle2D getBounds() {
    	
    	if(actionObject!=null && actionObject instanceof Element) {
    		
    		Rectangle2D bounds=AnimationsToolkit.getNodeBounds(
    				picture, (Element)actionObject);
    		
    		if(bounds!=null) {
    			
    			return bounds;
    		}
    	}
    	
    	return null;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#invalidMarkersAllowed()
     */
    public boolean invalidMarkersAllowed() {

    	return false;
    }
	
	/**
	 * @return whether the user is authorized to execute the action
	 */
	public boolean isAuthorized() {
		return isAuthorized;
	}
	
	/**
	 * shows a confirmation dialog if specified in the action 
	 * element and returns whether the action should be done
	 * @return whether the action should be done
	 */
	protected boolean showConfirmationDialog(){
		
		boolean doAction=true;
		
		String confirmationMessage=
			actionElement.getAttribute(confirmationDialogAtt);
		
		if(! confirmationMessage.equals("")){

			int result=JOptionPane.showConfirmDialog(
				component, confirmationMessage, "", JOptionPane.YES_NO_OPTION);
			
			doAction=(result==JOptionPane.YES_OPTION);
		}
		
		return doAction;
	}
	
	/**
	 * @return the point where the mouse can be found or the 
	 * center point of the parent element of this action
	 */
	protected Point getCurrentMousePoint(){
		
		Point point=component.getMousePosition();
		
		if(component instanceof SVGCanvas){
			
			try{
				//getting the bounds of the edited element
				Rectangle2D bounds=AnimationsToolkit.getNodeBounds(picture, 
						actionElement.getParentNode());
				
				point=new Point((int)(bounds.getX()+bounds.getWidth()/2), 
						(int)(bounds.getY()+bounds.getHeight()/2));
			}catch (Exception ex){}
		}

		return point;
	}
	
	/**
	 * shows a message notifying the user that 
	 * he is not authorized to execute the action
	 */
	protected void showUnAuthorizedMessage(){

		String tooltipText="<html><body>"+unauthorizedLabel+"</body></html>";
		
		component.setToolTipText(tooltipText);
    	JToolTip toolTip=component.createToolTip();
    	
    	Point point=getCurrentMousePoint();
    	toolTip.setLocation(point);
    	toolTip.setTipText(tooltipText);
	}
	
	/**
	 * shows a message notifying the user that 
	 * he is not entitled to execute the action
	 */
	protected void showNotEntitledMessage(){
		
		String tooltipText="<html><body>"+notEntitledLabel+"</body></html>";
		
		component.setToolTipText(tooltipText);
    	JToolTip toolTip=component.createToolTip();
    	toolTip.setLocation(getCurrentMousePoint());
    	toolTip.setTipText(tooltipText);
	}
	
	/**
	 * disposes this action
	 */
	public void dispose(){
		
		if(jwidgetRuntime!=null){
			
			//registering the action to the jwidget runtime object
			jwidgetRuntime.removeAction(this);
		}
	}
}
