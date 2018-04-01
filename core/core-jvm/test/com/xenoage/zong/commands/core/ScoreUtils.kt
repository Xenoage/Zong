package com.xenoage.zong.core

import com.xenoage.zong.commands.core.music.MeasureAdd
import com.xenoage.zong.commands.core.music.PartAdd
import com.xenoage.zong.core.music.Part


/** Creates a score with a single staff and 1 empty measure. */
fun score1Staff(): Score {
	val score = Score()
	PartAdd(score, Part(""), 0).execute()
	MeasureAdd(score, 1).execute()
	return score
}

/** Creates a score with a single staff and 4 empty measures. */
fun score1Staff4Measures(): Score {
	val score = score1Staff()
	MeasureAdd(score, 3).execute()
	return score
}