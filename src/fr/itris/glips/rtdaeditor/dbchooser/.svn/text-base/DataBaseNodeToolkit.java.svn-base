package fr.itris.glips.rtdaeditor.dbchooser;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.database.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.*;
import org.w3c.dom.svg.*;

import java.awt.geom.Point2D;
import java.net.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class DataBaseNodeToolkit {

	/**
	 * the back value
	 */
	public static final String quitValue="/**quit**/";
	
	/**
	 * the back value
	 */
	public static final String backValue="/**returnToPrevious**/";
	
	/**
	 * the close popup dialog value
	 */
	public static final String closePopupDialogValue="/**closePopupDialog**/";
	
	/**
	 * the node name of the view in the data base
	 */
	protected static String viewNodeName="view", idAttribute="id";
	
	/**
	 * creates and returns the xml path of the given element
	 * @param root the root for the element
	 * @param element an element
	 * @return the xml path of this given element
	 */
	public static String getPath(Element root, Element element){
		
		String path="", id="";
		
		if(element!=null){
			
			Node node=element;
			
			path=EditorAnimationsToolkit.normalizePath(((Element)node).getAttribute("id"));
			node=node.getParentNode();
			
			//builds the path of the given element by adding the id attribute to each parent
			while(node!=null && node instanceof Element && ! node.equals(root)){
				
				id=EditorAnimationsToolkit.normalizePath(((Element)node).getAttribute("id"));
				
				if(id!=null && ! id.equals("")){
					
					path=id+"/"+path;
					
				}else{
					
					break;
				}
				
				node=node.getParentNode();
			}
			
			if(path.indexOf("/")!=-1){
				
				path="/"+path;
			}
		}
		
		return path;
	}
	
	/**
	 * creates and returns the root database element corresponding to the given svg document
	 * @param doc a document
	 * @return the root database element corresponding to the given svg document
	 */
	public static Element getRootDataBaseElement(Document doc) {
		
		Element rootDataBaseElement=null;
		
		if(doc!=null) {
			
			if(Toolkit.isDocumentAView(doc)) {
				
				//getting the canvas path related to the given node
				String canvasPath="";
				
				try{
					canvasPath=new URI(((SVGDocument)doc).getURL()).toASCIIString();
				}catch (Exception ex){ex.printStackTrace();}
				
				//getting the reference node and the reference file of the document
				String referenceNode=doc.getDocumentElement().getAttribute("referenceNode");
				String referenceFile=doc.getDocumentElement().getAttribute("referenceFile");
				
				//getting the root node of the view data base
				rootDataBaseElement=DataBaseToolkit.getRootDataBaseElement(
						canvasPath, referenceFile, referenceNode);
				
			}else {
				
				//getting the root node for thewidget database
				rootDataBaseElement=DataBaseToolkit.getRtdaWidgetDataBase(doc);
			}
		}
		
		return rootDataBaseElement;
	}
	
	/**
	 * Returns the list of the available values for the tag
	 * @param doc a svg document
	 * @param tagName a tag
	 * @return the list of the available values for the tag
	 */
	public static LinkedList<String> getEnumeratedTagValues(Document doc, String tagName){
		
		LinkedList<String> values=new LinkedList<String>();
		
		if(doc!=null && tagName!=null) {
			
			tagName=EditorAnimationsToolkit.normalizePath(tagName);
			
			//getting the root node of the view data base
			Element dataBaseRoot=getRootDataBaseElement(doc);
			
			if(dataBaseRoot!=null){
				
				//getting the node corresponding to the given tag name
				Element element=getElementForTag(dataBaseRoot, tagName);
				
				if(element!=null && TagToolkit.getNodeType(
						element.getNodeName())==TagToolkit.ENUMERATED){
					
					//getting the list of the values of the enumerated tag
					values.addAll(getEnumeratedTagValues(element));
				}
			}
		}
		
		return values;
	}
	
	/**
	 * returns a point containing the min and max values 
	 * of the tag denoted by the provided name
	 * @param doc a document
	 * @param tagName a tag name
	 * @return a point containing the min and max values 
	 * of the tag denoted by the provided name
	 */
	public static Point2D getMinAndMax(Document doc, String tagName){
		
		Point2D point=null;
		
		if(doc!=null && tagName!=null) {
			
			tagName=EditorAnimationsToolkit.normalizePath(tagName);
			
			//getting the root node of the view data base
			Element dataBaseRoot=getRootDataBaseElement(doc);
			
			if(dataBaseRoot!=null){
				
				//getting the node corresponding to the given tag name
				Element element=getElementForTag(dataBaseRoot, tagName);
				
				if(element!=null){
					
					int tagType=TagToolkit.getNodeType(
							element.getNodeName());
					
					if(tagType==TagToolkit.ANALOGIC_FLOAT || 
							tagType==TagToolkit.ANALOGIC_INTEGER){
						
						String minTagVal=element.getAttribute("min");
						String maxTagVal=element.getAttribute("max");

						double min=Double.NaN;
						double max=Double.NaN;
						
						try{
							min=Double.parseDouble(minTagVal);
						}catch (Exception ex){}
						
						try{
							max=Double.parseDouble(maxTagVal);
						}catch (Exception ex){}
						
						point=new Point2D.Double(min, max);
					}
				}
			}
		}
		
		return point;
	}
	
	/**
	 * retrieves and returns the list of the child nodes "name" attribute of the given element
	 * @param element an element
	 * @return the list of the child values of the given element
	 */
	protected static LinkedList<String> getEnumeratedTagValues(Element element){
		
		LinkedList<String> values=new LinkedList<String>();
		
		if(element!=null && element.hasChildNodes()) {
			
			Node child=null;
			String id="";
			
			//for each child, retrieves its id, and adds it in the list
			for(child=element.getFirstChild(); child!=null; child=child.getNextSibling()){
				
				if(child instanceof Element){
					
					id=((Element)child).getAttribute("id");
					
					if(id!=null && ! id.equals("")){
						
						values.add(id);
					}
				}
			}
		}
		
		return values;
	}
	
	/**
	 * retrieves the node corresponding to the given tag name in the given data base
	 * @param rootDataBase the data base root node
	 * @param tagName a tag name
	 * @return the node corresponding to the given tag name in the given data base
	 */
	protected static Element getElementForTag(Element rootDataBase, String tagName) {
		
		Element tagElement=null;
		
		if(rootDataBase!=null && tagName!=null && ! tagName.equals("")) {
			
			//splitting the tag name into an array of path segments
			String[] splitTagName=Toolkit.getSplitPath(tagName);
			
			if(splitTagName!=null && splitTagName.length>0) {
				
				int i, j;
				Element element=rootDataBase;
				NodeList children=rootDataBase.getChildNodes();
				
				//handling the root node case
				tagElement=element;
				
				//checking the child nodes under the root node
				start :
					
					for(i=0; i<splitTagName.length && children!=null; i++){
						
						for(j=0; j<children.getLength(); j++) {
							
							if(children.item(j)!=null && children.item(j) instanceof Element) {
								
								element=(Element)children.item(j);
								
								//checking if one of the nodes has the current id
								if(element.getAttribute("id").equals(splitTagName[i])) {
									
									//setting the new root data base element
									tagElement=element;
									break;
								}
							}
							
							if(j==children.getLength()-1) {
								
								//no node having the corresponding id exists
								tagElement=null;
								break start;
							}
						}
						
						//getting the children of the current node
						children=tagElement.getChildNodes();
					}
			}
		}
		
		return tagElement;
	}
	
	/**
	 * returns the list of the available view xml paths from the given document
	 * @param doc a svg document
	 * @return the list of the available view xml paths from the given document
	 */
	public static LinkedList<String> getAllAvailableViewPath(Document doc){
		
		LinkedList<String> viewPaths=new LinkedList<String>();
		
		if(doc!=null) {
			
			//getting the view data base
			Element rootDataBase=getRootDataBaseElement(doc);
			
			if(rootDataBase!=null) {
				
				//getting the nodes that can be found under the root data base and that are view
				NodeList viewNodes=rootDataBase.getElementsByTagName(viewNodeName);
				
				//for each view node, getting its xml path
				String id="";
				
				for(int i=0; i<viewNodes.getLength(); i++) {
					
					id=getPath(rootDataBase, (Element)viewNodes.item(i));
					
					if(! id.equals("")) {
						
						viewPaths.add(id);
					}
				}
			}
		}
		
		return viewPaths;
	}
	
	/**
	 * returns the location of the view denoted by the given xml view path 
	 * @param doc a svg document
	 * @param viewPath a xml view path
	 * @return the location of the view denoted by the given xml view path 
	 */
	public static String getViewLocation(Document doc, String viewPath) {
		
		String location="";
		
		if(doc!=null && viewPath!=null && ! viewPath.equals("")) {
			
			//getting the view data base
			Element rootDataBase=getRootDataBaseElement(doc);
			
			if(rootDataBase!=null) {
				
				//getting the element corresponding to the given view path
				Element element=getElementForTag(rootDataBase, viewPath);
				
				if(element!=null) {
					
					//retrieving the value of the location attribute
					location=element.getAttribute("location");
				}
			}
		}
		
		return location;
	}
}
