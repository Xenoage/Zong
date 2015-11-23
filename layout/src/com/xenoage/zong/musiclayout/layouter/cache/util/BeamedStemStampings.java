package com.xenoage.zong.musiclayout.layouter.cache.util;

import lombok.AllArgsConstructor;

import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.stampings.StemStamping;

/**
 * This class is used by the layouter
 * to collect the stems connected by one beam.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamedStemStampings {

	public BeamNotation beam;
	public StemStamping[] stems;
	

	public StemStamping firstStem() {
		return stems[0];
	}
	
	public StemStamping lastStem() {
		return stems[stems.length - 1];
	}

}
