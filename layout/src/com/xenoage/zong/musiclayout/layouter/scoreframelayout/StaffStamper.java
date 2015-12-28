package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;

import java.util.List;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.StaffStampings;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * Creates the {@link StaffStampings} for a {@link FrameSpacing}.
 * 
 * @author Andreas Wenger
 */
public class StaffStamper {
	
	public static final StaffStamper staffStamper = new StaffStamper();
	

	public StaffStampings createStaffStampings(Score score, FrameSpacing frame) {
		int systemsCount = frame.getSystems().size();
		int stavesCount = score.getStavesCount();
		List<StaffStamping> allStaves = alist(systemsCount * stavesCount);

		//go through the systems
		for (int iSystem : range(systemsCount)) {
			SystemSpacing system = frame.getSystems().get(iSystem);
			float systemXOffset = system.getMarginLeftMm();
			
			//create staves of the system
			float yOffset = system.getOffsetYMm();
			for (int iStaff : range(stavesCount)) {
				yOffset += system.getStaffDistanceMm(iStaff);
				int linesCount = score.getStaff(iStaff).getLinesCount();
				float interlineSpace = score.getInterlineSpace(iStaff);
				StaffStamping staff = new StaffStamping(system, iStaff,
					new Point2f(systemXOffset, yOffset), system.widthMm, linesCount, interlineSpace);
				allStaves.add(staff);
				yOffset += system.getStaffHeightMm(iStaff);
			}
		}

		return new StaffStampings(allStaves, systemsCount, stavesCount);
	}

}
