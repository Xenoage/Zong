package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.ChordNotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class for a stem stamping.
 * 
 * A stem has a notehead position and an end position
 * and is slightly thinner than a staff line.
 *
 * The notehead side can be on a different staff than the end of the stem.
 * This is the case when we have a cross-staff beam, where the end of the
 * stem is bound to a beam line which belongs to another staff.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public final class StemStamping
	extends Stamping {

	/** The parent chord. */
	public final ChordNotation chord;
	/** The horizontal position in mm. */
	public final float xMm;
	/** The start line position of the stem. */
	public final float noteLp;
	/** The parent staff or the note side. */
	public final StaffStamping noteStaff;
	/** The end line position of the stem.
	 * Also non-integer values are allowed here. */
	public final float endLp;
	/** The parent staff or the stem end side. */
	public final StaffStamping endStaff;
	/** Stem direction: If up, the notes are at the bottom, and vice versa. */
	public final StemDirection direction;



	@Override public StampingType getType() {
		return StampingType.StemStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Music;
	}

}
