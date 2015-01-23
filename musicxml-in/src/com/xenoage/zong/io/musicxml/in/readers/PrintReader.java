package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.iterators.It.it;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.format.Break;
import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.core.music.layout.PageBreak;
import com.xenoage.zong.core.music.layout.SystemBreak;
import com.xenoage.zong.musicxml.types.MxlPrint;
import com.xenoage.zong.musicxml.types.MxlStaffLayout;
import com.xenoage.zong.musicxml.types.MxlSystemLayout;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintAttributes;
import com.xenoage.zong.musicxml.types.enums.MxlYesNo;
import com.xenoage.zong.musicxml.types.groups.MxlLayout;

/**
 * Reads a {@link MxlPrint} to the {@link Context}.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class PrintReader {

	private final MxlPrint mxlPrint;
	
	
	public void readToContext(Context context) {
		ScoreHeader header = context.getScore().getHeader();
		int measure = context.getMp().measure;

		//system and page break
		Break break_ = readBreak();
		if (break_ != null) {
			//MusicXML print is in the first broken measure, but we
			//store the break in the last measure before the break (thus -1)
			int breakMeasure = measure - 1;
			context.writeColumnElement(break_, breakMeasure);
		}

		//we assume that custom system layout information is just used in combination with
		//forced system/page breaks. so we ignore system-layout elements which are not combined
		//with system/page breaks.
		//the first measure of a score is also ok.
		if (measure == 0 || break_ != null) {

			//first page or new page?
			boolean isPageBreak = break_ != null && break_.getPageBreak() == PageBreak.NewPage;
			boolean isPageStarted = (measure == 0 || isPageBreak);
			if (isPageBreak) {
				//increment page index
				context.incPageIndex();
			}

			//first system or new system?
			boolean isSystemBreak = isPageBreak ||
				(break_ != null && break_.getSystemBreak() == SystemBreak.NewSystem);
			if (isSystemBreak) {
				//increment system index 
				context.incSystemIndex();
			}

			//read system layout, if there
			SystemLayout systemLayout = readSystemLayout(isPageStarted, context.getTenthMm());
			if (systemLayout != null)
				header.setSystemLayout(context.getSystemIndex(), systemLayout);
			
			//staff layouts
			MxlLayout mxlLayout = mxlPrint.getLayout();
			if (mxlLayout != null) {
				for (MxlStaffLayout mxlStaffLayout : it(mxlLayout.getStaffLayouts())) {
					int staffIndex = mxlStaffLayout.getNumberNotNull() - 1;
					//get system layout. if it does not exist yet, create it
					systemLayout = header.getSystemLayout(context.getSystemIndex());
					if (systemLayout == null) {
						systemLayout = new SystemLayout();
						header.setSystemLayout(context.getSystemIndex(), systemLayout);
					}
					StaffLayout staffLayout = new StaffLayoutReader(mxlStaffLayout, context.getTenthMm()).read();
					systemLayout.setStaffLayout(
						context.getPartStaffIndices().getStart() + staffIndex, staffLayout);
				}
			}

		}
	}
	
	private Break readBreak() {
		MxlPrintAttributes mxlPA = mxlPrint.getPrintAttributes();
		SystemBreak systemBreak = readSystemBreak(mxlPA.getNewSystem());
		PageBreak pageBreak = readPageBreak(mxlPA.getNewPage());
		if (systemBreak != null || pageBreak != null)
			return new Break(pageBreak, systemBreak);
		return null;
	}
	
	@MaybeNull private SystemBreak readSystemBreak(MxlYesNo mxlSystemBreak) {
		if (mxlSystemBreak == MxlYesNo.Yes)
			return SystemBreak.NewSystem;
		else if (mxlSystemBreak == MxlYesNo.No)
			return SystemBreak.NoNewSystem;
		else
			return null;
	}
	
	@MaybeNull private PageBreak readPageBreak(MxlYesNo mxlPageBreak) {
		if (mxlPageBreak == MxlYesNo.Yes)
			return PageBreak.NewPage;
		else if (mxlPageBreak == MxlYesNo.No)
			return PageBreak.NoNewPage;
		else
			return null;
	}
	
	private SystemLayout readSystemLayout(boolean isPageStarted, float tenthMm) {
		MxlLayout mxlLayout = mxlPrint.getLayout();
		if (mxlLayout != null) {
			MxlSystemLayout mxlSystemLayout = mxlLayout.getSystemLayout();
			if (mxlSystemLayout != null) {
				SystemLayoutReader systemLayoutReader = new SystemLayoutReader(mxlSystemLayout, tenthMm);
				SystemLayout systemLayout = systemLayoutReader.read();
				Float topSystemDistance = systemLayoutReader.getTopSystemDistance();
				//for first systems on a page, use top-system-distance
				if (isPageStarted && topSystemDistance != null)
					systemLayout.setDistance(topSystemDistance);
				return systemLayout;
			}
		}
		return null;
	}
	
}
