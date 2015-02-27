package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notations.BeamNotation;

/**
 * Stamping of a single beam line.
 * 
 * A beam has a start and end position in mm and two positions for the
 * end points. The two end points can be on different staves or on
 * the same staff.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public class BeamStamping
	extends Stamping {

	/** The stamped beam. */
	public final BeamNotation beam;
	/** The {@link StaffStamping} of the start position. */
	public final StaffStamping staff1;
	/** The {@link StaffStamping} of the end position. */
	public final StaffStamping staff2;
	/** The start coordinates of the beam. */
	public final SP sp1;
	/** The end coordinates of the beam. */
	public final SP sp2;


	@Override public StampingType getType() {
		return StampingType.BeamStamping;
	}

	@Override public Level getLevel() {
		return Level.Music;
	}
	
}
