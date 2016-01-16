package com.xenoage.zong.musiclayout.notator.chord.accidentals;

import static com.xenoage.zong.musiclayout.notation.chord.NoteSuspension.Right;

import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.musiclayout.notation.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notation.chord.NoteDisplacement;

/**
 * Displacement for a chord with 2 accidentals.
 * 
 * @author Andreas Wenger
 */
public class TwoAccidentals
	extends Strategy {
	
	public static final TwoAccidentals twoAccidentals = new TwoAccidentals();
	
	
	@Override AccidentalsNotation compute(Params p) {
		float width, xTop, xBottom;
		NoteDisplacement bottomNote = p.accsNote[0];
		NoteDisplacement topNote = p.accsNote[1];
		Accidental bottomAcc = p.accs[0];
		Accidental topAcc = p.accs[1];
		float accToAccGap = p.chordWidths.accToAccGap;
		//interval of at least a seventh?
		if (topNote.lp - bottomNote.lp >= 6) {
			//placed on the same horizontal position x = 0
			xTop = xBottom = 0;
			width = p.chordWidths.getMaxWidth(p.accs) + p.chordWidths.accToNoteGap;
		}
		else {
			//placed on different horizontal positions
			//normally begin with the bottom accidental
			boolean bottomFirst = true;
			//when the bottom note with an accidental is suspended
			//on the right side of the stem, and the top note with an accidental
			//is not, then the bottom accidental is nearer to the note (see Ross, p. 132)
			if (bottomNote.suspension == Right && topNote.suspension != Right)
				bottomFirst = false;
			//horizontal position
			float bottomWidth = p.chordWidths.get(bottomAcc);
			float topWidth = p.chordWidths.get(topAcc);
			if (bottomFirst) {
				//bottom accidental is leftmost
				xBottom = 0;
				xTop = bottomWidth + accToAccGap;
			}
			else {
				//top accidental is leftmost
				xBottom = topWidth + accToAccGap;
				xTop = 0;
			}
			//width
			width = bottomWidth + accToAccGap + topWidth + p.chordWidths.accToNoteGap;
		}
		return create(p, width, xBottom, xTop);
	}

}
