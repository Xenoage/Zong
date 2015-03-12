package com.xenoage.zong.musiclayout.spacer.beam;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;

/**
 * Slant of a beam, defined by the outer LPs of
 * the left and the right stem.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class Slant {
	public final float leftEndLp, rightEndLp;
}
