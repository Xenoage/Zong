package com.xenoage.zong.musiclayout.notator.chord.stem;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.None;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.notator.chord.stem.StemDrawer.stemDrawer;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.chord.ChordLps;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;

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
	 * @param staffLines  the number of lines in this staff
	 * @param scaling     scaling of the whole chord (e.g. smaller than 1 for a grace chord).
	 *                    ignored if the stem has a given fixed length
	 * @return  the vertical position of the stem, or {@link StemNotation#none} if the chord has no stem.
	 */
	@MaybeNull public StemNotation compute(Stem stem, ChordLps notesLp, StemDirection stemDir,
		StaffLines staffLines, float scaling) {
		float startLp = 0;
		float endLp = 0;

		//use a stem?
		if (stemDir == None)
			return null;

		//compute start position
		if (stemDir == Down) 
			startLp = notesLp.getTop();
		else if (stemDir == Up)
			startLp = notesLp.getBottom();

		//compute end position
		if (stem.getLengthIs() != null) {
			//used fixed length
			endLp = startLp + stemDir.getSign() * stem.getLengthIs() * 2;
		}
		else {
			//compute length
			endLp = startLp + stemDrawer.getPreferredStemLengthIs(notesLp, stemDir, staffLines);
		}

		return new StemNotation(startLp, endLp);
	}

}
