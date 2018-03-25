package com.xenoage.utils.math

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 2D point in float precision.
 */
data class Point2f(
		val x: Float,
		val y: Float) {

	constructor(size: Size2f) : this(size.width, size.height)

	constructor(p: Point2i) : this(p.x.toFloat(), p.y.toFloat())

	constructor(xy: Float): this(xy, xy)

	fun add(p: Point2f) = Point2f(x + p.x, y + p.y)

	fun add(p: Point2i) = Point2f(x + p.x, y + p.y)

	fun add(s: Size2f) = Point2f(x + s.width, y + s.height)

	fun add(x: Float, y: Float) = Point2f(this.x + x, this.y + y)

	fun sub(p: Point2f) = Point2f(x - p.x, y - p.y)

	fun sub(s: Size2f) = Point2f(x - s.width, y - s.height)

	fun sub(x: Float, y: Float) = Point2f(this.x - x, this.y - y)

	fun scale(s: Float) = Point2f(x * s, y * s)

	fun length(): Float {
		return sqrt((x * x + y * y).toDouble()).toFloat()
	}

	fun normalize(): Point2f {
		val length = sqrt((x * x + y * y).toDouble()).toFloat()
		return Point2f(x / length, y / length)
	}

	fun dotProduct(p: Point2f) = x * p.x + y * p.y

	/**
	 * Rotates this point around another one.
	 * @param around   the point to rotate around
	 * @param radians  the angle in radians
	 */
	fun rotate(around: Point2f, radians: Float): Point2f {
		val x = this.x - around.x
		val y = this.y - around.y
		val cos = cos(radians.toDouble()).toFloat()
		val sin = sin(radians.toDouble()).toFloat()
		val xr = x * cos - y * sin
		val yr = y * cos + x * sin
		return p(xr + around.x, yr + around.y)
	}

	override fun toString() = "($x, $y)"

	companion object {

		/** Origin point, (0, 0).  */
		val origin = Point2f(0f, 0f)

		fun p(x: Float, y: Float) = Point2f(x, y)

		fun distance(p1: Point2f, p2: Point2f): Float {
			val x = p1.x - p2.x
			val y = p1.y - p2.y
			return sqrt((x * x + y * y).toDouble()).toFloat()
		}

		/**
		 * Returns the normalized normal vector to the vector between the
		 * given two points.
		 */
		fun normalVector(start: Point2f, end: Point2f) = normalVector(end.sub(start))

		/**
		 * Returns the normalized normal vector to the given vector.
		 */
		fun normalVector(v: Point2f) = p(-v.y, v.x).normalize()

		/**
		 * Returns the normalized normal vector at the second given point.
		 * It is the mean value of the normal vector between the first and
		 * second and of the normal vector of the second and third point.
		 */
		fun normalVectorMean(start: Point2f, middle: Point2f, end: Point2f): Point2f {
			val n1 = normalVector(start, middle)
			val n2 = normalVector(middle, end)
			return n1.add(n2).normalize()
		}

		/**
		 * Returns true, if the given point p is within the triangle
		 * defined by the given points t1, t2 and t3.
		 */
		fun isPointInTriangle(p: Point2f, t1: Point2f, t2: Point2f, t3: Point2f): Boolean {
			val b1 = sign(p, t1, t2) < 0
			val b2 = sign(p, t2, t3) < 0
			val b3 = sign(p, t3, t1) < 0
			return b1 == b2 && b2 == b3
		}

		private fun sign(p1: Point2f, p2: Point2f, p3: Point2f): Float {
			return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y)
		}
	}

}
