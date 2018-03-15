package com.xenoage.zong.core.music.group


/**
 * Group of staves with a common barline.
 */
class BarlineGroup(
	/** The range of staves. */
	var staves: StavesRange,
	/** The visual style of this group. */
	var style: Style
) {

	/** Visual styles of barline grouping.  */
	enum class Style {
		/** Each staff has its own barlines, i.e. no grouping. */
		Single,
		/** The barlines of the staves are connected. */
		Common,
		/** Barlines are shown between the staves but not on them. */
		Mensurstrich
	}

}
