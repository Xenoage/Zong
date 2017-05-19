package com.xenoage.zong.android.view;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.android.R;

import java.io.IOException;

import static com.xenoage.utils.android.AndroidPlatformUtils.androidPlatformUtils;
import static com.xenoage.utils.kernel.Range.range;


/**
 * {@link ScoreView} for PDF documents.
 *
 * @author Andreas Wenger
 */
public class PdfScoreView
		extends ScoreView {

	PdfRenderer renderer;
	int pagesCount;

	public PdfScoreView(String filepath, Size2i screenSizePx, float zoom)
			throws IOException {
		/* AssetFileDescriptor afd = androidPlatformUtils().getContext().getAssets().openFd("files/" + filepath); //TIDY
		ParcelFileDescriptor pfd = afd.getParcelFileDescriptor();
		renderer = new PdfRenderer(pfd); */
		//GOON: use assets instead of res again, but change PDF library. the current one is buggy! and set back to min api level 15
		//https://github.com/barteksc/PdfiumAndroid
		Resources res = androidPlatformUtils().getContext().getResources();
		ParcelFileDescriptor pfd = res.openRawResourceFd(R.raw.pdfsample).getParcelFileDescriptor();
		renderer = new PdfRenderer(pfd);
		pagesCount = renderer.getPageCount();
		updateScreen(screenSizePx, zoom);
	}

	@Override public void updateScreen(Size2i screenSizePx, float zoom) {
	}

	@Override public int getPagesCount() {
		return pagesCount;
	}

	@Override public PageView getPage(int pageIndex) {
		Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
		PdfRenderer.Page page = renderer.openPage(pageIndex);
		page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
		page.close();
		return new PageView(bitmap);
	}

}
