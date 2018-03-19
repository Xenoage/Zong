package com.xenoage.zong.core.music.volta

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement

/**
 * Class for a volta (also called "ending" in MusicMXL, and informally called "Haus" in German).
 *
 * A volta is never used within a voice, but only in the [ColumnHeader].
 * Voltas span over whole measures, at least one. The number of spanned
 * measures is saved in this class.
 *
 * Each volta has a numbers attribute, which is a range which
 * tells in which repetitions it should be entered,
 * and optionally an arbitrary caption.
 * A downward hook on the right side is optional.
 *
 * The rules for ordering consecutive voltas (called a volta group) are as follows:
 * The voltas should sorted (even this is no technical restriction), e.g.
 * the "2nd time" volta should not appear before the "1st time" volta.
 * Gaps can be filled with default voltas (= voltas without numbers), e.g. a default
 * volta between a "1st time" and "4th time" volta is played two times. If a "4th time"
 * volta follows a "1st time" volta directly, the "1st time" volta is played 3 times.
 * When the last volta of a group is a default volta, it is always played (the very last time).
 */
class Volta(
		/** The number of measures this volta spans, at least one.  */
		val length: Int,
		/** The repetitions (beginning with 1) where this volta is entered, or null for the default case.
		 * E.g. [1,1] for the 1st time, ..., null for "else".  */
		val numbers: IntRange? = null,
		/** The caption, or null to use a default caption, generated from the numbers:
		 * [x,x] results to "x.", [x,y] to "x.–y.", null to "". */
		val captionOrNull: String? = null,
		/** True, iff there is a downward hook on the right side.  */
		val rightHook: Boolean = true
) : ColumnElement {

	init {
		if (length < 1)
			throw IllegalArgumentException("Volta must span at least 1 measure")
	}

	override var parent: ColumnHeader? = null

	/** The caption of this volta.
	 * This is never null, but may be the empty string. */
	val caption: String
		get() = captionOrNull ?:
		if (numbers == null) ""
		else if (numbers.count() === 1) "${numbers.start}."
		else "${numbers.start}.–${numbers.endInclusive}."

	/**
	 * True, if this volta is the default case,
	 * i.e. not for an explicit repeat time like 1st or 2nd time.
	 */
	val isDefault: Boolean
		get() = numbers == null

	/**
	 * The index of the measure, where this volta ends (inclusive).
	 * If this volta is not part of a score, an exception is thrown.
	 */
	val endMeasureIndex: Int
		get() = (mp?.measure ?: throw IllegalStateException()) + length - 1
}
