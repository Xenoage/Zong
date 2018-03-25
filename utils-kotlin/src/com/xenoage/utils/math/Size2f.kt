package com.xenoage.utils.math

import kotlin.coroutines.experimental.EmptyCoroutineContext.get
import kotlin.math.roundToInt

/**
 * 2D size in float precision.
 */
data class Size2f(
		val width: Float,
		val height: Float) {

	constructor(size: Size2i) : this(size.width.toFloat(), size.height.toFloat())

	val area: Float
		get() = width * height

	fun add(size: Size2i) = Size2f(this.width + size.width, this.height + size.height)

	fun scale(f: Float) = Size2f(width * f, height * f)

	override fun toString() = "${width}x${height}"

	companion object {
		/** Size with width and height of 0.  */
		val size0 = Size2f(0f, 0f)
	}

}
