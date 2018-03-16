package com.xenoage.zong.core.music.chord

import com.xenoage.zong.core.music.Pitch


/**
 * Class for a single note.
 * Within a score, it is always part of a chord.
 */
data class Note(
	val pitch: Pitch
) : Comparable<Note> {

	override fun toString() = "note($pitch)"

	override fun compareTo(other: Note) = pitch.compareTo(other.pitch)

}

/**
 * Returns a list of [Note]s from the given [Pitch]es.
 */
fun notes(vararg pitches: Pitch) = pitches.map(::Note)
