package com.xenoage.zong.renderer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

import com.xenoage.utils.graphics.Units;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;


/**
 * This class paints a page of a {@link Layout} into a {@link Bitmap}
 * using the {@link AndroidPageLayoutRenderer}.
 * 
 * @author Andreas Wenger
 */
public class AndroidBitmapPageRenderer
{
	
	/**
	 * Returns a {@link Bitmap} with the given page of the given {@link Layout}
	 * which is rendered at the given zoom level.
	 */
	public static Bitmap paint(Layout layout, int pageIndex, float zoom)
	{
		Page page = layout.pages.get(pageIndex);
		Size2f pageSize = page.format.size;
		
		int width = Units.mmToPxInt(pageSize.width, zoom);
		int height = Units.mmToPxInt(pageSize.height, zoom);
		
		Bitmap bitmap =  Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawARGB(255, 255, 255, 255); //GOON: necessary? and really 8888 bitmap?

  	canvas.scale(zoom, zoom);
  	AndroidPageLayoutRenderer renderer = AndroidPageLayoutRenderer.getInstance();
  	renderer.paint(layout, pageIndex, new AndroidCanvas(canvas, pageSize,
			CanvasFormat.Bitmap, CanvasDecoration.None, CanvasIntegrity.Perfect), zoom);
  	
		return bitmap;
	}

}
