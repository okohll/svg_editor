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
 * the class used to describe a resource node
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceModel{
    
    /**
     * the name of the type of the resource
     */
    private String name="";
    
    /**
     * the list of the attribute models
     */
    private final LinkedList attributes=new LinkedList();
    
    /**
     * the model for a child
     */
    private SVGVisualResourceModelChild childModel=null;
    
    /**
     * the list of the resource objects
     */
    private final LinkedList resourceObjectsList=new LinkedList();
    
    /**
     * the name of the tab corresponding to an object of this class
     */
    private String tabName="";
    
    /**
     * the default id
     */
    private String defaultId="";
    
    /**
     * the boolean telling if a resource has child shape nodes
     */
    private boolean isShapedResource=false;
    
    /**
     * the visual resources module
     */
    private SVGVisualResources visualResources=null;
    
    /**
     * the defs element
     */
    private Element defs=null;
    
    /**
     * the constructor of a class
     * @param visualResources the visual resources module
     * @param resourceElement the element describing a resource
     */
    public SVGVisualResourceModel(SVGVisualResources visualResources, Element resourceElement){
        
        this.visualResources=visualResources;
        SVGHandle handle=visualResources.getSVGEditor().getHandlesManager().getCurrentHandle();
        defs=visualResources.getDefs(handle);
        
        if(resourceElement!=null && handle!=null){
            
            //the name of the type of the resource
            name=visualResources.getNormalizedString(resourceElement.getAttribute("name"));
            
            //gets the name of the tab
            if(visualResources.getBundle()!=null){
                
                defaultId=resourceElement.getAttribute("vresource_newresource");
                tabName=resourceElement.getAttribute("name");
                
                try{
                    tabName=visualResources.getBundle().getString(tabName);
                }catch (Exception ex){}
            }
            
            Node cur;
            SVGVisualResourceModelAttribute attr=null;
            
            for(cur=resourceElement.getFirstChild(); cur!=null;cur=cur.getNextSibling()){
                
                if(cur instanceof Element){
                    
                    if(cur.getNodeName().equals("attribute") || cur.getNodeName().equals("group")){
                        
                        //creates the attributes models
                        attr=new SVGVisualResourceModelAttribute(visualResources, (Element)cur);
                        attributes.add(attr);
                        
                    }else if(cur.getNodeName().equals("child")){
                        
                        //creates the child model
                        childModel=new SVGVisualResourceModelChild(visualResources, (Element)cur);
                        
                    }else if(cur.getNodeName().equals("shape")){
                        
                        isShapedResource=true;
                    }
                }
            }
            
            //creating the resource objects for the nodes contained in the "defs" node
            if(defs!=null){

                String id="";
                
                for(cur=defs.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
                    
                    if(cur instanceof Element && cur.getNodeName().equals(name)){
                        
                        id=((Element)cur).getAttribute("id");
                        
                        if(id!=null && ! id.equals("")){
                            
                            //creates the resource object
                            createVisualResourceObject((Element)cur);
                        }
                    }
                }
            }
            
            Document visualResourceStore=visualResources.getVisualResourceStore();
            
            //creating the resource objects for the nodes contained in the resource store
            if(visualResourceStore!=null && visualResourceStore.getDocumentElement()!=null){

                String id="";

                for(cur=visualResourceStore.getDocumentElement().getFirstChild(); cur!=null; cur=cur.getNextSibling()){

                    if(cur instanceof Element && cur.getNodeName().equals(name)){
                        
                        id=((Element)cur).getAttribute("id");

                        if(id!=null && ! id.equals("")){

                            //creates the resource object
                            createVisualResourceObject((Element)cur);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * @return Returns the visualResources.
     */
    protected SVGVisualResources getVisualResources() {
        
        return visualResources;
    }
    
    /**
     * @return the name of the type of this resource
     */
    protected String getName(){
        
        return name;
    }
    
    /**
     * @return the absolute name
     */
    protected String getAbsoluteName(){
        
        return visualResources.getAbsoluteString(name);
    }
    
    /**
     * @return the list of the models of the attributes of the resource
     */
    protected LinkedList getAttributes(){
        
        return new LinkedList(attributes);
    }
    
    /**
     * @return the boolean telling if a resource has child shape nodes
     */
    protected boolean isShapedResource(){
        
        return isShapedResource;
    }
    
    /**
     * @return the model of a child
     */
    protected SVGVisualResourceModelChild getChildModel(){
        
        return childModel;
    }
    
    /**
     * @return the list of the resource objects
     */
    protected LinkedList getResourceObjectsList(){
        
        return new LinkedList(resourceObjectsList);
    }
    
    /**
     * @return the name of the tab
     */
    protected String getTabName(){
        
        return tabName;
    }
    
    /**
     * removes the resource object that has the given id
     * @param resourceId the id of a resource
     */
    protected void removeVisualResourceObject(String resourceId){
    	
    	if(resourceId!=null && ! resourceId.equals("")){
    		
    		SVGVisualResourceObject resObj=null;
    		
    		for(Iterator it=new LinkedList(resourceObjectsList).iterator(); it.hasNext();){
    			
    			resObj=(SVGVisualResourceObject)it.next();
    			
    			if(resObj!=null && resObj.getResourceId().equals(resourceId)){
    				
    				resourceObjectsList.remove(resObj);
    			}
    		}
    	}
    }
    
    /**
     * creates a resource object given a resource node
     * @param element a resource node
     * @return a resource object
     */
    protected SVGVisualResourceObject createVisualResourceObject(Element element){

        SVGVisualResourceObject resObj=null;
        
        //the boolean telling if the element could be modified or not
        boolean canBeModified=true;
        
        if(		element!=null && element.getOwnerDocument()!=null && defs!=null && 
                defs.getOwnerDocument()!=null &&
                ! element.getOwnerDocument().equals(defs.getOwnerDocument())){
            
            //sets that the node could not be modified
            canBeModified=false;
        }
        
        if(element!=null){
        	
        	//cloning the element
        	Element cloneElement=(Element)element.cloneNode(true);

            LinkedList resObjAttrList=new LinkedList();
            SVGVisualResourceModelAttribute attModel=null;
            SVGVisualResourceObjectAttribute resObjAttr=null;
            
            //creates the list of the resource object attribute
            for(Iterator it=attributes.iterator(); it.hasNext();){
                
                try{
                    attModel=(SVGVisualResourceModelAttribute)it.next();
                }catch (Exception ex){attModel=null;}
                
                if(attModel!=null){

                    resObjAttr=attModel.createVisualResourceObjectAttribute(cloneElement);
                    
                    if(resObjAttr!=null){
                        
                        resObjAttrList.add(resObjAttr);
                    }
                }
            }
            
            //creates the list of the resource objects of the children of the resource node
            LinkedList resObjChildList=new LinkedList();
            SVGVisualResourceObjectChild resObjChild=null;

            //creates the list of the resource object attribute
            if(childModel!=null){
                
                if(cloneElement.hasChildNodes()){

                    for(Node cur=cloneElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
                        
                        if(cur instanceof Element){
                            
                            resObjChild=childModel.createVisualResourceObjectChild((Element)cur);
                            
                            if(resObjChild!=null){
                                
                                resObjChildList.add(resObjChild);
                            }
                        }
                    }
                    
                }else if(defs!=null && ! cloneElement.hasAttributeNS(EditorToolkit.xmlnsXLinkNS, "href")){

                    //if there are no children, creates one
                    Element child=null;
                    
                    Document doc=defs.getOwnerDocument();
                    
                    if(doc!=null){
                        
                        child=doc.createElement(childModel.getName());
                        cloneElement.appendChild(child);
                    }
                    
                    //creates the child resource object
                    resObjChild=childModel.createVisualResourceObjectChild(child);
                    
                    if(resObjChild!=null){
                        
                        resObjChildList.add(resObjChild);
                    }
                }
            }
            
            //creates the resource object
            resObj=new SVGVisualResourceObject(cloneElement, element, this, resObjAttrList, resObjChildList, canBeModified);
            
            //adds it to the list
            if(resObj!=null){
                
                resourceObjectsList.addFirst(resObj);
            }
        }
        
        return resObj;
    }
}
