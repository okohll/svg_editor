/*
 * Created on 7 juin 2005
 * 
 =============================================
 GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
 GLIPS Graffiti Editor, a SVG Editor
 Copyright (C) 2003 Jordi SUC, Philippe Gil, SARL ITRIS
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
 Contact : jordi.suc@itris.fr; philippe.gil@itris.fr
 
 =============================================
 */
package fr.itris.glips.rtdaeditor.dbeditor;

import org.w3c.dom.*;
import java.util.*;

import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.*;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.display.canvas.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import javax.swing.tree.*;

/**
 * the node in the JTree
 * 
 * @author ITRIS, Jordi SUC
 */
public class WidgetDataBaseEditorTreeNode extends DefaultMutableTreeNode{

	/**
	 * the database editor
	 */
	private DataBaseEditorModule dbEditor=null;
	
	/**
	 * the widget database tree model 
	 */
	private WidgetDataBaseModel model=null;
	
	/**
	 * a svg handle
	 */
	private SVGHandle handle=null;
	
    /**
     * a dom element
     */
    private Element element=null;

    /**
     * whether this node is the representation of the value of a tag or not
     */
    private boolean isChildValue=false;
    
    /**
     * the id of the node
     */
    private String id="";
    
    /**
     * the label of the type of the node
     */
    private String typeLabel="";
    
    /**
     * the name of the icon of the node
     */
    private String iconName="";
    
    /**
     * the type of the node
     */
    private int type=TagToolkit.NOT_A_TAG;
    
    /**
     * the set of the children of the element
     */
    private HashSet<Element> childElements=new HashSet<Element>();
    
    /**
     * the listener to the dom
     */
    private SVGDOMListener domListener=null;
    
    /**
     * an instance of this class
     */
    private WidgetDataBaseEditorTreeNode treeNode=this;
    
    /**
     * the constructor of the class
     * @param dbEditor the database editor
     * @param model the widget data base model
     * @param element a dom element
     */
    public WidgetDataBaseEditorTreeNode(DataBaseEditorModule dbEditor, 
    		WidgetDataBaseModel model, Element element){
        
    	this.dbEditor=dbEditor;
    	this.model=model;
    	this.element=element;
    	this.handle=dbEditor.getSVGEditor().getHandlesManager().getCurrentHandle();
        this.id=element.getAttribute("id");
        
        //building the tree path
        LinkedList<WidgetDataBaseEditorTreeNode> pathList=new LinkedList<WidgetDataBaseEditorTreeNode>();
        pathList.add(this);
        
        setAllowsChildren(false);

        //getting the name of the element
        String elementName=element.getNodeName();
        elementName=elementName.replaceFirst("rtda:", "");
        
        //getting the type and icon name for the tag
        type=TagToolkit.getNodeType(elementName);
        iconName=TagToolkit.getIconName(type);
        typeLabel=TagToolkit.getTagLabel(type);
        
        //getting the label of the tag node and checking if the tag can have child nodes
        switch(type) {
        
            case TagToolkit.NOT_A_TAG :
            case TagToolkit.ENUMERATED :

                setAllowsChildren(true);
                break;
                
            case TagToolkit.ENUMERATED_CHILD :
                
                isChildValue=true;
                break;

        }

        if(! isChildValue){
            
            //building the children nodes of this node
            if(element.hasChildNodes()){
            	
            	Node child=null;
            	NodeList childrenNodes=element.getChildNodes();
            	WidgetDataBaseEditorTreeNode childTreeNode=null;
            	
            	for(int i=0; i<childrenNodes.getLength(); i++){
            		
            		child=childrenNodes.item(i);
            		
            		if(child!=null && child instanceof Element){
            			
            			childTreeNode=new WidgetDataBaseEditorTreeNode(dbEditor, model, (Element)child);
            			add(childTreeNode);
            		}
            	}
            }
        }
        
        //adding a listener to the element so that the tree node can be notified when the element changes
        domListener=new SVGDOMListener(this.element){

            @Override
            public void nodeChanged(){

                //checking if the given node equals this tree path element, and notifies the listener that this node
                //has been modified
                if(getNode()!=null && getNode() instanceof Element && getNode().equals(treeNode.element)){

                    //modifying the id of the node
                    treeNode.id=((Element)getNode()).getAttribute("id");
                    treeNode.model.nodeChanged(treeNode);
                    
                    //notifies that the selection has changed
                    EditorToolkit.forceReselection();
                }
            }
            
            @Override
            public void nodeInserted(Node insertedNode) {

                //checking if the inserted node has not already been inserted
                if(insertedNode!=null && ! childElements.contains(insertedNode)){

                    //creating and adding the new tree node
                    WidgetDataBaseEditorTreeNode childTreeNode=
                    	new WidgetDataBaseEditorTreeNode(treeNode.dbEditor, treeNode.model, (Element)insertedNode);
                    
                    treeNode.model.insertNodeInto(childTreeNode, treeNode, getChildCount());
                    treeNode.model.getTree().makeVisible(childTreeNode.getTreePath());
                }
            }

            @Override
            public void nodeRemoved(Node removedNode){
                
                if(removedNode!=null){
                    
                    //getting and removing the tree node
                    WidgetDataBaseEditorTreeNode childTreeNode=getTreeNode((Element)removedNode);
                    
                    if(childTreeNode!=null){
                        
                        treeNode.model.removeNodeFromParent(childTreeNode);
                        
                        if(treeNode.getChildCount()>0){
                            
                            WidgetDataBaseEditorTreeNode firstChild=((WidgetDataBaseEditorTreeNode)treeNode.getFirstChild());
                            
                            treeNode.model.getTree().setSelectionPath(firstChild.getTreePath());
                            treeNode.model.getTree().scrollPathToVisible(firstChild.getTreePath());
                            
                        }else{
                            
                            treeNode.model.getTree().setSelectionPath(treeNode.getTreePath());
                            treeNode.model.getTree().scrollPathToVisible(treeNode.getTreePath());
                        }
                    }
                }
            }

            @Override
            public void structureChanged(Node lastModifiedNode) {}
        };
        
        handle.getSvgDOMListenerManager().addDOMListener(domListener);
    }
    
    /**
     * disposes this node and its child nodes
     */
    public void dispose(){
        
        handle.getSvgDOMListenerManager().removeDOMListener(domListener);
        
        //disposes each child
        WidgetDataBaseEditorTreeNode child=null;
        Enumeration<?> childrenEnum=children();
        
        while(childrenEnum.hasMoreElements()){
            
            child=(WidgetDataBaseEditorTreeNode)childrenEnum.nextElement();
            
            if(child!=null){
                
                child.dispose();
            }
        }
    }
    
    @Override
	public void add(MutableTreeNode newTreeNode){
		
		super.add(newTreeNode);
		
		if(newTreeNode!=null && newTreeNode instanceof WidgetDataBaseEditorTreeNode){
			
			childElements.add(((WidgetDataBaseEditorTreeNode)newTreeNode).getElement());
		}
	}
	
    @Override
	public void remove(MutableTreeNode oldTreeNode){

		super.remove(treeNode);

		if(treeNode!=null && oldTreeNode instanceof WidgetDataBaseEditorTreeNode){
			
			childElements.remove(((WidgetDataBaseEditorTreeNode)oldTreeNode).getElement());
		}
	}
	
	/**
	 * @return whether this node can be removed or not
	 */
	public boolean canRemoveNode(){
	    
	    boolean canRemoveNode=false;
	    
	    if(type==TagToolkit.NOT_A_TAG){
	        
	        //if the parent node is null, then the node is the root node, and it cannot be removed
	        if(getParent()!=null){
	        	
	        	if(children==null){
	        		
	        		canRemoveNode=true;
	        		
	        	}else{
	        		
		            //checking if the children of the module can be removed, if not, the module cannot be removed
		            for(int i=0; i<children.size(); i++){

		                if(! ((WidgetDataBaseEditorTreeNode)children.get(i)).canRemoveNode()){
		                    
		                    break;
		                }
		                
		                if(i==children.size()-1){
		                    
		                    canRemoveNode=true;
		                }
		            }
	        	}
	        }
	        
	    }else if(type==TagToolkit.ENUMERATED_CHILD){
	        
	        //the enumerated value node can be removed if its parent can be removed
	        canRemoveNode=((WidgetDataBaseEditorTreeNode)getParent()).canRemoveNode();
	        
	    }else{

	        canRemoveNode= ! (EditorAnimationsToolkit.isTagUsed(
	        	element.getOwnerDocument().getDocumentElement(), getTagName()));
	    }

	    return canRemoveNode;
	}
	
	/**
	 * @return the tag name corresponding to this node
	 */
	public String getTagName(){
	    
	    String tagName=id;
	    
	    WidgetDataBaseEditorTreeNode parentNode=(WidgetDataBaseEditorTreeNode)getParent();
	    
	    while(parentNode!=null && parentNode.getParent()!=null){
	        
	        tagName=parentNode.getId()+"/"+tagName;
	        
	        parentNode=(WidgetDataBaseEditorTreeNode)parentNode.getParent();
	    }
	    
	    if(tagName.indexOf("/")!=-1){
	        
	        tagName="/"+tagName;
	    }
	    
	    return tagName;
	}
	
	/**
	 * @return te set of the child elements of this node
	 */
	public HashSet<Element> getChildElements(){
	    
	    return childElements;
	}
    
    /**
     * returns the tree node corresponding to the given element
     * @param childElement an element
     * @return the tree node corresponding to the given element
     */
    protected WidgetDataBaseEditorTreeNode getTreeNode(Element childElement){
        
        WidgetDataBaseEditorTreeNode childTreeNode=null;
        
        if(childElement!=null){
            
            WidgetDataBaseEditorTreeNode child=null;
            
            for(Iterator<?> it=children.iterator(); it.hasNext();){
                
                child=(WidgetDataBaseEditorTreeNode)it.next();
                
                if(child!=null && child.getElement().equals(childElement)){
                    
                    childTreeNode=child;
                    break;
                }
            }
        }
        
        return childTreeNode;
    }

	/**
	 * @return the path in the tree
	 */
	public TreePath getTreePath() {

		return new TreePath(getPath());
	}

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }
    
    /**
     * returns whether the given id is already used
     * @param foundId an id
     * @return  whether the given id is already used
     */
    public boolean isDuplicatedId(String foundId){
    	
    	boolean isDuplicatedId=false;
    	
    	if(foundId!=null && ! foundId.equals("")){
    		
            if(! isRoot()) {
                
                //creating the set of the ids of the siblings of the current node
                HashSet<String> siblingIds=new HashSet<String>();
                String currentId="";
                Set<Element> childEls=
                	((WidgetDataBaseEditorTreeNode)getParent()).getChildElements();
                
                for(Element el : childEls){
                	
                    if(el!=null && ! el.equals(element)){
                        
                        currentId=el.getAttribute("id");
                        
                        if(currentId!=null && ! currentId.equals("")){
                            
                            siblingIds.add(currentId);
                        }
                    }
                }
                
                isDuplicatedId=siblingIds.contains(foundId);
            }
    	}
    	
    	return isDuplicatedId;
    }
    
    /**
     * sets the new id
     * @param id the new id
     */
    public void setId(String id){
    	
    	if(id!=null && ! id.equals(this.id)){

    		final String newId=id, oldId=element.getAttribute("id");
    		final String oldTagName=getTagName();
    		this.id=id;
    		final String newTagName=getTagName();
            
            //getting the tag name of the parent tree node
            String parentTagName="";
            
            if(getParent()!=null) {
                
                parentTagName=((WidgetDataBaseEditorTreeNode)getParent()).getTagName();
            }
            
            final String fparentTagName=parentTagName;

			//setting the new value for the node
			final Runnable executeRunnable=new Runnable(){

				public void run() {
			        
				    element.setAttribute("id", newId);

				    //modifies the referenced tags in the animation nodes
				    if(type==TagToolkit.ENUMERATED_CHILD){
				        
				        EditorAnimationsToolkit.convertEnumeratedTagValue(	
				        		element.getOwnerDocument().getDocumentElement(), 
                                	fparentTagName, oldId, newId);
				        
				    }else{
				        
				        EditorAnimationsToolkit.convertTags(
				        		element.getOwnerDocument().getDocumentElement(), oldTagName, newTagName);
				    }
				    
				    //notifies that the node has been modified
				    handle.getSvgDOMListenerManager().fireNodeChanged(element);
				}
			};
			
			final Runnable undoRunnable=new Runnable(){

				public void run() {
					
					element.setAttribute("id", oldId);
					
				    //modifies the referenced tags in the animation nodes
				    if(type==TagToolkit.ENUMERATED_CHILD){
				        
				        EditorAnimationsToolkit.convertEnumeratedTagValue(	
				        		element.getOwnerDocument().getDocumentElement(), 
                                     fparentTagName, newId, oldId);
				        
				    }else{
				        
				        EditorAnimationsToolkit.convertTags(
				        	element.getOwnerDocument().getDocumentElement(), 
				        		newTagName, oldTagName);
				    }

				    //notifies that the node has been modified
				    handle.getSvgDOMListenerManager().fireNodeChanged(element);
				}
			};

			//adding the undo/redo actions
			UndoRedoAction action=new UndoRedoAction(
				dbEditor.labelRtdaDatabaseEditorUndoRedo, executeRunnable, 
					undoRunnable, executeRunnable, new HashSet<Element>());
	
			UndoRedoActionList actionlist=new UndoRedoActionList(
					dbEditor.labelRtdaDatabaseEditorUndoRedo, false);
			actionlist.add(action);
			handle.getUndoRedo().addActionList(actionlist, false);
    	}
    }

    /**
     * creates and returns a unique id in the application given a base string
     * @param baseString the base string for the id
     * @return a unique id in the application given a base string
     */
    protected String getId(String baseString){
        
        String returnId="";
        
        if(baseString!=null && ! baseString.equals("")){
        	
        	//creating the set of the ids of the siblings of the current node
        	HashSet<String> siblingIds=new HashSet<String>();
        	String currentId="";
        	Set<Element> childEls=
        		((WidgetDataBaseEditorTreeNode)getParent()).getChildElements();
        	
        	for(Element el : childEls){

        		if(el!=null && ! el.equals(element)){
        			
        			currentId=el.getAttribute("id");
        			
        			if(currentId!=null && ! currentId.equals("")){
        				
        				siblingIds.add(currentId);
        			}
        		}
        	}

            //computes the first id that is not already used
            for(int i=0; i<siblingIds.size(); i++){
                
            	returnId=baseString+i;
                
                if(! siblingIds.contains(returnId)){
                    
                    break;
                }
            }
        }
            
        return returnId;
    }
    
    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }
	
    /**
     * @return Returns the element.
     */
    public Element getElement() {
        return element;
    }
    
    /**
     * removes the given node from the tree
     * @param child
     */
    public void removeChildNode(WidgetDataBaseEditorTreeNode child){
    	
        if(getAllowsChildren() && child!=null){
            
            //the element that should be removed
            final Element elementToBeRemoved=child.getElement();

			//removing the node
			final Runnable executeRunnable=new Runnable(){

				public void run() {

				    element.removeChild(elementToBeRemoved);
				    handle.getSvgDOMListenerManager().
				    	fireNodeRemoved(element, elementToBeRemoved);
				}
			};
			
			final Runnable undoRunnable=new Runnable(){

				public void run() {
			
				    element.appendChild(elementToBeRemoved);
				    handle.getSvgDOMListenerManager().
				    	fireNodeInserted(element, elementToBeRemoved);
				}
			};
			//adding the undo/redo actions
			UndoRedoAction action=new UndoRedoAction(
					dbEditor.labelRtdaDatabaseEditorUndoRedo, executeRunnable, 
						undoRunnable, executeRunnable, new HashSet<Element>());
	
			UndoRedoActionList actionlist=
				new UndoRedoActionList(action.getName(), false);
			actionlist.add(action);
			handle.getUndoRedo().addActionList(actionlist, false);
        }
    }
    
    /**
     * inserts a new child node in the tree
     * @param nodeType the type of the node to be created
     */
    protected void insertChildNode(int nodeType){
        
        if(getAllowsChildren()){
            
            String nodeName=TagToolkit.getTagName(nodeType);
            String baseId=TagToolkit.getTagIdLabel(nodeType)+" ";
            
            //getting the id for the new node
            String newId=handle.getSvgElementsManager().getId(baseId, null);
            
            //creating the new element
            final Element newElement=element.getOwnerDocument().
            	createElementNS(null, "rtda:"+nodeName);
            newElement.setAttributeNS(null, "id", newId);
            
			//appending the new node
			Runnable executeRunnable=new Runnable(){

				public void run() {

				    element.appendChild(newElement);
				    handle.getSvgDOMListenerManager().
				    	fireNodeInserted(element, newElement);
				}
			};
			
			Runnable undoRunnable=new Runnable(){

				public void run(){
					
				    element.removeChild(newElement);
				    handle.getSvgDOMListenerManager().
				    	fireNodeRemoved(element, newElement);
				}
			};

			//adding the undo/redo actions
			UndoRedoAction action=new UndoRedoAction(
					dbEditor.labelRtdaDatabaseEditorUndoRedo,
						executeRunnable, undoRunnable, executeRunnable, new HashSet<Element>());
	
			UndoRedoActionList actionlist=
				new UndoRedoActionList(action.getName(), false);
			actionlist.add(action);
			handle.getUndoRedo().addActionList(actionlist, false);
        }
    }
    
    @Override
    public String toString(){
        
        return id;
    }
    
    /**
     * @return the tool tip text for this node
     */
    public String getToolTipText(){
        
        return typeLabel;
    }
    
    /**
     * @return the name of the icon that will be displayed for this node
     */
    public String getIconName(){
        
        return iconName;
    }
}
