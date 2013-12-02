package com.xenoage.zong.musiclayout.layouter.beamednotation.design;

import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Base class for beam designs. This includes for example
 * the minimum stem length, slants or spacings that make
 * the beam look beautiful.
 * 
 * For each number of beam lines there are different rules,
 * so there is an abstract base class (this one) with different
 * implementations.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public abstract class BeamDesign {

	public static final float BEAMLINE_WIDTH = 0.5f;

	protected StemDirection stemDirection;
	protected int staffLinesCount;


	/**
	 * Creates a new {@link BeamDesign}.
	 * @param stemDirection    the direction of the stems
	 * @param staffLinesCount  the number of lines in the staff
	 */
	public BeamDesign(StemDirection stemDirection, int staffLinesCount) {
		this.stemDirection = stemDirection;
		this.staffLinesCount = staffLinesCount;
	}

	/**
	 * Gets the minimum length of a stem in IS.
	 */
	public float getMinimumStemLength() {
		return 3;
	}

	/**
	 * Gets the slant of a beam, when the middle notes of the beam
	 * are sorted descending. The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 */
	public float getSlantDescendingMiddleNotes() {
		return -0.5f; //TODO: was -1f before, but I'm not sure if Uli already meant it as IS instead of LP
	}

	/**
	 * Gets the slant of a beam, when the middle notes of the beam
	 * are sorted ascending. The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 */
	public float getSlantAscendingMiddleNotes() {
		return 0.5f; //TODO: was 1f before, but I'm not sure if Uli already meant it as IS instead of LP
	}

	/**
	 * This method calculates whether the current position of the beam is correct or not.
	 * @param startLP          the LP of the stem endpoint at the beginning of the beam
	 * @param slantIS          the vertical distance of the beam endpoints in IS
	 */
	public boolean isGoodStemPosition(float startLP, float slantIS) {
		return true;
	}

	/**
	 * Returns the distance between two beamlines in IS.
	 * Normaly this is 0.25 but it can be up to 0.5 IS.
	 */
	public float getDistanceBetweenBeamLines() {
		return 0.25f;
	}

	/**
	 * Returns the (unsigned) slant if there is only few space between the notes.
	 * The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 * @param startNoteLP    the LP of the first relevant note
	 * @param endNoteLP      the LP of the last relevant note
	 */
	public float getCloseSpacing(int startNoteLP, int endNoteLP) {
		return 0.5f;
	}

	/**
	 * Returns the (unsigned) slant if there is plentiful space between the first and the last note.
	 * The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 * @param startNoteLP    the LP of the first relevant note
	 * @param endNoteLP      the LP of the last relevant note
	 */
	public float getWideSpacing(int startNoteLP, int endNoteLP) {
		return 2f;
	}

	/**
	 * Returns the (unsigned) slant for normal spacing conditions.
	 * The slant is defined as the directed vertical distance of
	 * the first stem endpoint and the last stem endpoint in IS.
	 * @param startNoteLP    the LP of the first relevant note
	 * @param endNoteLP      the LP of the last relevant note
	 */
	public float getNormalSpacing(int startNoteLP, int endNoteLP) {
		return 1f;
	}

}
