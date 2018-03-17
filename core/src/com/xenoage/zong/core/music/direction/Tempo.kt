package com.xenoage.zong.core.music.direction

import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.TextElement
import com.xenoage.zong.core.music.format.Positioning
import com.xenoage.zong.core.text.Text


/**
 * Class for a tempo direction, like "Andante", "♩ = 120"
 * or "Ziemlich langsam und mit Ausdruck".
 *
 * The text is optional, but the meaning must be given.
 */
class Tempo(
		/** The musical meaning: which beat per minute. */
		val baseBeat: Fraction,
		/** The musical meaning: how many of that beats per minute. */
		val beatsPerMinute: Int
) : TextElement, ColumnElement {

	init {
		check(beatsPerMinute >= 0, { "beatsPerMinute must be > 0" })
	}

	/** Custom caption, or null for format "baseBeat = beatsPerMinute".  */
	override var text: Text? = null

	var positioning: Positioning? = null

	override var parent: ColumnHeader? = null

	override val elementType: MusicElementType
		get() = MusicElementType.Tempo

}
