package com.xenoage.utils.math

/**
 * 2D size in integer precision.
 */
data class Size2i(
		val width: Int,
		val height: Int) {

	val area: Int
		get() = width * height

	constructor(p: Point2i) : this(p.x, p.y)

	fun scale(scaling: Int) = Size2i(width * scaling, height * scaling)

	fun scale(scaling: Float) = Size2f(width * scaling, height * scaling)

	override fun toString() = "${width}x${height}"

}
