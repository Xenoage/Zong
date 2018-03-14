package com.xenoage.zong.core.music.clef

import com.xenoage.zong.core.music.Pitch.pi

import com.xenoage.zong.core.music.Pitch

/**
 * Symbols for clefs.
 */
enum class ClefSymbol(
		/** The pitch of the line position this type of clef sits on. */
		val pitch: Pitch,
		/** The minimum line position allowed for a sharp symbol of a key signature. */
		val minSharpLp: Int,
		/** The minimum line position allowed for a flat symbol of a key signature. */
		val minFlatLp: Int,
		/** The octave change implied by this clef.
		 * E.g. 0 means no change, 1 means 1 octave higher, -2 means two octaves lower. */
		val octaveChange: Int
) {
	/** G clef.  */
	G(pi('G', 0, 4), 3, 1, 0),
	/** G clef quindicesima bassa.  */
	G15vb(pi('G', 0, 2), 3, 1, -2),
	/** G clef ottava bassa.  */
	G8vb(pi('G', 0, 3), 3, 1, -1),
	/** G clef ottava alta.  */
	G8va(pi('G', 0, 5), 3, 1, 1),
	/** G clef quindicesima alta.  */
	G15va(pi('G', 0, 6), 3, 1, 2),
	/** F clef.  */
	F(pi('F', 0, 3), 1, -1, 0),
	/** F clef quindicesima bassa.  */
	F15vb(pi('F', 0, 1), 1, -1, -2),
	/** F clef ottava bassa.  */
	F8vb(pi('F', 0, 2), 1, -1, -1),
	/** F clef ottava alta.  */
	F8va(pi('F', 0, 4), 1, -1, 1),
	/** F clef quindicesima alta.  */
	F15va(pi('F', 0, 5), 1, -1, 2),
	/** C clef.  */
	C(pi('C', 0, 4), 2, 0, 0),
	/** Tab clef.  */
	Tab(pi('B', 0, 4), 3, 1, 0),
	/** Smaller tab clef.  */
	TabSmall(pi('B', 0, 4), 3, 1, 0),
	/** Percussion clef, two filled rects.  */
	PercTwoRects(pi('B', 0, 4), 3, 1, 0),
	/** Percussion clef, large empty rects.  */
	PercEmptyRect(pi('B', 0, 4), 3, 1, 0),
	/** No clef symbol.  */
	None(pi('B', 0, 4), 3, 1, 0)
}
