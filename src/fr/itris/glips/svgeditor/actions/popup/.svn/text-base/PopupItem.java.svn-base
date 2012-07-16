/*
 * Created on 20 févr. 2005
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

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;
import java.awt.*;

/**
 * the class used for creating a pop up item
 * @author ITRIS, Jordi SUC
 */
public abstract class PopupItem {

	/**
	 * the editor
	 */
	protected Editor editor;
	
	/**
	 * the id of the popup item
	 */
	protected String id="";
	
	/**
	 * the menu item
	 */
	protected JMenuItem menuItem;
	
	/**
	 * the label of the menu item
	 */
	protected String label="";
	
	/**
	 * the regular and disabled icons
	 */
	protected ImageIcon icon=null, disabledIcon=null;
	
	/**
	 * the font
	 */
	protected static Font theFont=new Font("theFont", Font.ROMAN_BASELINE, 10);

	/**
	 * the constructor of the class
	 * @param editor the editor
	 * @param id the id of this popup item
	 * @param label the label for the popup item
	 * @param iconName the name of an icons
	 */
	public PopupItem(Editor editor, String id, String label, String iconName){
		
		this.editor=editor;
		this.id=id;
		this.label=label;
		
		//getting the icons
		if(iconName!=null){
			
			icon=ResourcesManager.getIcon(iconName, false);
			disabledIcon=ResourcesManager.getIcon(iconName, true);
		}

		menuItem=new JMenuItem();
		menuItem.setText(label);

		if(icon!=null){
			
			menuItem.setIcon(icon);
		}
		
		if(disabledIcon!=null){
			
			menuItem.setDisabledIcon(disabledIcon);
		}
	}
	
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		
		return id;
	}
	
	/**
	 * returns the popup item that will be displayed
	 * @param nodes the nodes onto which the popup item will act
	 * @return the menu item
	 */
	public JMenuItem getPopupItem(LinkedList<org.w3c.dom.Element> nodes){

		//the listener the menuitem actions that hides the popup
		menuItem.addActionListener(new ActionListener(){
		
			public void actionPerformed(ActionEvent evt) {

				editor.getPopupManager().hidePopup();
			}
		});

		return menuItem;
	}
	
	/**
	 * restores the popup item initial state
	 */
	public void setToInitialState(){
		
		ActionListener[] actionListeners=menuItem.getActionListeners();
		
		//removes all the action listeners from the menu item
		for(int i=0; i<actionListeners.length; i++){
			
			menuItem.removeActionListener(actionListeners[i]);
		}
	}
	
	/**
	 * @return whether this popup item is enabled or not
	 */
	public boolean isEnabled(){
		
		return menuItem.isEnabled();
	}
}
