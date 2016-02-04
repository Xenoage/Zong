package com.xenoage.zong.renderer;

import static com.xenoage.utils.math.geom.Point2i.origin;

import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.frames.FramesRenderer;

/**
 * This class renders a {@link Layout}.
 *
 * @author Andreas Wenger
 */
public class LayoutRenderer {

	/**
	 * Paints the given page of the given {@link Layout} on the given {@link Canvas}
	 * with no offset and a scaling of 1.
	 */
	public static void paintToCanvas(Layout layout, int pageIndex, Canvas canvas) {
		paintToCanvas(layout, pageIndex, 1, origin, canvas);
	}
	
	/**
	 * Paints a page on a {@link Canvas}.
	 */
	public static void paintToCanvas(Layout layout, int pageIndex,
		float scaling, Point2i offset, Canvas canvas) {
		//draw page
		Page page = layout.getPages().get(pageIndex);
		//draw frames
		for (Frame frame : page.getFrames()) {
			FramesRenderer.paintAny(frame, canvas,
				new RendererArgs(scaling, scaling, offset, layout.getDefaults().getSymbolPool()));
		}
	}

}
