package com.xenoage.zong.core.text

/**
 * The simplest implementation of [Text]:
 * Just an ordinary, unformatted [String].
 */
class UnformattedText(
		val text: String
) : Text {

	override val length = text.length

	override val rawText
		get() = text

	override fun toString() = text

}
