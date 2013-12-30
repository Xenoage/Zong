package com.xenoage.zong.desktop.renderer;

import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.zong.desktop.renderer.canvas.AwtCanvas;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.frames.AWTFrameRenderer;

/**
 * This class renders a {@link Layout} on an {@link AwtCanvas}.
 *
 * @author Andreas Wenger
 */
public class AwtPageLayoutRenderer {

	private static AwtPageLayoutRenderer instance = null;


	/**
	 * Gets the page layout renderer for printing.
	 */
	public static AwtPageLayoutRenderer getInstance() {
		if (instance == null)
			instance = new AwtPageLayoutRenderer();
		return instance;
	}

	private AwtPageLayoutRenderer() {
	}

	/**
	 * Paints the given page of the given {@link Layout} on the given {@link AwtCanvas}
	 * with no offset and a scaling of 1.
	 */
	public void paint(Layout layout, int pageIndex, AwtCanvas canvas) {
		//draw page
		Page page = layout.getPages().get(pageIndex);
		//draw frames
		for (Frame frame : page.getFrames()) {
			AWTFrameRenderer.getInstance().paintAny(frame, layout, canvas,
				new RendererArgs(1, 1, new Point2i(0, 0), layout.getDefaults().getSymbolPool(), null));
		}
	}

}
