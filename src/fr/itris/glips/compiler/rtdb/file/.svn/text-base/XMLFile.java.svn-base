/*
 * Created on 24 mai 2005
 */
package fr.itris.glips.compiler.rtdb.file;

import java.io.*;
import fr.itris.glips.compiler.rtdb.*;
import fr.itris.glips.library.*;
import org.w3c.dom.*;
import java.util.*;
import java.net.*;

/**
 * the class of the XML files that are contained in the rtdb
 * 
 * @author ITRIS, Jordi SUC
 */
public class XMLFile{
    
    /**
     * the file manager
     */
    private FileManager fileManager;
    
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
     * the path of the compiled file
     */
    private String savedPath="";
    
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
     * the map associating the name of a database file (when it is saved) to the source data base file 
     */
    private static final Map<String, File> dataBaseFilesMap=new HashMap<String, File>();
    
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
     * @param fileManager the file manager
     * @param workspacePath the path of the workspace of the project that is handled
     * @param projectName the name of the project that is handled
     * @param name the name of the xml file
     */
    public XMLFile(FileManager fileManager, String workspacePath, String projectName, String name){
        
        this.fileManager=fileManager;
        this.workspacePath=workspacePath;
        this.projectName=projectName;
        this.name=name;
        
        try{
            //creating the file
        	xmlFile=new File(new URI(workspacePath+"/"+projectName+"/"+name));

            //creating the document corresponding to this xml file
            doc=FileManager.getDocument(xmlFile);
        }catch (Exception ex){ex.printStackTrace();}
        
        //creating this compiled file path
        if(name!=null) {

            savedPath=fileManager.getOutputDirectory()+"/"+
            	FileManager.COMPILED_GLIPS_VIEW_FILE;
        }
    }
    
    /**
     * fills the map of the files specified in each inherit attribute, associating the value of the attribute to its
     * related xml file. Also fills the map of the data base files specified in each inherited xml file
     * @throws MissingFileException the exception raised if a required data base file is missing
     */
    public void initialize() throws MissingFileException{
        
        if(doc!=null){
            
            //for each node in the given document, checks if it has an "inherit" attribute, and then adds the 
            //specified xml file to the map
            String fileName="", value="";
            XMLFile file=null;
            Element root=doc.getDocumentElement(), el=null;
            NodeIterator it=new NodeIterator(root);
            Node cur=null;
            
            do{
                cur=it.next();
                
                if(cur!=null && cur instanceof Element){

                    el=(Element)cur;
                    
                    if(el.getNodeName().equals("view")){
                        
                        //setting the project name associated with the svg file that is referenced in this view node
                        el.setAttribute("project", projectName);
                        
                    } else if(el.getNodeName().equals("comserver.table")) {
                    	
                    	//getting the database file name
                    	String dataBaseFileName=el.getAttribute("description");
                    	
                    	if(! dataBaseFileName.equals("")) {

                    		String savedDataBaseFileName=getUniqueDataBaseFileName(dataBaseFileName);

                    		if(! savedDataBaseFileName.equals("")) {
                    			
                    			//setting the new value for the database file name attribute
	                    		el.setAttribute("description", savedDataBaseFileName);
	                    		
	                    		//getting the data base file name
	                    		String parentXMLFilePath=xmlFile.toURI().toASCIIString();
	                    		
	                    		int pos=parentXMLFilePath.lastIndexOf("/");
	                    		
	                    		if(pos!=-1) {
	                    			
	                    			parentXMLFilePath=parentXMLFilePath.substring(0, pos);
	                    		}
	                    		
	                    		if(! parentXMLFilePath.endsWith("/")) {
	                    			
	                    			parentXMLFilePath+="/";
	                    		}
	                    		
	                    		if(dataBaseFileName.startsWith("/")) {
	                    			
	                    			dataBaseFileName=dataBaseFileName.substring(1, dataBaseFileName.length());
	                    		}
	                    		
	                    		String absoluteDataBaseFileName=parentXMLFilePath+dataBaseFileName;

	                    		//creating the data base file
	                    		File dataBaseFile=null;
	                    		
	                    		try {
	                    			dataBaseFile=new File(new URI(absoluteDataBaseFileName));
	                    		}catch (Exception ex) {}
	                    		
	                    		if(dataBaseFile!=null) {

	                    			dataBaseFilesMap.put(savedDataBaseFileName, dataBaseFile);
	                    			
	                    		}else {
	                    			
	                    			throw new MissingFileException(dataBaseFileName);
	                    		}
                    		}	
                    	}
                    }
                    
                    if(el.hasAttribute("inherit")){

                        //getting the file name specified in the "inherit" attribute
                        fileName=el.getAttribute("inherit");

                        //adding the filename to the set of the file names of the inherited files
                        if(! inheritedFileNames.contains(fileName)){
                            
                            inheritedFileNames.add(fileName);
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
                        elementWithInheritAttribute.add(el);
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
     * clears the static fields of the class
     */
    public static void clearStaticElements() {
    	
    	inheritedFilesMap.clear();
    	dataBaseFilesMap.clear();
    }

    /**
     * @return Returns the fileManager.
     */
    public FileManager getFileManager() {
    	
        return fileManager;
    }
    
    /**
     * @return whether the xml file exists or not
     */
    public boolean exists(){
        
        return xmlFile.exists();
    }
    
    /**
     * @return the root element of the document of this xml file
     */
    public Element getRootElement(){
        
        return doc.getDocumentElement();
    }
    
    /**
     * creates and returns the single xml file that is the union of all the linked xml files
     * @param destinationDirectory the directory into which the xml file will be written
     * @return the single xml file that is the union of all the linked xml files
     * @throws InvalidInheritedNodeException the exception raised if there is a cycle in the inheritance node
     */
    public CompiledXMLFile compile(String destinationDirectory) throws InvalidInheritedNodeException{

        CompiledXMLFile compiledFile=null;

        if(destinationDirectory!=null && ! destinationDirectory.equals("")){
            
            //creating the list containing the file name of each previously handled files
            LinkedList<String> trajectory=new LinkedList<String>();
            trajectory.add(name);

            //handles the "inherit" attributes so that they are replaced by the accurate subtree and values for each
            //referenced file in this xml file
            resolveInheritance(trajectory);

            //handles the inherit attributes in this file
            replaceInheritAttributes();

            //creating the compiled xml file
            compiledFile=new CompiledXMLFile(fileManager, savedPath, doc, fileManager.getRootViewPath(), 
            				new HashMap<String, File>(dataBaseFilesMap));
        }
        
        inheritedFilesMap.clear();
        dataBaseFilesMap.clear();
        
        return compiledFile;
    }
    
    /**
     * returns the xml file associated with the given file name
     * @param fileName the path of the xml file relatively to the project file
     * @return the xml file associated with the given file name
     * @throws MissingFileException the exception raised if a required data base file is missing
     */
    protected XMLFile getXMLFile(String fileName) throws MissingFileException{

        XMLFile returnFile=null;

        //if the xml file denotes an xml file
        if(fileName!=null && ! fileName.equals("") &&
        		fileName.endsWith(FileManager.GLIPS_VIEW_EXTENSION)){
            
            String currentProjectName=fileManager.getProjectName(projectName, fileName);
            
            if(currentProjectName!=null && ! currentProjectName.equals("")){

                //creating the xml file
                returnFile=new XMLFile(fileManager, workspacePath, currentProjectName, fileName);
                returnFile.initialize();

                if(! returnFile.exists()){
                    
                    returnFile=null;
                }
            }
        }
        
        return returnFile;
    }

    /**
     * resolves the "inherit" attributes 
     * @param trajectory the list of the xml files that have been already parsed
     * @throws InvalidInheritedNodeException the exception raised if there is a cycle in the inheritance node
     */
    protected void resolveInheritance(LinkedList<String> trajectory)
    	throws InvalidInheritedNodeException{
        
        if(trajectory!=null && trajectory.size()>0){
            
            LinkedList<String> list=null;
            XMLFile file=null;
            
            for(String fileName : inheritedFileNames){

                //getting the file
                file=inheritedFilesMap.get(fileName);
                
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
                        
                    }else{
                        
                        //if the file has already been handled, an exception is raised
                        throw new InvalidInheritedNodeException(name, fileName);
                    }
                }
            }
        }
    }
    
    /**
     * replaces the inherit attributes by the accurate subtree
     */
    protected void replaceInheritAttributes(){
        
        if(doc!=null){
            
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
                        inheritRoot=(Element)doc.importNode(file.getRootElement(), true);
                        
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
    protected void mergeSubTree(Element element, Element inheritedElement){
        
        //handles the merging of the attributes of the both given elements
        mergeAttributes(element, inheritedElement);
        
        //building the map associating the id of a child of the given element to this node
        HashMap<String, Element> idToNodeMap=new HashMap<String, Element>();
        Element elt=null;
        
        for(Node cur=element.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
            
            if(cur instanceof Element){
                
            	elt=(Element)cur;
                
                if(elt.hasAttribute("id")) {
                    
                    idToNodeMap.put(elt.getAttribute("id"), elt);
                }
            }
        }
        
        //for each child of the inherited element, checks if it has the same id 
        //as the one of the given element, and then merges
        //the two elements, otherwise, the inherited element is simply added
        String id=null;
        Node mergedNode=null;
        LinkedList<Element> children=
        	Toolkit.getLinkedList(inheritedElement.getChildNodes());
  
        for(Element el : children){

            //getting the id attribute
            id=el.getAttribute("id");

            if(id!=null && ! id.equals("") && idToNodeMap.containsKey(id)){
                
                //the two nodes that have the same id are merged
                mergedNode=idToNodeMap.get(id);
                
                if(mergedNode!=null){
                    
                    mergeSubTree((Element)mergedNode, el);
                }
                
            }else{

                //as no node of the given element has the same id 
            	//than the current node of the inherited element, 
                //it is added to the given element
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
    
    /**
     * returns the unique data base file name corresponding to the given file name
     * @param fileName the name of a data base file
     * @return the unique data base file name corresponding to the given file name
     */
    protected String getUniqueDataBaseFileName(String fileName) {
    	
    	String uniqueFileName="";
    	
    	if(fileName!=null && ! fileName.equals("")) {
    		
    		//getting the last segment of the path
    		int pos=fileName.lastIndexOf("/");
    		
    		if(pos!=-1) {
    			
    			fileName=fileName.substring(pos+1, fileName.length());
    		}
    		
    		if(dataBaseFilesMap.containsKey(fileName)) {
    			
        		//getting the root of the file name
        		String root=fileName.substring(0, fileName.indexOf("."));
        		
        		//getting the unique name of the data base file
        		int i=0;
        		
        		while(dataBaseFilesMap.containsKey(root+i+FileManager.DATA_BASE_FILE_EXTENSION)) {
        			
        			i++;
        		}
        		
        		uniqueFileName=root+i+FileManager.DATA_BASE_FILE_EXTENSION;
    			
    		}else {
    			
    			uniqueFileName=fileName;
    		}
    		
    		if(uniqueFileName.startsWith("/")) {
    			
    			uniqueFileName=uniqueFileName.substring(1, uniqueFileName.length());
    		}
    	}
    	
    	return uniqueFileName;
    }
}
