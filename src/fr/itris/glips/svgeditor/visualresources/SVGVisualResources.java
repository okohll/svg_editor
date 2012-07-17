/*
 * Created on 26 août 2004
 * 
 =============================================
 GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
 GLIPS Graffiti Editor, a SVG Editor
 Copyright (C) 2003 Jordi SUC, Philippe Gil, SARL ITRIS
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
 Contact : jordi.suc@itris.fr; philippe.gil@itris.fr
 
 =============================================
 */
package fr.itris.glips.svgeditor.visualresources;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.toolbar.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class used to manipulate svg resources like gradients, patterns and
 * markers
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResources extends ModuleAdapter {

	/**
	 * the ids of the module
	 */
	final private String idvisualresources = "VisualResources";

	/**
	 * the labels
	 */
	protected String labelresources = "";

	/**
	 * the undo/redo labels
	 */
	protected String undoredoresources = "", undoredoresourcesnew = "",
			undoredoresourcesremove = "";

	/**
	 * the editor
	 */
	private Editor editor = null;

	/**
	 * the document used for the resources
	 */
	private Document docResources = null;

	/**
	 * the document storing the visual resources
	 */
	private Document visualResourceStore = null;

	/**
	 * the font
	 */
	private final Font theFont = new Font("theFont", Font.ROMAN_BASELINE, 10);

	/**
	 * the bundle used to get labels
	 */
	private ResourceBundle bundle = ResourcesManager.bundle;

	/**
	 * used to convert numbers into a string
	 */
	private DecimalFormat format;

	/**
	 * the panel in which the widget panel will be inserted
	 */
	private JPanel visualResourcesPanel = new JPanel();

	/**
	 * the panel displaying the lists of the resources
	 */
	private SVGVisualResourceListsPanel listsPanel = null;

	/**
	 * the map associating a svg handle to a resource state object
	 */
	private HashMap stateMap = new HashMap();

	/**
	 * the toolkit of this module
	 */
	private SVGVisualResourceToolkit visualResourcesToolkit = null;

	/**
	 * the bounds of the tool frame
	 */
	private Rectangle frameBounds = null;

	/**
	 * the frame into which the resources panel will be inserted
	 */
	private ToolsFrame visualResourcesFrame;

	/**
	 * whether the graphic components of the dialog have already been
	 * reinitialized
	 */
	private boolean hasBeenReinitialized = false;

	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public SVGVisualResources(Editor editor) {

		this.editor = editor;
		this.visualResourcesToolkit = new SVGVisualResourceToolkit(this);

		if (bundle != null) {

			try {
				labelresources = bundle.getString("label_visualresources");
				undoredoresources = bundle.getString("undoredoresources");
				undoredoresourcesnew = bundle.getString("undoredoresourcesnew");
				undoredoresourcesremove = bundle.getString("undoredoresourcesremove");
			} catch (MissingResourceException ex) {
				ex.printStackTrace();
			}
		}

		// sets the format used to convert numbers into a string
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("######.#", symbols);

		// a listener that listens to the changes of the svg handles
		final HandlesListener svgHandlesListener = new HandlesListener() {

			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {

				// removes all the components of the panel of the resources
				visualResourcesPanel.removeAll();

				if (listsPanel != null) {

					listsPanel.dispose();
					listsPanel = null;
				}

				final SVGHandle handle = getSVGEditor().getHandlesManager().getCurrentHandle();

				// checks the defs map and the resource state map consistency
				SVGHandle hnd = null;

				for (Iterator it = new LinkedList(stateMap.keySet()).iterator(); it.hasNext();) {

					try {
						hnd = (SVGHandle) it.next();
					} catch (NoSuchElementException ex) {
						ex.printStackTrace();
						hnd = null;
					}

					if (hnd != null && !handles.contains(hnd)) {

						stateMap.remove(hnd);
					}
				}

				// if a new svg document has been created, the defs element is
				// added,
				// and a new resource state object is added to the stateMap
				if (handle != null && !stateMap.containsKey(handle)) {

					SVGVisualResourceState resourceState = new SVGVisualResourceState();
					stateMap.put(handle, resourceState);
				}

				if (visualResourcesPanel.isVisible() || handle == null) {

					handleVisualResources(handle);
				}
			}
		};

		// adds the svg handles change listener
		editor.getHandlesManager().addHandlesListener(svgHandlesListener);

		// setting the layout for the visual resources panel
		visualResourcesPanel.setLayout(new BoxLayout(visualResourcesPanel, BoxLayout.Y_AXIS));

		// getting the preferred bounds
		frameBounds = editor.getPreferredWidgetBounds("visualresources");

		// creating the internal frame containing the properties panel
		visualResourcesFrame = new ToolsFrame(editor, idvisualresources, labelresources,
				visualResourcesPanel);

		// setting the visibility changes handler
		Runnable visibilityRunnable = new Runnable() {

			public void run() {

				SVGHandle svgHandle = getSVGEditor().getHandlesManager().getCurrentHandle();

				if (!visualResourcesFrame.isVisible() || svgHandle == null) {

					handleVisualResources(null);

				} else {

					handleVisualResources(svgHandle);
				}
			}
		};

		this.visualResourcesFrame.setVisibilityChangedRunnable(visibilityRunnable);

		// loading the documents
		docResources = ResourcesManager.getXMLDocument("visualResources.xml");
	}

	/**
	 * @return the editor
	 */
	public Editor getSVGEditor() {
		return editor;
	}

	/**
	 * @return a map associating a menu item id to its menu item object
	 */
	public HashMap getMenuItems() {

		HashMap menuItems = new HashMap();

		menuItems.put("ToolFrame_" + this.idvisualresources, visualResourcesFrame.getMenuItem());

		return menuItems;
	}

	@Override
	public HashMap<String, AbstractButton> getToolItems() {

		HashMap<String, AbstractButton> map = new HashMap<String, AbstractButton>();
		map.put(idvisualresources, visualResourcesFrame.getToolBarButton());
		return map;
	}

	/**
	 * @return Returns the bundle.
	 */
	protected ResourceBundle getBundle() {
		return bundle;
	}

	/**
	 * @return Returns the format.
	 */
	protected DecimalFormat getFormat() {
		return format;
	}

	/**
	 * @return Returns the visualResourcesToolkit.
	 */
	protected SVGVisualResourceToolkit getVisualResourcesToolkit() {
		return visualResourcesToolkit;
	}

	/**
	 * returns the defs element corresponding to the given svg handle
	 * 
	 * @param handle
	 *            a svg handle
	 * @return the defs element corresponding to the given svg handle
	 */
	protected Element getDefs(SVGHandle handle) {

		Element defs = null;

		if (handle != null) {

			// try{

			defs = handle.getSvgResourcesManager().getDefsElement();
			// }catch(Exception ex){defs=null;}
		}

		return defs;
	}

	/**
	 * @return Returns the docResources.
	 */
	protected Document getDocResources() {
		return docResources;
	}

	/**
	 * @return Returns the visualResourceStore.
	 */
	protected Document getVisualResourceStore() {
		return visualResourceStore;
	}

	/**
	 * @return Returns the listState
	 * @param handle
	 *            a svg handle
	 */
	protected SVGVisualResourceState getResourceState(SVGHandle handle) {

		SVGVisualResourceState resourceState = null;

		if (handle != null) {

			resourceState = (SVGVisualResourceState) stateMap.get(handle);
		}

		return resourceState;
	}

	/**
	 * manages the display of the resource panel
	 * 
	 * @param handle
	 *            the current svg handle
	 */
	protected void handleVisualResources(SVGHandle handle) {

		if (hasBeenReinitialized && (!visualResourcesFrame.isVisible() || handle == null)) {

			return;
		}

		if (!visualResourcesFrame.isVisible()) {

			handle = null;
		}

		hasBeenReinitialized = (handle == null);

		// removes all the components of the panel of the resources
		visualResourcesPanel.removeAll();

		if (listsPanel != null) {

			listsPanel.dispose();
			listsPanel = null;
		}

		if (handle != null && visualResourcesFrame.isVisible()) {

			// getting the resource store
			visualResourceStore = editor.getResourcesManager().getResourceStore();

			// creates the list of the visual resource items
			LinkedList models = getVisualResourceModels(handle);

			// creates the panel
			if (models != null) {

				listsPanel = new SVGVisualResourceListsPanel(this, models);
			}

			// adds the resource panel into the container and displays it
			visualResourcesPanel.add(listsPanel);
			visualResourcesPanel.setPreferredSize(new Dimension(frameBounds.width,
					frameBounds.height));
			visualResourcesFrame.revalidate();

			return;
		}

		if (bundle != null) {

			// initializes the value of the last selected tab
			String message = "";

			try {
				message = bundle.getString("visualresources_empty_dialog_noframe");
			} catch (MissingResourceException ex) {
			}

			JLabel label = new JLabel(message);
			label.setBorder(new EmptyBorder(5, 5, 5, 5));

			visualResourcesPanel.add(label);
			visualResourcesPanel.setPreferredSize(null);
		}

		visualResourcesFrame.revalidate();
	}

	/**
	 * creates the resource items
	 * 
	 * @param handle
	 *            the current svg handle
	 * @return the list of the resource items
	 */
	protected LinkedList getVisualResourceModels(SVGHandle handle) {

		LinkedList items = new LinkedList();

		if (handle != null && docResources != null) {

			Element root = docResources.getDocumentElement();

			if (root != null) {

				// creates the visual resource items
				SVGVisualResourceModel model = null;

				for (Node cur = root.getFirstChild(); cur != null; cur = cur.getNextSibling()) {

					if (cur instanceof Element) {

						model = new SVGVisualResourceModel(this, (Element) cur);
						items.add(model);
					}
				}
			}
		}

		return items;
	}

	/**
	 * transforms a string taken from the xml document into the accurate string
	 * 
	 * @param value
	 *            a string
	 * @return the absolute string
	 */
	protected String getAbsoluteString(String value) {

		String str = new String(value);
		str = "vresource_".concat(str);

		return str;
	}

	/**
	 * transforms a string taken from the xml document into the accurate string
	 * 
	 * @param value
	 *            a string
	 * @return the normalized string
	 */
	protected String getNormalizedString(String value) {

		String str = new String(value);

		if (value.length() > 10) {

			str = str.substring(10, str.length());
		}

		return str;
	}
}
