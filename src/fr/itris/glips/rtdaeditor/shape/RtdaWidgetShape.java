/*
 * Created on 18 août 2004
 * 
 =============================================
                   GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
GLIPS Graffiti Editor, a SVG Editor
Copyright (C) 2003 Jordi SUC, Philippe Gil, SARL ITRIS

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact : jordi.suc@itris.fr; philippe.gil@itris.fr

 =============================================
 */
package fr.itris.glips.rtdaeditor.shape;

import java.awt.geom.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.database.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class used to insert rtda widgets in a svg document
 * @author ITRIS, Jordi SUC
 */
public class RtdaWidgetShape extends RtdaShape{
    
	/**
	 * the element attributes names
	 */
	protected static String preserveAtt="preserveAspectRatio",
											referenceNodeAtt="referenceNode", valueAtt="value";

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public RtdaWidgetShape(Editor editor){
	    
		super(editor);
		
		shapeModuleId="RtdaWidgetShape";
		handledElementTagName="image";
		retrieveLabels();
		createMenuAndToolItems();

		//gets the labels from the bundle
		ResourceBundle bundle=ResourcesManager.bundle;
		wrongFileTypeLabel=bundle.getString("RtdaWidgetShapeInsertedSVGNotWidget");
	}
	
	@Override
	public boolean isElementTypeSupported(Element element) {
		
		return element.getNodeName().equals(handledElementTagName) && 
					element.hasAttribute(Toolkit.widgetAttribute);
	}
	
	@Override
	protected boolean isWrongFileType(Document doc) {

		return Toolkit.isDocumentAView(doc);
	}
	
	@Override
	protected String[] getSVGFile(SVGHandle handle){
		
		//the array that will be returned
		String[] array=null;
		
		//getting the initial file
		File canvasFile=null, initialFile=null;
		
		try{
			canvasFile=new File(new URI(handle.getCanvas().getURI()));
			initialFile=canvasFile.getParentFile();
		}catch (Exception ex){}

		if(canvasFile!=null){
			
			//display the file chooser dialog
			final File finitialFile=initialFile;
			final File fcanvasFile=canvasFile;
			
			try{
				SwingUtilities.invokeAndWait(new Runnable(){
					
					public void run() {
						
						fileDialog.askForFile(extensions, finitialFile, new File[]{fcanvasFile});
					}
				});
			}catch (Exception ex){}
			
			//getting the selected file
			File file=fileDialog.getLastSelectedFile();
			
			if(file==null || 
				! file.getName().endsWith(EditorToolkit.SVG_FILE_EXTENSION)){
				
				file=null;
			}
			
			if(file!=null){
				
				array=new String[3];
				array[0]=file.toURI().toASCIIString();
				array[1]="";
				array[2]="";
			}
		}

		return array;
	}
	
	@Override
	public Element createElement(
			final SVGHandle handle, ViewOrWidgetData widgetData, Point2D location){
		
		//creating the widget element
		Element widgetElement=null;
		
		if(widgetData!=null){
			
			//getting the size of the svg file
			Point2D size=widgetData.getSize();
			
			if(size!=null){
				
				//getting the project name of the canvas and the relativized path of the widget
				 String projectName="";
				 String widgetPath=widgetData.getPath();

				if(widgetPath!=null && ! widgetPath.equals("")){
				    
				    //getting the project name corresponding to the widget svg file
					projectName=widgetData.getProjectName();

				    //relativizing the widget path against the current canvas path
					widgetPath=widgetData.relativize(handle.getCanvas().getURI());
				}

				//getting the edited document
				Document doc=handle.getScrollPane().getSVGCanvas().getDocument();
				
			    //adding the rtda namespace attribute to the the root element if it does not contain it
				Toolkit.checkRtdaXmlns(doc);

		        //adding the xlink namespace to the document
		        EditorToolkit.checkXLinkNameSpace(doc);

				//creating the widget element
		        widgetElement=doc.createElementNS(
		        		doc.getDocumentElement().getNamespaceURI(), handledElementTagName);
		        widgetElement.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href", widgetPath);
		        EditorToolkit.setAttributeValue(widgetElement, xAtt, location.getX());
				EditorToolkit.setAttributeValue(widgetElement, yAtt, location.getY());
				EditorToolkit.setAttributeValue(widgetElement, wAtt, size.getX());
				EditorToolkit.setAttributeValue(widgetElement, hAtt, size.getY());
		        widgetElement.setAttributeNS(null, Toolkit.widgetAttribute, projectName);
		        widgetElement.setAttributeNS(null, preserveAtt, "none meet");

				//getting the root of the widget database
				Element dbRoot=
					DataBaseToolkit.getRtdaWidgetDataBase(widgetData.getDocument());
				
				if(dbRoot!=null){
				    
				    dbRoot=(Element)doc.importNode(dbRoot, true);
				    
				    //getting the reference node for this view
				    String referenceNode=doc.getDocumentElement().getAttribute(referenceNodeAtt);
				    
				    //setting the value attribute for the widget database root node
				    dbRoot.setAttribute(valueAtt, referenceNode);
				    
				    //adding the widget database as a child of the image node
					widgetElement.appendChild(dbRoot);
				}
				
				Toolkit.clearBatikImageCache();
				handle.getCanvas().clearCache();
				
				final Element fwidgetElement=widgetElement;

				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {

						//inserting the element in the document and handling the undo/redo support
						insertShapeElement(handle, fwidgetElement);
					}
				});
			}
		}

		return widgetElement;
	}
}

