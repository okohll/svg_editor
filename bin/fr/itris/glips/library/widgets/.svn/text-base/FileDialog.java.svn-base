package fr.itris.glips.library.widgets;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import fr.itris.glips.library.*;
import java.awt.*;
import java.io.File;
import java.util.*;

/**
 * the class of the dialog used to choose a file in the file system
 * @author Jordi SUC
 */
public class FileDialog {

	/**
	 * the file chooser
	 */
	private JFileChooser fileChooser=new JFileChooser();
	
	/**
	 * the description label used for the file filter
	 */
	private String descriptionLabel="";
	
	{
		descriptionLabel=Bundle.bundle.getString("FileFilterDescription");
	}
	
	/**
	 * the parent of the dialog
	 */
	private Component parent;
	
	/**
	 * the last file selected by the user
	 */
	private File lastSelectedFile;
	
	/**
	 * the constructor of the class
	 * @param frame the parent frame
	 */
	public FileDialog(Frame frame){
		
		this.parent=frame;
	}
	
	/**
	 * the constructor of the class
	 * @param dialog the parent dialog
	 */
	public FileDialog(JDialog dialog){
		
		this.parent=dialog;
	}
	
	/**
	 * ask the user to choose a file in the file system
	 * @param fileExtensions the array of the file extensions 
	 * that should appear in the file chooser
	 * @param startFile the file that should be initialy shown in the file chooser
	 * @param excludedFiles the array of the files that should not be displayed
	 * @return the choosen file
	 */
	public File askForFile(final String[] fileExtensions, 
			File startFile, File[] excludedFiles){
		
		//the file that will be returned
		File file=null;
		
		//creating the set of the excluded files
		final Set<File> excludedFilesSet=new HashSet<File>();
		
		for(File exFile : excludedFiles){
			
			excludedFilesSet.add(exFile);
		}
		
		//creating the file filter
		FileFilter filter=new FileFilter(){

			@Override
			public boolean accept(File theFile) {
			
				boolean accept=false;
				String fileName=theFile.getName().toLowerCase();
				
				for(String extension : fileExtensions){
					
					if(theFile.isDirectory() || (! excludedFilesSet.contains(theFile) && 
							fileName.endsWith(extension))){
						
						accept=true;
						break;
					}
				}
				
				return accept;
			}

			@Override
			public String getDescription() {
				
				String description=descriptionLabel;
				
				for(String extension : fileExtensions){
					
					description+="\"."+extension+"\" ";
				}
				
				return description;
			}
		};
		
		//setting the file filter
		fileChooser.setFileFilter(filter);
		
		//setting the start file
		if(startFile!=null){
			
			fileChooser.setCurrentDirectory(startFile);
		}
		
		fileChooser.setMultiSelectionEnabled(false); 
		
		//showing the dialog
		int returnVal=fileChooser.showOpenDialog(parent);
		
	    if(returnVal==JFileChooser.APPROVE_OPTION) {
	    	
	    	file=fileChooser.getSelectedFile();
	    }
	    
	    lastSelectedFile=file;
		
		return file;
	}

	/**
	 * @return the last file selected by the user
	 */
	public File getLastSelectedFile() {
		return lastSelectedFile;
	}
}
