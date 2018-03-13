package com.xenoage.zong.core.music.annotation

import com.xenoage.zong.core.music.format.Placement

/**
 * An ornament, like trill, turn or mordent.
 */
class Ornament(
		/** The type of the ornament, like trill, turn or mordent */
		val type: OrnamentType? = null
) : Annotation {

	/** The placement of the ornament: above, below or default (null) */
	var placement: Placement? = null

}

/**
 * Type of an [Ornament], like trill, turn or mordent.
 */
enum class OrnamentType {
	/** Trill. See [Wikipedia](http://en.wikipedia.org/wiki/Ornament_%28music%29#Trill).  */
	Trill,
	/** Upper mordent. See [Wikipedia](http://en.wikipedia.org/wiki/Ornament_%28music%29#Mordent).  */
	InvertedMordent,
	/** Lower mordent. See [Wikipedia](http://en.wikipedia.org/wiki/Ornament_%28music%29#Mordent).  */
	Mordent,
	/** Turn, a.k.a Grupetto. See [Wikipedia](http://en.wikipedia.org/wiki/Ornament_%28music%29#Turn).  */
	Turn,
	/** Inverted turn. See [Wikipedia](http://en.wikipedia.org/wiki/Ornament_%28music%29#Turn).  */
	InvertedTurn,
	/** Like [Turn], but notated between this and the next note.  */
	DelayedTurn,
	/** Like [InvertedTurn], but notated between this and the next note.  */
	DelayedInvertedTurn
}