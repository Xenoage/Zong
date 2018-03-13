package com.xenoage.zong.core.music.barline


/**
 * Repeat style of a barline, inspired by MusicXML.
 */
enum class BarlineRepeat {
	/** Repeat sign at the right side of a repeated section */
	Backward,
	/** Repeat sign at the left side of a repeated section */
	Forward,
	/** Repeat sign at both sides. Only allowed for barlines in the middle of a measure. */
	Both,
	/** No repeat */
	None;

	/** True for backward and both */
	val isBackward: Boolean
		get() = this == Backward || this == Both

	/** True for forward and both */
	val isForward: Boolean
		get() = this == Forward || this == Both

}
