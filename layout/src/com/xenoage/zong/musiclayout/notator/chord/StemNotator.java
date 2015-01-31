package com.xenoage.zong.musiclayout.notator.chord;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.None;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;

import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;

/**
 * Computes the {@link StemNotation} of a chord stem.
 * 
 * If there is a fixed stem length, it is used, otherwise a good value is computed.
 * Each chord is handled separately. Beams are not handled by this class.
 * 
 * @author Andreas Wenger
 */
public class StemNotator {
	
	public static final StemNotator stemNotator = new StemNotator();
	

	/**
	 * Computes the vertical position of the stem of the given chord layout.
	 * @param stem        the stem of the chord (direction is ignored)
	 * @param notes       the positions of the notes of of the chord
	 * @param stemDir     the direction of the stem
	 * @param linesCount  the number of lines in this staff
	 * @param scaling     scaling of the whole chord (e.g. smaller than 1 for a grace chord).
	 *                    ignored if the stem has a given fixed length
	 * @return  the vertical position of the stem, or {@link StemNotation#none} if the chord has no stem.
	 */
	public StemNotation compute(Stem stem, NotesNotation notes, StemDirection stemDir,
		int linesCount, float scaling) {
		NoteDisplacement topNote = notes.getTopNote();
		NoteDisplacement bottomNote = notes.getBottomNote();
		float startLp = 0;
		float endLp = 0;
		int staffMiddleLp = linesCount - 1;

		//use a stem?
		if (stemDir == None)
			return StemNotation.none;

		//compute start position
		if (stemDir == Down)
			startLp = topNote.yLp;
		else if (stemDir == Up)
			startLp = bottomNote.yLp;

		//compute end position
		if (stem.getLength() != null) {
			//used fixed length
			if (stemDir == Down)
				endLp = bottomNote.yLp - 2 * stem.getLength();
			else if (stemDir == Up)
				endLp = topNote.yLp + 2 * stem.getLength();
		}
		else {
			//compute length
			if (stemDir == Down) {
				endLp = bottomNote.yLp - 7 * scaling; //TODO: put 7 into LayoutSettings
				if (endLp > staffMiddleLp)
					endLp = staffMiddleLp;
			}
			else if (stemDir == Up) {
				startLp = bottomNote.yLp;
				endLp = topNote.yLp + 7 * scaling;
				if (endLp < staffMiddleLp)
					endLp = staffMiddleLp;
			}
		}

		return new StemNotation(startLp, endLp);
	}

}
