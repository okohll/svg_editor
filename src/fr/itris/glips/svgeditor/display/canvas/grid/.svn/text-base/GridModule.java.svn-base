/*
 * Created on 1 juil. 2004
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
package fr.itris.glips.svgeditor.display.canvas.grid;

import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/**
 * the class used to display the grid on the canvas
 * 
 * @author ITRIS, Jordi SUC
 */
public class GridModule extends ModuleAdapter{
	
	/**
	 * the ids
	 */
	private String gridDisplayId="GridDisplay", gridParametersId="GridParameters";
	
	/**
	 * the labels
	 */
	private String gridHiddenLabel="", gridShownLabel="", gridParametersLabel="";
	
	/**
	 * the menu item used to select the state of the grid display
	 */
	private final JMenuItem gridDisplayMenuItem=new JMenuItem();
	
	/**
	 * the menu item used to launch the dialog used to modify the grid parameters
	 */
	private final JMenuItem gridParametersMenuItem=new JMenuItem();

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public GridModule(Editor editor){
		
		//gets the labels from the resources
		ResourceBundle bundle=ResourcesManager.bundle;
		
		gridHiddenLabel=bundle.getString("GridHidden");
		gridShownLabel=bundle.getString("GridShown");
		gridParametersLabel=bundle.getString("GridParameters");

		//a listener that listens to the changes of the svg handles
		final HandlesListener svgHandlesListener=new HandlesListener(){
			
			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {

				gridParametersMenuItem.setEnabled(currentHandle!=null);
				
				if(currentHandle!=null){
				    
					//enables the menuitems
					gridDisplayMenuItem.setEnabled(true);
					
					if(Editor.getEditor().getHandlesManager().
							getGridParametersHandler().isGridEnabled()){
					    
						gridDisplayMenuItem.setText(gridShownLabel);
	
					}else{
					    
						gridDisplayMenuItem.setText(gridHiddenLabel);
					}
					
				}else{
				    
					//disables the menuitems
					gridDisplayMenuItem.setEnabled(false);
				}
			}	
		};
		
		//adds the svg handles change listener
		editor.getHandlesManager().addHandlesListener(svgHandlesListener);
		
		//the menu items
		gridDisplayMenuItem.setText(gridHiddenLabel);
		gridDisplayMenuItem.setEnabled(false);
		
		gridParametersMenuItem.setText(gridParametersLabel);
		gridParametersMenuItem.setEnabled(false);
		
		//adds the listener
		ActionListener listener=new ActionListener(){

			/**
			 * the method called when the action is done
			 */
			public void actionPerformed(ActionEvent evt) {
				
				GridParametersManager gridParametersHandler=
					Editor.getEditor().getHandlesManager().getGridParametersHandler();
				
				if(evt.getSource().equals(gridDisplayMenuItem)){
					
					SVGHandle svgHandle=
						Editor.getEditor().getHandlesManager().getCurrentHandle();
					
					if(svgHandle!=null){
					    
						if(! gridParametersHandler.isGridEnabled()){
						    
							gridParametersHandler.setGridEnabled(true);
							gridDisplayMenuItem.setText(gridShownLabel);
		
						}else{
						    
							gridParametersHandler.setGridEnabled(false);
							gridDisplayMenuItem.setText(gridHiddenLabel);
						}
					}
					
				}else{
					
					gridParametersHandler.launchDialog(gridParametersMenuItem);
				}
			}
		};
		
		gridDisplayMenuItem.addActionListener(listener);
		gridParametersMenuItem.addActionListener(listener);
		
		//setting the icons
		ImageIcon gridDisplayIcon=ResourcesManager.getIcon(gridDisplayId, false);
		ImageIcon gridDisplayDisabledIcon=ResourcesManager.getIcon(gridDisplayId, true);
		ImageIcon gridParametersIcon=ResourcesManager.getIcon(gridParametersId, false);
		ImageIcon gridParametersDisabledIcon=ResourcesManager.getIcon(gridParametersId, true);
		
		gridDisplayMenuItem.setIcon(gridDisplayIcon);
		gridDisplayMenuItem.setDisabledIcon(gridDisplayDisabledIcon);
		
		gridParametersMenuItem.setIcon(gridParametersIcon);
		gridParametersMenuItem.setDisabledIcon(gridParametersDisabledIcon);
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> menuItems=new HashMap<String, JMenuItem>();
		
		menuItems.put(gridDisplayId,gridDisplayMenuItem);
		menuItems.put(gridParametersId,gridParametersMenuItem);
		
		return menuItems;
	}
}
