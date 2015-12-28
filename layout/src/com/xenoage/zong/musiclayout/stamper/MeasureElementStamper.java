package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.MusicElementStamper.musicElementStamper;

import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.ClefNotation;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.TimeNotation;
import com.xenoage.zong.musiclayout.notation.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;

/**
 * Creates {@link Stamping}s for {@link MeasureElement}s.
 * 
 * @author Andreas Wenger
 */
public class MeasureElementStamper {
	
	public static final MeasureElementStamper measureElementStamper = new MeasureElementStamper();
	
	
	public Stamping stamp(Notation notation, StaffStamping staff, float xMm, Context context) {
		if (notation instanceof ClefNotation) {
			return musicElementStamper.createClefStamping((ClefNotation) notation, xMm,
				staff, context.symbols);
		}
		else if (notation instanceof TraditionalKeyNotation) {
			return musicElementStamper.createKeyStamping((TraditionalKeyNotation) notation,
				xMm, staff, context.symbols, context.settings);
		}
		else if (notation instanceof TimeNotation) {
			return musicElementStamper.createTimeStamping((TimeNotation) notation, xMm,
				staff, context.symbols);
		}
		else {
			throw new IllegalArgumentException("Notation not supported: " + notation);
		}
	}

}
