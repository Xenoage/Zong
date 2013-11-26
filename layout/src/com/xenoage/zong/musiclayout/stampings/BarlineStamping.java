package com.xenoage.zong.musiclayout.stampings;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;
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
	@Getter private final Barline barline;

	/** The list of staves this barline is spanning. */
	@Getter private final IList<StaffStamping> staves;

	/** The horizontal position in mm, relative to the parent frame. */
	@Getter private final float xPosition;

	/** The grouping style of the barline. */
	@Getter private final BarlineGroup.Style groupStyle;


	public BarlineStamping(Barline barline, IList<StaffStamping> staves, float xPosition,
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
