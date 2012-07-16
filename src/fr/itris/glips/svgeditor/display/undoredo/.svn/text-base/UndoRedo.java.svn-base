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

import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import java.util.*;
import org.w3c.dom.*;

/**
 * @author ITRIS, Jordi SUC
 * the class that manages the undo/redo stack that contains the actions to call when undoing or redoing
 */
public class UndoRedo extends ModuleAdapter{
	
	/**
	 * the svg handle this undo/redo manager is associated to
	 */
	private SVGHandle svgHandle;
	
	/**
	 * the stack of the undo action list
	 */
	private LinkedList<UndoRedoActionList> undoStack=new LinkedList<UndoRedoActionList>();
	
	/**
	 * the stack of the redo action list
	 */
	private LinkedList<UndoRedoActionList> redoStack=new LinkedList<UndoRedoActionList>();
	
	/**
	 * the amount of action lists the undo/redo stacks can contain
	 */
	private int stackDepth=30;

	/**
	 * the constructor of the class
	 * @param svgHandle the svg handle this undo/redo manager is associated to
	 */
	public UndoRedo(SVGHandle svgHandle){
		
		this.svgHandle=svgHandle;
	}
	
	/**
	 * adds an action list to the undo stack
	 * @param actionList  the actionList that describes what 
	 * has to be done when calling the undo or redo action
	 * @param refreshSelection whether the selection should be refreshed
	 */
	public void addActionList(final UndoRedoActionList actionList, 
			boolean refreshSelection){

		//checking if the undo stack is full, if true, removes the first added list
		if(undoStack.size()>=stackDepth){
			
			undoStack.removeFirst();
		}
		
		//adds the new action list to the undo stack
		undoStack.addLast(actionList);
		
		//clears the redo stack
		redoStack.clear();

		//getting the list of all the elements that are handled in the action list
		Set<Element> allElements=new HashSet<Element>();
		
		for(UndoRedoAction action : actionList){
			
			if(action.getElements()!=null){
				
				allElements.addAll(action.getElements());
			}
		}
		
		//creating the runnable used for the execution
		Runnable executeRunnable=new Runnable(){
			
			public void run() {

				for(UndoRedoAction action : actionList){
					
					action.execute();
				}
			}
		};
		
		//launching the execution
		svgHandle.enqueue(executeRunnable, 
				allElements, actionList.isInvokeInAWTThread());
		
		if(refreshSelection){

			svgHandle.getSelection().refreshSelection(true);
		}
		
		//notifying the svg handle that something has changed on the canvas
		svgHandle.setModified(true);
		
		//notifying that the handle has changed
		Editor.getEditor().getHandlesManager().handleContentChanged();
	}

	/**
	 * undoes the last action added into the undo stack
	 */
	public void undoLastAction(){
		
		//deselecting all elements
		svgHandle.getSelection().clearSelection();
		
		//getting the lastly entered action list, and removes it from the stack
		final UndoRedoActionList actionList=undoStack.getLast();
		undoStack.remove(actionList);
		
		//adding this action list to the redo stack
		redoStack.add(actionList);
		
		//getting the list of all the elements that are handled in the action list
		Set<Element> allElements=new HashSet<Element>();
		
		for(UndoRedoAction action : actionList){
			
			if(action.getElements()!=null){
				
				allElements.addAll(action.getElements());
			}
		}
		
		//creating the runnable used for the undo action
		Runnable executeRunnable=new Runnable(){
			
			public void run() {
				
				//executing the undo actions
				UndoRedoAction current=null;

				for(int i=actionList.size()-1; i>=0; i--){
				    
					try{current=actionList.get(i);}catch (Exception ex){}
					
					if(current!=null){
					    
						current.undo();
					}
				}
			}
		};
		
		//launching the execution
		svgHandle.enqueue(executeRunnable, 
				allElements, actionList.isInvokeInAWTThread());
		
		//notifying the svg handle that something has changed on the canvas
		svgHandle.setModified(true);
		
		//notifying that the handle has changed
		Editor.getEditor().getHandlesManager().handleContentChanged();
	}

	/**
	 * redoes the last action added into the redo stack
	 */	
	public void redoLastAction(){
		
		//deselecting all elements
		svgHandle.getSelection().clearSelection();
		
		if(redoStack.size()>0){
			
			//getting the lastly entered action list, and removes it from the stack
			final UndoRedoActionList actionList=redoStack.getLast();
			redoStack.remove(actionList);
			
			//adding this action list to the undo stack
			undoStack.add(actionList);
			
			//getting the list of all the elements that are handled in the action list
			Set<Element> allElements=new HashSet<Element>();
			
			for(UndoRedoAction action : actionList){
				
				if(action.getElements()!=null){
					
					allElements.addAll(action.getElements());
				}
			}
			
			//creating the runnable used for the execution
			Runnable executeRunnable=new Runnable(){
				
				public void run() {

					for(UndoRedoAction action : actionList){
						
						action.redo();
					}
				}
			};
			
			//launching the execution
			svgHandle.enqueue(executeRunnable, 
					allElements, actionList.isInvokeInAWTThread());
			
			//notifying the svg handle that something has changed on the canvas
			svgHandle.setModified(true);
		}
		
		//notifying that the handle has changed
		Editor.getEditor().getHandlesManager().handleContentChanged();
	}
	
	/**
	 * dispose the undo/redo manager
	 */
	public void dispose(){
		
		undoStack.clear();
		redoStack.clear();
		svgHandle=null;
	}
	
	/**
	 * @return the label of the last entered undo action list, or null if no action is available
	 */
	public String getUndoActionListLabel(){
		
		String label=null;
		
		if(undoStack.size()>0){
			
			label=undoStack.getLast().getName();
		}
		
		return label;
	}
	
	/**
	 * @return the label of the last entered redo action list, or null if no action is available
	 */
	public String getRedoActionListLabel(){
		
		String label=null;
		
		if(redoStack.size()>0){
			
			label=redoStack.getLast().getName();
		}
		
		return label;
	}
	
}
