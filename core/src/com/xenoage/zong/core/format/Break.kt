package com.xenoage.zong.core.format

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.layout.PageBreak
import com.xenoage.zong.core.music.layout.SystemBreak
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

	override var parent: ColumnHeader? = null

	override fun toString() = "Break ($pageBreak, $systemBreak)"

}
