package com.xenoage.zong.core.music;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;

import com.xenoage.utils.annotations.Optimized;


/**
 * Subclasses of {@link MusicElement}.
 * 
 * With this enum, a switch statement can be used instead of
 * if-instanceof-statements. 
 *
 * @author Andreas Wenger
 */
@Optimized(Performance)
public enum MusicElementType {
	Barline,
	Beam,
	Break,
	Chord,
	Clef,
	Coda,
	DaCapo,
	Dynamics,
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
