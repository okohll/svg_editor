package fr.itris.glips.library.geom;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import com.gtwm.util.GlipsException;

import fr.itris.glips.library.*;

/**
 * the superclass of all the polyshapes
 * 
 * @author ITRIS, Jordi SUC
 */
public abstract class PolyShape2D implements Shape {

	/**
	 * the array of the points
	 */
	protected LinkedList<Point2D> points = new LinkedList<Point2D>();

	/**
	 * the path that will represent the polyshape
	 */
	protected GeneralPath path = new GeneralPath();

	/**
	 * the constructor of the class
	 */
	public PolyShape2D() {
	}

	/**
	 * the constructor of the class
	 * 
	 * @param coordinates
	 *            the array of the coordinates of the points defining the
	 *            polyshape, the array size should be even
	 * @throws Exception
	 *             an exception raised if the array is null or empty and the
	 *             array size is not even
	 */
	public PolyShape2D(double[] coordinates) throws GlipsException {

		if (coordinates != null && (coordinates.length % 2) == 0 && coordinates.length > 0) {

			// initializing the polyshape
			createPointsArray(coordinates);
			fillPath();

		} else {

			throw new GlipsException(
					"the array should not be null or empty and the array size should be even");
		}
	}

	/**
	 * the constructor of the class
	 * 
	 * @param thePoints
	 *            the array of the points defining the polyshape, the array size
	 *            should be even
	 * @throws Exception
	 *             an exception raised if the array is null or empty and the
	 *             array size is not even
	 */
	public PolyShape2D(Point2D[] thePoints) throws GlipsException {

		if (thePoints != null && thePoints.length > 0) {

			// initializing the polyshape
			for (int i = 0; i < thePoints.length; i++) {

				points.add(thePoints[i]);
			}

			fillPath();

		} else {

			throw new GlipsException("the array should not be null or empty");
		}
	}

	/**
	 * the constructor of the class
	 * 
	 * @param thePoints
	 *            the array of the points defining the polyshape, the array size
	 *            should be even
	 * @throws Exception
	 *             an exception raised if the array is null or empty and the
	 *             array size is not even
	 */
	public PolyShape2D(Point[] thePoints) throws GlipsException {

		if (thePoints != null && thePoints.length > 0) {

			// initializing the polyshape
			for (int i = 0; i < thePoints.length; i++) {

				points.add(thePoints[i]);
			}

			fillPath();

		} else {

			throw new GlipsException("the array should not be null or empty");
		}
	}

	/**
	 * the constructor of the class
	 * 
	 * @param shape
	 *            the polyshape to clone
	 */
	public PolyShape2D(PolyShape2D shape) {

		points.addAll(shape.getPoints());
		fillPath();
	}

	/**
	 * create the array of the points defining the polyshape according to the
	 * given array of point coordinates
	 * 
	 * @param coordinates
	 *            an array of coordinates
	 */
	protected void createPointsArray(double[] coordinates) {

		// filling the points array
		for (int i = 0; i < coordinates.length; i += 2) {

			points.add(new Point2D.Double(coordinates[i], coordinates[i + 1]));
		}
	}

	/**
	 * fills the path with the points polyshape
	 */
	protected void fillPath() {

		int i = 0;

		for (Point2D point : points) {

			if (i == 0) {

				path.reset();
				path.moveTo((float) point.getX(), (float) point.getY());

			} else {

				path.lineTo((float) point.getX(), (float) point.getY());
			}

			i++;
		}
	}

	/**
	 * @return points the list of point of the polyshape
	 */
	public LinkedList<Point2D> getPoints() {
		return points;
	}

	/**
	 * sets a new point
	 * 
	 * @param index
	 * @param newPoint
	 */
	public void setPoint(int index, Point2D newPoint) {

		if (index >= 0 && index < points.size() && newPoint != null) {

			points.set(index, newPoint);
			refresh();
		}
	}

	/**
	 * adds a new point at the end of the path
	 * 
	 * @param point
	 *            a point
	 */
	public void addPoint(Point2D point) {

		points.add(point);
		refresh();
	}

	/**
	 * removes the last point
	 */
	public void removeLast() {

		points.removeLast();
		refresh();
	}

	/**
	 * refreshes the polyshape
	 */
	public void refresh() {

		// refreshing the path
		path.reset();
		fillPath();
	}

	/**
	 * resets the polyshape
	 */
	public void reset() {

		points.clear();
		path.reset();
	}

	/**
	 * applies an affine transform to the polyshape
	 * 
	 * @param transform
	 *            an affine transform
	 */
	public void applyTransform(AffineTransform transform) {

		// transforming each point
		Point2D point;

		for (int i = 0; i < points.size(); i++) {

			point = transform.transform(points.get(i), new Point2D.Double());

			if (point != null) {

				points.set(i, point);
			}
		}

		// refreshing
		refresh();
	}

	/**
	 * @return a cloned instance of the shape
	 */
	public abstract PolyShape2D cloneShape();

	@Override
	public String toString() {

		String sval = "";

		if (points != null) {

			for (Point2D point : points) {

				sval += FormatStore.format(point.getX()) + "," + FormatStore.format(point.getY())
						+ " ";
			}
		}

		return sval;
	}

	/**
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(Point2D point) {

		return path.contains(point);
	}

	/**
	 * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
	 */
	public boolean contains(Rectangle2D rect) {

		return path.contains(rect);
	}

	/**
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {

		return path.contains(x, y);
	}

	/**
	 * @see java.awt.Shape#contains(double, double, double, double)
	 */
	public boolean contains(double x, double y, double w, double h) {

		return path.contains(x, y, w, h);
	}

	/**
	 * @see java.awt.Shape#getBounds()
	 */
	public Rectangle getBounds() {

		return path.getBounds();
	}

	/**
	 * @see java.awt.Shape#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {

		return path.getBounds2D();
	}

	/**
	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform)
	 */
	public PathIterator getPathIterator(AffineTransform af) {

		return path.getPathIterator(af);
	}

	/**
	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform,
	 *      double)
	 */
	public PathIterator getPathIterator(AffineTransform af, double flatness) {

		return path.getPathIterator(af, flatness);
	}

	/**
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D rect) {

		return path.intersects(rect);
	}

	/**
	 * @see java.awt.Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {

		return path.intersects(x, y, w, h);
	}

	/**
	 * @return the path used to draw the shape
	 */
	public GeneralPath getPath() {
		return path;
	}

	/**
	 * computes and returns the array of points coordinates corresponding to the
	 * provided string
	 * 
	 * @param value
	 *            a string value
	 * @return the array of points coordinates corresponding to the provided
	 *         string
	 */
	public static double[] getCoordinates(String value) {

		// normalizing the string
		value = new String(value);
		value = value.replaceAll("\\s+", ",");
		value = value.replaceAll("\\s+", ",");
		value = value.replaceAll(",+", ",");

		// splitting the string value
		String[] splitValue = value.split(",");

		// filling the list of the Double objects
		LinkedList<Double> values = new LinkedList<Double>();
		Double dVal;

		for (String val : splitValue) {

			if (val != null && !val.equals("")) {

				try {
					dVal = Double.parseDouble(val);
				} catch (NumberFormatException ex) {
					System.err.println("Error extracting coordinated from " + value
							+ ". Caught on " + val + ": " + ex);
					ex.printStackTrace();
					dVal = null;
				}

				if (dVal != null) {
					values.add(dVal);
				}
			}
		}

		if (values.size() > 0) {

			// creating the array of the points coordinates
			double[] coordinates = new double[values.size()];
			int i = 0;

			for (Double dvalue : values) {

				coordinates[i] = dvalue.doubleValue();
				i++;
			}

			return coordinates;
		}

		return null;
	}
}
