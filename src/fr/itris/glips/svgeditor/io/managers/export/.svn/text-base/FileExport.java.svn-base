package fr.itris.glips.svgeditor.io.managers.export;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import fr.itris.glips.svgeditor.io.managers.dialog.*;
import fr.itris.glips.svgeditor.io.managers.export.handler.*;

/**
 * the class handling the exporting of files
 * @author Jordi SUC
 */
public class FileExport {
	
	   /**
     * the constant for the JPG format
     */
    public static final int JPG_FORMAT=0;
    
    /**
     * the constant for the PNG format
     */
    public static final int PNG_FORMAT=1;
    
    /**
     * the constant for the BMP format
     */
    public static final int BMP_FORMAT=2;
    
    /**
     * the constant for the PDF format
     */
    public static final int PDF_FORMAT=3;
    
    /**
     * the array of the formats that will be used
     */
    public static int[] formats={
    	JPG_FORMAT, PNG_FORMAT, BMP_FORMAT, PDF_FORMAT};
    
    /**
     * the array of the extensions of the files to be created
     */
    public static String[] extensions={".jpg", ".png", ".bmp", ".pdf"};
    
    /**
     * the array of the prefixes used to retrieve labels
     */
    public static String[] prefixLabels={"JPG", "PNG", "BMP", "PDF"};
    
    /**
     * the array of the export handlers
     */
    private Export[] exportHandlers=new Export[formats.length];
    
    /**
     * the array of the file chooser dialogs
     */
    private FileChooserDialog[] fileChooserDialogs=
    	new FileChooserDialog[formats.length];

	/**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
	public FileExport(IOManager ioManager){
		
		initialize();
	}
	
	/**
	 * initializes the manager
	 */
	protected void initialize(){
		
		//creating the handlers and the file filters
		ExportFileFilter fileFilter=null;
		
		for(int i=0; i<formats.length; i++){
			
			exportHandlers[i]=Export.getExport(formats[i], this);
			fileFilter=new ExportFileFilter(formats[i]);
			
			//creating the file chooser dialogs
			if(Editor.getParent() instanceof Frame){
				
				fileChooserDialogs[i]=new FileChooserDialog(
					(Frame)Editor.getParent(), FileChooserDialog.EXPORT_FILE_MODE);
				
			}else if(Editor.getParent() instanceof JDialog){
				
				fileChooserDialogs[i]=new FileChooserDialog(
					(JDialog)Editor.getParent(), FileChooserDialog.EXPORT_FILE_MODE);
			}
			
			fileChooserDialogs[i].setFileFilter(fileFilter);
		}
	}
	
	/**
	 * exports an image representation of the provided handle ; 
	 * the type of the export is defined by the provided index in the format array
	 * @param handle a handle
	 * @param index an index in the format array
	 * @param relativeComponent the component relatively 
	 * to which the dialog will be shown
	 */
	public void export(final SVGHandle handle, final int index, 
			final JComponent relativeComponent){
		
		//setting the file filter

		
		//getting the file where to save the document
		fileChooserDialogs[index].showDialog(relativeComponent);
		
		//getting the selected file
		File selectedFile=fileChooserDialogs[index].getSelectedFile();
		
		if(selectedFile!=null){
			
			selectedFile=checkFileExtension(selectedFile, formats[index]);

			//getting the export manager associated to the provided format index
			Export exportManager=exportHandlers[index];
			exportManager.export(relativeComponent,
				handle.getCanvas().getDocument(), selectedFile);
		}
	}
	
	/**
	 * checks if the provided file has an extension. 
	 * If not, the "svg" extension is added to it.
	 * @param file a file
	 * @param index an index in the format array
	 * @return the file whose extension has been corrected if needed
	 */
	protected File checkFileExtension(File file, int index){
		
		File newFile=file;
		
		//getting the path of the file
		String filePath=file.toURI().toASCIIString();
		
		if(filePath.indexOf(".")==-1){
			
			//the file has no extension, the svg one is then added
			filePath+=extensions[index];
			
			//creating the new file
			try{
				newFile=new File(new URI(filePath));
			}catch (Exception ex){}
		}
		
		return newFile;
	}
}
