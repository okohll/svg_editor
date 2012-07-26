/*
 * Created on 21 janv. 2005
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
package fr.itris.glips.svgeditor.visualresources;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;



/**
 * the class containing the values of a svg resource node
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceObject{
    
    /**
     * the model of this resource
     */
    private SVGVisualResourceModel resourceModel=null;
    
    /**
     * the list of the attributes
     */
    private final LinkedList attributes=new LinkedList();
    
    /**
     * the list of the children
     */
    private final LinkedList children=new LinkedList();
    
    /**
     * the node of the resource
     */
    private Element resourceNode=null;
    
    /**
     * the initial node of the resource
     */
    private Element initialNode=null;
    
    /**
     * tells whether this resource could be modified or not
     */
    private boolean canBeModified=true;
    
    /**
     * the last parent node that can be found
     */
    private Node lastParentNode=null;
    
    /**
     * whether children of this resource can be created
     */
    private boolean canCreateChildren=true;

    /**
     * the constructor of the class
     * @param resourceNode the node of the resource in the svg document
     * @param initialNode the initial resource node
     * @param resourceModel the model of this resource 
     * @param attributes the list of the attributes
     * @param children the list of the children
     * @param canBeModified true if the resource object can be modified
     */
    public SVGVisualResourceObject(Element resourceNode, Element initialNode, SVGVisualResourceModel resourceModel, LinkedList attributes, LinkedList children, boolean canBeModified){
        
        this.resourceNode=resourceNode;
        this.initialNode=initialNode;
        this.resourceModel=resourceModel;
        
        if(attributes!=null){
            
            this.attributes.addAll(attributes);
        }
        
        if(children!=null){
            
            this.children.addAll(children);
        }
        
        this.canBeModified=canBeModified;
        lastParentNode=getParentNode();

        SVGVisualResourceObjectAttribute att=null;
        
        for(Iterator it=attributes.iterator(); it.hasNext();){
            
                att=(SVGVisualResourceObjectAttribute)it.next();
        }
        
        canCreateChildren=(! resourceNode.hasAttributeNS(EditorToolkit.xmlnsXLinkNS, "href"));
    }
    
    /**
     * @return the parent node
     */
    protected Node getParentNode(){
        
        Node parentNode=null;
        
        if(resourceNode!=null){
            
            parentNode=initialNode.getParentNode();
            lastParentNode=parentNode;
        }
        
        return parentNode;
    }
    
    /**
     * @return the resource node
     */
    protected Element getResourceNode(){
        
        return resourceNode;
    }

	/**
	 * @return Returns the initialNode.
	 */
	public Element getInitialNode() {
		return initialNode;
	}
	
    /**
     * @return Returns the resourceModel.
     */
    protected SVGVisualResourceModel getResourceModel() {
        return resourceModel;
    }
    
    /**
     * modifies the values of the nodes of the attributes of the resource node and of its children
     */
    protected void applyChanges(){
        
        //modifies the value of the attribute nodes (if they exist)
        Iterator it;
        SVGVisualResourceObjectAttribute resAtt=null;
        
        for(it=attributes.iterator(); it.hasNext();){
            
                resAtt=(SVGVisualResourceObjectAttribute)it.next();
            
            if(resAtt!=null){
                
                resAtt.applyChanges();
            }
        }
        
        //modifies the value of the attribute nodes of the children nodes (if they exist)
        SVGVisualResourceObjectChild resChild=null;
        
        for(it=children.iterator(); it.hasNext();){
            
                resChild=(SVGVisualResourceObjectChild)it.next();
            
            if(resChild!=null){
                
                resChild.applyChanges();
            }
        }
    }
    
    /**
     * @return the id of the resource
     */
    protected String getResourceId(){

        //for each attribute, searches the "id" attribute and returns its value
        SVGVisualResourceObjectAttribute att=null;
        String idValue="", name="";
        
        for(Iterator it=attributes.iterator(); it.hasNext();){
            
                att=(SVGVisualResourceObjectAttribute)it.next();
            
            if(att!=null){
                
                name=att.getModel().getName();
                
                if(name!=null && name.equals("id")){
                    
                    idValue=att.getValue();
                }
            }
        }

        return idValue;
    }
    
    /**
     * @return true if the resource object can be modified
     */
    protected boolean canBeModified(){
        
        return canBeModified;
    }
    
    /**
     * creates a new child of the resource
     * @return a new child of the resource
     */
    protected SVGVisualResourceObjectChild createNewChild(){
        
        SVGVisualResourceObjectChild child=null;
        
        if(canCreateChildren){
            
            SVGVisualResourceModelChild childModel=resourceModel.getChildModel();
            
            if(childModel!=null && resourceNode!=null && resourceNode.getOwnerDocument()!=null){

                final SVGHandle svgHandle=resourceModel.getVisualResources().
                									getSVGEditor().getHandlesManager().getCurrentHandle();
                
                //creates a new child node
                final Element childElement=resourceModel.getVisualResources().getVisualResourcesToolkit().
                		createVisualResourceChildStructure(svgHandle, resourceNode);
                
                if(childElement!=null){

                    //creates a new resource child object and appends
                    child=childModel.createVisualResourceObjectChild(childElement);
                    
                    //adds the resource child object to the list
                    children.add(child);
                    
                    //appends the child to the parent node
                    resourceNode.appendChild(childElement);
                }
            }
        }

        return child;
    }
    
    /**
     * removes the given child resource object
     * @param resChild a resource child
     */
    protected void removeChildResource(SVGVisualResourceObjectChild resChild){
        
        final SVGHandle svgHandle=resourceModel.getVisualResources().getSVGEditor().getHandlesManager().getCurrentHandle();
        final SVGVisualResourceObjectChild fresChild=resChild;
        
        if(canCreateChildren && resChild!=null && svgHandle!=null){
            
            //removes the child from the dom
            resourceModel.getVisualResources().getVisualResourcesToolkit().removeVisualResourceChild(svgHandle, fresChild.getChildElement());
			
            //removes the child from the children list
            children.remove(resChild);
        }
    }

	/**
	 * @return Returns the children.
	 */
	protected LinkedList getChildren() {
		return children;
	}

	/**
	 * @return Returns the attributes.
	 */
	protected LinkedList getAttributes() {
		return attributes;
	}

    /**
     * @return whether children can be created
     */
    public boolean canCreateChildren() {
        return canCreateChildren;
    }
}
