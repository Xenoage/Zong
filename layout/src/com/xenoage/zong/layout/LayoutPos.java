package com.xenoage.zong.layout;

import com.xenoage.utils.math.geom.Point2f;

/**
 * Layout position.
 * 
 * This class contains a position within a layout:
 * a page index and coordinates relative to the
 * upper left corner in mm.
 * 
 * @author Andreas Wenger
 */
public final class LayoutPos {

	public final Layout layout;
	public final int pageIndex;
	public final Point2f position;


	public static LayoutPos layoutPos(Layout layout, int pageIndex, Point2f position) {
		return new LayoutPos(layout, pageIndex, position);
	}

	private LayoutPos(Layout layout, int pageIndex, Point2f position) {
		this.layout = layout;
		this.pageIndex = pageIndex;
		this.position = position;
	}

}
