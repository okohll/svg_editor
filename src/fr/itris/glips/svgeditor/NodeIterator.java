/*
 * Created on 24 mars 2004
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

import org.w3c.dom.*;
import java.util.Iterator;

/**
 * the class of the iterator on a DOM tree
 * @author ITRIS, Jordi SUC
 */
public class NodeIterator implements Iterator<Node>{

	/**
	 * the root and the current nodes
	 */
	private Node root, current;
	
	/**
	 * whether the current node has a next node
	 */
	private boolean hasNext=false;
	
	/**
	 * the constructor of the class
	 * @param root the root node for the iterator
	 */
	public NodeIterator(Node root) {
	    
		this.root=root;
		current=root;
	}
	
	/**
	 * @return true if the iteration has more elements.
	 */
	public boolean hasNext(){
	    
	    if(root!=null){
	        
			//if the current node has children
			if(current!=null && current.hasChildNodes()){
			    
				//the current node becomes the first child
				current=current.getFirstChild();
				hasNext=true;
				
			}else if(current!=null && current.getNextSibling()!=null){
			    
				//if the current node has no child but a sibling the current becomes the next sibling
				current=current.getNextSibling();
				hasNext=true;
				
			}else if(current!=null){
			    
				//otherwise we go up in the tree until we find a new sibling
				while(current!=null && current!=root && current.getNextSibling()==null){
				    
					current=current.getParentNode();
				}

				//if the current node is not the root node
				if(current!=null && current!=root){
				    
					current=current.getNextSibling();
					hasNext=true; 
				    
				}else{
				    
				    hasNext=false;
				}

			}else{
			    
			    hasNext=false;
			}
			
	    }else{
	        
	        hasNext=false;
	    }

		return hasNext;
	}
	
	/**
	 * @return the next element in the iteration
	 */
	public Node next(){
		return current;
	}

	/**
	 * Removes from the underlying collection the last element returned by the iterator (optional operation)
	 */
	public void remove(){
		
	}
}
