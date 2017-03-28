package com.xenoage.zong.android.view.provider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xenoage.utils.android.AndroidPlatformUtils;
import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.android.R;
import com.xenoage.zong.android.view.PageView;
import com.xenoage.zong.android.view.ScoreView;
import com.xenoage.zong.documents.ScoreDoc;

import static com.xenoage.utils.android.AndroidPlatformUtils.androidPlatformUtils;

/**
 * Creates data for {@link ScoreView},
 * e.g. from {@link ScoreDoc}s documents or PDF scores.
 *
 * @author Andreas Wenger
 */
public abstract class ScoreViewProvider {

	/**
	 * Gets the number of pages.
	 */
	public abstract int getPagesCount();

	/**
	 * Creates the view of the page with the given index.
	 */
	public abstract PageView createPageView(int pageIndex);

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
