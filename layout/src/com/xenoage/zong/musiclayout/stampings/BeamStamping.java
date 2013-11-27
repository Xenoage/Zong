package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.beam.Beam;

/**
 * Class for a beam stamping.
 * 
 * A beam has a start and end position in mm and two positions for the
 * end points. The two end points can be on different staves or on
 * the same staff.
 *
 * @author Andreas Wenger
 */
@Const public final class BeamStamping
	extends Stamping {

	//TODO: move into other class
	public static final float beamHeight = 0.5f; //height of beam in interline spaces
	public static final float beamGap = 0.3f; //gap between lines in interline spaces

	/** The {@link StaffStamping} of the start position. */
	public final StaffStamping staff1;

	/** The {@link StaffStamping} of the end position. */
	public final StaffStamping staff2;

	/** The horizontal start coordinate of the beam in mm. */
	public final float x1;

	/** The horizontal end coordinate of the beam in mm. */
	public final float x2;

	/** The end line position at the start position. */
	public final float lp1;

	/** The end line position at the end position. */
	public final float lp2;


	public BeamStamping(Beam beam, StaffStamping staff1, StaffStamping staff2, float x1, float x2,
		float lp1, float lp2) {
		super(staff1, Level.Music, beam, null);
		this.staff1 = staff1;
		this.staff2 = staff2;
		this.x1 = x1;
		this.x2 = x2;
		this.lp1 = lp1;
		this.lp2 = lp2;
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.BeamStamping;
	}

}
