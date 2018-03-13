package com.xenoage.zong.core.music.annotation

import com.xenoage.zong.core.music.format.Placement

/**
 * An articulation, like staccato or tenuto.
 */
class Articulation(
		/** The type of the articulation, like staccato or tenuto */
		val type: ArticulationType
) : Annotation {

	/** The placement of the articulation: above, below or default (null) */
	var placement: Placement? = null

}

/**
 * Type of an [Articulation], like staccato or tenuto.
 */
enum class ArticulationType {
	Accent,
	Staccato,
	Staccatissimo,
	/** Marcato. In MusicXML called strong accent.  */
	Marcato,
	Tenuto
}

