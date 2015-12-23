package com.xenoage.zong.musiclayout.layouter.cache.util;

import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.stampings.StemStamping;

import lombok.AllArgsConstructor;

/**
 * This class is used by the layouter
 * to collect the stems connected by one beam.
 * 
 * GOON TIDY
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamedStemStampings {

	public BeamNotation beamNotation;
	public StemStamping[] stems;
	

	public StemStamping firstStem() {
		return stems[0];
	}
	
	public StemStamping lastStem() {
		return stems[stems.length - 1];
	}

}
