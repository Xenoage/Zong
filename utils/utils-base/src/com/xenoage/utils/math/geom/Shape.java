package com.xenoage.utils.math.geom;

/**
 * Interface for a 2D shape (rectangle, circle, polygon, ...)
 *
 * @author Andreas Wenger
 */
public interface Shape {

	/**
	 * Returns true, if the given point is contained
	 * within this shape.
	 */
	public boolean contains(Point2f point);

}
