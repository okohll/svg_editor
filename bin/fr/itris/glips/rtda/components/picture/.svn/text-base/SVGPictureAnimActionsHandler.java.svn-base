package fr.itris.glips.rtda.components.picture;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.config.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the class handling the animations and 
 * actions and the jwidgets for a svg picture
 * 
 * @author Jordi SUC
 */
public class SVGPictureAnimActionsHandler {

	/**
	 * the name of the attribute allowing to specify the name of the server IP address
	 */
	public static String serverAddressName="serverIPAddress";
	
	/**
	 * the svg picture to which the animations and actions handler is associated
	 */
	private SVGPicture picture;
	
	/**
	 * the file of the project into which the svg file is inserted
	 */
	private File projectFile;

	/**
	 * the path of the view in the xml database
	 */
	private String viewPath;
	
	/**
	 * the right level for this picture
	 */
	private int rightLevel=0;
	
	/**
	 * the IP address of the server that should update tags
	 */
	private String serverIPAddress="";
	
	/**
	 * whether the animations have been added
	 */
	private boolean animationsAdded=false;
	
	/**
	 * the data changed listeners
	 */
	protected final List<DataChangedListener> dataChangedListeners=
		new CopyOnWriteArrayList<DataChangedListener>();
	
	/**
	 * the jwidget animations
	 */
	protected final List<JWidgetAnimation> jwidgetAnimations=
		new CopyOnWriteArrayList<JWidgetAnimation>();
	
	/**
	 * the actions
	 */
	protected final List<fr.itris.glips.rtda.animaction.Action> actions=
		new CopyOnWriteArrayList<fr.itris.glips.rtda.animaction.Action>();
	
	/**
	 * the map associating an element to a jwidget runtime object
	 */
	protected final Map<Element, JWidgetRuntime> elementToJWidgetRuntime=
		new ConcurrentHashMap<Element, JWidgetRuntime>();
	
	/**
	 * the list of blinking value modifiers, that handle the blinking colors
	 * in the canvas that are not inside an animation node
	 */
	protected final List<BlinkingValueModifier> blinkingValueModifiers=
		new CopyOnWriteArrayList<BlinkingValueModifier>();
	
	/**
	 * the constructor of the class
	 * @param picture a svg picture
	 */
	public SVGPictureAnimActionsHandler(SVGPicture picture){
		
		this.picture=picture;
		
		//setting the project file
		try{
			projectFile=AnimationsToolkit.getProjectFile(new URI(picture.getUri()), false);
		}catch (Exception ex){ex.printStackTrace();}
	}
	
	/**
	 * initializes the animations and actions handler
	 */
	protected void initialize(){
		
		//getting the configuration document corresponding to this picture
		ConfigurationDocument confDoc=
			picture.mainDisplay.getPictureManager().getConfigurationDocument(picture);
		
		if(confDoc!=null){
			
			rightLevel=confDoc.getAuthorizationLevelForView(viewPath);
			
			//getting the IP address of the server that should update tags
			serverIPAddress=confDoc.getRootElement().getAttribute("serverIPAddress");
		}
		
		//getting the view path of this view
		viewPath=picture.mainDisplay.getPictureManager().
			getViewXMLPath(getProjectName(), picture.uri);
	}
	
	/**
	 * builds the animations, the actions and the jwidgets linked to the canvas
	 */
	public void buildAnimActionsJwidgets(){

		Document document=picture.doc;

		if(document!=null){
			
			DataChangedListener dataChangedListener=null;
			fr.itris.glips.rtda.animaction.Action action=null;
			Element cur=null;
			Node node=null;

			//for each node of the svg file, tests if it an animation node, 
			//and if this is such a case, creates a new data changed listener, an action or a jwidget
			for(NodeIterator it=new NodeIterator(
					document.getDocumentElement()); it.hasNext();){
				
				node=it.next();
				
				if(node!=null && node instanceof Element) {
					
					cur=(Element)node;
					
					if(cur.getNodeName().startsWith(AnimationsToolkit.rtdaPrefix)){

						if(cur.getNodeName().equals(JWidgetRuntime.jwidgetTagName)) {

							createJWidgetRuntime(cur);

						}else if(! elementToJWidgetRuntime.containsKey(cur.getParentNode())) {

							if(ListenerActionBuilder.isAnimationNodeSupported(cur.getNodeName())){
								
								//getting the listener
								dataChangedListener=ListenerActionBuilder.
									createDataChangedListener(picture, cur);
								
								if(dataChangedListener!=null){

									dataChangedListeners.add(dataChangedListener);
								}
								
							}else if(ListenerActionBuilder.isActionNode(cur.getNodeName())) {
								
								action=ListenerActionBuilder.createAction(
										picture, getProjectName(),	picture.getCanvas(), cur.getParentNode(), cur);

								if(action!=null) {

									actions.add(action);
								}
							}
							
						}else {

							createJWidgetAnimActions(cur);
						}
						
					}else{
						
						//creating the blinking value modifiers that handle the blinking colors
						//in the canvas that are not inside an animation node
						blinkingValueModifiers.addAll(
							picture.mainDisplay.getColorsAndBlinkingsToolkit().
								getNotInAnimationsNodeBlinkingValueModifiers(
									cur, picture, getProjectFile()));
					}
				}
			}
		}
	}
	
	/**
	 * creates the jwidget runtime associated to and element
	 * @param jwidgetElement a jwidget element
	 */
	protected void createJWidgetRuntime(Element jwidgetElement){

		//creating the jwidget runtime object
		JWidgetRuntime jwidgetRuntime=picture.mainDisplay.getJwidgetRuntimeManager().
			createJWidgetRuntime(picture, jwidgetElement);

		if(jwidgetRuntime!=null) {
			
			elementToJWidgetRuntime.put(
					(Element)jwidgetElement.getParentNode(), jwidgetRuntime);
			
			jwidgetRuntime.initialize();
		}
	}
	
	/**
	 * creates the animation or the action of a jwidget 
	 * corresponding to the provided element
	 * @param element an animation or action element
	 */
	protected void createJWidgetAnimActions(Element element){
		
		//as the action or the animation belongs to a jwidget runtime object, the corresponding
		//jwidget runtime object is retrieved
		JWidgetRuntime jwidgetRuntime=
			elementToJWidgetRuntime.get(element.getParentNode());
		
		if(jwidgetRuntime!=null) {
			
			//checking if the node matches an animation
			JWidgetAnimation anim=JWidgetRuntimeManager.
				createAnimation(jwidgetRuntime, element);
			Action action=null;
			
			if(anim!=null){

				jwidgetAnimations.add(anim);
				
			}else{
				
				//the action is created
				action=JWidgetRuntimeManager.createAction(jwidgetRuntime, element);
				
				if(action!=null) {

					actions.add(action);
				}
			}
		}
	}
	
	/**
	 * destroys the animations listeners of the canvas
	 */
	public void destroyAnimations(){

		//disposing the animations
		for(ListenerAction anim : dataChangedListeners){
			
			if(anim!=null){
				
				anim.dispose();
			}
		}

		for(JWidgetAnimation anim : jwidgetAnimations){
			
			anim.dispose();
		}
		
		//disposing the actions
		for(Action action : actions){
			
			action.dispose();
		}
		
		//clearing the collections and maps
		dataChangedListeners.clear();
		jwidgetAnimations.clear();
		actions.clear();
		elementToJWidgetRuntime.clear();
		blinkingValueModifiers.clear();
		
		//removes the colors and blinkings maps associated with the canvas
		picture.mainDisplay.getColorsAndBlinkingsToolkit().removeColorsAndBlinkings(
				getProjectName());
		ActionsLoader.removeClasses(getProjectName());
	}
	
	/**
	 * initializes the animations and actions once the canvas is displayed 
	 * and before calls to the <code>dataChanged(DataEvent evt)</code> method
	 */
	protected void initAnimActionsOnceCanvasVisible() {
		
		//handling the jwidgets
		for(JWidgetRuntime jwidgetRuntime : elementToJWidgetRuntime.values()){
			
			if(jwidgetRuntime!=null){
				
				jwidgetRuntime.initializeWhenCanvasDisplayed();
			}
		}

		//handling the animations
		for(ListenerAction listener : dataChangedListeners) {
			
			if(listener!=null) {

				listener.initializeWhenCanvasDisplayed();
			}
		}
		
		//handling the jwidget animations
		for(JWidgetAnimation anim : jwidgetAnimations) {
			
			if(anim!=null) {

				anim.initializeWhenCanvasDisplayed();
			}
		}
		
		//handling the actions
		for(fr.itris.glips.rtda.animaction.Action action : actions) {
			
			if(action!=null) {

				action.initializeWhenCanvasDisplayed();
			}
		}
	}
	
	/**
	 * registers the animations and actions in the animations handler
	 */
	protected void addAnimationsActions() {
		
		if(! animationsAdded){

			for(JWidgetRuntime jwidgetRuntime : elementToJWidgetRuntime.values()){
				
				if(jwidgetRuntime!=null && jwidgetRuntime.requiresListening()){
					
					picture.mainDisplay.getAnimationsHandler().registerListenerOrAction(
							serverIPAddress, jwidgetRuntime);
				}
			}

			//adding the data changed listeners
			for(ListenerAction listener : dataChangedListeners) {
				
				if(listener!=null) {

					picture.mainDisplay.getAnimationsHandler().registerListenerOrAction(
							serverIPAddress, listener);
				}
			}
			
			//adding the jwidget animations
			for(JWidgetAnimation anim : jwidgetAnimations) {
				
				if(anim!=null) {

					picture.mainDisplay.getAnimationsHandler().registerListenerOrAction(
						serverIPAddress, anim);
				}
			}
			
			//adding the actions
			for(fr.itris.glips.rtda.animaction.Action action : actions) {
				
				if(action!=null) {

					picture.mainDisplay.getAnimationsHandler().registerListenerOrAction(
						serverIPAddress, action);
				}
			}
			
			//adding the blinking value modifiers to the blinkings handler
			picture.mainDisplay.getAnimationsHandler().getBlinkingsHandler().
				addBlinkingValueModifiers(blinkingValueModifiers);
			
			//loading this view//
			//getting the project name corresponding to the picture
			String projectName=getProjectName();

			if(projectName!=null && ! projectName.equals("")) {

				picture.mainDisplay.getAnimationsHandler().loadView(viewPath, serverIPAddress);
			}
			
			if(picture.getMainDisplay().getAnimationsHandler().isTestVersion()){
				
				//refreshing all the tag values
				picture.mainDisplay.getAnimationsHandler().refreshAllTags();
			}

			//refreshing the state of the jwidget runtime objects
			for(JWidgetRuntime jwidgetRuntime : elementToJWidgetRuntime.values()){
				
				if(jwidgetRuntime!=null){
					
					jwidgetRuntime.refreshComponentState();
				}
			}

			synchronized (this) {
				animationsAdded=true;
			}
		}
	}
	
	/**
	 * unregisters the animations and actions from the animations handler
	 */
	public void unregistersAnimationsActions() {
		
		if(animationsAdded){
			
			//unloading this view//
			//getting the project name corresponding to the picture
			String projectName=getProjectName();

			if(projectName!=null && ! projectName.equals("")) {

				picture.mainDisplay.getAnimationsHandler().unLoadView(
						viewPath, serverIPAddress);
			}
			
			//removing the jwidget runtime objects
			for(JWidgetRuntime jwidgetRuntime : elementToJWidgetRuntime.values()){
				
				if(jwidgetRuntime!=null && jwidgetRuntime.requiresListening()){
					
					picture.mainDisplay.getAnimationsHandler().unregistersListenerOrAction(
							serverIPAddress, jwidgetRuntime);
				}
			}

			//removing the data changed listeners
			for(DataChangedListener listener : dataChangedListeners){
				
				if(listener!=null){
					
					picture.mainDisplay.getAnimationsHandler().
						unregistersListenerOrAction(serverIPAddress, listener);
			    	picture.getMainDisplay().getAnimationsHandler().
						getInvalidityNotifier().unregisterListener(listener);
				}
			}

			//removing the jwidget animation
			for(JWidgetAnimation anim : jwidgetAnimations) {
				
				if(anim!=null) {
					
					picture.mainDisplay.getAnimationsHandler().
						unregistersListenerOrAction(serverIPAddress, anim);
				}
			}
			
			//removing the actions
			for(fr.itris.glips.rtda.animaction.Action action : actions) {
				
				if(action!=null) {
					
					picture.mainDisplay.getAnimationsHandler().
						unregistersListenerOrAction(serverIPAddress, action);
				}
			}
			
			//removing the blinking value modifiers
			picture.mainDisplay.getAnimationsHandler().getBlinkingsHandler().
				removeBlinkingValueModifiers(blinkingValueModifiers);
			
			//refreshing the state of the jwidget runtime objects
			for(JWidgetRuntime jwidgetRuntime : elementToJWidgetRuntime.values()){
				
				if(jwidgetRuntime!=null){
					
					jwidgetRuntime.refreshComponentState();
				}
			}

			synchronized (this) {
				animationsAdded=false;
			}
		}
	}
	
	/**
	 * @return whether the picture can be displayed
	 */
	public boolean isEntitled(){

		return (picture.mainDisplay.getUserRights().
			getRightForViewLoading()>=rightLevel);
	}
	
	/**
	 * @return the ip address of the server
	 */
	public String getServerIPAddress() {
		return serverIPAddress;
	}
	
    /**
     * @return the project name
     */
    public String getProjectName() {
    	
    	return Toolkit.getFileName(projectFile);
    }
    
    /**
     * @return the project file
     */
    public File getProjectFile() {
    	
		return projectFile;
	}
    
    /**
     * @return the map associating an element to a jwidget runtime
     */
    public Map<Element, JWidgetRuntime> getElementToJWidgetRuntime() {

		return elementToJWidgetRuntime;
	}
    
    /**
     * disposes this handler
     */
    public void dispose(){
    	
		//removing the animations
		unregistersAnimationsActions();
		
		//destroying the jwidget runtime objects
		for(JWidgetRuntime jwidgetRuntime : elementToJWidgetRuntime.values()) {
			
			jwidgetRuntime.dispose();
			picture.mainDisplay.getJwidgetRuntimeManager().
				removeJWidgetRuntimeObject(jwidgetRuntime);
		}
		
		//destroying the animations
		destroyAnimations();
    }
    
    /**
     * @return the view path
     */
    public String getViewPath() {
		return viewPath;
	}
}
