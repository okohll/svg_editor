/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda;

import fr.itris.glips.library.*;
import fr.itris.glips.rtda.rtdp.*;
import fr.itris.glips.rtda.test.*;
import fr.itris.glips.rtda.toolkit.*;
import java.util.*;
import java.util.concurrent.*;
import fr.itris.glips.rtda.resources.*;
import fr.itris.glips.rtda.action.svg.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.config.*;

/**
 * the class of the animations handler that will animate nodes by means of the values it receives
 *  from the rtdp, the functions handler,  the blinking handler, ...
 * 
 * @author ITRIS, Jordi SUC
 */
public class AnimationsHandler{
	
	/**
	 * the keyword for the "invalid" value
	 */
	public static final String invalidKeyWord="__invalid";
	
	/**
	 * the timer thread
	 */
	public static java.util.Timer timer=new java.util.Timer(true);
	
	/**
	 * the main display
	 */
	private MainDisplay mainDisplay;
	
    /**
     * the resource manager
     */
    private RtdaResources rtdaResource=new RtdaResources();
    
	/**
	 * the set of the animations state listeners
	 */
	private Set<AnimationsStateListener> animationsStateListeners=
		new CopyOnWriteArraySet<AnimationsStateListener>();
	
	/**
	 * the set of the listeners to the requests of the display of views
	 */
	private Set<RequireViewDisplayListener> requireViewDisplayListeners=
		new CopyOnWriteArraySet<RequireViewDisplayListener>();
	
	/**
	 * the list of all the data changed listeners and actions
	 */
	private List<ListenerAction> allListenersAndActions=
		new CopyOnWriteArrayList<ListenerAction>();

	/**
	 * the map associating the name of a data to its value
	 */
	private ConcurrentHashMap<String, Object> dataMap=
		new ConcurrentHashMap<String, Object>();
	
	/**
	 * the map associating the name of a data to its type
	 */
	private ConcurrentHashMap<String, Integer> dataNameToTypeMap=
		new ConcurrentHashMap<String, Integer>();
	
	/**
	 * the map associating an insensitive case tag to a sensitive case tag
	 */
	private Map<String, String> insensitiveToSensitiveMap=
		new ConcurrentHashMap<String, String>();
	
	/**
	 * the map associating the name of a data to its value, this map 
	 * stores all the tags that are handled with theur current value
	 */
	private Map<String, Object> persistentDataMap=
		new ConcurrentHashMap<String, Object>();
	
	/**
	 * the map associating the name of a data to the set of 
	 * information objects associated with this name
	 */
	private Map<String, Set<TestTagInformation>> dataToInformationMap=
		new ConcurrentHashMap<String, Set<TestTagInformation>>();
	
	/**
	 * the map associating the name of a data to the list of 
	 * listeners or actions associated with it
	 */
	private Map<String, List<ListenerAction>> listenersActionsMap=
		new ConcurrentHashMap<String, List<ListenerAction>>();
	
	/**
	 * the map of the data providers that are used by this class, 
	 * associating the id of a data provider to this data provider
	 */
	private Map<String, RealTimeDataProvider> realTimeDataProviders=
		new ConcurrentHashMap<String, RealTimeDataProvider>();
	
	/**
	 * the rtdp simulator for the test version
	 */
	private RealTimeDataProviderTestSimulator rtdpTestSimulator;
	
	/**
	 * whether the animations handler has been paused
	 */
	private boolean isPaused=false;
	
	/**
	 * whether the animations handler has been stopped or not
	 */
	private boolean isStopped=true;
	
	/**
	 * whether we are testing
	 */
	private boolean isTestVersion=false;
	
	/**
	 * the animation thread
	 */
	private Thread animationsThread;
	
	/**
	 * the actions handler
	 */
	private ActionsHandler actionsHandler;
	
	/**
	 * the functions handler
	 */
	private FunctionsHandler functionsHandler;
	
	/**
	 * the blinkings handler
	 */
	private BlinkingsHandler blinkingsHandler;
	
	/**
	 * the transforms handler
	 */
	private TransformHandler transformHandler;
	
	/**
	 * the invalidity notifier
	 */
	private InvalidityNotifier invalidityNotifier;
	
	/**
	 * whether we are in the debug mode
	 */
	private boolean debugMode=false;
	
	/**
	 * the constructor of the class
	 * @param mainDisplay the main display
	 * @param isTestVersion whether we are testing
	 * @param debugMode whether we are in the debug mode
	 */
	public AnimationsHandler(MainDisplay mainDisplay, 
			boolean isTestVersion, boolean debugMode){

		this.mainDisplay=mainDisplay;
		this.isTestVersion=isTestVersion;
		this.debugMode=debugMode;
		
		//creating the rtdp for the test version if we are testing
		if(isTestVersion){
			
			rtdpTestSimulator=new RealTimeDataProviderTestSimulator(this);
		}
		
		//creating the actions handler
		actionsHandler=new ActionsHandler(this);
		
		//creating the functions handler
		functionsHandler=new FunctionsHandler(this);
		
		//creating the blinkings handler
		blinkingsHandler=new BlinkingsHandler(this);
		
		//creating the transforms handler
		transformHandler=new TransformHandler(this);
		
		//creating the invalidity notifier
		invalidityNotifier=new InvalidityNotifier(this);
		
		//creating the animations thread
		animationsThread=new Thread(){

			@Override
			public void run() {

				Map<String, Object> clonedDataMap=null, nameToValue=null;
				Set<ListenerAction> listenersActionsSet=new HashSet<ListenerAction>();
				Set<String> stringSet=null;
				List<ListenerAction> list=null;
				Runnable runnable=null;
				AnimationsRunnablesHandler runnablesHandler=new AnimationsRunnablesHandler();
				Object value=null;

			    while(true){

			    	if(dataMap.size()>0 && ! isPaused() && ! isStopped()){

						//cloning the data map
						synchronized (dataMap) {
							
							clonedDataMap=new HashMap<String, Object>(dataMap);//TODO
							dataMap.clear();
						}

						//clearing the animations runnables handler
						runnablesHandler.clear();

						//creating and filling the set of the listeners
						for(String name : clonedDataMap.keySet()){

							if(name!=null){
								
								list=listenersActionsMap.get(name);

								if(list!=null){
									
									listenersActionsSet.addAll(list);
								}
							}
						}

						//creating the map associating a runnable queue to the list of runnables
						for(ListenerAction listenerAction : allListenersAndActions){
							
							if(listenersActionsSet.contains(listenerAction)){
								
								nameToValue=new HashMap<String, Object>();
								stringSet=listenerAction.getDataNames();
								
								//creating the map associating the name of a tag to its new value
								for(String name : stringSet){

									if(name!=null && ! name.equals("") && 
											clonedDataMap.containsKey(name)){
										
										value=clonedDataMap.get(name);
										
										if(value.equals(invalidKeyWord)){
											
											value=null;
										}
										
										nameToValue.put(name, value);
									}
								}

								//creating the runnable returned by the method's listener and that will be 
								//executed in a runnable queue
								try{
									runnable=listenerAction.dataChanged(new DataEvent(nameToValue, null));
								}catch (Exception ex){runnable=null;ex.printStackTrace();}

								//adds the runnable
								if(runnable!=null){

									runnablesHandler.addRunnables(listenerAction.getPicture(), runnable);
								}
							}
						}

						listenersActionsSet.clear();
						list=null;
						runnable=null;
						value=null;
						clonedDataMap.clear();
						
						//executing the runnables
						runnablesHandler.execute();
						
					}else{
						
						if(isStopped()) {
							
							clonedDataMap=null;
							nameToValue=null;
							stringSet=null;
							runnablesHandler.clear();
							
							try{sleep(500);}catch(Exception ex){ex.printStackTrace();}
							
						}else {
							
							try{sleep(50);}catch(Exception ex){ex.printStackTrace();}
						}
					}
			    }
			}
		};

		animationsThread.start();
	}
	
	/**
	 * disposes the animations handler
	 */
	public void dispose(){

		//retrieves the rtdp corresponding to the data name
		for(RealTimeDataProvider dataProvider : realTimeDataProviders.values()){

			if(dataProvider!=null){
				
				dataProvider.dispose();
				break;
			}
		}
	}
	
	/**
	 * @return whether we are in debug mode
	 */
	public boolean isDebugMode() {
		return debugMode;
	}
	
	/**
	 * sets the value of a data specified by its name 
	 * (this method should be used by a rtdp object)
	 * @param name the name of a data
	 * @param value the new value for the data
	 */
	public void setData(String name, Object value){
		
		if(name!=null){
			
			name=name.toLowerCase();
		}

		if(name!=null && ! name.equals("") && 
				insensitiveToSensitiveMap.containsKey(name)){
			
			String theName=insensitiveToSensitiveMap.get(name);
			
			//handling the value of the tag//
			
			//getting the type of the tag
			Integer tagType=dataNameToTypeMap.get(theName);

			if(tagType!=null){

				switch (tagType){

					case TagToolkit.ANALOGIC_FLOAT :

						if(value==null){
	
							value=Double.NaN;
	
						}else if(! (value instanceof Double)){
	
							value=Toolkit.getNumber(value);
						}
	
						break;

					case TagToolkit.ANALOGIC_INTEGER :

						if(value==null){
	
							value=0;
	
						}else if(! (value instanceof Integer)){
	
							value=Toolkit.getIntNumber(value);
						}
	
						break;

					case TagToolkit.ENUMERATED :
					case TagToolkit.STRING :

						if(value!=null && value instanceof String){
	
							value=((String)value).toLowerCase();
	
						}else if(value==null){
	
							value=invalidKeyWord;
						}
	
						break;
				}
			}
			
			System.out.println("data="+theName+" "+value+" "+tagType);

			dataMap.put(theName, value);
			persistentDataMap.put(theName, value);
			
		}else if(isDebugMode()){
			
			System.out.println("unknown tag="+name+" "+value);
		}
	}
	
	/**
	 * returns the value corresponding to the provided tag name
	 * @param tagName a tag name
	 * @return the value corresponding to the provided tag name
	 */
	public Object getData(String tagName){
		
		Object value=null;
		
		if(tagName!=null){
			
			value=persistentDataMap.get(tagName);
			
			if(value!=null && value.equals(invalidKeyWord)){
				
				value=null;
			}
		}
		
		return value;
	}
	
	/**
	 * refreshes all the tag values
	 */
	public void refreshAllTags(){

		if(isTestVersion){

			dataMap.putAll(persistentDataMap);
		}
	}
	
	/**
     * loads the view denoted by the given path
     * @param path a view path
     * @param serverIPAddress the IP address of the server
     */
    public void loadView(String path, String serverIPAddress) {

    	if(! isTestVersion) {
    		
    		RealTimeDataProvider rtdp=getRealTimeDataProvider(serverIPAddress);
    		
    		if(rtdp!=null) {
    			
    			rtdp.loadView(path);
    		}
    	}
    }
    
    /**
     * unloads the view denoted by the given path
     * @param path a view path
     * @param serverIPAddress the IP address of the server
     */
    public void unLoadView(String path, String serverIPAddress) {

    	if(! isTestVersion) {
    		
    		RealTimeDataProvider rtdp=getRealTimeDataProvider(serverIPAddress);
    		
    		if(rtdp!=null) {
    			
    			rtdp.unLoadView(path);
    		}
    	}
    }
	
	/**
	 * registers a listener to a data modification or and action
	 * @param serverIPAddress the IP address of the server
	 * @param listenerOrAction a listener or an action
	 */
	public void registerListenerOrAction(
		String serverIPAddress, final ListenerAction listenerOrAction) {
		
		if(listenerOrAction!=null){
			
			allListenersAndActions.add(listenerOrAction);
			
			//getting the configuration document for the listener or action
			ConfigurationDocument configDoc=mainDisplay.getPictureManager().
				getConfigurationDocument(listenerOrAction.getPicture());
			
			List<ListenerAction> list=null;
			int initialSize=listenersActionsMap.size();
			int type=-1;

			for(String name : listenerOrAction.getDataNames()){

				if(name!=null && ! name.equals("")){
					
					//adds the listener or action to the listeners or actions map
					list=listenersActionsMap.get(name);

					if(list==null){
						
						list=new CopyOnWriteArrayList<ListenerAction>();
						insensitiveToSensitiveMap.put(name.toLowerCase(), name);
						
						//getting the type of the tag
						type=configDoc.getTagType(name);
						
						dataNameToTypeMap.put(name, type);
					}
					
					list.add(listenerOrAction);
					listenersActionsMap.put(name, list);
					addDataToListen(serverIPAddress, name);
				}
			}
			
			if(listenerOrAction instanceof DataChangedListener) {
				
				DataChangedListener listener=(DataChangedListener)listenerOrAction;
				
				//if a tag, that is handled by this listener or action, is 
				//described by a function, the listener is added to the list
				//of the listeners that will be used to compute the value of the tag
				if(listener.useFunction()){
					
					functionsHandler.addFunctionTagListener(listener);
				}
				
				//adding the listener to the transform handler
				if(listener.isTransformAnimation()){
				    
					transformHandler.addDataChangedListener(listener);
				}

				//adding the blinking value modifiers
				Collection<BlinkingValueModifier> blinkingValueModifiers=
					listener.getBlinkingValueModifiers();

				if(blinkingValueModifiers!=null && blinkingValueModifiers.size()>0){
					
					blinkingsHandler.addBlinkingValueModifiers(blinkingValueModifiers);
				}
				
			}else if(listenerOrAction instanceof Action && 
				((Action)listenerOrAction).getEventsManager() instanceof SVGEventsManager) {
				
				actionsHandler.addAction((Action)listenerOrAction);
			}

			if(isTestVersion){
				
				//adds the information objects linked with the given listener or action
				Map<String, TestTagInformation> dataNamesToInfo=
								listenerOrAction.getDataNamesToInformation();
				TestTagInformation info=null;
				Set<TestTagInformation> infoSet=null;
				
				for(String name : dataNamesToInfo.keySet()){
					
					if(name!=null && ! name.equals("")){
						
						info=dataNamesToInfo.get(name);
						
						if(info!=null){
							
							infoSet=dataToInformationMap.get(name);
							
							if(infoSet==null){
								
								infoSet=new CopyOnWriteArraySet<TestTagInformation>();
								dataToInformationMap.put(name, infoSet);
							}
							
							infoSet.add(info);
						}
					}
				}
			}

			//notifies that animations have been added to be handled
			if(initialSize==0 && (isPaused || isStopped)){

				notifyAnimationsExistBeforeStarting();
			}
		}
	}
	
	/**
	 * unregisters a listener to a data modification or and action
	 * @param serverIPAddress the IP address of the server
	 * @param listenerOrAction a listener or an action
	 */
	public void unregistersListenerOrAction(String serverIPAddress, 
			final ListenerAction listenerOrAction) {
		
		if(listenerOrAction!=null){
			
			allListenersAndActions.remove(listenerOrAction);

			List<ListenerAction> list=null;
			int initialSize=listenersActionsMap.size();
			
			for(String name : listenerOrAction.getDataNames()){

				if(name!=null && ! name.equals("")){
					
					//adds the listener to the listeners map
					list=listenersActionsMap.get(name);
					
					if(list!=null){
						
						list.remove(listenerOrAction);
						
						if(list.size()==0){
							
							listenersActionsMap.remove(name);
							
							if(! isTestVersion){
								
								persistentDataMap.remove(name);
							}

							insensitiveToSensitiveMap.remove(name);
							dataNameToTypeMap.remove(name);
							removeDataToListen(serverIPAddress, name);
						}
					}
				}
			}
			
			if(listenerOrAction instanceof DataChangedListener) {
				
				DataChangedListener listener=(DataChangedListener)listenerOrAction;
				
				//if a tag, that is handled by this listener, is described by a 
				//function, the listener is removed from the list
				if(listener.useFunction()){
					
					functionsHandler.removeFunctionTagListener(listener);
				}
				
				//removing the listener to the transform handler
				if(listener.isTransformAnimation()){
				    
					transformHandler.removeDataChangedListener(listener);
				}
				
				//removing the blinking value modifiers
				blinkingsHandler.removeBlinkingValueModifiers(listener.getBlinkingValueModifiers());
				
			}else if(	listenerOrAction instanceof Action && 
							((Action)listenerOrAction).getActionComponent() instanceof SVGCanvas) {
				
				actionsHandler.removeAction((Action)listenerOrAction);
			}

			if(isTestVersion){
				
				//removes the information objects linked with the given listener
				Map<String, TestTagInformation> dataNamesToInfo=
					listenerOrAction.getDataNamesToInformation();
				TestTagInformation info=null;
				Set<TestTagInformation> testTagInfoSet=null;
				
				for(String name : dataNamesToInfo.keySet()){

					if(name!=null && ! name.equals("")){
						
						//getting the info object corresponding to the name
						info=dataNamesToInfo.get(name);
						
						if(info!=null){
							
							testTagInfoSet=dataToInformationMap.get(name);
							
							if(testTagInfoSet!=null){
								
								testTagInfoSet.remove(info);
								
								if(testTagInfoSet.size()==0){
									
									//removing the entry from the map
									dataToInformationMap.remove(name);
								}
							}
						}
					}
				}
			}
			
			//notifies that no more animations are handled
			if(listenersActionsMap.size()==0 && initialSize>0){
				
				notifyNoMoreAnimations();
			}
		}
	}
	
	/**
	 * reinitializes the handler
	 */
	public synchronized void reinitialize() {

		//clearing the data objects
		allListenersAndActions.clear();
		dataMap.clear();
		dataNameToTypeMap.clear();
		
		if(! isTestVersion){
			
			persistentDataMap.clear();
		}

		dataToInformationMap.clear();
		listenersActionsMap.clear();
		insensitiveToSensitiveMap.clear();
		
		if(isTestVersion){
			
			rtdpTestSimulator.reinitialize();
		}
	}
	
	/**
	 * sets the value for the given data name
	 * @param serverIPAddress the IP address of the server
	 * @param dataName the name of a data
	 * @param value the value for the data
	 */
	public void setDataValue(String serverIPAddress, String dataName, Object value){

		if(dataName!=null && ! dataName.equals("")){
			
			RealTimeDataProvider dataProvider=getRealTimeDataProvider(serverIPAddress);
			
			if(dataProvider!=null){

				dataProvider.setDataValue(dataName, value);
			}
		}
	}
	
	/**
	 * returns the data provider linked with the given name
	 * @param id the id of a data provider
	 * @return the data provider linked with the given name
	 */
	protected RealTimeDataProvider getRealTimeDataProvider(String id) {
		
		RealTimeDataProvider realTimeDataProvider=realTimeDataProviders.get(id);

		//if no rtdp has been found, a test simulator or a regular simulator is used
		if(realTimeDataProvider==null){
			
			if(isTestVersion){
				
				realTimeDataProvider=rtdpTestSimulator;
				
			}else{
				
				//creating the new data provider
				realTimeDataProvider=new ServerRealTimeDataProvider(this, id);
				realTimeDataProviders.put(id, realTimeDataProvider);
			}
		}

		return realTimeDataProvider;
	}
	
	/**
	 * notifies the accurate data provider to refresh the value of the data specified by the given name
	 * @param serverIPAddress the IP address of the server
	 * @param name the name of a data
	 */
	protected void addDataToListen(String serverIPAddress, String name){

		if(name!=null && ! name.equals("")){
			
			RealTimeDataProvider dataProvider=getRealTimeDataProvider(serverIPAddress);
			
			if(dataProvider!=null){

				dataProvider.addDataToListen(name);
			}
		}
	}
	
	/**
	 * notifies the accurate data provider to refresh the value of the data specified by the given name
	 * @param serverIPAddress the IP address of the server
	 * @param name the name of a data
	 */
	protected void removeDataToListen(String serverIPAddress, String name){
		
		if(name!=null && ! name.equals("")){
			
			RealTimeDataProvider dataProvider=getRealTimeDataProvider(serverIPAddress);
			
			if(dataProvider!=null){
				
				dataProvider.removeDataToListen(name);
			}
		}
	}
	
	/**
	 * returns an information object about the tag given by its name
	 * @param dataName the name of a tag
	 * @return an information object about the tag given by its name
	 */
	public TestTagInformation getDataInformation(String dataName){
		
		if(dataName!=null && dataName.equals("")){
			
			Set<TestTagInformation> set=dataToInformationMap.get(dataName);
			
			if(set!=null && set.size()>0){

				return set.iterator().next();
			}
		}
		
		return null;
	}
	
	/**
	 * returns the type of the tag denoted by the provided tag name
	 * @param name a tag name
	 * @return the type of the tag
	 */
	public int getTagType(String name){
		
		Integer tagType=dataNameToTypeMap.get(name);
		
		if(tagType==null){
			
			tagType=-1;
		}
		
		return tagType;
	}
	
	/**
	 * starts the animations
	 */
	public void start(){
		
	    if(isStopped && ! isPaused){
			
			if(AnimationsHandler.this.isTestVersion){
    			
    			rtdpTestSimulator.setIsStarted(true);
    		}
    		
    		//starting the other rtdps
    		for(RealTimeDataProvider rtdp : realTimeDataProviders.values()){
    			
    			rtdp.setIsStarted(true);
    		}
    		
    		//launches the animations
			setIsStopped(false);
			setIsPaused(false);

    		notifyAnimationsStarted();
	    }
	}
	
	/**
	 * pauses the animations
	 */
	public void pause(){
		
		setIsPaused(true);
		
		//stops the simulator rtdps
		if(isTestVersion){
			
			rtdpTestSimulator.setIsStarted(false);
		}
		
		notifyAnimationsPaused();
	}
	
	/**
	 * resumes the animations
	 */
	public void resume(){
		
		setIsPaused(false);
		
		//starts the simulator rtdps
		if(isTestVersion){
			
			rtdpTestSimulator.setIsStarted(true);
		}

		notifyAnimationsResumed();
	}
	
	/**
	 * stops the animations
	 */
	public void stop(){
		
		setIsPaused(false);
	    setIsStopped(true); 

		//stops the simulator rtdp
		if(isTestVersion){
			
			rtdpTestSimulator.setIsStarted(false);
		}

		notifyAnimationsStopped();
	}
	
	/**
	 * sets that the animations are paused or not
	 * @param isPaused whether the animations are paused or not
	 */
	protected synchronized void setIsPaused(boolean isPaused){
		
		this.isPaused=isPaused;
	}
	
	/**
	 * sets that the animations are stopped or not
	 * @param isStopped whether the animations are stopped or not
	 */
	protected synchronized void setIsStopped(boolean isStopped){
		
		this.isStopped=isStopped;
	}

	/**
	 * @return whether the animations are stopped
	 */
	public boolean isStopped(){
		
		return isStopped;
	}
	
	/**
	 * @return whether the animations are paused
	 */
	public boolean isPaused(){
		
		return isPaused;
	}
	
	/**
	 * adds an animations state listener
	 * @param listener an animations state listener
	 */
	public void addAnimationsStateListener(AnimationsStateListener listener){
		
		if(listener!=null){
			
			animationsStateListeners.add(listener);
		}
	}
	
	/**
	 * removes an animations state listener
	 * @param listener an animations state listener
	 */
	public void removeAnimationsStateListener(AnimationsStateListener listener){
		
		if(listener!=null){
			
			animationsStateListeners.remove(listener);
		}
	}
	
	/**
	 * adds a require view display listener
	 * @param listener an animations state listener
	 */
	public void addRequireViewDisplayListener(RequireViewDisplayListener listener){
		
		if(listener!=null){
			
			requireViewDisplayListeners.add(listener);
		}
	}
	
	/**
	 * removes an animations state listener
	 * @param listener an animations state listener
	 */
	public void removeRequireViewDisplayListener(RequireViewDisplayListener listener){
		
		if(listener!=null){
			
		    requireViewDisplayListeners.remove(listener);
		}
	}
	
	/**
	 * notifies that a request to display a view has been done
	 * @param viewBrowserId the id of the view browser
	 * @param viewPath the path of the view to be displayed
	 */
	public void fireViewDisplayRequest(String viewBrowserId, String viewPath){

	    if(viewPath!=null && ! viewPath.equals("")){

			for(RequireViewDisplayListener listener : requireViewDisplayListeners){

				if(listener!=null){

					listener.displayView(viewBrowserId, viewPath);
				}
			}
	    }
	}
	
	/**
	 * notifies that a request to display the last displayed view before the current one has been done
	 * @param viewBrowserId the id of the view browser
	 */
	public void fireReturnToLastViewRequest(String viewBrowserId){

		for(RequireViewDisplayListener listener : requireViewDisplayListeners){

			if(listener!=null){

				listener.displayPreviousView(viewBrowserId);
			}
		}
	}
	
	/**
	 * notifies that a request to exit the program has been done
	 */
	public void fireQuitProgramRequest(){
	    
	    stop();

		for(RequireViewDisplayListener listener : requireViewDisplayListeners){

			if(listener!=null){
				
				listener.quitProgram();
			}
		}
	}
	
	/**
	 * notifies that data changed listeners have been added to the animations handler
	 */
	protected void notifyAnimationsExistBeforeStarting(){

		for(AnimationsStateListener listener : animationsStateListeners){

			if(listener!=null){
				
				listener.animationsExistBeforeStarting();
			}
		}
	}
	
	/**
	 * notifies that no more data changed listeners exist
	 */
	protected void notifyNoMoreAnimations(){

		for(AnimationsStateListener listener : animationsStateListeners){
			
			if(listener!=null){
				
				listener.noMoreAnimations();
			}
		}
	}
	
	/**
	 * notifies the start of the animations
	 */
	protected void notifyAnimationsStarted(){

		for(AnimationsStateListener listener : animationsStateListeners){

			if(listener!=null){
				
				listener.animationsStarted();
			}
		}
	}
	
	/**
	 * notifies the pause of the animations
	 */
	protected void notifyAnimationsPaused(){

		for(AnimationsStateListener listener : animationsStateListeners){

			if(listener!=null){
				
				listener.animationsPaused();
			}
		}
	}
	
	/**
	 * notifies that animations have been resumed
	 */
	protected void notifyAnimationsResumed(){

		for(AnimationsStateListener listener : animationsStateListeners){

			if(listener!=null){
				
				listener.animationsResumed();
			}
		}
	}
	
	/**
	 * notifies that animations have been stopped
	 */
	protected void notifyAnimationsStopped(){

		for(AnimationsStateListener listener : animationsStateListeners){

			if(listener!=null){
				
				listener.animationsStopped();
			}
		}
	}
	
	/**
	 * @return Returns the rtdpTestSimulator.
	 */
	public RealTimeDataProviderTestSimulator getRtdpTestSimulator() {
		return rtdpTestSimulator;
	}
	
	
	/**
	 * @return Returns the dataToInformationMap.
	 */
	public Map<String, Set<TestTagInformation>> getDataToInformationMap() {
		
		return dataToInformationMap;
	}
	
	/**
	 * @return Returns the blinkingsHandler.
	 */
	public BlinkingsHandler getBlinkingsHandler() {
		return blinkingsHandler;
	}
	
	/**
	 * @return Returns the functionsHandler.
	 */
	public FunctionsHandler getFunctionsHandler() {
		return functionsHandler;
	}
    
    /**
     * @return Returns the transformHandler.
     */
    public TransformHandler getTransformHandler() {
        return transformHandler;
    }

    /**
     * @return Returns the rtdaResource.
     */
    public RtdaResources getRtdaResource() {
        return rtdaResource;
    }

	/**
	 * @return Returns the isTestVersion.
	 */
	public boolean isTestVersion() {
		return isTestVersion;
	}

	/**
	 * @return the actionsHandler
	 */
	public ActionsHandler getActionsHandler() {
		return actionsHandler;
	}

	/**
	 * @return the invalidityNotifier
	 */
	public InvalidityNotifier getInvalidityNotifier() {
		return invalidityNotifier;
	}

	/**
	 * @return the timer
	 */
	public static java.util.Timer getTimer() {
		return timer;
	}
	
	/**
	 * the class of the animations runnables
	 * @author ITRIS, Jordi SUC
	 */
	class AnimationsRunnable implements Runnable{
		
		/**
		 * the list of the runnables to be executed
		 */
		private LinkedList<Runnable> runnables=new LinkedList<Runnable>();

		/**
		 * executes the runnables
		 */
		public void run() {

			for(Runnable runnable : runnables){

				runnable.run();
			}
		}

		/**
		 * adds a new runnable to the set of runnables
		 * @param runnable a runnable
		 */
		public void addRunnable(Runnable runnable){
			
			runnables.add(runnable);
		}
		
		/**
		 * @return the size of the list of the runnables
		 */
		public int size(){
			
			return runnables.size();
		}
		
		/**
		 * clears this animations runnables
		 */
		public void clear(){

			runnables.clear();
		}
	}
	
	/**
	 * the class of the handler of the animations runnables
	 * @author ITRIS, Jordi SUC
	 */
	class AnimationsRunnablesHandler{
		
		/**
		 * the set of the animations runnables
		 */
		private HashMap<SVGPicture, AnimationsRunnable> animationsRunnables=
			new HashMap<SVGPicture, AnimationsRunnable>();
		
		/**
		 * the constructor of the class
		 */
		public AnimationsRunnablesHandler(){}
		
		/**
		 * adds a new runnable
		 * @param picture a svg picture
		 * @param runnable a runnable
		 */
		public void addRunnables(SVGPicture picture, Runnable runnable){
			
			if(picture!=null) {
				
				AnimationsRunnable animationsRunnable=animationsRunnables.get(picture);
				
				if(animationsRunnable==null){
					
					animationsRunnable=new AnimationsRunnable();
					animationsRunnables.put(picture, animationsRunnable);
				}
				
				animationsRunnable.addRunnable(runnable);
			}
		}
		
		/**
		 * executes the runnable
		 */
		public void execute(){
			
			AnimationsRunnable animationsRunnable=null;

			for(SVGPicture picture : animationsRunnables.keySet()){
				
				animationsRunnable=animationsRunnables.get(picture);
				
				if(picture!=null) {

					try{
						picture.enqueue(animationsRunnable, true);
					}catch (Exception ex) {ex.printStackTrace();}
				}
			}
		}
		
		/**
		 * clears this handler
		 */
		public void clear(){
			
			//the set of the svg pictures to remove
			HashSet<SVGPicture> pictures=new HashSet<SVGPicture>();
			AnimationsRunnable animationsRunnable=null;

			//for each picture, checks if it is used, and then clears its associated animations runnable
			for(SVGPicture picture : animationsRunnables.keySet()){
				
				animationsRunnable=animationsRunnables.get(picture);
				
				if(animationsRunnable.size()<=0){
					
					pictures.add(picture);
					
				}else{
					
					animationsRunnable.clear();
				}
			}
			
			//removing the unused pictures
			for(SVGPicture picture : pictures){
				
				animationsRunnables.remove(picture);
			}
			
			animationsRunnables.clear();
			pictures.clear();
		}
	}
}
