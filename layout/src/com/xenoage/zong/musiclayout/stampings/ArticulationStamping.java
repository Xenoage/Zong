package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.format.SP;

/**
 * Stamping of an articulation.
 *
 * @author Andreas Wenger
 */
@Const public final class ArticulationStamping
	extends StaffSymbolStamping {

	public ArticulationStamping(Chord chord, ArticulationType articulation, StaffStamping parentStaff,
		SP position, float scaling, SymbolPool symbolPool) {
		super(parentStaff, chord, symbolPool.getSymbol(CommonSymbol.getArticulation(articulation)),
			null, position, scaling, false);
	}

}
