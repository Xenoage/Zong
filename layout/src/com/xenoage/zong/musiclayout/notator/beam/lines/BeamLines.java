package com.xenoage.zong.musiclayout.notator.beam.lines;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Base class for computing the layout of a beam,
 * dependent on the number of beam lines.
 * 
 * This includes the minimum stem length, slants or spacings that make
 * the beam look beautiful.
 * 
 * For each number of beam lines there are different rules,
 * so there is an abstract base class (this one) with different
 * implementations.
 * 
 * TODO: lots of equal or similar code in the subclasses. merge when possible.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
@AllArgsConstructor
public abstract class BeamLines {

	public static final float beamLineHeightIs = 0.5f;

	StemDirection stemDirection;
	int staffLinesCount;


	/**
	 * Gets the minimum length of a stem in IS,
	 * from the outermost note at the stem side up to
	 * the end of the stem.
	 */
	public float getMinimumStemLengthIs() {
		return 3;
	}

	/**
	 * Gets the slant of a beam, when the middle notes of the beam
	 * are sorted descending. The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 */
	public float getSlantDescendingMiddleNotesIs() {
		return -0.5f; //TODO: was -1f before, but I'm not sure if Uli already meant it as IS instead of LP
	}

	/**
	 * Gets the slant of a beam, when the middle notes of the beam
	 * are sorted ascending. The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 */
	public float getSlantAscendingMiddleNotesIs() {
		return 0.5f; //TODO: was 1f before, but I'm not sure if Uli already meant it as IS instead of LP
	}

	/**
	 * This method calculates whether the current position of the beam is correct or not.
	 * @param startLp  the LP of the stem endpoint at the beginning of the beam
	 * @param slantIs  the vertical distance of the beam endpoints in IS
	 */
	public boolean isGoodStemPosition(float startLp, float slantIs) {
		return true;
	}

	/**
	 * Returns the distance between two beamlines in IS.
	 * Normaly this is 0.25 but it can be up to 0.5 IS.
	 */
	public float getDistanceBetweenBeamLinesIs() {
		return 0.25f;
	}

	/**
	 * Returns the (unsigned) slant if there is only few space between the notes.
	 * The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 * @param startNoteLp  the LP of the first relevant note
	 * @param endNoteLp    the LP of the last relevant note
	 */
	public float getCloseSpacingSlantIs(int startNoteLp, int endNoteLp) {
		return 0.5f;
	}

	/**
	 * Returns the (unsigned) slant if there is plentiful space between the first and the last note.
	 * The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 * @param startNoteLp  the LP of the first relevant note
	 * @param endNoteLp    the LP of the last relevant note
	 */
	public float getWideSpacingSlantIs(int startNoteLp, int endNoteLp) {
		return 2f;
	}

	/**
	 * Returns the (unsigned) slant for normal spacing conditions.
	 * The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 * @param startNoteLP  the LP of the first relevant note
	 * @param endNoteLP    the LP of the last relevant note
	 */
	public float getNormalSpacingSlantIs(int startNoteLP, int endNoteLP) {
		return 1f;
	}

	/**
	 * Returns true, when the lines of the given beam are completely outside the staff
	 * (not even touching a staff line).
	 * @param stemDirection      the direction of the stems
	 * @param firstStemEndLp     the LP of the endpoint of the first stem
	 * @param lastStemEndLp      the LP of the endpoint of the last stem  
	 * @param staffLinesCount    the number of staff lines 
	 * @param totalBeamHeightIs  the total height of the beam lines (including gaps) in IS
	 */
	static boolean isBeamOutsideStaff(StemDirection stemDirection, float firstStemEndLp,
		float lastStemEndLp, int staffLinesCount, float totalBeamHeightIs) {
		float maxStaffLp = (staffLinesCount - 1) * 2;
		if (stemDirection == Up) {
			//beam lines above the staff?
			if (firstStemEndLp > maxStaffLp + totalBeamHeightIs * 2 &&
				lastStemEndLp > maxStaffLp + totalBeamHeightIs * 2) {
				return true;
			}
			//beam lines below the staff?
			if (firstStemEndLp < 0 && lastStemEndLp < 0) {
				return true;
			}
		}
		else if (stemDirection == Down) {
			//beam lines above the staff?
			if (firstStemEndLp > maxStaffLp && lastStemEndLp > maxStaffLp) {
				return true;
			}
			//beam lines below the staff?
			if (firstStemEndLp < -1 * totalBeamHeightIs * 2 &&
				lastStemEndLp < -1 * totalBeamHeightIs * 2) {
				return true;
			}
		}
		return false;
	}
}
