package fr.itris.glips.svgeditor.selection;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.popup.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the module for the selection
 * @author ITRIS, Jordi SUC
 */
public class SelectionModule extends ModuleAdapter {

	/**
	 * the id for the regular selection mode
	 */
	private String regularSelectionId="RegularSelection";
	
	/**
	 * the id for the zone selection mode
	 */
	private String zoneSelectionId="ZoneSelection";
	
	/**
	 * the id for the all selection mode
	 */
	private String allSelectionId="AllSelection";
	
	/**
	 * the id for the deselection all mode
	 */
	private String allDeselectionId="AllDeselection";
	
	/**
	 * the id for the enter group action
	 */
	private String enterGroupId="EnterGroup";
	
	/**
	 * the id for the exit group action
	 */
	private String exitGroupId="ExitGroup";
	
	/**
	 * the id for the lock action
	 */
	private String lockId="Lock";
	
	/**
	 * the id for the unlock action
	 */
	private String unLockId="UnLock";
	
	/**
	 * the icons for the regular selection items
	 */
	protected Icon regularSelectionIcon, regularSelectionDisabledIcon;
	
	/**
	 * the icons for the zone selection mode
	 */
	protected Icon zoneSelectionIcon, zoneSelectionDisabledIcon;
	
	/**
	 * the icons for the all selection mode
	 */
	protected Icon allSelectionIcon, allSelectionDisabledIcon;
	
	/**
	 * the icons for the all deselection mode
	 */
	protected Icon allDeselectionIcon, allDeselectionDisabledIcon;
	
	/**
	 * the icons for the enter group action
	 */
	protected Icon enterGroupIcon, enterGroupDisabledIcon;
	
	/**
	 * the icons for the exit group action
	 */
	protected Icon exitGroupIcon, exitGroupDisabledIcon;
	
	/**
	 * the icons for the lock action
	 */
	protected Icon lockIcon, lockDisabledIcon;
	
	/**
	 * the icons for the unlock action
	 */
	protected Icon unLockIcon, unLockDisabledIcon;
	
	/**
	 * the menu item that is displayed in the menu bar to handle regular selections
	 */
	protected JMenuItem regularSelectionMenuItem;
	
	/**
	 * the menu item that is displayed in the menu bar to handle zone selections
	 */
	protected JMenuItem zoneSelectionMenuItem;
	
	/**
	 * the menu item that is displayed in the menu bar to handle all selection actions
	 */
	protected JMenuItem allSelectionMenuItem;
	
	/**
	 * the menu item that is displayed in the menu bar to handle deselections
	 */
	protected JMenuItem allDeselectionMenuItem;
	
	/**
	 * the menu item that is displayed in the menu bar to handle enter group actions
	 */
	protected JMenuItem enterGroupMenuItem;
	
	/**
	 * the menu item that is displayed in the menu bar to handle exit group actions
	 */
	protected JMenuItem exitGroupMenuItem;
	
	/**
	 * the menu item that is displayed in the menu bar to handle lock actions
	 */
	protected JMenuItem lockMenuItem;
	
	/**
	 * the menu item that is displayed in the menu bar to handle unlock actions
	 */
	protected JMenuItem unLockMenuItem;
	
	/**
	 * the tool item that is displayed in the toolbar to handle regular selections
	 */
	protected JToggleButton regularSelectionToolItem;
	
	/**
	 * the tool item that is displayed in the toolbar to handle zone selections
	 */
	protected JToggleButton zoneSelectionToolItem;
	
	/**
	 * the label for the regular selection menu and tool item
	 */
	protected String regularSelectionLabel="";
	
	/**
	 * the label for the zone selection menu and tool item
	 */
	protected String zoneSelectionLabel="";
	
	/**
	 * the label for the all selection menu item
	 */
	protected String allSelectionLabel="";
	
	/**
	 * the label for the deselection menu item
	 */
	protected String allDeselectionLabel="";
	
	/**
	 * the label for the enter group action items
	 */
	protected String enterGroupLabel="";
	
	/**
	 * the label for the exit group action items
	 */
	protected String exitGroupLabel="";
	
	/**
	 * the label for the lock action items
	 */
	protected String lockLabel="";
	
	/**
	 * the label for the unlock action items
	 */
	protected String unLockLabel="";
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public SelectionModule(Editor editor) {
		
		createMenuAndToolItems();
	}

	/**
	 * creates the menu and tool items
	 */
	protected void createMenuAndToolItems() {
		
		//getting the label
		regularSelectionLabel=ResourcesManager.bundle.getString(regularSelectionId+"ItemLabel");
		zoneSelectionLabel=ResourcesManager.bundle.getString(zoneSelectionId+"ItemLabel");
		allSelectionLabel=ResourcesManager.bundle.getString(allSelectionId+"ItemLabel");
		allDeselectionLabel=ResourcesManager.bundle.getString(allDeselectionId+"ItemLabel");
		enterGroupLabel=ResourcesManager.bundle.getString(enterGroupId+"ItemLabel");
		exitGroupLabel=ResourcesManager.bundle.getString(exitGroupId+"ItemLabel");
		lockLabel=ResourcesManager.bundle.getString(lockId+"ItemLabel");
		unLockLabel=ResourcesManager.bundle.getString(unLockId+"ItemLabel");
		
		//creating the listener to the regular and zone menu and tool items
		ActionListener selectionListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				
				if(evt.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						evt.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					//getting the new mode to set 
					int mode=SelectionInfoManager.REGULAR_MODE;
					
					if(evt.getSource().equals(zoneSelectionMenuItem) || 
							evt.getSource().equals(zoneSelectionToolItem)){
						
						mode=SelectionInfoManager.ZONE_MODE;
					}
					
					if(evt.getSource().equals(zoneSelectionMenuItem)){
						
						//selecting the zone tool item
						zoneSelectionToolItem.removeActionListener(this);
						zoneSelectionToolItem.setSelected(true);
						zoneSelectionToolItem.addActionListener(this);
							
					}else if(evt.getSource().equals(regularSelectionMenuItem)){
						
						//selecting the regular tool item
						regularSelectionToolItem.removeActionListener(this);
						regularSelectionToolItem.setSelected(true);
						regularSelectionToolItem.addActionListener(this);
					}
					
					Editor.getEditor().getSelectionManager().setSelectionMode(mode, null);
				}
			}
		};
		
		//creating the listener to the all selection and deselection menu and tool items
		ActionListener allDeSelectionListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				
				if(evt.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						evt.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					//getting the current handle
					SVGHandle handle=
						Editor.getEditor().getHandlesManager().getCurrentHandle();
					
					if(handle!=null){
						
						if(evt.getSource().equals(allSelectionMenuItem)){
							
							handle.getSelection().selectAllElements();
							
						}else if(evt.getSource().equals(allDeselectionMenuItem)){
							
							handle.getSelection().clearSelection();
						}
					}
				}
			}
		};
		
		//creating the listener to the enter group and exit group menu items
		ActionListener groupActionListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				if(evt.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						evt.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					if(evt.getSource().equals(enterGroupMenuItem)){
						
						enterGroup();
						
					}else{
						
						exitGroup();
					}
				}
			}
		};
		
		//creating the listener to the lock and unlock menu items
		ActionListener lockActionListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				if(evt.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						evt.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					//getting the current selection manager
			        SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
			        
			        if(handle!=null){
			        	
			        	Selection selection=handle.getSelection();
			        	
						if(evt.getSource().equals(lockMenuItem)){
							
							selection.lock();
							
						}else{
							
							selection.unLock();
						}
			        }
				}
			}
		};
		
		//getting the icons for the items
		regularSelectionIcon=ResourcesManager.getIcon(regularSelectionId, false);
		regularSelectionDisabledIcon=ResourcesManager.getIcon(regularSelectionId, true);
		zoneSelectionIcon=ResourcesManager.getIcon(zoneSelectionId, false);
		zoneSelectionDisabledIcon=ResourcesManager.getIcon(zoneSelectionId, true);
		allSelectionIcon=ResourcesManager.getIcon(allSelectionId, false);
		allSelectionDisabledIcon=ResourcesManager.getIcon(allSelectionId, true);
		allDeselectionIcon=ResourcesManager.getIcon(allDeselectionId, false);
		allDeselectionDisabledIcon=ResourcesManager.getIcon(allDeselectionId, true);
		enterGroupIcon=ResourcesManager.getIcon(enterGroupId, false);
		enterGroupDisabledIcon=ResourcesManager.getIcon(enterGroupId, true);
		exitGroupIcon=ResourcesManager.getIcon(exitGroupId, false);
		exitGroupDisabledIcon=ResourcesManager.getIcon(exitGroupId, true);
		lockIcon=ResourcesManager.getIcon(lockId, false);
		lockDisabledIcon=ResourcesManager.getIcon(lockId, true);
		unLockIcon=ResourcesManager.getIcon(unLockId, false);
		unLockDisabledIcon=ResourcesManager.getIcon(unLockId, true);
		

		//creating the regular selection menu item
		regularSelectionMenuItem=new JMenuItem(regularSelectionLabel, regularSelectionIcon);
		regularSelectionMenuItem.setDisabledIcon(regularSelectionDisabledIcon);
		regularSelectionMenuItem.addActionListener(selectionListener);
		regularSelectionMenuItem.setEnabled(false);
		
		//creating the zone selection menu item
		zoneSelectionMenuItem=new JMenuItem(zoneSelectionLabel, zoneSelectionIcon);
		zoneSelectionMenuItem.setDisabledIcon(zoneSelectionDisabledIcon);
		zoneSelectionMenuItem.addActionListener(selectionListener);
		zoneSelectionMenuItem.setEnabled(false);
		
		//creating the all selection menu item
		allSelectionMenuItem=new JMenuItem(allSelectionLabel, allSelectionIcon);
		allSelectionMenuItem.setDisabledIcon(allSelectionDisabledIcon);
		allSelectionMenuItem.addActionListener(allDeSelectionListener);
		allSelectionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, 
				java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		allSelectionMenuItem.setEnabled(false);
		
		//creating the all deselection menu item
		allDeselectionMenuItem=new JMenuItem(allDeselectionLabel, allDeselectionIcon);
		allDeselectionMenuItem.setDisabledIcon(allDeselectionDisabledIcon);
		allDeselectionMenuItem.addActionListener(allDeSelectionListener);
		allDeselectionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, 
				java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		allDeselectionMenuItem.setEnabled(false);
		
		//creating the enter group menu item
		enterGroupMenuItem=new JMenuItem(enterGroupLabel, enterGroupIcon);
		enterGroupMenuItem.setDisabledIcon(enterGroupDisabledIcon);
		enterGroupMenuItem.addActionListener(groupActionListener);
		enterGroupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, 
				java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		enterGroupMenuItem.setEnabled(false);
		
		//creating the exit group menu item
		exitGroupMenuItem=new JMenuItem(exitGroupLabel, exitGroupIcon);
		exitGroupMenuItem.setDisabledIcon(exitGroupDisabledIcon);
		exitGroupMenuItem.addActionListener(groupActionListener);
		exitGroupMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift E"));
		exitGroupMenuItem.setEnabled(false);
		
		//creating the lock menu item
		lockMenuItem=new JMenuItem(lockLabel, lockIcon);
		lockMenuItem.setDisabledIcon(lockDisabledIcon);
		lockMenuItem.addActionListener(lockActionListener);
		lockMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, 
				java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		lockMenuItem.setEnabled(false);
		
		//creating the unlock menu item
		unLockMenuItem=new JMenuItem(unLockLabel, unLockIcon);
		unLockMenuItem.setDisabledIcon(unLockDisabledIcon);
		unLockMenuItem.addActionListener(lockActionListener);
		unLockMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift L"));
		unLockMenuItem.setEnabled(false);
		
		//creating the regular selection tool item
		regularSelectionToolItem=new JToggleButton(regularSelectionIcon);
		regularSelectionToolItem.setDisabledIcon(regularSelectionDisabledIcon);
		regularSelectionToolItem.setToolTipText(regularSelectionLabel);
		regularSelectionToolItem.addActionListener(selectionListener);
		regularSelectionToolItem.setSelected(true);
		regularSelectionToolItem.setEnabled(false);
		
		//creating the zone selection tool item
		zoneSelectionToolItem=new JToggleButton(zoneSelectionIcon);
		zoneSelectionToolItem.setDisabledIcon(zoneSelectionDisabledIcon);
		zoneSelectionToolItem.setToolTipText(zoneSelectionLabel);
		zoneSelectionToolItem.addActionListener(selectionListener);
		zoneSelectionToolItem.setEnabled(false);
		
		//setting the tool button for the regular selection mode
		Editor.getEditor().getSelectionManager().setRegularButton(regularSelectionToolItem);
		
		//adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager=
			Editor.getEditor().getHandlesManager();
		
		svgHandleManager.addHandlesListener(new HandlesListener(){
			
			/**
			 * a listener on the selection changes
			 */
			private SelectionChangedListener selectionChangedListener=null;
			
			/**
			 * the last handle
			 */
			private SVGHandle lastHandle=null;
			
			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {

				if(lastHandle!=null && selectionChangedListener!=null && lastHandle.getSelection()!=null){
					
					//if a selection listener is already registered on a selection module, it is removed	
					lastHandle.getSelection().removeSelectionChangedListener(selectionChangedListener);
				}
				
				boolean existsHandle=svgHandleManager.getHandlesNumber()>0;
				regularSelectionMenuItem.setEnabled(existsHandle);
				zoneSelectionMenuItem.setEnabled(existsHandle);
				regularSelectionToolItem.setEnabled(existsHandle);
				zoneSelectionToolItem.setEnabled(existsHandle);
				
				allSelectionMenuItem.setEnabled(existsHandle);
				allDeselectionMenuItem.setEnabled(existsHandle);
				lockMenuItem.setEnabled(existsHandle);
				unLockMenuItem.setEnabled(existsHandle);
				selectionChangedListener=null;
				
				if(currentHandle!=null){

					manageSelection(new HashSet<Element>(
							currentHandle.getSelection().getSelectedElements()));
					
					//the listener of the selection changes
					selectionChangedListener=new SelectionChangedListener(){

						@Override
						public void selectionChanged(Set<Element> selectedElements) {
							
							manageSelection(selectedElements);
						}
					};
					
					//adds the selection listener
					currentHandle.getSelection().addSelectionChangedListener(selectionChangedListener);
				}
				
				lastHandle=currentHandle;
			}
			
			/**
			 * updates the selected items and the state of the menu items
			 * @param selectedElements the set of the selected elements
			 */
			protected void manageSelection(Set<Element> selectedElements){

				updateMenuItems();
			}		
		});
	}
	
	/**
	 * updates the state of the enter and exit group action 
	 * menu items and the lock and unlock menu items
	 */
	protected void updateMenuItems(){
		
		//disabling the items
		enterGroupMenuItem.setEnabled(false);
		exitGroupMenuItem.setEnabled(false);
		lockMenuItem.setEnabled(false);
		unLockMenuItem.setEnabled(false);
		
		//getting the current selection manager
        SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
        
        if(handle!=null){
        	
        	Selection selection=handle.getSelection();
        	
            //getting the current parent element
            Element parentElement=selection.getParentElement();
            
            //checking if the exit group action menu item can be enabled
            if(parentElement!=null && ! parentElement.equals(
            		parentElement.getOwnerDocument().getDocumentElement())){
            	
        		exitGroupMenuItem.setEnabled(true);
            }
        	
        	//getting the current selection
        	Set<Element> selectedElements=selection.getSelectedElements();
        	
        	if(selectedElements.size()==1){
        		
        		//getting the element of the selection
        		Element selectedElement=selectedElements.iterator().next();
        		
        		if(selectedElement!=null && 
        				Selection.isParentElementEligible(selectedElement)){
        			
        			//enabling the enter group item
        			enterGroupMenuItem.setEnabled(true);
        		}
        	}
        	
        	//checking the state of the lock and unlock menu items//
        	if(selectedElements.size()>0){
        		
            	lockMenuItem.setEnabled(! selection.isAllSelectionLocked());
            	unLockMenuItem.setEnabled(selection.isSelectionLocked());
        	}
        }
	}
	
	/**
	 * executes the enter group action
	 */
	protected void enterGroup(){
		
		//getting the current selection manager
        SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
        
        if(handle!=null){
        	
        	Selection selection=handle.getSelection();
        	
        	//getting the current selection
        	Set<Element> selectedElements=selection.getSelectedElements();
        	
        	if(selectedElements.size()==1){
        		
        		//getting the element of the selection
        		Element selectedElement=selectedElements.iterator().next();
        		
        		if(selectedElement!=null && 
        				Selection.isParentElementEligible(selectedElement)){
        			
        			//setting the new parent element
        			selection.setParentElement(selectedElement, true);
        		}
        	}
        }
	}
	
	/**
	 * executes the exit group action
	 */
	protected void exitGroup(){
		
		//getting the current selection manager
        SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
        
        if(handle!=null){
        	
        	Selection selection=handle.getSelection();
        	
            //getting the current parent element
            Element parentElement=selection.getParentElement();
            
            //checking if the exit group action menu item can be enabled
            if(parentElement!=null && parentElement.getParentNode()!=null){
            	
        		selection.setParentElement((Element)parentElement.getParentNode(), true);
            }
        }
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		
		HashMap<String, JMenuItem> menuItems=new HashMap<String, JMenuItem>();
		menuItems.put(regularSelectionId, regularSelectionMenuItem);
		menuItems.put(zoneSelectionId, zoneSelectionMenuItem);
		menuItems.put(allSelectionId, allSelectionMenuItem);
		menuItems.put(allDeselectionId, allDeselectionMenuItem);
		menuItems.put(enterGroupId, enterGroupMenuItem);
		menuItems.put(exitGroupId, exitGroupMenuItem);
		menuItems.put(lockId, lockMenuItem);
		menuItems.put(unLockId, unLockMenuItem);
		
		return menuItems;
	}

	@Override
	public HashMap<String, AbstractButton> getToolItems() {

		HashMap<String, AbstractButton> toolItems=
			new HashMap<String, AbstractButton>();
		toolItems.put(regularSelectionId, regularSelectionToolItem);
		toolItems.put(zoneSelectionId, zoneSelectionToolItem);
		
		return toolItems;
	}
	
	@Override
	public Collection<PopupItem> getPopupItems() {

		Set<PopupItem> popupItems=new HashSet<PopupItem>();
		
		//creating the group enter popup item
		PopupItem groupEnterItem=new PopupItem(
				Editor.getEditor(), enterGroupId, enterGroupLabel, enterGroupId){
			
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				menuItem.setEnabled(false);
				
				if(nodes!=null && nodes.size()==1){

					Element current=nodes.getFirst();
                    
                    if(current!=null && Selection.isParentElementEligible(current)){
                        
                    	menuItem.setEnabled(true);
						menuItem.addActionListener(new ActionListener(){
							
							public void actionPerformed(ActionEvent e) {
								
								enterGroup();
							}
						});
                    }
				}
				
				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(groupEnterItem);
		
		//creating the group exit popup item
		PopupItem groupExitItem=new PopupItem(
				Editor.getEditor(), exitGroupId, exitGroupLabel, exitGroupId){
		
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {
				
				menuItem.setEnabled(false);
				
				//getting the current svg handle
                SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
                
                if(handle!=null){
                	
                    //getting the current parent element
                    Element parentElement=handle.getSelection().getParentElement();

                    //checking if the menu item can be enabled
                    if(parentElement!=null && ! parentElement.equals(
                    		handle.getCanvas().getDocument().getDocumentElement())){

                    	menuItem.setEnabled(true);
                    	menuItem.addActionListener(new ActionListener(){
    						
    						public void actionPerformed(ActionEvent e) {
    							
    							exitGroup();
    						}
    					});
                    }
                }

				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(groupExitItem);
		
		//creating the lock popup item
		PopupItem lockItem=new PopupItem(
				Editor.getEditor(), lockId, lockLabel, lockId){
		
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {
				
				menuItem.setEnabled(false);
				
				//getting the current svg handle
                SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
                
                if(handle!=null){
                	
                	final Selection selection=handle.getSelection();
                	
                	//getting the currently selected elements
                	Set<Element> selectedElements=selection.getSelectedElements();
                	
                	//checking the state of the lock item
                	if(selectedElements.size()>0 && ! selection.isAllSelectionLocked()){
                		
                    	menuItem.setEnabled(true);
                    	menuItem.addActionListener(new ActionListener(){
    						
    						public void actionPerformed(ActionEvent e) {
    							
    							selection.lock();
    						}
    					});
                	}
                }

				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(lockItem);
		
		//creating the unlock popup item
		PopupItem unLockItem=new PopupItem(
				Editor.getEditor(), unLockId, unLockLabel, unLockId){
		
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {
				
				menuItem.setEnabled(false);
				
				//getting the current svg handle
                SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
                
                if(handle!=null){
                	
                	final Selection selection=handle.getSelection();
                	
                	//getting the currently selected elements
                	Set<Element> selectedElements=selection.getSelectedElements();
                	
                	//checking the state of the lock item
                	if(selectedElements.size()>0 && selection.isSelectionLocked()){
                		
                    	menuItem.setEnabled(true);
                    	menuItem.addActionListener(new ActionListener(){
    						
    						public void actionPerformed(ActionEvent e) {
    							
    							selection.unLock();
    						}
    					});
                	}
                }

				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(unLockItem);
		
		return popupItems;
	}
}
