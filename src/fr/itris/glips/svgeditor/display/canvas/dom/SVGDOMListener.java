/*
 * Created on 7 juin 2005
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
package fr.itris.glips.svgeditor.display.canvas.dom;

import org.w3c.dom.*;

import fr.itris.glips.svgeditor.display.handle.*;



import java.lang.ref.*;

/**
 * the listener to the dom elements
 * 
 * @author ITRIS, Jordi SUC
 */
public abstract class SVGDOMListener {
    
    /**
     * the reference of the node that is listened to
     */
    protected WeakReference<Node> nodeRef=null;
    
    /**
     * the reference of the handle the listener is linked to
     */
    protected WeakReference<SVGHandle> handleRef=null;
    
    /**
     * the constructor of the class
     * @param node the node that is listened to
     */
    public SVGDOMListener(Node node) {
        
        nodeRef=new WeakReference<Node>(node);
    }
    
    /**
	 * @param handle the handle linked to this listener
	 */
	public void setHandle(SVGHandle handle) {
		handleRef=new WeakReference<SVGHandle>(handle);
	}
	
	/**
	 * @return the handle linked to this listener
	 */
	public SVGHandle getHandle() {
		
		if(handleRef!=null) {
			
			return handleRef.get();
		}
		
		return null;
	}
	
	/**
	 * removes this dom listener
	 */
	public void removeListener() {
		
		if(handleRef!=null) {
			
			handleRef.get().getSvgDOMListenerManager().
				removeDOMListener(this);
			handleRef=null;
			nodeRef=null;
		}
	}

	/**
     * @return the node the listener is listening to
     */
    public Node getNode() {
    	
    	if(nodeRef!=null) {
    		
    		return nodeRef.get();
    	}
    	
        return null;
    }

    /**
	 * notifies that the given node has been changed
	 */
	public abstract void nodeChanged();
	
	/**
	 * notifies that the given node has been inserted under the node of the listener
	 * @param insertedNode a node
	 */
	public abstract void nodeInserted(Node insertedNode);
	
	/**
	 * notifies that the given node has been removed from the node of the listener
	 * @param removedNode a node
	 */
	public abstract void nodeRemoved(Node removedNode);
	
	/**
	 * notifies that the sub tree from which the root can been found has been modified
     * @param lastModifiedNode the last node that has been modified
	 */
	public abstract void structureChanged(Node lastModifiedNode);
}
