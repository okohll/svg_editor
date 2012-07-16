/*
 * Created on 2 avr. 2004
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
package fr.itris.glips.svgeditor.display.undoredo;

import java.util.*;
import org.w3c.dom.*;

/**
 * @author ITRIS, Jordi SUC
 * the class of the undo/redo actions
 */
public class UndoRedoAction {

	/**
	 * the action's name
	 */
	private String name="";
	
	/**
	 * the runnable used to execute the action
	 */
	private Runnable executeRunnable;
	
	/**
	 * the runnable used to undo the action
	 */
	private Runnable undoRunnable;
	
	/**
	 * the runnable used to redo the action
	 */
	private Runnable redoRunnable;
	
	/**
	 * the set of elements on which the action is done
	 */
	private Set<Element> elements;
	
	/**
	 * the construcor of the class
	 * @param name a string
	 * @param executeRunnable the runnable used to execute the action
	 * @param undoRunnable the runnable used to undo the action
	 * @param redoRunnable the runnable used to redo the action
	 * @param elements the set of elements on which the action is done
	 */
	public UndoRedoAction(String name, Runnable executeRunnable, 
			Runnable undoRunnable, Runnable redoRunnable, Set<Element> elements){
		
		this.name=name;
		this.executeRunnable=executeRunnable;
		this.redoRunnable=redoRunnable;
		this.undoRunnable=undoRunnable;
		this.elements=elements;
	}
	
	/**
	 * @return the action's name
	 */
	public String getName(){
		
		return name;
	}
	
	/**
	 * @param name the action's name
	 *
	 */
	public void setName(String name){
		
		this.name=name;
	}
	
	/**
	 * @return the set of elements on which the action is done
	 */
	public Set<Element> getElements() {
		return elements;
	}
	
	/**
	 * used to execute the action
	 */
	public void execute(){
		
		if(executeRunnable!=null){
			
			executeRunnable.run();
		}
	}
	
	/**
	 * used undo the action
	 */
	public void undo(){
		
		if(undoRunnable!=null){
			
			undoRunnable.run();
		}
	}
	
	/**
	 * used to redo the action
	 */
	public void redo(){
		
		if(redoRunnable!=null){
			
			redoRunnable.run();
		}
	}
	
	@Override
    public String toString() {
       
        return getName();
    }
}
