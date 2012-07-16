package fr.itris.glips.rtdaeditor.jwidget;

import javax.swing.*;
import fr.itris.glips.library.display.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import java.util.*;
import java.util.Timer;
import java.awt.*;
import java.awt.event.*;

/**
 * the class that manages the tool items for the jwidgets manager
 * @author Jordi SUC
 */
public class JWidgetToolsManager {

	/**
	 * the jwidget manager
	 */
	private JWidgetManager jwidgetManager;
	
	/**
	 * the currently selected jwidget edition object in the popup menu
	 */
	private JWidgetEdition currentJWidgetEditionObject;
	
	/**
	 * the tool button
	 */
	private JToggleButton toolButton;
	
	/**
	 * the icon manager for the tool button
	 */
	private ArrowIcon arrowIcon=new ArrowIcon();
	
	/**
	 * the popup menu
	 */
	private JPopupMenu popupMenu;
	
	/**
	 * the map of the menu items that should be added to the popup menu
	 */
	private Map<JMenuItem, JWidgetEdition> menuItemsMap=
		new HashMap<JMenuItem, JWidgetEdition>();
	
	/**
	 * the inverse map of the  menu items that should be added to the popup menu
	 */
	private Map<JWidgetEdition, JMenuItem> inverseMenuItemsMap=
		new HashMap<JWidgetEdition, JMenuItem>();
	
	/**
	 * the tooltip used for tool button
	 */
	private String tooltipLabel="";
	
	/**
	 * the listener to the items
	 */
	private ActionListener itemsListener;
	
	/**
	 * the constructor of the class
	 * @param jwidgetManager a jwidget manager
	 */
	public JWidgetToolsManager(JWidgetManager jwidgetManager){
		
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
					
					setCurrentJWidgetEditionObject(jwidgetEdition, true);
					toolButton.setSelected(true);
				}
			}
		};

		//creating the menu items
		JMenuItem item;

		for(JWidgetEdition jwidgetEdition : jwidgetEditionObjects){
			
			item=new JMenuItem(jwidgetEdition.getDescription(), jwidgetEdition.getIcon(false));
			item.setDisabledIcon(jwidgetEdition.getIcon(true));
			
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
		
		//creating the popup menu
		popupMenu=new JPopupMenu();
		
		//filling the popup menu
		for(JWidgetEdition jwidgetEdition : jwidgetEditionObjectsList){
			
			popupMenu.add(inverseMenuItemsMap.get(jwidgetEdition));
		}
		
		//creating the tool button
		toolButton=new JToggleButton(arrowIcon.getIcon());
		tooltipLabel=ResourcesManager.bundle.getString(jwidgetManager.getId()+"ItemToolTip");
		toolButton.setEnabled(false);
		
		//computing the area where the arrow can be found
		final Rectangle area=new Rectangle(ArrowIcon.arrowIconBounds);
		area.x+=toolButton.getMargin().left;
		area.width+=toolButton.getMargin().right;

		//adding a listener to the tool button
		toolButton.addMouseListener(new MouseAdapter(){
			
			/**
			 * the timer
			 */
			private java.util.Timer timer;

			@Override
			public void mousePressed(MouseEvent evt) {
				
				if(evt.getX()<area.getX()){
					
					TimerTask task=new TimerTask(){
						
						@Override
						public void run() {
							
							showPopup();
							timer=null;
						}
					};
					
					timer=new Timer();
					timer.schedule(task, 500);
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent evt) {

				if(timer!=null){
					
					timer.cancel();
					timer=null;
				}
				
				executeAction();

				if(evt.getX()>=area.getX()){
					
					//the arrow part has been clicked, the popup is then shown
					showPopup();
				}
			}
			
			/**
			 * shows the popup menu
			 */
			protected void showPopup(){
				
				//showing the popup menu
				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {

						Rectangle bounds=toolButton.getBounds();
						popupMenu.pack();
						popupMenu.show(toolButton, (int)(bounds.getWidth()/2), 
								(int)(bounds.getHeight()/2));
					}
				});
			}
		});

		//initializing the tool button
		if(jwidgetEditionObjectsList.size()>0){
			
			setCurrentJWidgetEditionObject(jwidgetEditionObjectsList.getFirst(), false);
		}

		//adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager=
			Editor.getEditor().getHandlesManager();
		
		svgHandleManager.addHandlesListener(new HandlesListener(){
					
			@Override
			public void handleChanged(
					SVGHandle currentHandle, Set<SVGHandle> handles) {
		
				boolean existsHandle=svgHandleManager.getHandlesNumber()>0;
				toolButton.setEnabled(existsHandle);
			}
		});
	}
	
	/**
	 * sets the current jwidget edition object
	 * @param jwidgetEdition a jwidget edition object
	 * @param execute whether to execute the action (initialization option)
	 */
	protected void setCurrentJWidgetEditionObject(
			JWidgetEdition jwidgetEdition, boolean execute){
		
		if(jwidgetEdition!=null){
			
			this.currentJWidgetEditionObject=jwidgetEdition;
			
			//setting the tooltip and icons for the tool button
			toolButton.setToolTipText(tooltipLabel+jwidgetEdition.getDescription());
			arrowIcon.setIcon(jwidgetEdition.getIcon(false));
			toolButton.setIcon(arrowIcon.getIcon());
			toolButton.setDisabledIcon(arrowIcon.getDisabledIcon());
			
			if(execute){
				
				executeAction();
			}
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
	 * @return the tool button
	 */
	public JToggleButton getToolButton() {
		return toolButton;
	}
	
	/**
	 * sets whether the tool button should be selected
	 * @param selected whether the tool button should be selected
	 */
	public void setToolButtonSelected(boolean selected){
		
		toolButton.setSelected(selected);
	}
}
