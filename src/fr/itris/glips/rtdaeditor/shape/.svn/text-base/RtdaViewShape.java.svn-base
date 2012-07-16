/*
 * Created on 18 aout 2004
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
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.rtda.database.*;
import fr.itris.glips.rtda.toolkit.*;
import java.net.*;
import java.util.*;

/**
 * the class used to insert rtda views in a svg document
 * @author ITRIS, Jordi SUC
 */
public class RtdaViewShape extends RtdaShape{
    
	/**
	 * the element attributes names
	 */
	protected static String preserveAtt="preserveAspectRatio";

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public RtdaViewShape(Editor editor) {
	    
		super(editor);
		
		shapeModuleId="RtdaViewShape";
		handledElementTagName="image";
		retrieveLabels();
		createMenuAndToolItems();
		
		//gets the labels from the bundle
		ResourceBundle bundle=ResourcesManager.bundle;
		wrongFileTypeLabel=bundle.getString("RtdaViewShapeInsertedSVGNotView");
	}
	
	@Override
	public boolean isElementTypeSupported(Element element) {

		return element.getNodeName().equals(handledElementTagName) && 
					element.hasAttribute(Toolkit.viewAtt);
	}

	@Override
	protected boolean isWrongFileType(Document doc) {

		return ! Toolkit.isDocumentAView(doc);
	}
	
	@Override
	protected String[] getSVGFile(SVGHandle handle) {
		
		//the array that will be returned
		String[] array=null;

		//getting the edited document
		Document doc=handle.getScrollPane().getSVGCanvas().getDocument();
		
		//creating the filter for choosing a view path
		DataBaseNodeFilter filter=new DataBaseNodeFilter(
				"", 0, TagToolkit.VIEW, true, false, handle.getName());
		
		//getting the information object about the selected view
		DataBaseNodeChooser nodeChooser=DataBaseNodeChooser.getDataBaseNodeChooser(
				Editor.getParent(), doc, filter, false, false);
		nodeChooser.showDialog(Editor.getEditor().getDesktop());
		nodeChooser.disposeDialog();
		DataBaseNodeInformation info=nodeChooser.getInfo();

		if(info!=null){
			
			//getting the path of the svg file corresponding to the view
			String viewPath=info.getXmlPath(), imagePath=info.getViewLocation();
			
			//getting the absolute path
			String absolutePath="";
			
			if(imagePath!=null && ! imagePath.equals("")){
				
				try{
					absolutePath=DataBaseToolkit.getAbsoluteLocation(
							new URI(imagePath), new URI(handle.getName()));
				}catch (Exception ex){imagePath=null;}
			}
			
			if(imagePath!=null && ! imagePath.equals("")){
				
				array=new String[3];
				array[0]=absolutePath;
				array[1]=imagePath;
				array[2]=viewPath;
			}
		}
		
		return array;
	}
	
	@Override
	protected Element createElement(
			SVGHandle handle, ViewOrWidgetData svgData, Point2D location) {

		Element element=null;
		
		//getting the image and the view paths
		String imagePath=svgData.relativize(handle.getCanvas().getURI());
		String viewPath=svgData.getViewXmlPath();
		Document doc=handle.getCanvas().getDocument();
		
		if(viewPath!=null && imagePath!=null){

		    //adds the rtda namespace attribute to the the root element if it does not contain it
	        Toolkit.checkRtdaXmlns(doc);
	        
	        //adds the xlink namespace to the document
	        EditorToolkit.checkXLinkNameSpace(doc);

			//creating the view element
			element=doc.createElementNS(
					doc.getDocumentElement().getNamespaceURI(), handledElementTagName);
			
			EditorToolkit.setAttributeValue(element, xAtt, location.getX());
			EditorToolkit.setAttributeValue(element, yAtt, location.getY());
			EditorToolkit.setAttributeValue(element, wAtt, svgData.getSize().getX());
			EditorToolkit.setAttributeValue(element, hAtt, svgData.getSize().getY());
			element.setAttributeNS(null, Toolkit.viewAtt, viewPath);
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
