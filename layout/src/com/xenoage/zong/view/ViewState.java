package com.xenoage.zong.view;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2i;

/**
 * The state of a view consists of its size in px,
 * the scaling factor (zoom) and the scroll position in mm.
 * 
 * @author Andreas Wenger
 */
public class ViewState {

	public final Size2i sizePx;
	public final float scaling;
	public final Point2f scrollMm;


	public ViewState(Size2i sizePx, float scaling, Point2f scrollMm) {
		this.sizePx = sizePx;
		this.scaling = scaling;
		this.scrollMm = scrollMm;
	}

}
