/*
 * Created on 24 mai 2005
 */
package fr.itris.glips.compiler.rtdb.file;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.compiler.rtdb.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.jwidget.*;
import java.net.*;

/**
 * the class of the SVG files
 *@author ITRIS, Jordi SUC
 */
public class SVGFile {
	
    /**
     * the xmlns prefix
     */
    public static final String xmlNSprefix="xmlns:";
	
    /**
     * the xlink prefix
     */
    public static final String xLinkprefix="xlink:";
    
    /**
     * the xlink namespace
     */
    public static final String xmlnsXLinkNS="http://www.w3.org/1999/xlink";
	
    /**
     * the prefix of the rtda animations
     */
    public static final String rtdaPrefix="rtda:";
	
    /**
     * the namespace of the rtda animations
     */
    public static final String rtdaNameSpace="http://www.itris.fr/2003/animation";
    
    /**
     * strings
     */
    protected static final String imageTagName="image", styleAttribute="style", 
    												rectStyleValue="fill:#ffffff;stroke:none;opacity:0;",
    												noneValue="none", rectTagName="rect", xAtt="x", yAtt="y", wAtt="width", 
    												hAtt="height";
    
    /**
     * the constant REGULAR
     */
    protected static final int REGULAR=0;
    
    /**
     * the constant WIDGET
     */
    protected static final int WIDGET=1;
    
    /**
     * the constant VIEW
     */
    protected static final int VIEW=2;
    
    /**
     * the constant JWIDGET
     */
    protected static final int JWIDGET=3;
    
    /**
     * the constant JWIDGET_ANIMATION
     */
    protected static final int JWIDGET_ANIMATION=4;
    
    /**
     * the constant JWIDGET_ANIMATION
     */
    protected static final int JWIDGET_IMAGE=5;
    
    /**
     * the prefix used for defining a uri in the local file system
     */
    protected static String urlFilePrefix="file:";

    /**
     * the file manager
     */
    protected FileManager fileManager;
    
    /**
     *  the path of the workspace of the project that is handled
     */
    protected String workspacePath="";
    
    /**
     * the name of the project that is handled
     */
    protected String projectName="";
    
    /**
     * the name of the svg file
     */
    protected String name="";
    
    /**
     * the xml path of this svg file
     */
    protected String svgXmlPath="";
    
    /**
     * the path of this svg file
     */
    protected String currentPath="";
    
    /**
     * the path of this svg file for the save action
     */
    protected String savedPath="";
    
    /**
     * the path that will be used to build the path of 
     * all the links that are contained in this svg
     */
    protected String parentPath="";
    
    /**
     * the svg file
     */
    protected File svgFile;
    
    /**
     * the document of this svg file
     */
    protected Document doc;

    /**
     * the set of the svg files that should be saved
     */
    protected final HashSet<SVGFile> filesToBeSaved=new HashSet<SVGFile>();
    
    /**
     * the map associating the location of a svg file to its xml path
     */
    protected static final HashMap<String, String> locationToXmlPath=
    	new HashMap<String, String>();
    
    /**
     * the map associating a xml path to the location of the svg file
     */
    protected static final HashMap<String, String> xmlPathToLocation=
    	new HashMap<String, String>();
    
    /**
     * the set of all the ids used in the application
     */
    protected static final Set<String> allIds=new HashSet<String>();
    
    /**
     * the list of the all the jwidget ids used in the application
     */
    protected static final Set<String> allJWidgetIds=new HashSet<String>();
    
    /**
     * the map associating an old id to a new id that is not a 
     * duplicated id in the application (all the ids are absolute)
     */
    protected static final Map<String, String> oldIdToNewId=
    	new HashMap<String, String>();
    
    /**
     * the map associating an old id to a new id  for a jwidget that is not a 
     * duplicated id in the application
     */
    protected static final Map<String, String> jwidgetOldIdToNewId=
    	new HashMap<String, String>();
    
    /**
     * the map associating the path of a used file to its saved path
     */
    protected static final HashMap<String, String> usedFilesPaths=
    	new HashMap<String, String>();
    
    /**
     * the set of the view files path that should be included
     */
    protected HashSet<String> includedViewSVGFilesPath=new HashSet<String>();
    
    /**
     * the map associating the element of a widget image to this widget file
     */
    protected Map<Element, SVGWidgetFile> includedWidgetSVGFiles=
    	new HashMap<Element, SVGWidgetFile>();
    
    /**
     * the set of the attributes that reference other ids
     */
    protected final HashSet<Node> attributesToBeChecked=new HashSet<Node>();
    
    /**
     * the set of the elements that should be replaced by a whole svg file document
     */
    private final HashSet<Element> viewOrWidgetNodes=new HashSet<Element>();
    
    /**
     * the map associating an absolute path to its corresponding relative path (relative to the current svg file path)
     */
    protected HashMap<String, String> absolutePathToRelativePath=new HashMap<String, String>();
    
    /**
     * whether the ids have been handled or not
     */
    protected boolean hasHandledIds=false;

    /**
     * whether the ids have been handled or not
     */
    protected boolean hasHandledReferencesAndIdsInReferences=false;
    
    /**
     * whether the insertion of the views and widgets has been done
     */
    protected boolean hasBeenInserted=false;
    
    /**
     * whether the insertion of the views and widgets has been done
     */
    protected boolean hasHandledURIs=false;
    
    /**
     * the list of the used tags in this view
     */
    protected HashSet<String> usedTagNames=new HashSet<String>();
    
    /**
     * the constructor of the class
     * @param fileManager the file manager
     * @param workspacePath the path of the workspace of the project that is handled
     * @param projectName the name of the project that is handled
     * @param name the name of the svg file
     * @param xmlPath the xml path of this svg file
     */
    public SVGFile(FileManager fileManager, String workspacePath, 
    		String projectName, String name, String xmlPath){
        
        this.fileManager=fileManager;
        this.workspacePath=workspacePath;
        this.projectName=projectName;
        this.name=name;
        this.svgXmlPath=xmlPath;
        
        currentPath=workspacePath+"/"+projectName+"/"+name;
        
        //creating the svg file and its associated document
        try{
            svgFile=new File(new URI(currentPath));
            doc=FileManager.getSVGDocument(svgFile);
            
            doc.getDocumentElement().removeAttribute("referenceNode");
            doc.getDocumentElement().removeAttribute("referenceFile");
            checkRtdaXmlns(doc);
        }catch (Exception ex){ex.printStackTrace();}
        
        //creating the name and the path that will be used to save the file
        if(name!=null) {

            savedPath=getUniqueSavedPath(name);
            
            locationToXmlPath.put(savedPath, xmlPath);
            xmlPathToLocation.put(xmlPath, savedPath);
        }
    }
    
    /**
     * clears the static fields of the class
     */
    public static void clearStaticElements() {
    	
    	locationToXmlPath.clear();
    	xmlPathToLocation.clear();
    	allIds.clear();
    	allJWidgetIds.clear();
    	oldIdToNewId.clear();
    	jwidgetOldIdToNewId.clear();
    	usedFilesPaths.clear();
    }
    
    /**
     * checks if the given document contains the rtda name space, if not, the namespace is added
     * @param doc a svg document
     */
    protected static void checkRtdaXmlns(Document doc){
        
        if(doc!=null){
            
            Element svgRoot=doc.getDocumentElement();
            
            if(! svgRoot.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:rtda")){
                
                svgRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:rtda", rtdaNameSpace);
            }
        }
    }
    
    /**
     * computes and returns the path of the svg that will be saved
     * @param path the path of the svg file
     * @return the path of the svg that will be saved
     */
    protected String getUniqueSavedPath(String path){

    	String spath=path;
    	
    	if(spath!=null && ! spath.equals("")){

            //getting the last segment of the path
            int pos=spath.lastIndexOf("/");
            
            if(pos!=-1) {
                
                spath=spath.substring(pos+1, spath.length());
            }
            
            //getting the extension of the file
            String extension=FileManager.SVG_FILE_EXTENSION;
            pos=spath.lastIndexOf(extension);
            spath=spath.substring(0, pos);
            String originalPath=spath;
            int i=0;
            
            //checks whether the save path does not already exists among the other view files and the used files
            while(locationToXmlPath.containsKey(
            		fileManager.getOutputDirectory()+"/"+spath+extension) || 
                         usedFilesPaths.containsValue(fileManager.getOutputDirectory()+
                        		 "/"+spath+extension)){
                
                spath=originalPath+i;
                i++;
            }
            
            spath=fileManager.getOutputDirectory()+"/"+spath+extension;
    	}
    	
    	return spath;
    }
    
    /**
     * @return the map of the used paths files
     */
    public static Map<String, String> getUsedFilesPaths(){
        
        return usedFilesPaths;
    }
    
	/**
	 * @return the path of the svg file
	 */
	public String getPath() {
		return currentPath;
	}

	/**
	 * @return Returns the currentPath.
	 */
	public String getCurrentPath() {
		return currentPath;
	}
	
    /**
     * @return whether the svg file exists or not
     */
    public boolean exists(){
        
        return svgFile.exists();
    }

    /**
     * @return the document of this svg file
     */
    public Document getDocument() {
        return doc;
    }
	
	/**
	 * @return Returns the svgXmlPath.
	 */
	public String getSvgXmlPath() {
		return svgXmlPath;
	}

	/**
	 * @return the savedPath
	 */
	public String getSavedPath() {
		return savedPath;
	}

	/**
	 * @return the files to be saved
	 */
	public Set<SVGFile> getFilesToBeSaved(){
		
		HashSet<SVGFile> set=new HashSet<SVGFile>();
		set.add(this);
		computeFilesToBeSaved(set);

		return set;
	}

	/**
	 * computed the files that should be saved
	 * @param files the set of files used to compute the file that should be saved
	 */
	protected void computeFilesToBeSaved(HashSet<SVGFile> files) {
		
		if(files!=null){
			
			files.add(this);

			for(SVGFile file : filesToBeSaved){

				if(file!=null && ! files.contains(file)){

					try{file.computeFilesToBeSaved(files);}catch (Throwable ex){ex.printStackTrace();}
				}
			}
		}
	}
	
    /**
     * creates and returns the compiled svg file corresponding to this file
     * @param compiledXMLFile the compiled xml file
     * @return the compiled svg file corresponding to this file
     */
    public CompiledSVGFile getCompiledFile(CompiledXMLFile compiledXMLFile){
        
        return new CompiledSVGFile(fileManager, savedPath, doc);
    }
    
    /**
     * @return the used tag names
     */
    public HashSet<String> getUsedTagNames() {
        return usedTagNames;
    }
    
    /**
     * checking if all the ids are unique in the 
     * application, and if not, replaces them
     */
    public void handleIds(){
        
        if(! hasHandledIds && doc!=null){
        	
        	//checks the jwidget ids
        	handleJWidgetIds(doc.getDocumentElement());

            //for each node, checks if the id has already been used in the application, (and if so, the id is replaced),
            //and stores the attribute nodes that reference ids//
        	
        	//the set of the nodes that should be removed
        	HashSet<Node> nodesToRemove=new HashSet<Node>();

            Node cur=null;
            String value="";
            Element el=null, rect=null;

            for(NodeIterator it=new NodeIterator(
            		doc.getDocumentElement()); it.hasNext();){

                cur=it.next();
            	
                if(cur!=null){

                    if(cur instanceof Element){
                    	
                    	el=(Element)cur;

                        Toolkit.transformFromStyleToAttribute(el);
                    	
                    	if(el.getNodeName().equals(imageTagName) && 
                    			getType(el)==JWIDGET_IMAGE){
                    		
                    		rect=doc.createElementNS(
                    			doc.getDocumentElement().getNamespaceURI(), "rect");
        					
                    		rect.setAttributeNS(null, xAtt, el.getAttribute(xAtt));
                    		rect.setAttributeNS(null, yAtt, el.getAttribute(yAtt));
                    		rect.setAttributeNS(null, wAtt, el.getAttribute(wAtt));
                    		rect.setAttributeNS(null, hAtt, el.getAttribute(hAtt));
                    		rect.setAttribute(styleAttribute, rectStyleValue);
                    		
                    		//appending the rect element to the document
                    		el.getParentNode().replaceChild(rect, el);

                    		LinkedList<Element> childNodes=Toolkit.getChildrenElements(el);
                    		
                    		//copying all the child nodes of the element into the rect element
                    		for(Element child : childNodes){
                    			
                				rect.appendChild(child);
                    		}

                    		el=rect;
                    		it.setCurrent(rect);

                    	}else{
                        	
                            //finds and stores in a set the attributes that should be 
                    		//checked and the attributes that contains a uri
                            findAttributesWithUriAndHandleTags(el);
                    	}

                    }else if(cur instanceof Text){
                        
                        //handling the text nodes by removing extra spaces
                        value=cur.getNodeValue().replaceAll("\\s+", " ");
                        
                        if(value.equals(" ")){
                            
                            value="";
                        }
                        
                        if(value.equals("")){
                        	
                        	nodesToRemove.add(cur);
                        	
                        }else{
                        	
                            cur.setNodeValue(value);
                        }
                    }
                }
            }
            
            //removing the nodes that should be removed
        	for(Node node : nodesToRemove){
        		
        		node.getParentNode().removeChild(node);
        	}
 
            //invoke the handleIds method on each widget svg files
            for(SVGFile file : includedWidgetSVGFiles.values()){

                if(file!=null){
                    
                    file.handleIds();
                }
            }
            
            hasHandledIds=true;
        }
    }
    
    /**
     * handles all the jwidget ids
     * @param element the element whose jwidget ids in the 
     * subtree should be checked
     */
    protected void handleJWidgetIds(Element element){
    	
       	//getting all the jwidget ids, modifying and storing them
    	NodeList jwidgetElements=element.getElementsByTagName(
    			JWidgetRuntime.jwidgetTagName);

    	if(jwidgetElements!=null && jwidgetElements.getLength()>0){
    		
    		Element jwidgetElement=null;
    		String jwidgetId="";
    		String newJWidgetId="";
    		
    		for(int i=0; i<jwidgetElements.getLength(); i++){
    			
    			jwidgetElement=(Element)jwidgetElements.item(i);
    			jwidgetId=jwidgetElement.getAttribute(
    					JWidgetRuntime.jwidgetIdAttributeName);

    			//computing the new unduplicated jwidget id
    			newJWidgetId=getJWidgetId(jwidgetId);
    			jwidgetElement.setAttribute(
    				JWidgetRuntime.jwidgetIdAttributeName, newJWidgetId);

    			jwidgetOldIdToNewId.put(jwidgetId, newJWidgetId);
    			allJWidgetIds.add(newJWidgetId);
    		}
    	}
    }
    
    /**
     * translates the jwidget ids in the provided attribute node
     * @param attNode an attribute node
     */
    protected void translateJWidgetIds(Node attNode){
    	
    	//checking if this attribute registers a jwidget id
		String value=attNode.getNodeValue();
		
		//for each jwidget id that has been changed, 
		//it is replaced in the value, if it is contained in it
		int pos=0;
		String prefix="", suffix="";
		
		for(String oldId : jwidgetOldIdToNewId.keySet()){
			
			pos=value.indexOf(oldId);
			
			if(pos!=-1){
				
				prefix=value.substring(0, pos);
				suffix=value.substring(pos+oldId.length(), value.length());
				value=prefix+jwidgetOldIdToNewId.get(oldId)+suffix;
			}
		}

		if(value!=null){
			
			attNode.setNodeValue(value);
		}
    }
    
    /**
     * finds and stores in a set the attributes that should be checked and the attributes that contains a uri
     * and handle all the tag values that could be found in the given element
     * @param element an element
     */
    protected void findAttributesWithUriAndHandleTags(Element element){

        if(element!=null){
        	
            //whether the given node is a jwidget element
            boolean isJwidgetElement=element.getNodeName().equals(JWidgetRuntime.jwidgetTagName);
            
            if(isJwidgetElement) {
            	
            	if(element.getAttribute(JWidgetRuntime.jwidgetNameAttributeName).
            			equals("ViewBrowserWidget")) {
            		
            		//getting the value of the initial view
            		String initialViewValue=element.getAttribute("initialView");

            		if(initialViewValue!=null && ! initialViewValue.equals("")) {
            			
            			//computing the absolute view path
            			initialViewValue=RtdaTagHandler.getTagName(svgXmlPath, initialViewValue);
            			element.setAttribute("initialView", initialViewValue);
            			
            			//getting the file corresponding to the xml path and registering so that it is saved
            			SVGFile file=fileManager.getSVGFile(initialViewValue);

            			if(file!=null){
            				
                    		filesToBeSaved.add(file);
            			}
            		}
            		
            	}else if(element.getAttribute(JWidgetRuntime.jwidgetNameAttributeName).
            			equals("TrendsWidget")){
            		
            		//getting the value of the tag 1 and tag 2 attributes
            		String tag1Value=element.getAttribute("tag1");
            		String tag2Value=element.getAttribute("tag2");
            		
            		//modifying the tag values
            		tag1Value=RtdaTagHandler.getTagName(svgXmlPath, tag1Value);
        			element.setAttribute("tag1", tag1Value);
        			
            		tag2Value=RtdaTagHandler.getTagName(svgXmlPath, tag2Value);
        			element.setAttribute("tag2", tag2Value);//TODO
        			
        			usedTagNames.add(tag1Value);
        			usedTagNames.add(tag2Value);
            	}
            	
            }else {
            	
                //whether the given node is a rtda node
                boolean isRtdaNode=
                	element.getNodeName().startsWith(rtdaPrefix) && ! isJwidgetElement;

                Node att=null;
                Element widgetDataBaseRootNode=null;
                String id="", newId="";
                NamedNodeMap attributes=null;
                int i, type=REGULAR;
                SVGWidgetFile widgetFile=null;
                String newTagName="", prjName="", location="";
                
                //computing the type of the node
                type=getType(element);

                if(type==WIDGET || type==VIEW){

                    //modifying the "view" attribute if this node links to a view
                    if(type==VIEW){
                        
                    	//the "view" attribute of the element
                    	att=element.getAttributeNode("view");
                    	
                    	if(att!=null){
                    		
                            //computing the absolute reference node
                            newTagName=RtdaTagHandler.getTagName(svgXmlPath, att.getNodeValue());

                            if(newTagName!=null && ! newTagName.equals("")){
                                
                                att.setNodeValue(newTagName);
                                includedViewSVGFilesPath.add(newTagName);
                                viewOrWidgetNodes.add(element);
                            }
                    	}
                    	
                    }else{
                        
                	    //getting the project name of the widget file
                	    prjName=element.getAttribute("widgetProjectName");
                	    
                	    //getting the relative path of the widget file to this svg file
                	    location=element.getAttributeNS(FileManager.xmlnsXLinkNS, "href");

                	    //absoluting the location
                	    if(location!=null){
                	        
                	        location=getAbsolutePath(location);
                	    }

                	    if(prjName!=null && ! prjName.equals("") && location!=null && ! location.equals("")){
                	        
                	        //the start of the location if it is consistent
                	        String startPath=workspacePath+"/"+prjName+"/";

                	        //getting the widget database root node
                	        NodeList nodes=element.getElementsByTagName(
                	        		RtdaTagHandler.widgetDataBaseRootNodeName);

                	        if(nodes!=null && nodes.getLength()>0){
                	            
                	            widgetDataBaseRootNode=(Element)nodes.item(0);
                	        }

                	        //creating the svg file
                	        if(location.startsWith(startPath)){
                	            
                	            location=location.substring(startPath.length());
                                widgetFile=new SVGWidgetFile(fileManager, 
                                	workspacePath, prjName, location, 
                                		svgXmlPath, widgetDataBaseRootNode);
                                
                	            includedWidgetSVGFiles.put(element, widgetFile);
                                viewOrWidgetNodes.add(element);
                	        }
                	    }
                    }

                }else if(type==REGULAR || type==JWIDGET || type==JWIDGET_ANIMATION){
                	
                    //checking the id and replacing it if it is a duplicated id
                    id=element.getAttribute("id");
                    
                    if(id!=null && ! id.equals("")){
                        
                        if(allIds.contains(id)){
                            
                            newId=getId(id);
                            oldIdToNewId.put(svgXmlPath+FileManager.separator+
                            		currentPath+"#"+id, currentPath+"#"+newId);
                            
                            //setting the new id
                            element.setAttribute("id", newId);
                            allIds.add(newId);
                            
                        }else{
                        	
                        	allIds.add(id);
                        }
                    }
                    
                	//getting the tag handler for this element
                	RtdaTagHandler currentHandler=null;
                	
                	if(isRtdaNode) {
                		
                    	if(type==REGULAR) {
                    		
                    		currentHandler=fileManager.getTagHandlersManager().getTagHandler(element);

                    	}else if(type==JWIDGET_ANIMATION){

                    		//getting the id of the jwidget corresponding to this element
                			String jwidgetId=getJWidgetName(element);

                			if(jwidgetId!=null && ! jwidgetId.equals("")) {
                				
                    			//getting the tag handler
                    			currentHandler=fileManager.getTagHandlersManager().getJWidgetTagHandler(jwidgetId);
                			}
                    	}
                	}

                    //checking the attributes that have references
                    attributes=element.getAttributes();
                    boolean isTagAttribute=false;
                    String wholeName="", newValue="", savePath="";
                    Set<String> colorIds=null;
                    Set<String> usedPaths=null;
                    
                    for(i=0; i<attributes.getLength(); i++){
                        
                        att=attributes.item(i);
                        isTagAttribute=false;
                        
                        if(isRtdaNode && currentHandler!=null){

                        	if(att.getNodeValue().indexOf(JWidgetRuntime.jwidgetPrefixtId)!=-1){
                        		
                            	translateJWidgetIds(att);
                        		
                        	}else if(currentHandler.isBlinkingAttribute(element.getNodeName(), att.getNodeName())){

                        		//the attribute defines a blinking, creating the whole name of the id of the blinking
                        		wholeName=this.projectName+FileManager.separator+att.getNodeValue();

                        		//checking if the value of the attribute is the name of a blinking that has been modified
                        		if(fileManager.getColorsBlinkingsOldIdToNewIdMap().containsKey(wholeName)){

                        			newValue=fileManager.getColorsBlinkingsOldIdToNewIdMap().get(wholeName);
                        			
                        			if(newValue!=null){
                        				
                            			att.setNodeValue(newValue);
                        			}
                        		}
                        		
                        	}else if(currentHandler.isTagAttribute(element.getNodeName(), att.getNodeName()) || 
                        			currentHandler.isViewAttribute(element.getNodeName(), att.getNodeName())){

                                //computing the new name for the tag (the absolute name of the tag relatively to
                                //the reference node of this svg file)
                                newTagName=RtdaTagHandler.getTagName(svgXmlPath, att.getNodeValue());

                                if(newTagName!=null && ! newTagName.equals("")){
                                    
                                    att.setNodeValue(newTagName);
                                    
                                    //getting the real tag name that can be found in the current tag name 
                                    //if the provided string contains a tag name and other information
                                    newTagName=RtdaTagHandler.getRealTagName(newTagName);//TODO
                                   
                                    //adding this tag name into the set of the tag names that are used by this svg file
                                    if(currentHandler.isViewAttribute(element.getNodeName(), att.getNodeName())){

                                    	//registers the referenced views in the attribute value
                                    	registerReferencedViews(att);
                                    	
                                    }else if(! usedTagNames.contains(newTagName)){

                                    	usedTagNames.add(newTagName);
                                    }
                                    
                                    isTagAttribute=true;
                                }
                            }
                        }

                        if(! isTagAttribute && att!=null){

                        	if(att.getNodeValue().indexOf(ColorsBlinkingsFile.start)!=-1){

                                //handles the colors ids//
                        		newValue=att.getNodeValue();
                        		
                        		//getting all the color ids that can be found in the attribute value
                        		colorIds=getAllColorIds(newValue);

                        		//for each found id, checking if a new id exists for it, and then replaces it in the attribute value
                        		for(String foundId : colorIds){

                        			if(foundId!=null && ! foundId.equals("")){
                        				
                        				//getting the new associated id
                        				newId=fileManager.getColorsBlinkingsOldIdToNewIdMap().get(
                        						this.projectName+FileManager.separator+foundId);

                        				if(newId!=null && ! newId.equals("")){
                        					
                        					//replacing all the old id values by the new ones
                        					newValue=newValue.replaceAll("/[*]"+foundId+"[*]/", 
                        							ColorsBlinkingsFile.start+newId+ColorsBlinkingsFile.end);
                        				}
                        			}
                        		}
                        		
                        		//setting the new attribute value
                        		att.setNodeValue(newValue);
                        	}
                        	
                        	if(att.getNodeName().equals("xlink:href") || 
                        			(! att.getNodeName().equals("id") && att.getNodeValue().indexOf("url(")!=-1)){
                                
                                //handles the attributes containing a url
                                attributesToBeChecked.add(att);
                                
                                //retrieving the potential list of paths used in this attribute value
                                usedPaths=getReferencedSVGFilesPath(att);

                                for(String usedPath : usedPaths) {
                                    
                                    if(usedPath!=null && ! usedPath.equals("") && ! usedFilesPaths.containsKey(usedPath)) {
                                        
                                        //computing the path that will be used for saving the used file
                                       savePath=getUsedFileUniqueSavePath(usedPath);

                                       if(savePath!=null && ! savePath.equals("")) {
                                           
                                           //putting the path of the used file and its save path into the map
                                           usedFilesPaths.put(usedPath, savePath);
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
    
    /**
     * registers the referenced view in the given attribute node
     * @param att an attribute node
     */
    protected void registerReferencedViews(Node att) {
    	
    	String path="";
    	SVGFile file=null;
    	
    	//getting the parent element of this attribute
    	Element parent=((Attr)att).getOwnerElement();
		
    	//if this attribute belongs to an element defining a rtda animation 
    	//enabling to change the current view, its path is stored
    	if(parent!=null){
    		
    		//getting the value of the attribute specifying the xml path of the view to go
    		path=att.getNodeValue();

    		if(path!=null && ! path.equals("")){//TODO

    			//getting the file corresponding to the xml path
    			file=fileManager.getSVGFile(path);

    			if(file!=null){

            		filesToBeSaved.add(file);
    			}
    		}
    	}
    }
    
    /**
     * returns the unique path that will be used to save the used file
     * @param path the path of a used file
     * @return the unique path that will be used to save the used file
     */
    protected String getUsedFileUniqueSavePath(String path) {
        
        String savePath=path;
        
        if(savePath!=null && ! savePath.equals("")){
            
            //getting the last segment of the path
            int pos=savePath.lastIndexOf("/");
            
            if(pos!=-1) {
                
                savePath=savePath.substring(pos+1, savePath.length());
            }
                
            //the suffix that will be added to the returned path
            String suffix="-ref";

            //computing the index at which the extension starts
            String extension="";
            pos=savePath.lastIndexOf(".");
            
            if(pos!=-1) {
                
                //getting the extension of the path and removing it from this path
                extension=savePath.substring(pos, savePath.length());
                savePath=savePath.substring(0, pos);
            }
            
            //adding the suffix to the path
            savePath+=suffix;
            
            String originalPath=savePath;
            int i=0;
            
            //checks whether the save path does not already exists among the view files
            //and the other used files
            while(locationToXmlPath.containsKey(
            		fileManager.getOutputDirectory()+"/"+savePath+extension) ||
                        usedFilesPaths.containsValue(
                        		fileManager.getOutputDirectory()+"/"+savePath+extension)){
                
                savePath=originalPath+i;
                i++;
            }
            
            savePath=fileManager.getOutputDirectory()+"/"+savePath+extension;
        }
        
        return savePath;
    }
    
    /**
     * puts the references into their absolute form and replaces the old ids in the references by the new ids
     */
    public void handleReferencesAndIdsInReferences(){

        if(! hasHandledReferencesAndIdsInReferences && doc!=null){

            String newRef="", value="", oldId="";
            Collection<String> col=null;

            for(Node att : attributesToBeChecked){

                if(att!=null){
                    
                    //the value of the attribute
                    value=att.getNodeValue();
                    
                    if(value!=null && ! value.equals("")){
                        
                        //getting the collection of the references that can be found in the current attribute value
                        col=normalizeAndGetReferences(att);

                        if(col!=null && col.size()>0){
                        	
                        	 value=att.getNodeValue();

                            //all the found references will be modified by the new ones
                            for(String ref : col){

                                if(ref!=null){
                                	
                                	oldId=svgXmlPath+FileManager.separator+ref;
                                	
                                	if(oldIdToNewId.containsKey(oldId)){
                                		
                                    	newRef=oldIdToNewId.get(oldId);
                                    	
                                    	if(newRef!=null && ! newRef.equals("")){
                                    		
                                            //replacing the old references by the new ones
                                    		if(att.getNodeName().equals("xlink:href")){
                                    			
                                    			value=newRef;
                                    			
                                    		}else{
                                    			
                                                value=value.replaceAll("url[(]"+ref+"[)]", "url("+newRef+")");
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

            //invoke the handleIds method on each widget svg files
            for(SVGFile file : includedWidgetSVGFiles.values()){

                if(file!=null){
                    
                    file.handleReferencesAndIdsInReferences();
                }
            }
            
            hasHandledReferencesAndIdsInReferences=true;
        }
    }
    
    /**
     * creates and returns a clone of the root element of the current file
     * @param parentSVGFile the file into which the cloned element will be inserted
     * @return a clone of the root element of the current file
     */
    protected Element importElement(SVGFile parentSVGFile){
        
        Element returnElement=null;
        
        if(parentSVGFile!=null && parentSVGFile.exists() && doc!=null && parentSVGFile.getDocument()!=null){
            
            //cloning the root element of the current svg file
            returnElement=(Element)parentSVGFile.getDocument().importNode(doc.getDocumentElement(), true);
        }
        
        return returnElement;  
    }
    
    /**
     * inserts all the referenced view or widgets
     */
    public void insertViewsAndWidgets(){
        
        if(! hasBeenInserted && doc!=null){

            NodeIterator it2;
            SVGFile file=null;
            
            //handles the insertion of all the included view files in this svg file
            for(String xmlPath : includedViewSVGFilesPath){

            	if(xmlPath!=null){
            		
            		file=fileManager.getSVGFile(xmlPath);
            		
                    if(file!=null){
                        
                        file.insertViewsAndWidgets();
                    }
            	}
            }

            Element elementToBeInserted=null, group=null;
            Node node=null, att=null;
            String value="";
            int i, type=REGULAR;
            NamedNodeMap attributes=null;
            boolean isRtdaElement=false;
            String xmlPath="";
            LinkedList<Element> childNodes;
            int elementType;
        	RtdaTagHandler currentHandler=null;
            
            //for each image node, a svg node is inserted and replaces the image node
            for(Element cur : viewOrWidgetNodes){

                if(cur!=null && cur.getParentNode()!=null){
                    
                    //getting the type of the current image node
                    type=getType(cur);
                    file=null;

                    if(type==VIEW){
                        
                        //getting the view attribute
                        xmlPath=cur.getAttribute("view");

                        if(xmlPath!=null && includedViewSVGFilesPath.contains(xmlPath)){
                        		
                        	//getting the corresponding svg file
                            file=fileManager.getSVGFile(xmlPath);
                        }

                    }else if(type==WIDGET){

                        if(includedWidgetSVGFiles.containsKey(cur)){
                            
                            file=includedWidgetSVGFiles.get(cur);
                        }
                    }
                    
                    if(file!=null){

                        elementToBeInserted=file.importElement(this);
                        
                        if(elementToBeInserted!=null){
                        	
                        	if(type==VIEW){
                        		
                        		//modifying each duplicated jwidget id
                        		handleJWidgetIds(elementToBeInserted);
                        		
                        		//adding all the referenced svg files to the files to be saved of this svg file
                        		filesToBeSaved.addAll(file.filesToBeSaved);
                        	}

                            //modifying all the tag names that can be found in the imported view
                            //so that they are absolute in this svg file
                            for(it2=new NodeIterator(elementToBeInserted); it2.hasNext();){
                                
                                node=it2.next();

                                if(node!=null && node instanceof Element){
                                	
                                	isRtdaElement=node.getNodeName().startsWith(rtdaPrefix);
                                	
                                	//getting the tag handler for this element
                                	if(isRtdaElement) {
                                		
                                		//getting the current type of the element
                                		elementType=getType((Element)node);
                                		
                                    	if(elementType==REGULAR) {
                                    		
                                    		currentHandler=fileManager.getTagHandlersManager().getTagHandler((Element)node);
                                    		
                                    	}else if(elementType==JWIDGET_ANIMATION){

                                    		//getting the id of the jwidget corresponding to this element
                                			String jwidgetId=getJWidgetName((Element)node);

                                			if(jwidgetId!=null && ! jwidgetId.equals("")) {
                                				
                                    			//getting the tag handler
                                    			currentHandler=fileManager.getTagHandlersManager().getJWidgetTagHandler(jwidgetId);
                                			}
                                    	}
                                	}

                                    //getting the list of the attributes of the element
                                    attributes=node.getAttributes();
                                    
                                    for(i=0; i<attributes.getLength(); i++){
                                        
                                        att=attributes.item(i);
                                        
                                        //for each attribute that contains a tag name
                                        if(att!=null){
                                        	
                                    		value=att.getNodeValue();
                                    		
                                    		if(type==VIEW && isRtdaElement && 
                                    				value.indexOf(JWidgetRuntime.jwidgetPrefixtId)!=-1){
   
                                    			translateJWidgetIds(att);
                                    			
                                    		}else if(isRtdaElement && currentHandler!=null && 
                                    				(currentHandler.isTagAttribute(node.getNodeName(), att.getNodeName()) ||
                                    						currentHandler.isViewAttribute(node.getNodeName(), att.getNodeName()))){//TODO
                                    			
                                                if(currentHandler.isTagName(value) && 
                                                		! currentHandler.isViewAttribute(node.getNodeName(), att.getNodeName())){
                                                	
                                                	//if the value not only contains a tag name but other information, 
                                                	//only the tag name is stored
                                                	value=RtdaTagHandler.getRealTagName(value);
                                                	
                                                	if(! usedTagNames.contains(value)){
                                                		
                                                        //adding the tag name into the set of the tag names that are used by this svg file
                                                        usedTagNames.add(value);
                                                	}
                                        		}

                                        	}else{
                                        		
                                        		//modifies the uri that can be found in this attribute value, ie, replaces the path of the 
                                        		//imported file by the path of this svg file
                                        		if(att.getNodeName().equals("xlink:href") && value.equals(file.getCurrentPath())){
                                        			
                                        			att.setNodeValue(currentPath);
                                        			
                                        		}else if(att.getNodeValue().indexOf("url("+file.getCurrentPath())!=-1){
                                        			
                                        			value=value.replaceAll("url[(]"+file.getCurrentPath()+"[)]", "url("+currentPath+")");
                                        			value=value.replaceAll("url[(]"+file.getCurrentPath()+"[#]", "url("+currentPath+"#");
                                        			att.setNodeValue(value);
                                        		}
                                        		
                                        		attributesToBeChecked.add(att);
                                        	}
                                        }
                                    }
                                }
                            }

                            //setting all the attributes of the image to the svg file
                            attributes=cur.getAttributes();
                            
                            for(int j=0; j<attributes.getLength(); j++){
                            	
                            	att=attributes.item(j);
                            	
                            	if(att!=null && ! att.getNodeName().startsWith(xLinkprefix) && 
                            			! att.getNodeName().startsWith(xmlNSprefix)){
                            		
                                    elementToBeInserted.setAttribute(att.getNodeName(), att.getNodeValue());
                            	}
                            }
                           
                            group=elementToBeInserted.getOwnerDocument().
                            	createElementNS(elementToBeInserted.getNamespaceURI(), "g");
                            group.setAttributeNS(null, "transform", cur.getAttribute("transform"));
                            group.appendChild(elementToBeInserted);
                            
                            //inserts all the child nodes of the view or the widget but the 
                            //data base root element into the group element
                            childNodes=Toolkit.getChildrenElements(cur);

                            for(Element childNode : childNodes){

                            	if(! childNode.getNodeName().equals(
                            				RtdaTagHandler.widgetDataBaseRootNodeName)){

                            		cur.removeChild(childNode);
                            		group.appendChild(childNode);
                            	}
                            }
                            
                            cur.getParentNode().insertBefore(group, cur);
                            cur.getParentNode().removeChild(cur);
                        }
                    }
                }
            }
            
            //clearing the lists and maps of all the included files
            viewOrWidgetNodes.clear();
            includedViewSVGFilesPath.clear();
            includedWidgetSVGFiles.clear();
            
            hasBeenInserted=true;
        }
    }
    
    /**
     * handles all the uris that can be found in this svg file after all linked views 
     * or widgets have been added, i.e.,  all the absolute uris are turned into relative uris.
     */
    public void handleURIs(){

        if(! hasHandledURIs && doc!=null){

            String newRef="", value="", fragment="", savePath="";
            Collection<String> col=null;
            int pos=-1;

            for(Node att : attributesToBeChecked){

                if(att!=null){
                    
                    //the value of the attribute
                    value=att.getNodeValue();
                    
                    if(value!=null && ! value.equals("")){

                        //getting the collection of the references that can be found in the current attribute value
                        col=getReferences(att);
                        
                        if(col!=null && col.size()>0){
                            
                            //all the found references will be modified so that their path is relative to the current svg file
                            for(String ref : col){

                                if(ref!=null && ! ref.equals("")){
                                    
                                	//getting the fragment
                                	pos=ref.indexOf("#");
                                	
                                	if(pos!=-1){
                                		
                                		fragment=ref.substring(pos, ref.length());
                                		ref=ref.substring(0, pos);
                                		
                                	}else{
                                		
                                		fragment="";
                                	}
                                	
                                	if(ref.equals(currentPath)){
                                		
                                		newRef=fragment;
                                		
                                	}else{
                                		
                                        //getting the name of the file corresponding to this reference in its saved form
                                        savePath=usedFilesPaths.get(ref);

                                        if(savePath!=null) {
                                            
                                            //computing the new reference that is relative to this svg file save path
                                            newRef=getSaveRelativePath(savePath)+fragment;
                                            
                                        }else {
                                            
                                            newRef=getSaveRelativePath(ref)+fragment;
                                        }
                                	}

                                    if(newRef!=null && ! newRef.equals("")){
                                        
                                    	//modifying the value of the attribute
                                        if(att.getNodeName().equals("xlink:href")){
                                        	
                                        	value=newRef;
                                        	
                                        }else{
                                        	
                                        	value=value.replaceAll("url[(]"+ref+fragment+"[)]", "url("+newRef+")");
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //setting the new value
                    att.setNodeValue(value);
                }
            }
            
            hasHandledURIs=true;
        }
    }
 
    /**
     * creates and returns the set of the used file paths
     * @param attribute an attribute
     * @return the set of the used file paths
     */
    protected Set<String> getReferencedSVGFilesPath(Node attribute){

        Set<String> set=new HashSet<String>();

        if(attribute!=null){//TODO
            
            //getting the value of the attribute
            String attributeValue=attribute.getNodeValue();
            
            if(attributeValue!=null && ! attributeValue.equals("")){
                
                String str=new String(attributeValue);
                String reference="";
                
                //checks the type of the reference
                if(attribute.getNodeName().equals("xlink:href")){
                    
                    //putting the reference into an absolute form
                    if(! str.startsWith("#")){
                        
                        int pos=str.indexOf("#");
                        
                        if(pos!=-1) {
                            
                            str=str.substring(0, str.indexOf("#"));
                        }

                        //if the path is not absolute it is added turned into an absolute path and added to the set
                        if(! fileManager.isAbsolutePath(str)) {
                            
                            str=getAbsolutePath(str);
                            set.add(str);
                            
                        }else if(str.startsWith(urlFilePrefix)) {
                            
                            set.add(str);
                        }
                    }

                }else{
                    
                    int pos=-1, pos2=-1;
                    
                    //parsing the string so that all the referenced ids can be found
                    while(str.indexOf("url(")!=-1){
                        
                        pos=str.indexOf("url(");
                        
                        if(pos!=-1){
                            
                            str=str.substring(pos+4, str.length());
                            
                            pos=str.indexOf(")");
                            reference=str.substring(0, pos);
                            
                            if(reference!=null && ! reference.equals("")){
                                
                            	pos2=reference.indexOf("#");
                            	
                            	//checks if the reference contains a path
                            	if(! reference.startsWith("#") && pos2!=-1){

                            		reference=reference.substring(0, pos2);
                            		
                            	}else if(pos2!=-1){

                            		reference=null;
                            	}
                            }
                            
                            if(reference!=null){
                                
                                if(! fileManager.isAbsolutePath(reference)) {
                                    
                                    reference=getAbsolutePath(reference);
                                    
                                    if(reference!=null && ! set.contains(reference)){
                                        
                                        set.add(reference);
                                    }
                                    
                                }else if(str.startsWith(urlFilePrefix)) {
                                    
                                    set.add(reference);
                                }
                            }
                        }
                    }
                }
            }
        }

        return set;
    }
    
    /**
     * creates and returns the list of the absolute references that are used in the given attribute value 
     * @param attribute an attribute with normalized references
     * @return the list of the ids that are used in the given attribute value
     */
    protected Collection<String> getReferences(Node attribute){
        
        HashSet<String> set=new HashSet<String>();
        
        if(attribute!=null){
            
            //getting the value of the attribute
            String attributeValue=attribute.getNodeValue();
            
            if(attributeValue!=null && ! attributeValue.equals("")){
                
                String str=new String(attributeValue);
                String reference="";
                
                //checks the type of the reference
                if(attribute.getNodeName().equals("xlink:href")){

                    if(str.startsWith(urlFilePrefix)) {
                        
                        set.add(str);
                    }
                    
                }else{
                    
                    int pos=-1;
                    
                    //parsing the string so that all the references can be found
                    while(str.indexOf("url")!=-1){
                        
                        pos=str.indexOf("url(");
                        
                        if(pos!=-1){
                            
                            str=str.substring(pos+4, str.length());
                            reference=str.substring(0, str.indexOf(")"));
                            
                            if(reference!=null && ! reference.equals("") && ! set.contains(reference) && str.startsWith(urlFilePrefix)){
                                
                                set.add(reference);
                            }
                        }
                    }
                }
            }
        }

        return set;
    }
    
    /**
     * creates and returns the list of the references that are used in the given attribute value and modifies the uri so that 
     * their format is the same
     * @param attribute an attribute an attribute whith not normalized references
     * @return the list of the ids that are used in the given attribute value
     */
    protected Collection<String> normalizeAndGetReferences(Node attribute){
        
        HashSet<String> set=new HashSet<String>();
        
        if(attribute!=null){
            
            //getting the value of the attribute
            String attributeValue=attribute.getNodeValue();
            
            if(attributeValue!=null && ! attributeValue.equals("")){
                
                String str=new String(attributeValue), newValue=new String(attributeValue);
                String reference="", lastReference="", fragment="";
                
                //checks the type of the reference
                if(attribute.getNodeName().equals("xlink:href")){
                    
                    //putting the reference into an absolute form
                    if(str.startsWith("#")){
                        
                    	//the value of the attribute is just made of a fragment
                        str=currentPath+str;
                        
                    }else{
                    	
                    	str=getAbsolutePath(str);
                    }
                    
                    set.add(str);
                    
                    //setting the value of the attribute with the modified uri
                    attribute.setNodeValue(str);
                    
                }else{
                    
                    int pos=-1, pos2=-1;
                    
                    //parsing the string so that all the referenced ids can be found
                    while(str.indexOf("url(")!=-1){
                        
                        pos=str.indexOf("url(");
                        
                        if(pos!=-1){
                            
                            str=str.substring(pos+4, str.length());
                            reference=str.substring(0, str.indexOf(")"));
                            lastReference=reference;
                            
                            if(reference!=null && ! reference.equals("")){
                                
                            	pos2=reference.indexOf("#");
                            	
                                //putting the reference into an absolute form
                                if(reference.startsWith("#")){
                                    
                                    reference=currentPath+reference;
                                    
                                }else if(pos2!=-1){
                                	
                                	fragment=reference.substring(pos2, reference.length());
                                	reference=getAbsolutePath(reference.substring(0, pos2))+fragment;

                                }else{
                                	
                                	reference=getAbsolutePath(reference);
                                }
                            }
                            
                            if(reference!=null){
                                
                                if(! set.contains(reference)){
                                	
                                	set.add(reference);
                                }

                                //modifies the current reference in the value string
                                newValue=newValue.replaceAll("url[(]"+lastReference+"[)]", "url("+reference+")");
                            }
                        }
                    }

                    //setting the new value for the attribute
                    attribute.setNodeValue(newValue);
                }
            }
        }

        return set;
    }

    /**
     * returns the absolute path corresponding to the given path
     * @param path a path
     * @return the absolute path corresponding to the given path
     */
    protected String getAbsolutePath(String path){

        String absolutePath="";
        
        if(path!=null){
        	
        	//checking if the given path is already an absolute path or not
        	if(fileManager.isAbsolutePath(path)){
        		
        		absolutePath=path;
        		
        	}else{
        		
        		//the given path is relative//
        		
               	if(path.startsWith("/")){
            		
            		path=path.substring(1, path.length());
            	}
        		
        		//creating the path segment of the current svg file relatively to the project name
        		String pathSeg=new String(name);
        		
        		if(pathSeg.indexOf("/")!=-1){
        			
        			pathSeg=pathSeg.substring(0, pathSeg.lastIndexOf("/"));
        			path=pathSeg+"/"+path;	
        		}

                absolutePath=workspacePath+"/"+projectName+"/"+path;
                
                //splitting the path and creating the list of its different parts
                String[] splitPath=FileManager.getSplitPath(absolutePath);
                LinkedList<String> pathList=new LinkedList<String>();
                
                //adding each segment of the path to the list, taking into account ".." segments,
                //i.e. : each ".." found  segment implies that the previous segment is removed from the list
                for(int i=0; i<splitPath.length; i++){
                	
                	if(splitPath[i]!=null && ! splitPath[i].equals("")){
                		
                		if(splitPath[i].equals("..") && pathList.size()>0){
                			
                			pathList.removeLast();
                			
                		}else{
                			
                			pathList.add(splitPath[i]);
                		}
                	}
                }
                
                String segment="";
                absolutePath="";
                
                //building the absolute path
                for(int i=0; i<pathList.size(); i++){
                	
                	segment=pathList.get(i);
                	
                	if(segment!=null && ! segment.equals("")){
                		
                		absolutePath+=segment;
                		
                		if(i!=pathList.size()-1){
                			
                			absolutePath+="/";
                		}
                	}
                }
        	}
        }
        
        return absolutePath;
    }

    /**
     * relativizes the given path to this svg save path
     * @param path a path
     * @return the given path to this svg save path
     */
    protected String getSaveRelativePath(String path){

        String resultPath="";
        
        if(path!=null){
        	
        	String refPath=new String(savedPath);
        	
        	//removing the last segment of the ref path
        	int pos=refPath.lastIndexOf("/");
        	
        	if(pos!=-1){
        		
        		refPath=refPath.substring(0, pos);
        	}
            
            //splitting the path into arrays of strings
            String[] refPathSplit=FileManager.getSplitPath(refPath);
            String[] imagePathSplit=FileManager.getSplitPath(path);
            int i, max=0;
            
            if(refPathSplit!=null && imagePathSplit!=null){

                //for each part of the reference path, tests if this part is the same as the corresponding part 
                //of the image path
                for(i=0; i<refPathSplit.length && i<imagePathSplit.length; i++){

                    if(refPathSplit[i]!=null && imagePathSplit[i]!=null && ! refPathSplit[i].equals(imagePathSplit[i])){
                        
                        max=i;
                        break;
                        
                    }else if(i==refPathSplit.length-1){
                        
                        max=refPathSplit.length;
                        
                    }else if(i==imagePathSplit.length-1){
                        
                        max=imagePathSplit.length;  
                    }
                }
                
                if(max>1){
                	
                    if(max<refPathSplit.length){
                        
                        //buidling the relative path
                        for(i=max; i<refPathSplit.length; i++){
                           
                            resultPath+=("../");
                        }
                    }

                    for(i=max; i<imagePathSplit.length; i++){
                        
                        resultPath+=imagePathSplit[i];
                        
                        if(i!=imagePathSplit.length-1){
                            
                            resultPath+="/";
                        }
                    }
                	
                }else{
                	
                	resultPath=path;
                }
            }
        }

        return resultPath;
    }
    
    /**
     * returns the type of the given node
     * @param node a node
     * @return the type of the given node
     */
    protected int getType(Element node){
        
        int type=REGULAR;
        
        if(node!=null){
            
            if(node.getNodeName().equals("image") && node.hasAttribute("view")){
                
                //the image node is linked to a view
                type=VIEW;
                
            }else if(node.hasAttribute("widgetProjectName")){
                
                type=WIDGET;
                
            }else if(node.getNodeName().equals(JWidgetRuntime.jwidgetTagName)){
            	
            	type=JWIDGET;
            	
            }else if(	isJWidgetAnimation(node) || //TODO
            				(node.getParentNode() instanceof Element && 
            				isJWidgetAnimation((Element)node.getParentNode()))){
            	
		    	type=JWIDGET_ANIMATION;
            	
            }else if(node.getNodeName().equals("image")){
            	
            	//getting the list of the child elements of the node that are jwidgets
            	NodeList jwidgetNodes=node.getElementsByTagName(
            			JWidgetRuntime.jwidgetTagName);
            	
            	if(jwidgetNodes!=null && jwidgetNodes.getLength()>0) {
            		
            		type=JWIDGET_IMAGE;
            	}
            }
        }
        
        return type;
    }
    
    /**
     * returns whether the given element is a jwidget animation or not
     * @param element an element
     * @return whether the given element is a jwidget animation or not
     */
    protected boolean isJWidgetAnimation(Element element) {
    	
    	if(element!=null && element.getNodeName().startsWith(rtdaPrefix)) {
    		
    		Element parentElement=(Element)element.getParentNode();
    		
    		if(! parentElement.getNodeName().equals("svg")){
    			
        		//getting the list of the jwidget elements that are siblings of the given element
        		NodeList jwidgetElements=parentElement.getElementsByTagName(
        				JWidgetRuntime.jwidgetTagName);

        		if(jwidgetElements!=null && jwidgetElements.getLength()>0) {
        			
        			return true;
        		}
    		}

			if(parentElement.getNodeName().startsWith(rtdaPrefix)) {
				
				Node superParentNode=parentElement.getParentNode();
				
				if(superParentNode instanceof Element && 
						superParentNode.getNodeName().equals("image")) {
					
					NodeList jwidgetElements=((Element)superParentNode).
	   					getElementsByTagName(JWidgetRuntime.jwidgetTagName);
	   				
	   				if(jwidgetElements!=null && jwidgetElements.getLength()>0) {
	   					
	   					return true;
	   				}
				}
			}
    	}
    	
    	return false;
    }
    
    /**
     * returns the name of the jwidget to which the given element 
     * that is an animation or action or a child of them belongs
     * @param element a jwidget animation or action or animation child or action child
     * @return the name of the jwidget to which the given element 
     * that is an animation or action or a child of them belongs
     */
    protected String getJWidgetName(Element element) {
    	
    	String jname="";
    	
		if(element.getParentNode()!=null && element.getParentNode() instanceof Element) {
			
			Element jwidgetElement=null;
			Element parentNode=(Element)element.getParentNode();
			
			//getting the jwidget element
			NodeList jwidgetElements=parentNode.getElementsByTagName(JWidgetRuntime.jwidgetTagName);
			
			if(jwidgetElements!=null && jwidgetElements.getLength()>0) {
				
				jwidgetElement=(Element)jwidgetElements.item(0);
				
			}else {
				
				if(parentNode.getParentNode()!=null && parentNode.getParentNode() instanceof Element) {
				
					//getting the parent node of the parent node
					Element superParentNode=(Element)parentNode.getParentNode();
					
					if(superParentNode!=null) {
						
						 jwidgetElements=superParentNode.getElementsByTagName(JWidgetRuntime.jwidgetTagName);
						 
						if(jwidgetElements!=null && jwidgetElements.getLength()>0) {
							
							jwidgetElement=(Element)jwidgetElements.item(0);
						}
					}
				}
			}
			
			if(jwidgetElement!=null) {
				
				jname=jwidgetElement.getAttribute(JWidgetRuntime.jwidgetNameAttributeName);
			}
		}
    	
    	return jname;
    }
    
    /**
     * creates and return the set of the ids of the colors that can be found in the given string
     * @param value a string
     * @return the set of the ids of the colors that can be found in the given string
     */
    protected Set<String> getAllColorIds(String value){
    	
    	HashSet<String> set=new HashSet<String>();
    	
    	if(value!=null && ! value.equals("")){

    		String id="", str=new String(value);
    		int pos=str.indexOf(ColorsBlinkingsFile.start), pos2=str.indexOf(ColorsBlinkingsFile.end);
    		
    		while(pos!=-1 || pos2!=-1){
    			
    			id=str.substring(pos+2, pos2);
    			str=str.substring(pos2+2, str.length());
    			
    			if(id!=null && ! id.equals("")){
    				
    				set.add(id);
    			}
    			
    			pos=str.indexOf(ColorsBlinkingsFile.start);
    			pos2=str.indexOf(ColorsBlinkingsFile.end);
    		}
    	}
    	
    	return set;
    }
    
    /**
     * creates and returns a unique id in the application given a base string
     * @param baseString the base string for the id
     * @return a unique id in the application given a base string
     */
    protected String getId(String baseString){
        
        String id="";
        
        if(baseString!=null && ! baseString.equals("")){

            //computes the first id that is not already used
            for(int i=0; i<allIds.size()+1; i++){
                
                id=baseString+i;
                
                if(! allIds.contains(id)){
                    
                    break;
                }
            }
        }
            
        return id;
    }
    
    /**
     * creates and returns a unique jwidget id in the application given a base string
     * @param baseString the base string for the id
     * @return a unique id in the application given a base string
     */
    protected String getJWidgetId(String baseString){
        
        String id="";
        
        if(baseString!=null && ! baseString.equals("")){

            //computes the first id that is not already used
            for(int i=0; i<allJWidgetIds.size()+1; i++){
                
                id=baseString+i;
                
                if(! allJWidgetIds.contains(id)){
                    
                    break;
                }
            }
        }
            
        return id;
    }
    
	/**
	 * returns the value of a style property
	 * @param element an element
	 * @param name the name of a style property
	 * @return  the value of a style property
	 */
	public static String getStyleProperty(Element element, String name){
		
		String value="";
		
		if(element!=null && name!=null && ! name.equals("")){
			
			//gets the value of the style attribute
			String styleValue=element.getAttribute("style");
			styleValue=styleValue.replaceAll("\\s*[;]\\s*", ";");
			styleValue=styleValue.replaceAll("\\s*[:]\\s*", ":");
			
			int rg=styleValue.indexOf(";".concat(name.concat(":")));
			
			if(rg!=-1){
				
				rg++;
			}
			
			if(rg==-1){
				
				rg=styleValue.indexOf(name.concat(":"));
				
				if(rg!=0){
					
					rg=-1;
				}
			}
			
			//if the value of the style attribute contains the property
			if(! styleValue.equals("") && rg!=-1){
				
				//computes the value of the property
				value=styleValue.substring(rg+name.length()+1, styleValue.length());
				rg=value.indexOf(";");
				value=value.substring(0, rg==-1?value.length():rg);
			}
		}
		
		return value;
	}
	
	/**
	 * setting the value of the given style element for the given node
	 * @param element an element
	 * @param name the name of a style element
	 * @param value the value for this style element
	 */
	public static void setStyleProperty(Element element, String name, String value){
		
		if(element!=null && name!=null && ! name.equals("")){
			
			if(value==null){
				
				value="";
			}
			
			//the separators
			String valuesSep=";", nameToValueSep=":";
			
			//the map associating the name of a property to its value
			HashMap<String, String> values=new HashMap<String, String>();

			//getting the value of the style attribute
			String styleValue=element.getAttribute("style");
			styleValue=styleValue.replaceAll("\\s*[;]\\s*", ";");
			styleValue=styleValue.replaceAll("\\s*[:]\\s*", ":");
			
			//filling the map associating a property to its value
			String[] splitValues=styleValue.split(valuesSep);
			int pos=-1;
			String sname="", svalue="";
			
			for(int i=0; i<splitValues.length; i++){
				
				if(splitValues[i]!=null && ! splitValues[i].equals("")){
					
					pos=splitValues[i].indexOf(nameToValueSep);
					
					sname=splitValues[i].substring(0, pos);
					svalue=splitValues[i].substring(pos+nameToValueSep.length(), splitValues[i].length());
					
					if(! sname.equals("") && ! svalue.equals("")){
						
						values.put(sname, svalue);
					}
				}
			}
			
			//adding the new value
			if(value.equals("")){
				
				values.remove(name);
				
			}else{
				
				values.put(name, value);
			}
			
			//computing the new style value
			styleValue="";
			
			for(String newName : values.keySet()){
				
				styleValue+=newName+nameToValueSep+values.get(newName)+valuesSep;
			}
			
			//sets the value of the style attribute
			if(! styleValue.equals("")){
				
				element.setAttribute("style", styleValue);
				
			}else{
				
				element.removeAttribute("style");
			}
		}
	}
	
}
