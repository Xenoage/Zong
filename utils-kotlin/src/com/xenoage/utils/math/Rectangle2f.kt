package com.xenoage.utils.math

import com.xenoage.utils.math.Point2f.Companion.p
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * 2D rectangle in float precision.
 */
class Rectangle2f(position: Point2f, size: Size2f) : Shape {

	val position: Point2f
	val size: Size2f

	init {
		//convert negative size
		if (size.width < 0 || size.height < 0) {
			var x = position.x
			var y = position.y
			if (size.width < 0)
				x = position.x + size.width
			if (size.height < 0)
				y = position.y + size.height
			this.position = Point2f(x, y)
			this.size = Size2f(abs(size.width), abs(size.height))
		} else {
			this.position = position
			this.size = size
		}
	}

	constructor(x: Float, y: Float, width: Float, height: Float) :
			this(Point2f(x, y), Size2f(width, height))

	val width get() = size.width
	val height get() = size.height

	fun scale(f: Float) =
			Rectangle2f(position.x * f, position.y * f, size.width * f, size.height * f)

	fun scale(fx: Float, fy: Float) =
			Rectangle2f(position.x * fx, position.y * fy, size.width * fx, size.height * fy)

	fun scaleX(f: Float) =
			Rectangle2f(position.x * f, position.y, size.width * f, size.height)

	fun scaleY(f: Float) =
			Rectangle2f(position.x, position.y * f, size.width, size.height * f)

	fun move(p: Point2f) = Rectangle2f(position.add(p), size)

	fun move(x: Float, y: Float) = Rectangle2f(position.add(x, y), size)

	override operator fun contains(point: Point2f) =
		point.x >= position.x && point.y >= position.y &&
				point.x <= position.x + size.width && point.y <= position.y + size.height

	/**
	 * Expands this rectangle by the given rectangle.
	 * After the operation both rectangles are optimally enclosed
	 * by this rectangle.
	 */
	fun extend(r: Rectangle2f): Rectangle2f {
		val newX1 = min(position.x, r.position.x)
		val newX2 = max(position.x + size.width, r.position.x + r.size.width)
		val newY1 = min(position.y, r.position.y)
		val newY2 = max(position.y + size.height, r.position.y + r.size.height)
		return Rectangle2f(newX1, newY1, newX2 - newX1, newY2 - newY1)
	}

	/**
	 * Expands this rectangle by the given point.
	 * After the operation the given point is enclosed by this rectangle
	 * (optimally, if the rectangle had to be extended).
	 */
	fun extend(p: Point2f): Rectangle2f {
		val newX1 = min(this.position.x, p.x)
		val newX2 = max(this.position.x + this.size.width, p.x)
		val newY1 = min(this.position.y, p.y)
		val newY2 = max(this.position.y + this.size.height, p.y)
		return Rectangle2f(newX1, newY1, newX2 - newX1, newY2 - newY1)
	}

	val x1 get() = position.x
	val y1 get() = position.y
	val x2 get() = position.x + size.width
	val y2 get() = position.y + size.height

	val center get() = Point2f(centerX, centerY)
	val centerX get() = position.x + size.width / 2
	val centerY get() = position.y + size.height / 2

	val nw get() = p(x1, y1)
	val ne get() = p(x2, y1)
	val sw get() = p(x1, y2)
	val se get() = p(x2, y2)

	override fun toString() = "[$position / $size]"

	companion object {

		fun fromX1Y1X2Y2(x1: Float, y1: Float, x2: Float, y2: Float): Rectangle2f {
			return Rectangle2f(Point2f(x1, y1), Size2f(x2 - x1, y2 - y1))
		}

		fun rf(x: Float, y: Float, width: Float, height: Float): Rectangle2f {
			return Rectangle2f(x, y, width, height)
		}
	}

}