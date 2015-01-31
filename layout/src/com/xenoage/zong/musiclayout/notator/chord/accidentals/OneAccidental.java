package com.xenoage.zong.musiclayout.notator.chord.accidentals;

import com.xenoage.zong.musiclayout.notations.chord.AccidentalsNotation;

/**
 * Displacement for a chord with 1 accidental.
 * 
 * @author Andreas Wenger
 */
public class OneAccidental
	extends Strategy {
	
	public static final OneAccidental oneAccidental = new OneAccidental();
	
	
	@Override AccidentalsNotation compute(Params p) {
		float x = 0;
		float width = p.chordWidths.get(p.accs[0]) + p.chordWidths.accToNoteGap;
		return create(p, width, x);
	}

}
