package fr.itris.glips.compiler.rtdb;

/**
 * the exception that will be raised when a file is missing
 * @author ITRIS, Jordi SUC
 */
public class CorruptedFileException extends Exception {
	
    /**
     * the constructor of the class
     * @param fileName the name of the  file
     */
    public CorruptedFileException(String fileName){
        
        super("Corrupted file : "+fileName);
    }
}
