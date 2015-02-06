package com.xenoage.zong.musiclayout.layouter.verticalframefilling;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.getLast;
import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * This vertical frame filling strategy
 * increases the distance of the systems
 * of the given {@link FrameSpacing} so that the
 * vertical space is completely used
 * and returns the result.
 * 
 * @author Andreas Wenger
 */
public class FillPageVerticalFrameFillingStrategy
	implements VerticalFrameFillingStrategy {

	public static final FillPageVerticalFrameFillingStrategy instance =
		new FillPageVerticalFrameFillingStrategy();


	@Override public FrameSpacing computeFrameArrangement(FrameSpacing frameArr, Score score) {
		FrameSpacing ret = frameArr;
		//if there is no or only one system, do nothing
		if (frameArr.getSystems().size() > 1) {
			//compute remaining space
			SystemSpacing lastSystem = getLast(frameArr.getSystems());
			float lastSystemEndY = lastSystem.getOffsetYMm() + lastSystem.getHeight();
			float remainingSpace = frameArr.getUsableSizeMm().height - lastSystemEndY;
			//compute additional space between the systems
			float additionalSpace = remainingSpace / (frameArr.getSystems().size() - 1);
			//compute new y-offsets
			CList<SystemSpacing> systemArrs = clist();
			for (int i : range(frameArr.getSystems())) {
				SystemSpacing system = frameArr.getSystems().get(i);
				system.offsetYMm += i * additionalSpace;
			}
			ret = new FrameSpacing(systemArrs.close(), frameArr.getUsableSizeMm());
		}
		return ret;
	}

}
