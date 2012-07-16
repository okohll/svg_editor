/*
 * Created on 15 mars 2005
 */
package fr.itris.glips.rtda.database;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;
import libraries.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.*;

/**
 * the toolkit used to retrieve information in the database
 * 
 * @author ITRIS, Jordi SUC
 */
public class DataBaseToolkit {
    
    /**
     * the lib path file name
     */
    public static final String LIB_PATH_FILE_NAME=".project";
    
    /**
     * the tag name of the root node of the widget database
     */
    public static final String widgetDatabaseRootTagName="widgetDatabase";
    
    /**
     * creates and returns the path of the project of the canvas path
     * @param canvasPath a canvas path
     * @param referenceFileName the file name of the reference file
     * @param isEditionMode whether the current mode is the edition or runtime mode
     * @return the path of the project of the canvas path
     */
    public static String getProjectPath(	
    	String canvasPath, String referenceFileName, boolean isEditionMode){
    	
    	String path="";
    	
    	if(canvasPath!=null && referenceFileName!=null){
    		
			//getting the project file corresponding to the given canvas path
			File projectFile=null;
			
			try{
				projectFile=AnimationsToolkit.getProjectFile(new URI(canvasPath), isEditionMode);
			}catch (Exception ex) {}
			
			if(projectFile!=null) {

				//getting the project path
				path=projectFile.toURI().toASCIIString();
				
				if(path.endsWith("/")){
					
					path=path.substring(0, path.length()-1);
				}
			}
    	}
    	
    	return path;
    }
    
    /**
     * creates and returns the name of the project of the canvas path
     * @param canvasPath a canvas path
     * @param referenceFileName the file name of the reference file
     * @param isEditionMode whether the current mode is the edition or runtime mode
     * @return the name of the project of the canvas path
     */
    public static String getProjectName(	
    	String canvasPath, String referenceFileName, boolean isEditionMode){
    	
    	String projectName="";
    	
    	if(canvasPath!=null && referenceFileName!=null){
    		
			//getting the project file corresponding to the given canvas path
			File projectFile=null;
			
			try{
				projectFile=AnimationsToolkit.getProjectFile(new URI(canvasPath), isEditionMode);
			}catch (Exception ex) {}
			
			if(projectFile!=null) {

				//getting the project path
				projectName=Toolkit.getFileName(projectFile);
			}
    	}
    	
    	return projectName;
    }
    
    /**
     * returns the root element of the widget database (creates it if it does not exist)
     * @param svgDoc a widget svg document
     * @return the root element of the widget database
     */
    public static Element getRtdaWidgetDataBase(Document svgDoc){
        
        Element widgetDataBaseRoot=null;
        
        if(svgDoc!=null){
        	
            Toolkit.checkRtdaXmlns(svgDoc);

            //getting the list of the nodes corresponding to the given tag name
            NodeList nodeList=svgDoc.getDocumentElement().getElementsByTagName(
            		"rtda:"+widgetDatabaseRootTagName);
            
            if(nodeList.getLength()>0){
                
                widgetDataBaseRoot=(Element)nodeList.item(0);
            }
            
            if(widgetDataBaseRoot==null){
            	
            	//checking if the svg file does not contain the rtda namespace
            	Toolkit.checkRtdaXmlns(svgDoc);

                //as the svg file does not contain any widget database, the root node of this database is created
                widgetDataBaseRoot=svgDoc.createElementNS(null, "rtda:"+widgetDatabaseRootTagName);
                widgetDataBaseRoot.setAttributeNS(null, "id", "root");
                svgDoc.getDocumentElement().appendChild(widgetDataBaseRoot);
            }
        }
        
        return widgetDataBaseRoot;
    }
    
	/**
	 * creates and returns the root element of the xml database that can be 
	 * found at the given reference file name and having the given reference node
	 * @param canvasPath the path of the svg file
	 * @param referenceFileName the reference file
	 * @param referenceNode the reference node
	 * @return the root element of the xml database that can be 
	 * found at the given reference file name and having the given reference node
	 */
	public static Element getRootDataBaseElement(
			String canvasPath, String referenceFileName, String referenceNode) {
		
		Element rootDataBaseElement=null;
		
		if(canvasPath!=null && referenceFileName!=null && referenceNode!=null) {
			
			//getting the project file corresponding to the given canvas path
			File projectFile=null;
			
			try{
				projectFile=AnimationsToolkit.getProjectFile(new URI(canvasPath), true);
			}catch (Exception ex) {}
			
			if(projectFile!=null) {
				
				//computing the workspace path
				String workspacePath="";
				
				try{
					workspacePath=projectFile.getParentFile().toURI().toASCIIString();
				}catch (Exception ex) {workspacePath=null;}
				
				//getting the project name
				String projectName=Toolkit.getFileName(projectFile);
				
				if(workspacePath!=null && ! workspacePath.equals("") && projectName!=null && 
						! projectName.equals("")) {
					
					//getting the map of the lib paths
					LinkedHashMap<String, LinkedList<String>> libPathsMap=
						DataBaseToolkit.getLibPathFileNamesMap(workspacePath, projectName);

					if(libPathsMap!=null) {
						
						//creating the xml file that will build the data base tree
						XMLFile xmlFile=new XMLFile(libPathsMap, 
								workspacePath, projectName, referenceFileName);
						
						//creating the whole dom tree of the data base
						Document doc=xmlFile.resolveInheritance();
						
						if(doc!=null) {
							
							//retrieving the appropriate node corresponding to the given reference node//
							
							//getting the list of each segment of the reference node path
							String[] splitNodePath=Toolkit.getSplitPath(referenceNode);
							
							if(splitNodePath!=null && splitNodePath.length>0) {

								int i, j;
								Element element=doc.getDocumentElement();
								NodeList children=doc.getDocumentElement().getChildNodes();
								
								//handling the root node case
								if(element!=null && element.getAttribute("id").equals(splitNodePath[0])) {
									
									rootDataBaseElement=element;
									
									//handling the child nodes under the root node
									start :
										
									for(i=1; i<splitNodePath.length && children!=null; i++){
										
										for(j=0; j<children.getLength(); j++) {
											
											if(children.item(j)!=null && children.item(j) instanceof Element) {
												
												element=(Element)children.item(j);
												
												//checking if one of the nodes has the current id
												if(element.getAttribute("id").equals(splitNodePath[i])) {
													
													//setting the new root data base element
													rootDataBaseElement=element;
													break;
												}
											}
											
											if(j==children.getLength()-1) {
												
												//no node having the corresponding id exists
												rootDataBaseElement=null;
												break start;
											}
										}
										
										//getting the children of the current node
										children=rootDataBaseElement.getChildNodes();
									}
								}
							}
						}
					}
				}
			}
		}
		
		return rootDataBaseElement;
	}

    /**
     * returns the absolute location of the given view image
     * @param imageViewURI the uri of the image of a view
     * @param canvasSVGURI the canvas uri into which the image 
     * having the imageViewPath path will be inserted
     * @return the absolute location of the given view image
     */
    public static String getAbsoluteLocation(URI imageViewURI, URI canvasSVGURI){

    	String location=""; 
    	String imageViewPath=imageViewURI.toASCIIString();
    	String canvasSVGPath=canvasSVGURI.toASCIIString();
    	
        if(canvasSVGPath!=null && ! canvasSVGPath.equals("") && 
        		imageViewPath!=null && ! imageViewPath.equals("")){
            
        	//relativizes the given baseSVGPath to the ".project" file of the current project
            int i, j;
            String[] splitBaseSVGPath=Toolkit.getSplitPath(canvasSVGPath);
            File file=null;
            String currentPath="";
            URI uri=null;
            File canvasSVGFile=null;

            try{
                canvasSVGFile=new File(canvasSVGURI);
            }catch(Exception ex){}

            if(splitBaseSVGPath!=null && canvasSVGFile!=null){
                
                //checking the index of the element of the path from which the ".project" file can be found
            	for(i=splitBaseSVGPath.length-1; i>=0; i--){
            		
            		currentPath="";
            		
            		//computing the path corresponding to this index
            		for(j=0; j<=i; j++){
            			
            	        //building the new path 
                	    if(j==0){

                	        currentPath=splitBaseSVGPath[0];
                	        
                	    }else{

                	        currentPath+=("/"+splitBaseSVGPath[j]);
                	    }
            		}

            	    try{uri=new URI(currentPath);}catch (Exception ex){uri=null;ex.printStackTrace();}
            	    
            	    if(uri!=null){
            	        
                	    //creating the file corresponding to the previously created path
                	    file=new File(uri);

                	    if(file.exists()){
                	        
                            //getting the file under the project directory whose name is ".project"
                            File[] files=file.listFiles(new FilenameFilter(){
                            
                                public boolean accept(File dir, String name) {
                                	
                                    if(name!=null && name.equals(".project")){
                                        
                                        return true;
                                    }
                                    
                                    return false;
                                }
                            });
                            
                            if(files!=null && files.length>0){

                                try{
                                    String workspacePath=
                                    	files[0].getParentFile().getParentFile().toURI().toASCIIString();
                                    
                                    if(workspacePath.endsWith("/")){
                                    	
                                    	workspacePath=workspacePath.substring(0, workspacePath.length()-1);
                                    }

                                    String projectName=Toolkit.getFileName(files[0].getParentFile());

                                    location=getAbsolutePath(workspacePath, projectName, imageViewPath);
                                }catch (Exception ex){location="";ex.printStackTrace();}

                                break;
                            }
                	    }
            	    }
            	}
            }
        }

        return location;
    }
    
    /**
     * returns the absolute path in the workspace base of the given image path
     * @param workspacePath the path of the workspace
     * @param baseProjectName the name of the base project
     * @param imagePath the path of an image
     * @return the absolute path in the workspace base of the given image path
     */
    protected static String getAbsolutePath(String workspacePath, 
    		String baseProjectName, String imagePath){
        
        String absolutePath="";
        
        if(workspacePath!=null && baseProjectName!=null && imagePath!=null){
            
            //getting the collection of the names of the projects that are used in the given project
            Collection<String> libPaths=getLibPathFileNamesList(workspacePath, baseProjectName);
            libPaths.add(baseProjectName);

            File file=null;
            String foundProjectName="";
            
            //searching in the projects if the file denoted by the given image path exists
            for(String projectName : libPaths){

                if(projectName!=null && ! projectName.equals("")){
                    
                    try{
                    	file=new File(new URI(workspacePath+"/"+projectName+"/"+imagePath));
                    }catch(Exception ex){file=null;}
                    
                    if(file!=null && file.exists()){
                        
                        foundProjectName=projectName;
                        break;
                    }
                }
            }
            
            if(! foundProjectName.equals("")){
                
                //builds the absolute path of the image path
                absolutePath=workspacePath+"/"+foundProjectName+"/"+imagePath;
            }
        }
        
        return absolutePath;
    }
    
    /**
     * parses the given xml file and returns its associated document
     * @param file a file
     * @return the document associated with the given file
     */
    public static Document getDocument(File file){
        
        Document doc=null;
        
        if(file!=null && file.exists()){
            
            try{
                //parsing the xml file and creating the document
    			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
    			DocumentBuilder builder=factory.newDocumentBuilder();
    			
    			factory.setValidating(false);
    			factory.setIgnoringComments(true);
                doc=builder.parse(file);
            }catch (Exception ex){}
        }

        return doc;
    }
    
    /**
     * returns the unique lib path file contained in the project
     * @param workspacePath the path of a workspace
     * @param projectName the name of a project
     * @return the unique lib path file contained in the project
     */
    public static File getLibPathsFile(String workspacePath, String projectName){
        
        File libPathsFile=null;
        
        if(projectName!=null && ! projectName.equals("")){
            
        	File projectFile=null;
        	
        	try{
        		projectFile=new File(new URI(workspacePath+"/"+projectName));
        	}catch (Exception ex){}
            
            if(projectFile!=null && projectFile.exists()){
                
                //getting the files under the project directory whose extension is ".libpath"
                File[] files=projectFile.listFiles(new FilenameFilter(){
                
                    public boolean accept(File dir, String name) {
                        
                        if(name!=null && name.equals(LIB_PATH_FILE_NAME)){
                            
                            return true;
                        }
                        
                        return false;
                    }
                });
                
                if(files!=null && files.length>0){
                    
                    //setting the lib path file
                    libPathsFile=files[0];
                }
            }
        }
        
        return libPathsFile;
    }
    
    /**
     * returns the list of the path files contained in the given lib path file
     * @param libPathsFile the file of a of the lib paths
     * @return the list of the path files contained in the given lib path file
     */
    public static LinkedList<String> getLibPaths(File libPathsFile){

    	LinkedList<String> libPaths=new LinkedList<String>();
        
    	if(libPathsFile!=null && libPathsFile.exists()) {
    		
   		 //creating the document that can be found in the given file
           Document doc=getDocument(libPathsFile);
           
           NodeList projectNodes=
        	   doc.getDocumentElement().getElementsByTagName("project");
           NodeList children=null;
           int j=0;
           
           for(int i=0; i<projectNodes.getLength(); i++) {
               
               if(projectNodes.item(i)!=null) {
                   
                   //getting the children of the node
                   children=projectNodes.item(i).getChildNodes();
                   
                   if(children!=null) {
                       
                       for(j=0; j<children.getLength(); j++) {
                           
                           if(children.item(j)!=null && children.item(j) instanceof Text &&
                               ! children.item(j).getNodeValue().equals("")) {
                               
                               libPaths.add(URIEncoderDecoder.encode(children.item(j).getNodeValue()));
                           }
                       }
                   }
               }
           }
    	}
        
        return libPaths;
    }
    
    /**
     * creates and fills the list of the lib path file names
     * @param workspacePath the path of the workspace
     * @param currentProjectName the name of a project
     * @return the list of the lib path file names
     */
    protected static LinkedList<String> getLibPathFileNamesList(
    		String workspacePath, String currentProjectName){
        
        LinkedList<String> libPathsList=new LinkedList<String>();
 
        //Getting the map of the lib paths
        LinkedHashMap<String, LinkedList<String>> libPathMap=
        	getLibPathFileNamesMap(workspacePath, currentProjectName);
        
        if(libPathMap!=null){
        	
        	//getting the list of the lib path for the current project name
        	libPathsList=new LinkedList<String>(libPathMap.get(currentProjectName));
        }
        
        return libPathsList;
    }
    
    /**
     * fills and returns the map of the lib path file names
     * @param workspacePath the path of the workspace
     * @param currentProjectName the name of a project
     * @return the map of the lib path file names
     */
    public static LinkedHashMap<String, LinkedList<String>> 
    	getLibPathFileNamesMap(String workspacePath, String currentProjectName){
        
    	//the map associating a project name to the list of its referenced project names
    	LinkedHashMap<String, LinkedList<String>> libPathsMap=
    		new LinkedHashMap<String, LinkedList<String>>();
    	
    	//the list of the names of the projects
        LinkedList<String> projectNamesList=new LinkedList<String>();
        projectNamesList.add(currentProjectName);
        
        //adding the current project name to the list
        LinkedList<String> list=null;
        String projectName="";
        int i=0;

        //filling the list of the lib paths for the current project and its referenced projects
        do{
            
            //getting the name of the current project
            projectName=projectNamesList.get(i);

            if(projectName!=null && ! projectName.equals("")){
                
                //getting the list of the names of the projects referenced by the current project
                list=getLibPaths(getLibPathsFile(workspacePath, projectName));
                
                //putting the list into the map
                libPathsMap.put(projectName, list);
                
                //adding the name of the referenced projects to the list of the lib paths
                for(String name : list){
                    
                    if(name!=null && ! name.equals("") && ! projectNamesList.contains(name)){
                        
                        projectNamesList.add(name);
                    }
                }
            }
            
            i++;
            
        }while(i<projectNamesList.size());
        
        //checks that no cycle can be found in the lib paths
        boolean isCorrect=checkCycle(libPathsMap, new LinkedList<String>(), currentProjectName);
        
        if(! isCorrect) {
        	
        	//if the lib paths is not correct, it is not sent
        	libPathsMap=null;
        }
        
        return libPathsMap;
    }
    
    /**
     * checks if a cycle can be found in the lib path files
     * @param libPathsMap the map of the lib path
     * @param projectList the list of the project we have already been through
     * @param currentProjectName a project name
     * @return whether or not 
     */
    protected static boolean checkCycle(
    		LinkedHashMap<String, LinkedList<String>> libPathsMap, 
    		LinkedList<String> projectList, String currentProjectName){
    	
    	boolean isCorrect=true;
    	
    	if(libPathsMap!=null && projectList!=null && currentProjectName!=null && 
    			! currentProjectName.equals("")){
    		
    		if(! projectList.contains(currentProjectName)){
    			
    			projectList.add(currentProjectName);
    			
    			//getting the list of the project names associated with the current project name
    			LinkedList<String> list=libPathsMap.get(currentProjectName);
    			
    			if(list!=null && list.size()>0){

    				//for each referenced project name, checks if they create a cycle
    				for(String name : list){
    					
    					if(name!=null && ! name.equals("")){
    						
    						isCorrect=isCorrect && 
    							checkCycle(libPathsMap, new LinkedList<String>(projectList), name);
    					}
    				}
    			}
    			
    		}else {
    			
    			isCorrect=false;
    		}
    	}
    	
    	return isCorrect;
    }
}
