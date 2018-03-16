package com.xenoage.zong.core.music.clef

import com.xenoage.zong.core.music.Pitch
import com.xenoage.zong.core.music.Pitch.Companion.pi
import com.xenoage.zong.core.music.Step

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
	G(pi(Step.G, 0, 4), 3, 1, 0),
	/** G clef quindicesima bassa.  */
	G15vb(pi(Step.G, 0, 2), 3, 1, -2),
	/** G clef ottava bassa.  */
	G8vb(pi(Step.G, 0, 3), 3, 1, -1),
	/** G clef ottava alta.  */
	G8va(pi(Step.G, 0, 5), 3, 1, 1),
	/** G clef quindicesima alta.  */
	G15va(pi(Step.G, 0, 6), 3, 1, 2),
	/** F clef.  */
	F(pi(Step.F, 0, 3), 1, -1, 0),
	/** F clef quindicesima bassa.  */
	F15vb(pi(Step.F, 0, 1), 1, -1, -2),
	/** F clef ottava bassa.  */
	F8vb(pi(Step.F, 0, 2), 1, -1, -1),
	/** F clef ottava alta.  */
	F8va(pi(Step.F, 0, 4), 1, -1, 1),
	/** F clef quindicesima alta.  */
	F15va(pi(Step.F, 0, 5), 1, -1, 2),
	/** C clef.  */
	C(pi(Step.C, 0, 4), 2, 0, 0),
	/** Tab clef.  */
	Tab(pi(Step.B, 0, 4), 3, 1, 0),
	/** Smaller tab clef.  */
	TabSmall(pi(Step.B, 0, 4), 3, 1, 0),
	/** Percussion clef, two filled rects.  */
	PercTwoRects(pi(Step.B, 0, 4), 3, 1, 0),
	/** Percussion clef, large empty rects.  */
	PercEmptyRect(pi(Step.B, 0, 4), 3, 1, 0),
	/** No clef symbol.  */
	None(pi(Step.B, 0, 4), 3, 1, 0)
}
