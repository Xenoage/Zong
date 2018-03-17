package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.format.Positioning
import com.xenoage.zong.core.position.MP
import lombok.Data
import lombok.EqualsAndHashCode


/**
 * Class for a coda sign.
 */
class Coda : NavigationSign {

	override var positioning: Positioning? = null

	override var parent: ColumnHeader? = null

	override val elementType: MusicElementType
		get() = MusicElementType.Coda

}
