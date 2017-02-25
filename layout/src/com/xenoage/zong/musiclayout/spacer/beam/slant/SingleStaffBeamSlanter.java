package com.xenoage.zong.musiclayout.spacer.beam.slant;

import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.spacer.beam.Direction;
import com.xenoage.zong.musiclayout.spacer.beam.Slant;
import com.xenoage.zong.musiclayout.spacer.beam.stem.BeamedStems;
import lombok.val;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.spacer.beam.Direction.Ascending;
import static com.xenoage.zong.musiclayout.spacer.beam.Direction.Descending;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.*;
import static java.lang.Math.abs;

/**
 * Computes the {@link Slant} of a beam within a single staff.
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
 * @author Andreas Wenger
 */
public class SingleStaffBeamSlanter {

	public static SingleStaffBeamSlanter singleStaffBeamSlanter = new SingleStaffBeamSlanter();


	public Slant compute(BeamedStems stems, int staffLines) {
		//Ross, p. 115, row 1: use horizontal beam, if first and last note is on the same LP
		if (stems.leftNoteLp == stems.rightNoteLp)
			return horizontalSlant;
		//unification of the rules in Ross, p. 115-117:
		//a horizontal beam may be correct if all middle notes are lower/higher or equal than
		//outer notes for a downstem/upstem beam
		val stemDir = stems.getFirst().dir;
		if (containsMiddleExtremeNote(stems, stemDir)) {
			//there are some exceptions, listed in Ross, p. 97
			//Ross, p. 97, row 3: 3 notes with middle note equal to outer note: normal slant
			if (is3NotesMiddleEqualsOuter(stems))
				return computeNormal(stems.leftNoteLp, stems.rightNoteLp);
			//Ross, p. 97, row 4: 4 notes in special constellation: half space slant
			int rossSpecialDir = get4NotesRossSpecialDir(stems, stemDir);
			if (rossSpecialDir != 0)
				return slant(rossSpecialDir * 0.5f);
			//Ross, p. 97, rows 5 and 6: inner run with half space slant
			int innerRunDir = getInnerRunDir(stems);
			if (innerRunDir != 0)
				return slant(innerRunDir * 0.5f);
			//no exception. horizontal is correct.
			return horizontalSlant;
		}
		//otherwise, compute slant dependent on the horizontal spacing
		Slant slant;
		if (isCloseSpacing(stems))
			slant = computeClose(stems, stemDir);
		else
			slant = computeNormal(stems.leftNoteLp, stems.rightNoteLp);
		//limit slant
		slant = limitSlantForExtremeNotes(slant, stems, stemDir, staffLines);
		return slant;
	}
	
	/**
	 * A special rule from Ross, p. 97, row 4.
	 * Returns the direction (1: up, -1: down) of this pattern
	 * if it can be found, otherwise 0.
	 */
	int get4NotesRossSpecialDir(BeamedStems stems, StemDirection stemDir) {
		if (stems.getCount() != 4)
			return 0;
		//first note must be like second one, or third like last
		boolean firstEqual;
		if (stems.leftNoteLp == stems.get(1).noteLp)
			firstEqual = true;
		else if (stems.get(2).noteLp == stems.get(3).noteLp)
			firstEqual = false;
		else
			return 0;
		//remaining note must be like its outer neighbor, but 1 LP further out
		int outerLp = stems.get(firstEqual ? 3 : 0).noteLp;
		int innerLp = stems.get(firstEqual ? 2 : 1).noteLp;
		if (outerLp == innerLp + 1 * stemDir.getSign())
			return (stems.leftNoteLp > stems.rightNoteLp ? -1 : 1); //overall direction
		return 0;
	}
	
	/**
	 * If the beam has at least 6 notes and the notes ascend or descend
	 * from the first to the second last note or from the second to the last note,
	 * we have an "inner run" (see Ross p. 97).
	 * The direction of the run is returned (1: up, -1: down, 0: no run found).
	 * It seems that 4 notes are not enough for this rule, otherwise for
	 * example p. 117 row 2 and row 7 col 1 would qualify, too.
	 */
	int getInnerRunDir(BeamedStems stems) {
		if (stems.getCount() < 6)
			return 0;
		//try both directions
		for (int dir : new int[]{-1, 1}) {
			boolean foundRun = true;
			boolean exceptFirst = false;
			for (int i : range(stems.getCount() - 1)) {
				if (dir * stems.get(i).noteLp >= dir * stems.get(i + 1).noteLp) {
					//break in run. allowed between first and second note,
					//and between second last and last note (but not both!)
					if (i == 0) {
						//break between first and second note
						exceptFirst = true;
					}
					else if (i == stems.getCount() - 2 && !exceptFirst) {
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
	 * Returns true, iff the given beam is very crowded on the x-axis
	 * and requires close spacing.
	 */
	boolean isCloseSpacing(BeamedStems stems) {
		//Ross, p. 100 and p. 112:
		//we use close spacing, if the distance is less than 3 or 4 spaces
		//we use the average value, 3.5, and have a look at the average stem distance
		float avgDistanceIs = (stems.rightXIs - stems.leftXIs) / (stems.getCount() - 1);
		return avgDistanceIs < 3.5;
	}
	
	/**
	 * Computes the slant for closely spaced beams.
	 */
	Slant computeClose(BeamedStems stems, StemDirection stemDir) {
		//Ross, p. 112: beams in close spacing slant only 1/4 to 1/2 space
		int dictatorLp = (stemDir == Up ? stems.getMaxNoteLp() : stems.getMinNoteLp());
		Direction dir = (stems.rightNoteLp > stems.leftNoteLp ? Ascending : Descending);
		//if dictator is on a staff line, use slant of 1/4 space
		if (dictatorLp % 2 == 0 || abs(stems.rightNoteLp - stems.leftNoteLp) <= 1)
			//on staff (Ross p. 112) or 2nd interval (Ross p. 111)
			return slantIs(0.25f, dir);
		else
			return slantIs(0.5f, dir);
	}

	/**
	 * Computes the slant for beams with normal horizontal spacing.
	 */
	public Slant computeNormal(int firstNoteLp, int lastNoteLp) {
		//Ross, p. 111 (and p. 101)
		int interval = abs(firstNoteLp - lastNoteLp);
		Direction dir = (lastNoteLp > firstNoteLp ? Ascending : Descending);
		
		//Ross' rules for 4th are inconsistent p. 101d min=0.5 and p. 111:
		//101d min=0.5 seems to be a special case, when an extreme note requires its stem to
		//reach the middle line. then, we could use the smaller slant of 0.5 IS instead of the
		//minimal slant of 1 IS which is described on page 111 4th.
		//we already tried to apply this rule, but the tests with lots of examples
		//showed no real improvements, so we ignore it and assume a minimum of 0.5 IS for a 4th slant
		
		switch (interval) {
			case 0: return horizontalSlant; //unison
			case 1: return slantIs(0.25f, dir); //2nd
			case 2: return slantIs(0.5f, 1, dir); //3rd
			case 3: return slantIs(0.5f, 1.25f, dir); //4th
			case 4: return slantIs(1.25f, dir); //5th
			case 5: return slantIs(1.25f, 1.5f, dir); //6th
			case 6: return slantIs(1.25f, 1.75f, dir); //7th
			default: return slantIs(1.25f, 2, dir); //8th or higher
		}
	}
	
	/**
	 * Ross, p. 111: Limit the slant to 0.5 IS for very high and very low notes.
	 */
	Slant limitSlantForExtremeNotes(Slant slant, BeamedStems stems,
		StemDirection stemDir, int staffLines) {
		int maxSlantAbsQs = 2; //2 QS = 0.5 IS
		if (slant.maxAbsQs > maxSlantAbsQs) {
			//Ross, p. 111, row 1 and 2: upstem and only notes below bottom leger lines
			//or downstem and only notes above top leger lines
			int bottomLegerLp = -2;
			int topLegerLp = staffLines * 2;
			if ((stemDir == Up && stems.getMaxNoteLp() < bottomLegerLp) ||
				(stemDir == Down && stems.getMinNoteLp() > topLegerLp)) {
				slant = slant.limitQs(maxSlantAbsQs);
			}
		}
		return slant;
	}

	/**
	 * Returns true, if at least one of the middle notes is higher/lower than or equal
	 * to the left and right note, when the given stem direction is up/down.
	 */
	boolean containsMiddleExtremeNote(BeamedStems stems, StemDirection stemDir) {
		if (stemDir == Up) {
			int outerMax = Math.max(stems.leftNoteLp, stems.rightNoteLp);
			for (int i : range(1, stems.getCount() - 2))
				if (stems.get(i).noteLp >= outerMax)
					return true;
		}
		else if (stemDir == Down) {
			int outerMin = Math.min(stems.leftNoteLp, stems.rightNoteLp);
			for (int i : range(1, stems.getCount() - 2))
				if (stems.get(i).noteLp <= outerMin)
					return true;
		}
		return false;
	}

	/**
	 * Returns true, iff the beam has 3 stems and the LP of the middle note is
	 * the same as the LP of the left or right note.
	 */
	boolean is3NotesMiddleEqualsOuter(BeamedStems stems) {
		return (stems.getCount() == 3 &&
				(stems.leftNoteLp == stems.get(1).noteLp || stems.get(1).noteLp == stems.rightNoteLp));
	}
	
}
