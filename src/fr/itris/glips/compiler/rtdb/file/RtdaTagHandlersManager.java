package fr.itris.glips.compiler.rtdb.file;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.resources.*;

/**
 * the class of the managers of the tag handlers
 * @author ITRIS, Jordi SUC
 */
public class RtdaTagHandlersManager {

	/**
	 * the map associating the name of a jwidget type to its tag handler
	 */
	private Map<String, RtdaTagHandler> jwidgetTagHandlers=
		new HashMap<String, RtdaTagHandler>();
	
	/**
	 * the main tag handler
	 */
	private RtdaTagHandler mainTagHandler;
	
	/**
	 * the main tag handler
	 */
	private RtdaTagHandler tagEventsTagHandler;
	
	/**
	 * the constructor of the class
	 */
	public RtdaTagHandlersManager() {
		
		loadMainTagHandler();
		loadTagEventsTagHandler();
		loadJWidgetTagHandlers();
	}
	
	/**
	 * loads the main tag handler
	 */
	protected void loadMainTagHandler() {
		
        Document doc=null;
        
        try{
        	doc=RtdaResources.getXMLDocument("rtdaAnimationsAndActions.xml");
        }catch (Exception ex){ex.printStackTrace();}
        
        if(doc!=null) {
        	
        	mainTagHandler=new RtdaTagHandler(doc);
        }
	}
	
	/**
	 * loads the tag events tag handler
	 */
	protected void loadTagEventsTagHandler() {
		
        Document doc=null;
        
        try{
        	doc=RtdaResources.getXMLDocument("rtdaAnimationsAndActionsOnEvents.xml");
        }catch (Exception ex){ex.printStackTrace();}
        
        if(doc!=null) {
        	
        	tagEventsTagHandler=new RtdaTagHandler(doc);
        }
	}
	
	/**
	 * loads the main tag handler
	 */
	protected void loadJWidgetTagHandlers() {
	
		//getting the map aassociating the id of a jwidget to its description document
		JWidgetRuntimeManager.loadAllJWidgetXMLDescriptions();
		Map<String, Document> jwidgetDocumentsMap=
			JWidgetRuntimeManager.getJWidgetDocumentsMap();
		
		//creating the rtda tag handlers
		Document doc=null;
		
		for(String id : jwidgetDocumentsMap.keySet()) {
			
			doc=jwidgetDocumentsMap.get(id);
	        
	        if(doc!=null) {
	        	
	        	jwidgetTagHandlers.put(id, new RtdaTagHandler(doc));
	        }
		}
	}

	/**
	 * @return the jwidgetTagHandlers map
	 */
	public Map<String, RtdaTagHandler> getJWidgetTagHandlers() {
		return jwidgetTagHandlers;
	}

	/**
	 * returns the tag handler corresponding to the provided element
	 * @param element en element
	 * @return the tag handler corresponding to the provided element
	 */
	public RtdaTagHandler getTagHandler(Element element) {

		if(element.getParentNode()!=null && 
			element.getParentNode().getNodeName().equals("svg")){
			
			return tagEventsTagHandler;
		}
		
		return mainTagHandler;
	}
	
	/**
	 * returns the jwidget animations and actions document given the jwidget id
	 * @param jwidgetId a jwidget id
	 * @return the jwidget animations and actions document given the jwidget id
	 */
	public RtdaTagHandler getJWidgetTagHandler(String jwidgetId) {
		
		return jwidgetTagHandlers.get(jwidgetId);
	}
}
