package com.xenoage.zong.renderer.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.renderer.LayoutRenderer;
import com.xenoage.zong.renderer.awt.canvas.AwtCanvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;

/**
 * Like {@link LayoutRenderer}, but more AWT specific rendering methods.
 *
 * @author Andreas Wenger
 */
public class AwtLayoutRenderer {
	
	/**
	 * Returns a {@link BufferedImage} with the given page of the given {@link Layout}
	 * which is rendered at the given zoom level.
	 */
	public static BufferedImage paintToImage(Layout layout, int pageIndex, float zoom) {
		Page page = layout.getPages().get(pageIndex);
		Size2f pageSize = page.getFormat().getSize();

		int width = Units.mmToPxInt(pageSize.width, zoom);
		int height = Units.mmToPxInt(pageSize.height, zoom);
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);

		g2d.scale(zoom, zoom);
		LayoutRenderer.paintToCanvas(layout, pageIndex, new AwtCanvas(g2d, pageSize, CanvasFormat.Raster,
			CanvasDecoration.Interactive, CanvasIntegrity.Perfect));
		g2d.dispose();

		return img;
	}

}
