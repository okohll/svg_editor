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
 * the class containing the value of an attribute of a svg resource node
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceObjectAttribute{
    
    /**
     * the node of the attribute
     */
    private Node attributeNode=null;
    
    /**
     * the model of the attribute
     */
    private SVGVisualResourceModelAttribute attributeModel=null;
    
    /**
     * the value of the attribute and its last value
     */
    private String value="";
    
    /**
     * the list of the attributes contained in a group attribute
     */
    private LinkedList groupAttributes=new LinkedList();
    
    /**
     * the constructor of the class
     * @param attributeNode the attribute node
     * @param attributeModel the model for the attribute
     * @param groupAttributes the list of the attributes contained in this node, if this is a group attribute
     */
    public SVGVisualResourceObjectAttribute(Node attributeNode, SVGVisualResourceModelAttribute attributeModel, LinkedList groupAttributes){

        this.attributeNode=attributeNode;
        this.attributeModel=attributeModel;
        
        if(attributeNode!=null){
            
            this.value=attributeNode.getNodeValue();
        }
        
        if(groupAttributes!=null){
            
            this.groupAttributes.addAll(groupAttributes);
        }
    }
    
    /**
     * @return the value of this attribute
     */
    protected String getValue(){
        
        return value;
    }
    
    /**
     * sets the value
     * @param value the new value
     */
    protected void setValue(String value){
        
        //setting the new value
        this.value=value;
    }
    
    /**
     * modifies the attribute nodes and the group attributes (if they exist) with the current value
     */
    protected void applyChanges(){
        
        //modifies the value of the current attribute node
        if(attributeNode!=null && ! getModel().isGroupAttribute()){
            
            if(value==null || (value!=null && value.equals(""))){
                
                value=attributeModel.getDefaultValue();
            }
            
            attributeNode.setNodeValue(value);
        }
        
        //modifies the value of the attribute nodes of the group attributes (if they exist)
        SVGVisualResourceObjectAttribute resAtt=null;
        
        for(Iterator it=groupAttributes.iterator(); it.hasNext();){
            
            try{
                resAtt=(SVGVisualResourceObjectAttribute)it.next();
            }catch (Exception ex){resAtt=null;}
            
            if(resAtt!=null){
                
                resAtt.applyChanges();
            }
        }
    }
    
    /**
     * @return the list of the attributes contained in this node, if this is a group attribute
     */
    protected LinkedList getGroupAttributes(){
        
        return new LinkedList(groupAttributes);
    }
    
    /**
     * @return the modelof the attribute
     */
    protected SVGVisualResourceModelAttribute getModel(){
        
        return attributeModel;
    }
    
}
