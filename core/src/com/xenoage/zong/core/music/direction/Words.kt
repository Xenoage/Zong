package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.TextElement
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.format.Positioning
import com.xenoage.zong.core.text.Text


/**
 * Direction words, that are not interpreted by this program.
 */
class Words(
		override var text: Text
) : Direction, TextElement {

	override var positioning: Positioning? = null

	override var parent: Chord? = null

	override val elementType: MusicElementType
		get() = MusicElementType.Words

}
