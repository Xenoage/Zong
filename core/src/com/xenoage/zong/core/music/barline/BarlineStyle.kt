package com.xenoage.zong.core.music.barline


/**
 * Style of a barline, inspired by MusicXML.
 *
 * Repeat dots are not encoded in the barline style, but in
 * the [Barline] element itself.
 */
enum class BarlineStyle {
	/** Normal single barline */
	Regular,
	/** Dotted barline */
	Dotted,
	/** Dashed barline */
	Dashed,
	/** Thick barline */
	Heavy,
	/** Double barline with two thin lines */
	LightLight,
	/** Double barline with a thin line followed by a thick one. Usually used as the final barline and for repeat ends. */
	LightHeavy,
	/** Double barline with a thick line followed by a thin one. Usually used for repeat beginnings. */
	HeavyLight,
	/** Double barline with two thick lines. */
	HeavyHeavy,
	/** Invisible barline. */
	None
}
