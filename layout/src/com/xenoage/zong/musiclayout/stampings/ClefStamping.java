package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;
import com.xenoage.zong.core.music.clef.Clef;

/**
 * Class for a clef stamping.
 *
 * @author Andreas Wenger
 */
@Const public final class ClefStamping
	extends StaffSymbolStamping {

	public ClefStamping(Clef clef, StaffStamping parentStaff, float xPosition, float scaling,
		SymbolPool symbolPool) {
		super(parentStaff, clef,
			symbolPool.getSymbol(CommonSymbol.getClef(clef.getType().getSymbol())), null,
			sp(xPosition, clef.getType().getLp()), scaling, false);
	}

}
