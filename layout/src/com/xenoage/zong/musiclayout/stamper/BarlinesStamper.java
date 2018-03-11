package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.core.text.FormattedText.fText;

import java.util.List;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.MeasureNumbering;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.stampings.BarlineStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StaffTextStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;

/**
 * Creates the {@link BarlineStamping}s and bar number stampings
 * for a given system.
 * 
 * @author Andreas Wenger
 */
public class BarlinesStamper {
	
	public static final BarlinesStamper barlinesStamper = new BarlinesStamper();
	
	
	public List<Stamping> stamp(SystemSpacing system, List<StaffStamping> systemStaves, Score score) {
		
		List<Stamping> ret = alist();
		StaffStamping firstStaff = getFirst(systemStaves);
		int stavesCount = systemStaves.size();
		int systemIndex = system.getSystemIndexInFrame();
		float xOffset = firstStaff.positionMm.x;
		
		//common barline at the beginning, when system has at least one measure
		if (system.columns.size() > 0) {
			ret.add(new BarlineStamping(Barline.barlineRegular(), systemStaves, xOffset,
				BarlineGroup.Style.Common));
		}
		
		//barlines within the system and measure numbers
		for (int iMeasure : range(system.columns)) {
			float xLeft = xOffset;
			
			//measure numbering
			MeasureNumbering measureNumbering = score.getFormat().getMeasureNumbering();
			int globalMeasureIndex = system.getStartMeasure() + iMeasure;
			boolean showMeasureNumber = false;
			if (measureNumbering == MeasureNumbering.System) {
				//measure number at the beginning of each system (except the first one)
				showMeasureNumber = (iMeasure == 0 && globalMeasureIndex > 0);
			}
			else if (measureNumbering == MeasureNumbering.Measure) {
				//measure number at each measure (except the first one)
				showMeasureNumber = (globalMeasureIndex > 0);
			}
			if (showMeasureNumber) {
				FormattedText text = fText("" + (globalMeasureIndex + 1),
					new FormattedTextStyle(8), Alignment.Left);
				ret.add(new StaffTextStamping(text, sp(xLeft, firstStaff.linesCount * 2),
					firstStaff, null));
			}
			
			//for the first measure in the system: begin after leading spacing
			if (iMeasure == 0)
				xLeft += system.columns.get(iMeasure).getLeadingWidthMm();
			xOffset += system.columns.get(iMeasure).getWidthMm();
			float xRight = xOffset;
			
			//regard the groups of the score
			for (int iStaff : range(stavesCount)) {
				ColumnHeader columnHeader = score.getColumnHeader(globalMeasureIndex);
				BarlineGroup.Style barlineGroupStyle = BarlineGroup.Style.Single;
				BarlineGroup group = score.getStavesList().getBarlineGroupByStaff(iStaff);
				if (group != null)
					barlineGroupStyle = group.getStyle();
				List<StaffStamping> groupStaves = getBarlineGroupStaves(systemStaves, group);
				
				//start barline
				Barline startBarline = columnHeader.getStartBarline();
				if (startBarline != null) {
					//don't draw a regular barline at the left side of first measure of a system
					if ((startBarline.getStyle() == BarlineStyle.Regular && systemIndex == 0) == false)
						ret.add(new BarlineStamping(startBarline, groupStaves, xLeft,
							barlineGroupStyle));
				}
				
				//end barline. if none is set, use a regular one.
				Barline endBarline = columnHeader.getEndBarline();
				if (endBarline == null)
					endBarline = Barline.barlineRegular();
				ret.add(new BarlineStamping(endBarline, groupStaves, xRight, barlineGroupStyle));
				
				//middle barlines
				for (BeatE<Barline> middleBarline : columnHeader.getMiddleBarlines()) {
					ret.add(new BarlineStamping(middleBarline.getElement(), groupStaves, xLeft +
						system.columns.get(iMeasure).getBarlineOffsetMm(middleBarline.getBeat()),
						barlineGroupStyle));
				}
				
				//go to next group
				if (group != null)
					iStaff = group.getStaves().getStop();
			}
		}
		
		return ret;
	}
	
	/**
	 * Gets the staves of the given group, using the given list of all staves.
	 * If the given group is null, all staves are returned.
	 */
	private List<StaffStamping> getBarlineGroupStaves(List<StaffStamping> systemStaves,
		BarlineGroup barlineGroup) {
		if (barlineGroup == null)
			return systemStaves;
		else {
			//use efficient sublist
			return systemStaves.subList(barlineGroup.getStaves().getStart(),
				barlineGroup.getStaves().getStop() + 1);
		}
	}

}
