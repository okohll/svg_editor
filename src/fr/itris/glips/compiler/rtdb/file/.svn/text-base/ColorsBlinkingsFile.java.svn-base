/*
 * Created on 1 juil. 2005
 */
package fr.itris.glips.compiler.rtdb.file;

import java.io.*;
import java.net.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.compiler.rtdb.*;

/**
 * the class handling a colors and blinkings file
 * 
 * @author ITRIS, Jordi SUC
 */
public class ColorsBlinkingsFile {
	
    /**
     * the id color start characters
     */
    public static final String start="/*";
    
    /**
     * the id color end characters
     */
    public static final String end="*/";
	
	/**
	 * the name of the parent element of the blinkings
	 */
	private static String parentBlinkingsElementName="blinkings";
	
	/**
	 * the name of the parent element of the colors
	 */
	private static String parentColorsElementName="colors";

    /**
     * the file manager
     */
    private FileManager fileManager=null;
    
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
    private final static String name="colorsBlinkings"+FileManager.XML_FILE_EXTENSION;
    
    /**
     * the colors and blinkings file
     */
    private File colorsBlinkingsFile;
    
    /**
     * the document of this xml file
     */
    private Document doc=null;
    
    /**
     * the set of the colors and blinkings files
     */
    private HashSet<ColorsBlinkingsFile> colorsBlinkingsFiles=new HashSet<ColorsBlinkingsFile>();
	
	/**
	 * the set of all the ids
	 */
	protected static final HashSet<String> allIds=new HashSet<String>();
	
	/**
	 * the map associating an old id (having the pattern : "projectName":::"oldId" to a new id
	 */
	protected static final HashMap<String, String> oldIdToNewId=new HashMap<String, String>();
	
    /**
     * the constructor of the class
     * @param fileManager the file manager
     * @param workspacePath the path of the workspace of the project that is handled
     * @param projectName the name of the project that is handled
     */
	public ColorsBlinkingsFile(FileManager fileManager, String workspacePath, String projectName){
        
        this.fileManager=fileManager;
        this.workspacePath=workspacePath;
        this.projectName=projectName;
		
        try{
            //creating the file
        	colorsBlinkingsFile=new File(new URI(workspacePath+"/"+projectName+"/"+name));
        	
            //creating the document corresponding of this xml file
            doc=FileManager.getDocument(colorsBlinkingsFile);
        }catch (Exception ex){ex.printStackTrace();}
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
        
        return colorsBlinkingsFile.exists();
    }
    
    /**
     * @return the root element of the document of this xml file
     */
    public Element getRootElement(){
        
        return doc.getDocumentElement();
    }
    
    /**
     * handles (i.e.: make the id unique) all the ids that can be found in the document
     */
    public void handleIds(){
    	
    	String oldId="", newId="", value="";
    	Node cur=null;
        Element el=null;
    	
    	//checking the id of each node
    	for(NodeIterator it=new NodeIterator(getRootElement()); it.hasNext();){
    		
    		cur=it.next();
    		
    		if(cur!=null){

                if(cur instanceof Element) {
                    
                    el=(Element)cur;
                    
                    oldId=el.getAttribute("id");
                    
                    if(oldId!=null){
                        
                        //if the old id already exists, it is replaced by the new id
                        if(allIds.contains(oldId)){
                            
                            newId=getId(oldId);
                            allIds.add(newId);
                            
                            //storing the old id and its corresponding new id
                            oldIdToNewId.put(projectName+FileManager.separator+oldId, newId);
                            
                            //setting the new id
                            el.setAttribute("id", newId);
                            
                        }else{
                            
                            allIds.add(oldId);
                        }
                    }
                    
                }else if(cur instanceof Text){
                    
                    cur.setNodeValue(null);
                }
    		}
    	}
    	
    	//modifies the referenced blinking ids that can be found in the blinking colors
    	Element parentColorsElement=getColorsElement();
    	
    	if(parentColorsElement!=null){
    		
        	for(cur=parentColorsElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
        		
        		if(cur instanceof Element && cur.getNodeName().equals("blinkingColor")){
        			
                    el=(Element)cur;
                    
        			//the value of the "blinkingName" attribute
        			value=el.getAttribute("blinkingName");
        			
        			if(value!=null && ! value.equals("")){
        				
        				//the absolute value
        				value=projectName+FileManager.separator+value;
        				
        				if(oldIdToNewId.containsKey(value)){
        					
        					//getting the new id corresponding to the old id
        					newId=oldIdToNewId.get(value);
        					
        					if(newId!=null && ! newId.equals("")){
        						
        						//modifying the attribute
                                el.setAttribute("blinkingName", newId);
        					}
        				}
        			}
        		}
        	}
    	}
    }

    /**
     * merges the colors and blinkings files and returns the compiled file
     * @return the compiled file
     */
    public CompiledColorsBlinkingsFile mergeColorsBlinkingsFiles(){
    	
    	CompiledColorsBlinkingsFile compiledFile=null;
    	
    	//getting the parent of the blinking nodes
    	Element parentBlinkingElement=getBlinkingsElement();
    	
    	//getting the parent of the color nodes
    	Element parentColorElement=getColorsElement();
    	
    	//handles the ids of the current file
    	handleIds();
    	
        //creating the related colors and blinkings files in the other projects that have been found in the lib path files
    	//and doing all the actions on them
        LinkedList<String> projectNames=fileManager.getProjectNamesList();
        ColorsBlinkingsFile file=null;
        Node cur=null, parent=null;

        for(String cbProjectName : projectNames){
        	
        	if(cbProjectName!=null && ! cbProjectName.equals("") && ! cbProjectName.equals(projectName)){
        		
        		//creating the colors and blinkings file corresponding to the project name
        		file=new ColorsBlinkingsFile(fileManager, workspacePath, cbProjectName);
        		
        		if(file.exists()){
        			
        			colorsBlinkingsFiles.add(file);
        			
        			//handles the ids for each file
        			file.handleIds();
        			
        			//inserting the blinkings nodes of the current file
        			parent=file.getBlinkingsElement();
        			
        			if(parent!=null){
        				
            			for(cur=parent.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
            				
            				if(cur instanceof Element){
            					
            					parentBlinkingElement.appendChild(doc.importNode(cur, true));
            				}
            			}
        			}

        			//inserting the colors nodes of the current file
        			parent=file.getColorsElement();
        			
        			if(parent!=null){
        				
            			for(cur=parent.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
            				
            				if(cur instanceof Element){
            					
            					parentColorElement.appendChild(doc.importNode(cur, true));
            				}
            			}
        			}
        		}
        	}
        }
        
        //creating the compiled file
        compiledFile=new CompiledColorsBlinkingsFile(fileManager, fileManager.getOutputDirectory()+"/"+name,
                                                                                    doc, oldIdToNewId);
    	
    	return compiledFile;
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
     * @return the parent of the blinking elements of this file
     */
    protected Element getBlinkingsElement(){
    	
    	Element parentBlinkingElement=null;
    	
    	//getting the list of the parent blinking nodes
    	NodeList nodeList=doc.getDocumentElement().getElementsByTagName(parentBlinkingsElementName);
    	
    	if(nodeList!=null && nodeList.getLength()>0){
    		
    		parentBlinkingElement=(Element)nodeList.item(0);
    		
    	}else{
    		
    		parentBlinkingElement=doc.createElementNS(doc.getDocumentElement().getNamespaceURI(), parentBlinkingsElementName);
    		doc.getDocumentElement().appendChild(parentBlinkingElement);
    	}
    	
    	return parentBlinkingElement;
    }
    
    /**
     * @return the parent of the blinking elements of this file
     */
    protected Element getColorsElement(){
    	
    	Element parentColorElement=null;
    	
    	//getting the list of the parent blinking nodes
    	NodeList nodeList=doc.getDocumentElement().getElementsByTagName(parentColorsElementName);
    	
    	if(nodeList!=null && nodeList.getLength()>0){
    		
    		parentColorElement=(Element)nodeList.item(0);
    		
    	}else{
    		
    		parentColorElement=doc.createElementNS(
    				doc.getDocumentElement().getNamespaceURI(), parentColorsElementName);
    		doc.getDocumentElement().appendChild(parentColorElement);
    	}
    	
    	return parentColorElement;
    }
    
    /**
     * clears the static fields of the class
     */
    public static void clearStaticElements() {
    	
    	allIds.clear();
    	oldIdToNewId.clear();
    }
}
