package com.xenoage.zong.musiclayout.spacer.frame.fill;

import static com.xenoage.utils.collections.CollectionUtils.getLast;
import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * Adds empty systems to the current frame
 * until the page is filled.
 * 
 * TODO: test this class. since the staff is empty, the layouter
 * may have problems with it
 * 
 * @author Andreas Wenger
 */
public class EmptySystems
	implements FrameFiller {

	public static final EmptySystems emptySystems = new EmptySystems();


	@Override public void compute(FrameSpacing frame, Score score) {
		Size2f usableSize = frame.usableSizeMm;

		//compute remaining space
		float remainingSpace = usableSize.height;
		float offsetY = 0;
		if (frame.systems.size() > 0) {
			SystemSpacing lastSystem = getLast(frame.systems);
			offsetY = lastSystem.getOffsetYMm() + lastSystem.getHeight();
			remainingSpace -= offsetY;
		}

		//compute height of an additional system
		SystemLayout defaultSystemLayout = score.getFormat().getSystemLayout();
		float defaultSystemDistance = defaultSystemLayout.getDistance();
		float defaultMargin = defaultSystemLayout.getMarginLeft() +
			defaultSystemLayout.getMarginRight();
		SystemSpacing newSystem = createEmptySystem(score, usableSize.width, 0);
		float newSystemHeight = defaultSystemDistance + newSystem.getHeight();

		//add as many additional empty staves as possible
		int newSystemsCount = (int) (remainingSpace / newSystemHeight);
		for (int i : range(newSystemsCount)) {
			frame.systems.add(createEmptySystem(score, usableSize.width - defaultMargin,
				offsetY + (i * newSystemHeight) + defaultSystemDistance));
		}
	}

	/**
	 * Creates an additional system for the given {@link Score} with
	 * the given width and y-offset in mm and returns it.
	 */
	private SystemSpacing createEmptySystem(Score score, float width, float offsetY) {
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
		return new SystemSpacing(-1, -1, CList.<ColumnSpacing> ilist(),
			defaultSystemLayout.getMarginLeft(), defaultSystemLayout.getMarginRight(), width,
			staffHeights, staffDistances, offsetY);
	}

}
