package fr.itris.glips.svgeditor.shape;

import java.awt.Frame;
import java.awt.geom.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class handling svg image elements
 * @author ITRIS, Jordi SUC
 */
public class ImageShape extends RectangularShape {

	/**
	 * the element attributes names
	 */
	protected static String hrefAtt="xlink:href", preserveAtt="preserveAspectRatio";
	
	/**
	 * the array of the file extensions supported by this module
	 */
	protected static final String[] extensions={"svg", "png", "jpg", "jpeg"};
	
	/**
	 * the dialog used to selected a file in the file system
	 */
	protected fr.itris.glips.library.widgets.FileDialog fileDialog;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public ImageShape(Editor editor) {
		
		super(editor);
		
		shapeModuleId="ImageShape";
		handledElementTagName="image";
		retrieveLabels();
		createMenuAndToolItems();
		
		//creating the file dialog
		if(Editor.getParent() instanceof Frame){
			
			fileDialog=new FileDialog((Frame)Editor.getParent());
			
		}else{
			
			fileDialog=new FileDialog((JDialog)Editor.getParent());
		}
	}
	
	@Override
	protected boolean isDrawingEnabled(SVGHandle handle) {

		boolean isDrawingEnabled=false;
		
		if(handle!=null){
			
			String canvasURI=handle.getCanvas().getURI();
			
			if(canvasURI!=null && ! canvasURI.equals("")){
				
				isDrawingEnabled=true;
			}
		}
		
		return isDrawingEnabled;
	}
	
	@Override
	public int getLevelCount() {

		return 1;
	}
	
	/**
	 * returns the image file selected by the user to be inserted in the canvas
	 * @param handle a svg handle
	 * @return an image file path relative to the canvas path denoted by the provided handle
	 */
	public String getImageFile(SVGHandle handle){
		
		//the found file path
		String foundFilePath="";
		
		//the file that will be returned
		File foundFile=null;
		
		//getting the initial file
		File canvasFile=null, initialFile=null;
		
		try{
			canvasFile=new File(new URI(handle.getCanvas().getURI()));
			initialFile=canvasFile.getParentFile();
		}catch (Exception ex){}

		if(canvasFile!=null){
			
			//displaying the file chooser dialog
			fileDialog.askForFile(extensions, initialFile, new File[]{canvasFile});
			
			//getting the selected file
			foundFile=fileDialog.getLastSelectedFile();
			
			if(foundFile!=null){
				
				//relativizing the path of the found file to the canvas path
				String canvasPath=canvasFile.getParentFile().toURI().toASCIIString();
				foundFilePath=Toolkit.getRelativePath(foundFile.toURI().toASCIIString(), canvasPath);
			}
		}
		
		return foundFilePath;
	}

	@Override
	public Element createElement(SVGHandle handle, Rectangle2D bounds){
		
		//the element
		Element element=null;
		
		//getting the image file path for the element
		String imagePath=getImageFile(handle);

		if(imagePath!=null && ! imagePath.equals("")){
			
			//the edited document
			Document doc=handle.getScrollPane().getSVGCanvas().getDocument();
			
			//normalizing the bounds of the element
			if(bounds.getWidth()<1){
			    
			    bounds.setRect(bounds.getX(), bounds.getY(), 1, bounds.getHeight());
			}
			
			if(bounds.getHeight()<1){
			    
				bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(), 1);
			}
			
			//creating the rectangle
			element=doc.createElementNS(
					doc.getDocumentElement().getNamespaceURI(), handledElementTagName);
			
			EditorToolkit.setAttributeValue(element, xAtt, bounds.getX());
			EditorToolkit.setAttributeValue(element, yAtt, bounds.getY());
			EditorToolkit.setAttributeValue(element, wAtt, bounds.getWidth());
			EditorToolkit.setAttributeValue(element, hAtt, bounds.getHeight());
			
			element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href", imagePath);
			element.setAttributeNS(null, preserveAtt, "none meet");
			
			Toolkit.clearBatikImageCache();
			handle.getCanvas().clearCache();
			
			//inserting the element in the document and handling the undo/redo support
			insertShapeElement(handle, element);
		}

		return element;
	}
}
