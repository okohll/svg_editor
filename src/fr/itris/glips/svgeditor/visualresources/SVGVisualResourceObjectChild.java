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
 * the class containing the values of the attributes of a child of a svg resource node
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceObjectChild{
    
    /**
     * the model of this child
     */
    private SVGVisualResourceModelChild childModel=null;
    
    /**
     * the list of the attributes
     */
    private LinkedList attributes=new LinkedList();
    
    /**
     * the element of the child
     */
    private Element childElement=null;
    
    /**
     * the constructor of the class
     * @param childElement a child of the node of the resource in the svg document
     * @param childModel the model of this child
     * @param attributes the list of the attributes
     */
    public SVGVisualResourceObjectChild(	Element childElement, SVGVisualResourceModelChild childModel, LinkedList attributes){
        
        this.childElement=childElement;
        this.childModel=childModel;
        
        if(attributes!=null){
            
            this.attributes.addAll(attributes);
        }
    }
    
    /**
     * @return the child model
     */
    protected SVGVisualResourceModelChild getChildModel(){
        
        return childModel;
    }
    
    /**
     * @return the child element
     */
    protected Element getChildElement(){
        
        return childElement;
    }
    
    /**
     * @return the list of the attributes of the child resource node
     */
    protected LinkedList getAttributes(){
        
        return new LinkedList(attributes);
    }
    
    /**
     * modifies the values of the nodes of the attributes
     */
    protected void applyChanges(){
        
        //modifies the value of the attribute nodes (if they exist)
        SVGVisualResourceObjectAttribute resAtt=null;
        
        for(Iterator it=attributes.iterator(); it.hasNext();){
            
            try{
                resAtt=(SVGVisualResourceObjectAttribute)it.next();
            }catch (Exception ex){resAtt=null;}
            
            if(resAtt!=null){
                
                resAtt.applyChanges();
            }
        }
    }
}
