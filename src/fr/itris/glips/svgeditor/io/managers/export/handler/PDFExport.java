package fr.itris.glips.svgeditor.io.managers.export.handler;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.*;
import org.w3c.dom.Document;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.io.managers.export.*;
import fr.itris.glips.svgeditor.io.managers.export.handler.dialog.*;
import fr.itris.glips.svgeditor.io.managers.export.image.*;
import fr.itris.glips.svgeditor.io.managers.export.monitor.*;

/**
 * the class used to export images in a jpg format
 * @author ITRIS, Jordi SUC
 */
public class PDFExport extends Export{
	
	   /**
     * the size of the pages
     */
    protected com.itextpdf.text.Rectangle pageSize=null;
    
    /**
     * whether the orientation is "portrait"
     */
    protected boolean isPortrait=true;
    
    /**
     * the margins
     */
    protected Insets margins=new Insets(0, 0, 0, 0);
    
    /**
     * information on the pdf file
     */
    protected String title="", author="", subject="", keywords="", creator="";
	
	/**
	 * the constructor of the class
	 * @param fileExport the object manager the export
	 */
	protected PDFExport(FileExport fileExport) {
		
		super(fileExport);
		
		//creating the dialog
		if(Editor.getParent() instanceof Frame){
			
			exportDialog=new PDFExportDialog((Frame)Editor.getParent());
			
		}else{
			
			exportDialog=new PDFExportDialog((JDialog)Editor.getParent());
		}
	}
	
	@Override
	public void export(JComponent relativeComponent, 
			Document document, File destFile) {

		monitor=new ExportMonitor(
				Editor.getParent(), 0, 100, FileExport.prefixLabels[3]);
		monitor.setRelativeComponent(relativeComponent);
		PDFExportDialog pdfExportDialog=(PDFExportDialog)exportDialog;
		
		//showing the dialog used to select the values of the parameters for the export
		int res=exportDialog.showExportDialog(document);
		
		if(res==ExportDialog.OK_ACTION) {

			//getting the parameters
			pageSize=pdfExportDialog.getPageSize();
            isPortrait=pdfExportDialog.isPortrait();
            margins=pdfExportDialog.getMargins();
            title=pdfExportDialog.getTitle();
            author=pdfExportDialog.getAuthor();
            subject=pdfExportDialog.getSubject();
            keywords=pdfExportDialog.getKeywords();
            creator=pdfExportDialog.getCreator();

			//creating the image
            writeImage(document, destFile);
		}
	}
	
	@Override
	protected void writeImage(final BufferedImage image, final File destFile) {}
	
	/**
	 * writes the image to export
	 * @param document a document
	 * @param destFile the destination file
	 */
	protected void writeImage(final org.w3c.dom.Document document, final File destFile) {
		
		Thread thread=new Thread() {
			
			/**
			 * whether the pdf document is open
			 */
			private boolean pdfDocOpen=true;
			
			@Override
			public void run() {
				
		        //showing the progress bar
		        monitor.start();
		        monitor.setProgress(0);
				
				UserAgentAdapter userAgent=null;
				GVTBuilder builder=null;
			    BridgeContext ctx=null;
			    RootGraphicsNode gvtRoot=null;
				
				try {
					//creating the graphics node
		    		userAgent=new UserAgentAdapter();
		    		ctx=new BridgeContext(userAgent, null, new DocumentLoader(userAgent));
		            builder=new GVTBuilder();
		            ctx.setDynamicState(BridgeContext.STATIC);
		            monitor.setProgress(5);
		            
		            if(monitor.isCancelled()) {

		            	monitor.stop();
		            	ctx.dispose();
		            	return;
		            }
		            
		            GraphicsNode gvt=builder.build(ctx, document);
		            monitor.setProgress(40);
		            
		            if(monitor.isCancelled()) {

		            	monitor.stop();
		            	ctx.dispose();
		            	return;
		            }
		            
		            if(gvt!=null) {
		            	
		            	gvtRoot=gvt.getRoot();
		            	
		            	//the output stream used to write the pdf file
	                    final BufferedOutputStream out=new BufferedOutputStream(
	                    		new FileOutputStream(destFile));
	                    com.itextpdf.text.Rectangle rect=pageSize;
	                    
	                    //setting the orientation of the pdf
	                    rect=isPortrait?rect:rect.rotate();
	                    
	                    //creating the pdf document and the writer
	                    final com.itextpdf.text.Document pdfDoc=new com.itextpdf.text.Document(
	                    		rect, margins.left, margins.right, margins.top, margins.bottom){
	                    	@Override
	                        public void open() {
	                            
	                            super.open();
	                            synchronized(PDFExport.this) {pdfDocOpen=true;}
	                        }
	                        
	                    	@Override
	                        public void close() {
	                            
	                            super.close();
	                            synchronized(PDFExport.this) {pdfDocOpen=false;}
	                        }
	                    };
	                    //adding meta data
	                    pdfDoc.addTitle(title);
	                    pdfDoc.addAuthor(author);
	                    pdfDoc.addSubject(subject);
	                    pdfDoc.addKeywords(keywords);
	                    pdfDoc.addCreator(creator);
	                    
	                    PdfWriter writer=PdfWriter.getInstance(pdfDoc, out);
	                    pdfDoc.open();
	           
	                    //getting the pdf graphics
	                    PdfContentByte cb=writer.getDirectContent();
	                    final PdfTemplate tp=cb.createTemplate(rect.getWidth(), rect.getHeight());
	                    tp.setWidth(rect.getWidth());
	                    tp.setHeight(rect.getHeight());
	                    
	                    Graphics2D g2=tp.createGraphics(
	                    		rect.getWidth(), rect.getHeight(), new DefaultFontMapper());
	                    SVGDocumentImageCreator.handleGraphicsConfiguration(g2);
	                    
	                    //the thread checking the pdf document state
	                    Thread writerStateThread=new Thread() {

	                    	@Override
	                    	public void run() {

	                    		while(pdfDocOpen) {

	                    			if(monitor.isCancelled()) {

	                    				synchronized(PDFExport.this) {pdfDocOpen=false;}

	                    					tp.reset();
	                    					pdfDoc.close();

	                    				monitor.stop();
	                    				break;
	                    			}

	                    			try {sleep(500);}catch (InterruptedException ex) {
	                    				Thread.currentThread().interrupt();
	                    			}
	                    		}
	                    	} 
	                    };
                        
                        writerStateThread.start();

	                    //painting
                        monitor.setIndeterminate(true);
	                    gvtRoot.paint(g2);
	                    
	                    g2.dispose();
	                    cb.addTemplate(tp, 0, 0);
	                    
	                    //closing the document
	                    pdfDoc.close();
						
						//cleaning up
						out.flush();
						out.close();
						monitor.stop();
		            }

					ctx.dispose();
					
				}catch (IOException | DocumentException ex) {
					
					handleExportFailure();
					
					if(ctx!=null){
						
						ctx.dispose();
					}
				}
			}
		};
		
		thread.start();
	}
}
