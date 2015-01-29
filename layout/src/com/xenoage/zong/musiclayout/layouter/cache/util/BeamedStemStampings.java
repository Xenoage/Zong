package com.xenoage.zong.musiclayout.layouter.cache.util;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import static com.xenoage.utils.CheckUtils.checkNotNullIn;
import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.stampings.StemStamping;

/**
 * This class is used by the layouter
 * to collect the stems connected by one beam.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamedStemStampings {

	public Beam beam;
	public StemStamping[] stems;
	

	public StemStamping firstStem() {
		return stems[0];
	}
	
	public StemStamping lastStem() {
		return stems[stems.length - 1];
	}
	
	public void checkComplete() {
		checkArgsNotNull(beam, stems);
		checkNotNullIn(stems);
	}

}
