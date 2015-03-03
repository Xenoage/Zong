package com.xenoage.zong.musiclayout.notator.beam.lines;

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
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
@AllArgsConstructor
public abstract class BeamLinesRules {

	/**
	 * Gets the maximum number of beam lines, e.g. 1 for a beam
	 * which contains at maximum 8th notes, 2 for 16th notes and so on.
	 */
	public abstract int getBeamLinesCount();
	
	/**
	 * Gets the minimum length of a stem in IS,
	 * from the innermost note at the stem side up to
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
	 * Returns the vertical distance between two beam lines in IS, that is
	 * the "white space" between two beam lines.
	 * Normaly this is 0.25 but it can be up to 0.5 IS.
	 */
	public float getGapIs() {
		return 0.25f;
	}

	/**
	 * Returns the (unsigned) slant if there is only few horizontal space between the notes.
	 * The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 * @param startNoteLp  the LP of the first relevant note
	 * @param endNoteLp    the LP of the last relevant note
	 */
	public float getSlantCloseSpacingIs(int startNoteLp, int endNoteLp) {
		return 0.5f;
	}

	/**
	 * Returns the (unsigned) slant if there is plentiful horizontal
	 * space between the first and the last note.
	 * The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 * @param startNoteLp  the LP of the first relevant note
	 * @param endNoteLp    the LP of the last relevant note
	 */
	public float getSlantWideSpacingIs(int startNoteLp, int endNoteLp) {
		return 2f;
	}

	/**
	 * Returns the (unsigned) slant for normal horizontal spacing conditions.
	 * The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 * @param startNoteLP  the LP of the first relevant note
	 * @param endNoteLP    the LP of the last relevant note
	 */
	public float getSlantNormalSpacingIs(int startNoteLP, int endNoteLP) {
		return 1f;
	}
	
}
