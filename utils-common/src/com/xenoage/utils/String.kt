package com.xenoage.utils

import kotlin.math.max
import kotlin.math.min

/**
 * Functions to work with strings.
 */

private val linebreakChars = charArrayOf(' ', '-', '\u2013', '–')


/** Returns a copy of this string, with trailing spaces omitted. */
fun String.trimRight(): String {
	for (i in length - 1 downTo 0) {
		if (this[i] != ' ')
			return substring(0, i + 1)
	}
	return ""
}

/** Returns true, if this character is a line break character, otherwise false. */
val Char.isLineBreakCharacter: Boolean
	get() = linebreakChars.contains(this)

/** Returns true, if there is a line break character in this string, otherwise false. */
fun String.containsLineBreakCharacter(): Boolean =
	find { it.isLineBreakCharacter } != null

/** Returns this string concatenated [count] times . */
fun String.repeat(count: Int): String {
	val ret = StringBuilder(length * count)
	repeat(count, { ret.append(this) })
	return ret.toString()
}

/**
 * Formats this integer number so that it has at least the given
 * number of digits by adding 0 digits to the front.
 */
fun Int.fillIntDigits(minDigits: Int): String {
	val n = this.toString()
	val ret = StringBuilder(max(minDigits, n.length))
	val zeroCount = min(0, minDigits - n.length)
	repeat(zeroCount, { ret.append("0") })
	ret.append(n)
	return ret.toString()
}
