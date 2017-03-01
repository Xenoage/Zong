package com.xenoage.zong.musiclayout.spacer.system;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.musiclayout.spacing.StavesSpacing;

import static com.xenoage.utils.kernel.Range.range;

/**
 * Arranges staves vertically within a system into a {@link StavesSpacing}.
 * 
 * @author Andreas Wenger
 */
public class StavesSpacer {
	
	public static final StavesSpacer stavesSpacer = new StavesSpacer();
	
	
	public StavesSpacing compute(Score score, int systemIndex) {
		//compute staff distances
		ScoreFormat scoreFormat = score.getFormat();
		ScoreHeader scoreHeader = score.getHeader();
		float[] distancesMm = new float[score.getStavesCount() - 1];
		for (int iStaff : range(1, distancesMm.length)) {
			StaffLayout staffLayout = scoreHeader.getStaffLayout(systemIndex, iStaff);
			float staffDistanceMm = 0;
			if (staffLayout != null) {
				//use custom staff distance
				staffDistanceMm = staffLayout.getDistance();
			}
			else {
				//use default staff distance
				staffDistanceMm = scoreFormat.getStaffLayoutNotNull(iStaff).getDistance();
			}
			distancesMm[iStaff - 1] = staffDistanceMm;
		}
		//create spacing
		StavesSpacing stavesSpacing = new StavesSpacing(score.getStavesList().getStaves(), distancesMm,
			scoreFormat.getInterlineSpace());
		return stavesSpacing;
	}

}
