/*
 * Created on 25 mai 2005
 */
package fr.itris.glips.compiler.rtdb;

/**
 * the exception that will be raised when an invalid inherited node error occurs
 * 
 * @author ITRIS, Jordi SUC
 */
public class InvalidLibPathException extends Exception {
    
    /**
     * the constructor of the class
     * @param projectName the name of the project of the lib path being incorrect
     */
    public InvalidLibPathException(String projectName){
        
        super(	"A cycle has been found in the \""+projectName+"\" project lib path file");
    }
}
