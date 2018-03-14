package com.xenoage.zong.core.music.util

import com.xenoage.zong.core.music.VoiceElement


/**
 * The side where to search for a [VoiceElement],
 * i.e. either the first matching element or the last one.
 */
enum class FirstOrLast {
	/** The first element (at the lowest beat) that matches a condition. */
	First,
	/** The last element (at the highest beat) that matches a condition. */
	Last
}
