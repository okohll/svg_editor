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

/**
 * the class used to describe the child of a resource node
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceModelChild{
    
    /**
     * the name of the child
     */
    private String name="";
    
    /**
     * the type of the child
     */
    private String type="";
    
    /**
     * the list of the attributes of a child
     */
    private final LinkedList attributes=new LinkedList();
    
    /**
     * the visual resources module
     */
    private SVGVisualResources visualResources=null;
    
    /**
     * the constructor of the class
     * @param visualResources the visual resources module
     * @param childElement the node describing the child elements of a resource
     */
    public SVGVisualResourceModelChild(SVGVisualResources visualResources, Element childElement){
        
        this.visualResources=visualResources;
        
        if(childElement!=null){
            
            //the name of the child
            name=visualResources.getNormalizedString(childElement.getAttribute("name"));
            
            //the type of the child
            type=childElement.getAttribute("type");
            
            //the attributes of the child
            SVGVisualResourceModelAttribute attr=null;
            
            for(Node cur=childElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
                
                if(cur instanceof Element){
                    
                    attr=new SVGVisualResourceModelAttribute(visualResources, (Element)cur);
                    attributes.add(attr);
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
     * @return the name of this child
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
     * @return the list of the attributes of the child
     */
    protected LinkedList getAttributes(){
        
        return new LinkedList(attributes);
    }
    
    /**
     * @return the type of the child
     */
    protected String getType(){
        
        return type;
    }
    
    /**
     * creates a resource attribute object given a resource node
     * @param element a resource node or a child node
     * @return a resource object
     */
    protected SVGVisualResourceObjectChild createVisualResourceObjectChild(Element element){
        
        SVGVisualResourceObjectChild resObjChild=null;
        
        if(element!=null){
            
            LinkedList resObjAttrList=new LinkedList();
            SVGVisualResourceModelAttribute attModel=null;
            SVGVisualResourceObjectAttribute resObjAttr=null;
            
            //creates the list of the resource object attribute
            for(Iterator it=attributes.iterator(); it.hasNext();){
                
                    attModel=(SVGVisualResourceModelAttribute)it.next();
                
                if(attModel!=null){
                    
                    resObjAttr=attModel.createVisualResourceObjectAttribute(element);
                    
                    if(resObjAttr!=null){
                        
                        resObjAttrList.add(resObjAttr);
                    }
                }
            }
            
            //creates the resource object
            resObjChild=new SVGVisualResourceObjectChild(element, this, resObjAttrList);
        }
        
        return resObjChild;
    }
    
}
