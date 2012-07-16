/*
 * Created on 25 mai 2005
 */
package fr.itris.glips.compiler.rtdb;

/**
 * the exception that will be raised when an invalid inherited node error occurs
 * 
 * @author ITRIS, Jordi SUC
 */
public class InvalidInheritedNodeException extends Exception {
	
    /**
     * the constructor of the class
     * @param fileName the name of the file containing the wrong "inherit" attribute
     * @param inheritedFileName the name of the file that is inherited by the first file
     */
    public InvalidInheritedNodeException(String fileName, String inheritedFileName){
        
        super(	"A cycle has been found in the inheritance tree :\nin the \""+fileName+"\" file, the reference " +
        			"of the inherit attribute to the file \""+inheritedFileName+"\" is wrong");
    }
}
