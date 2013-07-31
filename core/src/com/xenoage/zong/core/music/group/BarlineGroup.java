package com.xenoage.zong.core.music.group;

import lombok.Data;

import com.xenoage.utils.base.annotations.NonNull;


/**
 * Group of staves with a common barline.
 * 
 * @author Andreas Wenger
 */
@Data public final class BarlineGroup {

	/** Visual styles of barline grouping. */
	public enum Style {
		/** Each staff has its own barlines. */
		Single,
		/** The barlines of the staves are connected. */
		Common,
		/** Barlines are shown between the staves but not on them. */
		Mensurstrich;
	}

	/** The range of staves. */
	@NonNull private StavesRange staves;
	/** The visual style of this group. */
	@NonNull private Style style;

}
