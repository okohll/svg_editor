package fr.itris.glips.rtdaeditor.jwidget;

import java.awt.*;
import java.awt.geom.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.shape.RectangularShape;

/**
 * the jwidget manager
 * @author ITRIS, Jordi SUC
 */
public class JWidgetManager extends RectangularShape{
	
	/**
	 * the set of the widget objects
	 */
	private Set<JWidgetEdition> widgets=new HashSet<JWidgetEdition>();
	
	/**
	 * the tools manager
	 */
	private JWidgetToolsManager toolsManager=new JWidgetToolsManager(this);
	
	/**
	 * the menu items manager
	 */
	private JWidgetMenuItemsManager itemsManager=new JWidgetMenuItemsManager(this);
	
	/**
	 * the current jwidget edition
	 */
	private JWidgetEdition currentJWidgetEdition;
	
	/**
	 * the thread used to refresh the representation image of each jwidget element
	 */
	private JWidgetRepresentationRefreshThread refreshThread=
		new JWidgetRepresentationRefreshThread(this);
	
	/**
	 * the constructor
	 * @param editor the editor
	 */
	public JWidgetManager(Editor editor) {
		
		super(editor);
		
		//loading the jwidgets edition handlers
		loadJWidgets();
		
		shapeModuleId="JWidgetManager";
		handledElementTagName="image";
	}
	
	@Override
	public void initialize() {
		
		//adding the jwidget edition object to the tools and menu items managers
		toolsManager.addJWidgetEditionObjects(widgets);
		itemsManager.addJWidgetEditionObjects(widgets);
	}
	
	@Override
	public boolean isElementTypeSupported(Element element) {
		
		return element.getNodeName().equals(handledElementTagName) && 
			Toolkit.hasJWidgetChildElement(element);
	}
	
	@Override
	public boolean isPrioritary() {

		return true;
	}
	
	@Override
	public int getLevelCount() {
		
		return 0;
	}
	
	@Override
	public void refresh(Element element) {

		//getting the jwidget element corresponding to the element
		final Element jwidgetElement=getJWidgetElement(element);
		
		//getting the jwidget edition object
		JWidgetEdition jwidgetEdition=getJWidgetEdition(
				jwidgetElement.getAttribute(Toolkit.nameAttribute));
		
		if(jwidgetEdition!=null){
			
			//getting the current handle
			SVGHandle handle=
				Editor.getEditor().getHandlesManager().getCurrentHandle();
			
			jwidgetEdition.handleRepresentationImage(
					handle, jwidgetElement, (Element)jwidgetElement.getParentNode(), null);
		}
	}

	@Override
	protected void createMenuAndToolItems() {}
	
	/**
	 * @return the tools manager
	 */
	public JWidgetToolsManager getToolsManager() {
		return toolsManager;
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> map=new HashMap<String, JMenuItem>();
		map.put(shapeModuleId, itemsManager.getMenu());
		
		return map;
	}
	
	@Override
	public HashMap<String, AbstractButton> getToolItems() {
		
		HashMap<String, AbstractButton> map=new HashMap<String, AbstractButton>();
		map.put(shapeModuleId, toolsManager.getToolButton());
		
		return map;
	}
	
	@Override
	public Element createElement(SVGHandle handle, Rectangle2D bounds){
		
		if(currentJWidgetEdition!=null){
			
			//the edited document
			Document doc=handle.getScrollPane().getSVGCanvas().getDocument();
			
			//normalizing the bounds of the element
			if(bounds.getWidth()<1){
			    
			    bounds.setRect(bounds.getX(), bounds.getY(), 1, bounds.getHeight());
			}
			
			if(bounds.getHeight()<1){
			    
				bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(), 1);
			}
			
			// creating the rectangle
			final Element imageJWidgetElement=doc.createElementNS(
					doc.getDocumentElement().getNamespaceURI(), handledElementTagName);
			
			imageJWidgetElement.setAttributeNS(null, xAtt, FormatStore.format(bounds.getX()));
			imageJWidgetElement.setAttributeNS(null, yAtt, FormatStore.format(bounds.getY()));
			imageJWidgetElement.setAttributeNS(null, wAtt, FormatStore.format(bounds.getWidth()));
			imageJWidgetElement.setAttributeNS(null, hAtt, FormatStore.format(bounds.getHeight()));
			imageJWidgetElement.setAttributeNS(null, "preserveAspectRatio", "none meet");
			imageJWidgetElement.setAttributeNS(
					EditorToolkit.xmlnsXLinkNS, EditorToolkit.xLinkprefix+"actuate", "onRequest");
			
			//creating the jwidget element for this image element
			Element jwidgetElement=currentJWidgetEdition.createJWidgetElement(imageJWidgetElement);
			
			//sets the image corresponding to the graphical component
			currentJWidgetEdition.handleRepresentationImage(
					handle, jwidgetElement, imageJWidgetElement, 
						new Dimension((int)(bounds.getWidth()==0?1:bounds.getWidth()), 
							(int)(bounds.getHeight()==0?1:bounds.getHeight())));
			
			//inserting the element in the document and handling the undo/redo support
			insertShapeElement(handle, imageJWidgetElement);
			
			return imageJWidgetElement;
		}

		return null;
	}

	/**
	 * triggers the edition of a jwidget
	 * @param jwidgetEdition a jwidget edition object
	 */
	public void handleCurrentJWidgetEditionObject(JWidgetEdition jwidgetEdition) {
		
		this.currentJWidgetEdition=jwidgetEdition;
		notifyDrawingMode();
	}
	
	/**
	 * loads the jwidgets edition handlers
	 */
	protected void loadJWidgets() {
		
		Frame mainFrame=null;
		
		if(Editor.getParent() instanceof Frame) {
			
			mainFrame=(Frame)Editor.getParent();
			
		}else {
			
			mainFrame=new JFrame();
		}
		
		//getting the class of the jwidget runtime managers
		Map<String, Class<?>> jwidgetRuntimeClasses=
			JWidgetRuntimeManager.getJWidgetRuntimeClassesMap();
		
		//getting the class of the jwidget edition managers
		LinkedList<Class<?>> editionPlugins=new LinkedList<Class<?>>();
		
		//filling the map of the jwidget runtime classes
		Method method=null;
		Class<?> editionClass=null;
		
		for(Class<?> theClass : jwidgetRuntimeClasses.values()){
			
			if(theClass!=null){
				
				try{
					method=theClass.getMethod("getEditionClass", (Class<?>[])null);
					
					if(method!=null){
						
						editionClass=(Class<?>)method.invoke(null, (Object[])null);
					}
				}catch (Exception ex){editionClass=null;}
			
				if(editionClass!=null){
					
					editionPlugins.add(editionClass);
				}
			}
		}

    	if(editionPlugins.size()>0) {
 
            try{
    			//specifying the parameters for the constructor 
    			Class<?>[] parametersType={getClass(), Frame.class};
    			Object[] parameters={this, mainFrame};
    			
    			//creating the jwidget elements
    			Constructor<?> constructor=null;
    			Object obj=null;
    			
    			for(Class<?> pluginClass : editionPlugins) {
    				
        			//the constructor
        			constructor=pluginClass.getConstructor(parametersType);
        			
        			if(constructor!=null){

        				//creating an instance of the class
        				obj=constructor.newInstance(parameters);

        				if(obj!=null){
        					
        					widgets.add((JWidgetEdition)obj);
        				}
        			}
    			}
            }catch(Exception ex){ex.printStackTrace();}
    	}
	}
	
	/**
	 * @return the widgets
	 */
	public Set<JWidgetEdition> getJWidgets() {
		
		return widgets;
	}
	
	/**
	 * returns the jwidget element corresponding to the given element
	 * @param element an element
	 * @return the jwidget element corresponding to the given element
	 */
	public static Element getJWidgetElement(Element element) {
		
		Element jwidgetElement=null;
		
		if(element!=null) {

			for(Node node=element.getFirstChild(); node!=null; node=node.getNextSibling()) {
				
				if(node instanceof Element && 
						node.getNodeName().equals(Toolkit.jwidgetElementName)) {
					
					jwidgetElement=(Element)node;
					break;
				}
			}
		}
		
		return jwidgetElement;
	}
	
	/**
	 * returns the widget edition object corresponding to the given id
	 * @param jwidgetId an id
	 * @return the widget edition object corresponding to the given id
	 */
	public JWidgetEdition getJWidgetEdition(String jwidgetId) {
		
		JWidgetEdition jwidgetEdition=null;
		
		if(jwidgetId!=null && ! jwidgetId.equals("")) {
			
			for(JWidgetEdition jwe : widgets) {

				if(jwe.getId().equals(jwidgetId)) {
					
					jwidgetEdition=jwe;
					break;
				}
			}
		}
		
		return jwidgetEdition;
	}
	
	/**
	 * refreshes the representation image of the given jwidget element
	 * @param jwidgetElement a jwidget element
	 */
	public void refreshElementRepresentation(Element jwidgetElement) {
		
		if(jwidgetElement!=null) {
			
			refreshThread.addElement(jwidgetElement);
		}
	}
}
