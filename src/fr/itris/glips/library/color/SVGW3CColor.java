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
 * the class describing the standard w3c colors
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGW3CColor extends SVGColor {

    /**
     * the constructor of the class
     * @param w3cName the name of the color in the w3c standards
     * @param shownColor the color that should be shown
     */
    public SVGW3CColor(String w3cName, Color shownColor){
        
        super(w3cName, shownColor);
    }
    
    /**
     * @return a string representation of this color
     */
    public String getStringRepresentation(){
        
        return id+" : rgb("+getRed()+", "+getGreen()+" ,"+getBlue()+")";
    }
}
