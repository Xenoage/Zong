package com.xenoage.zong.core.music.barline

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.Measure

/**
 * Class for a barline.
 *
 * Though barlines can be placed in the middle of a measure, usually they are used
 * as the explicit start or end barline of a measure.
 *
 * They are never placed within a [Measure], but
 * always within a [ColumnHeader].
 */
data class Barline(
		/** The style of the line(s) */
		val style: BarlineStyle,
		/** The repeat style */
		val repeat: BarlineRepeat = BarlineRepeat.None,
		/** The number of repeats */
		val repeatTimes: RepeatTimes = 0
) : ColumnElement {

	override var parent: ColumnHeader? = null

	override fun toString() = "Barline [style=$style, repeat=$repeat, repeatTimes=$repeatTimes]"
}

/** Creates a barline without repeat. */
fun barline(style: BarlineStyle) = Barline(style, BarlineRepeat.None, 0)

/** Creates a regular barline. */
fun barlineRegular() = Barline(BarlineStyle.Regular, BarlineRepeat.None, 0)

/** Creates a barline with forward repeat. */
fun barlineForwardRepeat(style: BarlineStyle) = Barline(style, BarlineRepeat.Forward, 0)

/** Creates a barline with backward repeat. */
fun barlineBackwardRepeat(style: BarlineStyle, repeatTimes: RepeatTimes) =
		Barline(style, BarlineRepeat.Backward, repeatTimes)

/**
 * Creates a barline with repeat at both sides. This barline is only
 * supported as a mid-measure barline!
 */
fun barlineMiddleBothRepeat(style: BarlineStyle, repeatTimes: RepeatTimes) =
		Barline(style, BarlineRepeat.Both, repeatTimes)

/**
 * The number of repeats. n means: jump n times back.
 * Only used for backward repeats, otherwise 0.
 */
typealias RepeatTimes = Int

