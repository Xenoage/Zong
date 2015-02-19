package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Stamping of an articulation.
 *
 * @author Andreas Wenger
 */
@Const public final class ArticulationStamping
	extends StaffSymbolStamping {
	
	/** The chord this articulation belongs to. */
	public ChordNotation chord;

	public ArticulationStamping(ChordNotation chord, ArticulationType articulation, StaffStamping parentStaff,
		SP position, float scaling, SymbolPool symbolPool) {
		super(parentStaff, symbolPool.getSymbol(CommonSymbol.getArticulation(articulation)),
			null, position, scaling, false);
		this.chord = chord;
	}

}
