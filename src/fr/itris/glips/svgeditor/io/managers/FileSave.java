package fr.itris.glips.svgeditor.io.managers;

import java.awt.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.monitor.*;
import fr.itris.glips.library.util.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import fr.itris.glips.svgeditor.io.managers.dialog.*;
import fr.itris.glips.svgeditor.io.managers.monitor.*;

/**
 * the class handling the saving of files
 * @author Jordi SUC
 */
public class FileSave {

	/**
	 * the io manager
	 */
	private IOManager ioManager;
	
	/**
	 * the file chooser dialog
	 */
	private FileChooserDialog fileChooserDialog;
	
	/**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
	public FileSave(IOManager ioManager){
		
		this.ioManager=ioManager;

		//creating the file chooser dialog
		if(Editor.getParent() instanceof Frame){
			fileChooserDialog=new FileChooserDialog(
				(Frame)Editor.getParent(), FileChooserDialog.SAVE_FILE_MODE);			
		}else if(Editor.getParent() instanceof JDialog){
			fileChooserDialog=new FileChooserDialog(
				(JDialog)Editor.getParent(), FileChooserDialog.SAVE_FILE_MODE);
		}
		
		//setting the file filter
		fileChooserDialog.setFileFilter(new SVGFileFilter());
	}
	
	/**
	 * saves the document denoted by the handle 
	 * into the file the user has chosen, or the file that was 
	 * previously chosen or opened
	 * @param handle a svg handle
	 * @param saveAs whether the action is a saveAs one
	 * @param relativeComponent the component relatively 
	 * to which the dialog will be shown
	 * @return whether the save action has been launched
	 */
	public boolean saveHandleDocument(SVGHandle handle, 
			boolean saveAs, JComponent relativeComponent){
		
		//getting the file corresponding to the current handle
		File handleFile=getFile(handle);

		if(handleFile==null || saveAs){
			
			//getting the file where to save the document
			fileChooserDialog.showDialog(relativeComponent);
			
			//getting the selected file
			File selectedFile=fileChooserDialog.getSelectedFile();
			
			if(selectedFile!=null){
				
				selectedFile=checkFileExtension(selectedFile);

				//creating the monitor
				Monitor monitor=new SaveMonitor(
					Editor.getParent(), relativeComponent, 
						0, getNodesCount(handle.getCanvas().getDocument()));
				
				//saving the svg document into the selected file
				saveDocument(selectedFile, handle, monitor, true);
				return true;
			}
			
		}else{

			//creating the monitor
			Monitor monitor=new SaveMonitor(
				Editor.getParent(), relativeComponent, 0, 
					getNodesCount(handle.getCanvas().getDocument()));
			
			//saving the svg document into the found file
			saveDocument(handleFile, handle, monitor, false);
			return true;
		}
		
		return false;
	}
	
	/**
	 * saves the provided document into the provided file
	 * @param file a file
	 * @param handle the handle to save
	 * @param monitor the monitor used to display 
	 * the state of the save action
	 * @param handleNameChanged whether the handle name has changed
	 */
	public void saveDocument(
		final File file, final SVGHandle handle, final Monitor monitor, 
			boolean handleNameChanged){
		
		//getting the path corresponding to the file
		String filePath=file.toURI().toASCIIString();
		
		if(handleNameChanged){

			//setting the new name for the handle
			 Editor.getEditor().getHandlesManager().
			 	changeName(handle.getName(), filePath);
			 handle.getCanvas().setNewURI(filePath);
			 
			//storing the name of the file in the recent open files
	        Editor.getEditor().getResourcesManager().addRecentFile(filePath);
	        Editor.getEditor().getResourcesManager().notifyListeners();
		}
		
		 //setting the state of the handle to no more modified
		 handle.setModified(false);
		 final Document doc=handle.getCanvas().getDocument();
        
        //creating the runnable that will be added to the runnable queue
        ioManager.requestExecution(new Runnable(){
        	
        	public void run() {

                //prints the document into a file
        		XMLPrinter.printXML(doc, file, monitor);
        	}
        });
	}
	
	/**
	 * checks if the provided file has an extension. 
	 * If not, the "svg" extension is added to it.
	 * @param file a file
	 * @return the file whose extension has been corrected if needed
	 */
	protected File checkFileExtension(File file){
		
		File newFile=file;
		
		//getting the path of the file
		String filePath=file.toURI().toASCIIString();
		
		if(filePath.indexOf(".")==-1){
			
			//the file has no extension, the svg one is then added
			filePath+=EditorToolkit.SVG_FILE_EXTENSION;
			
			//creating the new file
			try{
				newFile=new File(new URI(filePath));
			}catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		
		return newFile;
	}
	
	/**
	 * returns the file corresponding to this svg handle
	 * @param handle a svg handle
	 * @return the file corresponding to this svg handle
	 */
	protected File getFile(SVGHandle handle){
		
		File file=null;
		
			try {
				file=new File(new URI(handle.getName()));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if(file!=null && ! file.exists()){
			
			file=null;
		}
		
		return file;
	}
	
	/**
	 * returns the nodes number that could be 
	 * found in the provided document
	 * @param doc a document
	 * @return the nodes number that could be 
	 * found in the provided document
	 */
	protected int getNodesCount(Document doc){
		
		int nodesCount=
			XMLPrinter.getNodesCount(doc);
		nodesCount+=nodesCount/5;
		
		return nodesCount;
	}
}
