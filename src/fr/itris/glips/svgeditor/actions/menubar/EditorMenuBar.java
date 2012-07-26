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
package fr.itris.glips.svgeditor.actions.menubar;

import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;
import javax.swing.*;
import org.w3c.dom.*;
import java.util.*;

/**
 * the class of the menu bar
 * @author ITRIS, Jordi SUC
 */
public class EditorMenuBar extends JMenuBar {

	/**
	 * the editor
	 */
	private Editor editor;
	
	/**
	 * the document describing the menu bar
	 */
	private Document docMenu;
	
	/**
	 * the map associating the name of a menu to this menu
	 */
	private LinkedHashMap<String, JMenu> menus=new LinkedHashMap<String, JMenu>();
	
	/**
	 * the HashMap associating a name to a JMenuItem
	 */
	private LinkedHashMap<String, JMenuItem> menuItems=
		new LinkedHashMap<String, JMenuItem>();
	
	/**
	 * the HashMap associating the name of a menu to a linked list of menu items
	 */
	private LinkedHashMap<String, LinkedList<JMenuItem>> unknownMenuItems=
			new LinkedHashMap<String, LinkedList<JMenuItem>>();
	
	/**
	 * the bundle
	 */
	private ResourceBundle bundle;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public EditorMenuBar(Editor editor) {
		
		super();
		this.editor=editor;
		
		//gets the bundle
        bundle=ResourcesManager.bundle;
	}
	
	/**
	 * initializes the menu bar
	 */
	public void init(){
		
		docMenu = ResourcesManager.getXMLDocument("/com/gtwm/svgeditor/plugins/resources/xml/menu.xml");
		System.out.println("Plugin menu is " + docMenu);
		docMenu=ResourcesManager.getXMLDocument("menu.xml");
		System.out.println("Built in menu is " + docMenu);
		parseXMLMenu();
		findModuleMenuItems();
		build();
	}
	
	/**
	 * @return editor the editor
	 */
	public Editor getSVGEditor(){
		
		return editor;
	}
	
	/**
	 * gets all the menu items from the modules
	 */
	protected void findModuleMenuItems(){
		
		Collection<Module> modules=
			getSVGEditor().getSVGModuleLoader().getModules();

		for(Module module : modules){
		    
			if(module!=null && module.getMenuItems()!=null){
			    
			    menuItems.putAll(module.getMenuItems());
			}
		}
	}
	
	/**
	 * adds a menu item to the list of menu items
	 * @param id the id of the menu item
	 * @param menuItem the menu item
	 */
	public void addMenuItem(String id, JMenuItem menuItem){
	    
	    if(id!=null && ! id.equals("") && menuItem!=null){
	        
	        menuItems.put(id, menuItem);
	    }
	}
	
	/**
	 * returns a menu given its name
	 * @param menuName the name of a menu
	 * @return a menu
	 */
	public JMenu getMenu(String menuName){
		
		if(menuName!=null && ! menuName.equals("")){

			return menus.get(menuName);
		}
		
		return null;
	}
	
	/**
	 * adds a menu item that is not listed in the xml menu file
	 * @param menuName the name of a menu
	 * @param menuItem the menu item
	 */
	public void addUnknownMenuItem(String menuName, JMenuItem menuItem){
		
		if(menuName!=null && ! menuName.equals("") && menuItem!=null){

			if(unknownMenuItems.containsKey(menuName)){
				
				LinkedList<JMenuItem> list=unknownMenuItems.get(menuName);
				list.add(menuItem);
				
			}else{
				
				LinkedList<JMenuItem> list=new LinkedList<JMenuItem>();
				list.add(menuItem);
				unknownMenuItems.put(menuName, list);
			}
		}
	}
	
	/**
	 * removes a menu item that is not listed in the xml menu file
	 * @param menuName the name of a menu
	 * @param menuItem the menu item
	 */
	public void removeUnknownMenuItem(String menuName, JMenuItem menuItem){
		
		if(menuName!=null && ! menuName.equals("") && menuItem!=null && 
				unknownMenuItems.containsKey(menuName)){

			LinkedList<JMenuItem> list=unknownMenuItems.get(menuName);
			
			if(list!=null){
			    
			    list.remove(menuItem);
			}
		}
	}

	/**
	 * builds the menubar given the order of menu items specified in the XML file
	 */
	public void build(){

		removeAll();
		
		if(docMenu!=null){
			
			Element root=docMenu.getDocumentElement();
			
			if(root!=null){

				Node current=null, parent=null;
				String name=null, att=null, attParent=null;
				JMenu menu=null, parentMenu=null;
				LinkedList<JMenuItem> list=null;
				JMenuItem menuItem;
				
				for(NodeIterator it=new NodeIterator(root); it.hasNext();){
					
					current=it.next();
					
					if(current!=null && current instanceof Element){
						
						name=current.getNodeName();
						
						if(name!=null){
							
							if(name.equals("menu")){
								
								//finds and adds the menu to the menu bar
								att=((Element)current).getAttribute("name");

								if(att!=null && ! att.equals("")){

									menu=menus.get(att);
									
									if(menu!=null){
										
										menu.removeAll();
										parent=current.getParentNode();
										
										if(parent!=null && parent instanceof Element){
											
											attParent=((Element)parent).getAttribute("name");
											parentMenu=null;
											
												parentMenu=menus.get(attParent);
										
											if(parentMenu!=null){
											    
												parentMenu.add(menu);
												
											}else{
												
												add(menu);
											}
										}
										
										//adds the non xml specified menu items
										list=unknownMenuItems.get(att);
										
										if(list!=null){

											for(JMenuItem mnItem : list){

												if(mnItem!=null){
												    
													menu.add(mnItem);
												}
											}
										}
									}
								}
									
							}else if(name.equals("menuitem")){
								
								//finds and adds the menu item to the current menu
								att=((Element)current).getAttribute("name");

								if(att!=null && ! att.equals("")){
									
									menuItem=menuItems.get(att);
									parent=current.getParentNode();
									
									if(menuItem!=null && parent!=null && parent instanceof Element){
										
										attParent=((Element)parent).getAttribute("name");
										parentMenu=null;
										
											parentMenu=menus.get(attParent);
									
										if(parentMenu!=null){
										    
											parentMenu.add(menuItem);
											
										}else if(menu!=null){
											
											menu.add(menuItem);
										}
									}
								}
								
							}else if(name.equals("separator")){
							    
								//adds a separator to the menu
								parent=current.getParentNode();
								
								if(parent!=null && parent instanceof Element){
									
									attParent=((Element)parent).getAttribute("name");
									parentMenu=null;
									
										parentMenu=menus.get(attParent);
								
									if(parentMenu!=null){
									    
										parentMenu.addSeparator();
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
	 * parses the document to get the menu and menu items 
	 * specified in the "menu.xml" file
	 */
	protected void parseXMLMenu(){

		if(docMenu!=null){
			
			Element root=docMenu.getDocumentElement();
			
			if(root!=null){

				Node current=null;
				String name=null, att=null, label="";
				JMenu menu=null;
				
				for(NodeIterator it=new NodeIterator(root); it.hasNext();){
					
					current=it.next();
					
					if(current!=null && current instanceof Element){	
						
						name=current.getNodeName();
						
						if(name!=null && name.equals("menu")){

							att=((Element)current).getAttribute("name");

							if(att!=null && ! att.equals("")){
								
								label=att;
								
								if(bundle!=null){
									
									try{label=bundle.getString(att);}catch (MissingResourceException ex) {
										ex.printStackTrace();
									}
								}
								
								menu=new JMenu(label);
								menus.put(att, menu);
							}
						}
					}
				}
			}
		}
	}
	
}
