package com.xenoage.zong.musiclayout.stamper;

import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.StaffStampings;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.common.CommonSymbol;

import static com.xenoage.zong.core.position.MP.atMeasure;

/**
 * Information stampers generally need.
 * 
 * @author Andreas Wenger
 */
public class StamperContext {
	
	/** The general layouter context. */
	public Context layouter;
	/** The index of the current system on the frame. */
	public int systemIndex;
	/** The current staff index. */
	public int staffIndex;
	/** The current measure index. */
	public int measureIndex;

	/** All the available staff stampings. */
	public StaffStampings staffStampings;
	/** All the available notations. */
	public Notations notations;


	/**
	 * Gets the current measure.
	 */
	public Measure getCurrentMeasure() {
		return layouter.score.getMeasure(atMeasure(staffIndex, measureIndex));
	}

	/**
	 * Gets the current measure column header.
	 */
	public ColumnHeader getCurrentColumnHeader() {
		return layouter.score.getColumnHeader(measureIndex);
	}

	/**
	 * Gets the current staff stamping.
	 */
	public StaffStamping getCurrentStaffStamping() {
		return staffStampings.get(systemIndex, staffIndex);
	}

	/**
	 * Gets the staff stamping for the staff with the given index within the
	 * current system.
	 */
	public StaffStamping getStaffStamping(int staffIndex) {
		return staffStampings.get(systemIndex, staffIndex);
	}
	
	/**
	 * Convenience method to get the {@link Notation} for the given
	 * element on the current staff.
	 */
	public Notation getNotation(MusicElement element) {
		return notations.get(element, staffIndex);
	}
	
	/**
	 * Convenience method to get a symbol.
	 */
	public Symbol getSymbol(CommonSymbol commonSymbol) {
		return layouter.symbols.getSymbol(commonSymbol);
	}
	
	/**
	 * Convenience method to get the settings.
	 */
	public LayoutSettings getSettings() {
		return layouter.settings;
	}
	
}
