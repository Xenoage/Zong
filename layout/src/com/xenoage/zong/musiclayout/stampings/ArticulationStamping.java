package com.xenoage.zong.musiclayout.stampings;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.symbols.Symbol;

/**
 * Stamping of an articulation.
 *
 * @author Andreas Wenger
 */
@Const @Getter
public class ArticulationStamping
	extends StaffSymbolStamping {
	
	/** The index of the articulation in the chord. */
	public final int articulationIndex;

	
	public ArticulationStamping(ChordNotation chord, int articulationIndex, StaffStamping parentStaff,
		SP position, float scaling, Symbol symbol) {
		super(chord, parentStaff, symbol, null, position, scaling, false);
		this.articulationIndex = articulationIndex;
	}
	
	@Override public ChordNotation getElement() {
		return (ChordNotation) element;
	}

}
