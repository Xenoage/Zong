package com.xenoage.zong.musiclayout.layouter.verticalframefilling;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.utils.collections.CList;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.FrameArrangement;
import com.xenoage.zong.musiclayout.SystemArrangement;

/**
 * This vertical frame filling strategy
 * increases the distance of the systems
 * of the given {@link FrameArrangement} so that the
 * vertical space is completely used
 * and returns the result.
 * 
 * @author Andreas Wenger
 */
public class FillPageVerticalFrameFillingStrategy
	implements VerticalFrameFillingStrategy {

	public static final FillPageVerticalFrameFillingStrategy instance =
		new FillPageVerticalFrameFillingStrategy();


	@Override public FrameArrangement computeFrameArrangement(FrameArrangement frameArr, Score score) {
		FrameArrangement ret = frameArr;
		//if there is no or only one system, do nothing
		if (frameArr.getSystems().size() > 1) {
			//compute remaining space
			SystemArrangement lastSystem = frameArr.getSystems().getLast();
			float lastSystemEndY = lastSystem.getOffsetY() + lastSystem.getHeight();
			float remainingSpace = frameArr.getUsableSize().height - lastSystemEndY;
			//compute additional space between the systems
			float additionalSpace = remainingSpace / (frameArr.getSystems().size() - 1);
			//compute new y-offsets
			CList<SystemArrangement> systemArrs = clist();
			for (int i : range(frameArr.getSystems())) {
				SystemArrangement system = frameArr.getSystems().get(i);
				system = system.withOffsetY(system.getOffsetY() + i * additionalSpace);
				systemArrs.add(system);
			}
			ret = new FrameArrangement(systemArrs.close(), frameArr.getUsableSize());
		}
		return ret;
	}

}
