package fr.itris.glips.svgeditor.io.managers.creation;

import java.awt.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;


import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling the file creation
 * @author Jordi SUC
 */
public class FileNew {

	/**
	 * the dialog used to specify the parameters for the new files to create
	 */
	private NewDialog newDialog;
	
	/**
	 * the labels
	 */
	private String untitledLabel="";
	
	/**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
	public FileNew(IOManager ioManager){
		
		//gets the labels from the resources
		ResourceBundle bundle=ResourcesManager.bundle;
		
		if(bundle!=null){
		    
			try{
				untitledLabel=bundle.getString("FileNewUntitled");
			}catch (MissingResourceException ex) {
				ex.printStackTrace();
			}
		}
		
		//creating the dialog used to specify the parameters for the new files to create
		if(Editor.getParent() instanceof Frame){
			
			newDialog=new NewDialog(this, (Frame)Editor.getParent());
			
		}else if(Editor.getParent() instanceof JDialog){
			
			newDialog=new NewDialog(this, (JDialog)Editor.getParent());
		}
	}
	
	/**
	 * asks the user for the parameters to create the new file
	 * @param relativeComponent the component relatively 
	 * to which the dialog should be displayed
	 */
	public void askForNewFileParameters(JComponent relativeComponent){
		
		newDialog.showDialog(relativeComponent);
	}
	
	/**
	 * creates a new document
	 * @param width the width of the new document
	 * @param height the height of the new document
	 */
	public void createNewDocument(String width, String height){
	    
		SVGHandle handle=
			Editor.getEditor().getHandlesManager().
				createSVGHandle(untitledLabel, true);
		
		if(handle!=null){
		    
		    handle.getScrollPane().getSVGCanvas().
		    	newDocument(width, height);
				
				// Immediately save the newly created file
		    // so that importing SVGs works - won't work if parent not saved to disk because of relative path errors
		    IOManager ioManager = Editor.getEditor().getIOManager();
				ioManager.getFileSaveManager().saveHandleDocument(handle, true, null);
				// Then close and re-open the file, otherwise errors occur when SVG patterns in the new file are used 
				String filename = handle.getName();
				// TODO: return here if cancel button pressed
				ioManager.getFileCloseManager().close(handle);
				File file = new File(filename.replace("file:", ""));
				// monitor parameter is null. We don't need to monitor the progress on opening a blank file, it should be quick
				ioManager.getFileOpenManager().open(file, null);
		}
	}
}
