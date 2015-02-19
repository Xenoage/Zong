package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Stamping of an accidental.
 *
 * @author Andreas Wenger
 */
public class AccidentalStamping
	extends StaffSymbolStamping {
	
	/** The chord this accidental belongs to. */
	public ChordNotation chord;


	public AccidentalStamping(ChordNotation chord, Accidental accidental, StaffStamping parentStaff,
		SP position, float scaling, SymbolPool symbolPool) {
		super(parentStaff, symbolPool.getSymbol(CommonSymbol.getAccidental(accidental)), null,
			position, scaling, false);
		this.chord = chord;
	}

}
