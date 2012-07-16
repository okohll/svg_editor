package fr.itris.glips.svgeditor.io.managers.export.handler;

import fr.itris.glips.library.monitor.*;
import fr.itris.glips.svgeditor.io.managers.export.*;
import fr.itris.glips.svgeditor.io.managers.export.handler.dialog.*;
import fr.itris.glips.svgeditor.io.managers.export.image.*;
import fr.itris.glips.svgeditor.resources.*;
import org.w3c.dom.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

/**
 * the abstract class of the all the classes that allow image format conversion
 * @author ITRIS, Jordi SUC
 */
public abstract class Export {
	
	/**
	 * the export dialog
	 */
	protected ExportDialog exportDialog;
	
	/**
	 * the monitor
	 */
	protected Monitor monitor;
	
	/**
	 * the labels
	 */
	protected String errorMessage="";
	
	/**
	 * the width and height of the document
	 */
	protected double width, height;
	
	/**
	 * the constructor of the class
	 * @param fileExport the object manager the export
	 */
	protected Export(FileExport fileExport) {

		//getting the label
		errorMessage=ResourcesManager.bundle.getString("FileExportErrorMessage");
	}
	
	/**
	 * returns the export manager corresponding to the given export type
	 * @param index an index in the format array
	 * @param fileExport the file export
	 * to which the dialogs will be shown
	 * @return the export manager corresponding to the given export type
	 */
	public static Export getExport(int index, FileExport fileExport) {
		
		Export exportObject=null;
		int exportType=FileExport.formats[index];
		
		switch(exportType) {
		
			case FileExport.JPG_FORMAT :

				exportObject=new JPGExport(fileExport);
				break;
				
			case FileExport.PNG_FORMAT :

				exportObject=new PNGExport(fileExport);
				break;
				
			case FileExport.BMP_FORMAT :

				exportObject=new BMPExport(fileExport);
				break;

			case FileExport.PDF_FORMAT :
				
				exportObject=new PDFExport(fileExport);
				break;
		}
		
		return exportObject;
	}
	
	/**
	 * exports the svg document into the file denoted by the given 
	 * path with the export object format
	 * @param relativeComponent the relative component
	 * @param document the svg document
	 * @param destFile the destination export file
	 */
	public abstract void export(JComponent relativeComponent, 
		Document document, File destFile);
	
	/**
	 * creates the image corresponding to the given svg document
	 * @param document a svg document
	 * @param destFile the destination export file
	 * @param useAlpaChannel whether the image should use a alpha channel
	 */
	protected void createImage(Document document, 
			final File destFile, boolean useAlpaChannel) {
		
        //showing the progress bar
        monitor.start();
        monitor.setProgress(0);
        
        //creating the image that will be converted
        SVGDocumentImageCreator imageCreator=
        	new SVGDocumentImageCreator(document, monitor);
        
        //adding the listener that will be called when the image is created
        SVGDocumentImageCreatorListener listener=
        	new SVGDocumentImageCreatorListener() {
        	
        	@Override
        	public void imageCreated(BufferedImage image) {
        		
        		writeImage(image, destFile);
        	}
        };
        
        imageCreator.addImageCreatorListener(listener);
        imageCreator.createImage(new Dimension(
        	(int)width, (int)height), useAlpaChannel);
	}
	
	/**
	 * writes the image that has been created at the accurate format
	 * @param image the image corresponding to the svg document
	 * @param destFile the destination export file
	 */
	protected abstract void writeImage(BufferedImage image, File destFile);
	
	/**
	 * @return the monitor
	 */
	public Monitor getMonitor() {
		return monitor;
	}

	/**
	 * handles the failure of the export action
	 */
	public void handleExportFailure() {
		
		monitor.setErrorMessage(errorMessage);
		monitor.setIndeterminate(false);
		monitor.setProgress(0);
		monitor.setErrorMessage(errorMessage);
	}
}
