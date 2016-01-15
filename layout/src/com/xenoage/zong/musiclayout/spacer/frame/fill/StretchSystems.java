package com.xenoage.zong.musiclayout.spacer.frame.fill;

import static com.xenoage.utils.collections.CollectionUtils.getLast;
import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * Increases the distance of the systems of the given {@link FrameSpacing}
 * so that the vertical space is completely used.
 * 
 * @author Andreas Wenger
 */
public class StretchSystems
	implements FrameFiller {

	public static final StretchSystems stretchSystems = new StretchSystems();


	@Override public void compute(FrameSpacing frame, Score score) {
		//if there is no or only one system, do nothing
		int systemsCount = frame.systems.size();
		if (systemsCount > 1) {
			//compute remaining space
			SystemSpacing lastSystem = getLast(frame.systems);
			float lastSystemEndY = lastSystem.getOffsetYMm() + lastSystem.getHeightMm();
			float remainingSpace = frame.usableSizeMm.height - lastSystemEndY;
			//compute additional space between the systems
			float additionalSpace = remainingSpace / (systemsCount - 1);
			//compute new y-offsets
			for (int i : range(systemsCount)) {
				SystemSpacing system = frame.systems.get(i);
				system.offsetYMm += i * additionalSpace;
			}
		}
	}

}
