package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Stamping of an accidental.
 *
 * @author Andreas Wenger
 */
@Const public final class AccidentalStamping
	extends StaffSymbolStamping {

	/**
	 * Creates a new {@link AccidentalStamping}.
	 * @param chord           the chord this accidental belongs to
	 * @param accidental      the type of the accidental
	 * @param parentStaff     the staff stamping this element belongs to
	 * @param position        the position of the symbol
	 * @param scaling         the scaling. e.g. 1 means, that it fits perfect
	 *                        to the staff size
	 * @param symbolPool      the pool where to find the symbol
	 */
	public AccidentalStamping(Chord chord, Accidental accidental, StaffStamping parentStaff,
		SP position, float scaling, SymbolPool symbolPool) {
		super(parentStaff, chord, symbolPool.getSymbol(CommonSymbol.getAccidental(accidental)), null,
			position, scaling, false);
	}

}
