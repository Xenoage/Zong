package com.xenoage.zong.core.music.annotation;

/**
 * Type of an {@link Ornament}, like trill, turn or mordent.
 * 
 * @author Andreas Wenger
 */
public enum OrnamentType {
	/** Trill. See <a href="http://en.wikipedia.org/wiki/Ornament_%28music%29#Trill">Wikipedia</a>. */
	Trill,
	/** Upper mordent. See <a href="http://en.wikipedia.org/wiki/Ornament_%28music%29#Mordent">Wikipedia</a>. */
	InvertedMordent,
	/** Lower mordent. See <a href="http://en.wikipedia.org/wiki/Ornament_%28music%29#Mordent">Wikipedia</a>. */
	Mordent,
	/** Turn, a.k.a Grupetto. See <a href="http://en.wikipedia.org/wiki/Ornament_%28music%29#Turn">Wikipedia</a>. */
	Turn,
	/** Inverted turn. See <a href="http://en.wikipedia.org/wiki/Ornament_%28music%29#Turn">Wikipedia</a>. */
	InvertedTurn,
	/** Like {@link #Turn}, but notated between this and the next note. */
	DelayedTurn,
	/** Like {@link #InvertedTurn}, but notated between this and the next note. */
	DelayedInvertedTurn;
}
