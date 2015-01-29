package com.xenoage.zong.musiclayout.notator.accidentals;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.xenoage.zong.musiclayout.notations.chord.AccidentalsDisplacement;

/**
 * Displacement for a chord with 1 accidental.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class OneAccidental
	extends Strategy {
	
	@Getter private final int accsCount = 1;
	
	
	@Override AccidentalsDisplacement compute() {
		float x = 0;
		float width = chordWidths.get(accs[0]) + chordWidths.accToNoteGap;
		return create(width, x);
	}

}
