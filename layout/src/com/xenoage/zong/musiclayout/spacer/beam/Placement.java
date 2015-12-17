package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.zong.musiclayout.spacer.beam.Direction.Ascending;
import static com.xenoage.zong.musiclayout.spacer.beam.Direction.Descending;
import static com.xenoage.zong.musiclayout.spacer.beam.Direction.Horizontal;

import com.xenoage.utils.annotations.Const;

import lombok.Data;


/**
 * Vertical placement of a beam, defined by the outer LPs of
 * the left and the right stem.
 * 
 * @author Andreas Wenger
 */
@Const @Data
public final class Placement {
	
	public final float leftEndLp, rightEndLp;
	
	
	public Direction getDirection() {
		float d = rightEndLp - leftEndLp;
		if (d > 0.1)
			return Ascending;
		else if (d < -0.1)
			return Descending;
		else
			return Horizontal;
	}
	
}
