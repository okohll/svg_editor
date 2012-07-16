/*
 * Created on 1 juil. 2005
 */
package fr.itris.glips.compiler.rtdb.file;

import org.w3c.dom.*;
import java.util.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class CompiledColorsBlinkingsFile extends CompiledFile {

	/**
	 * the map associating an old id to a new id of the colors and blinkings
	 */
	protected HashMap<String, String> oldIdsToNewIds=new HashMap<String, String>();
	
    /**
     * the constructor of the class
     * @param fileManager the file manager
     * @param path the path of the file
     * @param doc the document of this compiled file
     * @param oldIdsToNewIds the map associating an old id to a new id of the colors and blinkings
     */
	public CompiledColorsBlinkingsFile(FileManager fileManager, String path, Document doc, HashMap<String, String> oldIdsToNewIds){
		
        super(fileManager, path, doc, XML_FILE);
        
        this.oldIdsToNewIds=oldIdsToNewIds;
	}

	/**
	 * @return Returns the oldIdsToNewIds.
	 */
	public HashMap<String, String> getOldIdsToNewIds() {
		return oldIdsToNewIds;
	}
}
