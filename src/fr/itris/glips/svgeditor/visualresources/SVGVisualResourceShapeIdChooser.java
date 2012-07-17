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
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import fr.itris.glips.svgeditor.Editor;

import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the dialog for choosing the id of a shape
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceShapeIdChooser {

	/**
	 * a small font
	 */
	private static final Font smallFont = new Font("smallFont", Font.ROMAN_BASELINE, 9);

	/**
	 * the font
	 */
	private static final Font theFont = new Font("theFont", Font.ROMAN_BASELINE, 10);

	/**
	 * the last id selected
	 */
	private static String selectedId = "";

	/**
	 * the labels
	 */
	private static String titleLabel = "", titledBorderLabel = "", okLabel = "", cancelLabel = "",
			alertMessage = "", errorTitle = "";

	static {

		// getting the bundle
		ResourceBundle bundle = ResourcesManager.bundle;

		// getting the labels
		if (bundle != null) {

			try {
				titleLabel = bundle.getString("labelnew");
				titledBorderLabel = bundle.getString("vresource_displaywindowchooserforshapeid");
				okLabel = bundle.getString("labelok");
				cancelLabel = bundle.getString("labelcancel");
				alertMessage = bundle.getString("vresource_idnotselected");
				errorTitle = bundle.getString("labelerror");
			} catch (MissingResourceException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * shows the id shape chooser dialog
	 * 
	 * @param handle
	 *            a svg handle
	 * @return the selected id
	 */
	public static String showShapeChooserIdDialog(SVGHandle handle) {

		selectedId = "";

		if (handle != null) {

			// creating the dialog
			Container parentContainer = Editor.getParent();
			Frame parentFrame = null;

			if (parentContainer instanceof Frame) {

				parentFrame = (Frame) parentContainer;

			} else {

				parentFrame = new JFrame("");
			}

			final JDialog dialog = new JDialog(parentFrame, titleLabel, true);

			JPanel dialogPanel = new JPanel();
			dialogPanel.setLayout(new BorderLayout());

			// the combo box
			final JComboBox combo = new JComboBox();

			// the list of the ids of the nodes contained in the document
			LinkedList nodesIds = handle.getSvgElementsManager().getShapeNodesIds();

			// filling the combo box with the items
			SVGComboItem item = null;
			String id = "";

			// the empty item
			item = new SVGComboItem("", "");
			combo.addItem(item);

			// for each ids contained in the list
			for (Iterator it = nodesIds.iterator(); it.hasNext();) {

				try {
					id = (String) it.next();
				} catch (NoSuchElementException ex) {
					ex.printStackTrace();
					id = null;
				}

				if (id != null && !id.equals("")) {

					item = new SVGComboItem(id, id);
					combo.addItem(item);
				}
			}

			// the listener to the combo box
			final ActionListener comboListener = new ActionListener() {

				public void actionPerformed(ActionEvent evt) {

					String value = "";

					if (combo.getSelectedItem() != null) {

						value = ((SVGComboItem) combo.getSelectedItem()).getValue();
					}

					// modifies the widgetValue of the property item
					if (value != null && !value.equals("")) {

						selectedId = value;
					}
				}
			};

			// adds a listener to the combo box
			combo.addActionListener(comboListener);

			// creating the buttons
			final JButton okBt = new JButton(okLabel), cancelBt = new JButton(cancelLabel);
			final String falertMessage = alertMessage, ferrorTitle = errorTitle;

			// the listener to the buttons
			final ActionListener buttonsListener = new ActionListener() {

				public void actionPerformed(ActionEvent evt) {

					if (evt.getSource().equals(okBt)) {

						if (selectedId == null || (selectedId != null && selectedId.equals(""))) {

							JOptionPane.showMessageDialog(Editor.getEditor().getParent(),
									falertMessage, ferrorTitle, JOptionPane.ERROR_MESSAGE);
							return;
						}

					} else {

						selectedId = "";
					}

					// removes the listeners
					combo.removeActionListener(comboListener);
					okBt.removeActionListener(this);
					cancelBt.removeActionListener(this);
					dialog.setVisible(false);
				}
			};

			// adding the listener to the buttons
			okBt.addActionListener(buttonsListener);
			cancelBt.addActionListener(buttonsListener);

			// dealing with the dialog close button
			dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			// the panel containing the combo box
			JPanel cPanel = new JPanel();
			cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.X_AXIS));
			cPanel.setBorder(new EmptyBorder(0, 20, 0, 0));
			cPanel.add(combo);

			// the panel that will be displayed
			JPanel comboPanel = new JPanel();
			comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
			comboPanel.add(cPanel);

			// setting the border
			TitledBorder border = new TitledBorder(titledBorderLabel);
			comboPanel.setBorder(border);

			// the buttons panel
			JPanel buttons = new JPanel();
			buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
			buttons.add(okBt);
			buttons.add(cancelBt);

			// the content panel
			JPanel content = new JPanel();
			content.setLayout(new BorderLayout());
			content.add(comboPanel, BorderLayout.CENTER);
			content.add(buttons, BorderLayout.SOUTH);

			// adding the content pane to the dialog box
			dialog.getContentPane().add(content);

			// packing the dialog
			dialog.pack();

			// seting the location for the dialog
			dialog.setLocation(
					(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - dialog
							.getSize().width / 2), (int) (Toolkit.getDefaultToolkit()
							.getScreenSize().getHeight() / 2 - dialog.getSize().height / 2));

			// displays the dialog
			dialog.setVisible(true);

			while (dialog.isVisible()) {

				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}

		return selectedId;
	}

}
