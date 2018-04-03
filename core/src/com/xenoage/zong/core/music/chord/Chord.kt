package com.xenoage.zong.core.music.chord

import com.xenoage.utils.collections.SortedList
import com.xenoage.utils.collections.sortedListOf
import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math._0
import com.xenoage.utils.math._1_8
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.Pitch
import com.xenoage.zong.core.music.Voice
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.annotation.Articulation
import com.xenoage.zong.core.music.annotation.ArticulationType
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.direction.Direction
import com.xenoage.zong.core.music.lyric.Lyric
import com.xenoage.zong.core.music.slur.Slur
import com.xenoage.zong.core.music.tuplet.Tuplet
import com.xenoage.zong.core.music.util.Duration
import com.xenoage.zong.core.position.MPContainer


/**
 * Class for a chord.
 *
 * To make things easy, every note is in a chord.
 * Thus also a single note is a chord by definition.
 *
 * A chord can have a stem and articulations.
 * It may be a normal chord (default case), a cue chord (printed small,
 * but has a duration like normal chords) or a grace chord, which duration
 * is 0 (the grace duration is saved in the [Grace] class).
 *
 * A chord can be part of a tuplet, a beam, one or more slurs and
 * and one or more lyrics and directions can be attached to it.
 */
class Chord(
		/** The notes within this chord, sorted ascending (begin with lowest notated pitch). */
		val notes: SortedList<Note>,
		/** The duration of this chord. For a grace chord, this is 0. */
		override var duration: Duration
) : VoiceElement, MPContainer {

	/** The stem of this chord */
	var stem: Stem = defaultStem

	/** True, if this chord has cue size, otherwise false. */
	var isCue: Boolean = false

	/** The grace value of this chord, or null if it is a normal chord. */
	var grace: Grace? = null

	/** The articulation, ornament and other annotations on this chord,
	 * sorted by ascending distance to the chord. The empty list may be immutable. */
	var annotations: List<out Annotation> = emptyList()

	/** The beam this chord is part of, or null. */
	var beam: Beam? = null

	/** The slurs which start or end at this chord. */
	var slurs: List<Slur> = emptyList()

	/** The tuplet this chord is part of, or null. */
	var tuplet: Tuplet? = null

	/** The lyrics attached to this chord. */
	var lyrics: List<Lyric> = emptyList()

	/** The directions attached to this chord. */
	var directions: List<Direction> = emptyList()

	override var parent: Voice? = null

	/**
	 * Collects and returns all pitches of this chord.
	 * Pitches that are used more times are also used more
	 * times in the list.
	 */
	val pitches: List<Pitch>
		get() = notes.map { it.pitch }

	/**
	 * Gets the displayed duration of this chord. For full chords, this method returns the
	 * same value as [duration]. For grace notes, the value of [grace.graceDuration] is returned.
	 */
	val displayedDuration: Duration
		get() = grace?.graceDuration ?: duration

	/**
	 * Returns true, if this chord is a grace chord.
	 */
	val isGrace: Boolean
		get() = grace != null

	/**
	 * Convenience method. Gets the parent score of this chord,
	 * or null, if this chord is not part of a score.
	 */
	val score: Score?
		get() = (parent as Voice?)?.score

	/** Adds a pitch to this chord. */
	fun addPitch(pitch: Pitch) {
		addNote(Note(pitch))
	}

	/** Adds a note this chord. */
	fun addNote(note: Note) {
		notes.add(note)
	}

	fun addDirection(direction: Direction) {
		directions = directions.plus(direction)
	}

	override fun toString(): String =
		"chord(" + notes!![0].toString() + (if (notes!!.size > 1) ",..." else "") +
				(if (duration!!.isGreater0) ";dur:" + duration!! else ";grace") + ")"

	companion object {

		/** Creates a chord with the given notes and duration. */
		operator fun invoke(duration: Duration, vararg notes: Note) =
				Chord(sortedListOf(*notes), duration)

		/** Creates a chord with the given pitches and duration. */
		operator fun invoke(duration: Duration, vararg pitches: Pitch) =
				Chord(sortedListOf(pitches.map { Note(it) }), duration)

		/** Creates a grace chord with the given pitch and displayed duration. */
		fun graceChord(pitch: Pitch, displayedDuration: Fraction = _1_8): Chord {
			val chord = invoke(_0, pitch)
			chord.grace = Grace(true, displayedDuration)
			return chord
		}

	}

	//TODO: convenience method. move to helper class
	fun addArticulations(vararg articulations: ArticulationType): Chord {
		this.annotations += articulations.map { Articulation(it) as Annotation }
		return this
	}

}