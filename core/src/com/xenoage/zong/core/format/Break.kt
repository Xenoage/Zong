package com.xenoage.zong.core.format

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.layout.PageBreak
import com.xenoage.zong.core.music.layout.SystemBreak
import com.xenoage.zong.core.position.MP


/**
 * Break information for a measure column:
 * Is there a system break or a page break forced
 * or prohibited?
 */
data class Break(
		val pageBreak: PageBreak? = null,
		val systemBreak: SystemBreak? = null
) : ColumnElement {

	/** Back reference: the parent measure column, or null if not part of a score.  */
	override var parent: ColumnHeader? = null

	override val musicElementType = MusicElementType.Break

	override fun toString() = "Break ($pageBreak, $systemBreak)"

	override fun getMP(): MP {
		return MP.getMPFromParent(this)
	}

}
