package fr.itris.glips.svgeditor.shape;

import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;

import fr.itris.glips.library.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.popup.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;



import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the module handling the group nodes
 * @author Jordi SUC
 */
public class GroupShape extends MultiAbstractShape {
	
	/**
	 * the id for the unGroup action
	 */
	private String unGroupId="UnGroupShape";
	
	/**
	 * the labels
	 */
	private String unGroupLabel="";
	
	/**
	 * the tool tips
	 */
	//private String unGroupTooltip="";
	
	/**
	 * the undo/redo labels
	 */
	private String undoRedoUnGroupLabel="";
	
	/**
	 * the UnGroup icons
	 */
	private Icon unGroupIcon, unGroupDisabledIcon;
	
	/**
	 * the menu item
	 */
	private JMenuItem unGroupMenuItem;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public GroupShape(Editor editor) {
		
		super(editor);
		
		shapeModuleId="GroupShape";
		handledElementTagName="g";
		retrieveLabels();
		createMenuAndToolItems();
	}
	
	@Override
	protected void retrieveLabels() {

		super.retrieveLabels();

		unGroupLabel=ResourcesManager.bundle.getString(unGroupId+"ItemLabel");
		undoRedoUnGroupLabel=ResourcesManager.bundle.getString(unGroupId+"UndoRedoLabel");
	}
	
	@Override
	protected void createMenuAndToolItems() {

		//creating the listener to the menu and tool items
		ActionListener listener=new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				
				if(evt.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						evt.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					if(evt.getSource().equals(shapeCreatorMenuItem)){
						
						group();
						
					}else if(evt.getSource().equals(unGroupMenuItem)){
						
						ungroup();
					}
				}
			}
		};
		
		//getting the icons for the items
		shapeCreatorIcon=ResourcesManager.getIcon(shapeModuleId, false);
		shapeCreatorDisabledIcon=ResourcesManager.getIcon(shapeModuleId, true);
		unGroupIcon=ResourcesManager.getIcon(unGroupId, false);
		unGroupDisabledIcon=ResourcesManager.getIcon(unGroupId, true);
		
		//creating the menu items
		shapeCreatorMenuItem=new JMenuItem(itemLabel, shapeCreatorIcon);
		shapeCreatorMenuItem.setDisabledIcon(shapeCreatorDisabledIcon);
		shapeCreatorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, 
				java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		shapeCreatorMenuItem.addActionListener(listener);
		shapeCreatorMenuItem.setEnabled(false);
		
		unGroupMenuItem=new JMenuItem(unGroupLabel, unGroupIcon);
		unGroupMenuItem.setDisabledIcon(unGroupDisabledIcon);
		unGroupMenuItem.addActionListener(listener);
		unGroupMenuItem.setEnabled(false);
		
		//adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager=
			Editor.getEditor().getHandlesManager();
		
		svgHandleManager.addHandlesListener(new GroupHandlesListener());
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> menuItems=new HashMap<String, JMenuItem>();
		
		if(shapeCreatorMenuItem!=null){
			
			menuItems.put(shapeModuleId, shapeCreatorMenuItem);
			menuItems.put(unGroupId, unGroupMenuItem);
		}
		
		return menuItems;
	}
	
	@Override
	public Collection<PopupItem> getPopupItems() {

		Set<PopupItem> popupItems=new HashSet<PopupItem>();
		
		//creating the group popup item
		PopupItem groupItem=new PopupItem(
				Editor.getEditor(), shapeModuleId, itemLabel, shapeModuleId){
			
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				menuItem.setEnabled(false);
				
				//getting the current handle
				SVGHandle currentHandle=
					Editor.getEditor().getHandlesManager().getCurrentHandle();
				
				if(currentHandle!=null){
					
					//getting the set of the selected elements
					Set<Element> selectedElements=
						currentHandle.getSelection().getSelectedElements();
					
					if(selectedElements.size()>0){
						
						menuItem.setEnabled(true);
						menuItem.addActionListener(new ActionListener(){
							
							public void actionPerformed(ActionEvent e) {
								
								group();
							}
						});
					}
				}

				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(groupItem);
		
		//creating the group popup item
		PopupItem unGroupItem=new PopupItem(
				Editor.getEditor(), unGroupId, unGroupLabel, unGroupId){
			
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				menuItem.setEnabled(false);
				
				//getting the current handle
				SVGHandle currentHandle=
					Editor.getEditor().getHandlesManager().getCurrentHandle();
				
				if(currentHandle!=null){
					
					//getting the set of the selected elements
					Set<Element> selectedElements=
						currentHandle.getSelection().getSelectedElements();
					
					if(selectedElements.size()>0){

						boolean unGroupEnabled=true;
						
						for(Element element : selectedElements){
							
							if(! element.getNodeName().equals(handledElementTagName)){
								
								unGroupEnabled=false;
								break;
							}
						}

						if(unGroupEnabled){
							
							menuItem.setEnabled(true);
							
							menuItem.addActionListener(new ActionListener(){
								
								public void actionPerformed(ActionEvent e) {
									
									ungroup();
								}
							});
						}
					}
				}

				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(unGroupItem);
		
		return popupItems;
	}

	@Override
	public Set<Element> getElements(Set<Element> elements) {
		
		//creating the set of the children of the element that
		//is found in the provided set
		Element groupElement=elements.iterator().next();
		Set<Element> newElements=new HashSet<Element>();
		NodeList childNodes=groupElement.getChildNodes();
		Node node=null;

		for(int i=0; i<childNodes.getLength(); i++){
			
			node=childNodes.item(i);
			
			if(node!=null && node instanceof Element){
				
				newElements.add((Element)node);
			}
		}

		return newElements;
	}
	
	@Override
	public Rectangle2D getBounds(SVGHandle handle, Set<Element> elements){

		return handle.getSvgElementsManager().
			getNodeGeometryBounds(elements.iterator().next());
	}
	
	/**
	 * sets whether the items should be enabled or not
	 * @param enable whether the items should be enabled or not
	 */
	protected void setEnabled(boolean enable){
		
		setGroupEnabled(enable);
		setUnGroupEnabled(enable);
	}
	
	/**
	 * sets whether the group items should be enabled or not
	 * @param enable whether the group items should be enabled or not
	 */
	protected void setGroupEnabled(boolean enable){
		
		shapeCreatorMenuItem.setEnabled(enable);
	}
	
	/**
	 * sets whether the ungroup items should be enabled or not
	 * @param enable whether the ungroup items should be enabled or not
	 */
	protected void setUnGroupEnabled(boolean enable){
		
		unGroupMenuItem.setEnabled(enable);
	}
	
	/**
	 * groups the currently selected elements in the 
	 * currently selected svg handle
	 */
	protected void group(){
		
		//getting the current handle
		final SVGHandle currentHandle=
			Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		//getting the selected elements
		final Set<Element> selectedElements=
			new HashSet<Element>(currentHandle.getSelection().getSelectedElements());
		
		//getting the parent node
		final Element parentNode=currentHandle.getSelection().getParentElement();

		//creating the list of the child nodes of the parent element, 
		//in the order in which they are found in the document
		final LinkedList<Element> orderedElements=
			Toolkit.getChildrenElements(parentNode);
		
		//creating the new group node
		Document doc=parentNode.getOwnerDocument();
		final Element groupElement=doc.createElementNS(
				doc.getDocumentElement().getNamespaceURI(), handledElementTagName);
		
		//creating the execute runnable
		Runnable executeRunnable=new Runnable(){
			
			public void run() {

				//appending the child nodes to the group element
				for(Element element : orderedElements){
					
					if(selectedElements.contains(element)){
						
						parentNode.removeChild(element);
						groupElement.appendChild(element);
					}
				}
				
				//appending the group element to the parent node
				parentNode.appendChild(groupElement);
				
				currentHandle.getSelection().clearSelection();
				currentHandle.getSelection().handleSelection(groupElement, false, true);
			}
		};
		
		//creating the undo runnable
		Runnable undoRunnable=new Runnable(){
			
			public void run() {

				//removing the child nodes from the group element
				Element previousElement=null;
				
				for(Element element : orderedElements){
					
					if(selectedElements.contains(element)){
						
						groupElement.removeChild(element);

						if(previousElement!=null){

							parentNode.insertBefore(element, previousElement.getNextSibling());
							
						}else{

							parentNode.insertBefore(element, parentNode.getFirstChild());
						}
					}
					
					previousElement=element;
				}
				
				//removing the group element from the parent node
				parentNode.removeChild(groupElement);
			}
		};
		
		//creating the set of the elements that are modified
		Set<Element> elements=new HashSet<Element>(selectedElements);
		elements.add(groupElement);
		
		//creating the undo/redo action
		ShapeToolkit.addUndoRedoAction(
			currentHandle, undoRedoLabel, executeRunnable, 
				undoRunnable, elements);
	}
	
	/**
	 * ungroups the currently selected elements in the 
	 * currently selected svg handle
	 */
	protected void ungroup(){
		
		//getting the current handle
		final SVGHandle currentHandle=
			Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		//getting the selected elements
		final Set<Element> selectedElements=
			currentHandle.getSelection().getSelectedElements();
		
		//getting the parent node
		final Element parentNode=currentHandle.getSelection().getParentElement();
		
		//creating the set of the elements that will be modified
		Set<Element> elements=new HashSet<Element>(selectedElements);
		
		//creating the list of the child nodes of the parent element, 
		//in the order in which they are found in the document
		final LinkedList<Element> orderedElements=Toolkit.getChildrenElements(parentNode);
		
		final Map<Element, Element> groupNodesSuccessor=
			new HashMap<Element, Element>();
		
		for(Element element : orderedElements){
			
			groupNodesSuccessor.put(element, 
					EditorToolkit.getNextElementSibling(element));
		}
		
		//creating the map associating a group node to its child nodes
		final Map<Element, LinkedList<Element>> groupNodes=
			new HashMap<Element, LinkedList<Element>>();
		LinkedList<Element> childNodesList=null;
		NodeList groupChildNodes=null;
		Node node=null;
		
		for(Element groupElement : selectedElements){
			
			//getting the child nodes
			elements.add(groupElement);
			groupChildNodes=groupElement.getChildNodes();
			childNodesList=new LinkedList<Element>();
			groupNodes.put(groupElement, childNodesList);
			
			//filling the set of the child nodes
			for(int i=0; i<groupChildNodes.getLength(); i++){
				
				node=groupChildNodes.item(i);
				
				if(node!=null && node instanceof Element){
					
					childNodesList.add((Element)node);
					elements.add((Element)node);
				}
			}
		}
		
		//creating the execute runnable
		Runnable executeRunnable=new Runnable(){
			
			public void run() {

				//for each group node
				LinkedList<Element> groupChildNodesList=null;
				
				for(Element groupElement : groupNodes.keySet()){
					
					//getting the list of the child nodes of the group element
					groupChildNodesList=groupNodes.get(groupElement);
					
					for(Element childElement : groupChildNodesList){
						
						//appending the child element of the group node to the parent node
						parentNode.insertBefore(childElement, groupElement);
					}
					
					//removing the group element
					parentNode.removeChild(groupElement);
				}
				
				currentHandle.getSelection().clearSelection();
			}
		};
		
		//creating the undo runnable
		Runnable undoRunnable=new Runnable(){
			
			public void run() {

				//for each group node
				LinkedList<Element> groupChildNodesList=null;
				
				for(Element groupElement : groupNodes.keySet()){
					
					//getting the list of the child nodes of the group element
					groupChildNodesList=groupNodes.get(groupElement);
					
					for(Element childElement : groupChildNodesList){
						
						//appending the child element of the group node to the parent node
						parentNode.removeChild(childElement);
						groupElement.appendChild(childElement);
					}
					
					//appending the group element to the parent node
					Element nextSibling=groupNodesSuccessor.get(groupElement);
					parentNode.insertBefore(groupElement, nextSibling);
				}
			}
		};

		//creating the undo/redo action
		ShapeToolkit.addUndoRedoAction(
			currentHandle, undoRedoUnGroupLabel, executeRunnable, 
				undoRunnable, elements);
	}
	
	/**
	 * the class of the listener to svg handle modification
	 * @author Jordi SUC
	 */
	protected class GroupHandlesListener extends HandlesListener{
		
		/**
		 * the selection listener for the current handle
		 */
		private SelectionChangedListener selectionChangedListener;
		
		/**
		 * the last handle
		 */
		private SVGHandle lastHandle;
		
		@Override
		public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
		
			if(currentHandle!=null){
				
				//removing the last selection changed listener
				if(selectionChangedListener!=null && lastHandle!=null && lastHandle.getSelection()!=null){
					
					lastHandle.getSelection().removeSelectionChangedListener(
							selectionChangedListener);
				}
				
				selectionChanged(currentHandle);
				
				//adding a new selection changed listener
				selectionChangedListener=new SelectionChangedListener(){
					
					@Override
					public void selectionChanged(Set<Element> selectedElements) {
			
						GroupHandlesListener.this.selectionChanged(
								Editor.getEditor().getHandlesManager().getCurrentHandle());
					}
				};
				
				currentHandle.getSelection().addSelectionChangedListener(
						selectionChangedListener);
				
			}else{
				
				selectionChangedListener=null;
				setEnabled(false);
			}
			
			this.lastHandle=currentHandle;
		}
		
		/**
		 * called when the selection of the provided handle has changed
		 * @param currentHandle the current svg handle
		 */
		protected void selectionChanged(SVGHandle currentHandle){
			
			if(currentHandle!=null){
				
				//getting the set of the selected elements
				Set<Element> selectedElements=
					currentHandle.getSelection().getSelectedElements();
				
				setEnabled(false);
				
				if(selectedElements.size()>0){
					
					setGroupEnabled(true);
					boolean unGroupEnabled=true;
					
					for(Element element : selectedElements){
						
						if(! element.getNodeName().equals(handledElementTagName)){
							
							unGroupEnabled=false;
							break;
						}
					}
					
					setUnGroupEnabled(unGroupEnabled);
				}
			}
		}
	}
}
