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

import java.util.*;

/**
 * the class used to store the state of the resource window
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceState{
    
    /**
     * the id of the current tab
     */
    private String tabId="";
    
    /**
     * the map associating the id of a tab to the id of an item
     */
    private Map itemsMap=Collections.synchronizedMap(new HashMap());
    
    /**
     * the constructor of the class
     */
    protected SVGVisualResourceState(){
        
    }
    
    /**
     * returns the id of the item to selected in the given tab
     * @param tabId the id of a tab
     * @return the id of the item to select
     */ 
    protected String getSelectedItemId(String tabId) {
    	
    	String itemId="";
    	
    	if(tabId!=null){
    		
    		itemId=(String)itemsMap.get(tabId);
    	}
        return itemId;
    }
    
    /**
     * sets the id of the selected item for the given tab 
     * @param tabId the id of a tab
     * @param itemId the id of an item
     */
    protected void setSelectedItemId(String tabId, String itemId) {
        
    	if(tabId!=null && ! tabId.equals("") && itemId!=null && ! itemId.equals("")){
    		
    		itemsMap.put(tabId, itemId);
    	}
    }
    
    protected String getSelectedTabId() {
        return tabId;
    }
    
    protected void setSelectedTabId(String tabId) {
        this.tabId = tabId;
    }
}
