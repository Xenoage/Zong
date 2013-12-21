package com.xenoage.zong.io.musicxml.in.readers;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.musicxml.types.MxlSystemLayout;
import com.xenoage.zong.musicxml.types.MxlSystemMargins;

/**
 * This class reads system-layout elements into
 * {@link SystemLayout} objects.
 * 
 * @author Andreas Wenger
 */
public final class SystemLayoutReader {

	@AllArgsConstructor
	public static final class Value {

		public final SystemLayout systemLayout;
		public final Float topSystemDistance;
	}


	/**
	 * Reads a {@link MxlSystemLayout}.
	 */
	public static Value read(MxlSystemLayout mxlSystemLayout, float tenthMm) {
		SystemLayout systemLayout = new SystemLayout();

		//system-margins
		MxlSystemMargins mxlMargins = mxlSystemLayout.getSystemMargins();
		if (mxlMargins != null) {
			systemLayout.setMarginLeft(tenthMm * mxlMargins.getLeftMargin());
			systemLayout.setMarginRight(tenthMm * mxlMargins.getRightMargin());
		}

		//system-distance
		Float mxlSystemDistance = mxlSystemLayout.getSystemDistance();
		if (mxlSystemDistance != null) {
			systemLayout.setDistance(tenthMm * mxlSystemDistance);
		}

		//top-system-distance
		Float topSystemDistance = null;
		Float xmlTopSystemDistance = mxlSystemLayout.getTopSystemDistance();
		if (xmlTopSystemDistance != null)
			topSystemDistance = tenthMm * xmlTopSystemDistance.floatValue();

		return new Value(systemLayout, topSystemDistance);
	}

}
