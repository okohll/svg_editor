package fr.itris.glips.svgeditor.io.managers.export.handler;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.event.*;
import javax.imageio.plugins.bmp.*;
import javax.imageio.stream.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.io.managers.export.*;
import fr.itris.glips.svgeditor.io.managers.export.handler.dialog.*;
import fr.itris.glips.svgeditor.io.managers.export.monitor.*;

/**
 * the class used to export images in a bmp format
 * @author ITRIS, Jordi SUC
 */
public class BMPExport extends Export{
	
    /**
     * whether the bmp file should use indexed colors
     */
    private boolean usePalette=false;
	
	/**
	 * the constructor of the class
	 * @param fileExport the object manager the export
	 */
	protected BMPExport(FileExport fileExport) {
		
		super(fileExport);
		
		//creating the dialog
		if(Editor.getParent() instanceof Frame){
			
			exportDialog=new BMPExportDialog((Frame)Editor.getParent());
			
		}else{
			
			exportDialog=new BMPExportDialog((JDialog)Editor.getParent());
		}
	}
	
	@Override
	public void export(JComponent relativeComponent, 
			Document document, File destFile) {

		monitor=new ExportMonitor(
				Editor.getParent(), 0, 100, FileExport.prefixLabels[2]);
		monitor.setRelativeComponent(relativeComponent);
		BMPExportDialog bmpExportDialog=(BMPExportDialog)exportDialog;
		
		//showing the dialog used to select the values of the parameters for the export
		int res=exportDialog.showExportDialog(document);
		
		if(res==ExportDialog.OK_ACTION) {
			
			//getting the parameters
			width=bmpExportDialog.getExportSize().getX();
			height=bmpExportDialog.getExportSize().getY();
			usePalette=bmpExportDialog.usePalette();
			
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
					Iterator<ImageWriter> it=ImageIO.getImageWritersByFormatName("bmp");
					
					//getting the image writer
					ImageWriter w=null;
					
					while(it.hasNext()) {
						
						w=it.next();
						
						if(w!=null && w.getDefaultWriteParam() instanceof BMPImageWriteParam) {
							
							break;
						}
					}
					
					//getting the image writer
					final ImageWriter writer=w;
					
					//the parameters for the conversion
                    BMPImageWriteParam param=new BMPImageWriteParam();
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    
                    if(usePalette) {
                        
                        param.setCompressionType("BI_RGB");
                        
                    }else {
                        
                        param.setCompressionType("BI_BITFIELDS");
                    }
                    
                    //creating the destination file
                    ImageOutputStream out=ImageIO.createImageOutputStream(
                    	new RandomAccessFile(destFile, "rw"));
                    
                    //writing the image
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
                        
                        public void imageStarted(ImageWriter wr, int arg1) {}
                        
                        public void thumbnailComplete(ImageWriter wr) {}
                        
                        public void thumbnailProgress(ImageWriter wr, float arg1) {}
                        
                        public void thumbnailStarted(ImageWriter wr, int arg1, int arg2) {}
                    });
                    
                    writer.write(null, iioImage, param);
					
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
