/*
 * Created on 10 juin 2005
 */
package fr.itris.glips.compiler.rtdb.file;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.compiler.rtdb.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.action.tagevent.*;

/**
 * the class that enabling to handle the rtda tags
 * 
 * @author ITRIS, Jordi SUC
 */
public class RtdaTagHandler {

    /**
     * the separator
     */
    public static final String separator="::";
    
    /**
     * the name of the root node of the widget database
     */
    public static final String widgetDataBaseRootNodeName="rtda:widgetDatabase";
    
    /**
     * the set of the names of the animation attributes that require a tag name
     * (format : "parent name attribute value"+separator+"tagAttributeName")
     */
    protected HashSet<String> tagAttributes=new HashSet<String>();
    
    /**
     * the set of the names of the animation attributes that require a view name
     * (format : "parent name attribute value"+separator+"viewAttributeName")
     */
    private HashSet<String> viewAttributes=new HashSet<String>();
    
    /**
     * the set of the names of the animation attributes that require a blinking name  
     * ( format : "parent name attribute value"+separator+"blinkingAttributeName")
     */
    private HashSet<String> blinkingAttributes=new HashSet<String>();
    
    /**
     * the set of the strings used to check if the value of a tag attribute is a tag name or not
     */
    private static HashSet<String> notTagValues=new HashSet<String>();
    
    /**
     * the map associating the name of an animation tag attribute to the attribute child node containing one of  the tag values
     *  ( format : "parent name attribute value"+separator+"tagAttributeName")
     */
    private final HashMap<String, String> enumeratedTagToTagValue=new HashMap<String, String>();
    
    /**
     * the map associating the name of an animation attribute that require the values of an enumerated tag
     * to the attribute name that references this enumerated tag
     * (format : "parent name attribute value"+separator+"tagAttributeName")
     */
    private final HashMap<String, String> refAttributeToEnumeratedTag=new HashMap<String, String>();
    
    static {
    	
        //adding the strings used to check if the value of a tag attribute is a tag name or not
        notTagValues.add("auto");
        notTagValues.add("/**returnToPrevious**/");
        notTagValues.add("/**quit**/");
        notTagValues.add("/**closePopupDialog**/");
        notTagValues.add("mouseUpEvent");
        notTagValues.add("infinity");
    }
    
    /**
     * the document specifying the animations and actions
     */
    private Document doc;
    
    /**
     * the constructor of the class
     * @param doc the document specifying the animations and actions
     */
    public RtdaTagHandler(Document doc){

    	this.doc=doc;
    	
        if(doc!=null){

            Node cur=null, node=null;
            String name="", tagAttribute="", elementName="", wholeName="", type="";
            boolean isChildNodeAttribute=false;
            Element el=null;
            
            //for each node, checks if this node contains a "tagtype" and 
            //then adds the value of its "name" attribute to the set
            for(NodeIterator it=new NodeIterator(doc.getDocumentElement()); it.hasNext();){
                
                cur=it.next();
                
                if(cur!=null && cur instanceof Element){
                	
                	el=(Element)cur;
                	type=el.getAttribute("type");
                	
                	if(type!=null && type.equals("blinkingchooser")){
                		
                		wholeName=((Element)el.getParentNode()).getAttribute("name")+
                			separator+el.getAttribute("name");
                		blinkingAttributes.add(wholeName);
                		
                	}else if(el.hasAttribute("tagtype")){

                        name=el.getAttribute("name");

                        if(name!=null && ! name.equals("")){
                        	
                        	wholeName=((Element)el.getParentNode()).getAttribute("name")+separator+name;
                            
                        	if(el.getAttribute("tagtype").equals("view")){

                                viewAttributes.add(wholeName);
                        		
                        	}else{

                                tagAttributes.add(wholeName);
                        	}
                        }
                        
                    }else if(el.hasAttribute("constraint") && el.getAttribute("constraint").equals("tagvalue")){
                        
                        name=el.getAttribute("name");
                        
                        //getting the attribute name of the parent node corresponding to this tag value attribute
                        elementName=((Element)el.getParentNode()).getAttribute("name");

                        //getting the attribute name of the tag attribute corresponding to this tag value
                        tagAttribute="";
                        
                        //the attribute containing the tag value can be found in the child node of an animation node
                        for(node=cur.getParentNode().getParentNode().getFirstChild(); node!=null; node=node.getNextSibling()){
                            
                            if(node instanceof Element && node.getNodeName().equals("attribute") && 
                                    ((Element)node).getAttribute("constraint").equals("tag")){
                                
                                tagAttribute=((Element)node).getAttribute("name");
                                break;
                            }
                        }
                        
                        tagAttribute=((Element)el.getParentNode().getParentNode()).getAttribute("name")+separator+tagAttribute;

                        //creating the extended name of the tag value attribute
                        wholeName=elementName+separator+name;

                        if(wholeName!=null && ! wholeName.equals("") && ! enumeratedTagToTagValue.containsKey(tagAttribute)){
                            
                            enumeratedTagToTagValue.put(tagAttribute, wholeName);
                        }
                        
                    }else if(el.hasAttribute("ref")){
                        
                        name=el.getAttribute("name");
                        
                        //getting the attribute name of the parent node corresponding to this tag value attribute
                        elementName=((Element)el.getParentNode()).getAttribute("name");
                        
                        //whether this attribute belongs to a child element
                        isChildNodeAttribute=cur.getParentNode().getNodeName().equals("child");
                        
                        //getting the attribute name of the tag attribute corresponding to this tag value
                        tagAttribute=el.getAttribute("ref");
                        
                        if(isChildNodeAttribute){
                            
                            tagAttribute=((Element)el.getParentNode().getParentNode()).getAttribute("name")+separator+tagAttribute;
                            
                        }else{
                            
                            tagAttribute=elementName+separator+tagAttribute;
                        }
                        
                        //creating the extended name of the tag value attribute
                        wholeName=elementName+separator+name;

                        if(wholeName!=null && ! wholeName.equals("") && ! refAttributeToEnumeratedTag.containsKey(wholeName)){
                            
                            refAttributeToEnumeratedTag.put(wholeName, tagAttribute);
                        }                      
                    }
                }
            }
        }
    }
 
    /**
     * @return the document
     */
    public Document getDoc() {
		return doc;
	}

	/**
     * returns whether the given attribute name is a tag attribute or not
     * @param nodeName the name of the node containing the given attribute
     * @param attributeName the name of an attribute
     * @return whether the given attribute name is a tag attribute or not
     */
    public boolean isTagAttribute(String nodeName, String attributeName){
        
        boolean isTagAttribute=false;
        
        if(nodeName!=null && ! nodeName.equals("") && attributeName!=null){
            
            String wholeName=nodeName+separator+attributeName;

            if(tagAttributes.contains(wholeName)){
                
                isTagAttribute=true;
            }
        }

        return isTagAttribute;
    }
    
	/**
     * returns whether the given attribute name is a view attribute or not
     * @param nodeName the name of the node containing the given attribute
     * @param attributeName the name of an attribute
     * @return whether the given attribute name is a view attribute or not
     */
    public boolean isViewAttribute(String nodeName, String attributeName){
        
        boolean isTagAttribute=false;
        
        if(nodeName!=null && ! nodeName.equals("") && attributeName!=null){
            
            String wholeName=nodeName+separator+attributeName;

            if(viewAttributes.contains(wholeName)){
                
                isTagAttribute=true;
            }
        }

        return isTagAttribute;
    }
    
    /**
     * returns whether the given tag value is a tag 
     * @param tagName the name of a tag
     * @return whether the given tag value is a tag 
     */
    public boolean isTagName(String tagName){
    	
    	boolean isTagName=false;
    	
    	if(tagName!=null && ! tagName.equals("")){

            //checks if the tagName corresponds to a predefined value
            if(! notTagValues.contains(tagName)){
                
                //checks if the tagName corresponds to a number
                double d=Double.NaN;
                
                try{
                    d=Double.parseDouble(tagName);
                }catch (Exception ex){d=Double.NaN;}
                
                if(Double.isNaN(d)){
                    
                    isTagName=true;
                }
            }
    	}

    	return isTagName;
    }
    
    /**
     * computes and returns the absolute tag name corresponding to the given tag name, 
     * by checking if the given tag name is a real tag name and by concatenating 
     * the tag name to the ref path
     * @param xmlPath the xml path of a view
     * @param attValue the value of an attribute
     * @return the absolute tag name corresponding to the given tag name
     */
    public static String getTagName(String xmlPath, String attValue){//TODO
        
        String newTagName="";
        
        if(xmlPath!=null && ! xmlPath.equals("") && attValue!=null && ! attValue.equals("")){
            
            //checks if the tagName corresponds to a predefined value
            if(! notTagValues.contains(attValue)){
                
            	//removing a percent sign to check if the value is a percentage
            	String numberValue=attValue;
            	int pos=numberValue.indexOf("%");
            	
            	if(pos!=-1){
            		
            		numberValue=numberValue.substring(0, pos);
            	}

                //checks if the tagName corresponds to a number
                double d=Double.NaN;
                
                try{
                    d=Double.parseDouble(numberValue);
                }catch (Exception ex){d=Double.NaN;}
                
                if(Double.isNaN(d)){
                	
                	//checking if the string is a tag name or if it contains a tag name
                	if(attValue.indexOf(TagEventsManager.separator)!=-1){
                		
                		//splitting the value
                		String[] splitValues=attValue.split(TagEventsManager.separatorRegex);
                		
                		if(splitValues!=null && splitValues.length>1 && ! splitValues[1].equals("")){
                			
                			splitValues[1]=normalizeTagName(xmlPath, splitValues[1]);
            				
            				//rebuilding the attribute value
                			attValue="";
                			
            				for(int i=0; i<splitValues.length; i++){
            					
            					attValue+=splitValues[i]+TagEventsManager.separator
            					;
            				}
                		}
                		
                		newTagName=attValue;
                		
                	}else if(attValue.indexOf(Toolkit.startTagInRequest)!=-1){

                		String cValue=attValue;
                		//the list of the parts between the tags
                		LinkedList<String> partsBetweenTags=new LinkedList<String>();
                		//the list of the tag names
                		LinkedList<String> tagNames=new LinkedList<String>();
                		pos=cValue.indexOf(Toolkit.startTagInRequest);
                		int pos2=0;
                		String tagName="";
                		String beforeTagName="";
                		
                		while(pos!=-1){
                			
                			//getting the part before the tag name
                			beforeTagName=cValue.substring(0, pos+Toolkit.startTagInRequest.length());
                			cValue=cValue.substring(pos+Toolkit.startTagInRequest.length(), cValue.length());
                			
                			//getting the tag name
                			pos2=cValue.indexOf(Toolkit.endTagInRequest);
                			
                			if(pos2!=-1){
                				
                				tagName=cValue.substring(0, pos2);
                    			cValue=cValue.substring(pos2+Toolkit.endTagInRequest.length(), cValue.length());
                    			
                    			//modifying the tag name
                    			tagName=normalizeTagName(xmlPath, tagName);
                    			
                			}else{
                				
                				break;
                			}
                			
                			partsBetweenTags.add(beforeTagName);
                			tagNames.add(tagName);
                			
                			pos=cValue.indexOf(Toolkit.startTagInRequest);
                		}
                		
                		partsBetweenTags.add(cValue);
                		
                		//rebuilding the attribute value
                		int i=0;
                		
                		for(String part : partsBetweenTags){
                			
                			newTagName+=part;
                			
                			if(i<tagNames.size()){
                				
                				newTagName+=tagNames.get(i)+"'";
                			}
                			
                			i++;
                		}
                		
                	}else{
                		
                		newTagName=normalizeTagName(xmlPath, attValue);
                	}
                }
            }
        }
        
        return newTagName;
    }
    
    /**
     * computes and returns the tag name that could be found in the provided string
     * @param value a string containing a tag name
     * @return the absolute tag name corresponding to the given tag name
     */
    public static String getRealTagName(String value){
    	
    	//checking if the string is a tag name or if it contains a tag name
    	if(value.indexOf(TagEventsManager.separator)!=-1){
    		
    		//splitting the value
    		String[] splitValues=value.split(TagEventsManager.separatorRegex);
    		
    		if(splitValues!=null && splitValues.length>1 && 
    				! splitValues[1].equals("")){
    			
    			value=splitValues[1];
    		}
    	}
    	
    	return value;
    }
    
    /**
     * normalizes a tag name given its root path
     * @param xmlPath the root path
     * @param tagName the tag name
     * @return the normalized tag name
     */
    protected static String normalizeTagName(String xmlPath, String tagName){
    	
        xmlPath=normalizePath(xmlPath);
        tagName=normalizePath(tagName);

        //removing the last segment of the xml path
        int pos=xmlPath.lastIndexOf("/");
        
        if(pos!=-1){
        	
        	xmlPath=xmlPath.substring(0, pos);
        	
        }else{
        	
        	xmlPath="";
        }
        
        //computes the absolute tag name
        return "/"+xmlPath+"/"+tagName;
    }
    
    /**
     * returns the name of the attribute containing in the children of the node whose name is nodeName, this attribute
     * has one of the enumerated tag values as value
     * @param nodeName the name of a node
     * @param tagAttributeName the name of the attribute containing the tag
     * @return the name of the attribute containing in the children of the node whose name is nodeName, this attribute   
     */
    public String getEnumeratedTagValueAttributeName(String nodeName, String tagAttributeName){
        
        String enumeratedTagValueAttributeName="";
        
        if(nodeName!=null && ! nodeName.equals("") && tagAttributeName!=null && ! tagAttributeName.equals("")){

            enumeratedTagValueAttributeName=enumeratedTagToTagValue.get(nodeName+separator+tagAttributeName);
        }

        return enumeratedTagValueAttributeName;
    }
    
    /**
     * returns whether the given attribute name corresponds to an enumerated tag value
     * @param nodeName the name of a node
     * @param attributeName the name of an attribute
     * @return whether the given attribute name corresponds to an enumerated tag value
     */
    public boolean isEnumeratedTagValueReferenceAttribute(String nodeName, String attributeName){
        
        boolean isEnumeratedTagValueReferenceAttribute=false;
        
        if(nodeName!=null && ! nodeName.equals("") && attributeName!=null && ! attributeName.equals("")){

            isEnumeratedTagValueReferenceAttribute=refAttributeToEnumeratedTag.containsKey(nodeName+separator+attributeName);
        }
        
        return isEnumeratedTagValueReferenceAttribute;
    }
    
    /**
     * returns the attribute name that contains the referenced tag name
     * @param nodeName the name of a node
     * @param attributeName the name of an attribute
     * @return the attribute name that contains the referenced tag name
     */
    public String getEnumeratedTagValueReferenceAttribute(String nodeName, String attributeName){
        
        String enumeratedTagValueReferenceAttribute="";
        
        if(nodeName!=null && ! nodeName.equals("") && attributeName!=null && ! attributeName.equals("")){

            enumeratedTagValueReferenceAttribute=refAttributeToEnumeratedTag.get(nodeName+separator+attributeName);
        }
        
        return enumeratedTagValueReferenceAttribute;
    }
    
    /**
     * returns whether the given attribute name corresponds to a blinking attribute
     * @param nodeName the name of a node
     * @param attributeName the name of an attribute
     * @return whether the given attribute name corresponds to a blinking attribute
     */
    public boolean isBlinkingAttribute(String nodeName, String attributeName){
    	
    	boolean isBlinkingAttribute=false;
    	
    	if(nodeName!=null && attributeName!=null){

    		isBlinkingAttribute=blinkingAttributes.contains(nodeName+separator+attributeName);
    	}
    	
    	return isBlinkingAttribute;
    }
    
    /**
     * normalizes the given path, i.e., removes the '/' character at the start and the end of this file
     * @param path a path
     * @return the normalized path
     */
    public static String normalizePath(String path){
    	
    	String newPath=new String(path);
    	
    	if(! newPath.equals("")){
    		
            if(newPath.startsWith("/")){
                
            	newPath=newPath.substring(1, newPath.length());
            }
            
            if(newPath.endsWith("/")){
                
            	newPath=newPath.substring(0, newPath.lastIndexOf("/"));
            }
    	}
    	
    	return newPath;
    }

}
