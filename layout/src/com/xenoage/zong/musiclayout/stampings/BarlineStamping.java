package com.xenoage.zong.musiclayout.stampings;

import java.util.List;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.group.BarlineGroup;

/**
 * Class for a barline stamping.
 * 
 * The stamping can be placed on a single staff or a list of staves
 * (the barlines are connected).
 * 
 * A special case is a "Mensurstrich" barline,  which is placed between
 * the staves, but not on them.
 * 
 * At the moment a single stroke is used for the barline.
 *
 * @author Andreas Wenger
 */
@Const public final class BarlineStamping
	extends Stamping {

	/** The musical element, including the repeat and line style. */
	public final Barline barline;

	/** The list of staves this barline is spanning. */
	public final List<StaffStamping> staves;

	/** The horizontal position in mm, relative to the parent frame. */
	public final float xPosition;

	/** The grouping style of the barline. */
	public final BarlineGroup.Style groupStyle;


	public BarlineStamping(Barline barline, List<StaffStamping> staves, float xPosition,
		BarlineGroup.Style groupStyle) {
		super(staves.get(0), Level.Music, barline, null);
		this.barline = barline;
		this.staves = staves;
		this.xPosition = xPosition;
		this.groupStyle = groupStyle;
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.BarlineStamping;
	}

}
