/*
 * Created on 10 déc. 2004
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

import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.metal.*;
import org.apache.batik.dom.svg.*;
import org.apache.batik.swing.*;
import org.apache.batik.swing.gvt.*;
import org.apache.batik.swing.svg.*;
import org.w3c.dom.*;
import org.w3c.dom.svg.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to create an outline of a resource
 * 
 * @author ITRIS, Jordi SUC
 */
public class ResourceImageManager {

	/**
	 * used to convert numbers into a string
	 */
	private DecimalFormat format;

	/**
	 * the map associating a svg handle to a map associating the id of a
	 * resource to the imageRepresentation object representing this resource
	 */
	private final Map<SVGHandle, Map<String, ImageRepresentation>> handleToIdToImages = Collections
			.synchronizedMap(new HashMap<SVGHandle, Map<String, ImageRepresentation>>());

	/**
	 * the size of each image, and the size of the small images
	 */
	private final Dimension imageSize = new Dimension(20, 20), smallImageSize = new Dimension(16,
			16);

	/**
	 * the list containing runnables to execute, create, or update
	 */
	private java.util.List<Runnable> queue = Collections
			.synchronizedList(new LinkedList<Runnable>());

	/**
	 * the thread handling the queue
	 */
	private Thread queueManager = null;

	/**
	 * the list of the resource representations that have been added
	 */
	private java.util.List<ResourceRepresentation> resourceRepresentationList = Collections
			.synchronizedList(new LinkedList<ResourceRepresentation>());

	/**
	 * the editor
	 */
	private Editor editor = null;

	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public ResourceImageManager(Editor editor) {

		this.editor = editor;

		// the listener to the svg handle changes
		editor.getHandlesManager().addHandlesListener(new HandlesListener() {

			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {

				// removes the frames that have been closed from the map
				for (SVGHandle handle : new LinkedList<SVGHandle>(handleToIdToImages.keySet())) {

					if (handle != null && !handles.contains(handle)) {

						handleToIdToImages.remove(handle);
					}
				}

				// removes the image representations that belongs to disposed
				// svg handles
				for (ResourceRepresentation rep : new HashSet<ResourceRepresentation>(
						resourceRepresentationList)) {

					if (rep != null && !handles.contains(rep.getSVGHandle())) {

						rep.dispose();
						resourceRepresentationList.remove(rep);
					}
				}
			}
		});

		// sets the format used to convert numbers into a string
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("######.#", symbols);

		// the queue manager
		queueManager = new Thread() {

			@Override
			public void run() {

				Runnable runnable = null;

				while (true) {

					while (queue.size() > 0) {

						// getting the runnable
						runnable = queue.get(0);

						// removing it from the queue
						queue.remove(runnable);

						// running the runnable
						runnable.run();
						runnable = null;
					}

					try {
						sleep(100);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}
			}
		};

		queueManager.start();
	}

	/**
	 * returns a representation of the resource given by its id
	 * 
	 * @param svgHandle
	 *            a svg handle
	 * @param resourceId
	 *            the id of a resource node
	 * @param useSmallImage
	 *            whether the returned representation should be small or large
	 * @param componentToRefresh
	 *            the component to refresh
	 * @return a resource representation
	 */
	public ResourceRepresentation getResourceRepresentation(SVGHandle svgHandle, String resourceId,
			boolean useSmallImage, Component componentToRefresh) {

		final SVGHandle fhandle = svgHandle;
		final String fresourceId = resourceId;

		if (svgHandle != null && resourceId != null) {

			// creating the runnable
			Runnable runnable = new Runnable() {

				public void run() {

					ImageRepresentation imageRepresentation = getResourceImage(fhandle, fresourceId);

					if (imageRepresentation == null) {

						createNewImage(fhandle, fresourceId);
					}
				}
			};

			// enqueueing the runnable
			invokeLater(runnable);
		}

		return getRepresentation(svgHandle, resourceId, useSmallImage, componentToRefresh);
	}

	/**
	 * enqueues the given runnable
	 * 
	 * @param runnable
	 *            a runnable
	 */
	protected void invokeLater(Runnable runnable) {

		if (runnable != null) {

			queue.add(runnable);
		}
	}

	/**
	 * @return Returns the editor.
	 */
	protected Editor getSVGEditor() {
		return editor;
	}

	/**
	 * invalidates the representation of a resource
	 * 
	 * @param svgHandle
	 *            a svg handle
	 * @param resourceId
	 *            the id of a resource node
	 */
	public void invalidateResourceRepresentation(SVGHandle svgHandle, String resourceId) {

		if (resourceId != null && svgHandle != null) {

			Map<String, ImageRepresentation> idToImageMap = null;

			if (handleToIdToImages.containsKey(svgHandle)) {

				idToImageMap = handleToIdToImages.get(svgHandle);

				if (idToImageMap != null) {

					// removes the id in the map
					synchronized (this) {
						idToImageMap.remove(resourceId);
					}
				}
			}
		}
	}

	/**
	 * checks the consistency of the stored images
	 * 
	 * @param svgHandle
	 *            a svg handle
	 */
	public void checkConsistency(SVGHandle svgHandle) {

		if (svgHandle != null) {

			final SVGHandle fsvgHandle = svgHandle;

			Runnable runnable = new Runnable() {

				public void run() {

					checkResourceRepresentationsConsistency(fsvgHandle);
				}
			};

			// enqueueing the runnable
			invokeLater(runnable);
		}
	}

	/**
	 * checks if the map associating an id to an image is consistent
	 * 
	 * @param svgHandle
	 *            a svg handle
	 */
	protected void checkResourceRepresentationsConsistency(SVGHandle svgHandle) {

		if (svgHandle != null) {

			Map<String, ImageRepresentation> idToImageMap = null;

			if (handleToIdToImages.containsKey(svgHandle)) {

				idToImageMap = handleToIdToImages.get(svgHandle);

				if (idToImageMap != null) {

					// getting the map associating the id of a resource to the
					// resource node
					LinkedList<String> resourceNames = new LinkedList<String>();
					resourceNames.add("linearGradient");
					resourceNames.add("radialGradient");
					resourceNames.add("pattern");
					resourceNames.add("marker");

					Map<String, Element> resources = svgHandle.getSvgResourcesManager()
							.getResourcesFromDefs(svgHandle.getCanvas().getDocument(),
									resourceNames);

					for (String id : new LinkedList<String>(idToImageMap.keySet())) {

						if (id != null && !id.equals("") && !resources.containsKey(id)) {

							idToImageMap.remove(id);
						}
					}
				}
			}
		}
	}

	/**
	 * creates a new image
	 * 
	 * @param svgHandle
	 *            a svg handle
	 * @param resourceId
	 *            the id of the resource from which the image will be created
	 */
	protected void createNewImage(SVGHandle svgHandle, String resourceId) {

		if (svgHandle != null && resourceId != null && !resourceId.equals("")) {

			Element resourceElement = null;

			// try{
			resourceElement = svgHandle.getScrollPane().getSVGCanvas().getDocument()
					.getElementById(resourceId);
			// }catch (Exception ex) {}

			final String fresourceId = resourceId;

			if (resourceElement != null) {

				final SVGHandle fhandle = svgHandle;

				// creating the canvas and setting its properties
				final JSVGCanvas canvas = new JSVGCanvas() {

					@Override
					public void dispose() {

						removeKeyListener(listener);
						removeMouseMotionListener(listener);
						removeMouseListener(listener);
						disableInteractions = true;
						selectableText = false;
						userAgent = null;

						bridgeContext.dispose();
						super.dispose();
					}
				};

				// the element to be added
				Element elementToAdd = null;

				canvas.setDocumentState(JSVGComponent.ALWAYS_STATIC);
				canvas.setDisableInteractions(true);

				// creating the new document
				final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
				final SVGDocument doc = (SVGDocument) resourceElement.getOwnerDocument().cloneNode(
						false);

				// creating the root element
				final Element root = (Element) doc.importNode(resourceElement.getOwnerDocument()
						.getDocumentElement(), false);
				doc.appendChild(root);

				// removing all the attributes of the root element
				NamedNodeMap attributes = doc.getDocumentElement().getAttributes();

				for (int i = 0; i < attributes.getLength(); i++) {

					if (attributes.item(i) != null) {

						doc.getDocumentElement().removeAttribute(attributes.item(i).getNodeName());
					}
				}

				// adding the new attributes for the root
				root.setAttributeNS(null, "width", imageSize.width + "");
				root.setAttributeNS(null, "height", imageSize.height + "");
				root.setAttributeNS(null, "viewBox", "0 0 " + imageSize.width + " "
						+ imageSize.height);

				// the defs element that will contain the cloned resource node
				final Element defs = (Element) doc
						.importNode(resourceElement.getParentNode(), true);
				root.appendChild(defs);

				if (resourceElement.getNodeName().equals("linearGradient")
						|| resourceElement.getNodeName().equals("radialGradient")
						|| resourceElement.getNodeName().equals("pattern")) {

					// the rectangle that will be drawn
					final Element rect = doc.createElementNS(svgNS, "rect");
					rect.setAttributeNS(null, "x", "0");
					rect.setAttributeNS(null, "y", "0");
					rect.setAttributeNS(null, "width", imageSize.width + "");
					rect.setAttributeNS(null, "height", imageSize.height + "");

					elementToAdd = rect;

					// setting that the rectangle uses the resource
					String id = resourceElement.getAttribute("id");

					if (id == null) {

						id = "";
					}

					rect.setAttributeNS(null, "style", "fill:url(#" + id + ");");

					// getting the cloned resource node
					Node cur = null;
					Element clonedResourceElement = null;
					String id2 = "";

					for (cur = defs.getFirstChild(); cur != null; cur = cur.getNextSibling()) {

						if (cur instanceof Element) {

							id2 = ((Element) cur).getAttribute("id");

							if (id2 != null && id.equals(id2)) {

								clonedResourceElement = (Element) cur;
							}
						}
					}

					if (clonedResourceElement != null) {

						// getting the root element of the initial resource
						// element
						Element initialRoot = resourceElement.getOwnerDocument()
								.getDocumentElement();

						// getting the width and height of the initial root
						// element
						double initialWidth = 0, initialHeight = 0;

						try {
							initialWidth = EditorToolkit.getPixelledNumber(initialRoot
									.getAttributeNS(null, "width"));
							initialHeight = EditorToolkit.getPixelledNumber(initialRoot
									.getAttributeNS(null, "height"));
						} catch (DOMException ex) {
							ex.printStackTrace();
						}

						if (resourceElement.getNodeName().equals("linearGradient")) {

							if (resourceElement.getAttributeNS(null, "gradientUnits").equals(
									"userSpaceOnUse")) {

								double x1 = 0, y1 = 0, x2 = 0, y2 = 0;

								// normalizing the values for the vector to fit
								// the rectangle
								try {
									x1 = Double.parseDouble(resourceElement.getAttributeNS(null,
											"x1"));
									y1 = Double.parseDouble(resourceElement.getAttributeNS(null,
											"y1"));
									x2 = Double.parseDouble(resourceElement.getAttributeNS(null,
											"x2"));
									y2 = Double.parseDouble(resourceElement.getAttributeNS(null,
											"y2"));

									x1 = x1 / initialWidth * imageSize.width;
									y1 = y1 / initialHeight * imageSize.height;
									x2 = x2 / initialWidth * imageSize.width;
									y2 = y2 / initialHeight * imageSize.height;
								} catch (NumberFormatException | DOMException ex) {
									ex.printStackTrace();
								}

								clonedResourceElement.setAttributeNS(null, "x1", format.format(x1));
								clonedResourceElement.setAttributeNS(null, "y1", format.format(y1));
								clonedResourceElement.setAttributeNS(null, "x2", format.format(x2));
								clonedResourceElement.setAttributeNS(null, "y2", format.format(y2));
							}

						} else if (resourceElement.getNodeName().equals("radialGradient")) {

							if (resourceElement.getAttributeNS(null, "gradientUnits").equals(
									"userSpaceOnUse")) {

								double cx = 0, cy = 0, r = 0, fx = 0, fy = 0;

								// normalizing the values for the circle to fit
								// the rectangle
								try {
									cx = Double.parseDouble(resourceElement.getAttributeNS(null,
											"cx"));
									cy = Double.parseDouble(resourceElement.getAttributeNS(null,
											"cy"));
									r = Double.parseDouble(resourceElement
											.getAttributeNS(null, "r"));
									fx = Double.parseDouble(resourceElement.getAttributeNS(null,
											"fx"));
									fy = Double.parseDouble(resourceElement.getAttributeNS(null,
											"fy"));

									cx = cx / initialWidth * imageSize.width;
									cy = cy / initialHeight * imageSize.height;

									r = r
											/ (Math.abs(Math.sqrt(Math.pow(initialWidth, 2)
													+ Math.pow(initialHeight, 2))))
											* Math.abs(Math.sqrt(Math.pow(imageSize.width, 2)
													+ Math.pow(imageSize.width, 2)));

									fx = fx / initialWidth * imageSize.width;
									fy = fy / initialHeight * imageSize.height;
								} catch (NumberFormatException | DOMException ex) {
									ex.printStackTrace();
								}

								clonedResourceElement.setAttributeNS(null, "cx", format.format(cx));
								clonedResourceElement.setAttributeNS(null, "cy", format.format(cy));
								clonedResourceElement.setAttributeNS(null, "r", format.format(r));
								clonedResourceElement.setAttributeNS(null, "fx", format.format(fx));
								clonedResourceElement.setAttributeNS(null, "fy", format.format(fy));
							}

						} else if (resourceElement.getNodeName().equals("pattern")) {

							if (resourceElement.getAttributeNS(null, "patternUnits").equals(
									"userSpaceOnUse")) {

								double x = 0, y = 0, w = 0, h = 0;

								// normalizing the values for the vector to fit
								// the rectangle
								try {
									x = Double.parseDouble(resourceElement
											.getAttributeNS(null, "x"));
									y = Double.parseDouble(resourceElement
											.getAttributeNS(null, "y"));
									w = Double.parseDouble(resourceElement
											.getAttributeNS(null, "w"));
									h = Double.parseDouble(resourceElement
											.getAttributeNS(null, "h"));

									x = x / initialWidth * imageSize.width;
									y = y / initialHeight * imageSize.height;
									w = w / initialWidth * imageSize.width;
									h = h / initialHeight * imageSize.height;
								} catch (NumberFormatException | DOMException ex) {
									ex.printStackTrace();
								}

								clonedResourceElement.setAttributeNS(null, "x", format.format(x));
								clonedResourceElement.setAttributeNS(null, "y", format.format(y));
								clonedResourceElement.setAttributeNS(null, "width",
										format.format(w));
								clonedResourceElement.setAttributeNS(null, "height",
										format.format(h));
							}
						}
					}

				} else if (resourceElement.getNodeName().equals("marker")) {

					// the line that will be drawn
					final Element line = doc.createElementNS(svgNS, "line");
					line.setAttributeNS(null, "x1", (((double) imageSize.width) / 2) + "");
					line.setAttributeNS(null, "y1", (((double) imageSize.height) / 2) + "");
					line.setAttributeNS(null, "x2", ((double) imageSize.width / 2) + "");
					line.setAttributeNS(null, "y2", imageSize.height + "");

					elementToAdd = line;

					// setting that the line uses the resource
					String id = resourceElement.getAttribute("id");
					if (id == null)
						id = "";
					line.setAttributeNS(null, "style", "stroke:none;fill:none;marker-start:url(#"
							+ id + ");");
				}

				root.appendChild(elementToAdd);

				// adding a rendering listener to the canvas
				GVTTreeRendererAdapter gVTTreeRendererAdapter = new GVTTreeRendererAdapter() {

					@Override
					public void gvtRenderingCompleted(GVTTreeRendererEvent evt) {

						Image bufferedImage = canvas.getOffScreen();

						if (bufferedImage != null) {

							Graphics g = bufferedImage.getGraphics();
							Color borderColor = MetalLookAndFeel.getSeparatorForeground();

							g.setColor(borderColor);
							g.drawRect(0, 0, imageSize.width - 1, imageSize.height - 1);
						}

						setImage(fhandle, fresourceId, bufferedImage);

						// refreshing the panels that have been created when no
						// image was available for them
						Image image = null;

						for (ResourceRepresentation resourceRepresentation : new LinkedList<ResourceRepresentation>(
								resourceRepresentationList)) {

							if (resourceRepresentation != null) {

								resourceRepresentation.refreshRepresentation();
								image = resourceRepresentation.getImage();

								if (image != null) {

									resourceRepresentationList.remove(resourceRepresentation);
								}
							}
						}

						canvas.removeGVTTreeRendererListener(this);
						canvas.stopProcessing();
						canvas.dispose();
					}
				};

				canvas.addGVTTreeRendererListener(gVTTreeRendererAdapter);

				// setting the document for the canvas
				canvas.setSVGDocument(doc);

				canvas.setBackground(Color.white);
				canvas.setBounds(1, 1, imageSize.width, imageSize.height);
			}
		}
	}

	/**
	 * Returns the representation of the resource whose id is given
	 * 
	 * @param svgHandle
	 *            a svg handle
	 * @param resourceId
	 *            the id of the resource from which the image has been created
	 * @param useSmallImage
	 *            whether the representation should display a small or a large
	 *            image
	 * @param componentToRefresh
	 *            the component to refresh
	 * @return the representation of the resource whose id is given
	 */
	protected ResourceRepresentation getRepresentation(SVGHandle svgHandle, String resourceId,
			boolean useSmallImage, Component componentToRefresh) {

		ResourceRepresentation rep = null;

		if (svgHandle != null && resourceId != null && !resourceId.equals("")) {

			rep = new ResourceRepresentation(svgHandle, resourceId, useSmallImage,
					componentToRefresh);
		}

		return rep;
	}

	/**
	 * creates a new image
	 * 
	 * @param svgHandle
	 *            a svg handle
	 * @param resourceId
	 *            the id of the resource from which the image has been created
	 * @param image
	 *            the image representing the resource
	 */
	protected void setImage(SVGHandle svgHandle, String resourceId, Image image) {

		if (svgHandle != null && resourceId != null && image != null) {

			Map<String, ImageRepresentation> idToImageMap = null;

			if (handleToIdToImages.containsKey(svgHandle)) {

				idToImageMap = handleToIdToImages.get(svgHandle);

			} else {

				idToImageMap = new HashMap<String, ImageRepresentation>();
				handleToIdToImages.put(svgHandle, idToImageMap);
			}

			synchronized (this) {
				idToImageMap.put(resourceId, new ImageRepresentation(image));
			}
		}
	}

	/**
	 * getting the image representing a resource given the id of a resource
	 * 
	 * @param svgHandle
	 *            a svg handle
	 * @param resourceId
	 *            the id of the resource from which the image has been created
	 * @return the image representation object representing the resource
	 */
	public ImageRepresentation getResourceImage(SVGHandle svgHandle, String resourceId) {

		ImageRepresentation imageRepresentation = null;

		if (svgHandle != null && resourceId != null && !resourceId.equals("")) {

			Map<String, ImageRepresentation> idToImageMap = null;

			if (handleToIdToImages.containsKey(svgHandle)) {

				idToImageMap = handleToIdToImages.get(svgHandle);

				if (idToImageMap.containsKey(resourceId)) {

					imageRepresentation = idToImageMap.get(resourceId);
				}
			}
		}

		return imageRepresentation;
	}

	/**
	 * getting the image representing a resource given the id of a resource
	 * 
	 * @param svgHandle
	 *            a svg handle
	 * @param resourceId
	 *            the id of the resource from which the image has been created
	 * @param useSmallImage
	 *            whether the returned image should be small or large
	 * @return the image representing the resource
	 */
	public Image getImage(SVGHandle svgHandle, String resourceId, boolean useSmallImage) {

		Image image = null;

		if (svgHandle != null && resourceId != null && !resourceId.equals("")) {

			Map<String, ImageRepresentation> idToImageMap = null;

			if (handleToIdToImages.containsKey(svgHandle)) {

				idToImageMap = handleToIdToImages.get(svgHandle);

				if (idToImageMap.containsKey(resourceId)) {

					ImageRepresentation imageRepresentation = idToImageMap.get(resourceId);

					if (imageRepresentation != null) {

						if (useSmallImage) {

							image = imageRepresentation.getSmallImage();

						} else {

							image = imageRepresentation.getLargeImage();
						}
					}
				}
			}
		}

		return image;
	}

	/**
	 * the class of the panel displaying a representation of a resource
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	public class ResourceRepresentation extends JPanel {

		/**
		 * a svg handle
		 */
		private SVGHandle svgHandle;

		/**
		 * the id of a resource
		 */
		private String resourceId = "";

		/**
		 * the image of the representation of the resource
		 */
		public Image resourceImage = null;

		/**
		 * whether this resource representation should display a large or a
		 * small image
		 */
		private boolean useSmallImage = false;

		/**
		 * the component to refresh
		 */
		private Component componentToRefresh;

		/**
		 * the constructor of the class
		 * 
		 * @param svgHandle
		 *            a svg handle
		 * @param resourceId
		 *            the id of a resource
		 * @param useSmallImage
		 *            whether this resource representation should display a
		 *            large or a small image
		 * @param componentToRefresh
		 *            the component to refresh
		 */
		protected ResourceRepresentation(SVGHandle svgHandle, String resourceId,
				boolean useSmallImage, Component componentToRefresh) {

			this.svgHandle = svgHandle;
			this.resourceId = resourceId;
			this.useSmallImage = useSmallImage;
			this.componentToRefresh = componentToRefresh;

			setBackground(Color.white);
			setLayout(null);

			if (useSmallImage) {

				setPreferredSize(new Dimension(smallImageSize.width + 2, smallImageSize.height + 2));

			} else {

				setPreferredSize(new Dimension(imageSize.width + 2, imageSize.height + 2));
			}

			ImageRepresentation imageRepresentation = getResourceImage(svgHandle, resourceId);
			Image image = null;

			if (imageRepresentation != null) {

				if (useSmallImage) {

					image = new ImageIcon(imageRepresentation.getSmallImage()).getImage();

				} else {

					image = new ImageIcon(imageRepresentation.getLargeImage()).getImage();
				}
			}

			if (image == null) {

				resourceRepresentationList.add(this);

			} else {

				synchronized (this) {
					resourceImage = image;
				}
			}
		}

		/**
		 * refreshes the representation
		 */
		protected void refreshRepresentation() {// TODO

			ImageRepresentation imageRepresentation = getResourceImage(svgHandle, resourceId);

			if (imageRepresentation != null) {

				if (useSmallImage && imageRepresentation.getSmallImage() != null) {

					synchronized (this) {
						resourceImage = new ImageIcon(imageRepresentation.getSmallImage())
								.getImage();
					}

				} else if (imageRepresentation.getLargeImage() != null) {

					synchronized (this) {
						resourceImage = new ImageIcon(imageRepresentation.getLargeImage())
								.getImage();
					}
				}

				if (resourceImage != null) {

					refreshParents();
				}
			}
		}

		/**
		 * @return Returns the resourceImage.
		 */
		public Image getImage() {
			return resourceImage;
		}

		/**
		 * disposes this image representation
		 */
		public void dispose() {

			svgHandle = null;
			resourceImage = null;
		}

		/**
		 * @return the svg handle
		 */
		public SVGHandle getSVGHandle() {
			return svgHandle;
		}

		/**
		 * refreshes the parents
		 */
		protected void refreshParents() {

			if (componentToRefresh != null) {

				componentToRefresh.repaint();
			}
		}

		@Override
		protected void paintComponent(Graphics g) {

			super.paintComponent(g);

			if (resourceImage != null) {

				g.drawImage(resourceImage, 1, 1, this);
			}
		}
	}

	/**
	 * the class containing the images representing
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	protected class ImageRepresentation {

		/**
		 * the large image
		 */
		private Image largeImage = null;

		/**
		 * the small image
		 */
		private Image smallImage = null;

		/**
		 * the constructor of the class
		 * 
		 * @param image
		 *            an image
		 */
		protected ImageRepresentation(Image image) {

			this.largeImage = image;
			this.smallImage = image.getScaledInstance(smallImageSize.width, smallImageSize.height,
					Image.SCALE_SMOOTH);
		}

		/**
		 * @return Returns the largeImage.
		 */
		protected Image getLargeImage() {
			return largeImage;
		}

		/**
		 * @return Returns the smallImage.
		 */
		protected Image getSmallImage() {
			return smallImage;
		}
	}
}
