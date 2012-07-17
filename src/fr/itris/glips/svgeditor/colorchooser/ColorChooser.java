/*
 * Created on 22 févr. 2005
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
package fr.itris.glips.svgeditor.colorchooser;

import javax.swing.*;
import fr.itris.glips.library.color.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;

/**
 * the class of the color chooser for this editor
 * 
 * @author ITRIS, Jordi SUC
 */
public class ColorChooser extends JColorChooser {

	/**
	 * the editor
	 */
	protected Editor editor;

	/**
	 * the flavor of a color
	 */
	private DataFlavor colorFlavor;

	/**
	 * the flavor of a svg w3c color
	 */
	private DataFlavor w3cSVGColorFlavor;

	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public ColorChooser(Editor editor) {

		this.editor = editor;

		// adds the w3c standard colors chooser panel
		SVGW3CColorChooserPanel w3cColorChooserPanel = new SVGW3CColorChooserPanel(editor);
		addChooserPanel(w3cColorChooserPanel);

		// creating the color flavors
		try {
			colorFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
					+ ";class=java.awt.Color");
			w3cSVGColorFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
					+ ";class=fr.itris.glips.library.color.SVGW3CColor");
//					+ ";class=fr.itris.glips.svgeditor.colorchooser.SVGW3CColor");
		} catch (Exception ex) {
			ex.printStackTrace();
			colorFlavor = DataFlavor.stringFlavor;
			w3cSVGColorFlavor = DataFlavor.stringFlavor;
		}
	}

	/**
	 * shows a color chooser dialog
	 * 
	 * @param initialColor
	 *            the initial Color set when the color-chooser is shown
	 * @return the selected color or <code>null</code> if the user opted out
	 */
	public Color showColorChooserDialog(Color initialColor) {

		setColor(initialColor);
		SVGColorTracker ok = new SVGColorTracker(this);
		JDialog dialog = createDialog(Editor.getParent(), "", true, this, ok, null);
		dialog.setVisible(true);

		return ok.getColor();
	}

	/**
	 * Returns the color corresponding to the given string
	 * 
	 * @param handle
	 *            a svg handle
	 * @param colorString
	 *            a string representing a color
	 * @return the color corresponding to the given string
	 */
	public Color getColor(SVGHandle handle, String colorString) {

		return SVGColorsManager.getColor(colorString);
	}

	/**
	 * Returns the string representation of the given color
	 * 
	 * @param color
	 *            a color
	 * @return the string representation of the given color
	 */
	public String getColorString(Color color) {

		return SVGColorsManager.getColorString(color);
	}

	/**
	 * checks each color value in each attribute and in the style property of
	 * the given element
	 * 
	 * @param handle
	 *            a svg handle
	 * @param element
	 *            an element
	 */
	@SuppressWarnings(value = "all")
	public void checkColorString(SVGHandle handle, Element element) {

	}

	/**
	 * returns the data flavor of the given color
	 * 
	 * @param color
	 *            a color
	 * @return the data flavor of the given color
	 */
	public DataFlavor getColorFlavor(Color color) {

		DataFlavor flavor = null;

		if (color != null) {

			if (color instanceof SVGW3CColor) {

				flavor = w3cSVGColorFlavor;

			} else {

				flavor = colorFlavor;
			}
		}

		return flavor;
	}

	/**
	 * returns whether the given flavor is a color flavor
	 * 
	 * @param flavor
	 *            a flavor
	 * @return whether the given flavor is a color flavor
	 */
	public boolean isColorDataFlavor(DataFlavor flavor) {

		boolean isColorDataFlavor = false;

		if (flavor != null) {

			isColorDataFlavor = (flavor.isMimeTypeEqual(colorFlavor) || flavor
					.isMimeTypeEqual(w3cSVGColorFlavor));
		}

		return isColorDataFlavor;
	}

	/**
	 * @return the list of the data flavors
	 */
	public Collection<DataFlavor> getColorDataFlavors() {

		LinkedList<DataFlavor> dataFlavors = new LinkedList<DataFlavor>();

		dataFlavors.add(colorFlavor);
		dataFlavors.add(w3cSVGColorFlavor);

		return dataFlavors;
	}

	/**
	 * the project file corresponding to the given uri
	 * 
	 * @param uri
	 *            a uri
	 * @return the project file corresponding to the given uri
	 */
	@SuppressWarnings(value = "all")
	public File getProjectFile(String uri) {

		return null;
	}

	/**
	 * @return Returns the editor.
	 */
	public Editor getSVGEditor() {
		return editor;
	}

	/**
	 * the class of the svg color tracker
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	protected class SVGColorTracker implements ActionListener {

		/**
		 * the color chooser
		 */
		private JColorChooser chooser;

		/**
		 * the color
		 */
		private Color color;

		/**
		 * the constructor of the class
		 * 
		 * @param c
		 *            the color chooser
		 */
		public SVGColorTracker(JColorChooser c) {

			chooser = c;
		}

		public void actionPerformed(ActionEvent e) {
			color = chooser.getColor();
		}

		/**
		 * @return the color
		 */
		public Color getColor() {
			return color;
		}
	}
}
