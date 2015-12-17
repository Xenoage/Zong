package com.xenoage.zong.musiclayout.notator.beam.lines;

import static com.xenoage.zong.musiclayout.notation.BeamNotation.defaultGapIs;

import com.xenoage.zong.musiclayout.notation.BeamNotation;

import lombok.AllArgsConstructor;

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
 * The general rules are explained in Ted Ross' book, starting on
 * page 88.
 * 
 * The "slant" of a beam is defined as the vertical distance of
 * the first stem endpoint and the last stem endpoint in IS.
 * When not named "absolute", the slant normally denotes a directed value
 * (negative for descending and positive for ascending beams).
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public abstract class BeamRules {

	/**
	 * Gets the maximum number of beam lines, e.g. 1 for a beam
	 * which contains at maximum 8th notes, 2 for 16th notes and so on.
	 */
	public abstract int getBeamLinesCount();
	
	/**
	 * Gets the minimum absolute length of a stem in IS,
	 * from the innermost note at the stem side up to
	 * the end of the stem.
	 */
	public float getMinimumStemLengthIs() {
		return 3;
	}

	/**
	 * Gets the absolute slant of a beam in IS, when the middle notes of the beam
	 * are sorted ascending or descending (called "run" in Ross, p. 97).
	 */
	public float getAbsSlantForMiddleNotesRunIs() {
		return 0.5f; //according to Ross, p. 97
	}
	
	/*-* GOON: this method should replace isGoodStemPosition. add lots of tests from Ross/Chlapik
	 * This method returns true, iff the given end positions of a beam are correct.
	 * @param leftEndLp   the LP of the stem endpoint at the beginning of the beam
	 * @param rightEndLp  the LP of the stem endpoint at the end of the beam
	 * /
	public boolean isGoodPosition(float leftEndLp, float rightEndLp) {
		//Ross, p. 88:
		//The placement of a beam follows the cardinal rule that when it falls within the staff,
		//its ends must either sit, hang or straddle a staff line, whether the beam is single
		//or multiple.
		return false;
	} */

	/**
	 * This method calculates whether the current position of the beam is correct or not.
	 * @param startLp  the LP of the stem endpoint at the beginning of the beam
	 * @param slantIs  the vertical distance of the beam endpoints in IS
	 */
	@Deprecated public boolean isGoodStemPosition(float startLp, float slantIs) {
		return true;
	}

	/**
	 * Returns the vertical distance between two beam lines in IS, that is
	 * the "white space" between two beam lines.
	 * Normally this is {@link BeamNotation#defaultGapIs}, but it can be up to
	 * 0.5 IS for a beam with more than three lines.
	 */
	public float getGapIs() {
		return defaultGapIs;
	}

	/**
	 * Returns the absolute slant if there is only few horizontal space between the notes.
	 * @param startNoteLp  the LP of the first relevant note
	 * @param endNoteLp    the LP of the last relevant note
	 */
	@Deprecated //can be 0.25 or 0.5, see Riss, p. 113, the lower examples a) and b)
	public float getAbsSlantForCloseSpacingIs(int startNoteLp, int endNoteLp) {
		return 0.5f; //Ross, p. 112 ff. 
	}
	
	/**
	 * Returns the (unsigned) slant for normal horizontal spacing conditions.
	 * @param startNoteLP  the LP of the first relevant note
	 * @param endNoteLP    the LP of the last relevant note
	 */
	@Deprecated //depends on the situation, the return value 1 is a special case
	public float getAbsSlantForNormalSpacingIs(int startNoteLP, int endNoteLP) {
		return 1f; //TODO: apply Ross, p. 101, rule 3
	}

	/**
	 * Returns the absolute slant if there is plentiful horizontal
	 * space between the first and the last note.
	 * @param startNoteLp  the LP of the first relevant note
	 * @param endNoteLp    the LP of the last relevant note
	 */
	@Deprecated //depends on the situation, the return value 2 is a special case
	public float getAbsSlantForWideSpacingIs(int startNoteLp, int endNoteLp) {
		return 2f; //TODO: apply Ross, p. 101, rule 3
	}
	
}
