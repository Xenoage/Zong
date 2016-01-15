package com.xenoage.zong.musiclayout.stampings;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.symbols.Symbol;

/**
 * Class for a prolongation dot stamping.
 * 
 * Prolongation dots belong to a staff. They have
 * a horizontal position and a line position
 * around which they are centered.
 * They are 0.3 spaces wide.
 *
 * @author Andreas Wenger
 */
@Const @Getter
public class ProlongationDotStamping
	extends StaffSymbolStamping {

	public ProlongationDotStamping(ChordNotation chord, StaffStamping parentStaff, Symbol symbol, SP position) {
		super(chord, parentStaff, symbol, null, position, 1, false);
	}

}
