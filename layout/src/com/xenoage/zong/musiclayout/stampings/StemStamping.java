package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Class for a stem stamping.
 * 
 * A stem has a notehead position and an end position
 * and is slightly thinner than a staff line.
 * 
 * TODO: bind to note stamping somehow?
 *
 * @author Andreas Wenger
 */
@Const public final class StemStamping
	extends Stamping {

	/** The horizontal position in mm. */
	public final float xMm;

	/** The start line position of the stem. */
	public final float noteheadLp;

	/** The end line position of the stem.
	 * Also non-integer values are allowed here. */
	public final float endLp;

	/** Stem direction: If up, the notes are at the bottom, and vice versa. */
	public final StemDirection direction;


	public StemStamping(StaffStamping parentStaff, Chord chord, float xMm, float noteheadLp,
		float endLp, StemDirection direction) {
		super(parentStaff, Level.Music, chord, null);
		this.xMm = xMm;
		this.noteheadLp = noteheadLp;
		this.endLp = endLp;
		this.direction = direction;
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.StemStamping;
	}

}
