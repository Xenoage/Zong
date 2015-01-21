package com.xenoage.zong.renderer.javafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.renderer.LayoutRenderer;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.javafx.canvas.JfxCanvas;

/**
 * Like {@link LayoutRenderer}, but more JavaFx specific rendering methods.
 *
 * @author Andreas Wenger
 */
public class JfxLayoutRenderer {
	
	/**
	 * Returns a {@link WritableImage} with the given page of the given {@link Layout}
	 * which is rendered at the given zoom level.
	 */
	public static WritableImage paintToImage(Layout layout, int pageIndex, float zoom) {
		Page page = layout.getPages().get(pageIndex);
		Size2f pageSize = page.getFormat().getSize();

		int width = Units.mmToPxInt(pageSize.width, zoom);
		int height = Units.mmToPxInt(pageSize.height, zoom);
		WritableImage wim = new WritableImage(width, height);

    Canvas jfxCanvas = new Canvas(width, height);
    GraphicsContext context = jfxCanvas.getGraphicsContext2D();

    context.setFill(Color.WHITE);
    context.fillRect(0, 0, width, height);

    context.scale(zoom, zoom);
		LayoutRenderer.paintToCanvas(layout, pageIndex, new JfxCanvas(context, pageSize, CanvasFormat.Raster,
			CanvasDecoration.Interactive, CanvasIntegrity.Perfect));
		jfxCanvas.snapshot(null, wim);

		return wim;
	}

}
