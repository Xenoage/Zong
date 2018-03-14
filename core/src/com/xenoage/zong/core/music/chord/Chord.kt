package com.xenoage.zong.core.music.chord

import com.xenoage.utils.annotations.Optimized
import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.Pitch
import com.xenoage.zong.core.music.Voice
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.annotation.Articulation
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.direction.Direction
import com.xenoage.zong.core.music.direction.DirectionContainer
import com.xenoage.zong.core.music.lyric.Lyric
import com.xenoage.zong.core.music.slur.Slur
import com.xenoage.zong.core.music.tuplet.Tuplet
import com.xenoage.zong.core.music.util.Duration
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MPElement
import com.xenoage.zong.core.util.InconsistentScoreException
import lombok.NonNull

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
		/** The notes within this chord, sorted ascending (begin with lowest notated pitch) */
		notes: List<Note>,
		/** The duration of this chord. For a grace chord, this is 0. */
		override var duration: Duration
) : VoiceElement, DirectionContainer {

	var notes: List<Note>
		set(notes) {
			field = notes
		}

	/** The stem of this chord */
	var stem: Stem = defaultStem

	/** True, if this chord has cue size, otherwise false. */
	var isCue: Boolean = false

	/** The grace value of this chord, or null if it is a normal chord. */
	var grace: Grace? = null

	/** The articulation, ornament and other annotations on this chord,
	 * sorted by ascending distance to the chord. The empty list may be immutable. */
	var annotations: List<Annotation> = emptyList()

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

	/** Back reference: the parent voice, or null if not part of a score. */
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
		get() = parent?.score

	override val elementType: MusicElementType
		get() = MusicElementType.Chord

	/**
	 * Adds a pitch this chord.
	 */
	fun addPitch(pitch: Pitch) {
		addNote(Note(pitch))
	}

	/**
	 * Adds a note this chord.
	 */
	fun addNote(note: Note) {
		//insert at right position
		var i = 0
		while (i < notes!!.size) {
			if (notes!![i].pitch.compareToNotation(note.pitch) > 0)
				break
			i++
		}
		notes!!.add(i, note)
	}

	private fun checkNotesOrder(notes: ArrayList<Note>) {
		var currentPitch: Pitch? = null
		var lastPitch = notes[0].pitch
		for (i in range(1, notes.size - 1)) {
			currentPitch = notes[i].pitch
			//pitches must be sorted ascending
			if (currentPitch.compareToNotation(lastPitch) < 0)
				throw IllegalArgumentException("Pitches must be sorted ascending (notation order)!")
			lastPitch = currentPitch
		}
	}

	fun addDirection(direction: Direction) {
		directions = addOrNew(directions, direction)
	}

	override fun toString(): String {
		return "chord(" + notes!![0].toString() + (if (notes!!.size > 1) ",..." else "") +
				(if (duration!!.isGreater0) ";dur:" + duration!! else ";grace") + ")"
	}

	override fun getChildMP(child: MPElement<*>): MP {
		//all children have the same musical position as this chord
		return MP.getMP(this)
	}

}

/**
 * Creates a chord with the given note and duration.
 */
fun chord(note: Note, duration: Fraction) = Chord(listOf(note), duration)

/**
 * Creates a chord with the given notes and duration.
 * The pitches must be sorted ascending (begin with the lowest notated pitch,
 * end with the highest notated pitch), otherwise an [IllegalArgumentException] is thrown.
 */
constructor(notes: ArrayList<Note>, duration: Fraction) {
	checkArgsNotNull(notes, duration)
	checkNotesOrder(notes)
	if (false == duration.isGreater0)
		throw InconsistentScoreException("Only grace chords may not have 0 duration")
	this.notes = notes
	this.duration = duration
}

/**
 * Creates a grace chord with the given notes.
 * The pitches must be sorted ascending (begin with the lowest notated pitch,
 * end with the highest notated pitch), otherwise an [IllegalArgumentException] is thrown.
 */
constructor(notes: ArrayList<Note>, grace: Grace) {
	checkArgsNotNull(notes, grace)
	checkNotesOrder(notes)
	if (false == grace.graceDuration!!.isGreater0)
		throw InconsistentScoreException("Grace duration must be greater than 0")
	this.notes = notes
	this.duration = Companion.get_0()
	this.grace = grace
}