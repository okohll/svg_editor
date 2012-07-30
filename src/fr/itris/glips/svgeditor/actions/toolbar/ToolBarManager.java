package fr.itris.glips.svgeditor.actions.toolbar;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling the toolbars
 * 
 * @author Jordi SUC
 */
public class ToolBarManager {

	/**
	 * the panel that will contain the tool bars
	 */
	private JToolBar toolsBar = new JToolBar();

	/**
	 * the constructor of the class
	 */
	public ToolBarManager() {
	}

	/**
	 * initializes the toolbar after all the parts are initialized
	 */
	public void initializeParts() {

		// setting the properties of the menu bar
		toolsBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		// getting all the tool items of the application
		Collection<Module> modules = Editor.getEditor().getSVGModuleLoader().getModules();
		Map<String, AbstractButton> toolItems = new HashMap<String, AbstractButton>();
		Map<String, AbstractButton> items = null;

		for (Module module : modules) {

			if (module != null) {

				items = module.getToolItems();

				if (items != null) {

					toolItems.putAll(items);
				}
			}
		}

		// getting the xml document describing the tool bars
		Document doc = null;
		// Try plugin
		doc = ResourcesManager.getXMLDocument("tool.xml");
		// getting the root node
		Element rootElement = doc.getDocumentElement();
		ToolBar toolBar = null;

		// creating all the toolbars defined in the xml document
		for (Node node = rootElement.getFirstChild(); node != null; node = node.getNextSibling()) {

			if (node instanceof Element) {

				// creating the toolbar
				toolBar = new ToolBar(toolItems, (Element) node);

				// adding the tool bar to the menubar
				toolsBar.add(toolBar.getToolBar());
			}
		}
	}

	/**
	 * @return the component containing all the tool bars
	 */
	public JComponent getToolsBar() {
		return toolsBar;
	}
}
