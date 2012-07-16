package fr.itris.glips.svgeditor.undoredo;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.popup.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the module of the undo/redo manager
 * @author ITRIS, Jordi SUC
 */
public class UndoRedoModule extends ModuleAdapter{
	
	/**
	 * the ids of the module
	 */
	private static String idundoredo="UndoRedo", idundo="Undo", idredo="Redo";
	
	/**
	 * the labels
	 */
	private String labelundo="", labelredo="";
	
	/**
	 * the menuitems of the undo and redo actions
	 */
	private JMenuItem undoMenuItem, redoMenuItem;
	
	/**
	 * the undo and redo listeners
	 */
	private ActionListener undoListener=null, redoListener=null;
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public UndoRedoModule(Editor editor){
		
		//gets the labels from the resources
		ResourceBundle bundle=ResourcesManager.bundle;
		
		if(bundle!=null){
		    
			try{
				labelundo=bundle.getString("labelundo");
				labelredo=bundle.getString("labelredo");
			}catch (Exception ex){}
		}
		
		//getting the icons
		ImageIcon undoIcon=ResourcesManager.getIcon("Undo", false);
		ImageIcon dundoIcon=ResourcesManager.getIcon("Undo", true);
		ImageIcon redoIcon=ResourcesManager.getIcon("Redo", false);
		ImageIcon dredoIcon=ResourcesManager.getIcon("Redo", true);

		//creating the menuitems
		undoMenuItem=new JMenuItem(labelundo, undoIcon);
		undoMenuItem.setDisabledIcon(dundoIcon);
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
		undoMenuItem.setEnabled(false);
		
		redoMenuItem=new JMenuItem(labelredo, redoIcon);
		redoMenuItem.setDisabledIcon(dredoIcon);
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
		redoMenuItem.setEnabled(false);
		
		//creating the undo action listener
		undoListener=new ActionListener(){
		    
			public synchronized void actionPerformed(ActionEvent e) {
				
				if(e.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						e.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					//getting the current svg handle
					SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
					
					if(handle!=null){
						
						handle.getUndoRedo().undoLastAction();
					}
				}
			}
		};
		
		//adds the undo listener to the menuItem
		undoMenuItem.addActionListener(undoListener);
		
		//creating the redo action listener
		redoListener=new ActionListener(){
		    
			public synchronized void actionPerformed(ActionEvent e) {
			        
				if(e.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
						e.getModifiers()==InputEvent.BUTTON1_MASK || 
							Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
					
					//getting the current svg handle
					SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
					
					if(handle!=null){
						
						handle.getUndoRedo().redoLastAction();
					}
				}
			}
		};
		
		//adds the redo listener to the menuItem
		redoMenuItem.addActionListener(redoListener);
		
		//adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager=
			Editor.getEditor().getHandlesManager();
		
		svgHandleManager.addHandlesListener(new HandlesListener(){
			
			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
				
				updateMenuItems();
			}

			@Override
			public void handleContentChanged(SVGHandle currentHandle) {

				updateMenuItems();
			}
		});
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems(){
		
		HashMap<String, JMenuItem> menuItems=new HashMap<String, JMenuItem>();
		menuItems.put(idundo, undoMenuItem);
		menuItems.put(idredo, redoMenuItem);
		
		return menuItems;
	}
	
	@Override
	public Collection<PopupItem> getPopupItems() {

		Collection<PopupItem> popupItems=new LinkedList<PopupItem>();
		
		//creating and adding the undo popup item
		PopupItem undoItem=new PopupItem(Editor.getEditor(), idundo, labelundo, "Undo"){
		
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				//getting the current svg handle
				SVGHandle handle=editor.getHandlesManager().getCurrentHandle();
				
				if(handle!=null){
					
					//getting the undo and redo labels
					String undoLabel=handle.getUndoRedo().getUndoActionListLabel();
					
					//handling the undo item state and label
					if(undoLabel!=null){
						
						menuItem.setEnabled(true);
						menuItem.setText(labelundo+" "+undoLabel);
						
						//adds the action listeners
						menuItem.addActionListener(undoListener);
						
					}else{
						
						menuItem.setEnabled(false);
						menuItem.setText(labelundo);
					}
				}
				
				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(undoItem);
		
		//creating and adding the redo popup item
		PopupItem redoItem=new PopupItem(Editor.getEditor(), idredo, labelredo, "Redo"){
		
			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				//getting the current svg handle
				SVGHandle handle=editor.getHandlesManager().getCurrentHandle();
				
				if(handle!=null){
					
					//getting the undo and redo labels
					String redoLabel=handle.getUndoRedo().getRedoActionListLabel();
					
					//handling the redo item state and label
					if(redoLabel!=null){
						
						menuItem.setEnabled(true);
						menuItem.setText(labelredo+" "+redoLabel);
						
						//adds the action listeners
						menuItem.addActionListener(redoListener);
						
					}else{
						
						menuItem.setEnabled(false);
						menuItem.setText(labelredo);
					}
				}
				
				return super.getPopupItem(nodes);
			}
		};
		
		popupItems.add(redoItem);

		return popupItems;
	}
	
	/**
	 * updates the state and the label of the menu items
	 */
	protected void updateMenuItems(){
		
		//getting the current svg handle
		SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		if(handle!=null){
			
			//getting the undo and redo labels
			String undoLabel=handle.getUndoRedo().getUndoActionListLabel();
			String redoLabel=handle.getUndoRedo().getRedoActionListLabel();
			
			//handling the undo item state and label
			if(undoLabel!=null){
				
				undoMenuItem.setEnabled(true);
				undoMenuItem.setText(labelundo+" "+undoLabel);
				
			}else{
				
				undoMenuItem.setEnabled(false);
				undoMenuItem.setText(labelundo);
			}
			
			//handling the redo item state and label
			if(redoLabel!=null){
				
				redoMenuItem.setEnabled(true);
				redoMenuItem.setText(labelredo+" "+redoLabel);
				
			}else{
				
				redoMenuItem.setEnabled(false);
				redoMenuItem.setText(labelredo);
			}
			
		}else{
			
			undoMenuItem.setEnabled(false);
			undoMenuItem.setText(labelundo);
			redoMenuItem.setEnabled(false);
			redoMenuItem.setText(labelredo);
		}
	}
	
	/**
	 * gets the name of the module
	 * @return the name of the module
	 */
	public String getName(){
		
		return idundoredo;
	}
}
