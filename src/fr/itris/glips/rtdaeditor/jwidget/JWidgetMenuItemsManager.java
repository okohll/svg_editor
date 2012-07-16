package fr.itris.glips.rtdaeditor.jwidget;

import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import java.util.*;
import java.awt.event.*;

/**
 * the class that manages the menu items for the jwidgets manager
 * @author Jordi SUC
 */
public class JWidgetMenuItemsManager {

	/**
	 * the jwidget manager
	 */
	private JWidgetManager jwidgetManager;
	
	/**
	 * the currently selected jwidget edition object in the popup menu
	 */
	private JWidgetEdition currentJWidgetEditionObject;
	
	/**
	 * the menu that will contain all the menu items
	 */
	private JMenu menu=new JMenu();
	
	/**
	 * the map of the menu items that should be added to the menu
	 */
	private Map<JMenuItem, JWidgetEdition> menuItemsMap=
		new HashMap<JMenuItem, JWidgetEdition>();
	
	/**
	 * the inverse map of the  menu items that should be added to the menu
	 */
	private Map<JWidgetEdition, JMenuItem> inverseMenuItemsMap=
		new HashMap<JWidgetEdition, JMenuItem>();
	
	/**
	 * the listener to the items
	 */
	private ActionListener itemsListener;
	
	/**
	 * the constructor of the class
	 * @param jwidgetManager a jwidget manager
	 */
	public JWidgetMenuItemsManager(JWidgetManager jwidgetManager){
		
		this.jwidgetManager=jwidgetManager;
	}
	
	/**
	 * adds a set of jwidget edition objects
	 * @param jwidgetEditionObjects a set of jwidget edition objects
	 */
	public void addJWidgetEditionObjects(Set<JWidgetEdition> jwidgetEditionObjects) {
		
		//creating the items listener
		itemsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				
				//getting the jwidget edition object corresponding to the source of the event
				JWidgetEdition jwidgetEdition=menuItemsMap.get(e.getSource());

				if(jwidgetEdition!=null){
					
					// TODO jwidgetManager.getToolsManager().setToolButtonSelected(true);
					setCurrentJWidgetEditionObject(jwidgetEdition);
				}
			}
		};

		//creating the menu items
		JMenuItem item;

		for(JWidgetEdition jwidgetEdition : jwidgetEditionObjects){
			
			item=new JMenuItem(jwidgetEdition.getDescription(), jwidgetEdition.getIcon(false));
			item.setDisabledIcon(jwidgetEdition.getIcon(true));
			item.setEnabled(false);
			
			//putting the menu item into the map
			menuItemsMap.put(item, jwidgetEdition);
			inverseMenuItemsMap.put(jwidgetEdition, item);

			//adding the listener to the menu item
			item.addActionListener(itemsListener);
		}
		
		//sorting the list of the jwidget edition objects//
		
		//the comparator used to sort the list
		Comparator<JWidgetEdition> comparator=new Comparator<JWidgetEdition>() {

			public int compare(JWidgetEdition widgetEdition0, JWidgetEdition widgetEdition1) {

				if(widgetEdition0!=null && widgetEdition1!=null) {
					
					return widgetEdition0.getToolItemPosition()-widgetEdition1.getToolItemPosition();
				}
				
				return 0;
			}
		};
		
		//creating and sorting the list of the jwidget edition item
		LinkedList<JWidgetEdition> jwidgetEditionObjectsList=
			new LinkedList<JWidgetEdition>(jwidgetEditionObjects);
		Collections.sort(jwidgetEditionObjectsList, comparator);
		
		//setting the properties of the menu
		String menuLabel=ResourcesManager.bundle.getString(jwidgetManager.getId()+"ItemLabel");
		menu.setText(menuLabel);
		Icon menuIcon=ResourcesManager.getIcon(jwidgetManager.getId(), false);
		menu.setIcon(menuIcon);
		
		//filling the menu
		for(JWidgetEdition jwidgetEdition : jwidgetEditionObjectsList){
			
			menu.add(inverseMenuItemsMap.get(jwidgetEdition));
		}
		
		//adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager=
			Editor.getEditor().getHandlesManager();
		
		svgHandleManager.addHandlesListener(new HandlesListener(){
			
			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
		
				boolean existsHandle=svgHandleManager.getHandlesNumber()>0;
				
				for(JMenuItem theItem : menuItemsMap.keySet()){
					
					theItem.setEnabled(existsHandle);
				}
			}			
		});
	}
	
	/**
	 * sets the current jwidget edition object
	 * @param jwidgetEdition a jwidget edition object
	 */
	protected void setCurrentJWidgetEditionObject(JWidgetEdition jwidgetEdition){
		
		if(jwidgetEdition!=null){
			
			this.currentJWidgetEditionObject=jwidgetEdition;
			
			//propagates to the tool items the new selected jwidget edition object
			jwidgetManager.getToolsManager().setCurrentJWidgetEditionObject(
					jwidgetEdition, false);
			executeAction();
		}
	}
	
	/**
	 * executes the action used to draw a jwidget component, 
	 * according to the selected jwidget edition object
	 */
	protected void executeAction(){
		
		if(currentJWidgetEditionObject!=null){
			
			jwidgetManager.handleCurrentJWidgetEditionObject(
					currentJWidgetEditionObject);
		}
	}

	/**
	 * @return the currently selected jwidget edition object
	 */
	public JWidgetEdition getCurrentJWidgetEditionObject() {
		return currentJWidgetEditionObject;
	}

	/**
	 * @return the menu
	 */
	public JMenu getMenu() {
		return menu;
	}
}
