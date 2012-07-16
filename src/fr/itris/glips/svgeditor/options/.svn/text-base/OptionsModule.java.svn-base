package fr.itris.glips.svgeditor.options;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the module that provides widgets so that the 
 * user can graphically change the square mode
 * @author Jordi SUC
 */
public class OptionsModule extends ModuleAdapter {

	/**
	 * the id of the module
	 */
	protected static final String id="OptionsModule";
	
	/**
	 * the menu icon
	 */
	protected Icon menuIcon;
	
	/**
	 * the label for the menu
	 */
	protected String menuLabel="";
	
	/**
	 * the options menu
	 */
	protected JMenu optionsMenu;
	
	/**
	 * the array of the ids of the option items
	 */
	protected String[] ids={"RemanentMode", "SquareMode", "AlignWithRulersMode",
			"ClosePathMode", "ConstraintLinesMode"};
	
	/**
	 * the icons for the modes
	 */
	protected Icon[] optionsIcons, optionsDisabledIcons;
	
	/**
	 * the label for the menu and tool item
	 */
	protected String[] optionsItemLabels, 
		optionsOnItemToolTips,optionsOffItemToolTips;
	
	/**
	 * the menu items that are displayed in the menu bar for the options
	 */
	protected JCheckBoxMenuItem[] optionsMenuItems;
	
	/**
	 * the tool items that are displayed in the toolbar for the options
	 */
	protected JToggleButton[] optionsToolItems;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public OptionsModule(Editor editor) {
		
		createMenuAndToolItems();
	}

	/**
	 * creates the menu and tool items
	 */
	protected void createMenuAndToolItems() {
		
		//creating the menu//
		//getting the menu label
		menuLabel=
			ResourcesManager.bundle.getString(id+"ItemLabel");
		
		//getting the menu icons
		menuIcon=ResourcesManager.getIcon(id, false);

		//creating the menu
		optionsMenu=new JMenu(menuLabel);
		optionsMenu.setIcon(menuIcon);
		
		//creating the options item//
		
		//getting the resources for the items
		optionsItemLabels=new String[ids.length];
		optionsOnItemToolTips=new String[ids.length];
		optionsOffItemToolTips=new String[ids.length];
		optionsIcons=new Icon[ids.length];
		optionsDisabledIcons=new Icon[ids.length];
		optionsMenuItems=new JCheckBoxMenuItem[ids.length];
		optionsToolItems=new JToggleButton[ids.length];
		ActionListener listener=null;
		
		for(int i=0; i<ids.length; i++){
			
			final int index=i;
			
			//getting the labels
			optionsItemLabels[i]=
				ResourcesManager.bundle.getString(ids[i]+"ItemLabel");
			
			optionsOnItemToolTips[i]=
				ResourcesManager.bundle.getString(ids[i]+"OnItemToolTip");
			
			optionsOffItemToolTips[i]=
				ResourcesManager.bundle.getString(ids[i]+"OffItemToolTip");
			
			//getting the icons
			optionsIcons[i]=ResourcesManager.getIcon(ids[i], false);
			optionsDisabledIcons[i]=ResourcesManager.getIcon(ids[i], true);
			
			//creating the menu items
			optionsMenuItems[i]=new JCheckBoxMenuItem(optionsItemLabels[i]);
			optionsMenuItems[i].setIcon(optionsIcons[i]);
			optionsMenuItems[i].setDisabledIcon(optionsDisabledIcons[i]);
			optionsMenuItems[i].setEnabled(false);
			
			//creating the tool items
			optionsToolItems[i]=new JToggleButton();
			optionsToolItems[i].setIcon(optionsIcons[i]);
			optionsToolItems[i].setDisabledIcon(optionsDisabledIcons[i]);
			optionsToolItems[i].setEnabled(false);
			
			//creating the listener
			listener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
		
					if(e.getSource() instanceof JCheckBoxMenuItem){
						
						optionsToolItems[index].removeActionListener(this);
						optionsToolItems[index].setSelected(optionsMenuItems[index].isSelected());
						optionsToolItems[index].addActionListener(this);
						
					}else{
						
						optionsMenuItems[index].removeActionListener(this);
						optionsMenuItems[index].setSelected(optionsToolItems[index].isSelected());
						optionsMenuItems[index].addActionListener(this);
					}
					
					doAction(index, optionsMenuItems[index].isSelected());
					handleToolsState();
				}
			};
			
			optionsMenuItems[i].addActionListener(listener);
			optionsToolItems[i].addActionListener(listener);
			
			//adding to the menu
			optionsMenu.add(optionsMenuItems[i]);
		}

		//initializing the items
		initializeItemsState();
		
		//adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager=
			Editor.getEditor().getHandlesManager();
		
		svgHandleManager.addHandlesListener(new HandlesListener(){
					
			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
		
				boolean existsHandle=svgHandleManager.getHandlesNumber()>0;
				
				for(int i=0; i<optionsMenuItems.length; i++){
					
					optionsMenuItems[i].setEnabled(existsHandle);
					optionsToolItems[i].setEnabled(existsHandle);
				}
			}			
		});
		
		handleToolsState();
	}
	
	/**
	 * handles the tools state
	 */
	protected void handleToolsState(){
		
		for(int i=0; i<ids.length; i++){
			
			optionsToolItems[i].setToolTipText(optionsToolItems[i].isSelected()?
					optionsOnItemToolTips[i]:optionsOffItemToolTips[i]);
		}
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> map=new HashMap<String, JMenuItem>();
		map.put(id, optionsMenu);
		
		return map;
	}
	
	@Override
	public HashMap<String, AbstractButton> getToolItems() {

		HashMap<String, AbstractButton> map=
			new HashMap<String, AbstractButton>();

		for(int i=0; i<ids.length; i++){
			
			map.put(ids[i], optionsToolItems[i]);
		}
		
		return map;
	}
	
	/**
	 * initializes the items state
	 */
	protected void initializeItemsState(){

		optionsMenuItems[0].setSelected(
				Editor.getEditor().getRemanentModeManager().isRemanentMode());
		optionsMenuItems[1].setSelected(
				Editor.getEditor().getSquareModeManager().isSquareMode());		
		optionsMenuItems[2].setSelected(
				Editor.getEditor().getHandlesManager().
					getRulersParametersHandler().alignWithRulers());
		optionsMenuItems[3].setSelected(
				Editor.getEditor().getClosePathModeManager().shouldClosePath());
		optionsMenuItems[4].setSelected(
				Editor.getEditor().getConstraintLinesModeManager().constraintLines());
		
		optionsToolItems[0].setSelected(
				Editor.getEditor().getRemanentModeManager().isRemanentMode());
		optionsToolItems[1].setSelected(
				Editor.getEditor().getSquareModeManager().isSquareMode());
		optionsToolItems[2].setSelected(
				Editor.getEditor().getHandlesManager().
					getRulersParametersHandler().alignWithRulers());
		optionsToolItems[3].setSelected(
				Editor.getEditor().getClosePathModeManager().shouldClosePath());
		optionsToolItems[4].setSelected(
				Editor.getEditor().getConstraintLinesModeManager().constraintLines());
	}

	/**
	 * executes the action corresponding to the provided index
	 * @param index the index of an option manager
	 * @param isSelected whether the related item is selected
	 */
	protected void doAction(int index, boolean isSelected){
		
		switch (index){
			
			case 0 :
				
				Editor.getEditor().getRemanentModeManager().
					setRemanentMode(optionsMenuItems[index].isSelected());
				break;
				
			case 1 :
				
				Editor.getEditor().getSquareModeManager().
					setSquareMode(optionsMenuItems[index].isSelected());
				break;
				
			case 2 :
				
				Editor.getEditor().getHandlesManager().getRulersParametersHandler().
					setAlignWithRulers(optionsMenuItems[index].isSelected());
				break;
				
			case 3 :
				
				Editor.getEditor().getClosePathModeManager().
					setShouldClosePath(optionsMenuItems[index].isSelected());
				break;
				
			case 4 :
				
				Editor.getEditor().getConstraintLinesModeManager().
					setConstraintLines(optionsMenuItems[index].isSelected());
				break;
		}
	}
}
