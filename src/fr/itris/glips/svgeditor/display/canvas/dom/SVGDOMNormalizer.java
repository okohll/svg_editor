package fr.itris.glips.svgeditor.display.canvas.dom;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import org.apache.batik.ext.awt.geom.*;
import org.w3c.dom.*;
import org.w3c.dom.svg.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.library.geom.*;
import fr.itris.glips.library.geom.Polygon2D;
import fr.itris.glips.library.geom.Polyline2D;
import fr.itris.glips.library.geom.path.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to normalize the svg dom of a canvas
 * @author Jordi SUC
 */
public class SVGDOMNormalizer {

	/**
	 * the list of the resource names that should be handled
	 */
	private static final LinkedList<String> resourceNames=new LinkedList<String>();

	static{
		
		resourceNames.add("linearGradient");
		resourceNames.add("radialGradient");
		resourceNames.add("pattern");
		resourceNames.add("marker");
	}

	/**
	 * string constants
	 */
	private static final String styleAttributeName="style", gTagName="g",
		textNodeName="#text", textTagName="text", tspanTagName="tspan",
			/*viewBoxAtt="viewBox", */defsTagName="defs", styleAtt="style", 
				stopOpacityAtt="stop-opacity", stopColorAtt="stop-color",
					lineTagName="line", polygonTagName="polygon", 
						polylineTagName="polyline", x1Att="x1", x2Att="x2", y1Att="y1", 
							y2Att="y2", pointsAtt="points", pathTagName="path", dAtt="d",
								transformAtt="transform", circleTagName="circle", ellipseTagName="ellipse",
									rxAtt="rx", ryAtt="ry", rAtt="r";
	
	/**
	 * a svg handle
	 */
	private SVGHandle handle;
	
	/**
	 * the constructor of the class
	 * @param handle a svg handle
	 */
	public SVGDOMNormalizer(SVGHandle handle){
		
		this.handle=handle;
	}
    
	/**
	 * normalizes the provided document
	 * @param doc the svg document
	 * @param scaledSize the scaled size of the canvas
	 */
	public void normalize(Document doc, Dimension scaledSize){
		
		if(doc!=null){
			
			Element root=doc.getDocumentElement();

			if(root!=null){
				
				/*if(root.getAttribute(viewBoxAtt).equals("")){
					
					root.setAttributeNS(null, viewBoxAtt, "0 0 "+
						(int)scaledSize.getWidth()+" "+(int)scaledSize.getHeight());
				}*/
				
				//computing the defs element, by merging all the existing defs elements
				computeDefs(doc);
				
				//getting the map associating the id of a resource to the resource node
				Map<String, Element> resources=
					handle.getSvgResourcesManager().getResourcesFromDefs(
						doc, resourceNames);

				//applying modifications to the dom
				Node node=null;
				Element el=null;
				String nsp=EditorToolkit.svgNS;
				String nodeValue="";
				Set<Node> nodesToRemove=new HashSet<Node>();
				Set<Element> elementsToConvert=new HashSet<Element>();
				
				for(NodeIterator nit=new NodeIterator(root); nit.hasNext();){
					
					node=nit.next();
					
					if(node!=null && nsp.equals(node.getNamespaceURI()) && 
							node instanceof Element){
						
						el=(Element)node;
						
						//normalizing the node
						normalizeElement(el);
						
						//checking if the element should be converted
						if(shouldBeConverted(el)){
							
							elementsToConvert.add(el);
						}
						
						//checking the color values consistency
						Editor.getColorChooser().checkColorString(handle, el);
						
						//registering the node if it uses a resource
						handleResources(el, resources);
						
						if(el.getNodeName().equals(gTagName)){
							
							//normalizing the node
							normalizeGroupNode(el);
						}
						
					}else if(node!=null && node instanceof Text){
						
						//handling the text nodes
						nodeValue=node.getNodeValue();
						nodeValue=nodeValue.trim();

						if(nodeValue==null || nodeValue.equals("")){
							
							nodesToRemove.add(node);
							
						}else{
							
							node.setNodeValue(nodeValue);
						}
					}
				}
				
				//converting the elements
				for(Element element : elementsToConvert){
					
					convertElements(element);
				}
				
				//removing the unused text nodes
				for(Node textNode : nodesToRemove){
					
					textNode.getParentNode().removeChild(textNode);
				}
			}
		}
	}
    
    /**
     * registers the provided element if it uses a resource
     * @param resources the map associating the id of 
     * a resource to the resource node
     * @param element an element
     */
    protected void handleResources(
    		Element element, Map<String, Element> resources){

		//getting the style attribute
		String style=element.getAttribute(styleAttributeName);

		if(style!=null && ! style.equals("")){
			
			//for each resource id, checks if it is contained in the style attribute
			LinkedList<Element> nodesList=null;
			
			for(String id : resources.keySet()){
				
				nodesList=handle.getSvgResourcesManager().getUsedResources().get(id);
				
				//adds the node in the used resource map
				if(id!=null && ! id.equals("") && style.indexOf("#"+id)!=-1){
					
					if(nodesList==null){
						
						nodesList=new LinkedList<Element>();
						handle.getSvgResourcesManager().
							getUsedResources().put(id, nodesList);
					}
					
					nodesList.add(element);
				}
			}
		}
    }
	
	/**
	 * computes the "defs" node in a svg document
	 * @param doc a document
	 */
    protected void computeDefs(Document doc){
		
		Element defs=null;
		Element root=doc.getDocumentElement();
		NodeList defsNodes=doc.getElementsByTagName(defsTagName);
		LinkedList<Element> defsList=Toolkit.getLinkedList(defsNodes);
		
		if(defsList.size()>0){
			
			defs=defsList.getFirst();
			
			//getting the list of the additionnal defs
			if(defsList.size()>1) {
				
				ArrayList<Element> additionalDefs=new ArrayList<Element>();

				//inserting each resource that could be found in other 
				//"defs" nodes to the first "defs" node
				for(Element aDefs : defsList) {

					additionalDefs.add(aDefs);
					
					while (aDefs.hasChildNodes()){
						
						defs.appendChild(aDefs.removeChild(aDefs.getFirstChild()));
					}
				}
				
				//for each extra "defs" found nodes
				for(Element aDefs : additionalDefs) {
					
					aDefs.getParentNode().removeChild(aDefs);
				}
			}
		}
		
		if(defs==null){
			
			//adding a "defs" node to the svg document
			defs=doc.createElementNS(null, defsTagName);
               
			if(root.getFirstChild()!=null){
				
				root.insertBefore(defs, root.getFirstChild());
				
			}else{
				
				root.appendChild(defs);
			}
		}
	}
    
    /**
     * normalizes the provided element
     * @param element an element
     */
    protected void normalizeElement(Element element){
        
        removeTspans(element);
        translateFromAttributesToStyle(element);
    }
    
    /**
     * removes the tspans inside a text node
     * @param node the node on which the changes will be made
     */
    protected void removeTspans(Node node){
        
        if(node!=null && node.getNodeName().equals(textTagName)){
            
            String value=getText(node);
            
            //removing all the text children from the node
            NodeList children=node.getChildNodes();
            LinkedList<Node> childrenList=Toolkit.getNodeLinkedList(children);
            
            for(Node child : childrenList){

                if(child instanceof Text || 
                		child.getNodeName().equals(tspanTagName)){
                    
                    node.removeChild(child);
                }
            }
            
            //adds a #text node
            Document doc=node.getOwnerDocument();
            
            if(doc!=null){
                
                Text txt=doc.createTextNode(value);
                node.appendChild(txt);
            }
        }
    }
    
    /**
     * removes the attributes specified in the properties.xml and 
     * adds them to the style attribute for this node
     * @param node a node
     */
    protected void translateFromAttributesToStyle(Node node){
        
        if(node!=null && node instanceof Element && 
        	!(node instanceof SVGFontFaceElement)){
            
        	Element element=(Element)node;
            String style=element.getAttribute(styleAtt);
            
            //the list of the attributes to be removed and added to the style attribute
            HashSet<String> styleProperties=
            	Editor.getEditor().getResourcesManager().getAttributesToTranslate();
            styleProperties.add(stopOpacityAtt);
            styleProperties.add(stopColorAtt);
            
            String name="", value="";
            NamedNodeMap attributes=node.getAttributes();
            Set<String> attToBeRemoved=new HashSet<String>();
            Node att=null;
            
            for(int i=0; i<attributes.getLength(); i++){
                
                att=attributes.item(i);
                
                if(styleProperties.contains(att.getNodeName())){
                    
                    name=att.getNodeName();
                    value=att.getNodeValue();
                    
                    if(! value.equals("") && style.indexOf(name+":")==-1){
                        
                        //if the attribute is not in the style value, it is added to the style value
                        if(style.length()>0 && ! style.endsWith(";")){
                            
                            style=style.concat(";");
                        }
                        
                        style=style+name+":"+value+";";		
                    }
                    
                    attToBeRemoved.add(new String(name));
                }
            }
            
            //removes the attributes that have been added to the style attribute            
            for(String str : attToBeRemoved){
 
                if(!str.equals("")){
                    
                	element.removeAttribute(str);
                }
            }
            
            if(style.equals("")){
                
                //removes the style attribute
            	element.removeAttribute(styleAtt);
                
            }else{
                
                //modifies the style attribute
            	element.setAttribute(styleAtt, style);
            }
        }
    }
    
    /**
     * returns whether the provided element should be converted
     * @param element an element
     * @return whether the provided element should be converted
     */
    protected boolean shouldBeConverted(Element element){
    	
    	//getting the tag name of the node
    	String tagName=element.getNodeName();
    	
    	return tagName.equals(lineTagName) || 
			tagName.equals(polygonTagName) || 
				tagName.equals(polylineTagName) || 
					tagName.equals(circleTagName);
    }
    
    /**
     * converts the provided element to another one that is supported by the application
     * and modifying the matrix transform of its children
     * @param element an element
     */
    protected void convertElements(Element element){
        
    	//getting the tag name of the node
    	String tagName=element.getNodeName();
    	
    	if(tagName.equals(lineTagName) || 
    			tagName.equals(polygonTagName) || 
    				tagName.equals(polylineTagName)){
    		
    		//getting the map of the attributes of the node
    		Map<String, String> attributesMap=new HashMap<String, String>();
            NamedNodeMap attributes=element.getAttributes();
            Node att=null;
            String name="", value="";
            
            for(int i=0; i<attributes.getLength(); i++){
                
                att=attributes.item(i);
                name=att.getNodeName();
                value=att.getNodeValue();
                
                if(! name.equals("") && ! name.equals(x1Att) && 
                		! name.equals(x2Att) && ! name.equals(y1Att) &&
                		! name.equals(y2Att) && ! name.equals(pointsAtt)){
                	
                	attributesMap.put(name, value);
                }
            }

    		//computing general path corresponding to the element
            GeneralPath path=null;
            
        	if(tagName.equals(lineTagName)){
        		
        		//getting the coordinates of the line
        		double x1=0, y1=0, x2=0, y2=0;
        		
        		try{
            		x1=Double.parseDouble(element.getAttribute(x1Att));
            		y1=Double.parseDouble(element.getAttribute(y1Att));
            		x2=Double.parseDouble(element.getAttribute(x2Att));
            		y2=Double.parseDouble(element.getAttribute(y2Att));
        		}catch(Exception ex){}
        		
        		//creating a path shape corresponding to the line
        		path=new GeneralPath();
        		path.moveTo((float)x1, (float)y1);
        		path.lineTo((float)x2, (float)y2);

        	}else if(tagName.equals(polygonTagName)){
        		
        		try{
        			PolyShape2D polygon=new Polygon2D(
        					PolyShape2D.getCoordinates(element.getAttribute(pointsAtt)));
        			path=polygon.getPath();
        		}catch (Exception ex){}
        		
        	}else if(tagName.equals(polylineTagName)){
        		
        		try{
        			PolyShape2D polyline=new Polyline2D(
        					PolyShape2D.getCoordinates(element.getAttribute(pointsAtt)));
        			path=polyline.getPath();
        		}catch (Exception ex){}
        	}
        	
    		//computing the "d" attribute for the path element
        	if(path!=null){
        		
        		Path pathManager=new Path(new ExtendedGeneralPath(path));
        		String dValue=pathManager.toString();

                //getting the document
                Document doc=element.getOwnerDocument();
                
                //creating a path element
                Element pathElement=doc.createElementNS(
                		doc.getDocumentElement().getNamespaceURI(), pathTagName);
                
                //setting the attributes of the path element
                pathElement.setAttribute(dAtt, dValue);
                
                for(String nameAtt : attributesMap.keySet()){
                	
                	value=attributesMap.get(nameAtt);
                	pathElement.setAttribute(nameAtt, value);
                }
                
                //creating the list of the child nodes
                NodeList childNodes=element.getChildNodes();
                LinkedList<Node> childNodesList=new LinkedList<Node>();
                
                for(int i=0; i<childNodes.getLength(); i++){
                	
                	childNodesList.add(childNodes.item(i));
                }
              
                //appending all the child nodes of the element to the path element
                for(Node node : childNodesList){
                	
                	pathElement.appendChild(node);
                }
                
                //inserting the path element
                element.getParentNode().insertBefore(pathElement, element);
                
                //removing the initial element
                element.getParentNode().removeChild(element);
        	}
        	
    	}else if(tagName.equals(circleTagName)){

    		//getting the map of the attributes of the node
    		Map<String, String> attributesMap=new HashMap<String, String>();
            NamedNodeMap attributes=element.getAttributes();
            Node att=null;
            String name="", value="";
            
            for(int i=0; i<attributes.getLength(); i++){
                
                att=attributes.item(i);
                name=att.getNodeName();
                value=att.getNodeValue();
                
                if(! name.equals("") && ! name.equals(rAtt)){
                	
                	attributesMap.put(name, value);
                }
            }

            //getting the document
            Document doc=element.getOwnerDocument();
            
            //creating the ellipse node
            Element ellipseElement=doc.createElementNS(
            	doc.getDocumentElement().getNamespaceURI(), ellipseTagName);
            
            //getting the r attribute value
            String rValue=element.getAttribute(rAtt);
            
            //setting the attributes of the ellipse element
            ellipseElement.setAttribute(rxAtt, rValue);
            ellipseElement.setAttribute(ryAtt, rValue);
            
            for(String nameAtt : attributesMap.keySet()){
            	
            	value=attributesMap.get(nameAtt);
            	ellipseElement.setAttribute(nameAtt, value);
            }
            
            //creating the list of the child nodes
            NodeList childNodes=element.getChildNodes();
            LinkedList<Node> childNodesList=new LinkedList<Node>();
            
            for(int i=0; i<childNodes.getLength(); i++){
            	
            	childNodesList.add(childNodes.item(i));
            }
          
            //appending all the child nodes of the element to the path element
            for(Node node : childNodesList){
            	
            	ellipseElement.appendChild(node);
            }
            
            //inserting the path element
            element.getParentNode().insertBefore(ellipseElement, element);
            
            //removing the initial element
            element.getParentNode().removeChild(element);
    	}
    }
    
    /**
     * normalizes a group element, setting the transform of this node to identity 
     * and modifying the matrix transform of its children
     * @param g a group element
     */
    public void normalizeGroupNode(Element g){
        
    	//getting the transform attribute value of the group element
    	String mainTransformValue=g.getAttribute(transformAtt).trim();
    	
    	if(! mainTransformValue.equals("")){
    		
    		mainTransformValue+=" ";
    		g.removeAttribute(transformAtt);
    		
    		//concatenating this value to the transform attribute
    		//value of each child of the element
    		String transformValue="";
    		NodeList childNodes=g.getChildNodes();
    		Node node=null;
    		Element element=null;
    		
    		for(int i=0; i<childNodes.getLength(); i++){
    			
    			node=childNodes.item(i);
    			
    			if(node instanceof Element){
    				
    				element=(Element)node;
    				
					//getting the transform attribute value
					transformValue=element.getAttribute(transformAtt).trim();
					
					//setting the new transform value
					transformValue=mainTransformValue+transformValue;
					element.setAttribute(transformAtt, transformValue);
    			}
    		}
    	}
    }
    
    /**
     * gets the text written in a text node
     * @param node the text node
     * @return a string of the text value
     */
    protected String getText(Node node){
        
        String value="";
        
        if(node!=null && (node.getNodeName().equals(textTagName) || 
        		node.getNodeName().equals(tspanTagName))){
            
            //for each child of the given node, computes the text 
        	//it contains and concatenates it to the current value
            for(Node cur=node.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
                
                if(cur.getNodeName().equals(textNodeName)){
                    
                    value+=cur.getNodeValue().trim();
                    
                }else if(cur.getNodeName().equals(tspanTagName)){
                    
                    value=value+getText(cur);
                }
            }
        }
        
        return value;
    }
}
