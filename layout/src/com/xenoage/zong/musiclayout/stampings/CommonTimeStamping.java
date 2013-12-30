package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Class for a C time signature stamping.
 *
 * @author Andreas Wenger
 */
@Const public class CommonTimeStamping
	extends StaffSymbolStamping {

	public CommonTimeStamping(Time commonTime, float positionX, StaffStamping parentStaff,
		SymbolPool symbolPool) {
		super(parentStaff, commonTime, symbolPool.getSymbol(CommonSymbol.TimeCommon), null, sp(
			positionX, parentStaff.linesCount - 1), 1f, false);
	}

}
