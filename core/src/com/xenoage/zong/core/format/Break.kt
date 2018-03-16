package com.xenoage.zong.core.format

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.layout.PageBreak
import com.xenoage.zong.core.music.layout.SystemBreak
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MPContainer


/**
 * Break information for a measure column:
 * Is there a system break or a page break forced
 * or prohibited?
 */
data class Break(
		val pageBreak: PageBreak? = null,
		val systemBreak: SystemBreak? = null
) : ColumnElement {

	/** Back reference: the parent column header, or null if not part of a score */
	override var parent: MPContainer? = null

	override val elementType: MusicElementType
		get() = MusicElementType.Break

	override fun toString() = "Break ($pageBreak, $systemBreak)"

}
