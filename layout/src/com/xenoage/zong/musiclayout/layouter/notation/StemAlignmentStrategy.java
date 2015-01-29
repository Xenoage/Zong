package com.xenoage.zong.musiclayout.layouter.notation;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.None;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;

import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.ChordDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;

/**
 * A {@link StemAlignmentStrategy} computes the
 * length of single chord stems.
 * 
 * If there is a fixed stem length, it is used, otherwise
 * a good value is computed.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class StemAlignmentStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Computes the vertical position of the stem of the given chord layout.
	 * @param stem        the stem of the chord, or null for default stem
	 * @param na          the positions of the notes of of the chord
	 * @param sd          the direction of the stem
	 * @param linesCount  the number of lines in this staff
	 * @param scaling     scaling of the whole chord (e.g. smaller than 1 for a grace chord).
	 *                    ignored if the stem has a given fixed length
	 * @return  the vertical position of the stem, or {@link StemAlignment#none} if the chord has no stem.
	 */
	public StemAlignment computeStemAlignment(Stem stem, ChordDisplacement na, StemDirection sd,
		int linesCount, float scaling) {
		NoteDisplacement high = na.getNote(na.getNotesCount() - 1);
		NoteDisplacement low = na.getNote(0);
		float startlineposition = 0;
		float endlineposition = 0;
		int middlelineposition = linesCount - 1;

		//use a stem?
		if (sd == None) {
			return StemAlignment.none;
		}

		//compute start position
		if (sd == Down) {
			startlineposition = high.lp;
		}
		else if (sd == Up) {
			startlineposition = low.lp;
		}

		//compute end position
		if (stem != null && stem.getLength() != null) {
			//used fixed length
			if (sd == Down) {
				endlineposition = low.lp - 2 * stem.getLength();
			}
			else if (sd == Up) {
				endlineposition = high.lp + 2 * stem.getLength();
			}
		}
		else {
			//compute length
			if (sd == Down) {
				endlineposition = low.lp - 7 * scaling; //TODO: put 7 into LayoutSettings
				if (endlineposition > middlelineposition) {
					endlineposition = middlelineposition;
				}
			}
			else if (sd == Up) {
				startlineposition = low.lp;
				endlineposition = high.lp + 7 * scaling;
				if (endlineposition < middlelineposition) {
					endlineposition = middlelineposition;
				}
			}
		}

		StemAlignment stemAlignment = new StemAlignment(startlineposition, endlineposition);
		return stemAlignment;
	}

}
