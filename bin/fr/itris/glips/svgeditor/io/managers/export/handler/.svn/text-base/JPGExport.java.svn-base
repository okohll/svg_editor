package fr.itris.glips.svgeditor.io.managers.export.handler;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.event.*;
import javax.imageio.plugins.jpeg.*;
import javax.imageio.stream.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.io.managers.export.*;
import fr.itris.glips.svgeditor.io.managers.export.handler.dialog.*;
import fr.itris.glips.svgeditor.io.managers.export.monitor.*;

/**
 * the class used to export images in a jpg format
 * @author ITRIS, Jordi SUC
 */
public class JPGExport extends Export{
	
	/**
	 * the quality of the export
	 */
	private float jpgQuality;
	
	/**
	 * whether the Huffman tables are optimized
	 */
	private boolean isOptimized;
	
	/**
	 * whether the compression is progressive
	 */
	private boolean isProgressive;
	
	/**
	 * the constructor of the class
	 * @param fileExport the object manager the export
	 */
	protected JPGExport(FileExport fileExport) {
		
		super(fileExport);
		
		//creating the dialog
		if(Editor.getParent() instanceof Frame){
			
			exportDialog=new JPGExportDialog((Frame)Editor.getParent());
			
		}else{
			
			exportDialog=new JPGExportDialog((JDialog)Editor.getParent());
		}
	}
	
	@Override
	public void export(JComponent relativeComponent, 
			Document document, File destFile) {

		monitor=new ExportMonitor(
				Editor.getParent(), 0, 100, FileExport.prefixLabels[0]);
		monitor.setRelativeComponent(relativeComponent);
		JPGExportDialog jpgExportDialog=(JPGExportDialog)exportDialog;
		
		//showing the dialog used to select the values of the parameters for the export
		int res=exportDialog.showExportDialog(document);
		
		if(res==ExportDialog.OK_ACTION) {
			
			//getting the parameters
			width=jpgExportDialog.getExportSize().getX();
			height=jpgExportDialog.getExportSize().getY();
			jpgQuality=jpgExportDialog.getJpgQuality();
			isOptimized=jpgExportDialog.isOptimized();
			isProgressive=jpgExportDialog.isProgressive();
			
			//creating the image
			createImage(document, destFile, false);
		}
	}
	
	@Override
	protected void writeImage(final BufferedImage image, final File destFile) {
		
		Thread thread=new Thread() {
			
			@Override
			public void run() {
				
				try {
					//creating the IIOImage
					IIOImage iioImage=new IIOImage(image, null, null);
					Iterator<ImageWriter> it=ImageIO.getImageWritersByFormatName("jpeg");
					
					//getting the image writer
					ImageWriter w=null;
					
					while(it.hasNext()) {
						
						w=it.next();
						
						if(w!=null && w.getDefaultWriteParam() instanceof JPEGImageWriteParam) {
							
							break;
						}
					}
					
					final ImageWriter writer=w;
					
					//setting the parameters for the jpeg transcoding
					JPEGImageWriteParam params=new JPEGImageWriteParam(Locale.getDefault());
					params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
					params.setCompressionQuality(jpgQuality);
					params.setOptimizeHuffmanTables(isOptimized);
					
					if(isProgressive) {
						
						params.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
						
					}else {
						
						params.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
					}
					
					//writing the image
					ImageOutputStream out=ImageIO.createImageOutputStream(destFile);
					writer.setOutput(out);
					
					//adding the listener to the writer
					writer.addIIOWriteProgressListener(new IIOWriteProgressListener() {
						
						public void imageComplete(ImageWriter wr) {
							
							writer.removeIIOWriteProgressListener(this);
							monitor.stop();
						}
						
						public void writeAborted(ImageWriter wr) {
							
							writer.removeIIOWriteProgressListener(this);
							handleExportFailure();
						}
						
						public void imageProgress(ImageWriter wr, float value) {
							
							monitor.setProgress((int)(50+value/2));

							if(monitor.isCancelled()) {
								
								writer.abort();
							}
						}
						
						public void imageStarted(ImageWriter arg0, int arg1) {}
						
						public void thumbnailComplete(ImageWriter wr) {}
						
						public void thumbnailProgress(ImageWriter wr, float arg1) {}
						
						public void thumbnailStarted(ImageWriter wr, int arg1, int arg2) {}
					});
					
					writer.write(null, iioImage, params);
					
					//cleaning up
					out.flush();
					writer.removeAllIIOWriteProgressListeners();
					monitor.stop();
					writer.dispose();
					out.close();
				}catch (Exception ex) {
					
					handleExportFailure();
				}
			}
		};
		
		thread.start();
	}
}
