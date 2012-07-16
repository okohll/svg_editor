/*
 * Created on 26 août 2004
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
package fr.itris.glips.svgeditor.visualresources;

import java.awt.*;

import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class of the items in the list
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceListItem{
    
	/**
	 * a svg handle
	 */
	private SVGHandle handle=null;
	
    /**
     * the resource object
     */
    private SVGVisualResourceObject resObj;

    /**
     * the constructor of the class
     * @param handle a svg handle
     * @param resObj a resource object
     */
    public SVGVisualResourceListItem(SVGHandle handle, SVGVisualResourceObject resObj){
        
    	this.handle=handle;
        this.resObj=resObj;
    }
    
    @Override
    public String toString(){
    	
        return resObj.getResourceId();
    }

	/**
	 * @param componentToRefresh the component to refresh
	 * @return  the resourceRepresentation.
	 */
	protected JPanel getResourceRepresentation(Component componentToRefresh) {
		
		ResourceImageManager resImgManager=Editor.getEditor().getResourceImageManager();
		
		return resImgManager.getResourceRepresentation(handle, resObj.getResourceId(), false, componentToRefresh);
	}
	
	/**
	 * @return Returns the resObj.
	 */
	protected SVGVisualResourceObject getVisualResourceObject() {
		
		return resObj;
	}
}
