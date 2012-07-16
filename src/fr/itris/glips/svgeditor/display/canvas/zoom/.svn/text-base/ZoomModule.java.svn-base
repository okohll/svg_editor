package fr.itris.glips.svgeditor.display.canvas.zoom;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.selection.*;

/**
 * the module used to handle items for zoom actions
 * @author Jordi SUC
 */
public class ZoomModule extends ModuleAdapter{

	/**
	 * the id of the module
	 */
	protected static final String id="ZoomModule";
	
	/**
	 * the menu icon
	 */
	protected Icon menuIcon;
	
	/**
	 * the label for the menu
	 */
	protected String menuLabel="";
	
	/**
	 * the zoom menu
	 */
	protected JMenu zoomMenu;
	
	/**
	 * the array of the ids of the zoom items
	 */
	protected String[] ids={"ZoomIn", "ZoomOut",
		"ZoneZoom", "FitImageToViewport", "FitContentToViewport",
			"FitSelectedNodesToViewport"};
	
	/**
	 * the icons
	 */
	protected Icon[] icons, disabledIcons;
	
	/**
	 * the label for the menu and tool items
	 */
	protected String[] itemLabels;
	
	/**
	 * the array of the menu items
	 */
	protected JMenuItem[] menuItems;
	
	/**
	 * the zone zoom tool item
	 */
	private JToggleButton zoneZoomToolItem;
	
	/**
	 * the array of the menu items used to select a specified zoom factor
	 */
	protected JMenuItem[] zoomMenuItems;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public ZoomModule(Editor editor) {
		
		createMenuAndToolItems();
	}

	/**
	 * creates the menu and tool items
	 */
	protected void createMenuAndToolItems() {
		
		//creating the menu//
		//getting the menu label
		menuLabel=ResourcesManager.bundle.getString(id+"ItemLabel");
		
		//getting the menu icons
		menuIcon=ResourcesManager.getIcon(id, false);

		//creating the menu
		zoomMenu=new JMenu(menuLabel);
		zoomMenu.setIcon(menuIcon);
		
		//creating the options item//
		
		//getting the resources for the items
		itemLabels=new String[ids.length];
		icons=new Icon[ids.length];
		disabledIcons=new Icon[ids.length];
		menuItems=new JMenuItem[ids.length];
		ActionListener listener=null;
		
		for(int i=0; i<ids.length; i++){
			
			final int index=i;
			
			//getting the labels
			itemLabels[i]=
				ResourcesManager.bundle.getString(ids[i]+"ItemLabel");
			
			//getting the icons
			icons[i]=ResourcesManager.getIcon(ids[i], false);
			disabledIcons[i]=ResourcesManager.getIcon(ids[i], true);
			
			//creating the menu items
			menuItems[i]=new JMenuItem(itemLabels[i]);
			menuItems[i].setIcon(icons[i]);
			menuItems[i].setDisabledIcon(disabledIcons[i]);
			menuItems[i].setEnabled(false);
			
			if(i==2){
				
				zoneZoomToolItem=new JToggleButton(icons[i]);
				zoneZoomToolItem.setDisabledIcon(disabledIcons[i]);
				zoneZoomToolItem.setToolTipText(itemLabels[i]);
				zoneZoomToolItem.setEnabled(false);
			}
			
			//creating the listener
			listener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					
					if(e.getModifiers()==InputEvent.BUTTON1_DOWN_MASK || 
							e.getModifiers()==InputEvent.BUTTON1_MASK || 
								Editor.getEditor().getHandlesManager().keyStrokeActsOnSVGFrame()){
						
						if(index==2){
							
							if(e.getSource() instanceof JMenuItem){
								
								//selecting the tool item
								zoneZoomToolItem.removeActionListener(this);
								zoneZoomToolItem.setSelected(true);
								zoneZoomToolItem.addActionListener(this);
							}

							doAction(index);
							
						}else{
							
							doAction(index);
						}
					}
				}
			};
			
			menuItems[i].addActionListener(listener);
			
			if(i==2){
				
				zoneZoomToolItem.addActionListener(listener);
			}
			
			//adding to the menu
			zoomMenu.add(menuItems[i]);
		}
		
		//adding a separator to the menu
		zoomMenu.addSeparator();
		
		//setting the accelerators to the menu items
		menuItems[0].setAccelerator(KeyStroke.getKeyStroke('+'));
		menuItems[1].setAccelerator(KeyStroke.getKeyStroke('-'));
		
		//creating the menu items used to select a specified zoom factor
		zoomMenuItems=new JMenuItem[Zoom.defaultZoomFactors.length];
		
		for(int i=0; i<zoomMenuItems.length; i++){
			
			final int index=i;
			
			zoomMenuItems[i]=new JMenuItem(
				Integer.toString((int)(Zoom.defaultZoomFactors[i]*100))+" %");
			zoomMenuItems[i].setEnabled(false);
			
			if(Zoom.defaultZoomFactors[i]==1){
				
				zoomMenuItems[i].setAccelerator(KeyStroke.getKeyStroke('1'));
			}
			
			listener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					
					//getting the current handle
					SVGHandle handle=Editor.getEditor().
						getHandlesManager().getCurrentHandle();
					
					if(handle!=null){
						
						handle.getCanvas().getZoomManager().scaleTo(
							Zoom.defaultZoomFactors[index]);
					}
				}
			};
			
			zoomMenuItems[i].addActionListener(listener);
			zoomMenu.add(zoomMenuItems[i]);
		}
		
		//adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager=
			Editor.getEditor().getHandlesManager();
		
		svgHandleManager.addHandlesListener(new HandlesListener(){
					
			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
		
				boolean existsHandle=svgHandleManager.getHandlesNumber()>0;
				
				for(int i=0; i<menuItems.length; i++){
					
					menuItems[i].setEnabled(existsHandle);
				}
				
				zoneZoomToolItem.setEnabled(existsHandle);
				
				for(int i=0; i<zoomMenuItems.length; i++){
					
					zoomMenuItems[i].setEnabled(existsHandle);
				}
			}			
		});
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> map=
			new HashMap<String, JMenuItem>();
		map.put(id, zoomMenu);
		
		return map;
	}
	
	@Override
	public HashMap<String, AbstractButton> getToolItems() {

		HashMap<String, AbstractButton> map=
			new HashMap<String, AbstractButton>();

		map.put(ids[2], zoneZoomToolItem);
		
		return map;
	}

	/**
	 * executes the action corresponding to the provided index
	 * @param index the index of an option manager
	 */
	protected void doAction(int index){
		
		//getting the current zoom manager
		SVGHandle handle=
			Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		if(handle!=null){
			
			Zoom zoomManager=handle.getCanvas().getZoomManager();
			
			switch (index){

				case 0 :
					
					zoomManager.setToNextZoomInScale();
					break;
					
				case 1 :
					
					zoomManager.setToNextZoomOutScale();
					break;
					
				case 2 :
					
					Editor.getEditor().getSelectionManager().setSelectionMode(
							SelectionInfoManager.ZOOM_MODE, null);
					break;
					
				case 3 :
					
					zoomManager.fitImageToViewport();
					break;
					
				case 4 :
					
					zoomManager.fitContentToViewport();
					break;
					
				case 5 :
					
					zoomManager.fitSelectedNodesToViewport();
					break;
			}
		}
	}
}
