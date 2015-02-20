package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.format.SP;

/**
 * Class for a beam stamping.
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

	//TODO: move into other class
	public static final float beamHeight = 0.5f; //height of beam in interline spaces
	public static final float beamGap = 0.3f; //gap between lines in interline spaces
	
	/** The beam data. */
	public final Beam beam;
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
