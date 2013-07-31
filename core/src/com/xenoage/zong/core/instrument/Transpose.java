package com.xenoage.zong.core.instrument;

import lombok.Data;

import com.xenoage.utils.base.annotations.Const;
import com.xenoage.utils.base.annotations.MaybeNull;


/**
 * Transposition of an instrument.
 * 
 * Like in MusicXML (following documentation copied), a transposition
 * contains the following data:
 * 
 * The transposition is represented by chromatic steps
 * (required) and three optional elements: diatonic pitch
 * steps, octave changes, and doubling an octave down. The
 * chromatic and octave-change elements are numeric values
 * added to the encoded pitch data to create the sounding
 * pitch. The diatonic element is also numeric and allows
 * for correct spelling of enharmonic transpositions.
 * 
 * @author Andreas Wenger
 */
@Const @Data public final class Transpose
{
	
	/** The number of chromatic steps to add to the pitch */
	private final int chromatic;
	/** The number of diatonic steps, or null for default */
	@MaybeNull private final Integer diatonic;
	/** Octave change (like -2 for 2 octaves down) */
	private final int octaveChange;
	/** Copy pitch one octave down */
	private final boolean doubleOctaveDown;
	
	/** Instance with no transposition, */
	public static final Transpose noTranspose = new Transpose(0, 0, 0, false);
	
}
