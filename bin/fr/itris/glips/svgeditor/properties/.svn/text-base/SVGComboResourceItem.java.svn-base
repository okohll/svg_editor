/*
 * Created on 19 janv. 2005
 * 
 =============================================
                   GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
GLIPS Graffiti Editor, a SVG Editor
Copyright (C) 2004 Jordi SUC, Philippe Gil, SARL ITRIS

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
package fr.itris.glips.svgeditor.properties;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class of the items in the combo boxes
 * @author ITRIS, Jordi SUC
 */
public class SVGComboResourceItem{
	
    /**
     * the label and the value of the item
     */
	private String value, label;
	
	/**
	 * the component displaying a representation of a resource
	 */
	private JComponent resourceImage=null;
	
	/**
	 * the constructor of the class
	 * @param handle the current handle
	 * @param value the value of the item
	 * @param label the label of the item
	 * @param resource the resource node
	 */
	public SVGComboResourceItem(SVGHandle handle, String value, String label, Element resource){
	    
		this.value=value;
		this.label=label;
		
		if(resource!=null){
			
			resourceImage=Editor.getEditor().getResourceImageManager().
				getResourceRepresentation(handle, resource.getAttribute("id"), true, null);
			
		}else{
			
			resourceImage=new JPanel();
			resourceImage.setOpaque(false);
		}
	}
	
	/**
	 * @return the value of the item
	 */
	public String getValue(){
		return value;
	}
	
	/**
	 * @return the resource image
	 */
	public JComponent getResourceImage(){
	    
	    return resourceImage;
	}
	
	/**
	 * @return the label of the item
	 */
	public String toString(){
		return label;
	}
}
