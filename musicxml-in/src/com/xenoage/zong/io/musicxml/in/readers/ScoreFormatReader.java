package com.xenoage.zong.io.musicxml.in.readers;

import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.font.FontInfo;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.musicxml.types.MxlDefaults;
import com.xenoage.zong.musicxml.types.MxlLyricFont;
import com.xenoage.zong.musicxml.types.MxlScaling;
import com.xenoage.zong.musicxml.types.MxlStaffLayout;
import com.xenoage.zong.musicxml.types.MxlSystemLayout;
import com.xenoage.zong.musicxml.types.groups.MxlLayout;

/**
 * Reads a {@link ScoreFormat} from a {@link MxlDefaults}.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class ScoreFormatReader {

	@MaybeNull private final MxlDefaults mxlDefaults;
	
	private MxlLayout mxlLayout;
	private ScoreFormat scoreFormat;
	private float tenthsMm;
	
	
	@NonNull public ScoreFormat read() {
		scoreFormat = new ScoreFormat();
		if (mxlDefaults != null) {
			readInterlineSpace();
			mxlLayout = mxlDefaults.getLayout();
			if (mxlLayout != null) {
				readSystemLayout();
				readStaffLayouts();
			}
			readLyricsFont();
		}
		return scoreFormat;
	}

	private void readInterlineSpace() {
		MxlScaling mxlScaling = mxlDefaults.getScaling();
		if (mxlScaling != null) {
			float millimeters = mxlScaling.getMillimeters();
			float tenths = mxlScaling.getTenths();
			float interlineSpace = millimeters * 10 / tenths;
			scoreFormat.setInterlineSpace(interlineSpace);
			tenthsMm = interlineSpace / 10;	
		}
		else {
			tenthsMm = ScoreFormat.defaultInterlineSpace / 10;
		}
	}
	
	private void readSystemLayout() {
		MxlSystemLayout mxlSystemLayout = mxlLayout.getSystemLayout();
		if (mxlSystemLayout == null)
			return;
		
		SystemLayoutReader systemLayoutReader = new SystemLayoutReader(mxlSystemLayout, tenthsMm);
		scoreFormat.setSystemLayout(systemLayoutReader.read());
		Float topSystemDistance = systemLayoutReader.getTopSystemDistance();
		if (topSystemDistance != null)
			scoreFormat.setTopSystemDistance(topSystemDistance);
	}

	private void readStaffLayouts() {
		if (mxlLayout.getStaffLayouts() == null)
			return;

		for (MxlStaffLayout mxlStaffLayout : mxlLayout.getStaffLayouts()) {
			StaffLayoutReader staffLayoutReader = new StaffLayoutReader(mxlStaffLayout, tenthsMm);
			StaffLayout staffLayout = staffLayoutReader.read();
			Integer number = staffLayoutReader.getNumber();
			if (number == null)
				scoreFormat.setStaffLayoutOther(staffLayout);
			else
				scoreFormat.setStaffLayout(number - 1, staffLayout);
		}
	}

	private void readLyricsFont() {
		//only one lyric font is supported
		MxlLyricFont mxlLyricFont = mxlDefaults.getLyricFont();
		if (mxlLyricFont != null) {
			FontInfo defaultLyricFont = new FontInfoReader(mxlLyricFont.getFont(),
				scoreFormat.getLyricFont()).read();
			if (defaultLyricFont != null)
				scoreFormat.setLyricFont(defaultLyricFont);
		}
	}
}
