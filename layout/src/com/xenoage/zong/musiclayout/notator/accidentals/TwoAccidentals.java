package com.xenoage.zong.musiclayout.notator.accidentals;

import static com.xenoage.zong.musiclayout.notations.chord.NoteSuspension.Right;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.xenoage.zong.musiclayout.notations.chord.AccidentalsDisplacement;

/**
 * Displacement for a chord with 2 accidentals.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class TwoAccidentals
	extends Strategy {
	
	@Getter private final int accsCount = 2;
	
	
	@Override AccidentalsDisplacement compute() {
		float width, xTop, xBottom;
		//interval of at least a seventh?
		if (accsNote[1].lp - accsNote[0].lp >= 6) {
			//placed on the same horizontal position x = 0
			xTop = xBottom = 0;
			width = chordWidths.getMaxWidth(accs) + chordWidths.accToNoteGap;
		}
		else {
			//placed on different horizontal positions
			//normally begin with the bottom accidental
			boolean bottomFirst = true;
			//when the bottom note with an accidental is suspended
			//on the right side of the stem, and the top note with an accidental
			//is not, then the bottom accidental is nearer to the note (see Ross, p. 132)
			if (accsNote[0].suspension == Right && accsNote[1].suspension != Right)
				bottomFirst = false;
			//horizontal position
			if (bottomFirst) {
				//bottom accidental is leftmost
				xBottom = 0;
				xTop = chordWidths.get(accs[1]);
			}
			else {
				//top accidental is leftmost
				xBottom = chordWidths.get(accs[0]);
				xTop = 0;
			}
			//width
			width = chordWidths.get(accs[0]) + chordWidths.accToAccGap + chordWidths.get(accs[1]) +
				chordWidths.accToNoteGap;
		}
		return create(width, xBottom, xTop);
	}

}
