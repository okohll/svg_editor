/*
 * Created on 6 déc. 2004
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

package fr.itris.glips.svgeditor;

import java.awt.*;
import java.util.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class ColorManager {

	/**
	 * the current color used to draw items
	 */
	private static Color currentColor=new Color(100, 100, 255);
	
	/**
	 * the list of the listeners listening to the changes of the current color
	 */
	private LinkedList<ColorListener> colorListeners=new LinkedList<ColorListener>();
    
	/**
	 * the editor
	 */
	private Editor editor;
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public ColorManager(Editor editor) {
	    
		this.editor=editor;
	}

    /**
     * @return Returns the editor.
     */
    public Editor getSVGEditor() {
        
        return editor;
    }
    
	/**
	 * @return the current color
	 */
	public static Color getCurrentColor(){
		return currentColor;
	}
	
	/**
	 * adds a new color listener
	 * @param cl
	 */
	public synchronized void addColorListener(ColorListener cl){
	    
	    if(cl!=null){
	        
	        colorListeners.add(cl);
	    }
	}
	
	/**
	 * removes a color listener
	 * @param cl
	 */
	public synchronized void removeColorListener(ColorListener cl){
	    
	    if(cl!=null){
	        
	        colorListeners.remove(cl);
	    }
	}
	
	/**
	 * sets the current color
	 * @param currentColor the new color value
	 */
	public void setCurrentColor(Color currentColor){
	    
	    if(currentColor!=null){
	        
			ColorManager.currentColor=currentColor;

			//notifies that the current color has changed
			for(ColorListener cl : colorListeners){

			    if(cl!=null){
			        
			        cl.colorChanged(currentColor);
			    }
			}
	    }
	}
	
	/**
	 * the interface of the color listener
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	public interface ColorListener{
	    
	    /**
	     * notifies that the current color has changed
	     * @param color the new color
	     */
	    public void colorChanged(Color color);
	}
}
