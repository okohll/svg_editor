/*
 * Created on 24 mai 2005
 */
package fr.itris.glips.rtda.database;

import java.io.*;
import org.w3c.dom.*;
import java.util.*;
import java.net.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.*;

/**
 * the class of the XML files that are contained in the rtdb
 * 
 * @author ITRIS, Jordi SUC
 */
public class XMLFile{

    /**
     * the lib path map
     */
	private LinkedHashMap<String, LinkedList<String>> libPathsMap;
    
    /**
     * the path of the workspace of the project that is handled
     */
    private String workspacePath="";
    
    /**
     * the name of the project that is handled
     */
    private String projectName="";
    
    /**
     * the name of the xml file
     */
    private String name="";
    
    /**
     * the xml file
     */
    private File xmlFile;
    
    /**
     * the document of this xml file
     */
    private Document doc;
    
    /**
     * the map of the files specified in each inherit attribute, associating the value of the attribute to its
     * related xml file
     */
    private static final Map<String, XMLFile> inheritedFilesMap=new HashMap<String, XMLFile>();
    
    /**
     * the set of the path of the files that are specified in the "inherit" attributes
     */
    private HashSet<String> inheritedFileNames=new HashSet<String>();
    
    /**
     * the set of the elements that contain an "inherit" attribute
     */
    private HashSet<Element> elementWithInheritAttribute=new HashSet<Element>();
    
    /**
     * the constructor of the class
     * @param libPathsMap the lib path map
     * @param workspacePath the path of the workspace of the project that is handled
     * @param projectName the name of the project that is handled
     * @param name the name of the xml file
     */
    public XMLFile(LinkedHashMap<String, LinkedList<String>> libPathsMap, 
    		String workspacePath, String projectName, String name){
        
        this.libPathsMap=libPathsMap;
        this.workspacePath=workspacePath;
        this.projectName=projectName;
        this.name=name;
        
        try{
            //creating the file
        	this.xmlFile=new File(new URI(workspacePath+"/"+projectName+"/"+name));

            //creating the document corresponding of this xml file
            this.doc=DataBaseToolkit.getDocument(xmlFile);
        }catch (Exception ex){ex.printStackTrace();}

        //fills the map of the files specified in each "inherit" attribute
        fillInheritedFilesMap();
    }

    /**
     * @return the xml file
     */
    public File getFile() {
    	
		return this.xmlFile;
	}

	/**
     * @return whether the xml file exists or not
     */
    public boolean exists(){
        
        return this.xmlFile.exists();
    }
    
    /**
     * @return the root element of the document of this xml file
     */
    public Element getRootElement(){
        
        return this.doc.getDocumentElement();
    }
    
    /**
     * creates and returns the single xml file that is the union of all the linked xml files
     * @return the single xml file that is the union of all the linked xml files
     */
    public Document resolveInheritance(){
            
            //creating the list containing the file name of each previously handled files
            LinkedList<String> trajectory=new LinkedList<String>();
            trajectory.add(this.name);
            
            //handles the "inherit" attributes so that they are replaced by the accurate subtree and values for each
            //referenced file in this xml file
            resolveInheritance(trajectory);
            
            //handles the inherit attributes in this file
            replaceInheritAttributes();
            
            //clears the map of the inherited files
            inheritedFilesMap.clear();

        return this.doc;
    }
    
    /**
     * fills the map of the files specified in each inherit attribute, associating the value of the attribute to its
     * related xml file
     */
    protected void fillInheritedFilesMap(){

        if(this.doc!=null){
            
            //for each node in the given document, checks if it has an "inherit" attribute, and then adds the 
            //specified xml file to the map
            String fileName="", value="";
            XMLFile file=null;
            Element root=this.doc.getDocumentElement(), element=null;
            NodeIterator it=new NodeIterator(root);
            Node cur=null;
            
            do{
                
                cur=it.next();
                
                if(cur!=null && cur instanceof Element){

                	element=(Element)cur;
                	
                    if(element.getNodeName().equals("view")){
                        
                        //setting the project name associated with the svg file that is referenced in this view node
                    	element.setAttribute("project", this.projectName);
                    } 
                    
                    if(element.hasAttribute("inherit")){
                        
                        //getting the file name specified in the "inherit" attribute
                        fileName=element.getAttribute("inherit");
                        
                        //adding the filename to the set of the file names of the inherited files
                        if(! this.inheritedFileNames.contains(fileName)){
                            
                            this.inheritedFileNames.add(fileName);
                        }

                        //adding the file to the map of the inherited files
                        if(! inheritedFilesMap.containsKey(fileName)){

                            //getting the xml file associated with this file name
                            file=getXMLFile(fileName);

                            if(file!=null){

                                //putting the file name and the file into the map
                                inheritedFilesMap.put(fileName, file);
                            }
                        }
     
                        //adding the element to the list of the elements that have an "inherit" attribute
                        this.elementWithInheritAttribute.add(element);
                    }

                }else if(cur instanceof Text){

                    value=cur.getNodeValue().replaceAll("\\s+", " ");
                    
                    if(value.equals(" ")){
                        
                        value="";
                    }
                    
                    cur.setNodeValue(value);
                }
                
            }while(it.hasNext());
        }
    }
    
    /**
     * returns the xml file associated with the given file name
     * @param fileName the path of the xml file relatively to the project file
     * @return the xml file associated with the given file name
     */
    protected XMLFile getXMLFile(String fileName){

        XMLFile returnFile=null;

        //if the xml file denotes an xml file
        if(fileName!=null && ! fileName.equals("") && fileName.endsWith(AnimationsToolkit.GLIPS_VIEW_EXTENSION)){
            
            String currentProjectName=getProjectName(this.projectName, fileName);
            
            if(currentProjectName!=null && ! currentProjectName.equals("")){
                
                //creating the xml file
                returnFile=new XMLFile(this.libPathsMap, this.workspacePath, currentProjectName, fileName);

                if(! returnFile.exists()){
                    
                    returnFile=null;
                }
            }
        }
        
        return returnFile;
    }
    
    /**
     * returns the name of the project into which the file denoted by the given file name can be found
     * @param sourceProjectName the project name from which the file path has been found
     * @param fileName a file name relatively to a project path
     * @return the name of the project into which the file denoted by the given file name can be found
     */
    public String getProjectName(String sourceProjectName, String fileName){
        
        String returnedProjectName="";
        
        if(fileName!=null && ! fileName.equals("")){
            
            //getting the list of the projects names that are referenced by the given project name
            LinkedList<String> libPathList=new LinkedList<String>(this.libPathsMap.get(sourceProjectName));
            libPathList.add(sourceProjectName);
            
            File file=null;
            
            //searching for the first project name containing the file denoted by the given file name
            for(String currentProjectName : libPathList){

                if(currentProjectName!=null && ! currentProjectName.equals("")){
                    
                    //creating a file corresponding to the given file name in the current project and checking 
                    //if this exists or not
                    try{
                    	file=new File(new URI(this.workspacePath+"/"+currentProjectName+"/"+fileName));
                    }catch (Exception ex){file=null;}
                    
                    if(file!=null && file.exists()){
                        
                        //as the file denoted by the given file name can be found in the current project, the project name is returned
                        returnedProjectName=currentProjectName;
                        break;
                    }
                }
            }
        }
        
        return returnedProjectName;
    }

    /**
     * resolves the "inherit" attributes 
     * @param trajectory the list of the xml files that have been already parsed
     */
    protected void resolveInheritance(LinkedList<String> trajectory){
        
        if(trajectory!=null && trajectory.size()>0){
            
            LinkedList<String> list=null;
            XMLFile file=null;
            
            for(String fileName : inheritedFileNames){

                //getting the file
                try{file=inheritedFilesMap.get(fileName);}catch (Exception ex) {}
                
                //if the file exists and has not already been handled 
                if(fileName!=null && ! fileName.equals("") && file!=null && file.exists()){

                    if(! trajectory.contains(fileName)){
                        
                        //if the file has not already been handled, the file name is added to the directory 
                        //and the file handles its "inherit" attributes
                        list=new LinkedList<String>(trajectory);
                        list.add(fileName);

                        //resolves the inheritance for each child xml file 
                        file.resolveInheritance(list);

                        //replaces the "inherit" attribute contained by each node by the accurate sub tree 
                        file.replaceInheritAttributes();
                    }
                }
            }
        }
    }
    
    /**
     * replaces the inherit attributes by the accurate subtree
     */
    protected void replaceInheritAttributes(){
        
        if(this.doc!=null){
            
            //for each node in the given document, checks if it has an "inherit" attribute, and then, 
            //adds the tree of the referenced file to the 
            String fileName="";
            XMLFile file=null;
            Element inheritRoot=null;

            for(Element cur : elementWithInheritAttribute){

                if(cur!=null){
                    
                    //getting the file name specified in the "inherit" attribute
                    fileName=cur.getAttribute("inherit");

                    //getting the xml file associated with this file name
                    file=inheritedFilesMap.get(fileName);
                    
                    if(file!=null && file.getRootElement()!=null){

                        //importing the root element of this xml file into the document of the current xml file
                        inheritRoot=(Element)this.doc.importNode(file.getRootElement(), true);
                        
                        //merges the sub tree
                        mergeSubTree(cur, inheritRoot);
                    }
                    
                    //removes the inherit attribute
                    cur.removeAttribute("inherit");
                }
            }
        }
    }
    
    /**
     * merges the two given subtree
     * @param element the subtree that inherits the inherited tree
     * @param inheritedElement the inherited tree
     */
    protected void mergeSubTree(Element element, Element inheritedElement){//TODO
        
        //handles the merging of the attributes of the both given elements
        mergeAttributes(element, inheritedElement);
        
        //building the map associating the id of a child of the given element to this node
        HashMap<String, Element> idToNodeMap=new HashMap<String, Element>();
        Element elt=null;
        
        for(Node cur=element.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
            
            if(cur instanceof Element) {
            	
            	elt=(Element)cur;

            	if(elt.hasAttribute("id")) {
            		
                    idToNodeMap.put(elt.getAttribute("id"), elt);  
            	}
            }
        }
        
        //for each child of the inherited element, checks if it has the same id as the one of the given element, and then merges
        //the two elements, otherwise, the inherited element is simply added
        String id=null;
        Element mergedNode=null;

        NodeList children=inheritedElement.getChildNodes();
        LinkedList<Element> childrenList=Toolkit.getLinkedList(children);
        
        for(Element el : childrenList){
            
            //getting the id attribute
            id=el.getAttribute("id");

            if(id!=null && ! id.equals("") && idToNodeMap.containsKey(id)){
                
                //the two nodes that have the same id are merged
                mergedNode=idToNodeMap.get(id);
                
                if(mergedNode!=null){
                    
                    mergeSubTree(mergedNode, el);
                }
                
            }else{

                //as no node of the given element has the same id as 
            	//the current node of the inherited element, 
                //it is added to the given element
            	inheritedElement.removeChild(el);
                element.appendChild(el);
            }
        }
    }
    
    /**
     * merges the attributes of the two given element
     * @param element an element
     * @param inheritedElement the inherited element
     */
    protected void mergeAttributes(Element element, Element inheritedElement){
        
        //getting all the attributes of the inherited node
        NamedNodeMap inheritedAttributes=inheritedElement.getAttributes();
        Node cur=null;
        
        for(int i=0; i<inheritedAttributes.getLength(); i++){
            
            cur=inheritedAttributes.item(i);
            
            //if the attribute is not the id attribute, and the attribute is not already specified in the first element, 
            //the attribute is added
            if(! cur.getNodeName().equals("id") && ! element.hasAttribute(cur.getNodeName())){
                
                element.setAttribute(cur.getNodeName(), cur.getNodeValue());
            }
        }
    }
}
