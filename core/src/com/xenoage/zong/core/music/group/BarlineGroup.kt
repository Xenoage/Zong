package com.xenoage.zong.core.music.group

import lombok.NonNull


/**
 * Group of staves with a common barline.
 */
class BarlineGroup {

	/** The range of staves.  */
	@NonNull
	@get:NonNull
	var staves: StavesRange? = null
		set(@NonNull staves) {
			field = this.staves
		}
	/** The visual style of this group.  */
	@NonNull
	@get:NonNull
	var style: Style? = null
		set(@NonNull style) {
			field = this.style
		}

	/** Visual styles of barline grouping.  */
	enum class Style {
		/** Each staff has its own barlines, i.e. no grouping. Use this value instead of null.  */
		Single,
		/** The barlines of the staves are connected.  */
		Common,
		/** Barlines are shown between the staves but not on them.  */
		Mensurstrich
	}

}
