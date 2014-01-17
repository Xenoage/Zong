package com.xenoage.zong.renderer;

import static com.xenoage.zong.renderer.frames.AndroidFramesRenderer.androidFramesRenderer;

import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.renderer.canvas.Canvas;

/**
 * This class renders a {@link Layout} on an Android {@link Canvas}.
 *
 * @author Andreas Wenger
 */
public class AndroidPageLayoutRenderer {

	public static AndroidPageLayoutRenderer androidPageLayoutRenderer = new AndroidPageLayoutRenderer();


	/**
	 * Paints the given page of the given {@link Layout} on the given {@link AndroidCanvas}
	 * with no offset and the given scaling.
	 */
	public void paint(Layout layout, int pageIndex, AndroidCanvas canvas, float scaling) {
		//draw page
		Page page = layout.getPages().get(pageIndex);
		//draw frames
		for (Frame frame : page.getFrames()) {
			androidFramesRenderer.paintAny(frame, canvas,
				new RendererArgs(scaling, scaling, new Point2i(0, 0), layout.getDefaults().getSymbolPool(), null));
		}
	}

}
