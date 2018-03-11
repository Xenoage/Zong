package com.xenoage.zong.core.format

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.layout.PageBreak
import com.xenoage.zong.core.music.layout.SystemBreak
import com.xenoage.zong.core.position.MP
import lombok.Data
import lombok.EqualsAndHashCode


/**
 * Break information for a measure column:
 * Is there a system break or a page break forced
 * or prohibited?
 *
 * @author Andreas Wenger
 */
@Const
@Data
@EqualsAndHashCode(exclude = arrayOf("parent"))
class Break : ColumnElement {

	val pageBreak: PageBreak? = null
	val systemBreak: SystemBreak? = null

	/** Back reference: the parent measure column, or null if not part of a score.  */
	private val parent: ColumnHeader? = null


	override fun toString(): String {
		return "Break ($pageBreak, $systemBreak)"
	}

	override fun getMusicElementType(): MusicElementType {
		return MusicElementType.Break
	}

	override fun getMP(): MP {
		return MP.getMPFromParent(this)
	}

}
