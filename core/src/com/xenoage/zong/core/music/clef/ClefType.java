package com.xenoage.zong.core.music.clef;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

import com.xenoage.zong.core.music.Pitch;

/**
 * This is the type of a clef.
 * 
 * It contains the clef symbol, the pitch of its center, the line position of its center,
 * and the minimum line position allowed for flats and sharps
 * of a key signature.
 *
 * @author Andreas Wenger
 */
@Data public class ClefType {

	/** Treble clef. */
	public static final ClefType clefTreble = new ClefType(ClefSymbol.G, 2);
	/** Treble clef quindicesima bassa. */
	public static final ClefType clefTreble15vb = new ClefType(ClefSymbol.G15vb, 2);
	/** Treble clef ottava bassa. */
	public static final ClefType clefTreble8vb = new ClefType(ClefSymbol.G8vb, 2);
	/** Treble clef ottava alta. */
	public static final ClefType clefTreble8va = new ClefType(ClefSymbol.G8va, 2);
	/** Treble clef quindicesima alta. */
	public static final ClefType clefTreble15va = new ClefType(ClefSymbol.G15va, 2);
	/** Bass clef. */
	public static final ClefType clefBass = new ClefType(ClefSymbol.F, 6);
	/** Bass clef quindicesima bassa. */
	public static final ClefType clefBass15vb = new ClefType(ClefSymbol.F15vb, 6);
	/** Bass clef ottava bassa. */
	public static final ClefType clefBass8vb = new ClefType(ClefSymbol.F8vb, 6);
	/** Bass clef ottava alta. */
	public static final ClefType clefBass8va = new ClefType(ClefSymbol.F8va, 6);
	/** Bass clef quindicesima alta. */
	public static final ClefType clefBass15va = new ClefType(ClefSymbol.F15va, 6);
	/** Alto clef. */
	public static final ClefType clefAlto = new ClefType(ClefSymbol.C, 4);
	/** Tenor clef. */
	public static final ClefType clefTenor = new ClefType(ClefSymbol.C, 6);
	/** Tab clef. */
	public static final ClefType clefTab = new ClefType(ClefSymbol.Tab, 4);
	/** Smaller tab clef. */
	public static final ClefType clefTabSmall = new ClefType(ClefSymbol.TabSmall, 4);
	/** Percussion clef, two filled rects. */
	public static final ClefType clefPercTwoRects = new ClefType(ClefSymbol.PercTwoRects, 4);
	/** Percussion clef, large empty rects. */
	public static final ClefType clefPercEmptyRect = new ClefType(ClefSymbol.PercEmptyRect, 4);
	/** No clef. Transposition like treble clef, but invisible. */
	public static final ClefType clefNone = new ClefType(ClefSymbol.None, 4);

	/** Common clefs. Instances for reuse. */
	public static final Map<ClefSymbol, ClefType> commonClefs = createCommonClefs();
	
	/** The symbol of this clef. */
	private final ClefSymbol symbol;
	/** The line position this type of clef lies on. */
	private final int lp;

	/**
	 * Computes and returns the line position of the given pitch, that is the
	 * vertical offset of the note in half-spaces from the bottom line at a
	 * 5-lines-staff. Some examples:
	 * <ul>
	 *   <li>0: note is on the bottom line</li>
	 *   <li>-2: note is on the first lower leger line</li>
	 *   <li>5: note is between the 3rd and 4th line from below</li>
	 * </ul>
	 */
	public int getLp(Pitch pitch) {
		Pitch clefPitch = this.symbol.getPitch();
		int ret = pitch.getStep() + 7 * pitch.getOctave();
		ret -= clefPitch.getStep() + 7 * clefPitch.getOctave();
		ret += lp;
		return ret;
	}

	/**
	 * Gets the lowest line that may be used for a key signature.
	 * @param fifth  the number within the circle of fifth, e.g. 1 for G major or E minor. Between -7 and +7.
	 */
	public int getKeySignatureLowestLp(int fifth) {
		if (fifth < 0)
			return symbol.getMinFlatLp();
		else
			return symbol.getMinSharpLp();
	}
	
	private static Map<ClefSymbol, ClefType> createCommonClefs() {
		HashMap<ClefSymbol, ClefType> ret = new HashMap<ClefSymbol, ClefType>();
		for (ClefType clef : new ClefType[]{ clefTreble, clefTreble15vb, clefTreble8vb, clefTreble8va,
			clefTreble15va, clefBass, clefBass15vb, clefBass8vb, clefBass8va, clefBass15va,
			clefAlto, clefTenor, clefTab, clefTabSmall, clefPercTwoRects, clefPercEmptyRect, clefNone}) {
			ret.put(clef.symbol, clef);
		}
		return ret;
	}

}
