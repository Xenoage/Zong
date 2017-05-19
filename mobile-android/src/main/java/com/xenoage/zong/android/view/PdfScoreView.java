package com.xenoage.zong.android.view;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.xenoage.utils.math.geom.Size2i;

import static com.xenoage.utils.kernel.Range.range;


/**
 * {@link ScoreView} for PDF documents.
 *
 * @author Andreas Wenger
 */
public class PdfScoreView
		extends ScoreView {

	public PdfScoreView(String filepath, Size2i screenSizePx, float zoom) {
		updateScreen(screenSizePx, zoom);
	}

	@Override public void updateScreen(Size2i screenSizePx, float zoom) {
	}

	@Override public int getPagesCount() {
		return 1;
	}

	@Override public PageView getPage(int pageIndex) {
		Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
		for (int x : range(200))
			for (int y : range(200))
				bitmap.setPixel(x, y, Color.RED);
		return new PageView(bitmap);
	}

}
