package com.xenoage.zong.android.renderer;

import static com.xenoage.zong.android.renderer.AndroidPageLayoutRenderer.androidPageLayoutRenderer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.android.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;

/**
 * This class paints a page of a {@link Layout} into a {@link Bitmap}
 * using the {@link AndroidPageLayoutRenderer}.
 * 
 * @author Andreas Wenger
 */
public class AndroidBitmapPageRenderer {

	/**
	 * Returns a {@link Bitmap} with the given page of the given {@link Layout}
	 * which is rendered at the given zoom level.
	 */
	public static Bitmap paint(Layout layout, int pageIndex, float zoom) {
		Page page = layout.getPages().get(pageIndex);
		Size2f pageSize = page.getFormat().getSize();

		int width = Units.mmToPxInt(pageSize.width, zoom);
		int height = Units.mmToPxInt(pageSize.height, zoom);

		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawARGB(255, 255, 255, 255); //TODO: necessary? and really 8888 bitmap?

		canvas.scale(zoom, zoom);
		androidPageLayoutRenderer.paint(layout, pageIndex, new AndroidCanvas(canvas, pageSize,
			CanvasFormat.Raster, CanvasDecoration.None, CanvasIntegrity.Perfect), zoom);

		return bitmap;
	}

}
