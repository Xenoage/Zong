package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.ArrayUtils.getFirst;
import static com.xenoage.utils.collections.ArrayUtils.getLast;
import static com.xenoage.utils.collections.ArrayUtils.max;
import static com.xenoage.utils.collections.ArrayUtils.min;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.horizontalSlant;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.slant;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Computes the {@link Slant} of a beam.
 * 
 * The rules are adopted from Ross, p. 97-118, and Chlapik, p. 41-43.
 * 
 * On page 115 ff., Ross names a lot of rules when beams have to be horizontal.
 * It seems as if they can be unified to two rules:
 * <ol>
 *   <li>A beam is horizontal, if the first and last note are on the same LP.</li>
 *   <li>An upstem/downstem beam with 3 or more chords is horizontal, if at least one of
 *     the middle notes is higher/lower than or equal to the left and right note.
 *     There are some exceptions, listed in Ross, p. 97.</li> 
 * </ol>
 * All other beams receive a non-horizontal slant.
 * 
 * The amount of non-horizontal slants is dependent on
 * <ul>
 *   <li>the interval between the first and last note (see Ross, p. 111) and</li>
 *   <li>the horizontal spacing (close spacing for crowded notes, normal
 *     spacing otherwise, see Ross p. 112 ff.)</li> 
 *   <li>the LP of the notes (Ross, p. 111, rows 1 and 2)</li>
 * </ul>
 * 
 * The slant is flexible
 * 
 * @author Andreas Wenger
 */
public class BeamSlanter {
	
	public static final BeamSlanter beamSlanter = new BeamSlanter();
	
	
	public Slant compute(int[] notesLp, StemDirection stemDir, float[] stemsXIs, int staffLines) {
		//Ross, p. 115, row 1: use horizontal beam, if first and last note is on the same LP
		if (isFirstAndLastNoteEqual(notesLp))
			return horizontalSlant;
		//unification of the rules in Ross, p. 115-117:
		//a horizontal beam may be correct if all middle notes are lower/higher or equal than
		//outer notes for a downstem/upstem beam
		if (containsMiddleExtremum(notesLp, stemDir)) {
			//there are some exceptions, listed in Ross, p. 97
			//Ross, p. 97, row 3: 3 notes with middle note equal to outer note: normal slant
			if (is3NotesMiddleEqualsOuter(notesLp))
				return computeNormal(getFirst(notesLp), getLast(notesLp), stemDir);
			//Ross, p. 97, row 4: 4 notes in special constellation: half space slant
			int rossSpecialDir = get4NotesRossSpecialDir(notesLp, stemDir);
			if (rossSpecialDir != 0)
				return slant(rossSpecialDir * 0.5f);
			//Ross, p. 97, rows 5 and 6: inner run with half space slant
			int innerRunDir = getInnerRunDir(notesLp);
			if (innerRunDir != 0)
				return slant(innerRunDir * 0.5f);
			//no exception. horizontal is correct.
			return horizontalSlant;
		}
		//otherwise, compute slant dependent on the horizontal spacing
		//GOON
		Slant slant =  computeNormal(getFirst(notesLp), getLast(notesLp), stemDir);
		//limit slant
		slant = limitSlantForExtremeNotes(slant, notesLp, stemDir, staffLines);
		return slant;
	}
	
	/**
	 * Returns true, if the LP of the first note equals the LP of the last note.
	 */
	boolean isFirstAndLastNoteEqual(int[] notesLp) {
		return (getFirst(notesLp) == getLast(notesLp));
	}

	/**
	 * Returns true, if at least one of the middle notes is higher/lower than or equal
	 * to the left and right note.
	 */
	boolean containsMiddleExtremum(int[] notesLp, StemDirection stemDir) {
		if (stemDir == Up) {
			int outerMax = max(getFirst(notesLp), getLast(notesLp));
			for (int i : range(1, notesLp.length - 2))
				if (notesLp[i] >= outerMax)
					return true;
		}
		else if (stemDir == Down) {
			int outerMin = min(getFirst(notesLp), getLast(notesLp));
			for (int i : range(1, notesLp.length - 2))
				if (notesLp[i] <= outerMin)
					return true;
		}
		return false;
	}
	
	/**
	 * Returns true, iff the beam has 3 notes and the LP of the middle note is
	 * the same as the LP of the left or right note.
	 */
	boolean is3NotesMiddleEqualsOuter(int[] notesLp) {
		return (notesLp.length == 3 &&
			(notesLp[0] == notesLp[1] || notesLp[1] == notesLp[2]));
	}
	
	/**
	 * A special rule from Ross, p. 97, row 4.
	 * Returns the direction (1: up, -1: down) of this pattern
	 * if it can be found, otherwise 0.
	 */
	int get4NotesRossSpecialDir(int[] notesLp, StemDirection stemDir) {
		if (notesLp.length != 4)
			return 0;
		//first note must be like second one, or third like last
		boolean firstEqual;
		if (notesLp[0] == notesLp[1])
			firstEqual = true;
		else if (notesLp[2] == notesLp[3])
			firstEqual = false;
		else
			return 0;
		//remaining note must be like its outer neighbor, but 1 LP further out
		int outerLp = (firstEqual ? notesLp[3] : notesLp[0]);
		int innerLp = (firstEqual ? notesLp[2] : notesLp[1]);
		if (outerLp == innerLp + 1 * stemDir.getSign())
			return (notesLp[0] > notesLp[3] ? -1 : 1); //overall direction
		return 0;
	}
	
	/**
	 * If the beam has at least 4 notes and the notes ascend or descend from the first to the
	 * second last note or from the second to the last note, we have an "inner run".
	 * The direction of the run is returned (1: up, -1: down, 0: no run found).
	 */
	int getInnerRunDir(int[] notesLp) {
		if (notesLp.length < 4)
			return 0;
		//try both directions
		for (int dir : new int[]{-1, 1}) {
			boolean foundRun = true;
			boolean exceptFirst = false;
			for (int i : range(notesLp.length - 1)) {
				if (dir * notesLp[i] >= dir * notesLp[i + 1]) {
					//break in run. allowed between first and second note,
					//and between second last and last note (but not both!)
					if (i == 0) {
						//break between first and second note
						exceptFirst = true;
					}
					else if (i == notesLp.length - 2 && !exceptFirst) {
						//break between second last and last note (and none before)
					}
					else {
						//break, no matching run found
						foundRun = false;
						break;
					}
				}
			}
			if (foundRun)
				return dir;
		}
		return 0;
	}

	/**
	 * Computes the slant for beams with normal horizontal spacing.
	 */
	Slant computeNormal(int firstNoteLp, int lastNoteLp, StemDirection stemDir) {
		return horizontalSlant;
	}
	
	/**
	 * Ross, p. 111: Limit the slant to 0.5 for very high and very low notes.
	 */
	Slant limitSlantForExtremeNotes(Slant slant, int[] notesLp,
		StemDirection stemDir, int staffLines) {
		float maxSlantAbs = 0.5f;
		if (abs(slant.maxIs) > maxSlantAbs || abs(slant.minIs) > maxSlantAbs) {
			//Ross, p. 111, row 1 and 2: upstem and only notes below bottom leger lines
			//or downstem and only notes above top leger lines
			int bottomLegerLp = -2;
			int topLegerLp = staffLines * 2;
			if ((stemDir == Up && max(notesLp) < bottomLegerLp) ||
				(stemDir == Down && min(notesLp) > topLegerLp)) {
				slant = slant.limit(maxSlantAbs);
			}
		}
		return slant;
	}
	
}
