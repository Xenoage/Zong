package com.xenoage.zong.core.music.direction

import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.format.Positioning
import com.xenoage.zong.core.position.MP
import lombok.NoArgsConstructor


/**
 * Class for a da capo sign.
 *
 * A [DaCapo] an only be placed at the end of a measure column (origin navigation sign).
 */
class DaCapo : NavigationSign {

	/** True, iff repeats should be played after jumping back (con repetizione or senza repetizione). */
	var isWithRepeats: Boolean = true

	override var positioning: Positioning? = null

	override var parent: ColumnHeader? = null

	override val elementType: MusicElementType
		get() = MusicElementType.DaCapo

}
