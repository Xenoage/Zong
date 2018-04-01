package com.xenoage.zong.core.music.chord

import com.xenoage.zong.core.music.util.Duration

/**
 * Grace chord with slash (acciaccatura) or without (appoggiatura).
 */
class Grace(
		/** True for acciaccatura, false for appoggiatura.  */
		val isSlash: Boolean = false,
		/** Duration of the grace, like 1/8 for a grace chord that looks like an eighth.  */
		val graceDuration: Duration)