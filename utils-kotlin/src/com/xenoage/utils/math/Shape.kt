package com.xenoage.utils.math

/**
 * Interface for a 2D shape (rectangle, circle, polygon, ...).
 */
interface Shape {

	/** Returns true, if the given point is contained within this shape. */
	operator fun contains(point: Point2f): Boolean

}
