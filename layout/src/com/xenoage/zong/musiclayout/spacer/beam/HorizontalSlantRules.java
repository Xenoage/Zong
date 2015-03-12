package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.ArrayUtils.getFirst;
import static com.xenoage.utils.collections.ArrayUtils.getLast;
import static com.xenoage.utils.collections.ArrayUtils.max;
import static com.xenoage.utils.collections.ArrayUtils.min;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static java.lang.Math.max;
import static java.lang.Math.min;
import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Rules for beam slanting, when a horizontal beam has to be used.
 * 
 * The rules are based on Ross, p. 115 ff.
 * All notes have to be on the same staff.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor public class HorizontalSlantRules {

	private int[] notesLp;
	private StemDirection stemDir;


	/**
	 * Returns true, iff a horizontal beam should be created for the
	 * beamed chords with the given inner notes and stem direction.
	 */
	private boolean isHorizontal() {
		int chordsCount = notesLp.length;
		
		return (isHorizontal() || )
		
		

		//if all middlenotes are further away from the beam than a direct line from
		//the first to the last note, a normal slant is used
		boolean useDefaultSlant = true;
		for (int i = 1; i < chordsCount - 2; i++) {
			float lp = leftInnerNoteLp + 1f * (rightInnerNoteLp - leftInnerNoteLp) * i /
				(chordsCount - 1);
			if ((stemDirection == Up && innerNoteLps[i] > Math.ceil(lp)) ||
				(stemDirection == Down && innerNoteLps[i] < Math.floor(lp))) {
				useDefaultSlant = false;
				break;
			}
		}
		if (useDefaultSlant) {
			//use default rules (Ted Ross page 111)
			float lengthX = stemX[chordsCount - 1] - stemX[0];
			return computeNormalBeamSlant(beamDesign, leftInnerNoteLp, rightInnerNoteLp, lengthX,
				staffLinesCount);
		}

		//rule Ted Ross page 97 bottom
		//When the first and last notes are on different staff degrees, and all inside notes
		//descend to the last note, the beam slants one-half space in the direction
		//of the run of the inside notes.
		if (leftInnerNoteLp != rightInnerNoteLp) {
			boolean ascend = true;
			for (int i = 2; i < chordsCount; i++) {
				if (innerNoteLps[i - 1] >= innerNoteLps[i]) {
					ascend = false;
					break;
				}
			}
			if (ascend) {
				return beamDesign.getSlantAscendingMiddleNotesIs();
			}
			boolean descend = true;
			for (int i = 1; i < chordsCount - 1; i++) {
				if (innerNoteLps[i - 1] <= innerNoteLps[i]) {
					descend = false;
					break;
				}
			}
			if (descend) {
				return beamDesign.getSlantDescendingMiddleNotesIs();
			}
		}

		//otherwise: horizontal beam
		return 0;
	}

	
	
	/**
	 * Ross, p. 115, row 4: use horizontal beam, when the notes outline the
	 * same interval, e.g. a e a e [a e]*
	 */
	boolean sameInterval() {
		int chordsCount = notesLp.length;
		if (chordsCount >= 4 && chordsCount % 2 == 0) {
			for (int i : range(2, chordsCount - 1)) {
				if (notesLp[i % 2] != notesLp[i])
					return false;
			}
			return true;
		}
		return false;
	}

}
