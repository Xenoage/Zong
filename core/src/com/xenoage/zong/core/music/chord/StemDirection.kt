package com.xenoage.zong.core.music.chord

import com.xenoage.utils.math.VSide

/**
 * Direction of a chord stem.
 */
enum class StemDirection {

	/** No stem */
	None,
	/** Upward stem */
	Up,
	/** Downward stem */
	Down,
	/** Default direction */
	Default;

	/**
	 * Gets the direction of the stem as its signum:
	 * 1 for up, -1 for down, 0 for none.
	 */
	val sign: Int
		get() = when (this) {
			Up -> 1
			Down -> -1
			else -> 0
		}

	/**
	 * Returns `true` if the given [VSide] has the same side as this stem.
	 */
	fun equalsSide(side: VSide): Boolean {
		return this == Up && side === VSide.Top || this == Down && side === VSide.Bottom
	}

}
