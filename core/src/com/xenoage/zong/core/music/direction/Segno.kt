package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.format.Positioning


/**
 * Class for a segno sign.
 */
class Segno : NavigationSign {

	/** True, iff repeats should be played after jumping back (con repetizione or senza repetizione).  */
	var isWithRepeats: Boolean = true

	override var positioning: Positioning? = null

	override var parent: ColumnHeader? = null

}
