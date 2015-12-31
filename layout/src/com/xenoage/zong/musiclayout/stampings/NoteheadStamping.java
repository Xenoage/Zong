package com.xenoage.zong.musiclayout.stampings;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.color.Color;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.symbols.Symbol;

/**
 * Class for an notehead stamping.
 *
 * @author Andreas Wenger
 */
@Const @Getter
public final class NoteheadStamping
	extends StaffSymbolStamping {
	
	/** The index of the notehead within the chord. */
	public final int noteheadIndex;


	public NoteheadStamping(ChordNotation chord, int noteheadIndex, Symbol symbol, Color color,
		StaffStamping parentStaff, SP sp, float scaling) {
		super(chord, parentStaff, symbol, color, sp, scaling, false);
		this.noteheadIndex = noteheadIndex;
	}

	public ChordNotation getChord() {
		return (ChordNotation) element;
	}
	
}
