/*
 * Created on 25 mars 2004
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
package fr.itris.glips.svgeditor.display.handle;

import javax.swing.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;
import java.awt.event.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.menubar.*;
import fr.itris.glips.svgeditor.display.canvas.grid.*;
import fr.itris.glips.svgeditor.display.canvas.rulers.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class that manages the svg handles, that manages the svg canvases
 * @author ITRIS, Jordi SUC
 */
public class HandlesManager {
	
	/**
	 * a svg handle disposer
	 */
	public static HandlesDisposer handleDisposer=new HandlesDisposer();
	
	/**
	 * the handler of the grid parameters
	 */
	private GridParametersManager gridParametersManager;
	
	/**
	 * the handler of the rulers parameters
	 */
	private RulersParametersManager rulersParametersManager;
	
	/**
	 * the ids
	 */
	final private String idmenuframe="menuframe";
	
	/**
	 * the list of the svg handles that are handled
	 */
	private List<SVGHandle> handles=
		new CopyOnWriteArrayList<SVGHandle>();
	
	/**
	 * the current svg handle, whose frame is displayed
	 */
	private SVGHandle currentHandle;
	
	/**
	 * the editor
	 */
	private Editor editor;
	
	/**
	 * the set of the listeners on the changes of the svg handles
	 */
	private Set<HandlesListener> handlesListeners=
		new CopyOnWriteArraySet<HandlesListener>();
	
	/**
	 * the group that manages the menu items
	 */
	private ButtonGroup group=new ButtonGroup();
	
	/**
	 * the figure associated with a handle in the menu
	 */
	private int handleFigure=0;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public HandlesManager(Editor editor) {
	    
		this.editor=editor;
		this.gridParametersManager=new GridParametersManager(this);
		this.rulersParametersManager=new RulersParametersManager(this);
	}
	
	/**
	 * initializes some parts after the modules are initialized
	 */
	public void initializeParts(){
		
		//getting the icons
		ImageIcon icon=ResourcesManager.getIcon("Window", false);
		ImageIcon disabledIcon=ResourcesManager.getIcon("Window", true);
		
		//getting the menu
		JMenu menu=editor.getMenuBar().getMenu(idmenuframe);
		
		if(menu!=null){

			menu.setIcon(icon);
			menu.setDisabledIcon(disabledIcon);
			editor.getMenuBar().build();
		}
	}
	
	/**
	 * @return whether the key strokes act on 
	 * an open and selected svg frame
	 */
	public boolean keyStrokeActsOnSVGFrame(){
		
		return currentHandle!=null && 
			currentHandle.getSVGFrame().isSelected();
	}
	
	/**
	 * adds a listener called when the current svg handle changes
	 * @param listener the listener to be added
	 */
	public void addHandlesListener(HandlesListener listener){
	    
		handlesListeners.add(listener);
	}
	
	/**
	 * removes a listener called when the current svg handle changes
	 * @param listener the listener to be removed
	 */
	public void removeHandlesListener(HandlesListener listener){
	    
		handlesListeners.remove(listener);
	}
	
	/**
	 * notifies the listeners when a svg handle has changed
	 */
	public void handleChanged(){

		Runnable runnable=new Runnable(){
			
			public void run() {

                for(HandlesListener listener : handlesListeners){
                    
                    if(listener!=null){
                        
                        listener.handleChanged(currentHandle, 
                        		new HashSet<SVGHandle>(handles));
                    }
                }

                if(currentHandle!=null){
                    
                    currentHandle.getSVGFrame().getFrameMenuItem().setSelected(true);
                }
			}
		};
		
		if(SwingUtilities.isEventDispatchThread()){
			
			runnable.run();
			
		}else{
			
	        	try {
					SwingUtilities.invokeAndWait(runnable);
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.out.println("Interrupted while invoking " + runnable + ": " + e);
					Thread.currentThread().interrupt();
				}
		}
	}
	
	/**
	 * notifies the listeners when a svg handle has changed
	 */
	public void handleContentChanged(){

        for(HandlesListener listener : handlesListeners){
            
            if(listener!=null){
                
                listener.handleContentChanged(currentHandle);
            }
        }
	}
	
	/**
	 * @param name the name of a svg handle
	 * @return a svg handle given its name
	 */
	public SVGHandle getHandle(String name){

		for(SVGHandle current : handles){

			if(current.getName()!=null &&
					current.getName().equals(name)){
			    
			    return current;
			}
		}

		return null;
	}
	
	/**
	 * @return the number of the available svg handles
	 */
	public int getHandlesNumber(){
		return handles.size();
	}
	
	/**
	 * @return a collection of all the available svg handles
	 */
	public Collection<SVGHandle> getHandles(){
		
		LinkedList<SVGHandle> list=new LinkedList<SVGHandle>();
		list.addAll(handles);
		
		return list;
	}
	
	/**
	 * adds the svg handle's menu item into the menu bar
	 * @param handle the svg handle whose menuitem will be inserted into the menu bar
	 */
	protected void addHandleInMenu(SVGHandle handle){
		
		final SVGHandle hndl=handle;
		EditorMenuBar menubar=editor.getMenuBar();
		
		//gets or create the menu that will contain the menu item
		//adds the action to the menu item
		handle.getSVGFrame().getFrameMenuItem().addActionListener(new ActionListener(){		
			
			public void actionPerformed(ActionEvent e){
			    
				HandlesManager.this.setCurrentHandle(hndl.getName());
			}	
		});
		
		if(menubar!=null){
			
			if(handleFigure<10){
			    
				handle.getSVGFrame().getFrameMenuItem().setAccelerator(
						KeyStroke.getKeyStroke("ctrl NUMPAD"+handleFigure));
				handleFigure++;
			}
			
			menubar.addUnknownMenuItem(idmenuframe, handle.getSVGFrame().getFrameMenuItem());
			group.add(handle.getSVGFrame().getFrameMenuItem());
			handle.getSVGFrame().getFrameMenuItem().setSelected(true);
			menubar.build();
		}
	}
	
	/**
	 * removes the svg handle's menu item from the menu bar
	 * @param handle the svg handle whose menuitem will be removed from the menu bar
	 */
	protected void removeHandleInMenu(SVGHandle handle){
		
		EditorMenuBar menubar=editor.getMenuBar();
		
		if(handle!=null){
			
			menubar.removeUnknownMenuItem(
					idmenuframe, handle.getSVGFrame().getFrameMenuItem());
			group.remove(handle.getSVGFrame().getFrameMenuItem());	
			
			menubar.build();
		}
	}
	
	/**
	 * creates a new svg handle
	 * @param name the name of the new svg handle
	 * @return the new svg handle
	 */
	public SVGHandle createSVGHandle(String name){
		
		if(name!=null && ! name.equals("")){
		    
			//checks if the name is correct
			//if another handle has the same name
			//a number is concatenated at the end of the name
			int count=countName(name), nb=0;
			
			while(count>0){
			    
				count=countName(name+" ("+new Integer(nb+1)+")");
				nb++;
			}
			
			if(nb>0){
			    
			    name=name+" ("+new Integer(nb)+")";
			}
			
			SVGHandle handle=new SVGHandle(name);

			//adds the svg handles in the list
			handles.add(handles.size(), handle);
			
			if(currentHandle!=null){
			    
				//hides the current svg handle
				currentHandle.getSVGFrame().moveFrameToBack();
			}
			
			currentHandle=handle;
			addHandleInMenu(handle);
			handle.getSVGFrame().moveFrameToFront();

			return handle;
		}
        
        return null;
	}
	
	/**
	 * modifies the name of the handle with the new provided name
	 * @param oldName the current name of the handle
	 * @param newName the new name of the handle
	 */
	public void changeName(String oldName, String newName){
		
		SVGHandle handle=getHandle(oldName);
		
		if(newName!=null && ! newName.equals("")){
		    
			//checks if the name is correct
			//if another handle has the same name,
			//a number is concatenated at the end of the name
			int count=countName(newName), nb=0;
			
			while(count>0){
			    
				count=countName(newName+" ("+new Integer(nb+1)+")");
				nb++;
			}
			
			if(nb>0){
			    
			    newName=newName+" ("+new Integer(nb)+")";
			}
			
			handle.setName(newName);
		}
		
		handleChanged();
	}
	
	/**
	 * sets the svg handle denoted by the given name as the current handle
	 * @param name the name of the svg handle
	 */
	public void setCurrentHandle(String name){
		
		SVGHandle handle=getHandle(name);
		
		if(handle!=null){
			
			if(currentHandle!=null){
			    
				currentHandle.getSVGFrame().moveFrameToBack();
			}
			
			currentHandle=handle;
			handles.remove(handle);
			handles.add(handles.size(), handle);
			handle.getSVGFrame().moveFrameToFront();
			
			//notifies that the current handle has changed
			handleChanged();
		}
	}
	
	/**
	 * @return the handle whose frame containing the svg canvas is currently selected
	 */
	public SVGHandle getCurrentHandle(){
		return currentHandle;
	}
	
	/**
	 * removes the handle from the list : the frame containing the svg canvas will be hidden
	 * @param name the name of the svg handle to remove
	 */
	public void removeHandle(String name){

		SVGHandle handle=getHandle(name);

		if(handle!=null){//TODO
		    
		    removeHandleInMenu(handle);
			handle.getSVGFrame().moveFrameToBack();
			handle.getSVGFrame().removeFromDesktop();
			handles.remove(handle);
			
			//if it is not the last handle in the list, another handle is taken into account
			if(handles.size()>0){
			    
				setCurrentHandle(handles.get(handles.size()-1).getName());
				
			}else{
				
				currentHandle=null;
                
                //notifies that the current handle has changed       
                handleChanged();
			}

			handle.dispose();
			
			if(editor.getClipboardManager()!=null && 
					editor.getClipboardManager().getSourceHandle()!=null &&
						editor.getClipboardManager().getSourceHandle().equals(handle)){
				
				editor.getClipboardManager().clearSourceHandle();
			}
		}
	}

	/**
	 * removes the handle from the list : the frame containing the svg canvas will be hidden
	 * @param name the name of the svg handle to remove
	 */
	public void removeHandleWithoutDisposing(String name){

		SVGHandle handle=getHandle(name);

		if(handle!=null){
		    
		    removeHandleInMenu(handle);
			handle.getSVGFrame().moveFrameToBack();
			handle.getSVGFrame().removeFromDesktop();
			handles.remove(handle);
			
			//if it is not the last svg handle in the list, another handle is taken into account
			if(handles.size()>0){
			    
				setCurrentHandle(handles.get(handles.size()-1).getName());
				
			}else{
				
				currentHandle=null;
                
                //notifies that the current handle has changed       
                handleChanged();
			}
		}
	}
	
	/**
	 * returns the number of svg handles having the same name as "name"
	 * @param name the name of a svg handle
	 * @return the number of svg handle having the same name as "name"
	 */
	protected int countName(String name){

		int nb=0;
		
		for(SVGHandle handle : handles){

			if(handle.getName()!=null && handle.getName().equals(name)){
			    
			    nb++;
			}
		}
		
		return nb;
	}

	/**
	 * @return the grid parameters manager
	 */
	public GridParametersManager getGridParametersHandler() {
		
		return gridParametersManager;
	}
	
	/**
	 * @return the rulers parameters manager
	 */
	public RulersParametersManager getRulersParametersHandler() {
		
		return rulersParametersManager;
	}
}
