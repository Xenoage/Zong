package com.xenoage.zong.musiclayout.stampings;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.symbols.Symbol;

/**
 * Stamping of an accidental.
 *
 * @author Andreas Wenger
 */
@Const @Getter
public class AccidentalStamping
	extends StaffSymbolStamping {
	
	/** The index of the accidental in the chord. */
	public final int accidentalIndex;


	public AccidentalStamping(ChordNotation chord, int accidentalIndex, StaffStamping parentStaff,
		SP position, float scaling, Symbol symbol) {
		super(chord, parentStaff, symbol, null, position, scaling, false);
		this.accidentalIndex = accidentalIndex;
	}
	
	@Override public ChordNotation getElement() {
		return (ChordNotation) element;
	}

}
