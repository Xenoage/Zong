package com.xenoage.zong.core.music.chord

/**
 * Accidental.
 *
 * Compare musicxml 2.0: note.mod, l. 216
 * harp-sharp, flat-flat, natural-sharp, natural-flat, quarter-flat, quarter-sharp,
 * three-quarters-flat, and three-quarters-sharp are not implemented yet.
 */
enum class Accidental {

	DoubleSharp,
	Sharp,
	Natural,
	Flat,
	DoubleFlat;


	companion object {

		fun fromAlter(alter: Int) = when (alter) {
			-2 -> DoubleFlat
			-1 -> Flat
			+1 -> Sharp
			+2 -> DoubleSharp
			else -> Natural
		}
	}

}
