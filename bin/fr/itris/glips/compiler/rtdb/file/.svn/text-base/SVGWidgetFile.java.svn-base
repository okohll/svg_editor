/*
 * Created on 29 juin 2005
 */
package fr.itris.glips.compiler.rtdb.file;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.compiler.rtdb.*;
import fr.itris.glips.library.*;

/**
 * the class of the svg widget files 
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGWidgetFile extends SVGFile{
	
	/**
	 * the root element of the widget database
	 */
	protected Element widgetDataBaseRoot=null;
	
	/**
	 * the map associating a widget tag name to the object containing information on the widget database
	 */
	protected HashMap<String, TagObject> widgetDataBase=new HashMap<String, TagObject>();
	
	/**
	 * the constructor of the class
	 * @param fileManager the file manager
	 * @param workspacePath the path of the workspace of the project that is handled
	 * @param projectName the name of the project that is handled
	 * @param name the name of the svg file
	 * @param xmlPath the xml path of this svg file
	 * @param widgetDataBaseRoot the root element of the widget database
	 */
	public SVGWidgetFile(FileManager fileManager, String workspacePath, 
			String projectName, String name, String xmlPath, Element widgetDataBaseRoot){
		
		super(fileManager, workspacePath, projectName, name, xmlPath);
		
		//creating the objects associating a tag name to the objects containing information on each node
		Node cur=null;
		TagObject tagObject=null;
		String widgetPath="";
		
		if(widgetDataBaseRoot!=null){
			
			for(NodeIterator it=new NodeIterator(widgetDataBaseRoot); it.hasNext();){
				
				cur=it.next();
				
				if(cur!=null && cur instanceof Element && 
						cur.getNodeName().startsWith("rtda:tag.")){
					
					tagObject=new TagObject((Element)cur);
					widgetPath=tagObject.getWidgetTagName();
					
					if(widgetPath!=null && ! widgetPath.equals("")){
						
						widgetDataBase.put(widgetPath, tagObject);
					}
				}
			}
			
			//translates the tags in this svg widget file
			translateTags();
		}
	}
	
	/**
	 * translates the tags in this svg widget file
	 */
	protected void translateTags(){
		
		Node cur=null, att=null, child=null, childAttribute=null;
		Element el=null;
		String 	nodeName="", attributeName="", attributeValue="", viewValue="", associatedChildAttributeName="",
		referencedTagName="", tagAttributeNodeName="", tagAttributeName="", referencedTagAttribute="", 
		newValue="", childNodeName="", childAttributeName="";
		NamedNodeMap attributes=null, childAttributes=null;
		TagObject tagObject=null;
		Map<String, String> values=null;
		String[] splitValues=null;
		int i, j, pos, type;
		RtdaTagHandler tagHandler=null;
		
		//the set of the animation nodes that will be found during the first iteration
		HashSet<Element> animationNodes=new HashSet<Element>();
		
		//checking the attributes of the widget that uses 
		//the values of a referenced enumerated tag 
		for(NodeIterator it=new NodeIterator(
				doc.getDocumentElement()); it.hasNext();){
			
			cur=it.next();
			
			if(cur!=null && cur instanceof Element && 
					cur.getNodeName().startsWith(rtdaPrefix)){
				
				el=(Element)cur;
				
				animationNodes.add(el);
				
				//getting the tag handler for this node
				tagHandler=null;
				
				//getting the type of this node
				type=getType(el);
				
				if(type==JWIDGET_ANIMATION) {
					
					//getting the id of the jwidget corresponding to this element
					String jwidgetId=getJWidgetName(el);
					
					if(jwidgetId!=null && ! jwidgetId.equals("")) {
						
						//getting the tag handler
						tagHandler=fileManager.getTagHandlersManager().getJWidgetTagHandler(jwidgetId);
					}
					
				}else {
					
					tagHandler=fileManager.getTagHandlersManager().getTagHandler(el);
				}
				
				if(tagHandler!=null) {
					
					//getting the name and the attributes of the node
					nodeName=el.getNodeName();
					attributes=el.getAttributes();
					
					for(i=0; i<attributes.getLength(); i++){
						
						att=attributes.item(i);
						attributeName=att.getNodeName();
						attributeValue=att.getNodeValue();
						
						if(tagHandler.isEnumeratedTagValueReferenceAttribute(nodeName, attributeName)){
							
							//getting the tag name corresponding to the referenced tag name of this attribute
							referencedTagAttribute=tagHandler.getEnumeratedTagValueReferenceAttribute(nodeName, attributeName);
							
							if(referencedTagAttribute!=null && 
									referencedTagAttribute.indexOf(RtdaTagHandler.separator)!=-1){
								
								//getting the name of the element containing the tag attribute
								pos=referencedTagAttribute.indexOf(RtdaTagHandler.separator);
								tagAttributeNodeName=referencedTagAttribute.substring(0, pos);
								
								//getting the name of the attribute containing the tag itself
								tagAttributeName=referencedTagAttribute.substring(pos+
										RtdaTagHandler.separator.length(), referencedTagAttribute.length());
								
								//getting the value of the tag attribute
								if(nodeName.equals(tagAttributeNodeName)){
									
									//the tag attribute is an attribute of the current node
									referencedTagName=((Element)cur).getAttribute(tagAttributeName);
									
								}else{
									
									//the tag attribute is an attribute of the parent node of the current node
									try{
										referencedTagName=((Element)el.getParentNode()).getAttribute(tagAttributeName);
									}catch (Exception ex){referencedTagName="";ex.printStackTrace();}
								}
								
								//getting the object containing information on the found tag name
								if(referencedTagName!=null && ! referencedTagName.equals("")){
									
									tagObject=widgetDataBase.get(referencedTagName);
									
									if(tagObject!=null){
										
										//getting the map of the values of the enumerated tag
										values=tagObject.getWidgetToViewValues();
										
										//modifying the value of the attribute
										if(attributeValue.indexOf("|")==-1){
											
											//the attribute only contains one of the values of the enumerated tag
											viewValue=values.get(attributeValue);
											
											if(viewValue!=null){
												
												//setting the new value for the attribute
												att.setNodeValue(viewValue);
											}
											
										}else{
											
											//the attribute contains a list of the values of the enumerated tag
											splitValues=attributeValue.split("|");
											newValue="";
											
											if(splitValues!=null){
												
												for(j=0; j<splitValues.length; j++){
													
													viewValue=values.get(splitValues[j]);
													
													if(viewValue!=null){
														
														newValue+=viewValue+"|";
													}
												}
												
												//setting the new value
												att.setNodeValue(newValue);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		//for each animation nodes
		for(Node node : animationNodes){
			
			//getting the tag handler for this node
			tagHandler=null;
			
			//getting the type of this node
			type=getType((Element)node);
			
			if(type==JWIDGET_ANIMATION) {
				
				//getting the id of the jwidget corresponding to this element
				String jwidgetId=getJWidgetName(el);
				
				if(jwidgetId!=null && ! jwidgetId.equals("")) {
					
					//getting the tag handler
					tagHandler=fileManager.getTagHandlersManager().getJWidgetTagHandler(jwidgetId);
				}
				
			}else {
				
				tagHandler=fileManager.getTagHandlersManager().getTagHandler((Element)node);
			}
			
			if(tagHandler!=null) {
				
				//getting the name and the attributes of the node
				nodeName=node.getNodeName();
				attributes=node.getAttributes();
				
				for(i=0; i<attributes.getLength(); i++){
					
					att=attributes.item(i);
					attributeName=att.getNodeName();
					attributeValue=att.getNodeValue();

					if(tagHandler.isTagAttribute(nodeName, attributeName)){
						
						tagObject=widgetDataBase.get(attributeValue);
						
						if(tagObject!=null){
							
							//modifying the tag name
							viewValue=tagObject.getViewTagName();

							
							if(viewValue!=null){

								att.setNodeValue(viewValue);
							}
							
							//if the tag is an enumerated tag, the values of this tag, that can be found in the child nodes
							//of the animation node, are replaces by the view tag values
							if(tagObject.getWidgetToViewValues().size()>0){
								
								//getting the name of the attribute of the child nodes that contains one of the values of the enumerated tag
								associatedChildAttributeName=tagHandler.getEnumeratedTagValueAttributeName(
										node.getNodeName(), attributeName);
								
								if(associatedChildAttributeName!=null && ! associatedChildAttributeName.equals("")){
									
									//getting the node name of the child nodes of the animation node and the name of the attribute
									//containing a value of the current tag
									pos=associatedChildAttributeName.indexOf(RtdaTagHandler.separator);
									childNodeName=associatedChildAttributeName.substring(0, pos);
									childAttributeName=associatedChildAttributeName.substring(
											pos+RtdaTagHandler.separator.length(), associatedChildAttributeName.length());
									
									//for each child of the current node
									for(child=node.getFirstChild(); child!=null; child=child.getNextSibling()){
										
										if(child instanceof Element && child.getNodeName().equals(childNodeName)){
											
											//checking all the attributes of the child node, to get the attribute 
											//that use one of the values of the previously computed tag
											childAttributes=child.getAttributes();
											
											for(j=0; j<childAttributes.getLength(); j++){
												
												childAttribute=childAttributes.item(j);
												
												if(childAttribute!=null && childAttribute.getNodeName().equals(childAttributeName)){
													
													//the widget tag value
													attributeValue=childAttribute.getNodeValue();
													
													//getting the associated view tag value
													viewValue=tagObject.getWidgetToViewValues().get(attributeValue);
													
													if(viewValue!=null){
														
														//setting the view tag value for the attribute
														childAttribute.setNodeValue(viewValue);
													}
													
													break;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * creates and returns a clone of the root element of the current file
	 * @param parentSVGFile the file into which the cloned element will be inserted
	 * @return a clone of the root element of the current file
	 */
	@Override
	protected Element importElement(SVGFile parentSVGFile){
		
		Element returnElement=super.importElement(parentSVGFile);
		
		if(returnElement!=null){
			
			//getting the widget database nodes that can be found in the element
			NodeList nodes=returnElement.getElementsByTagName(
					RtdaTagHandler.widgetDataBaseRootNodeName);
			LinkedList<Element> nodesList=Toolkit.getLinkedList(nodes);
			
			//removing each node that defines a widget data base
			for(Element element : nodesList){

				if(element.getParentNode()!=null){
					
					//removing the node from its parent node
					element.getParentNode().removeChild(element);
				}
			}
		}
		
		return returnElement;  
	}
	
	/**
	 * the class containing information on the widget database, and enabling to make the correspondance
	 * between the widget tag names and the view tag names
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	protected class TagObject{
		
		/**
		 * the name of the tag in the widget
		 */
		private String widgetTagName="";
		
		/**
		 * the name of the tag in the view
		 */
		private String viewTagName="";
		
		/**
		 * the map associating the value of a widget to the value of a view
		 */
		private LinkedHashMap<String, String> widgetValuesToViewValues=
			new LinkedHashMap<String, String>();
		
		/**
		 * the constructor of the class
		 * @param tagElement the element containing 
		 */
		protected TagObject(Element tagElement){
			
			//getting the tag names
			String[] paths=getAbsoluteTagPaths(tagElement);
			
			if(paths!=null){
				
				widgetTagName=paths[0];
				viewTagName=paths[1];
			}
			
			if(tagElement.getNodeName().indexOf("tag.enumerated")!=-1){
				
				//getting the view and widget values, as the tag is an enumerated one
				String widgetValue="", viewValue="";
				Element el=null;
				
				for(Node cur=tagElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
					
					if(cur instanceof Element){
						
						el=(Element)cur;
						
						widgetValue=el.getAttribute("id");
						viewValue=el.getAttribute("value");
						
						if(widgetValue!=null && ! widgetValue.equals("")){
							
							widgetValuesToViewValues.put(widgetValue, viewValue);
						}
					}
				}
			}
		}
		
		/**
		 * computes and returns the absolute tag names for the widget and the view
		 * @param tagElement a node of the widget database corresponding to a tag
		 * @return the absolute tag names for the widget and the view
		 */
		protected String[] getAbsoluteTagPaths(Element tagElement){
			
			String[] paths=new String[2];
			
			if(tagElement!=null){
				
				String 	widgetTagPath=RtdaTagHandler.normalizePath(tagElement.getAttribute("id")), 
				viewTagPath=RtdaTagHandler.normalizePath(tagElement.getAttribute("value"));
				Node parent=tagElement.getParentNode();
				
				while(	parent!=null && parent instanceof Element && 
						! parent.getNodeName().equals(RtdaTagHandler.widgetDataBaseRootNodeName)){
					
					widgetTagPath=RtdaTagHandler.normalizePath(((Element)parent).getAttribute("id"))+"/"+widgetTagPath;
					viewTagPath=RtdaTagHandler.normalizePath(((Element)parent).getAttribute("value"))+"/"+viewTagPath;
					
					parent=parent.getParentNode();
				}
				
				paths[0]=widgetTagPath;
				paths[1]=viewTagPath;
			}
			
			return paths;
		}
		
		/**
		 * @return the map associating the value for a widget to the value for a view
		 */
		protected Map<String, String> getWidgetToViewValues(){
			
			return widgetValuesToViewValues;
		}
		
		/**
		 * @return Returns the viewTagName.
		 */
		public String getViewTagName() {
			return viewTagName;
		}
		
		/**
		 * @return Returns the widgetTagName.
		 */
		public String getWidgetTagName() {
			return widgetTagName;
		}
	}
	
}
