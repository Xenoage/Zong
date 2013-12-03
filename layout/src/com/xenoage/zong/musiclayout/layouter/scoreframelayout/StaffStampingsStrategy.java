package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.llist;
import static com.xenoage.utils.kernel.Range.range;

import java.util.ArrayList;
import java.util.LinkedList;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.FrameArrangement;
import com.xenoage.zong.musiclayout.MeasureMarks;
import com.xenoage.zong.musiclayout.StaffMarks;
import com.xenoage.zong.musiclayout.SystemArrangement;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.StaffStampings;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * This strategy creates the staves of all systems of a
 * given {@link FrameArrangement}.
 * 
 * @author Andreas Wenger
 */
public class StaffStampingsStrategy {

	public StaffStampings createStaffStampings(Score score, FrameArrangement frameArr) {
		int systemsCount = frameArr.getSystems().size();
		int stavesCount = score.getStavesCount();
		LinkedList<StaffStamping> allStaves = llist();

		//go through the systems
		for (int iSystem : range(systemsCount)) {
			SystemArrangement system = frameArr.getSystems().get(iSystem);

			//create staves of the system
			StaffStamping[] systemStaves = new StaffStamping[stavesCount];
			float systemXOffset = system.getMarginLeft();
			float yOffset = system.getOffsetY();
			for (int iStaff = 0; iStaff < stavesCount; iStaff++) {
				yOffset += system.getStaffDistance(iStaff);
				StaffStamping staff = new StaffStamping(new Point2f(systemXOffset, yOffset), system.width,
					5, score.getInterlineSpace(iStaff), null);
				systemStaves[iStaff] = staff;
				yOffset += system.getStaffHeight(iStaff);
			}

			//create position marks
			IList<ColumnSpacing> css = system.getColumnSpacings();
			int measuresCount = system.getColumnSpacings().size();
			float[] measureMarkersLeft = new float[measuresCount];
			float[] measureMarkersLeading = new float[measuresCount];
			float[] measureMarkersRight = new float[measuresCount];
			float xOffset = 0; //start at the beginning of the staff
			for (int iMeasure : range(measuresCount)) {
				float xLeft = xOffset;
				float xLeading = xLeft + css.get(iMeasure).getLeadingWidth();
				xOffset += system.getColumnSpacings().get(iMeasure).getWidth();
				float xRight = xOffset;
				//mark measure offset
				measureMarkersLeft[iMeasure] = xLeft;
				measureMarkersLeading[iMeasure] = xLeading;
				measureMarkersRight[iMeasure] = xRight;
			}

			//compute beat positions
			for (int iStaff : range(stavesCount)) {

				//collect beat offsets
				xOffset = 0; //start at the beginning of the staff
				ArrayList<IList<BeatOffset>> staffBeats = alist(measuresCount);
				BeatOffset lastBeatOffset;
				for (int iMeasure : range(measuresCount)) {
					ColumnSpacing cs = css.get(iMeasure);
					CList<BeatOffset> measureBeats = clist();
					lastBeatOffset = null;
					for (Fraction beat : cs.getMeasureSpacings().get(iStaff).getUsedBeats()) {
						BeatOffset bo = cs.getBeatOffset(beat);
						if (bo == null)
							bo = lastBeatOffset; //fallback: reuse last offset (happens currently only for ActorPreludeSample)
						if (bo == null)
							throw new IllegalStateException("No offset defined for beat " + beat + " in system " +
								iSystem + ", staff " + iStaff + ", system measure " + iMeasure +
								", global measure " + (system.getStartMeasureIndex() + iMeasure));
						bo = bo.withOffsetMm(xOffset + bo.getOffsetMm() + cs.getLeadingWidth());
						measureBeats.add(bo);
						lastBeatOffset = bo;
					}
					staffBeats.add(measureBeats.close());
					xOffset += cs.getWidth();
				}

				//create StaffMarks for each staff
				CList<MeasureMarks> measureMarks = clist();
				for (int iMeasure : range(measuresCount)) {
					IList<BeatOffset> beatOffsets = staffBeats.get(iMeasure);
					if (beatOffsets.size() == 0) {
						throw new IllegalStateException("No beat markers for measure " + iMeasure +
							" in staff " + iStaff + " in system " + iSystem +
							" on frame beginning with measure " + frameArr.getStartMeasureIndex());
					}
					else {
						measureMarks.add(new MeasureMarks(iMeasure, measureMarkersLeft[iMeasure],
							measureMarkersLeading[iMeasure], measureMarkersRight[iMeasure], beatOffsets));
					}
				}
				StaffMarks staffMarks = new StaffMarks(iSystem, iStaff, system.getStartMeasureIndex(),
					measureMarks.close());

				StaffStamping ss = systemStaves[iStaff].withStaffMarks(staffMarks);
				systemStaves[iStaff] = ss;
				allStaves.add(ss);
			}

		}

		return new StaffStampings(allStaves, systemsCount, stavesCount);
	}

}
