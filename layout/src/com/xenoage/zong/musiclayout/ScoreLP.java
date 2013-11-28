package com.xenoage.zong.musiclayout;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;

/**
 * This class contains a position within a score layout,
 * not in musical sense but in metric coordinates: frame index
 * and coordinates relative to the upper left corner in mm.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class ScoreLP {

	public final int frameIndex;
	public final Point2f pMm;

}
