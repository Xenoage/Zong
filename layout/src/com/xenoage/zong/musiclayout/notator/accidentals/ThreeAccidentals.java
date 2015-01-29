package com.xenoage.zong.musiclayout.notator.accidentals;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.notations.chord.NoteSuspension.Right;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsDisplacement;

/**
 * Displacement for a chord with 3 accidentals.
 * 
 * The 6 rules are adepted from Ross, page 132 f.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class ThreeAccidentals
	extends Strategy {
	
	@Getter private final int accsCount = 3;
	
	
	@Override AccidentalsDisplacement compute() {
		float width, xBottom, xMiddle, xTop;
		//interval of at least a seventh?
		if (accsNote[2].lp - accsNote[0].lp >= 6) {
			//interval of at least a seventh. can be rule 1, 3 or 4
			if (accsNote[2].suspension == Right) {
				//top note is suspended on the right side of the stem.
				//this is rule 4. (same code as rule 1)
				float middleWidth = chordWidths.get(accs[1]);
				xBottom = xTop = middleWidth + chordWidths.accToAccGap;
				xMiddle = 0;
				width = middleWidth + chordWidths.accToAccGap + chordWidths.getMaxWidth(accs[0], accs[2]) +
					chordWidths.accToNoteGap;
			}
			else if (accsNote[1].suspension == Right) {
				//middle note is suspended on the right side of the stem.
				//(bottom note is never suspended on the right)
				//this is rule 3.
				accs[0] = new AccidentalDisplacement(notes[bottomNoteIndex].lp, 0f, atBottom);
				float bottomWidth = chordWidths.get(atBottom);
				accs[2] = new AccidentalDisplacement(notes[topNoteIndex].lp, bottomWidth +
					chordWidths.accToAccGap, atTop);
				float topWidth = chordWidths.get(atTop);
				accs[1] = new AccidentalDisplacement(notes[middleNoteIndex].lp, bottomWidth +
					chordWidths.accToAccGap + topWidth + chordWidths.accToAccGap, atMiddle);
				width = bottomWidth + chordWidths.accToAccGap + topWidth + chordWidths.accToAccGap +
					chordWidths.get(atMiddle) + chordWidths.accToNoteGap;
			}
			else {
				//there are no accidental notes suspended on the right side of the stem.
				//this is rule 1.
				accs[1] = new AccidentalDisplacement(notes[middleNoteIndex].lp, 0f, atMiddle);
				float middleWidth = chordWidths.get(atMiddle);
				accs[0] = new AccidentalDisplacement(notes[bottomNoteIndex].lp, middleWidth +
					chordWidths.accToAccGap, atBottom);
				accs[2] = new AccidentalDisplacement(notes[topNoteIndex].lp, middleWidth +
					chordWidths.accToAccGap, atTop);
				width = middleWidth + chordWidths.accToAccGap + chordWidths.getMaxWidth(atBottom, atTop) +
					chordWidths.accToNoteGap;
			}
		}
		else {
			//interval of less than a seventh. can be rule 2, 5 or 6
			if (notes[topNoteIndex].suspension == Right) {
				//top note is suspended on the right side of the stem.
				//this is rule 5. (same code as rule 2)
				accs[1] = new AccidentalDisplacement(notes[middleNoteIndex].lp, 0f, atMiddle);
				float middleWidth = chordWidths.get(atMiddle);
				accs[0] = new AccidentalDisplacement(notes[bottomNoteIndex].lp, middleWidth +
					chordWidths.accToAccGap, atBottom);
				float bottomWidth = chordWidths.get(atBottom);
				accs[2] = new AccidentalDisplacement(notes[topNoteIndex].lp, middleWidth +
					chordWidths.accToAccGap + bottomWidth + chordWidths.accToAccGap, atTop);
				float topWidth = chordWidths.get(atTop);
				width = middleWidth + chordWidths.accToAccGap + bottomWidth + chordWidths.accToAccGap +
					topWidth + chordWidths.accToNoteGap;
			}
			else if (notes[middleNoteIndex].suspension == Right) {
				//middle note is suspended on the right side of the stem.
				//(bottom note is never suspended on the right)
				//this is rule 6. (same code as rule 3)
				accs[0] = new AccidentalDisplacement(notes[bottomNoteIndex].lp, 0f, atBottom);
				float bottomWidth = chordWidths.get(atBottom);
				accs[2] = new AccidentalDisplacement(notes[topNoteIndex].lp, bottomWidth +
					chordWidths.accToAccGap, atTop);
				float topWidth = chordWidths.get(atTop);
				accs[1] = new AccidentalDisplacement(notes[middleNoteIndex].lp, bottomWidth +
					chordWidths.accToAccGap + topWidth + chordWidths.accToAccGap, atMiddle);
				width = bottomWidth + chordWidths.accToAccGap + topWidth + chordWidths.accToAccGap +
					chordWidths.get(atMiddle) + chordWidths.accToNoteGap;
			}
			else {
				//there are no accidental notes suspended on the right side of the stem.
				//this is rule 2.
				accs[1] = new AccidentalDisplacement(notes[middleNoteIndex].lp, 0f, atMiddle);
				float middleWidth = chordWidths.get(atMiddle);
				accs[0] = new AccidentalDisplacement(notes[bottomNoteIndex].lp, middleWidth +
					chordWidths.accToAccGap, atBottom);
				float bottomWidth = chordWidths.get(atBottom);
				accs[2] = new AccidentalDisplacement(notes[topNoteIndex].lp, middleWidth +
					chordWidths.accToAccGap + bottomWidth + chordWidths.accToAccGap, atTop);
				float topWidth = chordWidths.get(atTop);
				width = middleWidth + chordWidths.accToAccGap + bottomWidth + chordWidths.accToAccGap +
					topWidth + chordWidths.accToNoteGap;
			}
		}
		return new AccidentalsDisplacement(accs, width);
	}
	
	int getAccNoteIndex(int startIndex) {
		for (int i : range(startIndex, pitches.size() - 1))
			if (mc.getAccidental(pitches.get(i)) != null)
				return i;
		throw new IllegalStateException();
	}

}
