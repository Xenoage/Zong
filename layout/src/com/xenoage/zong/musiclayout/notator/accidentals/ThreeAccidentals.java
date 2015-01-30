package com.xenoage.zong.musiclayout.notator.accidentals;

import static com.xenoage.zong.musiclayout.notations.chord.NoteSuspension.Right;

import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;

/**
 * Displacement for a chord with 3 accidentals.
 * 
 * The 6 rules are adepted from Ross, page 132 f.
 * 
 * @author Andreas Wenger
 */
public class ThreeAccidentals
	extends Strategy {
	
	public static final ThreeAccidentals threeAccidentals = new ThreeAccidentals();
	
	
	@Override AccidentalsNotation compute(Params p) {
		float width, xBottom, xMiddle, xTop;
		NoteDisplacement bottomNote = p.accsNote[0];
		NoteDisplacement middleNote = p.accsNote[1];
		NoteDisplacement topNote = p.accsNote[2];
		Accidental bottomAcc = p.accs[0];
		Accidental middleAcc = p.accs[1];
		Accidental topAcc = p.accs[2];
		float bottomWidth = p.chordWidths.get(bottomAcc);
		float middleWidth = p.chordWidths.get(middleAcc);
		float topWidth = p.chordWidths.get(topAcc);
		float accToAccGap = p.chordWidths.accToAccGap;
		float accToNoteGap = p.chordWidths.accToNoteGap;
		//interval of at least a seventh?
		if (topNote.yLp - bottomNote.yLp >= 6) {
			//interval of at least a seventh. can be rule 1, 3 or 4
			if (topNote.suspension == Right) {
				//top note is suspended on the right side of the stem.
				//this is rule 4. (same code as rule 1)
				xBottom = xTop = middleWidth + accToAccGap;
				xMiddle = 0;
				width = xBottom + p.chordWidths.getMaxWidth(bottomAcc, topAcc) + accToNoteGap;
			}
			else if (middleNote.suspension == Right) {
				//middle note is suspended on the right side of the stem.
				//(bottom note is never suspended on the right)
				//this is rule 3.
				xBottom = 0;
				xMiddle = bottomWidth + accToAccGap + topWidth + accToAccGap;
				xTop = bottomWidth + accToAccGap;
				width = xMiddle + middleWidth + accToNoteGap;
			}
			else {
				//there are no accidental notes suspended on the right side of the stem.
				//this is rule 1.
				xBottom = xTop = middleWidth + accToAccGap;
				xMiddle = 0f;
				width = xBottom + p.chordWidths.getMaxWidth(bottomAcc, topAcc) + accToNoteGap;
			}
		}
		else {
			//interval of less than a seventh. can be rule 2, 5 or 6
			if (topNote.suspension == Right) {
				//top note is suspended on the right side of the stem.
				//this is rule 5. (same code as rule 2)
				xBottom = middleWidth + accToAccGap;
				xMiddle = 0f;
				xTop = xBottom + bottomWidth + accToAccGap;
				width = xTop + topWidth + accToNoteGap;
			}
			else if (middleNote.suspension == Right) {
				//middle note is suspended on the right side of the stem.
				//(bottom note is never suspended on the right)
				//this is rule 6. (same code as rule 3)
				xBottom = 0f;
				xMiddle = bottomWidth + accToAccGap + topWidth + accToAccGap;
				xTop = bottomWidth + accToAccGap;
				width = xMiddle + middleWidth + accToNoteGap;
			}
			else {
				//there are no accidental notes suspended on the right side of the stem.
				//this is rule 2.
				xBottom = middleWidth + accToAccGap;
				xMiddle = 0f;
				xTop = middleWidth + accToAccGap + bottomWidth + accToAccGap;
				width = xTop + topWidth + accToNoteGap;
			}
		}
		return create(p, width, xBottom, xMiddle, xTop);
	}

}
