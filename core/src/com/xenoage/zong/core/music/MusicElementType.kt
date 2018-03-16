package com.xenoage.zong.core.music

import com.xenoage.utils.annotations.Optimized
import com.xenoage.utils.annotations.Reason.Performance


/**
 * Subclasses of [MusicElement].
 *
 * With this enum, a switch statement can be used instead of
 * complicated if-instanceof-statements.
 */
@Optimized(Performance)
enum class MusicElementType {
	Barline,
	Beam,
	Break,
	Chord,
	Clef,
	Coda,
	DaCapo,
	Dynamic,
	InstrumentChange,
	Lyric,
	Pedal,
	Rest,
	Segno,
	Slur,
	Tempo,
	Time,
	TraditionalKey,
	Volta,
	Wedge,
	WedgeEnd,
	Words
}
