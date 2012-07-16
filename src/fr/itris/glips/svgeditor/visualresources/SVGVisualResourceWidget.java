/*
 * Created on 21 janv. 2005
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
import java.text.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the widgets that will be displayed in the properties dialog
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceWidget{
    
	/**
	 * a small font
	 */
	protected static final Font smallFont=new Font("smallFont", Font.ROMAN_BASELINE, 9);
	
	/**
	 * the font
	 */
	protected static final Font theFont=new Font("theFont", Font.ROMAN_BASELINE, 10);
	
    /**
     * the runnable used to dispose the widget
     */
    protected Runnable disposer=null;
    
	/**
	 * used to convert numbers into a string
	 */
	protected static DecimalFormat format;
    
    static{
    	
		//sets the format used to convert numbers into a string
		DecimalFormatSymbols symbols=new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format=new DecimalFormat("######.#",symbols);
    }
    
    /**
     * the bundle used to get labels
     */
    protected ResourceBundle bundle=ResourcesManager.bundle;
	
    /**
     * the component
     */
    protected JComponent component=null;
    
    /**
     * the label associated with the panel
     */
    protected String label="";
    
    /**
     * the constructor of the class
     */
    public SVGVisualResourceWidget(){

    }
    
    /**
     * @return the component
     */
    protected JComponent getComponent(){
        
        return component;
    }
    
    /**
     * @return the label
     */
    protected String getLabel(){
        
        return label;
    }
    
    /**
     * disposes the widget
     */
    public void dispose(){
    	
    	if(disposer!=null){
    		
    		disposer.run();
    		disposer=null;
    		component=null;
    	}
    }
}
