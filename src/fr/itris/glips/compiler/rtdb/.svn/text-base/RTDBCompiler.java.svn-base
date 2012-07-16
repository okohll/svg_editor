/*
 * Created on 25 mai 2005
 */
package fr.itris.glips.compiler.rtdb;

import fr.itris.glips.compiler.rtdb.file.*;

/**
 * the main class for the compiler
 * @author ITRIS, Jordi SUC
 */
public class RTDBCompiler{

	/**
	 * the file manager
	 */
	FileManager fileManager=null;
	
    /**
     * the constructor of the class
     * @param workspacePath the path of the workspace of the project that is handled
     * @param projectName the name of the project that is handled
     * @param fileName the name of the xml file that is to be compiled
     * @param outputDirectory the output directory 
     * @param viewPath the root view path
     * @param isTestMode whether the compiler is in the test mode
     */
    public RTDBCompiler(
    		String workspacePath, String projectName, 
    			String fileName, String outputDirectory, 
    				String viewPath, boolean isTestMode){
        
        if(workspacePath!=null && ! workspacePath.equals("") &&
            projectName!=null && ! projectName.equals("") &&
            fileName!=null && ! fileName.equals("")){

            fileManager=new FileManager(
            		workspacePath, projectName, fileName, 
            		outputDirectory, viewPath, isTestMode);

        }else{
            
            System.out.println("The parameters are not valid : fill in the following parameters <workspacePath> <projectName> "+
            							"<fileName> <outputDirectory> <viewPath> <isTestMode>");
        }
    }
    
    /**
     * compiles the given application
     */
    public void compile(){
    	
    	if(fileManager!=null){
    		
            try{
                fileManager.compile();
            }catch (Exception ex){ex.printStackTrace();}
    	}
    }
    
    /**
     * the main method
     * @param args the array of the arguments
     */
    public static void main(String[] args) {

        RTDBCompiler compiler=new RTDBCompiler(args[0], args[1], args[2], args[3], args[4], Boolean.parseBoolean(args[5]));
        compiler.compile();
    }
}
