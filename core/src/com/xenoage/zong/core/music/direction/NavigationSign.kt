package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.format.Positioning

/**
 * Interface for [Direction]s with modify the playback sequence,
 * like [DaCapo] or [Coda].
 *
 * The term "navigation sign" is inspired by Wikipedia's article
 * on "Dal Segno", which calls it a "navigation marker".
 *
 * Navigation signs can either be placed at the beginning or at the end
 * of a measure column. At the beginning, it is a target sign, i.e. a jump
 * ends here and this measure is played next. At the ending, it is an
 * origin sign, i.e. the jump is performed from here after the measure is played.
 */
interface NavigationSign : ColumnElement {

	/** Custom position, or null for the default position.  */
	var positioning: Positioning?

}
