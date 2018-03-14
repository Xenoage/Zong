package com.xenoage.zong.core.music.chord

import com.xenoage.zong.core.music.Pitch


/**
 * Class for a single note.
 * Within a score, it is always part of a chord.
 */
data class Note(
	val pitch: Pitch
) {
	override fun toString() = "note($pitch)"
}

/**
 * Returns a list of [Note]s from the given [Pitch]es.
 */
fun notes(vararg pitches: Pitch) = pitches.map(::Note)
