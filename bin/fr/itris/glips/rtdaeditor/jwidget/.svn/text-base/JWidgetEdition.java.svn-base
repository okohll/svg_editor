/*
 * Created on 28 avr. 2005
 */
package fr.itris.glips.rtdaeditor.jwidget;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.resources.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import libraries.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.jwidget.*;
import com.keypoint.*;

/**
 * the interface of the plugins that are used to handle jwidgets
 * 
 * @author ITRIS, Jordi SUC
 */
public abstract class JWidgetEdition{
	
	/**
	 * the jwidget manager
	 */
	protected JWidgetManager jwidgetManager;
	
	/**
	 * the id
	 */
	protected String id="";
	
	/**
	 * the description
	 */
	protected String description="";
	
	/**
	 * the position of the toogle button in the tool bar
	 */
	protected int toolItemPosition=0;
	
	/**
	 * the animations and actions document for this widget
	 */
	protected Document animationsAndActionsDocument;
	
	/**
	 * whether the xml document contains animations or actions
	 */
	protected boolean defineAnimations=false, defineActions=false;
	
	/**
	 * the list of the names of the properties
	 */
	protected LinkedList<String> propertiesList=new LinkedList<String>();
	
	/**
	 * the list of the default values for the properties
	 */
	protected LinkedList<String> defaultValues=new LinkedList<String>();
	
	/**
	 * the icons
	 */
	protected ImageIcon icon, grayIcon;
	
	/**
	 * the bundle
	 */
	protected ResourceBundle bundle;
	
    /**
     * the main frame
     */
    protected Frame mainFrame;
    
    /**
     * the component used to configure the widget
     */
    protected JWidgetConfigurationPanel configurationPanel;
    
    /**
     * the undo/redo modification label
     */
    private String undoRedoModificationLabel="";
    
    /**
     * the animations and actions source choosers
     */
    protected AnimationChooserJWidgetSourceChooser 
    	animationsSourceChooser, actionsSourceChooser;
    
    /**
     * whether the jwidget contains inner components
     */
    protected boolean containsInnerComponents=false;

    /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     * @param id the id of the widget edition object
     * @param toolItemPosition the tool item position
     */
    public JWidgetEdition(JWidgetManager jwidgetManager, 
    		Frame mainFrame, String id, int toolItemPosition) {

    	this.jwidgetManager=jwidgetManager;
        this.mainFrame=mainFrame;
        this.id=id;
        this.toolItemPosition=toolItemPosition;

    	animationsAndActionsDocument=
    		getAnimationsAndActionsDocument(getClass(), getId()+".xml");

    	//checking if the animations and actions document defines animations and actions
    	NodeList animations=animationsAndActionsDocument.
    		getElementsByTagName(Toolkit.animationTagName);
		defineAnimations=(animations.getLength()>0);
    	
    	NodeList actions=animationsAndActionsDocument.
    		getElementsByTagName(Toolkit.actionTagName);
		defineActions=(actions.getLength()>0);
    
    	//creating the icons
    	ImageIcon[] imageIcons=getIcons(getClass(), getId());
    	icon=imageIcons[0];
    	grayIcon=imageIcons[1];
    	
        try{
            bundle=ResourceBundle.getBundle(
            		getClass().getPackage().getName()+".properties."+getId(), 
            				Locale.getDefault(), PluginLoader.urlClassLoader);
        }catch (Exception ex){}
        
        if(bundle==null) {
        	
            try{
                bundle=ResourceBundle.getBundle(
                		getClass().getPackage().getName()+".properties."+getId());
            }catch (Exception e){e.printStackTrace();}
        }
        
        try {
    		description=bundle.getString("description");
        	undoRedoModificationLabel=ResourcesManager.bundle.getString("jwidgeteditionundoredomodify");
        }catch (Exception ex) {ex.printStackTrace();}
        
        //buildConfigurationPanel(); TODO
    }
    
    /**
     * @return the id of the extension
     */
    public String getId() {
    	return id;
    }
    
    /**
     * @return the localized description of the plugin
     */
    public String getDescription() {
    	return description;
    }

    /**
	 * @return the toolItemPosition
	 */
	public int getToolItemPosition() {
		return toolItemPosition;
	}

	/**
	 * @return the defaultValues
	 */
	public LinkedList<String> getDefaultValues() {
		return defaultValues;
	}

	/**
	 * @return the propertiesList
	 */
	public LinkedList<String> getPropertiesList() {
		return propertiesList;
	}

	/**
	 * @return the bundle
	 */
	public ResourceBundle getBundle() {
		return bundle;
	}
	
	/**
     * @param isGrayIcon whether the icon should be grayed or not
     * @return the icon for the extension
     */
	public ImageIcon getIcon(boolean isGrayIcon) {

		if(isGrayIcon) {
			
			return grayIcon;
		}
		
		return icon;
	}

    /**
     * returns the configuration panel used to configure the extension
     * @param jwidgetElement a jwidget element
     * @return the configuration panel used to configure the extension
     */
    public JPanel handleConfigurationPane(Element jwidgetElement){
    	
		configurationPanel.setElement(jwidgetElement);
		
		if(jwidgetElement==null) {
			
			if(actionsSourceChooser!=null) {
				
				actionsSourceChooser.clean();
			}
			
			if(animationsSourceChooser!=null) {
				
				animationsSourceChooser.clean();
			}
		}

		return configurationPanel;
    }

    /**
     * sets the image of this jwidget element
     * @param svgHandle a svg handle
     * @param jwidgetElement the jwidget element
     * @param imageJWidgetElement the image jwidget element
     * @param size the size of the image
     */
	public void handleRepresentationImage(SVGHandle svgHandle, Element jwidgetElement, 
					final Element imageJWidgetElement, Dimension size) {

		if(size==null) {
			
			//computing the current size
			double width=1, height=1;
			
			try {
				width=Double.parseDouble(imageJWidgetElement.getAttribute("width"));
				height=Double.parseDouble(imageJWidgetElement.getAttribute("height"));
			}catch (Exception ex) {}
			
			size=new Dimension((int)width, (int)height);
		}
		
		final String attributeValue="data:image/png;base64,"+
			getEncodedImage(createImage(jwidgetElement, size));
		
		Runnable runnable=new Runnable() {
			
			public void run() {
			
				imageJWidgetElement.setAttributeNS(
						EditorToolkit.xmlnsXLinkNS, EditorToolkit.xLinkprefix+"href", attributeValue);
			}
		};
		
		//the set of the elements that are modified
		HashSet<Element> elements=new HashSet<Element>();
		elements.add(imageJWidgetElement);
		
		svgHandle.enqueue(runnable, elements, true);
	}
	
	/**
	 * creates the image
	 * @param size the size of the image
	 * @param jwidgetElement the jwidget element
	 * @return image an image
	 */
	protected abstract BufferedImage createImage(Element jwidgetElement, Dimension size);
    
    /**
     * encodes the given component into a base64 string
     * @param image an image
     * @return the encoded image string
     */
    protected String getEncodedImage(BufferedImage image) {
    	
    	String encodedString="";
    	
    	if(image!=null) {
    		
        	//creating the encoder
        	PngEncoder pngEncoder=new PngEncoder(image, true, PngEncoder.FILTER_NONE, 9);
        	
        	//encoding the image
        	byte[] data=pngEncoder.pngEncode();
        	
    		try{
    			
    			//encoding the data in base64
    			StringBuffer buffer=new StringBuffer(new String(Base64Coder.encode(data)));
    			
    			for(int i=256; i<buffer.length(); i+=256) {
    				
    				buffer.insert(i, '\n');
    			}
    			
    			encodedString=buffer.toString();
    		}catch (Exception ex) {ex.printStackTrace();}
    	}

    	return encodedString;
    }
    
	/**
	 * builds the configuration pane
	 */
	protected abstract void buildConfigurationPanel();
	
	/**
	 * sets the new property value for the given jwidget element
	 * @param jwidgetElement a jwidget element
	 * @param propertyName the name of a property
	 * @param newValue the new value of a property
	 * @param refreshElementRepresentation whether the representation image of the jwidget element should
	 * 																	be refreshed
	 */
	public void setProperty(final Element jwidgetElement, final String propertyName, final String newValue, 
				final boolean refreshElementRepresentation) {
		
		final SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		final String oldValue=jwidgetElement.getAttribute(propertyName);

		if(! oldValue.equals(newValue)) {
			
			final Element topMostJWidgetElement=getTopMostJWidgetElement(jwidgetElement);

			//the execute runnable
			Runnable executeRunnable=new Runnable(){
				
				public void run() {

					//setting the new value for the property
					jwidgetElement.setAttribute(propertyName, newValue);
					
					if(refreshElementRepresentation){
						
						//refreshing the image representation of the property
						jwidgetManager.refreshElementRepresentation(topMostJWidgetElement);	
					}
				}
			};

			//adding the undo/redo runnables
			Runnable undoRunnable=new Runnable(){

				public void run() {

					jwidgetElement.setAttribute(propertyName, oldValue);
					handle.getSvgDOMListenerManager().
						fireNodeChanged(jwidgetElement);
					
					if(refreshElementRepresentation) {
						
						jwidgetManager.refreshElementRepresentation(topMostJWidgetElement);
					}
				}
			};
			
			Runnable redoRunnable=new Runnable(){

				public void run() {

					jwidgetElement.setAttribute(propertyName, newValue);
					handle.getSvgDOMListenerManager().
						fireNodeChanged(jwidgetElement);
					
					if(refreshElementRepresentation) {
						
						jwidgetManager.refreshElementRepresentation(topMostJWidgetElement);
					}
				}
			};
			
			addUndoRedoAction(executeRunnable, undoRunnable, redoRunnable);
		}
	}
	
	/**
	 * returns the value of the property of the given jwidget element
	 * @param jwidgetElement a jwidget element
	 * @param propertyName the name of a property
	 * @return the value of the property of the given jwidget element
	 */
	public String getProperty(Element jwidgetElement, String propertyName) {
		
		return jwidgetElement.getAttribute(propertyName);
	}
	
	/**
	 * creating the jwidget element
	 * @param parentElement the parent element to which the jwidget node is added
	 * @return the jwidget element
	 */
	public Element createJWidgetElement(Element parentElement) {
		
		Element jwidgetElement=parentElement.getOwnerDocument().
			createElementNS(null, Toolkit.jwidgetElementName);
		jwidgetElement.setAttribute(Toolkit.nameAttribute, getId());
		jwidgetElement.setAttribute(Toolkit.idAttribute, getUniqueId());
		
		//creating the property attributes
		int i=0;
		
		for(String property : propertiesList) {

			//setting the new attribute for the jwidget element
			jwidgetElement.setAttribute(property, defaultValues.get(i));
			i++;
		}
		
		parentElement.appendChild(jwidgetElement);
		
		return jwidgetElement;
	}
	
	/**
	 * creates a new sub widget element
	 * @param jwidgetParentElement the parent jwidget element
	 * @param name the name of the new jwidget sub element
	 * @param widgetLabel the label of the sub widget element
	 * @param attributes the map associating a property name to its default value
	 * @return a new sub widget element
	 * @param refreshElementRepresentation whether the representation image of the jwidget element should
	 * 																	be refreshed
	 */
	public Element createSubWidgetElement(	final Element jwidgetParentElement, String name, String widgetLabel, 
																		HashMap<String, String> attributes, 
																		final boolean refreshElementRepresentation) {

		final SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		final Element jwidgetElement=jwidgetParentElement.getOwnerDocument().
			createElementNS(null, Toolkit.jwidgetElementName);
		jwidgetElement.setAttribute(Toolkit.nameAttribute, name);
		jwidgetElement.setAttribute(Toolkit.labelAttribute, widgetLabel);
		jwidgetElement.setAttribute(Toolkit.idAttribute, getUniqueId());
		
		if(attributes!=null){
			
			//creating the property attributes
			String defaultValue=null;

			for(String property : attributes.keySet()) {

				defaultValue=attributes.get(property);
				
				//setting the new attribute for the jwidget element
				jwidgetElement.setAttribute(property, defaultValue);
			}
		}
		
		//the execute runnable
		Runnable executeRunnable=new Runnable(){
			
			public void run() {
				
				//adding the sub widget element to the parent element
				jwidgetParentElement.appendChild(jwidgetElement);
				handle.getSvgDOMListenerManager().
					fireNodeInserted(jwidgetParentElement, jwidgetElement);

				//refreshing the image representation of the property
				if(refreshElementRepresentation) {
					
					jwidgetManager.refreshElementRepresentation(jwidgetParentElement);
				}
			}
		};
		
		//the undo/redo runnables
		Runnable undoRunnable=new Runnable(){

			public void run() {

				jwidgetParentElement.removeChild(jwidgetElement);
				handle.getSvgDOMListenerManager().
					fireNodeRemoved(jwidgetParentElement, jwidgetElement);
				
				//refreshing the image representation of the property
				if(refreshElementRepresentation) {
					
					jwidgetManager.refreshElementRepresentation(jwidgetParentElement);
				}
			}
		};
		
		addUndoRedoAction(executeRunnable, undoRunnable, executeRunnable);

		return jwidgetElement;
	}
	
	/**
	 * @return a unique id
	 */
	protected String getUniqueId() {
		
		String number=	((int)Math.floor(Math.abs(Math.random())*10))+""+
									((int)Math.floor(Math.abs(Math.random())*10))+""+
									((int)Math.floor(Math.abs(Math.random())*10))+""+
									((int)Math.floor(Math.abs(Math.random())*10))+""+
									((int)Math.floor(Math.abs(Math.random())*10))+""+
									((int)Math.floor(Math.abs(Math.random())*10))+""+
									((int)Math.floor(Math.abs(Math.random())*10))+""+
									((int)Math.floor(Math.abs(Math.random())*10))+""+
									((int)Math.floor(Math.abs(Math.random())*10))+""+
									((int)Math.floor(Math.abs(Math.random())*10));

		return JWidgetRuntime.jwidgetPrefixtId+number;
	}
	
	/**
	 * removes the given child element from the given jwidget parent element
	 * @param jwidgetParentElement the jwidget parent element
	 * @param childElement the jwidget child element
	 * @param refreshElementRepresentation whether the representation image of the jwidget element should
	 * 																	be refreshed
	 */
	public  void removeSubWidgetElement(	final Element jwidgetParentElement, final Element childElement,
																	final boolean refreshElementRepresentation) {

		final SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		//getting the element that can be found after the given child element
		final Element nextElement=getNextElement(childElement);

		//the execute runnable
		Runnable executeRunnable=new Runnable(){
			
			public void run() {
				
				//removing the node from the parent node
				jwidgetParentElement.removeChild(childElement);
				handle.getSvgDOMListenerManager().
					fireNodeRemoved(jwidgetParentElement, childElement);
				
				//refreshing the image representation of the property
				if(refreshElementRepresentation) {
					
					jwidgetManager.refreshElementRepresentation(jwidgetParentElement);
				}
			}
		};
		
		//the undo/redo runnables
		Runnable undoRunnable=new Runnable(){

			public void run() {

				if(nextElement!=null) {
					
					jwidgetParentElement.insertBefore(childElement, nextElement);
					
				}else {
					
					jwidgetParentElement.appendChild(childElement);
				}
				
				handle.getSvgDOMListenerManager().
					fireNodeInserted(jwidgetParentElement, childElement);
				
				//refreshing the image representation of the property
				if(refreshElementRepresentation) {
					
					jwidgetManager.refreshElementRepresentation(jwidgetParentElement);
				}
			}
		};
		
		addUndoRedoAction(executeRunnable, undoRunnable, executeRunnable);
	}
	
	/**
	 * puts the given element at a upper place in the tree
	 * @param jWidgetElement a jwidget element node
	 * @param refreshElementRepresentation whether the representation image of the jwidget element should
	 * 																	be refreshed
	 */
	public void putUp(final Element jWidgetElement, final boolean refreshElementRepresentation) {

		final SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		final Element parentElement=(Element)jWidgetElement.getParentNode();
		final Element previousElement=getPreviousElement(jWidgetElement);
		final Element topMostJWidgetElement=getTopMostJWidgetElement(jWidgetElement);

		if(previousElement!=null) {
			
			//getting the next element
			final Element nextElement=getNextElement(jWidgetElement);

			//the execute runnable
			Runnable executeRunnable=new Runnable(){
				
				public void run() {
					
					//inserts the element before its previous sibling
					parentElement.insertBefore(jWidgetElement, previousElement);
					handle.getSvgDOMListenerManager().
						fireStructureChanged(parentElement, jWidgetElement);
					
					//refreshing the image representation of the property
					if(refreshElementRepresentation) {
						
						jwidgetManager.refreshElementRepresentation(topMostJWidgetElement);
					}
				}
			};
			
			//the undo/redo runnables
			Runnable undoRunnable=new Runnable(){

				public void run() {

					if(nextElement!=null) {
						
						parentElement.insertBefore(jWidgetElement, nextElement);
						
					}else {
						
						parentElement.appendChild(jWidgetElement);
					}
					
					handle.getSvgDOMListenerManager().
						fireStructureChanged(parentElement, jWidgetElement);
					
					//refreshing the image representation of the property
					if(refreshElementRepresentation) {
						
						jwidgetManager.refreshElementRepresentation(topMostJWidgetElement);
					}
				}
			};
			
			addUndoRedoAction(executeRunnable, undoRunnable, executeRunnable);
		}
	}
	
	/**
	 * puts the given element at a lower place in the tree
	 * @param jWidgetElement a jwidget element node
	 * @param refreshElementRepresentation whether the representation image of the jwidget element should
	 * 																	be refreshed
	 */
	public void putDown(final Element jWidgetElement, final boolean refreshElementRepresentation) {

		final SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		final Element parentElement=(Element)jWidgetElement.getParentNode();
		final Element nextElement=getNextElement(jWidgetElement);
		final Element topMostJWidgetElement=getTopMostJWidgetElement(jWidgetElement);
		
		if(nextElement!=null) {
			
			//getting the next sibling of the previously computed sibling
			final Element secondNextSibling=getNextElement(nextElement);
			
			//the execute runnable
			Runnable executeRunnable=new Runnable(){
				
				public void run() {
				
					//executing the operation
					if(secondNextSibling!=null) {
						
						parentElement.insertBefore(jWidgetElement, secondNextSibling);
						
					}else {
						
						parentElement.appendChild(jWidgetElement);
					}
					
					handle.getSvgDOMListenerManager().
						fireStructureChanged(parentElement, jWidgetElement);
					
					//refreshing the image representation of the property
					if(refreshElementRepresentation) {
						
						jwidgetManager.refreshElementRepresentation(topMostJWidgetElement);
					}
				}
			};

			//the undo/redo runnables
			Runnable undoRunnable=new Runnable(){

				public void run() {

					parentElement.insertBefore(jWidgetElement, nextElement);
					handle.getSvgDOMListenerManager().
						fireStructureChanged(parentElement, jWidgetElement);
					
					//refreshing the image representation of the property
					if(refreshElementRepresentation) {
						
						jwidgetManager.refreshElementRepresentation(topMostJWidgetElement);
					}
				}
			};
			
			addUndoRedoAction(executeRunnable, undoRunnable, executeRunnable);
		}
	}
	
	/**
	 * returns the element before the given node
	 * @param element the element before this element
	 * @return the previous element
	 */
	protected Element getPreviousElement(Element element) {
		
		Element previousElement=null;
		
		for(Node node=element.getPreviousSibling(); node!=null; node=node.getPreviousSibling()) {
			
			if(node instanceof Element) {
				
				previousElement=(Element)node;
				break;
			}
		}
		
		return previousElement;
	}
	
	/**
	 * returns the element after the given node
	 * @param element the element after this element
	 * @return the next element
	 */
	protected Element getNextElement(Element element) {
		
		Element nextElement=null;

		for(Node node=element.getNextSibling(); node!=null; node=node.getNextSibling()) {
			
			if(node instanceof Element) {
				
				nextElement=(Element)node;
				break;
			}
		}
		
		return nextElement;
	}
	
	/**
	 * returns the top most jwidget element that is a parent of the given jwidget parent element
	 * @param element a jwidget element
	 * @return the top most jwidget element that is a parent of the given jwidget parent element
	 */
	protected Element getTopMostJWidgetElement(Element element) {
		
		Element parentJWidgetElement=null;

		if(element!=null) {
			
			Node parentNode=element;
			
			while(parentNode.getParentNode()!=null && 
						parentNode.getParentNode().getNodeName().equals(Toolkit.jwidgetElementName)) {
				
				parentNode=parentNode.getParentNode();
			}
			
			parentJWidgetElement=(Element)parentNode;
		}

		return parentJWidgetElement;
	}
	
	/**
	 * adds an undo/redo action
	 * @param executeRunnable the execute runnable
	 * @param undoRunnable the undo runnable
	 * @param redoRunnable the redo runnable
	 */
	protected void addUndoRedoAction(
			Runnable executeRunnable, Runnable undoRunnable, Runnable redoRunnable) {
		
		//getting the current handle
		final SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		//create the undo/redo action and insert it into the undo/redo stack
		UndoRedoAction action=new UndoRedoAction(
				undoRedoModificationLabel, executeRunnable, undoRunnable, 
					redoRunnable, new HashSet<Element>());

		UndoRedoActionList actionlist=new UndoRedoActionList(action.getName(), false);
		actionlist.add(action);
		handle.getUndoRedo().addActionList(actionlist, true);
	}

	/**
	 * @return the animationsAndActionsDocument
	 */
	public Document getAnimationsAndActionsDocument() {
		
		return animationsAndActionsDocument;
	}

	/**
	 * @return whether the xml document defines actions
	 */
	public boolean defineActions() {
		return defineActions;
	}

	/**
	 * @return whether the xml document defines animations
	 */
	public boolean defineAnimations() {
		return defineAnimations;
	}

	/**
	 * cleans this object from its unused listeners
	 */
	public void clean() {
		
		if(configurationPanel!=null) {
			
			configurationPanel.clean();
		}
		
		if(animationsSourceChooser!=null) {
			
			animationsSourceChooser.clean();
		}
		
		if(actionsSourceChooser!=null) {
			
			actionsSourceChooser.clean();
		}
	}

	/**
	 * computes and returns the animations and actions document
	 * @param baseClass the base class from which the xml document should be loaded
	 * @param name a name
	 * @return the animations and actions document
	 */
	protected Document getAnimationsAndActionsDocument(Class<?> baseClass, String name) {
		
		Document doc=null;
        DocumentBuilderFactory docBuildFactory=DocumentBuilderFactory.newInstance();
        
        String path=baseClass.getResource("xml/"+name).toExternalForm();
        
        try{
            //parses the XML file
            DocumentBuilder docBuild=docBuildFactory.newDocumentBuilder();
            doc=docBuild.parse(path);
        }catch (Exception ex){}

		return doc;
	}

	/**
	 * @return whether the jwidget contains inner components
	 */
	public boolean containsInnerComponents() {
		
		return containsInnerComponents;
	}
	
	/**
	 * refreshes the source choosers
	 */
	public void refreshSourceChoosers() {
		
		if(actionsSourceChooser!=null) {
			
			actionsSourceChooser.updateWidgets();
		}
		
		if(animationsSourceChooser!=null) {
			
			animationsSourceChooser.updateWidgets();
		}
	}

	/**
	 * @param isAnimations whether the animations source chooser is required or not
	 * @return the source chooser
	 */
	public AnimationChooserJWidgetSourceChooser getSourceChooser(boolean isAnimations) {
		
		if(isAnimations) {
			
			return animationsSourceChooser;
		}
		
		return actionsSourceChooser;
	}
	
	/**
	 * returns an array of two icons : the first one is the regular icon, the second is the grayed one
	 * @param baseClass the base class from which the icon should be loaded
	 * @param name a name
	 * @return an array of two icons : the first one is the regular icon, the second is the grayed one
	 */
	protected static ImageIcon[] getIcons(Class<?> baseClass, String name) {

		ImageIcon[] imageIcons=new ImageIcon[2];
		
        if(name!=null && ! name.equals("")){

        	InputStream in=baseClass.getResourceAsStream("icons/"+name+".png");
        	ByteArrayOutputStream out=new ByteArrayOutputStream();
        	byte[] data=new byte[256];
        	int pos=-1;
        	
        	try {
            	while(in.available()>0) {
            		
            		pos=in.read(data);
            		
            		if(pos!=-1) {
            			
            			out.write(data, 0, pos);
            		}
            	}
            	
            	data=out.toByteArray();
            	imageIcons[0]=new ImageIcon(data);
        	}catch (Exception ex) {ex.printStackTrace();}

            if(imageIcons[0]!=null){
            	
            	imageIcons[0]=new ImageIcon(imageIcons[0].getImage());
            	imageIcons[1]=new ImageIcon(GrayFilter.createDisabledImage(imageIcons[0].getImage()));
            	
            }else {
            	
            	imageIcons[1]=null;
            }
        }
		
		return imageIcons;
	}
	
}
