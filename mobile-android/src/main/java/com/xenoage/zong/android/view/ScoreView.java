package com.xenoage.zong.android.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.android.R;

import static com.xenoage.utils.android.AndroidPlatformUtils.androidPlatformUtils;

/**
 * View of a score, consisting of pages.
 *
 * @author Andreas Wenger
 */
public abstract class ScoreView {

	/**
	 * Gets the number of pages in this score.
	 */
	public abstract int getPagesCount();

	/**
	 * Gets the page with the given index.
	 */
	public abstract PageView getPage(int pageIndex);

	/**
	 * Call this method when the screen size or zoom has been changed.
	 */
	public abstract void updateScreen(Size2i screenSizePx, float zoom);

	/**
	 * Gets the background image for the given page.
	 */
	Bitmap getBackgroundImage(int pageIndex) {
		int imageId = getBackgroundImageId(pageIndex);
		return BitmapFactory.decodeResource(androidPlatformUtils().getResources(), imageId);
	}

	/**
	 * Gets the background image ID for the given page.
	 */
	private int getBackgroundImageId(int pageIndex) {
		if (getPagesCount() == 1)
			return R.drawable.paper_single;
		else if (pageIndex == 0)
			return R.drawable.paper_first;
		else if (pageIndex == getPagesCount() - 1)
			return R.drawable.paper_last;
		else
			return R.drawable.paper_middle;
	}

}
