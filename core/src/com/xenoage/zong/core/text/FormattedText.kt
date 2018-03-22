package com.xenoage.zong.core.text

import com.xenoage.utils.Mm
import com.xenoage.utils.math.Rectangle2f
import com.xenoage.utils.max
import com.xenoage.utils.sumFloat
import com.xenoage.utils.sumInt
import com.xenoage.zong.core.text.Alignment.Center
import com.xenoage.zong.core.text.Alignment.Right

/**
 * A formatted text contains a list of paragraphs with styled text.
 */
class FormattedText(
		/** The list of paragraphs  */
		val paragraphs: List<FormattedTextParagraph>
) : Text {

	/** The bounding rectangle of the text. */
	val boundingRect: Rectangle2f
		get() {
			var x = 0f
			val p = firstParagraph
			when (p.alignment) {
				Center -> x -= width / 2
				Right -> x -= width
			}
			val y = -1 * p.metrics.ascent
			return Rectangle2f(x, y, width, height)
		}

	/** The first paragraph. */
	val firstParagraph: FormattedTextParagraph
		get() = paragraphs.first()

	override val length: Int
		get() = paragraphs.sumInt { it.length } + /* \n line breaks: */ paragraphs.size - 1

	/**
	 * Breaks this formatted text up so that it fits into the given width
	 * and returns the result.
	 * The result is no deep copy of the whole text, instead references to the
	 * unmodified parts are used.
	 */
	fun lineBreak(width: Float): FormattedText {
		val ret = mutableListOf<FormattedTextParagraph>()
		//break up paragraphs
		for (paragraph in paragraphs)
			for (paragraphLine in paragraph.lineBreak(width))
				ret.add(paragraphLine)
		return FormattedText(ret)
	}

	override fun toString(): String = rawText

	override val rawText: String
		get() = paragraphs.joinToString { "\n" }

	/** The width of this text in mm (without automatic line breaks). */
	val width: Mm
			by lazy { paragraphs.max({ it.metrics.width }, 0f) }

	/** The height of this text in mm (without automatic line breaks). */
	val height: Mm
			by lazy { paragraphs.sumFloat { it.heightMm } }


	companion object {

		/** An empty text.  */
		val empty = FormattedText(emptyList())

		/**
		 * Creates a [FormattedText] with the given text, using
		 * the given style and alignment. Paragraphs are divided by "\n".
		 */
		operator fun invoke(text: String, style: FormattedTextStyle, alignment: Alignment): FormattedText {
			val lines = text.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }
			val paras = lines.map { FormattedTextParagraph(listOf(FormattedTextString(it, style)), alignment) }
			return FormattedText(paras)
		}
	}

}
