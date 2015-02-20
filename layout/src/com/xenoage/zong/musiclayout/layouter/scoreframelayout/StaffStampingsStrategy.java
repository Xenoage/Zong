package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.collections.CollectionUtils.llist;
import static com.xenoage.utils.kernel.Range.range;

import java.util.LinkedList;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.StaffStampings;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * This strategy creates the staves of all systems of a
 * given {@link FrameSpacing}.
 * 
 * @author Andreas Wenger
 */
public class StaffStampingsStrategy {

	public StaffStampings createStaffStampings(Score score, FrameSpacing frameArr) {
		int systemsCount = frameArr.getSystems().size();
		int stavesCount = score.getStavesCount();
		LinkedList<StaffStamping> allStaves = llist();

		//go through the systems
		for (int iSystem : range(systemsCount)) {
			SystemSpacing system = frameArr.getSystems().get(iSystem);
			float systemXOffset = system.getMarginLeftMm();
			
			//create staves of the system
			float yOffset = system.getOffsetYMm();
			for (int iStaff : range(stavesCount)) {
				yOffset += system.getStaffDistanceMm(iStaff);
				StaffStamping staff = new StaffStamping(system, iStaff,
					new Point2f(systemXOffset, yOffset), system.widthMm, 5, score.getInterlineSpace(iStaff));
				allStaves.add(staff);
				yOffset += system.getStaffHeightMm(iStaff);
			}
		}

		return new StaffStampings(allStaves, systemsCount, stavesCount);
	}

}
