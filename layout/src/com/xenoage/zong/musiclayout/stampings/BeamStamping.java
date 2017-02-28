package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Stamping of a single beam line.
 * 
 * A beam has a start and end position in mm and two positions for the
 * end points. When it is a cross-staff beam, the beam belongs to
 * the staff of the first chord.
 * 
 * Dependent on the stem direction, the beam line sits on or hangs
 * from the given vertical position.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public class BeamStamping
	extends Stamping {

	/** The stamped beam. */
	public final BeamSpacing beam;
	/** The {@link StaffStamping} of the beam.
	 * For cross-staff beams, this is the staff of the first chord. */
	public final StaffStamping staff;
	/** The start coordinates of the beam. */
	public final SP sp1;
	/** The end coordinates of the beam. */
	public final SP sp2;
	/** The stem direction.
	 * For cross-staff beams, this is the stem direction of the first chord. */
	public final StemDirection stemDir;
	

	@Override public StampingType getType() {
		return StampingType.BeamStamping;
	}

	@Override public Level getLevel() {
		return Level.Music;
	}
	
}
