package com.xenoage.zong.core.music;

import com.xenoage.utils.annotations.Optimized;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;


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
	Words;


	/**
	 * Returns true, if the given {@link MusicElement} is not null and of this type.
	 */
	public boolean is(MusicElement e) {
		return e != null && e.getMusicElementType() == this;
	}

}
