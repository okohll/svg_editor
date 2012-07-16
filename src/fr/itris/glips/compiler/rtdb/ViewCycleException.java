/*
 * Created on 25 mai 2005
 */
package fr.itris.glips.compiler.rtdb;

/**
 * the exception that will be raised when an cycle in the view tree occurs
 * @author ITRIS, Jordi SUC
 */
public class ViewCycleException extends Exception {
	
    /**
     * the constructor of the class
     * @param fileName the name of the file containing the wrong "view" attribute
     * @param insertedFileName the name of the file that is inserted in the first file
     */
    public ViewCycleException(String fileName, String insertedFileName){
        
        super(	"A cycle has been found in the view hierarchy :\nin the \""+fileName+"\" file, the reference " +
        			"of the view attribute to the file \""+insertedFileName+"\" is wrong");
    }
}
