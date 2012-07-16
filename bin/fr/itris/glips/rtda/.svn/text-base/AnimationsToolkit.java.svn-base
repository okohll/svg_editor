/*
 * Created on 14 fevr. 2005
 */
package fr.itris.glips.rtda;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.w3c.dom.*;
import org.w3c.dom.svg.*;
import org.apache.batik.bridge.*;
import org.apache.batik.ext.awt.geom.*;
import org.apache.batik.gvt.*;
import fr.itris.glips.library.geom.path.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the toolkit for the animations
 * 
 * @author ITRIS, Jordi SUC
 */
public class AnimationsToolkit {
	
	/**
	 * attributes and nodes names
	 */
	private static final String dbNodeName="equipment.historymanager", dbNameAtt="DBname",
		tableNodeName="historymanager.table", tableNameAtt="id";
	
    /**
     * the namespace of the rtda animations
     */
    public static final String rtdaNameSpace="http://www.itris.fr/2003/animation";
    
    /**
     * the prefix for the namespace of the rtda animations
     */
    public static final String rtdaPrefix="rtda:";
	
    /**
     * the glips view file extension
     */
    public static final String GLIPS_VIEW_EXTENSION=".gv";
    
    /**
     * the xml  file extension
     */
    public static final String XML_FILE_EXTENSION=".xml";
    
	/**
	 * the name of the constant function
	 */
	public static final String CONSTANT_FUNCTION="constant";
	
	/**
	 * the name of the sin function
	 */
	public static final String SIN_FUNCTION="sin";
	
	/**
	 * the name of the triangle function
	 */
	public static final String TRIANGLE_FUNCTION="triangle";
	
	/**
	 * the name of the ramp function
	 */
	public static final String RAMP_FUNCTION="ramp";
	
	/**
	 * returns whether the given tag is a function or not
	 * @param tag a tag
	 * @return whether the given tag is a function or not
	 */
	public static boolean isFunction(String tag){
		
		if(tag!=null && ! tag.equals("")){
			
			return (tag.indexOf("function(")!=-1);
		}
		
		return false;
	}
	
	/**
	 * returns the modulo value of the given value
	 * @param value a value
	 * @param period the period
	 * @return the modulo value of the given value
	 */
	public static double getModuloValue(double value, double period){
		
		return value-period*Math.floor(value/period);
	}
	
	/**
	 * returns the value (a double between -1 and 1) of the sin function given the t parameter
	 * @param t the parameter
	 * @param initialValue the initial value
	 * @param period the period for the function
	 * @return the value (a double between -1 and 1) of the triangle function given the t parameter
	 */
	public static double sin(double t, double initialValue, double period){
		
		if(! Double.isNaN(t) && ! Double.isNaN(period)){
			
			t=AnimationsToolkit.getModuloValue(t, period)+
				AnimationsToolkit.getModuloValue(initialValue-period/4, period);
			t=AnimationsToolkit.getModuloValue(t, period);
			
			//computes the value of the triangle
			if(! Double.isNaN(t) && t>=0 && t<=period){
				
				return Math.sin(2*Math.PI*t/period);
			}
		}
		
		return 0;
	}
	
	/**
	 * returns the value (a double between -1 and 1) of the triangle function given the t parameter
	 * @param t the parameter
	 * @param initialValue the initial value
	 * @param period the period for the function
	 * @return the value (a double between -1 and 1) of the triangle function given the t parameter
	 */
	public static double triangle(double t, double initialValue, double period){
		
		if(! Double.isNaN(t) && ! Double.isNaN(period)){
			
			t=AnimationsToolkit.getModuloValue(t, period)+AnimationsToolkit.getModuloValue(initialValue, period);
			t=AnimationsToolkit.getModuloValue(t, period);
			
			//computes the value of the triangle
			if(! Double.isNaN(t) && t>=0 && t<=period){
				
				if(t<=(period/2)){
					
					return ((4/period)*t-1);
					
				}else if(t>(period/2)){
					
					return ((-4/period)*t+3);
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * returns the value (a double between -1 and 1) of the triangle function given the t parameter
	 * @param t the parameter
	 * @param initialValue the initial value
	 * @param period the period for the function
	 * @return the value (a double between -1 and 1) of the triangle function given the t parameter
	 */
	public static double ramp(double t, double initialValue, double period){
		
		if(! Double.isNaN(t) && ! Double.isNaN(period)){
			
			t=AnimationsToolkit.getModuloValue(t, period)+AnimationsToolkit.getModuloValue(initialValue, period);
			t=AnimationsToolkit.getModuloValue(t, period);
			
			//computes the value of the triangle
			if(! Double.isNaN(t) && t>=0 && t<=period){
				
				return ((2/period)*t-1);
			}
		}
		
		return 0;
	}
	
	/**
	 * returns the value (a double between -1 and 1) of the triangle function given the t parameter
	 * @param t the parameter
	 * @param t1 the instant when the function go from -1 to 1
	 * @param t2 the instant when the function go from 1 to -1
	 * @param period the period for the function
	 * @return the value (a double between -1 and 1) of the triangle function given the t parameter
	 */
	public static double square(double t, double t1, double t2, double period){
		
		double value=0;
		
		if(! Double.isNaN(t) && ! Double.isNaN(period)){
			
			t=AnimationsToolkit.getModuloValue(t, period);
			t1=AnimationsToolkit.getModuloValue(t1, period);
			t2=AnimationsToolkit.getModuloValue(t2, period);
			
			//computes the value of the function
			if(		! Double.isNaN(t) && t>=0 && t<=period && ! Double.isNaN(t1) && t1>=0 && t1<=period &&
					! Double.isNaN(t2) && t2>=0 && t2<=period){
				
				if(t1<t2){
					
					if(t1<=t && t<t2){
						
						value=1;
						
					}else{
						
						value=-1;
					}
					
				}else if(t2<t1){
					
					if((t>=0 && t<t2) || (t>=t1 && t<=period)){
						
						value=1;
						
					}else{
						
						value=-1;
					}
					
				}else if(t2==t1){
					
					value=(t==t1?1:-1);
				}
			}
		}
		
		return value;
	}
	
	/**
	 * returns this node or a parent of this node that has animation elements
	 * @param node a node
	 * @return this node or a parent of this node that has animation elements
	 */
	public static Node getAnimatedElement(Node node){
	    
	    Node animatedElement=null;
	    
	    if(node!=null && node instanceof Element){
	        
	        //the root element of the node
	        Node root=node.getOwnerDocument().getDocumentElement();
	        
	        //for each parent of the node, checks is it has animation nodes
	        while(node!=null && root!=node){
	            
	            if(hasAnimationNodes(node)){
	                
	                animatedElement=node;
	                break;
	            }
	            
	            node=node.getParentNode();
	        }
	    }
	    
	    return animatedElement;
	}
	
	/**
	 * returns whether the given node has animation node or not
	 * @param node a node
	 * @return whether the given node has animation node or not
	 */
	public static boolean hasAnimationNodes(Node node){
	    
	    boolean hasAnimationNodes=false;
	    
	    if(node!=null && node.hasChildNodes()){
	        
	        for(Node cur=node.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
	            
	            //for each child of the given node, checks if it is an animation node
	            if(cur.getNodeName().startsWith("rtda:")){
	                
	                hasAnimationNodes=true;
	                break;
	            }
	        }
	    }
	    
	    return hasAnimationNodes;
	}
	
	/**
	 * returns whether the given node contains active animation nodes
	 * @param node a node
	 * @return whether the given node contains active animation nodes
	 */
	public static boolean hasActiveAnimationNodes(Node node){
	    
	    boolean hasActiveAnimationNodes=false;
	    
	    if(node!=null && node.hasChildNodes()){
	        
	        for(Node cur=node.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
	            
	            //for each child of the given node, checks if it is an animation node
	            if(cur.getNodeName().startsWith("rtda:") && 
	                    ListenerActionBuilder.isActionNode(cur.getNodeName())){
	                
	                hasActiveAnimationNodes=true;
	                break;
	            }
	        }
	    }
	        
	    return hasActiveAnimationNodes;
	}
	
	/**
	 * returns the nodes at the given point
	 * @param picture a svg picture
	 * @param point the point on which a mouse event has been done
	 * @param exact whether the exact node corresponding to the given point should be returned or 
	 *    						if a parent of this node that contains animation nodes should be returned
	 * @return the node on which a mouse event has been done
	 */
	public static Node getNodeAt(SVGPicture picture, Point point, boolean exact){
		
		if(picture!=null && point!=null && picture.getCanvas()!=null){
			
			//gets the bridge context 
			BridgeContext ctxt= picture.getCanvas().getUpdateManager().getBridgeContext();
			
			if(ctxt!=null){
				
				//gets the graphics node corresponding to the given node
				GraphicsNode gnode=null;
				
				try{
					gnode=ctxt.getGraphicsNode(
							picture.getDocument().getDocumentElement());
				}catch (Exception e){}
				
				if(gnode!=null){
					
					GraphicsNode graphicsNode=null;
					
					try{graphicsNode=gnode.nodeHitAt(point);}catch (Exception ex){}
					
					if(graphicsNode!=null){
						
						Node node=ctxt.getElement(graphicsNode);
						
						//getting one of the parent nodes of this node, 
						//that contains either animation nodes or active animation nodes
						while(node!=null){
						    
						    if(exact){
						        
						        if(hasAnimationNodes(node)){
						            
						            break;
						        }
						        
						    }else{
						        
						        if(hasActiveAnimationNodes(node)){
						            
						            break;
						        }
						    }
						    
						    node=node.getParentNode();
						}
						
						return node;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * computes the position and the size of a node on the canvas 
	 * @param picture a svg picture
	 * @param node the node whose position and size is to be computed
	 * @return a rectangle representing the position and size of the given node
	 */
	public static Rectangle2D getNodeBounds(SVGPicture picture, Node node){
		
		Rectangle2D bounds=new Rectangle();
		
		if(picture!=null && picture.getCanvas()!=null && node!=null){
            
            //gets the bridge context 
            BridgeContext ctxt=null;
            
            if(picture.getCanvas().getUpdateManager()!=null){
            	
            	ctxt=picture.getCanvas().getUpdateManager().getBridgeContext();
            }

            if(ctxt!=null){
                
                //gets the graphics node corresponding to the given node
                GraphicsNode gnode=null;
                
                try{gnode=ctxt.getGraphicsNode((Element)node);}catch (Exception e){}

                if(gnode!=null){

                	bounds=gnode.getGeometryBounds();
                	
                	if(bounds!=null && (bounds.getWidth()==0 || bounds.getHeight()==0) && 
                			node.getNodeName().equals("text")){
                		
                		//getting the location of the text
                		Element textEl=(Element)node;
                		double x=0;
                		double y=0;
                		
                		try{
                			x=Double.parseDouble(textEl.getAttribute("x"));
                		}catch (Exception ex){}
                		
                		try{
                			y=Double.parseDouble(textEl.getAttribute("y"));
                		}catch (Exception ex){}
                		
                		bounds=new Rectangle2D.Double(x, y, 1, 1);
                	}

                	if(bounds!=null){
                    	
                    	AffineTransform transform=new AffineTransform();

                    	if(gnode.getGlobalTransform()!=null){
                    		
                    		transform.preConcatenate(gnode.getGlobalTransform());
                    	}
                    	
                		bounds=transform.createTransformedShape(bounds).getBounds2D();

                	}else {
                		
                		bounds=new Rectangle();
                	}
                }
            }
        }

        return bounds;
    }
	
	/**
	 * computes the position and the size of a node on the canvas 
	 * @param picture a svg picture
	 * @param node the node whose position and size is to be computed
	 * @return a rectangle representing the position and size of the given node
	 */
	public static Rectangle2D getGeometryNodeBounds(SVGPicture picture, Node node){
		
		Rectangle2D bounds=new Rectangle();
		
		if(picture!=null && picture.getCanvas()!=null && node!=null){
            
            //gets the bridge context 
            BridgeContext ctxt=null;
            
            if(picture.getCanvas().getUpdateManager()!=null){
            	
            	ctxt=picture.getCanvas().getUpdateManager().getBridgeContext();
            }

            if(ctxt!=null){
                
                //gets the graphics node corresponding to the given node
                GraphicsNode gnode=null;
                
                try{gnode=ctxt.getGraphicsNode((Element)node);}catch (Exception e){}

                if(gnode!=null){

                	bounds=gnode.getTransformedBounds(new AffineTransform());
                }
            }
		}
		
		return bounds;
	}
	
	/**
	 * returns the area into which the node is painted
	 * @param picture a svg picture
	 * @param node a node
	 * @return the area into which the node is painted
	 */
	protected static Shape getSensitiveArea(SVGPicture picture, Node node){
		
		Shape shape=null;
		
		if(picture!=null && picture.getCanvas()!=null && 
				node!=null && node instanceof Element){
			
			//gets the bridge context 
			BridgeContext ctxt=picture.getCanvas().getUpdateManager().getBridgeContext();
			
			if(ctxt!=null){
				
				//gets the graphics node corresponding to the given node
				GraphicsNode gnode=null;
				
				try{gnode=ctxt.getGraphicsNode((Element)node);}catch (Exception e){}
				
				if(gnode!=null){
					
					try{shape=gnode.getOutline();}catch (Exception ex){}
					
					if(shape!=null){
						
						AffineTransform af=gnode.getTransform();
						
						if(af==null){
							
							af=new AffineTransform();
						}
						
	                	//getting the viewing transform
	                	AffineTransform vt=picture.getCanvas().getViewingTransform();
	                	
	                	if(vt!=null){
	                		
	                		af.preConcatenate(vt);
	                	}
						
						shape=af.createTransformedShape(shape);
					}
				}
			}
		}
		
		return shape;
	}
	
	/**
	 * find the accurate id for a node
	 * @param picture a svg picture
	 * @param baseString the base of the id
	 * @return an id
	 */
	public static String getId(SVGPicture picture, String baseString){
		
		if(picture!=null && picture.getCanvas()!=null){
			
			SVGDocument doc=picture.getCanvas().getSVGDocument();
			
			if(doc!=null){
				
				LinkedList<String> ids=new LinkedList<String>();
				Node current=null;
				String attId="";
				
				//adds to the list all the ids found among the children of the root element
				for(NodeIterator it=new NodeIterator(doc.getDocumentElement()); it.hasNext();){
					
					try{current=it.next();}catch (Exception ex){current=null;}
					
					if(current!=null && current instanceof Element && 
							! current.equals(doc.getDocumentElement())){
						
						attId=((Element)current).getAttribute("id");
						
						if(attId!=null && ! attId.equals("")){
							
							ids.add(attId);
						}
					}
				}
				
				int i=0;
				
				//tests for each integer string if it is already an id
				while(ids.contains(baseString.concat(i+""))){
					
					i++;
				}
				
				return new String(baseString.concat(i+""));
			}
		}
		
		return "";
	}
	
	/**
	 * checks whether the given id already exists or not among the children of the given root node
	 * @param canvas a canvas
	 * @param id an id to be checked
	 * @return true if the id does not already exists
	 */
	public static boolean checkId(SVGCanvas canvas, String id){
		
		if(canvas!=null){
			
			SVGDocument doc=canvas.getSVGDocument();
			
			if(doc!=null){
				
				LinkedList<String> ids=new LinkedList<String>();
				Node current=null;
				String attId="";
				
				//adds to the list all the ids found among the children of the root element
				for(NodeIterator it=new NodeIterator(doc.getDocumentElement()); it.hasNext();){
					
					current=it.next();
					
					if(current!=null && current instanceof Element && 
							! current.equals(doc.getDocumentElement())){
						
						attId=((Element)current).getAttribute("id");
						
						if(attId!=null && ! attId.equals("")){
							
							ids.add(attId);
						}
					}
				}
				
				//tests for each integer string if it is already an id
				if(ids.contains(id)){
					
					return false;
				}					
				
				return true; 
			}
		}
		
		return false;
	}
	
	/**
	 * computes the outline of a node on the canvas that has the node's transform applied
	 * @param picture a svg picture
	 * @param node the node
	 * @return the outline of the given node
	 */
	protected static Shape getTransformedOutline(SVGPicture picture, Node node){
		
		Shape outline=new Rectangle();
		
		if(picture!=null && picture.getCanvas()!=null && node!=null){
			
			outline=getGeometryOutline(picture, node);
			
			//gets the bridge context 
			BridgeContext ctxt=picture.getCanvas().getUpdateManager().getBridgeContext();
			
			if(ctxt!=null){
				
				//gets the graphics node corresponding to the given node
				GraphicsNode gnode=null;
				
				try{gnode=ctxt.getGraphicsNode((Element)node);}catch (Exception e){}
				
				if(gnode!=null){
					
					AffineTransform af=gnode.getTransform();
					
					if(af!=null){
						
						outline=af.createTransformedShape(outline);
					}
				}
			}
		}
		
		return outline;
	}
	
	/**
	 * computes the outline of a node on the canvas
	 * @param picture a svg picture
	 * @param node the node
	 * @return the outline of the given node
	 */
	protected static Shape getGeometryOutline(SVGPicture picture, Node node){
		
		Shape outline=new Rectangle();
		
		if(picture!=null && picture.getCanvas()!=null && 
				node!=null && node instanceof Element){
			
			//gets the bridge context 
			BridgeContext ctxt=picture.getCanvas().getUpdateManager().getBridgeContext();
			
			if(ctxt!=null){
				
				//gets the graphics node corresponding to the given node
				GraphicsNode gnode=null;
				
				try{gnode=ctxt.getGraphicsNode((Element)node);}catch (Exception e){}
				
				if(gnode!=null){
					
					outline=gnode.getOutline();
				}
			}
		}
		
		return outline;
	}
	
	/**
	 * converts the given element into a path
	 * @param picture a svg picture
	 * @param baseElement an element
	 * @return a path element
	 */
	public static Element convertToPath(SVGPicture picture, Element baseElement){
		
		Element pathElement=null;
	    
	    if(picture!=null && picture.getCanvas()!=null && baseElement!=null){
	        
			SVGDocument doc=picture.getCanvas().getSVGDocument();

			if(doc!=null){
				
				double[] values=null, vals=new double[7];
				int type=-1;
				StringBuffer pathValues=new StringBuffer("");
				
				Shape outline=getTransformedOutline(picture, baseElement);
				GeneralPath gpath=new GeneralPath(outline);
				
				//for each command in the path, the command and its values are added to the string value
				for(PathIterator pit=gpath.getPathIterator(new AffineTransform()); ! pit.isDone(); pit.next()){
					
					type=pit.currentSegment(vals);
					
					if(type==PathIterator.SEG_CLOSE){
						
						pathValues.append("Z ");
						
					}else if(type==PathIterator.SEG_CUBICTO){
						
						values=new double[6];
						pit.currentSegment(values);
						pathValues.append("C "+values[0]+" "+values[1]+" "+values[2]+
								" "+values[3]+" "+values[4]+" "+values[5]+" ");
						
					}else if(type==PathIterator.SEG_LINETO){
						
						values=new double[2];
						pit.currentSegment(values);
						pathValues.append("L "+values[0]+" "+values[1]+" ");
						
					}else if(type==PathIterator.SEG_MOVETO){
						
						values=new double[2];
						pit.currentSegment(values);
						pathValues.append("M "+values[0]+" "+values[1]+" ");
						
					}else if(type==PathIterator.SEG_QUADTO){
						
						values=new double[4];
						pit.currentSegment(values);
						pathValues.append("Q "+values[0]+" "+values[1]+" "+values[2]+" "+values[3]+" ");
						
					}else{
						
						values=null;
					}
				}
				
				//creates the path element
				pathElement=doc.createElementNS(
					doc.getDocumentElement().getNamespaceURI(), "path");
				
				//setting the attributes for the path
				pathElement.setAttribute("d", pathValues.toString());
			}
	    }

		return pathElement;
	}
	
	/**
	 * creates the "d" attribute value of a path that is the intersection of 
	 * the provided shape and rectangle
	 * @param picture a svg picture
	 * @param shape an element
	 * @param rect the rectangle with which the shape will be intersected
	 * @return the "d" attribute value of a path that is the intersection of 
	 * the provided shape and rectangle
	 */
	public static String intersectPath(SVGPicture picture, Element shape, Rectangle rect){
		
		String dAtt="";
	    
	    if(picture!=null && picture.getCanvas()!=null && shape!=null && rect!=null){
	        
			SVGDocument doc=picture.getCanvas().getSVGDocument();
			
			if(doc!=null){

				Shape outline=getTransformedOutline(picture, shape);
				
				//creating the intersection area
				Area area=new Area(outline);
				area.intersect(new Area(rect));
				
				//creating the path corresponding to the computed area
				Path pathManager=new Path(new ExtendedGeneralPath(area));
				
				//setting the attributes for the path
				dAtt=pathManager.toString();
			}
	    }

		return dAtt;
	}
	
    /**
     * returns the value of the text nodes contained in the given element
     * @param element an element
     * @return the value of the text nodes contained in the given element
     */
    public static String getText(Element element){

        String text="";
        
        if(element!=null){
            
            //for each child of the given element, builds the value of the text nodes 
            for(Node cur=element.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
                
                if(cur.getNodeName().equals("#text")){
                    
                    text=text.concat(cur.getNodeValue());
                }
            }
        }

        return text;
    }
    
    /**
     * sets the new new text values for an element (removes all the previous text values before)
     * @param element an element
     * @param text the new text value
     */
    public static void setText(Element element, String text){
        
        if(element!=null && text!=null){
            
            NodeList children=element.getChildNodes();
            LinkedList<Node> childrenList=new LinkedList<Node>();
            
            for(int i=0; i<children.getLength(); i++){
            	
                if(children.item(i).getNodeName().equals("#text")){
                	
                	childrenList.add(children.item(i));
                }
            }

            //for each child of the given element, builds the value of the text nodes 
            for(Node node : childrenList){
                
                element.removeChild(node);
            }
            
            //creates and adds the new text node
            Node textNode=element.getOwnerDocument().createTextNode(text);
            element.appendChild(textNode);
        }
    }
    
    /**
     * returns the affine transform corresponding to this node and the canvas
     * @param picture a svg picture
     * @param node a node
     * @return the corresponding affine transform
     */
    public static AffineTransform getTransform(SVGPicture picture, Node node){
        
    	AffineTransform af=new AffineTransform();
    	
		if(picture!=null && picture.getCanvas()!=null && node!=null){
            
            //gets the bridge context 
            BridgeContext ctxt=null;
            
            if(picture.getCanvas().getUpdateManager()!=null){
            	
            	ctxt=picture.getCanvas().getUpdateManager().getBridgeContext();
            }

            if(ctxt!=null){
                
                //gets the graphics node corresponding to the given node
                GraphicsNode gnode=null;
                
                try{gnode=ctxt.getGraphicsNode((Element)node);}catch (Exception e){}
                
                if(gnode!=null && gnode.getTransform()!=null){

                	af=new AffineTransform(gnode.getTransform());
                }
            }
        }
		
		return af;
    }
    
    /**
     * sets the affine transform corresponding to this node and the canvas
     * @param picture a svg picture
     * @param node a node
     * @param newTransform the new transform for the element
     */
    public static void setTransform(
    		SVGPicture picture, Node node, AffineTransform newTransform){

		if(picture!=null && picture.getCanvas()!=null &&
				node!=null && newTransform!=null){
            
            //gets the bridge context 
            BridgeContext ctxt=null;
            
            if(picture.getCanvas().getUpdateManager()!=null){
            	
            	ctxt=picture.getCanvas().getUpdateManager().getBridgeContext();
            }

            if(ctxt!=null){
                
                //gets the graphics node corresponding to the given node
                GraphicsNode gnode=null;
                
                try{gnode=ctxt.getGraphicsNode((Element)node);}catch (Exception e){}
                
                if(gnode!=null){

                	gnode.setTransform(newTransform);
            		final SVGTransformMatrix matrix=new SVGTransformMatrix(1, 0, 0, 1, 0, 0);
                    matrix.concatenateTransform(newTransform);
                    
                    ((Element)node).setAttribute(
                        	"transform", matrix.getMatrixRepresentation());
                }
            }
        }
    }
    
    /**
     * returns the file that is the anchor of the project
     * @param fileUri the uri of a file
     * @param isEditionMode whether the current mode is the edition or the runtime mode
     * @return the file that is the anchor of the project
     */
    public static File getProjectFile(URI fileUri, boolean isEditionMode){

    	File projectFile=null;
        
        if(fileUri!=null){

    		File theFile=null;

    		try{theFile=new File(fileUri);}catch (Exception ex){ex.printStackTrace();}
    		
    		if(isEditionMode) {

                File[] children=null;
                File file=null;
                int i;
    			
                //getting the first directory that contains a ".project" file
                while(file!=null){
                    
                    //checking if one of the children of the file is a ".project" file
                    children=file.listFiles();
                    
                    if(children!=null){
                        
                        for(i=0; i<children.length; i++){
                            
                            if(children[i]!=null && children[i].getName().equals(".project")){
                                
                                projectFile=file;
                                break;
                            }
                        }
                    }
                    
                    file=file.getParentFile();
                }
                
                if(projectFile==null && theFile!=null){
                	
                	projectFile=theFile.getParentFile();
                }
    			
    		}else if(theFile!=null){
    			
    			//getting the parent directory of the given file, as in the runtime version, all the file can be found 
    			//in the same directory
    			projectFile=theFile.getParentFile();
    		}
        }

        return projectFile;
    }
    
	/**
	 * returns the map associating the name of a db to 
	 * the set of the names of all the tables in this db
	 * @param dbRootElement the root element of the xml db
	 * @return the map associating the name of a db to 
	 * the set of the names of all the tables in this db
	 */
	public static LinkedHashMap<String, Set<String>> 
		getDBsAndTables(Element dbRootElement){
		
		LinkedHashMap<String, Set<String>> map=
			new LinkedHashMap<String, Set<String>>();
		
		//getting all the db nodes in the sub tree of the root element
		NodeList dbNodes=dbRootElement.getElementsByTagName(dbNodeName);

		if(dbNodes.getLength()>0){
	
			Element dbEl, tbEl;
			NodeList tableNodes;
			Set<String> tableNames;
			String dbName="", tableName="";
			
			for(int i=0; i<dbNodes.getLength(); i++){

				dbEl=(Element)dbNodes.item(i);
				
				//getting the name of the db
				dbName=dbEl.getAttribute(dbNameAtt);
				tableNodes=dbEl.getElementsByTagName(tableNodeName);
				
				if(tableNodes.getLength()>0){
					
					tableNames=new HashSet<String>();
					
					for(int j=0; j<tableNodes.getLength(); j++){
						
						tbEl=(Element)tableNodes.item(j);
						
						tableName=tbEl.getAttribute(tableNameAtt);
						tableNames.add(tableName);
					}

					map.put(dbName, tableNames);
				}
			}
		}

		return map;
	}
	
	/**
	 * normalizes the value provided as a parameters
	 * @param value a string
	 * @return the normalized string
	 */
	public static String normalizeEnumeratedValue(String value){
		
		return value.toLowerCase();
	}
	
    /**
     * returns the double number corresponding to the provided object and 
     * that is a value of the type of the provided type
     * @param valStr the string defining the value
     * @param tagType the type of the tag whose value is computed
     * @return the double number corresponding to the provided object
     */
    public static Object getNumber(String valStr, int tagType){
    	
    	Object value=null;
    	
        //getting the value to set
        if(tagType==TagToolkit.ANALOGIC_INTEGER){
        	
        	try{
        		value=Integer.parseInt(valStr);
        	}catch (Exception ex){}
        	
        }else{
        	
        	try{
        		value=Double.parseDouble(valStr);
        	}catch (Exception ex){}
        }
        
        return value;
    }
}
