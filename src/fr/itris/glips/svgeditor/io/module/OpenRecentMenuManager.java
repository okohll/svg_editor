package fr.itris.glips.svgeditor.io.module;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.library.monitor.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.io.managers.monitor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the manager used to build and handle the state 
 * of the open recent menu items of the open recent menu
 * @author Jordi SUC
 */
public class OpenRecentMenuManager {

	/**
	 * the menu
	 */
	private JMenu menu=new JMenu();
	
	/**
	 * the list of the menu items
	 */
	private LinkedList<JMenuItem> menuItems=
		new LinkedList<JMenuItem>();
	
	/**
	 * the constructor of the class
	 */
	public OpenRecentMenuManager(){
		
		//adding a listener to the recent files changes
		RecentFilesListener listener=new RecentFilesListener(){
			
			@Override
			public void recentFilesListChanged(
					LinkedList<String> filesPathsList) {

				updateMenuItems();
			}
		};
		
		Editor.getEditor().getResourcesManager().
			addRecentFilesListener(listener);
		
		updateMenuItems();
	}
	
	/**
	 * updates the menu items
	 */
	protected void updateMenuItems() {
		
		//removing the previous menu items and disposing them
		if(menuItems.size()>0){
			
			for(JMenuItem item : menuItems){
				
				removeListeners(item);
			}
			
			menuItems.clear();
			menu.removeAll();
		}

		//getting the list of the recent files paths
		LinkedList<String> recentFilesList=
			Editor.getEditor().getResourcesManager().getRecentFiles();

		if(recentFilesList.size()==0){
			
			menu.setEnabled(false);
			
		}else{
			
			menu.setEnabled(true);
			
			//creating the menuitems
			ActionListener listener=null;
			File file=null;

			for(String recentFilePath : recentFilesList){
				
				//getting the file corresponding to the path and checking its existence
				try{
					file=new File(new URI(recentFilePath));
				}catch (URISyntaxException ex) {
					ex.printStackTrace();
				}
				
				if(file!=null/* && file.exists()*/){
					
					//creating the menu item
					final JMenuItem menuItem=new JMenuItem();
					
					//setting the properties of the menu item
					menuItem.setText(file.getName());
					menuItem.setToolTipText(recentFilePath);
					
					menu.add(menuItem);
					menuItems.add(menuItem);
					
					final File ffile=file;

					//creating the listener to the item
					listener=new ActionListener(){
						
						public void actionPerformed(ActionEvent evt) {

							Monitor monitor=new OpenMonitor(
									ffile, Editor.getParent(), menuItem, 0, 100);
							
							Editor.getEditor().getIOManager().
								getFileOpenManager().open(ffile, monitor);
						}
					};
					
					menuItem.addActionListener(listener);

				}else{
					
					//as the file does not exist, it is removed from the list of the recent files
					Editor.getEditor().getResourcesManager().
						removeRecentFile(recentFilePath);
				}
			}
		}
	}

	/**
	 * @return the menu containing the export menu items
	 */
	public JMenu getMenu() {
		return menu;
	}
	
	/**
	 * removes the listeners from the provided menu item
	 * @param menuItem a menu item
	 */
	protected void removeListeners(JMenuItem menuItem){
		
		ActionListener[] listeners=menuItem.getActionListeners();
		
		for(ActionListener listener : listeners){

			menuItem.removeActionListener(listener);
		}
	}
	
}
