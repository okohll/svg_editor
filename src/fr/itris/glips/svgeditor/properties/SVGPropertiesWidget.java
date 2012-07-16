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

import java.awt.*;
import javax.swing.*;
import java.text.*;
import java.util.*;
import org.w3c.dom.*;

/**
 * the class of the widgets displayed in the properties panel
 * 
 * @author ITRIS, Jordi SUC
 */
public abstract class SVGPropertiesWidget {
	
	/**
	 * a small font
	 */
	protected static final Font smallFont=new Font("smallFont", Font.ROMAN_BASELINE, 9);
	
	/**
	 * the font
	 */
	protected static final Font theFont=new Font("theFont", Font.ROMAN_BASELINE, 10);
	    
    /**
     * the component that will be displayed
     */
    protected JComponent component;
    
    /**
     * the name of the property
     */
    protected String name="";
    
    /**
     * the label
     */
    protected String label="";
    
    /**
     * the property item
     */
    protected SVGPropertyItem propertyItem=null;
    
    /**
     * the list of the nodes being modified
     */
    protected LinkedList<Element> nodesList=new LinkedList<Element>();
    
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
		format=new DecimalFormat("######.##",symbols);
    }
    
	/**
	 * the list of the system fonts
	 */
	protected static final LinkedList fontList=new LinkedList();
	
	/**
	 * the list of the system font families
	 */
	protected static final LinkedList fontFamilyList=new LinkedList();
	
	/**
	 * creates the list of the system fonts
	 */
	static{

		//gets the graphics environment and the array of the platform available fonts 
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fontTab=ge.getAllFonts();
		
		//removes all the duplicated font families and creates the list of fonts
		Font cfont=null;
		
		for(int i=0;i<fontTab.length;i++){
		    
			if(fontTab[i]!=null && ! fontFamilyList.contains(fontTab[i].getFamily())){
			    
				cfont=fontTab[i].deriveFont((float)(11));
				
				if(cfont!=null){
				    
					fontList.add(cfont);
					fontFamilyList.add(cfont.getFamily());
				}
			}
		}
	}
    
    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
    public SVGPropertiesWidget(SVGPropertyItem propertyItem){
        
        this.propertyItem=propertyItem;
        name=new String(propertyItem.getPropertyName());
        label=new String(propertyItem.getPropertyLabel());
        nodesList.addAll(propertyItem.getNodeList());
    }

    /**
     * @return Returns the component.
     */
    public JComponent getComponent() {
        return component;
    }

    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * disposes the widget
     */
    public void dispose(){
    	
    	if(disposer!=null){
    		
    		disposer.run();
    	}
    }
}
