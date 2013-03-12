/*
 * Created on 2 juin 2004
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
package fr.itris.glips.svgeditor.resources;

import fr.itris.glips.library.*;
import fr.itris.glips.svgeditor.*;
import org.apache.batik.dom.svg.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.awt.*;
import java.util.prefs.*;

/**
 * @author ITRIS, Jordi SUC
 * 
 *         The class managing the resources
 */
public class ResourcesManager {

	/**
	 * the constant id for the recent files preference
	 */
	protected static String RECENT_FILES_PREFERENCE_ID = "RecentFiles";

	/**
	 * the current directory for the system
	 */
	private File currentDirectory;

	/**
	 * the resource bundle
	 */
	public static ResourceBundle bundle = null;

	static {

		try {
			// Try to load plugin
			bundle = ResourceBundle
					.getBundle("com.gtwm.svgeditor.plugins.resources.properties.SVGEditorStrings");
			System.out.println("Loaded plugin SVGEditorStrings");
		} catch (MissingResourceException mrex) {
			// Fall back to built in
			try {
				bundle = ResourceBundle
						.getBundle("fr.itris.glips.svgeditor.resources.properties.SVGEditorStrings");
				System.out.println("Loaded built in SVGEditorStrings");
			} catch (MissingResourceException ex) {
				ex.printStackTrace();
				bundle = null;
			}
		}
	}

	/**
	 * the HashMap associating a an icon's name to an icon
	 */
	private static final HashMap<String, ImageIcon> icons = new HashMap<String, ImageIcon>();

	/**
	 * the HashMap associating a an icon's name to a gray icon
	 */
	private static final HashMap<String, ImageIcon> grayIcons = new HashMap<String, ImageIcon>();

	/**
	 * the list of the recent files
	 */
	private final java.util.List<String> recentFiles = Collections
			.synchronizedList(new LinkedList<String>());

	/**
	 * the map associating the name of a xml document to this document
	 */
	private final static HashMap<String, Document> cachedXMLDocuments = new HashMap<String, Document>();

	/**
	 * the set of the listeners to the recent files
	 */
	private Set<RecentFilesListener> recentFilesListeners = Collections
			.synchronizedSet(new HashSet<RecentFilesListener>());

	/**
	 * the document of the xml properties
	 */
	private Document xmlProperties;

	/**
	 * the document of the resource store
	 */
	private Document resourceStore;

	/**
	 * the set of the style element names
	 */
	private final HashSet<String> styleProperties = new HashSet<String>();

	/**
	 * the list of the runnables that will be run when the editor is exited
	 */
	private final LinkedList<Runnable> exitRunnables = new LinkedList<Runnable>();

	/**
	 * the editor
	 */
	private Editor editor;

	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public ResourcesManager(Editor editor) {

		this.editor = editor;

		// creates the list of the recently opened files
		Preferences recentFilesPreferences = PreferencesStore
				.getPreferenceNode(RECENT_FILES_PREFERENCE_ID);
		String[] keys = null;

		try {
			keys = recentFilesPreferences.keys();
		} catch (IllegalStateException | BackingStoreException ex) {
			ex.printStackTrace();
		}

		if (keys != null) {

			// filling the list of the recent files
			String val = "";

			for (int i = 0; i < keys.length; i++) {

				val = recentFilesPreferences.get(keys[i], null);

				if (val != null && !val.equals("")) {

					recentFiles.add(val);
				}
			}
		}
	}

	/**
	 * @return the editor
	 */
	public Editor getSVGEditor() {
		return editor;
	}

	/**
	 * computes the path of a resource given its name
	 * 
	 * @param resource
	 *            the name of a resource
	 * @return the full path of a resource or an empty string if the resource
	 *         wasn't found
	 */
	public static String getPath(String resource) {
		// TODO: extract plugin folder into properties file or other source
		String pluginResourceFolder = "/com/gtwm/svgeditor/plugins/resources";
		String path = "";
		URL resourceURL = null;
		// Try plugin first
		resourceURL = ResourcesManager.class.getResource(pluginResourceFolder + "/" + resource);
		if (resourceURL == null) {
			resourceURL = ResourcesManager.class.getResource(resource);
		} else {
			System.out.println("Plugin resource found: " + resource);
		}
		if (resourceURL == null) {
			System.err.println("Resource not found for " + resource);
		} else {
			path = resourceURL.toExternalForm();
		}
		return path;
	}

	/**
	 * gives an ImageIcon object given the name of it as it is witten in the
	 * SVGEditorIcons.properties file
	 * 
	 * @param name
	 *            the name of an icon
	 * @param isGrayIcon
	 *            true if the icon should be used for a disabled widget
	 * @return an image icon
	 * 
	 *         TODO: use a cache for the bundle
	 */
	public static ImageIcon getIcon(String name, boolean isGrayIcon) {

		ImageIcon icon = null;

		if (name != null && !name.equals("")) {

			if (icons.containsKey(name)) {

				if (isGrayIcon) {

					icon = grayIcons.get(name);

				} else {

					icon = icons.get(name);
				}

			} else {

				// gets the name of the icons from the resources
				ResourceBundle iconsBundle = null;
				try {
					// Try plugins first
					iconsBundle = ResourceBundle
							.getBundle("com.gtwm.svgeditor.plugins.resources.properties.SVGEditorIcons");
				} catch (MissingResourceException mrex) {
					try {
						// Fall back to built in
						iconsBundle = ResourceBundle
								.getBundle("fr.itris.glips.svgeditor.resources.properties.SVGEditorIcons");
					} catch (MissingResourceException ex) {
						ex.printStackTrace();
					}
				}

				String path = "";

				if (iconsBundle != null) {

					try {
						path = iconsBundle.getString(name);
					} catch (MissingResourceException ex) {
						ex.printStackTrace();
						path = "";
					}

					if (path != null && !path.equals("")) {

						try {
							String iconPath = getPath("icons/" + path);
							if (!iconPath.equals("")) {
								icon = new ImageIcon(new URL(iconPath));
								System.out.println("Icon width for " + iconPath + " is " + icon.getIconWidth());
							} else {
								System.err.println("Icon file not found: icons/" + path);
							}
						} catch (MalformedURLException ex) {
							ex.printStackTrace();
						}

						if (icon != null) {

							icons.put(name, icon);
							Image image = icon.getImage();

							ImageIcon grayIcon = new ImageIcon(
									GrayFilter.createDisabledImage(image));
							grayIcons.put(name, grayIcon);

							if (isGrayIcon) {

								icon = grayIcon;
							}
						}
					}
				}
			}
		}
		return icon;
	}

	/**
	 * @return the current directory
	 */
	public File getCurrentDirectory() {
		return currentDirectory;
	}

	/**
	 * sets he current directory
	 * 
	 * @param directory
	 *            a file representing a directory
	 */
	public void setCurrentDirectory(File directory) {
		currentDirectory = directory;
	}

	/**
	 * create a document from the given file in the resource files
	 * 
	 * @param name
	 *            the name of the xml file
	 * @return the document
	 */
	public static Document getXMLDocument(String name) {

		Document doc = null;

		if (name != null && !name.equals("")) {

			if (cachedXMLDocuments.containsKey(name)) {

				doc = cachedXMLDocuments.get(name);

			} else {

				DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();

				String path = "";

				try {
					// parses the XML file
					DocumentBuilder docBuild = docBuildFactory.newDocumentBuilder();
					path = getPath("xml/" + name);
					if (path.length() > 0) {
						doc = docBuild.parse(path);
					}
				} catch (IOException | SAXException | ParserConfigurationException ex) {
					ex.printStackTrace();
				}

				if (doc != null) {

					cachedXMLDocuments.put(name, doc);
				}
			}
		}

		return doc;
	}

	/**
	 * @return the resource store document
	 */
	public Document getResourceStore() {

		if (resourceStore != null) {

			SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(
					"org.apache.xerces.parsers.SAXParser");

			try {
				resourceStore = factory.createDocument(getPath("xml/visualResourceStore.xml"));
			} catch (IOException ex) {
				ex.printStackTrace();
				resourceStore = null;
			}
		}

		return resourceStore;
	}

	/**
	 * writes a documents to a file
	 * 
	 * @param path
	 *            the path of the file in which the document will be written
	 * @param doc
	 *            the document to be written
	 */
	public void writeXMLDocument(String path, Document doc) {

		if (doc != null && path != null && !path.equals("")) {

			Element root = doc.getDocumentElement();

			if (root != null) {

				String res = "<?xml version=\"1.0\" ?>";

				res = printChild(root, res);

				byte[] result = new byte[0];

				try {
					result = res.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// writes the string
				OutputStream writer = null;

				try {
					writer = new BufferedOutputStream(new FileOutputStream(new URI(getPath("xml/"
							+ path)).getPath()));
				} catch (IOException | URISyntaxException ex) {
					ex.printStackTrace();
				}

				if (writer != null) {

					try {
						writer.write(result, 0, result.length);
						writer.flush();
						writer.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * the recursive function allowing to concat the string representation of
	 * the given node
	 * 
	 * @param node
	 *            a node
	 * @param str
	 *            a string
	 * @return the string representation of the subtree of the given node
	 */
	protected String printChild(Node node, String str) {

		String res = new String(str);

		if (node instanceof Element) {

			// writes the node name
			res = res.concat("<" + node.getNodeName() + " ");

			// writes the attributes
			NamedNodeMap att = node.getAttributes();
			Node at = null;

			for (int i = 0; i < att.getLength(); i++) {

				at = att.item(i);

				if (at != null) {

					res = res.concat(at.getNodeName() + "=\"" + at.getNodeValue() + "\" ");
				}
			}

			// writes the children
			Node cur = null;

			if (node.hasChildNodes()) {

				res = res.concat(">");
				cur = node.getFirstChild();

			} else {

				res = res.concat("/>");
			}

			while (cur != null) {

				res = printChild(cur, res);

				cur = cur.getNextSibling();
			}

			if (node.hasChildNodes()) {

				res = res.concat("</" + node.getNodeName() + ">");
			}
		}

		return res;
	}

	/**
	 * @return the collection of the recently opened files
	 */
	public LinkedList<String> getRecentFiles() {

		return new LinkedList<String>(recentFiles);
	}

	/**
	 * adds a new file to the list of the recent files
	 * 
	 * @param fileName
	 *            the name of the file
	 */
	public void addRecentFile(String fileName) {

		if (recentFiles != null && fileName != null && !fileName.equals("")) {

			if (recentFiles.contains(fileName)) {

				// if the file name is already contained in the list, it is
				// removed
				recentFiles.remove(fileName);

			} else if (recentFiles.size() > 5) {

				recentFiles.remove(recentFiles.size() - 1);
			}

			recentFiles.add(0, fileName);
		}
	}

	/**
	 * removes a the file that has the given name from the list of the recent
	 * files
	 * 
	 * @param fileName
	 *            the name of the file
	 */
	public void removeRecentFile(String fileName) {

		if (recentFiles != null && fileName != null && !fileName.equals("")
				&& recentFiles.contains(fileName)) {

			recentFiles.remove(fileName);
		}
	}

	/**
	 * saves the list of the recent files into a file
	 */
	protected void saveRecentFiles() {

		if (recentFiles != null) {

			try {
				// getting the recent files preferences node
				Preferences recentFilesPreferences = PreferencesStore
						.getPreferenceNode(RECENT_FILES_PREFERENCE_ID);

				// removing all the child nodes from the preference node
				String[] keys = recentFilesPreferences.keys();

				if (keys != null) {

					// filling the list of the recent files
					for (int i = 0; i < keys.length; i++) {

						recentFilesPreferences.remove(keys[i]);
					}
				}

				if (recentFiles.size() > 0) {

					int n = 0;

					for (String rf : recentFiles) {

						if (rf != null && !rf.equals("")) {

							recentFilesPreferences.put(n + "", rf);
							n++;
						}
					}

					recentFilesPreferences.flush();
				}
			} catch (BackingStoreException ex) {
				ex.printStackTrace();
			}
		}

		notifyListeners();
	}

	/**
	 * adds a new listener to the recent files list changes
	 * 
	 * @param listener
	 *            a listener
	 */
	public void addRecentFilesListener(RecentFilesListener listener) {

		if (listener != null) {

			recentFilesListeners.add(listener);
		}
	}

	/**
	 * removes the listener from the recent files list changes
	 * 
	 * @param listener
	 *            a listener
	 */
	public void removeRecentFilesListener(RecentFilesListener listener) {

		if (listener != null) {

			recentFilesListeners.remove(listener);
		}
	}

	/**
	 * notifies that the recent files list has changed
	 */
	public void notifyListeners() {

		LinkedList<String> recentFilesList = new LinkedList<String>(recentFiles);

		for (RecentFilesListener listener : new HashSet<RecentFilesListener>(recentFilesListeners)) {

			listener.recentFilesListChanged(recentFilesList);
		}
	}

	/**
	 * @return the list of the style properties found in the properties.xml file
	 */
	public HashSet<String> getAttributesToTranslate() {

		if (styleProperties.size() <= 0) {

			Document doc = null;

			if (xmlProperties != null) {

				doc = xmlProperties;

			} else {

				doc = getXMLDocument("properties.xml");
			}

			if (doc != null) {

				Node root = doc.getDocumentElement();

				if (root != null) {

					// the node iterator
					Node node = null;
					String str = "";

					// for each property npde
					for (NodeIterator it = new NodeIterator(root); it.hasNext();) {

						node = it.next();

						if (node != null && node instanceof Element
								&& node.getNodeName().equals("property")) {

							// tests if the node is a style property
							if (((Element) node).getAttribute("type") != null
									&& ((Element) node).getAttribute("type").equals("style")) {

								str = ((Element) node).getAttribute("name");
								// gets the name of the stye property
								if (str != null && !str.equals("")) {

									str = str.substring(9, str.length());
									styleProperties.add(str);
								}
							}
						}
					}
				}
			}
		}

		return styleProperties;
	}

	/**
	 * adds a runnable to the list of the runnables that will be run when the
	 * editor is exited
	 * 
	 * @param runnable
	 *            a runnable
	 */
	public void addExitRunnable(Runnable runnable) {

		synchronized (this) {
			exitRunnables.add(runnable);
		}
	}

	/**
	 * saves the editor's current state before it is exited
	 */
	public void saveEditorsCurrentState() {

		// saves the current files list
		saveRecentFiles();

		// runs the list of the runnables
		LinkedList<Runnable> exitRun = new LinkedList<Runnable>(exitRunnables);

		for (Runnable runnable : exitRun) {

			if (runnable != null) {

				runnable.run();
			}
		}
	}
}
