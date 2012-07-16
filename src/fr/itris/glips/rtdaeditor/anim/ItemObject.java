package fr.itris.glips.rtdaeditor.anim;

import org.w3c.dom.*;
import fr.itris.glips.rtda.resources.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the abstract class for the animation items
 * @author ITRIS, Jordi SUC
 */
public abstract class ItemObject {

	/**
	 * the name attribute
	 */
	public static final String nameAttribute="name";
	
	/**
	 * the animation tag name
	 */
	public static final String animationTagName="animation";
	
	/**
	 * the attribute tag name
	 */
	public static final String attributeTagName="attribute";
	
	/**
	 * the action tag name
	 */
	public static final String actionTagName="action";
	
	/**
	 * the view path constraint
	 */
	public static final String viewPathConstraint="viewPath";
	
	/**
	 * the default value attribute name
	 */
	public static final String defaultValueAttribute="defaultValue";
	
    /**
     * the document of the animations and actions
     */
    public static Document regularRtdaAnimationsAndActionsDocument;
    
    /**
     * the document of the actions on the events triggered 
     */
    public static Document tagEventRtdaAnimationsAndActionsDocument;
    
    /**
     * the constant for the animations
     */
    public static final int ANIMATION=0;
    
    /**
     * the constant for the actions
     */
    public static final int ACTION=1;
    
	/**
	 * the animations undo/redo label
	 */
	public static String animationUndoRedoLabel="";
	
	/**
	 * the actions undo/redo label
	 */
	public static String actionUndoRedoLabel="";
    
    /**
     * filling the map of the items
     */
    static{
    	
    	regularRtdaAnimationsAndActionsDocument=
    		RtdaResources.getXMLDocument("rtdaAnimationsAndActions.xml");
    	
    	tagEventRtdaAnimationsAndActionsDocument=
    		RtdaResources.getXMLDocument("rtdaAnimationsAndActionsOnEvents.xml");
    	
    	//getting the labels
		animationUndoRedoLabel=ResourcesManager.bundle.getString("undoredortdaanim");
		actionUndoRedoLabel=ResourcesManager.bundle.getString("undoredortdaaction");
    }

    /**
     * the spec document
     */
    protected Document specDocument;
    
    /**
     * sets the new spec document
     * @param doc the new spec document
     */
    public void setNewSpecAnimationDocument(Document doc) {
    	
    	this.specDocument=doc;
    }

    /**
     * @return the regular rtda animations and actions spec document
     */
    public static Document getRegularRtdaAnimationsAndActionsDocument() {
        return regularRtdaAnimationsAndActionsDocument;
    }
    
    /**
     * @return the tag event rtda animations and actions spec document
     */
    public static Document getTagEventRtdaAnimationsAndActionsDocument() {
		return tagEventRtdaAnimationsAndActionsDocument;
	}
    
    /**
     * computes the object type and the spec element corresponding to the animation element
     * @param animationElement an animation element
     * @return the object type and the spec element corresponding to the animation element as an array
     */
    protected Object[] computeObjectType(Element animationElement) {
    	
    	Object[] returnedObjects=new Object[2];
    	String name=animationElement.getNodeName();

    	if(name!=null && ! name.equals("") && specDocument!=null) {
    		
    		Element el=null;
    		
    	   	//for each spec animation element, checks if the given element maps one of them
        	NodeList items=specDocument.getElementsByTagName(animationTagName);
        	
        	for(int i=0; i<items.getLength(); i++){
        		
        		el=(Element)items.item(i);

        		if(el.getAttribute(nameAttribute).equals(name)){
        			
        			returnedObjects[0]=ANIMATION;
        			returnedObjects[1]=el;
        			
        			return returnedObjects;
        		}
        	}
        	
    	   	//for each spec animation element, checks if the given element maps one of them
        	items=specDocument.getElementsByTagName(actionTagName);
        	
        	for(int i=0; i<items.getLength(); i++){
        		
        		el=(Element)items.item(i);

        		if(el.getAttribute(nameAttribute).equals(name)){
        			
        			returnedObjects[0]=ACTION;
        			returnedObjects[1]=el;
        			
        			return returnedObjects;
        		}
        	}
    	}

    	return returnedObjects;
    }
    
}
