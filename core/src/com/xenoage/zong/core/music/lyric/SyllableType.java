package com.xenoage.zong.core.music.lyric;


/**
 * Position of a syllable within lyrics.
 * 
 * @author Andreas Wenger
 */
public enum SyllableType {
	/** Single syllable like "Auf" or "dem". */
	Single,
	/** First syllable of a word, like "Hü" in "Hügel". */
	Begin,
	/** Middle syllable of a word, like "gel" in "Hügelland". */
	Middle,
	/** Last syllable of a word, like "gel" in "Hügel". */
	End,
	/** Melisma line (underscore line) for a syllable which spreads over multiple notes. */
	Extend
};