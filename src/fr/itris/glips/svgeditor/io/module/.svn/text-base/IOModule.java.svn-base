package fr.itris.glips.svgeditor.io.module;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the IO module used to provide widgets for the 
 * user to interact with the file system
 * @author Jordi SUC
 */
public class IOModule extends ModuleAdapter {

	/**
	 * the id of the module
	 */
	protected static final String id="IOModule";
	
	/**
	 * the array of the ids of the io manager
	 */
	protected String[] ids={"FileNew", "FileOpen", 
		"FileOpenRecent", "FileSave", "FileSaveAs", 
			"FileExport", "FilePrint", "FileClose", "EditorExit"};
	
	protected String[] accelerators={
		"ctrl N", "ctrl O", null, "ctrl S", "ctrl shift S", 
			null, "ctrl P", "ctrl W", "Esc"};
	
	/**
	 * the icons for the square mode
	 */
	protected Icon[] icons, disabledIcons;
	
	/**
	 * the label for the menu and tool item
	 */
	protected String[] labels;
	
	/**
	 * the menu items that are displayed in the menu bar
	 */
	protected JMenuItem[] menuItems;
	
	/**
	 * the open recent menu manager
	 */
	private OpenRecentMenuManager openRecentMenuManager=
			new OpenRecentMenuManager();
	
	/**
	 * the export menu manager
	 */
	private ExportMenuManager exportMenuManager=
			new ExportMenuManager();

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public IOModule(Editor editor) {
		
		createMenuItems();
	}

	/**
	 * creates the menu items
	 */
	protected void createMenuItems() {
		
		//creating the arrays
		labels=new String[ids.length];
		icons=new Icon[ids.length];
		disabledIcons=new Icon[ids.length];
		menuItems=new JMenuItem[ids.length];
		
		//setting the open recent menu
		menuItems[2]=openRecentMenuManager.getMenu();
		
		//setting the export menu
		menuItems[5]=exportMenuManager.getMenu();
		
		//creating the items and setting their properties
		ActionListener listener=null;
		
		for(int i=0; i<ids.length; i++){
			
			final int index=i;
			
			//getting the labels
			labels[i]=ResourcesManager.bundle.
				getString(ids[i]+"ItemLabel");
			
			//getting the icons
			icons[i]=ResourcesManager.getIcon(ids[i], false);
			disabledIcons[i]=ResourcesManager.getIcon(ids[i], true);

			if(i!=2 && i!=5){
				
				//creating the menu items
				menuItems[i]=new JMenuItem();
				
				if(accelerators[i]!=null){
					
					menuItems[i].setAccelerator(
						KeyStroke.getKeyStroke(accelerators[i]));
				}
				
				//creating the listener
				listener=new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {

						doAction(index);
					}
				};
				
				menuItems[i].addActionListener(listener);
			}
			
			//setting the properties of the menu items
			menuItems[i].setText(labels[i]);
			menuItems[i].setIcon(icons[i]);
			menuItems[i].setDisabledIcon(disabledIcons[i]);
		}
		
		//adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager=
			Editor.getEditor().getHandlesManager();
		
		svgHandleManager.addHandlesListener(new HandlesListener(){
					
			@Override
			public void handleChanged(
				SVGHandle currentHandle, Set<SVGHandle> handles) {

				handleItemsState();
			}			
		});
		
		handleItemsState();
	}
	
	/**
	 * handles the tools state
	 */
	protected void handleItemsState(){
		
		//getting the current handle
		SVGHandle currentHandle=
			Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		for(int i=3; i<7; i++){
			
			if(i!=5){
				
				menuItems[i].setEnabled(currentHandle!=null);
			}
		}
		
		exportMenuManager.handleItemsState(currentHandle);
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> map=
			new HashMap<String, JMenuItem>();
		
		for(int i=0; i<ids.length; i++){
			
			if(menuItems[i]!=null){
				
				map.put(ids[i], menuItems[i]);
			}
		}

		return map;
	}

	/**
	 * executes the action corresponding to the provided index
	 * @param index the index of an option manager
	 */
	protected void doAction(int index){
		
		SVGHandle currentHandle=
			Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		switch (index){
			
			case 0 :
				
				Editor.getEditor().getIOManager().getFileNewManager().
					askForNewFileParameters(menuItems[index]);
				break;
				
			case 1 :

				Editor.getEditor().getIOManager().getFileOpenManager().
					askUserForFile(menuItems[index]);
				break;
				
			case 3 :
				
				Editor.getEditor().getIOManager().getFileSaveManager().
					saveHandleDocument(currentHandle, false, menuItems[index]);
					
				break;
				
			case 4 :
				
				Editor.getEditor().getIOManager().getFileSaveManager().
					saveHandleDocument(currentHandle, true, menuItems[index]);
				break;
				
			case 6 :
				
				Editor.getEditor().getIOManager().getFilePrint().print(currentHandle);
				break;
				
			case 7 :
				
				Editor.getEditor().getIOManager().getFileCloseManager().closeHandle(
						currentHandle, menuItems[index]);
				break;
				
			case 8 :
				
				Editor.getEditor().getIOManager().getEditorExitManager().exit();
				break;
		}
	}
}
