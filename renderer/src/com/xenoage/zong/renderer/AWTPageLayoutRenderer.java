package com.xenoage.zong.renderer;

import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.zong.desktop.renderer.canvas.AWTCanvas;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.renderer.frames.AWTFrameRenderer;


/**
 * This class renders a {@link Layout} on an {@link AWTCanvas}.
 *
 * @author Andreas Wenger
 */
public class AWTPageLayoutRenderer
{

	private static AWTPageLayoutRenderer instance = null;


	/**
	 * Gets the page layout renderer for printing.
	 */
	public static AWTPageLayoutRenderer getInstance()
	{
		if (instance == null)
			instance = new AWTPageLayoutRenderer();
		return instance;
	}


	private AWTPageLayoutRenderer()
	{
	}


	/**
	 * Paints the given page of the given {@link Layout} on the given {@link AWTCanvas}
	 * with no offset and a scaling of 1.
	 */
	public void paint(Layout layout, int pageIndex, AWTCanvas canvas)
	{
		//draw page
		Page page = layout.pages.get(pageIndex);
		//draw frames
		for (Frame frame : page.frames) {
			AWTFrameRenderer.getInstance().paintAny(frame, layout, canvas,
				new RendererArgs(1, 1, new Point2i(0, 0), layout.defaults.getSymbolPool(), null));
		}
	}

}
