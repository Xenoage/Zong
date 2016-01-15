package com.xenoage.zong.android.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.android.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.renderer.LayoutRenderer;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;

/**
 * Like {@link LayoutRenderer}, but more Android specific rendering methods.
 *
 * @author Andreas Wenger
 */
public class AndroidLayoutRenderer {

    public static AndroidLayoutRenderer androidLayoutRenderer = new AndroidLayoutRenderer();


    /**
     * Returns a {@link Bitmap} with the given page of the given {@link Layout}
     * which is rendered at the given zoom level.
     */
    public Bitmap paint(Layout layout, int pageIndex, float zoom) {
        Page page = layout.getPages().get(pageIndex);
        Size2f pageSize = page.getFormat().getSize();

        int width = Units.mmToPxInt(pageSize.width, zoom);
        int height = Units.mmToPxInt(pageSize.height, zoom);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(255, 255, 255, 255); //TODO: necessary? and really 8888 bitmap?

        canvas.scale(zoom, zoom);
        paint(layout, pageIndex, new AndroidCanvas(canvas, pageSize,
                CanvasFormat.Raster, CanvasDecoration.None, CanvasIntegrity.Perfect), zoom);

        return bitmap;
    }

    /**
     * Paints the given page of the given {@link Layout} on the given {@link AndroidCanvas}
     * with no offset and the given scaling.
     */
    public void paint(Layout layout, int pageIndex, AndroidCanvas canvas, float scaling) {
        canvas.transformSave();
        canvas.transformScale(scaling, scaling);
        Page page = layout.getPages().get(pageIndex);
        LayoutRenderer.paintToCanvas(layout, pageIndex, canvas);
        canvas.transformRestore();
    }

}
