/*
 * Created on 24 mai 2005
 */
package fr.itris.glips.compiler.rtdb.file;

import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import fr.itris.glips.compiler.rtdb.*;
import java.net.*;
import org.apache.batik.dom.svg.*;
import org.apache.batik.util.*;
import java.nio.channels.*;
import java.nio.*;
import libraries.*;

/**
 * the class that handles the files
 * 
 * @author ITRIS, Jordi SUC
 */
public class FileManager {
    
    /**
     * the glips view file extension
     */
    public static final String GLIPS_VIEW_EXTENSION=".gv";
    
    /**
     * the glips view compiled file extension
     */
    public static final String COMPILED_GLIPS_VIEW_FILE="root.gvc";
    
    /**
     * the svg file extension
     */
    public static final String SVG_FILE_EXTENSION=".svg";
    
    /**
     * the svg file extension
     */
    public static final String SVGZ_FILE_EXTENSION=".svgz";
    
    /**
     * the xml  file extension
     */
    public static final String XML_FILE_EXTENSION=".xml";
    
    /**
     * the tag list files extension
     */
    public static final String TAG_LIST_FILE_EXTENSION=".taglist";
    
    /**
     * the data base files extension
     */
    public static final String DATA_BASE_FILE_EXTENSION=".csv";
    
    /**
     * the java class files extension
     */
    public static final String CLASS_FILE_EXTENSION=".class";
    
    /**
     * the lib path file name
     */
    public static final String LIB_PATH_FILE_NAME=".project";
    
    /**
     * the actions directory name
     */
    public static final String ACTIONS_DIRECTORY_NAME="actions";
    
    /**
     * the name space for declaring namespaces
     */
    public static final String xmlnsNS="http://www.w3.org/2000/xmlns/";
    
    /**
     * the xlink attribute namespace name
     */
    public static final String xmlnsXLinkAttributeName="xmlns:xlink";
    
    /**
     * the xlink namespace
     */
    public static final String xmlnsXLinkNS="http://www.w3.org/1999/xlink";
    
    /**
     * the separator
     */
    public static final String separator=":::";
    
    /**
     *  the path of the workspace of the project that is handled
     */
    private String workspacePath="";
    
    /**
     * the name of the project that is handled
     */
    private String projectName="";
    
    /**
     * the name of the file to be compiled
     */
    private String fileName="";
    
    /**
     * the directory into which the output files should be written
     */
    private String outputDirectory="";
    
    /**
     * the root view path
     */
    private String rootViewPath="";
    
    /**
     * the tag handler
     */
    private RtdaTagHandlersManager rtdaTagHandlersManager=new RtdaTagHandlersManager();
    
    /**
     * the map associating the name of a project to the list of the projects that it references
     */
    private LinkedHashMap<String, LinkedList<String>> libPathsMap=new LinkedHashMap<String, LinkedList<String>>();
    
    /**
     * the list of all the referenced project names
     */
    private LinkedList<String> projectNamesList=new LinkedList<String>();
    
    /**
     * the compiled file of the colors and blinkings
     */
    private CompiledColorsBlinkingsFile compiledColorsBlinkingsFile=null;
    
    /**
     * the main xml file
     */
    private XMLFile mainFile=null;
    
    /**
     * the compiled XML file
     */
    private CompiledXMLFile compiledXMLFile=null;
    
    /**
     * the map associating the absolute xml path of an svg file to this svg file 
     * (the svg files are those referenced in the main xml file)
     */
    private HashMap<String, SVGFile> svgFiles=new HashMap<String, SVGFile>();
    
    /**
     * the set of the compiled svg files
     */
    private HashSet<CompiledSVGFile> compiledSVGFiles=new HashSet<CompiledSVGFile>();
    
    /**
     * the map of the file paths that are used by all the svg files, it associates the path of the file
     * found in the svg file to the path used for copying it
     */
    private HashMap<String, String> usedFilesPaths=new HashMap<String, String>();

    /**
     *  the constructor of the class
     * @param workspacePath the path of the workspace of the project that is handled
     * @param projectName the name of the project that is handled
     * @param fileName the name of the file to be compiled
     * @param outputDirectory the directory into which the output files should be written
     * @param rootViewPath the root view path
     * @param isTestMode whether the compiler is in the test mode
     */
    public FileManager(String workspacePath, String projectName, String fileName, String outputDirectory, 
    								String rootViewPath, boolean isTestMode){
        
        this.workspacePath=normalizePath(workspacePath);
        this.projectName=projectName;
        this.fileName=fileName.replace(File.separatorChar, '/');
        this.rootViewPath=rootViewPath;

        //checking the output directory
        if(outputDirectory==null || outputDirectory.equals("")){
            
            this.outputDirectory=this.workspacePath;
            
        }else{

            this.outputDirectory=normalizePath(outputDirectory);
            
            if(isTestMode) {
            	
            	this.outputDirectory+="/"+projectName;
            }
        }
    }

    /**
     * @return Returns the outputDirectory.
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
	 * @return Returns the rootViewPath.
	 */
	public String getRootViewPath() {
		return rootViewPath;
	}

	/**
     * compiling the application
     * @throws InvalidLibPathException the exception raised if there is a cycle in the lib path files
     * @throws InvalidInheritedNodeException the exception raised if there is a cycle in the inheritance node
     * @throws MissingFileException the exception raised if a required data base file is missing
     * @throws Exception the exception raised if something wrong happens
     * @throws DuplicateActionFileException 	the exception raised when two or more 
     * 																	action java classes have the same name
     */
    public void compile() throws InvalidLibPathException, InvalidInheritedNodeException, 
    												MissingFileException, DuplicateActionFileException, Exception{

        //filling the list of the lib paths
        fillLibPathFileNamesMap();

    	//compiled the colors and blinkings files
    	ColorsBlinkingsFile colorsBlinkingsFile=new ColorsBlinkingsFile(this, workspacePath, projectName);
    	
    	if(colorsBlinkingsFile.exists()){

        	this.compiledColorsBlinkingsFile=colorsBlinkingsFile.mergeColorsBlinkingsFiles();
    	}

        //creating the main XML file
        mainFile=new XMLFile(this, workspacePath, projectName, fileName);
        
        //initializing the xml file
        mainFile.initialize();

        //putting all xml files into a single one
        compiledXMLFile=mainFile.compile(outputDirectory);

        if(compiledColorsBlinkingsFile!=null){
        	
        	compiledColorsBlinkingsFile.writeFile();
        }
        
        if(compiledXMLFile!=null){

            //filling the map of the svg files used in the xml files
            svgFiles.putAll(compiledXMLFile.getSVGfiles());
            
            //compiling the svg files
            compileSVGFiles();

            //creating the link items of the compiled xml file
            compiledXMLFile.handleLinkItems();
            
            //normalizes the XML file
            compiledXMLFile.normalizeXMLDocument();

            //writes the xml file
            compiledXMLFile.writeFile();

            //writing all the compiled SVG files
            for(CompiledSVGFile compiledSVGFile : compiledSVGFiles){

                if(compiledSVGFile!=null){

                    compiledSVGFile.writeFile();
                }
            }
            
            //copying the files used by the svg files
            writeUsedFiles();

            //copying the action classes
            ActionClassesHandler actionClassesHandler=new ActionClassesHandler();
            actionClassesHandler.copyActionClasses(workspacePath, outputDirectory, projectNamesList);
        	
        }else{
        	
        	throw new Exception("invalid output directory");
        }
        
        //clearing the static fields
        ColorsBlinkingsFile.clearStaticElements();
        SVGFile.clearStaticElements();
        XMLFile.clearStaticElements();
    }
    
    /**
     * compiles the svg files
     */
    protected void compileSVGFiles() {
    	
        //handles the ids of all the svg files
        for(SVGFile file : svgFiles.values()){

            if(file!=null){

                file.handleIds();
            }
        }

        //handles the references and replaces the ids in the references of all the svg files
        for(SVGFile file : svgFiles.values()){

            if(file!=null){
                
                file.handleReferencesAndIdsInReferences();
            }
        }

        //inserts the views and the widgets in all the svg files
        for(SVGFile file : svgFiles.values()){

            if(file!=null){
                
                file.insertViewsAndWidgets();
            }
        }

        //handles the uris in all the svg files
        for(SVGFile file : svgFiles.values()){

            if(file!=null){
                
                file.handleURIs();
            }
        }

        //filling the set of the compiled svg files
        CompiledSVGFile compiledFile=null;
        
        //creating the compiled file of the main svg file
        SVGFile svgFile=getSVGFile(compiledXMLFile.getRootView());

        if(svgFile!=null){
        	
        	Set<SVGFile> filesToBeSaved=svgFile.getFilesToBeSaved();

            //creating the compiled files of the svg files that are specified in the "loadView" animation nodes 
            for(SVGFile file : filesToBeSaved){

                if(file!=null){

                    compiledFile=file.getCompiledFile(compiledXMLFile);

                    if(compiledFile!=null){
                        
                        compiledSVGFiles.add(compiledFile);
                    }
                }
            }
        }

        //filling the set of the used files paths
        usedFilesPaths.putAll(SVGFile.getUsedFilesPaths());
    }
    
    /**
	 * @return the list of the project names
	 */
	public LinkedList<String> getProjectNamesList() {
		
		return projectNamesList;
	}
    
    /**
     * creates and returns the svg file corresponding to the given absolute xml path
     * @param xmlPath an absolute xml path
     * @return the svg file corresponding to the given absolute xml path
     */
    public SVGFile getSVGFile(String xmlPath){

        SVGFile file=null;

        if(xmlPath!=null && svgFiles.containsKey(xmlPath)){

    		file=svgFiles.get(xmlPath);
        }

        return file;
    }

    /**
     * normalizes the given path
     * @param path a path
     * @return the normalized path
     */
    public String normalizePath(String path){
        
        String res="";
        
        if(path!=null){
            
        	boolean isURI=true;
        	
        	try{
        		new URI(path);
        		res=path;
        	}catch (Exception ex){isURI=false;ex.printStackTrace();}
        	
        	if(! isURI){
        		
            	try{
            		res=new File(path).toURI().toASCIIString();
            	}catch (Exception ex){res="";ex.printStackTrace();}
        	}

            if(res.endsWith("/")){
            	
            	res=res.substring(0, res.length()-1);
            }
        }
        
        return res;
    }
    
    /**
     * returns the name of the project into which the file denoted by the given file name can be found
     * @param sourceProjectName the project name from which the file path has been found
     * @param relativeFileName a file name relatively to a project path
     * @return the name of the project into which the file denoted by the given file name can be found
     */
    public String getProjectName(String sourceProjectName, String relativeFileName){
        
        String returnedProjectName="";

        if(relativeFileName!=null && ! relativeFileName.equals("")){
            
            //getting the list of the projects names that are referenced by the given project name
            LinkedList<String> libPathList=new LinkedList<String>(libPathsMap.get(sourceProjectName));
            libPathList.add(sourceProjectName);

            File file=null;

            //searching for the first project name containing the file denoted by the given file name
            for(String currentProjectName : libPathList){

                if(currentProjectName!=null && ! currentProjectName.equals("")){
                    
                    //creating a file corresponding to the given file name in the current project and checking 
                    //if this exists or not
                    try{
                    	file=new File(new URI(workspacePath+"/"+currentProjectName+"/"+relativeFileName));
                    }catch (Exception ex){file=null;ex.printStackTrace();}
                    
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
     * computes and returns a split path corresponding to the given path
     * @param path
     * @return a split path corresponding to the given path
     */
    public static String[] getSplitPath(String path){
    	
    	String[] splitPath=null;
    	
    	if(path!=null && ! path.equals("")){
    		
    		String[] splitPath2=path.split("/");
    		LinkedList<String> list=new LinkedList<String>();
    		
    		for(int i=0; i<splitPath2.length; i++){
    			
    			if(splitPath2[i]!=null && ! splitPath2[i].equals("")){
    				
    				list.add(splitPath2[i]);
    			}
    		}
    		
    		splitPath=new String[list.size()];
    		
    		for(int i=0; i<list.size(); i++){
    			
    			splitPath[i]=list.get(i);
    		}
    	}
    	
    	return splitPath;
    }

	/**
	 * @return the map associating the path of a svg file to this svg file
	 */
	public HashMap<String, SVGFile> getSvgFiles() {
		return svgFiles;
	}
	
    /**
     * @return Returns the tagHandler.
     */
    public RtdaTagHandlersManager getTagHandlersManager() {
    	
        return rtdaTagHandlersManager;
    }

    /**
     * @return Returns the workspacePath.
     */
    public String getWorkspacePath() {
        return workspacePath;
    }
	
	/**
	 * @return the map associating an old id (having the pattern : "projectName":::"oldId" to a new id
	 */
	public HashMap<String, String> getColorsBlinkingsOldIdToNewIdMap(){
		
		HashMap<String, String> map=null;
		
		if(compiledColorsBlinkingsFile!=null){
			
			map=compiledColorsBlinkingsFile.getOldIdsToNewIds();
			
		}else{
			
			map=new HashMap<String, String>();
		}
		
		return map;
	}
    
    /**
     * copy the used files in the output directory if it is different from the workspace directory
     */
    protected void writeUsedFiles(){
        
        if(outputDirectory!=null && ! outputDirectory.equals(workspacePath)){
            
            String newPath="";
            BufferedInputStream in=null;
            BufferedOutputStream out=null;
            byte[] tab=new byte[256];
            int res=-1;
            
            for(String path : usedFilesPaths.keySet()){

                if(path!=null && ! path.equals("")){
                    
                    //computing the new path
                    newPath=usedFilesPaths.get(path);
                    
                    if(newPath!=null && ! newPath.equals("")){
                        
                        //writing the content of the old file into the new file
                        try{
                            in=new BufferedInputStream(new FileInputStream(new File(new URI(path))));
                            out=new BufferedOutputStream(new FileOutputStream(new File(new URI(newPath))));
                            
                            //for each availabel byte in the old file
                            while(in.available()>0){
                                
                                res=in.read(tab);
                                
                                if(res!=-1){
                                    
                                    out.write(tab, 0, res);
                                }
                            }
                            
                            in.close();
                            out.flush();
                            out.close();
                            
                        }catch(Exception ex){ex.printStackTrace();}
                    }
                }
            }
        }
    }
    
    /**
     * writes the action classes and their  that can be found in the 
     */
    protected void writeClasses() {
    	
    	//creating the action directory
    	File destinationActionDirectory=null;
    	try{
    		destinationActionDirectory=new File(new File(new URI(outputDirectory)), ACTIONS_DIRECTORY_NAME);
    		destinationActionDirectory.createNewFile();
    	}catch (Exception ex){ex.printStackTrace();}
    	
    	if(destinationActionDirectory!=null) {
    		
    	   	//for each project
        	File pFile=null, actionDirectory=null;
        	File[] children=null;
        	
        	for(String pName : projectNamesList) {
        		
        		if(pName!=null && ! pName.equals("")) {
        			
        			try{
        				pFile=new File(new URI(workspacePath+"/"+pName));
        			}catch (Exception ex) {pFile=null;ex.printStackTrace();}
        			
        			if(pFile!=null) {
        				
        				//getting the action directory
        				children=pFile.listFiles();
        				actionDirectory=null;
        				
        				for(File child : children) {
        					
        					if(child!=null && child.isDirectory() && 
        							child.getName().equals(ACTIONS_DIRECTORY_NAME)) {
        						
        						actionDirectory=child;
        						break;
        					}
        				}
        				
        				if(actionDirectory!=null) {
        					
        					//adding all the files that can be found in the actions directory recursively
        					copyClassFiles(actionDirectory, destinationActionDirectory);
        				}
        			}
        		}
        	}
    	}
    }
    
    /**
     * copies the files under the given file into the given directory
     * @param initFile the initial file
     * @param destFile the destination file
     */
    protected void copyClassFiles(File initFile, File destFile) {
    	
    	if(initFile!=null && destFile!=null) {
    		
    		File[] children=initFile.listFiles();
    		File childDestinationDirectory=null, destChild=null;
    		FileInputStream in=null;
    		FileOutputStream out=null;
    		FileChannel cin=null, cout=null;
    		
    		//copying the files that are children of the initial file as children of the destination file
    		for(File child : children) {
    			
    			if(child!=null) {
    				
    				if(child.isDirectory()) {
    					
    					//checking if this directory exists among the destination child files
    					childDestinationDirectory=fileExistAsChild(destFile, child.getName());
    					
    					if(childDestinationDirectory==null) {
    						
    						//creates the directory having the same name as the current file as 
    						//a child of the destination directory
    				    	try{
    				    		childDestinationDirectory=new File(destFile, child.getName());
    				    		childDestinationDirectory.mkdir();
    				    	}catch (Exception ex){ex.printStackTrace();}
    					}
    					
    					//copying the child files for this directory
    					copyClassFiles(child, childDestinationDirectory);
    					
    				}else {
    					
    					//copies the file to the destination directory
    					try{
    						destChild=new File(destFile, child.getName());
    						in=new FileInputStream(child);
    						out=new FileOutputStream(destChild);
    						
    						cin=in.getChannel();
    						cout=out.getChannel();
    						ByteBuffer buffer=ByteBuffer.allocate(1000);
    						int pos=0;
    						
    						while(cin.position()<cin.size()) {
    							
    							pos=cin.read(buffer);
    							
    							if(pos>0) {
    								
    								cout.write(buffer);
    							}
    						}
    						
    						cin.close();
    						cout.close();
    						
    					}catch (Exception ex) {ex.printStackTrace();}
    				}
    			}
    		}
    	}
    }
    
    /**
     * returns the file having the given name among the child files of the given parent file
     * @param parentFile the parent file
     * @param name the name of a child file
     * @return the file having the given name among the child files of the given parent file
     */
    protected File fileExistAsChild(File parentFile, String name) {
    	
    	if(parentFile!=null && name!=null && ! name.equals("")) {
    		
    		File[] children=parentFile.listFiles();
    		
    		for(File file : children) {
    			
    			if(file!=null && file.getName().equals(name)) {
    				
    				return file;
    			}
    		}
    	}
    	
    	return null;
    }
    
    /**
     * returns whether the given path is an absolute path
     * @param path a path
     * @return whether the given path is an absolute path
     */
    public boolean isAbsolutePath(String path) {
        
        boolean isAbsolutePath=false;
        
        try {
            URI uri=new URI(path);
            isAbsolutePath=uri.isAbsolute();
        }catch (Exception ex) {}
        
        return isAbsolutePath;
    }
    
        
    /**
     * parses the given xml file and returns its associated document
     * @param file a file 
     * @return the document associated with the given file
     */
    public static Document getDocument(File file){
        
        Document doc=null;
        
        if(file!=null /*&& file.exists()*/){

            try{

                //getting the document builder factory and the document builder
    			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
    			DocumentBuilder builder=factory.newDocumentBuilder();
    			
    			factory.setNamespaceAware(true);
    			factory.setValidating(true);
    			factory.setIgnoringComments(true);
            
                //parsing the xml file and creating the document
                doc=builder.parse(file);
            }catch (Exception ex){ex.printStackTrace();}
        }

        return doc;
    }
    
    /**
     * parses the given xml file and returns its associated document
     * @param in an input stream
     * @return the document associated with the given file
     */
    public static Document getDocument(InputStream in){
        
        Document doc=null;
        
        if(in!=null /*&& file.exists()*/){
        	
            try{
                //getting the document builder factory and the document builder
    			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
    			DocumentBuilder builder=factory.newDocumentBuilder();
    			
    			factory.setNamespaceAware(true);
    			factory.setValidating(true);
    			factory.setIgnoringComments(true);
            
                //parsing the xml file and creating the document
                doc=builder.parse(in);
            }catch (Exception ex){ex.printStackTrace();}
        }

        return doc;
    }
    
    /**
     * creates and returns the svg document linked with the given file
     * @param file a file
     * @return the svg document linked with the given file
     */
    public static Document getSVGDocument(File file){

    	Document doc=null;
    	
    	if(file!=null){
    		
    		//getting the dom implementation
    		SAXSVGDocumentFactory documentFactory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName(), true);
    		documentFactory.setValidating(false);
    		
    		try{
    			doc=documentFactory.createSVGDocument(file.toURI().toASCIIString());
    		}catch (Exception ex){ex.printStackTrace();}
    	}
    	
    	return doc;
    }
    
    /**
     * creates and returns the file corresponding to the given path name
     * @param path a path
     * @return the file corresponding to the given path name
     */
    public static File createFile(String path){
    	
    	path=new String(path);
        File file=null;
        
        if(! path.equals("")){
            
            int pos=path.lastIndexOf("/");
            
            if(pos>0){
                
                //getting the path of the directory into file the file denoted by the path could be found
                String parentDirectoryPath=path.substring(0, pos);

                if(parentDirectoryPath!=null && ! parentDirectoryPath.equals("")){
                    
                    File parentDirectory=null;
                    
                    try{
                    	parentDirectory=new File(new URI(parentDirectoryPath));
                    }catch (Exception ex){ex.printStackTrace();}
                    
                    //creating the directory on the file system
                    if(parentDirectory!=null) {
                    	
                    	parentDirectory.mkdirs();
                    }

                    try{
                        //creating the file corresponding to the given path
                        file=new File(new URI(path));
                    	file.createNewFile();
                    }catch (Exception ex){ex.printStackTrace();}
                }
            }
        }

        return file;
    }
    
    /**
     * returns the unique lib path file contained in the project
     * @param libProjectName a project name
     * @return the unique lib path file contained in the project
     */
    protected File getLibPathsFile(String libProjectName){
        
        File libPathsFile=null;
        
        if(libProjectName!=null && ! libProjectName.equals("")){
            
            File projectFile=null;
            
            try{
            	projectFile=new File(new URI(workspacePath+"/"+libProjectName));
            }catch (Exception ex){ex.printStackTrace();}
            
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
     * returns the list of the path files contained in the given file
     * @param libPathsFile the file containing the names of the referenced projects
     * @return the list of the path files contained in the given file
     */
    protected LinkedList<String> getLibPaths(File libPathsFile){
        
        LinkedList<String> list=new LinkedList<String>();
        
        if(libPathsFile!=null && libPathsFile.exists()){
            
            //creating the document that can be found in the given file
            Document doc=getDocument(libPathsFile);
            
            NodeList projectNodes=doc.getDocumentElement().getElementsByTagName("project");
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
                                
                                list.add(URIEncoderDecoder.encode(children.item(j).getNodeValue()));
                            }
                        }
                    }
                }
            }
        }
        
        return list;
    }
    
    /**
     * fills the list of the lib path file names
     * @throws InvalidLibPathException the exception raised if there is a cycle in the lib path files
     * @throws Exception the exception raised if something wrong happens
     */
    protected void fillLibPathFileNamesMap() throws InvalidLibPathException, Exception{
        
        projectNamesList.add(this.projectName);
        
        //adding the current project name to the list
        LinkedList<String> list=null;
        String name=null;
        int i=0;

        //filling the list of the lib paths for the current project and its referenced projects
        do{
            
            //getting the name of the current project
            name=projectNamesList.get(i);
            
            if(name!=null && ! name.equals("")){
                
                //getting the list of the names of the projects referenced by the current project
                list=getLibPaths(getLibPathsFile(name));
                
                //putting the list into the map
                libPathsMap.put(name, list);
                
                //adding the name of the referenced projects to the list of the lib paths
                for(String nm : list){

                    if(nm!=null && ! nm.equals("") && ! projectNamesList.contains(nm)){
                        
                        projectNamesList.add(nm);
                    }
                }
            }
            
            i++;
            
        }while(i<projectNamesList.size());
        
        //checks that no cycle can be found in the lib paths
        checkCycle(new LinkedList<String>(), this.projectName);
    }
    
    /**
     * checks if a cycle can be found in the lib path files
     * @param projectList the list of the project we have already been through
     * @param currentProjectName a project name
     * @throws InvalidLibPathException the exception raised if there is a cycle in the lib path files
     * @throws Exception the exception raised if something wrong happens
     */
    protected void checkCycle(LinkedList<String> projectList, String currentProjectName) throws InvalidLibPathException, Exception{
    	
    	if(projectList!=null && currentProjectName!=null && ! currentProjectName.equals("")){
    		
    		if(! projectList.contains(currentProjectName)){
    			
    			projectList.add(currentProjectName);
    			
    			//getting the list of the project names associated with the current project name
    			LinkedList<String> list=libPathsMap.get(currentProjectName);
    			
    			if(list!=null && list.size()>0){

    				//for each referenced project name, checks if they create a cycle
    				for(String name : list){

    					if(name!=null && ! name.equals("")){
    						
    						checkCycle(new LinkedList<String>(projectList), name);
    					}
    				}
    			}
    			
    		}else{

    			if(projectList.size()>0){
    				
        			//there is a cycle in the lib path files
        			throw new InvalidLibPathException(projectList.getLast());
    			}
                
    			//there is a cycle in the lib path files
                throw new InvalidLibPathException(this.projectName);
    		}
    	}
    }
}
