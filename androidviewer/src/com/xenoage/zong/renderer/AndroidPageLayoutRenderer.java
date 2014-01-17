package com.xenoage.zong.renderer;

import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.renderer.canvas.AWTCanvas;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.renderer.frames.AWTFrameRenderer;
import com.xenoage.zong.renderer.frames.AndroidFrameRenderer;


/**
 * This class renders a {@link Layout} on an {@link AndroidCanvas}.
 * 
 * GOON: merge with {@link AWTPageLayoutRenderer}
 *
 * @author Andreas Wenger
 */
public class AndroidPageLayoutRenderer
{

	private static AndroidPageLayoutRenderer instance = null;


	/**
	 * Gets the page layout renderer for printing.
	 */
	public static AndroidPageLayoutRenderer getInstance()
	{
		if (instance == null)
			instance = new AndroidPageLayoutRenderer();
		return instance;
	}


	private AndroidPageLayoutRenderer()
	{
	}


	/**
	 * Paints the given page of the given {@link Layout} on the given {@link AndroidCanvas}
	 * with no offset and the given scaling.
	 */
	public void paint(Layout layout, int pageIndex, AndroidCanvas canvas, float scaling)
	{
		//draw page
		Page page = layout.pages.get(pageIndex);
		//draw frames
		for (Frame frame : page.frames) {
			AndroidFrameRenderer.getInstance().paintAny(frame, layout, canvas,
				new RendererArgs(scaling, scaling, new Point2i(0, 0), layout.defaults.getSymbolPool(), null));
		}
	}

}
