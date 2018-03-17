package com.xenoage.zong.core.music.key

import com.xenoage.utils.math.modMin
import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.*
import com.xenoage.zong.core.music.Pitch.Companion.pi
import com.xenoage.zong.core.music.Step.*
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MPContainer
import kotlin.properties.Delegates.notNull

/**
 * Traditional key signature in the circle of fifth and a mode (like major or minor).
 */
class TraditionalKey(
		/** The number within the circle of fifths, e.g. 1 for G major or E minor. Between -7 and +7.  */
		val fifths: Fifth = 0,
		/** The mode, e.g. major. */
		val mode: Mode = Mode.Unknown
) : Key {

	override var parent: ColumnHeader? = null

	/** Major, minor and other modes.  */
	enum class Mode {
		Unknown,
		Major,
		Minor,
		Ionian,
		Dorian,
		Phrygian,
		Lydian,
		Mixolydian,
		Aeolian,
		Locrian
	}

	init {
		if (false == fifths in -7..7)
			throw IllegalArgumentException("fifths must be between -7 and +7")
	}

	override val alterations: List<Int> = _alterations[fifths + 7]

	/**
	 * Gets the step of the flat/sharp with the given index.
	 * For example, in the key Eb flat: index = 0 is B, index = 1 is E and index = 2 is A.
	 */
	fun getStep(index: Int): Step =
			(if (fifths < 0) _stepsFlat else _stepsSharp)[index]

	/**
	 * Returns the nearest higher [Pitch] in the current key.
	 */
	override fun getNearestHigherPitch(pitch: Pitch): Pitch {
		val step = (pitch.step.ordinal + 1) % 7
		val octave = pitch.octave + (pitch.step.ordinal + 1) / 7
		val alter = alterations[step]
		return pi(step, alter, octave)
	}

	/**
	 * Returns the nearest lower [Pitch] in the current key.
	 */
	override fun getNearestLowerPitch(pitch: Pitch): Pitch {
		val step = (pitch.step.ordinal - 1 + 7) % 7
		val octave = pitch.octave + (pitch.step.ordinal - 1 + 7) / 7 - 1
		val alter = alterations[step]
		return pi(step, alter, octave)
	}

	companion object {

		//alterations for the circle of fifths (from -7 to +7, represented by the indices 0 to 14)
		private val _alterations = listOf(
				listOf(-1, -1, -1, -1, -1, -1, -1),
				listOf(-1, -1, -1, 0, -1, -1, -1),
				listOf(0, -1, -1, 0, -1, -1, -1),
				listOf(0, -1, -1, 0, 0, -1, -1),
				listOf(0, 0, -1, 0, 0, -1, -1),
				listOf(0, 0, -1, 0, 0, 0, -1),
				listOf(0, 0, 0, 0, 0, 0, -1),
				listOf(0, 0, 0, 0, 0, 0, 0),
				listOf(0, 0, 0, +1, 0, 0, 0),
				listOf(+1, 0, 0, +1, 0, 0, 0),
				listOf(+1, 0, 0, +1, +1, 0, 0),
				listOf(+1, +1, 0, +1, +1, 0, 0),
				listOf(+1, +1, 0, +1, +1, +1, 0),
				listOf(+1, +1, +1, +1, +1, +1, 0),
				listOf(+1, +1, +1, +1, +1, +1, +1))

		//order of steps in the flat and sharp key signatures
		private val _stepsFlat = arrayOf(B, E, A, D, G, C, F)
		private val _stepsSharp = arrayOf(F, C, G, D, A, E, B)

		/**
		 * Gets the [LP] for the sharp or flat
		 * with the given 0-based index, when C4 is on the given line position.
		 */
		fun getLP(index: Int, sharp: Boolean, lpC4: LP, lpMin: LP): LP {
			var ret = lpC4 + 2 +
					if (sharp) getSharpLP_GKey(index) else getFlatLP_GKey(index)
			ret = modMin(ret, 7, lpMin)
			return ret
		}

		/**
		 * Gets the [LP] for the flat with the given 0-based index when there is a normal G key.
		 */
		private fun getFlatLP_GKey(index: Int): LP =
			when (index) {
				0 -> 4 //Bb
				1 -> 7 //Eb
				2 -> 3 //Ab
				3 -> 6 //Db
				4 -> 2 //Gb
				5 -> 5 //Cb
				6 -> 1 //Fb
				else -> throw IllegalArgumentException("Invalid index: $index")
			}

		/**
		 * Gets the [LP] for the sharp with the given 0-based index when there is a normal G key.
		 */
		private fun getSharpLP_GKey(index: Int): LP =
			when (index) {
				0 -> 8 //F#
				1 -> 5 //C#
				2 -> 9 //G#
				3 -> 6 //D#
				4 -> 3 //A#
				5 -> 7 //E#
				6 -> 4 //H#
				else -> throw IllegalArgumentException("Invalid index: $index")
			}
	}

}
