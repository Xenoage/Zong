package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.format.Positioning


/**
 * Class for a coda sign.
 */
class Coda : NavigationSign {

	override var positioning: Positioning? = null

	override var parent: ColumnHeader? = null

}
