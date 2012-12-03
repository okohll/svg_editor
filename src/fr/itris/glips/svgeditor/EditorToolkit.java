/*
 * Created on 21 juin 2004
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
package fr.itris.glips.svgeditor;

import org.w3c.dom.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.plaf.metal.*;
import fr.itris.glips.library.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * a class providing utility methods
 * 
 * @author ITRIS, Jordi SUC, Maciej Wojtkiewicz
 */
public class EditorToolkit {

	/**
	 * the xml file extension
	 */
	public static final String XML_FILE_EXTENSION = ".xml";

	/**
	 * the svg file extension
	 */
	public static final String SVG_FILE_EXTENSION = ".svg";

	/**
	 * the svgz file extension
	 */
	public static final String SVGZ_FILE_EXTENSION = ".svgz";

	/**
	 * the name space for declaring namespaces
	 */
	public static final String xmlnsNS = "http://www.w3.org/2000/xmlns/";

	/**
	 * the xlink attribute namespace name
	 */
	public static final String xmlnsXLinkAttributeName = "xmlns:xlink";

	/**
	 * the xlink prefix
	 */
	public static final String xLinkprefix = "xlink:";

	/**
	 * the xlink namespace
	 */
	public static final String xmlnsXLinkNS = "http://www.w3.org/1999/xlink";

	/**
	 * the svg namespace
	 */
	public static final String svgNS = "http://www.w3.org/2000/svg";

	/**
	 * the jwidget tag name
	 */
	public static final String jwidgetTagName = "rtda:jwidget";

	/**
	 * the decimal formatter
	 */
	public static DecimalFormat format = null;

	/**
	 * the map associating the name of a svg element shape to its label
	 */
	protected static HashMap<String, String> svgElementLabels = new HashMap<String, String>();

	/**
	 * the set of the available svg shape elements
	 */
	protected static Set<String> svgShapeElementNames = new HashSet<String>();

	/**
	 * the label for an unknow shape
	 */
	public static String unknownShapeLabel = "";

	static {

		// the object used to convert double values into strings
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("######.#", symbols);

		svgElementLabels.put("g", ResourcesManager.bundle.getString("svgElementName_g"));
		svgElementLabels
				.put("ellipse", ResourcesManager.bundle.getString("svgElementName_ellipse"));
		svgElementLabels.put("image", ResourcesManager.bundle.getString("svgElementName_image"));
		svgElementLabels.put("path", ResourcesManager.bundle.getString("svgElementName_path"));
		svgElementLabels.put("rect", ResourcesManager.bundle.getString("svgElementName_rect"));
		svgElementLabels.put("text", ResourcesManager.bundle.getString("svgElementName_text"));

		unknownShapeLabel = ResourcesManager.bundle.getString("svgElementName_unknown");

		svgShapeElementNames.add("g");
		svgShapeElementNames.add("ellipse");
		svgShapeElementNames.add("image");
		svgShapeElementNames.add("path");
		svgShapeElementNames.add("rect");
		svgShapeElementNames.add("text");
	}

	/**
	 * computes and returns the value of the given attribute, in pixels,
	 * provided that it's a numeric value
	 * 
	 * @param element
	 *            an element
	 * @param attributeName
	 *            the name of an attribute
	 * @return the value of the given attribute, provided that it's a numeric
	 *         value
	 */
	public static double getAttributeValue(Element element, String attributeName) {

		double value = 0;

		try {
			value = Double.parseDouble(element.getAttributeNS(null, attributeName));
		} catch (NumberFormatException ex) {
			System.err.println("Error on attribute " + attributeName + ", element " + element.getNodeName() + ", " + element.getNodeType());
			//ex.printStackTrace();
			value = 0;
			//value = Double.NaN;
		}

		return value;
	}

	/**
	 * computes and returns the value of the given attribute, in pixels,
	 * provided that it's a numeric value
	 * 
	 * @param element
	 *            an element
	 * @param attributeName
	 *            the name of an attribute
	 * @param value
	 *            the new value for the attribute
	 */
	public static void setAttributeValue(Element element, String attributeName, double value) {

		element.setAttributeNS(null, attributeName, FormatStore.format(value));
	}

	/**
	 * checks if the xlink namespace is defined in the given document
	 * 
	 * @param doc
	 *            a document
	 */
	public static void checkXLinkNameSpace(Document doc) {

		if (doc != null
				&& !doc.getDocumentElement().hasAttributeNS(xmlnsNS, xmlnsXLinkAttributeName)) {

			doc.getDocumentElement().setAttributeNS(xmlnsNS, xmlnsXLinkAttributeName, xmlnsXLinkNS);
		}
	}

	/**
	 * checks if the given document contains the given name space, if not, the
	 * namespace is added
	 * 
	 * @param doc
	 *            a svg document
	 * @param prefix
	 *            the name space prefix
	 * @param nameSpace
	 *            a name space
	 */
	public static void checkXmlns(Document doc, String prefix, String nameSpace) {

		if (doc != null && prefix != null && nameSpace != null) {

			Element svgRoot = doc.getDocumentElement();

			if (!svgRoot.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix)) {

				svgRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix,
						nameSpace);
			}
		}
	}

	/**
	 * @return creates and returns a combo box enabling to choose the units
	 */
	public JComboBox getUnitsComboBoxChooser() {

		// creating the items
		String[] items = new String[] { "px", "pt", "pc", "mm", "cm", "in" };

		JComboBox combo = new JComboBox(items);
		combo.setSelectedIndex(0);

		return combo;
	}

	/**
	 * returns the previous element sibling of the given element
	 * 
	 * @param element
	 *            an element
	 * @return the previous element sibling of the given element
	 */
	public static Element getPreviousElementSibling(Element element) {

		Element previousSibling = null;

		if (element != null) {

			Node cur = null;

			for (cur = element.getPreviousSibling(); cur != null; cur = cur.getPreviousSibling()) {

				if (cur instanceof Element) {

					previousSibling = (Element) cur;
					break;
				}
			}
		}

		return previousSibling;
	}

	/**
	 * returns the next element sibling of the given element
	 * 
	 * @param element
	 *            an element
	 * @return the next element sibling of the given element
	 */
	public static Element getNextElementSibling(Element element) {

		Element nextSibling = null;

		if (element != null) {

			Node cur = null;

			for (cur = element.getNextSibling(); cur != null; cur = cur.getNextSibling()) {

				if (cur instanceof Element) {

					nextSibling = (Element) cur;
					break;
				}
			}
		}

		return nextSibling;
	}

	/**
	 * computes the number corresponding to this string in pixel
	 * 
	 * @param str
	 * @return the number corresponding to this string in pixel
	 */
	public static double getPixelledNumber(String str) {

		double i = 0;

		if (str != null && !str.equals("")) {

			str = str.trim();

			if (!Character.isDigit(str.charAt(str.length() - 1))) {

				String unit = str.substring(str.length() - 2, str.length());
				String nb = str.substring(0, str.length() - 2);

				try {
					i = Double.parseDouble(nb);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}

				if (unit.equals("pt")) {

					i = i * 1.25;

				} else if (unit.equals("pc")) {

					i = i * 15;

				} else if (unit.equals("mm")) {

					i = i * 3.543307;

				} else if (unit.equals("cm")) {

					i = i * 35.43307;

				} else if (unit.equals("in")) {

					i = i * 90;
				}

			} else {

				try {
					i = Double.parseDouble(str);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}

		return i;
	}

	/**
	 * converts the given pixelled value into the given units value
	 * 
	 * @param value
	 *            the pixelled value
	 * @param unit
	 *            the new unit
	 * @return the value in the given units
	 */
	public static double convertFromPixelToUnit(double value, String unit) {

		double i = value;

		if (unit != null && !unit.equals("")) {

			unit = unit.trim();

			if (unit.equals("pt")) {

				i = value / 1.25;

			} else if (unit.equals("pc")) {

				i = value / 15;

			} else if (unit.equals("mm")) {

				i = value / 3.543307;

			} else if (unit.equals("cm")) {

				i = value / 35.43307;

			} else if (unit.equals("in")) {

				i = value / 90;
			}
		}

		return i;
	}

	/**
	 * computes a rectangle given the coordinates of two points
	 * 
	 * @param point1
	 *            the first point
	 * @param point2
	 *            the second point
	 * @return the correct rectangle
	 */
	public static Rectangle2D getComputedRectangle(Point2D point1, Point2D point2) {

		if (point1 != null && point2 != null) {

			double width = point2.getX() - point1.getX(), height = point2.getY() - point1.getY(), x = point1
					.getX(), y = point1.getY();

			if (point1.getX() > point2.getX() && point1.getY() > point2.getY()) {

				x = point2.getX();
				y = point2.getY();
				width = point1.getX() - point2.getX();
				height = point1.getY() - point2.getY();

			} else if (point1.getX() > point2.getX() && point1.getY() < point2.getY()) {

				width = point1.getX() - point2.getX();
				height = point2.getY() - point1.getY();
				x = point2.getX();
				y = point1.getY();

			} else if (point1.getX() < point2.getX() && point1.getY() > point2.getY()) {

				width = point2.getX() - point1.getX();
				height = point1.getY() - point2.getY();
				x = point1.getX();
				y = point2.getY();
			}

			return new Rectangle2D.Double(x, y, width, height);
		}

		return new Rectangle2D.Double(0, 0, 0, 0);
	}

	/**
	 * computes a square given the coordinates of two points
	 * 
	 * @param point1
	 *            the first point
	 * @param point2
	 *            the second point
	 * @return the correct square
	 */
	public static Rectangle2D getComputedSquare(Point2D point1, Point2D point2) {

		if (point1 != null && point2 != null) {

			double width = point2.getX() - point1.getX(), height = point2.getY() - point1.getY(), x = point1
					.getX(), y = point1.getY();

			if (point1.getX() > point2.getX() && point1.getY() > point2.getY()) {

				x = point2.getX();
				y = point2.getY();
				width = point1.getX() - point2.getX();
				height = point1.getY() - point2.getY();

				if (width < height) {

					y = point2.getY() + (height - width);
					height = width;

				} else {

					x = point2.getX() + (width - height);
					width = height;
				}

			} else if (point1.getX() > point2.getX() && point1.getY() <= point2.getY()) {

				width = point1.getX() - point2.getX();
				height = point2.getY() - point1.getY();
				x = point2.getX();
				y = point1.getY();

				if (width < height) {

					height = width;

				} else {

					x = point2.getX() + (width - height);
					width = height;
				}

			} else if (point1.getX() <= point2.getX() && point1.getY() > point2.getY()) {

				width = point2.getX() - point1.getX();
				height = point1.getY() - point2.getY();
				x = point1.getX();
				y = point2.getY();

				if (width < height) {

					y = point2.getY() + (height - width);
					height = width;

				} else {

					width = height;
				}

			} else if (point1.getX() <= point2.getX() && point1.getY() <= point2.getY()) {

				if (width < height) {

					height = width;

				} else {

					width = height;
				}
			}

			return new Rectangle2D.Double(x, y, width, height);
		}

		return new Rectangle2D.Double(0, 0, 0, 0);
	}

	/**
	 * returns whether the given element is a shape node or not
	 * 
	 * @param element
	 * @return whether the given element is a shape node or not
	 */
	public static boolean isElementAShape(Element element) {

		if (element != null) {

			return svgShapeElementNames.contains(element.getNodeName());
		}

		return false;
	}

	/**
	 * returns the label corresponding to the given element
	 * 
	 * @param element
	 *            an element
	 * @return the label corresponding to the given element
	 */
	public static String getElementLabel(Element element) {

		if (element != null && svgElementLabels.containsKey(element.getTagName())) {

			return svgElementLabels.get(element.getTagName());
		}

		return unknownShapeLabel;
	}

	/**
	 * force a refresh of the current selection
	 */
	public static void forceReselection() {

		// getting the current handle
		SVGHandle handle = Editor.getEditor().getHandlesManager().getCurrentHandle();

		if (handle != null) {

			handle.getSelection().refreshSelection(true);
		}
	}

	/**
	 * converts a string to a double that is a percentage
	 * 
	 * @param str
	 *            a string
	 * @param isPercentage
	 *            the boolean telling if the string describes a percentage value
	 * @return the corresponding value of the given string
	 */
	public double getDoubleValue(String str, boolean isPercentage) {

		if (str == null) {
			str = "";
		}
		// Addition by Oliver
		if (str.equals("")) {
			return 0;
		}

		str = str.replaceAll("\\s+", "");

		double val = 0;
		boolean hasPercentSign = (str.indexOf("%") != -1);

		try {
			if (isPercentage) {

				if (hasPercentSign) {

					str = str.replaceAll("%", "");
					val = Double.parseDouble(str);

				} else {

					val = Double.parseDouble(str);
					val = val * 100;
				}

			} else {

				val = Double.parseDouble(str);
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}

		return val;
	}

	/**
	 * picks the color at the given point on the screen
	 * 
	 * @param point
	 *            a point in the screen coordinates
	 * @return the color corresponding to the given point
	 */
	public Color pickColor(Point point) {

		Color color = new Color(255, 255, 255);

		if (point != null) {

			try {
				// getting the color at this point
				Robot robot = new Robot();
				color = robot.getPixelColor(point.x, point.y);
			} catch (AWTException ex) {
				ex.printStackTrace();
			}
		}

		return color;
	}

	/**
	 * returns an icon displaying the given color
	 * 
	 * @param color
	 *            a color
	 * @param size
	 *            the size of the image to be returned
	 * @return an icon displaying the given color
	 */
	public Image getImageFromColor(Color color, Dimension size) {

		if (color != null && size != null && size.width > 0 && size.height > 0) {

			BufferedImage image = new BufferedImage(size.width, size.height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) image.getGraphics();

			g.setColor(color);
			g.fillRect(0, 0, size.width, size.height);

			g.setColor(MetalLookAndFeel.getSeparatorForeground());
			g.drawRect(0, 0, size.width - 1, size.height - 1);

			return image;
		}

		return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * creates a cursor containing the given color and returns it
	 * 
	 * @param color
	 *            a color
	 * @return a cursor containing the given color
	 */
	public Cursor createCursorImageFromColor(Color color) {

		Cursor cursor = null;

		if (color != null) {

			// tells which size is better for the cursor images or if the cutom
			// cursors option can't be used
			Dimension bestSize = Toolkit.getDefaultToolkit().getBestCursorSize(22, 22);

			try {
				cursor = Toolkit.getDefaultToolkit().createCustomCursor(
						getImageFromColor(color, bestSize), new Point(0, 0), "color");
			} catch (IndexOutOfBoundsException ex) {
				ex.printStackTrace();
			}
		}

		return cursor;
	}

	/**
	 * creates a cursor given an image
	 * 
	 * @param image
	 *            an image
	 * @return a cursor containing the image
	 */
	public Cursor createCursorFromImage(Image image) {

		Cursor cursor = null;

		if (image != null) {

			try {
				cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0),
						"resource");
			} catch (IndexOutOfBoundsException ex) {
				ex.printStackTrace();
			}
		}

		return cursor;
	}

	/**
	 * returns the value of a style property
	 * 
	 * @param element
	 *            an element
	 * @param name
	 *            the name of a style property
	 * @return the value of a style property
	 */
	public String getStyleProperty(Element element, String name) {

		String value = "";

		if (element != null && name != null && !name.equals("")) {

			// gets the value of the style attribute
			String styleValue = element.getAttribute("style");
			styleValue = styleValue.replaceAll("\\s*[;]\\s*", ";");
			styleValue = styleValue.replaceAll("\\s*[:]\\s*", ":");

			int rg = styleValue.indexOf(";".concat(name.concat(":")));

			if (rg != -1) {

				rg++;
			}

			if (rg == -1) {

				rg = styleValue.indexOf(name.concat(":"));

				if (rg != 0) {

					rg = -1;
				}
			}

			// if the value of the style attribute contains the property
			if (!styleValue.equals("") && rg != -1) {

				// computes the value of the property
				value = styleValue.substring(rg + name.length() + 1, styleValue.length());
				rg = value.indexOf(";");
				value = value.substring(0, rg == -1 ? value.length() : rg);
			}
		}

		return value;
	}

	/**
	 * setting the value of the given style element for the given node
	 * 
	 * @param element
	 *            an element
	 * @param name
	 *            the name of a style element
	 * @param value
	 *            the value for this style element
	 */
	public static void setStyleProperty(Element element, String name, String value) {

		if (element != null && name != null && !name.equals("")) {

			if (value == null) {

				value = "";
			}

			// the separators
			String valuesSep = ";", nameToValueSep = ":";

			// the map associating the name of a property to its value
			HashMap<String, String> values = new HashMap<String, String>();

			// getting the value of the style attribute
			String styleValue = element.getAttribute("style");
			styleValue = styleValue.replaceAll("\\s*[;]\\s*", ";");
			styleValue = styleValue.replaceAll("\\s*[:]\\s*", ":");

			// filling the map associating a property to its value
			String[] splitValues = styleValue.split(valuesSep);
			int pos = -1;
			String sname = "", svalue = "";

			for (int i = 0; i < splitValues.length; i++) {

				if (splitValues[i] != null && !splitValues[i].equals("")) {

					pos = splitValues[i].indexOf(nameToValueSep);

					sname = splitValues[i].substring(0, pos);
					svalue = splitValues[i].substring(pos + nameToValueSep.length(),
							splitValues[i].length());

					if (!sname.equals("") && !svalue.equals("")) {

						values.put(sname, svalue);
					}
				}
			}

			// adding the new value
			if (value.equals("")) {

				values.remove(name);

			} else {

				values.put(name, value);
			}

			// computing the new style value
			styleValue = "";

			for (String newName : values.keySet()) {

				styleValue += newName + nameToValueSep + values.get(newName) + valuesSep;
			}

			// sets the value of the style attribute
			if (!styleValue.equals("")) {

				element.setAttribute("style", styleValue);

			} else {

				element.removeAttribute("style");
			}
		}
	}

	/**
	 * recursively removes all the attributes that are not necessary for the
	 * given node
	 * 
	 * @param element
	 *            a node
	 */
	public void removeUselessAttributes(Element element) {

		if (element != null) {

			String nspXLink = "http://www.w3.org/1999/xlink", nspNS = "http://www.w3.org/2000/xmlns/";
			Node node = null;
			Element el = null;

			for (NodeIterator it = new NodeIterator(element); it.hasNext();) {

				node = it.next();

				if (node != null && node instanceof Element && !node.getNodeName().equals("svg")) {

					el = (Element) node;

					el.removeAttributeNS(nspXLink, "xlink:show");
					el.removeAttributeNS(nspXLink, "xlink:type");
					el.removeAttributeNS(nspXLink, "xlink:actuate");
					el.removeAttributeNS(nspNS, "xlink");
				}
			}
		}
	}
}
