package com.xenoage.utils.math

import kotlin.math.round
import kotlin.math.roundToInt

/**
 * 2D point in integer precision.
 */
data class Point2i(
		val x: Int,
		val y: Int) {

	constructor(p: Point2f) : this(p.x.roundToInt(), p.y.roundToInt())

	fun add(x: Int, y: Int) = Point2i(this.x + x, this.y + y)

	fun add(p: Point2i) = Point2i(this.x + p.x, this.y + p.y)

	fun sub(p: Point2i) = Point2i(this.x - p.x, this.y - p.y)

	override fun toString() = "($x, $y)"

	companion object {
		/** Point at the origin (0, 0).  */
		val origin = Point2i(0, 0)
	}

}
