package com.xenoage.zong.core.music.clef

import com.xenoage.zong.core.music.Fifth
import com.xenoage.zong.core.music.LP
import com.xenoage.zong.core.music.Pitch

/**
 * This is the type of a clef.
 *
 * It contains the clef symbol, the pitch of its center, the line position of its center,
 * and the minimum line position allowed for flats and sharps of a key signature.
 */
data class ClefType(
	/** The symbol of this clef.  */
	val symbol: ClefSymbol,
	/** The line, this type of clef lies on.  */
	val lp: LP
) {

	/**
	 * Computes and returns the line position of the given pitch, that is the
	 * vertical offset of the note in half-spaces from the bottom line at a
	 * 5-lines-staff. Some examples:
	 *
	 *  * 0: note is on the bottom line
	 *  * -2: note is on the first lower leger line
	 *  * 5: note is between the 3rd and 4th line from below
	 */
	fun getLP(pitch: Pitch): Int {
		val clefPitch = this.symbol.pitch
		var ret = pitch.step + 7 * pitch.octave
		ret -= clefPitch.step + 7 * clefPitch.octave
		ret += lp
		return ret
	}

	/** Gets the lowest line that may be used for a key signature.*/
	fun getKeySignatureLowestLP(fifth: Fifth): LP =
		if (fifth < 0) symbol.minFlatLp else symbol.minSharpLp
}

/** Treble clef.  */
val clefTreble = ClefType(ClefSymbol.G, 2)
/** Treble clef quindicesima bassa.  */
val clefTreble15vb = ClefType(ClefSymbol.G15vb, 2)
/** Treble clef ottava bassa.  */
val clefTreble8vb = ClefType(ClefSymbol.G8vb, 2)
/** Treble clef ottava alta.  */
val clefTreble8va = ClefType(ClefSymbol.G8va, 2)
/** Treble clef quindicesima alta.  */
val clefTreble15va = ClefType(ClefSymbol.G15va, 2)
/** Bass clef.  */
val clefBass = ClefType(ClefSymbol.F, 6)
/** Bass clef quindicesima bassa.  */
val clefBass15vb = ClefType(ClefSymbol.F15vb, 6)
/** Bass clef ottava bassa.  */
val clefBass8vb = ClefType(ClefSymbol.F8vb, 6)
/** Bass clef ottava alta.  */
val clefBass8va = ClefType(ClefSymbol.F8va, 6)
/** Bass clef quindicesima alta.  */
val clefBass15va = ClefType(ClefSymbol.F15va, 6)
/** Alto clef.  */
val clefAlto = ClefType(ClefSymbol.C, 4)
/** Tenor clef.  */
val clefTenor = ClefType(ClefSymbol.C, 6)
/** Tab clef.  */
val clefTab = ClefType(ClefSymbol.Tab, 4)
/** Smaller tab clef.  */
val clefTabSmall = ClefType(ClefSymbol.TabSmall, 4)
/** Percussion clef, two filled rects.  */
val clefPercTwoRects = ClefType(ClefSymbol.PercTwoRects, 4)
/** Percussion clef, large empty rects.  */
val clefPercEmptyRect = ClefType(ClefSymbol.PercEmptyRect, 4)
/** No clef. Transposition like treble clef, but invisible.  */
val clefNone = ClefType(ClefSymbol.None, 4)

/** Common clefs. Instances for reuse.  */
val commonClefs: Map<ClefSymbol, ClefType> = listOf(clefTreble, clefTreble15vb, clefTreble8vb, clefTreble8va, clefTreble15va,
		clefBass, clefBass15vb, clefBass8vb, clefBass8va, clefBass15va, clefAlto, clefTenor, clefTab, clefTabSmall,
		clefPercTwoRects, clefPercEmptyRect, clefNone).associateBy { it.symbol }