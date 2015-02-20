package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.color.Color;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.symbols.Symbol;

/**
 * Class for an notehead stamping.
 *
 * @author Andreas Wenger
 */
@Const
public final class NoteheadStamping
	extends StaffSymbolStamping {

	//TIDY: use enum
	public static final int NOTEHEAD_WHOLE = 0;
	public static final int NOTEHEAD_HALF = 1;
	public static final int NOTEHEAD_QUARTER = 2;

	//TIDY: use enum
	public static final int SIDE_LEFT = 0;
	public static final int SIDE_CENTER = 1;
	public static final int SIDE_RIGHT = 2;
	
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
