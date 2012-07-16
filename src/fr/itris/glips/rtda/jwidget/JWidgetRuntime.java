/*
 * Created on 30 novembre 2005
 */
package fr.itris.glips.rtda.jwidget;

import javax.swing.*;
import org.w3c.dom.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.animaction.Action;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.test.*;

/**
 * the abstract class that each jwidget should extend
 * @author ITRIS, Jordi SUC
 */
public abstract class JWidgetRuntime implements ListenerAction{
	
    /**
     * the tag name of each jwidget node
     */
    public static final String jwidgetTagName="rtda:jwidget";
	
	/**
	 * the name attribute
	 */
	public static final String jwidgetNameAttributeName="jwidget-name";
	
	/**
	 * the label attribute
	 */
	public static final String jwidgetLabelAttributeName="jwidget-label";
	
	/**
	 * the id attribute
	 */
	public static String jwidgetIdAttributeName="jwidget-id";
	
	/**
	 * the prefix for the id attribute
	 */
	public static String jwidgetPrefixtId="jw-";
	
	/**
	 * the source attribute
	 */
	public static String jwidgetSourceAttributeName="jwidget-source";
	
	/**
	 * the svg picture this jwidget runtime object registers
	 */
	protected SVGPicture picture;
	
    /**
     * the parent component
     */
    protected JComponent component;
    
	/**
	 * the bounds of the component
	 */
	protected Rectangle componentBounds=new Rectangle();
    
    /**
     * the name of the project to which this jwidget runtime object belongs
     */
    protected String projectName="";
    
    /**
     * the svg element defining the jwidget
     */
    protected Element element;
    
	/**
	 * the set of the data names that are handled
	 */
	protected Set<String> dataNames=new CopyOnWriteArraySet<String>();
	
	/**
	 * the map associating a data name to a test tag information object
	 */
	protected Map<String, TestTagInformation> dataNamesToTestTagInfo=
		new ConcurrentHashMap<String, TestTagInformation>();
	
	/**
	 * the list of the animations of the jwidget runtime object
	 */
	protected List<JWidgetAnimation> animations=
		new CopyOnWriteArrayList<JWidgetAnimation>();
	
	/**
	 * the list of the actions of the jwidget runtime object
	 */
	protected List<Action> actions=new CopyOnWriteArrayList<Action>();
 
    /**
     * the constructor of the class
     * @param picture the picture this jwidget registers
     * @param element the svg element defining the jwidget
     */
    public JWidgetRuntime(SVGPicture picture, Element element){

        this.picture=picture;
    	this.projectName=picture.getAnimActionsHandler().getProjectName();
        this.element=element;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#getPicture()
     */
    public SVGPicture getPicture() {
    	
    	return picture;
    }
    
    /**
     * @return whether this jwidget runtime object needs to receive data updates
     */
    public boolean requiresListening(){
    	
    	return dataNames.size()>0;
    }
    
    /**
     * initializes the jwidget runtime
     */
    public abstract void initialize();
    
    /**
     * gets and sets the bounds of the component
     */
    public void handleBounds(){
    	
    	computeBounds();
    	component.setBounds(componentBounds);
    }
    
    /**
     * sets the value of the given tag to <code>tagValue</code>
     * @param tagName the name of a tag
     * @param tagValue the value of a tag
     */
    public final void putTagValue(String tagName, Object tagValue) {
    	
    	picture.getMainDisplay().getAnimationsHandler().
    			setDataValue(picture.getAnimActionsHandler().getServerIPAddress(), 
    					tagName, tagValue);
    }
    
    /**
     * creates and returns the animation object corresponding to the given animation element
     * @param animationElement an animation element
     * @return the animation object corresponding to the given animation element
     */
    public JWidgetAnimation createAnimation(Element animationElement){
    	
    	return null;
    }
    
    /**
     * creates and returns the action object corresponding to the given action element
     * @param actionElement an action element
     * @return the action object corresponding to the given action element
     */
    public fr.itris.glips.rtda.animaction.Action createAction(Element actionElement){
    	
    	fr.itris.glips.rtda.animaction.Action action=null;
    	
    	if(actionElement!=null) {
    		
    		String tagName=actionElement.getTagName();
    		
    		if(tagName.equals("rtda:class")) {
    			
    			//retrieving the class name in the action element
    			String className=actionElement.getAttribute(CustomAction.classAttributeName);
    			
    			//getting the class corresponding to the given class name
    			Class<?> theClass=ActionsLoader.getClass(CustomAction.class, 
    					picture.getAnimActionsHandler().getProjectFile(), className, false);
    			
    			if(theClass!=null) {

    				//creating an instance of the class
    				Class<?>[] classParam={SVGPicture.class, String.class, Container.class, 
    													JComponent.class, Object.class, Element.class};
    				Object[] params={picture, projectName, picture.getCanvas(), component, null, actionElement};
    				
    				try {
    					action=(fr.itris.glips.rtda.animaction.Action)theClass.getConstructor(classParam).newInstance(params);
    				}catch (Exception ex) {}
    			}
    		}
    	}
    	
    	return action;
    }
    
    /**
     * refreshes the state of the component 
     * according to the state of its actions
     */
    public void refreshComponentState(){

		component.setEnabled(! allActionsInactive());
    }
    
    /**
     * @return whether all the actions are inactive
     */
    protected boolean allActionsInactive(){
    	
    	return actionsInactive(actions);
    }
    
    /**
     * returns whether the provided actions are inactive
     * @param actionsSet a set of actions
     * @return whether the provided actions are inactive
     */
    protected boolean actionsInactive(Collection<Action> actionsSet){
    	
    	boolean actionsInactive=true;
    	
    	for(Action action : actionsSet){
    		
    		if(action!=null && action.isEntitled() && action.isAuthorized()){
    			
    			actionsInactive=false;
    			break;
    		}
    	}
    	
    	return actionsInactive;
    }
    
    /**
     * @return the component that will be displayed
     */
    public JComponent getComponent() {
    	
    	return component;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#initializeWhenCanvasDisplayed()
     */
    public void initializeWhenCanvasDisplayed(){}
    
    /**
     * @return the bounds of the component
     */
    public Rectangle getBounds() {
    	
    	return componentBounds;
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
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#invalidMarkersAllowed()
	 */
	public boolean invalidMarkersAllowed() {

		return false;
	}
	
	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#getTooltipText()
	 */
	public String getTooltipText() {

		return null;
	}
	
	/**
	 * @return the id of the jwidget runtime
	 */
	public String getId() {
		
		String id="";
		
		if(element!=null) {
			
			id=element.getAttribute(jwidgetIdAttributeName);
		}
		
		return id;
	}
	
	/**
	 * @see fr.itris.glips.rtda.animaction.ListenerAction#dataChanged(fr.itris.glips.rtda.animaction.DataEvent)
	 */
	public Runnable dataChanged(DataEvent evt) {

		return null;
	}
	
	/**
	 * @return the document of the element describing this jwidget element
	 */
	public Document getDocument() {
		
		return element.getOwnerDocument(); 
	}
	
	/**
	 * @return the jwidget element
	 */
	public Element getJWidgetElement() {
		return element;
	}
	
	/**
	 * @return the project name
	 */
	public String getProjectName() {
		return projectName;
	}
	
    /**
     * computes the bounds corresponding to the action element
     */
    protected void computeBounds(){

    	componentBounds=JWidgetToolkit.computeBounds(picture, element);
    }
    
    /**
     * handles the look attributes of the element
     */
    protected void handleLook() {
    	
    	JWidgetToolkit.handleLook(element, component);
    }
    
    /**
     * adds a new animation
     * @param animation an animation linked to this jwidget runtime object
     */
    public void addAnimation(JWidgetAnimation animation){
    	
    	if(animation!=null){
    		
    		animations.add(animation);
    	}
    }
    
    /**
     * @return the animations
     */
    public List<JWidgetAnimation> getAnimations() {
    	
		return animations;
	}
    
    /**
     * adds the provided animation
     * @param animation an animation linked to this jwidget runtime object
     */
    public void removeAnimation(JWidgetAnimation animation){
    	
    	if(animation!=null){
    		
    		animations.remove(animation);
    	}
    }
    
    /**
     * adds a new action
     * @param action an action linked to this jwidget runtime object
     */
    public void addAction(Action action){
    	
    	if(action!=null && ! actions.contains(action)){
    		
    		actions.add(action);
    	}
    }
    
    /**
     * @return the list of the actions
     */
    public List<Action> getActions() {
    	
		return actions;
	}
    
    /**
     * removes the provided action
     * @param action an action linked to this jwidget runtime object
     */
    public void removeAction(Action action){
    	
    	if(action!=null){
    		
    		actions.remove(action);
    	}
    }
    
    /**
     * registers all the listeners
     */
    public abstract void registerListeners();
    
    /**
     * unregisters all the listeners
     */
    public abstract void unregisterListeners();
    
    /**
     * @return the map associating the id of a sub component to its set of actions
     * (this method should only be called by jwidgets that register action to their sub component)
     */
    public Map<String, Set<Action>> getSubComponentIdToActionsMap(){
    	
    	Map<String, Set<Action>> map=
    		new HashMap<String, Set<Action>>();

    	String sourceId="";
    	Set<Action> actionsSet=null;
    	
    	for(Action action : actions){
    		
    		if(action!=null){
    			
    			sourceId=action.getSourceId();
    			
    			if(sourceId!=null){
    				
    				actionsSet=map.get(sourceId);
    				
    				if(actionsSet==null){
    					
    					actionsSet=new HashSet<Action>();
    					map.put(sourceId, actionsSet);
    				}
    				
    				actionsSet.add(action);
    			}
    		}
    	}
    	
    	return map;
    }
	
	/**
	 * disposes this jwidget runtime
	 */
	public void dispose() {
		
		picture.getMainDisplay().getJwidgetRuntimeManager().
			removeJWidgetRuntimeObject(this);
		dataNamesToTestTagInfo.clear();
		dataNames.clear();
	}
}
