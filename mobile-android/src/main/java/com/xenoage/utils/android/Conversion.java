package com.xenoage.utils.android;

import android.graphics.RectF;

import com.xenoage.utils.math.geom.Rectangle2f;

public class Conversion {

	public static RectF rectF(Rectangle2f r) {
		return new RectF(r.x1(), r.y1(), r.x2(), r.y2());
	}

}
