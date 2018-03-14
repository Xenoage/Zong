package com.xenoage.utils.math

/**
 * Vertical side: top or bottom.
 */
enum class VSide {
	Top,
	Bottom;

	val dir: Int
		get() = if (this == Bottom) -1 else 1

}
