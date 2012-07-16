/*
 * Created on 18 avr. 2005
 */
package fr.itris.glips.rtdaeditor;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.resources.*;
import fr.itris.glips.svgeditor.*;

/**
 * the toolkit for the rtda animations editor
 * 
 * @author ITRIS, Jordi SUC
 */
public class EditorAnimationsToolkit{
	
    /**
     * the separator
     */
    public static final String separator="::";
    
	static{
		
		//adding the rtda name space to the list of the required name spaces
		Editor.requiredNameSpaces.put("rtda", Toolkit.rtdaNameSpace);
	}
    
    /**
     * the tag name of the root node of the widget database
     */
    public static final String widgetDatabaseRootTagName="widgetDatabase";
    
    /**
     * the set of the names of the animation attributes that require a tag name
     */
    private static final HashSet<String> tagAttributes=new HashSet<String>();
    
    /**
     * the map associating the name of an animation attribute that require the value of an enumerated tag
     * to the a description object
     */
    private static final HashMap<String, EnumeratedTagValueAttributeDescription> 
    		tagValueAttributes=new HashMap<String, EnumeratedTagValueAttributeDescription>();
    
    /**
     * the map associating the name of an animation attribute that requires the values of an enumerated tag
     * to the a description object
     */
    private static final HashMap<String, EnumeratedTagValueAttributeDescription> 
    									refAttributes=new HashMap<String, EnumeratedTagValueAttributeDescription>();
    
    /**
     * getting information from the rtda animations xml document
     */
    static{
        
        Document doc=null;
        
        try{
        	doc=RtdaResources.getXMLDocument("rtdaAnimationsAndActions.xml");
        }catch (Exception ex){}

        if(doc!=null){

            Node cur=null, node=null;
            Element el=null, el2=null;
            String name="", tagAttribute="", elementName="", wholeName="";
            EnumeratedTagValueAttributeDescription desc=null;
            boolean isChildNodeAttribute=false;
            
            //for each node, checks if this node contains a "tagtype" and then adds the value of its "name" attribute to the set
            for(NodeIterator it=new NodeIterator(doc.getDocumentElement()); it.hasNext();){
                
                cur=it.next();
                
                if(cur!=null && cur instanceof Element){
                    
                	el=(Element)cur;
                	
                    if(el.hasAttribute("tagtype")){
                        
                        name=el.getAttribute("name");
                        
                        if(name!=null && ! name.equals("") && ! tagAttributes.contains(name)){
                            
                            tagAttributes.add(name);
                        }
                        
                    }else if(el.hasAttribute("constraint") && el.getAttribute("constraint").equals("tagvalue")){
                        
                        name=el.getAttribute("name");
                        
                        //getting the attribute name of the parent node corresponding to this tag value attribute
                        elementName=((Element)el.getParentNode()).getAttribute("name");
                        
                        //whether this attribute belongs to a child element
                        isChildNodeAttribute=cur.getParentNode().getNodeName().equals("child");
                        
                        //getting the attribute name of the tag attribute corresponding to this tag value
                        tagAttribute="";
                        
                        if(isChildNodeAttribute){
                            
                            //the attribute containing the tag value can be found in the child node of an animation node
                            for(node=cur.getParentNode().getParentNode().getFirstChild(); 
                            	node!=null; node=node.getNextSibling()){
                                
                                if(node instanceof Element && node.getNodeName().equals("attribute")) {
                                	
                                	el2=(Element)node;
                                	
                                	if(el2.getAttribute("constraint").equals("tag")) {
                                		
                                        tagAttribute=el2.getAttribute("name");
                                        
                                        break;
                                	}
                                }
                            }
                        }
                        
                        //creating the extended name of the tag value attribute
                        wholeName=elementName+separator+name;

                        if(wholeName!=null && ! wholeName.equals("") && 
                        		! tagValueAttributes.containsKey(wholeName)){
                            
                            //creating the description object
                            desc=new EnumeratedTagValueAttributeDescription(
                            		name, elementName, isChildNodeAttribute, tagAttribute);
                            tagValueAttributes.put(wholeName, desc);
                        }
                        
                    }else if(((Element)cur).hasAttribute("ref")){
                        
                        name=((Element)cur).getAttribute("name");
                        
                        //getting the attribute name of the parent node corresponding to this tag value attribute
                        elementName=((Element)cur.getParentNode()).getAttribute("name");
                        
                        //whether this attribute belongs to a child element
                        isChildNodeAttribute=cur.getParentNode().getNodeName().equals("child");
                        
                        //getting the attribute name of the tag attribute corresponding to this tag value
                        tagAttribute=((Element)cur).getAttribute("ref");
                        
                        //creating the extended name of the tag value attribute
                        wholeName=elementName+separator+name;

                        if(wholeName!=null && ! wholeName.equals("") && ! refAttributes.containsKey(wholeName)){
                            
                            //creating the description object
                            desc=new EnumeratedTagValueAttributeDescription(name, elementName, isChildNodeAttribute, tagAttribute);
                            refAttributes.put(wholeName, desc);
                        }                      
                    }
                }
            }
            
            //adding the strings used to check if the value of a tag attribute is a tag name or not
            /*notTagValues.add("auto");
            notTagValues.add("returnToPrevious");
            notTagValues.add("quit");
            notTagValues.add("infinity");*/
        }
    }
    
    /**
     * returns whether the given attribute name is a tag attribute or not
     * @param attributeName the name of an attribute
     * @return whether the given attribute name is a tag attribute or not
     */
    public static boolean isTagAttribute(String attributeName){
        
        boolean isTagAttribute=false;
        
        if(attributeName!=null && tagAttributes.contains(attributeName)){
            
            isTagAttribute=true;
        }
        
        return isTagAttribute;
    }

    /**
     * normalizes the given path, i.e., removes the '/' character at the start and the end of this file
     * @param path a path
     * @return the normalized path
     */
    public static String normalizePath(String path){
    	
    	String newPath="";
    	
    	if(path!=null){
    	    
    	    newPath=new String(path);
    	    newPath=newPath.trim();
        	
        	if(newPath!=null && ! newPath.equals("")){
        		
                if(newPath.startsWith("/")){
                    
                	newPath=newPath.substring(1, newPath.length());
                }
                
                if(newPath.endsWith("/")){
                    
                	newPath=newPath.substring(0, newPath.lastIndexOf("/"));
                }
        	}
    	}
    	
    	return newPath;
    }
    
    /**
     * returns whether the given tag name is used in one of the descendants of the given root element
     * @param root a root element
     * @param tagName the name of a tag
     * @return whether the given tag name is used in one of the descendants of the given root element
     */
    public static boolean isTagUsed(Element root, String tagName){
        
        boolean isTagUsed=false;
        
        if(root!=null && tagName!=null && ! tagName.equals("")){
            
            Node cur=null, att=null;
            NamedNodeMap attributes=null;
            int i=0;
            
            start :
            
            //for each descendant node of the given root element
            for(NodeIterator it=new NodeIterator(root); it.hasNext();){
                
                cur=it.next();
                
                if(cur!=null && cur instanceof Element && cur.getNodeName().startsWith("rtda:")){
                    
                    //getting all the attributes of the node
                    attributes=cur.getAttributes();
                    
                    //for each attribute, checks if it is a tag attribute and if its value corresponds to the given tag name
                    for(i=0; i<attributes.getLength(); i++){
                        
                        att=attributes.item(i);
                        
                        if(att!=null && isTagAttribute(att.getNodeName()) && att.getNodeValue().equals(tagName)){
                            
                            isTagUsed=true;
                            break start;
                        }
                    }
                }
            }
        }
        
        return isTagUsed;
    }
    
    /**
     * converts all the tags referenced in the animation nodes from the old value to the new value
     * @param rootElement a root element
     * @param oldTag the old tag name
     * @param newTag the new tag name
     */
    public static void convertTags(Element rootElement, String oldTag, String newTag){
        
        if(rootElement!=null && oldTag!=null && ! oldTag.equals("") && newTag!=null && ! newTag.equals("")){
            
            Node cur=null, att=null;
            NamedNodeMap attributes=null;
            int i=0;
            String newValue="";

            //for each descendant node of the given root element
            for(NodeIterator it=new NodeIterator(rootElement); it.hasNext();){
                
                cur=it.next();
                
                if(cur!=null && cur instanceof Element && cur.getNodeName().startsWith("rtda:")){
                    
                    //getting all the attributes of the node
                    attributes=cur.getAttributes();
                    
                    //for each attribute, checks if it is a tag attribute and if its value corresponds to the given tag name
                    for(i=0; i<attributes.getLength(); i++){
                        
                        att=attributes.item(i);
                        
                        if(att!=null && isTagAttribute(att.getNodeName()) && att.getNodeValue().startsWith(oldTag)){
                            
                            newValue=att.getNodeValue().replaceFirst(oldTag, newTag);
                            att.setNodeValue(newValue);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * converts all the enumerated tag values referenced in the 
     * animation nodes from the old value to the new value
     * @param rootElement a root element
     * @param tagName the name of the tag
     * @param oldValue the old value
     * @param newValue the new value
     */
    public static void convertEnumeratedTagValue(
    	Element rootElement, String tagName, String oldValue, String newValue){
        
        if(rootElement!=null && tagName!=null && ! tagName.equals("") && 
        	oldValue!=null && ! oldValue.equals("") 
                && newValue!=null && ! newValue.equals("")){
            
            Node cur=null, att=null, nodeToCheck=null;
            NamedNodeMap attributes=null;
            String wholeName="", foundTagName="", value="";
            int i, j;
            String[] splitValue=null;
            EnumeratedTagValueAttributeDescription desc=null;

            //for each descendant node of the given root element
            for(NodeIterator it=new NodeIterator(rootElement); it.hasNext();){
                
                cur=it.next();
                
                if(cur!=null && cur instanceof Element && cur.getNodeName().startsWith("rtda:")){
                    
                    //getting all the attributes of the node
                    attributes=cur.getAttributes();
                    
                    //for each attribute, checks if it is a tag attribute and if its value corresponds to the given tag name
                    for(i=0; i<attributes.getLength(); i++){
                        
                        att=attributes.item(i);
                        
                        if(att!=null){
                            
                            wholeName=cur.getNodeName()+separator+att.getNodeName();
                            
                            //checking if the attribute corresponds to a tag value attribute
                            desc=tagValueAttributes.get(wholeName);//TODO
                            
                            if(desc==null){
                                
                                //checking if the attribute corresponds to a reference tag value to a given referenced node 
                                desc=refAttributes.get(wholeName);
                            }
                            
                            //checking and modifying, if necessary, the value of the attribute
                            if(desc!=null){
                                
                                //getting the tag name corresponding to this description item
                                foundTagName="";
                                
                                if(desc.isChildNodeAttribute()){
                                    
                                    //the attribute containing the tag name can be found in an attribute of the parent node of the current node
                                    nodeToCheck=cur.getParentNode();
                                    
                                }else{
                                    
                                    //the attribute containing the tag name can be found in the current element
                                    nodeToCheck=cur;
                                }
                                
                                if(nodeToCheck!=null){
                                    
                                    foundTagName=((Element)nodeToCheck).getAttribute(desc.getEnumeratedTagAttribute());
                                }
                                
                                if(foundTagName!=null && foundTagName.equals(tagName)){
                                    
                                    //getting the current value of the attribute
                                    value=att.getNodeValue();
                                    
                                    if(value!=null){

                                        if(value.indexOf("|")==-1){
                                            
                                            //the value corresponds to a single tag value
                                            if(value.equals(oldValue)){
                                                
                                                att.setNodeValue(newValue);
                                            }
                                            
                                        }else{
                                            
                                            //the value corresponds to a list of enumerated tag values
                                            splitValue=value.split("[|]");
                                            value="";
                                            
                                            //buidling the new value for the attribute
                                            if(splitValue!=null){
                                                
                                                for(j=0; j<splitValue.length; j++){
                                                    
                                                    if(splitValue[j].equals(oldValue)){
                                                        
                                                        value+=newValue+"|";
                                                        
                                                    }else{
                                                        
                                                        value+=splitValue[j]+"|";
                                                    }
                                                }
                                                
                                                att.setNodeValue(value);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
