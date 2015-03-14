package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.math.MathUtils.mod;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.notations.BeamNotation.lineHeightIs;
import static java.lang.Math.round;

import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Position of a beam end relative to a staff line.
 * See Ross p. 103, the box at the bottom.
 * 
 * @author Andreas Wenger
 */
public enum Anchor {
	/** Beam hangs below the staff line or leger line position. */
	Hang,
	/** Beam sits on the staff line or leger line position. */
	Sit,
	/** Beam straddles the staff line or leger line position. */
	Straddle,
	/** Beam is in the white space of a staff line. */
	WhiteSpace;
	
	/**
	 * Gets the anchor of a beam ending at the given LP
	 * with the given stem direction.
	 */
	public static Anchor fromLp(float stemEndLp, StemDirection stemDir) {
		float lp = stemEndLp - (stemDir == Up ? lineHeightIs * 2 : 0);
		int qs = round(lp * 2); //in quarter spaces
		int modQs = mod(qs, 4);
		switch (modQs) {
			case 0: return Sit;
			case 1: return WhiteSpace;
			case 2: return Hang;
			default: return Straddle;
		}
	}
	
}
