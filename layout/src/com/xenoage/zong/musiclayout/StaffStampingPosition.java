package com.xenoage.zong.musiclayout;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * A {@link StaffStampingPosition} is a reference
 * to a StaffStamping and a x-coordinate
 * in mm, as well as the index of its score frame.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class StaffStampingPosition {

	public final StaffStamping staff;
	public final int frameIndex;
	public final float positionX;

}
