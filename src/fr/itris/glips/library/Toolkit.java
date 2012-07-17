package fr.itris.glips.library;

import java.io.*;
import java.util.*;
import org.apache.batik.ext.awt.image.spi.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;

/**
 * the toolkit for this library
 * @author Jordi SUC
 */
public class Toolkit {
	
    /**
     * the namespace of the rtda animations
     */
    public static final String rtdaNameSpace="http://www.itris.fr/2003/animation";
    
    /**
     * the prefix for the rtda animations
     */
    public static final String rtdaPrefix="rtda:";
    
    /**
     * the svg node name
     */
    public static final String svgNodeName="svg";
    
	/**
	 * the jwidget element name
	 */
	public static final String jwidgetElementName=fr.itris.glips.library.Toolkit.rtdaPrefix+"jwidget";
	
	/**
	 * the name attribute
	 */
	public static final String nameAttribute="jwidget-name";
	
	/**
	 * the label attribute
	 */
	public static final String labelAttribute="jwidget-label";
	
	/**
	 * the jwidget child element name
	 */
	public static final String jwidgetChildElementName=fr.itris.glips.library.Toolkit.rtdaPrefix+"property";
	
	/**
	 * the value attribute
	 */
	public static final String valueAttribute="value";
	
	/**
	 * the source attribute name
	 */
	public static final String sourceAttributeName="jwidget-source";
	
	/**
	 * the id attribute
	 */
	public static final String idAttribute="jwidget-id";
	
	/**
	 * the animation tag name
	 */
	public static final String animationTagName="animation";
	
	/**
	 * the action tag name
	 */
	public static final String actionTagName="action";
    
    /**
     * the node name of an image
     */
    public static final String imageNodeName="image";
    
    /**
     * the name of the attribute that makes an image node a view node
     */
    public static final String viewAtt="view";
    
    /**
     * the name of the attribute contained in an image node, 
     * meaning that the image represents an inserted widget
     */
    public static final String widgetAttribute="widgetProjectName";
    
	/**
	 * attribute name for history managers
	 */
    public static final String historyManagerAttName="equipment.historymanager";
    
    /**
     * attribute name for history tables
     */
    public static final String historyTableAttName="historymanager.table";
    
    /**
     * attribute name for history manager driver
     */
    public static final String driverNameAttName="driverName";
    
    /**
     * attribute name for history manager sub protocol
     */
    public static final String subProtocolAttName="subProtocol";
    
    /**
     * attribute name for history manager host
     */
    public static final String hostAttName="host";
    
    /**
     * attribute name for history manager name
     */
    public static final String dbNameAttName="DBname";
    
    /**
     * attribute name for history manager uiser
     */
    public static final String userAttName="user";
    
    /**
     * attribute name for history manager password
     */
    public static final String passwordAttName="password";
    
    /**
     * attribute name for history manager id
     */
    public static final String idAttName="id";
    
    /**
     * the style attribute name
     */
    public static final String styleAttName="style";
	
	/**
	 * the array of the property names
	 */
	public static final String[] properties= {"fill", "stroke", "opacity", "visibility", "stroke-opacity", 
			"stroke-width", "stroke-dasharray", "stroke-dashoffset", "stroke-linecap", 
			"stroke-linejoin", "stroke-miterlimit", "fill-opacity", "marker-start", "marker-mid",
			"marker-end", "font-family", "font-size", "font-weight", "font-style", "font-stretch", 
			"letter-spacing", "word-spacing", "text-decoration", "writing-mode", 
			"glyph-orientation-horizontal", "text-anchor"};
	
    /**
     * the start delimiter of a tag in a SQL request
     */
	public static final String startTagInRequest="'@";
    
    /**
     * the end delimiter of a tag in a SQL request
     */
    public static final String endTagInRequest="'";
	
	/**
	 * the request separator
	 */
	public final static String separatorBrRegex="<br>";
    
    /**
     * tells whether the current svg document is a view or a widget
     * @param doc a svg document
     * @return true if the current document is a view
     */
    public static boolean isDocumentAView(Document doc){
        
        boolean isAView=false;
        
        if(doc!=null){
            
            Element root=doc.getDocumentElement();
            
            if(root!=null){
                
                String 	att1=root.getAttribute("referenceFile"),
                			att2=root.getAttribute("referenceNode");
                
                if(att1!=null && ! att1.equals("") && att2!=null && !att2.equals("")){
                    
                    isAView=true;
                }
            }
        }
        
        return isAView;
    }
    
    /**
     * returns the view path corresponding to the given canvas document
     * @param doc the document of a view
     * @return the view path corresponding to the given canvas document
     */
    public static String getViewPath(Document doc) {
    	
    	String viewPath="";
    	
    	if(doc!=null) {
    		
    		//getting the view path attribute
            viewPath=doc.getDocumentElement().getAttribute("viewPath");
    	}
  
    	return viewPath;
    }

	/**
     * relativizes the given path to the given ref svg path
     * @param path a path
     * @param refPath the reference path
     * @return the given path to the given ref svg path
     */
    public static String getRelativePath(String path, String refPath){

        String resultPath="";
        
        if(path!=null && refPath!=null){
            
            //splitting the path into arrays of strings
            String[] refPathSplit=getSplitPath(refPath);
            String[] imagePathSplit=getSplitPath(path);
            int i, max=0;
            
            if(refPathSplit!=null && imagePathSplit!=null){

                //for each part of the reference path, tests if this part is the same as the corresponding part 
                //of the image path
                for(i=0; i<refPathSplit.length && i<imagePathSplit.length; i++){

                    if(refPathSplit[i]!=null && imagePathSplit[i]!=null && 
                    		! refPathSplit[i].equals(imagePathSplit[i])){
                        
                        max=i;
                        break;
                        
                    }else if(i==refPathSplit.length-1){
                        
                        max=refPathSplit.length;
                        
                    }else if(i==imagePathSplit.length-1){
                        
                        max=imagePathSplit.length;  
                    }
                }
                
                if(i==0){
                	
                	resultPath=path;
                	
                }else{
                	
                    if(max<refPathSplit.length){
                        
                        //buidling the relative path
                        for(i=max; i<refPathSplit.length; i++){
                           
                            resultPath+=("../");
                        }
                    }

                    for(i=max; i<imagePathSplit.length; i++){
                        
                        resultPath+=imagePathSplit[i];
                        
                        if(i!=imagePathSplit.length-1){
                            
                            resultPath+="/";
                        }
                    }
                }
            }
        }

        return resultPath;
    }
    
    /**
     * computes and returns a split path corresponding to the given path
     * @param path
     * @return a split path corresponding to the given path
     */
    public static String[] getSplitPath(String path){
    	
    	String[] splitPath=null;
    	
    	if(path!=null && ! path.equals("")){
    		
    		String[] splitPath2=path.split("/");
    		LinkedList<String> list=new LinkedList<String>();
    		
    		for(int i=0; i<splitPath2.length; i++){
    			
    			if(splitPath2[i]!=null && ! splitPath2[i].equals("")){
    				
    				list.add(splitPath2[i]);
    			}
    		}
    		
    		splitPath=new String[list.size()];
    		
    		for(int i=0; i<list.size(); i++){
    			
    			splitPath[i]=list.get(i);
    		}
    	}
    	
    	return splitPath;
    }
    
	/**
	 * returns whether the given element element is a jwidget element
	 * @param element an element
	 * @return whether the given element element is a jwidget element
	 */
	public static boolean isJWidgetElement(Element element) {
		
		boolean isJWidgetElement=false;
		
		if(element!=null && element.getNodeName().equals(
				Toolkit.jwidgetElementName)) {
			
			isJWidgetElement=true;
		}
		
		return isJWidgetElement;
	}
	
	/**
	 * checks whether the given element has a jwidget element as a child
	 * @param element an element
	 * @return whether the given element has a jwidget element as a child
	 */
	public static boolean hasJWidgetChildElement(Element element) {
		
		boolean hasJWidgetChildElement=false;
		
		if(element!=null) {

			for(Node node=element.getFirstChild(); node!=null; 
				node=node.getNextSibling()) {

				if(node instanceof Element && 
						node.getNodeName().equals(Toolkit.jwidgetElementName)) {
					
					hasJWidgetChildElement=true;
					break;
				}
			}
		}
		
		return hasJWidgetChildElement;
	}
    
    /**
     * checks if the given document contains the rtda name space, if not, the namespace is added
     * @param doc a svg document
     */
    public static void checkRtdaXmlns(Document doc){
        
        if(Editor.isRtdaAnimationsVersion && doc!=null){
            
            Element svgRoot=doc.getDocumentElement();
            
            if(! svgRoot.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:rtda")){
                
                svgRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:rtda", rtdaNameSpace);
            }
        }
    }
    
    /**
     * returns the file name of this file
     * @param file a file
     * @return the file name of this file
     */
    public static String getFileName(File file){
        
        String fileName="";
        
        //getting the file name
        if(file!=null && file.exists()){
            
        	String uri=file.toURI().toASCIIString();
        	
        	if(uri.endsWith("/")){
        		
        		uri=uri.substring(0, uri.length()-1);
        	}
        	
        	int pos=uri.lastIndexOf("/");
        	
        	if(pos!=-1){
        		
        		fileName=uri.substring(pos+1, uri.length());
        	}
        }
        
        return fileName;
    }
    
    /**
     * clears the batik image cache
     */
    public static void clearBatikImageCache(){
    	
    	ImageTagRegistry.getRegistry().flushCache();
    }
    
    /**
     * returns whether the provided element is or contains rtda nodes
     * @param element an element
     * @return whether the provided element is or contains rtda nodes
     */
    public static boolean containsRtdaElements(Element element){
    	
    	boolean containsRtdaElements=false;
    	
    	if(element.getNodeName().startsWith(rtdaPrefix) ||
    		(element.getNodeName().equals(imageNodeName) && 
    			(element.hasAttribute(viewAtt) || element.hasAttribute(widgetAttribute)))){
    		
    		//the element is a rtda element or an image 
    		//that references a view or a widget
    		containsRtdaElements=true;
    		
    	}else if(element.hasChildNodes()){
    		
    		Node node=null;
    		
    		for(NodeIterator iterator=new NodeIterator(element); iterator.hasNext();){
    			
    			node=iterator.next();
    			
    			if(node!=null && node instanceof Element && 
    				node.getNodeName().startsWith(rtdaPrefix)){
    				
    				//the element contains a rtda element in its subtree
    	    		containsRtdaElements=true;
    	    		break;
    			}
    		}
    	}
    	
    	return containsRtdaElements;
    }
    
    /**
     * whether the provided element has animations as children
     * @param parentElement an element
     * @return whether the provided element has animations as children
     */
    public static boolean hasAnimations(Element parentElement){
    	
    	boolean hasAnimations=false;
    	
    	NodeList childNodes=parentElement.getChildNodes();
    	Node node=null;
    	
    	for(int i=0; i<childNodes.getLength(); i++){
    		
    		node=childNodes.item(i);
    		
    		if(node!=null && node instanceof Element && 
    			node.getNodeName().startsWith(rtdaPrefix) && 
    				! node.getNodeName().equals(jwidgetElementName)){
    			
    			hasAnimations=true;
    			break;
    		}
    	}
    	
    	return hasAnimations;
    }
    
    /**
     * returns the linked list of the child elements of the provided element
     * @param parentElement a parent element
     * @return the linked list of the child elements of the provided element
     */
    public static LinkedList<Element> getChildrenElements(
    		Element parentElement){
    	
    	NodeList elements=parentElement.getChildNodes();
        
        return getLinkedList(elements);
    }
    
    /**
     * returns the linked list corresponding to the node list
     * @param nodeList a node list
     * @return the linked list corresponding to the node list
     */
    public static LinkedList<Element> getLinkedList(NodeList nodeList){
    	
        LinkedList<Element> elementsList=new LinkedList<Element>();
        Node cur=null;
        
        for(int i=0; i<nodeList.getLength(); i++){
        	
            cur=nodeList.item(i);
            
            if(cur!=null && cur instanceof Element){
            	
            	elementsList.add((Element)cur);
            }
        }
        
        return elementsList;
    }
    
    /**
     * returns the linked list corresponding to the node list
     * @param nodeList a node list
     * @return the linked list corresponding to the node list
     */
    public static LinkedList<Node> getNodeLinkedList(NodeList nodeList){
    	
        LinkedList<Node> nodesList=new LinkedList<Node>();
        Node cur=null;
        
        for(int i=0; i<nodeList.getLength(); i++){
        	
            cur=nodeList.item(i);
            
            if(cur!=null){
            	
            	nodesList.add(cur);
            }
        }
        
        return nodesList;
    }
    
    /**
     * converts some style property values into attribute values
     * @param element an element
     */
    public static void transformFromStyleToAttribute(Element element) {
    	
    	if(element!=null) {
    		
    		String value="";
    		
    		for(String propertyName : properties) {
    			
    			value=getStyleProperty(element, propertyName);
    			
    			if(value!=null && ! value.equals("")) {
    				
    				setStyleProperty(element, propertyName, "");
    				element.setAttribute(propertyName, value);
    			}
    		}
    	}
    }
    
	/**
	 * returns the value of a style property
	 * @param element an element
	 * @param name the name of a style property
	 * @return  the value of a style property
	 */
	public static String getStyleProperty(Element element, String name){
		
		String value="";
		
		if(element!=null && name!=null && ! name.equals("")){
			
			//gets the value of the style attribute
			String styleValue=element.getAttribute("style");
			styleValue=styleValue.replaceAll("\\s*[;]\\s*", ";");
			styleValue=styleValue.replaceAll("\\s*[:]\\s*", ":");
			
			int rg=styleValue.indexOf(";"+name+":");
			
			if(rg!=-1){
				
				rg++;
			}
			
			if(rg==-1){
				
				rg=styleValue.indexOf(name+":");
				
				if(rg!=0){
					
					rg=-1;
				}
			}
			
			//if the value of the style attribute contains the property
			if(! styleValue.equals("") && rg!=-1){
				
				//computes the value of the property
				value=styleValue.substring(rg+name.length()+1, styleValue.length());
				rg=value.indexOf(";");
				value=value.substring(0, rg==-1?value.length():rg);
			}
		}
		
		return value;
	}
	
	/**
	 * setting the value of the given style element for the given node
	 * @param element an element
	 * @param name the name of a style element
	 * @param value the value for this style element
	 */
	public static void setStyleProperty(Element element, String name, String value){
		
		if(element!=null && name!=null && ! name.equals("")){
			
			if(value==null){
				
				value="";
			}
			
			//the separators
			String valuesSep=";", nameToValueSep=":";

			//getting the value of the style attribute
			String styleValue=element.getAttribute(styleAttName);
			styleValue=styleValue.replaceAll("\\s*[;]\\s*", ";");
			styleValue=styleValue.replaceAll("\\s*[:]\\s*", ":");

			int rg=styleValue.indexOf(valuesSep+name+nameToValueSep);
			
			if(rg!=-1){
				
				rg++;
			}
			
			if(rg==-1){
				
				rg=styleValue.indexOf(name+nameToValueSep);
				
				if(rg!=0){
					
					rg=-1;
				}
			}
			
			//if the value of the style attribute contains the property
			if(rg!=-1){
				
				String firstPart="", secondPart="";
				
				//computing the first part of the value before the current property
				firstPart=styleValue.substring(0, rg).trim();
				
				//getting the next index of a values separator
				int nextPos=styleValue.indexOf(valuesSep, rg);
				
				if(nextPos!=-1){
					
					secondPart=styleValue.substring(nextPos+1, styleValue.length()).trim();
					
				}else{
					
					secondPart="";
				}
				
				styleValue=firstPart+secondPart;
				
				if(! styleValue.equals("") && ! styleValue.endsWith(valuesSep)){
					
					styleValue+=valuesSep;
				}
				
				if(! value.equals("")){
					
					styleValue+=name+nameToValueSep+value+valuesSep;
				}

			}else{
				
				if(! value.equals("")){
					
					styleValue=name+nameToValueSep+value+valuesSep;
					
				}else{
					
					styleValue="";
				}
			}
			
			//sets the value of the style attribute
			if(! styleValue.equals("")){
				
				element.setAttribute(styleAttName, styleValue);
				
			}else{
				
				element.removeAttribute(styleAttName);
			}
		}
	}

    /**
     * removes the "url()" prefix in the given string
     * @param value the string to be modified
     * @return the string without the "url()" prefix
     */
    public static String toUnURLValue(String value){
        
        if(value==null){
            
            value="";
        }
        
        String val=new String(value);
        int ind0=val.indexOf("url(#"), ind1=val.indexOf(")");
        
        if(ind0>=0 && ind1>=0){
            
            val=val.substring(ind0+5, ind1);
        }
        
        return val;
    }
    
    /**
     * adds the "url()" prefix in the given string
     * @param value the string to be modified
     * @return the string withthe "url()" prefix
     */
    public static String toURLValue(String value){
        
        if(value==null || value.equals("")){
            
            value="";
            
        }else{
            
            value=new String("url(#"+value+")");
        }
        
        return value;
    }
    
    /**
     * returns the double number corresponding to the provided object
     * @param obj an object
     * @return the double number corresponding to the provided object
     */
    public static double getNumber(Object obj){
    	
    	double number=Double.NaN;
    	
    	if(obj!=null){
    		
    		if(obj instanceof Double){
    			
    			number=(Double)obj;
    			
    		}else if(obj instanceof Float){
    			
    			number=(Float)obj;
    			
    		}else if(obj instanceof Integer){
    			
    			number=(Integer)obj;
    			
    		}else if(obj instanceof String){

    			try{
    				number=Double.parseDouble((String)obj);
    			}catch (NumberFormatException ex) {
    				ex.printStackTrace();
    			}
    		}
    	}
    	
    	return number;
    }
    
    /**
     * returns the int number corresponding to the provided object
     * @param obj an object
     * @return the int number corresponding to the provided object
     */
    public static int getIntNumber(Object obj){
    	
    	int number=0;
    	
    	if(obj!=null){
    		
    		if(obj instanceof Double){
    			
    			number=((Double)obj).intValue();
    			
    		}else if(obj instanceof Float){
    			
    			number=((Float)obj).intValue();
    			
    		}else if(obj instanceof Integer){
    			
    			number=(Integer)obj;
    			
    		}else if(obj instanceof String){
    			
    			try{
    				number=Integer.parseInt((String)obj);
    			}catch (NumberFormatException ex) {
    				ex.printStackTrace();
    			}
    		}
    	}
    	
    	return number;
    }
    
    /**
     * returns the double number corresponding to the provided object
     * @param obj an object
     * @return the double number corresponding to the provided object
     */
    public static Object getNumberObject(Object obj){
    	
    	Object value=obj;
    	
    	if(obj!=null){
    		
    		if(obj instanceof Double && 
    				Double.isNaN((Double)obj)){
    			
    			value=null;
    			
    		}else if(obj instanceof String){
    			
    			try{
    				value=Double.parseDouble((String)obj);
    			} catch (NumberFormatException ex) {
    				ex.printStackTrace();
    				value=null;
    			}
    		}
    	}
    	
    	return value;
    }
}
