package com.xenoage.zong.core.music.annotation;

/**
 * Type of an {@link Articulation}, like staccato or tenuto.
 * 
 * @author Andreas Wenger
 */
public enum ArticulationType {
	Accent,
	Staccato,
	Staccatissimo,
	/** Marcato. In MusicXML called strong accent. */
	Marcato,
	Tenuto
}
