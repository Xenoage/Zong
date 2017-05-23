package com.xenoage.zong.android.view;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
	private Size2i screenSizePx;

	public PdfScoreView(String filepath, Size2i screenSizePx, float zoom)
			throws IOException {

		String FILENAME = "temp.pdf";
		Context context = androidPlatformUtils().getContext();
		File file = new File(context.getCacheDir(), FILENAME);
		if (!file.exists()) {
			// Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
			// the cache directory.
			InputStream asset = context.getAssets().open("files/Blockbladdl-Boarischer.pdf");
			FileOutputStream output = new FileOutputStream(file);
			final byte[] buffer = new byte[1024];
			int size;
			while ((size = asset.read(buffer)) != -1) {
				output.write(buffer, 0, size);
			}
			asset.close();
			output.close();
		}
		ParcelFileDescriptor mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

		renderer = new PdfRenderer(mFileDescriptor);
		pagesCount = renderer.getPageCount();
		updateScreen(screenSizePx, zoom);
	}

	@Override public void updateScreen(Size2i screenSizePx, float zoom) {
		this.screenSizePx = screenSizePx;
	}

	@Override public int getPagesCount() {
		return pagesCount;
	}

	@Override public PageView getPage(int pageIndex) {
		Bitmap bitmap = Bitmap.createBitmap(screenSizePx.width, screenSizePx.height, Bitmap.Config.ARGB_8888);
		PdfRenderer.Page page = renderer.openPage(pageIndex);
		page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
		page.close();
		return new PageView(bitmap);
	}

}
