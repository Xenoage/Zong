package com.xenoage.zong.io.musicxml.in.readers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.musicxml.types.MxlSystemLayout;
import com.xenoage.zong.musicxml.types.MxlSystemMargins;

/**
 * Reads a {@link SystemLayout} from a {@link MxlSystemLayout}.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class SystemLayoutReader {

	private final MxlSystemLayout mxlSystemLayout;
	private final float tenthMm;
	
	private SystemLayout systemLayout;
	@Getter private Float topSystemDistance;


	public SystemLayout read() {
		systemLayout = new SystemLayout();
		readSystemMargins();
		readSystemDistance();
		readTopSystemDistance();
		return systemLayout;
	}
	
	private void readSystemMargins() {
		MxlSystemMargins mxlMargins = mxlSystemLayout.getSystemMargins();
		if (mxlMargins != null) {
			systemLayout.setMarginLeft(tenthMm * mxlMargins.getLeftMargin());
			systemLayout.setMarginRight(tenthMm * mxlMargins.getRightMargin());
		}
	}

	private void readSystemDistance() {
		Float mxlSystemDistance = mxlSystemLayout.getSystemDistance();
		if (mxlSystemDistance != null) {
			systemLayout.setDistance(tenthMm * mxlSystemDistance);
		}
	}

	private void readTopSystemDistance() {
		topSystemDistance = null;
		Float mxlTopSystemDistance = mxlSystemLayout.getTopSystemDistance();
		if (mxlTopSystemDistance != null)
			topSystemDistance = tenthMm * mxlTopSystemDistance.floatValue();
	}

}
