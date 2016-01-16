package com.xenoage.zong.musiclayout.stamper;

import com.xenoage.utils.annotations.Optimized;
import com.xenoage.utils.annotations.Optimized.Reason;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Information stampers generally need.
 * 
 * @author Andreas Wenger
 */
public class StamperContext {
	
	/** The general layouter context. */
	public Context layouter;
	/** The parent staff, where the next element or groups of elements (voice, measure)
	 * have to be stamped on, or null. */
	public StaffStamping staff;
	/** The index of the current system on the frame. */
	public int systemIndex;
	/** All the available notations. */
	public Notations notations;
	
	
	/**
	 * Convenience method to get the current {@link MP}.
	 * During the stamping process, this {@link MP} may not be precise because of
	 * performance reasons. But at least the staff and measure should be correct.
	 */
	@Optimized(Reason.Performance)
	public MP getMp() {
		return layouter.mp;
	}
	
	/**
	 * Convenience method to get the current staff index.
	 */
	public int getStaffIndex() {
		return layouter.mp.staff;
	}
	
	/**
	 * Convenience method to get the {@link Notation} for the given
	 * element on the current staff.
	 */
	public Notation getNotation(MusicElement element) {
		return notations.get(element, getStaffIndex());
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
