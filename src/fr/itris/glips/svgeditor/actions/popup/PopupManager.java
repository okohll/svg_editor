/*
 * Created on 13 janv. 2005
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
package fr.itris.glips.svgeditor.actions.popup;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling the popups that appear when clicking on a node
 * @author ITRIS, Jordi SUC
 */
public class PopupManager {

	/**
	 * the editor
	 */
	private Editor editor;
	
	/**
	 * the map (the description of the popup menu) associating the name of a group 
	 * of popup items to the list of the popup items names
	 */
	private final Map<String, LinkedList<String>> popupItemsDescription=
															new LinkedHashMap<String, LinkedList<String>>();
	
	/**
	 * the map associating the id of a popup item to this popup item
	 */
	private final Map<String, PopupItem> popupItems=new HashMap<String, PopupItem>();
	
	/**
	 * the popup menu
	 */
	private JPopupMenu popupMenu=new JPopupMenu();
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public PopupManager(Editor editor){
	    
		this.editor=editor;
		
		//adding a handles listener
		editor.getHandlesManager().addHandlesListener(new HandlesListener(){

			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {

	            //restores the initial state of each popup item
	            for(PopupItem popupItem : popupItems.values()){

	            	if(popupItem!=null){
	            		
	            		popupItem.setToInitialState();
	            	}
	            }
	        }
		});
		
		//creating the listener to the popup menu
		popupMenu.addPopupMenuListener(new PopupMenuListener(){

			public void popupMenuCanceled(PopupMenuEvent evt) {

				hidePopup();
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent evt) {
			}

			public void popupMenuWillBecomeVisible(PopupMenuEvent evt) {
			}
		});

		//gets the order of the popup items from a xml file
		parseXMLPopupMenuItems();
		
		//gets the popup items from each module
		retrieveModulePopupItems();
	}
	
	/**
	 * @return editor the editor
	 */
	public Editor getSVGEditor(){
		return editor;
	}
	
	/**
	 * retrieves all the popup items from the modules
	 */
	protected void retrieveModulePopupItems(){

		//getting all the loaded modules
		Collection<Module> modules=getSVGEditor().getSVGModuleLoader().getModules();
		Collection<PopupItem> itemsList=null;
		
		//for each module, gets the popup items linked with the given list of nodes
		for(Module module : modules){

			if(module!=null){
			    
				//getting the list of the popup items of each module
				itemsList=module.getPopupItems();

			    if(itemsList!=null){
			    	
			    	for(PopupItem item : itemsList){

			    		if(item!=null){
			    			
			    			popupItems.put(item.getId(), item);
			    			System.out.println("Put popup item " + item + ", id " + item.getId());
			    		}
			    	}
			    }
			}
		}
	}

	/**
	 * shows a popup on the given handle at the given location
	 * @param handle a handle
	 * @param location the location of the mouse click wher the popup should appear
	 */
	public void showPopup(SVGHandle handle, Point location){
	    
	    if(handle!=null && location!=null && popupMenu!=null){

	    	LinkedList<Element> selectedNodes=new LinkedList<Element>(
	    			handle.getSelection().getSelectedElements());

            if(popupItems!=null && popupItems.size()>0){
            	
            	popupMenu.removeAll();

                JMenuItem menuItem=null;
                PopupItem popupItem=null;
                boolean hasAddedAnItem=false;
                boolean isGroupEnabled=false;
                LinkedList<JMenuItem> popupItemsToAdd=null;
                int i=0;
                
                //for each group of menu items, fills the pop up menu and put popup 
                //separators between each group of them
                for(LinkedList<String>itemNames : popupItemsDescription.values()){

                    if(itemNames!=null){
                        
                        hasAddedAnItem=false;
                        isGroupEnabled=false;
                        popupItemsToAdd=new LinkedList<JMenuItem>();
                        
                        for(String itemName : itemNames){
                            
                            //if the name of the menuitem of the popup model is contained 
                            //in the list of the names of the menuitems that should be displayed
                            if(itemName!=null && popupItems.containsKey(itemName)){
                            	
                            	popupItem=popupItems.get(itemName);
                            	System.out.println("Got popup item " + itemName);
                            	
                            	if(popupItem!=null){
                            		
                            		menuItem=popupItem.getPopupItem(selectedNodes);
                            		System.out.println("Menu item is " + menuItem);
                            		
                                    if(menuItem!=null){
                                        
                                        //the menu item is added to a list
	                                    popupItemsToAdd.add(menuItem);
	                                    hasAddedAnItem=true;
	                                    isGroupEnabled=isGroupEnabled || popupItem.isEnabled();
                                    }
                            	}
                            }
                        }
                        
                        if(isGroupEnabled){
                        	
                            for(JMenuItem mi : popupItemsToAdd){

                                if(mi!=null){
                                	
                                	//the menu item is added to the popup menu
                                	popupMenu.add(mi);
                                }
                            }
                            
                            popupItemsToAdd.clear();
                            
                            if(hasAddedAnItem && i<popupItemsDescription.size()-1){
                                
                                //if other groups of menu items could be added and if menu items 
                                //from the previous group have been added, a new separator is added
                                popupMenu.addSeparator();
                            }
                        }
                    }
                    
                    i++;
                }
                
                popupMenu.show(handle.getScrollPane().
                	getSVGCanvas(), location.x, location.y);
            }
	    }
	}
	
	/**
	 * hides the popup
	 */
	public void hidePopup(){
		
        popupMenu.removeAll();
        restorePopupItemsInitialState();
	}
	
	/**
	 * restores the initial state of all the popup items
	 */
	protected void restorePopupItemsInitialState(){

    	for(PopupItem item : popupItems.values()){

    		if(item!=null){
    			
    			item.setToInitialState();
    		}
    	}
	}
	
	/**
	 *parses the document to get the menu items specified in the "popup.xml" file
	 */
	protected void parseXMLPopupMenuItems(){

		Document doc=ResourcesManager.getXMLDocument("popup.xml");
		
		if(doc!=null){
		    
		    Element root=doc.getDocumentElement();
		    
		    if(root!=null){
		        
				String groupName="", itemName="";
				LinkedList<String> itemList=null;
				Node item=null;
				
				//for each group of menu items
				for(Node current=root.getFirstChild(); current!=null; current=current.getNextSibling()){
					
					if(current instanceof Element && current.getNodeName().equals("group")){

						groupName=((Element)current).getAttribute("name");
							
						if(groupName!=null && ! groupName.equals("")){
							
						    itemList=new LinkedList<String>();

						    //for each menuitem, gets its name
						    for(item=current.getFirstChild(); item!=null; item=item.getNextSibling()){
						        
						    	if(item instanceof Element){
						    		
							        itemName=((Element)item).getAttribute("name");
							        
							        if(itemName!=null && ! itemName.equals("")){
							            
							            itemList.add(itemName);
							        }
						    	}
						    }
						    
						    if(itemList.size()>0){
						        
						    	popupItemsDescription.put(groupName, itemList);
						    }
						}
					}
				}
		    }
		}
	}
	
}
