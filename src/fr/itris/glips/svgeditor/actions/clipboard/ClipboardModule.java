/*
 * Created on 15 avr. 2004
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
package fr.itris.glips.svgeditor.actions.clipboard;

import fr.itris.glips.library.Toolkit;
import fr.itris.glips.svgeditor.actions.popup.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.shape.*;
import fr.itris.glips.svgeditor.*;
import org.w3c.dom.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

/**
 * @author ITRIS, Jordi SUC
 * the class managing all the copy, paste, cut, delete actions
 */
public class ClipboardModule extends ModuleAdapter{
    
	/**
	 * the ids of the module
	 */
	private static final String idClipboard="Clipboard", idCopy="Copy", 
		idPaste="Paste", idPastePos="PastePos", idCut="Cut", idDelete="Delete";
	
	/**
	 * the labels
	 */
	private String copyLabel="", pasteLabel="", pastePosLabel="", cutLabel="", deleteLabel="";
	
	/**
	 * the undo/redo labels
	 */
	private String undoRedoPasteLabel="", 
		undoRedoCutLabel="", undoRedoDeleteLabel="";
	
	/**
	 * the editor
	 */
	private Editor editor;
	
	/**
	 * the menu items that will be added to the menubar
	 */
	private JMenuItem copyMenuItem, pasteMenuItem, pastePosMenuItem, 
		cutMenuItem, deleteMenuItem;

	/**
	 * the listener the the copy, paste, cut and delete menu items
	 */
	private ActionListener copyListener, pasteListener, pastePosListener, 
		cutListener, deleteListener;
	
	/**
	 * the clipboard manager
	 */
	private ClipboardManager clipboardManager;

	/**
	 * the nodes that are currently selected
	 */
	private final Set<Element> selectedNodes=new HashSet<Element>();
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public ClipboardModule(Editor editor){
		
		this.editor=editor;
		this.clipboardManager=editor.getClipboardManager();
		
		//gets the labels from the resources
		ResourceBundle bundle=ResourcesManager.bundle;
		
		copyLabel=bundle.getString("Copy");
		pasteLabel=bundle.getString("Paste");
		pastePosLabel=bundle.getString("PastePos");
		cutLabel=bundle.getString("Cut");
		deleteLabel=bundle.getString("Delete");
		undoRedoPasteLabel=bundle.getString("UndoRedoPaste");
		undoRedoCutLabel=bundle.getString("UndoRedoCut");
		undoRedoDeleteLabel=bundle.getString("UndoRedoDelete");

		//a listener that listens to the changes of the svg handles
		final HandlesListener svgHandleListener=new HandlesListener(){
			
			/**
			 * a listener on the selection changes
			 */
			private SelectionChangedListener selectionListener;
			
			/**
			 * the last handle
			 */
			private SVGHandle lastHandle=null;
			
			@Override
			public void handleChanged(final SVGHandle currentHandle, Set<SVGHandle> handles) {
				
				if(lastHandle!=null && selectionListener!=null && lastHandle.getSelection()!=null){
					
					//if a selection listener is already registered on a selection module, it is removed	
					lastHandle.getSelection().removeSelectionChangedListener(selectionListener);
					selectionListener=null;
				}
				
				//clearing the selected nodes
				selectedNodes.clear();
				
				//disables the menuitems
				copyMenuItem.setEnabled(false);
				cutMenuItem.setEnabled(false);
				deleteMenuItem.setEnabled(false);
				pasteMenuItem.setEnabled(false);
				pastePosMenuItem.setEnabled(false);
				selectionListener=null;

				if(currentHandle!=null){

					if(clipboardManager.getClipboardContent().size()>0){
					    
					    pasteMenuItem.setEnabled(true);
					    pastePosMenuItem.setEnabled(true);
					}
					
					manageSelection(currentHandle, new HashSet<Element>(
							currentHandle.getSelection().getSelectedElements()));
					
					//the listener of the selection changes
					selectionListener=new SelectionChangedListener(){

						@Override
						public void selectionChanged(Set<Element> selectedElements) {
							
							manageSelection(currentHandle, selectedElements);
						}
					};
					
					//adds the selection listener
					if(selectionListener!=null){
					    
						currentHandle.getSelection().addSelectionChangedListener(selectionListener);
					}
				}
				
				lastHandle=currentHandle;
			}	
			
			/**
			 * updates the selected items and the state of the menu items
			 * @param handle the current handle
			 * @param selectedElements the currently selected elements
			 */
			protected void manageSelection(SVGHandle handle, Set<Element> selectedElements){

				//disabling the menuitems							
				copyMenuItem.setEnabled(false);
				cutMenuItem.setEnabled(false);
				deleteMenuItem.setEnabled(false);
				
				selectedNodes.clear();
				
				//refreshing the selected nodes list
				if(selectedElements!=null){
				    
				    selectedNodes.addAll(selectedElements);
				}

				if(selectedNodes.size()>0){
				    
					copyMenuItem.setEnabled(true);
					
					if(! handle.getSelection().isSelectionLocked()){
						
						cutMenuItem.setEnabled(true);
						deleteMenuItem.setEnabled(true);
					}
				}
			}
		};
		
		//adds the svg handles change listener
		editor.getHandlesManager().addHandlesListener(svgHandleListener);
		
		//getting the icons
		ImageIcon copyIcon=ResourcesManager.getIcon("Copy", false);
		ImageIcon dcopyIcon=ResourcesManager.getIcon("Copy", true);
		ImageIcon pasteIcon=ResourcesManager.getIcon("Paste", false);
		ImageIcon dpasteIcon=ResourcesManager.getIcon("Paste", true);
		ImageIcon pastePosIcon=ResourcesManager.getIcon("PastePos", false);
		ImageIcon dpastePosIcon=ResourcesManager.getIcon("PastePos", true);
		ImageIcon cutIcon=ResourcesManager.getIcon("Cut", false);
		ImageIcon dcutIcon=ResourcesManager.getIcon("Cut", true);
		ImageIcon deleteIcon=ResourcesManager.getIcon("Delete", false);
		ImageIcon ddeleteIcon=ResourcesManager.getIcon("Delete", true);

		//initializing the menuitems, the popup items and adds the listeners on them//
		
		copyMenuItem=new JMenuItem(copyLabel, copyIcon);
		copyMenuItem.setDisabledIcon(dcopyIcon);
		copyMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_C, 
						java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		copyMenuItem.setEnabled(false);

		copyListener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent e){

				if(e.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						e.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					copy();
					pasteMenuItem.setEnabled(true);
					pastePosMenuItem.setEnabled(true);
				}
			}
		};
		
		copyMenuItem.addActionListener(copyListener);
		
		pasteMenuItem=new JMenuItem(pasteLabel, pasteIcon);
		pasteMenuItem.setDisabledIcon(dpasteIcon);
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, 
				java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		pasteMenuItem.setEnabled(false);

		pasteListener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent e){

				if(e.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						e.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					paste(false, e.getModifiers()!=InputEvent.BUTTON1_DOWN_MASK && 
							e.getModifiers()!=InputEvent.BUTTON1_MASK);
				}
			}
		};
		
		pasteMenuItem.addActionListener(pasteListener);

		pastePosMenuItem=new JMenuItem(pastePosLabel, pastePosIcon);
		pastePosMenuItem.setDisabledIcon(dpastePosIcon);
		pastePosMenuItem.setEnabled(false);

		pastePosListener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent e){

				if(e.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						e.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					paste(true, false);
				}
			}
		};
		
		pastePosMenuItem.addActionListener(pastePosListener);
		
		cutMenuItem=new JMenuItem(cutLabel, cutIcon);
		cutMenuItem.setDisabledIcon(dcutIcon);
		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 
				java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		cutMenuItem.setEnabled(false);
		
		cutListener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent e){

				if(e.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						e.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					cut();
					pasteMenuItem.setEnabled(true);
				}
			}
		};
		
		cutMenuItem.addActionListener(cutListener);
		
		deleteMenuItem=new JMenuItem(deleteLabel, deleteIcon);
		deleteMenuItem.setDisabledIcon(ddeleteIcon);
		deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false));
		deleteMenuItem.setEnabled(false);

		deleteListener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent e){

				if(e.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						e.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					delete(true);
				}
			}
		};
		
		deleteMenuItem.addActionListener(deleteListener);
	}
	
	/**
	 * @return the editor
	 */
	public Editor getSVGEditor(){
	    
		return editor;
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> menuItems=new HashMap<String, JMenuItem>();
		menuItems.put(idCopy, copyMenuItem);
		menuItems.put(idPaste, pasteMenuItem);
		menuItems.put(idPastePos, pastePosMenuItem);
		menuItems.put(idCut, cutMenuItem);
		menuItems.put(idDelete, deleteMenuItem);
		
		return menuItems;
	}
	
	@Override
	public Collection<PopupItem> getPopupItems() {

		LinkedList<PopupItem> popupItems=new LinkedList<PopupItem>();
		
		//creating the copy popup item
		PopupItem item=new PopupItem(getSVGEditor(), idCopy, copyLabel, "Copy"){
		
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				if(nodes!=null && nodes.size()>0){
					
					menuItem.setEnabled(true);
					
					//adds the action listeners
					menuItem.addActionListener(copyListener);
					
				}else{
					
					menuItem.setEnabled(false);
				}

				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(item);
		
		//creating the paste popup item
		item=new PopupItem(getSVGEditor(), idPaste, pasteLabel, "Paste"){
		
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {
				
				if(clipboardManager.getClipboardContent().size()>0){
					
					menuItem.setEnabled(true);
					
					//adds the action listeners
					menuItem.addActionListener(pasteListener);
					
				}else{
					
					menuItem.setEnabled(false);
				}
				
				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(item);
		
		//creating the paste to same location popup item
		item=new PopupItem(getSVGEditor(), idPastePos, pastePosLabel, "PastePos"){
		
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {
				
				if(clipboardManager.getClipboardContent().size()>0){
					
					menuItem.setEnabled(true);
					
					//adds the action listeners
					menuItem.addActionListener(pastePosListener);
					
				}else{
					
					menuItem.setEnabled(false);
				}
				
				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(item);
		
		//creating the cut popup item
		item=new PopupItem(getSVGEditor(), idCut, cutLabel, "Cut"){
			
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {
				
				menuItem.setEnabled(false);
				
				if(nodes!=null && nodes.size()>0){
					
					//getting the current svg handle
	                SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
	                
	                if(handle!=null && ! handle.getSelection().isSelectionLocked()){
	                	
						menuItem.setEnabled(true);
						
						//adds the action listeners
						menuItem.addActionListener(cutListener);
	                }
				}

				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(item);
		
		//creating the delete popup item
		item=new PopupItem(getSVGEditor(), idDelete, deleteLabel, "Delete"){
			
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {
				
				menuItem.setEnabled(false);
				
				if(nodes!=null && nodes.size()>0){
					
					//getting the current svg handle
	                SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
	                
	                if(handle!=null && ! handle.getSelection().isSelectionLocked()){
	                	
						menuItem.setEnabled(true);
						
						//adds the action listeners
						menuItem.addActionListener(deleteListener);
	                }
				}

				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(item);

		return popupItems;
	}
	
	/**
	 * copies the selected nodes of the current handle into the clipboard
	 */
	public void copy(){
	    
		clipboardManager.clearClipboard();
	    Node clonedNode=null;
	    Rectangle2D bounds=null;
	
	    //getting the current handle
	    SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
	    
	    if(handle!=null){
	    
	    	//getting the parent element
			Element parent=handle.getSelection().getParentElement();
	    	
	    	//the map associating a cloned element to its bounds
	    	LinkedHashMap<Element, Rectangle2D> elementToBounds=
	    		new LinkedHashMap<Element, Rectangle2D>();
	    	
			final LinkedList<Element> docChildNodesList=
				Toolkit.getChildrenElements(parent);
	    	
			//creating the linkedlist of the elements to be handled
			final LinkedList<Element> elementsList=new LinkedList<Element>();

			for(Element element : docChildNodesList){
				
				if(selectedNodes.contains(element)){
					
					elementsList.add(element);
					
				}else if(selectedNodes.size()==elementsList.size()){
					
					break;
				}
			}
	    	
			//cloning the nodes in the list and storing their bounds 
			for(Element cur : elementsList){

				if(cur!=null){
				    
					bounds=handle.getSvgElementsManager().
						getNodeGeometryBounds(cur);
				    clonedNode=getClonedNodeWithoutUseNodes(cur);
				    
				    if(clonedNode!=null){
				        
				    	elementToBounds.put((Element)clonedNode, bounds);
				    }
				}
			}
			
			clipboardManager.addToClipboard(handle, elementToBounds);
	    }
	}
	
	/**
	 * pastes the copied nodes
	 * @param samePosition whether to paste on the same position as the copied node
	 * @param mousePosition whether to paste at the mouse position
	 */
	public void paste(boolean samePosition, boolean mousePosition){

		//getting the current handle
		final SVGHandle handle=editor.getHandlesManager().getCurrentHandle();

		if(clipboardManager.getClipboardContent().size()>0 && handle!=null){
			
			final Element parent=handle.getSelection().getParentElement();
			final Document doc=handle.getScrollPane().getSVGCanvas().getDocument();
			
		    //the list of the nodes to paste
		    final LinkedList<Element> nodesToPaste=new LinkedList<Element>();
		    
			//modifying the id of the nodes
			final LinkedList<Element> sourceNodesToPaste=
				new LinkedList<Element>(clipboardManager.getClipboardContent());
			Set<Element> alreadyHandledNodes=new HashSet<Element>();
			Set<Element> nodesToRemove=new HashSet<Element>();
			String sid="";
			SVGHandle clipboardHandle=clipboardManager.getSourceHandle();
			
			for(Element current : sourceNodesToPaste){

				if(current!=null){
					
					if(((clipboardHandle!=null && 
							clipboardHandle.equals(handle)) || 
							! Toolkit.hasJWidgetChildElement(current))){

						sid=handle.getSvgElementsManager().getId("", alreadyHandledNodes);
						current.setAttribute("id", sid);
						alreadyHandledNodes.add(current);

					}else{

						nodesToRemove.add(current);
					}
				}
			}
			
			//removing all the nodes that should not be pasted
			sourceNodesToPaste.removeAll(nodesToRemove);
			
			if(nodesToRemove.size()>0){
				
				//showing a message that some nodes cannot be 
				//pasted because they are rtda nodes
				
				//getting the labels
				String warningLabel=
					ResourcesManager.bundle.getString("WarningLabel");
				String messageLabel="";
				
				if(sourceNodesToPaste.size()==0){
					
					messageLabel=
						ResourcesManager.bundle.getString("NoRightNodeToPaste");
					
				}else{
					
					messageLabel=
						ResourcesManager.bundle.getString("WrongNodesExistInCopy");
				}
				
				//showing the information dialog notifying that not all 
				//the nodes or no node has been pasted
				JOptionPane.showMessageDialog(
					Editor.getParent(), messageLabel, warningLabel, 
						JOptionPane.WARNING_MESSAGE);
			}

			if(sourceNodesToPaste.size()>0){
				
	            //the defs node
	            final Element defs=
	            	handle.getSvgResourcesManager().getDefsElement();
	            
			    //the list of the resources used by the pasted nodes
	            Set<Element> resNodes=new HashSet<Element>();
			    
			    //the list of the resources imported into the current document
			    final Set<Element> usedResourceNodes=new HashSet<Element>();
				LinkedList<Element> resourceNodes=null;
				Element clonedElement=null;
				AbstractShape shapeModule=null;
				Point2D canvasSize=null;
				Rectangle2D canvasBounds=null;
				Point2D.Double translationFactors=null;
				Rectangle2D shapeBounds=null;
				UndoRedoAction translateAction=null;
				Set<Element> set;

				for(Element current : sourceNodesToPaste){

					if(current!=null){
					    
				        //getting all the resource nodes used by this node
				        resourceNodes=getResourcesUsedByNode(current, true);

					    //if the copied node does not belong to this svg document, it is imported
					    if(! current.getOwnerDocument().equals(doc)){

				            //for each resource node, check if it is contained in the list 
					    	//of the resources used by the copied nodes
				            for(Element res : resourceNodes){

				                if(res!=null && ! resNodes.contains(res)){
				                    
				                    resNodes.add(res);
				                }
				            }
					    }

					    //cloning the node
					    clonedElement=(Element)doc.importNode(current, true);
						nodesToPaste.add(clonedElement);
						
						//removing all the rtda elements if the current handle 
						//is not the one of the clipboard 
						if(clipboardHandle==null || ! clipboardHandle.equals(handle)){
							
							removeRtdaElements(clonedElement);
						}

						//handling the position of the node
						if(! samePosition){
							
							//getting the shape manager for this node
							shapeModule=ShapeToolkit.getShapeModule(clonedElement);
							
							if(shapeModule!=null){
								
								shapeBounds=clipboardManager.getClipboardContentMap().get(current);
								
								if(shapeBounds!=null){
									
									//translating the shape if it is necessary
									//computing the translation factors
									translationFactors=new Point2D.Double();
									
									//getting the size of the svg file
									canvasSize=handle.getCanvas().getGeometryCanvasSize();
									canvasBounds=new Rectangle2D.Double(
											0, 0, canvasSize.getX(), canvasSize.getY());
									
									if(mousePosition){
										
										//getting the mouse position
										Point mousePoint=handle.getCanvas().getMousePosition();
										
										if(mousePoint!=null){
											
											translationFactors.x=-shapeBounds.getX()+mousePoint.getX();
											translationFactors.y=-shapeBounds.getY()+mousePoint.getY();
										}
										
									}else if(! canvasBounds.intersects(shapeBounds)){
										
										translationFactors.x=-shapeBounds.getX()+
											canvasSize.getX()/2-shapeBounds.getWidth()/2;
										
										translationFactors.y=-shapeBounds.getY()+
											canvasSize.getY()/2-shapeBounds.getHeight()/2;

									}else{
										
										translationFactors.x=5;
										translationFactors.y=5;
									}
									
									if(translationFactors.getX()!=0 || translationFactors.getY()!=0){

										//translating the node
										set=new HashSet<Element>();
										set.add(clonedElement);
										translateAction=shapeModule.translate(handle, set, translationFactors);
										translateAction.execute();
									}
								}
							}
						}
					}
				}

				String resId="", newId="";
				
				//adding the resource nodes to the defs element
				for(Element res : resNodes){
				    
				    try{
				        res=(Element)doc.importNode(res, true);
				        resId=res.getAttribute("id");
				    }catch (DOMException ex) {
				    		ex.printStackTrace();
				    		res=null;
				    		resId=null;
				    	}
				    
				    if(res!=null && resId!=null){

				        if(! handle.getSvgElementsManager().checkId(resId)){
				            
				            //creating the new id
				            newId=handle.getSvgElementsManager().getId(resId, null);
				            
				            //modifying the id of the resource
				            res.setAttribute("id", newId);
				            modifyReferencedResourceId(handle, 
				            	new HashSet<Element>(nodesToPaste), resId, newId);
				        }
				        
				        //adding the resource node to the list of the resource nodes
				        usedResourceNodes.add(res);
				    }
				}
				
				//checking if the sub tree of the pasted nodes requires a specific name space
				for(Element current : nodesToPaste){

				    if(current!=null){
				    	
				        checkNamespace(current);
				    }
				}
				
				//the runnable that will be executed
				final Runnable executeRunnable=new Runnable(){
					
					public void run() {
						
						handle.getSelection().clearSelection();
						
						//appending the resources
						for(Element current : usedResourceNodes){

							if(current!=null){
							    
							    defs.appendChild(current);
							}
						}

						//appending the children
						for(Element current : nodesToPaste){

							if(current!=null){
							    
							    parent.appendChild(current);
							    
								//registering the current node to the used 
							    //resources map if it uses a resource
								handle.getSvgResourcesManager().
									registerUsedResource(current);
							}
						}
						
						//refreshing the properties and the resources handle
						handle.getSelection().handleSelection(
							new HashSet<Element>(nodesToPaste), true, true);
						getSVGEditor().getHandlesManager().handleChanged();
					}
				};
				
				//the runnable used for undoing
				final Runnable undoRunnable=new Runnable(){
					
					public void run() {
						
						//removing the added children from the parent node
						for(Element current : nodesToPaste){

							if(current!=null){
							    
							    parent.removeChild(current);
							    
								//unregister the current node to the used resources 
							    //map if it uses a resource
								handle.getSvgResourcesManager().
									unregisterAllUsedResource(current);
							}
						}
						
						//removing the added resources
						for(Element current : usedResourceNodes){

							if(current!=null){
							    
							    defs.removeChild(current);
							}
						}
						
						//refreshing the properties and the resources handle
						handle.getSelection().refreshSelection(true);
						getSVGEditor().getHandlesManager().handleChanged();
					}
				};

				//adding the undo/redo action list
				UndoRedoAction action=new UndoRedoAction(
					undoRedoPasteLabel, executeRunnable, 
						undoRunnable, executeRunnable, new HashSet<Element>(nodesToPaste));
			
				//creating the undo/redo list and adds the action to it					
				UndoRedoActionList actionlist=
					new UndoRedoActionList(action.getName(), false);
				actionlist.add(action);
				handle.getUndoRedo().addActionList(actionlist, true);
			}
		}
	}
	
	/**
	 * removes all the rtda elements that could be found under this element
	 * @param element a rtda element
	 */
	protected void removeRtdaElements(Element element){
		
        if(element!=null && element.hasChildNodes()){
            
            NodeList children=element.getChildNodes();
            LinkedList<Element> childrenList=Toolkit.getLinkedList(children);
            
            for(Element el : childrenList){
                
                if(el.getNodeName().startsWith(Toolkit.rtdaPrefix)){
                    
                    //if the node is a rtda node, it is removed
                	element.removeChild(el);
                    
                }else{
                    
                    //if the node is not a rtda node, its subtree is checked
                	removeRtdaElements(el);
                }
            }
        }
	}

	/**
	 * modifies the referenced resource id in the elements properties and their child nodes
	 * @param handle a svg handle
	 * @param elements a set of elements
	 * @param oldId the old id of a resource
	 * @param newId the new id of a resource
	 */
	protected void modifyReferencedResourceId(
			SVGHandle handle, Set<Element> elements, String oldId, String newId){
		
        //for each pasted node, modifying the name of the resource it uses
		String style="";
		Node node=null;
		Element element=null;
		
        for(Element current : elements){

            if(current!=null){
                
                style=current.getAttribute("style");

                if(style!=null && style.indexOf("#".concat(oldId))!=-1){
                    
                    style=style.replaceAll("#".concat(oldId)+"[)]", "#".concat(newId)+")");
                    current.setAttribute("style", style);
                    handle.getSvgResourcesManager().addNodeUsingResource(newId, current);
                }
                
                //modifying the nodes in the subtree under the pasted node
                for(NodeIterator nodeIt=new NodeIterator(current); nodeIt.hasNext();){
                	
                	node=nodeIt.next();
                	
                	if(node!=null && ! node.equals(current) && 
                	        node instanceof Element && ((Element)node).hasAttribute("style")){
                		
                		element=(Element)node;
	                    style=element.getAttribute("style");

	                    if(style!=null && style.indexOf("#".concat(oldId))!=-1){
	                        
	                        style=style.replaceAll("#".concat(oldId)+"[)]", "#".concat(newId)+")");
	                        element.setAttribute("style", style);
	                        handle.getSvgResourcesManager().addNodeUsingResource(newId, node);
	                    }
                	}
                }
            }
        }
	}
	
	/**
	 * checking if the sub tree of the node requires a specific name space
	 * @param element an element
	 */
	protected void checkNamespace(Element element){
		
		Map<String, String> nameSpaces=
			new HashMap<String, String>(Editor.requiredNameSpaces);
		
		NodeIterator nit=new NodeIterator(element);
		Node cur;
		String prefix="", nsp="";
		int pos=0;

        //checking if the nodes under the pasted nodes use a specific name space
        do{
        	
        	cur=nit.hasNext()?nit.next():element;

        	if(cur!=null && cur instanceof Element){

        		pos=cur.getNodeName().indexOf(":");
        		
        		if(pos!=-1){
        			
        			prefix=cur.getNodeName().substring(0, pos);
        			nsp=nameSpaces.get(prefix);
        			
        			if(nsp!=null){
        				
        				EditorToolkit.checkXmlns(cur.getOwnerDocument(), prefix, nsp);
        				nameSpaces.remove(prefix);
        			}
        		}
        	}
        	
        }while(nit.hasNext());
	}
	
	/**
	 * cuts the current selection
	 */
	public void cut(){
	    
		//copies the nodes
		copy();
		
		//removes the nodes
		delete(false);
	}
	
	/**
	 * deletes the selected nodes
	 * @param isDelete whether it is used in the delete action or in the cut action
	 */
	public void delete(boolean isDelete){
		
		final SVGHandle handle=editor.getHandlesManager().getCurrentHandle();
		
		if(selectedNodes.size()>0){

			//cloning the selected nodes set
			Set<Element> clonedSelectedNodesSet=
				new HashSet<Element>(selectedNodes);
			
			//the parent node
			final Node parent=handle.getSelection().getParentElement();
			
			//creating the ordered list of the selected nodes 
			//(the list order is the one that is found in the parent)
			final LinkedList<Element> elementsToDelete=
				new LinkedList<Element>();
			
			//creating the map associating an element to its next element
			final Map<Element, Element> nextNodesMap=
				new HashMap<Element, Element>();
			
			LinkedList<Element> childNodesList=
				Toolkit.getChildrenElements((Element)parent);
			int i=0;
			
			for(Element el : childNodesList){

				if(clonedSelectedNodesSet.contains(el)){
					
					elementsToDelete.add(el);
					nextNodesMap.put(el, 
						(((i+1)<childNodesList.size())?childNodesList.get(i+1):null));
				}
				
				i++;
			}
			
			//reverting the list of the elements to delete
			Collections.reverse(elementsToDelete);

			//the runnable that will be executed
			final Runnable executeRunnable=new Runnable(){
				
				public void run() {

					//removing the children from the parent element					
					for(Element element : elementsToDelete){

						if(element!=null){
						    
						    parent.removeChild(element);
							
							//register the current node to the used 
						    //resources map if it uses a resource
							handle.getSvgResourcesManager().
								unregisterAllUsedResource(element);
						}
					}
					
					handle.getSelection().refreshSelection(true);
				}
			};
			
			//the runnable used for the undo/redo action
			final Runnable undoRunnable=new Runnable(){
				
				public void run() {

					//re-adding the children to the parent element
					Element nextElement=null;
					
					for(Element element : elementsToDelete){

						if(element!=null){
							
							//getting the next element of the current element
							nextElement=nextNodesMap.get(element);
							
							//inserting the element
							if(nextElement!=null){
								
								parent.insertBefore(element, nextElement);
								
							}else{
								
								parent.appendChild(element);
							}

							//register the current node to the used
							//resources map if it uses a resource
							handle.getSvgResourcesManager().
								registerUsedResource(element);
						}
					}
					
					handle.getSelection().refreshSelection(true);
				}
			};

			//adding the undo/redo action list
			UndoRedoAction action=new UndoRedoAction(
				isDelete?undoRedoDeleteLabel:undoRedoCutLabel, 
					executeRunnable, undoRunnable, executeRunnable, 
						new HashSet<Element>(elementsToDelete));
	
			//creates the undo/redo list and adds the action to it					
			UndoRedoActionList actionlist=new UndoRedoActionList(action.getName(), false);
			actionlist.add(action);
			handle.getUndoRedo().addActionList(actionlist, true);
		}
	}
	
    /**
     * returns the cloned node of the given node whose use nodes have been removed
     * @param node a node
     * @return the cloned node of the given node whose use nodes have been removed
     */
    public Node getClonedNodeWithoutUseNodes(Node node){
        
        Node clonedNode=null;
        
        if(node!=null){
            
            clonedNode=node.cloneNode(true);
            
            if(! clonedNode.getNodeName().equals("use")){
                
                //removes the use nodes from the subtree of the cloned node
                removeUseNodes(clonedNode);
                
            }else{
                
                clonedNode=null;
            }
        }
        
        return clonedNode;
    }
    
    /**
     * removes the use nodes in the given nodes
     * @param node a node
     */
    protected void removeUseNodes(Node node){
        
        if(node!=null && node.hasChildNodes()){
            
            NodeList children=node.getChildNodes();
            LinkedList<Element> childrenList=Toolkit.getLinkedList(children);
            
            for(Element element : childrenList){
                
                if(element.getNodeName().equals("use")){
                    
                    //if the node is a use node, it is removed
                    node.removeChild(element);
                    
                }else{
                    
                    //if the node is not a use node, its subtree is checked
                    removeUseNodes(element);
                }
            }
        }
    }
    
    /**
     * creates the list of the resources used by the given node and returns it
     * @param element an element
     * @param deep true if the children of the given node should be inspected
     * @return the list of the resources used by the given node
     */
    public LinkedList<Element> getResourcesUsedByNode(Element element, boolean deep){
        
        LinkedList<Element> resources=new LinkedList<Element>();
        
        if(element!=null){
            
            //getting the defs element
            Element root=element.getOwnerDocument().getDocumentElement();
            Node cur=null;
            Element defs=null;
            
            for(cur=root.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
                
                if(cur instanceof Element && cur.getNodeName().equals("defs")){
                    
                    defs=(Element)cur;
                }
            }
            
            //the string containing the ids of the resources needed
            String style=element.getAttribute("style");
            
            if(deep){
                
                for(NodeIterator it=new NodeIterator(element); it.hasNext();){
                    
                    cur=it.next();
                    
                    if(cur instanceof Element){
                        
                        style=style.concat(((Element)cur).getAttribute("style"));
                    }
                }
            }
            
            if(defs!=null && style!=null && ! style.equals("")){
                
                String id="";
                Element el=null;
                
                //for each child of the "defs" element, adds it to the list if it is used by the given element
                for(cur=defs.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
                    
                    if(cur instanceof Element){
                        
                        el=(Element)cur;
                        id=el.getAttribute("id");
                        
                        //if the id of the resource is contained in the style attribute
                        if(id!=null && style.indexOf("#".concat(id))!=-1){
                            
                            resources.add(el);
                        }
                    }
                }
            }
        }
        
        return resources;
    }

	/**
	 * gets the name of the module
	 * @return the name of the module
	 */
	public String getName(){
	    
		return idClipboard;
	}
}
