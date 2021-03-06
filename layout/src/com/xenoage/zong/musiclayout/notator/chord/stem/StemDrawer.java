package com.xenoage.zong.musiclayout.notator.chord.stem;

import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.chord.ChordLps;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;

/**
 * Computes the lenght of a stem for a chord.
 * 
 * The values are valid for a stem with no beam or just a single beam line (8th).
 * For beams with more lines, more space may be required in a later step.
 * 
 * @author Andreas Wenger
 */
public class StemDrawer {
	
	public static final StemDrawer stemDrawer = new StemDrawer();
	
	
	/**
	 * Gets the ideal stem length in IS of the chord with the given notes
	 * (ascending LPs) using the given stem direction within a staff with the
	 * given number of lines.
	 */
	public float getPreferredStemLengthIs(ChordLps notesLp, StemDirection stemDir, StaffLines staff) {
		int bottomNoteLp = notesLp.getBottom();
		int topNoteLp = notesLp.getTop();
		//begin with the extreme LPs
		//supports also staves with more or less than 5 lines
		if (isStemExtendedToMiddleLine(notesLp, stemDir, staff)) {
			if (stemDir == Down)
				return (bottomNoteLp - staff.getMiddleLp()) / 2f;
			else
				return (staff.getMiddleLp() - topNoteLp) / 2f;
		}
		else if ((stemDir == Up && topNoteLp > staff.getTopLp() - 4) ||
			(stemDir == Down && bottomNoteLp <= staff.getBottomLp() + 2)) {
			//Ross, p. 86, row 3. Upstem notes above the third last staff line
			//or downstem notes at or below the second staff line are shorter
			//(generalized for staff with more than 5 lines)
			return 2.5f;
		}
		else if ((stemDir == Up && topNoteLp == staff.getTopLp() - 4) ||
			(stemDir == Down && bottomNoteLp == staff.getBottomLp() + 3)) {
			//Ross, p. 86, row 6. Special cases for upstem/downstem note
			//on "3rd last line"/"2nd space"
			//(generalized for staff with more than 5 lines)
			return 3f;
		}
		else {
			//default stem length.
			//Ross, p. 83. Also see footnote, which explains why e.g. Chlapik p. 39, 4,
			//recommends a length of 3 spaces (just a different measurement)
			return 3.5f;
		}
	}
	
	/**
	 * Returns the minimum stem length which still looks artistically correct.
	 */
	public float getMinStemLength() {
		//Ross, p. 83
		return 2.5f;
	}
	
	/**
	 * Returns true, iff the given chord needs a stem which needs to be lengthened to
	 * touch the middle line.
	 */
	public boolean isStemExtendedToMiddleLine(ChordLps notesLp,
		StemDirection stemDir, StaffLines staff) {
		//Ross, p. 86, last row. Also Chlapik p. 39, 5.
		//For downstem/upstem notes, which are above/below the first top/bottom
		//leger line position, lengthen the stem to the middle staff line.
		//Ross starts this rule at the second leger line, while Chlapik starts it
		//from the first leger line. In practice, this makes no difference, since
		//the default length of 3.5 spaces also matches that position.
		return (stemDir == Down && notesLp.getBottom() > staff.getTopLegerLp()) ||
			(stemDir == Up && notesLp.getTop() < staff.getBottomLegerLp());
	}

}
