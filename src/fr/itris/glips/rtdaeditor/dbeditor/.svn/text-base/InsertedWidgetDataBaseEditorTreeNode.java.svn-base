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
import fr.itris.glips.svgeditor.display.canvas.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import javax.swing.tree.*;

/**
 * the node in the JTree
 * @author ITRIS, Jordi SUC
 */
public class InsertedWidgetDataBaseEditorTreeNode extends DefaultMutableTreeNode{
    
	/**
	 * the database editor
	 */
	private DataBaseEditorModule dbEditor=null;
	
	/**
	 * the widget database tree model 
	 */
	private InsertedWidgetDataBaseModel model=null;
	
	/**
	 * a svg handle
	 */
	private SVGHandle svgHandle=null;
	
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
     * the value of the view database corresponding to this node
     */
    private String correspondingValue="";
    
    /**
     * the string representing the node
     */
    private String representationString="";
    
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
     * the list of the children of the element
     */
    private LinkedList<Element> childElements=new LinkedList<Element>();
    
    /**
     * the listener to the dom
     */
    private SVGDOMListener domListener=null;
    
    /**
     * an instance of this class
     */
    private InsertedWidgetDataBaseEditorTreeNode treeNode=this;
    
    /**
     * the constructor of the class
     * @param dbEditor the database editor
     * @param model the widget data base model
     * @param element a dom element
     */
    public InsertedWidgetDataBaseEditorTreeNode(
    		DataBaseEditorModule dbEditor, InsertedWidgetDataBaseModel model, Element element){
        
    	this.dbEditor=dbEditor;
    	this.model=model;
    	this.element=element;
    	this.svgHandle=dbEditor.getSVGEditor().getHandlesManager().getCurrentHandle();
        this.id=element.getAttribute("id");
        this.correspondingValue=element.getAttribute("value");
        
        //building the tree path
        LinkedList<InsertedWidgetDataBaseEditorTreeNode> pathList=
               new LinkedList<InsertedWidgetDataBaseEditorTreeNode>();
        pathList.add(this);
        
        setAllowsChildren(false);

        //getting the name of the element
        String elementName=element.getNodeName();
        elementName=elementName.replaceFirst("rtda:", "");
        
        //getting the int tag type, the name of the icon and the label for the node
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
            	InsertedWidgetDataBaseEditorTreeNode childTreeNode=null;
            	
            	for(int i=0; i<childrenNodes.getLength(); i++){
            		
            		child=childrenNodes.item(i);
            		
            		if(child!=null && child instanceof Element){
            			
            			childTreeNode=new InsertedWidgetDataBaseEditorTreeNode(dbEditor, model, (Element)child);
            			add(childTreeNode);
            		}
            	}
            }
        }
        
        //creating the string representation
        this.representationString=getRepresentationString();

        //adding a listener to the element so that the tree node can be notified when the element changes
        this.domListener=new SVGDOMListener(this.element){

        	@Override
            public void nodeChanged(){

                //checking if the given node equals this tree path element, and modifies the data associated with this node
                if(getNode()!=null && getNode() instanceof Element && getNode().equals(treeNode.element)){

                    //updating the data
                    treeNode.correspondingValue=((Element)getNode()).getAttribute("value");
                    treeNode.representationString=getRepresentationString();
                    treeNode.model.nodeChanged(treeNode);
                }
            }
            
        	@Override
            public void nodeInserted(Node insertedNode) {

            }

        	@Override
            public void nodeRemoved(Node removeNode){

            }

        	@Override
            public void structureChanged(Node lastModifiedNode) {

                //updating the tree under the node
                if(getNode()!=null && getNode() instanceof Element && 
                	getNode().equals(treeNode.element) && lastModifiedNode!=null && 
                        lastModifiedNode instanceof Element){

                    treeNode.model.nodeStructureChanged(treeNode);
                    
                    //getting the last modified tree node
                    InsertedWidgetDataBaseEditorTreeNode lastModifiedTreeNode=
                                 getTreeNode(treeNode, (Element)lastModifiedNode);
                    
                    //selecting the last modified node
                    treeNode.model.getTree().setSelectionPath(lastModifiedTreeNode.getTreePath());
                }
            }
        };
        
        svgHandle.getSvgDOMListenerManager().addDOMListener(domListener);
    }
    
    /**
     * disposes this node and its child nodes
     */
    public void dispose(){
        
    	svgHandle.getSvgDOMListenerManager().removeDOMListener(domListener);
        
        //disposes each child
        InsertedWidgetDataBaseEditorTreeNode child=null;
        Enumeration<?> childrenEnum=children();
        
        while(childrenEnum.hasMoreElements()){
            
            child=(InsertedWidgetDataBaseEditorTreeNode)childrenEnum.nextElement();
            
            if(child!=null){
                
                child.dispose();
            }
        }
    }
    
	@Override
	public void add(MutableTreeNode newTreeNode) {
		
		super.add(newTreeNode);
		
		if(newTreeNode!=null && newTreeNode instanceof InsertedWidgetDataBaseEditorTreeNode){
			
			childElements.add(((InsertedWidgetDataBaseEditorTreeNode)newTreeNode).getElement());
		}
	}
	
	@Override
	public void remove(MutableTreeNode oldTreeNode){

		super.remove(oldTreeNode);

		if(oldTreeNode!=null && oldTreeNode instanceof InsertedWidgetDataBaseEditorTreeNode){
			
			childElements.remove(((InsertedWidgetDataBaseEditorTreeNode)oldTreeNode).getElement());
		}
	}
	
	/**
	 * @return the set of the child elements of this node
	 */
	public LinkedList<Element> getChildElements(){
	    
	    return childElements;
	}
    
    /**
     * returns the tree node corresponding to the given element
     * @param childElement an element
     * @return the tree node corresponding to the given element
     */
    protected InsertedWidgetDataBaseEditorTreeNode getTreeNode(Element childElement){
        
        InsertedWidgetDataBaseEditorTreeNode childTreeNode=null;
        
        if(childElement!=null && children!=null){
            
            InsertedWidgetDataBaseEditorTreeNode child=null;
            
            for(Iterator<?> it=children.iterator(); it.hasNext();){
                
                child=(InsertedWidgetDataBaseEditorTreeNode)it.next();
                
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
     * returns the tree node corresponding to the given dom node and that can be found
     * under the root node
     * @param rootNode the root node
     * @param nodeToBeFound a dom node
     * @return the tree node corresponding to the given dom node and that can be found
     * under the root node
     */
    protected InsertedWidgetDataBaseEditorTreeNode getTreeNode(
            InsertedWidgetDataBaseEditorTreeNode rootNode, Element nodeToBeFound) {
        
        InsertedWidgetDataBaseEditorTreeNode foundTreeNode=null;
        
        if(rootNode!=null && nodeToBeFound!=null) {
            
            //checking if the searched tree node corresponds to the given root node
            if(rootNode.getElement().equals(nodeToBeFound)) {
                
                foundTreeNode=rootNode;
                
            }else {
                
                //searching into the children of the root node, if a corresponding tree node can be found
                Enumeration<?> childrenEnum=rootNode.children();
                InsertedWidgetDataBaseEditorTreeNode childNode=null;
                
                while(childrenEnum.hasMoreElements()) {
                    
                    childNode=(InsertedWidgetDataBaseEditorTreeNode)childrenEnum.nextElement();
                    
                    if(childNode!=null) {
                        
                        childNode=getTreeNode(childNode, nodeToBeFound);
                        
                        if(childNode!=null) {
                            
                            foundTreeNode=childNode;
                        }
                    }
                }
            }
        }
        
        return foundTreeNode;
    }
	
    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }
    
    /**
     * @return the tag name (in the view data base) corresponding to this node
     */
    public String getTagName(){
        
        String tagName="", nodeId="";
        
        if(type!=TagToolkit.NOT_A_TAG){
            
            //getting the name of the tag for this node
            Element node=element;
            tagName=EditorAnimationsToolkit.normalizePath(correspondingValue);
            
            if(correspondingValue!=null && ! correspondingValue.equals("")){
                
                node=(Element)node.getParentNode();
                
                while(  node!=null && 
                            ! node.equals(((InsertedWidgetDataBaseEditorTreeNode)getRoot()).getElement())){
                    
                    nodeId=EditorAnimationsToolkit.normalizePath(node.getAttribute("value"));
                    
                    if(nodeId!=null && ! nodeId.equals("")){
                        
                        tagName=nodeId+"/"+tagName;
                        
                    }else{
                        
                        break;
                    }

                    node=(Element)node.getParentNode();
                }
                
                if(tagName.indexOf("/")!=-1){
                    
                    tagName="/"+tagName;
                }
            }
        }
        
        return tagName;
    }

    /**
     * @return Returns the correspondingValue.
     */
    public String getCorrespondingValue() {
        return correspondingValue;
    }
    
    /**
     * sets the new corresponding value
     * @param value the new corresponding value
     * @param childrenValues the values for each child of the tag, if it is an enumerated tag
     */
    public void setCorrespondingValue(String value, LinkedList<String> childrenValues) {
    	
    	if(value!=null && ! isRoot()){

            final LinkedList<String> oldChildrenValues=new LinkedList<String>();

            //filling the list for the parent
            if(type!=TagToolkit.ENUMERATED_CHILD) {

                if(childrenValues!=null){
                    
                    //for each child of the current child, its value is stored
                    String childValue="";
                    
                    for(Element child : childElements){

                        if(child!=null){
                            
                            childValue=child.getAttribute("value");
                            
                            if(childValue==null){
                                
                                childValue="";
                            }
                            
                            oldChildrenValues.add(childValue);
                        }
                    }
                }
            }

            //creating the list of the new values
            final LinkedList<String> newChildrenValues=new LinkedList<String>();
            
            if(childrenValues!=null){
            	
            	newChildrenValues.addAll(childrenValues);
            }

    	    //storing the old and new values for the tag
    		final String newCorrespondingValue=value, 
    			oldCorrespondingValue=element.getAttribute("value");
            
			//setting the new value for the node
			final Runnable executeRunnable=new Runnable(){

				public void run(){
                    
			        int i=0;
			        String childValue="";

				    //modifying the corresponding value of the current element
				    element.setAttribute("value", newCorrespondingValue);
                    
                    //notifies that the node has been modified
				    svgHandle.getSvgDOMListenerManager().fireNodeChanged(element);

				    //handling the child elements if the tag is an enumerated one
				    if(type!=TagToolkit.ENUMERATED_CHILD && newChildrenValues.size()>0){
				        
                        i=0;
                        
				        //setting the new values for the child nodes
		    		    for(Element child : childElements){
	
		    		        if(child!=null){
		    		            
		    		            if(i<newChildrenValues.size()){
		    		                
			    		            childValue=newChildrenValues.get(i);
			    		            
		    		            }else{
		    		                
		    		                childValue="";
		    		            }

		    		            if(childValue==null){
		    		                
		    		                childValue="";
		    		            }
		    		            
		    		            //setting the new value for the attribute
		    		            child.setAttribute("value", childValue);
                                
                                //notifies that the node has changed
		    		            svgHandle.getSvgDOMListenerManager().fireNodeChanged(child);
		    		        }
		    		        
		    		        i++;
		    		    }
				    }
				}
			};

			Runnable undoRunnable=new Runnable(){
				
				public void run() {

				    //modifying the corresponding value of the current element
				    element.setAttribute("value", oldCorrespondingValue);
					
				    //notifies that the node has been modified
				    svgHandle.getSvgDOMListenerManager().fireNodeChanged(element);
				    
				    //handling the child elements if the tag is an enumerated one
				    if(type!=TagToolkit.ENUMERATED_CHILD && newChildrenValues.size()>0){
				        
				        //setting the new values for the child nodes
				        int i=0;
				        String childValue="";
				        
		    		    for(Element child : childElements){

		    		        if(child!=null){
		    		            
		    		            if(i<oldChildrenValues.size()){
		    		                
			    		            childValue=oldChildrenValues.get(i);
			    		            
		    		            }else{
		    		                
		    		                childValue="";
		    		            }

		    		            if(childValue==null){
		    		                
		    		                childValue="";
		    		            }
		    		            
		    		            //setting the new value of the attribute
		    		            child.setAttribute("value", childValue);
		    		            
		    		            //notifies that the node has changed
		    		            svgHandle.getSvgDOMListenerManager().fireNodeChanged(child);
		    		        }
		    		    }
				    }
				}
			};

			//adding the undo/redo actions
			UndoRedoAction action=new UndoRedoAction(
				dbEditor.labelRtdaDatabaseEditorUndoRedo, 
					executeRunnable, undoRunnable, executeRunnable, new HashSet<Element>());
	
			UndoRedoActionList actionlist=new UndoRedoActionList(
					dbEditor.labelRtdaDatabaseEditorUndoRedo, false);
			actionlist.add(action);
			svgHandle.getUndoRedo().addActionList(actionlist, false);
    	}
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
    
    protected String getRepresentationString(){
        
        String toString="";
        
        if(type==TagToolkit.NOT_A_TAG){
        	
        	toString=id;
        	
        }else{
        	
        	toString="<html><body>"+id+" =\"";
        	
            if(correspondingValue==null || 
            	(correspondingValue!=null && correspondingValue.equals(""))){
                
                toString+="&lt;"+TagToolkit.noneLabel+"&gt;";
                
            }else{
                
                toString+=correspondingValue;
            }
            
            toString+="\"</body></html>";
        }

        return toString;
    }
    
	@Override
    public String toString(){

        return representationString;
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
