package fr.itris.glips.svgeditor.io.managers;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.library.monitor.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import fr.itris.glips.svgeditor.io.managers.dialog.*;
import fr.itris.glips.svgeditor.io.managers.monitor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling the opening of files
 * @author Jordi SUC
 */
public class FileOpen {
	
	/**
	 * the io manager
	 */
	private IOManager ioManager;
	
	/**
	 * the file chooser dialog
	 */
	private FileChooserDialog fileChooserDialog;

	/**
	 * the labels
	 */
	private String warningNotNullMessage="", 
		warningNullMessage="", warningTitle="";
	
	/**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
	public FileOpen(IOManager ioManager){
		
		this.ioManager=ioManager;
		
		//creating the file chooser dialog
		if(Editor.getParent() instanceof Frame){
			
			fileChooserDialog=new FileChooserDialog(
				(Frame)Editor.getParent(), FileChooserDialog.OPEN_FILE_MODE);
			
		}else if(Editor.getParent() instanceof JDialog){
			
			fileChooserDialog=new FileChooserDialog(
				(JDialog)Editor.getParent(), FileChooserDialog.OPEN_FILE_MODE);
		}
		
		//setting the file filter
		fileChooserDialog.setFileFilter(new SVGFileFilter());
		
		//getting the labels from the resources store
		ResourceBundle bundle=ResourcesManager.bundle;
		
		if(bundle!=null){
		    
			try{
				warningNotNullMessage=bundle.getString("OpenFailedErrorFileNotNullMessage");
				warningNullMessage=bundle.getString("OpenFailedErrorFileNullMessage");
				warningTitle=bundle.getString("OpenFailedErrorTitle");
			} catch (MissingResourceException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * shows a file chooser so that the user can choose the files to open
	 * @param relativeComponent the component relatively 
	 * to which the dialog will be shown
	 */
	public void askUserForFile(JComponent relativeComponent){
		
		fileChooserDialog.showDialog(relativeComponent);
		
		//getting the array of the files selected by the user
		File[] selectedFiles=fileChooserDialog.getSelectedFiles();

		if(selectedFiles!=null && selectedFiles.length>0){
			
			Monitor monitor=null;
			
			for(File file : selectedFiles){
				
				//creating the monitor
				monitor=new OpenMonitor(file,
					Editor.getParent(), relativeComponent, 0, 100);
				
				//opening the file
				open(file, monitor);
			}
		}
	}
	
	/**
	 * open the provided file
	 * @param file a file
	 * @param monitor the object used to monitor the opening of the file
	 */
	public void open(final File file, final Monitor monitor){
		
		SVGHandle handle=null;
		
		if(file!=null && file.exists()){
		    
			//checking if a svg file having the same name already exists
			String path=file.toURI().toASCIIString();
			handle=Editor.getEditor().getHandlesManager().getHandle(path);
			
			if(handle!=null){
				
				Editor.getEditor().getHandlesManager().setCurrentHandle(path);
				
			}else{
				
				//creating a new handle
				handle=Editor.getEditor().getHandlesManager().
					createSVGHandle(file.toURI().toASCIIString());
				
				//adding the file name to the list of the recent files
				Editor.getEditor().getResourcesManager().addRecentFile(file.toURI().toASCIIString());
				Editor.getEditor().getResourcesManager().notifyListeners();
				final SVGCanvas canvas=handle.getScrollPane().getSVGCanvas();
				
				Runnable runnable=new Runnable(){
					
					public void run() {

						//setting the uri of the svg file
						canvas.setURI(file.toURI().toASCIIString(), monitor);
					}
				};
				
				ioManager.requestExecution(runnable);
			}
			
		}else{
			
			//computing the warning message
			String message=warningNullMessage;
			
			if(file!=null){
				
				message=warningNotNullMessage+
					file.getAbsolutePath()+".</body></html>";
			}
			
			//if the file could not be opened, a dialog is 
			//displayed to notify that an error occured
			JOptionPane.showMessageDialog(
				Editor.getParent(), message, 
					warningTitle, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * @return the linked list of the recently opened files. 
	 */
	public LinkedList<File> getRecentFiles(){
		
		//the list that will be returned
		LinkedList<File> recentFiles=
			new LinkedList<File>();
		
		//getting the collection of the path of the last recently opened svg files
		Collection<String> filePaths=
			Editor.getEditor().getResourcesManager().getRecentFiles();
		
		//filling the list
		File file=null;
		
		for(String path : filePaths){
			
			try{
				file=new File(new URI(path));
				recentFiles.add(file);
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}	

		return recentFiles;
	}
}
