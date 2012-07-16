/*
 * Created on 1 juil. 2004
 *
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
package fr.itris.glips.svgeditor.display.canvas.rulers;

import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/**
 * the class used to display the rulers
 * @author ITRIS, Jordi SUC
 */
public class RulersModule extends ModuleAdapter{
	
	/**
	 * the id
	 */
	private String id="Rulers";
	
	/**
	 * the labels
	 */
	private String rulersHiddenLabel="", rulersShownLabel="";
	
	/**
	 * the menu item used for the rulers
	 */
	private final JMenuItem displayRulers=new JMenuItem();

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public RulersModule(Editor editor){
		
		//gets the labels from the resources
		ResourceBundle bundle=ResourcesManager.bundle;
		
		if(bundle!=null){
		    
			try{
				rulersHiddenLabel=bundle.getString("RulersHidden");
				rulersShownLabel=bundle.getString("RulersShown");
			}catch (Exception ex){}
		}
		
		//a listener that listens to the changes of the svg handles
		final HandlesListener svgHandlesListener=new HandlesListener(){
			
			@Override
			public void handleChanged(
				SVGHandle currentHandle, Set<SVGHandle> handles) {

				if(currentHandle!=null){
				    
					//enables the menuitems
					displayRulers.setEnabled(true);
					
				}else{
				    
					//disables the menuitems
					displayRulers.setEnabled(false);
				}
			}	
		};
		
		//adds the svg handles change listener
		editor.getHandlesManager().addHandlesListener(svgHandlesListener);
		
		//getting the icons
		ImageIcon icon=ResourcesManager.getIcon(id, false);
		ImageIcon disabledIcon=ResourcesManager.getIcon(id, true);
		
		//setting the properties of the menuitem
		displayRulers.setText(rulersShownLabel);
		displayRulers.setIcon(icon);
		displayRulers.setDisabledIcon(disabledIcon);
		displayRulers.setEnabled(false);
		
		//adding the listener
		displayRulers.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent evt) {
				
				SVGHandle handle=Editor.getEditor().
					getHandlesManager().getCurrentHandle();
				
				if(handle!=null){
					
					RulersParametersManager manager=Editor.getEditor().
						getHandlesManager().getRulersParametersHandler();
				    
					if(! manager.areRulersEnabled()){
					    
						manager.setRulersEnabled(true);
						displayRulers.setText(rulersShownLabel);
	
					}else{
					    
						manager.setRulersEnabled(false);
						displayRulers.setText(rulersHiddenLabel);
					}
				}
			}
		});
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> menuItems=
			new HashMap<String, JMenuItem>();
		menuItems.put(id, displayRulers);
		
		return menuItems;
	}
}
