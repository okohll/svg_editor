/*
 * Created on 23 mars 2004 
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
package fr.itris.glips.svgeditor;

import fr.itris.glips.svgeditor.options.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.selection.*;
import fr.itris.glips.svgeditor.shape.*;
import fr.itris.glips.svgeditor.actions.clipboard.*;
import fr.itris.glips.svgeditor.actions.menubar.*;
import fr.itris.glips.svgeditor.actions.popup.*;
import fr.itris.glips.svgeditor.actions.toolbar.*;
import fr.itris.glips.svgeditor.colorchooser.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import javax.swing.*;
import com.jgoodies.looks.plastic.*;
import com.jgoodies.looks.windows.*;
import org.w3c.dom.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.dnd.*;
import java.net.*;

/**
 * The main class of the editor
 * 
 * @author ITRIS, Jordi SUC
 */
public class Editor {
	
	/**
	 * whether the editor handles the rtda animations
	 */
	public static boolean isRtdaAnimationsVersion=false;
	
	/**
	 * the parent container
	 */
	private static Container parentContainer;
	
	/**
	 * the desktop pane
	 */
	private JDesktopPane desktop;
	
	/**
	 * the svg handles manager
	 */
	private HandlesManager svgHandlesManager;
	
	/**
	 * the module loader
	 */
	private static ModuleManager moduleManager;
	
	/**
	 * the square mode manager
	 */
	private SquareModeManager squareModeManager;
	
	/**
	 * the remanent mode manager
	 */
	private RemanentModeManager remanentModeManager;
	
	/**
	 * the close path mode manager
	 */
	private ClosePathModeManager closePathModeManager;
	
	/**
	 * the constraint line mode manager
	 */
	private ConstraintLinesModeManager constraintLinesModeManager;
	
	/**
	 * the new clipboard manager
	 */
	private ClipboardManager clipboardManager;
	
	/**
	 * the io manager
	 */
	private IOManager ioManager;
	
	/**
	 * the class that gives the resources
	 */
	private ResourcesManager resourcesManager;
	
	/**
	 * the toolkit object
	 */
	private EditorToolkit toolkit;
	
	/**
	 * tells whether the mutli windowing method has to be used or not
	 */
	private boolean isMultiWindow=false;
	
	/**
	 * the map associating a name to a rectangle representing bounds
	 */
	private final Map<String, Rectangle> widgetBounds=new HashMap<String, Rectangle>();
	
	/**
	 * the svg selection manager
	 */
	private fr.itris.glips.svgeditor.selection.SelectionInfoManager selectionManager;
	
	/**
	 * the drag source
	 */
	private DragSource dragSource=DragSource.getDefaultDragSource();
    
	/**
	 * the color chooser of the editor
	 */
	private static ColorChooser colorChooser;
    
    /**
     * whether the quit action is disabled
     */
    private boolean isQuitActionDisabled=false;
    
    /**
     * whether the JVM will be exited when the user requires to exit from the editor
     */
    private boolean canExitFromJVM=false;
    
    /**
     * the set of the runnables that should be run when the editor is exiting
     */
    private HashSet<Runnable> disposeRunnables=new HashSet<Runnable>();
    
    /**
     * the map of the name spaces that should be checked
     */
    public static HashMap<String, String> requiredNameSpaces=new HashMap<String, String>();
	
	/**
	 * the svg editor
	 */
	private static Editor editor;
	
	/**
	 * The constructor of the class
	 */
	public Editor(){

		try {
			//PlasticXPLookAndFeel laf=new PlasticXPLookAndFeel();
			//PlasticXPLookAndFeel.set3DEnabled(true);
			WindowsLookAndFeel laf = new WindowsLookAndFeel();
			UIManager.setLookAndFeel(laf);
		 } catch (Exception e) {}
		 
		 editor=this;
	}
	
	/**
	 * initializing the editor
	 * @param parent the parent container for the application
	 * @param fileToBeLoaded the file to be directly loaded
     * @param showSplash whether the splash screen should be shown or not
     * @param displayFrame whether or not to show the frame 
     * @param quitDisabled whether the quit action is disabled
     * @param exitFromJVM whether the JVM will be exited when the user requires to exit from the editor
     * @param disposeRunnable the runnable that should be run when exiting the editor
	 */
	public void init(Container parent, String fileToBeLoaded, 
		boolean showSplash, final boolean displayFrame, 
			boolean quitDisabled, boolean exitFromJVM, Runnable disposeRunnable) {

		if(parent instanceof JApplet){
			
			parentContainer=SwingUtilities.getAncestorOfClass(Frame.class, parent);
			
		}else{
			
			parentContainer=parent;
		}
		
        this.isQuitActionDisabled=quitDisabled;
        this.canExitFromJVM=exitFromJVM;
        
        if(disposeRunnable!=null){
        	
    		this.disposeRunnables.add(disposeRunnable);
        }

		//setting the values for the tooltip manager
		ToolTipManager.sharedInstance().setInitialDelay(100);
		ToolTipManager.sharedInstance().setDismissDelay(10000);
		ToolTipManager.sharedInstance().setReshowDelay(100);

		//creating the toolkit object
		toolkit=new EditorToolkit();
		
		//creating the managers
		resourcesManager=new ResourcesManager(this);
		ioManager=new IOManager();
		colorChooser=new ColorChooser(this);
		svgHandlesManager=new HandlesManager(this);
		clipboardManager=new ClipboardManager();
		squareModeManager=new SquareModeManager();
		remanentModeManager=new RemanentModeManager();
		closePathModeManager=new ClosePathModeManager();
		constraintLinesModeManager=new ConstraintLinesModeManager();

		//getting the configuration values
		
		//the bounds of the widgets contained in the main frame
		Map<String, Rectangle> map=getWidgetBoundsMap();
		
		if(map!=null){
			
			widgetBounds.putAll(map);
		}
		
		//creating the desktop
		desktop=new JDesktopPane();
        desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);

		//creating the selection manager
		selectionManager=new fr.itris.glips.svgeditor.selection.SelectionInfoManager();
		
		//the module loader is created and initialized
		moduleManager=new ModuleManager(this);
		moduleManager.init();

		if(parent instanceof JFrame){
			
			JFrame parentFrame=(JFrame)parent;
			
			//setting the icon
			ImageIcon icon2=ResourcesManager.getIcon("Editor", false);
			
			if(icon2!=null && icon2.getImage()!=null){
				
				parentFrame.setIconImage(icon2.getImage());
			}
			
			//handling the frame content
			parentFrame.getContentPane().setLayout(new BorderLayout());
			parentFrame.getContentPane().add(
					moduleManager.getToolBarManager().getToolsBar(), BorderLayout.NORTH);
			parentFrame.getContentPane().add(desktop, BorderLayout.CENTER);
			parentFrame.setJMenuBar(moduleManager.getMenuBar());
			
			//computing the bounds of the main frame
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			Insets screenInsets=Toolkit.getDefaultToolkit().getScreenInsets(
				parentFrame.getGraphicsConfiguration());
			final Rectangle frameBounds=new Rectangle(
					screenInsets.left, screenInsets.top, 
						screenSize.width-screenInsets.left-screenInsets.right, 
							screenSize.height-screenInsets.top-screenInsets.bottom);
			widgetBounds.put("mainframe", frameBounds);
			
			//sets the bounds of the main frame
			parentFrame.setBounds(frameBounds);
			desktop.setBounds(frameBounds);
			
		}else if(parent instanceof JApplet){

			JApplet applet=(JApplet)parent;
			
			//handling the applet content
			applet.getContentPane().setLayout(
					new BoxLayout(applet.getContentPane(), BoxLayout.Y_AXIS));
			applet.getContentPane().add(
					moduleManager.getToolBarManager().getToolsBar(), BorderLayout.NORTH);
			applet.getContentPane().add(desktop, BorderLayout.CENTER);
			applet.setJMenuBar(moduleManager.getMenuBar());
			applet.validate();
		}

		//layout some elements inside the frame
		moduleManager.initializeParts();
		
		//displays the main frame
		if(parentContainer instanceof JFrame){
			
			((JFrame)parentContainer).setVisible(displayFrame);
		}
		
        //opening the file specified in the constructor arguments
		File initialFile=null;
		
		if(fileToBeLoaded!=null && ! fileToBeLoaded.equals("") && 
			(fileToBeLoaded.endsWith(EditorToolkit.SVG_FILE_EXTENSION) ||
				fileToBeLoaded.endsWith(EditorToolkit.SVGZ_FILE_EXTENSION))){
			
			//computing the file that should be opened
			try{
				initialFile=new File(new URI(fileToBeLoaded));
			}catch (Exception ex){}
		}

		if(initialFile!=null && initialFile.exists()){
		
			 ioManager.getFileOpenManager().open(initialFile, null);
		}
	}

	/**
	 * exits the editor
	 */
	public void exit(){
		
		ioManager.getEditorExitManager().exit();
	}
	
	/**
	 * @return the svg handles manager
	 */
	public HandlesManager getHandlesManager(){
		return  svgHandlesManager;
	}
	
	/**
	 * @return the main frame
	 */
	public static Container getParent(){
		return parentContainer;
	}
    
    /**
     * shows or hides the editor frame
     * @param visible whether the editor frame should be visible or not
     */
    public void setVisible(final boolean visible) {
        
    	if(parentContainer instanceof JFrame){
    		
            ((JFrame)parentContainer).setVisible(visible);
            
            if(visible){
            	
                ((JFrame)parentContainer).toFront();
                
            }else{
            	
                ((JFrame)parentContainer).toBack();
            }
    	}
    }

	/**
     * @return whether the user can exit the jvm
     */
    public boolean isQuitActionDisabled() {
        return isQuitActionDisabled;
    }

    /**
	 * @return whether the mutli windowing method has to be used or not
	 */
	public boolean isMultiWindow(){
		return isMultiWindow;
	}
	
	/**
	 * @return Returns the dragSource.
	 */
	public DragSource getDragSource() {
		return dragSource;
	}
	
	/**
	 * @return Returns the colorChooser.
	 */
	public static ColorChooser getColorChooser() {
		return colorChooser;
	}
	
	/**
	 * sets the new color chooser
	 * @param newColorChooser The colorChooser to set.
	 */
	public static void setColorChooser(ColorChooser newColorChooser) {
		
		colorChooser=newColorChooser;
	}
	
	/**
	 * @return the map containing the bounds of each widget in the main frame
	 */
	protected Map<String, Rectangle> getWidgetBoundsMap(){
		
		Map<String, Rectangle> map=new HashMap<String, Rectangle>();
		Document doc=ResourcesManager.getXMLDocument("config.xml");
		
		if(doc!=null){
			
			Element root=doc.getDocumentElement();
			
			if(root!=null){
				
				//getting the node containing the nodes giving the bounds of the widgets in the main frame
				Node cur=null, bounds=null;
				
				for(cur=root.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
					
					if(cur instanceof Element && cur.getNodeName().equals("bounds")){
						
						bounds=cur;
						break;
					}
				}
				
				if(bounds!=null){
					
					//filling the map with the bounds
					Rectangle rectBounds=null;
					int x=0, y=0, width=0, height=0;
					String name, strX, strY, strW, strH;
					Element el=null;
					
					for(cur=bounds.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
						
						if(cur instanceof Element && cur.getNodeName().equals("widget")){
							
							el=(Element)cur;
							
							//the name of the widget
							name=el.getAttribute("name");
							
							//getting each value of the bounds
							strX=el.getAttribute("x");
							strY=el.getAttribute("y");
							strW=el.getAttribute("width");
							strH=el.getAttribute("height");
							
							x=0;
							y=0;
							width=0;
							height=0;
							
							try{
								x=Integer.parseInt(strX);
								y=Integer.parseInt(strY);
								width=Integer.parseInt(strW);
								height=Integer.parseInt(strH);
							}catch (Exception ex){}
							
							//creating the rectangle
							rectBounds=new Rectangle(x, y, width, height);
							
							//putting the bounds in the map
							if(name!=null && ! name.equals("")){
								
								map.put(name, rectBounds);
							}
						}
					}
				}
			}
		}
		
		return map;
	}
	
	/**
	 * @param name the name of a widget
	 * @return the preferred bounds of a widget
	 */
	public Rectangle getPreferredWidgetBounds(String name){
		
		Rectangle rect=null;
		
		if(name!=null && ! name.equals("")){
			
			try{
				rect=widgetBounds.get(name);
			}catch (Exception ex){}
		}
		
		return rect;
	}
	
	/**
	 * @return the component into which 
	 * all internal frames will be placed
	 */
	public JDesktopPane getDesktop(){
		return desktop;
	}
	
	/**
	 * @return the menubar
	 */
	public EditorMenuBar getMenuBar(){
		return moduleManager.getMenuBar();
	}
	
	/**
	 * @return the tool bar manager
	 */
	public ToolBarManager getToolBarManager(){
		
		return moduleManager.getToolBarManager();
	}
	
	/**
	 * @return the popup manager
	 */
	public PopupManager getPopupManager() {
		return moduleManager.getPopupManager();
	}
	
	/**
	 * @return Returns the resourceImageManager.
	 */
	public ResourceImageManager getResourceImageManager() {
		return moduleManager.getResourceImageManager();
	}
	
	/**
	 * @return the clip board manager
	 */
	public ClipboardManager getClipboardManager() {
		return clipboardManager;
	}
	
	/**
	 * @return the module loader
	 */
	public ModuleManager getSVGModuleLoader(){
		return moduleManager;
	}
	
	/**
	 * @return the toolkit object containing utility methods
	 */
	public EditorToolkit getSVGToolkit(){
		return toolkit;
	}
	
	/**
	 * @return the selection manager
	 */
	public SelectionInfoManager getSelectionManager() {
		return selectionManager;
	}
	
	/**
	 * @return the painter manager
	 */
	public static ColorManager getSVGColorManager(){
		return moduleManager.getColorManager();
	}
	
	/**
	 * @return an object of the class managing the resources
	 */
	public ResourcesManager getResourcesManager(){
		return resourcesManager;
	}
	
	/**
	 * @return the collection of the shape modules
	 */
	public Set<AbstractShape> getShapeModules(){
		
		return moduleManager.getShapeModules();
	}
	
	/**
	 * @param name the name of the module
	 * @return the module object
	 */
	public Object getModule(String name){
		return moduleManager.getModule(name);
	}
	
	/**
	 * returns a shape module given its id
	 * @param moduleId the id of a module
	 * @return a shape module given its id
	 */
	public AbstractShape getShapeModule(String moduleId){
		
		return moduleManager.getShapeModule(moduleId);
	}

     /**
	 * @return the square mode manager
	 */
	public SquareModeManager getSquareModeManager() {
		return squareModeManager;
	}
	
	/**
	 * @return the remanent mode manager
	 */
	public RemanentModeManager getRemanentModeManager() {
		return remanentModeManager;
	}
	
	/**
	 * @return the close path manager
	 */
	public ClosePathModeManager getClosePathModeManager() {
		return closePathModeManager;
	}
	
	/**
	 * @return the constraint line manager
	 */
	public ConstraintLinesModeManager getConstraintLinesModeManager() {
		return constraintLinesModeManager;
	}
	
	/**
	 * @return the IO manager
	 */
	public IOManager getIOManager() {
		return ioManager;
	}
	
	/**
	 * @return whether the JVM will be exited when 
	 * the user requires to exit from the editor
	 */
	public boolean canExitFromJVM() {
		return canExitFromJVM;
	}

	/**
	 * disposes the editor
	 */
	public void dispose(){
		
    	//running the dispose runnables
    	for(Runnable run : disposeRunnables){
    		
    		run.run();
    	}
	}
    
    /**
     * @return the svg editor
     */
    public static Editor getEditor() {
    	
    	return editor;
    }

}
