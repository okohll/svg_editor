package fr.itris.glips.rtda.config;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.database.*;
import fr.itris.glips.rtda.toolkit.*;

/**
 * the class of the configuration document
 * @author ITRIS, Jordi SUC
 */
public class ConfigurationDocument {

	/**
	 * the strings
	 */
	protected static String rootGvcFileName="root.gvc", enumeratedTagName="tag.enumerated",
			itemTagName="item", idAttributeName="id", 
						viewAuthorizationLevelAtt="authorizationLevel";
	
	/**
	 * the svg picture
	 */
	private SVGPicture picture;
	
    /**
     * the xml document root element
     */
    private Element rootElement;
    
    /**
     * whether the handled canvases are widgets
     */
    private boolean isWidget=false;
    
    /**
     * the xml file containing the configuration elements
     */
    private File xmlFile;
    
    /**
     * the object handling the rights configuration provided in the database
     */
    private Rights rights;
    
	/**
	 * the map associating the name of a data to its type
	 */
	private ConcurrentHashMap<String, Integer> dataNameToTypeMap=
		new ConcurrentHashMap<String, Integer>();
    
    /**
     * the constructor of the class
     * @param picture a svg picture
     */
    public ConfigurationDocument(SVGPicture picture){
    	
    	this.picture=picture;
    	build();
    }
    
    /**
     * builds the document
     */
    protected void build(){
    	
    	xmlFile=getXMLFile(picture);

    	if(xmlFile!=null){

    		//getting the xml document corresponding to this file
    		rootElement=DataBaseToolkit.getDocument(xmlFile).getDocumentElement();
    		
    	}else{
    		
    		rootElement=DataBaseToolkit.getRtdaWidgetDataBase(
    				picture.getDocument());
    		isWidget=true;
    	}
    	
    	rights=new Rights(rootElement);
    	
    	//computing the map associating a tag name to its type for all 
    	//the tags that can be found in the document
    	computeTagTypes();
    }
    
    /**
     * returns the xml file corresponding to the provided picture
     * @param pict a picture
     * @return the xml file corresponding to the provided picture
     */
    protected File getXMLFile(SVGPicture pict){
    	
    	File newXMLFile=null;
    	
    	//getting the xml file
    	File projectFile=picture.getCanvas().getProjectFile();
    	File[] children=projectFile.listFiles();
    	
    	for(int i=0; i<children.length; i++){
    		
    		if(children[i]!=null && children[i].getName().equals(rootGvcFileName)){
    			
    			newXMLFile=children[i];
    			break;
    		}
    	}
    	
    	return newXMLFile;
    }
    
    /**
     * @return the root element
     */
    public Element getRootElement() {
		return rootElement;
	}
    
    /**
     * returns the authorization level for the view denoted by the provided view path
     * @param viewPath the view path
     * @return the authorization level for the view denoted by the provided view path
     */
    public int getAuthorizationLevelForView(String viewPath){
    	
    	int authorizationLevel=0;
    	
    	//getting the element corresponding to the provided view path
    	Element viewElement=getElement(viewPath);

    	//getting the authorization level for the view
    	try{
        	authorizationLevel=Integer.parseInt(
        		viewElement.getAttribute(viewAuthorizationLevelAtt));
    	}catch (Exception ex){}
    	
    	return authorizationLevel;
    }
    
    /**
     * returns the element corresponding to the tag name
     * @param tagName a tag name
     * @return the element corresponding to the tag name
     */
    public Element getElement(String tagName){
    	
    	Element tagElement=null;
    	
    	if(tagName!=null){
    		
          	//splitting the tag name into segments
        	String[] segments=tagName.split("/");
        	
        	//creating the list of the segments that are non empty string values
        	LinkedList<String> segmentsList=new LinkedList<String>();
        	
        	for(int i=0; i<segments.length; i++){
        		
        		if(segments[i]!=null && ! segments[i].equals("")){
        			
            		segmentsList.add(segments[i]);
        		}
        	}

        	Element element=rootElement;
        	Element cEl=null;
        	Node node=null;
        	NodeList childNodes=null;
        	
        	if(isWidget && element!=null){
        		
        		segmentsList.addFirst(element.getAttribute(idAttributeName));
        	}

        	if(element!=null && segmentsList.size()>0 &&
        			element.getAttribute(idAttributeName).equals(segmentsList.get(0))){

        		for(int i=1; i<segmentsList.size(); i++){
        			
        			childNodes=element.getChildNodes();
        			tagElement=null;
        			
        			//getting the element whose id matches the current segment
        			for(int j=0; j<childNodes.getLength(); j++){
        				
        				node=childNodes.item(j);

        				if(node!=null && node instanceof Element){
        					
        					cEl=(Element)node;
        					
        					if(cEl.getAttribute(idAttributeName).equals(segmentsList.get(i))){
        						
        						tagElement=cEl;
            					element=cEl;
            					break;
        					}
        				}
        			}
        			
        			if(tagElement==null){

        				break;
        			}
        		}
        	}
    	}
    	
    	return tagElement;
    }
    
    /**
	 * computes the map associating a tag name to its type for all 
     * the tags that can be found in the document
     */
    protected void computeTagTypes(){
    	
    	Node node;
    	Element element;
    	String tagName="";
    	int tagType=0;
    	
    	for(NodeIterator iterator=new NodeIterator(rootElement); 
    		iterator.hasNext();){
    		
    		node=iterator.next();
    		
    		if(node!=null && node instanceof Element && 
    				node.getNodeName().startsWith("tag.")){
    			
    			element=(Element)node;
    			
    			//getting the tag name corresponding to this element
    			tagName=TagToolkit.getPath(element);
    			
    			if(tagName!=null){
    				
    				//getting the type of the tag
        			tagType=TagToolkit.getNodeType(element.getNodeName());
        			
        			if(tagType!=-1){
        				
        				dataNameToTypeMap.put(tagName, tagType);
        			}
    			}
    		}
    	}
    }
    
    /**
     * returns the element corresponding to the tag name
     * @param tagName a tag name
     * @return the element corresponding to the tag name
     */
    public int getTagType(String tagName){
    	
    	Integer tagType=dataNameToTypeMap.get(tagName);
    	
    	if(tagType==null){
    		
    		tagType=TagToolkit.NOT_A_TAG;
    	}
    	
    	return tagType;
    }
    
	/**
	 * returns the child values of the tag defined by the tag name
	 * @param tagName an enumerated tag name
	 * @return the child values of the tag defined by the tag name
	 */
    public LinkedList<String> getChildValues(String tagName){
    	
    	//the list that will be returned
    	LinkedList<String> tagValues=new LinkedList<String>();
    	
    	//getting the tag element
    	Element tagElement=getElement(tagName);

    	if(tagElement!=null){

    		//getting the item nodes of the element, so that the tag values can be stored
    		NodeList items=tagElement.getChildNodes();
    		String id="";
        	Node node=null;
        	Element cEl=null;
    		
    		for(int i=0; i<items.getLength(); i++){
    			
    			node=items.item(i);
    			
    			if(node!=null && node instanceof Element){
    				
    				cEl=(Element)node;
    				id=cEl.getAttribute(idAttributeName);
    				
    				if(id!=null && ! id.equals("")){
    					
    					tagValues.add(
    						AnimationsToolkit.normalizeEnumeratedValue(id));
    				}
    			}
    		}
    	}

    	return tagValues;
    }
    
    /**
     * @return whether the svg document corresponding to
     * this configuration document is a widget
     */
    public boolean isWidget(){
    	
    	return isWidget;
    }

	/**
	 * @return the object storing information 
	 * on the rights of the application
	 */
	public Rights getRights() {
		
		return rights;
	}
}
