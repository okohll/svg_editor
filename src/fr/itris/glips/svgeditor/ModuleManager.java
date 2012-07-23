/*
 * Created on 7 juin 2004
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

import fr.itris.glips.svgeditor.shape.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.actions.menubar.*;
import fr.itris.glips.svgeditor.actions.popup.*;
import fr.itris.glips.svgeditor.actions.toolbar.*;
import org.w3c.dom.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author ITRIS, Jordi SUC
 * the class loading the modules 
 */
public class ModuleManager {
	
	/**
	 * the editor
	 */
	private Editor editor;
	
	/**
	 * the menu bar
	 */
	private EditorMenuBar menubar;
	
	/**
	 * the popup manager
	 */
	private PopupManager popupManager;
	
	/**
	 * the color manager
	 */
	private ColorManager colorManager;
	
	/**
	 * the toolBar manager
	 */
	private ToolBarManager toolBarManager;
	
	/**
	 * the list of the modules
	 */
	private LinkedList<Module> modules=new LinkedList<Module>();
	
	/**
	 * the list of the shape modules
	 */
	private Set<fr.itris.glips.svgeditor.shape.AbstractShape> shapeModules=
			new HashSet<fr.itris.glips.svgeditor.shape.AbstractShape>();
	
	/**
	 * the list of the classes of the modules
	 */
	private LinkedList<String> moduleClasses=new LinkedList<String>();
	
	/**
	 * the resource image manager
	 */
	private ResourceImageManager resourceImageManager;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public ModuleManager(Editor editor) {
	    
		this.editor=editor;
	}
	
	/**
	 * initializes the object
	 */
	public void init(){
		
		//the menu bar
		menubar=new EditorMenuBar(editor);
		
		//the color manager
		colorManager=new ColorManager(editor);
		
		//the resource image manager 
		resourceImageManager=new ResourceImageManager(editor);
		
		//gets the module's classes
		parseXMLModules();
		
		//creates the static modules
		createModuleObjects();
		
		//the popup menu manager
		popupManager=new PopupManager(editor);
		
		//the toolBar manager
		toolBarManager=new ToolBarManager();
		
		//initializes the menu bar
		menubar.init();
	}
	
	/**
	 * initializes some parts
	 */
	public void initializeParts(){

		Collection<Module> mds=getModules();

		for(Module module : mds){

			if(module!=null){
			    
				module.initialize();
			}
		}
		
		toolBarManager.initializeParts();
		editor.getHandlesManager().initializeParts();
	}

	/**
	 * parses the XML document to get the modules
	 */
	protected void parseXMLModules(){
		
		Document doc=null;
		doc=ResourcesManager.getXMLDocument("modules.xml");	
		if(doc==null)return;
		
		Element root=doc.getDocumentElement();
		Node current=null;
		NamedNodeMap attributes=null;
		String name=null, sclass=null;
		
		for(NodeIterator it=new NodeIterator(root); it.hasNext();){
		    
			current=it.next();
			
			if(current!=null){	
			    
				name=current.getNodeName();
				attributes=current.getAttributes();	
				
				if(name!=null && name.equals("module") && attributes!=null){
				    
					//adds the string representing a class in the list linked with static items
					sclass=attributes.getNamedItem("class").getNodeValue();
					
					if(sclass!=null && ! sclass.equals("")){
					    
					    moduleClasses.add(sclass);
					}
				}
			}
		}
	}
	
	/**
	 * creates the objects corresponding to the modules
	 */
	protected void createModuleObjects(){

		Object obj=null;
		
		for(String current : moduleClasses){
			
			if(current!=null && ! current.equals("")){
			    
				try{
					Class<?>[] classargs={Editor.class};
					Object[] args={editor};
					
					//creates instances of each static module
					System.out.println("About to instantiate " + current + ", classargs " + classargs + ", " + " args " + args);
					obj=Class.forName(current).getConstructor(classargs).newInstance(args);
					
					//if it is a shape module, it is added to the list of the shape module				
					if(obj instanceof AbstractShape) {
						
						shapeModules.add((AbstractShape)obj);
					}
					
					modules.add((Module)obj);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * gets the module given its name
	 * @param name the module's name
	 * @return a module
	 */
	public Object getModule(String name){

		String cname=null;
		
		for(Module module : modules){
			
			try{
				cname=(String)module.getClass().getMethod(
					"getName", (Class[])null).invoke(module, (Object[])null);
			}catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				e.printStackTrace();
				cname=null;
			}
			
			if(cname!=null && cname.equals(name)){
			    
			    return module;
			}
		}
		return null;
	}
	
	/**
	 * @return the collection of the objects corresponding to the modules
	 */
	public Collection<Module> getModules(){
		return modules;
	}
	
	/**
	 * returns a shape module given its id
	 * @param moduleId the id of a module
	 * @return a shape module given its id
	 */
	public AbstractShape getShapeModule(String moduleId){
		
		for(AbstractShape shape : shapeModules){
			
			if(shape.getId().equals(moduleId)){
				
				return shape;
			}
		}
		
		return null;
	}
	
	/**
	 * @return the collection of the objects corresponding to the shape modules
	 */
	public Set<AbstractShape> getShapeModules(){
		return shapeModules;
	}

    /**
     * @return Returns the color manager.
     */
    public ColorManager getColorManager() {
        return colorManager;
    }
    
	/**
	 * @return the menubar
	 */
	public EditorMenuBar getMenuBar(){
		return menubar;
	}
	
	/**
	 * @return the tool bar manager
	 */
	public ToolBarManager getToolBarManager(){
		
		return toolBarManager;
	}
	
	/**
	 * @return the popup manager
	 */
	public PopupManager getPopupManager() {
		return popupManager;
	}
	
	/**
	 * @return Returns the resourceImageManager.
	 */
	protected ResourceImageManager getResourceImageManager() {
		return resourceImageManager;
	}

}
