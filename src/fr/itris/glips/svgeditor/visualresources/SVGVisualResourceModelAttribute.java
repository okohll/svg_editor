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

import fr.itris.glips.svgeditor.display.handle.*;




/**
 * the class used to describe the attributes of a resource node
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceModelAttribute{
    
    /**
     * the name of the attribute
     */
    private String name="";
    
    /**
     * the default value
     */
    private String defaultValue="";
    
    /**
     * the type of the attribute
     */
    private String type="";
    
    /**
     * the boolean telling if an attribute is in a group attribute or not
     */
    private boolean isInGroup=false;
    
    /**
     * 	the list containing the attributes of this group if this attribute is a group attribute
     */
    private final LinkedList groupAttributes=new LinkedList();
    
    /**
     * the map associating the name of a the description of the content of the attribute to its default value
     */
    private final LinkedHashMap contents=new LinkedHashMap();
    
    /**
     * the map of the possible values for the attribute, associating the name to the value
     */
    private final LinkedHashMap possibleValues=new LinkedHashMap();
    
    /**
     * the visual resources module
     */
    private SVGVisualResources visualResources=null;
    
    /**
     * the constructor of the class
     * @param visualResources the visual resources module
     * @param attributeElement the node of the description of the attribute
     */
    public SVGVisualResourceModelAttribute(SVGVisualResources visualResources, Element attributeElement){
        
        this.visualResources=visualResources;
        
        if(attributeElement!=null){
            
            //the attributes of the node
            name=visualResources.getNormalizedString(attributeElement.getAttribute("name"));
            defaultValue=attributeElement.getAttribute("defaultvalue");
            type=attributeElement.getAttribute("type");
            
            //checks if this attribute belongs to a group attribute
            Node parent=attributeElement.getParentNode();
            
            if(parent!=null){
                
                isInGroup=parent.getNodeName().equals("group");
            }
            
            //checks if this node is a group node and then creates the attribute objects linked to it
            if(attributeElement.getNodeName().equals("group")){

                SVGVisualResourceModelAttribute attr=null;
                
                for(Node cur=attributeElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
                    
                    if(cur instanceof Element){
                        
                        attr=new SVGVisualResourceModelAttribute(visualResources, (Element)cur);
                        groupAttributes.add(attr);
                    }
                }
                
            }else{
                
                //adds the values in the "contents" map and in the possible values map
                String nm="", defValue="", val=""; 
                
                for(Node cur=attributeElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
                    
                    if(cur instanceof Element){
                        
                        //the case for a"content" node
                        if(cur.getNodeName().equals("content")){
                            
                            nm=((Element)cur).getAttribute("name");
                            defValue=((Element)cur).getAttribute("defaultvalue");
                            
                            if(defValue==null){
                                
                                defValue="";
                            }
                            
                            if(name!=null && !name.equals("")){
                                
                                contents.put(name, defValue);
                            }
                            
                            //the case for an "item" node
                        }else if(cur.getNodeName().equals("item")){
                            
                            nm=((Element)cur).getAttribute("name");
                            val=((Element)cur).getAttribute("value");
                            
                            if(val==null){
                                
                                val="";
                            }
                            
                            if(nm!=null && ! nm.equals("")){
                                
                                possibleValues.put(nm, val);
                            }
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
     * @return the name of this attribute
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
     * @return the defaultValue of this attribute
     */
    protected String getDefaultValue(){
        
        return defaultValue;
    }
    
    /**
     * @return the type of this attribute
     */
    protected String getType(){
        
        return type;
    }
    
    /**
     * @return true if an attribute is in a group attribute or not
     */
    protected boolean isInGroup(){
        
        return isInGroup;
    }
    
    /**
     * @return the list of the attributes contained in this node, if this is a group attribute
     */
    protected LinkedList getGroupAttributes(){
        
        return new LinkedList(groupAttributes);
    }
    
    /**
     * @return true if this attribute is a group attribute
     */
    protected boolean isGroupAttribute(){
        
        return groupAttributes.size()>0;
    }
    
    /**
     * @return true if the current attribute has a description of its content
     */
    protected boolean hasContent(){
        
        return contents.size()>0;
    }
    
    /**
     * @return the map associating the name of a the description of the content of the attribute to its default value
     */
    protected LinkedHashMap getContents(){
        
        return new LinkedHashMap(contents);
    }
    
    /**
     * @return the map of the possible values, associating the name to the value
     */
    protected LinkedHashMap getPossibleValues(){
        
        return new LinkedHashMap(possibleValues);
    }
    
    /**
     * creates a resource attribute object given a resource node
     * @param element a resource node or a child node
     * @return a resource object
     */
    protected SVGVisualResourceObjectAttribute createVisualResourceObjectAttribute(Element element){
        
        SVGVisualResourceObjectAttribute resObjAttr=null;
        
        if(element!=null){
            
            LinkedList resObjAttrList=new LinkedList();
            
            if(isGroupAttribute()){

                SVGVisualResourceModelAttribute attModel=null;
                SVGVisualResourceObjectAttribute resObjAttr2=null;
                
                //creates the list of the resource object attribute
                for(Iterator it=groupAttributes.iterator(); it.hasNext();){
                    
                        attModel=(SVGVisualResourceModelAttribute)it.next();
                    
                    if(attModel!=null){
                        
                        resObjAttr2=attModel.createVisualResourceObjectAttribute(element);
                        
                        if(resObjAttr2!=null){
                            
                            resObjAttrList.add(resObjAttr2);
                        }
                    }
                }
            }
            
            SVGHandle svgHandle=visualResources.getSVGEditor().getHandlesManager().getCurrentHandle();
            
            //gets the node attribute
            Node attr=null;
            
            if(! isGroupAttribute()){
                
                attr=visualResources.getVisualResourcesToolkit().getVisualResourceAttributeNode(svgHandle, element, name, defaultValue);
            }

            //creates the resource object
            resObjAttr=new SVGVisualResourceObjectAttribute(attr, this, resObjAttrList);
        }
        
        return resObjAttr;
    }
}
