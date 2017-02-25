package com.xenoage.zong.musiclayout.notator.chord.stem;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.chord.ChordLps;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;

import static com.xenoage.zong.core.music.chord.StemDirection.*;
import static com.xenoage.zong.musiclayout.notator.chord.stem.StemDrawer.stemDrawer;

/**
 * Computes the {@link StemNotation} of a chord stem.
 * 
 * If there is a fixed stem length, it is used, otherwise a good value is computed.
 * Each chord is handled separately. Beams are not handled by this class, i.e. the
 * length of the stem is computed as if there was no beam.
 * 
 * @author Andreas Wenger
 */
public class StemNotator {
	
	public static final StemNotator stemNotator = new StemNotator();
	

	/**
	 * Computes the vertical position of the stem of the given chord layout.
	 * @param stem        the stem of the chord (direction is ignored)
	 * @param notesLp     the positions of the notes of of the chord
	 * @param stemDir     the direction of the stem
	 * @param staffLines  the number of lines in this staff
	 * @param scaling     scaling of the whole chord (e.g. smaller than 1 for a grace chord).
	 *                    ignored if the stem has a given fixed length
	 * @return  the vertical position of the stem, or null if the chord has no stem.
	 */
	@MaybeNull public StemNotation compute(Stem stem, ChordLps notesLp, StemDirection stemDir,
		StaffLines staffLines, float scaling) {
		float startLp = 0, innerLp = 0, endLp;

		//use a stem?
		if (stemDir == None)
			return null;

		//compute start position
		if (stemDir == Down) {
			startLp = notesLp.getTop();
			innerLp = notesLp.getBottom();
		}
		else if (stemDir == Up) {
			startLp = notesLp.getBottom();
			innerLp = notesLp.getTop();
		}

		//compute end position
		float stemLengthIs; 
		if (stem.getLengthIs() != null) {
			//used fixed length
			stemLengthIs = stem.getLengthIs();
		}
		else {
			//compute length
			stemLengthIs = stemDrawer.getPreferredStemLengthIs(notesLp, stemDir, staffLines);
		}
		endLp = innerLp + stemDir.getSign() * stemLengthIs * 2;

		return new StemNotation(startLp, endLp);
	}

}
