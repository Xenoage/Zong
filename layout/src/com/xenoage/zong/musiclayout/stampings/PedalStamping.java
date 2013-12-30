package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;
import com.xenoage.zong.core.music.direction.Pedal;
import com.xenoage.zong.core.music.format.SP;

/**
 * Stamping of a pedal symbol.
 *
 * @author Andreas Wenger
 */
@Const public final class PedalStamping
	extends StaffSymbolStamping {

	public PedalStamping(Pedal pedal, StaffStamping parentStaff, SP position, float scaling,
		SymbolPool symbolPool) {
		super(parentStaff, pedal, symbolPool.getSymbol(CommonSymbol.getPedal(pedal.getType())), null,
			position, scaling, false);
	}

}
