package fr.itris.glips.rtdaeditor.anim.components;

import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.resources.*;
import java.awt.event.*;
import java.awt.*;

/**
 * the class of the animations and actions
 * @author ITRIS, Jordi SUC
 */
public class AnimationsAndActionsMenu {

	/**
	 * the restriction attribute name
	 */
	private static final String restrictionAtt="restriction";
	
	/**
	 * the single attribute value
	 */
	private static final String restrictionOneAttValue="1";
	
	/**
	 * the list of the menu items
	 */
	private LinkedList<JMenuItem> menuItems=new LinkedList<JMenuItem>();
	
	/**
	 * whether the menu should be used forcreating animations or actions
	 */
	private boolean isAnimations=true;
	
	/**
	 * the runnables used for disposing the menu items
	 */
	private HashSet<Runnable> disposeRunnables=new HashSet<Runnable>();
	
	/**
	 * the pop up menu
	 */
	private JPopupMenu popUpMenu=null;
	
	/**
	 * the current spec document
	 */
	private Document currentSpecDocument=null;
	
	/**
	 * the current source name
	 */
	private String currentSourceName="";
	
	/**
	 * the undo/redo label
	 */
	private static String undoRedoLabel="";
	
	/**
	 * the constructor of the class
	 * @param isAnimations whether the menu should be used forcreating animations or actions
	 */
	public AnimationsAndActionsMenu(boolean isAnimations) {
		
		this.isAnimations=isAnimations;

		//getting the undo/redo label
		undoRedoLabel=isAnimations?ItemObject.animationUndoRedoLabel:
			ItemObject.actionUndoRedoLabel;
	}
	
	/**
	 * cleans this object
	 * @param cleanAll whether all the object should be cleant
	 */
	public void clean(boolean cleanAll) {
		
		if(cleanAll) {
			
			//disposing all the previous menu items
			for(Runnable runnable : disposeRunnables) {
				
				runnable.run();
			}
			
			menuItems.clear();
			popUpMenu=null;
			currentSpecDocument=null;
		}
	}
	
	/**
	 * @param sourceName the current source name
	 */
	public void setCurrentSourceName(String sourceName) {
		this.currentSourceName=sourceName;
	}
	
	/**
	 * builds the menu items
	 * @param specDocument the specification 
	 * document used to build the menu items
	 * @param optionalBundle the bundle to use 
	 * when a label can't be found in the default bundle
	 * @param selectedElement the element that is selected on the canvas
	 */
	protected void buildMenuItems(Document specDocument, 
			ResourceBundle optionalBundle, final Element selectedElement) {

		this.currentSpecDocument=specDocument;
		
		//disposing all the previous menu items
		for(Runnable runnable : disposeRunnables) {
			
			runnable.run();
		}

		menuItems.clear();
		popUpMenu=null;
		
		if(specDocument!=null) {
			
			//getting the rtda animations document
			Document rtdaAnimationsDocument=specDocument;

			//getting all the group
			NodeList groups=rtdaAnimationsDocument.
				getDocumentElement().getElementsByTagName(
						isAnimations?"animationgroup":"actiongroup");
			
			Element group=null;
			JMenu groupMenu=null;
			String label="", prefix=fr.itris.glips.library.Toolkit.rtdaPrefix;
			NodeList nodes=null;

			for(int i=0; i<groups.getLength(); i++) {
				
				//getting the label 
				group=(Element)groups.item(i);
				label=group.getAttribute("name");
				label=getLabel("rtdaanim_menuItem_"+label, optionalBundle);
				
				//creating the menu
				groupMenu=new JMenu(label);
				menuItems.add(groupMenu);
				
				//the list of the animations or actions in the group
				nodes=group.getElementsByTagName(isAnimations?"animation":"action");

				for(int j=0; j<nodes.getLength(); j++) {

					final Element parent=(Element)nodes.item(j);
					label=parent.getAttribute("name");

					try {
						label=label.substring(label.indexOf(prefix)+prefix.length(), label.length());
						label=getLabel("rtdaanim_menuItem_"+label, optionalBundle);
					}catch (Exception ex) {}
					
					//creating the menu item corresponding to this animation or action
					final JMenuItem menuItem=new JMenuItem(label);
					groupMenu.add(menuItem);

					menuItem.setEnabled(false);
					
					//handling the state of the menu item
					if(parent.hasAttribute(restrictionAtt)) {
						
						if(selectedElement.getNodeName().equals(
								parent.getAttribute(restrictionAtt)) || 
									(parent.getAttribute(restrictionAtt).equals(restrictionOneAttValue) && 
										! Toolkit.hasAnimations(selectedElement))) {
							
							menuItem.setEnabled(true);
						}

					}else {
						
						menuItem.setEnabled(true);
					}
					
					//adding the action listener to the menu item
					final ActionListener actionListener=new ActionListener() {
						
						public void actionPerformed(ActionEvent evt) {

							//creating the animation
							createAnimationOrAction(parent, selectedElement);
						}
					};

					menuItem.addActionListener(actionListener);
					
					disposeRunnables.add(new Runnable() {

						public void run() {
							
							menuItem.removeActionListener(actionListener);
						}
					});
				}
			}
		}
	}
	
	/**
	 * returns the label corresponding to the provided id
	 * @param id an id
	 * @param optionalBundle the bundle to use when a label 
	 * can't be found in the default bundle
	 * @return the label corresponding to the provided id
	 */
	public String getLabel(String id, ResourceBundle optionalBundle){
		
		String label="";
		
		try {
			label=ResourcesManager.bundle.getString(id);
		}catch (Exception ex) {
			
			if(optionalBundle!=null){
				
				try{
					label=optionalBundle.getString(id);
				}catch (Exception e){}
			}
		}
		
		if(label==null || label.equals("")){
			
			label=id;
		}

		return label;
	}

	/**
	 * @return the currentSpecDocument
	 */
	public Document getCurrentSpecDocument() {
		return currentSpecDocument;
	}
	
	/**
	 * creates a new animation or action on the selected node
	 * @param specElement the spec element
	 * @param selectedElement the element that is selected on the canvas
	 */
	protected void createAnimationOrAction(
			Element specElement, final Element selectedElement) {

		if(specElement!=null && selectedElement!=null) {
			
			//creating the new node and its attributes
			final Element newElement=selectedElement.getOwnerDocument().
					createElementNS(fr.itris.glips.library.Toolkit.rtdaNameSpace, 
							specElement.getAttribute("name"));
			
			//setting the attributes for this new element
			Element att=null;
			String name="", defaultValue="";
			
			for(Node node=specElement.getFirstChild(); node!=null; node=node.getNextSibling()) {
				
				if(node instanceof Element && node.getNodeName().equals("attribute")) {

					att=(Element)node;
					defaultValue=att.getAttribute("defaultValue");
					name=att.getAttribute("name");

					if(! name.equals("") && ! defaultValue.equals("")) {
						
						newElement.setAttribute(name, defaultValue);
					}
				}
			}
			
			//setting the source name for this animation or action
			if(! currentSourceName.equals("")) {
				
				newElement.setAttribute(
					fr.itris.glips.library.Toolkit.sourceAttributeName, 
						currentSourceName);
			}

			final SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();

			//the runnable used for the execution 
			Runnable executeRunnable=new Runnable(){

				public void run() {

					//adding the new element as a child of the selected node
					selectedElement.appendChild(newElement);
					handle.getSvgDOMListenerManager().
						fireNodeInserted(selectedElement, newElement);
					handle.getSelection().handleSelection(selectedElement, false, true);
				}
			};
			
			//the runnable used for undoing 
			Runnable undoRunnable=new Runnable(){

				public void run() {

					//adding the new element as a child of the selected node
					selectedElement.removeChild(newElement);
					handle.getSvgDOMListenerManager().
						fireNodeRemoved(selectedElement, newElement);
					handle.getSelection().handleSelection(selectedElement, false, true);
				}
			};

			addUndoRedoAction(executeRunnable, undoRunnable, executeRunnable);
		}
	}
	
	/**
	 * @return the menu allowing to create animations or actions
	 */
	public JMenu getMenu() {
		
		JMenu menu=new JMenu("toto");
		
		//adding the menu items
		for(JMenuItem item : menuItems) {
			
			menu.add(item);
		}
		
		return menu;
	}
	
	/**
	 * creates a new popup menu
	 * @param specDocument a spec document
	 * @param optionalBundle the bundle to use when a label 
	 * can't be found in the default bundle
	 * @param selectedElement the element that is selected on the canvas
	 */
	public void createPopUpMenu(Document specDocument, 
				ResourceBundle optionalBundle, Element selectedElement) {

		buildMenuItems(specDocument, optionalBundle, selectedElement);
		popUpMenu=new JPopupMenu();
		
		//filling the popup menu
		for(JMenuItem menuItem : menuItems) {
			
			popUpMenu.add(menuItem);
		}
	}
	
	/**
	 * shows a pop up used to create an animation
	 * @param component a component
	 * @param mousePosition the position of the mouse 
	 */
	public void showPopupMenu(JComponent component, Point mousePosition) {

		//showing the pop up menu
		popUpMenu.show(component, mousePosition.x, mousePosition.y);
	}
	
	/**
	 * adds a new undo/redo action
	 * @param executeRunnable the runnable that is run for executing the action
	 * @param undoRunnable the undo runnable
	 * @param redoRunnable the redo runnable
	 */
	public void addUndoRedoAction(Runnable executeRunnable, 
			Runnable undoRunnable, Runnable redoRunnable) {
		
		//creating the undo/redo item
		UndoRedoAction action=new UndoRedoAction(
			undoRedoLabel, executeRunnable, undoRunnable, 
				redoRunnable, new HashSet<Element>());
		
		//creates the undo/redo list so that actions can be added to it
		UndoRedoActionList actionlist=new UndoRedoActionList(undoRedoLabel, false);
		actionlist.add(action);

		SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		handle.getUndoRedo().addActionList(actionlist, false);
	}
}
