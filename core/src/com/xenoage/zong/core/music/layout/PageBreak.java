package com.xenoage.zong.core.music.layout;


/**
 * Force or prohibit a page break.
 * 
 * When using a frame based layout (e.g. multiple scores per page),
 * this means a frame break, not always a page break.
 * 
 * @author Andreas Wenger
 */
public enum PageBreak {
	/** Force break. */
	NewPage,
	/** Prohibit break. */
	NoNewPage;
}
