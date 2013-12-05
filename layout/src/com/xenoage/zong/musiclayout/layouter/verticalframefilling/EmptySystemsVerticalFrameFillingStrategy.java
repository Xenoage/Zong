package com.xenoage.zong.musiclayout.layouter.verticalframefilling;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.musiclayout.FrameArrangement;
import com.xenoage.zong.musiclayout.SystemArrangement;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;

/**
 * This vertical frame filling strategy
 * adds empty systems to the current frame
 * until the page is filled and returns
 * the result.
 * 
 * @author Andreas Wenger
 */
public class EmptySystemsVerticalFrameFillingStrategy
	implements VerticalFrameFillingStrategy {

	public static final EmptySystemsVerticalFrameFillingStrategy instance =
		new EmptySystemsVerticalFrameFillingStrategy();


	/**
	 * Fill frame with empty systems.
	 */
	@Override public FrameArrangement computeFrameArrangement(FrameArrangement frameArr, Score score) {
		Size2f usableSize = frameArr.getUsableSize();
		FrameArrangement ret = frameArr;

		//compute remaining space
		float remainingSpace = usableSize.height;
		float offsetY = 0;
		if (frameArr.getSystems().size() > 0) {
			SystemArrangement lastSystem = frameArr.getSystems().getLast();
			offsetY = lastSystem.getOffsetY() + lastSystem.getHeight();
			remainingSpace -= offsetY;
		}

		//compute height of an additional system
		SystemLayout defaultSystemLayout = score.getFormat().getSystemLayout();
		float defaultSystemDistance = defaultSystemLayout.getDistance();
		float defaultMargin = defaultSystemLayout.getMarginLeft() +
			defaultSystemLayout.getMarginRight();
		SystemArrangement newSystem = createEmptySystem(score, usableSize.width, 0);
		float newSystemHeight = defaultSystemDistance + newSystem.getHeight();

		//add as many additional empty staves as possible
		int newSystemsCount = (int) (remainingSpace / newSystemHeight);
		if (newSystemsCount > 0) {
			//otherwise add the empty systems
			CList<SystemArrangement> newSystems = clist();
			newSystems.addAll(frameArr.getSystems());
			for (int i = frameArr.getSystems().size() - 1; i < newSystemsCount; i++) {
				newSystems.add(createEmptySystem(score, usableSize.width - defaultMargin, offsetY +
					defaultSystemDistance));
				offsetY += newSystemHeight;
			}
			ret = new FrameArrangement(newSystems.close(), frameArr.getUsableSize());
		}

		return ret;
	}

	/**
	 * Creates an additional system for the given {@link Score} with
	 * the given width and y-offset in mm and returns it.
	 */
	private SystemArrangement createEmptySystem(Score score, float width, float offsetY) {
		float[] staffHeights = new float[score.getStavesCount()];
		float[] staffDistances = new float[score.getStavesCount() - 1];
		//compute staff heights
		for (int iStaff : range(score.getStavesCount())) {
			Staff staff = score.getStaff(iStaff);
			staffHeights[iStaff] = (staff.getLinesCount() - 1) * score.getInterlineSpace(iStaff);
		}
		//compute staff distances 
		for (int iStaff : range(1, score.getStavesCount() - 1)) {
			staffDistances[iStaff - 1] = score.getFormat().getStaffLayoutNotNull(iStaff).getDistance();
		}
		//create and returns system
		SystemLayout defaultSystemLayout = score.getFormat().getSystemLayout();
		return new SystemArrangement(-1, -1, CList.<ColumnSpacing> ilist(),
			defaultSystemLayout.getMarginLeft(), defaultSystemLayout.getMarginRight(), width,
			staffHeights, staffDistances, offsetY);
	}

}
