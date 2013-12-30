package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Class for an notehead stamping.
 *
 * @author Andreas Wenger
 */
@Const public final class NoteheadStamping
	extends StaffSymbolStamping {

	public static final int NOTEHEAD_WHOLE = 0;
	public static final int NOTEHEAD_HALF = 1;
	public static final int NOTEHEAD_QUARTER = 2;

	public static final int SIDE_LEFT = 0;
	public static final int SIDE_CENTER = 1;
	public static final int SIDE_RIGHT = 2;


	public NoteheadStamping(Chord chord, int notehead, Color color, StaffStamping parentStaff,
		SP position, float scaling, SymbolPool symbolPool) {
		super(parentStaff, chord, getSymbol(notehead, symbolPool), color, sp(
			computePositionX(position.xMm, notehead, scaling, parentStaff, symbolPool), position.lp),
			scaling, false);
	}

	private static Symbol getSymbol(int notehead, SymbolPool symbolPool) {
		if (notehead == NOTEHEAD_WHOLE)
			return symbolPool.getSymbol(CommonSymbol.NoteWhole);
		else if (notehead == NOTEHEAD_HALF)
			return symbolPool.getSymbol(CommonSymbol.NoteHalf);
		else
			return symbolPool.getSymbol(CommonSymbol.NoteQuarter);
	}

	private static float computePositionX(float positionX, int notehead, float scaling,
		StaffStamping staff, SymbolPool symbolPool) {
		float ret = positionX;
		Symbol symbol = getSymbol(notehead, symbolPool);
		Rectangle2f bounds;
		if (symbol != null)
			bounds = symbol.getBoundingRect().scale(scaling);
		else
			bounds = new Rectangle2f(0, 0, 0, 0);
		float interlineSpace = staff.is;
		float lineWidth = staff.getLineWidth();
		ret += (bounds.size.width / 2) * interlineSpace - lineWidth / 2;
		return ret;
	}

}
