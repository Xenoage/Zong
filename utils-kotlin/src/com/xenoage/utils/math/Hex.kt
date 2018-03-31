package com.xenoage.utils.math

import com.xenoage.utils.throwEx

private val hexDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")

/**
 * Converts this Int value to a hexadecimal number.
 */
fun Int.toHex(): String =
		when {
			this < 0 -> throwEx("only positive numbers are supported")
			this < 16 -> hexDigits[this]
			else -> (this / 16).toHex() + hexDigits[this % 16]
		}