package fr.itris.glips.svgeditor.display.handle.manager;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.*;
import org.apache.batik.parser.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to retrieve information on svg elements
 * 
 * @author Jordi SUC
 */
public class SVGElementsManager {

	/**
	 * the transform attribute
	 */
	public static final String transformAttribute = "transform";

	/**
	 * the related svg handle
	 */
	private SVGHandle handle;

	/**
	 * the constructor of the class
	 * 
	 * @param handle
	 *          a svg handle
	 */
	public SVGElementsManager(SVGHandle handle) {

		this.handle = handle;
	}

	/**
	 * computes the position and the size of a node on the canvas
	 * 
	 * @param shape
	 *          the node whose position and size is to be computed
	 * @return a rectangle representing the position and size of the given node
	 */
	public Rectangle2D getSensitiveBounds(Element shape) {

		Rectangle2D bounds = null;

		if (shape != null) {

			// gets the bridge context
			BridgeContext ctxt = handle.getCanvas().getBridgeContext();

			if (ctxt != null) {

				// gets the graphics node corresponding to the given node
				GraphicsNode gnode = null;

				gnode = ctxt.getGraphicsNode(shape);
				System.out.println("gnode for shape " + shape + " is " + gnode);

				if (gnode != null) {

					AffineTransform transform = gnode.getTransform();
					bounds = gnode.getSensitiveBounds();
					// this bit debugging
					if (gnode instanceof ShapeNode) {
						ShapePainter shapePainter = ((ShapeNode) gnode).getShapePainter();
						if (shapePainter instanceof CompositeShapePainter) {
							CompositeShapePainter cShapePainter = ((CompositeShapePainter) shapePainter);
							System.out.println("Shape painter count is " + cShapePainter.getShapePainterCount());
							bounds = null;
							for (int i = 0; i < cShapePainter.getShapePainterCount(); ++i) {
								Rectangle2D pb = cShapePainter.getShapePainter(i).getSensitiveBounds2D();
								if (bounds == null) {
									bounds = (Rectangle2D) pb.clone();
									System.out.println("Bounds is now " + bounds);
								} else {
									bounds.add(pb);
									System.out.println("Added bounds " + pb + ", result is " + bounds);
								}
							}
						}
						System.out.println("Bounds for gnode " + gnode + " on pointer event type "
								+ ((ShapeNode) gnode).getPointerEventType() + " are " + bounds);
					}
					// end debugging
					if (transform != null) {
						bounds = transform.createTransformedShape(bounds).getBounds2D();
					}
				}
			}
		}

		return bounds;
	}

	/**
	 * computes the position and the size of a node on the canvas
	 * 
	 * @param shape
	 *          the node whose position and size is to be computed
	 * @return a rectangle representing the position and size of the given node
	 */
	public Rectangle2D getNodeBounds(Element shape) {

		Rectangle2D bounds = new Rectangle();

		if (shape != null) {

			// gets the bridge context
			BridgeContext ctxt = handle.getCanvas().getBridgeContext();

			if (ctxt != null) {

				// gets the graphics node corresponding to the given node
				GraphicsNode gnode = null;

				gnode = ctxt.getGraphicsNode(shape);

				if (gnode != null) {

					bounds = gnode.getGeometryBounds();

					AffineTransform affine = new AffineTransform();

					if (gnode.getTransform() != null) {

						affine.preConcatenate(gnode.getTransform());
					}

					if (handle.getCanvas().getViewingTransform() != null) {

						affine.preConcatenate(handle.getCanvas().getViewingTransform());
					}

					if (handle.getCanvas().getRenderingTransform() != null) {

						affine.preConcatenate(handle.getCanvas().getRenderingTransform());
					}

					bounds = affine.createTransformedShape(bounds).getBounds2D();
				}
			}
		}

		return bounds;
	}

	/**
	 * computes the position and the size of a node on the canvas, the only
	 * transform applied is the node transform
	 * 
	 * @param shape
	 *          the node whose position and size is to be computed
	 * @return a rectangle representing the position and size of the given node
	 */
	public Rectangle2D getNodeGeometryBounds(Element shape) {

		Rectangle2D bounds = new Rectangle2D.Double();

		if (shape != null) {

			// gets the bridge context
			BridgeContext ctxt = handle.getCanvas().getBridgeContext();

			if (ctxt != null) {

				// gets the graphics node corresponding to the given node
				GraphicsNode gnode = null;

				gnode = ctxt.getGraphicsNode(shape);

				if (gnode != null) {

					Rectangle2D bounds2D = gnode.getGeometryBounds();

					if (bounds2D != null) {

						// getting the transform of this node
						AffineTransform af = gnode.getTransform();

						if (af != null) {

							bounds2D = af.createTransformedShape(bounds2D).getBounds2D();
						}

						bounds = new Rectangle2D.Double(bounds2D.getX(), bounds2D.getY(), bounds2D.getWidth(),
								bounds2D.getHeight());
					}
				}
			}
		}

		return bounds;
	}

	/**
	 * computes the outline of a node on the canvas that has the node's transform
	 * applied
	 * 
	 * @param shapeNode
	 *          a shape node
	 * @param af
	 *          an affine transform
	 * @return the outline of the given node
	 */
	public Shape getTransformedOutline(Element shapeNode, AffineTransform af) {

		Shape shape = new Rectangle();

		if (shapeNode != null) {

			if (af == null) {

				af = new AffineTransform();
			}

			// gets the bridge context
			BridgeContext ctxt = handle.getCanvas().getBridgeContext();

			if (ctxt != null) {

				// gets the graphics node corresponding to the given node
				GraphicsNode gnode = null;

				gnode = ctxt.getGraphicsNode(shapeNode);

				if (gnode != null) {

					shape = gnode.getOutline();

					// transforming the shape
					if (shape != null) {

						AffineTransform affine = new AffineTransform();

						if (gnode.getTransform() != null) {

							affine.preConcatenate(gnode.getTransform());
						}

						affine.preConcatenate(af);

						if (handle.getCanvas().getViewingTransform() != null) {

							affine.preConcatenate(handle.getCanvas().getViewingTransform());
						}

						if (handle.getCanvas().getRenderingTransform() != null) {

							affine.preConcatenate(handle.getCanvas().getRenderingTransform());
						}

						shape = affine.createTransformedShape(shape);
					}
				}
			}
		}

		return shape;
	}

	/**
	 * parses and returns the transform that is described in the transform
	 * attribute of the element
	 * 
	 * @param shapeElement
	 *          an element
	 * @return the element's transform
	 */
	public AffineTransform parseTransform(Element shapeElement) {

		return AWTTransformProducer.createAffineTransform(shapeElement.getAttribute("transform"));
	}

	/**
	 * returns the affine transform that is applied to the given element
	 * 
	 * @param shapeElement
	 *          a shape element
	 * @return the affine transform that is applied to the given element, that is
	 *         never null
	 */
	public AffineTransform getTransform(Element shapeElement) {

		AffineTransform af = null;

		if (shapeElement != null) {

			// gets the bridge context
			BridgeContext ctxt = handle.getCanvas().getBridgeContext();

			if (ctxt != null) {

				// gets the graphics node corresponding to the given node
				GraphicsNode gnode = null;

				gnode = ctxt.getGraphicsNode(shapeElement);

				if (gnode != null) {

					af = gnode.getTransform();

				} else {

					af = parseTransform(shapeElement);
				}

				if (af != null) {

					af = new AffineTransform(af);
				}
			}
		}

		if (af == null) {

			af = new AffineTransform();
		}

		return af;
	}

	/**
	 * sets the affine transform to apply to the given element
	 * 
	 * @param shapeElement
	 *          a shape element
	 * @param af
	 *          the affine transform
	 */
	public void setTransform(Element shapeElement, AffineTransform af) {

		if (shapeElement != null) {

			if (af == null || af.isIdentity()) {

				// removing the transform attribute
				shapeElement.removeAttribute(transformAttribute);

			} else {

				StringBuffer attValue = new StringBuffer("matrix(");
				attValue.append(FormatStore.format(af.getScaleX()));
				attValue.append(" ");
				attValue.append(FormatStore.format(af.getShearY()));
				attValue.append(" ");
				attValue.append(FormatStore.format(af.getShearX()));
				attValue.append(" ");
				attValue.append(FormatStore.format(af.getScaleY()));
				attValue.append(" ");
				attValue.append(FormatStore.format(af.getTranslateX()));
				attValue.append(" ");
				attValue.append(FormatStore.format(af.getTranslateY()));
				attValue.append(")");

				shapeElement.setAttribute(transformAttribute, attValue.toString());
			}
		}
	}

	/**
	 * computes the outline of a node on the canvas
	 * 
	 * @param node
	 *          the node
	 * @return the outline of the given node
	 */
	public Shape getOutline(Node node) {

		Shape shape = new Rectangle();

		if (node != null && node instanceof Element) {

			// gets the bridge context
			BridgeContext ctxt = handle.getCanvas().getBridgeContext();

			if (ctxt != null) {

				// gets the graphics node corresponding to the given node
				GraphicsNode gnode = null;

				gnode = ctxt.getGraphicsNode((Element) node);

				if (gnode != null) {

					AffineTransform affine = new AffineTransform();

					if (gnode.getTransform() != null) {

						affine.preConcatenate(gnode.getTransform());
					}

					shape = affine.createTransformedShape(getGeometryShape(gnode));
				}
			}
		}

		return shape;
	}

	/**
	 * computes the outline of a node on the canvas
	 * 
	 * @param node
	 *          the node
	 * @return the outline of the given node
	 */
	public Shape getGeometryOutline(Node node) {

		Shape shape = new Rectangle();

		if (node != null && node instanceof Element) {

			// gets the bridge context
			BridgeContext ctxt = handle.getCanvas().getBridgeContext();

			if (ctxt != null) {

				// gets the graphics node corresponding to the given node
				GraphicsNode gnode = null;

				gnode = ctxt.getGraphicsNode((Element) node);

				if (gnode != null) {

					shape = getGeometryShape(gnode);
				}
			}
		}

		return shape;
	}

	/**
	 * returns the shape of the given graphics node
	 * 
	 * @param graphicsNode
	 *          a graphics node
	 * @return the shape of the given graphics node
	 */
	protected Shape getGeometryShape(GraphicsNode graphicsNode) {

		Shape shape = new Rectangle();

		if (graphicsNode != null) {

			shape = graphicsNode.getOutline();
		}

		return shape;
	}

	/**
	 * returns the nodes at the given point
	 * 
	 * @param parent
	 *          the parent node
	 * @param point
	 *          the point on which a mouse event has been done
	 * @return the node on which a mouse event has been done
	 */
	public Element getNodeAt(Node parent, Point2D point) {

		Element pointElement = null;

		if (parent != null && point != null) {

			// computing the zone that surrounds the provided point
			double diff = 6 / handle.getCanvas().getZoomManager().getCurrentScale();
			Rectangle2D zone = new Rectangle2D.Double(point.getX() - diff, point.getY() - diff, 2 * diff,
					2 * diff);

			// getting the graphics node of the parent node
			BridgeContext ctxt = handle.getCanvas().getBridgeContext();

			if (ctxt != null) {

				GraphicsNode gparentNode = null;

				gparentNode = ctxt.getGraphicsNode((Element) parent);

				if (gparentNode != null) {

					// setting the selection mode for the parent and its children
					gparentNode.setPointerEventType(GraphicsNode.VISIBLE);

					// getting the graphics node that intersects the zone
					NodeList childNodes = parent.getChildNodes();
					Node node = null;
					Element element = null;
					GraphicsNode gnode = null;

					for (int i = childNodes.getLength() - 1; i >= 0; i--) {

						node = childNodes.item(i);

						if (node != null && node instanceof Element) {

							element = (Element) node;

							if (EditorToolkit.isElementAShape(element)) {

								// getting the graphics node corresponding to this element
								gnode = ctxt.getGraphicsNode(element);

								if (intersects(gnode, zone)) {

									pointElement = element;
									break;
								}
							}
						}
					}
				}
			}
		}

		return pointElement;
	}

	/**
	 * returns whether the graphics node sensitive bounds intersects the provided
	 * zone
	 * 
	 * @param gnode
	 *          a gnode
	 * @param zone
	 *          the zone
	 * @return whether the graphics node sensitive bounds intersects the provided
	 *         zone
	 */
	protected boolean intersects(GraphicsNode gnode, Rectangle2D zone) {

		boolean intersects = false;

		if (gnode != null) {

			// getting the sensitive bounds
			Rectangle2D sensitiveBounds = gnode.getSensitiveBounds();

			if (gnode.getTransform() != null) {

				sensitiveBounds = gnode.getTransform().createTransformedShape(sensitiveBounds)
						.getBounds2D();
			}

			if (sensitiveBounds == null) {

				sensitiveBounds = gnode.getGeometryBounds();

				if (gnode.getTransform() != null) {

					sensitiveBounds = gnode.getTransform().createTransformedShape(sensitiveBounds)
							.getBounds2D();
				}
			}

			if (sensitiveBounds != null && sensitiveBounds.intersects(zone)) {

				// checking if the outline of the node intersects the zone
				if (gnode instanceof ShapeNode) {

					Shape shape = ((ShapeNode) gnode).getSensitiveArea();

					if (gnode.getTransform() != null) {

						shape = gnode.getTransform().createTransformedShape(getGeometryShape(gnode));
					}

					intersects = shape.intersects(zone);

				} else if (gnode instanceof CompositeGraphicsNode && !(gnode instanceof RasterImageNode)
						&& !(gnode instanceof ImageNode)) {

					GraphicsNode childGNode = null;

					// checking if one of the children of the composite node intersects
					// the zone
					for (Object object : ((CompositeGraphicsNode) gnode).getChildren()) {

						if (object != null && object instanceof GraphicsNode) {

							childGNode = (GraphicsNode) object;

							if (intersects(childGNode, zone)) {

								intersects = true;
								break;
							}
						}
					}

				} else {

					Shape shape = gnode.getOutline();

					if (gnode.getTransform() != null) {

						shape = gnode.getTransform().createTransformedShape(shape);
					}

					intersects = shape.intersects(zone);
				}
			}
		}

		return intersects;
	}

	/**
	 * find the accurate id for a node, that does not exist for the nodes in the
	 * handle document, or in the provided elements set
	 * 
	 * @param baseString
	 *          the base of the id
	 * @param excludedNodes
	 *          the set of the elements that should be taken into account for
	 *          checking the uniqueness of the id
	 * @return the unique id
	 */
	public String getId(String baseString, Set<Element> excludedNodes) {

		Document doc = handle.getCanvas().getDocument();

		if (doc != null) {

			LinkedList<String> ids = new LinkedList<String>();
			Node current = null;
			Element el = null;
			String attId = "";

			// adding to the list all the ids found among the children of the root
			// element
			for (NodeIterator it = new NodeIterator(doc.getDocumentElement()); it.hasNext();) {

				current = it.next();

				if (current != null && current instanceof Element) {

					el = (Element) current;
					attId = el.getAttribute("id");

					if (attId != null && !attId.equals("")) {

						ids.add(attId);
					}
				}
			}

			if (excludedNodes != null) {

				// adding to the list all the ids found in the provided elements set
				for (Element element : excludedNodes) {

					attId = element.getAttribute("id");

					if (attId != null && !attId.equals("")) {

						ids.add(attId);
					}
				}
			}

			int i = 0;

			// tests for each integer string if the newly computed id already exists
			while (ids.contains(baseString.concat(i + ""))) {

				i++;
			}

			return new String(baseString.concat(i + ""));
		}

		return "";
	}

	/**
	 * checks whether the given id already exists or not among the children of the
	 * given root node
	 * 
	 * @param id
	 *          an id to be checked
	 * @return true if the id does not already exists
	 */
	public boolean checkId(String id) {

		Document doc = handle.getCanvas().getDocument();

		if (doc != null) {

			LinkedList<String> ids = new LinkedList<String>();
			Node current = null;
			String attId = "";

			// adds to the list all the ids found among the children of the root
			// element
			for (NodeIterator it = new NodeIterator(doc.getDocumentElement()); it.hasNext();) {

				current = it.next();

				if (current != null && current instanceof Element) {

					attId = ((Element) current).getAttribute("id");

					if (attId != null && !attId.equals("")) {

						ids.add(attId);
					}
				}
			}

			// tests for each integer string if it is already an id
			if (ids.contains(id)) {

				return false;
			}

			return true;
		}

		return false;
	}

	/**
	 * @return the list of the ids of the shape nodes that are contained in the
	 *         given svg document
	 */
	public LinkedList<String> getShapeNodesIds() {

		LinkedList<String> idNodes = new LinkedList<String>();
		Document doc = handle.getCanvas().getDocument();

		if (doc != null && doc.getDocumentElement() != null) {

			Node cur = null;
			String id = "";
			Element el = null;

			// for each children of the root element (but the "defs" element), adds
			// its id to the map
			for (NodeIterator it = new NodeIterator(doc.getDocumentElement()); it.hasNext();) {

				cur = it.next();

				if (cur instanceof Element) {

					el = (Element) cur;

					if (EditorToolkit.isElementAShape(el)) {

						id = el.getAttribute("id");

						if (id != null && !id.equals("")) {

							idNodes.add(id);
						}
					}
				}
			}
		}

		return idNodes;
	}
}
