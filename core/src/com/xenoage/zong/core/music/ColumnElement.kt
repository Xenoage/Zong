package com.xenoage.zong.core.music

import com.xenoage.zong.core.format.Break
import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.barline.Barline
import com.xenoage.zong.core.music.direction.Tempo
import com.xenoage.zong.core.music.key.Key
import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.music.volta.Volta
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.core.position.MPElement


/**
 * Column elements are [MPElement]s, which can
 * appear in a whole measure column.
 *
 * This is the case for [TimeSignature], [Barline], [Volta],
 * [Key], [Tempo] and [Break].
 */
interface ColumnElement : MPElement {

	/** Back reference: The parent column, or null if not part of a column. */
	override var parent: ColumnHeader?

}

/** Sets the parent of this given element to the given container and returns it. */
fun <T: ColumnElement> T.setParent(container: ColumnHeader?): T {
	if (this != null) parent = container
	return this
}

/** Sets the parent of this given element (if any) to null and returns it. */
fun <T: ColumnElement> T?.unsetParent(): T? {
	if (this != null) parent = null
	return this
}