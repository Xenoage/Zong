package com.xenoage.zong.core.music

import com.xenoage.utils.annotations.Optimized
import com.xenoage.utils.annotations.Reason
import com.xenoage.zong.core.music.Step.*


/**
 * Pitch is represented as a combination of the step of the
 * diatonic scale, the chromatic alteration, and the octave.
 * The step element uses the numbers 0 (C) to 6 (B).
 * The alter element represents chromatic alteration in
 * number of semitones (e.g., -1 for flat, 1 for sharp).
 * The octave element is represented
 * by the numbers 0 to 9, where 4 indicates the octave
 * started by middle C.
 * (Parts copied from the MusicXML specification)
 */
@Optimized(Reason.MemorySaving)
data class Pitch(
	val step: Step,
	val alter: Int,
	val octave: Int
) : Comparable<Pitch> {

	/** The step as a char between 'A' and 'G' */
	val stepAsChar: Char
		get() = _stepAsChar

	/**
	 * Gets a copy of this pitch without alter.
	 */
	fun withoutAlter(): Pitch =
		pi(step, 0, octave)

	/**
	 * Returns this [Pitch] as a String in the format "(step,alter,octave)", e.g. "(B,0,4)".
	 */
	override fun toString() = "($stepAsChar,$alter,$octave)"

	override fun compareTo(pitch: Pitch): Int =
			_value.compareTo(pitch._value)

	/**
	 * Like [compareTo], but ignores the alter component.
	 * E.g. a E# has a lower notation position than Fb, although it sounds
	 * higher. This method regards only the notation position.
	 */
	fun compareToNotation(pitch: Pitch): Int =
			_valueNoAlter.compareTo(pitch._valueNoAlter)

	//cache
	private var _stepAsChar: Char = '?'
	private var _value: Int = 0
	private var _valueNoAlter: Int = 0

	init {
		//cache: step as a char
		_stepAsChar = step.toString()[0]

		//cache: value consisting of octave, step (and alter)
		_value = octave * 12 + getStepSemitones(step) + alter
		_valueNoAlter = octave * 12 + getStepSemitones(step)
	}

	private fun getStepSemitones(step: Step): Int {
		when (step) {
			C -> return 0
			D -> return 2
			E -> return 4
			F -> return 5
			G -> return 7
			A -> return 9
			B -> return 11
		}
	}

	companion object {

		fun pi(step: Step, alter: Int, octave: Int): Pitch {
			return if (alter in -2..2 && octave in 0..8)
				_pitches[step.ordinal][alter][octave] //use cached pitch
			else
				Pitch(step, alter, octave) //create new pitch
		}

		fun pi(step: Int, alter: Int, octave: Int): Pitch =
			pi(Step.values()[step], alter, octave)

		//cache for reuse: (nearly) all possible pitch values
		private val _pitches: Array<Array<Array<Pitch>>> =
				Step.values().map { step ->
					(-2..2).map { alter ->
						(0..8).map { octave ->
							Pitch(step, alter, octave)
						}.toTypedArray()
					}.toTypedArray()
				}.toTypedArray()

	}

}

/** Musical step, like 'A' or 'G'. */
enum class Step {
	C,
	D,
	E,
	F,
	G,
	A,
	B
}