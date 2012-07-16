package fr.itris.glips.svgeditor;

import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.actions.popup.*;

/**
 * the interface that each module must implement
 * @author ITRIS, Jordi SUC
 *
 */
public interface Module {

	/**
	 * @return a map associating a menu item id to its menu item object
	 */
	public HashMap<String, JMenuItem> getMenuItems();
	
	/**
	 * Returns the list of the popup items
	 * @return the list of the popup items
	 */
	public Collection<PopupItem> getPopupItems();
	
	/**
	 * @return a map associating a tool item id to its tool item object
	 */
	public HashMap<String, AbstractButton> getToolItems();
	
	/**
	 * initializes the module
	 */
	public void initialize();
}
