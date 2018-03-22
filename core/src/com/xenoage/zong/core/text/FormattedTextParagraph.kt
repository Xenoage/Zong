package com.xenoage.zong.core.text

import com.sun.xml.internal.ws.util.StringUtils
import com.xenoage.utils.*
import com.xenoage.utils.collections.*
import com.xenoage.utils.font.TextMetrics
import kotlin.math.max

/**
 * One paragraph within a [FormattedText].
 */
class FormattedTextParagraph(
		/** The elemens of this paragraph  */
		val elements: List<FormattedTextElement>,
		/** The horizontal alignment of this paragraph  */
		val alignment: Alignment
) {

	/** The measurements of this paragraph.  */
	val metrics: TextMetrics

	init {
		//compute ascent, descent and leading
		val ascent = elements.max({ it.metrics.ascent }, 0f)
		val descent = elements.max({ it.metrics.descent }, 0f)
		val leading = elements.max({ it.metrics.leading }, 0f)
		val width = elements.sumFloat { it.metrics.width }
		this.metrics = TextMetrics(ascent, descent, leading, width)
	}

	/** The plain text. */
	val rawText: String
		get() = elements.joinToString("")

	val length: Int
		get() = elements.sumBy { it.length }

	/** The height of this paragraph in mm. */
	val heightMm: Float
		get() {
			var maxAscent = 0f
			var maxDescentAndLeading = 0f
			for (element in elements) {
				val m = element.metrics
				maxAscent = max(maxAscent, m.ascent)
				maxDescentAndLeading = max(maxDescentAndLeading, m.descent + m.leading)
			}
			return maxAscent + maxDescentAndLeading
		}

	/**
	 * Breaks this paragraph up into one or more lines so
	 * that it fits into the given width and returns the result.
	 */
	fun lineBreak(width: Float): List<FormattedTextParagraph> {
		val ret = mutableListOf<FormattedTextParagraph>()
		if (elements.size == 0) {
			//nothing to break up
			ret.add(this)
		} else {
			//queue with elements still to format
			val queue = Queue<FormattedTextElement>(elements.size)
			queue.addAll(elements)
			//loop through all elements
			while (queue.size > 0) {
				//create list to collect elements for current line
				val line = Queue<FormattedTextElement>()
				var lineWidth = 0f
				//add elements to the line until width is reached
				do {
					val currentElement = queue.removeFirst()
					line.addLast(currentElement)
					lineWidth += currentElement.metrics.width
				} while (lineWidth <= width && queue.size > 0)
				//line too wide?
				if (lineWidth > width) {
					//yes. we have to do a line break.
					//for string elements, we can divide the text and put one part in this
					//line and the other part into the next one. for symbols, we always begin a new line.
					val last = line.last()
					if (last is FormattedTextString) {
						val lineWidthBeforeLineBreak = lineWidth
						//first test if line is wide enough for at least one character (if it is a String)
						//or the whole symbol (if it is a symbol)
						val firstCharElement = FormattedTextString(last.text.substring(0, 1), last.style)
						if (firstCharElement.metrics.width > width) {
							//not enough space for even one character. return empty list.
							return emptyList()
						}
						//spacing character within this line?
						var lineBreakSuccess = false
						if (line.containsLineBreakCharacter()) {
							//go back until the last spacing character.
							//search for element to cut
							var searchCuttedElement = line.removeLast() as FormattedTextString
							lineWidth -= searchCuttedElement.metrics.width
							var queuedElementsCount = 0
							while (false == searchCuttedElement.text.containsLineBreakCharacter()) {
								queue.addFirst(searchCuttedElement)
								queuedElementsCount++
								searchCuttedElement = line.removeLast() as FormattedTextString
								lineWidth -= searchCuttedElement.metrics.width
							}
							val cuttedElement = searchCuttedElement
							//find last spacing character that fits into the given width and split the
							//cuttedElement there
							var forThisLine: FormattedTextString? = null
							var forThisLineTrimRight: FormattedTextString? = null
							for (i in cuttedElement.text.length - 1 downTo 0) {
								val c = cuttedElement.text[i]
								if (c.isLineBreakCharacter) {
									forThisLine = FormattedTextString(
											cuttedElement.text.substring(0, i + 1), cuttedElement.style)
									//ignore spaces at the end
									val forThisLineTrimRightText = forThisLine.text.trimRight()
									forThisLineTrimRight = if (forThisLineTrimRightText.length > 0)
										FormattedTextString(forThisLineTrimRightText, forThisLine.style)
									else
										null
									if (forThisLineTrimRight == null || lineWidth + forThisLineTrimRight.metrics.width <= width)
										break
								}
							}
							//if the left side of the cutted line is now short enough to fit into the width, we had
							//success and apply the linebreak. otherwise we must do a linebreak in the middle of a word.
							if (forThisLine != null &&
									(forThisLineTrimRight == null || lineWidth + forThisLineTrimRight.metrics.width <= width)) {
								lineBreakSuccess = true
								//complete this line
								line.addLast(forThisLine)
								ret.add(FormattedTextParagraph(line.toList(), alignment))
								//begin next line
								if (forThisLine.text.length < cuttedElement.text.length) {
									val forNextLine = FormattedTextString(
											cuttedElement.text.substring(forThisLine.text.length), cuttedElement.style)
									queue.addFirst(forNextLine)
								}
							} else {
								lineBreakSuccess = false
								lineWidth = lineWidthBeforeLineBreak
								//restore original line and queue
								for (i in 0 until queuedElementsCount) {
									line.addLast(queue.removeFirst())
								}
								line.addLast(cuttedElement)
							}
						}
						if (!lineBreakSuccess) {
							//there is no spacing character in this line or we had no success using it,
							//so we have to do a linebreak in the middle of a word.
							//since we have added element after element, the possible line break must be
							//within the last element.
							val cuttedElement = line.removeLast() as FormattedTextString
							lineWidth -= cuttedElement.metrics.width
							var forThisLine: FormattedTextString? = null
							for (i in cuttedElement.text.length - 1 downTo -1) {
								if (i >= 0) {
									forThisLine = FormattedTextString(
											cuttedElement.text.substring(0, i + 1), cuttedElement.style)
									//ignore spaces at the end
									val forThisLineTrimRightText = forThisLine.text.trimRight()
									val forThisLineTrimRight = if (forThisLineTrimRightText.length > 0)
										FormattedTextString(forThisLineTrimRightText, forThisLine.style)
									else
										null
									if (forThisLineTrimRight == null || lineWidth + forThisLineTrimRight.metrics.width <= width) {
										break
									}
								} else {
									forThisLine = null
								}
							}
							//complete this line
							if (forThisLine != null) {
								line.addLast(forThisLine)
							}
							ret.add(FormattedTextParagraph(line.toList(), alignment))
							//begin next line
							if (forThisLine == null || forThisLine.text.length < cuttedElement.text.length) {
								val forNextLine = FormattedTextString(
										if (forThisLine != null)
											cuttedElement.text.substring(forThisLine.text.length)
										else
											cuttedElement.text,
										cuttedElement.style)
								queue.addFirst(forNextLine)
							}
						}
					} else if (last is FormattedTextSymbol) {
						if (line.size > 1) {
							//at least two elements, so one can be placed in this line
							//move symbol into next line
							val symbol = line.removeLast()
							ret.add(FormattedTextParagraph(line.toList(), alignment))
							//begin next line
							queue.addFirst(symbol)
						} else {
							//not enough space for even one symbol. return empty list.
							return emptyList()
						}
					}
				} else {
					//no. we can use exactly that line. that means, this was
					//the last line and we are finished.
					ret.add(FormattedTextParagraph(line.toList(), alignment))
					break
				}
			}
		}
		return ret
	}

	/**
	 * Returns true, if a line break character is found in this list
	 * of formatted text elements, but not at the end (trailing spaces are ignored).
	 */
	private fun List<FormattedTextElement>.containsLineBreakCharacter(): Boolean {
		val last = elements.last()
		for (element in elements) {
			if (element !== last) {
				if (element.text.containsLineBreakCharacter())
					return true
			} else {
				return element.text.trimRight().containsLineBreakCharacter()
			}
		}
		return false
	}

	override fun toString(): String =
			elements.joinToString("")

	companion object {
		val defaultAlignment = Alignment.Left
	}

}
