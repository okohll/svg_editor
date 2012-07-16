package fr.itris.glips.rtda.components.viewbrowser;

import java.util.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class of the view browsers manager
 * @author ITRIS, Jordi SUC
 */
public class ViewBrowsersManager {
	
	/**
	 * the map associating the id of a view browser to this view browser
	 */
	private Map<String, ViewBrowser> idToViewBrowsers=new HashMap<String, ViewBrowser>();
	
	/**
	 * the map associating the id of a view browser to the set of the loaded view listeners 
	 */
	private Map<String, Set<ViewLoadedListener>> loadedViewListeners=
			new HashMap<String, Set<ViewLoadedListener>>();
	
	/**
	 * the main display
	 */
	protected MainDisplay mainDisplay;
	
	/**
	 * the constructor of the class
	 * @param mainDisplay the main display
	 */
	public ViewBrowsersManager(MainDisplay mainDisplay){
		
		this.mainDisplay=mainDisplay;
	}
	
	/**
	 * registers the given view browser
	 * @param viewBrowser a view browser
	 */
	public void registerViewBrowser(ViewBrowser viewBrowser) {
		
		if(viewBrowser!=null) {
			
			idToViewBrowsers.put(viewBrowser.getViewBrowserId(), viewBrowser);
		}
	}
	
	/**
	 * adds a new view loaded listener
	 * @param viewBrowserId the id of a view browser
	 * @param listener a view loaded listener
	 */
	public void addViewLoadedListener(String viewBrowserId, ViewLoadedListener listener){
		
		if(viewBrowserId!=null && listener!=null){
			
			Set<ViewLoadedListener> listeners=loadedViewListeners.get(viewBrowserId);
			
			if(listeners==null){
				
				listeners=new HashSet<ViewLoadedListener>();
				loadedViewListeners.put(viewBrowserId, listeners);
			}
			
			listeners.add(listener);
		}
	}
	
	/**
	 * removes the given view loaded listener
	 * @param listener a view loaded listener
	 */
	public void removeViewLoadedListener(ViewLoadedListener listener){
		
		if(listener!=null){
			
			Set<ViewLoadedListener> listeners=null;
			
			for(String id : loadedViewListeners.keySet()){
				
				listeners=loadedViewListeners.get(id);
				
				if(listeners!=null){
					
					listeners.remove(listener);
				}
			}
		}
	}
	
	/**
	 * fires that a view has been loaded in the view browser denoted by the given id
	 * @param projectName the name of a project
	 * @param viewBrowserId the id of a view browser
	 * @param uri the uri of a view
	 */
	public void fireViewLoaded(String projectName, String viewBrowserId, String uri){
		
		//getting the xml path of the view corresponding to the given uri
		String xmlViewPath=mainDisplay.getPictureManager().getViewXMLPath(projectName, uri);
		
		if(viewBrowserId!=null && xmlViewPath!=null && ! xmlViewPath.equals("")){
			
			//getting the set of the listeners corresponding to the given view browser id
			Set<ViewLoadedListener> listeners=loadedViewListeners.get(viewBrowserId);
			
			if(listeners!=null){
				
				for(ViewLoadedListener listener : listeners){
					
					listener.viewLoaded(xmlViewPath);
				}
			}
		}
	}
	
	/**
	 * unregisters the given view browser
	 * @param viewBrowser a view browser
	 */
	public void unregisterViewBrowser(ViewBrowser viewBrowser) {
		
		if(viewBrowser!=null) {
			
			idToViewBrowsers.remove(viewBrowser.getViewBrowserId());
		}
	}
	
	/**
	 * returns the view browser denoted by the given id
	 * @param id the id of a view browser
	 * @return the view browser denoted by the given id
	 */
	public ViewBrowser getViewBrowser(String id) {
		
		if(id!=null && ! id.equals("")) {
			
			return idToViewBrowsers.get(id);
		}
		
		return mainDisplay.getMainViewBrowser();
	}
	
	/**
	 * @return the unique id for a view browser
	 */
	public String getUniqueIdForViewBrowser(){
		
		String id="0";
		int i=0;
		
		while(idToViewBrowsers.containsKey(id)){
			
			i++;
			id=i+"";
		}

		return id;
	}
	
	/**
	 * disposes all the view browsers
	 */
	public void disposeAllViewBrowsers() {
		
		ViewBrowser viewBrowser=null;
		
		for(String id : new HashSet<String>(idToViewBrowsers.keySet())) {
			
			viewBrowser=idToViewBrowsers.get(id);
			
			if(viewBrowser!=null) {
				
				viewBrowser.clear();
				viewBrowser.dispose();
			}
		}
		
		idToViewBrowsers.clear();
		loadedViewListeners.clear();
	}
}
