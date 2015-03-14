package com.xenoage.zong.musiclayout.spacer.beam;

import static java.lang.Math.abs;
import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;


/**
 * Vertical placement of a beam, defined by the outer LPs of
 * the left and the right stem.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class Placement {
	
	public final float leftEndLp, rightEndLp;
	
	public boolean isHorizontal() {
		return abs(rightEndLp - leftEndLp) <= 0.1f;
	}
	
	public boolean isAscending() {
		return rightEndLp - leftEndLp > 0.1f;
	}
	
	public boolean isDescending() {
		return leftEndLp - rightEndLp > 0.1f;
	}
	
}
