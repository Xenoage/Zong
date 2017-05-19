package com.xenoage.zong.android.view;

import android.graphics.Bitmap;

/**
 * View of a page of a score.
 * It consists of a bitmap.
 *
 * @author Andreas Wenger
 */
public class PageView {

	public Bitmap pageImage;

	public PageView(Bitmap pageImage) {
		this.pageImage = pageImage;
	}

}
