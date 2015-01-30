package com.xenoage.zong.musiclayout.notator.accidentals;

import com.xenoage.zong.musiclayout.notations.chord.AccidentalsDisplacement;

/**
 * Displacement for a chord with any number of accidentals.
 * 
 * Currently this strategy returns no good layout,
 * since all accidentals are within a single column.
 * 
 * @author Andreas Wenger
 */
public class ManyAccidentals
	extends Strategy {
	
	public static final ManyAccidentals manyAccidentals = new ManyAccidentals();
	
	
	@Override AccidentalsDisplacement compute(Params p) {
		float width = p.chordWidths.getMaxWidth(p.accs) + p.chordWidths.accToNoteGap;
		float[] xs = new float[p.accs.length]; //auto-init with 0f
		return create(p, width, xs);
	}

}
