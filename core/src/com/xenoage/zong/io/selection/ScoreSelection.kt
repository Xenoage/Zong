package com.xenoage.zong.io.selection

import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.music.Pitch
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.io.score.ScoreInputOptions
import com.xenoage.zong.utils.exceptions.IllegalMPException
import com.xenoage.zong.utils.exceptions.MeasureFullException


/**
 * Interface for selections within a score.
 *
 * A simple cursor, a list of elements or staves and many
 * other things may form a selection.
 *
 * A selection always contains a score. When one of the
 * `write` methods is called, the selection and the score is transformed in
 * some way, maybe dependent on the given [ScoreInputOptions].
 *
 * @author Andreas Wenger
 */
interface ScoreSelection : Selection {


	/**
	 * Gets the score behind the selection.
	 */
	val score: Score


	/**
	 * Gets the [MP], that fits best to describe this
	 * selection (most often, this will be the start position).
	 */
	val mp: MP


	/**
	 * Writes the given pitches, e.g. as a chord.
	 * Each type of selection may have a different behavior.
	 */
	@Throws(IllegalMPException::class, MeasureFullException::class)
	fun write(pitches: Array<Pitch>, options: ScoreInputOptions)


	/**
	 * Writes the given [ColumnElement].
	 * Each type of selection may have a different behavior.
	 */
	@Throws(IllegalMPException::class)
	fun write(element: ColumnElement)


	/**
	 * Writes the given [ColumnElement].
	 * Each type of selection may have a different behavior.
	 */
	@Throws(IllegalMPException::class, MeasureFullException::class)
	fun write(element: ColumnElement, options: ScoreInputOptions)


	/**
	 * Writes the given [VoiceElement].
	 * Each type of selection may have a different behavior.
	 */
	@Throws(IllegalMPException::class, MeasureFullException::class)
	fun write(element: VoiceElement)


	/**
	 * Writes the given [VoiceElement].
	 * Each type of selection may have a different behavior.
	 */
	@Throws(IllegalMPException::class, MeasureFullException::class)
	fun write(element: VoiceElement, options: ScoreInputOptions)


	/**
	 * Writes the given [MeasureElement] element.
	 * Dependent on its type, it may replace elements of the same type.
	 * Each type of selection may have a different
	 * behavior.
	 */
	@Throws(IllegalMPException::class)
	fun write(element: MeasureElement)


	/**
	 * Writes the given [MeasureElement] element.
	 * Dependent on its type, it may replace elements of the same type.
	 * Each type of selection may have a different
	 * behavior.
	 */
	@Throws(IllegalMPException::class)
	fun write(element: MeasureElement, options: ScoreInputOptions)

}
