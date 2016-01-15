package com.xenoage.utils.android;

import android.graphics.Rect;

import com.xenoage.utils.math.geom.Rectangle2f;

public class Conversion {

	public static Rect rect(Rectangle2f r) {
		return new Rect((int) (r.x1() + 0.5f), (int) (r.y1() + 0.5f), (int) (r.x2() + 0.5f),
			(int) (r.y2() + 0.5f));
	}

}
