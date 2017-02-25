package com.xenoage.zong.musiclayout.notation;

import com.xenoage.utils.annotations.Optimized;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.notation.beam.Fragments;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Layout information about a beam, like the appearance
 * of the beam lines.
 * 
 * Ross (p. 119) calles the 8th beam line a primary line, and all others
 * (16th, 32nd, ...) secondary lines.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamNotation
	implements Notation {
	
	/** Height of a beam line in IS. See Ross, p. 88.*/
	public static final float lineHeightIs = 0.5f;
	/** Default gap between lines in IS. Since three beam lines should
	 * fit into two IS (see Chlapik, p. 41, rule 1, and Ross, p. 119),
	 * the default gap between the lines is {@value #normalGapIs}. */
	public static final float normalGapIs = 0.25f;
	/** Larger gap between lines in IS. It should be used when a beam
	 * has more than four lines, to avoid beam lines centered within staff whitespace. */
	public static final float largeGapIs = 0.5f;
	/** Length of a beam hook in IS. See Chlapik, p. 45, rule 6.
	 * Ross, p. 124, section 9, demands the width of a notehead. */
	public static final float hookLengthIs = 1.25f;
	/** Minimum horizontal stem distance in IS, when wide spacing should
	 * be used. TODO: where does this rule come from? */
	public static final float wideSpacingIs = 8;
	/** Maximum horizontal stem distance in IS, when close spacing should
	 * be used. See Ross, p. 100. */
	public static final float closeSpacingIs = 5;


	/** The beam element. */
	@Getter public final Beam element;
	/** The {@link MP}, stored for performance reasons. */
	@Getter @Optimized(Optimized.Reason.Performance) public final MP mp;
	/** The fragments for each secondary line, starting with the 16th line, then 32nd ... .
	 * The primary 8th line is always continuous, so it is not stored. */
	public List<Fragments> linesFragments;
	/** The vertical gap (distance on y-axis) between the beam lines in IS. */
	public float gapIs;
	/** The beamed chords. */
	public List<ChordNotation> chords;
	
	
	public int getLinesCount() {
		return linesFragments.size() + 1;
	}
	
	/**
	 * Returns the height of all lines of the beam including their distances in IS.
	 */
	public float getTotalHeightIs() {
		int linesCount = getLinesCount();
		return lineHeightIs * linesCount + gapIs * (linesCount - 1);
	}

	/**
	 * Not used for beams.
	 */
	@Override public ElementWidth getWidth() {
		throw new UnsupportedOperationException();
	}
	
}
