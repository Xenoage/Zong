package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.ChordNotation;

/**
 * Class for a stem stamping.
 * 
 * A stem has a notehead position and an end position
 * and is slightly thinner than a staff line.
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
	public final float noteheadLp;
	/** The end line position of the stem.
	 * Also non-integer values are allowed here. */
	public final float endLp;
	/** Stem direction: If up, the notes are at the bottom, and vice versa. */
	public final StemDirection direction;
	/** The parent staff. */
	public final StaffStamping parentStaff;


	@Override public StampingType getType() {
		return StampingType.StemStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Music;
	}

}
