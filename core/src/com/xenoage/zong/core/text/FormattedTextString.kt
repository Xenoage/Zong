package com.xenoage.zong.core.text

import com.xenoage.utils.font.TextMetrics

/**
 * Formatted substring of a text.
 */
class FormattedTextString(
		text: String,
		override val style: FormattedTextStyle
) : FormattedTextElement {

	override val text: String

	init {
		var text = text

		//text may not contain a line break. this must be represented as several paragraphs.
		//transform line breaks to spaces.
		if (text.contains("\n"))
			text = text.replace("\n".toRegex(), " ")

		//text may not contain tabs. tabs are replaced by spaces.
		text = text.replace("\t".toRegex(), " ")

		this.text = text

		//TODO this.metrics = platformUtils().getTextMeasurer().measure(font, text)
	}

	override val metrics: TextMetrics = TextMetrics(0f, 0f, 0f, 0f) //TODO

	override val length: Int
		get() = text.length

	override fun toString(): String = text

}
