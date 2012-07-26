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

import java.util.MissingResourceException;

/**
 * the class of the widgets that will be displayed in the properties dialog, and enabling to modify the properties
 * of an attribute (or a group of attributes) of a resource node
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceAttributeWidget extends SVGVisualResourceWidget {

    /**
     * the resource object whose children will be modified by this widget
     */
    protected SVGVisualResourceObjectAttribute resourceObjectAttribute;
	
	/**
	 * the constructor of the class
	 * @param resourceObjectAttribute the attribute object that will be modified
	 */
    public SVGVisualResourceAttributeWidget(SVGVisualResourceObjectAttribute resourceObjectAttribute) {

        this.resourceObjectAttribute=resourceObjectAttribute;
        
        if(resourceObjectAttribute!=null){
            
            String name=resourceObjectAttribute.getModel().getAbsoluteName();
            label=name;
            
            if(bundle!=null){
                
                try{
                    label=bundle.getString(name);
                }catch (MissingResourceException ex) {
                		ex.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void dispose() {

    	super.dispose();
    	resourceObjectAttribute=null;
    }
}
