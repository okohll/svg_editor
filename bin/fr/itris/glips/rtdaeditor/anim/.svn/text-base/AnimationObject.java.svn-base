package fr.itris.glips.rtdaeditor.anim;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling a rtda animation
 * @author ITRIS, Jordi SUC
 */
public class AnimationObject extends ItemObject{
	
	/**
	 * the type of this object
	 */
	private int objectType=ANIMATION;
	
	/**
	 * the animation element
	 */
	private Element animationElement;
	
	 /**
     * the element for the specification of an animation
     */
    private Element specAnimationElement;
	
    /**
     * the element for the specification of a child
     */
    private Element specChildElement;

    /**
     * the type of the animation
     */
    private int type=TagToolkit.NONE;
    
    /**
     * whether this animation could have child nodes
     */
    private boolean hasChildren=false;
    
    /**
     * the list of the attributes
     */
    private LinkedList<AttributeObject> attributesList=new LinkedList<AttributeObject>();
    
    /**
     * the list of the children
     */
    private LinkedList<ChildObject> childrenList=new LinkedList<ChildObject>();
    
	/**
	 * the list of the child attribute names
	 */
	private LinkedList<String> childAttributesName=new LinkedList<String>();
    
	/**
	 * the undo/redo label
	 */
	private static String undoRedoLabel="";
	
	/**
	 * the svg handle
	 */
	private SVGHandle handle=
		Editor.getEditor().getHandlesManager().getCurrentHandle();
	
	/**
	 * the current jwidget edition
	 */
	private JWidgetEdition jwidgetEdition;
	
    /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param animationElement the animation element
     */
    public AnimationObject(JWidgetManager jwidgetManager, Element animationElement) {

    	this.animationElement=animationElement;
    	Element parentElement=(Element)animationElement.getParentNode();
    	
    	//checking if this animationElement belongs to a jwidget or to a svg shape
		if(Toolkit.hasJWidgetChildElement(parentElement)) {
			
			Element jwidgetElement=JWidgetManager.getJWidgetElement(parentElement);
			String name=jwidgetElement.getAttribute(fr.itris.glips.library.Toolkit.nameAttribute);

			//getting the jwidget edition object corresponding to this jwidget element
			jwidgetEdition=jwidgetManager.getJWidgetEdition(name);

			if(jwidgetEdition!=null) {

				//setting the new spec element
				setNewSpecAnimationDocument(jwidgetEdition.getAnimationsAndActionsDocument());
			}

		}else if(animationElement.getParentNode().getOwnerDocument().
				getDocumentElement().equals(animationElement.getParentNode())){
			
			setNewSpecAnimationDocument(getTagEventRtdaAnimationsAndActionsDocument());
			
		}else{

			//setting the new spec element
			setNewSpecAnimationDocument(getRegularRtdaAnimationsAndActionsDocument());
		}

    	//getting the type of the object and the spec element
    	Object[] returnedObjects=computeObjectType(animationElement);

    	if(returnedObjects!=null && returnedObjects[0]!=null && returnedObjects[1]!=null) {
    		
    		objectType=(Integer)returnedObjects[0];
    		specAnimationElement=(Element)returnedObjects[1];
    	}

        if(specAnimationElement!=null){
        	
        	//getting the type of this animation
            type=EditableItem.getType(specAnimationElement.getAttribute("type"));

        	//creating the attribute objects//
            //getting the attribute nodes of the spec
        	LinkedList<Element> groupElements=null;
        	String groupValue="", currentGroup=null, name=null;
        	Element cur=null;
        	LinkedList<String> attributeNames=new LinkedList<String>();
        	Map<String, AttributeObject> tmpAttributes=new HashMap<String, AttributeObject>();
        	
        	for(Node node=specAnimationElement.getFirstChild(); node!=null; node=node.getNextSibling()){
        		
        		if(node.getNodeName().equals("attribute") && node instanceof Element){
        			
               		cur=(Element)node;
            		
            		if(cur.hasAttribute("group")){
            			
            			groupValue=cur.getAttribute("group");
            			
            			//the attribute belongs to a group//
            			if(groupElements!=null && groupElements.size()>0 && 
            				currentGroup!=null && ! currentGroup.equals(groupValue)){
            				
            				//handles the nodes of the previous group
            				tmpAttributes.put(currentGroup, new AttributeObject(this, animationElement, groupElements));
            				groupElements=null;
            				currentGroup=null;
            			}
            			
        				//if a new group starts
        				if(groupElements==null){
        					
        					groupElements=new LinkedList<Element>();
        					currentGroup=groupValue;
        					attributeNames.add(groupValue);
        				}
        				
        				//adding the element to the list of the elements being in the same group
        				groupElements.add(cur);
            			
            		}else{

            			name=cur.getAttribute("name");
            			attributeNames.add(name);
            			tmpAttributes.put(name, new AttributeObject(this, animationElement, cur));
            		}
        		}
        	}
        	
        	//validating the remaining group attributes if they exist
			if(groupElements!=null && groupElements.size()>0){
				
				//handles the nodes of the previous group
				tmpAttributes.put(currentGroup, new AttributeObject(this, animationElement, groupElements));
			}
			
			
			//filling the list of the attribute objects
			AttributeObject attObj=null;
			
			for(String attName : attributeNames){
				
				attObj=tmpAttributes.get(attName);
				
				if(attObj!=null){
					
					attributesList.add(attObj);
				}
			}
        }
        
		//getting the undo/redo label
		undoRedoLabel=getObjectType()==ACTION?actionUndoRedoLabel:animationUndoRedoLabel;
		
		//creating the children
		handleChildren();
    }
    
    /**
     * handles the children of this object
     */
    public void handleChildren() {

    	if(specAnimationElement!=null){
    		
    	   	//clearing the list of the children
        	childrenList.clear();
        	childAttributesName.clear();
        	
        	//creating the child objects
        	NodeList childNodes=specAnimationElement.getElementsByTagName("child");
        	
        	if(childNodes!=null && childNodes.getLength()>0){
        		
        		hasChildren=true;
        		specChildElement=(Element)childNodes.item(0);
        		
                Element cur=null;
                //whether this child requires a xml view path among all the available ones
                boolean hasViewPathConstraint=false;
                String viewPathAttribute="";
                String groupName="";
    	
            	//creating the child attribute names list
            	for(Node node=specChildElement.getFirstChild(); node!=null; node=node.getNextSibling()){
            		
            		if(node.getNodeName().equals("attribute") && node instanceof Element){
            			
            			cur=(Element)node;
            			
            			if(cur.hasAttribute("group")){
            				
            				groupName=cur.getAttribute("group");
            				
            				if(! childAttributesName.contains(groupName)){
            					
            					childAttributesName.add(groupName);
            				}

            			}else{
            				
            				childAttributesName.add(cur.getAttribute("name"));
            			}

            			if(cur.getAttribute("constraint").equals(viewPathConstraint)) {
            				
            				viewPathAttribute=cur.getAttribute("name");
            				hasViewPathConstraint=true;
            			}
            		}
            	}
            	
            	if(hasViewPathConstraint) {
            		
                	//getting the list of the available view xml path
                	LinkedList<String> viewPaths=DataBaseNodeToolkit.
                																		getAllAvailableViewPath(animationElement.getOwnerDocument());
                	
                	//getting the map associating a view xml path to an existing child node
                	HashMap<String, Element> viewPathToElement=new HashMap<String, Element>();
            		NodeList childrenElements=animationElement.getElementsByTagName(specChildElement.getAttribute("name"));
            		
            		if(childrenElements!=null){
            			
            			Element child=null;
            			String childViewPath="";
            			
            			for(int i=0; i<childrenElements.getLength(); i++){
            				
            				//storing the child view path and the element in the map
            				child=(Element)childrenElements.item(i);
            				childViewPath=child.getAttribute(viewPathAttribute);
            				viewPathToElement.put(childViewPath, child);
            			}
            		}
            		
            		//creating the child elements corresponding to each found view path
            		Element newChild=null;
            		
            		for(String viewXMLPath : viewPaths) {
            			
            			if(! viewPathToElement.containsKey(viewXMLPath)) {
            				
            				 newChild=animationElement.getOwnerDocument().createElementNS(
    				 																							animationElement.getNamespaceURI(), 
    				 																							specChildElement.getAttribute("name"));
            				 newChild.setAttribute(viewPathAttribute, viewXMLPath);
                 			 animationElement.appendChild(newChild);
            			}
            		}
            	}
            		
        		//creating the child objects of this animation node
        		NodeList childrenElements=animationElement.getElementsByTagName(specChildElement.getAttribute("name"));
        		
        		if(childrenElements!=null){
        			
        			Element child=null;
        			
        			for(int i=0; i<childrenElements.getLength(); i++){
        				
        				child=(Element)childrenElements.item(i);
        				childrenList.add(new ChildObject(this, child, specChildElement));
        			}
        		}
        	}
    	}
    }

	/**
     * @return Returns the attributesList.
     */
    public LinkedList<AttributeObject> getAttributesList() {
        return attributesList;
    }

    /**
     * @return Returns the childrenList.
     */
    public LinkedList<ChildObject> getChildrenList() {
        return childrenList;
    }

    /**
	 * @return Returns the animationElement.
	 */
	public Element getAnimationElement() {
		return animationElement;
	}

	/**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }

	/**
     * @return the animation object name
     */
    public String getName(){
    	
    	return animationElement.getNodeName();
    }

	/**
	 * @return whether this animation could have children
	 */
	public boolean hasChildren() {
		return hasChildren;
	}

	/**
	 * @return Returns the objectType.
	 */
	public int getObjectType() {
		return objectType;
	}
	
	/**
	 * @return the attribute that is a tag value
	 */
	public String getChildTagValueAttribute() {
		
		String att="";
		
		if(specChildElement!=null) {
			
			NodeList childAttributes=specChildElement.getElementsByTagName("attribute");
			Element el=null;
			
			for(int i=0; i<childAttributes.getLength(); i++) {
				
				el=(Element)childAttributes.item(i);
				
				if(el.getAttribute("constraint").equals("tagvalue")) {
					
					att=el.getAttribute("name");
					break;
				}
			}
		}
		
		return att;
	}
	
    /**
     * @return the list of the names of the child attributes
     */
    public LinkedList<String> getChildAttributeNamesList(){
    	
    	return childAttributesName;
    }

	/**
	 * @return the specChildElement
	 */
	public Element getSpecChildElement() {
		return specChildElement;
	}
	
	/**
	 * adds a new undo/redo action
	 * @param executeRunnable the runnable for the execution of the action
	 * @param undoRunnable the undo runnable
	 * @param redoRunnable the redo runnable
	 */
	public void addUndoRedoAction(
		Runnable executeRunnable, Runnable undoRunnable, Runnable redoRunnable) {
		
		//creating the undo/redo item
		UndoRedoAction action=new UndoRedoAction(undoRedoLabel, executeRunnable, 
				undoRunnable, redoRunnable, new HashSet<Element>());
		
		//creates the undo/redo list so that actions can be added to it
		UndoRedoActionList actionlist=new UndoRedoActionList(undoRedoLabel, false);
		actionlist.add(action);
		handle.getUndoRedo().addActionList(actionlist, false); 
	}
	
	/**
	 * @return the source that is used in the JWidget
	 */
	public String getJWidgetSource() {
		
		return animationElement.getAttribute(
			fr.itris.glips.library.Toolkit.sourceAttributeName);
	}

	/**
	 * @return the svg handle
	 */
	public SVGHandle getSVGHandle() {
		return handle;
	}
	
	/**
	 * returns the label corresponding to the provided id
	 * @param id an id
	 * @return the label corresponding to the provided id
	 */
	public String getLabel(String id){
		
		String label="";
		
		try {
			label=ResourcesManager.bundle.getString(id);
		}catch (Exception ex) {
			
			if(jwidgetEdition!=null){
				
				try{
					label=jwidgetEdition.getBundle().getString(id);
				}catch (Exception e){}
			}
		}
		
		if(label==null || label.equals("")){
			
			label=id;
		}

		return label;
	}
	
	/**
	 * disposes this animation object
	 */
	public void dispose() {
		
		
	}

}
