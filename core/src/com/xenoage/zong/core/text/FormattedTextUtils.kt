package com.xenoage.zong.core.text

/**
 * This class creates [FormattedText] instances.
 */
object FormattedTextUtils {

	//OBSOLETE?

	/*

	/**
	 * Creates a [FormattedText] from the given [Text] instance.
	 * If it is already a [FormattedText], it is returned. Otherwise,
	 * the given default style and alignment is applied.
	 */
	fun styleText(text: Text, defaultStyle: FormattedTextStyle,
	              defaultAlignment: Alignment): FormattedText {
		return text as? FormattedText ?: fText(text.toString(), defaultStyle, defaultAlignment)
	}

	/**
	 * Creates a [FormattedText] from the given [Text] instance.
	 * If it is already a [FormattedText], it is returned. Otherwise,
	 * the given default style and left alignment is applied.
	 */
	fun styleText(text: Text, defaultStyle: FormattedTextStyle): FormattedText {
		return text as? FormattedText ?: fText(text.toString(), defaultStyle, Alignment.Left)
	}

	/**
	 * Creates a [FormattedText] from the given String.
	 * The given default style and left alignment is applied.
	 */
	fun styleText(text: String, defaultStyle: FormattedTextStyle): FormattedText {
		return fText(text, defaultStyle, Alignment.Left)
	}

	/**
	 * Merges the given two texts.
	 * The last paragraph of the first text and the first paragraph of the second
	 * text are merged into a single paragraph with the alignment of the
	 * last paragraph of the first text.
	 */
	fun merge(text1: FormattedText, text2: FormattedText): FormattedText {
		val ps = clist()
		//first text
		for (i in range(0, text1.paragraphs.size() - 2))
			ps.add(text1.paragraphs.get(i))
		//connection paragraph
		val lastPText1 = text1.paragraphs.getLast()
		val firstPText2 = text2.paragraphs.getFirst()
		val middleP = clist()
		middleP.addAll(lastPText1.getElements())
		middleP.addAll(firstPText2.getElements())
		ps.add(fPara(middleP.close(), lastPText1.getAlignment()))
		//second text
		for (i in range(1, text2.paragraphs.size() - 1))
			ps.add(text2.paragraphs.get(i))
		return clean(fText(ps.close()))
	}

	/**
	 * Splits the given [FormattedText] at the given position.
	 */
	fun split(source: FormattedText, position: Int): Tuple2<FormattedText, FormattedText> {
		var curPos = 0
		val ps1 = clist()
		val ps2 = clist()

		//collect data up to split position
		var iP: Int
		iP = 0
		firstText@ while (iP < source.paragraphs.size()) {
			val oldP = source.paragraphs.get(iP)
			for (iE in range(oldP.getElements())) {
				val oldE = oldP.getElements().get(iE)
				if (position >= curPos && position <= curPos + oldE.length) {
					//element found. if we are at the beginning or end of the found element, we can reuse it.
					//otherwise, we have to split the found element.
					if (position == curPos) {
						//easy case: at the beginning of an element.
						//just use all elements before this one for the first text
						//and this one and the following ones for the second text
						val es1 = clist()
						val es2 = clist()
						for (i in range(oldP.getElements())) {
							if (i < iE)
								es1.add(oldP.getElements().get(i))
							else
								es2.add(oldP.getElements().get(i))
						}
						ps1.add(fPara(es1.close(), oldP.getAlignment()))
						ps2.add(fPara(es2.close(), oldP.getAlignment()))
					} else if (position == curPos + oldE.length) {
						//easy case: at the end of an element.
						//just use all elements up to this one for the first text
						//and the following ones for the second text
						val es1 = clist()
						val es2 = clist()
						for (i in range(oldP.getElements())) {
							if (i <= iE)
								es1.add(oldP.getElements().get(i))
							else
								es2.add(oldP.getElements().get(i))
						}
						ps1.add(fPara(es1.close(), oldP.getAlignment()))
						ps2.add(fPara(es2.close(), oldP.getAlignment()))
					} else {
						//split element:
						//use all elements before this one and the first part of this one for the first text;
						//and the second part of this one and the following elements for the second text
						val splitPos = position - curPos
						val splitText = oldE.text
						val style = oldE.style
						val oldEPart1 = Companion.fString(splitText.substring(0, splitPos), style)
						val oldEPart2 = if (splitText.length > splitPos)
							Companion.fString(
									splitText.substring(splitPos, splitText.length), style)
						else
							null
						//collect elements
						val es1 = clist()
						val es2 = clist()
						for (i in range(oldP.getElements())) {
							if (i < iE)
								es1.add(oldP.getElements().get(i))
							else if (i == iE) {
								es1.add(oldEPart1)
								if (oldEPart2 != null)
									es2.add(oldEPart2)
							} else
								es2.add(oldP.getElements().get(i))
						}
						ps1.add(fPara(es1.close(), oldP.getAlignment()))
						ps2.add(fPara(es2.close(), oldP.getAlignment()))
					}
					break@firstText
				} else {
					//position not yet reached. go on.
					curPos += oldE.length
				}
			}
			ps1.add(oldP)
			curPos += 1 //line break character
			iP++
		}

		//collect data after split position
		iP++
		while (iP < source.paragraphs.size()) {
			ps2.add(source.paragraphs.get(iP))
			iP++
		}

		return t(fText(ps1.close()), fText(ps2.close()))
	}

	/**
	 * Inserts the given [FormattedText] at the given position.
	 */
	fun insert(source: FormattedText, position: Int, input: FormattedText): FormattedText {
		val sourceSplit = split(source, position)
		return merge(merge(sourceSplit.get1(), input), sourceSplit.get2())
	}

	/**
	 * Inserts the given [FormattedTextElement] at the given position.
	 */
	fun insert(source: FormattedText, position: Int, input: FormattedTextElement): FormattedText {
		val tInput = fText(fPara(input))
		return insert(source, position, tInput)
	}

	/**
	 * Inserts the given string at the given position.
	 * The style at the given position is used for the inserted string.
	 */
	fun insert(source: FormattedText, position: Int, input: String): FormattedText {
		val style = getStyle(source, position)
		return insert(source, position, Companion.fString(input, style))
	}

	/**
	 * Gets the style of the given text at the given position.
	 */
	private fun getStyle(text: FormattedText, position: Int): FormattedTextStyle {
		//get the style at the insert position
		var curPos = 0
		val oldPs = it(text.paragraphs)
		for (oldP in oldPs) {
			val oldEs = it(oldP.getElements())
			for (oldE in oldEs) {
				if (position > curPos && position <= curPos + oldE.length) {
					return oldE.style
				} else {
					//position not yet reached. go on.
					curPos += oldE.length
				}
			}
			curPos += 1 //line break character
		}
		throw IndexOutOfBoundsException("Invalid position")
	}

	/**
	 * Cleans the given text by merging adjacent [FormattedTextString]s
	 * with the same style.
	 * PERFORMANCE: only copy array if there is really a change
	 */
	fun clean(text: FormattedText): FormattedText {
		val newPs = clist(text.paragraphs)
		for (iP in range(newPs)) {
			val oldP = text.paragraphs.get(iP)
			val newEs = clist(oldP.getElements())
			var iE = 0
			var changed = false
			while (iE < newEs.size() - 1) {
				//look at pairs of adjacent elements
				val e1 = newEs.get(iE)
				val e2 = newEs.get(iE + 1)
				if (e1 is FormattedTextString && e2 is FormattedTextString &&
						e1.style == e2.style) {
					//merge two adjacent strings with the same style
					changed = true
					newEs.remove(iE + 1)
					newEs.remove(iE)
					newEs.add(iE, FormattedTextString(e1.text + e2.text, e1.style))
				} else {
					iE++
				}
			}
			if (changed)
				newPs.set(iP, FormattedTextParagraph(newEs, oldP.getAlignment()))
		}
		return FormattedText(newPs.close())
	}

	*/

}
