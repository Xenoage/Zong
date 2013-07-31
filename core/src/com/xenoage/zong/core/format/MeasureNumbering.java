package com.xenoage.zong.core.format;

/**
 * Styles for numbering measures.
 *  
 * @author Andreas Wenger
 */
public enum MeasureNumbering {
	/** Show a number at each measure (except the first one). */
	Measure,
	/** Show a measure number at the beginning of each system (except the first one). */
	System,
	/** Don't show measure numbers. */
	None;
}
