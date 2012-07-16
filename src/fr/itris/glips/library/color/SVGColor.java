/*
 * Created on 18 févr. 2005
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
package fr.itris.glips.library.color;

import java.awt.*;

/**
 * the class describing colors
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGColor extends Color {

    /**
     * the id of the color
     */
    protected String id="";
    
    /**
     * the constructor of the class
     * @param id the id of the color
     * @param color a color
     */
    public SVGColor(String id, Color color){
        
        super(color.getRed(), color.getGreen(), color.getBlue());
        this.id=id;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }
}
